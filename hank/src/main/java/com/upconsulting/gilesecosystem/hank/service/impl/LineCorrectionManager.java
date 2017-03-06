package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
    public ICorrection saveCorrectedLines(String username, String imageId, String ocrRun, String correctionId, String page, List<LineCorrection> corrections) throws RunDoesNotExistException, FileStorageException, IOException {
        IOCRRun run = runManager.getRun(ocrRun);
        if (run == null) {
            throw new RunDoesNotExistException("No run with id " + ocrRun);
        }
        
        ICorrection correction;
        if (correctionId != null) {
            correction = dbClient.getById(correctionId);
        } else {
            correction = new Correction();
            correction.setCorrectedRun(run);
            correction.setId(dbClient.generateId());
        }
        correction.setDate(LocalDateTime.now());
            
        String runFolder = storageManager.getAndCreateStoragePath(username, imageId, run.getId());
        File correctionFolder = storageManager.createFolder(username, imageId, run.getId(), correction.getId());
        File pageFolder = new File(correctionFolder.getAbsolutePath() + File.separator + page);
        if (!pageFolder.exists()) {
            pageFolder.mkdir();
        }
        for (LineCorrection line : corrections) {
            storageManager.saveFileInFolder(pageFolder, line.getLineName() + ".txt", line.getText().getBytes());
            // copy image file
            String filename = line.getLineName() + ".bin.png";
            File imageFile = new File(runFolder + File.separator + page + File.separator + filename);
            FileUtils.copyFile(imageFile, new File(pageFolder + File.separator + filename));
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
    
    @Override
    public ICorrection getCorrections(String username, String imageId, String runId, String page) {
        List<Correction> corrections = dbClient.getCorrectionsByImage(runId);
        
        // we assume there is just one correction per page
        if (corrections.size() > 0) {
            ICorrection cor = corrections.get(0);
            String runFolder = storageManager.getAndCreateStoragePath(username, imageId, runId);
            File corrFolder = new File(runFolder + File.separator + cor.getId());
            File pageFolder = new File (corrFolder + File.separator + page);
            if (pageFolder.exists()) {
                return cor;
            }
        }
        
        return null;
    }
}
