package org.ofbiz.base.util;


import java.util.Arrays;
import java.util.Date;


public class AesTool {

    public static void main(String[] args) {


    }


    /**
     * @3 加密
     * @param uniqueUserStr
     * @param time
     * @param authKey
     * @return
     */
    public static String encryptAuth(String uniqueUserStr, long time , String authKey){

        String ipStr = "_"; //预留，本来想记录ip地址的，这样会更加安全些

        String str = uniqueUserStr  + "@@" + time + "@@" + ipStr;
        try {
            String res = AES.encrypt(str, authKey);
            return UrlBase64Coder.encoded(uniqueUserStr + "@@" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @1 解密第一步
     * @param base64AuthCrypto
     * @return 分解成2部分，分别是 用户id 和 待aes解密的字符串
     * 根据用户id查找数据库得到对应的aes的密钥authKey,再用方法decryptAuth解密（即是解密第二步）
     */
    public static String[] splitAuthCrypto(String base64AuthCrypto){
        if(base64AuthCrypto == null){
            return null;
        }
        try {
            base64AuthCrypto = UrlBase64Coder.decode(base64AuthCrypto);
            return base64AuthCrypto.split("@@");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @2 解密第二步
     * @param authCryptoStr
     * @param authKey
     * @return
     */
    public static String[] decryptAuth(String authCryptoStr , String authKey){
        try {
            String de = AES.decrypt(authCryptoStr, authKey);
            if(de != null){
                return de.split("@@");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
