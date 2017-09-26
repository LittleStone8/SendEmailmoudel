package org.ofbiz.base.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;


/*******************************************************************************
 * AES加解密算法 
 *  
 *  
 
  加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定 
  此处使用AES-128-CBC加密模式，key需要为16位。 
   也是使用<span style="font-size: 1em; line-height: 1.5;">0102030405060708</span>
 */  
  
public class AES {  
  /*
    // 加密  
    public static String encrypt(String sSrc, String sKey) throws Exception { 
    	
        if (sKey == null) {  
            System.out.print("Key为空null");  
            return null;  
        }  
        // 判断Key是否为16位  
        if (sKey.length() != 16) {  
            System.out.print("Key长度不是16位");  
            return null;  
        }  
        byte[] raw = sKey.getBytes();  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"  
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度  
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);  
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        System.out.println(new String(encrypted));
  
        return Base64.encodeBase64String(encrypted);//此处使用BAES64做转码功能，同时能起到2次加密的作用。  
    }  
  
    // 解密  
    public static String decrypt(String sSrc, String sKey) throws Exception {  
        try {  
            // 判断Key是否正确  
            if (sKey == null) {  
                System.out.print("Key为空null");  
                return null;  
            }  
            // 判断Key是否为16位  
            if (sKey.length() != 16) {  
                System.out.print("Key长度不是16位");  
                return null;  
            }  
            byte[] raw = sKey.getBytes("ASCII");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            IvParameterSpec iv = new IvParameterSpec("0102030405060708"  
                    .getBytes());  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);  
            byte[] encrypted1 = Base64.decodeBase64(sSrc);//先用bAES64解密  
            try {  
                byte[] original = cipher.doFinal(encrypted1);  
                String originalString = new String(original);  
                return originalString;  
            } catch (Exception e) {  
                System.out.println(e.toString());  
                return null;  
            }  
        } catch (Exception ex) {  
            System.out.println(ex.toString());  
            return null;  
        }  
    }  
*/

    public static final String DEFAULT_CODING = "utf-8";

    /**
     * 解密
     * @author lmiky
     * @date 2014-2-25
     * @param encrypted
     * @param seed
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypted, String seed) throws Exception {
        //System.out.println("### dec" + encrypted);
        byte[] keyb = seed.getBytes(DEFAULT_CODING);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);

        byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
        return new String(clearbyte);
    }

    /**
     * 加密
     * @author lmiky
     * @date 2014-2-25
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes(DEFAULT_CODING);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(key.getBytes(DEFAULT_CODING));
        SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skc);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);

        return parseByte2HexStr(cipherText);
    }

    /**
     * 字符串转字节数组
     * @author lmiky
     * @date 2014-2-25
     * @param hexString
     * @return
     */
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * 字节转16进制数组
     * @author lmiky
     * @date 2014-2-25
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
		String key ="12345678123456AB";
		String str = "Message";
		String mm = encrypt(str, key);
		
		System.out.println(mm);
		
		System.out.println(decrypt(mm, key));
	}

} 