package com.yiwill.pos.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.webapp.control.LoginWorker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiwill.pos.util.HttpsUtils;

public class LoginEvents {

    public static final String LOGIN_TOKEN_KEY = "U_TOKEN";
    
    
    public static String validateToken(HttpServletRequest request, HttpServletResponse response) {
	
	 HttpSession session = request.getSession();
//       if (session.getAttribute("userLogin") != null) {
//           return "success";
//       }
       
  	 Delegator delegator = (Delegator) request.getAttribute("delegator");
  	 String loginToken = "";
           Cookie[] cookies = request.getCookies();
           if (cookies != null) {
               for (int i = 0; i < cookies.length; i++) {
                   if (cookies[i].getName().equals(LOGIN_TOKEN_KEY)) {
                   	loginToken = cookies[i].getValue();
                       break;
                   }
               }
            }
           
           if(!loginToken.equals("")){
    	    try {
    		    LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
    		    Map<String, Object> context = new HashMap<String, Object>();
    		    Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
    		    String proxyHost = (String) uomInfo.get("proxyHost");
    		    
		    String cookie =  request.getHeader("Cookie");
		    Map<String, String> header = new HashMap<String, String>();
			   header.put("Cookie", cookie);
			   
			   String validateToken =  HttpsUtils.get(proxyHost+"/api/security/v1/security/validateToken", header);
			   if(validateToken !=null ){
			        JSONObject validateTokenJson = JSON.parseObject(validateToken);
			        Boolean success = validateTokenJson.getBoolean("success");
			        if(success){
			         JSONObject jsonData = validateTokenJson.getJSONObject("data");
			       
			         UtilHttp.setLocale(request, "en");
			         
			            GenericValue userLogin = delegator.makeValue("UserLogin");
			            userLogin.put("userLoginId", jsonData.getString("userLoginId"));
			            userLogin.put("partyId", jsonData.getString("partyId"));
			            userLogin.put("lastCurrencyUom", jsonData.getString("lastCurrencyUom"));
			            userLogin.put("lastTimeZone", jsonData.getString("lastTimeZone"));
			            userLogin.put("currentPassword", jsonData.getString("currentPassword"));
			            
			            session.setAttribute("userLogin", userLogin);
			            
			            
			            
			            return "success";
			        }
			   }
			   session.removeAttribute("userLogin");
    	    } catch (GenericServiceException e) {
    		e.printStackTrace();
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
           }else{
          	 session.removeAttribute("userLogin");
           }
           
           
           String contentType = request.getHeader("Content-Type");
           if(contentType ==null ||  contentType.indexOf("html") > -1){
               return "error";
           }else{
               try {
           	response.setHeader("Content-Type", "application/json;charset=UTF-8");
    		ServletOutputStream outputStream = response.getOutputStream();
    		Map<String, Object> map = ServiceUtil.returnError("user not login");
    		map.put("errCode", 100);
    		JSONObject json = new JSONObject();
    		json.putAll(map);
    		outputStream.write(json.toJSONString().getBytes());
    		outputStream.flush();  
    		outputStream.close();  
    		return "error";
    	    } catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		return "error";
    	    }
       }
           
    }
    
    
    public static Map<String, Object> login(HttpServletRequest request, HttpServletResponse response,JSONObject json) {
    	
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
//		StringBuffer jsonStringBuffer = new StringBuffer();
//		BufferedReader reader;
//		try {
//			reader = request.getReader();
//			String line;
//			while ((line = reader.readLine()) != null) {
//				jsonStringBuffer.append(line);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block  
//			e.printStackTrace();
//		}		
//		String loginVOStr =  jsonStringBuffer.toString();
//		
//		
//		JSONObject json = JSON.parseObject(loginVOStr);
		
		    try {
			    LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
	    		    Map<String, Object> context = new HashMap<String, Object>();
			    Map<String, Object> sysInfo = dispatcher.runSync("getSystemSettings", context);
			    String systemSettingsCountries = (String) sysInfo.get("systemsettinguomid");
			    String proxyHost = (String) sysInfo.get("proxyHost");
			    json.put("scope", systemSettingsCountries);
		 
			    
			    String cookie =  request.getHeader("Cookie");
			    Map<String, String> header = new HashMap<String, String>();
				   			   header.put("Cookie", cookie);
			    String postString =  json.toJSONString();
			    String loginStr =  HttpsUtils.post(proxyHost+"/api/security/v1/login/login", header,postString);
			    
			    JSONObject loginJson = JSON.parseObject(loginStr);
			        Boolean success = loginJson.getBoolean("success");
			        if(success){
			            JSONObject jsonData = loginJson.getJSONObject("data");
			            String token = jsonData.getString("token");
			            JSONObject user = jsonData.getJSONObject("user");
			            
			            GenericValue userLogin = delegator.makeValue("UserLogin");
			            userLogin.put("userLoginId", user.getString("userLoginId"));
			            userLogin.put("partyId", user.getString("partyId"));
			            userLogin.put("lastCurrencyUom", user.getString("lastCurrencyUom"));
			            userLogin.put("lastTimeZone", user.getString("lastTimeZone"));
			            userLogin.put("currentPassword", user.getString("currentPassword"));
			            session.setAttribute("userLogin", userLogin);
			            
			            Cookie loginCookie = new Cookie(LOGIN_TOKEN_KEY,token);
			            loginCookie.setPath("/");
		    		        response.addCookie(loginCookie);
		    		       Map<String, Object> map =  ServiceUtil.returnSuccess();
		    		    	map.put("success", true);
		    		        return map;
			        }else{
			            Long errCode = loginJson.getLong("errCode");
			            if( errCode!= null && errCode ==101){
			        	
			        	   JSONObject jsonData = loginJson.getJSONObject("data");
				            JSONObject user = jsonData.getJSONObject("user");
				            
				            GenericValue userLogin = delegator.makeValue("UserLogin");
				            userLogin.put("userLoginId", user.getString("userLoginId"));
				            userLogin.put("partyId", user.getString("partyId"));
				            userLogin.put("lastCurrencyUom", user.getString("lastCurrencyUom"));
				            userLogin.put("lastTimeZone", user.getString("lastTimeZone"));
				            userLogin.put("currentPassword", user.getString("currentPassword"));
				            
				            session.setAttribute("userLogin", userLogin);
				            Map<String, Object> map =  ServiceUtil.returnSuccess();
			    		    	map.put("success", true);
			    		        return map;
			            }
			            Cookie uCookie = new Cookie(LOGIN_TOKEN_KEY,"");
			            uCookie.setPath("/");
			            uCookie.setMaxAge(0);
			            response.addCookie(uCookie);
			            session.removeAttribute("userLogin");
			            return ServiceUtil.returnError("login fail");
			        }
			        
		    } catch (GenericServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    session.removeAttribute("userLogin");
//	    	LoginWorker.login(request, response);
           return ServiceUtil.returnError("login fail 001");
    }
    
    public static Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> map =  ServiceUtil.returnSuccess();
    	
    	HttpSession session = request.getSession();
    	session.removeAttribute("userLogin");
    	
        Cookie cookie = new Cookie(LOGIN_TOKEN_KEY,"");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    	
//    	LoginWorker.logout(request, response);
    	
    	map.put("success", true);
    	return map;
    }
    
}