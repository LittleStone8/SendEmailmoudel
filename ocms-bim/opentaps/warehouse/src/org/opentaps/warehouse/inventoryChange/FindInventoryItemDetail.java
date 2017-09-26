package org.opentaps.warehouse.inventoryChange;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.Delegator;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.foundation.service.ServiceException;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FindInventoryItemDetail {

	/**
	 * find inventoryItem detail
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	public static Map<String,Object> getInventoryItemDetail(HttpServletRequest request,HttpServletResponse response) throws ServiceException {
		Map<String,Object> result = new HashMap<String, Object>();
	    LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	    Delegator delegator = dispacher.getDelegator();
  	
        //get parameters
        try {
        	 JSONObject jsonObject = makeParamsToList(request);
	        if(jsonObject == null){
	        	result.put("resultCode", -1);
				result.put("resultMsg", "Please fill in the parameter");
	        }
	        
	        String pageNum = (String) jsonObject.get("pageNum");
			if(pageNum == null || pageNum.equals("")){
				pageNum = "1";
			}
			String pageSize = (String) jsonObject.get("pageSize");
			if(pageSize == null || pageSize.equals("")){
				pageSize = "10";
			}
	        
	        String inventoryItemId = (String)jsonObject.get("inventoryItemId");
	        StringBuffer sql = new StringBuffer("");
	        sql.append(" SELECT iid.INVENTORY_ITEM_DETAIL_SEQ_ID as inventoryItemDetailSeqId,iid.EFFECTIVE_DATE as effectiveDate,iid.OPERATOR as operator,p.FIRST_NAME_LOCAL as firstNameLocal, ");
	        sql.append(" iid.QOH as qty,iid.ATP as atp,iid.USAGE_TYPE as usageType,iid.TRANSFER_ID as transferId,iid.REASON_ENUM_ID as reasonEnumId,iid.FROM_WAREHOUSE as fromWarehouse,iid.TO_WAREHOUSE as toWarehouse,");
	        sql.append(" iid.QUANTITY_ON_HAND_DIFF as quantityOnHandDiff,iid.AVAILABLE_TO_PROMISE_DIFF as availableToPromiseDiff,iid.ORDER_ID as orderId ");
	        sql.append(" FROM INVENTORY_ITEM_DETAIL iid ");
	        sql.append(" LEFT JOIN PERSON p ON iid.OPERATOR = p.PARTY_ID ");
	        sql.append(" WHERE iid.INVENTORY_ITEM_ID = '" + inventoryItemId + "'");
	        sql.append(" ORDER BY iid.INVENTORY_ITEM_DETAIL_SEQ_ID DESC ");
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), InventoryItemDetailVo.class, delegator);
			List list = (List)result.get("result");
			if(!CollectionUtils.isEmpty(list)){
				for(int i = 0;i<list.size();i++){
					InventoryItemDetailVo vo = (InventoryItemDetailVo)list.get(i);
					vo.setEffectiveDate(DateUtils.getLocalizedDate(vo.getEffectiveDate(), request));
				}
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatternew = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i= 0;i<list.size();i++){
				InventoryItemDetailVo vo = (InventoryItemDetailVo)list.get(i);
				String recei = vo.getEffectiveDate();
				Date currentTime_2;
				try {
					currentTime_2 = formatter.parse(recei);
				} catch (Exception e) {
					continue;
				}
				vo.setEffectiveDate(formatternew.format(currentTime_2));
			}
			
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");		
	       
        }catch(Exception e){
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
			jsonObject =  JSON.parseObject(jsonStringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
