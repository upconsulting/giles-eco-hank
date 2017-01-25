package com.upconsulting.gilesecosystem.hank.web.forms;

import java.util.HashMap;
import java.util.Map;

import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public class ImageFileForm extends ImageFile {

    private String[] processingFiles;
    private Map<String, String[]> lineFolders = new HashMap<String, String[]>();
    private byte[] content;

    public String[] getProcessingFiles() {
        return processingFiles;
    }

    public void setProcessingFiles(String[] processingFiles) {
        this.processingFiles = processingFiles;
    }

    public Map<String, String[]> getLineFolders() {
        return lineFolders;
    }

    public void setLineFolders(Map<String, String[]> lineFolders) {
        this.lineFolders = lineFolders;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    
    
}
