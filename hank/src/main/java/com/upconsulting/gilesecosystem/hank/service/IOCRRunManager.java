package com.upconsulting.gilesecosystem.hank.service;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.model.IRunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;

public interface IOCRRunManager {

    public abstract IOCRRun saveOCRRun(IOCRRun run);

    public abstract IRunStep createRunStep(StepType type);

    public abstract IOCRRun getRun(String id);

    /**
     * Retrieves pages of the run identified by the given id, or, if a correction id
     * is provided, the pages of the specified correction.
     * 
     * @param runId Id of the run pages should be retrieved for.
     * @param correctionId Id of the correction that pages should be retrieved for; if null 
     * pages for specified run will be retrieved.
     * @return List of pages for the specified run or correction.
     * @throws ImageFileDoesNotExistException
     */
    public abstract List<IPage> getPages(String runId, String correctionId) throws ImageFileDoesNotExistException;

}