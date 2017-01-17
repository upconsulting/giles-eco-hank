package com.upconsulting.gilesecosystem.hank.model.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.omg.CosNaming.IstringHelper;

import com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

@Entity
public class ImageFile implements IStorableObject {

    @Id
    private String id;
    private String username;
    private String filename;
    private WorkflowStatus status;
    private String contentType;
    private String uploadId;
    private long size;
    
    public ImageFile() {}
    
    public ImageFile(String filename) {
        this.filename = filename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
}
