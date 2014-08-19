package org.adaptlab.chpir.android.survey.Models;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "DeviceUser")
public class DeviceUser extends Model {
    private static final String TAG = "DeviceUser";
    private static final int PBKDF2_ITERATIONS = 1000;
    
    @Column(name = "RealName")
    private String mRealName;
    @Column(name = "UserName")
    private String mUserName;
    @Column(name = "Password")
    private String mPassword;
    
    public void setRealName(String realName) {
        mRealName = realName;
    }
    
    public String getRealName() {
        return mRealName;
    }
    
    public void setUserName(String userName) {
        mUserName = userName;
    }
    
    public String getUserName() {
        return mUserName;
    }
    
    public boolean checkPassword(String password) {
        try {
            return validatePassword(password, mPassword);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm: " + e);
            return false;
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "Invalid Key Spec: " + e);
            return false;
        }
    }
    
    public void setPassword(String password) {
        try {
            mPassword = generatePasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm: " + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "Invalid key spec exception: " + e);
        }
    }
    
    private static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, PBKDF2_ITERATIONS, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return new String(salt);
    }
    
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        
        return diff == 0;
    }
    
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
