package com.example.backend.auth;

import com.example.backend.config.JwtService;
import com.example.backend.exception.*;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws DuplicateKeyException {
        validateEmail(request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Такая почта уже существует");
        }
        var user = User.builder()
                .email(request.getEmail())
                .fullName(NicknameGenerator.generateNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void validateEmail(String email) throws InvalidEmailFormatException {;
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailFormatException(email);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new IncorrectPasswordException("Такой почты не существует");
        }
        String currentPassword = userRepository.getUserByEmail(request.getEmail()).get().getPassword();
        if (!passwordEncoder.matches(request.getPassword(), currentPassword)) {
            throw new IncorrectPasswordException("Вы ввели неправильный пароль, попробуйте еще раз");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String createPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email));
        String randomPassword = generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(randomPassword));

        userRepository.save(user);

        return randomPassword;
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
