package com.mailer.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mailer.service.EmailService;

@RestController
@RequestMapping("/m")
public class MailerController {
	
	@Autowired
    private EmailService emailService;
	
	@SuppressWarnings("unchecked")
	@PostMapping("/sendEmail")
	public ResponseEntity<Map<String,String>> sendEmail(@RequestBody Map<String,Object> requestMap){
		Map<String,String> responseMap = new HashMap<>();
		List<String> attachments = requestMap.get("attachments")!=null?(List<String>)requestMap.get("attachments"):new ArrayList<String>();
		List<String> emailIds = requestMap.get("emailIds")!=null?(List<String>)requestMap.get("emailIds"):new ArrayList<String>();
		String subject = requestMap.get("subject")!=null?(String)requestMap.get("subject"):"";
		String body = requestMap.get("body")!=null?(String)requestMap.get("body"):"";
		MimeMessage[] message=null;
		if(emailIds.size()>0) {
			try {
				message = emailService.createMessage(emailIds,subject,body,attachments);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				emailService.sendEmail(message);
				responseMap.put("status", "success");
				responseMap.put("message", "Mail Sended Successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				responseMap.put("status", "error");
				responseMap.put("message", e.getStackTrace().toString());
			}
		}else {
			responseMap.put("status", "error");
			responseMap.put("message", "Please specify atleast one recepient.");
		}
	    
	    return new ResponseEntity<Map<String,String>>(responseMap,HttpStatus.OK);
	}

	
	@GetMapping("/test")
	public String test() {
	    return "test";
	}
	
}
