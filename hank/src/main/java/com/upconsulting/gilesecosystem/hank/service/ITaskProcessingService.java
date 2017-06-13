package com.upconsulting.gilesecosystem.hank.service;

import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.ITask;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

public interface ITaskProcessingService {

    void logMessages(String msg, ITask task) throws UnknownObjectTypeException, UnstorableObjectException;

    void finishTask(ITask task, boolean success)
            throws UnstorableObjectException, UnknownObjectTypeException;

}