package com.example.urbanmarket.config.mail.service;

public interface EmailService {

    void sendLetter(String to, String subject, String text);

    void sendVerificationEmailLetter(String to, String emailVerificationCode);

    void sendVerificationPasswordLetter(String to, String passwordVerificationCode);
}