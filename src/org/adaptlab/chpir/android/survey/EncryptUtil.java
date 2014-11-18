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

import android.util.Base64;
import android.util.Log;

public class EncryptUtil {
    private final static String TAG = "EncryptUtil";
    private final static String ALGORITHM = "RSA";
    private static PublicKey PUBLIC_KEY;
    private final static String KEY_STRING = "" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAs8LxB8S3q0l0gwbTLtWm" +
            "e1HkiQHrX23lNg7Y9zP6DnMuN2n6uZ8ZNPCwSVsMaxGjFW+C1/RuWIPcNHpdthXJ" +
            "rzzU0Ns5m1t1YwAWN1B+3i+1IFEbhJMN2Sme27C7eEQME9DIhKuPIkdZ6rXR43nH" +
            "5M6v2YGcaQqY99OdheUrpYhTBQfctpuWpY47S0zol0QyHyrUkB4RQZ0mT8SxRbVh" +
            "ET3ELBxk11Lp6tMXG3ykoVYzcczZDDCttuhkNvPdbVLeHDbctyLspRi34nheQ7Xe" +
            "erquqvjfxGHyDW0PHH6wx0B+6i9ZvAw+xBFADe9vgGtIyQJB6nL1XefhpO/TC2GW" +
            "M7U0nMYJmGJStIBUu4dStRaAfsPOQuR8Pg5/BSPUmBd/F1IzGfWHxF3sePaWQ7Tr" +
            "R26Hr1/DacVmiMitKetipaRaisffalkbfi0KioZh+NP390JPLRmcwHPgF6tEwWHI" +
            "JSrmxDN2j5EldWK1Vie4mhjpaFIWphzSdrq/8QV4taXOEN0S2+Er2ZZc3iv2Z01I" +
            "8Xiahtl7BYsr1jHTV4dXWkT4Ey3mpyimEceBUnJjTAfCJ9t+Vm/guNltt2x2WmaI" +
            "u7VGG3UJaauGwN1j+mliUyEEUwh04TWvgTPKvaRm5VA/+1mmE0ue4ZAdG0IMMwgS" +
            "DPebw82DBi+KoDHchgZRDD0CAwEAAQ=="; // TEST public key
    
    public static String encrypt(String text) {
        if (text == null || text.isEmpty()) return "";   
        
        if (KEY_STRING == null || KEY_STRING.isEmpty()) {
            throw new SecurityException("Public Key is not set for EncryptUtil");
        }
        
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
                PUBLIC_KEY = KeyFactory.getInstance(ALGORITHM).generatePublic(
                        new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            } catch (InvalidKeySpecException ikse) {
                Log.e(TAG, "InvalidKeySpecException: " + ikse);
            } catch (NoSuchAlgorithmException nsae) {
                Log.e(TAG, "NoSuchAlgorithmException: " + nsae);
            }
        }
        
        return PUBLIC_KEY;
    }
}
