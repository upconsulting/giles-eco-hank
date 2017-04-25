package com.upconsulting.gilesecosystem.hank.db.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ITrainingDBClient;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.model.impl.Training;

import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Transactional
@Component
public class TrainingDBClient extends DatabaseClient<ITraining> implements ITrainingDBClient {

    @PersistenceContext(unitName="DataPU")
    private EntityManager em;
    
    @Override
    protected String getIdPrefix() {
        return "TRAIN";
    }

    @Override
    protected ITraining getById(String id) {
        return em.find(Training.class, id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }

}
