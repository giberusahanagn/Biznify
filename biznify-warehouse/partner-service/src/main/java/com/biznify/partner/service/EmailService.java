package com.biznify.partner.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}