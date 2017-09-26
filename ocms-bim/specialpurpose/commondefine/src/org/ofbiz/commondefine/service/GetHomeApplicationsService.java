package org.ofbiz.commondefine.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.ofbiz.base.component.ComponentConfig;
import org.ofbiz.base.component.ComponentConfig.WebappInfo;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.foundation.infrastructure.User;

import javolution.util.FastList;
import javolution.util.FastMap;

public class GetHomeApplicationsService {

	  public static Map<String, Object> getHomeApplications(DispatchContext dctx, Map context) {
	        LocalDispatcher dispatcher = dctx.getDispatcher();
	        Delegator delegator = dctx.getDelegator();
	        GenericValue userLogin = (GenericValue) context.get("userLogin");
	        Map<String, Object> result = new FastMap<String, Object>();
	        User user = userLogin==null ? null : new User(userLogin, delegator);
			try {
				List<GenericValue> opentapsWebAppsList = delegator.findAll("OpentapsWebApps");
		        List<WebappInfo> webapps = ComponentConfig.getAllWebappResourceInfos();
		        Map<String, String[]> webappsMap = FastMap.newInstance();
		        for (WebappInfo webapp : webapps) {
		            webappsMap.put(webapp.getName() , webapp.getBasePermission());
		        }
		        List<GenericValue> apps = FastList.newInstance();
		        if (UtilValidate.isNotEmpty(opentapsWebAppsList)) {
		            for (GenericValue webapp : opentapsWebAppsList) {
//		        	 apps.add(webapp);
		        	
		                String[] permissions = webappsMap.get(webapp.getString("applicationId"));
		                if (user != null) {
		                    boolean permitted = true;
		                    if (permissions != null) {
		                        //  if there are permissions for this application, then check if the user can view it
		                        for (int i = 0; i < permissions.length; i++) {
		                            // if the application has basePermissions and user doesn't has VIEW/ADMIN permissions on them, don't get the app
		                            try {
		                                if (!"NONE".equals(permissions[i]) && !user.hasPermission(permissions[i], "VIEW") && !user.hasAdminPermissionsForModule(permissions[i])) {
		                                    permitted = false;
		                                    break;
		                                }
		                            } catch (Exception e) {
		                            }
		                        }
		                    }
		                    if (permitted) {
		                        apps.add(webapp);
		                    }
		                } else {
		                    // if user is not authenticated
		                    if (permissions == null) {
		                        // if there are no permissions required for the application, or if it is an external link,
		                        apps.add(webapp);
		                        
		                    } else if (permissions.length > 0) {
		                        //  or, if the application is defined with permission of "NONE",  such as the ofbiz e-commerce store
		                        if ("NONE".equals(permissions[0])) {
		                            //permissions[0] will always exists
		                            apps.add(webapp);
		                        }
		                    }
		                }
		            }
		        }
		        String TimeZonestring;
		        if(userLogin == null){
		        	TimeZonestring = "";
		        }else{
		        	TimeZonestring =userLogin.getString("lastTimeZone");
		        }
		        
		        
		        if(TimeZonestring==null||"".equals(TimeZonestring))
		        {
		        	TimeZone timeZone = (TimeZone) context.get("timeZone");
		        	if(timeZone!=null)
		        		TimeZonestring = timeZone.getID();
		        }
		        Locale locale = (Locale) context.get("locale");
		        
		        TimeZone timeZones = TimeZone.getTimeZone(TimeZonestring);
		        
		        int rawOffset = timeZones.getRawOffset();
		        result.put("timeZoneId", TimeZonestring);
		        
		        result.put("timeNow", new Date().getTime());
		        result.put("rawOffset", rawOffset);
		        
		        
		        String currenttime = UtilDateTime.timeStampToStringwww(timeZones,locale);
		        result.put("timeZoneId", TimeZonestring);
		        result.put("currenttime", currenttime);
		        Map<String, Object> ret = GetSystemSetting.getSystemSettings(dctx, context);
		        result.put("isShip", ret.get("isShip"));
		        result.put("SystemSettingsUomid", ret.get("systemsettinguomid"));
		        result.put("imei", ret.get("imei"));
		        result.put("imei", ret.get("imei"));
		        result.put("piwikHost", ret.get("piwikHost"));
		        result.put("piwikPort", ret.get("piwikPort"));
		        result.put("piwikSiteId", ret.get("piwikSiteId"));
		        result.put("apps", apps);
			} catch (GenericEntityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			/*yzl20170601*/
//	        String TimeZonestring =userLogin.getString("lastTimeZone");
//	        if(TimeZonestring==null||"".equals(TimeZonestring))
//	        {
//	        	TimeZone timeZone = (TimeZone) context.get("timeZone");
//	        	if(timeZone!=null)
//	        		TimeZonestring = timeZone.getID();
//	        }
//	        Locale locale = (Locale) context.get("locale");
//	        String currenttime = UtilDateTime.timeStampToStringwww(TimeZone.getTimeZone(TimeZonestring),locale);
//	        result.put("timeZoneId", TimeZonestring);
//	        result.put("currenttime", currenttime);
			
	        return result;
	    }

    
}