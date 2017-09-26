package org.opentaps.warehouse.inventoryChange;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;
import org.opentaps.warehouse.inventoryChange.constant.WarehouseConstant;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.ChangeTransferStatusModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.PickingDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.PickingDtoEX;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.SaveScanImeiDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.StartPickingDto;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TransferInventoryModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.IMEIInventoryCheckingVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.PickingByWarehouseVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.PickingVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.TranferInventoryVo;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.TranshipmentShippingBillVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;
public class InventoryTransfer{
	
	/**
	 * 模糊查询指定仓库的产品列
	 * @param request
	 * @param response
	 * @return
	 * @throws GenericDataSourceException
	 */
	public static Map<String,Object> findProductByFacility(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		JSONObject json = makeParams(request);
		//接收参数
		String productId = (String) json.get("productId");
		String productIdNum = (String) json.get("productIdNum");
		String productIdStatu = (String) json.get("productIdStatu");
		
		String brandName = (String) json.get("brandName");
		String brandNameNum = (String) json.get("brandNameNum");
		String brandNameStatu = (String) json.get("brandNameStatu");
		
		String internalName = (String) json.get("internalName");
		String internalNameNum = (String) json.get("internalNameNum");
		String internalNameStatu = (String) json.get("internalNameStatu");
	
		String pageNum = (String) json.get("pageNum");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		String pageSize = (String) json.get("pageSize");
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			StringBuffer sql = new StringBuffer("SELECT z.pickingNum,x.inventoryItemTypeId,x.product_Id as productId,x.BRAND_NAME as brandName,x.INTERNAL_NAME as model,x.atp as atp,y.description as description FROM(select a.INVENTORY_ITEM_TYPE_ID as inventoryItemTypeId,a.product_Id,b.BRAND_NAME,b.INTERNAL_NAME ,sum(AVAILABLE_TO_PROMISE_TOTAL) as atp from INVENTORY_ITEM a left join PRODUCT b on a.PRODUCT_ID=b.PRODUCT_ID where (a.STATUS_ID is null or a.STATUS_ID='INV_AVAILABLE' or a.STATUS_ID='IXF_COMPLETE') AND  a.FACILITY_ID='"+facilityId+"'");
			makeCondition(sql, "a.PRODUCT_ID", productIdNum, productId, productIdStatu);
			makeCondition(sql, "b.INTERNAL_NAME", internalNameNum, internalName, internalNameStatu);
			makeCondition(sql, "b.BRAND_NAME", brandNameNum, brandName, brandNameStatu);		
			sql.append("GROUP BY a.PRODUCT_ID )x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS description FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) y ON x.product_Id = y.product_Id "
					+ " LEFT JOIN (SELECT X.PRODUCT_ID,SUM(Y.TRANSFER_NUMBER) AS pickingNum FROM (SELECT INVENTORY_ITEM_ID,PRODUCT_ID FROM INVENTORY_ITEM WHERE STATUS_ID='IXF_PICKING' AND INVENTORY_ITEM_TYPE_ID='SERIALIZED_INV_ITEM') X LEFT JOIN INVENTORY_TRANSFER Y ON X.INVENTORY_ITEM_ID=Y.INVENTORY_ITEM_ID LEFT JOIN TRANSHIPMENT_SHIPPING_BILL Z ON Y.TRANSHIPMENT_SHIPPING_BILL_ID=Z.TRANSHIPMENT_SHIPPING_BILL_ID WHERE Z.STATUS='0' AND Y.FACILITY_ID ='"+facilityId+"'"
					+ " GROUP BY X.PRODUCT_ID) z on x.product_Id = z.PRODUCT_ID where x.atp>0 ");
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), FacilityProductCountVo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
		
	protected static void makeCondition(StringBuffer sql,String name,String Num,String value,String flag){
		if(value == null || value.equals("")) return ;
		switch(Integer.parseInt(Num)){
		case 1:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") like '%"+value.toUpperCase()+"%'");
			}else{
				sql.append(" and "+name+" like '%"+value+"%'");
			}
			break;
		case 2:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") like '"+value.toUpperCase()+"%'");
			}else{
				sql.append(" and "+name+" like '"+value+"%'");
			}
			break;
		case 3:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") ='"+value.toUpperCase()+"'");
			}else{
				sql.append(" and "+name+"='"+value+"'");
			}
			break;
		}
	}
	
	/**
	 * 查询所有的仓库
	 * @return
	 */	
	public static Map<String,Object> getAllFacility(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = WarehouseCommon.queryAllFacility(request, response);
		List<FacilityVo> list = (List<FacilityVo>) result.get("result");
		Iterator<FacilityVo> itr = list.iterator();
		String facilityId = "";
		try {
			facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);		
			while(itr.hasNext()){
				FacilityVo facilityVo = itr.next();
				if(facilityId.equals(facilityVo.getFacilityId())){
					itr.remove();
					break;
				}
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return result;
	}
		
	
	/**
	 * 库存转运
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> inventoryTransShipment(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String partyId = WarehouseCommon.getPartyIdByRequest(request);
		String userLoginId = WarehouseCommon.getUserLoginIdByRequest(request);
		try{
			TransactionUtil.begin();
			List<TransferInventoryModel> params = makeTransferParams(request,TransferInventoryModel.class);
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select products that need to be transferred");
			}				
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			String facilityName = WarehouseCommon.getFacilityNameByFacilityId(facilityId, delegator);
			
			List list = new ArrayList();

			for(TransferInventoryModel transferInventoryDto : params){
				String facilityTo = transferInventoryDto.getFacilityTo();
				String productId = transferInventoryDto.getProductId();
				String transferNums = transferInventoryDto.getTransferNum();	
				
				Timestamp timeStamp = new Timestamp(new Date().getTime());
				String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(facilityTo, delegator);
				
				String regex = "^[0-9]*[1-9][0-9]*$";
				Pattern p=Pattern.compile(regex);
				Matcher m=p.matcher(transferNums);
				if(!m.matches()){
					throw new ServiceException("The number of transshipment must be integers");
				}
				int actualNum = 0;
				int transferNum = Integer.parseInt(transferNums);
				int totalNums = transferNum;
				List<GenericValue> inventoryItems = getProductNumFromWarehouse(productId, facilityId, delegator);
				if(inventoryItems == null || inventoryItems.size() == 0){
					throw new ServiceException(productId +": quantity not sufficient");
				}
				for(GenericValue inventoryItem : inventoryItems){
					if("SERIALIZED_INV_ITEM".equals(inventoryItem.getString("inventoryItemTypeId"))){			
						if(transferNum == 0){
							break;
						}
						inventoryItem.set("statusId", null);
						inventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
						delegator.store(inventoryItem);
						actualNum += 1;
						transferNum -= 1;
						String inventoryItemId = inventoryItem.getString("inventoryItemId");					
						GenericValue newInventoryItem = delegator.makeValue("InventoryItem");
						newInventoryItem = inventoryItem;
						newInventoryItem.set("inventoryItemId", delegator.getNextSeqId("InventoryItem"));
						newInventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
						newInventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
						newInventoryItem.set("facilityId", facilityTo);
						newInventoryItem.set("parentInventoryItemId", inventoryItemId);
						newInventoryItem.set("statusId", "IXF_REQUESTED");
						list.add(newInventoryItem);
						
						GenericValue InventoryItemValueHistory = delegator.makeValue("InventoryItemValueHistory");
						InventoryItemValueHistory.set("inventoryItemValueHistId", delegator.getNextSeqId("InventoryItemValueHistory"));
						InventoryItemValueHistory.set("inventoryItemId", newInventoryItem.getString("inventoryItemId"));
						InventoryItemValueHistory.set("dateTime", timeStamp);
						InventoryItemValueHistory.set("unitCost", newInventoryItem.getBigDecimal("unitCost"));
						InventoryItemValueHistory.set("setByUserLogin", userLoginId);
						list.add(InventoryItemValueHistory);
						
						GenericValue inventoryTransfer = delegator.makeValue("InventoryTransfer");
						inventoryTransfer.set("inventoryTransferId", delegator.getNextSeqId("InventoryTransfer"));
						inventoryTransfer.set("statusId", "IXF_REQUESTED");
						inventoryTransfer.set("inventoryItemId", newInventoryItem.getString("inventoryItemId"));
						inventoryTransfer.set("facilityId", facilityId);
						inventoryTransfer.set("facilityIdTo", facilityTo);
						inventoryTransfer.set("sendDate", timeStamp);
						inventoryTransfer.set("transferNumber", new BigDecimal(1));
						list.add(inventoryTransfer);
						
						GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
						inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
						inventoryItemDetail.set("effectiveDate", timeStamp);							
						inventoryItemDetail.set("inventoryItemId", inventoryItemId);
						inventoryItemDetail.set("availableToPromiseDiff", new BigDecimal(1).negate());
						//inventoryItemDetail.set("quantityOnHandDiff", tranferNum.negate());
						inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
						inventoryItemDetail.set("qoh",new BigDecimal(1));
						inventoryItemDetail.set("atp", BigDecimal.ZERO);
						inventoryItemDetail.set("usageType", UsageTypeEnum.TransferRequest.getName());							
						inventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
						inventoryItemDetail.set("fromWarehouse", facilityName);
						inventoryItemDetail.set("toWarehouse", facilityToName);
						inventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
						list.add(inventoryItemDetail);
					}else{
						BigDecimal QTY = inventoryItem.getBigDecimal("quantityOnHandTotal");
						BigDecimal ATP = inventoryItem.getBigDecimal("availableToPromiseTotal");
						if(QTY.intValue() > 0 && ATP.intValue() > 0){
							if(transferNum > ATP.intValue()){
								actualNum += ATP.intValue();
								transferNum -= ATP.intValue();
								makeValue(inventoryItem, QTY, ATP, ATP,list, delegator, facilityId, facilityTo, timeStamp, partyId, userLoginId, facilityName, facilityToName);
							}else{
								actualNum += transferNum;
								makeValue(inventoryItem, QTY, ATP, new BigDecimal(transferNum),list, delegator, facilityId, facilityTo, timeStamp, partyId, userLoginId, facilityName, facilityToName);;
								break;
							}
						}
					}					
				}
				if(actualNum != totalNums){
					throw new ServiceException("Someone has already operated this batch of goods before you, please refresh your retry");
				}				
			}
			delegator.storeAll(list);
			TransactionUtil.commit();
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(ServiceException se){
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
	 * 确认或者取消转运
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map completeOrCancelledTransfer(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		try{
			List<ChangeTransferStatusModel> params = makeTransferParams(request,ChangeTransferStatusModel.class);
			List list = new ArrayList();
			Timestamp timeStamp = new Timestamp(new Date().getTime());
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select the record to operate");
			}
			String partyId = WarehouseCommon.getPartyIdByRequest(request);
			String userLoginId = WarehouseCommon.getUserLoginIdByRequest(request);
			for(ChangeTransferStatusModel changeTransferStatusModel : params){
				String transferId = changeTransferStatusModel.getTransferId();
				String statusId = changeTransferStatusModel.getStatusId();
				String comments = changeTransferStatusModel.getComments();
				
				GenericValue inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId", transferId));
				inventoryTransfer.set("statusId", statusId);
				inventoryTransfer.set("comments", comments);
				inventoryTransfer.set("receiveDate", timeStamp);
				list.add(inventoryTransfer);
				
				String facilityName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityId"), delegator);
				String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityIdTo"), delegator);
				
				GenericValue inventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryTransfer.getString("inventoryItemId")));
				String oldInventoryItemId = inventoryItem.getString("parentInventoryItemId");
				GenericValue oldInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", oldInventoryItemId));	
				BigDecimal transferNumber = inventoryItem.getBigDecimal("quantityOnHandTotal");
				int newQoh = transferNumber.intValue();
				if(transferNumber.intValue() == 0){
					transferNumber = inventoryTransfer.getBigDecimal("transferNumber");
				}
				if("SERIALIZED_INV_ITEM".equals(inventoryItem.getString("inventoryItemTypeId"))){
					GenericValue parentInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryItem.getString("parentInventoryItemId")));
					if(inventoryItem.getString("parentInventoryItemId") == null){
						inventoryItem.set("statusId", "INV_AVAILABLE");
						if("IXF_COMPLETE".equals(statusId)){
							inventoryItem.set("facilityId", inventoryTransfer.getString("facilityIdTo"));
							inventoryItem.set("availableToPromiseTotal", new BigDecimal(1));
							inventoryItem.set("quantityOnHandTotal", new BigDecimal(1));
							inventoryItem.set("datetimeReceived",timeStamp);
							inventoryItem.set("createdStamp",timeStamp);
						}else{
							inventoryItem.set("availableToPromiseTotal", new BigDecimal(1));
						}
					}else{
						if("IXF_COMPLETE".equals(statusId)){
							inventoryItem.set("statusId", "INV_AVAILABLE");
							inventoryItem.set("availableToPromiseTotal", new BigDecimal(1));
							inventoryItem.set("createdStamp",timeStamp);
							inventoryItem.set("quantityOnHandTotal", new BigDecimal(1));
							inventoryItem.set("datetimeReceived",timeStamp);
							
							parentInventoryItem.set("statusId", "INV_PROMISED");
							parentInventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);

						}else{
							inventoryItem.set("statusId", statusId);
							inventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
							parentInventoryItem.set("statusId", "INV_AVAILABLE");
							parentInventoryItem.set("availableToPromiseTotal", new BigDecimal(1));
							
						}
						delegator.store(parentInventoryItem);
					}
					delegator.store(inventoryItem);
					if("IXF_COMPLETE".equals(statusId)){
						//增加老爸的记录
						WarehouseCommon.insertInventoryItemDetail(delegator, timeStamp, parentInventoryItem.getString("inventoryItemId"), BigDecimal.ZERO, transferNumber.negate(), BigDecimal.ZERO, BigDecimal.ZERO, UsageTypeEnum.TransferCompleted.getName(), WarehouseCommon.getOperator(request), facilityName, facilityToName, transferId,null,null);
						//增加儿子的记录
						WarehouseCommon.insertInventoryItemDetail(delegator, timeStamp, inventoryItem.getString("inventoryItemId"), transferNumber, transferNumber, transferNumber, transferNumber, UsageTypeEnum.TransferCompleted.getName(), WarehouseCommon.getOperator(request), facilityName, facilityToName, transferId,null,null);
					}else
						//增加老爸的记录
						WarehouseCommon.insertInventoryItemDetail(delegator, timeStamp, parentInventoryItem.getString("inventoryItemId"), transferNumber, BigDecimal.ZERO, new BigDecimal(1), new BigDecimal(1), UsageTypeEnum.TransferCanceled.getName(), WarehouseCommon.getOperator(request), facilityName, facilityToName, transferId,null,null);
				}else{
					inventoryItem.set("statusId", statusId);
					inventoryItem.set("createdStamp",timeStamp);
					if("IXF_COMPLETE".equals(statusId)){
						inventoryItem.set("quantityOnHandTotal", transferNumber);
						inventoryItem.set("availableToPromiseTotal", transferNumber);
						inventoryItem.set("datetimeReceived",timeStamp);
						inventoryItem.set("facilityId", inventoryTransfer.getString("facilityIdTo"));
					}else{
						inventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
					}
					delegator.store(inventoryItem);
					
					if(newQoh != 0){
						GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
						inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
						inventoryItemDetail.set("effectiveDate", timeStamp);							
						inventoryItemDetail.set("inventoryItemId", inventoryItem.getString("inventoryItemId"));
						if("IXF_COMPLETE".equals(statusId)){
							inventoryItemDetail.set("availableToPromiseDiff", transferNumber);
							inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
							inventoryItemDetail.set("qoh",transferNumber);
							inventoryItemDetail.set("atp",transferNumber);
						}else{
							inventoryItemDetail.set("availableToPromiseDiff", BigDecimal.ZERO);
							inventoryItemDetail.set("quantityOnHandDiff", transferNumber.negate());
							inventoryItemDetail.set("qoh",BigDecimal.ZERO);
							inventoryItemDetail.set("atp",BigDecimal.ZERO);
						}
						if("IXF_COMPLETE".equals(statusId)){
							inventoryItemDetail.set("usageType", UsageTypeEnum.TransferCompleted.getName());	
						}else{
							inventoryItemDetail.set("usageType", UsageTypeEnum.TransferCanceled.getName());	
						}
						inventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
						inventoryItemDetail.set("fromWarehouse", facilityName);
						inventoryItemDetail.set("toWarehouse", facilityToName);
						inventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
						list.add(inventoryItemDetail);
					}else{
						if("IXF_COMPLETE".equals(statusId)){
							GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
							inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
							inventoryItemDetail.set("effectiveDate", timeStamp);							
							inventoryItemDetail.set("inventoryItemId", inventoryItem.getString("inventoryItemId"));
							inventoryItemDetail.set("availableToPromiseDiff", transferNumber);
							inventoryItemDetail.set("quantityOnHandDiff", transferNumber);
							inventoryItemDetail.set("qoh",transferNumber);
							inventoryItemDetail.set("atp",transferNumber);
							inventoryItemDetail.set("usageType", UsageTypeEnum.TransferCompleted.getName());
							inventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
							inventoryItemDetail.set("fromWarehouse", facilityName);
							inventoryItemDetail.set("toWarehouse", facilityToName);
							inventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
							list.add(inventoryItemDetail);
											
							oldInventoryItem.set("quantityOnHandTotal", oldInventoryItem.getBigDecimal("quantityOnHandTotal").subtract(transferNumber));
							delegator.store(oldInventoryItem);
							GenericValue oldInventoryItemDetail = delegator.makeValue("InventoryItemDetail");
							oldInventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
							oldInventoryItemDetail.set("effectiveDate", timeStamp);							
							oldInventoryItemDetail.set("inventoryItemId", oldInventoryItemId);
							oldInventoryItemDetail.set("availableToPromiseDiff",BigDecimal.ZERO);
							oldInventoryItemDetail.set("quantityOnHandDiff", transferNumber.negate());
							oldInventoryItemDetail.set("qoh",oldInventoryItem.getBigDecimal("quantityOnHandTotal"));
							oldInventoryItemDetail.set("atp", oldInventoryItem.getBigDecimal("availableToPromiseTotal"));
							oldInventoryItemDetail.set("usageType", UsageTypeEnum.TransferCompleted.getName());							
							oldInventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
							oldInventoryItemDetail.set("fromWarehouse", facilityName);
							oldInventoryItemDetail.set("toWarehouse", facilityToName);
							oldInventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
							list.add(oldInventoryItemDetail);
						}
					}
					if("IXF_CANCELLED".equals(statusId)){
						oldInventoryItem.set("availableToPromiseTotal", oldInventoryItem.getBigDecimal("availableToPromiseTotal").add(transferNumber));
						//oldInventoryItem.set("quantityOnHandTotal", oldInventoryItem.getBigDecimal("quantityOnHandTotal"));
						delegator.store(oldInventoryItem);
						
						GenericValue oldInventoryItemDetail = delegator.makeValue("InventoryItemDetail");
						oldInventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
						oldInventoryItemDetail.set("effectiveDate", timeStamp);							
						oldInventoryItemDetail.set("inventoryItemId", oldInventoryItemId);
						oldInventoryItemDetail.set("availableToPromiseDiff",transferNumber);
						if(newQoh != 0){
							oldInventoryItemDetail.set("quantityOnHandDiff", transferNumber);
						}else{
							oldInventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
						}
						oldInventoryItemDetail.set("qoh",oldInventoryItem.getBigDecimal("quantityOnHandTotal"));
						oldInventoryItemDetail.set("atp", oldInventoryItem.getBigDecimal("availableToPromiseTotal"));
						oldInventoryItemDetail.set("usageType", UsageTypeEnum.TransferCanceled.getName());							
						oldInventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
						oldInventoryItemDetail.set("fromWarehouse", facilityName);
						oldInventoryItemDetail.set("toWarehouse", facilityToName);
						oldInventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
						list.add(oldInventoryItemDetail);
					}
				}
			}
			delegator.storeAll(list);
			TransactionUtil.commit();
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
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
	 * 查询指定的转运记录
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map queryTransferData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = makeParams(request);
		
		String type = json.getString("type");
		String facility = json.getString("facilityId");
		String productId = json.getString("productId");
		String model = json.getString("model");
		String sendDate = json.getString("sendDate");
		String statusId = json.getString("statusId");
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
			StringBuffer sql = new StringBuffer("SELECT b.SERIAL_NUMBER AS imei,a.COMMENTS as comments,e.FACILITY_ID as facilityId,e.FACILITY_NAME as facilityName,a.TRANSFER_NUMBER as transferNumber,a.SEND_DATE as sendDate,a.STATUS_ID as statusId, "
					+ " a.INVENTORY_TRANSFER_ID as inventoryTransferId,a.INVENTORY_ITEM_ID as inventoryItemId,b.PRODUCT_ID as productId,b.QUANTITY_ON_HAND_TOTAL as quantityOnHandTotal,c.INTERNAL_NAME as model,d.description as description"
					+ " FROM INVENTORY_TRANSFER a LEFT JOIN INVENTORY_ITEM b ON a.INVENTORY_ITEM_ID=b.INVENTORY_ITEM_ID LEFT JOIN PRODUCT c ON b.PRODUCT_ID=c.PRODUCT_ID LEFT JOIN"
					+ "(SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as description "
					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) d on c.PRODUCT_ID=d.PRODUCT_ID ");		
			
			if("sent".equals(type)){
				sql.append(" LEFT JOIN FACILITY e ON a.FACILITY_ID_TO=e.FACILITY_ID WHERE 1=1  AND a.FACILITY_ID='"+facilityId+"' ");
				if(facility != null && !facility.equals("")){
					sql.append(" AND a.FACILITY_ID_TO='"+facility+"' ");
				}
			}else{
				sql.append(" LEFT JOIN FACILITY e ON a.FACILITY_ID=e.FACILITY_ID WHERE 1=1 AND a.FACILITY_ID_TO='"+facilityId+"' ");
				if(facility != null && !facility.equals("")){
					sql.append(" AND a.FACILITY_ID='"+facility+"' ");
				}
			}
			if(productId != null && !productId.equals("")){
				sql.append(" AND b.PRODUCT_ID like '%"+productId+"%' ");
			}
			if(model != null && !model.equals("")){
				sql.append(" AND c.INTERNAL_NAME like '%"+model+"%' ");
			}
			if(sendDate != null && !sendDate.equals("")){
				sql.append(" AND a.SEND_DATE like '%"+sendDate+"%' ");
			}
			if(statusId != null && !statusId.equals("")){
				sql.append(" AND a.STATUS_ID ='"+statusId+"' order by sendDate desc");
			}else{
				sql.append(" ORDER BY a.STATUS_ID DESC , sendDate desc");
			}
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), TranferInventoryVo.class, delegator);
			List<TranferInventoryVo> tranferInventoryVos = (List<TranferInventoryVo>) result.get("result");
			for(TranferInventoryVo tranferInventoryVo : tranferInventoryVos){
				tranferInventoryVo.setSendDate(DateUtils.getLocalizedDate(tranferInventoryVo.getSendDate(), request));
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatternew = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i= 0;i<tranferInventoryVos.size();i++){
				TranferInventoryVo vo = (TranferInventoryVo)tranferInventoryVos.get(i);
				String recei = vo.getSendDate();
				Date currentTime_2;
				try {
					currentTime_2 = formatter.parse(recei);
				} catch (Exception e) {
					continue;
				}
				vo.setSendDate(formatternew.format(currentTime_2));
			}
			
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");	
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static JSONObject makeParams(HttpServletRequest request){		
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
		String productPriceList =  jsonStringBuffer.toString();
		JSONObject json = JSON.parseObject(productPriceList);
		return json;
	}
	
	public static <T> List<T> makeTransferParams(HttpServletRequest request,Class<T> clazz){		
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
		String param =  jsonStringBuffer.toString();
		List<T> list = JSON.parseArray(param, clazz);
		return list;
	}
	
	public static List<GenericValue> getProductNumFromWarehouse(String productId,String facilityId,Delegator delegator) throws GenericEntityException{
		List<String> order = new ArrayList<String>();
		order.add("datetimeReceived");
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
		exprs.add(EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
		return delegator.findByCondition("InventoryItem", EntityCondition.makeCondition(exprs), null, order);
	}
	
	
	
	/**
	 * 待捡货
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> toPickUpInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String partyId = WarehouseCommon.getPartyIdByRequest(request);
		String userLoginId = WarehouseCommon.getUserLoginIdByRequest(request);
		StringBuffer trackingId = new StringBuffer("");
		try{
			TransactionUtil.begin();
			List<TransferInventoryModel> params = makeTransferParams(request,TransferInventoryModel.class);
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select products that need to be transferred");
			}	
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			String facilityName = WarehouseCommon.getFacilityNameByFacilityId(facilityId, delegator);
			Timestamp timeStamp = new Timestamp(new Date().getTime());
			//根据所到仓库分类			
			Set<Entry<String,List<TransferInventoryModel>>> set = makeSetParam(params);
			for(Entry<String,List<TransferInventoryModel>> entry : set){
				List<TransferInventoryModel> param = entry.getValue();
				//生成转运单记录
				String transhipmentShippingBillId = WarehouseCommon.insertTranshipmentShippingBill(delegator, WarehouseConstant.PICKING, timeStamp, timeStamp, userLoginId,userLoginId,facilityId,param.get(0).getFacilityTo());
				trackingId.append(transhipmentShippingBillId+",");
				for(TransferInventoryModel transferInventoryDto : param){
					String facilityTo = transferInventoryDto.getFacilityTo();
					String productId = transferInventoryDto.getProductId();
					String transferNums = transferInventoryDto.getTransferNum();	
					
					String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(facilityTo, delegator);
					//验证正则
					checkRegExpInteger(transferNums);
					
					int actualNum = 0;
					int transferNum = Integer.parseInt(transferNums);
					int totalNums = transferNum;
					//验证产品数量是否充足
					List<GenericValue> inventoryItems = getProductNumFromWarehouse(productId, facilityId, delegator);
					int pickingNum = checkAvaToPickInventory(delegator,facilityId,productId);
					checkProductNum(inventoryItems, productId,transferNum,pickingNum);
					
					//如果是非IMEI，去除不能转运的数量
					//makeInventoryATPNum(inventoryItems, pickingNum);
					//start picking
					for(GenericValue inventoryItem : inventoryItems){
						if(transferNum == 0){
							break;
						}
						if("SERIALIZED_INV_ITEM".equals(inventoryItem.getString("inventoryItemTypeId"))){							
							//插入item记录
							String newInventoryItemId = WarehouseCommon.insertInventoryItem(delegator, WarehouseConstant.IMEI_TYPE_ID, productId, WarehouseCommon.getOwnerPartyId(delegator, facilityId), null, WarehouseConstant.IXF_PICKING, BigDecimal.ZERO, BigDecimal.ZERO,
									facilityTo, null, null, null, null, null);
							//插入transfer记录
							WarehouseCommon.insertInventoryTransfer(delegator, WarehouseConstant.IXF_PICKING, newInventoryItemId, facilityId, null, facilityTo, new BigDecimal(1), transhipmentShippingBillId);
							actualNum += 1;
							transferNum -= 1;						
						}else{
							BigDecimal QTY = inventoryItem.getBigDecimal("quantityOnHandTotal");
							BigDecimal ATP = inventoryItem.getBigDecimal("availableToPromiseTotal");
							if(QTY.intValue() > 0 && ATP.intValue() > 0){
								if(transferNum > ATP.intValue()){
									actualNum += ATP.intValue();
									transferNum -= ATP.intValue();
									//插入item记录
									String newInventoryItemId = WarehouseCommon.insertInventoryItem(delegator, WarehouseConstant.NO_IMEI_TYPE_ID, productId, WarehouseCommon.getOwnerPartyId(delegator, facilityId), null, WarehouseConstant.IXF_PICKING, BigDecimal.ZERO, BigDecimal.ZERO,
											facilityTo, inventoryItem.getString("lotId"), inventoryItem.getString("serialNumber"), inventoryItem.getString("unitCost"), inventoryItem.getString("currencyUomId"), inventoryItem.getString("inventoryItemId"));
									//插入transfer记录
									String transferId = WarehouseCommon.insertInventoryTransfer(delegator, WarehouseConstant.IXF_PICKING, newInventoryItemId, facilityId, null, facilityTo, ATP, transhipmentShippingBillId);
									//修改父itemAtp
									WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, null, BigDecimal.ZERO, null, null, null, null, null, null);
									//插入Detail日志记录
									WarehouseCommon.insertInventoryItemDetail(delegator, timeStamp, inventoryItem.getString("inventoryItemId"), ATP.negate(), BigDecimal.ZERO, QTY, BigDecimal.ZERO, UsageTypeEnum.TransferPicking.getName(), WarehouseCommon.getOperator(request), facilityName, facilityToName, transferId, null, inventoryItem.getString("statusId"));
								}else{
									actualNum += transferNum;
									//插入item记录
									String newInventoryItemId = WarehouseCommon.insertInventoryItem(delegator, WarehouseConstant.NO_IMEI_TYPE_ID, productId, WarehouseCommon.getOwnerPartyId(delegator, facilityId), null, WarehouseConstant.IXF_PICKING, BigDecimal.ZERO, BigDecimal.ZERO,
											facilityTo, inventoryItem.getString("lotId"), inventoryItem.getString("serialNumber"), inventoryItem.getString("unitCost"), inventoryItem.getString("currencyUomId"), inventoryItem.getString("inventoryItemId"));
									//插入transfer记录
									String transferId = WarehouseCommon.insertInventoryTransfer(delegator, WarehouseConstant.IXF_PICKING, newInventoryItemId, facilityId, null, facilityTo, new BigDecimal(transferNum),transhipmentShippingBillId);
									//修改父itemAtp
									WarehouseCommon.updateInventoryItemById(delegator, inventoryItem.getString("inventoryItemId"), null, null, null, null, null, null, ATP.subtract(new BigDecimal(transferNum)), null, null, null, null, null, null);
									//插入Detail日志记录
									WarehouseCommon.insertInventoryItemDetail(delegator, timeStamp, inventoryItem.getString("inventoryItemId"), new BigDecimal(transferNum).negate(), BigDecimal.ZERO, QTY, ATP.subtract(new BigDecimal(transferNum)), UsageTypeEnum.TransferPicking.getName(), WarehouseCommon.getOperator(request), facilityName, facilityToName, transferId, null, inventoryItem.getString("statusId"));
									break;
								}
							}
						}					
					}
					if(actualNum != totalNums){
						throw new ServiceException("Someone has already operated this batch of goods before you, please refresh your retry");
					}				
				}
			}
			TransactionUtil.commit();
			result.put("trackingId", trackingId.substring(0, trackingId.length()-1));
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(ServiceException se){
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
	 * 查询单号
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryTranshipmentShippingBill(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String trackingId = json.getString("trackingId");
		String receivedBy = json.getString("receivedBy");
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
			StringBuffer sql = new StringBuffer("SELECT TRANSHIPMENT_SHIPPING_BILL_ID as id,STATUS as status,FACILITY_NAME as toWarehouse, CREATE_TIME as createTime,LAST_UPDATE_TIME as lastUpdateTime,CREATE_USER_lOGIN_ID as createUserLoginId,LAST_UPDATE_USER_lOGIN_ID as lastUpdateUserLoginid FROM TRANSHIPMENT_SHIPPING_BILL X LEFT JOIN FACILITY Y ON X.TO_WAREHOUSE=Y.FACILITY_ID WHERE X.STATUS='0' AND FROM_WAREHOUSE='"+facilityId+"'");
			if(trackingId != null && !trackingId.equals("")){
				sql.append(" AND TRANSHIPMENT_SHIPPING_BILL_ID='"+trackingId+"'");
			}
			if(receivedBy != null && !receivedBy.equals("")){
				sql.append(" AND TO_WAREHOUSE='"+receivedBy+"'");
			}

			sql.append(" ORDER BY STATUS");
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), TranshipmentShippingBillVo.class, delegator);
			List<TranshipmentShippingBillVo> list = (List<TranshipmentShippingBillVo>) result.get("result");
			if(list != null && list.size() > 0){
				for(TranshipmentShippingBillVo transhipmentShippingBillVo : list){
					transhipmentShippingBillVo.setCreateTime(DateUtils.getLocalizedDate(transhipmentShippingBillVo.getCreateTime(), request));
					transhipmentShippingBillVo.setLastUpdateTime(DateUtils.getLocalizedDate(transhipmentShippingBillVo.getLastUpdateTime(), request));
				}
			}
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
	 * 取消单号
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> cancelTranshipmentShippingBill(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String trackingIds = json.getString("trackingId");
		try{
			TransactionUtil.begin();
			if(trackingIds == null || trackingIds.equals("")){
				throw new ServiceException("Please enter the transhipment number");
			}
			String[] ids = trackingIds.split(",");
			Timestamp timestamp = new Timestamp(new Date().getTime());
			String operator = WarehouseCommon.getOperator(request);
			for(String id : ids){
				//更新TranshipmentShippingBill表
				WarehouseCommon.updateTranshipmentShippingBillById(delegator, id, WarehouseConstant.CANCELLED, null, new Timestamp(new Date().getTime()), null,WarehouseCommon.getUserLoginIdByRequest(request), null, null);
				//获取所有transfer
				List<EntityExpr> exprs = FastList.newInstance();
				exprs.add(EntityCondition.makeCondition("transhipmentShippingBillId", EntityOperator.EQUALS, id));
				List<GenericValue> inventoryTransfers = delegator.findByCondition("InventoryTransfer", EntityCondition.makeCondition(exprs), null, null);
				for(GenericValue inventoryTransfer : inventoryTransfers){
					String inventoryTransferId = inventoryTransfer.getString("inventoryTransferId");
					String inventoryItemId = inventoryTransfer.getString("inventoryItemId");
					BigDecimal transferNumber = inventoryTransfer.getBigDecimal("transferNumber");
					String facilityName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityId"), delegator);
					String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityIdTo"), delegator);
					//更新inventoryTransfer表
					WarehouseCommon.updateInventoryTransferById(delegator, inventoryTransferId, WarehouseConstant.IXF_CANCELLED, null, null, null, null, null, null);
					//更新inventoryItem表
					WarehouseCommon.updateInventoryItemById(delegator, inventoryItemId, null, null, null, null, WarehouseConstant.IXF_CANCELLED, null, null, null, null, null, null, null, null);
					//如果是非IMEI产品，需要归还ATP
					GenericValue newInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryItemId));
					String parentInventoryItemId = newInventoryItem.getString("parentInventoryItemId");
					GenericValue parentInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", parentInventoryItemId));					
					if(parentInventoryItem != null){
						BigDecimal ATP = parentInventoryItem.getBigDecimal("availableToPromiseTotal");
						BigDecimal QTY = parentInventoryItem.getBigDecimal("quantityOnHandTotal");
						//回滚ATP的值
						WarehouseCommon.updateInventoryItemById(delegator, parentInventoryItemId, null, null, null, null, null, null, ATP.add(transferNumber), null, null, null, null, null, null);
						//记录日志
						WarehouseCommon.insertInventoryItemDetail(delegator, timestamp, parentInventoryItemId, transferNumber, BigDecimal.ZERO, QTY, ATP.add(transferNumber), UsageTypeEnum.TransferCanceled.getName(), operator, facilityName, facilityToName, inventoryTransferId, null, parentInventoryItem.getString("statusId"));
					}
				}
			}			
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			TransactionUtil.commit();
		} catch(Exception e){
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 验证EAN是否一致
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> checkEANByProductId(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String EAN = json.getString("EAN");
		String productId = json.getString("productId");
		try{
			checkEAN(delegator, EAN, productId);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据单号进行捡货
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> PickingByTranshipmentShippingBillId(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		JSONObject json = InventoryTransfer.makeParams(request);
		String trackingIds = json.getString("trackingId");
		try{
			if(trackingIds == null || trackingIds.equals("")){
				throw new ServiceException("Please enter the transhipment number");
			}
			String[] ids = trackingIds.split(",");
			//拼接参数
			StringBuffer tId = new StringBuffer();
			for(int i=0;i<ids.length;i++){
				if(i == ids.length-1){
					tId.append("'"+ids[i]+"'");
				}else{
					tId.append("'"+ids[i]+"',");
				}
			}
			String sql = "SELECT K.FACILITY_NAME AS toWarehouse,X.TRANSHIPMENT_SHIPPING_BILL_ID AS id,Y.PRODUCT_ID AS productId,Z.BRAND_NAME as brandName,Z.INTERNAL_NAME as model,G.DESRIPTION AS descritpion,Y.INVENTORY_ITEM_TYPE_ID as type,SUM(X.TRANSFER_NUMBER) as transNum,	(select count(1) from TRANSHIPMENT_SHIPPING_BILL_DETAIL as tsbd where tsbd.TRANSHIPMENT_SHIPPING_BILL_ID=X.TRANSHIPMENT_SHIPPING_BILL_ID and tsbd.PRODUCT_ID=Y.PRODUCT_ID)   AS scanNum "
					+ " FROM(SELECT * FROM INVENTORY_TRANSFER WHERE TRANSHIPMENT_SHIPPING_BILL_ID in ("+tId.toString()+")) X LEFT JOIN INVENTORY_ITEM Y ON X.INVENTORY_ITEM_ID = Y.INVENTORY_ITEM_ID"
					+ " LEFT JOIN PRODUCT Z ON Y.PRODUCT_ID = Z.PRODUCT_ID "
					+ " LEFT JOIN FACILITY K ON Y.FACILITY_ID = K.FACILITY_ID"
					+ " LEFT JOIN TRANSHIPMENT_SHIPPING_BILL H ON X.TRANSHIPMENT_SHIPPING_BILL_ID=H.TRANSHIPMENT_SHIPPING_BILL_ID "
					+ " LEFT JOIN (SELECT b.PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESRIPTION"
					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) G ON Y.PRODUCT_ID = G.PRODUCT_ID"
					+ " GROUP BY X.TRANSHIPMENT_SHIPPING_BILL_ID,Y.PRODUCT_ID";
			ResultSet rs = processor.executeQuery(sql);
			Map<String,List<PickingVo>> map = new HashMap<String, List<PickingVo>>();
			boolean flag = false;
			while(rs.next()){
				flag = true;
				String id = rs.getString("id");
				PickingVo pickingVo = makePickingVo(rs.getString("id"),rs.getString("toWarehouse"),rs.getString("productId"),rs.getString("brandName"),
						rs.getString("model"),rs.getString("descritpion"),rs.getString("type"),rs.getString("transNum"),rs.getString("scanNum"));
				if(map.get(id) == null){
					List<PickingVo> list = new ArrayList<PickingVo>();				
					list.add(pickingVo);
					map.put(id, list);
				}else{
					map.get(id).add(pickingVo);
				}
			}
			List<PickingByWarehouseVo> results = new ArrayList<PickingByWarehouseVo>();
			if(flag){
				Set<Entry<String, List<PickingVo>>> set = map.entrySet();
				for(Entry<String, List<PickingVo>> entry :set){
					String transNum = entry.getKey();
					List<PickingVo> list = entry.getValue();
					String warehouse = list.get(0).getToWarehouse();
					PickingByWarehouseVo pickingByWarehouseVo = makePickByWarehouse(warehouse,transNum,list);
					results.add(pickingByWarehouseVo);
				}
			}
			result.put("result", results);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch(ServiceException se){
			result.put("resultCode", -1);
			result.put("resultMsg", se.getMessage());
			se.printStackTrace();
		} catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}

	
//	public static Map<String,Object> PickingByTranshipmentShippingBillId(HttpServletRequest request,HttpServletResponse response){
//		Map<String, Object> result = new HashMap<String, Object>();
//		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
//		//获取JDBC连接
//		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
//		SQLProcessor processor = new SQLProcessor(helperInfo);
//		JSONObject json = InventoryTransfer.makeParams(request);
//		String trackingIds = json.getString("trackingId");
//		try{
//			if(trackingIds == null || trackingIds.equals("")){
//				throw new ServiceException("Please enter the transhipment number");
//			}
//			String[] ids = trackingIds.split(",");
//			//拼接参数
//			StringBuffer tId = new StringBuffer();
//			for(int i=0;i<ids.length;i++){
//				if(i == ids.length-1){
//					tId.append("'"+ids[i]+"'");
//				}else{
//					tId.append("'"+ids[i]+"',");
//				}
//			}
//			String sql = "SELECT K.FACILITY_NAME AS toWarehouse,X.TRANSHIPMENT_SHIPPING_BILL_ID AS id,Y.PRODUCT_ID AS productId,Z.BRAND_NAME as brandName,Z.INTERNAL_NAME as model,G.DESRIPTION AS descritpion,Y.INVENTORY_ITEM_TYPE_ID as type,SUM(X.TRANSFER_NUMBER) as transNum"
//					+ " FROM(SELECT * FROM INVENTORY_TRANSFER WHERE TRANSHIPMENT_SHIPPING_BILL_ID in ("+tId.toString()+")) X LEFT JOIN INVENTORY_ITEM Y ON X.INVENTORY_ITEM_ID = Y.INVENTORY_ITEM_ID"
//					+ " LEFT JOIN PRODUCT Z ON Y.PRODUCT_ID = Z.PRODUCT_ID "
//					+ " LEFT JOIN FACILITY K ON Y.FACILITY_ID = K.FACILITY_ID"
//					+ " LEFT JOIN TRANSHIPMENT_SHIPPING_BILL H ON X.TRANSHIPMENT_SHIPPING_BILL_ID=H.TRANSHIPMENT_SHIPPING_BILL_ID"
//					+ " LEFT JOIN (SELECT b.PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESRIPTION"
//					+ " FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) G ON Y.PRODUCT_ID = G.PRODUCT_ID"
//					+ " GROUP BY X.TRANSHIPMENT_SHIPPING_BILL_ID,Y.PRODUCT_ID";
//			ResultSet rs = processor.executeQuery(sql);
//			Map<String,List<PickingVo>> map = new HashMap<String, List<PickingVo>>();
//			boolean flag = false;
//			while(rs.next()){
//				flag = true;
//				String id = rs.getString("id");
//				PickingVo pickingVo = makePickingVo(rs.getString("id"),rs.getString("toWarehouse"),rs.getString("productId"),rs.getString("brandName"),
//						rs.getString("model"),rs.getString("descritpion"),rs.getString("type"),rs.getString("transNum"));
//				if(map.get(id) == null){
//					List<PickingVo> list = new ArrayList<PickingVo>();				
//					list.add(pickingVo);
//					map.put(id, list);
//				}else{
//					map.get(id).add(pickingVo);
//				}
//			}
//			List<PickingByWarehouseVo> results = new ArrayList<PickingByWarehouseVo>();
//			if(flag){
//				Set<Entry<String, List<PickingVo>>> set = map.entrySet();
//				for(Entry<String, List<PickingVo>> entry :set){
//					String transNum = entry.getKey();
//					List<PickingVo> list = entry.getValue();
//					String warehouse = list.get(0).getToWarehouse();
//					PickingByWarehouseVo pickingByWarehouseVo = makePickByWarehouse(warehouse,transNum,list);
//					results.add(pickingByWarehouseVo);
//				}
//			}
//			result.put("result", results);
//			result.put("resultCode", 1);
//			result.put("resultMsg", "Successful operation");
//		} catch(ServiceException se){
//			result.put("resultCode", -1);
//			result.put("resultMsg", se.getMessage());
//			se.printStackTrace();
//		} catch(Exception e){
//			result.put("resultCode", -1);
//			result.put("resultMsg", "System exception, please contact the administrator");
//			e.printStackTrace();
//		}
//		return result;
//	}
	/**
	 * 开始捡货
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> startPicking(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		try{
			TransactionUtil.begin();
			List<StartPickingDto> params = makeTransferParams(request, StartPickingDto.class);
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			if(params == null || params.size() == 0){
				throw new ServiceException("Please select your products");
			}
			Timestamp timestamp = new Timestamp(new Date().getTime());
			String operator = WarehouseCommon.getOperator(request);
			//add IMEI
			addIMEIIsAva(params,delegator);
			//check IMEI
			List<IMEIInventoryCheckingVo> list = checkIMEIIsAva(params,delegator,facilityId);
			if(list.size() > 0){
				result.put("result", list);
				result.put("resultCode", -2);
				result.put("resultMsg", "There is no such IMEI product in the warehouse!");
				return result;
			}
			for(StartPickingDto startPickingDto : params){
				//获取转运单
				String transhipmentShippingBillId = startPickingDto.getTranshipmentShippingBillId();
				//获取此单所有产品
				List<PickingDto> pickingDtos = startPickingDto.getPickingDtos();
				/*if(pickingDtos == null || pickingDtos.size() == 0){
					throw new ServiceException("This port of transshipment does not have any products to be transferred, please check!");
				}*/
				//开始
				for(PickingDto pickingDto : pickingDtos){
					//区别是IMEI还是非IMEI
					String isIMEI = pickingDto.getIsIMEI();
					//获取EAN
					String MyEAN = pickingDto.getEAN();
					//获取单号与产品ID对应的转运记录
					List<TranferInventoryVo> tranferInventoryVos = queryTransferByProductAndTid(delegator, transhipmentShippingBillId, pickingDto.getProductId());
					if("Y".equals(isIMEI)){
						//imei产品
						String imeiStr = pickingDto.getImei();
						String[] imeis = imeiStr.split(",");
						if(tranferInventoryVos.size() < imeis.length){
							throw new ServiceException("IMEI number Cannot be greater than the number of transshipment!");
						}
						for(int i=0;i<imeis.length;i++){					
							String inventoryItemId = tranferInventoryVos.get(i).getInventoryItemId();
							String inventoryTransferId = tranferInventoryVos.get(i).getInventoryTransferId();
							String transferProductId = tranferInventoryVos.get(i).getProductId();
							
							String imei = imeis[i];
							//将transfer中的inventoryItem表中增加IMEI号，将父记录ATP-1，记录日志
							GenericValue inventoryItem = WarehouseCommon.findInventoryItemByIMEI(delegator, imei,facilityId);
							/*if(inventoryItem == null){
								throw new ServiceException("There is no such IMEI["+imei+"] product["+transferProductId+"] in the warehouse or this product is not available!");
							}*/
							String productId = inventoryItem.getString("productId");
							String parentInventoryItemId = inventoryItem.getString("inventoryItemId");
							
							/*if(!transferProductId.equals(productId)){
								throw new ServiceException("This IMEI : "+imei+" is not a product to be transferred");
							}*/
							//验证EAN码是否一致
							if(MyEAN != null && !MyEAN.equals("")){
								checkEAN(delegator, MyEAN, productId);
							}
							//更新父记录
							inventoryItem.set("statusId", null);
							inventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
							delegator.store(inventoryItem);
							//更新transfer记录
							GenericValue inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId", inventoryTransferId));						
							inventoryTransfer.set("statusId", "IXF_REQUESTED");
							inventoryTransfer.set("sendDate", timestamp);
							String facilityName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityId"), delegator);
							String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityIdTo"), delegator);
							delegator.store(inventoryTransfer);
							//父记录插入日志
							WarehouseCommon.insertInventoryItemDetail(delegator, timestamp, parentInventoryItemId, new BigDecimal(1).negate(), BigDecimal.ZERO, new BigDecimal(1), BigDecimal.ZERO, UsageTypeEnum.TransferRequest.getName(), operator, facilityName, facilityToName, inventoryTransferId,null,null);
							//更新子记录				
							GenericValue newInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryItemId));
							newInventoryItem.set("statusId", "IXF_REQUESTED");
							newInventoryItem.set("serialNumber", imei);
							newInventoryItem.set("lotId", inventoryItem.getString("lotId"));
							newInventoryItem.set("unitCost", inventoryItem.getString("unitCost"));
							newInventoryItem.set("currencyUomId", inventoryItem.getString("currencyUomId"));
							newInventoryItem.set("createdTxStamp", timestamp);
							newInventoryItem.set("parentInventoryItemId", parentInventoryItemId);
							delegator.store(newInventoryItem);
						}
					}else{
						//非IMEI产品
						for(TranferInventoryVo tranferInventoryVo : tranferInventoryVos){
							//验证EAN码是否一致
							if(MyEAN != null && !MyEAN.equals("")){
								checkEAN(delegator, MyEAN, tranferInventoryVo.getProductId());
							}
							String inventoryItemId = tranferInventoryVo.getInventoryItemId();
							String inventoryTransferId = tranferInventoryVo.getInventoryTransferId();
							handleTransfer(delegator, inventoryItemId, timestamp, inventoryTransferId, operator);
						}
					}
				}
				//将转运单置为在途状态
				WarehouseCommon.updateTranshipmentShippingBillById(delegator, transhipmentShippingBillId, WarehouseConstant.REQUEST, null, timestamp, null, WarehouseCommon.getUserLoginIdByRequest(request), null, null);
			}				
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			TransactionUtil.commit();
		} catch(ServiceException e2){
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			result.put("resultCode", -1);
			result.put("resultMsg", e2.getMessage());
			e2.printStackTrace();
		}catch(Exception e){
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 检查IMEI是否可用
	 * @param params
	 * @throws ServiceException 
	 * @throws GenericEntityException 
	 * @throws SQLException 
	 */
	private static List<IMEIInventoryCheckingVo> checkIMEIIsAva(List<StartPickingDto> params,Delegator delegator,String facilityId) throws ServiceException, GenericEntityException, SQLException {
		List<IMEIInventoryCheckingVo> result = new ArrayList<IMEIInventoryCheckingVo>();
		for(StartPickingDto startPickingDto : params){
			//获取转运单
			String transhipmentShippingBillId = startPickingDto.getTranshipmentShippingBillId();
			List<PickingDto> pickingDtos = startPickingDto.getPickingDtos();
			if(pickingDtos == null || pickingDtos.size() == 0){
				throw new ServiceException("This port of transshipment["+transhipmentShippingBillId+"] does not have any products to be transferred, please check!");
			}
			//开始
			for(PickingDto pickingDto : pickingDtos){
				String isIMEI = pickingDto.getIsIMEI();
				//获取单号与产品ID对应的转运记录
				List<TranferInventoryVo> tranferInventoryVos = queryTransferByProductAndTid(delegator, transhipmentShippingBillId, pickingDto.getProductId());
				if("Y".equals(isIMEI)){
					//imei产品
					String imeiStr = pickingDto.getImei();
					String[] imeis = imeiStr.split(",");
					for(int i=0;i<imeis.length;i++){					
						String inventoryTransferId = tranferInventoryVos.get(i).getInventoryTransferId();
						String transferProductId = tranferInventoryVos.get(i).getProductId();
						String model = tranferInventoryVos.get(i).getModel();
						String description = tranferInventoryVos.get(i).getDescription();
						
						String imei = imeis[i];
						
						GenericValue inventoryItem = WarehouseCommon.findInventoryItemByIMEI(delegator, imei,facilityId);
						if(inventoryItem == null){
							IMEIInventoryCheckingVo iMEIInventoryCheckingVo = new IMEIInventoryCheckingVo();
							iMEIInventoryCheckingVo.setImei(imei);
							iMEIInventoryCheckingVo.setDescription("IMEI is unavailable in this warehouse!");
							result.add(iMEIInventoryCheckingVo);
						}else if(!inventoryItem.getString("productId").equals(transferProductId)){
							IMEIInventoryCheckingVo iMEIInventoryCheckingVo = new IMEIInventoryCheckingVo();
							iMEIInventoryCheckingVo.setImei(imei);
							iMEIInventoryCheckingVo.setDescription("This IMEI cannot match with the product ID to transfer.");
							result.add(iMEIInventoryCheckingVo);
						}
					}
				}
			}
		}
		return result;
	}
	private static void addIMEIIsAva(List<StartPickingDto> params,Delegator delegator) throws ServiceException, GenericEntityException, SQLException {
		List<IMEIInventoryCheckingVo> result = new ArrayList<IMEIInventoryCheckingVo>();
		for(StartPickingDto startPickingDto : params){
			//获取转运单
			String transhipmentShippingBillId = startPickingDto.getTranshipmentShippingBillId();
			
			if(transhipmentShippingBillId==null||"".equals(transhipmentShippingBillId))
				continue;
			
			StringBuffer querysql = new StringBuffer("select X.IS_ACTIVE as isIMEI,X.PRODUCT_ID as productId,GROUP_CONCAT(X.IMEI ) AS imei  from TRANSHIPMENT_SHIPPING_BILL_DETAIL as X where X.TRANSHIPMENT_SHIPPING_BILL_ID=\'"+transhipmentShippingBillId+"\'  GROUP BY X.TRANSHIPMENT_SHIPPING_BILL_ID,X.PRODUCT_ID");
			
			try {
				List<PickingDto> ret = (List<PickingDto>)EncapsulatedQueryResultsUtil.getResults_( querysql,PickingDto.class, delegator);
				List<PickingDto> pickingDtos = startPickingDto.getPickingDtos();
				if(pickingDtos==null)
					pickingDtos=new ArrayList<PickingDto>();
				pickingDtos.addAll(ret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 处理转运后台逻辑
	 * @param delegator
	 * @param inventoryItemId
	 * @param timestamp
	 * @param inventoryTransferId
	 * @param operator
	 * @throws GenericEntityException
	 * @throws ServiceException 
	 */
	private static void handleTransfer(Delegator delegator,String inventoryItemId,Timestamp timestamp,String inventoryTransferId,String operator) throws GenericEntityException, ServiceException {
		//更新子记录
		GenericValue newInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryItemId));
		newInventoryItem.set("statusId", "IXF_REQUESTED");
		newInventoryItem.set("createdTxStamp", timestamp);
		delegator.store(newInventoryItem);
		String parentInventoryItemId = newInventoryItem.getString("parentInventoryItemId");
		//更新transfer
		GenericValue inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId", inventoryTransferId));						
		inventoryTransfer.set("statusId", "IXF_REQUESTED");
		inventoryTransfer.set("sendDate", timestamp);
		String facilityName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityId"), delegator);
		String facilityToName = WarehouseCommon.getFacilityNameByFacilityId(inventoryTransfer.getString("facilityIdTo"), delegator);
		delegator.store(inventoryTransfer);
		//更新父记录
		//GenericValue parentInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", parentInventoryItemId));
		//if(parentInventoryItem.getBigDecimal("availableToPromiseTotal").subtract(inventoryTransfer.getBigDecimal("transferNumber")).intValue() < 0){
			//throw new ServiceException("The number of available products has been used up!");
		//}
		//parentInventoryItem.set("statusId", null);
		//parentInventoryItem.set("availableToPromiseTotal", parentInventoryItem.getBigDecimal("availableToPromiseTotal").subtract(inventoryTransfer.getBigDecimal("transferNumber")));
		//delegator.store(parentInventoryItem);
		//父记录插入日志
		//WarehouseCommon.insertInventoryItemDetail(delegator, timestamp, parentInventoryItemId, inventoryTransfer.getBigDecimal("transferNumber").negate(), BigDecimal.ZERO,parentInventoryItem.getBigDecimal("quantityOnHandTotal"),  parentInventoryItem.getBigDecimal("availableToPromiseTotal"), UsageTypeEnum.TransferRequest.getName(), operator, facilityName, facilityToName, inventoryTransferId,null,null);	
	}
	
	
	private static PickingByWarehouseVo makePickByWarehouse(String warehouse, String transNum, List<PickingVo> list) {
		PickingByWarehouseVo pickingByWarehouseVo = new PickingByWarehouseVo();
		pickingByWarehouseVo.setPickingVo(list);
		pickingByWarehouseVo.setTransNum(transNum);
		pickingByWarehouseVo.setWarehouse(warehouse);
		return pickingByWarehouseVo;
		
	}

	private static PickingVo makePickingVo(String id, String toWarehouse, String productId, String brandName, String model,
			String descritpion, String type, String transNum,String scanNum) {
		PickingVo pickingVo = new PickingVo();
		pickingVo.setBrandName(brandName);
		pickingVo.setDescritpion(descritpion);
		pickingVo.setId(id);
		pickingVo.setModel(model);
		pickingVo.setProductId(productId);
		pickingVo.setToWarehouse(toWarehouse);
		pickingVo.setTransNum(transNum);
		pickingVo.setType(type);
		pickingVo.setScanNum(scanNum);
		return pickingVo;
	}

	/**
	 * 查询待拣货的数量
	 * @param delegator
	 * @param facilityId
	 * @param productId
	 * @return
	 * @throws GenericDataSourceException
	 * @throws GenericEntityException
	 * @throws SQLException
	 */
	private static int checkAvaToPickInventory(Delegator delegator,String facilityId,String productId) throws GenericDataSourceException, GenericEntityException, SQLException {
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String sql = "SELECT SUM(X.TRANSFER_NUMBER) AS pickNumber FROM (SELECT * FROM INVENTORY_TRANSFER WHERE FACILITY_ID='"+facilityId+"' AND STATUS_ID='IXF_PICKING') X LEFT JOIN INVENTORY_ITEM Y ON X.INVENTORY_ITEM_ID=Y.INVENTORY_ITEM_ID LEFT JOIN TRANSHIPMENT_SHIPPING_BILL Z ON X.TRANSHIPMENT_SHIPPING_BILL_ID=Z.TRANSHIPMENT_SHIPPING_BILL_ID WHERE Z.STATUS='0' AND Y.PRODUCT_ID='"+productId+"' ";
		ResultSet rs = processor.executeQuery(sql);
		while(rs.next()){
			int pickNumber = rs.getInt("pickNumber");
			rs.close();
			processor.close();
			return pickNumber;
		}
		rs.close();
		processor.close();
		return 0;
	}
	
	/**
	 * 根据产品ID与EAN验证是否一致
	 * @param delegator
	 * @param MyEAN
	 * @param productId
	 * @return
	 * @throws GenericDataSourceException
	 * @throws GenericEntityException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	private static void checkEAN(Delegator delegator,String MyEAN,String productId) throws GenericDataSourceException, GenericEntityException, SQLException, ServiceException {
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
		List<GenericValue> list = delegator.findByCondition("GoodIdentification", EntityCondition.makeCondition(exprs), null, null);
		if(list == null || list.size() == 0){
			throw new ServiceException("This product has no EAN!");
		}
		String EAN = list.get(0).getString("idValue");
		if(!MyEAN.equals(EAN)){
			throw new ServiceException(productId+" : EAN is wrong");
		}
	}
	
	private static List<TranferInventoryVo> queryTransferByProductAndTid(Delegator delegator,String tId,String productId) throws GenericDataSourceException, GenericEntityException, SQLException {
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String sql = "SELECT H.INTERNAL_NAME AS model,Z.description,X.INVENTORY_ITEM_ID AS inventoryItemId,X.TRANSFER_NUMBER AS transferNum,X.INVENTORY_TRANSFER_ID AS inventoryTransferId,Y.PRODUCT_ID as productId "
				+ " FROM(SELECT * FROM INVENTORY_TRANSFER WHERE TRANSHIPMENT_SHIPPING_BILL_ID = '"+tId+"') X LEFT JOIN INVENTORY_ITEM Y ON X.INVENTORY_ITEM_ID = Y.INVENTORY_ITEM_ID LEFT JOIN ( SELECT b.PRODUCT_ID, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS description FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY PRODUCT_ID) Z ON Y.PRODUCT_ID = Z.PRODUCT_ID"
				+ " LEFT JOIN PRODUCT H ON Y.PRODUCT_ID=H.PRODUCT_ID WHERE Y.PRODUCT_ID='"+productId+"'";
		ResultSet rs = processor.executeQuery(sql);
		List<TranferInventoryVo> tranferInventoryVos = new ArrayList<TranferInventoryVo>();
		while(rs.next()){
			TranferInventoryVo tranferInventoryVo = new TranferInventoryVo();
			tranferInventoryVo.setInventoryItemId(rs.getString("inventoryItemId"));
			tranferInventoryVo.setTransferNumber(rs.getString("transferNum"));
			tranferInventoryVo.setInventoryTransferId(rs.getString("inventoryTransferId"));
			tranferInventoryVo.setProductId(rs.getString("productId"));
			tranferInventoryVo.setModel(rs.getString("model"));
			tranferInventoryVo.setDescription(rs.getString("description"));
			tranferInventoryVos.add(tranferInventoryVo);
		}
		rs.close();
		processor.close();
		return tranferInventoryVos;
	}
	
	private static Set<Entry<String,List<TransferInventoryModel>>> makeSetParam(List<TransferInventoryModel> params) {
		Map<String,List<TransferInventoryModel>> map = new HashMap<String,List<TransferInventoryModel>>();
		for(TransferInventoryModel transferInventoryDto : params){
			String facilityTo = transferInventoryDto.getFacilityTo();
			if(map.get(facilityTo) == null){
				List<TransferInventoryModel> list = new ArrayList<TransferInventoryModel>();
				list.add(transferInventoryDto);
				map.put(facilityTo,list);
			}else{
				map.get(facilityTo).add(transferInventoryDto);
			}
		}
		return map.entrySet();
	}
	
	private static void checkRegExpInteger(String transferNums) throws ServiceException {
		String regex = "^[0-9]*[1-9][0-9]*$";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(transferNums);
		if(!m.matches() || transferNums.equals("0")){
			throw new ServiceException("The number of transshipment must be integers");
		}
	}
	
	private static void checkProductNum(List<GenericValue> inventoryItems,String productId,int transferNum,int pickingNum) throws ServiceException {
		if(inventoryItems == null || inventoryItems.size() == 0){
			throw new ServiceException(productId +": quantity not sufficient");
		}	
		int inventoryNum = 0;
		for(GenericValue inventoryItem : inventoryItems){
			inventoryNum += inventoryItem.getBigDecimal("availableToPromiseTotal").intValue();
		}
		if("SERIALIZED_INV_ITEM".equals(inventoryItems.get(0).getString("inventoryItemTypeId"))){			
			if(inventoryNum - pickingNum < transferNum){
				throw new ServiceException("The number of transhipment is greater than the number of available quantity");
			}
		}else{
			if(inventoryNum  < transferNum){
				throw new ServiceException("The number of transhipment is greater than the number of available quantity");
			}
		}
		
	}
	
	private static void makeInventoryATPNum(List<GenericValue> inventoryItems,int pickingNum){
		if(inventoryItems.get(0).getString("inventoryItemTypeId").equals("NON_SERIAL_INV_ITEM")){
			for(GenericValue inventoryItem : inventoryItems){
				int ATP = inventoryItem.getBigDecimal("availableToPromiseTotal").intValue();
				if(pickingNum >= ATP){
					pickingNum -= ATP;
					inventoryItem.set("availableToPromiseTotal", "0");
				}else{
					ATP -= pickingNum;
					inventoryItem.set("availableToPromiseTotal", new BigDecimal(ATP));
				}
			}
			Iterator<GenericValue> itr = inventoryItems.iterator();
			while(itr.hasNext()){
				GenericValue inventoryItem = itr.next();
				int ATP = inventoryItem.getBigDecimal("availableToPromiseTotal").intValue();
				if(ATP == 0){
					itr.remove();
				}
			}
		}
	}
	
	private static void makeValue(GenericValue inventoryItem,BigDecimal QTY,BigDecimal ATP,BigDecimal tranferNum,List list,Delegator delegator,
			String facilityId,String facilityTo,Timestamp timeStamp,String partyId,String userLoginId,String facilityName,
			String facilityToName) throws GenericEntityException{
		//inventoryItem.set("quantityOnHandTotal", QTY.subtract(tranferNum));
		inventoryItem.set("availableToPromiseTotal", ATP.subtract(tranferNum));
		delegator.store(inventoryItem);
		
		String inventoryItemId = inventoryItem.getString("inventoryItemId");
		
		GenericValue newInventoryItem = delegator.makeValue("InventoryItem");
		newInventoryItem = inventoryItem;
		newInventoryItem.set("inventoryItemId", delegator.getNextSeqId("InventoryItem"));
		//newInventoryItem.set("quantityOnHandTotal", tranferNum);
		newInventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
		newInventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
		newInventoryItem.set("facilityId", facilityTo);
		newInventoryItem.set("parentInventoryItemId", inventoryItemId);
		newInventoryItem.set("statusId", "IXF_REQUESTED");
		/*newInventoryItem.set("lastUpdatedStamp",timeStamp);
		newInventoryItem.set("lastUpdatedTxStamp",timeStamp);
		newInventoryItem.set("createdStamp",timeStamp);
		newInventoryItem.set("createdTxStamp",timeStamp);*/
		list.add(newInventoryItem);
		
		GenericValue inventoryTransfer = delegator.makeValue("InventoryTransfer");
		inventoryTransfer.set("inventoryTransferId", delegator.getNextSeqId("InventoryTransfer"));
		inventoryTransfer.set("statusId", "IXF_REQUESTED");
		inventoryTransfer.set("inventoryItemId", newInventoryItem.getString("inventoryItemId"));
		inventoryTransfer.set("facilityId", facilityId);
		inventoryTransfer.set("facilityIdTo", facilityTo);
		inventoryTransfer.set("sendDate", timeStamp);
		inventoryTransfer.set("transferNumber", tranferNum);
		list.add(inventoryTransfer);
		
		GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
		inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
		inventoryItemDetail.set("effectiveDate", timeStamp);							
		inventoryItemDetail.set("inventoryItemId", inventoryItemId);
		inventoryItemDetail.set("availableToPromiseDiff", tranferNum.negate());
		//inventoryItemDetail.set("quantityOnHandDiff", tranferNum.negate());
		inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
		inventoryItemDetail.set("qoh",QTY);
		inventoryItemDetail.set("atp", ATP.subtract(tranferNum));
		inventoryItemDetail.set("usageType", UsageTypeEnum.TransferRequest.getName());							
		inventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
		inventoryItemDetail.set("fromWarehouse", facilityName);
		inventoryItemDetail.set("toWarehouse", facilityToName);
		inventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
		list.add(inventoryItemDetail);
		
		/*GenericValue newInventoryItemDetail = delegator.makeValue("InventoryItemDetail");
		newInventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
		newInventoryItemDetail.set("effectiveDate", timeStamp);							
		newInventoryItemDetail.set("inventoryItemId", newInventoryItem.getString("inventoryItemId"));
		newInventoryItemDetail.set("availableToPromiseDiff", BigDecimal.ZERO);
		newInventoryItemDetail.set("quantityOnHandDiff", tranferNum);
		newInventoryItemDetail.set("qoh",tranferNum);
		newInventoryItemDetail.set("atp", BigDecimal.ZERO);
		newInventoryItemDetail.set("usageType", UsageTypeEnum.TransferRequest.getName());							
		newInventoryItemDetail.set("operator", userLoginId+"("+partyId+")");
		newInventoryItemDetail.set("fromWarehouse", facilityName);
		newInventoryItemDetail.set("toWarehouse", facilityToName);
		newInventoryItemDetail.set("transferId", inventoryTransfer.getString("inventoryTransferId"));
		list.add(newInventoryItemDetail);*/
		
		GenericValue InventoryItemValueHistory = delegator.makeValue("InventoryItemValueHistory");
		InventoryItemValueHistory.set("inventoryItemValueHistId", delegator.getNextSeqId("InventoryItemValueHistory"));
		InventoryItemValueHistory.set("inventoryItemId", newInventoryItem.getString("inventoryItemId"));
		InventoryItemValueHistory.set("dateTime", timeStamp);
		InventoryItemValueHistory.set("unitCost", newInventoryItem.getBigDecimal("unitCost"));
		InventoryItemValueHistory.set("setByUserLogin", userLoginId);
		list.add(InventoryItemValueHistory);
	}
	
	/**
	 * 是否是新版转运
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> newTransferOrOldTransfer(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		Map<String, Object> map = WarehouseCommon.getSystemSettings();
		String facilityIds = (String) map.get("SystemSettingsNewTransferWarehouse");
		try {
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);				
			if(facilityIds != null && facilityIds.contains(facilityId)){
				result.put("isNew", "Y");
			}else{
				result.put("isNew", "N");
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
	
	public static Map<String,Object> queryIMEIStatus(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		int reust=1;
		List<PickingDtoEX> params = makeTransferParams(request, PickingDtoEX.class);
		try {
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);	
			
			for(int i=0;i<params.size();i++)
			{
				PickingDtoEX temp = params.get(i);
				String ret = WarehouseCommon.findInventoryItemByIMEI_(delegator,temp.getProductId(),temp.getImei(),facilityId);
				if("OK".equals(ret))
				{
					temp.setResult("success");
					temp.setReason(ret);
				}else 
				{
					reust=-1;
					temp.setResult("failure");
					temp.setReason(ret);
				}
			}
			result.put("resultdate", params);
			result.put("resultCode", reust);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {			
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	public static Map<String,Object> queryScan(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		JSONObject json = InventoryTransfer.makeParams(request);
		String transhipmentShippingBillId = json.getString("transhipmentShippingBillId");
		String productId = json.getString("productId");
		if(transhipmentShippingBillId==null||"".equals(transhipmentShippingBillId)||productId==null||"".equals(productId))
		{
			result.put("resultCode", -1);
			result.put("resultMsg", "Parameter error");
		}
			
		
		List ret=new ArrayList<PickingDtoEX>();
		
		
		try {
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);	
			StringBuffer sql=new StringBuffer("select * from TRANSHIPMENT_SHIPPING_BILL_DETAIL as tsbd where tsbd.TRANSHIPMENT_SHIPPING_BILL_ID =\'"+transhipmentShippingBillId+"\' and tsbd.PRODUCT_ID =\'"+productId+"\' and tsbd.IS_ACTIVE=\'Y\'" );
			try {
				ret = EncapsulatedQueryResultsUtil.getResults_(sql,PickingDtoEX.class, delegator);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.put("resultDate", ret);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {			
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		
		return result;
	}
		
	public static Map<String,Object> saveScanImei(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);	
		List<SaveScanImeiDto> params = makeTransferParams(request, SaveScanImeiDto.class);
		
		String transhipmentShippingBillId;
		String fristtranshipmentShippingBillId=null;
		String productId;
		String fristproductId=null;
		if(params==null|| params.size()==0)
		{
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			return result;
		}
		for (int i = 0; i < params.size(); i++) {
			transhipmentShippingBillId = params.get(i).getTranshipmentShippingBillId();
			productId = params.get(i).getProductId();
			if (transhipmentShippingBillId == null || "".equals(transhipmentShippingBillId) || productId == null
					|| "".equals(productId)) {
				result.put("resultCode", -1);
				result.put("resultMsg", "Parameter error");
				return result;
			}
			if(i==0)
			{
				fristproductId=productId;
				fristtranshipmentShippingBillId=transhipmentShippingBillId;
			}else 
			{
				if(!productId.equals(fristproductId)|| !transhipmentShippingBillId.equals(fristtranshipmentShippingBillId))
				{
					result.put("resultCode", -1);
					result.put("resultMsg", "Parameter error");
					return result;
				}	
			}
			
		}
		List ret=new ArrayList<PickingDtoEX>();
		try {
			String operator = WarehouseCommon.getOperator(request);
			
			StringBuffer deletesql=new StringBuffer("delete from TRANSHIPMENT_SHIPPING_BILL_DETAIL  where TRANSHIPMENT_SHIPPING_BILL_DETAIL.TRANSHIPMENT_SHIPPING_BILL_ID =\'"+fristtranshipmentShippingBillId+"\' and TRANSHIPMENT_SHIPPING_BILL_DETAIL.PRODUCT_ID =\'"+fristproductId+"\'" );
			
			processor.prepareStatement(deletesql.toString());
			processor.executeUpdate();
			int conut = 0;
			int addsum = 500;
			StringBuffer insersql=new StringBuffer("INSERT INTO TRANSHIPMENT_SHIPPING_BILL_DETAIL(TRANSHIPMENT_SHIPPING_BILL_ID,IS_ACTIVE,PRODUCT_ID,IMEI,CREATE_TIME,LAST_UPDATE_TIME,CREATE_USER_lOGIN_ID,LAST_UPDATE_USER_lOGIN_ID) VALUES ");
			for(int k=0;k<params.size();k++)
			{
				SaveScanImeiDto tempdate = params.get(k);
				if(conut==0)
				 {
					 insersql.append(tempdate.toinsertsql(operator));
				 }
				 else 
				 {
					 insersql.append(" ,"+tempdate.toinsertsql(operator));
				 }
				 conut++;
				 if(conut>=addsum)
				 {
					conut=0;
					processor.prepareStatement(insersql.toString());
					processor.executeUpdate();
					
					insersql.delete(0, insersql.length());
					insersql.append("INSERT INTO TRANSHIPMENT_SHIPPING_BILL_DETAIL(TRANSHIPMENT_SHIPPING_BILL_ID,IS_ACTIVE,PRODUCT_ID,IMEI,CREATE_TIME,LAST_UPDATE_TIME,CREATE_USER_lOGIN_ID,LAST_UPDATE_USER_lOGIN_ID) VALUES ");
				 }
			}
			if(conut>0)
			{
				processor.prepareStatement(insersql.toString());
				processor.executeUpdate();
			}
			processor.close();
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {		
			try {
				processor.close();
			} catch (GenericDataSourceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<String,Object> deleteScanImei(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);	
		List<SaveScanImeiDto> params = makeTransferParams(request, SaveScanImeiDto.class);
		
		String transhipmentShippingBillId;
		String fristtranshipmentShippingBillId=null;
		String productId;
		String fristproductId=null;
		
		if(params==null||params.size()==0)
		{
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			return result;
		}
		for (int i = 0; i < params.size(); i++) {
			transhipmentShippingBillId = params.get(i).getTranshipmentShippingBillId();
			productId = params.get(i).getProductId();
			if (transhipmentShippingBillId == null || "".equals(transhipmentShippingBillId) || productId == null
					|| "".equals(productId)) {
				result.put("resultCode", -1);
				result.put("resultMsg", "Parameter error");
				return result;
			}
			if(i==0)
			{
				fristproductId=productId;
				fristtranshipmentShippingBillId=transhipmentShippingBillId;
			}else 
			{
				if(!productId.equals(fristproductId)|| !transhipmentShippingBillId.equals(fristtranshipmentShippingBillId))
				{
					result.put("resultCode", -1);
					result.put("resultMsg", "Parameter error");
					return result;
				}	
			}
			
		}
		try {
			StringBuffer deletesql=new StringBuffer("delete from TRANSHIPMENT_SHIPPING_BILL_DETAIL where TRANSHIPMENT_SHIPPING_BILL_DETAIL.TRANSHIPMENT_SHIPPING_BILL_ID =\'"+fristtranshipmentShippingBillId+"\' and TRANSHIPMENT_SHIPPING_BILL_DETAIL.PRODUCT_ID =\'"+fristproductId+"\' and TRANSHIPMENT_SHIPPING_BILL_DETAIL.IMEI in (");
			boolean isfrsit=true;
			for(int i=0;i<params.size();i++)
			{
				String temimei = params.get(i).getImei();
				if(temimei!=null&&!"".equals(temimei))
				{
					if(isfrsit)
					{
						deletesql.append(" \'"+temimei+"\' ");
						isfrsit=false;
					}
					else 
					{
						deletesql.append(", \'"+temimei+"\' ");
					}
				}
			}
			deletesql.append(" ) ");
			processor.prepareStatement(deletesql.toString());
			processor.executeUpdate();
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			processor.close();
		} catch (GenericEntityException e) {			
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");		
			try {
				processor.close();
			} catch (GenericDataSourceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
}