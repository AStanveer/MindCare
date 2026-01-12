package com.teamspring.MindCare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    public String storeFile(MultipartFile file, String subdirectory) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }
        
        // Validate file size (5MB max)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IOException("File size exceeds maximum limit of 5MB");
        }
        
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("Only image files are allowed");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, subdirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Copy file to upload directory
        Path targetLocation = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        // Return relative URL
        return "/" + uploadDir + "/" + subdirectory + "/" + newFilename;
    }
    
    public void deleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            try {
                Path filePath = Paths.get(fileUrl.substring(1)); // Remove leading slash
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log error but don't throw exception
                e.printStackTrace();
            }
        }
    }
}