package com.upconsulting.gilesecosystem.hank.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.objectdb.o.INT;
import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Controller
public class ShowLinesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IImageFileManager imageManager; 
    
    @Autowired
    private IFileStorageManager storageManager;
    

    @RequestMapping(value = "/files/image/{fileId}/{runId}/pages")
    public String showPages(Model model, @PathVariable String fileId, @PathVariable String runId) throws ImageFileDoesNotExistException {
        model.addAttribute("imageId", fileId);
        model.addAttribute("runId", runId);
        
        List<IPage> pages = runManager.getPages(runId);
        model.addAttribute("pages", pages);
        
        return "files/image/pages";
    }
    
    @RequestMapping(value = "/files/image/{fileId}/{runId}/page/{pagenr}/lines")
    public String showLines(Model model, @PathVariable String fileId, @PathVariable String runId, @PathVariable String pagenr) throws ImageFileDoesNotExistException {
        List<IPage> pages = runManager.getPages(runId);
        IPage page = pages.stream().filter(p -> p.getPage() ==  new Integer(pagenr)).findFirst().get();
        
        model.addAttribute("imageId", fileId);
        model.addAttribute("page", page);
        return "files/image/page";
    }
    
    @RequestMapping(value = "/files/image/{fileId}/{runId}/line/{page}/{filename:.+}")
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
