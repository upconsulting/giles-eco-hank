package com.upconsulting.gilesecosystem.hank.db.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.IOCRModelDBClient;
import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRModel;

import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Transactional
@Component
public class OCRModelDBClient extends DatabaseClient<IOCRModel> implements IOCRModelDBClient {

    @PersistenceContext(unitName="DataPU")
    private EntityManager em;

    @Override
    protected String getIdPrefix() {
        return "MOD";
    }

    @Override
    protected IOCRModel getById(String id) {
        return em.find(OCRModel.class, id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }
    
    
}
