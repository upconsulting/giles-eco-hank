package com.upconsulting.gilesecosystem.hank.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.ZipFileGenerationException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Controller
public class RunDownloadController {

    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IImageFileManager fileManager;
    
    @Autowired
    private IFileStorageManager storageManager; 
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/download")
    public ResponseEntity<String> downloadOCRRun(@PathVariable String fileId, @PathVariable String runId, HttpServletResponse response, Principal principal) throws ImageFileDoesNotExistException, ZipFileGenerationException {
        IOCRRun run = runManager.getRun(runId);
        
        if (run == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
         
        IImageFile imageFile = fileManager.getByRunId(runId);
        if (imageFile == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        if (!imageFile.getUsername().equals(principal.getName())) {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
        
        
        File zipFile = runManager.zipOCRFolder(runId);
        
        byte[] content = storageManager.getFileContent(principal.getName(), imageFile.getId(), null, zipFile.getName());
        response.setHeader("Content-disposition", "filename=\"" + zipFile.getName() + "\""); 
        response.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(zipFile.length());
        
        try {
            response.getOutputStream().write(content);
            response.getOutputStream().close();
        } catch (IOException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
