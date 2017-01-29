package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.upconsulting.gilesecosystem.hank.db.impl.ImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.StepStatus;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus;
import com.upconsulting.gilesecosystem.hank.workflow.IOctopusBridge;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Transactional
@Service
public class OCRWorkflowManager {
    
    @Autowired
    private IFileStorageManager fileStorageManager;
    
    @Autowired
    private IModelManager modelManager;
    
    @Autowired
    private ImageFileDBClient imageFileDBClient;
    
    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IOctopusBridge octopusBridge;
    
    public List<ImageFile> startOCR(String username, String modelId, MultipartFile[] files) throws FileStorageException, IOException, UnstorableObjectException, DockerConnectionException {
        
        List<ImageFile> imageFiles = new ArrayList<ImageFile>();
        for (MultipartFile f : files) {
            ImageFile file = new ImageFile(f.getOriginalFilename());
            file.setId(imageFileDBClient.generateId());
            file.setUsername(username);
           
            byte[] bytes = f.getBytes();
            fileStorageManager.saveFile(username, file.getId(), null, file.getFilename(), bytes);
            
            String contentType = null;
            
            if (bytes != null) {
               Tika tika = new Tika();
               contentType = tika.detect(bytes);
            }
            
            if (contentType == null) {
                contentType = f.getContentType();
            }
            
            file.setContentType(contentType);
            file.setSize(f.getSize());
            
            imageFiles.add(file);
            
            IOCRRun runInfo = new OCRRun();
            runInfo.setModel(modelManager.getModel(modelId));
            runInfo.setDate(LocalDateTime.now());
            runManager.saveOCRRun(runInfo);
            
            file.getOcrRuns().add(runInfo);
            imageFileDBClient.saveImageFile(file);
            
            
            if (runNlb(file, runInfo) == null) {
                continue;
            }
            
            if (runLayoutAnalysis(file, runInfo) == null) {
                continue;
            }
            
            if (runLineRecognition(file, runInfo) == null) {
                continue;
            }
        }
        
        return imageFiles;
    }
    
    private IImageFile runNlb(IImageFile file, IOCRRun runInfo) throws DockerConnectionException, UnstorableObjectException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.BINARIZATION));
        
        boolean success = octopusBridge.runNlbin(file);
        if (success) {
            file.setStatus(WorkflowStatus.BINARIZED);
            runInfo.setStepStatus(StepType.BINARIZATION, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.BINARIZATION, StepStatus.FAILED);
            return null;
        }
        
        runManager.saveOCRRun(runInfo);
        return imageFileDBClient.saveImageFile(file);
    }
    
    private IImageFile runLayoutAnalysis(IImageFile file, IOCRRun runInfo) throws DockerConnectionException, UnstorableObjectException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.LAYOUT_ANALYSIS));
        
        boolean success = octopusBridge.runPageLayoutAnalysis(file);
        if (success) {
            file.setStatus(WorkflowStatus.LAYOUT_ANALYZED);
            runInfo.setStepStatus(StepType.LAYOUT_ANALYSIS, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.LAYOUT_ANALYSIS, StepStatus.FAILED);
            return null;
        }
        
        runManager.saveOCRRun(runInfo);
        return imageFileDBClient.saveImageFile(file);
    }
    
    private IImageFile runLineRecognition(IImageFile file, IOCRRun runInfo) throws DockerConnectionException, UnstorableObjectException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.LINE_RECOGNITION));
        
        boolean success = octopusBridge.runLineRecognition(file, runInfo.getModel());
        if (success) {
            file.setStatus(WorkflowStatus.LINES_RECOGNIZED);
            runInfo.setStepStatus(StepType.LINE_RECOGNITION, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.LINE_RECOGNITION, StepStatus.FAILED);
            return null;
        }
        
        runManager.saveOCRRun(runInfo);
        return imageFileDBClient.saveImageFile(file);
    }
}
