package com.upconsulting.gilesecosystem.hank.web.forms;

public class LineCorrection {

    private String lineName;
    private String text;
    private String imageFilename;
    
    public LineCorrection() {}
    
    public LineCorrection(String lineName, String text, String imageFilename) {
        super();
        this.lineName = lineName;
        this.text = text;
        this.imageFilename = imageFilename;
    }
    
    public String getLineName() {
        return lineName;
    }
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getImageFilename() {
        return imageFilename;
    }
    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
    
}
