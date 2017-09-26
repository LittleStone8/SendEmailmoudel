package org.opentaps.warehouse.inventoryChange;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ntp.TimeStamp;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;

import com.alibaba.fastjson.JSONObject;

public class BatchAddWarehouseManage {
	
	/**
	 * Gets a repository that has administrator roles
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> getWarehouseManageByPerson(HttpServletRequest request,HttpServletResponse response){
		Delegator delegator = (Delegator) request.getAttribute("delegator");		
		JSONObject json = InventoryTransfer.makeParams(request);
		String partyId = json.getString("partyId");			
		return getWarehouseManageByPartyId(partyId, delegator);
	}
	
	/**
	 * 
	 * @param partyId
	 * @param delegator
	 * @return
	 */
	public static Map<String,Object> getWarehouseManageByPartyId(String partyId,Delegator delegator){
		Map<String,Object> result = new HashMap<String, Object>();
		
		String sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID";
		try {
			List<FacilityVo> inventoryFacilitys = WarehouseCommon.getFacilityByPermission(delegator, sql);
			result.put("inventoryFacilitys", inventoryFacilitys);
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
	 * Fuzzy query for all person
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> queryPerson(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		JSONObject json = InventoryTransfer.makeParams(request);	
		String partyId = json.getString("partyId");
		String partyIdNum = json.getString("partyIdNum");
		String partyIdStatus = json.getString("partyIdStatus");
		
		String firstName = json.getString("firstName");
		String firstNameNum = json.getString("firstNameNum");
		String firstNameStatus = json.getString("firstNameStatus");
		
		String pageNum = (String) json.get("pageNum");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		String pageSize = (String) json.get("pageSize");
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		StringBuffer sql = new StringBuffer("SELECT PARTY_ID AS partyId,FIRST_NAME AS firstName FROM PERSON WHERE 1=1 ");
		InventoryTransfer.makeCondition(sql, "PARTY_ID", partyIdNum, partyId, partyIdStatus);
		InventoryTransfer.makeCondition(sql, "FIRST_NAME_LOCAL", firstNameNum, firstName, firstNameStatus);
		
		try{
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), PersonVo.class, delegator);
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
	 * Modify the identity of the warehouse administrator for some of the warehouses
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> addWarehouseManageByPerson(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		JSONObject json = InventoryTransfer.makeParams(request);
		String partyId = json.getString("partyId");
		String facilityIdArrays = json.getString("facilityId");
		
		Map<String,Object> map = getWarehouseManageByPartyId(partyId, delegator);
		List<FacilityVo> inventoryFacilitys = (List<FacilityVo>) map.get("inventoryFacilitys");
		List<String> existFacilityIds = new ArrayList<String>();
		for(FacilityVo faciityVo : inventoryFacilitys){
			existFacilityIds.add(faciityVo.getFacilityId());
		}
		
		String[] facilityIds = facilityIdArrays.split(",");
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
		String date = dateFormat.format(new Date());*/
		Timestamp date = new Timestamp(new Date().getTime());
		StringBuffer sql = new StringBuffer("INSERT INTO `FACILITY_PARTY_PERMISSION` (`FACILITY_ID`, `PARTY_ID`, `SECURITY_GROUP_ID`, `FROM_DATE`, `THRU_DATE`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ");
		boolean flag = false;
		try{
			TransactionUtil.begin();
			for(int i=0;i<facilityIds.length;i++){
				if(existFacilityIds.contains(facilityIds[i])){
					continue;
				}
				if(i != facilityIds.length - 1){
					sql.append("('"+facilityIds[i]+"', '"+partyId+"', 'WRHS_MANAGER', '"+date+"', NULL, '"+date+"', '"+date+"', '"+date+"', '"+date+"'),");			
				}else{
					sql.append("('"+facilityIds[i]+"', '"+partyId+"', 'WRHS_MANAGER', '"+date+"', NULL, '"+date+"', '"+date+"', '"+date+"', '"+date+"')");			
				}
				flag = true;
			}
			if(flag){
				processor.prepareStatement(sql.toString());
				processor.executeUpdate();
			}			
			result = getWarehouseManageByPartyId(partyId, delegator);
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
		} finally{
			try{			
				processor.close();
			}catch(Exception e){
				
			}
		}
		return result;
	}
	
	/**
	 * Remove the administrator identity of a repository
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String,Object> removeWarehouseManageByPerson(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		JSONObject json = InventoryTransfer.makeParams(request);
		String partyId = json.getString("partyId");
		String facilityId = json.getString("facilityId");
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
		String date = dateFormat.format(new Date());*/
		Timestamp date = new Timestamp(new Date().getTime());
		try{
			TransactionUtil.begin();
			String sql = "UPDATE FACILITY_PARTY_PERMISSION SET FACILITY_ID=FACILITY_ID, PARTY_ID=PARTY_ID, SECURITY_GROUP_ID=SECURITY_GROUP_ID, FROM_DATE=FROM_DATE, THRU_DATE='"+date+"', LAST_UPDATED_STAMP='"+date+"', LAST_UPDATED_TX_STAMP='"+date+"', CREATED_STAMP=CREATED_STAMP, CREATED_TX_STAMP=CREATED_TX_STAMP WHERE FACILITY_ID='"+facilityId+"' AND PARTY_ID='"+partyId+"';";			
			processor.prepareStatement(sql);
			processor.executeUpdate();
			result = getWarehouseManageByPartyId(partyId, delegator);
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
		} finally{
			try{			
				processor.close();
			}catch(Exception e){
				
			}
		}
		
		return result;
	}
}
