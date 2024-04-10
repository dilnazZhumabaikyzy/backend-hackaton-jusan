package com.example.backend.repository;

import com.example.backend.model.Santa;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SantaRepository extends JpaRepository<Santa, Long> {
    Santa getSantaBySantaUserAndEvent_Id(User user, String eventID);
}
