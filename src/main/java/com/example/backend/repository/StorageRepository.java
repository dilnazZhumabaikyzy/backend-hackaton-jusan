package com.example.backend.repository;

import com.example.backend.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);
}