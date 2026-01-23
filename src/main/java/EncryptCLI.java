import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptCLI {

    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;

    private static final Dotenv dotenv = Dotenv.load();

    //Via Cesare Battisti 1315, Cislago, 21040 VA
    public static void main(String[] args) throws Exception {
        String plaintext = "VA";

        // stessa chiave dell'app
        String base64Key = dotenv.get("AES_KEY");
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_SIZE, iv));

        byte[] encrypted = cipher.doFinal(plaintext.getBytes());

        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        String result = Base64.getEncoder().encodeToString(combined);
        System.out.println(result);
    }
}
