package com.upconsulting.gilesecosystem.hank.model;

public interface ITask {

    String getLog();

    void setLog(String log);

    void setDone(boolean done);

    boolean isDone();

    void setSuccess(boolean success);

    boolean isSuccess();

}