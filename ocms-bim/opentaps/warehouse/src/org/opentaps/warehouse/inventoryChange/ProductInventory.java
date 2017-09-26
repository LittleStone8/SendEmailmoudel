package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.foundation.action.ActionContext;
import org.opentaps.foundation.service.ServiceException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;

/**
 * 此类已废弃
 * @author wei.he
 *
 */
public class ProductInventory {
	
	/**
	 * 获取库存明细
	 * @param dctx
	 * @param context
	 * @return
	 * @throws ServiceException
	 */
	public static Map getProductInventory(DispatchContext dctx, Map context) throws ServiceException {
        Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) dctx.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		String productId = (String) context.get("productId");
		String internalName = (String) context.get("internalName");
		String serialNumber = (String) context.get("serialNumber");
		String lotId = (String) context.get("lotId");
		String facilityIdToFind = (String) context.get("facilityIdToFind");
		if(facilityIdToFind == null){
			facilityIdToFind = (String) context.get("facilityId");
		}
		//获取可查询的仓库
		//设置仓库默认�	
		result.put("selectFacilityId", facilityIdToFind);	
		Map<String,Object> facilitys = getFacility(dctx, context);
		List<ProductFromInventory> list = (List<ProductFromInventory>) facilitys.get("inventoryFacilitys");
		result.put("inventoryFacilitys", list);
		boolean facilityIdFlag = true;
		for(ProductFromInventory productFromInventory : list){
			if(productFromInventory.getFacilityId().equals(facilityIdToFind)){
				facilityIdFlag = false;
				break;
			}
		}
		
		if(facilityIdFlag && facilityIdToFind != null){
			result.put("errorMessage", "You do not have the authority to query the warehouse");
			result.put("inventoryItems",new ArrayList());
			result.put("selectFacilityName","");
			return result;
		}
		String orderBy=(String) context.get("orderBy");
		
		String flag = (String) context.get("flag");
		//拼接sql
		StringBuffer sql = new StringBuffer("SELECT x.*, y.DESCRIPTION FROM (SELECT "
				+ " a.INVENTORY_ITEM_ID,"
				+ "a.SERIAL_NUMBER,"
				+ "a.LOT_ID,"
				+ "a.DATETIME_RECEIVED,"
				+ "a.STATUS_ID,"
				+ "a.QUANTITY_ON_HAND_TOTAL,"
				+ "a.product_Id,"
				+ "b.INTERNAL_NAME"
				+ " FROM INVENTORY_ITEM a LEFT JOIN PRODUCT b ON a.PRODUCT_ID = b.PRODUCT_ID WHERE (a.STATUS_ID IS NULL OR a.STATUS_ID = 'INV_AVAILABLE' OR a.STATUS_ID = 'IXF_COMPLETE')  AND a.FACILITY_ID = '"+facilityIdToFind+"'");
		if(flag == null){
			sql.append(" AND a.QUANTITY_ON_HAND_TOTAL>0");
		}
		if(productId != null && !productId.equals("")){
			sql.append(" AND a.product_id='"+productId+"'");
		}
		if(internalName != null && !internalName.equals("")){
			sql.append(" AND b.internal_Name like '%"+internalName+"%'");
		}
		if(serialNumber != null && !serialNumber.equals("")){
			sql.append(" AND a.serial_Number like '%"+serialNumber+"%'");
		}
		if(lotId != null && !lotId.equals("")){
			sql.append(" AND a.lot_Id='"+lotId+"'");
		}
		sql.append(") x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) y ON x.product_Id = y.product_id");
		//执行sql
		if(orderBy != null && !orderBy.equals("")){
			sql.append(" order by "+orderBy);
		}
		ResultSet rs = null;
		List<ProductFromInventory> inventoryItems = new ArrayList<ProductFromInventory>();
		try {	
			GenericValue facility = delegator.findByPrimaryKey("Facility", UtilMisc.toMap("facilityId",facilityIdToFind));
			if(facility != null)
			result.put("selectFacilityName", facility.getString("facilityName"));
			//查询库存
			rs = processor.executeQuery(sql.toString());
			while(rs.next()){
				ProductFromInventory productFromInventory = new ProductFromInventory();
				productFromInventory.setInventoryItemId(rs.getString("INVENTORY_ITEM_ID"));
				productFromInventory.setProductId(rs.getString("product_Id"));
				productFromInventory.setInternalName(rs.getString("INTERNAL_NAME"));
				productFromInventory.setSerialNumber(rs.getString("SERIAL_NUMBER"));
				productFromInventory.setLotId(rs.getString("LOT_ID"));
				productFromInventory.setDatetimeReceived(rs.getTimestamp("DATETIME_RECEIVED"));
				productFromInventory.setStatusId(rs.getString("STATUS_ID"));
				productFromInventory.setQuantityOnHandTotal(rs.getBigDecimal("QUANTITY_ON_HAND_TOTAL"));
				productFromInventory.setDescription(rs.getString("DESCRIPTION"));
				inventoryItems.add(productFromInventory);
			}
			result.put("inventoryItems", inventoryItems);
		} catch (Exception e) {
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
	}
	
	/**
	 * 获取物理库存变化明细
	 * @param dctx
	 * @param context
	 * @return
	 */
	public static Map getPhysicalInventory(DispatchContext dctx, Map context){
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) dctx.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		String productId = (String) context.get("productId");
		if(productId == null) productId = "";
		String internalName = (String) context.get("internalName");
		if(internalName == null) internalName = "";
		String facilityId = (String) context.get("facilityId");
		
		String orderBy = (String) context.get("orderBy");
		ResultSet rs = null;
		List<ProductFromInventory> physicalInventory = new ArrayList<ProductFromInventory>();
		try {
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
				sql.append(" AND a.product_id='"+productId+"'");
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
			result.put("facilityId", facilityId.trim());
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
	}
	
	/**
	 * 获取库存转移的数�
	 * @param dctx
	 * @param context
	 * @return
	 * @throws ServiceException
	 */
	public static Map getTransferInventory(DispatchContext dctx, Map context) {
        Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) dctx.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		boolean activeOnly = false;
		Object activeOnlys = context.get("activeOnly");
		if(activeOnlys == null) 
			activeOnly = true;
		else 
			activeOnly = (Boolean) activeOnlys;	
		String facilityId = (String) context.get("facilityId");
		
		String orderBy = (String) context.get("orderBy");
		
		StringBuffer sqlTO = null;
		StringBuffer sqlFrom = null;
		//拼接sql
		if(activeOnly){
			sqlTO = new StringBuffer("select * from ( select a.INVENTORY_TRANSFER_ID,a.inventory_item_id,b.product_id,c.internal_name,b.QUANTITY_ON_HAND_TOTAL,a.FACILITY_ID,a.FACILITY_ID_TO, a.send_date,a.STATUS_ID,d.FACILITY_NAME from INVENTORY_TRANSFER a LEFT JOIN INVENTORY_ITEM b on a.inventory_item_id=b.inventory_item_id LEFT JOIN PRODUCT c on b.product_id=c.product_id LEFT JOIN FACILITY d on a.FACILITY_ID_TO=d.FACILITY_ID where a.FACILITY_ID='"+facilityId+"' AND a.status_id='IXF_REQUESTED' ) x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id ) y on x.product_id=y.product_id");
			sqlFrom = new StringBuffer("select * from ( select a.INVENTORY_TRANSFER_ID,a.inventory_item_id,b.product_id,c.internal_name,b.QUANTITY_ON_HAND_TOTAL,a.FACILITY_ID,a.FACILITY_ID_TO, a.send_date,a.STATUS_ID,d.FACILITY_NAME from INVENTORY_TRANSFER a LEFT JOIN INVENTORY_ITEM b on a.inventory_item_id=b.inventory_item_id LEFT JOIN PRODUCT c on b.product_id=c.product_id LEFT JOIN FACILITY d on a.FACILITY_ID=d.FACILITY_ID where a.FACILITY_ID_TO='"+facilityId+"' AND a.status_id='IXF_REQUESTED' ) x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id ) y on x.product_id=y.product_id");
		}else{
			sqlTO = new StringBuffer("select * from ( select a.INVENTORY_TRANSFER_ID,a.inventory_item_id,b.product_id,c.internal_name,b.QUANTITY_ON_HAND_TOTAL,a.FACILITY_ID,a.FACILITY_ID_TO, a.send_date,a.STATUS_ID,d.FACILITY_NAME from INVENTORY_TRANSFER a LEFT JOIN INVENTORY_ITEM b on a.inventory_item_id=b.inventory_item_id LEFT JOIN PRODUCT c on b.product_id=c.product_id LEFT JOIN FACILITY d on a.FACILITY_ID_TO=d.FACILITY_ID where a.FACILITY_ID='"+facilityId+"') x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id ) y on x.product_id=y.product_id");	
			sqlFrom = new StringBuffer("select * from ( select a.INVENTORY_TRANSFER_ID,a.inventory_item_id,b.product_id,c.internal_name,b.QUANTITY_ON_HAND_TOTAL,a.FACILITY_ID,a.FACILITY_ID_TO, a.send_date,a.STATUS_ID,d.FACILITY_NAME from INVENTORY_TRANSFER a LEFT JOIN INVENTORY_ITEM b on a.inventory_item_id=b.inventory_item_id LEFT JOIN PRODUCT c on b.product_id=c.product_id LEFT JOIN FACILITY d on a.FACILITY_ID=d.FACILITY_ID where a.FACILITY_ID_TO='"+facilityId+"') x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id ) y on x.product_id=y.product_id");
		}
		//结果1
		List<ProductFromInventory> toTransfers = new ArrayList<ProductFromInventory>();
		List<ProductFromInventory> fromTransfers = new ArrayList<ProductFromInventory>();
		ResultSet rs = null;
		if(orderBy != null && !orderBy.equals("")){
			sqlTO.append(" order by "+orderBy);
			sqlFrom.append(" order by "+orderBy);
		}else{
			sqlTO.append(" order by x.send_date");
			sqlFrom.append(" order by x.send_date");
		}
		try {
			rs = processor.executeQuery(sqlTO.toString());
			while(rs.next()){
				ProductFromInventory productFromInventory = new ProductFromInventory();
				productFromInventory.setInventoryTransferId(rs.getString("INVENTORY_TRANSFER_ID"));
				productFromInventory.setInventoryItemId(rs.getString("inventory_item_id"));
				productFromInventory.setProductId(rs.getString("product_id"));
				productFromInventory.setInternalName(rs.getString("internal_name"));
				productFromInventory.setQuantityOnHandTotal(rs.getBigDecimal("QUANTITY_ON_HAND_TOTAL"));
				productFromInventory.setFacilityId(rs.getString("FACILITY_ID"));
				productFromInventory.setFacilityIdTo(rs.getString("FACILITY_ID_TO"));
				productFromInventory.setSendDate(rs.getTimestamp("send_date"));
				productFromInventory.setStatusId(rs.getString("STATUS_ID"));
				productFromInventory.setDescription(rs.getString("productAttributes"));
				productFromInventory.setFacilityName(rs.getString("FACILITY_NAME"));
				fromTransfers.add(productFromInventory);
			}
			rs = processor.executeQuery(sqlFrom.toString());
			while(rs.next()){
				ProductFromInventory productFromInventory = new ProductFromInventory();
				productFromInventory.setInventoryTransferId(rs.getString("INVENTORY_TRANSFER_ID"));
				productFromInventory.setInventoryItemId(rs.getString("inventory_item_id"));
				productFromInventory.setProductId(rs.getString("product_id"));
				productFromInventory.setInternalName(rs.getString("internal_name"));
				productFromInventory.setQuantityOnHandTotal(rs.getBigDecimal("QUANTITY_ON_HAND_TOTAL"));
				productFromInventory.setFacilityId(rs.getString("FACILITY_ID"));
				productFromInventory.setFacilityIdTo(rs.getString("FACILITY_ID_TO"));
				productFromInventory.setSendDate(rs.getTimestamp("send_date"));
				productFromInventory.setDescription(rs.getString("productAttributes"));
				productFromInventory.setStatusId(rs.getString("STATUS_ID"));
				productFromInventory.setFacilityName(rs.getString("FACILITY_NAME"));
				toTransfers.add(productFromInventory);
			}
			result.put("toTransfers", toTransfers);
			result.put("fromTransfers", fromTransfers);
			result.put("activeOnly", activeOnly);
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}
	
	/**
	 * 获取facility的默认值以及可查询的仓� * @param dctx
	 * @param context
	 * @return
	 */
	public static Map getFacility(DispatchContext dctx, Map context) {
        Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) dctx.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//接收参数
		String partyId = (String) context.get("partyId");
		
		//根据partyId获取对应的仓�	/*String sql = "SELECT FACILITY_ID,FACILITY_NAME FROM FACILITY b LEFT JOIN PRODUCT_STORE a ON a.INVENTORY_FACILITY_ID = b.FACILITY_ID LEFT JOIN PRODUCT_STORE_ROLE c ON a.PRODUCT_STORE_ID=c.PRODUCT_STORE_ID"
				//+ " WHERE( c.ROLE_TYPE_ID = 'STORE_MANAGER' OR c.ROLE_TYPE_ID = 'MANAGER') AND c.THRU_DATE IS NULL AND c.PARTY_ID = '"+partyId+"'";
		String sql = "";
		if("admin".equals(partyId)){
			sql = "select FACILITY_ID,FACILITY_NAME FROM FACILITY";
		}else{
			sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID";
		}
		ResultSet rs = null;
		List<ProductFromInventory> inventoryFacilitys = new ArrayList<ProductFromInventory>();
		try {
			rs = processor.executeQuery(sql);
			while(rs.next()){
				ProductFromInventory productFromInventory = new ProductFromInventory();
				productFromInventory.setFacilityId(rs.getString("FACILITY_ID"));
				productFromInventory.setFacilityName(rs.getString("FACILITY_NAME"));
				inventoryFacilitys.add(productFromInventory);
			}
			result.put("inventoryFacilitys", inventoryFacilitys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
				processor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 接收库存
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> receiveInventoryProduct(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> result = new HashMap<String, Object>();
        LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = dispacher.getDelegator();
        //获取参数
		//JSONObject json = InventoryTransfer.makeParams(request);
        List<JSONObject> params = makeParamsToList(request);
        try {
	        if(params == null || params.size() <= 0){
	        	throw new ServiceException("Please fill in the parameter");
	        }
	        TransactionUtil.begin();
	        //遍历参数
	        for(JSONObject json : params){
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
				String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
				//校验参数
				//验证是否传递productId
				if(productId == null || productId.equals("")){
					throw new ServiceException("Enter the product you want to receive");
				}
				//验证价格是否符合正则
				if(unitCost != null && !unitCost.equals("")){
					String regexCost = "^(0|([1-9]\\d*))(\\.\\d+)?$";
					Pattern costPattern=Pattern.compile(regexCost);
					Matcher costMatcher=costPattern.matcher(unitCost);
					if(!costMatcher.matches()){
						throw new ServiceException("The input unitCost is illegal");
					}
				}
				//验证批次是否存在
				if(lotId != null && !lotId.equals("")){						
					GenericValue lot = delegator.findByPrimaryKey("Lot", UtilMisc.toMap("lotId", lotId));
					if(lot == null){
						throw new ServiceException("The lotId you entered does not exist");
					}
				}
				//验证productId是否存在
				GenericValue product = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
				if(product == null){
					throw new ServiceException("The product you entered doesn't exist");
				}
				//验证productId是否有变形之后的sku
				List<EntityExpr> exprsProductId = FastList.newInstance();
				exprsProductId.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
				List<GenericValue> productAssoc = delegator.findByCondition("ProductAssoc", EntityCondition.makeCondition(exprsProductId), null, null);
				if(productAssoc != null && productAssoc.size() > 0){
					throw new ServiceException("The specified parameter does not exist or the parameter is illegal");
				}
				//包装参数
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("productId", productId);
				param.put("inventoryItemTypeId", inventoryItemTypeId);
				param.put("itemDescription", itemDescription);
				if(datetimeReceived.equals("")){
					param.put("datetimeReceived", new Timestamp(new Date().getTime()));
				}else{
					param.put("datetimeReceived", datetimeReceived);
				}
				param.put("lotId", lotId);
				param.put("serialNumber", serialNumber);
				param.put("quantityAccepted", quantityAccepted);
				param.put("unitCost", unitCost);
				param.put("facilityId", facilityId);
				param.put("validateAccountingTags", validateAccountingTags);
				param.put("userLogin", userLogin);
				param.put("quantityRejected", quantityRejected);
				//判断是序列化还是非序列化
				if(inventoryItemTypeId.equals("SERIALIZED_INV_ITEM")){
					//如果是序列化,判断序列号不为空
					if(serialNumber.equals("")){
						throw new ServiceException("Please enter your serial number");
					}
					//获取序列�
					String[] serialNumbers = serialNumber.split(",");
					Set<String> set = new HashSet<String>();
					for(String str : serialNumbers){
					   set.add(str);
					}
					if(set.size() != serialNumbers.length){
						throw new ServiceException("IMEI can not repeat");
					}
					//判断是否重复序列�
					List<EntityExpr> exprs = FastList.newInstance();
					exprs.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
					exprs.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
					exprs.add(EntityCondition.makeCondition("inventoryItemTypeId", EntityOperator.EQUALS, "SERIALIZED_INV_ITEM"));		
					List<GenericValue> inventoryItems = delegator.findByCondition("InventoryItem", EntityCondition.makeCondition(exprs), null, null);
					for(String IMEI : serialNumbers){
						if(inventoryItems != null && inventoryItems.size()>0){
							for(GenericValue inventoryItem : inventoryItems){
								if(IMEI.equals(inventoryItem.getString("serialNumber"))){
									throw new ServiceException(IMEI + " Serial number already exists");
								}
							}
						}
						//循环插入数据
						param.put("serialNumber", IMEI);
						param.put("quantityAccepted", "1");
						dispacher.runSync("receiveInventoryProduct", param);
					}				
				}else{
					//验证接收数量满足要求
					String regex = "^[0-9]*[1-9][0-9]*$";
					Pattern p=Pattern.compile(regex);
					Matcher m=p.matcher(quantityAccepted);
					//验证正则
					if(!m.matches()){
						throw new ServiceException("The number of recipients must be integers");
					}
					dispacher.runSync("receiveInventoryProduct", param);
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
	 * 查询指定条件下的产品
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> productInquiry(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		
		JSONObject json = InventoryTransfer.makeParams(request);
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
		//拼接sql
		StringBuffer sql = new StringBuffer("SELECT x.PRODUCT_ID as productId ,x.INTERNAL_NAME as internalName,x.BRAND_NAME as brandName,x.PRODUCT_TYPE_ID as productTypeId, y.productAttributes FROM("
				+ " SELECT b.PRODUCT_ID,b.INTERNAL_NAME,b.BRAND_NAME,b.PRODUCT_TYPE_ID FROM("
				+ " SELECT PRODUCT_ID,INTERNAL_NAME,BRAND_NAME,PRODUCT_TYPE_ID FROM PRODUCT a WHERE a.PRODUCT_TYPE_ID = '"+productTypeId+"'");
		
		InventoryTransfer.makeCondition(sql, "a.PRODUCT_ID", productIdNum, productId, productIdStatu);
		InventoryTransfer.makeCondition(sql, "a.INTERNAL_NAME", internalNameNum, internalName, internalNameStatu);
		InventoryTransfer.makeCondition(sql, "a.BRAND_NAME", brandNameNum, brandName, brandNameStatu);	
		
		sql.append(" ) b  WHERE NOT EXISTS ( SELECT * FROM PRODUCT_ASSOC c WHERE c.PRODUCT_ID = b.PRODUCT_ID) ORDER BY b.PRODUCT_ID) x"
				+ " LEFT JOIN ( SELECT b.product_id,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) AS productAttributes FROM PRODUCT_FEATURE a"
				+ " LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID"
				+ " GROUP BY product_id) y ON x.PRODUCT_ID = y.PRODUCT_ID ");
		try {
			//执行sql，获取结果集
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
	 * 批次查询
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryLots(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		
		JSONObject json = InventoryTransfer.makeParams(request);
		//接收参数
		String lotId = (String) json.get("lotId");
		//String supplierPartyId = (String) json.get("supplierPartyId");
		String pageNum = (String) json.get("pageNum");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		String pageSize = (String) json.get("pageSize");
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		//拼接sql
		/*StringBuffer sql = new StringBuffer("select a.LOT_ID as lotId,b.PARTY_TYPE_ID as partyTypeId,b.PARTY_ID as partyId,c.GROUP_NAME as groupName,d.FIRST_NAME_LOCAL as personName from LOT a"
				+ " LEFT JOIN PARTY b on a.SUPPLIER_PARTY_ID=b.PARTY_ID "
				+ " LEFT JOIN PARTY_GROUP c on b.PARTY_ID=c.PARTY_ID "
				+ " LEFT JOIN PERSON d on b.PARTY_ID=d.PARTY_ID where 1=1 ");*/
		StringBuffer sql = new StringBuffer("select LOT_ID as lotId,CREATION_DATE AS createDate,EXPIRATION_DATE as expirationDate ,UOM_ID AS uomId, COMMENTS as comments ,QUANTITY as quantity from LOT where 1=1");
		if(lotId != null && !lotId.equals("")){
			sql.append(" AND LOT_ID like '%"+lotId+"%'");
		}
		/*if(supplierPartyId != null && !supplierPartyId.equals("")){
			sql.append(" AND a.SUPPLIER_PARTY_ID like '%"+supplierPartyId+"%'");
		}*/
		//查询结果
		try {
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), LotVo.class, delegator);
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
	 * 查询产品类型
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryProductType(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		//获取JDBC连接
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
	 * 查找特定的UOM
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryUomId(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<UomVo> uom = new ArrayList<UomVo>();
		String sql = "select UOM_ID as uomId,DESCRIPTION as description from UOM where UOM_TYPE_ID in ('WEIGHT_MEASURE', 'VOLUME_LIQ_MEASURE', 'VOLUME_DRY_MEASURE')";
		ResultSet rs = null;
		try {
			rs = processor.executeQuery(sql);
			while(rs.next()){
				UomVo uomVo = new UomVo();
				uomVo.setUomId(rs.getString("uomId"));
				uomVo.setDescription(rs.getString("description"));
				uom.add(uomVo);
			}
			result.put("result", uom);
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
	 * 创建批次
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> createLot(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result= new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = dispacher.getDelegator();
		//获取JDBC连接
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		JSONObject json = InventoryTransfer.makeParams(request);
		//接收参数
		String lotId = (String) json.get("lotId");
		String quantity = (String) json.get("quantity");
		String comments = (String) json.get("comments");
		String expirationDate = (String) json.get("expirationDate");
		String uomId = (String) json.get("uomId");
		
		if(quantity != null && !quantity.equals("")){
			//验证只能是正�
			String regexCost = "^(0|([1-9]\\d*))(\\.\\d+)?$";
			Pattern costPattern=Pattern.compile(regexCost);
			Matcher costMatcher=costPattern.matcher(quantity);
			if(!costMatcher.matches()){
				result.put("resultCode", -1);
				result.put("resultMsg", "The input quantity is illegal");
				return result;
			}
		}
		//包装参数
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("lotId", lotId);
		if(quantity != null && !quantity.equals("")){
			map.put("quantity", new BigDecimal(quantity));
		}else{
			map.put("quantity", new BigDecimal(0));
		}
		map.put("comments", comments);
		if(expirationDate != null && !expirationDate.equals("")){
			map.put("expirationDate", Timestamp.valueOf(expirationDate));
		}else{
			map.put("expirationDate", expirationDate);
		}
		map.put("uomId", uomId);
		
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		map.put("userLogin", userLogin);
		ResultSet rs = null;
		try{
			TransactionUtil.begin();
			if(lotId != null && !lotId.equals("")){
				//判断lotI d是否重复
				GenericValue lot = delegator.findByPrimaryKey("Lot", UtilMisc.toMap("lotId", lotId));
				if(lot != null){
					result.put("resultCode", -1);
					result.put("resultMsg", "The lotId you entered does not exist");
					return result;
				}
			}
			Map<String,Object> resultLots = dispacher.runSync("warehouse.createLot", map);
			//根据lotId查询批次
			String newLotId = (String) resultLots.get("lotId");
			rs = processor.executeQuery("SELECT LOT_ID AS lotId,LOT.CREATION_DATE AS createDate,QUANTITY as quantity,EXPIRATION_DATE as expirationDate,UOM_ID as uomId,COMMENTS as comments FROM LOT WHERE LOT_ID='"+newLotId+"'");
			while(rs.next()){
				LotVo lotVo = new LotVo();
				lotVo.setLotId(rs.getString("lotId"));
				lotVo.setCreateDate(rs.getString("createDate"));
				lotVo.setComments(rs.getString("comments"));
				lotVo.setExpirationDate(rs.getString("expirationDate"));
				lotVo.setQuantity(rs.getString("quantity"));
				lotVo.setUomId(rs.getString("uomId"));
				result.put("result", lotVo);
			}
			TransactionUtil.commit();
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");			
		} catch(Exception e){
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
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
	 * 获取接收产品参数对象集合
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
		
		String[] params = jsonStringBuffer.substring(1,jsonStringBuffer.length()-1).replace("},", "}~@~").split("~@~");
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(String param : params){
		list.add(JSON.parseObject(param));
		}
		return list;
	}
	
	/**
	 * 获取当前用户的所有安全组
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
	 * 验证是否有接收库存这个页面的访问权限
	 * @param request
	 * @param response
	 * @return
	 */
	public static String verifPermissions(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = userPermission(request, response);
		String permission = (String) map.get("result");
		if(permission != null && (permission.contains("WRHS_ADMIN") || permission.contains("WRHS_INV_RAI"))){
			return "success";
		}
        return "error";
	}
}

