package com.example.backend.service;
import com.example.backend.model.ImageData;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    ImageData uploadImage(MultipartFile file) throws IOException;
    byte[] downloadImage(long imageDataId);
}
