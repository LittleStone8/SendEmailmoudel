package org.ofbiz.product.feature.newfunction.security;

import javax.servlet.http.HttpServletRequest;

import org.ofbiz.security.Security;

public class SecurityCheck {
//	public static final String resource = "ProductErrorUiLabels";
	public static boolean hasEntityPermission(HttpServletRequest request ,String updateMode ){
//		String errMsg=null;
		Security security = (Security) request.getAttribute("security");
		
		if (!security.hasEntityPermission("CATALOG", "_" + updateMode, request.getSession())) {
//            Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
//            errMsg = UtilProperties.getMessage(resource,"productevents.not_sufficient_permissions", messageMap, UtilHttp.getLocale(request));
//            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return false;
        }else{
        	return true;
        }
	}
}
