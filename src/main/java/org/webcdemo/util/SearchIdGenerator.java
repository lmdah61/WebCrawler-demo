package org.webcdemo.util;

import java.security.SecureRandom;

public class SearchIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateId() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        StringBuilder id = new StringBuilder();
        for (byte b : bytes) {
            id.append(CHARACTERS.charAt(b & 0x3F));
        }

        return id.toString();
    }
}
