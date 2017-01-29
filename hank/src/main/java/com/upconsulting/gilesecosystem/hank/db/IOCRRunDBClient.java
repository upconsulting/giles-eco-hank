package com.upconsulting.gilesecosystem.hank.db;

import com.upconsulting.gilesecosystem.hank.model.IOCRRun;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface IOCRRunDBClient extends IDatabaseClient<IOCRRun> {

    public abstract IOCRRun getById(String id);

}