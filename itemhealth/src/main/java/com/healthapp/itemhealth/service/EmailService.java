package com.healthapp.itemhealth.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import  org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage mes = new SimpleMailMessage();
        mes.setFrom(fromEmail);
        mes.setTo(to);
        mes.setSubject(subject);
        mes.setText(body);
        mailSender.send(mes);
    }

     public String getBossEmail(){
        //to impliment
        return "totest@gmail.com";
    }

    public String formatEmailSubject(){
        return "Alert for " + LocalDateTime.now();
    }


    public String formatEmailBody(List items){
        return "test";
    }

}



