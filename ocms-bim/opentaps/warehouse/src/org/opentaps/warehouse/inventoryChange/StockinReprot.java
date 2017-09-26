package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.StockinReportVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.TranferInventoryVo;

import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.math.BigDecimal;

import javolution.util.FastList;

public class StockinReprot {
	
	/**
	 * fuzzy query product
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryProductByCondition(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		
		String params = json.getString("params");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		StringBuffer sql = new StringBuffer("SELECT X.PRODUCT_ID as productId,X.BRAND_NAME as brandName,X.INTERNAL_NAME as internalName,X.PRODUCT_TYPE_ID AS productTypeId,Y.description as productAttributes FROM(SELECT PRODUCT_ID,BRAND_NAME,INTERNAL_NAME,PRODUCT_TYPE_ID FROM PRODUCT WHERE "
				+ " (IS_ACTIVE='Y' OR IS_ACTIVE IS NULL) AND IS_VARIANT='N' AND ( PRODUCT_ID LIKE '%"+params+"%' OR BRAND_NAME LIKE '%"+params+"%' OR INTERNAL_NAME LIKE '%"+params+"%')) X"
				+ " LEFT JOIN(SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as description FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id)Y ON X.PRODUCT_ID=Y.PRODUCT_ID");
		try{
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), ProductVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");	
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<String,Object> queryProductByConditionbymodel(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		
		String params = json.getString("params");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		StringBuffer sql = new StringBuffer("SELECT * FROM  (SELECT X.PRODUCT_ID as productId,X.BRAND_NAME as brandName,X.INTERNAL_NAME as internalName,X.PRODUCT_TYPE_ID AS productTypeId,Y.description as productAttributes FROM(SELECT PRODUCT_ID,BRAND_NAME,INTERNAL_NAME,PRODUCT_TYPE_ID FROM PRODUCT WHERE "
				+ "  INTERNAL_NAME LIKE '%"+params+"%') X"
				+ " LEFT JOIN(SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as description FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id)Y ON X.PRODUCT_ID=Y.PRODUCT_ID ) AS WW GROUP BY WW.internalName  ");
		try{
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), ProductVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");	
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * Data query statements
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryReportData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String pageNum = "";
		String pageSize = "";
		String productId = "";
		String facilityId = "";
		String startDate = "";
		String endDate = "";
		if(request.getRequestURI().contains("queryReportData") || request.getRequestURI().contains("queryOperatorsReportData")){
			JSONObject json = InventoryTransfer.makeParams(request);
			productId = json.getString("productId");
			facilityId = json.getString("facilityId");
			startDate = json.getString("startDate");
			endDate = json.getString("endDate");
			pageNum = json.getString("pageNum");
			pageSize = json.getString("pageSize");
			if(pageNum == null || pageNum.equals("")){
				pageNum = "1";
			}
			if(pageSize == null || pageSize.equals("")){
				pageSize = "10";
			}
		}else{
			Map map = UtilHttp.getParameterMap(request);
			facilityId = (String) map.get("facilityId");
			productId = (String) map.get("productId");
			startDate = (String) map.get("startDate");
			endDate = (String) map.get("endDate");
			pageNum = "0";
			pageSize = (String) map.get("pageSize");			
			if(pageSize == null || pageSize.equals("")){
				pageSize = "10";
			}
		}
		try{
			int timeDiff = WarehouseCommon.getTimeDifference(request);
			StringBuffer sql = new StringBuffer("SELECT X.PRODUCT_ID as productId,T.BRAND_NAME as brandName,T.INTERNAL_NAME as model, G.DESRIPTION as description,X.FACILITY_ID as facilityId,H.FACILITY_NAME as facilityName,X.RECEIVE_TIME as receiveTime, "
				+ " SUM(IFNULL(QUANTITY_ACCEPTED, 0)) AS receiveQuantity, SUM(IFNULL(TRANSFER_NUMBER, 0)) AS transferQuantity,"
				+ " (SUM(X.UNIT_COST*IFNULL(Y.QUANTITY_ACCEPTED, 0)+X.UNIT_COST*IFNULL(Z.TRANSFER_NUMBER, 0))/SUM(IFNULL(QUANTITY_ACCEPTED, 0)+IFNULL(TRANSFER_NUMBER, 0))) AS unitCost"
				+ " FROM(SELECT INVENTORY_ITEM_ID,PRODUCT_ID,FACILITY_ID,UNIT_COST,DATE_FORMAT(DATE_SUB(CREATED_STAMP,INTERVAL '"+timeDiff+"' HOUR),'%Y-%m-%d') AS RECEIVE_TIME"
				+ " FROM INVENTORY_ITEM WHERE DATE_FORMAT(DATE_SUB(CREATED_STAMP,INTERVAL '"+timeDiff+"' HOUR),'%Y-%m-%d') >= '"+startDate+"'"
				+ " AND DATE_FORMAT(DATE_SUB(CREATED_STAMP,INTERVAL '"+timeDiff+"' HOUR),'%Y-%m-%d') < '"+endDate+"' "
				+ " AND IFNULL(STATUS_ID, '') != 'IXF_REQUESTED' AND IFNULL(STATUS_ID, '') != 'IXF_CANCELLED'");
			
			if(productId != null && !productId.equals("")){
				sql.append(" AND PRODUCT_ID='"+productId+"'");
			}
			if(facilityId != null && !facilityId.equals("")){
				sql.append(" AND FACILITY_ID='"+facilityId+"'");
			}else{
				String facilityIds = WarehouseCommon.getAllPermissionFacility(request, delegator);
				sql.append(" AND  FACILITY_ID in ("+facilityIds+")");
			}
			sql.append(") X"
					+ " LEFT JOIN SHIPMENT_RECEIPT Y ON X.INVENTORY_ITEM_ID = Y.INVENTORY_ITEM_ID"
					+ " LEFT JOIN INVENTORY_TRANSFER Z ON X.INVENTORY_ITEM_ID = Z.INVENTORY_ITEM_ID"
					+ " LEFT JOIN FACILITY H ON X.FACILITY_ID = H.FACILITY_ID"
					+ " LEFT JOIN (SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as DESRIPTION "
					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) G on X.PRODUCT_ID=G.PRODUCT_ID"
					+ " LEFT JOIN PRODUCT T ON X.PRODUCT_ID=T.PRODUCT_ID GROUP BY X.RECEIVE_TIME,X.FACILITY_ID,X.PRODUCT_ID");
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), StockinReportVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");	
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	 /**export CVF
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public static String exportReceivedData(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	Map<String,Object> datas = ItemToString(request, response);
			if((Integer)datas.get("resultCode") == -1){
				return "error";
			}
			
			List<String> dataList = (List<String>) datas.get("result");
			response.setCharacterEncoding("UTF-8");
	        response.setHeader("contentType", "text/html; charset=UTF-8");
	        response.setContentType("application/octet-stream");
	        response.addHeader("Content-Disposition", "attachment; filename=StockinReport.csv");
	        
	        BufferedOutputStream out = null;
	        try {
	            out = new BufferedOutputStream(response.getOutputStream());
	            for(String data : dataList) {
	                out.write(data.getBytes("UTF-8"));
	                out.write("\n".getBytes("UTF-8"));
	            }	           
	        }
	        catch (IOException e) {        	
	            e.printStackTrace();
	        }
	        finally{
	            try {
	                if(out != null){
	                    out.flush();
	                    out.close();
	                }
	            }
	            catch (IOException e) {
	            	e.printStackTrace();
	            }
	        }
	        return "success";
	}
	 
	 /**
	  * @param inventoryItemService
	  * @return
	 * @throws ParseException 
	  * @throws Exception
	  */
	 public static Map<String,Object> ItemToString(HttpServletRequest request, HttpServletResponse response) {
		   Map<String,Object> result = new HashMap<String, Object>();
		   List<String> dataList = new ArrayList<String>();
		   Map<String,Object> map = new HashMap<String, Object>();
		   if(request.getRequestURI().contains("exportReceivedData")){
			   map = queryReportData(request, response);
		   }else{
			   map = queryOperatorsReportData(request, response);
		   }
           result.put("resultCode", map.get("resultCode"));
           result.put("resultMsg", map.get("resultMsg"));
           List<StockinReportVo> list = (List<StockinReportVo>) map.get("result");
           dataList.add("Product ID,Description,Unit cost,Receive Quantity,Transfer Quantity,Total quantity,Receive Time,Facility");
           if(list != null){
	            for(StockinReportVo stockinReportVo : list){
	            	String productId = stockinReportVo.getProductId();
	            	String brandName = stockinReportVo.getBrandName();            	
	            	String model = stockinReportVo.getModel();
	            	String description = stockinReportVo.getDescription(); 
	            	if(description == null){
	            		description = "";
	            	}else{
	            		description = description.replace("\r", "");
	            		description = description.replace(",", " ");
	            	}
	            	String descriptionStr = brandName+"|"+model;
	            	if(!"".equals(description)){
	            		descriptionStr = descriptionStr + "|"+description;
	            	}
	            	String unitCost = stockinReportVo.getUnitCost();
	            	String receiveQuantity = stockinReportVo.getReceiveQuantity();
	            	String transferQuantity = stockinReportVo.getTransferQuantity();
	            	String totalQuantity = new BigDecimal(receiveQuantity).add(new BigDecimal(transferQuantity)).toString();
	            	String receiveTime = stockinReportVo.getReceiveTime();
	            	String facilityName = stockinReportVo.getFacilityName();
	            	
	        		dataList.add(productId+","+descriptionStr+","+unitCost+","+receiveQuantity+","+transferQuantity+","+totalQuantity+","+receiveTime+","+facilityName);
	            }
           } 
	        result.put("result", dataList);
	        return result;
	    }
	 
	 /**
	  * Operators Report Data
	  * @param request
	  * @param response
	  * @return
	  */
	 public static Map<String,Object> queryOperatorsReportData(HttpServletRequest request,HttpServletResponse response){
		 GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		 Map<String,Object> result = queryReportData(request, response);
		 try{
			 List<StockinReportVo> list = (List<StockinReportVo>) result.get("result");
			 if(list != null && list.size() > 0){
				 for(StockinReportVo stockinReportVo : list){
					List<EntityExpr> exprs = FastList.newInstance();
					exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, stockinReportVo.getProductId()));
					exprs.add(EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, "SPECIAL_COST_PRICE"));
					List<GenericValue> productPrices = delegator.findByCondition("ProductPrice", EntityCondition.makeCondition(exprs), null, null);
					if(productPrices != null && productPrices.size() > 0){
						GenericValue productPrice = productPrices.get(0);
						stockinReportVo.setUnitCost(productPrice.getString("price"));
					}else{
						stockinReportVo.setUnitCost("0");
					}
				 }
			 }
		 } catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		 }
		 return result;
	 }
}
