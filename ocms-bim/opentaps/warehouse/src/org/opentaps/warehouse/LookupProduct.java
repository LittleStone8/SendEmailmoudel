package org.opentaps.warehouse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.condition.EntityWhereString;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.order.finaccount.FinAccountHelper;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.warehouse.bean.findLookupProduct_Bean;
public class LookupProduct {

	/*
	 * wareHouse查询产品
	 * shenshihua
	 * 
	 */
	public static String findLookupProduct(HttpServletRequest request, HttpServletResponse response) {
		try {
			TransactionUtil.begin();
			
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

			List<String> fields = UtilMisc.toList("productId", "productTypeId", "brandName", "internalName");
			String locationSeqId = request.getParameter("locationSeqId");
			if(locationSeqId.equals("&parm0") || locationSeqId=="&parm0" || locationSeqId==""){
				locationSeqId = null;
			}
			
			
			List<EntityExpr> conditionList = UtilMisc.toList(
					EntityCondition.makeCondition(
						EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, null), EntityOperator.OR,
						EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, "Y")
					)
					);
			
			if(locationSeqId !=null && locationSeqId.length() > 0){
				conditionList.add(EntityCondition.makeCondition("locationSeqId", EntityOperator.EQUALS, locationSeqId));
			}
			
			EntityCondition conditions = EntityCondition.makeCondition(conditionList);
			EntityListIterator iterator = delegator.findListIteratorByCondition("ProductAndGoodIdentificationAndLocation",
					conditions, null, fields, null, UtilCommon.DISTINCT_READ_OPTIONS);
			
			GenericValue trans;
			ArrayList<findLookupProduct_Bean> resultList = new ArrayList<findLookupProduct_Bean>();
			
			while ((trans = iterator.next()) != null) {
				findLookupProduct_Bean prod = new findLookupProduct_Bean();
				prod.setProductId((String) trans.get("productId"));
				prod.setBrandName((String) trans.get("brandName"));
				prod.setInternalName((String) trans.get("internalName"));
				prod.setProductTypeId((String) trans.get("productTypeId"));
				resultList.add(prod);
			}
			request.setAttribute("resultList", resultList);
			TransactionUtil.commit();
			return "success";
		} catch (GenericEntityException e) {
			e.printStackTrace();
			 try {
                 TransactionUtil.rollback();
	         } catch (GenericTransactionException e2) {
	         request.setAttribute("_ERROR_MESSAGE_", e2.getMessage());
	         return "error";
	         }
			return "erro";
		}
	}

}
