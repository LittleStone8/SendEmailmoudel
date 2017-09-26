package org.ofbiz.commondefine.maindecorator;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceEvents {

    public static String module = ServiceEvents.class.getName();

    public static String test(HttpServletRequest request, HttpServletResponse response) {
    	
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("success", true);
    	
    	request.getAttribute("");
    	
    	
    	
    	request.setAttribute("testmap", map);
    	  
        return "success";
    }

    
}