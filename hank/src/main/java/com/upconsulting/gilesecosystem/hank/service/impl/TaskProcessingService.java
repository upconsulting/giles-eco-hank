package com.upconsulting.gilesecosystem.hank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ITrainingDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITask;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.service.ITaskProcessingService;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Transactional
@Service
public class TaskProcessingService implements ITaskProcessingService {

    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private ITrainingDBClient trainingDbClient;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.ITaskProcessingService#logMessages(java.lang.String, com.upconsulting.gilesecosystem.hank.model.ITask)
     */
    @Override
    public void logMessages(String msg, ITask task) throws UnknownObjectTypeException, UnstorableObjectException {
        if (task.getLog() == null) {
            task.setLog("");
        }
        task.setLog(task.getLog() + "\n" + msg);
        
        saveTask(task);
    } 
    
    @Override
    public void finishTask(ITask task, boolean success) throws UnstorableObjectException, UnknownObjectTypeException {
        task.setDone(true);
        task.setSuccess(success);
        saveTask(task);
    }
    
    private void saveTask(ITask task)
            throws UnstorableObjectException, UnknownObjectTypeException {
        //FIXME: this needs to be improved!
        if (task instanceof ITraining) {
            trainingDbClient.update((ITraining) task);
        } else if (task instanceof IOCRRun) {
            runManager.saveOCRRun((IOCRRun) task);
        } else {
            throw new UnknownObjectTypeException("Can't store object of type: " + task.getClass());
        }
    }
}
