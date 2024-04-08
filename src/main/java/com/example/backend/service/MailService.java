package com.example.backend.service;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendRecoveryMail(String password, String email);
}
