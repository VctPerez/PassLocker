package es.uma.passlocker.keyStore;

import javax.crypto.Cipher;
import java.security.KeyStore;
import java.security.PublicKey;

public class EncryptionHelper {

    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    // Cifrar datos (contraseña)
    public static byte[] encrypt(String data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Obtener la clave pública del KeyStore
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

        // Cifrar usando la clave pública
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data.getBytes());
    }
}
