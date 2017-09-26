package org.ofbiz.base.util;

import java.util.Random;

public class RandomUtil {

    public static String getRandomStr16(){
        return getRandomString(16);
    }

	 public static String getRandomString(int length){  
	        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
	        Random random = new Random();  
	        StringBuffer sb = new StringBuffer();  
	          
	        for(int i = 0 ; i < length; ++i){  
	            int number = random.nextInt(62);//[0,62)  
	              
	            sb.append(str.charAt(number));  
	        }  
	        return sb.toString();  
	}

    public static String getRandomUpperString(int length){
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        int number = 0;
        for(int i = 0 ; i < length; ++i){
            number = random.nextInt(26);//[0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
