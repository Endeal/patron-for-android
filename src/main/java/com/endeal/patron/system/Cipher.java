/**
Thanks to owlstead at:
http://stackoverflow.com/questions/20063239/function-decrypt-throws-javax-crypto-badpaddingexception-pad-block-corrupted-in
and his example at:
http://stackoverflow.com/questions/4551263/how-can-i-convert-string-to-secretkey/8828196#8828196
*/

package com.endeal.patron.system;

import java.security.SecureRandom;
import java.security.InvalidKeyException;
import java.security.GeneralSecurityException;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.nio.charset.Charset;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

import com.endeal.patron.lists.ListKeys;

public class Cipher
{
     public static String encrypt(String text) throws javax.crypto.IllegalBlockSizeException, java.lang.ArrayIndexOutOfBoundsException
     {
        final String plainMessage = text;
        final String symKeyHex = ListKeys.PASSWORD_ENCRYPTION_KEY;
        final byte[] symKeyData = hexStringToByteArray(symKeyHex);
        final byte[] encodedMessage = plainMessage.getBytes(Charset.forName("UTF-8"));
        try
        {
            final javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

            // generate random IV using block size (possibly create a method for this)
            final byte[] ivData = new byte[blockSize];
            final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            rnd.nextBytes(ivData);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, symKey, iv);

            final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

            // concatenate IV and encrypted message
            final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedMessage.length];
            System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
            System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage, blockSize, encryptedMessage.length);

            final String ivAndEncryptedMessageBase64 = Base64.encodeToString(ivAndEncryptedMessage, Base64.DEFAULT);//DatatypeConverter.printBase64Binary(ivAndEncryptedMessage);

            return ivAndEncryptedMessageBase64;
        }
        catch (InvalidKeyException e)
        {
            throw new IllegalArgumentException("key argument does not contain a valid AES key");
        }
        catch (GeneralSecurityException e)
        {
            throw new IllegalStateException("Unexpected exception during encryption", e);
        }
    }

    public static String decrypt(String text) throws javax.crypto.IllegalBlockSizeException, java.lang.ArrayIndexOutOfBoundsException
    {
        final String ivAndEncryptedMessageBase64 = text;
        final String symKeyHex = ListKeys.PASSWORD_DECRYPTION_KEY;
        final byte[] symKeyData = hexStringToByteArray(symKeyHex);

        final byte[] ivAndEncryptedMessage = Base64.decode(ivAndEncryptedMessageBase64, Base64.DEFAULT);
        try {
            final javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

            // retrieve random IV from start of the received message
            final byte[] ivData = new byte[blockSize];
            System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            // retrieve the encrypted message itself
            final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length - blockSize];
            System.arraycopy(ivAndEncryptedMessage, blockSize, encryptedMessage, 0, encryptedMessage.length);

            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, symKey, iv);

            final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

            // concatenate IV and encrypted message
            final String message = new String(encodedMessage, Charset.forName("UTF-8"));

            return message;
        }
        catch (InvalidKeyException e)
        {
            throw new IllegalArgumentException("key argument does not contain a valid AES key");
        }
        catch (BadPaddingException e)
        {
            // you'd better know about padding oracle attacks
            return null;
        }
        catch (GeneralSecurityException e)
        {
            throw new IllegalStateException("Unexpected exception during decryption", e);
        }
    }

    private static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    final private static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    private static String byteArrayToHexString(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
