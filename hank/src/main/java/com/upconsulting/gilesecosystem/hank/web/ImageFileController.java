package com.upconsulting.gilesecosystem.hank.web;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.Principal;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.service.IModelManager;
import com.upconsulting.gilesecosystem.hank.web.forms.ImageFileForm;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;

@Controller
public class ImageFileController {
    
    @Autowired
    private IImageFileManager imageManager; 
    
    @Autowired
    private IFileStorageManager storageManager;
    
    @Autowired
    private IModelManager modelManager;


    @RequestMapping(value = "/files/image/{id}")
    public String showImageFile(Model model, @PathVariable String id, Principal principal) {
        IImageFile imageFile = imageManager.getImageFile(id);
        
        ImageFileForm p = new ImageFileForm();
        p.setImageFile(imageFile);
        p.setId(imageFile.getId());
        p.setFilename(imageFile.getFilename());
        p.setProcessingFolder(imageFile.getProcessingFolder());
        
        byte[] imageBytes = storageManager.getFileContent(principal.getName(), imageFile.getId(), null, imageFile.getFilename());
        p.setContent(imageBytes);
        
        String imageFolder = storageManager.getAndCreateStoragePath(imageFile.getUsername(), imageFile.getId(), null);
        File processFolder = new File(imageFolder + File.separator + imageFile.getProcessingFolder());
        String[] files = processFolder.list(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                if (new File(dir.getAbsolutePath() + File.separator + name).isDirectory()) {
                    return false;
                }
                return true;
            }
        });
        
        p.setProcessingFiles(files);
        
        File[] folders = processFolder.listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        
        if (folders != null) {
            for (File folder : folders) {
                p.getLineFolders().put(folder.getName(), folder.list());
            }
        }
        
        model.addAttribute("image", p);
        model.addAttribute("models", modelManager.getModels(principal.getName(), 0, 40));
        return "files/image";
    }
    
    @RequestMapping(value = "/files/image/{id}/content")
    public ResponseEntity<String> getImageContent(HttpServletResponse response, @PathVariable String id, Principal principal) {
        IImageFile file = imageManager.getImageFile(id);
        byte[] content = storageManager.getFileContent(principal.getName(), file.getId(), null, file.getFilename());
        
        response.setContentType(file.getContentType());
        //response.setContentLength(content.length);
        response.setHeader("Content-disposition", "filename=\"" + file.getFilename() + "\""); 
        try {
            response.getOutputStream().write(content);
            response.getOutputStream().close();
        } catch (IOException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/files/image/{id}/{runId}/text")
    public ResponseEntity<String> getText(HttpServletResponse response, @PathVariable String id, @PathVariable String runId, Principal principal) {
        IImageFile file = imageManager.getImageFile(id);
        
        IOCRRun foundRun = null;
        if (file.getOcrRuns() != null) {
            try  {
                foundRun = file.getOcrRuns().stream().filter(run -> run.getId().equals(runId)).findFirst().get();
            } catch (NoSuchElementException ex) {
                return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            }
        }
        
        if (foundRun != null) {
            byte[] content = storageManager.getFileContent(principal.getName(), file.getId(), foundRun.getId(), foundRun.getHocrFile());
            response.setHeader("Content-disposition", "filename=\"" + foundRun.getHocrFile() + "\""); 
            try {
                response.getOutputStream().write(content);
                response.getOutputStream().close();
            } catch (IOException e) {
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
}
