package com.mailer.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(MimeMessage[] message) {
		emailSender.send(message);
	}

	public MimeMessage[] createMimeMessage(List<String> emailIds, String subject, String body, List<String> attachments)
            throws MalformedURLException, Exception, MessagingException {
        MimeMessage[] messages = new MimeMessage[emailIds.size()];
        AtomicInteger count = new AtomicInteger(emailIds.size()-1);
        emailIds.stream().forEach(emailId -> {
            MimeMessage message = emailSender.createMimeMessage();
            final MimeMessageHelper[] helper = {null};
            try {
                helper[0] = new MimeMessageHelper(message, true);
                helper[0].setTo(emailId);
                helper[0].setSubject(subject);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            StringBuilder modifiedBody = new StringBuilder(body);
            if(attachments.size()>0) {
            	attachments.stream().forEach(attachment -> {
            		UrlResource attachmentResource = null;
            		try {
            			attachmentResource = new UrlResource(new URL(attachment));
            		} catch (MalformedURLException e) {
            			e.printStackTrace();
            		}
            		String extension = getFileExtensionFromUrlResource(attachmentResource);
            		if (extension.isEmpty()) {
            			modifiedBody.append("\n").append(attachment);
            		} else {
            			String attachmentFileName = attachmentResource.getFilename().toString();
            			try {
            				helper[0].addAttachment(attachmentFileName, attachmentResource);
            			} catch (MessagingException e) {
            				e.printStackTrace();
            			}
            		}
            	});
            }
            try {
                helper[0].setText(modifiedBody.toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if (count.get() >= 0) {
                messages[(emailIds.size() - 1) - count.get()] = message;
                count.decrementAndGet();
            }
        });
        return messages;
    }


	public MimeMessage[] createMessage(List<String> emailIds, String subject, String body, List<String> attachments) throws Exception{
		MimeMessage[] message = createMimeMessage(emailIds, subject, body, attachments);
		return message;
	}

	public static String getFileExtensionFromUrlResource(UrlResource urlResource) {
		String filename = urlResource.getFilename();
		if (filename != null && filename.contains(".")) {
			return filename.substring(filename.lastIndexOf("."));
		}
		return "";
	}
}
