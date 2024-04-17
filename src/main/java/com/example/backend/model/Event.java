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
    @Column(nullable = false)
    private String id;
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Boolean isLimitSet;
    private int price;
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;
    @Column
    private boolean active = true;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Santa> santaList = new ArrayList<>();
}
