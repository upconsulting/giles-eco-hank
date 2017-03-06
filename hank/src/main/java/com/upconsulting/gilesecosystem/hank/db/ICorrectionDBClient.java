package com.upconsulting.gilesecosystem.hank.db;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.impl.Correction;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface ICorrectionDBClient extends IDatabaseClient<ICorrection> {

    public abstract List<Correction> getCorrectionsByImage(String runId);
    
    ICorrection getById(String id);

}