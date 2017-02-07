package com.upconsulting.gilesecosystem.hank.web;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upconsulting.gilesecosystem.hank.db.impl.ImageFileDBClient;
import com.upconsulting.gilesecosystem.hank.model.IImageFile;
import com.upconsulting.gilesecosystem.hank.model.impl.ImageFile;
import com.upconsulting.gilesecosystem.hank.service.IImageFileManager;
import com.upconsulting.gilesecosystem.hank.web.forms.ImageFileForm;

import edu.asu.diging.gilesecosystem.util.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.util.store.IPropertiesCopier;

@Controller
public class ListImageFilesController {
    
    @Autowired
    private IImageFileManager imageFileManager;
    
    @Autowired
    private IPropertiesCopier copier;
    
    @Autowired
    private IFileStorageManager storageManager;

    @RequestMapping(value = "/files/uploads")
    public String showUploads(Principal principal, Model model) {
        List<IImageFile> files = imageFileManager.getImageFiles(principal.getName());
        List<ImageFileForm> pages = new ArrayList<ImageFileForm>();
        files.forEach(f -> pages.add(createPage(f)));
        model.addAttribute("files", pages);
        return "files/uploads";
    }
    
    private ImageFileForm createPage(IImageFile file) {
        ImageFileForm p = new ImageFileForm(); 
        //copier.copyObject(file, p);
        p.setId(file.getId());
        p.setProcessingFolder(file.getProcessingFolder());
        
        String imageFolder = storageManager.getAndCreateStoragePath(file.getUsername(), file.getId(), null);
        File processFolder = new File(imageFolder + File.separator + file.getProcessingFolder());
        String[] files = processFolder.list();
        p.setProcessingFiles(files);
        return p;
    }
}
