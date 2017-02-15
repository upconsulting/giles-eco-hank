package com.upconsulting.gilesecosystem.hank.model;

public interface IPageLine {

    public abstract String getText();

    public abstract void setText(String text);

    public abstract byte[] getImage();

    public abstract void setImage(byte[] image);

    public abstract void setLineName(String lineName);

    public abstract String getLineName();

    public abstract void setImageFilename(String imageFilename);

    public abstract String getImageFilename();

}