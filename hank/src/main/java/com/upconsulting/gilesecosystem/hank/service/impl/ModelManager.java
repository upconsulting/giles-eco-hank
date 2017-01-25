package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.IOCRModelDBClient;
import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRModel;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Transactional
@Service
public class ModelManager implements IModelManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final String MODEL_FOLDER = "models";

    @Autowired
    private IFileStorageManager fileStorageManager;
    
    @Autowired
    private IOCRModelDBClient modelDb;

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IModelManager#createModel(java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[])
     */
    @Override
    public IOCRModel createModel(String username, String filename, String title, String description, byte[] content) throws FileStorageException {
        IOCRModel ocrModel = new OCRModel();
        ocrModel.setTitle(title);
        ocrModel.setDescription(description);
        ocrModel.setFilename(filename);
        ocrModel.setUsername(username);
        ocrModel.setId(modelDb.generateId());
        
        fileStorageManager.saveFile(username, MODEL_FOLDER, ocrModel.getId(), filename, content);
        try {
            modelDb.store(ocrModel);
        } catch (UnstorableObjectException e) {
            // should not happen because we assign id
            logger.error("Could not store element.", e);
        }
        return ocrModel;
    }
    
    @Override
    public List<IOCRModel> getModels(String username, int start, int numberOfResults) {
        if (start < 0) {
            start = 0;
        }
        if (numberOfResults < 0) {
            numberOfResults = 20;
        }
        return modelDb.getModels(username, start, numberOfResults);
    }
    
    @Override
    public IOCRModel getModel(String id) {
        IOCRModel model = modelDb.getById(id);
        String parentPath = fileStorageManager.getAndCreateStoragePath(model.getUsername(), MODEL_FOLDER, model.getId());
        model.setPath(parentPath + File.separator + model.getFilename());
        model.setRelativePath(MODEL_FOLDER + File.separator + model.getId() + File.separator + model.getFilename());
        return model;
    }
}
