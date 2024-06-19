package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static com.april.furnitureapi.utils.EmailUtils.getVerificationURL;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String UTF_8 = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${VERIFY_EMAIL_HOST}")
    private String verifyHost;

    @Override
    public void sendVerificationEmail(User user, String token) {
        try{
            var context = new Context();
            context.setVariables(Map.of("name", user.getName(), "url", getVerificationURL(verifyHost, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            var message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            helper.setPriority(1);
            helper.setSubject("New user account verification");
            helper.setTo(user.getEmail());
            helper.setText(text, true);
            mailSender.send(message);
        } catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }

    }
}
