package com.upconsulting.gilesecosystem.hank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.db.impl.ImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;

@Service
public class ImageFileManager {

    @Autowired
    private ImageFileDBClient dbClient;
    
    public IImageFile getImageFile(String id) {
        IImageFile imageFile = dbClient.getFileById(id);
        return imageFile;
    }
}
