package com.upconsulting.gilesecosystem.hank.db.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    public IOCRModel getById(String id) {
        return em.find(OCRModel.class, id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }
    
    @Override
    public List<IOCRModel> getModels(String username, int start, int pageSize) {
        CriteriaQuery<IOCRModel> query = em.getCriteriaBuilder().createQuery(IOCRModel.class);
        Root<OCRModel> model = query.from(OCRModel.class);
        query.select(model);
        
        return em.createQuery(query).setFirstResult(start).setMaxResults(pageSize).getResultList();
    }
    
}
