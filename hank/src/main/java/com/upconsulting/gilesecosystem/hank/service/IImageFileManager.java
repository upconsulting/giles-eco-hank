package com.upconsulting.gilesecosystem.hank.service;

import java.util.ArrayList;
import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public interface IImageFileManager {

    public abstract IImageFile getImageFile(String id);

    public abstract List<IImageFile> getImageFiles(String username);

    public abstract IImageFile storeOrUpdateImageFile(IImageFile file);

    public abstract IImageFile getByRunId(String runId);

    public abstract List<IImageFile> getImageFiles(String username, int page);

    public abstract int getNumberOfPages(String username);

}