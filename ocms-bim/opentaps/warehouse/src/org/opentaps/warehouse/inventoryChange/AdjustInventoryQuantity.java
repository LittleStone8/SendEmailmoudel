package org.opentaps.warehouse.inventoryChange;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
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
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.AdjustIMEIQuantityDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.AdjustQuantityDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.AdjustIMEIInventoryLogVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.AdjustInventoryLogVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.AdjustInventoryVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.AdjustInventoryVoEX;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.StockinReportVo;

import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.AdjustQuantityDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.AdjustInventoryVo;

public class AdjustInventoryQuantity {
	/**
	 * Querying physical inventory
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryPhysicalInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String productId = json.getString("productId");
		String model = json.getString("model");
		String orderBy = json.getString("orderBy");
		String flag = json.getString("flag");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			StringBuffer sql = new StringBuffer("SELECT X.PRODUCT_ID AS productId,Y.INTERNAL_NAME AS model,Z.DESCRIPTION as description,SUM(X.QUANTITY_ON_HAND_TOTAL) AS qoh,SUM(X.AVAILABLE_TO_PROMISE_TOTAL) AS atp "
					+ " FROM INVENTORY_ITEM X LEFT JOIN PRODUCT Y ON X.PRODUCT_ID = Y.PRODUCT_ID LEFT JOIN ( SELECT b.product_id, "
					+ " GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID "
					+ " GROUP BY product_id) Z ON X.PRODUCT_ID = Z.PRODUCT_ID "
					+ " WHERE X.FACILITY_ID='"+facilityId+"' AND (X.STATUS_ID IS NULL  OR X.STATUS_ID = 'IXF_COMPLETE') AND X.INVENTORY_ITEM_TYPE_ID != 'SERIALIZED_INV_ITEM' ");
			if(productId != null && !productId.equals("")){
				sql.append(" AND X.PRODUCT_ID='"+productId+"'");
			}
			if(model != null && !model.equals("")){
				sql.append(" AND Y.INTERNAL_NAME LIKE '%"+model+"%'");
			}
			if(flag != null && flag.equals("N")){
				sql.append(" AND X.QUANTITY_ON_HAND_TOTAL>0");
			}
			sql.append(" GROUP BY X.PRODUCT_ID ORDER BY "+orderBy);
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), AdjustInventoryVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Querying IMEI physical inventory
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryIMEIPhysicalInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String imei ="";
		String model="";
		String orderBy="";
		String pageNum="";
		String pageSize="";
		if(json!=null)
		{
			imei = json.getString("imei");
			model = json.getString("model");
			orderBy = json.getString("orderBy");
			pageNum = json.getString("pageNum");
			pageSize = json.getString("pageSize");
		}
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			StringBuffer sql = new StringBuffer("SELECT X.PRODUCT_ID AS productId,Y.INTERNAL_NAME AS model,X.SERIAL_NUMBER AS imei,X.STATUS_ID AS status,Z.DESCRIPTION as description,X.QUANTITY_ON_HAND_TOTAL AS qoh,X.AVAILABLE_TO_PROMISE_TOTAL AS atp,X.INVENTORY_ITEM_ID as itemid "
					+ " FROM INVENTORY_ITEM X LEFT JOIN PRODUCT Y ON X.PRODUCT_ID = Y.PRODUCT_ID LEFT JOIN ( SELECT b.product_id, "
					+ " GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID "
					+ " GROUP BY product_id) Z ON X.PRODUCT_ID = Z.PRODUCT_ID "
					+ " WHERE X.FACILITY_ID='"+facilityId+"' AND   X.INVENTORY_ITEM_TYPE_ID = 'SERIALIZED_INV_ITEM' AND IFNULL(X.STATUS_ID,\"\") != 'IXF_REQUESTED' AND IFNULL(X.STATUS_ID,\"\") != 'IXF_CANCELLED' AND IFNULL(X.STATUS_ID,\"\") != 'IXF_PICKING' ");
			if(imei != null && !imei.equals("")){
				sql.append(" AND X.SERIAL_NUMBER='"+imei+"'");
			}
			if(model != null && !model.equals("")){
				sql.append(" AND Y.INTERNAL_NAME LIKE '%"+model+"%'");
			}
				
			//sql.append(" AND X.QUANTITY_ON_HAND_TOTAL>0");
			
			//sql.append(" GROUP BY X.PRODUCT_ID ");
			
			result = EncapsulatedQueryResultsUtil.getResults_(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), AdjustInventoryVoEX.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * adjust quantity on hand
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> adjustPhysicalInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String partyId = WarehouseCommon.getPartyIdByRequest(request);
		String userLoginId = WarehouseCommon.getUserLoginIdByRequest(request);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		try{
			TransactionUtil.begin();
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			List<AdjustQuantityDto> params = InventoryTransfer.makeTransferParams(request, AdjustQuantityDto.class);
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select products that need to be adjust");
			}
			for(AdjustQuantityDto adjustQuantityDto : params){
				String productId = adjustQuantityDto.getProductId();
				String adjustNumberStr = adjustQuantityDto.getAdjustNumber();
				String totalNumberStr = adjustQuantityDto.getTotalNumber();
				String varianceReasonId = adjustQuantityDto.getVarianceReasonId();
				String common = adjustQuantityDto.getCommon();
				
				String regex = "^-?\\d+$";
				Pattern p=Pattern.compile(regex);
				Matcher m=p.matcher(adjustNumberStr);
				if(!m.matches()){
					throw new ServiceException("The number of adjustments must be integers");
				}
				
				int adjustNumber = Integer.parseInt(adjustNumberStr);
				int totalNumber =  Integer.parseInt(totalNumberStr);
				int actualNum = 0;
				int adjustmount = Math.abs(adjustNumber);
				if(adjustNumber < 0 && adjustmount > totalNumber){
					throw new ServiceException("The adjustment of the mathematical cannot exceed the maximum ATP of the product");
				}
				List<GenericValue> inventoryItems = new ArrayList<GenericValue>();
				if(adjustNumber < 0){
					inventoryItems = getProductNumFromWarehouse(productId, facilityId, delegator,true);
					if(inventoryItems == null || inventoryItems.size() == 0){
						throw new ServiceException(productId+"|"+adjustQuantityDto.getModel()+": quantity not sufficient");
					}
				}else{
					inventoryItems = getProductNumFromWarehouse(productId, facilityId, delegator,false);
				}
				
				Map<String,Object> map = new HashMap<String, Object>();
				
				map.put("varianceReasonId", varianceReasonId);
				map.put("operator", (String)userLogin.get("userLoginId")+"("+(String)userLogin.get("partyId")+")");
				map.put("usageType", UsageTypeEnum.Adjustment.getName());
				map.put("userLogin", userLogin);
				if(adjustNumber < 0){
					
					adjustNumber = Math.abs(adjustNumber);
					for(GenericValue inventoryItem : inventoryItems){
						map.put("inventoryItemId", inventoryItem.getString("inventoryItemId"));			
						BigDecimal QTY = inventoryItem.getBigDecimal("quantityOnHandTotal");
						BigDecimal ATP = inventoryItem.getBigDecimal("availableToPromiseTotal");
						if(adjustNumber > ATP.intValue()){
							map.put("availableToPromiseVar", ATP.negate());
							map.put("quantityOnHandVar", ATP.negate());
							map.put("qoh", QTY.subtract(ATP));
							map.put("atp", ATP.subtract(ATP));
							actualNum += ATP.intValue();
							adjustNumber -= ATP.intValue();
							
							
							WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), null, null, null, null, null, null);
							String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
							WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItem.getString("inventoryItemId"), physicalInventoryId, varianceReasonId, (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), null);
							WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItem.getString("inventoryItemId"), (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,inventoryItem.getString("statusId"));
					
							//dispatcher.runSync("createPhysicalInventoryAndVariance", map);
						}else{
							map.put("availableToPromiseVar", new BigDecimal(adjustNumber).negate());
							map.put("quantityOnHandVar", new BigDecimal(adjustNumber).negate());
							map.put("qoh", QTY.subtract(new BigDecimal(adjustNumber)));
							map.put("atp", ATP.subtract(new BigDecimal(adjustNumber)));
							actualNum += adjustNumber;
							
							WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), null, null, null, null, null, null);
							String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
							WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItem.getString("inventoryItemId"), physicalInventoryId, varianceReasonId, (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), null);
							WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItem.getString("inventoryItemId"), (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,inventoryItem.getString("statusId"));
							
							//dispatcher.runSync("createPhysicalInventoryAndVariance", map);
							break;
						}
						
					}
					if(actualNum != adjustmount){
						throw new ServiceException("Someone has already operated this batch of goods before you, please refresh your retry");
					}
				}else{
					/*GenericValue oneInventoryItem = inventoryItems.get(0);
					GenericValue cloneInventoryItem = (GenericValue) oneInventoryItem.clone();*/
					Iterator<GenericValue> itr = inventoryItems.iterator();
					while(itr.hasNext()){
						GenericValue inventoryItem = itr.next();
						List<EntityExpr> exprsProductId = FastList.newInstance();
						exprsProductId.add(EntityCondition.makeCondition("inventoryItemId", EntityOperator.EQUALS, inventoryItem.getString("inventoryItemId")));
						List<GenericValue> inventoryItemVars = delegator.findByCondition("InventoryItemVariance", EntityCondition.makeCondition(exprsProductId), null, null);
						if(inventoryItemVars == null || inventoryItemVars.size() == 0){
							itr.remove();
						}
					}
					if(inventoryItems.size() == 0){
						throw new ServiceException("This product has no adjustment record, please use the warehousing procedure");
					}
					for(int i=inventoryItems.size()-1;i>=0;i--){
						GenericValue inventoryItem = inventoryItems.get(i);
						
						String inventoryItemId = inventoryItem.getString("inventoryItemId");
						BigDecimal QTY = inventoryItem.getBigDecimal("quantityOnHandTotal");
						BigDecimal ATP = inventoryItem.getBigDecimal("availableToPromiseTotal");
						String productIdstr=inventoryItem.getString("productId");
						String ownerPartyId=inventoryItem.getString("ownerPartyId");
						Timestamp datetimeReceived = inventoryItem.getTimestamp("datetimeReceived");
						String statusId=inventoryItem.getString("statusId");
						String lotId=inventoryItem.getString("lotId");
						String serialNumber=inventoryItem.getString("serialNumber");
						String unitCost=inventoryItem.getString("unitCost");
						String currencyUomId=inventoryItem.getString("currencyUomId");
						String parentInventoryItemId=inventoryItem.getString("parentInventoryItemId");
						
						map.put("inventoryItemId", inventoryItem.getString("inventoryItemId"));	
												
						List<EntityExpr> exprsProductId = FastList.newInstance();
						exprsProductId.add(EntityCondition.makeCondition("inventoryItemId", EntityOperator.EQUALS, inventoryItem.getString("inventoryItemId")));
						List<GenericValue> inventoryItemVars = delegator.findByCondition("InventoryItemVariance", EntityCondition.makeCondition(exprsProductId), null, null);
						
						int adjustQuantity = 0;
						for(GenericValue inventoryItemVar : inventoryItemVars){
							adjustQuantity += inventoryItemVar.getBigDecimal("quantityOnHandVar").intValue();
						}
						if(i == 0){
							map.put("availableToPromiseVar", new BigDecimal(adjustNumber));
							map.put("quantityOnHandVar", new BigDecimal(adjustNumber));
							map.put("qoh", QTY.add(new BigDecimal(adjustNumber)));
							map.put("atp", ATP.add(new BigDecimal(adjustNumber)));
							
//							map.put("availableToPromiseVar", new BigDecimal(adjustNumber).negate());
//							map.put("quantityOnHandVar", new BigDecimal(adjustNumber).negate());
							WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), null, null, null, null, null, null);
						//	WarehouseCommon.updateInventoryItemById(delegator, null, productIdstr, ownerPartyId, datetimeReceived, statusId, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), facilityId, lotId, serialNumber, unitCost, currencyUomId, parentInventoryItemId);
						//	WarehouseCommon.insertInventoryItem(delegator, "NON_SERIAL_INV_ITEM", productIdstr, ownerPartyId, datetimeReceived, statusId, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), facilityId, lotId, serialNumber, unitCost, currencyUomId, parentInventoryItemId);
							String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
							WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItemId, physicalInventoryId, varianceReasonId,(BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), null);
							WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItemId, (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,inventoryItem.getString("statusId"));
							
						//	dispatcher.runSync("createPhysicalInventoryAndVariance", map);
							break;
						}
						if(adjustQuantity < 0){
							if(adjustNumber > Math.abs(adjustQuantity)){
								map.put("availableToPromiseVar", new BigDecimal(Math.abs(adjustQuantity)));
								map.put("quantityOnHandVar", new BigDecimal(Math.abs(adjustQuantity)));
								map.put("qoh", QTY.add(new BigDecimal(Math.abs(adjustQuantity))));
								map.put("atp", ATP.add(new BigDecimal(Math.abs(adjustQuantity))));
								
								
								
								//dispatcher.runSync("createPhysicalInventoryAndVariance", map);
								
								WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), null, null, null, null, null, null);
								String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
								WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItemId, physicalInventoryId, varianceReasonId,(BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), null);
								WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItemId, (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,inventoryItem.getString("statusId"));
								
								adjustNumber -= Math.abs(adjustQuantity);
							}else{
								map.put("availableToPromiseVar", new BigDecimal(adjustNumber));
								map.put("quantityOnHandVar", new BigDecimal(adjustNumber));
								map.put("qoh", QTY.add(new BigDecimal(adjustNumber)));
								map.put("atp", ATP.add(new BigDecimal(adjustNumber)));
								
								
								//dispatcher.runSync("createPhysicalInventoryAndVariance", map);
								
								WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), null, null, null, null, null, null);
								String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
								WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItemId, physicalInventoryId, varianceReasonId,(BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), null);
								WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItemId, (BigDecimal)map.get("availableToPromiseVar"), (BigDecimal)map.get("quantityOnHandVar"), (BigDecimal)map.get("qoh"), (BigDecimal)map.get("atp"), "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,inventoryItem.getString("statusId"));
								
								break;
							}
						}
						
					}
				}
					
			}
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
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
	 * adjust IMEI quantity on hand
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> adjustIMEIPhysicalInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String partyId = WarehouseCommon.getPartyIdByRequest(request);
		String userLoginId = WarehouseCommon.getUserLoginIdByRequest(request);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		try{
			TransactionUtil.begin();
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			List<AdjustIMEIQuantityDto> params = InventoryTransfer.makeTransferParams(request, AdjustIMEIQuantityDto.class);
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select products that need to be adjust");
			}
			for(AdjustIMEIQuantityDto adjustQuantityDto : params){
				String imei = adjustQuantityDto.getImei();
				String Itemid = adjustQuantityDto.getItemid();
				String varianceReasonId = adjustQuantityDto.getVarianceImeiReasonId();
				
				
				List<GenericValue> inventoryItems = new ArrayList<GenericValue>();
				//inventoryItems = getProductNumIMEIFromWarehouse(imei, facilityId, delegator,true);
				inventoryItems = getProductNumIMEIFromWarehouseByinvid(Itemid, facilityId, delegator,true);
				
				
				GenericValue current=null;
				for(int i=0;i<inventoryItems.size();i++)
				{
					GenericValue temp = inventoryItems.get(i);
					String statusId = temp.getString("statusId");
					if("SERIALIZED_INV_ITEM".equals(temp.getString("inventoryItemTypeId"))      )
					{
						current=temp;
						break;
					}
				}
				if(current==null)
					throw new ServiceException("This product has no adjustment record, please use the warehousing procedure");
				
				
				String inventoryItemId = current.getString("inventoryItemId");
				String statusId = current.getString("statusId");
				
				BigDecimal iniQTY=null;
				BigDecimal iniATP=null;
				BigDecimal QTY=null;
				BigDecimal ATP=null;
				BigDecimal retQTY=null;
				BigDecimal retATP=null;
				if("VAR_FOUND".equals(varianceReasonId))//增加
				{
					iniQTY = current.getBigDecimal("quantityOnHandTotal");
					iniATP = current.getBigDecimal("availableToPromiseTotal");
					
					QTY=new BigDecimal(1);
					ATP=new BigDecimal(1);
					
					retQTY=new BigDecimal(1);
					retATP=new BigDecimal(1);
					current.set("quantityOnHandTotal",retQTY);
					current.set("availableToPromiseTotal", retATP);
					current.set("statusId", "INV_AVAILABLE");
					
				}else if("VAR_STOLEN".equals(varianceReasonId)||"VAR_LOST".equals(varianceReasonId)||"VAR_DAMAGED".equals(varianceReasonId)) //减少
				{
					iniQTY = current.getBigDecimal("quantityOnHandTotal");
					iniATP = current.getBigDecimal("availableToPromiseTotal");
					
					QTY=new BigDecimal(-1);;
					ATP=new BigDecimal(-1);
					
					retQTY=new BigDecimal(0);
					retATP=new BigDecimal(0);
					current.set("availableToPromiseTotal", retATP);
					current.set("quantityOnHandTotal",retQTY);
					current.set("statusId", "INV_DEACTIVATED");
				}
				else {
					throw new ServiceException("Wrong operation");
				}
				
				delegator.store(current);
				String physicalInventoryId = WarehouseCommon.insertPhysicalInventory(delegator,partyId,new Timestamp(System.currentTimeMillis()),null);
				WarehouseCommon.insertInventoryItemVariance(delegator, inventoryItemId, physicalInventoryId, varianceReasonId, ATP, QTY, null);
				WarehouseCommon.insertInventoryItemDetail(delegator, new Timestamp(System.currentTimeMillis()), inventoryItemId, ATP,QTY , retATP, retQTY, "Adjustment", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")", null, null, null,varianceReasonId,current.getString("statusId"));
			}
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
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
	 * Adjustment of inventory reasons
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryVarianceReason(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");		
		try {
			List<GenericValue> varianceReasons = delegator.findAll("VarianceReason");
			List<GenericValue> retvarianceReasons =new ArrayList<GenericValue>();
			Iterator<GenericValue> itr = varianceReasons.iterator();
			while(itr.hasNext()){
				GenericValue varianceReason = itr.next();
				if(varianceReason.getString("varianceReasonId").equals("VAR_DAMAGED") || varianceReason.getString("varianceReasonId").equals("VAR_FOUND")
						|| varianceReason.getString("varianceReasonId").equals("VAR_LOST") || varianceReason.getString("varianceReasonId").equals("VAR_STOLEN") || varianceReason.getString("varianceReasonId").equals("VAR_WRONG_OPERATION")) {
					
					if(varianceReason.getString("varianceReasonId").equals("VAR_WRONG_OPERATION"))
					{
						varianceReason.set("description","Wrong Operation");
					}
					retvarianceReasons.add(varianceReason);
				}
			}
			result.put("varianceReasons", retvarianceReasons);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
		}
		return result;
	}
	
	/**
	 * Adjustment of IMEI inventory reasons
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryIMEIVarianceReason(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");		
		try {
			List<GenericValue> varianceReasons = delegator.findAll("VarianceReason");
			List<GenericValue> retvarianceReasons =new ArrayList<GenericValue>();
			Iterator<GenericValue> itr = varianceReasons.iterator();
			while(itr.hasNext()){
				GenericValue varianceReason = itr.next();
				if(varianceReason.getString("varianceReasonId").equals("VAR_DAMAGED") || varianceReason.getString("varianceReasonId").equals("VAR_LOST")
						|| varianceReason.getString("varianceReasonId").equals("VAR_STOLEN") || varianceReason.getString("varianceReasonId").equals("VAR_FOUND") ) {
					retvarianceReasons.add(varianceReason);
				}
			}
			result.put("varianceReasons", retvarianceReasons);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
		}
		return result;
	}
	
	/**
	 * Query the Adjustment inventory log
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryAdjustInventoryLog(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String productId = json.getString("productId");
		String model = json.getString("model");
		String orderBy = json.getString("orderBy");
		String varianceReason = json.getString("varianceReason");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			StringBuffer sql = new StringBuffer("SELECT H.DESRIPTION AS description,X.INVENTORY_ITEM_ID AS inventoryItemId, X.PRODUCT_ID as productId,G.INTERNAL_NAME AS model,Y.EFFECTIVE_DATE AS effectiveDate,Y.OPERATOR as operator,Y.QUANTITY_ON_HAND_DIFF AS adjustNumber,Z.DESCRIPTION as adjustReason,Y.QOH AS qoh,Y.ATP as atp FROM INVENTORY_ITEM X "
					+ " LEFT JOIN INVENTORY_ITEM_DETAIL Y ON  X.INVENTORY_ITEM_ID=Y.INVENTORY_ITEM_ID LEFT JOIN VARIANCE_REASON Z ON Y.REASON_ENUM_ID=Z.VARIANCE_REASON_ID LEFT JOIN PRODUCT G ON X.PRODUCT_ID=G.PRODUCT_ID "
					+ " LEFT JOIN (SELECT b.PRODUCT_ID, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as DESRIPTION "
					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) H ON X.PRODUCT_ID=H.PRODUCT_ID"
					+ " WHERE 1=1 AND X.FACILITY_ID='"+facilityId+"' AND Y.USAGE_TYPE='Adjustment' ");
			if(varianceReason != null && !varianceReason.equals("")){
				sql.append(" AND Y.REASON_ENUM_ID='"+varianceReason+"'");
			}
			if(model != null && !model.equals("")){
				sql.append(" AND G.INTERNAL_NAME LIKE '%"+model+"%'");
			}
			if(productId != null && !productId.equals("")){
				sql.append(" AND X.PRODUCT_ID ='"+productId+"'");
			}
			sql.append(" ORDER BY "+orderBy);
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), AdjustInventoryLogVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<String,Object> queryIMEIAdjustInventoryLog(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String imei = json.getString("imei");
		String model = json.getString("model");
		//String varianceReason = json.getString("varianceReason");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			StringBuffer sql = new StringBuffer("SELECT H.DESRIPTION AS description,X.SERIAL_NUMBER AS imei,Y.STATUS_ID AS status,G.INTERNAL_NAME AS model,Y.OPERATOR as operator,Z.DESCRIPTION as adjustReason,Y.ATP as atp FROM INVENTORY_ITEM X "
					+ " LEFT JOIN INVENTORY_ITEM_DETAIL Y ON  X.INVENTORY_ITEM_ID=Y.INVENTORY_ITEM_ID LEFT JOIN VARIANCE_REASON Z ON Y.REASON_ENUM_ID=Z.VARIANCE_REASON_ID LEFT JOIN PRODUCT G ON X.PRODUCT_ID=G.PRODUCT_ID "
					+ " LEFT JOIN (SELECT b.PRODUCT_ID, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as DESRIPTION "
					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) H ON X.PRODUCT_ID=H.PRODUCT_ID"
					+ " WHERE 1=1 AND X.FACILITY_ID='"+facilityId+"' AND Y.USAGE_TYPE='Adjustment' ");
//			if(varianceReason != null && !varianceReason.equals("")){
//				sql.append(" AND Y.REASON_ENUM_ID='"+varianceReason+"'");
//			}
			if(model != null && !model.equals("")){
				sql.append(" AND G.INTERNAL_NAME LIKE '%"+model+"%'");
			}
			if(imei != null && !imei.equals("")){
				sql.append(" AND X.SERIAL_NUMBER ='"+imei+"'");
			}
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), AdjustIMEIInventoryLogVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static List<GenericValue> getProductNumFromWarehouse(String productId,String facilityId,Delegator delegator,boolean zero) throws GenericEntityException{
		List<String> order = new ArrayList<String>();
		order.add("datetimeReceived");
		
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));		
		exprs.add(EntityCondition.makeCondition("inventoryItemTypeId", EntityOperator.EQUALS, "NON_SERIAL_INV_ITEM"));
		if(zero)
			exprs.add(EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
		
		List<EntityExpr> exprs2 = FastList.newInstance();
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, null));
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "IXF_COMPLETE"));

		
		EntityCondition totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
				EntityCondition.makeCondition(exprs, EntityOperator.AND),
				EntityCondition.makeCondition(exprs2, EntityOperator.OR)),
				EntityOperator.AND);
		return delegator.findByCondition("InventoryItem", totalCondition, null, order);
	}
	
	public static List<GenericValue> getProductNumIMEIFromWarehouse(String imei,String facilityId,Delegator delegator,boolean zero) throws GenericEntityException{
		List<String> order = new ArrayList<String>();
		order.add("datetimeReceived");
		
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));		
		

		EntityCondition totalCondition = EntityCondition.makeCondition(exprs);
		return delegator.findByCondition("InventoryItem", totalCondition, null, order);
	}
	
	public static List<GenericValue> getProductNumIMEIFromWarehouseByinvid(String invid,String facilityId,Delegator delegator,boolean zero) throws GenericEntityException{
		List<String> order = new ArrayList<String>();
		order.add("datetimeReceived");
		
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("inventoryItemId", EntityOperator.EQUALS, invid));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));		
		

		EntityCondition totalCondition = EntityCondition.makeCondition(exprs);
		return delegator.findByCondition("InventoryItem", totalCondition, null, order);
	}
	/**
	 * 获取物理库存变化明细
	 * @param dctx
	 * @param context
	 * @return
	 *//*
	public static Map getPhysicalInventory(DispatchContext dctx, Map context){
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) dctx.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		GenericValue userLogin = (GenericValue) context.get("userLogin");
		String productId = (String) context.get("productId");
		if(productId == null) productId = "";
		String internalName = (String) context.get("internalName");
		if(internalName == null) internalName = "";		
		internalName = internalName.trim();
		String orderBy = (String) context.get("orderBy");
		ResultSet rs = null;
		List<ProductFromInventory> physicalInventory = new ArrayList<ProductFromInventory>();
		try {
			String facilityId = UtilCommon.getUserLoginViewPreference(userLogin,delegator, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			//获取变化原因
			List<GenericValue> varianceReasons = delegator.findAll("VarianceReason");
			result.put("varianceReasons", varianceReasons);
			//获取库存
			StringBuffer sql = new StringBuffer("SELECT "
					+ "x.INVENTORY_ITEM_ID,"
					+ "x.product_id,"
					+ "x.lot_id,"
					+ "x.internal_Name,"
					+ "x.QUANTITY_ON_HAND_TOTAL,"
					+ "y.productAttributes,"
					+ "z.num"
					+ " FROM( SELECT "
					+ " a.INVENTORY_ITEM_ID,"
					+ "a.lot_id,"
					+ "a.product_id,"
					+ "a.QUANTITY_ON_HAND_TOTAL,"
					+ "b.internal_Name FROM INVENTORY_ITEM a LEFT JOIN PRODUCT b ON a.PRODUCT_ID = b.PRODUCT_ID"
					+ " WHERE (a.STATUS_ID IS NULL OR a.STATUS_ID = 'INV_AVAILABLE' OR a.STATUS_ID = 'IXF_COMPLETE')  AND a.INVENTORY_ITEM_TYPE_ID='NON_SERIAL_INV_ITEM' AND a.facility_id='"+facilityId+"'");
			if(productId != null && !productId.equals("")){
				sql.append(" AND a.product_id like '"+productId+"'");
			}
			if(internalName != null && !internalName.equals("")){
				sql.append(" AND b.internal_Name like '%"+internalName+"%'");
			}
			sql.append(") x LEFT JOIN ( SELECT "
					+ "b.product_id,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS productAttributes FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id ) y ON x.product_id = y.product_id LEFT JOIN ( SELECT a.product_id, SUM(a.QUANTITY_ON_HAND_TOTAL) AS num FROM INVENTORY_ITEM a WHERE( a.STATUS_ID IS NULL OR a.STATUS_ID = 'INV_AVAILABLE' OR a.STATUS_ID = 'IXF_COMPLETE') AND facility_id='"+facilityId+"'  GROUP BY a.product_id) z ON x.product_id = z.product_id");
			//执行sql
			if(orderBy != null && !orderBy.equals("")){
				sql.append(" order by "+orderBy);
			}
			rs = processor.executeQuery(sql.toString());
			if(rs != null){
				while(rs.next()){
					ProductFromInventory productFromInventory = new ProductFromInventory();
					productFromInventory.setInventoryItemId(rs.getString("INVENTORY_ITEM_ID"));
					productFromInventory.setProductId(rs.getString("product_Id"));
					productFromInventory.setLotId(rs.getString("lot_id"));
					productFromInventory.setInternalName(rs.getString("internal_Name"));
					productFromInventory.setQoh(rs.getBigDecimal("num"));
					productFromInventory.setDescription(rs.getString("productAttributes"));
					productFromInventory.setQuantityOnHandTotal(rs.getBigDecimal("QUANTITY_ON_HAND_TOTAL"));
					physicalInventory.add(productFromInventory);
				}
			}
			//设置返回结果
			result.put("physicalInventory", physicalInventory);
			result.put("productId", productId.trim());
			result.put("internalName", internalName.trim());
			result.put("facilityId", facilityId);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				processor.close();
			} catch (GenericDataSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}*/
}
