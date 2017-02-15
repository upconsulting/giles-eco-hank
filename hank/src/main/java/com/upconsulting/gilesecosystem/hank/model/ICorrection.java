package com.upconsulting.gilesecosystem.hank.model;

import java.time.LocalDateTime;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface ICorrection extends IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract IOCRRun getCorrectedRun();

    public abstract void setCorrectedRun(IOCRRun correctedRun);

    public abstract LocalDateTime getDate();

    public abstract void setDate(LocalDateTime date);

    public abstract String getHocrFile();

    public abstract void setHocrFile(String hocrFile);

}