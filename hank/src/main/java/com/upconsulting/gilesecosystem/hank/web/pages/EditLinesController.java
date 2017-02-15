package com.upconsulting.gilesecosystem.hank.web.pages;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.RunDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.service.ILineCorrectionManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.web.forms.LineCorrection;
import com.upconsulting.gilesecosystem.hank.web.forms.TextEditForm;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;

@Controller
public class EditLinesController {

    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private ILineCorrectionManager correctionManager;
    
    @RequestMapping(value = "/files/image/{fileId}/{runId}/page/{pagenr}/lines/edit")
    public String showLines(Model model, @PathVariable String fileId, @PathVariable String runId, @PathVariable String pagenr) throws ImageFileDoesNotExistException {
        List<IPage> pages = runManager.getPages(runId);
        IPage page = pages.stream().filter(p -> p.getPage() ==  new Integer(pagenr)).findFirst().get();
        
        model.addAttribute("imageId", fileId);
        model.addAttribute("page", page);
        
        TextEditForm form = new TextEditForm();
        form.setLineCorrections(new ArrayList<LineCorrection>());
        page.getLines().forEach(l -> form.getLineCorrections().add(new LineCorrection(l.getLineName(), l.getText(), l.getImageFilename())));
        
        model.addAttribute(form);
        return "files/image/page/edit";
    }
    
    @RequestMapping(value = "/files/image/{fileId}/{runId}/page/{pagenr}/lines/edit", method = RequestMethod.POST)
    public String saveCorrectedLines(@ModelAttribute TextEditForm form, @PathVariable String fileId, @PathVariable String runId, @PathVariable String pagenr, Principal principal) throws NumberFormatException, RunDoesNotExistException, FileStorageException, IOException {
        
        correctionManager.saveCorrectedLines(principal.getName(), fileId, runId, String.format ("%04d", new Integer(pagenr)), form.getLineCorrections());
        return "redirect:/files/image/" + fileId + "/" + runId + "/pages";
    }
    
}
