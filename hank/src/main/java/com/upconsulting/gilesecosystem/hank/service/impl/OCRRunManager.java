package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.db.IOCRRunDBClient;
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
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.ILineCorrectionManager;
import com.upconsulting.gilesecosystem.hank.service.IOCRRunManager;

import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Service
public class OCRRunManager implements IOCRRunManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IOCRRunDBClient runDBClient;
    
    @Autowired
    private ILineCorrectionManager correctionsManager;
    
    @Autowired
    private IImageFileManager imageManager;
    
    @Autowired
    private IFileStorageManager fileStorageManager;
   
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.service.impl.IOCRRunManager#saveOCRRun(com.upconsulting.gilesecosystem.hank.model.IOCRRun)
     */
    @Override
    public IOCRRun saveOCRRun(IOCRRun run) {
        if (run.getId() == null) {
            run.setId(runDBClient.generateId());
        }
        
        try {
            runDBClient.store(run);
        } catch (UnstorableObjectException e) {
            // should never happen since we're setting it above
            logger.error("Could not store OCR run.", e);
        }
        
        return run;
     }

    @Override
    public IRunStep createRunStep(StepType type) {
        IRunStep step = new RunStep();
        step.setStepType(type);
        step.setDate(LocalDateTime.now());
        return step;
    }
    
    @Override
    public IOCRRun getRun(String id) {
        return runDBClient.getById(id);
    }
    
    @Override
    public List<IPage> getPages(String runId) throws ImageFileDoesNotExistException {
        IOCRRun run = runDBClient.getById(runId);
        IImageFile file = imageManager.getByRunId(runId);
        if (file == null) {
            throw new ImageFileDoesNotExistException();
        }
        String runFolderPath = fileStorageManager.getAndCreateStoragePath(file.getUsername(), file.getId(), run.getId());
        File runFolder = new File(runFolderPath);
        File[] pageFolders = runFolder.listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isDirectory()) {
                    return false;
                }
                return pathname.getName().matches("[0-9]{4}");
            }
        });
        
        if (pageFolders == null) {
            return Collections.emptyList();
        }
        
        List<IPage> pages = new ArrayList<>();
        for (File folder : pageFolders) {
            Map<String, IPageLine> lines = new HashMap<>();
            IPage page = new Page();
            page.setPage(new Integer(folder.getName()));
            File[] lineFiles = folder.listFiles();
            if (lineFiles != null) {
                createLines(lines, lineFiles);
            }
            
            List<IPageLine> pageLines = new ArrayList<IPageLine>(lines.values());
            pageLines.sort(new Comparator<IPageLine>() {

                @Override
                public int compare(IPageLine o1, IPageLine o2) {
                    if (o1.getLineName() == null) {
                        return -1;
                    }
                    if (o2.getLineName() == null) {
                        return 1;
                    }
                    return Integer.parseInt(o1.getLineName(), 16) - Integer.parseInt(o2.getLineName(), 16);
                }
            });
            
            page.setLines(pageLines);
            
            // set corrections
            page.setCorrections(correctionsManager.getCorrections(file.getUsername(), file.getId(), runId, folder.getName()));
            pages.add(page);
        }
        
        return pages;
    }

    private void createLines(Map<String, IPageLine> lines, File[] lineFiles) {
        for (File line : lineFiles) {
            int idx = line.getName().indexOf(".");
            String lineName = line.getName().substring(0, idx);
            if (lines.get(lineName) == null) {
                lines.put(lineName, new PageLine());
                lines.get(lineName).setLineName(lineName);
            }
            
            
            if (line.getName().endsWith(".txt")) {
                byte[] fileContent;
                try {
                    fileContent = fileStorageManager.getFileContentFromUrl(line.toURI().toURL());
                } catch (IOException e) {
                    logger.error("Could not read file.", e);
                    continue;
                }
                lines.get(lineName).setText(new String(fileContent));
            } else if (line.getName().endsWith(".bin.png")) {
                lines.get(lineName).setImageFilename(line.getName());
            }
        }
    }
}
