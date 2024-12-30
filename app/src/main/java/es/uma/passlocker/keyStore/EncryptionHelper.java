package es.uma.passlocker.keyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.KeyStore;
import java.security.PublicKey;

public class EncryptionHelper {

    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding"; // Usamos AES/GCM

    /**
     * Encrypt data (password)
     * @param data the data to encrypt
     * @return the encrypted data
     * @throws Exception if the encryption fails
     */
    public static byte[] encrypt(String data) throws Exception {
        SecretKey key = KeyStoreHelper.getKey(); // Obtenemos la clave secreta

        // Cifrar usando AES/GCM (el IV será generado automáticamente)
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV(); // El IV generado automáticamente
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        // Concatenar el IV y los datos cifrados
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        return combined; // Devolver los datos cifrados + IV concatenados
    }

    /**
     * Decrypt data (password)
     * @param encryptedData the data to decrypt
     * @return the decrypted data
     * @throws Exception if the decryption fails
     */
    public static String decrypt(byte[] encryptedData) throws Exception {
        SecretKey key = KeyStoreHelper.getKey();


        byte[] iv = new byte[12];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);

        byte[] encryptedText = new byte[encryptedData.length - iv.length];
        System.arraycopy(encryptedData, iv.length, encryptedText, 0, encryptedText.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128 bits de autenticación
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        try {
            byte[] decryptedText = cipher.doFinal(encryptedText);
            return new String(decryptedText);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar los datos: " + e.getMessage());
        }
    }
}
