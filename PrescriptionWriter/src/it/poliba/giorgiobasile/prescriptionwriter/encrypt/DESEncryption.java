package it.poliba.giorgiobasile.prescriptionwriter.encrypt;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import Decoder.BASE64Encoder;
import Decoder.BASE64Decoder;

public class DESEncryption {

   private static final String UNICODE_FORMAT = "UTF8";
   public static final String DES_ENCRYPTION_SCHEME = "DES";
   private KeySpec myKeySpec;
   private SecretKeyFactory mySecretKeyFactory;
   private Cipher cipher;
   byte[] keyAsBytes;
   public static final String FIRST_KEY = "FirstEncryptionKey";
   public static final String SECOND_KEY = "SecondEncryptionKey";
   public static final String THIRD_KEY = "ThirdEncryptionKey";
   private String myEncryptionScheme;
   SecretKey key;

   public DESEncryption(String encrypt_key) throws Exception
   {
       myEncryptionScheme = DES_ENCRYPTION_SCHEME;
       keyAsBytes = encrypt_key.getBytes(UNICODE_FORMAT);
       myKeySpec = new DESKeySpec(keyAsBytes);
       mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
       cipher = Cipher.getInstance(myEncryptionScheme);
       key = mySecretKeyFactory.generateSecret(myKeySpec);
   }

   /**
    * Method To Encrypt The String
    */
   public String encrypt(String unencryptedString) {
       String encryptedString = null;
       try {
           cipher.init(Cipher.ENCRYPT_MODE, key);
           byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
           byte[] encryptedText = cipher.doFinal(plainText);
           BASE64Encoder base64encoder = new BASE64Encoder();
           encryptedString = base64encoder.encode(encryptedText);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return encryptedString;
   }
   /**
    * Method To Decrypt An Ecrypted String
    */
   public String decrypt(String encryptedString) {
       String decryptedText=null;
       try {
           cipher.init(Cipher.DECRYPT_MODE, key);
           BASE64Decoder base64decoder = new BASE64Decoder();
           byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);
           byte[] plainText = cipher.doFinal(encryptedText);
           decryptedText= bytes2String(plainText);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return decryptedText;
   }
   /**
    * Returns String From An Array Of Bytes
    */
   private static String bytes2String(byte[] bytes) {
       StringBuffer stringBuffer = new StringBuffer();
       for (int i = 0; i < bytes.length; i++) {
           stringBuffer.append((char) bytes[i]);
       }
       return stringBuffer.toString();
   }
}