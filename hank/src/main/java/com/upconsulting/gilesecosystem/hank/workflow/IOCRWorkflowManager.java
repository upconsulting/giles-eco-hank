package com.upconsulting.gilesecosystem.hank.workflow;

import java.io.IOException;
import java.time.LocalDateTime;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRRun;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

public interface IOCRWorkflowManager {

    public abstract IOCRRun startOCR(IImageFile file, String modelId)
            throws FileStorageException, IOException, UnstorableObjectException,
            DockerConnectionException, UnknownObjectTypeException;

    IOCRRun startOCRWithTrainingModel(IImageFile file, ITraining training)
            throws DockerConnectionException, UnstorableObjectException,
            UnknownObjectTypeException;

}