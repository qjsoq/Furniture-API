package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${EMAIL_USERNAME}")
    private String fromMail;
    @Override
    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Verify your new account");
        message.setTo(user.getEmail());
        message.setFrom(fromMail);
        message.setText("Yeees, it works");
        mailSender.send(message);
    }
}
