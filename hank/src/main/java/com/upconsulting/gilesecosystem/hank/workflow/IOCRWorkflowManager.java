package com.upconsulting.gilesecosystem.hank.workflow;

import java.io.IOException;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

public interface IOCRWorkflowManager {

    public abstract IOCRRun startOCR(IImageFile file, String modelId)
            throws FileStorageException, IOException, UnstorableObjectException,
            DockerConnectionException;

}