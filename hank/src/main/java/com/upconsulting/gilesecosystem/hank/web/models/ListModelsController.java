package com.upconsulting.gilesecosystem.hank.web.models;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.upconsulting.gilesecosystem.hank.service.IModelManager;

@Controller
public class ListModelsController {
    
    @Autowired
    private IModelManager modelManager;

    @RequestMapping(value = "/models")
    public String showModels(Model model, Principal principal, @RequestParam(defaultValue = "0") String start, @RequestParam(defaultValue = "20") String numberOfResults) {
        model.addAttribute("models", modelManager.getModels(principal.getName(), Integer.parseInt(start), Integer.parseInt(numberOfResults)));
        return "models";
    }
}
