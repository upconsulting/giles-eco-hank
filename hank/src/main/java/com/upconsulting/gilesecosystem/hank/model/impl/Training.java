package com.upconsulting.gilesecosystem.hank.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.upconsulting.gilesecosystem.hank.model.ITraining;

@Entity
public class Training implements ITraining {

    @Id
    private String id;
    private String runId;
    private String startingModel;
    private ZonedDateTime date;
    
    private int linesToTrain;
    private int savingFreq;
    
    private String trainingFolder;
    private String testFolder;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#getId()
     */
    @Override
    public String getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#getCorrectedRun()
     */
    @Override
    public String getRunId() {
        return runId;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#setCorrectedRun(com.upconsulting.gilesecosystem.hank.model.IOCRRun)
     */
    @Override
    public void setRunId(String runId) {
        this.runId = runId;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#getDate()
     */
    @Override
    public ZonedDateTime getDate() {
        return date;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITraining#setDate(java.time.LocalDateTime)
     */
    @Override
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    @Override
    public int getLinesToTrain() {
        return linesToTrain;
    }
    @Override
    public void setLinesToTrain(int linesToTrain) {
        this.linesToTrain = linesToTrain;
    }
    @Override
    public int getSavingFreq() {
        return savingFreq;
    }
    @Override
    public void setSavingFreq(int savingFreq) {
        this.savingFreq = savingFreq;
    }
    @Override
    public String getTrainingFolder() {
        return trainingFolder;
    }
    @Override
    public void setTrainingFolder(String trainingFolder) {
        this.trainingFolder = trainingFolder;
    }
    @Override
    public String getTestFolder() {
        return testFolder;
    }
    @Override
    public void setTestFolder(String testFolder) {
        this.testFolder = testFolder;
    }
    @Override
    public String getStartingModel() {
        return startingModel;
    }
    @Override
    public void setStartingModel(String startingModel) {
        this.startingModel = startingModel;
    }
}
