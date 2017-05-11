package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upconsulting.gilesecosystem.hank.db.ITrainingDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.TrainingException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.model.impl.Training;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.ILineCorrectionManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;
import com.upconsulting.gilesecosystem.hank.service.ITrainingService;
import com.upconsulting.gilesecosystem.hank.workflow.IOctopusBridge;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Transactional
@Service
public class TrainingService implements ITrainingService {

    public final static String TRAIN_FOLDER = "train";
    public final static String TEST_FOLDER = "test";
    
    private final float DEFAULT_TRAINING_RATIO = 0.6f;
    
    @Autowired
    private IOCRRunManager runManager;
    
    @Autowired
    private IImageFileManager fileManager;
    
    @Autowired
    private ILineCorrectionManager correctionManager;
    
    @Autowired
    private IFileStorageManager storageManager;
    
    @Autowired
    private ITrainingDBClient trainingDbClient;
    
    @Autowired
    private IOctopusBridge octopusBridge;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.ITrainingService#trainModel(java.lang.String)
     */
    @Override
    public void trainModel(String runId, int linesToTrain, int savingFreq) throws ImageFileDoesNotExistException, TrainingException {
        
        IImageFile file = fileManager.getByRunId(runId);
        IOCRRun run = runManager.getRun(runId);
        String trainingId = trainingDbClient.generateId();
        
        ITraining training = new Training();
        training.setId(trainingId);
        training.setRunId(runId);
        training.setDate(ZonedDateTime.now());
        training.setLinesToTrain(linesToTrain);
        training.setSavingFreq(savingFreq);
        
        File trainingFolder = storageManager.createFolder(file.getUsername(), file.getId(), trainingId, TRAIN_FOLDER);
        File testFolder = storageManager.createFolder(file.getUsername(), file.getId(), trainingId, TEST_FOLDER);
        
        List<IPage> pages = runManager.getPages(runId, null);
        for (IPage page : pages) {
            int pagenr = page.getPage();
            String formattedPageNr = String.format("%04d", pagenr);
            // create page folders
            File trainingPageFolder = new File(trainingFolder.getAbsolutePath() + File.separator + formattedPageNr);
            if (!trainingPageFolder.exists()) {
                trainingPageFolder.mkdir();
            }
            training.setTrainingFolder(TRAIN_FOLDER);
            
            File testPageFolder = new File(testFolder.getAbsolutePath() + File.separator + formattedPageNr);
            if (!testPageFolder.exists()) {
                testPageFolder.mkdir();
            }
            training.setTestFolder(TEST_FOLDER);
            
            // copy text files for page
            String runFolder = storageManager.getAndCreateStoragePath(file.getUsername(), file.getId(), runId);
            
            File correctedPageFolder = new File(runFolder + File.separator + LineCorrectionManager.CORRECTION_FOLDER + File.separator + formattedPageNr);
            File[] textFiles = correctedPageFolder.listFiles(new FilenameFilter() {
                
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            
            int totalNrTextFiles = textFiles.length;
            float trainingFileNumber = totalNrTextFiles * DEFAULT_TRAINING_RATIO;
            int trainingFilesLength = Math.round(trainingFileNumber);
            
            for(int i = 0; i < textFiles.length; i++) {
                File lineFile =textFiles[i];
                String gtFilename = lineFile.getName().replace(".txt", ".gt.txt");
                File copyTo;
                if (i < trainingFilesLength) {
                    copyTo  = new File(trainingPageFolder.getAbsolutePath() + File.separator + gtFilename);
                } else {
                    copyTo  = new File(testPageFolder.getAbsolutePath() + File.separator + gtFilename);
                }
                try {
                    FileUtils.copyFile(lineFile, copyTo);
                } catch (IOException e) {
                    throw new TrainingException("There was an error creating the training data.", e);
                }
            }
            
            // copy image files
            File[] imageFiles = correctedPageFolder.listFiles(new FilenameFilter() {
                
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".bin.png");
                }
            });
            
            for (int i=0; i<imageFiles.length; i++) {
                File lineFile = imageFiles[i];
                File copyTo;
                if (i < trainingFilesLength) {
                    copyTo = new File(trainingPageFolder.getAbsolutePath() + File.separator + lineFile.getName());
                } else {
                    copyTo = new File(testPageFolder.getAbsolutePath() + File.separator + lineFile.getName());
                }
                try {
                    FileUtils.copyFile(lineFile, copyTo);
                } catch (IOException e) {
                    throw new TrainingException("There was an error creating the training data.", e);
                }
            }
        }
        try {
            trainingDbClient.store(training);
        } catch (UnstorableObjectException e) {
            throw new TrainingException("Could not store training.", e);
        }
        
        try {
            octopusBridge.runTraining(training, file, run);
        } catch (DockerConnectionException e) {
            throw new TrainingException("Could not run training.", e);
        }
    }
}
