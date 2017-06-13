package com.upconsulting.gilesecosystem.hank.web.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.ITraining;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public class ImageFileForm extends ImageFile {

    private String[] processingFiles;
    private Map<String, String[]> lineFolders = new HashMap<String, String[]>();
    private byte[] content;
    private IImageFile imageFile;
    private List<IOCRRun> runs;
    private Map<IOCRRun, List<ITraining>> trainings;

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

    public IImageFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(IImageFile imageFile) {
        this.imageFile = imageFile;
    }

    public List<IOCRRun> getRuns() {
        return runs;
    }

    public void setRuns(List<IOCRRun> runs) {
        this.runs = runs;
    }

    public Map<IOCRRun, List<ITraining>> getTrainings() {
        return trainings;
    }

    public void setTrainings(Map<IOCRRun, List<ITraining>> trainings) {
        this.trainings = trainings;
    }

}
