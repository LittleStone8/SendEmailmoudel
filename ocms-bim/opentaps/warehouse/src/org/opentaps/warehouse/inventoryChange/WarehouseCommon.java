package org.opentaps.warehouse.inventoryChange;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockCategoryModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockDetailModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.DescriptionVO;

import javolution.util.FastList;

public class WarehouseCommon {
	
	/**
	 * 查询可以操作哪些仓库
	 * @param request
	 * @param response
	 * @return
	 */
	 public static Map<String,Object> queryFacility(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	        Delegator delegator = dispacher.getDelegator();
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
			String partyId = (String) userLogin.get("partyId");
			String userLoginId = (String) userLogin.get("userLoginId");			
			try {
				String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);				
				String sql = makeSqlToFindFacility(delegator, userLoginId, partyId);
				List<FacilityVo> inventoryFacilitys = getFacilityByPermission(delegator, sql);				
				result.put("inventoryFacilitys", inventoryFacilitys);
				result.put("defaultFacilityId", facilityId);
				result.put("resultCode", 1);
				result.put("resultMsg", "Successful operation");
			} catch (Exception e) {
				result.put("resultCode", -1);
				result.put("resultMsg", "System exception, please contact the administrator");
				e.printStackTrace();
			}
			return result;
	 }
	 
	 public static String makeSqlToFindFacility(Delegator delegator,String userLoginId,String partyId) throws GenericEntityException{
		 String sql = "";
		 List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);			
		 List<String> groupIds = new ArrayList<String>();
		 for(GenericValue userSecurityGroup : userSecurityGroups){
			 groupIds.add(userSecurityGroup.getString("groupId"));
		 }
		 if(groupIds != null && groupIds.contains("WRHS_ADMIN")){
			 sql = "select FACILITY_ID,FACILITY_NAME FROM FACILITY";
		 }else{
			 sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID";
		 }
		 return sql;
	 }
	
	 /**
	  * 查询所有仓库
	  * @param request
	  * @param response
	  * @return
	  */
	public static Map<String,Object> queryAllFacility(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		//查询所有的仓库
		try {
			List<GenericValue> facilitys = (List<GenericValue>) delegator.findAll("Facility");			
			List<FacilityVo> result = new ArrayList<FacilityVo>();
			if(facilitys != null && facilitys.size() > 0){
				for(GenericValue facility : facilitys){
					FacilityVo facilityVo = new FacilityVo();
					facilityVo.setFacilityId(facility.getString("facilityId"));
					facilityVo.setFacilityName(facility.getString("facilityName"));
					result.add(facilityVo);
				}
			}
			Collections.sort(result);
			map.put("result", result);
			map.put("resultCode", 1);			
			map.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			map.put("resultCode", -1);
			map.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static Map<String,Object> queryAllFacility_(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		//查询所有的仓库
		try {
			List<GenericValue> facilitys = (List<GenericValue>) delegator.findAll("Facility");			
			List<FacilityVo> result = new ArrayList<FacilityVo>();
			if(facilitys != null && facilitys.size() > 0){
				for(GenericValue facility : facilitys){
					FacilityVo facilityVo = new FacilityVo();
					facilityVo.setFacilityId(facility.getString("facilityId"));
					facilityVo.setFacilityName(facility.getString("facilityName"));
					result.add(facilityVo);
				}
			}
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			
			Collections.sort(result);
			
			for(int i=0;i<result.size();i++)
			{
				FacilityVo temp = result.get(i);
				if("Egatee_Warehouse".equals(temp.getFacilityName()))
				{

					result.remove(i);

					result.add(0, temp);
				}
			}
			
			map.put("inventoryFacilitys", result);
			map.put("defaultFacilityId", facilityId);
			map.put("resultCode", 1);			
			map.put("resultMsg", "Successful operation");
		} catch (GenericEntityException e) {
			map.put("resultCode", -1);
			map.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 *获取拥有的菜单权限以及默认仓库
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> userPermission(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		String userLoginId = userLogin.getString("userLoginId");
		try {
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			GenericValue facility = delegator.findByPrimaryKey("Facility",UtilMisc.toMap("facilityId", facilityId));
			String facilityName = "";
			if(facility != null){
				facilityName = facility.getString("facilityName");
			}
			List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);			
			List<String> groupIds = new ArrayList<String>();
			for(GenericValue userSecurityGroup : userSecurityGroups){
				groupIds.add(userSecurityGroup.getString("groupId"));
			}
			List<InventoryMenuContent> inventoryMenuContents = getMenu(groupIds);		
			result.put("result",inventoryMenuContents);
			result.put("facility",facilityName);
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
	 * 获取仓库三级菜单
	 * @param groupIds
	 * @return
	 */
	private static List<InventoryMenuContent> getMenu(List<String> groupIds){
		List<InventoryMenuContent> list = new ArrayList<InventoryMenuContent>();	
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_RAI")){
			InventoryMenuContent inventoryMenuContent = new InventoryMenuContent();
			inventoryMenuContent.setText("Receive Items");
			inventoryMenuContent.setUrl("/warehouse/control/receiveInventoryItem");
			list.add(inventoryMenuContent);
		}
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_FII")){
			InventoryMenuContent inventoryMenuContent = new InventoryMenuContent();
			inventoryMenuContent.setText("Find Inventory Item");
			inventoryMenuContent.setUrl("/warehouse/control/newFindInventoryItem");
			list.add(inventoryMenuContent);
		}
		
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_AQ")){
			InventoryMenuContent inventoryMenuContent = new InventoryMenuContent();
			inventoryMenuContent.setText("Adjust Quantities");
			inventoryMenuContent.setUrl("/warehouse/control/physicalInventory");
			list.add(inventoryMenuContent);
		}
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_TI")){
			InventoryMenuContent inventoryMenuContent = new InventoryMenuContent();
			inventoryMenuContent.setText("Transfer Inventory");
			inventoryMenuContent.setUrl("/warehouse/control/newTransferInventory");
			list.add(inventoryMenuContent);
		}
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_ML")){
			InventoryMenuContent inventoryMenuContent = new InventoryMenuContent();
			inventoryMenuContent.setText("Manage Lots");
			inventoryMenuContent.setUrl("/warehouse/control/manageLots");
			list.add(inventoryMenuContent);
		}
			
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_FII")){
			InventoryMenuContent inventoryMenuContent0 = new InventoryMenuContent();
			inventoryMenuContent0.setText("Product Stock");
			inventoryMenuContent0.setUrl("/warehouse/control/productStock");
			list.add(inventoryMenuContent0);
		}
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_CHECK")){
			InventoryMenuContent inventoryMenuContent0 = new InventoryMenuContent();
			inventoryMenuContent0.setText("Check Stock");
			inventoryMenuContent0.setUrl("/warehouse/control/checkStock");
			list.add(inventoryMenuContent0);
		}
		if(groupIds.contains("WRHS_ADMIN") || groupIds.contains("WRHS_INV_MPM")){
			InventoryMenuContent inventoryMenuContent0 = new InventoryMenuContent();
			inventoryMenuContent0.setText("Modify Product Model");
			inventoryMenuContent0.setUrl("/warehouse_web/index.html#changeproductmodel");
			list.add(inventoryMenuContent0);
		}
		return list;
	}
	
	/**
	 * 获取系统时区的时间字符串
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> getDateNow(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		DateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = dates.format(new Date());
		String date = DateUtils.getLocalizedDate(dateStr+".0", request);
		result.put("date", date);
		return result;
	}
	
	/**
	 * 根据sql查询对应的仓库
	 * @param delegator
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<FacilityVo> getFacilityByPermission(Delegator delegator,String sql) throws Exception{
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<FacilityVo> inventoryFacilitys = new ArrayList<FacilityVo>();		
		ResultSet rs = processor.executeQuery(sql);
		while(rs.next()){
			FacilityVo facilityVo = new FacilityVo();
			facilityVo.setFacilityId(rs.getString("FACILITY_ID"));
			facilityVo.setFacilityName(rs.getString("FACILITY_NAME"));
			inventoryFacilitys.add(facilityVo);
		}
		Collections.sort(inventoryFacilitys);
		rs.close();
		processor.close();
		return inventoryFacilitys;
	}
	
	/**
	 * 验证仓库名称是否正则
	 * @throws ServiceException 
	 */
	public static Map<String, Object> verifyWarehouseName(DispatchContext dctx, Map<String, ? extends Object> context) throws ServiceException{
		Map<String, Object> results = ServiceUtil.returnSuccess();
		String facilityName = (String) context.get("facilityName");
		String facilityId = (String) context.get("facilityId");
		String regex = "^[\\u4E00-\\u9FA5a-zA-Z0-9_]*$";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(facilityName);
		//验证正则
		if(facilityName == null || facilityName.equals("") || !m.matches()){
			throw new ServiceException("Please enter a valid name of warehouse");
		}
		//验证仓库是否存在
		Delegator delegator = dctx.getDelegator();
		try {
			List<GenericValue> facilitys = (List<GenericValue>) delegator.findAll("Facility");
			for(GenericValue facility : facilitys){
				String fn = facility.getString("facilityName");
				String fi = facility.getString("facilityId");
				if(facilityName.equals(fn) && !fi.equals(facilityId)){
					throw new ServiceException("Warehouse name already exists");
				}
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return results;
		
	}
	
	/**
	 * 验证参数是否合理
	 * @throws ServiceException 
	 */
	public static Map<String, Object> validation(DispatchContext dctx, Map<String, ? extends Object> context) throws ServiceException{
		Delegator delegator = dctx.getDelegator();
		Map<String, Object> results = ServiceUtil.returnSuccess();
		String inventoryItemId = (String) context.get("inventoryItemId");
		String quantityOnHandVar =(String) context.get("quantityOnHandVar");
		//验证只能输入整数
		if(quantityOnHandVar == null || quantityOnHandVar.contains(".") || quantityOnHandVar.equals("0")){
			 throw new ServiceException("Please enter an integer is not equal to zero");
		}
		if(quantityOnHandVar.contains("-")){
			quantityOnHandVar = quantityOnHandVar.replace("-", "");
			quantityOnHandVar = quantityOnHandVar.replaceAll(",", "");
			//验证仓库库存数量是否充足
			try {
				GenericValue inventoryItem = delegator.findByPrimaryKey("InventoryItem",UtilMisc.toMap("inventoryItemId",new BigDecimal(inventoryItemId)));
				if(inventoryItem != null){
					if(inventoryItem.getBigDecimal("quantityOnHandTotal").compareTo(new BigDecimal(quantityOnHandVar)) < 0){
						throw new ServiceException("lack of stock");
					}
				}
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
		}
		return results;
		
	}
	
	public static String getFacilityNameByFacilityId(String facilityId,Delegator delegator) throws GenericEntityException{
		GenericValue facility = delegator.findByPrimaryKey("Facility",
                UtilMisc.toMap("facilityId", facilityId));
		return facility.getString("facilityName");
	}
	
	public static String getPartyIdByRequest(HttpServletRequest request) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		return userLogin.getString("partyId");
	}
	
	public static String getUserLoginIdByRequest(HttpServletRequest request) {
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		return userLogin.getString("userLoginId");
	}
	
	public static int getTimeDifference(HttpServletRequest request) throws ParseException{
		Date serverTime = new Date();
		long serverTimes = serverTime.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = dateFormat.format(serverTime);
		String localTimeStr = DateUtils.getLocalizedDate(dateStr+".0", request);
		Date localTime = dateFormat.parse(localTimeStr);
		long localTimes = localTime.getTime();
		long l=serverTimes-localTimes;
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		return Math.round((hour));
	}
	
	public static String getAllPermissionFacility(HttpServletRequest request,Delegator delegator) throws Exception{
		 GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		 String partyId = (String) userLogin.get("partyId");
		 String userLoginId = (String) userLogin.get("userLoginId");	
		 String sqls = WarehouseCommon.makeSqlToFindFacility(delegator, userLoginId, partyId);
		 List<FacilityVo> inventoryFacilitys = WarehouseCommon.getFacilityByPermission(delegator, sqls);
		 StringBuffer facilitys = new StringBuffer("");
		 for(int i=0;i<inventoryFacilitys.size();i++){
			 if(i != inventoryFacilitys.size() - 1){
				 facilitys.append("'"+inventoryFacilitys.get(i).getFacilityId()+"',");
			 }else{
				 facilitys.append("'"+inventoryFacilitys.get(i).getFacilityId()+"'");
			 }
		 }
		 return facilitys.toString();
	 }
	
	public static String insertInventoryItem(Delegator delegator,String inventoryItemTypeId,String productId
			,String ownerPartyId,Timestamp datetimeReceived,String statusId,BigDecimal QTY,BigDecimal ATP
			,String facilityId,String lotId,String serialNumber,String unitCost,String currencyUomId
			,String parentInventoryItemId) throws GenericEntityException{
		GenericValue newInventoryItem = delegator.makeValue("InventoryItem");
		newInventoryItem.set("inventoryItemId",delegator.getNextSeqId("InventoryItem"));
		newInventoryItem.set("inventoryItemTypeId",inventoryItemTypeId);
		newInventoryItem.set("productId", productId);
		newInventoryItem.set("ownerPartyId",ownerPartyId);	
		newInventoryItem.set("datetimeReceived",datetimeReceived);
		newInventoryItem.set("statusId", statusId);
		newInventoryItem.set("quantityOnHandTotal", QTY);
		newInventoryItem.set("availableToPromiseTotal", ATP);
		newInventoryItem.set("facilityId",facilityId);
		newInventoryItem.set("lotId", lotId);
		newInventoryItem.set("serialNumber",serialNumber);
		newInventoryItem.set("unitCost",unitCost);
		newInventoryItem.set("currencyUomId",currencyUomId);
		newInventoryItem.set("parentInventoryItemId", parentInventoryItemId);
		delegator.create(newInventoryItem);
		return newInventoryItem.getString("inventoryItemId");
	}
	
	public static void updateInventoryItemById(Delegator delegator,String inventoryItemId,String inventoryItemTypeId,String productId
			,String ownerPartyId,Timestamp datetimeReceived,String statusId,BigDecimal QTY,BigDecimal ATP
			,String facilityId,String lotId,String serialNumber,String unitCost,String currencyUomId
			,String parentInventoryItemId) throws GenericEntityException{
		GenericValue newInventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId",inventoryItemId));
		if(inventoryItemTypeId != null)
			newInventoryItem.set("inventoryItemTypeId",inventoryItemTypeId);
		if(productId != null)
			newInventoryItem.set("productId", productId);
		if(ownerPartyId != null)
			newInventoryItem.set("ownerPartyId",ownerPartyId);	
		if(datetimeReceived != null)
			newInventoryItem.set("datetimeReceived",datetimeReceived);
		if(statusId != null)
			newInventoryItem.set("statusId", statusId);
		if(QTY != null)
			newInventoryItem.set("quantityOnHandTotal", QTY);
		if(ATP != null)
			newInventoryItem.set("availableToPromiseTotal", ATP);
		if(facilityId != null)
			newInventoryItem.set("facilityId",facilityId);
		if(lotId != null)
			newInventoryItem.set("lotId", lotId);
		if(serialNumber != null)
			newInventoryItem.set("serialNumber",serialNumber);
		if(unitCost != null)
			newInventoryItem.set("unitCost",unitCost);
		if(currencyUomId != null)
			newInventoryItem.set("currencyUomId",currencyUomId);
		if(parentInventoryItemId != null)
			newInventoryItem.set("parentInventoryItemId", parentInventoryItemId);
		delegator.store(newInventoryItem);
	}
	
	public static void insertInventoryItemDetail(Delegator delegator,Timestamp effectiveDate,String inventoryItemId,BigDecimal availableToPromiseDiff
			,BigDecimal quantityOnHandDiff,BigDecimal qoh,BigDecimal atp
			,String usageType,String operator,String facilityName,String facilityToName,String transferId,String reasonEnumId,String statusId) throws GenericEntityException{
		GenericValue newInventoryItemDetail = delegator.makeValue("InventoryItemDetail");
		newInventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));						
		newInventoryItemDetail.set("effectiveDate", effectiveDate);							
		newInventoryItemDetail.set("inventoryItemId", inventoryItemId);
		newInventoryItemDetail.set("availableToPromiseDiff", availableToPromiseDiff);
		newInventoryItemDetail.set("quantityOnHandDiff", quantityOnHandDiff);
		newInventoryItemDetail.set("qoh",qoh);
		newInventoryItemDetail.set("atp", atp);
		newInventoryItemDetail.set("reasonEnumId", reasonEnumId);
		newInventoryItemDetail.set("usageType", usageType);							
		newInventoryItemDetail.set("operator", operator);
		newInventoryItemDetail.set("fromWarehouse", facilityName);
		newInventoryItemDetail.set("toWarehouse", facilityToName);
		newInventoryItemDetail.set("transferId", transferId);
		newInventoryItemDetail.set("statusId", statusId);
		delegator.create(newInventoryItemDetail);
	}
	
	public static void insertShipmentReceipt(Delegator delegator,String inventoryItemId,String productId
			,String userLoginId,Timestamp datetimeReceived,String itemDescription,BigDecimal QTY) throws GenericEntityException{
		GenericValue shipmentReceipt = delegator.makeValue("ShipmentReceipt");
		shipmentReceipt.set("receiptId", delegator.getNextSeqId("ShipmentReceipt"));
		shipmentReceipt.set("inventoryItemId", inventoryItemId);
		shipmentReceipt.set("productId", productId);
		shipmentReceipt.set("receivedByUserLoginId", userLoginId);
		shipmentReceipt.set("datetimeReceived", datetimeReceived);
		shipmentReceipt.set("itemDescription", itemDescription);
		shipmentReceipt.set("quantityAccepted", QTY);
		delegator.create(shipmentReceipt);
	}
	
	public static void insertInventoryItemValueHistory(Delegator delegator,String inventoryItemId
			,Timestamp dateTime,String unitCost,String userLoginId) throws GenericEntityException{
		GenericValue InventoryItemValueHistory = delegator.makeValue("InventoryItemValueHistory");
		InventoryItemValueHistory.set("inventoryItemValueHistId", delegator.getNextSeqId("InventoryItemValueHistory"));
		InventoryItemValueHistory.set("inventoryItemId", inventoryItemId);
		InventoryItemValueHistory.set("dateTime", dateTime);
		InventoryItemValueHistory.set("unitCost", unitCost);
		InventoryItemValueHistory.set("setByUserLogin", userLoginId);
		delegator.create(InventoryItemValueHistory);
	}
	
	public static String insertInventoryTransfer(Delegator delegator,String statusId,String inventoryItemId
			,String facilityId,Timestamp sendDate,String facilityTo,BigDecimal tranferNum,String transhipmentShippingBillId) throws GenericEntityException{
		GenericValue inventoryTransfer = delegator.makeValue("InventoryTransfer");
		inventoryTransfer.set("inventoryTransferId", delegator.getNextSeqId("InventoryTransfer"));
		inventoryTransfer.set("statusId", statusId);
		inventoryTransfer.set("inventoryItemId", inventoryItemId);
		inventoryTransfer.set("facilityId", facilityId);
		inventoryTransfer.set("facilityIdTo", facilityTo);
		inventoryTransfer.set("sendDate", sendDate);
		inventoryTransfer.set("transferNumber", tranferNum);
		inventoryTransfer.set("transhipmentShippingBillId", transhipmentShippingBillId);
		delegator.create(inventoryTransfer);
		return inventoryTransfer.getString("inventoryTransferId");
	}
	
	public static void updateInventoryTransferById(Delegator delegator,String inventoryTransferId,String statusId,String inventoryItemId
			,String facilityId,Timestamp sendDate,String facilityTo,BigDecimal tranferNum,String transhipmentShippingBillId) throws GenericEntityException{
		GenericValue inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId",inventoryTransferId));
		if(statusId != null)
			inventoryTransfer.set("statusId", statusId);
		if(inventoryItemId != null)
			inventoryTransfer.set("inventoryItemId", inventoryItemId);
		if(facilityId != null)
			inventoryTransfer.set("facilityId", facilityId);
		if(facilityTo != null)
			inventoryTransfer.set("facilityIdTo", facilityTo);
		if(sendDate != null)
			inventoryTransfer.set("sendDate", sendDate);
		if(tranferNum != null)
			inventoryTransfer.set("transferNumber", tranferNum);
		if(transhipmentShippingBillId != null)
			inventoryTransfer.set("transhipmentShippingBillId", transhipmentShippingBillId);
		delegator.store(inventoryTransfer);
	}
	
	public static String insertPhysicalInventory(Delegator delegator,String partyId
			,Timestamp physicalInventoryDate,String comment) throws GenericEntityException{
		GenericValue physicalInventory = delegator.makeValue("PhysicalInventory");
		physicalInventory.set("physicalInventoryId", delegator.getNextSeqId("PhysicalInventory"));
		physicalInventory.set("physicalInventoryDate", physicalInventoryDate);
		physicalInventory.set("partyId", partyId);
		physicalInventory.set("generalComments", comment);
		delegator.create(physicalInventory);
		return physicalInventory.getString("physicalInventoryId");
	}
	
	public static void insertInventoryItemVariance(Delegator delegator,String inventoryItemId
			,String physicalInventoryId,String varianceReasonId,BigDecimal atp,BigDecimal qoh,String comments) throws GenericEntityException{
		GenericValue inventoryItemVariance = delegator.makeValue("InventoryItemVariance");
		inventoryItemVariance.set("physicalInventoryId", physicalInventoryId);
		inventoryItemVariance.set("inventoryItemId", inventoryItemId);
		inventoryItemVariance.set("varianceReasonId", varianceReasonId);
		inventoryItemVariance.set("availableToPromiseVar", atp);
		inventoryItemVariance.set("quantityOnHandVar", qoh);
		inventoryItemVariance.set("comments", comments);
		delegator.create(inventoryItemVariance);
	}
	
	public static String insertTranshipmentShippingBill(Delegator delegator,String status
			,Timestamp createTime,Timestamp lastUpdateTime,String createUserLoginId,String lastUpdateUserLoginId,String fromWarehouse,String toWarehouse) throws GenericEntityException{
		GenericValue transhipmentShippingBill = delegator.makeValue("TranshipmentShippingBill");
		transhipmentShippingBill.set("transhipmentShippingBillId", delegator.getNextSeqId("TranshipmentShippingBill"));
		transhipmentShippingBill.set("status", status);
		transhipmentShippingBill.set("createTime", createTime);
		transhipmentShippingBill.set("lastUpdateTime", lastUpdateTime);
		transhipmentShippingBill.set("createUserLoginId", createUserLoginId);
		transhipmentShippingBill.set("lastUpdateUserLoginId", lastUpdateUserLoginId);
		transhipmentShippingBill.set("fromWarehouse", fromWarehouse);
		transhipmentShippingBill.set("toWarehouse", toWarehouse);
		delegator.create(transhipmentShippingBill);
		return transhipmentShippingBill.getString("transhipmentShippingBillId");
	}
	
	public static void updateTranshipmentShippingBillById(Delegator delegator,String transhipmentShippingBillId,String status
			,Timestamp createTime,Timestamp lastUpdateTime,String createUserLoginId,String lastUpdateUserLoginId,String fromWarehouse,String toWarehouse) throws GenericEntityException{
		GenericValue transhipmentShippingBill = delegator.findByPrimaryKey("TranshipmentShippingBill", UtilMisc.toMap("transhipmentShippingBillId",transhipmentShippingBillId));
		if(status != null)
			transhipmentShippingBill.set("status", status);
		if(createTime != null)
			transhipmentShippingBill.set("createTime", createTime);
		if(lastUpdateTime != null)
			transhipmentShippingBill.set("lastUpdateTime", lastUpdateTime);
		if(createUserLoginId != null)
			transhipmentShippingBill.set("createUserLoginId", createUserLoginId);
		if(lastUpdateUserLoginId != null)
			transhipmentShippingBill.set("lastUpdateUserLoginId", lastUpdateUserLoginId);
		if(fromWarehouse != null)
			transhipmentShippingBill.set("fromWarehouse", fromWarehouse);
		if(toWarehouse != null)
			transhipmentShippingBill.set("toWarehouse", toWarehouse);
		delegator.store(transhipmentShippingBill);
	}
	
	public static String insertTakeStock(Delegator delegator
			,Long createTime,String createYears,String operator,String isImei,String facilityId,String result) throws GenericEntityException{
		GenericValue takeStock = delegator.makeValue("TakeStock");
		takeStock.set("takeStockId", delegator.getNextSeqId("TakeStock"));
		takeStock.set("createTime", createTime);
		takeStock.set("createYears", createYears);
		takeStock.set("operator", operator);
		takeStock.set("isImei", isImei);		
		takeStock.set("facilityId", facilityId);	
		takeStock.set("result", result);
		delegator.create(takeStock);
		return takeStock.getString("takeStockId");
	}
	
	public static String insertTakeStockDetail(Delegator delegator,String takeStockId
			,String imeiOrEan,String description,String reason,String status,BigDecimal inventoryQuantity,BigDecimal difQuantity) throws GenericEntityException{
		GenericValue takeStockDetail = delegator.makeValue("TakeStockDetail");
		takeStockDetail.set("takeStockDetailId", delegator.getNextSeqId("TakeStockDetail"));
		takeStockDetail.set("takeStockId", takeStockId);
		takeStockDetail.set("imeiOrEan", imeiOrEan);
		takeStockDetail.set("description", description);
		takeStockDetail.set("reason", reason);
		takeStockDetail.set("status", status);
		takeStockDetail.set("inventoryQuantity", inventoryQuantity);
		takeStockDetail.set("difQuantity", difQuantity);
		delegator.create(takeStockDetail);
		return takeStockDetail.getString("takeStockDetailId");
	}
	
	public static void insertBatchTakeStockDetail(Delegator delegator,List<TakeStockDetailModel> params,String takeStockId,String isIMEI) throws GenericEntityException{
		if(params == null || params.size() == 0) return;
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		StringBuffer sql = new StringBuffer();
		if("Y".equals(isIMEI)){
			sql = new StringBuffer("INSERT INTO `TAKE_STOCK_DETAIL` (`TAKE_STOCK_DETAIL_ID`, `TAKE_STOCK_ID`, `IMEI_OR_EAN`, `DESCRIPTION`,  `REASON`, `STATUS`) VALUES ");
		}else{
			sql = new StringBuffer("INSERT INTO `TAKE_STOCK_DETAIL` (`TAKE_STOCK_DETAIL_ID`, `TAKE_STOCK_ID`, `IMEI_OR_EAN`, `DESCRIPTION`, `INVENTORY_QUANTITY`, `DIF_QUANTITY`, `REASON`) VALUES ");
		}

		for(int i=0;i<params.size();i++){
			TakeStockDetailModel takeStockDetailModel = params.get(i);
			String imeiOrEan = takeStockDetailModel.getImeiOrEan();
			if(imeiOrEan==null)
				imeiOrEan="";
			
			String description = takeStockDetailModel.getDescription();
			if(description==null)
				description="";
			
			String reason = takeStockDetailModel.getReason();
			String status = takeStockDetailModel.getStatus();
			String inventoryQuantity = takeStockDetailModel.getInventoryQuantity();
			String difQuantity = takeStockDetailModel.getDifQuantity();
			if(i != params.size() - 1){
				if("Y".equals(isIMEI)){
					sql.append("('"+delegator.getNextSeqId("TakeStockDetail")+"', '"+takeStockId+"', '"+imeiOrEan+"', '"+description+"', '"+reason+"', '"+status+"'),");			
				}else{
					sql.append("('"+delegator.getNextSeqId("TakeStockDetail")+"', '"+takeStockId+"', '"+imeiOrEan+"', '"+description+"', '"+inventoryQuantity+"', '"+difQuantity+"', '"+reason+"'),");			
				}
			}else{
				if("Y".equals(isIMEI)){
					sql.append("('"+delegator.getNextSeqId("TakeStockDetail")+"', '"+takeStockId+"', '"+imeiOrEan+"', '"+description+"', '"+reason+"', '"+status+"')");			
				}else{
					sql.append("('"+delegator.getNextSeqId("TakeStockDetail")+"', '"+takeStockId+"', '"+imeiOrEan+"', '"+description+"', '"+inventoryQuantity+"', '"+difQuantity+"', '"+reason+"')");			
				}
				
			}
		}
		processor.prepareStatement(sql.toString());
		processor.executeUpdate();
		processor.close();
	}
	
	public static void insertTakeStockCategory(Delegator delegator
			,String takeStockId,String imeiOrEan,BigDecimal quantity) throws GenericEntityException{
		GenericValue takeStockCategory = delegator.makeValue("TakeStockCategory");
		takeStockCategory.set("takeStockId", takeStockId);
		takeStockCategory.set("imeiOrEan", imeiOrEan);
		takeStockCategory.set("quantity", quantity);	
		delegator.create(takeStockCategory);
	}
	
	public static void insertBatchTakeStockCategory(Delegator delegator
			,List<TakeStockCategoryModel> params,String takeStockId,String isIMEI) throws GenericEntityException{
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		StringBuffer sql = new StringBuffer();
		if("Y".equals(isIMEI)){
			sql = new StringBuffer("INSERT INTO `TAKE_STOCK_CATEGORY` (`TAKE_STOCK_ID`, `IMEI_OR_EAN`) VALUES ");
		}else{
			sql = new StringBuffer("INSERT INTO `TAKE_STOCK_CATEGORY` (`TAKE_STOCK_ID`, `IMEI_OR_EAN`, `QUANTITY`) VALUES ");
		}
		for(int i=0;i<params.size();i++){
			TakeStockCategoryModel takeStockCategoryModel = params.get(i);
			String imeiOrEan = takeStockCategoryModel.getImeiOrEan();
			String quantity = takeStockCategoryModel.getQuantity();

			if(i != params.size() - 1){
				if("Y".equals(isIMEI)){
					sql.append("('"+takeStockId+"', '"+imeiOrEan+"'),");	
				}else{
					sql.append("('"+takeStockId+"', '"+imeiOrEan+"', '"+quantity+"'),");	
				}
			}else{
				if("Y".equals(isIMEI)){
					sql.append("('"+takeStockId+"', '"+imeiOrEan+"')");
				}else{
					sql.append("('"+takeStockId+"', '"+imeiOrEan+"', '"+quantity+"')");
				}
							
			}
		}
		processor.prepareStatement(sql.toString());
		processor.executeUpdate();
		processor.close();
	}

	
	public static GenericValue findInventoryItemByIMEI(Delegator delegator,String imei,String facilityId) throws GenericEntityException{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
		exprs.add(EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
		List<GenericValue> inventoryItem =  delegator.findByCondition("InventoryItem", EntityCondition.makeCondition(exprs), null, null);
		if(inventoryItem == null ||inventoryItem.size() == 0){
			return null;
		}
		return inventoryItem.get(0);
	}
	
	public static String findInventoryItemByIMEI_(Delegator delegator,String productid,String imei,String facilityId) throws GenericEntityException{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei));
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
		exprs.add(EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
		List<GenericValue> inventoryItem =  delegator.findByCondition("InventoryItem", EntityCondition.makeCondition(exprs), null, null);
		if(inventoryItem == null ||inventoryItem.size() == 0){
			return "Cannot match IMEI ("+imei+") in the current warehouse.";
		}
		String productId = inventoryItem.get(0).getString("productId");
		if(productId==null||!productId.equals(productid))
			return "This IMEI("+imei+") does not belong to this product ID.";
		boolean isprostm=true;
		for(int i=0;i<inventoryItem.size();i++)
		{
			GenericValue temp = inventoryItem.get(i);
			String statusId = temp.getString("statusId");
			if(statusId!=null && !"INV_PROMISED".equals(statusId)|| !"".equals(statusId))
				isprostm=false;
			else if("INV_DEACTIVATED".equals(isprostm))
				return "This IMEI("+imei+")  was stolen.";
			else 
				return "OK";
				
		}
		if(isprostm)
			return "This IMEI("+imei+") is unavailable.";
		return "OK";
	}
	
	
	public static String getCurrencyUomId(Delegator delegator,String ownerPartyId) throws GenericEntityException{
		GenericValue partyAcctgPreference = delegator.findByPrimaryKey("PartyAcctgPreference",UtilMisc.toMap("partyId",ownerPartyId));
		String currencyUomId = "";
		if(partyAcctgPreference != null){
			currencyUomId = partyAcctgPreference.getString("baseCurrencyUomId");
		}else{
			currencyUomId = UtilProperties.getPropertyValue("general.properties", "currency.uom.id.default");
		}
		return currencyUomId;
	}
	
	public static String getOwnerPartyId(Delegator delegator,String facilityId) throws GenericEntityException{
		GenericValue facility = delegator.findByPrimaryKey("Facility",UtilMisc.toMap("facilityId",facilityId));
		String ownerPartyId = facility.getString("ownerPartyId");
		return ownerPartyId;
	}
	
	public static String getUnitCost(Delegator delegator,String productId) throws GenericEntityException{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
		exprs.add(EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, "DEFAULT_COST_PRICE"));
		List<GenericValue> productPrices = delegator.findByCondition("ProductPrice", EntityCondition.makeCondition(exprs), null, null);
		if(productPrices != null && productPrices.size() > 0){
			GenericValue productPrice = productPrices.get(0);
			return productPrice.getString("price");
		}else{
			return "0";
		}		
	}
	
	public static String getOperator(HttpServletRequest request) throws GenericEntityException{
		
		String userLoginId = getUserLoginIdByRequest(request);
		String partyId = getPartyIdByRequest(request);
		return userLoginId+"("+partyId+")";
	}
	
	public static String ListToSqlParam(List<String> list) throws GenericEntityException{
		StringBuffer tId = new StringBuffer();
		for(int i=0;i<list.size();i++){
			if(i == list.size()-1){
				tId.append("'"+list.get(i)+"'");
			}else{
				tId.append("'"+list.get(i)+"',");
			}
		}
		return tId.toString();
	}
	
	public static String getLocalYears(HttpServletRequest request) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowStr = dateFormat.format(new Date());
		nowStr = nowStr+".0";
		String localDateStr = DateUtils.getLocalizedDate(nowStr, request);
		Date localDate = dateFormat.parse(localDateStr);
		
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
		String localYears = dateFormat2.format(localDate);
		return localYears;
	}
	
	public static Map<String, Object> getSystemSettings() {
		Map<String, Object> retmap=new HashMap<String, Object>();
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key.trim());
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		String tempfilepath = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsTempFilePath");
		
		String SystemSettingsSendemailUserName = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailUserName");
		String SystemSettingsSendemailAuthorizationCode = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailAuthorizationCode");
		String SystemSettingsSendemailRecipient = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailRecipient");
		String SystemSettingsNewTransferWarehouse = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsNewTransferWarehouse");
		String SystemSettingsNewReceived = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsNewReceived");
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId.trim());
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		retmap.put("tempfilepath", tempfilepath);
		
		retmap.put("SystemSettingsSendemailUserName", SystemSettingsSendemailUserName.trim());
		retmap.put("SystemSettingsSendemailAuthorizationCode", SystemSettingsSendemailAuthorizationCode.trim());
		retmap.put("SystemSettingsSendemailRecipient", SystemSettingsSendemailRecipient.trim());
		retmap.put("SystemSettingsNewTransferWarehouse", SystemSettingsNewTransferWarehouse.trim());
		retmap.put("SystemSettingsNewReceived", SystemSettingsNewReceived.trim());
		return retmap;
	}
	public static String getDescriptionbyEan(Delegator delegator,String ean)
	{
		if(ean==null||"".equals(ean))
			return "";
		
		String descsql = "select gi.PRODUCT_ID as productId,gi.ID_VALUE as ean,profed.DESCRIPTION as description,pr.BRAND_NAME as brand,pr.INTERNAL_NAME as model from GOOD_IDENTIFICATION as gi LEFT JOIN PRODUCT  as pr on pr.PRODUCT_ID=gi.PRODUCT_ID "
				+ " LEFT JOIN (select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
				+ " ( SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID "
				+ " FROM " + " PRODUCT_FEATURE_APPL c "
				+ " LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID "
				+ " 	) as  fea " + " group by PRODUCT_ID "
				+ " ) as profed on profed.PRODUCT_ID=gi.PRODUCT_ID"
				+ " where gi.GOOD_IDENTIFICATION_TYPE_ID=\'ean\' and gi.ID_VALUE =\'" + ean+ "\'";
		try {
			List<DescriptionVO> addqueryRetList = EncapsulatedQueryResultsUtil.getResults_(new StringBuffer(descsql), DescriptionVO.class, delegator);
			if(addqueryRetList.size()>0)
			{
				return addqueryRetList.get(0).getcompletelydescription();
			}
			else 
			{
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String getDescriptionbyproductid(Delegator delegator,String productid)
	{
		if(productid==null||"".equals(productid))
			return "";
		String descsql = "select pr.PRODUCT_ID as productId,ret.DESCRIPTION as description,pr.BRAND_NAME as brand,pr.INTERNAL_NAME as model from PRODUCT  as pr"
				+ " LEFT JOIN  "
				+ "( "
				+ " SELECT"
				+ " PRODUCT_ID,"
				+ " GROUP_CONCAT("
				+ " DISTINCT DESCRIPTION"
				+ " ORDER BY"
				+ " PRODUCT_FEATURE_TYPE_ID"
				+ " ) AS DESCRIPTION"
				+ " FROM"
				+ " ("
				+ " SELECT"
				+ " c.PRODUCT_ID AS PRODUCT_ID,"
				+ " c.PRODUCT_FEATURE_ID AS PRODUCT_FEATURE_ID,"
				+ " DESCRIPTION,"
				+ " d.PRODUCT_FEATURE_TYPE_ID AS PRODUCT_FEATURE_TYPE_ID"
				+ " FROM"
				+ " PRODUCT_FEATURE_APPL c"
				+ " LEFT JOIN PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID"
				+ " ) AS fea"
				+ " GROUP BY"
				+ " PRODUCT_ID"
				+ " ) AS ret on ret.PRODUCT_ID=pr.PRODUCT_ID"
				+ " where pr.PRODUCT_ID =\'" + productid+ "\'";
		try {
			List<DescriptionVO> addqueryRetList = EncapsulatedQueryResultsUtil.getResults_(new StringBuffer(descsql), DescriptionVO.class, delegator);
			if(addqueryRetList.size()>0)
			{
				return addqueryRetList.get(0).getcompletelydescription();
			}
			else 
			{
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 检查用户是否有店长权限
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> checkUserHasStoreManage(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
        LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = dispacher.getDelegator();
        String partyId = getPartyIdByRequest(request);
        List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
		exprs.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "STORE_MANAGER"));
		exprs.add(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null));
		try {
			List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", getUserLoginIdByRequest(request)), null);			
			List<String> groupIds = new ArrayList<String>();
			for(GenericValue userSecurityGroup : userSecurityGroups){
				groupIds.add(userSecurityGroup.getString("groupId"));
			}
			if(groupIds.contains("WRHS_ADMIN")){
				result.put("isStoreManage", "N");
			}else{
				List<GenericValue> list = delegator.findByCondition("ProductStoreRole", EntityCondition.makeCondition(exprs), null, null);
				if(list == null || list.size() == 0){
					result.put("isStoreManage", "N");
				}else{
					result.put("isStoreManage", "Y");
				}
			}		
			result.put("resultCode", 1);
			result.put("resultMsg", "operate successfully");
		} catch (GenericEntityException e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
        return result;
	}
}
