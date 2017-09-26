package org.opentaps.warehouse.inventoryChange;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.product.enums.UsageTypeEnum;
import org.ofbiz.service.LocalDispatcher;



public class PhysicalInventoryAndVariance {
	
	 public static String createPhysicalInventoryAndVariance(HttpServletRequest request, HttpServletResponse response) {
		 	String availableToPromiseVar = request.getParameter("availableToPromiseVar");
		 	String quantityOnHandVar = request.getParameter("quantityOnHandVar");
			LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
			Delegator delegator = (Delegator) dispacher.getDelegator();
		 	GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		 	if(availableToPromiseVar == null || availableToPromiseVar.equals("")){
		 		availableToPromiseVar = quantityOnHandVar;
		 	}
		 	try {
				GenericValue inventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId",request.getParameter("inventoryItemId")));
		 		
				dispacher.runSync("createPhysicalInventoryAndVariance", UtilMisc.toMap(
	                 "inventoryItemId", request.getParameter("inventoryItemId"),
	                 "varianceReasonId", request.getParameter("varianceReasonId"),
	                 "comments",request.getParameter("comments"),
	                 "availableToPromiseVar",new BigDecimal(availableToPromiseVar),
	                 "quantityOnHandVar", new BigDecimal(quantityOnHandVar),
	                 "operator",userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")",
	                 "usageType",UsageTypeEnum.Adjustment.getName(),
	                 "qoh",inventoryItem.getBigDecimal("quantityOnHandTotal").add(new BigDecimal(quantityOnHandVar)),
	                 "atp",inventoryItem.getBigDecimal("availableToPromiseTotal").add(new BigDecimal(availableToPromiseVar)),
	                 "userLogin", userLogin));
	            return "success";
	        } catch (Exception e) {
	            return "error";
	        }
	    }

	  
}
