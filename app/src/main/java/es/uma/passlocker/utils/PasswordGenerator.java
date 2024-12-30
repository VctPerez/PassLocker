package es.uma.passlocker.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]|,./?><";

    public static String generate(Integer length, boolean upper, boolean digits, boolean special) {
        if (length == null || length <= 0) {
            throw new IllegalArgumentException("Password length must be a positive integer");
        }
        int minLen = getMinLen(upper, digits, special);
        if (length < minLen) {
            throw new IllegalArgumentException("Password length must be at least " + minLen);
        }

        String charset = buildCharset(upper, digits, special);
        SecureRandom random = new SecureRandom();
        List<Character> passwordBuilder = new ArrayList<>(length);

        // Always include at least one character from each group
        passwordBuilder.add(LOWER.charAt(random.nextInt(LOWER.length())));
        if (upper) {
            passwordBuilder.add(UPPER.charAt(random.nextInt(UPPER.length())));
        }
        if (digits) {
            passwordBuilder.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        if (special) {
            passwordBuilder.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        }

        // Fill the rest of the password with random characters from the charset
        for (int i = minLen; i < length; i++) {
            passwordBuilder.add(charset.charAt(random.nextInt(charset.length())));
        }

        // Shuffle the password
        Collections.shuffle(passwordBuilder);

        return passwordBuilder.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private static int getMinLen(boolean upper, boolean digits, boolean special) {
        int minLen = 1;
        minLen += (upper) ? 1 : 0;
        minLen += (digits) ? 1 : 0;
        minLen += (special) ? 1 : 0;
        return minLen;
    }

    private static String buildCharset(boolean upper, boolean digits, boolean special) {
        String charset = "";
        charset = charset + LOWER;
        charset = (upper) ? charset + UPPER : charset;
        charset = (digits) ? charset + DIGITS : charset;
        charset = (special) ? charset + SPECIAL : charset;
        return charset;
    }
}
