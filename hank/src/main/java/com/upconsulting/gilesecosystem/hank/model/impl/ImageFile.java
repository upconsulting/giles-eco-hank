package com.upconsulting.gilesecosystem.hank.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus;

@Entity
public class ImageFile implements IImageFile {

    @Id
    private String id;
    private String username;
    private String filename;
    private WorkflowStatus status;
    private String contentType;
    private String uploadId;
    private String processingFolder;
    private long size;
    
    @OneToMany(fetch=FetchType.EAGER, targetEntity=OCRRun.class)
    private List<IOCRRun> ocrRuns;
    
    public ImageFile() {}
    
    public ImageFile(String filename) {
        this.filename = filename;
    }
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setUsername(java.lang.String)
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getStatus()
     */
    @Override
    public WorkflowStatus getStatus() {
        return status;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setStatus(com.upconsulting.gilesecosystem.hank.service.impl.WorkflowStatus)
     */
    @Override
    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getContentType()
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setContentType(java.lang.String)
     */
    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getUploadId()
     */
    @Override
    public String getUploadId() {
        return uploadId;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setUploadId(java.lang.String)
     */
    @Override
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getProcessingFolder()
     */
    @Override
    public String getProcessingFolder() {
        return processingFolder;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setProcessingFolder(java.lang.String)
     */
    @Override
    public void setProcessingFolder(String processingFolder) {
        this.processingFolder = processingFolder;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#getSize()
     */
    @Override
    public long getSize() {
        return size;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IImageFile#setSize(long)
     */
    @Override
    public void setSize(long size) {
        this.size = size;
    }
    
    @Override
    public List<IOCRRun> getOcrRuns() {
        if (ocrRuns == null) {
            ocrRuns = new ArrayList<IOCRRun>();
        }
        return ocrRuns;
    }

    @Override
    public void setOcrRuns(List<IOCRRun> ocrRuns) {
        this.ocrRuns = ocrRuns;
    }

    
}
