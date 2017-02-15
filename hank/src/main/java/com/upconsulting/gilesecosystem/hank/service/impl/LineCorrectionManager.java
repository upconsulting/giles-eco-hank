package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ICorrectionDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.RunDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.Correction;
import com.upconsulting.gilesecosystem.hank.service.ILineCorrectionManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.web.forms.LineCorrection;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Transactional
@Service
public class LineCorrectionManager implements ILineCorrectionManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICorrectionDBClient dbClient;
    
    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IFileStorageManager storageManager;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.ILineCorrectionManager#saveCorrectedLines(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public ICorrection saveCorrectedLines(String username, String imageId, String ocrRun, String page, List<LineCorrection> corrections) throws RunDoesNotExistException, FileStorageException, IOException {
        ICorrection correction = new Correction();
        IOCRRun run = runManager.getRun(ocrRun);
        if (run == null) {
            throw new RunDoesNotExistException("No run with id " + ocrRun);
        }
        correction.setCorrectedRun(run);
        correction.setDate(LocalDateTime.now());
        correction.setId(dbClient.generateId());
        
        File pageFolder = storageManager.createFolder(username, imageId, correction.getId(), page);
        for (LineCorrection line : corrections) {
            storageManager.saveFileInFolder(pageFolder, line.getLineName() + ".txt", line.getText().getBytes());
        }
        
        try {
            dbClient.store(correction);
        } catch (UnstorableObjectException e) {
            // should never happen, we set the id above
            logger.error("Couldn't store correction.", e);
            return null;
        }
        return correction;
    }
}
