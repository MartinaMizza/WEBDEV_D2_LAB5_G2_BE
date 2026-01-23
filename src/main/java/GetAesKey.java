import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class GetAesKey {
    public static void main(String[] args) throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Chiave AES (Base64): " + base64Key);
    }
}
