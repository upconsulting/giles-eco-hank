package com.upconsulting.gilesecosystem.hank.model;

import java.time.LocalDateTime;

import com.upconsulting.gilesecosystem.hank.model.impl.StepStatus;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;

public interface IRunStep {

    public abstract long getId();

    public abstract void setId(long id);

    public abstract LocalDateTime getDate();

    public abstract void setDate(LocalDateTime date);

    public abstract StepStatus getStatus();

    public abstract void setStatus(StepStatus status);

    public abstract void setStepType(StepType stepType);

    public abstract StepType getStepType();

}