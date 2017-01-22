package com.upconsulting.gilesecosystem.hank.web.models;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.web.forms.ModelForm;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Controller
public class UploadModelController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IModelManager modelManager;

    @RequestMapping(value = "/models/upload")
    public String showUploadPage(Model model) {

        model.addAttribute("modelForm", new ModelForm());

        return "models/upload";
    }

    @RequestMapping(value = "/models/upload", method = RequestMethod.POST)
    public String uploadModel(@ModelAttribute ModelForm modelForm,
            @RequestParam("modelfile") MultipartFile modelfile, Principal principal, RedirectAttributes redirectAttrs) {

        if (modelfile != null) {
            try {
                modelManager.createModel(principal.getName(),
                        modelfile.getOriginalFilename(), modelForm.getTitle(),
                        modelForm.getDescription(), modelfile.getBytes());
            } catch (FileStorageException | IOException e) {
                logger.error("Error storing file.", e);
                redirectAttrs.addFlashAttribute("show_alert", true);
                redirectAttrs.addFlashAttribute("alert_type", "danger");
                redirectAttrs.addFlashAttribute("alert_msg", "File could not be uploaded.");
            }
        }
        
        redirectAttrs.addFlashAttribute("show_alert", true);
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Model was successfully stored.");
        return "redirect:/models/upload";
    }
}
