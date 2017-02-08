package com.upconsulting.gilesecosystem.hank.service;

import java.time.LocalDateTime;

import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IRunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.RunStep;
import com.upconsulting.gilesecosystem.hank.model.impl.StepType;

public interface IOCRRunManager {

    public abstract IOCRRun saveOCRRun(IOCRRun run);

    public abstract IRunStep createRunStep(StepType type);

}