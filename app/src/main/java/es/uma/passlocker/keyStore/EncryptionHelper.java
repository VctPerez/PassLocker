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

    // Cifrar datos (contraseña)
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

    // Descifrar datos (contraseña)
    // Descifrar datos (contraseña)
    public static String decrypt(byte[] encryptedData) throws Exception {
        SecretKey key = KeyStoreHelper.getKey(); // Obtenemos la clave secreta

        // Extraemos el IV (primeros 12 bytes) y los datos cifrados (resto)
        byte[] iv = new byte[12]; // El IV tiene un tamaño fijo de 12 bytes en AES/GCM
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);

        byte[] encryptedText = new byte[encryptedData.length - iv.length];
        System.arraycopy(encryptedData, iv.length, encryptedText, 0, encryptedText.length);

        // Descifrar usando el IV extraído
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128 bits de autenticación
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        try {
            byte[] decryptedText = cipher.doFinal(encryptedText);
            return new String(decryptedText); // Devolver el texto descifrado
        } catch (Exception e) {
            // Lanzar excepción si la autenticación falla
            throw new RuntimeException("Error al descifrar los datos: " + e.getMessage());
        }
    }
}
