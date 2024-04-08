package com.example.backend.service.impl;

import com.example.backend.model.ImageData;
import  com.example.backend.repository.StorageRepository;
import com.example.backend.service.StorageService;
import  com.example.backend.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepository repository;

    public ImageData uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());

        return imageData;
    }
    public byte[] downloadImage(long imageDataId){
        Optional<ImageData> dbImageData = repository.findById(imageDataId);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

}