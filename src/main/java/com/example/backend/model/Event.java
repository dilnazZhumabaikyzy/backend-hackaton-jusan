package com.example.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {
    @Id
    private String id;
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Boolean isLimitSet;
    private int price;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "event")
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Santa> santaList = new ArrayList<>();
}
