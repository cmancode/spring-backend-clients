package com.cmancode.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadFileImpl implements IUploadFile {

	
	@Override
	public boolean uploadFile(MultipartFile file, String fileName) {

		Path ruta = Paths.get("files").resolve(fileName).toAbsolutePath();
		try {
			Files.copy(file.getInputStream(), ruta);
		} catch (IOException e) {
			log.info("Causa: "+e.getCause());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteFile(String fileName) {
		
		Path ruta = Paths.get("files").resolve(fileName).toAbsolutePath();
		File file = ruta.toFile();
		if(file.exists() && file.canRead()) {
			file.delete();
			log.info("Imagen eliminada con éxito");
			return true;
		}
		return false;
	}

}
