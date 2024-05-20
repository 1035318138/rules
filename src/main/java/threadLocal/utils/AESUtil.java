package threadLocal.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Can.Ru
 */
public class AESUtil {


    private static final String SECRET_KEY = "iiXAjdd/eEr+0CR0SCpn/Q==";
    private static final String IV = "0123456789ABCDEF";

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";



    /**
     *
     */
    private static Cipher encodeCipher;
    /**
     *
     */
    private static Cipher decodeCipher;

    // 提供一个方法来获取实例，该方法允许在第一次调用时才创建实例
    public static synchronized Cipher getEncodeCipher() {
        if (encodeCipher == null) {
            try {
                AesConfig context = new AnnotationConfigApplicationContext(AesConfig.class).getBean(AesConfig.class);
                String b =AesConfig.secret1;
                String b1 =AesConfig.secret1;

                System.out.println(context.toString());
                byte[] keyBytes = "AesConfig.getSecret()".getBytes("UTF-8");
                SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivSpec = new IvParameterSpec("AesConfig.getIv()".getBytes("UTF-8"));
                // 创建cipher实例并初始化为加密模式
                encodeCipher = Cipher.getInstance(ALGORITHM);
                encodeCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                return encodeCipher;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return encodeCipher;
    }

    public static synchronized Cipher getDecodeCipher() {
        if (decodeCipher == null) {
            try {
                AesConfig context = new AnnotationConfigApplicationContext(AesConfig.class).getBean(AesConfig.class);
                byte[] keyBytes = "context.getSecret()".getBytes("UTF-8");
                SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivSpec = new IvParameterSpec("context.getIv()".getBytes("UTF-8"));
                // 创建cipher实例并初始化为加密模式
                decodeCipher = Cipher.getInstance(ALGORITHM);
                decodeCipher.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
                return decodeCipher;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return decodeCipher;
    }




    public static String encrypt(String plaintext) throws Exception {

        // 加密数据
        byte[] encryptedBytes = getEncodeCipher().doFinal(plaintext.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedBase64) throws Exception {
        byte[] decryptedBytes = getDecodeCipher().doFinal(Base64.getDecoder().decode(encryptedBase64));
        return new String(decryptedBytes, "UTF-8");
    }

    public static void main(String[] args) {
        String plaintext = "18956810086";
        String encryptedText = null;
        try {
            encryptedText = encrypt(plaintext);
            System.out.println("Encrypted: " + encryptedText);
            String decryptedText = decrypt(encryptedText);
            System.out.println("Decrypted: " + decryptedText);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
