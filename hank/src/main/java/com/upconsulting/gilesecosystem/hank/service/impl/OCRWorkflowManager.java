package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.upconsulting.gilesecosystem.hank.db.impl.ImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.service.IOctopusBridge;

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
            imageFileDBClient.saveImageFile(file);
            
            imageFiles.add(file);
            
            IOCRModel model = modelManager.getModel(modelId);
            
            
            boolean success = octopusBridge.runNlbin(file);
            if (success) {
                file.setStatus(WorkflowStatus.BINARIZED);
            } else {
                file.setStatus(WorkflowStatus.ERROR);
                continue;
            }
            imageFileDBClient.saveImageFile(file);
            
            success = octopusBridge.runPageLayoutAnalysis(file);
            if (success) {
                file.setStatus(WorkflowStatus.LAYOUT_ANALYZED);
            } else {
                file.setStatus(WorkflowStatus.ERROR);
                continue;
            }
            imageFileDBClient.saveImageFile(file);
            
            success = octopusBridge.runLineRecognition(file, model);
            if (success) {
                file.setStatus(WorkflowStatus.LINES_RECOGNIZED);
            } else {
                file.setStatus(WorkflowStatus.ERROR);
                continue;
            }
        }
        
        return imageFiles;
    }
}
