package com.upconsulting.gilesecosystem.hank.db;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;

import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface IOCRModelDBClient extends IDatabaseClient<IOCRModel> {

    public abstract List<IOCRModel> getModels(String username, int start, int pageSize);
    public IOCRModel getById(String id);
}