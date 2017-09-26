package org.ofbiz.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class ParaUtils {

    public static String getParameter(HttpServletRequest request, String parameterName) {
        String result = request.getParameter(parameterName);
        if (result == null) {
            return null;
        }
        result = result.trim();
        if (result.length() == 0) {
            return null;
        }
        return result;
    }
    
    /**
	 * 获取前台传：返回JsonObj
	 * 
	 * @param request
	 * @return
	 */
	public static JSONObject getJsonByRequest(HttpServletRequest request) {
		StringBuffer jsonStr = new StringBuffer();
		BufferedReader bufferedReader;
		try {
			bufferedReader = request.getReader();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				jsonStr.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		JSONObject jsonObj = JSONObject.parseObject(jsonStr.toString());
		return jsonObj;
	}
}
