package com.upconsulting.gilesecosystem.hank.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.workflow.IOCRWorkflowManager;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Controller
public class OCRRunController {
    
    @Autowired
    private IImageFileManager imageManager; 
    
    @Autowired
    private IOCRWorkflowManager workflowManager;
    

    @RequestMapping(value = "/files/image/{id}/ocr/run", method = RequestMethod.POST)
    public String runOCR(@RequestParam String modelId, @PathVariable String id) throws FileStorageException, IOException, UnstorableObjectException, DockerConnectionException {
        if (id == null || modelId == null) {
            throw new RuntimeException("Model id null or image id null.");
        }
        
        IImageFile file = imageManager.getImageFile(id);
        IOCRRun run = workflowManager.startOCR(file, modelId);
        file.getOcrRuns().add(run);
        imageManager.storeOrUpdateImageFile(file);        
        
        return "redirect:/files/image/" + id;
    }
}
