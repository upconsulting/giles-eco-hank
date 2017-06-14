package com.upconsulting.gilesecosystem.hank.model;

import java.time.LocalDateTime;
import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.impl.StepStatus;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface IOCRRun extends ITask, IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract LocalDateTime getDate();

    public abstract void setDate(LocalDateTime date);

    public abstract IOCRModel getModel();

    public abstract void setModel(IOCRModel model);

    public abstract List<IRunStep> getSteps();

    public abstract IRunStep setStepStatus(StepType type, StepStatus status);

    public abstract void setHocrFile(String hocrFile);

    public abstract String getHocrFile();

    void setTrainingId(String trainingId);

    String getTrainingId();

}