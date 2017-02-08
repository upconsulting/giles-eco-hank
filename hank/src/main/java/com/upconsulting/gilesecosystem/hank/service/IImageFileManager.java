package com.upconsulting.gilesecosystem.hank.service;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;

public interface IImageFileManager {

    public abstract IImageFile getImageFile(String id);

    public abstract List<IImageFile> getImageFiles(String username);

    public abstract IImageFile storeOrUpdateImageFile(IImageFile file);

}