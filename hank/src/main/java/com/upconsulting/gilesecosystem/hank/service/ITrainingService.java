package com.upconsulting.gilesecosystem.hank.service;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.TrainingException;

public interface ITrainingService {

    public abstract void trainModel(String runId) throws ImageFileDoesNotExistException, TrainingException ;

}