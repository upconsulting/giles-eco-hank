package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.StepStatus;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus;
import com.upconsulting.gilesecosystem.hank.workflow.IOCRWorkflowManager;
import com.upconsulting.gilesecosystem.hank.workflow.IOctopusBridge;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Transactional
@Service
public class OCRWorkflowManager implements IOCRWorkflowManager {
    
    public final static String TEXT_FILENAME = "text";

    @Autowired
    private IOctopusBridge octopusBridge;

    @Autowired
    private IModelManager modelManager;

    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IImageFileManager imageFileManager;
    

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.workflow.impl.IOCRWorkflowManager#startOCR(com.upconsulting.gilesecosystem.hank.model.IImageFile, java.lang.String)
     */
    @Override
    public IOCRRun startOCR(IImageFile file, String modelId) throws FileStorageException,
            IOException, UnstorableObjectException, DockerConnectionException, UnknownObjectTypeException {

        IOCRRun runInfo = new OCRRun();
        runInfo.setModel(modelManager.getModel(modelId));
        runInfo.setDate(LocalDateTime.now());
        runManager.saveOCRRun(runInfo);

        if (runNlb(file, runInfo) == null) {
            return runInfo;
        }

        if (runLayoutAnalysis(file, runInfo) == null) {
            return runInfo;
        }

        if (runLineRecognition(file, runInfo) == null) {
            return runInfo;
        }
        
        runHOCROutput(file, runInfo);

        return runInfo;      
    }

    private IImageFile runNlb(IImageFile file, IOCRRun runInfo)
            throws DockerConnectionException, UnstorableObjectException, UnknownObjectTypeException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.BINARIZATION));

        boolean success = octopusBridge.runNlbin(file, runInfo);
        if (success) {
            file.setStatus(WorkflowStatus.BINARIZED);
            runInfo.setStepStatus(StepType.BINARIZATION, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.BINARIZATION, StepStatus.FAILED);
            return null;
        }

        runManager.saveOCRRun(runInfo);
        return imageFileManager.storeOrUpdateImageFile(file);
    }

    private IImageFile runLayoutAnalysis(IImageFile file, IOCRRun runInfo)
            throws DockerConnectionException, UnstorableObjectException, UnknownObjectTypeException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.LAYOUT_ANALYSIS));

        boolean success = octopusBridge.runPageLayoutAnalysis(file, runInfo);
        if (success) {
            file.setStatus(WorkflowStatus.LAYOUT_ANALYZED);
            runInfo.setStepStatus(StepType.LAYOUT_ANALYSIS, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.LAYOUT_ANALYSIS, StepStatus.FAILED);
            return null;
        }

        runManager.saveOCRRun(runInfo);
        return imageFileManager.storeOrUpdateImageFile(file);
    }

    private IImageFile runLineRecognition(IImageFile file, IOCRRun runInfo)
            throws DockerConnectionException, UnstorableObjectException, UnknownObjectTypeException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.LINE_RECOGNITION));

        boolean success = octopusBridge.runLineRecognition(file, runInfo);
        if (success) {
            file.setStatus(WorkflowStatus.LINES_RECOGNIZED);
            runInfo.setStepStatus(StepType.LINE_RECOGNITION, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.LINE_RECOGNITION, StepStatus.FAILED);
            return null;
        }

        runManager.saveOCRRun(runInfo);
        return imageFileManager.storeOrUpdateImageFile(file);
    }
    
    private IImageFile runHOCROutput(IImageFile file, IOCRRun runInfo) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        runInfo.getSteps().add(runManager.createRunStep(StepType.HOCR_GENERATION));

        String filename = octopusBridge.runHOCROutput(file, runInfo, TEXT_FILENAME);
        if (filename != null) {
            file.setStatus(WorkflowStatus.HOCR_GENERATED);
            runInfo.setHocrFile(filename);
            runInfo.setStepStatus(StepType.HOCR_GENERATION, StepStatus.SUCCEEDED);
        } else {
            file.setStatus(WorkflowStatus.ERROR);
            runInfo.setStepStatus(StepType.HOCR_GENERATION, StepStatus.FAILED);
            return null;
        }

        runManager.saveOCRRun(runInfo);
        return imageFileManager.storeOrUpdateImageFile(file);
    }
}
