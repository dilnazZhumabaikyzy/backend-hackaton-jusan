package com.example.backend.util;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class AuthenticationUtils {
    @Autowired
    private UserRepository userRepository;

    public User getUser(Authentication authentication) {
        long userId = 0;
        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();

            if (principal instanceof User user) {
                userId = user.getId();
            }
        }
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
