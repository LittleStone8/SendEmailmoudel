package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.product.enums.UsageTypeEnum;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.ProductEanModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.ProductEanVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;

import javolution.util.FastList;

public class ReceiveInventoryProduct {
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> receiveInventoryProduct(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> result = new HashMap<String, Object>();
        LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = dispacher.getDelegator();

		//JSONObject json = InventoryTransfer.makeParams(request);
        List<JSONObject> params = makeParamsToList(request);
        try {
	        if(params == null || params.size() <= 0){
	        	throw new ServiceException("Please fill in the parameter or login");
	        }
	        TransactionUtil.begin();
	        List<String> productList = new ArrayList<String>();
	        String isNew = (String) WarehouseCommon.getSystemSettings().get("SystemSettingsNewReceived");
	        //校验ean是否存在，不存在添加list返回
	        /*for(JSONObject json : params){
	        	String ean = (String) json.get("ean");
	        	String productId = (String) json.get("productId");
	        	if(ean!=null && !"".equals(ean)){
					GenericValue goodIdentification = delegator.findOne("GoodIdentification", UtilMisc.toMap("goodIdentificationTypeId", "EAN","productId", productId ), false);
					if(goodIdentification!=null){
						String eanIdValue = goodIdentification.getString("idValue");
						if(!eanIdValue.equals(ean)){
							productList.add(productId);
						}
					}else{
						productList.add(productId);
					}
				}
	        }
	        //返回错误ean的产品id
	        if(!productList.isEmpty()){
	        	result = ServiceUtil.returnError("Sorry EAN not exist please contact admin");
	        	result.put("errorProducts", productList);
	        	return result;
	        }*/

	        for(JSONObject json : params){
	        	String uomId = (String) json.get("uomId");
	        	String ean = (String) json.get("ean");
				String productId = (String) json.get("productId");
				String inventoryItemTypeId = (String) json.get("inventoryItemTypeId");
				String itemDescription = (String) json.get("itemDescription");
				String datetimeReceived = (String) json.get("datetimeReceived");
				String lotId = (String) json.get("lotId");
				String serialNumber = (String) json.get("serialNumber");
				String quantityAccepted = (String) json.get("quantityAccepted");
				String unitCost = (String) json.get("unitCost");
				String validateAccountingTags = "True";
				String quantityRejected = "0";
				GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
				String userLoginId = (String) userLogin.get("userLoginId");	
				String operater = userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")";
				String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);

				
				BigDecimal reciveQuantity = null;
				if(quantityAccepted!=null){
					reciveQuantity = new BigDecimal(quantityAccepted);
				}else{
					reciveQuantity = new BigDecimal(0);
				}
				Timestamp receivedDatetime = new Timestamp(Long.parseLong(datetimeReceived));
				
				
				if(productId == null || productId.equals("")){
					throw new ServiceException("Enter the product you want to receive");
				}

				if(unitCost != null && !unitCost.equals("")){
					String regexCost = "^(0|([1-9]\\d*))(\\.\\d+)?$";
					Pattern costPattern=Pattern.compile(regexCost);
					Matcher costMatcher=costPattern.matcher(unitCost);
					if(!costMatcher.matches()){
						throw new ServiceException("The input unitCost is illegal");
					}
				}

				if(lotId != null && !lotId.equals("")){						
					GenericValue lot = delegator.findByPrimaryKey("Lot", UtilMisc.toMap("lotId", lotId));
					if(lot == null){
						throw new ServiceException("The lotId you entered does not exist");
					}
				}

				GenericValue product = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
				if(product == null){
					throw new ServiceException("The product you entered doesn't exist");
				}

				List<EntityExpr> exprsProductId = FastList.newInstance();
				exprsProductId.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
				List<GenericValue> productAssoc = delegator.findByCondition("ProductAssoc", EntityCondition.makeCondition(exprsProductId), null, null);
				if(productAssoc != null && productAssoc.size() > 0){
					throw new ServiceException("The specified parameter does not exist or the parameter is illegal");
				}

//				if(ean!=null && !"".equals(ean)){
//					GenericValue goodIdentification = delegator.findOne("GoodIdentification", UtilMisc.toMap("goodIdentificationTypeId", "EAN","productId", productId ), true);
//					if(goodIdentification!=null){
//						String eanIdValue = goodIdentification.getString("idValue");
//						if(!eanIdValue.equals(ean)){
//							throw new ServiceException("Sorry EAN not exist please contact admin");
//						}
//					}else{
//						throw new ServiceException("Sorry EAN not exist please contact admin");
//					}
//				}
				
				exprsProductId.add(EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, "N"));
				List<GenericValue> products = delegator.findByCondition("Product", EntityCondition.makeCondition(exprsProductId), null, null);
				if(products != null && products.size() > 0){
					throw new ServiceException("This product is not available");
				}
				//insert EAN to product
				if(ean != null && !ean.equals("")){
					String proxyHost = (String) WarehouseCommon.getSystemSettings().get("proxyHost");
					//yuan da pao de jie kou
					String cookie =  request.getHeader("Cookie");
				    Map<String, String> header = new HashMap<String, String>();
					   			   header.put("Cookie", cookie);
				    
					if("Y".equals(isNew)){
						List<EntityExpr> exprs = FastList.newInstance();
						exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
						List<GenericValue> list = delegator.findByCondition("GoodIdentification", EntityCondition.makeCondition(exprs), null, null);
						ProductEanModel productEanModel = new ProductEanModel();
						ProductEanVo productEanVo = new ProductEanVo();
						productEanVo.setType("EAN");
						productEanVo.setProduct(productId);
						productEanVo.setValue(ean);
						productEanModel.setType("EAN");
						ArrayList<ProductEanVo> productEanVos = new ArrayList<ProductEanVo>();
						productEanVos.add(productEanVo);
						productEanModel.setProductList(productEanVos);
						String jsons = JSON.toJSONString(productEanModel);
						if(list == null || list.size() == 0){						
							String resultStr =  post(proxyHost+"/api/product/v1/product/updategoodIdentificationInfo", header,jsons);
							JSONObject resultJson = JSON.parseObject(resultStr);
							boolean resultFlag = resultJson.getBoolean("success");
							if(!resultFlag){
								throw new ServiceException("Insert or update the product EAN exception, please contact the administrator");
							}
						}else if(!ean.equals(list.get(0).getString("idValue"))){
							String resultStr =  post(proxyHost+"/api/product/v1/product/updategoodIdentificationInfo", header,jsons);
							JSONObject resultJson = JSON.parseObject(resultStr);
							boolean resultFlag = resultJson.getBoolean("success");
							if(!resultFlag){
								throw new ServiceException("Insert or update the product EAN exception, please contact the administrator");
							}
						}
					}
				}
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("productId", productId);
				param.put("inventoryItemTypeId", inventoryItemTypeId);
				param.put("itemDescription", itemDescription);
				if(datetimeReceived == null || datetimeReceived.equals("")){
					param.put("datetimeReceived", new Timestamp(new Date().getTime()));
				}else{
					param.put("datetimeReceived", new Timestamp(Long.parseLong(datetimeReceived)));
				}
				param.put("lotId", lotId);
				param.put("serialNumber", serialNumber);
				param.put("quantityAccepted", quantityAccepted);
				if(unitCost == null || unitCost.equals("")){
					List<EntityExpr> exprs = FastList.newInstance();
					exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
					exprs.add(EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, "DEFAULT_COST_PRICE"));		
					List<GenericValue> productPrice = delegator.findByCondition("ProductPrice", EntityCondition.makeCondition(exprs), null, null);
					if(productPrice != null && productPrice.size() > 0){
						unitCost = productPrice.get(0).getString("price");
					}else{
						unitCost = "0";
					}
				}
				param.put("unitCost", unitCost);
				param.put("facilityId", facilityId);
				param.put("validateAccountingTags", validateAccountingTags);
				param.put("userLogin", userLogin);
				param.put("quantityRejected", quantityRejected);

				if(inventoryItemTypeId.equals("SERIALIZED_INV_ITEM")){

					if(serialNumber.equals("")){
						throw new ServiceException("Please enter your serial number");
					}

					String[] serialNumbers = serialNumber.split(",");
					Set<String> set = new HashSet<String>();
					for(String str : serialNumbers){
					   set.add(str);
					}
					if(set.size() != serialNumbers.length){
						throw new ServiceException("IMEI can not repeat");
					}
					//
					/*List<EntityExpr> exprs = FastList.newInstance();
					
					exprs.add(EntityCondition.makeCondition("inventoryItemTypeId", EntityOperator.EQUALS, "SERIALIZED_INV_ITEM"));		
					exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_AVAILABLE"));		
					List<GenericValue> inventoryItems = delegator.findByCondition("InventoryItem", EntityCondition.makeCondition(exprs), null, null);
					*/
					GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
					SQLProcessor processor = new SQLProcessor(helperInfo);
					List<String> exisIMEIs = new ArrayList<String>();
					String sql = "SELECT SERIAL_NUMBER as imei FROM ("
							+ " select SERIAL_NUMBER from INVENTORY_ITEM where INVENTORY_ITEM_TYPE_ID='SERIALIZED_INV_ITEM' and IFNULL(STATUS_ID,'') != 'INV_DEACTIVATED' and IFNULL(STATUS_ID,'') != 'IXF_PICKING' and IFNULL(STATUS_ID,'') != 'IXF_CANCELLED' ORDER BY CONVERT(INVENTORY_ITEM_ID ,DECIMAL)   desc)"
							+ " A GROUP BY A.SERIAL_NUMBER";
					ResultSet rs = processor.executeQuery(sql);
					while(rs.next()){
						exisIMEIs.add(rs.getString("imei"));
					}
					rs.close();
					processor.close();
					for(String IMEI : serialNumbers){
						if(exisIMEIs.size()>0){
							for(String exisIMEI:exisIMEIs){
								if(IMEI.equals(exisIMEI)){
									throw new ServiceException("Sorry.IMEI ["+IMEI+"] is repetitive. Please contact admin in the WhatsApp group named 'New System'");
								}
							}
						}
						
						//
						param.put("serialNumber", IMEI);
						param.put("quantityAccepted", "1");
						reciveQuantity = new BigDecimal(1);
//						dispacher.runSync("receiveInventoryProduct", param);
						
						
						String inventoryItemId = WarehouseCommon.insertInventoryItem(delegator, inventoryItemTypeId, productId, "AMAZON", 
								receivedDatetime, "INV_AVAILABLE", reciveQuantity, reciveQuantity, facilityId, lotId, IMEI, unitCost, uomId, null);
						WarehouseCommon.insertInventoryItemDetail(delegator, receivedDatetime, inventoryItemId, reciveQuantity, reciveQuantity, 
								reciveQuantity, reciveQuantity, UsageTypeEnum.InventoryReceive.getName(), operater, null, null, null,null,null);
						WarehouseCommon.insertShipmentReceipt(delegator, inventoryItemId, productId, userLoginId, receivedDatetime, itemDescription, reciveQuantity);
						WarehouseCommon.insertInventoryItemValueHistory(delegator, inventoryItemId, receivedDatetime, unitCost, userLoginId);
					}				
				}else{
					//
					String regex = "^[0-9]*[1-9][0-9]*$";
					Pattern p=Pattern.compile(regex);
					Matcher m=p.matcher(quantityAccepted);
					//
					if(!m.matches()){
						throw new ServiceException("The number of recipients must be integers");
					}
					
					param.put("qoh", quantityAccepted);
					param.put("atp", quantityAccepted);
					param.put("operator", operater);
					param.put("usageType", UsageTypeEnum.InventoryReceive.getName());
//					dispacher.runSync("receiveInventoryProduct", param);
					if(lotId.equals("")){
						lotId=null;
					}
					String inventoryItemId = WarehouseCommon.insertInventoryItem(delegator, inventoryItemTypeId, productId, "AMAZON", 
							receivedDatetime, null, reciveQuantity, reciveQuantity, facilityId, lotId, null, unitCost, uomId, null);
					WarehouseCommon.insertInventoryItemDetail(delegator, receivedDatetime, inventoryItemId, reciveQuantity, reciveQuantity, 
							reciveQuantity, reciveQuantity, UsageTypeEnum.InventoryReceive.getName(), operater, null, null, null , null,null);
					WarehouseCommon.insertShipmentReceipt(delegator, inventoryItemId, productId, userLoginId, receivedDatetime, itemDescription, reciveQuantity);
					WarehouseCommon.insertInventoryItemValueHistory(delegator, inventoryItemId, receivedDatetime, unitCost, userLoginId);
				
					
				}
	        }
	        result.put("resultCode", 1);
			result.put("resultMsg", "operate successfully");
			TransactionUtil.commit();
		} catch(ServiceException se){
			result.put("resultCode", -1);
			result.put("resultMsg", se.getMessage());
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			se.printStackTrace();
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}	
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> productInquiry(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		
		JSONObject json = InventoryTransfer.makeParams(request);

		String productId = (String) json.get("productId");
		String productIdNum = (String) json.get("productIdNum");
		String productIdStatu = (String) json.get("productIdStatu");
		
		String brandName = (String) json.get("brandName");
		String brandNameNum = (String) json.get("brandNameNum");
		String brandNameStatu = (String) json.get("brandNameStatu");
		
		String internalName = (String) json.get("internalName");
		String internalNameNum = (String) json.get("internalNameNum");
		String internalNameStatu = (String) json.get("internalNameStatu");
		
		String productTypeId = (String) json.get("productTypeId");
		if(productTypeId == null){
			productTypeId = "FINISHED_GOOD";
		}
		String pageNum = (String) json.get("pageNum");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		String pageSize = (String) json.get("pageSize");
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}

		StringBuffer sql = new StringBuffer("SELECT x.PRODUCT_ID as productId ,x.INTERNAL_NAME as internalName,x.BRAND_NAME as brandName,x.PRODUCT_TYPE_ID as productTypeId, y.productAttributes FROM("
				+ " SELECT b.PRODUCT_ID,b.INTERNAL_NAME,b.BRAND_NAME,b.PRODUCT_TYPE_ID FROM("
//				+ " SELECT PRODUCT_ID,INTERNAL_NAME,BRAND_NAME,PRODUCT_TYPE_ID FROM PRODUCT a WHERE a.PRODUCT_TYPE_ID = '"+productTypeId+"'");
+ " SELECT PRODUCT_ID,INTERNAL_NAME,BRAND_NAME,PRODUCT_TYPE_ID FROM PRODUCT a WHERE  (a.IS_ACTIVE = 'Y' OR a.IS_ACTIVE IS NULL) AND a.PRODUCT_TYPE_ID = '"+productTypeId+"'");
		
		InventoryTransfer.makeCondition(sql, "a.PRODUCT_ID", productIdNum, productId, productIdStatu);
		InventoryTransfer.makeCondition(sql, "a.INTERNAL_NAME", internalNameNum, internalName, internalNameStatu);
		InventoryTransfer.makeCondition(sql, "a.BRAND_NAME", brandNameNum, brandName, brandNameStatu);	
		
		sql.append(" ) b  WHERE NOT EXISTS ( SELECT * FROM PRODUCT_ASSOC c WHERE c.PRODUCT_ID = b.PRODUCT_ID) ORDER BY b.PRODUCT_ID) x"
				+ " LEFT JOIN ( SELECT b.product_id,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a"
				+ " LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID"
				+ " GROUP BY product_id) y ON x.PRODUCT_ID = y.PRODUCT_ID ");
		try {

			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), ProductVo.class, delegator);
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
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryProductType(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		//鑾峰彇JDBC杩炴帴
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<String> productTypeId = new ArrayList<String>();
		String sql = "SELECT PRODUCT_TYPE_ID FROM PRODUCT_TYPE";
		ResultSet rs = null;
		try {
			rs = processor.executeQuery(sql);
			while(rs.next()){
				productTypeId.add(rs.getString("PRODUCT_TYPE_ID"));
			}
			result.put("result", productTypeId);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		} finally{
			try{
				if(rs != null){
					rs.close();
				}
				processor.close();
			}catch(Exception e){
				
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static List<JSONObject> makeParamsToList(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
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
		List<JSONObject> list = new ArrayList<JSONObject>();
		if(userLogin == null){
//			throw new ServiceException("please login ");
			return list;
		}
		String[] params = jsonStringBuffer.substring(1,jsonStringBuffer.length()-1).replace("},", "}~@~").split("~@~");
		for(String param : params){
		list.add(JSON.parseObject(param));
		}
		return list;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String verifPermissions(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = getGroupId(request, response);
		String permission = (String) map.get("result");
		if(permission != null && (permission.contains("WRHS_ADMIN") || permission.contains("WRHS_INV_RAI"))){
			return "success";
		}
        return "error";
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> getGroupId(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		String userLoginId = userLogin.getString("userLoginId");
		try {			
			List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);
			StringBuffer resultStr = new StringBuffer("");
			for(GenericValue userSecurityGroup : userSecurityGroups){
				resultStr.append(userSecurityGroup.getString("groupId")+" - ");
			}
			result.put("result",resultStr.toString());
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据EAN获取productId
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> findProductByEAN(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		ResultSet rs = null;
		JSONObject json = InventoryTransfer.makeParams(request);
		String EAN = json.getString("EAN");
		ProductVo productVo = null;
		String isNew = (String) WarehouseCommon.getSystemSettings().get("SystemSettingsNewReceived");
		result.put("isNew",isNew);
		try {			
			String sql = "SELECT Y.PRODUCT_ID AS productId,Y.BRAND_NAME AS brandName,Y.INTERNAL_NAME as model,Z.description FROM ("
					+ " SELECT PRODUCT_ID,ID_VALUE FROM GOOD_IDENTIFICATION WHERE ID_VALUE='"+EAN+"')X LEFT JOIN PRODUCT Y ON X.PRODUCT_ID=Y.PRODUCT_ID LEFT JOIN ( SELECT b.PRODUCT_ID, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID)"
					+ "  AS description FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) Z ON X.PRODUCT_ID = Z.PRODUCT_ID";
			rs = processor.executeQuery(sql);
			while(rs.next()){
				productVo = new ProductVo();
				productVo.setProductId(rs.getString("productId"));
				productVo.setBrandName(rs.getString("brandName"));
				productVo.setInternalName(rs.getString("model"));
				productVo.setProductAttributes(rs.getString("description"));
				result.put("result",productVo);
				result.put("flag", true);
			}
			if(productVo == null){
				result.put("flag", false);
			}		
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		} finally{
			try{
				if(rs != null){
					rs.close();
				}
				processor.close();
			}catch(Exception e){
				
			}
		}
		return result;
	}
	
	/**
	 * 根据productId获取EAN
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> findEANByProductId(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String productId = json.getString("productId");
		try {			
			List<EntityExpr> exprs = FastList.newInstance();
			exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
			List<GenericValue> list = delegator.findByCondition("GoodIdentification", EntityCondition.makeCondition(exprs), null, null);
			if(list == null || list.size() == 0){
				result.put("result","");
			}else{
				GenericValue goodIdentification = list.get(0);
				if(goodIdentification.getString("idValue") == null){
					result.put("result","");
				}else{
					result.put("result",goodIdentification.getString("idValue"));
				}
				
			}			
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	public static String post(String  url, Map<String, String> header,String postString) {
		PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.setRequestProperty("Cookie", header.get("Cookie"));
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(postString);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        System.out.println(result);
	        return result;
    }
	
}
