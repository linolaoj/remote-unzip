package com.liferay.sopapo.hotfixdata.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationUtil {
    public static String getAuthorization(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
