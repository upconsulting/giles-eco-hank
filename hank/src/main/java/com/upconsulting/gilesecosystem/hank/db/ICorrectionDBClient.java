package com.upconsulting.gilesecosystem.hank.db;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.impl.Correction;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface ICorrectionDBClient extends IDatabaseClient<ICorrection> {

    public abstract List<Correction> getCorrectionsByImage(String runId);

}