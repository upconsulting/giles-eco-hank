package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.upconsulting.gilesecosystem.hank.db.ITrainingDBClient;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Component
@Scope("prototype")
@Transactional
class TrainingFinishedListener
        implements ListenableFutureCallback<ITraining> {
   
    
    @Autowired 
    private IImageFileManager fileManager;
    
    @Autowired 
    private IFileStorageManager storageManager;

    @Autowired 
    private ITrainingDBClient trainingDbClient;

    @Override
    public void onSuccess(ITraining result) {
        IImageFile file = fileManager.getByRunId(result.getRunId());
        
        String trainingFolderPath = storageManager.getAndCreateStoragePath(file.getUsername(),
                file.getId(), result.getId());
        File trainingFolder = new File(trainingFolderPath);
        String[] modelFiles = trainingFolder.list(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(result.getModelName());
            }
        });
        
        // get last model file
        List<String> modelFilesList = Arrays.asList(modelFiles);
        Collections.sort(modelFilesList);
        String finalModel = modelFilesList.get(modelFilesList.size() - 1);
        result.setFinalModel(finalModel);
        trainingDbClient.update(result);
    }

    @Override
    public void onFailure(Throwable ex) {
        // TODO Auto-generated method stub
        
    }
}