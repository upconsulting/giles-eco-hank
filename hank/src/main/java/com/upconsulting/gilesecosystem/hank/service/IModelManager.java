package com.upconsulting.gilesecosystem.hank.service;

import java.io.File;
import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;

public interface IModelManager {

    public abstract IOCRModel createModel(String username, String filename, String title,
            String description, byte[] content) throws FileStorageException;

    public abstract List<IOCRModel> getModels(String username, int start, int numberOfResults);

    public abstract IOCRModel getModel(String id);

    byte[] getModelAsBytes(IOCRModel model);

}