package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String save(MultipartFile file, String type, Long ownerId) throws Exception;
}
