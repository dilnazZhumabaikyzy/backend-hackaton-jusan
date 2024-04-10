package com.example.backend.repository;

import com.example.backend.model.Event;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findEventsByOwner(User user);

    Event findEventById(String id);
}
