package com.example.urbanmarket.utils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailSender {
    private final JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String mailAddress;
    @Autowired
    public MailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public String send(String mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Welcome from Urban Zen marketplace!");
        simpleMailMessage.setText("Hello from Urban Zen marketplace, Happy to see you in our marketplace!");
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setFrom(mailAddress);
        javaMailSender.send(simpleMailMessage);
        return "Mail sent successfully";
    }
}