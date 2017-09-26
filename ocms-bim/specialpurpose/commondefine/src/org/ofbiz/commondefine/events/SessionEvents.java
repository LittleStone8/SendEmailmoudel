package org.ofbiz.commondefine.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.common.CommonEvents;
import org.ofbiz.webapp.control.LoginWorker;

public class SessionEvents {

    public static String module = SessionEvents.class.getName();

    public static Map<String, Object> setSessionLocale(HttpServletRequest request, HttpServletResponse response) {
    	
//    	  HttpSession session = request.getSession();
//        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//        Delegator delegator = (Delegator) request.getAttribute("delegator");
//        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//        Locale locale = UtilHttp.getLocale(request);
//        Security security = (Security) request.getAttribute("security");
//        String externalLoginKey = LoginWorker.getExternalLoginKey(request);

    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String externalLoginKey = LoginWorker.getExternalLoginKey(request);
    	map.put("externalLoginKey", externalLoginKey);
    	
    	map.put("data", CommonEvents.setSessionLocale(request, response));
    	map.put("success", true);
    	
        return map;
    }
    
    
    
}