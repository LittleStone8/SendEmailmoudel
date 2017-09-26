package org.ofbiz.commondefine.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.commondefine.bean.NavInfoBeanV2;
import org.ofbiz.commondefine.util.HttpsUtils;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class LoginEvents {

    public static final String LOGIN_TOKEN_KEY = "U_TOKEN";

    public static String validateToken(HttpServletRequest request, HttpServletResponse response) {

	HttpSession session = request.getSession();
	// if (session.getAttribute("userLogin") != null) {
	// return "success";
	// }

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

	if (!loginToken.equals("")) {
	    try {
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Map<String, Object> context = new HashMap<String, Object>();
		Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
		String proxyHost = (String) uomInfo.get("proxyHost");

		String cookie = request.getHeader("Cookie");
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", cookie);

		String validateToken = HttpsUtils.get(proxyHost + "/api/security/v1/security/validateToken", header);
		if (validateToken != null) {
		    JSONObject validateTokenJson = JSON.parseObject(validateToken);
		    Boolean success = validateTokenJson.getBoolean("success");
		    if (success) {
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
	} else {
	    session.removeAttribute("userLogin");
	}
	// return "error";

	String contentType = request.getHeader("Content-Type");
	if (contentType == null || contentType.indexOf("html") > -1) {
	    return "error";
	} else {
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
		session.removeAttribute("userLogin");
		return "error";
	    }
	}
    }

    public static Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) {

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
	String loginVOStr = jsonStringBuffer.toString();

	JSONObject json = JSON.parseObject(loginVOStr);

	try {
	    LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Map<String, Object> context = new HashMap<String, Object>();
	    Map<String, Object> sysInfo = dispatcher.runSync("getSystemSettings", context);
	    String systemSettingsCountries = (String) sysInfo.get("systemsettinguomid");
	    String proxyHost = (String) sysInfo.get("proxyHost");
	    json.put("scope", systemSettingsCountries);

	    String cookie = request.getHeader("Cookie");
	    Map<String, String> header = new HashMap<String, String>();
	    header.put("Cookie", cookie);
	    String postString = json.toJSONString();
	    String loginStr = HttpsUtils.post(proxyHost + "/api/security/v1/login/login", header, postString);

	    JSONObject loginJson = JSON.parseObject(loginStr);
	    Boolean success = loginJson.getBoolean("success");
	    if (success) {
		JSONObject jsonData = loginJson.getJSONObject("data");
		String token = jsonData.getString("token");

		JSONObject userData = jsonData.getJSONObject("user");

		UtilHttp.setLocale(request, "en");

		GenericValue userLogin = delegator.makeValue("UserLogin");
		userLogin.put("userLoginId", userData.getString("userLoginId"));
		userLogin.put("partyId", userData.getString("partyId"));
		userLogin.put("lastCurrencyUom", userData.getString("lastCurrencyUom"));
		userLogin.put("lastTimeZone", userData.getString("lastTimeZone"));
		userLogin.put("currentPassword", userData.getString("currentPassword"));

		session.setAttribute("userLogin", userLogin);

		Cookie loginCookie = new Cookie(LOGIN_TOKEN_KEY, token);
		loginCookie.setPath("/");
		response.addCookie(loginCookie);
		Map<String, Object> map = ServiceUtil.returnSuccess();
		map.put("success", true);
		return map;
	    } else {

		Long errCode = loginJson.getLong("errCode");

		if (errCode != null && errCode == 101) {
		    JSONObject jsonData = loginJson.getJSONObject("data");
		    JSONObject userData = jsonData.getJSONObject("user");
		    UtilHttp.setLocale(request, "en");
		    GenericValue userLogin = delegator.makeValue("UserLogin");
		    userLogin.put("userLoginId", userData.getString("userLoginId"));
		    userLogin.put("partyId", userData.getString("partyId"));
		    userLogin.put("lastCurrencyUom", userData.getString("lastCurrencyUom"));
		    userLogin.put("lastTimeZone", userData.getString("lastTimeZone"));
		    userLogin.put("currentPassword", userData.getString("currentPassword"));
		    session.setAttribute("userLogin", userLogin);
		    Map<String, Object> map = ServiceUtil.returnSuccess();
		    map.put("success", true);
		    return map;
		}
		Cookie uCookie = new Cookie(LOGIN_TOKEN_KEY, "");
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

	// LoginWorker.login(request, response);
	return ServiceUtil.returnError("login fail 001");
    }

    public static Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
	Map<String, Object> map = ServiceUtil.returnSuccess();

	Cookie cookie = new Cookie(LOGIN_TOKEN_KEY, "");
	cookie.setPath("/");
	cookie.setMaxAge(0);
	response.addCookie(cookie);

	// LoginWorker.logout(request, response);

	map.put("success", true);
	return map;
    }

    public static Map<String, Object> getloginUserInfo(HttpServletRequest request, HttpServletResponse response) {
	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
	Security security = (Security) request.getAttribute("security");
	HttpSession session = request.getSession();
	
	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

	
	Map<String, Object> map = ServiceUtil.returnSuccess();

	try {
	    Map<String, Object> context = new HashMap<String, Object>();
	    context.put("userLogin", userLogin);
	    
	    
	    map = dispatcher.runSync("getHomeApplications", context);
	    
	    map.put("userLogin", userLogin);
	    
	    String isShip =(String) map.get("isShip");
	    
	    List<GenericValue> apps = (List<GenericValue>) map.get("apps");

	    boolean catalogView = false;
	    boolean orderManagementView = false;
	    boolean partyAdminView = false;
	    boolean reportView = false;
	    boolean storeView = false;
	    boolean warehouseView = false;
	    boolean webPosView = false;

	    for (GenericValue app : apps) {
		String applicationId = app.getString("applicationId");
		
		if("catalog".equals(applicationId)){
		    if(security.hasPermission("PRODUCT_CATEGORY", session)){
			catalogView = true;
		    }else if(security.hasPermission("PPR_FIND", session)){
			catalogView = true;
		    }else if(security.hasPermission("PPR_FIND_LOG", session)){
			catalogView = true;
		    }
		}
		
		if("ordermgr".equals(applicationId)){
		    orderManagementView = true;
		}
		
		if("party".equals(applicationId)){
		    partyAdminView = true;
		}
		
		if("report".equals(applicationId)){
		    
		    if(security.hasPermission("REPMENU_SALE", session)){
			reportView = true;
		    }else if(security.hasPermission("REPMENU_OP_INV", session)){
			reportView = true;
		    }else if(security.hasPermission("WRHS_VIEW_REPORT", session)){
			reportView = true;
		    }else if(security.hasPermission("REPMENU_OP_INV", session)){
			reportView = true;
		    }else if(security.hasPermission("REPMENU_OP_SALE", session)){
			reportView = true;
		    }else if(security.hasPermission("WRHS_VIEW_MTNREPORT", session)){
			reportView = true;
		    }
		}
		
		if("Store".equals(applicationId)){
		    storeView = true;
		}
		
		if("warehouse".equals(applicationId)){
		    
		    NavInfoBeanV2 warehouseNavInfo = new NavInfoBeanV2();
		    warehouseNavInfo.setName("warehouse");
		    warehouseNavInfo.setName("warehouse");
		 
		    
		    List<NavInfoBeanV2> navInfoChildNodeList = new ArrayList<NavInfoBeanV2>();
		    
		    
		    if(security.hasPermission("WRHS_INV_RAI", session)){
			
			
			  NavInfoBeanV2 inventoryNavInfo = new NavInfoBeanV2();
			  inventoryNavInfo.setShortName("Inventory");
			  inventoryNavInfo.setName("Inventory");
			  inventoryNavInfo.setLinkUrl("/warehouse/control/receiveInventoryItem");
			  navInfoChildNodeList.add(inventoryNavInfo);
			  
			  
			  if(isShip != null && isShip.equals("Y")){
				  NavInfoBeanV2 shippingNavInfo = new NavInfoBeanV2();
				  shippingNavInfo.setShortName("Shipping");
				  shippingNavInfo.setName("Shipping");
				  shippingNavInfo.setLinkUrl("/warehouse/control/shippingMain");
				  navInfoChildNodeList.add(shippingNavInfo);
			    }
			  
			  
			  if(security.hasPermission("WRHS_ADMIN", session)){
			      warehouseView = true;
			      warehouseNavInfo.setLinkUrl("/warehouse/control/receiveInventoryItem");
			      
			
				  
				  NavInfoBeanV2 configurationNavInfo = new NavInfoBeanV2();
				  configurationNavInfo.setShortName("Warehouse Configuration");
				  configurationNavInfo.setName("Warehouse Configuration");
				  configurationNavInfo.setLinkUrl("/warehouse/control/configurationMain");
				  navInfoChildNodeList.add(configurationNavInfo);
			      
				      NavInfoBeanV2 batchAddMemberNavInfo = new NavInfoBeanV2();
				      batchAddMemberNavInfo.setShortName("Batch Add Member");
				      batchAddMemberNavInfo.setName("Batch Add Member");
				      batchAddMemberNavInfo.setLinkUrl("/warehouse/control/batchAddMember");
				navInfoChildNodeList.add(batchAddMemberNavInfo);
					  
			    }else if(security.hasPermission("WRHS_BAM", session)){
				warehouseView = true;
				warehouseNavInfo.setLinkUrl("/warehouse/control/receiveInventoryItem");
				
				    NavInfoBeanV2 batchAddMemberNavInfo = new NavInfoBeanV2();
				      batchAddMemberNavInfo.setShortName("Batch Add Member");
				      batchAddMemberNavInfo.setName("Batch Add Member");
				      batchAddMemberNavInfo.setLinkUrl("/warehouse/control/batchAddMember");
				navInfoChildNodeList.add(batchAddMemberNavInfo);
				
			    }else{
				warehouseView = true;
				warehouseNavInfo.setLinkUrl("/warehouse/control/receiveInventoryItem");
			    }
			  
		    }else if(security.hasPermission("REPMENU_OP_INV", session)){
			
			  NavInfoBeanV2 inventoryNavInfo = new NavInfoBeanV2();
			  inventoryNavInfo.setShortName("Inventory");
			  inventoryNavInfo.setName("Inventory");
			  inventoryNavInfo.setLinkUrl("/warehouse/control/newFindInventoryItem");
			  navInfoChildNodeList.add(inventoryNavInfo);
			  
			  if(isShip != null && isShip.equals("Y")){
				  NavInfoBeanV2 shippingNavInfo = new NavInfoBeanV2();
				  shippingNavInfo.setShortName("Shipping");
				  shippingNavInfo.setName("Shipping");
				  shippingNavInfo.setLinkUrl("/warehouse/control/shippingMain");
				  navInfoChildNodeList.add(shippingNavInfo);
			    }
			  
			  
			 if(security.hasPermission("WRHS_ADMIN", session)){
			     warehouseView = true;
			     warehouseNavInfo.setLinkUrl("/warehouse/control/newFindInventoryItem");
			     
			     NavInfoBeanV2 configurationNavInfo = new NavInfoBeanV2();
				  configurationNavInfo.setShortName("Warehouse Configuration");
				  configurationNavInfo.setName("Warehouse Configuration");
				  configurationNavInfo.setLinkUrl("/warehouse/control/configurationMain");
				  navInfoChildNodeList.add(configurationNavInfo);
			      
				    NavInfoBeanV2 batchAddMemberNavInfo = new NavInfoBeanV2();
				      batchAddMemberNavInfo.setShortName("Batch Add Member");
				      batchAddMemberNavInfo.setName("Batch Add Member");
				      batchAddMemberNavInfo.setLinkUrl("/warehouse/control/batchAddMember");
				navInfoChildNodeList.add(batchAddMemberNavInfo);
			  }else if(security.hasPermission("WRHS_BAM", session)){
			      warehouseView = true;
			      warehouseNavInfo.setLinkUrl("/warehouse/control/newFindInventoryItem");
			      NavInfoBeanV2 batchAddMemberNavInfo = new NavInfoBeanV2();
			      batchAddMemberNavInfo.setShortName("Batch Add Member");
			      batchAddMemberNavInfo.setName("Batch Add Member");
			      batchAddMemberNavInfo.setLinkUrl("/warehouse/control/batchAddMember");
			      navInfoChildNodeList.add(batchAddMemberNavInfo);
			  }else{
			      warehouseView = true;
			      warehouseNavInfo.setLinkUrl("/warehouse/control/newFindInventoryItem");
			  }
		    }
		    warehouseNavInfo.setChildNode(navInfoChildNodeList);
		    map.put("warehouseNavInfo", warehouseNavInfo);
		}
		
		if("webpos".equals(applicationId)){
		    webPosView =true;
		}
		
	    }
	    map.put("catalogView", catalogView);
	    map.put("orderManagementView", orderManagementView);
	    map.put("partyAdminView", partyAdminView);
	    map.put("reportView", reportView);
	    map.put("storeView", storeView);
	    map.put("warehouseView", warehouseView);
	    map.put("webPosView", webPosView);

	} catch (GenericServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	map.put("success", true);
	return map;
    }

}