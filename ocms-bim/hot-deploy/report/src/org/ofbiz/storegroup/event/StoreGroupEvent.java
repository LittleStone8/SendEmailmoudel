package org.ofbiz.storegroup.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreAndGroupBean;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.helper.StoreGroupHelper;
import org.ofbiz.util.ParaUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;

public class StoreGroupEvent {
	public static final String SGROUPID_AGENT = "AGENT";
	public static final String SGROUPID_RETAIL = "RETAIL";
	public static final String SGROUPID_WHOLESALE = "WHOLESALE";
	public static final String SGROUPID_B2R = "B2R";
	public static final String SGROUPID_DEFAULT = "DEFAULT";
	
	public static final String SGROUPID_MTN = "MTN_S";
	public static final String SGROUPID_BAN = "BANANA_S";
	public static final String SGROUPID_AIRTEL = "AIRTEL_S";
	public static final String SGROUPID_UTL = "UTL_S";
	public static final String SGROUPID_TEST = "TEST";
	
	public static final String SGROUPTYPE_STORE = "STORE_GROUP";
	public static final String SGROUPTYPE_PRICE = "PRICE_GROUP";

	// addyzl 20170511
	public static final String SGROUPTYPE_STORE_Warehouse = "STORE_GROUP_Warehouse";

	/**
	 * 依据条件查询店铺和店铺租关系
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchStoreAndGroupsByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String productStoreId = ParaUtils.getParameter(request,"StoreId");
		String productStoreGroupId = ParaUtils.getParameter(request,"StoreGroupId");
		
		List<StoreAndGroupBean> storeAndGroupBeans = FastList.newInstance();
		try {
			storeAndGroupBeans = StoreGroupHelper.searchSGMemberByConditions(delegator, productStoreId,productStoreGroupId);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search store and group error"); 
		}
		Map<String, Object> map = ServiceUtil.returnSuccess(); // 返回值map
		map.put("StoreAndSGroup", storeAndGroupBeans);
		return map;
	}

	/**
	 * 店铺和店铺租关系批量更改
	 * 先删除所有该店的店铺组，再重新添加
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> sGroupsBatchUpdates(HttpServletRequest request, HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = ParaUtils.getJsonByRequest(request);// 获取前台JSON
		
		if (UtilValidate.isEmpty(json)) {
			return ServiceUtil.returnError("info error");
		}
		String storeId = json.getString("StoreId");
		JSONArray sGroupjsonArray = json.getJSONArray("SGroupJSONArr");
		
		List<StoreGroupBean> sGroupBeens = sGroupjsonArray.toJavaList(StoreGroupBean.class);
		if (sGroupBeens == null || sGroupBeens.size() <= 0) {
			return ServiceUtil.returnError("infor error");
		}
		List<GenericValue> sGroupGVs = new ArrayList<GenericValue>();
		Timestamp createTime = new Timestamp(new Date().getTime());
		
		for (StoreGroupBean pageBeen : sGroupBeens) {
			String sGroupyId = pageBeen.getsGroupId();
			sGroupGVs.add(delegator.makeValue("ProductStoreGroupMember", UtilMisc.toMap("productStoreGroupId",
					sGroupyId, "productStoreId", storeId, "fromDate", createTime)));
		}
		
		
		Map<String, Object> map = ServiceUtil.returnSuccess(); // 返回值map
		try {
			TransactionUtil.begin();
			List<GenericValue> deleteGroupGVs = getDeleteGVs(delegator, storeId);
			StoreGroupHelper.removeAllSGMembers(delegator, deleteGroupGVs);
			StoreGroupHelper.storeAllSGMembers(delegator, sGroupGVs);
			TransactionUtil.commit();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return ServiceUtil.returnError("update error");
		}

		return map;
	}
	

	/**
	 * 所有待删除的店铺租
	 * @param delegator
	 * @param storeId
	 * @return
	 */
	private static List<GenericValue> getDeleteGVs(GenericDelegator delegator, String storeId) {
		List<GenericValue> sGroupGvs = FastList.newInstance();
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_AGENT, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_RETAIL, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_WHOLESALE, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_B2R, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_DEFAULT, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_MTN, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_BAN, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_UTL, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_AIRTEL, "productStoreId", storeId)));
		sGroupGvs.add(delegator.makeValue("ProductStoreGroupMember",
				UtilMisc.toMap("productStoreGroupId", SGROUPID_TEST, "productStoreId", storeId)));
		return sGroupGvs;
	}
}
