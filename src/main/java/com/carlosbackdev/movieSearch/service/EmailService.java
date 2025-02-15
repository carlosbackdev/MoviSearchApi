
package com.carlosbackdev.movieSearch.service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.mail.javamail.JavaMailSender; 
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service; 
import jakarta.mail.MessagingException; 
import jakarta.mail.internet.MimeMessage;  


@Service 

public class EmailService { 
 
    @Autowired 
    private JavaMailSender mailSender; 


    public void enviarCorreo(String destinatario, String asunto, String mensaje) throws MessagingException { 
        MimeMessage mail = mailSender.createMimeMessage(); 
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(destinatario); 
        helper.setSubject(asunto); 
        helper.setText(mensaje, true); 
        mailSender.send(mail); 
    } 
} 