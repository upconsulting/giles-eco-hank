package com.upconsulting.gilesecosystem.hank.web.pages;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

@Controller
public class ShowLinesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
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
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/page/{pagenr}/lines")
    public String showLines(Model model, @PathVariable String fileId, @PathVariable String runId, @PathVariable String pagenr) throws ImageFileDoesNotExistException {
        List<IPage> pages = runManager.getPages(runId, null);
        IPage page = pages.stream().filter(p -> p.getPage() ==  new Integer(pagenr)).findFirst().get();
        
        model.addAttribute("imageId", fileId);
        model.addAttribute("page", page);
        return "files/image/page";
    }
}
