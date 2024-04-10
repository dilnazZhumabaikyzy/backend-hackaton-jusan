package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "santa")
public class Santa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "santa_id")
    private User santaUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverUser;


    public Santa(Event event, User user1, User user2) {
        this.event = event;
        this.santaUser = user1;
        this.receiverUser = user2;
    }
}
