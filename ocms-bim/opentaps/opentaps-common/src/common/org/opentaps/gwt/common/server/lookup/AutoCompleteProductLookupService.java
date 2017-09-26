/*
 * Copyright (c) Open Source Strategies, Inc.
 *
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.opentaps.gwt.common.server.lookup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.base.entities.ProductAndGoodIdentification;
import org.opentaps.foundation.entity.EntityInterface;
import org.opentaps.foundation.infrastructure.InfrastructureException;
import org.opentaps.gwt.common.client.lookup.configuration.ProductLookupConfiguration;
import org.opentaps.gwt.common.server.HttpInputProvider;
import org.opentaps.gwt.common.server.InputProviderInterface;
import org.opentaps.gwt.common.server.lookup.bean.CreateOrderProductInfo;
import org.opentaps.gwt.common.server.lookup.bean.IMEIVo;

import com.ibm.icu.math.BigDecimal;



/**
 * The RPC service used to populate the Product autocompleters widgets.
 */
public class AutoCompleteProductLookupService extends EntityLookupAndSuggestService {

	protected AutoCompleteProductLookupService(InputProviderInterface provider) {
        super(provider, ProductLookupConfiguration.LIST_OUT_FIELDS);
    }

    /**
     * AJAX event to suggest Product.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the JSON response
     * @throws InfrastructureException if an error occurs
     */
    public static String suggestProduct(HttpServletRequest request, HttpServletResponse response) throws InfrastructureException {
        InputProviderInterface provider = new HttpInputProvider(request);
        JsonResponse json = new JsonResponse(response);
        AutoCompleteProductLookupService service = new AutoCompleteProductLookupService(provider);
        service.suggestProduct();
        return json.makeSuggestResponse(ProductLookupConfiguration.OUT_PRODUCT_ID, service);
    }

    /**
     * Suggests a list of <code>Product</code>.
     * @return the list of <code>Product</code>, or <code>null</code> if an error occurred
     */
    public List<ProductAndGoodIdentification> suggestProduct() {

        EntityCondition activeCondition = EntityCondition.makeCondition(EntityOperator.OR,
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, null),
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, "Y")
                );

        return findSuggestMatchesAnyOf(ProductAndGoodIdentification.class, ProductLookupConfiguration.LIST_LOOKUP_FIELDS, activeCondition);
    }

    /**
     * AJAX event to suggest Product.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the JSON response
     * @throws InfrastructureException if an error occurs
     */
    public static String suggestProductForCart(HttpServletRequest request, HttpServletResponse response) throws InfrastructureException {
        InputProviderInterface provider = new HttpInputProvider(request);
        JsonResponse json = new JsonResponse(response);
        AutoCompleteProductLookupService service = new AutoCompleteProductLookupService(provider);
        service.suggestProductForCart();
        return json.makeSuggestResponse(ProductLookupConfiguration.OUT_PRODUCT_ID, service);
    }
    
    /**
     * Suggests a list of <code>Product</code>.
     * @return the list of <code>Product</code>, or <code>null</code> if an error occurred
     */
    public List<ProductAndGoodIdentification> suggestProductForCart() {

        EntityCondition activeCondition = EntityCondition.makeCondition(EntityOperator.OR,
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, null),
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, "Y")
                );
        EntityCondition filterOutVirtualCondition = EntityCondition.makeCondition(EntityOperator.AND,
                EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isVirtual.name(), EntityOperator.NOT_EQUAL, "Y"),
                activeCondition);
        List<ProductAndGoodIdentification> a = findSuggestMatchesAnyOf(ProductAndGoodIdentification.class, ProductLookupConfiguration.LIST_LOOKUP_FIELDS, filterOutVirtualCondition); 
        for (ProductAndGoodIdentification productAndGoodIdentification : a) {
			System.out.println(productAndGoodIdentification);
		}
        return findSuggestMatchesAnyOf(ProductAndGoodIdentification.class, ProductLookupConfiguration.LIST_LOOKUP_FIELDS, filterOutVirtualCondition);
    }

    @Override
    public String makeSuggestDisplayedText(EntityInterface product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getString(ProductLookupConfiguration.OUT_PRODUCT_ID)).append(":").append(product.getString(ProductLookupConfiguration.OUT_INTERNAL_NAME));
        return sb.toString();
    }

    /**
     * 根据productId和modle查询
     * @param request
     * @param response
     * @return
     */
    
    public static Map<String, Object> createOrderFindProduct(HttpServletRequest request, HttpServletResponse response){
    	try {
			String query = request.getParameter("query");
			String storeId = request.getParameter("storeId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String regEx1 = "^\\d{8}$";
			String regEx2 = "^\\d{13}$";
			Pattern pattern1 = Pattern.compile(regEx1);
			Pattern pattern2 = Pattern.compile(regEx2);
			Matcher matcher1 = pattern1.matcher(query);
			Matcher matcher2 = pattern2.matcher(query);
			boolean rs1 = matcher1.matches();
			boolean rs2 = matcher2.matches();
			
//			String sql = "  SELECT p.PRODUCT_ID ,p.INTERNAL_NAME, GROUP_CONCAT(DISTINCT pf.DESCRIPTION ORDER BY pf.PRODUCT_FEATURE_TYPE_ID ) as DESCRIPTION "
//						+" FROM PRODUCT_FEATURE pf "
//						+" right JOIN PRODUCT_FEATURE_APPL pfa ON pf.PRODUCT_FEATURE_ID = pfa.PRODUCT_FEATURE_ID "
//						+" right JOIN (select * from PRODUCT where IS_VIRTUAL <> \'Y\' AND (IS_ACTIVE IS NULL OR IS_ACTIVE = \'Y\') AND "
//						+" (UPPER(PRODUCT_ID) LIKE UPPER(\'%param%\') OR UPPER(INTERNAL_NAME) LIKE UPPER(\'%param%\'))) p "
//						+" ON p.PRODUCT_ID = pfa.PRODUCT_ID GROUP BY p.PRODUCT_ID limit 16 ";
			
			String whereScraech = null;
			if(rs1 || rs2){
				whereScraech= " gi.ID_VALUE='param' ";
			}else{
				whereScraech= " (UPPER(p.PRODUCT_ID) LIKE UPPER('%param%') OR UPPER(p.INTERNAL_NAME) LIKE UPPER('%param%')) ";
			}
			
			
			String sql = " SELECT "
					+" p.PRODUCT_ID, "
					+" p.INTERNAL_NAME, "
					+" GROUP_CONCAT(DISTINCT pf.DESCRIPTION "
	                +" ORDER BY pf.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION, "
	                +" gi.ID_VALUE, "
	                +" NULL AS SERIAL_NUMBER "
	                +" FROM "
	                +" PRODUCT_FEATURE pf "
	                +" RIGHT JOIN PRODUCT_FEATURE_APPL pfa ON pf.PRODUCT_FEATURE_ID = pfa.PRODUCT_FEATURE_ID "
	                +" RIGHT JOIN (SELECT  "
	                +" * "
	                +" FROM "
	                +" PRODUCT "
	                +" WHERE "
	                +" IS_VIRTUAL <> 'Y' "
	                +" AND (IS_ACTIVE IS NULL OR IS_ACTIVE = 'Y')) p ON p.PRODUCT_ID = pfa.PRODUCT_ID "
	                +" LEFT JOIN (SELECT  "
	                +" * "
	                +" FROM "
	                +" GOOD_IDENTIFICATION "
	                +" WHERE "
	                +" GOOD_IDENTIFICATION_TYPE_ID = 'EAN') gi ON gi.PRODUCT_ID = p.PRODUCT_ID "
	                +" WHERE "
	                + whereScraech
	                +" GROUP BY p.PRODUCT_ID "
	                +" LIMIT 16 ";

 
 

			
			
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
				String description = rs.getString("DESCRIPTION");
				if(description == null){
					description = "";
				}
				map.put("text", internalName + " " + description);
				
//				String serialNumber = rs.getString("SERIAL_NUMBER");
//				if(serialNumber != null ){
//					map.put("serialNumber", serialNumber);
//				}else{
//					map.put("serialNumber", "");
//				}
				
				String eanValue = rs.getString("ID_VALUE");
				if(eanValue != null){
					map.put("eanValue", eanValue);
				}else{
					map.put("eanValue", "");
				}
//				String apt = rs.getString("APT");
//				if(apt == null && "".equals(apt)){
//					map.put("apt", "0");
//				}else{
//					map.put("apt", apt);
//				}
				
				
				String isImei = "N";
				
				if(storeId != null){
					GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", storeId), true);
					String facilityId = productStore.getString("inventoryFacilityId");
					List<GenericValue> inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("facilityId", facilityId , "productId" , productId ));
					
					BigDecimal total = new BigDecimal(0);
					for (GenericValue inventoryItem : inventoryItemList) {
						String availableToPromiseTotal = inventoryItem.getString("availableToPromiseTotal");
						if(availableToPromiseTotal==null)
							availableToPromiseTotal="0.0";
						if(new BigDecimal(availableToPromiseTotal).compareTo(new BigDecimal("0")) == 1){
							total = total.add(new BigDecimal(availableToPromiseTotal));
						}
						String imei = inventoryItem.getString("serialNumber");
						if(imei!=null && !"".equals(imei) && inventoryItem.getBigDecimal("availableToPromiseTotal").intValue()>0){
							isImei="Y";
						}
					}
					String num = total.toString();
					if(!"0".equals(num)){
						map.put("availableToPromiseTotal", num.substring(0, num.indexOf(".")));
					}else{
						map.put("availableToPromiseTotal", "0");
					}
					
//					inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId" , productId));
//					for (GenericValue inventoryItem : inventoryItemList) {
//						String imei = inventoryItem.getString("serialNumber");
//						if(imei!=null && !"".equals(imei)){
//							isImei="Y";
//						}
//					}
					
					map.put("isImei", isImei);
				}
				items.add(map);
				
			}
			
			if((rs1 || rs2) && items.isEmpty()){
				return ServiceUtil.returnError(" Cannot match the EAN code in the system. Please scan the code again or check.");
			}
			resultMap.put("items", items);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
    }
    
    /**
     * 根据ean查询产品
     * @param request
     * @param response
     * @return
     */
    public static Map<String, Object> createOrderFindProductByEan(HttpServletRequest request, HttpServletResponse response){
    	try {
			String query = request.getParameter("query");
			String storeId = request.getParameter("storeId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String regEx1 = "^\\d{8}$";
			String regEx2 = "^\\d{13}$";
			Pattern pattern1 = Pattern.compile(regEx1);
			Pattern pattern2 = Pattern.compile(regEx2);
			Matcher matcher1 = pattern1.matcher(query);
			Matcher matcher2 = pattern2.matcher(query);
			boolean rs1 = matcher1.matches();
			boolean rs2 = matcher2.matches();
			

			//限制查询条件
			String whereScraech = null;
			if(rs1 || rs2){
				whereScraech= " gi.ID_VALUE='param' ";
			}else{
				return ServiceUtil.returnError("query failed,please input 8 or 13 digits");
			}
			
			
			String sql = " SELECT "
					+" p.PRODUCT_ID, "
					+" p.INTERNAL_NAME, "
					+" GROUP_CONCAT(DISTINCT pf.DESCRIPTION "
	                +" ORDER BY pf.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION, "
	                +" gi.ID_VALUE, "
	                +" NULL AS SERIAL_NUMBER "
	                +" FROM "
	                +" PRODUCT_FEATURE pf "
	                +" RIGHT JOIN PRODUCT_FEATURE_APPL pfa ON pf.PRODUCT_FEATURE_ID = pfa.PRODUCT_FEATURE_ID "
	                +" RIGHT JOIN (SELECT  "
	                +" * "
	                +" FROM "
	                +" PRODUCT "
	                +" WHERE "
	                +" IS_VIRTUAL <> 'Y' "
	                +" AND (IS_ACTIVE IS NULL OR IS_ACTIVE = 'Y')) p ON p.PRODUCT_ID = pfa.PRODUCT_ID "
	                +" LEFT JOIN (SELECT  "
	                +" * "
	                +" FROM "
	                +" GOOD_IDENTIFICATION "
	                +" WHERE "
	                +" GOOD_IDENTIFICATION_TYPE_ID = 'EAN') gi ON gi.PRODUCT_ID = p.PRODUCT_ID "
	                +" WHERE "
	                + whereScraech
	                +" GROUP BY p.PRODUCT_ID "
	                +" LIMIT 16 ";

 
 

			
			
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
				String description = rs.getString("DESCRIPTION");
				if(description == null){
					description = "";
				}
				map.put("text", internalName + " " + description);
				
//				String serialNumber = rs.getString("SERIAL_NUMBER");
//				if(serialNumber != null ){
//					map.put("serialNumber", serialNumber);
//				}else{
//					map.put("serialNumber", "");
//				}
				
				String eanValue = rs.getString("ID_VALUE");
				if(eanValue != null){
					map.put("eanValue", eanValue);
				}else{
					map.put("eanValue", "");
				}
//				String apt = rs.getString("APT");
//				if(apt == null && "".equals(apt)){
//					map.put("apt", "0");
//				}else{
//					map.put("apt", apt);
//				}
				
				
				String isImei = "N";
				
				if(storeId != null){
					GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", storeId), true);
					String facilityId = productStore.getString("inventoryFacilityId");
					List<GenericValue> inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("facilityId", facilityId , "productId" , productId ));
					
					BigDecimal total = new BigDecimal(0);
					for (GenericValue inventoryItem : inventoryItemList) {
						String availableToPromiseTotal = inventoryItem.getString("availableToPromiseTotal");
						if(new BigDecimal(availableToPromiseTotal).compareTo(new BigDecimal("0")) == 1){
							total = total.add(new BigDecimal(availableToPromiseTotal));
						}
						String imei = inventoryItem.getString("serialNumber");
						if(imei!=null && !"".equals(imei)){
							isImei="Y";
						}
					}
					String num = total.toString();
					if(!"0".equals(num)){
						map.put("availableToPromiseTotal", num.substring(0, num.indexOf(".")));
					}else{
						map.put("availableToPromiseTotal", "0");
					}
					
//					inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId" , productId));
//					for (GenericValue inventoryItem : inventoryItemList) {
//						String imei = inventoryItem.getString("serialNumber");
//						if(imei!=null && !"".equals(imei)){
//							isImei="Y";
//						}
//					}
					
					map.put("isImei", isImei);
				}
				items.add(map);
				
			}
			
			if((rs1 || rs2) && items.isEmpty()){
				return ServiceUtil.returnError(" Cannot match the EAN code in the system. Please scan the code again or check.");
			}
			resultMap.put("items", items);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
    }
    
    
    /**
     * 模糊查询IMEI
     * @param request
     * @param response
     * @return
     */
    public static Map<String, Object> createOrderFindImei(HttpServletRequest request, HttpServletResponse response){
    	Map<String,Object> result = new HashMap<String, Object>();
    	Delegator delegator = (Delegator) request.getAttribute("delegator");	
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		String imei = request.getParameter("query");
		if(imei == null || imei.equals("")){
			return result;
		}
		//sql
		StringBuffer sql = new StringBuffer("select PRODUCT_ID as productId,SERIAL_NUMBER as imei from INVENTORY_ITEM where STATUS_ID = 'INV_AVAILABLE' AND SERIAL_NUMBER like '%"+imei+"%'");
		try {
			result = EncapsulatedQueryResultsUtil.getResults(sql, 1, 10, IMEIVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
    }
    /**
     * imei查询
     * @param request
     * @param response
     * @return
     */
    public static Map<String, Object> createOrderFindProductByImei(HttpServletRequest request, HttpServletResponse response){
    	try {
	    	Map<String,Object> result = new HashMap<String, Object>();
	    	Delegator delegator = (Delegator) request.getAttribute("delegator");	
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String storeId = request.getParameter("productStoreId");
			//接收参数
			String query = request.getParameter("query");
			if(query == null || query.equals("")){
				return result;
			}
			//是否是imei
			String regEx1 = "^\\d{15}$";
			Pattern pattern1 = Pattern.compile(regEx1);
			Matcher matcher1 = pattern1.matcher(query);
			boolean rs1 = matcher1.matches();
			if(!rs1){
				return ServiceUtil.returnError(" Cannot match IMEI code in the system. Please scan the code again or check ");
			}
			//查询仓库
			String facilityId = null;
			if(storeId != null && !"".equals(storeId)){               
				GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", storeId), true);
				facilityId = productStore.getString("inventoryFacilityId");
			}else{
				return ServiceUtil.returnError("Can not get the store , please try again");
			}
			
			//sql
			String sql = "select PRODUCT_ID as productId,SERIAL_NUMBER as imei , AVAILABLE_TO_PROMISE_TOTAL as atp from INVENTORY_ITEM "
					+ "where (STATUS_ID = 'INV_AVAILABLE') AND SERIAL_NUMBER = '"+query+"'  and FACILITY_ID = '" + facilityId + "'";
			
			
			ResultSet rs;
			
			rs = processor.executeQuery(sql);
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			List<Object> items = new ArrayList<Object>();
			while(rs.next()){
				String productId = rs.getString("productId");
				if(productId == null || "".equals(productId)){
					return ServiceUtil.returnError("can not find productId , please try other ones");
				}
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("productId", productId);
				
				String imei = rs.getString("imei");
				if(imei == null || "".equals(imei)){
					return ServiceUtil.returnError("Can not find IMEI , please try other ones");
				}
				item.put("imei", imei);
				
				String atp = rs.getString("atp");
				if(atp == null || "".equals(atp)){
					item.put("atp", "0");
				}else{
					String[] sb = atp.split(".");
					item.put("atp", atp.substring(0, atp.indexOf(".")));
				}
				
				items.add(item);
			}
			if(items.isEmpty()){
				return ServiceUtil.returnError("Cannot match IMEI code in the system. Please scan the code again or check");
			}
			resultMap.put("result", items);
			return resultMap;
		} catch (GenericDataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ServiceUtil.returnError("Query error");
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ServiceUtil.returnError("Query error");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("Query sql error");
		}
		
		

		
    }
    
    

}
