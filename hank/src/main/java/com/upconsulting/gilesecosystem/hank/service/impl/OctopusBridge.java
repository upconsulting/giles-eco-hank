package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IOctopusBridge;
import com.upconsulting.gilesecosystem.hank.util.Properties;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Service
public class OctopusBridge implements IOctopusBridge {

    private final String PROCESSING_FOLDER = "processing";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFileStorageManager fileStorageManager;

    @Autowired
    private IPropertiesManager propertiesManager;

    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IOctopusBridge#runNlbin(com.upconsulting.gilesecosystem.hank.model.impl.ImageFile)
     */
    @Override
    public boolean runNlbin(ImageFile imageFile) throws DockerConnectionException {
        Process p = null;
        try {
            String imageFolder = fileStorageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null) + File.separator;
            String cmd = String.format("%s run -v %s:/data ocropus ./ocropus-nlbin /data/%s -o /data/%s",
                    propertiesManager.getProperty(Properties.DOCKER_LOCATION), imageFolder, imageFile.getFilename(), PROCESSING_FOLDER);
            logger.debug("Running command: " + cmd);
            p = Runtime.getRuntime().exec(cmd);
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

        imageFile.setProcessingFolder(PROCESSING_FOLDER);
        return true;
    }
}
