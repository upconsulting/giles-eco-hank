package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.service.impl.ImageFileManager;
import com.upconsulting.gilesecosystem.hank.util.Properties;
import com.upconsulting.gilesecosystem.hank.workflow.IOctopusBridge;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Service
public class OctopusBridge implements IOctopusBridge {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFileStorageManager fileStorageManager;

    @Autowired
    private IPropertiesManager propertiesManager;

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IOctopusBridge#runNlbin(com.upconsulting.gilesecosystem.hank.model.impl.ImageFile)
     */
    @Override
    public boolean runNlbin(IImageFile imageFile, IOCRRun run) throws DockerConnectionException {
        String imageFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null) + File.separator;
        String cmd = String.format("%s run -v %s:/data ocropus ocropus-nlbin /data/%s/* -o /data/%s",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), imageFolder, ImageFileManager.IMAGE_FOLDER, run.getId());
        
        boolean success = runCommand(cmd);

        if (success) {
            imageFile.setProcessingFolder(run.getId());
        } 
        return true;
    }
    
    @Override
    public boolean runPageLayoutAnalysis(IImageFile imageFile, IOCRRun run) throws DockerConnectionException {
        String imageFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null) + File.separator;
        String cmd = String.format("%s run -v %s:/data ocropus ocropus-gpageseg '/data/%s/????.bin.png'",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), imageFolder, run.getId());
        
        return runCommand(cmd);
    }
    
    @Override
    public boolean runLineRecognition(IImageFile imageFile, IOCRRun run) throws DockerConnectionException {
        String modelPath = run.getModel().getRelativePath();
        String userFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), null, null) + File.separator;
        String cmd = String.format("%s run -v %s:/data ocropus ocropus-rpred -Q %s -m /data/%s '/data/%s/%s/????/??????.bin.png'",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), userFolder,  "2", modelPath, imageFile.getId(), run.getId());
        
        return runCommand(cmd);
    }
    
    @Override
    public String runHOCROutput(IImageFile imageFile, IOCRRun run, String outputFilename) throws DockerConnectionException {
        String runFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), run.getId()) + File.separator;
        String hocrFileending = ".html";
        
        String cmd = String.format("%s run -v %s:/data ocropus ocropus-hocr '????/??????.bin.png' -o %s%s",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), runFolder, outputFilename, hocrFileending);
        
        boolean success = runCommand(cmd);
        if (success) {
            return outputFilename + hocrFileending;
        }
        
        return null;
    }
    
    @Override
    public String runTraining(ITraining training, IImageFile imageFile, IOCRRun run) throws DockerConnectionException {
        String trainingsFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), training.getId());
        
        String cmd = String.format("%s run -v %s:/data ocropus ocropus-rtrain -N %s -F %s -c %s/*/*.gt.txt %s/*/*.gt.txt -o model.pyrnn.gz %s/*/*.bin.png",
                propertiesManager.getProperty(Properties.DOCKER_LOCATION), trainingsFolder, training.getLinesToTrain(), training.getSavingFreq(), training.getTrainingFolder(), training.getTestFolder(), training.getTrainingFolder());
        
        boolean success = runCommand(cmd);
        if (success) {
            return "model.pyrnn.gz";
        }
        
        return null;
    }
    
    private boolean runCommand(String cmd) throws DockerConnectionException {
        Process p = null;
        try {
            logger.debug("Running command: " + cmd);
            p = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd });
        } catch (IOException e) {
            throw new DockerConnectionException("Could not run Docker.", e);
        }
        String line = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                logger.info(line);
            }
        } catch (IOException e) {
            logger.error("Could not read info.", e);
            return false;
        } finally {
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
