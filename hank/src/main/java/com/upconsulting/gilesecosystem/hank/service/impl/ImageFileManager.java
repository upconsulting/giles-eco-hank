package com.upconsulting.gilesecosystem.hank.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.IImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

@Transactional
@Service
public class ImageFileManager implements IImageFileManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IImageFileDBClient dbClient;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IImageFileManager#getImageFile(java.lang.String)
     */
    @Override
    public IImageFile getImageFile(String id) {
        IImageFile imageFile = dbClient.getFileById(id);
        return imageFile;
    }
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IImageFileManager#getImageFiles(java.lang.String)
     */
    @Override
    public List<IImageFile> getImageFiles(String username) {
        List<ImageFile> files = dbClient.getImageFiles(username);
        List<IImageFile> imageFiles = new ArrayList<IImageFile>();
        files.forEach(f -> imageFiles.add(f));
        return imageFiles;
    }
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IImageFileManager#storeOrUpdateImageFile(com.upconsulting.gilesecosystem.hank.model.IImageFile)
     */
    @Override
    public IImageFile storeOrUpdateImageFile(IImageFile file) {
        if (file.getId() == null) {
            file.setId(dbClient.generateId());
        }
        try {
            return dbClient.saveImageFile(file);
        } catch (UnstorableObjectException e) {
            // should never happen
            logger.error("Could not store file.", e);
        }
        return null;
    }
}
