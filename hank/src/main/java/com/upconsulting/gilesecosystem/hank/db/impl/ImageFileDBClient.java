package com.upconsulting.gilesecosystem.hank.db.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Transactional("txmanager_data")
@Component
public class ImageFileDBClient extends DatabaseClient<ImageFile> {

    @PersistenceContext(unitName="DataPU")
    private EntityManager em;
    
    public ImageFile saveImageFile(ImageFile file) throws UnstorableObjectException {
        ImageFile existing = getById(file.getId());
        
        if (existing == null) {
            return store(file);
        }
        
        return update(file);
    }
    
    public ImageFile getFileById(String id) {
        return em.find(ImageFile.class, id);
    }

    @Override
    protected String getIdPrefix() {
        return "IMGF";
    }

    @Override
    protected ImageFile getById(String id) {
        return getFileById(id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }
    
}
