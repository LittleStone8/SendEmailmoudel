package org.opentaps.warehouse.inventoryChange;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.LocalDispatcher;

import com.alibaba.fastjson.JSONObject;

public class ManageLot {
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
}
