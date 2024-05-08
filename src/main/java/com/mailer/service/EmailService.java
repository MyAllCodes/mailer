package com.mailer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(SimpleMailMessage message) {
        emailSender.send(message);
    }
    
    public SimpleMailMessage createMessage(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
    
    public void sendBulkEmail(SimpleMailMessage[] message) {
        emailSender.send(message);
    }
}
