package com.example.backend.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl {
    private final JavaMailSender emailSender;

    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void sendRecoveryMail(String password, String email) {
        String emailBody = "Your password has been reset. New password: "+password+"\n\n" +
                "After logging in, please change it for the security of your account.\n\n" +
                "If this action has not been performed by you, contact customer support.\n\n" +
                "Thank you.\n\n" +
                "Secret Santa Inc";
        sendSimpleMessage(email, "A new password for your Secret Santa account", emailBody);
    }
}
