package org.ofbiz.product.product;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.product.bean.FindAllproductPriceBean;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;







public class CreateProductPrice {

	public static final String resource = "ProductErrorUiLabels";

	public static Map<String, Object> newCreateProductPrice(LocalDispatcher dispatcher, GenericValue userLogin,String productId,String productPriceTypeId,String productPricePurposeId,
			String currencyUomId,String productStoreGroupId,BigDecimal price,String termUomId,String customPriceCalcService) {
		

		
		Timestamp fromDate = new Timestamp(new Date().getTime());
		try {
			TransactionUtil.begin();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("price", price);
			reqMap.put("productId", productId);
			reqMap.put("productPriceTypeId", productPriceTypeId);
			reqMap.put("productPricePurposeId", productPricePurposeId);
			reqMap.put("currencyUomId", currencyUomId);
			reqMap.put("productStoreGroupId", productStoreGroupId);
			reqMap.put("termUomId", termUomId);
			reqMap.put("customPriceCalcService", customPriceCalcService);
			reqMap.put("fromDate", fromDate);
			 if (userLogin != null ) {
				 reqMap.put("userLogin", userLogin);
		        }
			dispatcher.runSync("createProductPrice", reqMap);
			TransactionUtil.commit();
			return ServiceUtil.returnSuccess("create success");
		} catch (Exception e) {
			try {
                TransactionUtil.rollback();
	         } catch (GenericTransactionException e2) {
	        	 e2.printStackTrace();
	        	 return ServiceUtil.returnError("create failed");
	         }
			e.printStackTrace();
			return ServiceUtil.returnError("create failed");
		}
		
    }

	
	
	
	
	
	
	
	
	
	
	public static Map<String, Object> newfindAllProductPrice(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String productId = request.getParameter("productId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<FindAllproductPriceBean> resultPoductPriceList = new ArrayList<FindAllproductPriceBean>();
			List<GenericValue> productPriceList = delegator.findByAnd("NewProductAndPriceView",UtilMisc.toMap("productId", productId));
			for (GenericValue genericValue : productPriceList) {
				FindAllproductPriceBean productPrice = new FindAllproductPriceBean();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String str = genericValue.getString("fromDate");
				Date date = sdf.parse(str);
				long ts = date.getTime();
				String fromDate = String.valueOf(ts);
				productPrice.setFromDate(fromDate);
				String thruDate = genericValue.getString("thruDate");
				if(!(thruDate ==""||"".equals(thruDate)||thruDate==null)){
					str = thruDate;
					date = sdf.parse(str);
					ts = date.getTime();
					thruDate = String.valueOf(ts);
				}
				productPrice.setThruDate(thruDate);
				productPrice.setProductId(genericValue.getString("productId"));
				productPrice.setCurrencyUomId(genericValue.getString("currencyUomId"));
				productPrice.setCustomPriceCalcService(genericValue.getString("customPriceCalcService"));
				productPrice.setPrice(genericValue.getString("price"));
				productPrice.setProductPriceTypeId(genericValue.getString("productPriceTypeId"));
				productPrice.setProductPricePurposeId(genericValue.getString("productPricePurposeId"));
				productPrice.setProductStoreGroupID(genericValue.getString("productStoreGroupId"));
				resultPoductPriceList.add(productPrice);
			}
			map.put("resultPoductPriceList", resultPoductPriceList);
//			request.setAttribute("resultPoductPriceList", resultPoductPriceList);
			map.put("responseMessage", "success");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create failed");
		}
		
    }
	
	
	
	
	
	
	
//	public static String newUpdateProductPrice(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			HttpSession session = request.getSession();
//			String productId = request.getParameter("productId");
//			String productPriceTypeId = request.getParameter("productPriceTypeId");
//			String productPricePurposeId = request.getParameter("productPricePurposeId");
//			String currencyUomId = request.getParameter("currencyUomId");
//			String productStoreGroupId = request.getParameter("productStoreGroupId");
//			BigDecimal price = new BigDecimal(request.getParameter("price"));
//			String termUomId = request.getParameter("termUomId");
//			String customPriceCalcService = request.getParameter("customPriceCalcService");
//			Timestamp fromDate = new Timestamp(Long.valueOf(request.getParameter("fromDate")));
//			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//			Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
//			reqMap.put("price", price);
//			reqMap.put("productId", productId);
//			reqMap.put("productPriceTypeId", productPriceTypeId);
//			reqMap.put("productPricePurposeId", productPricePurposeId);
//			reqMap.put("currencyUomId", currencyUomId);
//			reqMap.put("productStoreGroupId", productStoreGroupId);
//			reqMap.put("termUomId", termUomId);
//			reqMap.put("customPriceCalcService", customPriceCalcService);
//			reqMap.put("fromDate", fromDate);
//			if (userLogin != null) {
//				 reqMap.put("userLogin", userLogin);
//		        }
//			dispatcher.runAsync("updateProductPrice", reqMap);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//    }
//	
	
	
	
	
	
	
	
	
	
	public static Map<String, Object> newDeleteProductPrice(HttpServletRequest request, HttpServletResponse response) {
		try {
			//判断权限
			String updateMode = "DELETE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to delete price");
	        }
			
			HttpSession session = request.getSession();
			String productId = request.getParameter("productId");
			String productPriceTypeId = request.getParameter("productPriceTypeId");
			String productPricePurposeId = request.getParameter("productPricePurposeId");
			String currencyUomId = request.getParameter("currencyUomId");
			String productStoreGroupId = request.getParameter("productStoreGroupId");
			Timestamp fromDate = new Timestamp(Long.valueOf(request.getParameter("fromDate")));
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
			reqMap.put("productId", productId);
			reqMap.put("productPriceTypeId", productPriceTypeId);
			reqMap.put("productPricePurposeId", productPricePurposeId);
			reqMap.put("currencyUomId", currencyUomId);
			reqMap.put("productStoreGroupId", productStoreGroupId);
			reqMap.put("fromDate", fromDate);
			if (userLogin != null) {
				 reqMap.put("userLogin", userLogin);
		        }
			dispatcher.runAsync("deleteProductPrice", reqMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("delete failed");
		}
		return ServiceUtil.returnSuccess("delete success");
    }
	
	
	/**
	 * 同时跟新多个sku的单价。
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> oneKeyUpdateAllProductPrice(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to edit price");
	        }
			
			TransactionUtil.begin();
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			HttpSession session = request.getSession();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			
			StringBuffer jsonStringBuffer = new StringBuffer();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
			String productPriceList =  jsonStringBuffer.toString();
			JSONObject json1 = JSON.parseObject(productPriceList);
			JSONArray json = json1.getJSONArray("productPriceList");
			flag = false;
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
//			List<GenericValue> storePirceChange = new ArrayList<GenericValue>();
			for (int i = 0; i < json.size(); i++) {
				JSONObject jsonObj = json.getJSONObject(i);					
//				LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
				Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
				boolean isDigits = NumberUtils.isDigits(jsonObj.getString("price"));     //判断str是否整数， true-整数  false-非整数
				boolean isNumber = NumberUtils.isNumber(jsonObj.getString("price"));  //判断str是否数字（整数、小数、科学计数法等等格式）
				if(jsonObj.getString("price") == null || jsonObj.getString("price").equals("") ){
					continue;
				}else if(!(isDigits || isNumber)){
					return ServiceUtil.returnError("Please write the correct price");
				}else{
					flag = true;
				}
				BigDecimal price = new BigDecimal(jsonObj.getString("price"));
				
				if(price.doubleValue() >= 0){
					reqMap.put("price", price);
					String productId = jsonObj.getString("productId");
//					List<GenericValue> productList =  delegator.findByAnd("Product", UtilMisc.toMap("productId",productId));
//					GenericValue product = productList.get(0);
//					if("N".equals(product.getString("isActive"))){
//						return ServiceUtil.returnError("can not use the product");
//					}
					reqMap.put("productId", productId);
					String productPriceTypeId = jsonObj.getString("productPriceTypeId");
					
					reqMap.put("productPriceTypeId",productPriceTypeId);
					String productPricePurposeId = jsonObj.getString("productPricePurposeId");
					reqMap.put("productPricePurposeId", productPricePurposeId);
					String currencyUomId = jsonObj.getString("currencyUomId");
					List<GenericValue> uomList = delegator.findByAndCache("Uom", UtilMisc.toMap("uomId", currencyUomId));
					if(uomList.isEmpty()){
						return ServiceUtil.returnError("have no " + currencyUomId);
					}
					reqMap.put("currencyUomId", currencyUomId);
					String productStoreGroupId = jsonObj.getString("productStoreGroupId");
					reqMap.put("productStoreGroupId", productStoreGroupId);
					
					long time = Long.parseLong(jsonObj.getString("fromDate"));
					Timestamp fromDate = new Timestamp(time);
					
//					Timestamp fromDate = new Timestamp(Long.parseLong(jsonObj.getString("fromDate")));
					reqMap.put("fromDate", fromDate);
//					if (userLogin != null) {
//						 reqMap.put("userLogin", userLogin);
//			        }
					 
//					dispatcher.runAsync("updateProductPrice", reqMap);
//			        <entity-one entity-name="ProductPrice" value-field="lookedUpValue"/>
//			        <!-- grab the old price value before setting nonpk parameter fields -->
//			        <field-to-result field="lookedUpValue.price" result-name="oldPrice"/>
//			        <set-nonpk-fields map="parameters" value-field="lookedUpValue"/>
//			        <now-timestamp field="nowTimestamp"/>
//			        <set field="lookedUpValue.lastModifiedDate" from-field="nowTimestamp"/>
//			        <set field="lookedUpValue.lastModifiedByUserLogin" from-field="userLogin.userLoginId"/>

//					 GenericValue productPrice = delegator.findOne("ProductPrice", reqMap, false);
					
					 List<GenericValue> productPricelist =  delegator.findByAnd("ProductPrice", UtilMisc.toMap("productId",productId,"currencyUomId",currencyUomId,"productStoreGroupId",productStoreGroupId));
					 if(productPricelist.size() > 0){
						 /*
						  * <prim-key field="productId"/>
						      <prim-key field="productPriceTypeId"/>
						      <prim-key field="productPricePurposeId"/>
						      <prim-key field="currencyUomId"/>
						      <prim-key field="productStoreGroupId"/>
						      <prim-key field="fromDate"/>
						  */
						 GenericValue unuselyproductPrice = EntityUtil.getFirst(productPricelist);
						 delegator.findByAnd("ProductPrice", UtilMisc.toMap("productId",productId,"currencyUomId",currencyUomId,"productStoreGroupId",productStoreGroupId));
						 GenericValue productPrice = delegator.findOne("ProductPrice", UtilMisc.toMap(
								 "productId",unuselyproductPrice.getString("productId"),
								 "productPriceTypeId",unuselyproductPrice.getString("productPriceTypeId"),
								 "productPricePurposeId",unuselyproductPrice.getString("productPricePurposeId"),
								 "currencyUomId",unuselyproductPrice.getString("currencyUomId"),
								 "productStoreGroupId",unuselyproductPrice.getString("productStoreGroupId"),
								 "fromDate",unuselyproductPrice.getString("fromDate")), false);
						 String oldprice = productPrice.getString("price");
						 
						 
//						 delegator.findByAnd("ProductPriceChange", UtilMisc.toMap("productId",productId));
						 String productPriceChangeId = delegator.getNextSeqId("ProductPriceChange");
						 GenericValue productPriceChange = delegator.makeValue("ProductPriceChange");
						 productPriceChange.setFields(reqMap);
						 productPriceChange.set("productPriceChangeId", productPriceChangeId);
						 productPriceChange.set("oldPrice", oldprice);
						 productPriceChange.set("changedDate", new Date());
						 productPriceChange.set("changedByUserLogin", userLogin.get("userLoginId"));
						 toBeStored.add(productPriceChange);
						 
						 String customPriceCalcService = jsonObj.getString("customPriceCalcService");
						 reqMap.put("customPriceCalcService", customPriceCalcService);
						 String termUomId = jsonObj.getString("termUomId");
						 reqMap.put("termUomId", termUomId);
//						 productPrice.setFields(reqMap);
						 
						 productPrice.set("price", price);
						 productPrice.set("lastModifiedDate", new Date());
						 productPrice.set("lastModifiedByUserLogin", userLogin.get("userLoginId"));
//						 productPrice.store();
						 toBeStored.add(productPrice);
					 }else{
						 
//						 productPrice = delegator.makeValue("ProductPrice");
//						 String userLoginId = (String) userLogin.get("userLoginId");
						 String customPriceCalcService = jsonObj.getString("customPriceCalcService");
						 reqMap.put("customPriceCalcService", customPriceCalcService);
						 String termUomId = jsonObj.getString("termUomId");
						 reqMap.put("termUomId", termUomId);
						 if(currencyUomId != null){
							 newCreateProductPrice(dispatcher, userLogin, productId, productPriceTypeId, productPricePurposeId, currencyUomId, productStoreGroupId, price, termUomId, customPriceCalcService);
						 }
					 }
				}else{ 
					return ServiceUtil.returnError("The price cannot be less than 0 ");
				}
				
			}
			if(flag){
				delegator.storeAll(toBeStored);
				TransactionUtil.commit();
				return ServiceUtil.returnSuccess("Modify success ");
			}else{
				return ServiceUtil.returnError("price cannot be null ");
			}
			
		} catch (Exception e) {
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
				return ServiceUtil.returnError("fail");
			}
			e.printStackTrace();
			return ServiceUtil.returnError("create fail");

		}
		
    }
	
	
	
//	public static void updateProductPrice(LocalDispatcher dispatcher,Map<String, Object> reqMap){
//		try {
//			TransactionUtil.begin();
//			dispatcher.runAsync("updateProductPrice", reqMap);
//			TransactionUtil.commit();
//		} catch (Exception e) {
//			try {
//				TransactionUtil.rollback();
//			} catch (GenericTransactionException e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 选中全选是一次查询所有的产品的价格
	 * 
	 * chenshihua
	 * 2017-4-13
	 */
	
	public static Map<String, Object> findAllcheckedProductPrice(HttpServletRequest request, HttpServletResponse response){
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			StringBuffer jsonStringBuffer = new StringBuffer();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
			String productPriceList =  jsonStringBuffer.toString();
			JSONObject json = JSON.parseObject(productPriceList);
			
			String productList = json.getString("productList");
			JSONArray productJsonList = json.parseArray(productList);
//			List<Object> productIdList = newfindAllProductPrice(productJsonList , delegator);
			Map<String, Object> productIdMap = newfindAllProductPrice(productJsonList , delegator);
			if(productIdMap.size() > 0){
				Map<String, Object> returnSuccess = ServiceUtil.returnSuccess();
				returnSuccess.put("productIdList", productIdMap);
				return returnSuccess;
			}else{
				return ServiceUtil.returnError("The product is not queried to the price ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fail");
		}
	}
	
	
	
	
	
	
	
	/*
	 * 查询所有的商品的价格
	 * 
	 * 
	 */
	
	
	public static Map<String, Object> newfindAllProductPrice(JSONArray productJsonList , Delegator delegator) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (Object object : productJsonList) {
				String productId = (String) object;
				List<Object> resultPoductPriceList = new ArrayList<Object>();
				List<GenericValue> productPriceList = delegator.findByAnd("NewProductAndPriceView",UtilMisc.toMap("productId", productId));
				for (GenericValue genericValue : productPriceList) {
					FindAllproductPriceBean productPrice = new FindAllproductPriceBean();
					productPrice.setProductId(genericValue.getString("productId"));
					productPrice.setCurrencyUomId(genericValue.getString("currencyUomId"));
					productPrice.setCustomPriceCalcService(genericValue.getString("customPriceCalcService"));
					productPrice.setPrice(genericValue.getString("price"));
					productPrice.setFromDate(genericValue.getString("fromDate"));
					productPrice.setProductPriceTypeId(genericValue.getString("productPriceTypeId"));
					productPrice.setProductPricePurposeId(genericValue.getString("productPricePurposeId"));
					productPrice.setProductStoreGroupID(genericValue.getString("productStoreGroupId"));
					resultPoductPriceList.add(productPrice);
				}
				map.put(productId, resultPoductPriceList);
			}
			return map;
//			return resultPoductPriceList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
    }
	
	
	
	
	
	
	
	
	
	
	public static Map<String, Object> findPriceUOM(HttpServletRequest request, HttpServletResponse response) {
		try {
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> context = new HashMap<String, Object>();
			Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
			String uomId = (String) uomInfo.get("systemsettinguomid");
			String isShip = (String) uomInfo.get("isShip");
			String countryId = (String) uomInfo.get("countryId");
			
			String egateeCost = (String) uomInfo.get("egateeCost");
			String specialCost = (String) uomInfo.get("specialCost");
			String retailCost = (String) uomInfo.get("retailCost");
			
			if(uomInfo != null){
				
				Delegator delegator = (Delegator) request.getAttribute("delegator");
				GenericValue uom = delegator.findOne("Uom", true, UtilMisc.toMap("uomId", uomId));
				if(uom != null){
					Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
					Map<String, Object> uomMap = new HashMap<String, Object>();
					uomMap.put("uomId", uom.getString("uomId"));
					uomMap.put("description", uom.getString("description"));
					uomMap.put("abbreviation", uom.getString("abbreviation"));
					//是否需要运送
					uomMap.put("isShip", isShip);
					
					
					GenericValue geo = delegator.findOne("Geo", true, UtilMisc.toMap("geoId", countryId));
					String countryName = geo.getString("geoName");
					uomMap.put("countryId", countryId);
					uomMap.put("countryName", countryName);
					uomMap.put("egateeCost", Double.parseDouble(egateeCost));
					uomMap.put("specialCost", Double.parseDouble(specialCost));
					uomMap.put("retailCost", Double.parseDouble(retailCost));
					resultMap.put("uomMap", uomMap);
					return resultMap;
				}
			}
			return ServiceUtil.returnError("query erro");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query erro");
		}
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean hasEntityPermission(HttpServletRequest request ,String updateMode ){
		String errMsg=null;
		Security security = (Security) request.getAttribute("security");
		if (!security.hasEntityPermission("CATALOG", "_" + updateMode, request.getSession())) {
            Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
            errMsg = UtilProperties.getMessage(resource,"productevents.not_sufficient_permissions", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return false;
        }else{
        	return true;
        }
	}
	
	
	
	
}
