package com.upconsulting.gilesecosystem.hank.workflow;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.util.Properties;

public interface IOctopusBridge {

    public abstract boolean runNlbin(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException;

    public abstract boolean runPageLayoutAnalysis(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException;

    public abstract boolean runLineRecognition(IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException;

    public abstract String runHOCROutput(IImageFile imageFile, IOCRRun run, String outputFilename)
            throws DockerConnectionException;

    String runTraining(ITraining training, IImageFile imageFile, IOCRRun run)
            throws DockerConnectionException;

}