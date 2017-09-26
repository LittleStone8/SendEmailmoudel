package org.ofbiz.commondefine.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.service.DispatchContext;

public class GetSystemSetting {
	
	
	
	
	public static Map<String, Object> getSystemSettings(DispatchContext dctx, Map<String, ? extends Object> context) {
		Map<String, Object> retmap=new HashMap<String, Object>();
//		Properties prop = LoadProperties();
//		String key = prop.getProperty("SystemSettingsUomid");   
//		retmap.put("systemsettinguomid", key);
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key);
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		
		String piwikHost = UtilProperties.getPropertyValue("SystemSettings.properties", "piwikHost");
		String piwikPort = UtilProperties.getPropertyValue("SystemSettings.properties", "piwikPort");
		String piwikSiteId = UtilProperties.getPropertyValue("SystemSettings.properties", "piwikSiteId");
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId);
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		
		retmap.put("piwikHost", piwikHost);
		retmap.put("piwikPort", piwikPort);
		retmap.put("piwikSiteId", piwikSiteId);
		return retmap;
	}
	
//	public static  Properties LoadProperties()
//	{
//		InputStream in = GetSystemSetting.class.getResourceAsStream("/SystemSettings.properties");   
//		Properties p = new Properties();   
//		try {
//			p.load(in);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		return p;
//	}
	
	
}