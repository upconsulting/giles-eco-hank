package com.upconsulting.gilesecosystem.hank.workflow;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRModel;

public interface IOctopusBridge {

    public abstract boolean runNlbin(IImageFile imageFile)
            throws DockerConnectionException;

    public abstract boolean runPageLayoutAnalysis(IImageFile imageFile)
            throws DockerConnectionException;

    public abstract boolean runLineRecognition(IImageFile imageFile, IOCRModel model)
            throws DockerConnectionException;

}