package com.upconsulting.gilesecosystem.hank.service;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;

public interface IModelManager {

    public abstract IOCRModel createModel(String username, String filename, String title,
            String description, byte[] content) throws FileStorageException;

}