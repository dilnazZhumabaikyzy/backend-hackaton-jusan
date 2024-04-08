package com.example.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private  String fullName;
    private String email;
    @JsonIgnore
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ImageData imageData;

    @OneToMany(mappedBy = "owner")
    List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "santaUser")
    List<Santa> santas = new ArrayList<>();

    @OneToMany(mappedBy = "receiverUser")
    List<Santa> receivers = new ArrayList<>();

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword(){
        return password;
    }
}
