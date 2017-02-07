package com.upconsulting.gilesecosystem.hank.workflow;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;

public interface IUploadManager {

    public abstract List<IImageFile> processFiles(String username, String modelId,
            MultipartFile[] files) throws FileStorageException, IOException,
            UnstorableObjectException, DockerConnectionException;

}