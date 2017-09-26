package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilHttp;
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
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.service.ServiceException;
import org.opentaps.warehouse.inventoryChange.constant.WarehouseConstant;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockCategoryModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockDetailModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.IMEIInventoryCheckingVo;

import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;

public class InventoryChecking {
	
	/**
	 * 盘点IMEI库存
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> checkIMEIInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String imeiStr = json.getString("imei");
		try{
			TransactionUtil.begin();
			if(imeiStr == null || imeiStr.equals("")){
				throw new ServiceException("Please confirm the product to take stock of!");
			}
			//查询出仓库中所有可用的IMEI库存
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);		
			List<GenericValue> inventorys = findIMEIProductByFcility(delegator, facilityId,true);
			//获取仓库所有的可用IMEI号
			List<String> exitsIMEI = findIMEINumber(inventorys);
			String[] imeis = imeiStr.split(",");
			//检查是否有重复IMEI
			if(checkRepete(imeis)){
				throw new ServiceException("IMEI can not repeat!");
			}
			//储存所有不在仓库的imei
			List<String> profitIMEI = new ArrayList<String>();
			List<String> inInventoryIMEI = new ArrayList<String>();
			//所有在仓库有过记录的IMEI
			List<GenericValue> allInventorys = findIMEIProductByFcility(delegator, facilityId,false);
			List<String> allExitsIMEI = findIMEINumber(allInventorys);
			//所有多出来但是从未入库的
			List<String> neverReceive = new ArrayList<String>();
			for(String imei : imeis){
				if(!exitsIMEI.contains(imei)){
					if(allExitsIMEI.contains(imei)){
						profitIMEI.add(imei);
					}else{
						neverReceive.add(imei);
					}
				}else{
					inInventoryIMEI.add(imei);
				}
			}
			//获取仓库所有存在过的IMEI
			
			List<IMEIInventoryCheckingVo> profixIMEIs = new ArrayList<IMEIInventoryCheckingVo>();
			List<IMEIInventoryCheckingVo> lossIMEIs = new ArrayList<IMEIInventoryCheckingVo>();			
			if(profitIMEI.size() > 0){
				profixIMEIs = queryReasonInventory(delegator, profitIMEI, facilityId);
			}
			//获取丢失的IMEI号
			List<String> lossIMEI = findIMEINumber(findLossIMEI(inventorys, inInventoryIMEI));
			if(lossIMEI != null && lossIMEI.size() > 0){
				lossIMEIs = queryLossInventory(delegator, lossIMEI, facilityId);
			}
			
			for(String imei : neverReceive){
				IMEIInventoryCheckingVo iMEIInventoryCheckingVo = new IMEIInventoryCheckingVo();
				iMEIInventoryCheckingVo.setImei(imei);
				iMEIInventoryCheckingVo.setBrandName("");
				iMEIInventoryCheckingVo.setDescription("");
				iMEIInventoryCheckingVo.setModel("");
				iMEIInventoryCheckingVo.setReason("Not yet received");
				iMEIInventoryCheckingVo.setReasonType("inventory profit");
				profixIMEIs.add(iMEIInventoryCheckingVo);
			}
			
			//插入日志
			String takeStockId = insertCheckLog(request,delegator,profixIMEIs,lossIMEIs,imeis);
			result.put("takeStockId", takeStockId);
			List<IMEIInventoryCheckingVo> list = new ArrayList<IMEIInventoryCheckingVo>();
			list.addAll(lossIMEIs);
			list.addAll(profixIMEIs);
			result.put("result", list);
			result.put("isIMEI", "Y");
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			TransactionUtil.commit();
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
	 * 插入盘点日志
	 * @param delegator
	 * @param profixIMEIs
	 * @param lossIMEIs
	 * @throws ParseException 
	 * @throws GenericEntityException 
	 */
	private static String insertCheckLog(HttpServletRequest request,GenericDelegator delegator, List<IMEIInventoryCheckingVo> profixIMEIs,
			List<IMEIInventoryCheckingVo> lossIMEIs,String[] imeis) throws GenericEntityException, ParseException {
		//插入日志
		String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);		
		String result = "";
		if(profixIMEIs.size() == 0 && lossIMEIs.size() == 0){
			result = "Perfectly Match";
		}else{
			result = "Abnormal";
		}
		String takeStockId = WarehouseCommon.insertTakeStock(delegator, new Date().getTime(),  WarehouseCommon.getLocalYears(request), WarehouseCommon.getUserLoginIdByRequest(request), "Y",facilityId,result);
		List<TakeStockDetailModel> takeStockDetailModels = new ArrayList<TakeStockDetailModel>();
		for(IMEIInventoryCheckingVo profixIMEI : profixIMEIs){
			//插入日志详情
			String description = profixIMEI.getDescription();
			if(description == null){
				description = "";
			}else{
				description = description.replace("\r", "").replace(",", " ");
			}
			String pa = "";
			StringBuffer descriptions = new StringBuffer("");
			if(profixIMEI.getBrandName() != null && !profixIMEI.getBrandName().equals("")){
				descriptions.append(profixIMEI.getBrandName()+"|");
			}
			if(profixIMEI.getModel() != null && !profixIMEI.getModel().equals("")){
				descriptions.append(profixIMEI.getModel()+"|");
			}
			if(!description.equals("")){
				descriptions.append(description+"|");
			}
			if(descriptions.length() > 0){
				pa = descriptions.substring(0, descriptions.length()-1);
			}
			/*WarehouseCommon.insertTakeStockDetail(delegator, takeStockId, profixIMEI.getImei(), descriptions, "inventory profit",
					profixIMEI.getReason(), null, null);*/
			TakeStockDetailModel takeStockDetailModel = new TakeStockDetailModel();
			takeStockDetailModel.setDescription(pa);
			takeStockDetailModel.setImeiOrEan(profixIMEI.getImei());
			takeStockDetailModel.setReason("inventory profit");
			takeStockDetailModel.setStatus(profixIMEI.getReason());
			takeStockDetailModels.add(takeStockDetailModel);
		}

		for(IMEIInventoryCheckingVo lossIMEI : lossIMEIs){
			//插入日志详情
			String description = lossIMEI.getDescription();
			if(description == null){
				description = "";
			}else{
				description = description.replace("\r", "").replace(",", " ");
			}
			String pa = "";
			StringBuffer descriptions = new StringBuffer("");
			if(lossIMEI.getBrandName() != null && !lossIMEI.getBrandName().equals("")){
				descriptions.append(lossIMEI.getBrandName()+"|");
			}
			if(lossIMEI.getModel() != null && !lossIMEI.getModel().equals("")){
				descriptions.append(lossIMEI.getModel()+"|");
			}
			if(!description.equals("")){
				descriptions.append(description+"|");
			}
			if(descriptions.length() > 0){
				pa = descriptions.substring(0, descriptions.length()-1);
			}
			/*WarehouseCommon.insertTakeStockDetail(delegator, takeStockId, lossIMEI.getImei(), descriptions, "inventory loss",
					lossIMEI.getReason(), null, null);*/
			TakeStockDetailModel takeStockDetailModel = new TakeStockDetailModel();
			takeStockDetailModel.setDescription(pa);
			takeStockDetailModel.setImeiOrEan(lossIMEI.getImei());
			takeStockDetailModel.setReason("inventory loss");
			takeStockDetailModel.setStatus("---");
			takeStockDetailModels.add(takeStockDetailModel);
		}
		
		List<TakeStockCategoryModel> takeStockCategoryModels = new ArrayList<TakeStockCategoryModel>();
		for(String imei : imeis){
			//WarehouseCommon.insertTakeStockCategory(delegator, takeStockId, imei, null);
			TakeStockCategoryModel takeStockCategoryModel = new TakeStockCategoryModel();
			takeStockCategoryModel.setImeiOrEan(imei);
			takeStockCategoryModels.add(takeStockCategoryModel);
		}
		//插入批量日志详情以及盘点目录
		WarehouseCommon.insertBatchTakeStockDetail(delegator, takeStockDetailModels, takeStockId,"Y");
		WarehouseCommon.insertBatchTakeStockCategory(delegator, takeStockCategoryModels, takeStockId,"Y");
		return takeStockId;
	}

	/**
	 * 检查是否有重复项
	 * @param imeis
	 * @return
	 */
	private static boolean checkRepete(String[] imeis) {
		Set<String> set = new HashSet<String>();
		for(String str : imeis){
		   set.add(str);
		}
		if(set.size() != imeis.length){
			return true;
		}
		return false;
	}

	/**
	 * 查询本仓库所有可用ATP>0的IMEI库存
	 * @param delegator
	 * @param facilityId
	 * @return
	 */
	private static List<GenericValue> findIMEIProductByFcility(Delegator delegator,String facilityId,boolean flag) throws GenericEntityException  {
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
		exprs.add(EntityCondition.makeCondition("inventoryItemTypeId", EntityOperator.EQUALS, WarehouseConstant.IMEI_TYPE_ID));
		if(flag)
			exprs.add(EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
		
		List<EntityExpr> exprs2 = FastList.newInstance();
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, null));
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "IXF_COMPLETE"));
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_AVAILABLE"));
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_PROMISED"));
		exprs2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_DEACTIVATED"));
		
		EntityCondition totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
				EntityCondition.makeCondition(exprs, EntityOperator.AND),
				EntityCondition.makeCondition(exprs2, EntityOperator.OR)),
				EntityOperator.AND);
		
		List<GenericValue> list = delegator.findByCondition("InventoryItem", totalCondition, null, null);
		return list;
	}
	
	/**
	 * 根据IMEI库存获取全部的IMEI号
	 * @param list
	 * @return
	 */
	private static List<String> findIMEINumber(List<GenericValue> list){
		List<String> imeis = new ArrayList<String>();
		for(GenericValue inventoryItem :list){
			imeis.add(inventoryItem.getString("serialNumber"));
		}
		return imeis;
	}
	
	/**
	 * 获取丢失的库存项
	 * @param list
	 * @param list2
	 * @return
	 */
	private static List<GenericValue> findLossIMEI(List<GenericValue> list,List<String> list2){
		Iterator<GenericValue> itr = list.iterator();
		while(itr.hasNext()){
			GenericValue inventoryItem = itr.next();
			String imei = inventoryItem.getString("serialNumber");
			if(list2.contains(imei)){
				itr.remove();
			}
		}
		return list;
	}
	
	/**
	 * 获取多出来的IMEI的原因
	 * @param delegator
	 * @param imeis
	 * @return
	 * @throws GenericEntityException 
	 * @throws GenericDataSourceException 
	 * @throws SQLException 
	 */
	private static List<IMEIInventoryCheckingVo> queryReasonInventory(Delegator delegator,List<String> imeis,String facilityId) throws GenericDataSourceException, GenericEntityException, SQLException{
		List<IMEIInventoryCheckingVo> results = new ArrayList<IMEIInventoryCheckingVo>();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String imei = WarehouseCommon.ListToSqlParam(imeis);
		String sql = "SELECT X.SERIAL_NUMBER AS imei,Y.INVENTORY_ITEM_ID as transit,L.INVENTORY_ITEM_ID AS tc,Z.STATUS_ID AS beReserved,K.adjustReason,J.DESCRIPTION as description,H.INTERNAL_NAME as model,H.BRAND_NAME AS brandName"
				+ " FROM(SELECT * FROM (SELECT * FROM INVENTORY_ITEM WHERE FACILITY_ID = '"+facilityId+"' AND SERIAL_NUMBER IN ("+imei+") ORDER BY INVENTORY_ITEM_ID DESC) A GROUP BY A.SERIAL_NUMBER) X"
						+ " LEFT JOIN (SELECT * FROM INVENTORY_ITEM WHERE STATUS_ID = 'IXF_REQUESTED') Y ON X.INVENTORY_ITEM_ID=Y.PARENT_INVENTORY_ITEM_ID"
						+ " LEFT JOIN (SELECT * FROM INVENTORY_ITEM WHERE IFNULL(STATUS_ID,'') !='IXF_REQUESTED' AND IFNULL(STATUS_ID,'') !='IXF_CANCELLED') L ON X.INVENTORY_ITEM_ID=L.PARENT_INVENTORY_ITEM_ID"
						+ " LEFT JOIN (SELECT * FROM ORDER_ITEM WHERE STATUS_ID = 'ORDER_PAYMENT' OR STATUS_ID = 'ORDER_PACKED' ) Z ON X.SERIAL_NUMBER=Z.IMEI"
						+ " LEFT JOIN ORDER_HEADER G ON Z.ORDER_ID=G.ORDER_ID"
						+ " LEFT JOIN PRODUCT H ON X.PRODUCT_ID=H.PRODUCT_ID"
						+ " LEFT JOIN   ( SELECT b.product_id, GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID "
						+ " GROUP BY product_id) J ON  X.PRODUCT_ID=J.PRODUCT_ID"
						+ " LEFT JOIN (SELECT * FROM (SELECT B.DESCRIPTION AS adjustReason,INVENTORY_ITEM_ID FROM INVENTORY_ITEM_VARIANCE A LEFT JOIN VARIANCE_REASON B ON A.VARIANCE_REASON_ID=B.VARIANCE_REASON_ID  ORDER BY A.PHYSICAL_INVENTORY_ID) A  GROUP BY A.INVENTORY_ITEM_ID) K"
						+ " ON X.INVENTORY_ITEM_ID=K.INVENTORY_ITEM_ID";
		ResultSet rs = processor.executeQuery(sql);
		while(rs.next()){
			IMEIInventoryCheckingVo imeiInventoryCheckingVo = new IMEIInventoryCheckingVo();
			imeiInventoryCheckingVo.setImei(rs.getString("imei"));
			imeiInventoryCheckingVo.setDescription(rs.getString("description"));
			imeiInventoryCheckingVo.setModel(rs.getString("model"));
			imeiInventoryCheckingVo.setReasonType("inventory profit");
			imeiInventoryCheckingVo.setBrandName(rs.getString("brandName"));
			if(rs.getString("adjustReason") != null){
				imeiInventoryCheckingVo.setReason(rs.getString("adjustReason"));
			}
			if(rs.getString("beReserved") != null){
				imeiInventoryCheckingVo.setReason("be reserved");
			}
			if(rs.getString("transit") != null){
				imeiInventoryCheckingVo.setReason("in transit");
			}
			if(rs.getString("tc") != null){
				imeiInventoryCheckingVo.setReason("transfer to other shop already");
			}
			if(imeiInventoryCheckingVo.getReason() == null){
				imeiInventoryCheckingVo.setReason("Not yet received");
			}
			results.add(imeiInventoryCheckingVo);
		}
		rs.close();
		processor.close();
		return results;
	}
	
	/**
	 * 获取缺少的IMEI产品
	 * @param delegator
	 * @param imeis
	 * @return
	 * @throws GenericEntityException 
	 * @throws GenericDataSourceException 
	 * @throws SQLException 
	 */
	private static List<IMEIInventoryCheckingVo> queryLossInventory(Delegator delegator,List<String> imeis,String facilityId) throws GenericDataSourceException, GenericEntityException, SQLException{
		List<IMEIInventoryCheckingVo> results = new ArrayList<IMEIInventoryCheckingVo>();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String imei = WarehouseCommon.ListToSqlParam(imeis);
		String sql="SELECT A.SERIAL_NUMBER AS imei,B.INTERNAL_NAME AS model,B.BRAND_NAME AS brandName,C.DESCRIPTION as description FROM (SELECT PRODUCT_ID,SERIAL_NUMBER FROM INVENTORY_ITEM WHERE FACILITY_ID='"+facilityId+"' AND SERIAL_NUMBER IN ("+imei+")) A LEFT JOIN PRODUCT B ON A.PRODUCT_ID=B.PRODUCT_ID"
				+ " LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID "
				+ " GROUP BY product_id) C ON  A.PRODUCT_ID=C.PRODUCT_ID";
		ResultSet rs = processor.executeQuery(sql);
		while(rs.next()){
			IMEIInventoryCheckingVo imeiInventoryCheckingVo = new IMEIInventoryCheckingVo();
			imeiInventoryCheckingVo.setImei(rs.getString("imei"));
			imeiInventoryCheckingVo.setModel(rs.getString("model"));
			imeiInventoryCheckingVo.setReasonType("inventory loss");
			imeiInventoryCheckingVo.setDescription(rs.getString("description"));
			results.add(imeiInventoryCheckingVo);
		}
		rs.close();
		processor.close();
		return results;
	}
	
	/**
	 * 导出盘点结果
	 * @param request
	 * @param response
	 */
	public static String exportTakeStockLog(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
	 	Map<String,Object> datas = ItemToString(request, response);
		if((Integer)datas.get("resultCode") == -1){
			return "error";
		}
		
		List<String> dataList = (List<String>) datas.get("result");
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=TakeStock.csv");
        
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
	
	private static Map<String, Object> ItemToString(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		ResultSet rs = null;
		List<TakeStockDetailModel> takeStockDetailModels = new ArrayList<TakeStockDetailModel>();
		
		Map map = UtilHttp.getParameterMap(request);
		String takeStockId = (String) map.get("takeStockId");
		String isIMEI = (String) map.get("isIMEI");
		
		List<String> dataList = new ArrayList<String>();
		if("Y".equals(isIMEI)){
			dataList.add("IMEI,Description,Reason,Status");
		}else{
			dataList.add("EAN,Description,Inventory Quantity,DIF Quantity,Reason");
		}
		try {
			findTakeStockDetail(takeStockId, rs, processor, takeStockDetailModels);
			for(TakeStockDetailModel takeStockDetailModel : takeStockDetailModels){
				if("Y".equals(isIMEI)){
					dataList.add(takeStockDetailModel.getImeiOrEan()+"\t,"+takeStockDetailModel.getDescription()+","+takeStockDetailModel.getReason()+","+takeStockDetailModel.getStatus());
				}else{
					dataList.add(takeStockDetailModel.getImeiOrEan()+"\t,"+takeStockDetailModel.getDescription()+","+takeStockDetailModel.getInventoryQuantity()+","+takeStockDetailModel.getDifQuantity()+","+takeStockDetailModel.getReason());
				}
			}
			result.put("result", dataList);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
		} finally {
			try {
				if(rs != null)
					rs.close();
				processor.close();
			} catch (GenericDataSourceException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据年月查询所有的盘点日志
	 * @param request
	 * @param response
	 */
	public static Map<String,Object> findTakeStockLogByYears(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		JSONObject json = InventoryTransfer.makeParams(request);
		String years = json.getString("years");
		ResultSet rs = null;
		List<TakeStockModel> takeStockModels = new ArrayList<TakeStockModel>();
		try{
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);		
			String sql = "SELECT TAKE_STOCK_ID AS takeStockId,CREATE_TIME as createTime,CREATE_YEARS as createYears,OPERATOR as operator,IS_IMEI as isIMEI FROM TAKE_STOCK WHERE FACILITY_ID='"+facilityId+"' AND CREATE_YEARS='"+years+"' ORDER BY takeStockId DESC";
			rs = processor.executeQuery(sql);
			while(rs.next()){
				TakeStockModel takeStockModel = new TakeStockModel();
				takeStockModel.setTakeStockId(rs.getString("takeStockId"));
				takeStockModel.setCreateTime(rs.getString("createTime"));
				takeStockModel.setCreateYears(rs.getString("createYears"));
				takeStockModel.setIsIMEI(rs.getString("isIMEI"));
				takeStockModel.setOperator(rs.getString("operator"));
				takeStockModels.add(takeStockModel);
			}
			result.put("result", takeStockModels);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				processor.close();
			} catch (GenericDataSourceException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据盘点日期获取所有盘点详细数据
	 * @param request
	 * @param response
	 */
	public static Map<String,Object> findTakeStockDetailLog(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		JSONObject json = InventoryTransfer.makeParams(request);
		String takeStockId = json.getString("takeStockId");
		ResultSet rs = null;
		List<TakeStockDetailModel> takeStockDetailModels = new ArrayList<TakeStockDetailModel>();
		List<TakeStockCategoryModel> takeStockCategoryModels = new ArrayList<TakeStockCategoryModel>();
		try{
			findTakeStockDetail(takeStockId, rs, processor, takeStockDetailModels);
			rs = null;
			
			String categorySql = "SELECT TAKE_STOCK_ID AS takeStockId,IMEI_OR_EAN as imeiOrEan,QUANTITY as quantity FROM TAKE_STOCK_CATEGORY where TAKE_STOCK_ID='"+takeStockId+"'";
			rs = processor.executeQuery(categorySql);
			while(rs.next()){
				TakeStockCategoryModel takeStockCategoryModel = new TakeStockCategoryModel();
				takeStockCategoryModel.setTakeStockId(rs.getString("takeStockId"));
				takeStockCategoryModel.setImeiOrEan(rs.getString("imeiOrEan"));
				takeStockCategoryModel.setQuantity(rs.getString("quantity"));
				takeStockCategoryModels.add(takeStockCategoryModel);
			}
			result.put("takeStockCategory", takeStockCategoryModels);
			result.put("takeStockDetail", takeStockDetailModels);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				processor.close();
			} catch (GenericDataSourceException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private static List<TakeStockDetailModel> findTakeStockDetail(String takeStockId,ResultSet rs,SQLProcessor processor,List<TakeStockDetailModel> takeStockDetailModels) throws SQLException, GenericDataSourceException, GenericEntityException{
		String detailSql = "SELECT TAKE_STOCK_DETAIL_ID AS takeStockDetailId,TAKE_STOCK_ID as takeStockId,IMEI_OR_EAN as imeiOrEan,DESCRIPTION as description,INVENTORY_QUANTITY inventoryQuantity,"
				+ " DIF_QUANTITY difQuantity,REASON as reason,STATUS as status FROM TAKE_STOCK_DETAIL WHERE TAKE_STOCK_ID='"+takeStockId+"'";
		rs = processor.executeQuery(detailSql);
		while(rs.next()){
			TakeStockDetailModel takeStockDetailModel = new TakeStockDetailModel();
			takeStockDetailModel.setTakeStockId(rs.getString("takeStockId"));
			takeStockDetailModel.setTakeStockDetailId(rs.getString("takeStockDetailId"));
			takeStockDetailModel.setDescription(rs.getString("description"));
			takeStockDetailModel.setDifQuantity(rs.getString("difQuantity"));
			takeStockDetailModel.setImeiOrEan(rs.getString("imeiOrEan"));
			takeStockDetailModel.setInventoryQuantity(rs.getString("inventoryQuantity"));
			takeStockDetailModel.setReason(rs.getString("reason"));
			takeStockDetailModel.setStatus(rs.getString("status"));
			takeStockDetailModels.add(takeStockDetailModel);
		}
		return takeStockDetailModels;
	}
	
	/**
	 * 根据条件获取盘点数据
	 * @param request
	 * @param response
	 */
	public static Map<String,Object> findTakeStockLogByCondition(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = InventoryTransfer.makeParams(request);
		String checkTimeFrom = json.getString("checkTimeFrom");
		String checkTimeTo = json.getString("checkTimeTo");
		String facilityId = json.getString("facilityId");
		String resultType = json.getString("resultType");
		String pageNum = json.getString("pageNum");
		String pageSize = json.getString("pageSize");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		try{
			StringBuffer sql = new StringBuffer("SELECT TAKE_STOCK_ID AS takeStockId,CREATE_TIME as createTime,OPERATOR AS operator,IS_IMEI as isIMEI,RESULT AS resultType,Y.FACILITY_NAME AS facilityName,X.CREATE_YEARS as createYears"
					+ " FROM TAKE_STOCK X LEFT JOIN FACILITY Y ON X.FACILITY_ID=Y.FACILITY_ID WHERE 1=1 ");
			if(checkTimeFrom != null && !checkTimeFrom.equals("")){
				sql.append(" AND CREATE_TIME >= "+Long.parseLong(checkTimeFrom));
			}
			if(checkTimeTo != null && !checkTimeTo.equals("")){
				sql.append(" AND CREATE_TIME <= "+Long.parseLong(checkTimeTo));
			}
			if(facilityId != null && !facilityId.equals("")){
				sql.append(" AND X.FACILITY_ID='"+facilityId+"'");
			}
			if(resultType != null && !resultType.equals("")){
				sql.append(" AND X.RESULT='"+resultType+"'");
			}
			sql.append(" ORDER BY CONVERT(`TAKE_STOCK_ID`,SIGNED) DESC");
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.parseInt(pageNum), Integer.parseInt(pageSize), TakeStockModel.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
}
