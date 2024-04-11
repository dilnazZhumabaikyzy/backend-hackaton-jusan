package com.example.backend.repository;

import com.example.backend.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageData, Long> {

}
