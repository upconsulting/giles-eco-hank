package com.upconsulting.gilesecosystem.hank.model;

import java.time.ZonedDateTime;
import java.util.List;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface ITraining extends ITask, IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getRunId();

    public abstract void setRunId(String runId);

    public abstract ZonedDateTime getDate();

    public abstract void setDate(ZonedDateTime date);

    void setTestFolder(String testFolder);

    String getTestFolder();

    void setTrainingFolder(String trainingFolder);

    String getTrainingFolder();

    void setSavingFreq(int savingFreq);

    int getSavingFreq();

    void setLinesToTrain(int linesToTrain);

    int getLinesToTrain();

    void setStartingModel(String startingModel);

    String getStartingModel();

    void setPages(List<Integer> pages);

    List<Integer> getPages();

    void setModelName(String modelName);

    String getModelName();

    void setFinalModel(String finalModel);

    String getFinalModel();

}