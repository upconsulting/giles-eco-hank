package com.upconsulting.gilesecosystem.hank.db;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.store.IDatabaseClient;

public interface IImageFileDBClient extends IDatabaseClient<IImageFile> {

    public abstract IImageFile saveImageFile(IImageFile file)
            throws UnstorableObjectException;

    public abstract List<ImageFile> getImageFiles(String username);

    public abstract IImageFile getFileById(String id);

    public abstract IImageFile getImageForRunId(String runId);

    public abstract List<ImageFile> getImageFiles(String username, int offset, int pageSize);

    public abstract int getNumberOfImageFiles(String username);

}