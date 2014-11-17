package org.adaptlab.chpir.android.survey;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Log;

public class EncryptUtil {
    private final static String TAG = "EncryptUtil";
    private final static String ALGORITHM = "RSA";
    private static PublicKey PUBLIC_KEY;
    private final static String KEY_STRING = "";
    
    public static String encrypt(String text) {
        if (text == null || text.isEmpty()) return "";
        
        try {
            return encryptWithKey("text", getPublicKey());
        } catch (NoSuchAlgorithmException nsae) {
            Log.e(TAG, "NoSuchAlgorithmException: " + nsae);
        } catch (InvalidKeyException ike) {
            Log.e(TAG, "InvalidKeyException: " + ike);
        } catch (NoSuchPaddingException nspe) {
            Log.e(TAG, "NoSuchPaddingException: " + nspe);
        } catch (IllegalBlockSizeException ibse) {
            Log.e(TAG, "IllegalBlockSizeException: " + ibse);
        } catch (BadPaddingException bpe) {
            Log.e(TAG, "BadPaddingException: " + bpe);
        }
        
        return "";
    }
    
    private static String encryptWithKey(String text, PublicKey key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        byte[] cipherText = null;
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
        return cipherText.toString();
    }
    
    private static PublicKey getPublicKey() {
        if (PUBLIC_KEY == null) {
            try {
                byte[] key = KEY_STRING.getBytes(Charset.forName("UTF-8"));
                PUBLIC_KEY = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(key));
            } catch (InvalidKeySpecException ikse) {
                Log.e(TAG, "InvalidKeySpecException: " + ikse);
            } catch (NoSuchAlgorithmException nsae) {
                Log.e(TAG, "NoSuchAlgorithmException: " + nsae);
            }
        }
        
        return PUBLIC_KEY;
    }
}
