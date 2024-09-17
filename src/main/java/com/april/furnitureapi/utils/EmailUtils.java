package com.april.furnitureapi.utils;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.AUTH;

public class EmailUtils {
    public static String getVerificationUrl(String host, String token) {
        return host + API + AUTH + "/?token=" + token;
    }
}
