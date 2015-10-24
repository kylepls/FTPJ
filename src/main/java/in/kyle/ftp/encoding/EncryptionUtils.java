package in.kyle.ftp.encoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by Kyle on 9/13/2015.
 * <p>
 * Stolen from: http://www.javacodegeeks.com/2013/08/safely-create-and-store-passwords.html
 * Also stolen from: http://stackoverflow.com/a/7762892/2821370
 * <p>
 * Modified by: @Kyle
 */
@UtilityClass
public class EncryptionUtils {
    
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String AES_ALGORITHM = "AES";
    
    public static String getRandomString() {
        SecureRandom secureRandom = new SecureRandom();
        return RandomStringUtils.randomAlphanumeric(20 + secureRandom.nextInt(20));
    }
    
    public static byte[] createPasswordHash(final String password, final String salt) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(DEFAULT_CHARSET));
            digest.update(password.getBytes(DEFAULT_CHARSET));
            result = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            new Error(e);
        }
        
        return result;
    }
    
    public static PasswordHash createPasswordHash(final String password) {
        String salt = getRandomString();
        String hash = encodeBase64(createPasswordHash(password, salt));
        return new PasswordHash(salt, hash);
    }
    
    public static boolean checkPassword(final String hash, final String salt, final String password) {
        
        boolean result = false;
        
        byte[] checkPasswordHashBytes = createPasswordHash(password, salt);
        String checkPasswordHash = encodeBase64(checkPasswordHashBytes);
        
        if (hash != null && checkPasswordHash.equals(hash)) {
            result = true;
        }
        
        return result;
    }
    
    public static String encodeBase64(byte[] text) {
        return new String(Base64.getEncoder().encode(text), DEFAULT_CHARSET);
    }
    
    public static void main(String[] args) throws Exception {
        /*
        String salt = getRandomString();
        System.out.println("Salt: " + salt);
        String password = "asd";
        System.out.println("Pass: " + password);
        String hash = encodeBase64(createPasswordHash(password, salt));
        System.out.println("Hash: " + hash);
        
        System.out.println("Check: " + checkPassword(hash, salt, "asd"));
         */
        
        byte[] pass = "asd".getBytes();
        byte[] data = "My data!".getBytes();
        String encrypt = encrypt(pass, data);
        System.out.println("Encrypted string: " + encrypt);
        String decrypt = decrypt(pass, encrypt.getBytes());
        System.out.println("Decrypted string: " + decrypt);
    }
    
    public static byte[] getMD5Hash(byte[] string) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(string);
    }
    
    public static String encrypt(byte[] keyString, byte[] value) throws Exception {
        Key key = generateKey(keyString);
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(value);
        String encryptedValue = Base64.getEncoder().encodeToString(encValue);
        return encryptedValue;
    }
    
    public static String decrypt(byte[] keyString, byte[] value) throws Exception {
        Key key = generateKey(keyString);
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(value);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }
    
    private static Key generateKey(byte[] keyValue) throws Exception {
        return new SecretKeySpec(getMD5Hash(keyValue), AES_ALGORITHM);
    }
    
    @Data
    @AllArgsConstructor
    public static class PasswordHash {
        private String salt;
        private String hash;
    }
}
