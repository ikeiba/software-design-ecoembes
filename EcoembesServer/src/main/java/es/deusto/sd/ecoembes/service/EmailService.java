package es.deusto.sd.ecoembes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
    private JavaMailSender emailSender;
    
    @Value("${spring.mail.username}")
    private String sender;
 
	public EmailService(JavaMailSender emailSender) {
    	this.emailSender = emailSender;
    }
	
	@Async
    public void sendSimpleMessage(List<String>recipients, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage(); 
        System.out.println("Sending from: " + sender);
        message.setFrom(sender);
        message.setTo(recipients.toArray(new String[0])); // convierte la lista a String[]
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
}
