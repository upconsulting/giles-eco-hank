package com.upconsulting.gilesecosystem.hank.workflow.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upconsulting.gilesecosystem.hank.exceptions.DockerConnectionException;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.workflow.IOCRWorkflowManager;
import com.upconsulting.gilesecosystem.hank.workflow.IUploadManager;

import edu.asu.diging.gilesecosystem.util.exceptions.FileStorageException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Service
public class UploadManager implements IUploadManager {

    @Autowired
    private IOCRWorkflowManager workflowManager;
    
    @Autowired
    private IImageFileManager imageFileManager;
    
    @Autowired
    private IFileStorageManager fileStorageManager;
     
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.workflow.impl.IUploadManager#processFiles(java.lang.String, java.lang.String, org.springframework.web.multipart.MultipartFile[])
     */
    @Override
    public List<IImageFile> processFiles(String username, String modelId, MultipartFile[] files) throws FileStorageException, IOException, UnstorableObjectException, DockerConnectionException {
        
        List<IImageFile> imageFiles = new ArrayList<IImageFile>();
        for (MultipartFile f : files) {
            IImageFile file = new ImageFile(f.getOriginalFilename());
            file.setUsername(username);
            file = imageFileManager.storeOrUpdateImageFile(file);
           
            byte[] bytes = f.getBytes();
            fileStorageManager.saveFile(username, file.getId(), null, file.getFilename(), bytes);
            
            String contentType = null;
            
            if (bytes != null) {
               Tika tika = new Tika();
               contentType = tika.detect(bytes);
            }
            
            if (contentType == null) {
                contentType = f.getContentType();
            }
            
            file.setContentType(contentType);
            file.setSize(f.getSize());
            
            imageFiles.add(file);
           
            IOCRRun runInfo = workflowManager.startOCR(file, modelId);        

            file.getOcrRuns().add(runInfo);
            imageFileManager.storeOrUpdateImageFile(file);
        }
        
        return imageFiles;
    }
}
