package com.upconsulting.gilesecosystem.hank.service;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;

public interface IModelManager {

    public abstract IOCRModel createModel(String username, String filename, String title,
            String description, byte[] content) throws FileStorageException;

    public abstract List<IOCRModel> getModels(String username, int start, int numberOfResults);

}