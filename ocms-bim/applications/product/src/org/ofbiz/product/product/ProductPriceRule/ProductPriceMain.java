package org.ofbiz.product.product.ProductPriceRule;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.security.Security;
import org.ofbiz.service.ServiceUtil;

import com.ibm.icu.math.BigDecimal;


public class ProductPriceMain {

	
	
	public static Map<String, Object> initProductPriceRule(HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			Map<String, Object> resultMap = ServiceUtil.returnSuccess();
			
	        //查询店铺
	        HttpSession session = request.getSession();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String username = (String) userLogin.get("userLoginId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username), false);
			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId")));
			Set<String> storeId = new HashSet<String>();
			for (GenericValue genericValue : resultList) {
				storeId.add((String) genericValue.get("productStoreId"));
			}
			List<String> storeIds = new ArrayList<String>();
			storeIds.addAll(storeId);
			Collections.sort(storeIds);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (String item : storeIds) {
				resultList = delegator.findByAnd("ProductStore", UtilMisc.toMap("productStoreId", item));
				for (GenericValue result : resultList) {
					Map<String, String> storeInfo = new HashMap<String, String>();
					storeInfo.put("productStoreId", result.getString("productStoreId"));
					storeInfo.put("storeName", result.getString("storeName"));
					list.add(storeInfo);
				}
				
			}
			resultMap.put("store", list);
			return resultMap;
		} catch (Exception e) {
			return ServiceUtil.returnError("error");
		}
	}
	

	
	public static Map<String, Object> categoryPriceRuleFindProduct(HttpServletRequest request, HttpServletResponse response){
    	try {
			String query = request.getParameter("query");
			String storeId = request.getParameter("storeId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			HttpSession session = request.getSession();
		    GenericValue userLogin = (GenericValue)session.getAttribute("userLogin");
		    String partyId = userLogin.getString("partyId");
			HashSet<String> storeIdList = new HashSet<String>();
			
			if(storeId == null){
				List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("partyId", partyId));
				if(productStoreRoleList.size() == 0){
					return ServiceUtil.returnError("you have no store");
				}
				for (GenericValue genericValue : productStoreRoleList) {
					storeIdList.add(genericValue.getString("productStoreId"));
				}
				
			}else{
				storeIdList.add(storeId);
			}
			String storeIdCollection = " (";
			for (String string : storeIdList) {
				storeIdCollection =  storeIdCollection + "'" + string + "',";
			}
			
			storeIdCollection = storeIdCollection.substring(0, storeIdCollection.length() - 1);
			storeIdCollection = storeIdCollection + ")";
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			/*
			String sql = " select * from  (select distinct PRODUCT_ID , FACILITY_ID from INVENTORY_ITEM) as ii "
					+" left join PRODUCT pr on (ii.PRODUCT_ID=pr.PRODUCT_ID) "
					+" left join PRODUCT_STORE ps on (ps.INVENTORY_FACILITY_ID =ii.FACILITY_ID) "
					+" where ii.FACILITY_ID in (select INVENTORY_FACILITY_ID from PRODUCT_STORE ps where ps.PRODUCT_STORE_ID in" + storeIdCollection + ") "
					+" and UPPER(ii.PRODUCT_ID) LIKE UPPER('%param%') OR UPPER(pr.INTERNAL_NAME) LIKE UPPER('%param%')  "
					+" OR UPPER(pr.BRAND_NAME) LIKE UPPER('%param%') order by ii.PRODUCT_ID limit 10 ";
			sql=sql.replaceAll("param", query);*/
			
			
		
			 String sql = " select * from  ( "
					+" select ii.PRODUCT_ID from  INVENTORY_ITEM as ii "
					+" where ii.FACILITY_ID in  "
					+" (  "
					+" 	select ps.INVENTORY_FACILITY_ID  "
					+" 	from PRODUCT_STORE as ps  WHERE ps.PRODUCT_STORE_ID IN " + storeIdCollection
					+" ) group by ii.PRODUCT_ID "
					+" ) as a "
					+" left join PRODUCT p on (p.PRODUCT_ID=a.PRODUCT_ID) "
					+" where  "
					+" UPPER(p.PRODUCT_ID) LIKE UPPER(\'%param%\') OR UPPER(p.INTERNAL_NAME) LIKE UPPER(\'%param%\') OR UPPER(p.BRAND_NAME) LIKE UPPER(\'%param%\') LIMIT 10 ";
			
			
			 sql=sql.replaceAll("param", query);
			
			ResultSet rs = processor.executeQuery(sql);
			
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			List<Object> items = new ArrayList<Object>();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				String productId = rs.getString("PRODUCT_ID");
				map.put("id", productId);
				String internalName = rs.getString("INTERNAL_NAME");
				if(internalName == null ){
					internalName = "";
				}
				String brandName = rs.getString("BRAND_NAME");
				if(brandName == null){
					brandName = "";
				}
				map.put("productId", productId);
				map.put("model", internalName);
				map.put("brand", brandName);
				items.add(map);
			}
			resultMap.put("items", items);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
    }
	
	
	public static Map<String, Object> securityPermission(HttpServletRequest request, HttpServletResponse response){
		Security security = (Security) request.getAttribute("security");
		String findProductp = "N";
		if(security.hasEntityPermission("PPR", "_FIND", request.getSession())){
			findProductp = "Y";
		}
		String findLog = "N";
		if(security.hasEntityPermission("PPR", "_FIND_LOG", request.getSession())){
			findLog = "Y";
		}
		
		String batchManage = "N";
		if(security.hasEntityPermission("PPR", "_BATCH_MANAGE", request.getSession())){
			batchManage = "Y";
		}
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		resultMap.put("findProductp", findProductp);
		resultMap.put("findLog", findLog);
		resultMap.put("batchManage", batchManage);
		return resultMap;
	}
}
