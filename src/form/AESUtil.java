/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.*;
import java.util.Base64;

public class AESUtil {
  // must match  PHP hex key:
  private static final byte[] KEY = hexStringToByteArray(
    "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"
  );

  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len/2];
    for (int i = 0; i < len; i += 2) {
      data[i/2] = (byte)((Character.digit(s.charAt(i),16)<<4)
                        +Character.digit(s.charAt(i+1),16));
    }
    return data;
  }

  public static Map<String,String> encrypt(String plaintext) throws Exception {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(KEY,"AES");
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
    byte[] cipherBytes = c.doFinal(plaintext.getBytes("UTF-8"));

    Map<String,String> out = new HashMap<>();
    out.put("cipher", Base64.getEncoder().encodeToString(cipherBytes));
    out.put("iv",     Base64.getEncoder().encodeToString(iv));
    return out;
  }

  public static String decrypt(String cipherB64, String ivB64) throws Exception {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(KEY,"AES");
    byte[] iv = Base64.getDecoder().decode(ivB64);
    c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
    byte[] plainBytes = c.doFinal(Base64.getDecoder().decode(cipherB64));
    return new String(plainBytes, "UTF-8");
  }
}

