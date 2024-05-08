package com.mailer.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(Object message,boolean attachment) {
    	if (attachment) {
            MimeMessage mimeMessage = (MimeMessage) message;
            emailSender.send(mimeMessage);
        } else {
            SimpleMailMessage simpleMailMessage = (SimpleMailMessage) message;
            emailSender.send(simpleMailMessage);
        }
    }
    
    public SimpleMailMessage createSimpleMessage(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
    
    public MimeMessage createMimeMessage(String emailId,String subject, String body,String attachmentUrl) throws Exception {
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
    	helper.setTo(emailId);
        helper.setSubject(subject);
        UrlResource attachmentResource = new UrlResource(new URL(attachmentUrl));
        String extension = getFileExtensionFromUrlResource(attachmentResource);
        if(extension.isEmpty()) {
        	body+="\n"+attachmentUrl;
        	helper.setText(body);
        }else {
        	String attachmentFileName = attachmentResource.getFilename().toString();
            helper.addAttachment(attachmentFileName, attachmentResource);
            helper.setText(body);
        }
        return message;
    }
    
    public Object createMessage(String emailId,String subject,String body,String attachmentUrl,boolean hasAttachment) {
		Object message = null;
		if(hasAttachment) {
			try {
				message = createMimeMessage(emailId, subject,body,attachmentUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			message = createSimpleMessage(emailId,subject,body);
		}
		return message;
    }
    
    public void sendBulkEmail(SimpleMailMessage[] message) {
        emailSender.send(message);
    }
    
    public static String getFileExtensionFromUrlResource(UrlResource urlResource) {
        String filename = urlResource.getFilename();
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}
