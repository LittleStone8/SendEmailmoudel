package org.opentaps.warehouse.inventoryChange;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.DispatchContext;
import org.opentaps.foundation.service.ServiceException;

public class FindInventoryItem {
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
	 * 获取facility的默认值以及可查询的仓
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
		//String userLoginId = (String) context.get("userLoginId");
		GenericValue userLogin = (GenericValue) context.get("userLogin");
		String userLoginId = userLogin.getString("userLoginId");
		String partyId = (String) context.get("partyId");
		ResultSet rs = null;
		try {
			List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);
			
			List<String> groupIds = new ArrayList<String>();
			for(GenericValue userSecurityGroup : userSecurityGroups){
				groupIds.add(userSecurityGroup.getString("groupId"));
			}
			//根据partyId获取对应的仓�	/*String sql = "SELECT FACILITY_ID,FACILITY_NAME FROM FACILITY b LEFT JOIN PRODUCT_STORE a ON a.INVENTORY_FACILITY_ID = b.FACILITY_ID LEFT JOIN PRODUCT_STORE_ROLE c ON a.PRODUCT_STORE_ID=c.PRODUCT_STORE_ID"
					//+ " WHERE( c.ROLE_TYPE_ID = 'STORE_MANAGER' OR c.ROLE_TYPE_ID = 'MANAGER') AND c.THRU_DATE IS NULL AND c.PARTY_ID = '"+partyId+"'";
			String sql = "";
			if(groupIds != null && groupIds.contains("WRHS_ADMIN")){
				sql = "select FACILITY_ID,FACILITY_NAME FROM FACILITY";
			}else{
				sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID";
			}
			
			List<ProductFromInventory> inventoryFacilitys = new ArrayList<ProductFromInventory>();
		
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
}
