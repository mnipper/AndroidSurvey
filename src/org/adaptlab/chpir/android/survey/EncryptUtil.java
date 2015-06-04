package org.adaptlab.chpir.android.survey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {

    private static int iterationCount = 1000;
    private static int keyLength = 256;
    private static SecretKey theKey = null;
    static int saltLength = keyLength / 8;

       public static String encrypt(String value, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
               NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
               BadPaddingException, UnsupportedEncodingException {

           SecureRandom random = new SecureRandom();
           byte[] salt = new byte[saltLength];
           random.nextBytes(salt);

           SecretKey key = getKey(password, salt);

           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           byte[] iv = new byte[cipher.getBlockSize()];
           random.nextBytes(iv);
           IvParameterSpec ivParams = new IvParameterSpec(iv);
           cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
           byte[] cipherText = cipher.doFinal(value.getBytes("UTF-8"));
           
           return salt + "::" + iv + "::" + new String(cipherText, "UTF-8");
       }
       
       public static String decrypt(String ciphertext, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
               NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
               BadPaddingException, UnsupportedEncodingException {
           
           String[] fields = ciphertext.split("::");

           byte[] salt = Base64.decodeBase64(fields[0]);
           byte[] iv = Base64.decodeBase64(fields[1]);
           byte[] cipherBytes = Base64.decodeBase64(fields[2]);
           
           SecretKey key = getKey(password, salt);

           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           IvParameterSpec ivParams = new IvParameterSpec(iv);
           cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
           byte[] plaintext = cipher.doFinal(cipherBytes);
           
           return new String(plaintext , "UTF-8");
       }

       private static SecretKey getKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
           if (theKey != null) return theKey;

           KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                               iterationCount, keyLength);
           SecretKeyFactory keyFactory = SecretKeyFactory
                               .getInstance("PBKDF2WithHmacSHA1");
           byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
           SecretKey key = new SecretKeySpec(keyBytes, "AES");
           theKey = key;
           
           return theKey;
       }

}
