package com.upconsulting.gilesecosystem.hank.model.impl;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.upconsulting.gilesecosystem.hank.model.IRunStep;

@Entity
public class RunStep implements IRunStep {

    @Id
    @GeneratedValue
    private long id;
    
    private LocalDateTime date;
    private StepType stepType;
    private StepStatus status;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#getId()
     */
    @Override
    public long getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#setId(long)
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#getDate()
     */
    @Override
    public LocalDateTime getDate() {
        return date;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#setDate(java.time.LocalDateTime)
     */
    @Override
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
 
    @Override
    public StepType getStepType() {
        return stepType;
    }
    @Override
    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#getStatus()
     */
    @Override
    public StepStatus getStatus() {
        return status;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IRunStep#setStatus(com.upconsulting.gilesecosystem.hank.model.impl.StepStatus)
     */
    @Override
    public void setStatus(StepStatus status) {
        this.status = status;
    }
    
}
