package org.ofbiz.ordermgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.ServiceUtil;

public class StoreAddressServices {
	
	public static Map<String, Object> findGeo(HttpServletRequest request, HttpServletResponse response) {
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String geoId = request.getParameter("geoId");
			if(geoId == null || geoId.equals("")){
				geoId = (String) getSystemSettings().get("countryId");
			}
			int index = 0;
			List<GenericValue> geoAssocList = delegator.findByAnd("GeoAssoc", UtilMisc.toMap("geoId", geoId));
			if(geoAssocList.size() > 0){
				Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
				List<Object> reslutList = new ArrayList<Object>();
				for (GenericValue geoAssoc : geoAssocList) {
					GenericValue geo = delegator.findOne("Geo", true , UtilMisc.toMap("geoId", geoAssoc.getString("geoIdTo")));
//						resultMap.put(geo.getString("geoId"), geo.getString("geoName"));
					
					Map<Integer, Object> map = new HashMap<Integer, Object>();
					CreateOrderGeo createOrderGeo = new CreateOrderGeo();
					createOrderGeo.setId(geo.getString("geoId"));
					createOrderGeo.setName(geo.getString("geoName"));
					map.put(index++, createOrderGeo);
					reslutList.add(createOrderGeo);
				}
				resultMap.put("reslutList", reslutList);
				return resultMap;
			}else{
				return ServiceUtil.returnError("query failed");
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
		
	}
	
	public static Map<String, Object> getSystemSettings() {
		Map<String, Object> retmap=new HashMap<String, Object>();
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key);
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		String tempfilepath = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsTempFilePath");
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId);
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		retmap.put("tempfilepath", tempfilepath);
		return retmap;
	}
	
	
	public static Map<String,Object> updateStoreAddress(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String,Object>();		
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		String countryId = (String) getSystemSettings().get("countryId");
		String provinceId = request.getParameter("provinceId");
		String cityId = request.getParameter("cityId");
		String areaId = request.getParameter("areaId");
		String productStoreId = request.getParameter("productStoreId");
		try {
			GenericValue productStore = delegator.findByPrimaryKey("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
			productStore.set("countryId", countryId);
			productStore.set("provinceId", provinceId);
			productStore.set("cityId", cityId);
			productStore.set("areaId", areaId);
			delegator.store(productStore);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
}
