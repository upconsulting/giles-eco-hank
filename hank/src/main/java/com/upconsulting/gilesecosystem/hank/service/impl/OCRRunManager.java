package com.upconsulting.gilesecosystem.hank.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.db.IOCRRunDBClient;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IRunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.RunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Service
public class OCRRunManager implements IOCRRunManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IOCRRunDBClient runDBClient;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IOCRRunManager#saveOCRRun(com.upconsulting.gilesecosystem.hank.model.IOCRRun)
     */
    @Override
    public IOCRRun saveOCRRun(IOCRRun run) {
        if (run.getId() == null) {
            run.setId(runDBClient.generateId());
        }
        
        try {
            runDBClient.store(run);
        } catch (UnstorableObjectException e) {
            // should never happen since we're setting it above
            logger.error("Could not store OCR run.", e);
        }
        
        return run;
     }

    @Override
    public IRunStep createRunStep(StepType type) {
        IRunStep step = new RunStep();
        step.setStepType(type);
        step.setDate(LocalDateTime.now());
        return step;
    }
}
