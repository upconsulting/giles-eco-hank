package com.upconsulting.gilesecosystem.hank.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.upconsulting.gilesecosystem.hank.exceptions.RunDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.web.forms.LineCorrection;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;

public interface ILineCorrectionManager {

    ICorrection saveCorrectedLines(String username, String imageId,
            String ocrRun, String correctionId, String page, List<LineCorrection> corrections)
            throws RunDoesNotExistException, FileStorageException, IOException;

    ICorrection getCorrections(String username, String imageId, String runId,
            String page);

    public abstract File getCorrectionsFolder(String username, String imageId, String runId);

}