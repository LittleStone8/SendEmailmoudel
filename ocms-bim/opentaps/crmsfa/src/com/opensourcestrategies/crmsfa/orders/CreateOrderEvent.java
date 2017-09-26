package com.opensourcestrategies.crmsfa.orders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.service.ServiceUtil;

import com.opensourcestrategies.crmsfa.orders.bean.CommonBean;
import com.opensourcestrategies.crmsfa.orders.bean.FindSalesChannel_bean;
import com.opensourcestrategies.crmsfa.orders.bean.ProductStoreVo;
import com.opensourcestrategies.crmsfa.orders.bean.SaleChannle;

public class CreateOrderEvent {

	/**
	 *  查询销售渠道 2017-3-23 chenshihua
	 */
	public static String findSalesChannel(HttpServletRequest request, HttpServletResponse response) {

		try {
			List<FindSalesChannel_bean> enumList = new ArrayList<FindSalesChannel_bean>();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> salesChannels = delegator.findByAnd("Enumeration",
					UtilMisc.toMap("enumTypeId", "ORDER_SALES_CHANNEL"));
			for (GenericValue genericValue : salesChannels) {
				FindSalesChannel_bean channel = new FindSalesChannel_bean();
				channel.setDescription((String) genericValue.get("description"));
				channel.setEnumCode((String) genericValue.get("enumCode"));
				channel.setEnumId((String) genericValue.get("enumId"));
				channel.setEnumTypeId((String) genericValue.get("enumTypeId"));
				enumList.add(channel);
			}
			request.setAttribute("enumList", enumList);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}

		return "success";
	}

	
	
	
	
	
	
	
	
	
	/**
	 *  查询销售渠道 2017-3-24 chenshihua
	 */
	public static Map<String, Object> findSalesStore(HttpServletRequest request, HttpServletResponse response) {
		try {
			TransactionUtil.begin();
			
			HttpSession session = request.getSession();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String username = (String) userLogin.get("userLoginId");

			Delegator delegator = (Delegator) request.getAttribute("delegator");
			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username), false);
			
			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId")));
			
//			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId") , "roleTypeId" , "MANAGER"));
			Set<String> storeId = new HashSet<String>();

			for (GenericValue genericValue : resultList) {
				storeId.add((String) genericValue.get("productStoreId"));
			}
			List<String> storeIds = new ArrayList<String>();
			storeIds.addAll(storeId);
			Collections.sort(storeIds);
			List<ProductStoreVo> list = new ArrayList<ProductStoreVo>();
			//Map<String, String> store = new HashMap<String, String>();
			for (String item : storeIds) {
				resultList = delegator.findByAnd("ProductStore", UtilMisc.toMap("productStoreId", item));
				for (GenericValue result : resultList) {
					ProductStoreVo productStoreVo = new ProductStoreVo();
					productStoreVo.setIssupportaccount(result.getString("issupportaccount"));
					productStoreVo.setStoreName(result.getString("storeName"));
					productStoreVo.setProductStoreId(result.getString("productStoreId"));
					//store.put((String) result.get("productStoreId"), (String) result.get("storeName"));
					list.add(productStoreVo);
				}
			}
			TransactionUtil.commit();
			Map<String, Object> result = ServiceUtil.returnSuccess("query success");
			result.put("store", list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return ServiceUtil.returnError("erro");
		}

	}
	
	
	
	
	
	
	
	
	/**
	 *  查询店铺所属的所有销售人员   2017-3-25 chenshihua
	 */
	public static Map<String, Object> findStoreSaleMens(HttpServletRequest request, HttpServletResponse response) {
		try {
			String storeId = request.getParameter("storeId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("productStoreId", storeId , "roleTypeId" , "SALES_REP"));
//			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("productStoreId" , storeId , "roleTypeId" , "SALES_REP" , "roleTypeId" , "MANAGER"));
			
			/*
			 * if(storeSaleMens.size() > 0 ){
			 *		for (GenericValue genericValue : resultList) {
			 *			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("partyId", genericValue.getString("partyId")), true);
			 *			storeSaleMens.add(genericValue.getString("partyId"));
			 *		}
			 *	}
			 */
			if(resultList.size() > 0){
				List<Object> storeSaleMens = new ArrayList<Object>();
				for (GenericValue genericValue : resultList) {
					String partyId = genericValue.getString("partyId");
					GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), true);
					if(person != null){
						String firstName = person.getString("firstName");
						String lastName = person.getString("lastName");
						String str = firstName + " " + lastName;
						CommonBean commonBean = new CommonBean();
						commonBean.setId(genericValue.getString("partyId"));
						commonBean.setDes(str);
						storeSaleMens.add(commonBean);
					}
				}
				Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
				resultMap.put("storeSaleMens", storeSaleMens);
				return resultMap;
			}else{
				return ServiceUtil.returnError("query neno");
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fails");
		}
	}

	 
	 
	 
	 
	 public static Map<String, Object> findSaleChannel(HttpServletRequest request, HttpServletResponse response){
		 try {
			 Delegator delegator = (Delegator) request.getAttribute("delegator");
			 List<GenericValue> salechannelList = delegator.findByAnd("ProductStoreGroup", UtilMisc.toMap("productStoreGroupTypeId", "PRICE_GROUP"));
			 Map<String, Object> resultMap ;
			 if(salechannelList.size() > 0){
				 resultMap = ServiceUtil.returnSuccess("query success");
				 List<Object> reslutList = new ArrayList<Object>();
				 
				 for (GenericValue genericValue : salechannelList) {
					 SaleChannle saleChannle = new SaleChannle();
					 saleChannle.setId(genericValue.getString("productStoreGroupId"));
					 saleChannle.setName(genericValue.getString("productStoreGroupName"));
					 reslutList.add(saleChannle);
				}
				 resultMap.put("reslutList", reslutList);
			 }else{
				 resultMap = ServiceUtil.returnError("There is not data"); 
			 }
			 return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query exception");
		}
	 }
	 public static Map<String, Object> findsalechannleByStoreId(HttpServletRequest request, HttpServletResponse response){
		 try {
			 
			String productStoreId = request.getParameter("productStoreId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> productStoreGroupMemberList = delegator.findByAnd("ProductStoreGroupMember", UtilMisc.toMap("productStoreId", productStoreId));
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			if(productStoreGroupMemberList.size() > 0 ){
				 List<Object> reslutList = new ArrayList<Object>();
				for (GenericValue productStoreGroupMember : productStoreGroupMemberList) {
					GenericValue productStoreGroup = delegator.findOne("ProductStoreGroup",  UtilMisc.toMap("productStoreGroupId", productStoreGroupMember.getString("productStoreGroupId")), false);
					SaleChannle saleChannle = new SaleChannle();
					if(!("PRICE_GROUP".equals(productStoreGroup.getString("productStoreGroupTypeId")))){
						continue;
					}
					saleChannle.setId(productStoreGroup.getString("productStoreGroupId"));
					saleChannle.setName(productStoreGroup.getString("productStoreGroupName"));
					reslutList.add(saleChannle);
				}
				resultMap.put("reslutList", reslutList);
			}
			 return resultMap;
			} catch (GenericEntityException e) {
				e.printStackTrace();
				return ServiceUtil.returnError("query exception");
			}
	 }
	 
}
