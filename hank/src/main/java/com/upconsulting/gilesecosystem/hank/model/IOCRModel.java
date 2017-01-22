package com.upconsulting.gilesecosystem.hank.model;

import java.util.List;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface IOCRModel extends IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getUsername();

    public abstract void setUsername(String username);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract List<String> getDerivedFrom();

    public abstract void setDerivedFrom(List<String> derivedFrom);

}