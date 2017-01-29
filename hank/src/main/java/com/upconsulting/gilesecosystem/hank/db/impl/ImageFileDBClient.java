package com.upconsulting.gilesecosystem.hank.db.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Transactional
@Component
public class ImageFileDBClient extends DatabaseClient<IImageFile> {

    @PersistenceContext(unitName="DataPU")
    private EntityManager em;
    
    public IImageFile saveImageFile(IImageFile file) throws UnstorableObjectException {
        IImageFile existing = getById(file.getId());
        
        if (existing == null) {
            return store(file);
        }
        
        return update(file);
    }
    
    public List<ImageFile> getImageFiles(String username) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ImageFile> query = builder.createQuery(ImageFile.class);
        Root<ImageFile> root = query.from(ImageFile.class);
        query.select(root);
        query.where(builder.equal( root.get("username"), username ));
        return em.createQuery(query).getResultList();
    }
    
    public IImageFile getFileById(String id) {
        return em.find(ImageFile.class, id);
    }

    @Override
    protected String getIdPrefix() {
        return "IMGF";
    }

    @Override
    protected IImageFile getById(String id) {
        return getFileById(id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }
    
}
