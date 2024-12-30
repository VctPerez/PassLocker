package es.uma.passlocker.keyStore;

import java.security.MessageDigest;

public class HashHelper {


    /**
     * Generate a SHA-256 hash from the given data
     * @param data the data to hash
     * @return the hash as a hexadecimal string
     * @throws Exception if the hash algorithm is not found
     */
    public static String generateHash(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);

        // Convertir el hash a una representación en texto hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
