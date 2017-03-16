package com.upconsulting.gilesecosystem.hank.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upconsulting.gilesecosystem.hank.db.IOCRRunDBClient;
import com.upconsulting.gilesecosystem.hank.exceptions.ImageFileDoesNotExistException;
import com.upconsulting.gilesecosystem.hank.exceptions.ZipFileGenerationException;
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.upconsulting.gilesecosystem.hank.service.impl.IOCRRunManager#saveOCRRun
     * (com.upconsulting.gilesecosystem.hank.model.IOCRRun)
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
    public List<IPage> getPages(String runId, String correctionId)
            throws ImageFileDoesNotExistException {
        IOCRRun run = runDBClient.getById(runId);
        IImageFile file = imageManager.getByRunId(runId);
        if (file == null) {
            throw new ImageFileDoesNotExistException();
        }

        String runFolderPath = fileStorageManager.getAndCreateStoragePath(
                file.getUsername(), file.getId(), run.getId());
        File fileFolder = new File(runFolderPath);

        if (correctionId != null) {
            fileFolder = correctionsManager.getCorrectionsFolder(file.getUsername(),
                    file.getId(), run.getId());
        }

        File[] pageFolders = fileFolder.listFiles(new FileFilter() {

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
            pages.add(createPage(runId, file, folder));
        }

        return pages;
    }

    @Override
    public File zipOCRFolder(String runId) throws ImageFileDoesNotExistException, ZipFileGenerationException {
        IOCRRun run = runDBClient.getById(runId);
        IImageFile file = imageManager.getByRunId(runId);
        if (file == null) {
            throw new ImageFileDoesNotExistException();
        }

        String runFolderPath = fileStorageManager.getAndCreateStoragePath(
                file.getUsername(), file.getId(), run.getId());
        
        String imageFileFolder = fileStorageManager.getAndCreateStoragePath(file.getUsername(), file.getId(), null);
        
        File runFolder = new File(runFolderPath);
        
        try {
            String zipFilePath = imageFileFolder + File.separator + runId + ".zip";
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            addToZip(runFolder.getAbsolutePath(), runFolder, zos);
            
            zos.close();
            fos.close();
            
            return new File(zipFilePath);
        } catch (FileNotFoundException e) {
            throw new ZipFileGenerationException("Could not create ZIP file.", e);
        } catch (IOException e) {
            throw new ZipFileGenerationException("Could not create ZIP file.", e);
        }
    }
    
    /**
     * Add files to zip file.
     * @param zipDirPath Absolute path to the directory that is being zipped up.
     * @param dirToAddToZip {@link File} representing the directory to be zipped up.
     * @param zipFile {@link ZipInputStream} for final zip file.
     * @throws IOException
     */
    private void addToZip(String zipDirPath, File dirToAddToZip, ZipOutputStream zipFile) throws IOException {
         
        File[] content = dirToAddToZip.listFiles();
        
        for (File file : content) {
            if (file.isDirectory()) {
                addToZip(zipDirPath, file, zipFile);
            } else {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry fileEntry = new ZipEntry(file.getAbsolutePath().substring(zipDirPath.length()));
                zipFile.putNextEntry(fileEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipFile.write(bytes, 0, length);
                }

                zipFile.closeEntry();
                fis.close();
            }
        }
    }

    private IPage createPage(String runId, IImageFile file, File folder) {
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
                return Integer.parseInt(o1.getLineName(), 16)
                        - Integer.parseInt(o2.getLineName(), 16);
            }
        });

        page.setLines(pageLines);

        // set corrections
        page.setCorrection(correctionsManager.getCorrections(file.getUsername(),
                file.getId(), runId, folder.getName()));
        return page;
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
                    fileContent = fileStorageManager.getFileContentFromUrl(line.toURI()
                            .toURL());
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
