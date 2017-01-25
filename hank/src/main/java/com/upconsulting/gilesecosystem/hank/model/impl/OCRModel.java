package com.upconsulting.gilesecosystem.hank.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.service.impl.ModelManager;

@Entity
public class OCRModel implements IOCRModel {

    @Id
    private String id;
    
    private String username;
    private String filename;
    private String title;
    private String description;
    
    private List<String> derivedFrom;
    
    /**
     * We don't want to persist this path, since it might change whenever
     * the storage folder is moved.
     * Let the {@link ModelManager} fill that one.
     */
    @Transient
    private String path;
    
    /**
     * The relative path to the model inside a user's folder.
     */
    @Transient
    private String relativePath;

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setUsername(java.lang.String)
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setTitle(java.lang.String)
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#getDerivedFrom()
     */
    @Override
    public List<String> getDerivedFrom() {
        return derivedFrom;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRModel#setDerivedFrom(java.util.List)
     */
    @Override
    public void setDerivedFrom(List<String> derivedFrom) {
        this.derivedFrom = derivedFrom;
    }
    
    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
