package com.example.backend.repository;

import com.example.backend.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long> {
}
