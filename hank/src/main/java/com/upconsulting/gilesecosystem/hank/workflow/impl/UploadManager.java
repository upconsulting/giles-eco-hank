package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.exceptions.UnknownObjectTypeException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.impl.ImageFileManager;
import com.upconsulting.gilesecosystem.hank.workflow.IOCRWorkflowManager;
import com.upconsulting.gilesecosystem.hank.workflow.IUploadManager;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Service
public class UploadManager implements IUploadManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOCRWorkflowManager workflowManager;

    @Autowired
    private IImageFileManager imageFileManager;

    @Autowired
    private IFileStorageManager fileStorageManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.upconsulting.gilesecosystem.hank.workflow.impl.IUploadManager#
     * processFiles(java.lang.String, java.lang.String,
     * org.springframework.web.multipart.MultipartFile[])
     */
    @Override
    public List<IImageFile> processFiles(String username, String modelId,
            MultipartFile[] files) throws FileStorageException, IOException,
            UnstorableObjectException, DockerConnectionException {

        List<IImageFile> imageFiles = new ArrayList<IImageFile>();
        for (MultipartFile f : files) {
            IImageFile file = new ImageFile(f.getOriginalFilename());
            file.setUsername(username);
            file = imageFileManager.storeOrUpdateImageFile(file);

            String contentType = null;
            byte[] bytes = f.getBytes();
            if (bytes != null) {
                Tika tika = new Tika();
                contentType = tika.detect(bytes);
            }
            
            // is it a zip?
            if (f.getName().endsWith("zip") || f.getName().endsWith("ZIP") || contentType.equals("application/zip")) {
                String imageFolder = fileStorageManager.getAndCreateStoragePath(username, file.getId(), ImageFileManager.IMAGE_FOLDER);
                unzip(bytes, imageFolder);
            } else {
                fileStorageManager.saveFile(username, file.getId(),
                        ImageFileManager.IMAGE_FOLDER, file.getFilename(), bytes);
            }
            
            
            if (contentType == null) {
                contentType = f.getContentType();
            }

            file.setContentType(contentType);
            file.setSize(f.getSize());

            imageFiles.add(file);

            IOCRRun runInfo;
            try {
                runInfo = workflowManager.startOCR(file, modelId);
            } catch (UnknownObjectTypeException e) {
                logger.error("Could not store task object.", e);
                continue;
            }

            file.getOcrRuns().add(runInfo);
            imageFileManager.storeOrUpdateImageFile(file);
        }

        return imageFiles;
    }

    /**
     * Taken from: https://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/
     * @param zipFileBytes
     * @param destDir
     * @throws IOException
     */
    private void unzip(byte[] zipFileBytes, String destDir) throws IOException {
         InputStream fis;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        fis = new ByteArrayInputStream(zipFileBytes);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            // let's ignore folders
            if (ze.isDirectory()) {
                ze = zis.getNextEntry();
                continue;
            }
            String fileName = ze.getName();
            
            File newFile = new File(destDir + File.separator + fileName.substring(fileName.lastIndexOf(File.separator)));

            // create directories for sub directories in zip
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            // close this ZipEntry
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        // close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }
}
