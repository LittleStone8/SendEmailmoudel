package org.ofbiz.commondefine.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.component.ComponentConfig;
import org.ofbiz.base.component.ComponentConfig.WebappInfo;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.commondefine.bean.NavInfoBean;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.Security;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.webapp.control.LoginWorker;

public class AppEvents {

    public static String module = AppEvents.class.getName();

    public static Map<String, Object> findAppInfo(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//        String lastLocale = (String) userLogin.get("lastLocale");
//        Locale locale = UtilHttp.getLocale(request,session,lastLocale);
        Locale locale = UtilHttp.getLocale(request);
        
        Security security = (Security) request.getAttribute("security");
        
        String externalLoginKey = LoginWorker.getExternalLoginKey(request);
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	List<WebappInfo> list = ComponentConfig.getAppBarWebInfos("default-server","main");
    	
    	
    	List<NavInfoBean> navInfoList = new ArrayList<NavInfoBean>();
    	for (WebappInfo webappInfo : list) {
    		String description = webappInfo.getDescription();
    		String name =  webappInfo.getName();
    		
    		Boolean permission = true;
    		String[] basePermissions = webappInfo.getBasePermission();
    		if(basePermissions != null){
    			for (String basePermission : basePermissions) {
    				if(!basePermission.equals("NONE") && !security.hasEntityPermission(basePermission, "_VIEW", session)){
    					permission = false;
    				}
    			}
    		}
    		   
    		if(permission){
    			String thisApp = webappInfo.getContextRoot();
    			String contextPath = request.getContextPath();
    			
    			boolean selected = false;
    			if(thisApp.equals(contextPath) || thisApp.equals(contextPath + "/")){
    				selected = true;
    			}
    			
    			NavInfoBean navInfo = new NavInfoBean();
        		navInfo.setDescription(UtilProperties.getMessage("CommonUiLabels",description, locale));
        		navInfo.setName(UtilProperties.getMessage("CommonUiLabels",name, locale));
        		navInfo.setSelected(selected);
        		
        		if(!thisApp.equals("/")){
        			thisApp += "/control/main";
        		}
//        		thisApp += "?externalLoginKey=" + externalLoginKey;
        		navInfo.setLinkUrl(thisApp);
        		
        		navInfoList.add(navInfo);
        		
//    			Map<String,String> mapsss = new HashMap<String,String>();
//    			try {
//					dispatcher.runSync("getVisualThemeResources", mapsss);
//				} catch (GenericServiceException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
              
    		}
    		   
		}
    	
//    	Calendar time= Calendar.getInstance(); 
//    	time.setTime(new Date());
//    	Date data = time.getTime();
    	
    	
    	
    	HashMap<String, String> languageMap = new HashMap<String, String>();
    	
    	String commonPreferences = UtilProperties.getMessage("CommonUiLabels","CommonPreferences", locale);
    	String commonLanguageTitle = UtilProperties.getMessage("CommonUiLabels","CommonLanguageTitle", locale);
    	String commonVisualThemes = UtilProperties.getMessage("CommonUiLabels","CommonVisualThemes", locale);
    	String commonWelcome = UtilProperties.getMessage("CommonUiLabels","CommonWelcome", locale);
    	
    	languageMap.put("commonPreferences", commonPreferences);
    	languageMap.put("commonLanguageTitle", commonLanguageTitle);
    	languageMap.put("commonVisualThemes", commonVisualThemes);
    	languageMap.put("commonWelcome", commonWelcome);
    	
    	TimeZone timeZone = UtilHttp.getTimeZone(request);
    	String timeZoneDisplayName = timeZone.getDisplayName(timeZone.useDaylightTime(), TimeZone.LONG, locale);
    	
    	
    	List<Locale> availableList = UtilMisc.availableLocales();
    	Map<String,String> availableMap = new HashMap<String,String>();
    	for (Locale availablelocale : availableList) {
    		availableMap.put(availablelocale.toString(), availablelocale.getDisplayName(availablelocale));
		}
    	
    	
    	
    	
    	map.put("availableMap", availableMap);
    	map.put("timeZoneDisplayName", timeZoneDisplayName);
    	map.put("languageMap", languageMap);
    	map.put("userLoginId", userLogin ==null ? null : userLogin.get("userLoginId"));
//    	map.put("person", request.getAttribute("person"));
//    	map.put("partyGroup", request.getAttribute("partyGroup"));
    	map.put("timeNow", new Date());
    	map.put("externalLoginKey", externalLoginKey);
    	map.put("locale", locale.getDisplayName());
    	map.put("navInfoList", navInfoList);
    	
    	map.put("success", true);
        return map;
    }
    
}