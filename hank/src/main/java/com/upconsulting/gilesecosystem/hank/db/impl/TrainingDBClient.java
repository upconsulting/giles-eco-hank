package com.upconsulting.gilesecosystem.hank.db.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    @Override
    public List<ITraining> getTrainingsByRunId(String runId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Training> query = builder.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);
        query.select(root);
        query.where(builder.equal( root.get("runId"), runId ));
        
        List<Training> trainings = em.createQuery(query).getResultList();
        List<ITraining> results = new ArrayList<>();
        trainings.forEach(t -> results.add((ITraining) t));
        return results;
    }

}
