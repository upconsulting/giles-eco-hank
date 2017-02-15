package com.upconsulting.gilesecosystem.hank.service;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.model.IPageLine;
import com.upconsulting.gilesecosystem.hank.model.IRunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.Page;
import com.upconsulting.gilesecosystem.hank.model.impl.PageLine;
import com.upconsulting.gilesecosystem.hank.model.impl.RunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;

public interface IOCRRunManager {

    public abstract IOCRRun saveOCRRun(IOCRRun run);

    public abstract IRunStep createRunStep(StepType type);

    public abstract IOCRRun getRun(String id);

    public abstract List<IPage> getPages(String runId) throws ImageFileDoesNotExistException;

}