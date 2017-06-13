package com.upconsulting.gilesecosystem.hank.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.model.impl.Training;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface ITrainingDBClient extends IDatabaseClient<ITraining>{

    List<ITraining> getTrainingsByRunId(String runId);

}