package com.upconsulting.gilesecosystem.hank.db.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.upconsulting.gilesecosystem.hank.db.IOCRRunDBClient;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.OCRRun;

import edu.asu.diging.gilesecosystem.util.store.objectdb.DatabaseClient;

@Component
public class OCRRunDBClient extends DatabaseClient<IOCRRun> implements IOCRRunDBClient {
    
    @PersistenceContext(unitName="DataPU")
    private EntityManager em;



    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.db.impl.ICORRunDBClient#getById(java.lang.String)
     */
    @Override
    public IOCRRun getById(String id) {
        return em.find(OCRRun.class, id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }
    
    @Override
    protected String getIdPrefix() {
        return "RUN";
    }

}
