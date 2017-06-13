package com.upconsulting.gilesecosystem.hank.service;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.TrainingException;
import com.upconsulting.gilesecosystem.hank.model.ITraining;

public interface ITrainingService {

    public abstract void trainModel(String runId, int linesToTrain, int savingFreq, List<Integer> pageNumbers) throws ImageFileDoesNotExistException, TrainingException ;

    List<ITraining> getTrainings(String ocrRunId);

}