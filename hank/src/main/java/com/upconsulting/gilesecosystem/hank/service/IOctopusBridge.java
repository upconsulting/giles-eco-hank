package com.upconsulting.gilesecosystem.hank.service;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public interface IOctopusBridge {

    public abstract boolean runNlbin(ImageFile imageFile)
            throws DockerConnectionException;

}