package com.upconsulting.gilesecosystem.hank.model.impl;

import com.upconsulting.gilesecosystem.hank.model.IPageLine;

public class PageLine implements IPageLine {

    private String lineName;
    private String text;
    private byte[] image;
    private String imageFilename;
    
    @Override
    public String getLineName() {
        return lineName;
    }
    @Override
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPageLine#getText()
     */
    @Override
    public String getText() {
        return text;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPageLine#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPageLine#getImage()
     */
    @Override
    public byte[] getImage() {
        return image;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPageLine#setImage(byte[])
     */
    @Override
    public void setImage(byte[] image) {
        this.image = image;
    }
    @Override
    public String getImageFilename() {
        return imageFilename;
    }
    @Override
    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
    
}
