package com.upconsulting.gilesecosystem.hank.db.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ICorrectionDBClient;
import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.impl.Correction;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

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

    @Override
    public List<Correction> getCorrectionsByImage(String runId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Correction> query = builder.createQuery(Correction.class);
        Root<Correction> root = query.from(Correction.class);
        query.select(root);
        query.where(builder.equal( root.get("correctedRun").get("id"), runId ));
        return em.createQuery(query).getResultList();
    }
}
