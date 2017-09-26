package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;

import com.alibaba.fastjson.JSONObject;

public class ProductStock {
	
	 
	 /**
	  * @param request
	  * @param response
	  * @return
	  */
	 public static Map<String,Object> queryCategory(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	        Delegator delegator = dispacher.getDelegator();

			JSONObject json = InventoryTransfer.makeParams(request);
			String categoryName = json.getString("categoryName");
			String pageNum = json.getString("pageNum");
			String pageSize = json.getString("pageSize");
			if(pageNum == null || pageNum.equals("")){
				pageNum = "1";
			}
			if(pageSize == null || pageSize.equals("")){
				pageSize = "10";
			}

			StringBuffer sql = new StringBuffer("select PRODUCT_CATEGORY_ID AS productCategoryId,CATEGORY_NAME AS categoryName from PRODUCT_CATEGORY");
			if(categoryName != null && !categoryName.equals("")){
				sql.append(" WHERE CATEGORY_NAME LIKE '%"+categoryName+"%'");
			}

			try {
				result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), ProductCategoryVo.class, delegator);
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
	  * @param request
	  * @param response
	  * @return
	  */
	 public static Map<String,Object> queryPurchaseInventory(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	        Delegator delegator = dispacher.getDelegator();

			String facilityId = "";
			String productId = "";
			String internalName = "";
			String productTypeId = "";
			String productCategoryId = "";
			String qohm = "";
			String atpm = "";
			
			String pageNum = "";
			String pageSize = "";
			String flag = "";
			if(request.getRequestURI().contains("queryPurchaseInventory")){
				JSONObject json = InventoryTransfer.makeParams(request);
				facilityId = json.getString("facilityId");
				productId = json.getString("productId");
				internalName = json.getString("internalName");
				productTypeId = json.getString("productTypeId");
				productCategoryId = json.getString("productCategoryId");
				qohm = json.getString("qohm");
				atpm = json.getString("atpm");
				
				pageNum = json.getString("pageNum");
				pageSize = json.getString("pageSize");
				if(pageNum == null || pageNum.equals("")){
					pageNum = "1";
				}
				if(pageSize == null || pageSize.equals("")){
					pageSize = "10";
				}
				
				flag = json.getString("flag");
			}else{
				Map map = UtilHttp.getParameterMap(request);
				facilityId = (String) map.get("facilityId");
				productId = (String) map.get("productId");
				internalName = (String) map.get("internalName");
				productTypeId = (String) map.get("productTypeId");
				productCategoryId = (String) map.get("productCategoryId");
				qohm = (String) map.get("qohm");
				atpm = (String) map.get("atpm");
				
				pageNum = "0";
				pageSize = (String) map.get("pageSize");
				
				if(pageSize == null || pageSize.equals("")){
					pageSize = "10";
				}
				flag = (String) map.get("flag");;
			}
			try {

				String regex = "^(\\-|\\+?)\\d+(\\.\\d+)?$";
				Pattern p=Pattern.compile(regex);
				if(qohm != null && !qohm.equals("")){					
					Matcher m=p.matcher(qohm);
					if(!m.matches()){
						throw new ServiceException("The number of QOH minus Min Stock must be numbers");
					}					
				}
				if(atpm != null && !atpm.equals("")){				
					Matcher m=p.matcher(atpm);
					if(!m.matches()){
						throw new ServiceException("The number of ATP minus Min Stock must be numbers");
					}					
				}
				StringBuffer sql = new StringBuffer("SELECT x.FACILITY_NAME as facilityName,x.PRODUCT_ID as productId,x.INTERNAL_NAME as internalName,x.BRAND_NAME as brandName,x.QOH AS qoh,x.ATP as atp,y.description,z.orderNum FROM("
						+ " SELECT e.FACILITY_ID,e.FACILITY_NAME,a.PRODUCT_ID,b.INTERNAL_NAME,b.BRAND_NAME,sum(a.QUANTITY_ON_HAND_TOTAL) AS QOH,sum(a.AVAILABLE_TO_PROMISE_TOTAL) AS ATP"
						+ " FROM INVENTORY_ITEM a LEFT JOIN PRODUCT b ON a.PRODUCT_ID = b.PRODUCT_ID "
						+ " LEFT JOIN PRODUCT_CATEGORY_MEMBER c ON b.PRODUCT_ID=c.PRODUCT_ID "
						+ " LEFT JOIN PRODUCT_CATEGORY d ON c.PRODUCT_CATEGORY_ID=d.PRODUCT_CATEGORY_ID"
						+ " LEFT JOIN FACILITY e ON a.FACILITY_ID=e.FACILITY_ID"
						+ " WHERE (a.STATUS_ID IS NULL OR a.STATUS_ID = 'INV_AVAILABLE' OR a.STATUS_ID = 'IXF_COMPLETE') ");
				
				
				if(facilityId != null && !facilityId.equals("")){
					sql.append(" AND a.FACILITY_ID = '"+facilityId+"'");
				}else{
					String facilityIds = WarehouseCommon.getAllPermissionFacility(request, delegator);
					sql.append(" AND a.FACILITY_ID in ("+facilityIds+")");
				}
				
				if(productId != null && !productId.equals("")){
					sql.append(" AND b.PRODUCT_ID='"+productId+"'");
				}
				if(internalName != null && !internalName.equals("")){
					sql.append(" AND b.INTERNAL_NAME like '%"+internalName+"%'");
				}
				if(productTypeId != null && !productTypeId.equals("")){
					sql.append(" AND b.PRODUCT_TYPE_ID ='"+productTypeId+"'");
				}
				if(productCategoryId != null && !productCategoryId.equals("")){
					sql.append(" AND d.PRODUCT_CATEGORY_ID ='"+productCategoryId+"'");
				}
				if(flag != null && flag.equals("1")){
					sql.append(" GROUP BY a.PRODUCT_ID order by  a.PRODUCT_ID ) x ");
				}else{
					sql.append(" GROUP BY a.FACILITY_ID,a.PRODUCT_ID order by  a.PRODUCT_ID ) x ");
				}
				sql.append(""
						+ " LEFT JOIN( SELECT b.product_id,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as description "
						+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) y on x.PRODUCT_ID=y.PRODUCT_ID "
						+ " LEFT JOIN(SELECT c.INVENTORY_FACILITY_ID,a.PRODUCT_ID,sum(a.QUANTITY) as orderNum FROM ORDER_ITEM a"
						+ " LEFT JOIN ORDER_HEADER b ON a.ORDER_ID = b.ORDER_ID "
						+ " LEFT JOIN PRODUCT_STORE c ON b.PRODUCT_STORE_ID = c.PRODUCT_STORE_ID"
						+ " where  b.STATUS_ID !='ORDER_CREATED' AND b.STATUS_ID !='ORDER_COMPLETED'");
					
				if(facilityId != null && !facilityId.equals("")){
					sql.append(" AND c.INVENTORY_FACILITY_ID='"+facilityId+"'");
				}else{
					String facilityIds = WarehouseCommon.getAllPermissionFacility(request, delegator);
					sql.append(" AND  c.INVENTORY_FACILITY_ID in ("+facilityIds+")");
				}
				if(flag != null && flag.equals("1")){
					if(facilityId != null && facilityId.equals("")){
						sql.append(" group by a.PRODUCT_ID) z on (x.PRODUCT_ID = z.PRODUCT_ID ) WHERE 1=1 ");
					}else{
						sql.append(" group by c.INVENTORY_FACILITY_ID,a.PRODUCT_ID) z on (x.PRODUCT_ID = z.PRODUCT_ID and x.FACILITY_ID=z.INVENTORY_FACILITY_ID) WHERE 1=1 ");
					}
				}else{
					sql.append(" group by c.INVENTORY_FACILITY_ID,a.PRODUCT_ID) z on (x.PRODUCT_ID = z.PRODUCT_ID and x.FACILITY_ID=z.INVENTORY_FACILITY_ID) WHERE 1=1 ");
				}
				if(qohm != null && !qohm.equals("")){
					sql.append(" AND x.QOH <"+qohm);
				}
				if(atpm != null && !atpm.equals("")){
					sql.append(" AND x.ATP <"+atpm);
				}	
				result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), PurchaseInventoryVo.class, delegator);
				result.put("resultCode", 1);
				result.put("resultMsg", "Successful operation");	
			} catch(ServiceException e1){
				result.put("resultCode", -1);
				result.put("resultMsg", e1.getMessage());
				e1.printStackTrace();
			} catch (Exception e) {
				result.put("resultCode", -1);
				result.put("resultMsg", "System exception, please contact the administrator");
				e.printStackTrace();
			}
			return result;
	 }
	 
	 /**
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public static String exportPurchaseInventory(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	Map<String,Object> datas = ItemToString(request, response);
			if((Integer)datas.get("resultCode") == -1){
				return "error";
			}
			
			List<String> dataList = (List<String>) datas.get("result");
			response.setCharacterEncoding("UTF-8");
	        response.setHeader("contentType", "text/html; charset=UTF-8");
	        response.setContentType("application/octet-stream");
	        response.addHeader("Content-Disposition", "attachment; filename=ProductStock.csv");
	        
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
	  * @throws Exception
	  */
	 public static Map<String,Object> ItemToString(HttpServletRequest request, HttpServletResponse response) {
		    Map<String,Object> result = new HashMap<String, Object>();
		    List<String> dataList = new ArrayList<String>();
            Map<String,Object> map = queryPurchaseInventory(request, response);
            result.put("resultCode", map.get("resultCode"));
            result.put("resultMsg", map.get("resultMsg"));
            List<PurchaseInventoryVo> list = (List<PurchaseInventoryVo>) map.get("result");
            dataList.add("Product ID,Model,BrandName,Description,Total ATP,Total QOH,Orderer Quantity,Minimum Stock,QOH minus Min Stock,ATP minus Min Stock,Usage");
            if(list != null){
	            for(PurchaseInventoryVo purchaseInventoryVo : list){
	            	String productId = purchaseInventoryVo.getProductId();
	            	String internalName = purchaseInventoryVo.getInternalName();
	            	if(internalName == null){
	            		internalName = "";
	            	}
	            	String brandName = purchaseInventoryVo.getBrandName();
	            	if(brandName == null){
	            		brandName = "";
	            	}
	            	String qoh = purchaseInventoryVo.getQoh();
	            	
	            	String atp = purchaseInventoryVo.getAtp();
	            	
	            	String description = purchaseInventoryVo.getDescription();
	            	if(description == null){
	            		description = "";
	            	}else{
	            		description = description.replace("\r", "");
	            		description = description.replace(",", " ");
	            	}
	            	String orderNum = purchaseInventoryVo.getOrderNum();
	            	if(orderNum == null){
	            		orderNum = "0";
	            	}
	            	
	        		String minimumStock = "0";
	        		dataList.add(productId+","+internalName+","+brandName+","+description+","+atp+","+qoh+","+orderNum+","+minimumStock+","+qoh+","+atp+","+"");
	            }
            } 
	        result.put("result", dataList);
	        return result;
	    }
	 
	 
}
