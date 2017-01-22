package com.upconsulting.gilesecosystem.hank.web.forms;

import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public class ImageFileForm extends ImageFile {

    private String[] processingFiles;

    public String[] getProcessingFiles() {
        return processingFiles;
    }

    public void setProcessingFiles(String[] processingFiles) {
        this.processingFiles = processingFiles;
    }
    
}
