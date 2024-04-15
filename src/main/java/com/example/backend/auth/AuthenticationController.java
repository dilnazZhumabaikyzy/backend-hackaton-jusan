package com.example.backend.auth;

import com.example.backend.dto.RequestDto;
import com.example.backend.service.impl.MailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final MailServiceImpl mailService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, MailServiceImpl mailService) {
        this.authenticationService = authenticationService;
        this.mailService = mailService;
    }


    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/password-recovery")
    public ResponseEntity<Void> passwordRecovery(@RequestBody RequestDto requestDto) {
        mailService.sendRecoveryMail(authenticationService.createPassword(requestDto.getEmail()), requestDto.getEmail());
        return ResponseEntity.ok().build();
    }
}
