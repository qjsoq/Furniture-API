package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.User;

public interface EmailService {
    void sendVerificationEmail(User user);
}
