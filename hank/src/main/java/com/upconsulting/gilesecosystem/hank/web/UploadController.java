package com.upconsulting.gilesecosystem.hank.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.workflow.IUploadManager;
import com.upconsulting.gilesecosystem.hank.workflow.impl.OCRWorkflowManager;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Controller
public class UploadController {

    private static Logger logger = LoggerFactory
            .getLogger(UploadController.class);
    
    @Autowired
    private IUploadManager uploadManager;
    
    @Autowired
    private IModelManager modelManager;

    @RequestMapping(value = "/files/upload", method = RequestMethod.GET)
    public String showUploadPage(Principal principal, Model model) {
        model.addAttribute("models", modelManager.getModels(principal.getName(), 0, 40));
        return "files/upload";
    }

    @RequestMapping(value = "/files/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFiles(Principal principal, @RequestParam("selectedModel") String selectedModel,
            @RequestParam("file") MultipartFile[] files,
            Locale locale) throws FileStorageException, IOException, UnstorableObjectException {

        List<IImageFile> imageFiles;
        try {
            imageFiles = uploadManager.processFiles(principal.getName(), selectedModel, files);
        } catch (DockerConnectionException e) {
            return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode filesNode = root.putArray("files");

        for (IImageFile file : imageFiles) {
            ObjectNode fileNode = mapper.createObjectNode();
            fileNode.put("name", file.getFilename());
            fileNode.put("uploadId", file.getUploadId());
            
            filesNode.add(fileNode);
        }
        
        return new ResponseEntity<String>(root.toString(), HttpStatus.OK);
    }
}
