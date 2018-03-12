package com.shankiya.prediictool.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
 
@Service
public class StorageService {
 
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	
	private final Path rootLocation = Paths.get("upload-dir");
	public static final String uploadingdir = System.getProperty("user.dir") + "/upload-dir/";
 
	public void store(MultipartFile uploadingFiles) {
		try {
			
		     File directory = new File(uploadingdir);
		     if (! directory.exists()){
		         directory.mkdirs();
		     }
			    
		     File file = new File(uploadingdir+ uploadingFiles.getOriginalFilename());
			 uploadingFiles.transferTo(file);
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}
 
	public Resource loadFile(String filename) {
		try {
			System.out.println("loadFile"+rootLocation);
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}
 
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
 
	public void init() {
		try {
			
			System.out.println("init"+rootLocation);
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}