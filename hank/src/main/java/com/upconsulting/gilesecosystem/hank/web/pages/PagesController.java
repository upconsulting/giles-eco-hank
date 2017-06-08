package com.upconsulting.gilesecosystem.hank.web.pages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

@Controller
public class PagesController {
    
    @Autowired
    private IOCRRunManager runManager;
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/pages")
    public String showPages(Model model, @PathVariable String fileId, @PathVariable String runId) throws ImageFileDoesNotExistException {
        model.addAttribute("imageId", fileId);
        model.addAttribute("runId", runId);
        
        List<IPage> pages = runManager.getPages(runId, null);
        model.addAttribute("pages", pages);
        
        return "files/image/pages";
    }
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/pages", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPages(@PathVariable String fileId, @PathVariable String runId) {
        List<IPage> pages;
        try {
            pages = runManager.getPages(runId, null);
        } catch (ImageFileDoesNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ResponseEntity<String>(mapper.writeValueAsString(pages), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
