package com.upconsulting.gilesecosystem.hank.service;

import java.io.File;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.util.Properties;

public interface IOctopusBridge {

    public abstract boolean runNlbin(ImageFile imageFile)
            throws DockerConnectionException;

    public abstract boolean runPageLayoutAnalysis(ImageFile imageFile)
            throws DockerConnectionException;

    public abstract boolean runLineRecognition(ImageFile imageFile, IOCRModel model)
            throws DockerConnectionException;

}