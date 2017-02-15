package com.upconsulting.gilesecosystem.hank.db.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ICorrectionDBClient;
import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.impl.Correction;

import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Transactional
@Component
public class CorrectionDBClient extends DatabaseClient<ICorrection> implements ICorrectionDBClient {

    @PersistenceContext(unitName="DataPU")
    private EntityManager em;
   
    @Override
    protected String getIdPrefix() {
        return "COR";
    }

    @Override
    protected ICorrection getById(String id) {
        return em.find(Correction.class, id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }

}
