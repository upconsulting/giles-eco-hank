package com.upconsulting.gilesecosystem.hank.web.pages;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Controller
public class LineController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IFileStorageManager storageManager;
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/line/{page}/{filename:.+}")
    public ResponseEntity<String> getLineImage(@PathVariable String fileId, @PathVariable String runId, @PathVariable String page, @PathVariable String filename, Principal principal, HttpServletResponse response) {
        
        String runFolder = storageManager.getAndCreateStoragePath(principal.getName(), fileId, runId);
        File imageFile = new File(runFolder + File.separator + String.format ("%04d", new Integer(page)) + File.separator + filename);
        if (!imageFile.exists()) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        
        byte[] content;
        try {
            content = storageManager.getFileContentFromUrl(imageFile.toURI().toURL());
        } catch (IOException e1) {
            logger.error("Could not load image.", e1);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setContentLength(content.length);
        response.setHeader("Content-disposition", "filename=\"" + filename + "\""); 
        try {
            response.getOutputStream().write(content);
            response.getOutputStream().close();
        } catch (IOException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
