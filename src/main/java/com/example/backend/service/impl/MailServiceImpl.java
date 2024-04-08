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
        String emailBody = "Ваш пароль был сброшен. Новый пароль: "+password+"\n\n" +
                "После входа в систему, пожалуйста, смените его для безопасности вашей учетной записи.\n\n" +
                "Если это действие не было выполнено вами, обратитесь в службу поддержки.\n\n" +
                "Спасибо.\n\n" +
                "Secret Santa Inc";
        sendSimpleMessage(email, "Новый пароль для вашей учетной записи Secret Santa", emailBody);
    }
}
