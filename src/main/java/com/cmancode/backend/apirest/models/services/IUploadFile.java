package com.cmancode.backend.apirest.models.services;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadFile {
	
	public boolean uploadFile(MultipartFile file, String fileName);
	public boolean deleteFile(String fileName);

}
