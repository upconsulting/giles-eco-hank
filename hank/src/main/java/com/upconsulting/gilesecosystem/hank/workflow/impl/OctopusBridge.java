package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITask;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.service.ITaskProcessingService;
import com.upconsulting.gilesecosystem.hank.service.impl.ImageFileManager;
import com.upconsulting.gilesecosystem.hank.util.Properties;
import com.upconsulting.gilesecosystem.hank.workflow.IOctopusBridge;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Service
public class OctopusBridge implements IOctopusBridge {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFileStorageManager fileStorageManager;

    @Autowired
    private IPropertiesManager propertiesManager;
    
    @Autowired
    private ITaskProcessingService processingService;
    
    private final String MODEL_NAME = "model.pyrnn.gz";

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IOctopusBridge#runNlbin(com.upconsulting.gilesecosystem.hank.model.impl.ImageFile)
     */
    @Override
    public boolean runNlbin(IImageFile imageFile, IOCRRun run) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        String imageFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null) + File.separator;
        String cmd = String.format("%s run -v %s:/data --rm -u %s ocropus ocropus-nlbin /data/%s/* -o /data/%s",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), imageFolder, propertiesManager.getProperty(Properties.DOCKER_USER), ImageFileManager.IMAGE_FOLDER, run.getId());
        
        boolean success = runCommand(cmd, run);

        if (success) {
            imageFile.setProcessingFolder(run.getId());
        } 
        return true;
    }
    
    @Override
    public boolean runPageLayoutAnalysis(IImageFile imageFile, IOCRRun run) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        String imageFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null) + File.separator;
        String cmd = String.format("%s run -v %s:/data --rm -u %s ocropus ocropus-gpageseg '/data/%s/????.bin.png'",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), imageFolder, propertiesManager.getProperty(Properties.DOCKER_USER), run.getId());
        
        return runCommand(cmd, run);
    }
    
    @Override
    public boolean runLineRecognition(IImageFile imageFile, IOCRRun run, ITraining training) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        String modelPath = null;
        if (training != null && training.getFinalModel() != null) {
            modelPath = imageFile.getId() + File.separator + training.getId() + File.separator + training.getFinalModel();
        } else {
            modelPath = run.getModel().getRelativePath();
        } 
        String userFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), null, null) + File.separator;
        String cmd = String.format("%s run -v %s:/data --rm -u %s ocropus ocropus-rpred -Q %s -m /data/%s '/data/%s/%s/????/??????.bin.png'",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), userFolder, propertiesManager.getProperty(Properties.DOCKER_USER),  "2", modelPath, imageFile.getId(), run.getId());
        
        return runCommand(cmd, run);
    }
    
    @Override
    public String runHOCROutput(IImageFile imageFile, IOCRRun run, String outputFilename) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        String runFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), run.getId()) + File.separator;
        String hocrFileending = ".html";
        
        String cmd = String.format("%s run -v %s:/data --rm -u %s ocropus ocropus-hocr '????/??????.bin.png' -o %s%s",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), runFolder, propertiesManager.getProperty(Properties.DOCKER_USER), outputFilename, hocrFileending);
        
        boolean success = runCommand(cmd, run);
        if (success) {
            return outputFilename + hocrFileending;
        }
        
        return null;
    }
    
    @Override
    @Async
    public ListenableFuture<ITraining> runTraining(ITraining training, IImageFile imageFile, IOCRRun run) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        String trainingsFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), training.getId());
        
        String cmd = String.format("%s run -v %s:/data --rm -u %s ocropus ocropus-rtrain -N %s -F %s -c %s/*/*.gt.txt %s/*/*.gt.txt -o %s %s/*/*.bin.png",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), trainingsFolder, propertiesManager.getProperty(Properties.DOCKER_USER), training.getLinesToTrain(), training.getSavingFreq(), training.getTrainingFolder(), training.getTestFolder(), MODEL_NAME, training.getTrainingFolder());
        
        boolean success = runCommand(cmd, training);
        if (success) {
            training.setModelName(MODEL_NAME);
            return new AsyncResult<ITraining>(training);
        }
        
        return new AsyncResult<ITraining>(null);
    }
    
    private boolean runCommand(String cmd, ITask task) throws DockerConnectionException, UnknownObjectTypeException, UnstorableObjectException {
        Process p = null;
        try {
            logger.debug("Running command: " + cmd);
            processingService.logMessages("Running command: " + cmd, task);
            p = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd });
        } catch (IOException e) {
            processingService.finishTask(task, false);
            throw new DockerConnectionException("Could not run Docker.", e);
        }
        String line = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                logger.info(line);
                processingService.logMessages(line, task);
            }
        } catch (IOException e) {
            logger.error("Could not read info.", e);
            processingService.logMessages("Error encountered: " + e.getMessage(), task);
            processingService.finishTask(task, false);
            return false;
        } finally {
            processingService.finishTask(task, true);
            try {
                input.close();
            } catch (IOException e) {
                logger.error("Could not close stream.", e);
                return false;
            }
        }
        return true;
    }
}
