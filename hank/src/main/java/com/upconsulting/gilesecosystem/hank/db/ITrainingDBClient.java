package com.upconsulting.gilesecosystem.hank.db;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.ITraining;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface ITrainingDBClient extends IDatabaseClient<ITraining>{

    List<ITraining> getTrainingsByRunId(String runId);

    ITraining getById(String id);
}