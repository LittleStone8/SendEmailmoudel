package com.yiwill.pos.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiwill.pos.util.PosWorker;

/**
 * Events used for processing checkout and orders.
 */
public class YWShopLoginEvents {

    public static final String module = YWShopLoginEvents.class.getName();

	public static Map<String, Object> mlogin(HttpServletRequest request, HttpServletResponse response) {

	    
	    Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		StringBuffer jsonStringBuffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}		
		String loginVOStr =  jsonStringBuffer.toString();
		
		
		JSONObject json = JSON.parseObject(loginVOStr);
		
		
		UtilHttp.setLocale(request, "en");
		Map<String, Object> responseMap = LoginEvents.login(request, response,json);
		Boolean success = (Boolean) responseMap.get("success");

		if (success != null && success) {
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

			if (userLogin == null) {
				return ServiceUtil.returnError("user not login");
			}
			String productStoreId = json.getString("productStoreId");
//			String posTerminalId = request.getParameter("posTerminalId");			
			
			Map<String, Object> check = PosWorker.autoCheckByUser(delegator, userLogin, productStoreId);
			if(ServiceUtil.isError(check)){
				return check;
			}else{
//				posTerminalId = (String) check.get("posTerminalId");
				productStoreId = (String)check.get("productStoreId");
				String facilityId = (String) check.get("facilityId");
				String currencyUom = (String) check.get("currencyUom");
				if ( UtilValidate.isEmpty(productStoreId) || UtilValidate.isEmpty(facilityId) || UtilValidate.isEmpty(currencyUom)) {
					session.removeAttribute("userLogin");
					Map<String, Object> errorMap = ServiceUtil.returnError("check fail");
					errorMap.putAll(check);
					return errorMap;
				}else{
					Map<String, Object> map = ServiceUtil.returnSuccess();	
					map.put("checkAll", true);
					return map;	
				}
			}
		} else {
			return ServiceUtil.returnError("login fail");
		}
	}
	
	public static String wPosCheck(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		
		Map<String, List<GenericValue>> resMap = PosWorker.autoGetTerminalAndProductStoreByUser(delegator, userLogin);
		
		List<GenericValue> productStoreList = resMap.get("productStoreList");
//		List<GenericValue> posTerminals = resMap.get("posTerminals");
		
		
		if (UtilValidate.isEmpty(productStoreList) ){
				return "login";
			}
		
		if(productStoreList.size() == 1){
			return "success";
		}else{
			return "login";
		}
		
	}
   
}