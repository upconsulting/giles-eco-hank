package com.upconsulting.gilesecosystem.hank.model.impl;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;

@Entity
public class Correction implements ICorrection {

    @Id
    private String id;
    @ManyToOne(targetEntity=OCRRun.class)
    private IOCRRun correctedRun;
    private LocalDateTime date;
    private String hocrFile;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#getId()
     */
    @Override
    public String getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#getCorrectedRun()
     */
    @Override
    public IOCRRun getCorrectedRun() {
        return correctedRun;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#setCorrectedRun(com.upconsulting.gilesecosystem.hank.model.IOCRRun)
     */
    @Override
    public void setCorrectedRun(IOCRRun correctedRun) {
        this.correctedRun = correctedRun;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#getDate()
     */
    @Override
    public LocalDateTime getDate() {
        return date;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#setDate(java.time.LocalDateTime)
     */
    @Override
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#getHocrFile()
     */
    @Override
    public String getHocrFile() {
        return hocrFile;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ICorrection#setHocrFile(java.lang.String)
     */
    @Override
    public void setHocrFile(String hocrFile) {
        this.hocrFile = hocrFile;
    }

}
