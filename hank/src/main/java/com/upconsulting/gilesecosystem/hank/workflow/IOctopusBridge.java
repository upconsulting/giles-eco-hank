package com.upconsulting.gilesecosystem.hank.workflow;

import java.util.concurrent.Future;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITraining;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

public interface IOctopusBridge {

    public abstract boolean runNlbin(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException;

    public abstract boolean runPageLayoutAnalysis(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException;

    public abstract boolean runLineRecognition(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException;

    public abstract String runHOCROutput(IImageFile imageFile, IOCRRun run, String outputFilename)
            throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException;

    Future<String> runTraining(ITraining training, IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException;

}