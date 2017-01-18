package com.upconsulting.gilesecosystem.hank.model;

import com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface IImageFile extends IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getUsername();

    public abstract void setUsername(String username);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract WorkflowStatus getStatus();

    public abstract void setStatus(WorkflowStatus status);

    public abstract String getContentType();

    public abstract void setContentType(String contentType);

    public abstract String getUploadId();

    public abstract void setUploadId(String uploadId);

    public abstract String getProcessingFolder();

    public abstract void setProcessingFolder(String processingFolder);

    public abstract long getSize();

    public abstract void setSize(long size);

}