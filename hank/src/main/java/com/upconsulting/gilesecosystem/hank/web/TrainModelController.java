package com.upconsulting.gilesecosystem.hank.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.TrainingException;
import com.upconsulting.gilesecosystem.hank.service.ITrainingService;

@Controller
public class TrainModelController {

    @Autowired
    private ITrainingService trainingService;
    
    @RequestMapping(value = "/files/image/{fileId:IMG[0-9a-zA-Z]+}/{runId:RUN[0-9a-zA-Z]+}/train", method = RequestMethod.POST)
    public String trainModel(@PathVariable String fileId, @PathVariable String runId) throws ImageFileDoesNotExistException, TrainingException {
        trainingService.trainModel(runId);
        
        return "redirect:/files/image/" + fileId;
    }
}
