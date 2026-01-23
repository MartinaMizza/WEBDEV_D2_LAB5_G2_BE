package it.packovery.service;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptoService {

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12; // GCM standard
    private static final int TAG_SIZE = 128;
    private static final Dotenv dotenv = Dotenv.load();

    private SecretKey key;
    private final SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    void init() {
        String base64Key = dotenv.get("AES_KEY");
        if (base64Key == null) {
            throw new RuntimeException("Variabile AES_KEY non impostata!");
        }
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
    }

    public String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        secureRandom.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes());

        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String encryptedBase64) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedBase64);

        byte[] iv = new byte[IV_SIZE];
        byte[] cipherText = new byte[combined.length - IV_SIZE];
        System.arraycopy(combined, 0, iv, 0, IV_SIZE);
        System.arraycopy(combined, IV_SIZE, cipherText, 0, cipherText.length);

        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decrypted = cipher.doFinal(cipherText);
        return new String(decrypted);
    }
}
