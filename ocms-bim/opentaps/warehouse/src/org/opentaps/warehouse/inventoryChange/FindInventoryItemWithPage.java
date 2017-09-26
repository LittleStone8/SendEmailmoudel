package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.foundation.service.ServiceException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FindInventoryItemWithPage {

	/**
	 * 查询库存带分页功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	public static Map<String, Object> getInventoryItemWithPage(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {
		Map<String, Object> result = new HashMap<String, Object>();
		LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = dispacher.getDelegator();
		// get parameters
		try {
			JSONObject jsonObject = makeParamsToList(request);
			if (jsonObject == null) {
				result.put("resultCode", -1);
				result.put("resultMsg", "Please fill in the parameter");
			}
			//分页
			String pageNum = (String) jsonObject.get("pageNum");
			if(pageNum == null || pageNum.equals("")){
				pageNum = "1";
			}
			String pageSize = (String) jsonObject.get("pageSize");
			if(pageSize == null || pageSize.equals("")){
				pageSize = "10";
			}
			
			//接收参数
			String productId = (String) jsonObject.get("productId");
			String internalName = (String) jsonObject.get("internalName");
			String serialNumber = (String) jsonObject.get("serialNumber");
			String lotId = (String) jsonObject.get("lotId");
			String facilityIdToFind = (String) jsonObject.get("facilityIdToFind");
			
			if(StringUtils.isEmpty(facilityIdToFind)){
				result.put("resultCode", -1);
				result.put("resultMsg", "facility is empty,Please fill in the parameter");
			}
			
			String orderBy=(String) jsonObject.get("orderBy");
			//qty是否需要包含0
			String flag = (String) jsonObject.get("flag");
			
			
			//拼接sql
			StringBuffer sql = new StringBuffer("SELECT "
					+ "x.INVENTORY_ITEM_ID as inventoryItemId,"
					+ "x.SERIAL_NUMBER as serialNumber,"
					+ "x.LOT_ID as lotId,"
					+ "x.DATETIME_RECEIVED as datetimeReceived,"
					+ "x.STATUS_ID as statusId,"
					+ "x.QUANTITY_ON_HAND_TOTAL as quantityOnHandTotal,"
					+ "x.product_Id as productId,"
					+ "x.INTERNAL_NAME as internalName,"
					+ " y.DESCRIPTION ,"
					+ "x.FACILITY_NAME as facilityName FROM (SELECT "
					+ " a.INVENTORY_ITEM_ID,"
					+ "a.SERIAL_NUMBER,"
					+ "a.LOT_ID,"
					+ "a.DATETIME_RECEIVED,"
					+ "a.STATUS_ID,"
					+ "a.QUANTITY_ON_HAND_TOTAL,"
					+ "a.product_Id,"
					+ "b.INTERNAL_NAME ,t_f.FACILITY_NAME as FACILITY_NAME"
					+ " FROM INVENTORY_ITEM a LEFT JOIN PRODUCT b ON a.PRODUCT_ID = b.PRODUCT_ID LEFT JOIN GOOD_IDENTIFICATION gi ON gi.PRODUCT_ID=a.PRODUCT_ID left join FACILITY t_f on t_f.FACILITY_ID=a.FACILITY_ID WHERE (gi.GOOD_IDENTIFICATION_TYPE_ID = 'EAN' OR gi.GOOD_IDENTIFICATION_TYPE_ID IS NULL) AND (a.STATUS_ID IS NULL OR a.STATUS_ID = 'INV_AVAILABLE' OR a.STATUS_ID = 'IXF_COMPLETE' ");
			if(flag != null && "Y".equals(flag)){
				sql.append(" OR a.STATUS_ID = 'INV_PROMISED' OR a.STATUS_ID = 'INV_DEACTIVATED'");
			}
			StringBuilder facilitySql= new StringBuilder(" )AND a.FACILITY_ID in (");
			if(StringUtils.isNotBlank(facilityIdToFind)){
				String[] facilityIds=facilityIdToFind.split(",");
				for(String facilityId:facilityIds){
					facilitySql.append("'"+facilityId.trim()+"',");
				}
			}
			sql.append(facilitySql.toString().substring(0, facilitySql.length()-1)+")");
			
			if(flag != null && "N".equals(flag)){
				sql.append(" AND a.QUANTITY_ON_HAND_TOTAL>0");
			}
			if(productId != null && !productId.equals("")){
				sql.append(" AND a.product_id='"+productId+"'");
			}
			if(internalName != null && !internalName.equals("")){
				sql.append(" AND b.internal_Name like '%"+internalName+"%'");
			}
			
			if(serialNumber != null && !serialNumber.equals("")){
				System.out.println(serialNumber.length() == 15);
				if(serialNumber.length() == 15){
					sql.append(" AND a.serial_Number like '%"+serialNumber+"%'");
				}else{
					sql.append(" AND gi.ID_VALUE like '%"+serialNumber+"%'");
				}
			}
			
			if(lotId != null && !lotId.equals("")){
				sql.append(" AND a.lot_Id='"+lotId+"'");
			}
			sql.append(") x LEFT JOIN ( SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) y ON x.product_Id = y.product_id");
			
			if(orderBy != null && !orderBy.equals("")){
				sql.append(" order by FACILITY_NAME," + orderBy);
			}
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), InventoryItemVo.class, delegator);
			
			List l = (List)result.get("result");
			for(int i= 0;i<l.size();i++){
				InventoryItemVo vo = (InventoryItemVo)l.get(i);
				vo.setDatetimeReceived(DateUtils.getLocalizedDate(vo.getDatetimeReceived(), request));
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatternew = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i= 0;i<l.size();i++){
				InventoryItemVo vo = (InventoryItemVo)l.get(i);
				String recei = vo.getDatetimeReceived();
				Date currentTime_2;
				try {
					currentTime_2 = formatter.parse(recei);
				} catch (Exception e) {
					continue;
				}
				vo.setDatetimeReceived(formatternew.format(currentTime_2));
			}
			
			
			
			GenericValue facility = delegator.findByPrimaryKey("Facility", UtilMisc.toMap("facilityId",facilityIdToFind));
			if(facility != null)
			result.put("selectFacilityName", facility.getString("facilityName"));
			
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
			String userLoginId = userLogin.getString("userLoginId");
			List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);			
			List<String> groupIds = new ArrayList<String>();
			for(GenericValue userSecurityGroup : userSecurityGroups){
				groupIds.add(userSecurityGroup.getString("groupId"));
			}
			if(groupIds.contains("WRHS_INV_EDIT") || groupIds.contains("WRHS_ADMIN")){
				result.put("showEditPage", true);
			}else{
				result.put("showEditPage", false);
			}
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}

	public static JSONObject makeParamsToList(HttpServletRequest request) {
		StringBuffer jsonStringBuffer = new StringBuffer();
		BufferedReader reader;
		JSONObject jsonObject = null;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
			jsonObject = JSON.parseObject(jsonStringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
