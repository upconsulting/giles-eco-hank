package com.upconsulting.gilesecosystem.hank.model.impl;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.upconsulting.gilesecosystem.hank.model.ITask;

@MappedSuperclass
public class Task implements ITask {

    @Lob
    private String log;
    private boolean done;
    private boolean success;

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITask#getLog()
     */
    @Override
    public String getLog() {
        return log;
    }

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.ITask#setLog(java.lang.String)
     */
    @Override
    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }    
    
}
