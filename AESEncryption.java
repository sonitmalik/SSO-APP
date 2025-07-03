package com.kramer.smauthenticator.qrcodereader;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    // private static final String secretKeyInstance = "0123456789abcdef0123456789abcdef";
    private static final String secretKeyInstance = "wxq$ryktvRaeME3b@Ky8K7F#u!sR^Rmv";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String initializationVector = "aaaaaaaaaaaaaaaa";
    private static String INIT_VECTOR = "encryptionIntVec";

    //    public static String decrypt(String textToDecrypt) throws Exception {
//
//        byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
//        SecretKeySpec skeySpec = new SecretKeySpec(secretKeyInstance.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance(cypherInstance);
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
//        byte[] decrypted = cipher.doFinal(encryted_bytes);
//        return new String(decrypted, "UTF-8");
//    }
    public static String decrypt(String encryptedData) {

        try {

            byte[] encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT);

            byte[] secretKeyBytes = secretKeyInstance.getBytes(StandardCharsets.UTF_8);

            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, ENCRYPTION_ALGORITHM);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {

            throw new RuntimeException("Error decrypting data: " + e.getMessage(), e);

        }

    }
    public static String encrypt1(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKeyInstance.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt1(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKeyInstance.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}