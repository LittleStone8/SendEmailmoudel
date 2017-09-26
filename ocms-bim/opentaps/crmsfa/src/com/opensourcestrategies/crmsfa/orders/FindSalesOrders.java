package com.opensourcestrategies.crmsfa.orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.ServiceUtil;

public class FindSalesOrders {

	public static Map<String, Object> findSalesOrders(HttpServletRequest request, HttpServletResponse response) {
		
		
		
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		String userLoginId = (String) userLogin.get("userLoginId");
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		try {
			String orderId =  request.getParameter("orderId");
			String partyId = request.getParameter("partyId");
			String productStoreId = request.getParameter("productStoreId");
			String shipment = request.getParameter("shipment");
			String paymentWay = request.getParameter("paymentWay");
			String isFixOrder = request.getParameter("isFixOrder");
			String statusId = request.getParameter("statusId");
			String fromDate =  request.getParameter("fromDate");
			String thruDate = request.getParameter("thruDate");
			String productId = request.getParameter("productId");
			String pageN = request.getParameter("pageNum");
			String pageSize = request.getParameter("pageSize");
			
			List<GenericValue> productStoreList = delegator.findAll("ProductStore");
			Map<String, String> productStoreMap = new HashMap<String, String>();
			for (GenericValue genericValue : productStoreList) {
				productStoreMap.put(genericValue.getString("productStoreId"), genericValue.getString("storeName"));
			}
			
			
			String selectCountSql = "select count(*)  AS totalNum from ( ";
			String selectAllSql = "select * from ( ";
			String endSql = " )a ";

			String paymentSql = " WHERE a.PAYMENT_METHOD_TYPE_ID LIKE '%"+ paymentWay +"%' ";
			String productSql = " AND a.PRODUCT_IDS LIKE '%"+ productId +"%' ";
			
			String limitSql = " LIMIT " + (Integer.parseInt(pageN) - 1)*20 +"," + pageSize ;
			
			
			String ohWhereSql = " ";
			//orderId条件
			if(orderId != null && !"".equals(orderId)){
				ohWhereSql = ohWhereSql + " AND ORDER_ID = '" + orderId+ "'";
			}
			//添加店铺查询，店铺过滤。不相关的店铺查询不到
			if(productStoreId != null && !"".equals(productStoreId)){
				ohWhereSql = ohWhereSql + " AND PRODUCT_STORE_ID = '" + productStoreId+ "'";
			}else{
				GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
				List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId")));
				List<String> storeIds = new ArrayList<String>();
				for (GenericValue genericValue : productStoreRoleList) {
					String queryStoreId = genericValue.getString("productStoreId");
					if(!storeIds.contains(queryStoreId)){
						storeIds.add(queryStoreId);
					}
				}
				if(!storeIds.isEmpty()){
					for (int i = 0; i < storeIds.size(); i++) {
						if(i == 0){
							ohWhereSql = ohWhereSql + " AND (PRODUCT_STORE_ID = " + " '" + storeIds.get(i) + "'";
						}else {
							ohWhereSql = ohWhereSql + " OR PRODUCT_STORE_ID = '" + storeIds.get(i) + "'" ;
						}
					}
					ohWhereSql = ohWhereSql + ") " ;
				}
				
			}
			if(statusId != null && !"".equals(statusId)){
				ohWhereSql = ohWhereSql + " AND STATUS_ID = '" + statusId+ "'";
			}
			if(shipment != null && !"".equals(shipment)){
				ohWhereSql = ohWhereSql + " AND IS_NEED_SHIP = '" + shipment+ "'";
			}
			if(isFixOrder != null && !"".equals(isFixOrder)){
				ohWhereSql = ohWhereSql + " AND IS_FIX_ORDER = '" + isFixOrder+ "'";
			}
			if(partyId != null && !"".equals(partyId)){
				ohWhereSql = ohWhereSql + " AND BUYER_ID = '" + partyId+ "'";
			}
			
			
			
			
			
			
			String sql = " SELECT "
					+" oh.ORDER_DATE, "
					+" oh.ORDER_ID, "
					+" oh.BUYER_ID, "
					+" per.FIRST_NAME_LOCAL, "
					+" oh.STATUS_ID, "
					+" si.DESCRIPTION AS STATUS_DESCRIPTION, "
					+" oh.ENTRY_DATE, "
					+" oh.PRODUCT_STORE_ID,"
					+" GROUP_CONCAT(oi.PRODUCT_ID) AS PRODUCT_IDS, "
					+" GROUP_CONCAT(opp.PAYMENT_METHOD_TYPE_ID) AS PAYMENT_METHOD_TYPE_ID, "
					+" oh.GRAND_TOTAL, "
					+" SUM(opp.MAX_AMOUNT) AS AMOUNT, "
					+" oh.IS_FIX_ORDER "
					+" FROM (SELECT * FROM ORDER_HEADER WHERE ORDER_TYPE_ID = 'SALES_ORDER' "
					+ ohWhereSql
					+ ") oh "
					+ " LEFT JOIN ORDER_ITEM oi ON oh.ORDER_ID = oi.ORDER_ID "
					+" LEFT JOIN ORDER_PAYMENT_PREFERENCE opp ON opp.ORDER_ID = oh.ORDER_ID "
					+" LEFT JOIN PERSON per ON per.PARTY_ID = oh.BUYER_ID "
					+" LEFT JOIN STATUS_ITEM si ON si.STATUS_ID = oh.STATUS_ID "
					+" GROUP BY oh.ORDER_ID ORDER BY oh.ORDER_DATE DESC ";
			ResultSet rs1 = processor.executeQuery(selectCountSql + sql + endSql + paymentSql + productSql);
			ResultSet rs2 = processor.executeQuery(selectAllSql + sql + endSql + paymentSql + productSql+  limitSql );
			Map<String, Object> resultMap = ServiceUtil.returnSuccess();
			
			
			Map<String, Object> pageMap = new HashMap<String, Object>();
			Integer totalNum = 0;
			while (rs1.next()) {
				totalNum = Integer.parseInt(rs1.getString("totalNum"));
			}
			//分页
			Integer endPage = 0;
			Integer resultCode = 1;
			Integer startPage = 0;
			Integer totalPage = 0;
			if(pageN == null || "".equals(pageN)){
				pageN = "1";
			}
			Integer pageNum = Integer.parseInt(pageN);
			
			if(totalNum <= 1){
				totalPage = 1;
			}else{
				totalPage = (totalNum - 1 ) / Integer.parseInt(pageSize) + 1;
			}
			if((int) pageNum <= 3){
				startPage = 1;
				if(totalPage >= 5){
					endPage = 5;
				}else{
					endPage = totalPage;
				}
			}else{
				startPage = pageNum - 2;
				if((totalPage - 2 ) < pageNum){
					endPage = totalPage;
				}else{
					endPage = pageNum + 2;
				}
			}
			pageMap.put("endPage", endPage);
			pageMap.put("pageNum", pageNum);
			pageMap.put("resultCode", resultCode);
			pageMap.put("startPage", startPage);
			pageMap.put("totalNum", totalNum);
			pageMap.put("totalPage", totalPage);
			
			resultMap.put("pageMap", pageMap);
			
			List<Object> orderList = new ArrayList<Object>();
			while (rs2.next()) {
				Map<String, Object> itemMap = new HashMap<String, Object>();
				SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy HH:mm:ss");
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String orderDate = rs2.getString("ORDER_DATE");
				Long orderTime = 0L;
				if(orderDate != null){
					orderTime = sdf.parse(orderDate).getTime();
				}
				itemMap.put("orderDate", dateFormat.format(new Date(orderTime)));
				
				
				
				itemMap.put("orderTime",orderTime);
				String itemOrderId = rs2.getString("ORDER_ID");
				itemMap.put("itemOrderId", itemOrderId);
				String buyerId = rs2.getString("BUYER_ID");
				itemMap.put("buyerId", buyerId);
				String customer = rs2.getString("FIRST_NAME_LOCAL");
				itemMap.put("customer", customer);
				itemMap.put("statusId", rs2.getString("STATUS_ID"));
				itemMap.put("statusDescription", rs2.getString("STATUS_DESCRIPTION"));
				itemMap.put("entryDate", rs2.getString("ENTRY_DATE"));
				itemMap.put("productStoreId", rs2.getString("PRODUCT_STORE_ID"));
				itemMap.put("storeName", productStoreMap.get(rs2.getString("PRODUCT_STORE_ID")));
				itemMap.put("paymentMethodTypeId", rs2.getString("PAYMENT_METHOD_TYPE_ID"));
				itemMap.put("grandTotal", rs2.getString("GRAND_TOTAL"));
				itemMap.put("amount", rs2.getString("AMOUNT"));
				if(rs2.getString("IS_FIX_ORDER") == null){
					itemMap.put("isFixOrder", "Y");
				}else{
					itemMap.put("isFixOrder", rs2.getString("IS_FIX_ORDER"));
				}
				
//				String ids = rs2.getString("PRODUCT_IDS");
//				String[] productIds;
//				if(ids != null || !"".equals(ids)){
//					productIds = ids.split(",");
//				}
				
				String findOrderProductSql = " SELECT oi.ORDER_ID, oi.QUANTITY, p.PRODUCT_ID,p.BRAND_NAME,p.INTERNAL_NAME,CONCAT(pf.DESCRIPTION) AS DESCRIPTION "
						+" FROM (SELECT * FROM ORDER_ITEM WHERE ORDER_ID = '" + itemOrderId + "') oi "
						+" LEFT JOIN PRODUCT p ON p.PRODUCT_ID = oi.PRODUCT_ID "
						+" LEFT JOIN PRODUCT_FEATURE_APPL pfa ON pfa.PRODUCT_ID = p.PRODUCT_ID "
						+" LEFT JOIN PRODUCT_FEATURE pf ON pfa.PRODUCT_FEATURE_ID = pf.PRODUCT_FEATURE_ID ";
				ResultSet rs3 = processor.executeQuery(findOrderProductSql);
				Map<String, Object>  productInfoMap = new HashMap<String, Object>();
				String productDesc = "";
				while (rs3.next()) {
					Map<String, String> productInfo = new HashMap<String, String>();
					rs3.getString("ORDER_ID");
					rs3.getString("QUANTITY");
					rs3.getString("PRODUCT_ID");
					rs3.getString("DESCRIPTION");
					if(itemOrderId.equals(rs3.getString("ORDER_ID"))){
						productInfo.put("orderId", rs3.getString("ORDER_ID"));
						productInfo.put("quantity", rs3.getString("QUANTITY"));
						productInfo.put("productId", rs3.getString("PRODUCT_ID"));
						productInfo.put("brandName", rs3.getString("BRAND_NAME"));
						productInfo.put("internalName", rs3.getString("INTERNAL_NAME"));
						productInfo.put("description", rs3.getString("DESCRIPTION"));
						productInfo.put("productInfo", rs3.getString("QUANTITY").substring(0, rs3.getString("QUANTITY").indexOf(".")) + "*" + rs3.getString("INTERNAL_NAME") + " " + rs3.getString("DESCRIPTION"));
						productDesc = productDesc + rs3.getString("QUANTITY").substring(0, rs3.getString("QUANTITY").indexOf(".")) + "*" + rs3.getString("INTERNAL_NAME") + " " + rs3.getString("DESCRIPTION") + " -- ";
						productInfoMap.put(rs3.getString("PRODUCT_ID"), productInfo);
					}
				}
				itemMap.put("productDesc", productDesc);
				itemMap.put("productInfoMap", productInfoMap);
				orderList.add(itemMap);
			}
			
			resultMap.put("orderList", orderList);
			
			return resultMap;
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("GenericDataSourceException");
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("GenericEntityException");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("SQLException");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("DateParseException");
		}
		
		
		
		
	}
}
