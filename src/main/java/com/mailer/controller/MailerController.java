package com.mailer.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@PostMapping("/sendEmail")
	public ResponseEntity<Map<String,String>> sendEmail(@RequestBody Map<String,String> requestMap) {
		Map<String,String> responseMap = new HashMap<>();
		SimpleMailMessage message = emailService.createMessage(requestMap.get("EmailId"), requestMap.get("Subject"), requestMap.get("Body"));
	    try {
			emailService.sendEmail(message);
			responseMap.put("status", "success");
		    responseMap.put("message", "Mail Sended Successfully.");
		} catch (Exception e) {
			responseMap.put("status", "error");
		    responseMap.put("message", e.getStackTrace().toString());
		}
	    
	    return new ResponseEntity<Map<String,String>>(responseMap,HttpStatus.OK);
	}
	
	@PostMapping("/sendBulkEmail")
	public ResponseEntity<Map<String, String>> sendBulkEmail(@RequestBody Map<String, String> requestMap) {
	    Map<String, String> responseMap = new HashMap<>();
	    List<String> emailIds = Arrays.asList(requestMap.get("EmailIds").split(","));
	    SimpleMailMessage[] messages = emailIds.stream()
	            .map(recipient -> emailService.createMessage(recipient, requestMap.get("Subject"), requestMap.get("Body")))
	            .toArray(SimpleMailMessage[]::new);
	    try {
			emailService.sendBulkEmail(messages);
			responseMap.put("status", "success");
		    responseMap.put("message", "Mail Sended Successfully.");
		} catch (Exception e) {
			responseMap.put("status", "error");
		    responseMap.put("message", e.getStackTrace().toString());
		}
	    return ResponseEntity.ok(responseMap);
	}

	
	@GetMapping("/test")
	public String test() {
	    return "test";
	}
	
}
