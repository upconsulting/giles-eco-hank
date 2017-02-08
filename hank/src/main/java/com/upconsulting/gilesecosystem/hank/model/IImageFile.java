package com.upconsulting.gilesecosystem.hank.model;

import java.util.List;

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

    public abstract void setOcrRuns(List<IOCRRun> ocrRuns);

    public abstract List<IOCRRun> getOcrRuns();

}