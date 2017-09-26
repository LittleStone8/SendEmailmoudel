package org.ofbiz.commondefine.sso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.Security;
import org.ofbiz.service.LocalDispatcher;

public class SSOEvents {

    public static String module = SSOEvents.class.getName();

    public static Map<String, Object> ssoLogin(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        Security security = (Security) request.getAttribute("security");
    	Map<String, Object> map = new HashMap<String, Object>();
    	
//    	String externalLoginKey = LoginWorker.getExternalLoginKey(request);
//    	map.put("externalLoginKey", externalLoginKey);
    	
    	map.put("locale", locale);
    	map.put("success", true);
        return map;
    }
    
    

    
}