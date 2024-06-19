package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
}
