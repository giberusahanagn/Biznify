package com.biznify.warehouse.serviceImplimentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.service.EmailService;

@Service
public class EmailServiceImplimentation implements EmailService {

	  @Autowired
	    private JavaMailSender mailSender;

	    @Override
	    public void sendEmail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("sahanagn275@gmail.com"); // same as configured
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }
}
