package org.adaptlab.chpir.android.survey;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import android.util.Base64;
import android.util.Log;

/*
 * Encrypts AES key with RSA using provided public key, and encrypts
 * provided text with AES CBC.
 * 
 * Returns string with delimited base 64 encoded iv, cipher key, and encrypted text.
 */
public class EncryptUtil {
    private final static String TAG = "EncryptUtil";
    private final static String ASYMMETRIC_ALGO = "RSA/None/PKCS1Padding";
    private final static String SYMMETRIC_ALGO = "AES/CBC/PKCS5Padding";
    private final static int SYMMETRIC_KEY_SIZE = 256;
    private final static String PROVIDER = "SC";
    private final static String FIELD_DELIMITER = "::"; // delimits iv, cipher key, and encrypted text
    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
    
    private static PublicKey PUBLIC_KEY;
    private final static String KEY_STRING = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr19m7jjA3"
            + "2IrF9io\ns9MWth119faF4Xc7clCtWWi9ncmMSBq0uQsASkpf/J"
            + "VdVi91TZbzdzhInhhZ\nomMh+26r8oiAibRHRcxGRyCIUhEUZQu"
            + "R8QeeaO5wugpg1zOYuYrevDH+MxuY\nebeAcqccW91ZQBDxsUKY"
            + "jSiVIhm/Cmas/J/g9m+k+HKUGHREVzNZSRIToxuV\nc2DHVylWT"
            + "0NPN14OUnOt4PHJZED/QwiiNFWo/UiovPkw1PjAC09gmV9sYmyK"
            + "\nwu57oeCVvm6xbHkjO30an0NbaGrRFkR7wzLbVK+r8uTbkKPcm"
            + "Mv0UPrG9hsg\nY4g5le8kVoDpdSYLIU7lc5LRDQIDAQAB"; // TEST public key
    
    public static String encrypt(String text) {
        if (text == null || text.isEmpty()) return "";
        
        if (KEY_STRING == null || KEY_STRING.isEmpty()) {
            throw new SecurityException("Public Key is not set for EncryptUtil");
        }
        
        try {
            return encryptWithKey(text, generateAESKey());
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
        } catch (NoSuchProviderException nspe) {
            Log.e(TAG, "NoSuchProviderException: " + nspe);
        }
        
        return "";
    }
    
    /*
     * Encrypt provided text with provided secret key.  Secret key is used in the
     * symmetric encryption algorithm.
     * 
     * Returns format:  IV::CIPHER_KEY::CIPHER_TEXT
     */
    private static String encryptWithKey(String text, SecretKey key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        
        final Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_ALGO, PROVIDER);
        symmetricCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(), SYMMETRIC_ALGO));
        String cipherText = Base64.encodeToString(symmetricCipher.doFinal(text.getBytes()), Base64.DEFAULT);
        String iv =  Base64.encodeToString(symmetricCipher.getIV(), Base64.DEFAULT);
        
        final Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_ALGO, PROVIDER);
        asymmetricCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        String cipherKey = Base64.encodeToString(asymmetricCipher.doFinal(key.getEncoded()), Base64.DEFAULT);

        return iv + FIELD_DELIMITER + cipherKey + FIELD_DELIMITER + cipherText;
    }
    
    private static PublicKey getPublicKey() throws NoSuchProviderException {
        if (PUBLIC_KEY == null) {
            try {
                byte[] key = KEY_STRING.getBytes(Charset.forName("UTF-8"));
                PUBLIC_KEY = KeyFactory.getInstance("RSA", PROVIDER).generatePublic(
                        new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            } catch (InvalidKeySpecException ikse) {
                Log.e(TAG, "InvalidKeySpecException: " + ikse);
            } catch (NoSuchAlgorithmException nsae) {
                Log.e(TAG, "NoSuchAlgorithmException: " + nsae);
            }
        }
        
        return PUBLIC_KEY;
    }
    
    private static SecretKey generateAESKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES", PROVIDER);
        kgen.init(SYMMETRIC_KEY_SIZE);
        return kgen.generateKey();      
    }
}
