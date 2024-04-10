package com.example.backend.auth;

import com.example.backend.config.JwtService;
import com.example.backend.exception.DuplicateKeyException;
import com.example.backend.exception.IncorrectPasswordException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) throws DuplicateKeyException {

            if(userRepository.existsByEmail(request.getEmail())){
                throw new DuplicateKeyException("Such email already exists");
            }
           var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if(!userRepository.existsByEmail(request.getEmail())) {
            throw new IncorrectPasswordException("Such email does not exist");
        }
        String currentPassword = userRepository.getUserByEmail(request.getEmail()).getPassword();
        if(!passwordEncoder.matches(request.getPassword(), currentPassword)){
            throw new IncorrectPasswordException("Вы ввели неправильный пароль, попробуйте еще раз");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        System.out.println("authenticated!");
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String createPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String randomPassword = generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(randomPassword));

        userRepository.save(user);

        return  randomPassword;
    }


    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
