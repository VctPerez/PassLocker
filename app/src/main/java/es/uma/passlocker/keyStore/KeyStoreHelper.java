package es.uma.passlocker.keyStore;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyPairGenerator;
import java.security.KeyStore;

public class KeyStoreHelper {

    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    // Inicializar KeyStore y generar la clave si no existe
    public static void generateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        // Si ya existe, no es necesario generar la clave nuevamente
        if (keyStore.containsAlias(KEY_ALIAS)) {
            return;
        }

        // Generar una clave RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE);
        keyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
        );
        keyPairGenerator.generateKeyPair();
    }
}

