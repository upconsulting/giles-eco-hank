package com.upconsulting.gilesecosystem.hank.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.upconsulting.gilesecosystem.hank.db.IImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;

public class ImageFileManagerTest {

    @Mock
    private IImageFileDBClient mockedDbClient;
    
    @InjectMocks
    private ImageFileManager managerToTest;
    
    private String CONTENT_TYPE = "contentType";
    
    private String IMAGE_FILE_ID1 = "IMG1";
    private String FILENAME1 = "filename1";
    private String PROC_FOLDER1 = "procFolder1";
    private String UPLOAD_ID1 = "UP1";
    private long SIZE1 = 1L;
    private String USERNAME1 = "user1";
    
    private String IMAGE_FILE_ID2 = "IMG2";
    private String FILENAME2 = "filename2";
    private String PROC_FOLDER2 = "procFolder2";
    private String UPLOAD_ID2 = "UP2";
    private long SIZE2 = 2L;
    
    private IImageFile imageFile1;
    private IImageFile imageFile2;
    
    
    @Before
    public void setUp() {
        managerToTest = new ImageFileManager();
        MockitoAnnotations.initMocks(this);
        
        imageFile1 = new ImageFile();
        imageFile1.setId(IMAGE_FILE_ID1);
        imageFile1.setContentType(CONTENT_TYPE);
        imageFile1.setFilename(FILENAME1);
        imageFile1.setProcessingFolder(PROC_FOLDER1);
        imageFile1.setSize(SIZE1);
        imageFile1.setStatus(WorkflowStatus.BINARIZED);
        imageFile1.setUploadId(UPLOAD_ID1);
        imageFile1.setUsername(USERNAME1);

        imageFile2 = new ImageFile();
        imageFile2.setId(IMAGE_FILE_ID2);
        imageFile2.setContentType(CONTENT_TYPE);
        imageFile2.setFilename(FILENAME2);
        imageFile2.setProcessingFolder(PROC_FOLDER2);
        imageFile2.setSize(SIZE2);
        imageFile2.setStatus(WorkflowStatus.BINARIZED);
        imageFile2.setUploadId(UPLOAD_ID2);
        imageFile2.setUsername(USERNAME1);
        
        Mockito.when(mockedDbClient.getFileById(IMAGE_FILE_ID1)).thenReturn(imageFile1);
    }
    
    @Test
    public void test_getImageFile_success() {
        IImageFile imageFile = managerToTest.getImageFile(IMAGE_FILE_ID1);
        Assert.assertNotNull(imageFile);
        Assert.assertEquals(IMAGE_FILE_ID1, imageFile.getId());
        Assert.assertEquals(CONTENT_TYPE, imageFile.getContentType());
        Assert.assertEquals(FILENAME1, imageFile.getFilename());
        Assert.assertEquals(PROC_FOLDER1, imageFile.getProcessingFolder());
        Assert.assertEquals(SIZE1, imageFile.getSize());
        Assert.assertEquals(WorkflowStatus.BINARIZED, imageFile.getStatus());
        Assert.assertEquals(UPLOAD_ID1, imageFile.getUploadId());
        Assert.assertEquals(USERNAME1, imageFile.getUsername());
    }
    
    @Test
    public void test_getImageFile_noResult() {
        IImageFile imageFile = managerToTest.getImageFile("ID2");
        Assert.assertNull(imageFile);
    }
    
    @Test
    public void test_getImageFiles_success() {
        List<ImageFile> files = new ArrayList<ImageFile>();
        files.add((ImageFile)imageFile1);
        files.add((ImageFile)imageFile2);
        Mockito.when(mockedDbClient.getImageFiles(USERNAME1)).thenReturn(files);
        
        List<IImageFile> results = managerToTest.getImageFiles(USERNAME1);
        Assert.assertEquals(2, results.size());
        Collections.sort(results, new Comparator<IImageFile>() {

            @Override
            public int compare(IImageFile o1, IImageFile o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        
        {
            IImageFile imageFile = results.get(0);
            Assert.assertEquals(IMAGE_FILE_ID1, imageFile.getId());
            Assert.assertEquals(CONTENT_TYPE, imageFile.getContentType());
            Assert.assertEquals(FILENAME1, imageFile.getFilename());
            Assert.assertEquals(PROC_FOLDER1, imageFile.getProcessingFolder());
            Assert.assertEquals(SIZE1, imageFile.getSize());
            Assert.assertEquals(WorkflowStatus.BINARIZED, imageFile.getStatus());
            Assert.assertEquals(UPLOAD_ID1, imageFile.getUploadId());
            Assert.assertEquals(USERNAME1, imageFile.getUsername());
        }
        
        {
            IImageFile imageFile = results.get(1);
            Assert.assertEquals(IMAGE_FILE_ID2, imageFile.getId());
            Assert.assertEquals(CONTENT_TYPE, imageFile.getContentType());
            Assert.assertEquals(FILENAME2, imageFile.getFilename());
            Assert.assertEquals(PROC_FOLDER2, imageFile.getProcessingFolder());
            Assert.assertEquals(SIZE2, imageFile.getSize());
            Assert.assertEquals(WorkflowStatus.BINARIZED, imageFile.getStatus());
            Assert.assertEquals(UPLOAD_ID2, imageFile.getUploadId());
            Assert.assertEquals(USERNAME1, imageFile.getUsername());
        }
    }
}
