package es.uma.passlocker.keyStore;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyStore;

public class KeyStoreHelper {

    private static final String KEY_ALIAS = "PassLockerAlias";  // Alias de la clave almacenada en Keystore
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    /**
     * Generate AES key and store it in the Android Keystore
     * @throws Exception
     */
    public static void generateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (keyStore.containsAlias(KEY_ALIAS)) {
            return;
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
        keyGenerator.init(
                new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
        );
        keyGenerator.generateKey();
    }

    /**
     * Get the AES key from the Android Keystore
     * @return the AES key
     * @throws Exception if the key is not found
     */
    public static SecretKey getKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        //clearKey();
        generateKey();
        // Asegúrate de que la clave esté en el Keystore y sea del tipo adecuado
        KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);

        if (entry instanceof KeyStore.SecretKeyEntry) {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        } else {
            throw new Exception("No se encontró una clave secreta en el Keystore.");
        }
    }

    /**
     * Delete the AES key from the Android Keystore
     * @throws Exception
     */
    public static void clearKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.deleteEntry(KEY_ALIAS);  // Eliminar la clave del Keystore
        }
    }
}
