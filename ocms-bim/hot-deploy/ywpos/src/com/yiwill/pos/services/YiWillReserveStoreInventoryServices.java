package com.yiwill.pos.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.product.enums.UsageTypeEnum;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

public class YiWillReserveStoreInventoryServices {

	public static final String module = YiWillReserveStoreInventoryServices.class.getName();

	public static final String resource_error = "OrderErrorUiLabels";

	public static Map<String, Object> reserveStoreInventory(DispatchContext dctx, Map<String, Object> context)
			throws GeneralException {

		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dctx.getDelegator();

		GenericValue userLogin = (GenericValue) context.get("userLogin");
//		YiWillCart cart = (YiWillCart) context.get("cart");

		java.sql.Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
		
		String imei = (String) context.get("imei");
		
		Boolean isNeedShip = (Boolean) context.get("isNeedShip");
		
		String productId = (String) context.get("productId");
		String productStoreId = (String) context.get("productStoreId");
		String orderId = (String) context.get("orderId");
		BigDecimal quantity = (BigDecimal) context.get("quantity");
		String status = (String) context.get("status");
		String orderItemSeqId = (String) context.get("orderItemSeqId");

		GenericValue product = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
		
		GenericValue productStore = delegator.findByPrimaryKey("ProductStore",UtilMisc.toMap("productStoreId", productStoreId));

		GenericValue orderHeader = delegator.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));

		if (productStore == null) {
			return null;
		}

		String reserveInventory = productStore.getString("reserveInventory");
		if (reserveInventory.equals("N")) {
			return null;
		}

		String inventoryFacilityId = productStore.getString("inventoryFacilityId");
		
		String oneInventoryFacility = productStore.getString("oneInventoryFacility");
		if (reserveInventory.equals("N")) {

		
			String reserveOrderEnumId = productStore.getString("reserveOrderEnumId");

		}
		
		GenericValue productType = product.getRelatedOne("ProductType");
		
		if (productType != null && productType.getString("isPhysical").equals("N")) {

		} else {
			
			
			String reserveOrderEnumId = "";
			
			String orderByString = "+datetimeReceived";
			if (reserveOrderEnumId.equals("INVRO_GUNIT_COST")) {
				orderByString = "-unitCost";
			} else if (reserveOrderEnumId.equals("INVRO_LUNIT_COST")) {
				orderByString = "+unitCost";
			} else if (reserveOrderEnumId.equals("INVRO_FIFO_EXP")) {
				orderByString = "+expireDate";
			} else if (reserveOrderEnumId.equals("INVRO_LIFO_EXP")) {
				orderByString = "-expireDate";
			} else if (reserveOrderEnumId.equals("INVRO_LIFO_REC")) {
				orderByString = "-datetimeReceived";
			} 
			if(imei != null && !imei.equals("")){
				EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
						EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei));
				List<GenericValue> inventoryItemList = delegator.findByCondition("InventoryItem",
						conditions, null, null);
				for (GenericValue inventoryItem : inventoryItemList) {
					String statusId = inventoryItem.getString("statusId");
					if ("INV_AVAILABLE".equals(statusId)) {
						
						inventoryItem.set("statusId", "INV_PROMISED");
					
						String inventoryItemId = inventoryItem.getString("inventoryItemId");
						GenericValue orderItemShipGrpInvRes = delegator.makeValue("OrderItemShipGrpInvRes");
					        orderItemShipGrpInvRes.set("orderId", orderId);
					        orderItemShipGrpInvRes.set("shipGroupSeqId", "00001");
					        orderItemShipGrpInvRes.set("orderItemSeqId", orderItemSeqId);
					        orderItemShipGrpInvRes.set("inventoryItemId", inventoryItemId);
					        orderItemShipGrpInvRes.set("reserveOrderEnumId", "INVRO_FIFO_REC");
					        orderItemShipGrpInvRes.set("quantity", BigDecimal.ONE);
					        orderItemShipGrpInvRes.set("reservedDatetime", UtilDateTime.nowTimestamp());
					        orderItemShipGrpInvRes.set("createdDatetime", UtilDateTime.nowTimestamp());
					        orderItemShipGrpInvRes.set("promisedDatetime", UtilDateTime.getTimestamp(new Date().getTime()+1000*60*60*24*5));
	//				        toBeStored.add(orderItemShipGrpInvRes);
					        orderItemShipGrpInvRes.create();
					        
					        GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
						inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));
						inventoryItemDetail.set("orderId", orderId);
						
						inventoryItemDetail.set("effectiveDate", nowTimestamp);
						
						inventoryItemDetail.set("inventoryItemId", inventoryItemId);
						inventoryItemDetail.set("orderItemSeqId", inventoryItem.getString("orderItemSeqId"));
						inventoryItemDetail.set("availableToPromiseDiff", BigDecimal.ONE.negate());
						
						if(isNeedShip != null && isNeedShip.equals(false)){
						    inventoryItemDetail.set("qoh", BigDecimal.ZERO);
						    inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ONE.negate());
						    if("ORDER_COMPLETED".equals(status)){
						    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompleted.getName());
						    }else{
						    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompletedWithoutPayment.getName());
						    }
						}else{
						    inventoryItemDetail.set("qoh",BigDecimal.ONE);
						    inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
						    inventoryItemDetail.set("usageType", UsageTypeEnum.OrderPayed.getName());
						}

						inventoryItemDetail.set("atp", BigDecimal.ZERO);
						inventoryItemDetail.set("accountingQuantityDiff", BigDecimal.ZERO);
						inventoryItemDetail.set("operator", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")");
						inventoryItemDetail.create();
						
						if(isNeedShip != null && isNeedShip.equals(false)){
						    inventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
						}
						inventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
						inventoryItem.store();
						
						quantity = quantity.subtract(BigDecimal.ONE);
					}
				}
				
				List<GenericValue> toBeStored = new ArrayList<GenericValue>();
				
				GenericValue orderRoleSalesRep = delegator.makeValue("OrderRole");
				orderRoleSalesRep.set("orderId", orderId);
				orderRoleSalesRep.set("partyId", orderHeader.getString("salesId"));
				orderRoleSalesRep.set("roleTypeId", "SALES_REP");
				toBeStored.add(orderRoleSalesRep);
				//SHIP_TO_CUSTOMER
				GenericValue orderRoleShipToCustomer = delegator.makeValue("OrderRole");
				orderRoleShipToCustomer.set("orderId", orderId);
				orderRoleShipToCustomer.set("partyId", orderHeader.getString("buyerId"));
				orderRoleShipToCustomer.set("roleTypeId", "SHIP_TO_CUSTOMER");
				toBeStored.add(orderRoleShipToCustomer);
				//BILL_TO_CUSTOMER
				GenericValue orderRoleBillToCustomer = delegator.makeValue("OrderRole");
				orderRoleBillToCustomer.set("orderId", orderId);
				orderRoleBillToCustomer.set("partyId", orderHeader.getString("buyerId"));
				orderRoleBillToCustomer.set("roleTypeId", "BILL_TO_CUSTOMER");
				toBeStored.add(orderRoleBillToCustomer);
				//SHIP_FROM_VENDOR
				GenericValue orderRoleShipFromVendor = delegator.makeValue("OrderRole");
				orderRoleShipFromVendor.set("orderId", orderId);
				orderRoleShipFromVendor.set("partyId", "EGATEE");
				orderRoleShipFromVendor.set("roleTypeId", "SHIP_FROM_VENDOR");
				toBeStored.add(orderRoleShipFromVendor);
				//BILL_FROM_VENDOR
				GenericValue orderRoleBillFromVendor = delegator.makeValue("OrderRole");
				orderRoleBillFromVendor.set("orderId", orderId);
				orderRoleBillFromVendor.set("partyId", orderHeader.getString("buyerId"));
				orderRoleBillFromVendor.set("roleTypeId", "BILL_FROM_VENDOR");
				toBeStored.add(orderRoleBillFromVendor);
				
				GenericValue orderRolePlacingCustomer = delegator.makeValue("OrderRole");
				orderRolePlacingCustomer.set("orderId", orderId);
				orderRolePlacingCustomer.set("partyId", orderHeader.getString("buyerId"));
				orderRolePlacingCustomer.set("roleTypeId", "PLACING_CUSTOMER");
				toBeStored.add(orderRolePlacingCustomer);
				
				delegator.storeAll(toBeStored);
				
			}else{
			
			EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
					EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
					EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, inventoryFacilityId),
					EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, 0));
			
			
			List<GenericValue> inventoryItemList = delegator.findByCondition("InventoryItem",
					conditions, null, UtilMisc.toList(orderByString));
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			
			
			for (GenericValue inventoryItem : inventoryItemList) {
				
				if(quantity.compareTo(BigDecimal.ZERO) > 0){
					
					String inventoryItemTypeId = inventoryItem.getString("inventoryItemTypeId");
	
					String statusId = inventoryItem.getString("statusId");
	
					if ("SERIALIZED_INV_ITEM".equals(inventoryItemTypeId)) {
						if ("INV_AVAILABLE".equals(statusId)) {
							inventoryItem.set("statusId", "INV_PROMISED");
						
							
							
							String inventoryItemId = inventoryItem.getString("inventoryItemId");
							GenericValue orderItemShipGrpInvRes = delegator.makeValue("OrderItemShipGrpInvRes");
						        orderItemShipGrpInvRes.set("orderId", orderId);
						        orderItemShipGrpInvRes.set("shipGroupSeqId", "00001");
						        orderItemShipGrpInvRes.set("orderItemSeqId", orderItemSeqId);
						        orderItemShipGrpInvRes.set("inventoryItemId", inventoryItemId);
						        orderItemShipGrpInvRes.set("reserveOrderEnumId", "INVRO_FIFO_REC");
						        orderItemShipGrpInvRes.set("quantity", BigDecimal.ONE);
						        orderItemShipGrpInvRes.set("reservedDatetime", UtilDateTime.nowTimestamp());
						        orderItemShipGrpInvRes.set("createdDatetime", UtilDateTime.nowTimestamp());
						        orderItemShipGrpInvRes.set("promisedDatetime", UtilDateTime.getTimestamp(new Date().getTime()+1000*60*60*24*5));
						        
						        orderItemShipGrpInvRes.create();
//						        toBeStored.add(orderItemShipGrpInvRes);
						        
						        
							GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
							inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));
							inventoryItemDetail.set("orderId", orderId);
							
							inventoryItemDetail.set("effectiveDate", nowTimestamp);
							
							inventoryItemDetail.set("inventoryItemId", inventoryItemId);
							inventoryItemDetail.set("orderItemSeqId", inventoryItem.getString("orderItemSeqId"));
							inventoryItemDetail.set("availableToPromiseDiff", BigDecimal.ONE.negate());
							
							if(isNeedShip != null && isNeedShip.equals(false)){
							    inventoryItemDetail.set("qoh", BigDecimal.ZERO);
							    inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ONE.negate());
							    if("ORDER_COMPLETED".equals(status)){
							    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompleted.getName());
							    }else{
							    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompletedWithoutPayment.getName());
							    }
							}else{
							    inventoryItemDetail.set("qoh",BigDecimal.ONE);
							    inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
							    inventoryItemDetail.set("usageType", UsageTypeEnum.OrderPayed.getName());
							}

							inventoryItemDetail.set("atp", BigDecimal.ZERO);
							inventoryItemDetail.set("accountingQuantityDiff", BigDecimal.ZERO);
							inventoryItemDetail.set("operator", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")");
							
							if(isNeedShip != null && isNeedShip.equals(false)){
							    inventoryItem.set("availableToPromiseTotal",BigDecimal.ZERO);
							}
							
							if(isNeedShip != null && isNeedShip.equals(false)){
							    inventoryItem.set("quantityOnHandTotal", BigDecimal.ZERO);
							}
							inventoryItem.set("availableToPromiseTotal", BigDecimal.ZERO);
							
							inventoryItemDetail.create();
							
							inventoryItem.store();
							quantity = quantity.subtract(BigDecimal.ONE);
						}
					}
	
					if ("NON_SERIAL_INV_ITEM".equals(inventoryItemTypeId)) {
						if ("INV_NS_ON_HOLD".equals(statusId) || "INV_NS_DEFECTIVE".equals(statusId) || "IXF_REQUESTED".equals(statusId)) {
							continue;
						}
						BigDecimal availableToPromiseTotal = inventoryItem.getBigDecimal("availableToPromiseTotal");
						BigDecimal quantityOnHandTotal = inventoryItem.getBigDecimal("quantityOnHandTotal");
	
						if (availableToPromiseTotal != null && availableToPromiseTotal.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal deductAmount;
							if (quantity.compareTo(availableToPromiseTotal) > 0) {
								deductAmount = availableToPromiseTotal;
							}else {
								deductAmount = quantity;
							}
	
							String inventoryItemId = inventoryItem.getString("inventoryItemId");
							
							
							System.out.println("inventoryItemId @## "+inventoryItemId);
							
							GenericValue inventoryItemDetail = delegator.makeValue("InventoryItemDetail");
							inventoryItemDetail.set("inventoryItemDetailSeqId", delegator.getNextSeqId("InventoryItemDetail"));
							inventoryItemDetail.set("orderId", orderId);
							
							inventoryItemDetail.set("effectiveDate", nowTimestamp);
							
							inventoryItemDetail.set("inventoryItemId", inventoryItemId);
							inventoryItemDetail.set("orderItemSeqId", inventoryItem.getString("orderItemSeqId"));
							inventoryItemDetail.set("availableToPromiseDiff", deductAmount.negate());
							
							if(isNeedShip != null && isNeedShip.equals(false)){
							    BigDecimal qoh = quantityOnHandTotal.subtract(deductAmount);
							    inventoryItemDetail.set("qoh", qoh);
							    inventoryItemDetail.set("quantityOnHandDiff", deductAmount.negate());
							    if("ORDER_COMPLETED".equals(status)){
							    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompleted.getName());
							    }else{
							    	inventoryItemDetail.set("usageType", UsageTypeEnum.OrderCompletedWithoutPayment.getName());
							    }
							}else{
							    inventoryItemDetail.set("qoh",quantityOnHandTotal);
							    inventoryItemDetail.set("quantityOnHandDiff", BigDecimal.ZERO);
							    inventoryItemDetail.set("usageType", UsageTypeEnum.OrderPayed.getName());
							}
							   BigDecimal atp = availableToPromiseTotal.subtract(deductAmount);
							inventoryItemDetail.set("atp", atp);
							inventoryItemDetail.set("accountingQuantityDiff", BigDecimal.ZERO);
//							inventoryItemDetail.create();
							inventoryItemDetail.set("operator", userLogin.getString("userLoginId")+"("+userLogin.getString("partyId")+")");
							toBeStored.add(inventoryItemDetail);
							if(isNeedShip != null && isNeedShip.equals(false)){
							    inventoryItem.set("quantityOnHandTotal", quantityOnHandTotal.subtract(deductAmount));
							}
							
							inventoryItem.set("availableToPromiseTotal", availableToPromiseTotal.subtract(deductAmount));
//							inventoryItem.store();
							
							toBeStored.add(inventoryItem);
							
							quantity = quantity.subtract(deductAmount);
							
							
							//添加运送OrderItemShipGrpInvRes		chenshihua	2017-6-2
							GenericValue orderItemShipGrpInvRes = delegator.makeValue("OrderItemShipGrpInvRes");
					        orderItemShipGrpInvRes.set("orderId", orderId);
					        orderItemShipGrpInvRes.set("shipGroupSeqId", "00001");
					        orderItemShipGrpInvRes.set("orderItemSeqId", orderItemSeqId);
					        orderItemShipGrpInvRes.set("inventoryItemId", inventoryItemId);
					        orderItemShipGrpInvRes.set("reserveOrderEnumId", "INVRO_FIFO_REC");
					        orderItemShipGrpInvRes.set("quantity", deductAmount);
					        orderItemShipGrpInvRes.set("reservedDatetime", UtilDateTime.nowTimestamp());
					        orderItemShipGrpInvRes.set("createdDatetime", UtilDateTime.nowTimestamp());
					        orderItemShipGrpInvRes.set("promisedDatetime", UtilDateTime.getTimestamp(new Date().getTime()+1000*60*60*24*5));
				          
					        orderItemShipGrpInvRes.create();
//					        toBeStored.add(orderItemShipGrpInvRes);
							
							//添加运送OrderItemShipGrpInvRes		chenshihua	2017-6-2
//							GenericValue orderItemShipGrpInvRes = delegator.makeValue("OrderItemShipGrpInvRes");
//							orderItemShipGrpInvRes.set("orderId", orderId);
//							orderItemShipGrpInvRes.set("shipGroupSeqId", "00001");
//							orderItemShipGrpInvRes.set("orderItemSeqId", inventoryItem.getString("orderItemSeqId"));
//							orderItemShipGrpInvRes.set("inventoryItemId", inventoryItemId);
//							orderItemShipGrpInvRes.set("reserveOrderEnumId", "INVRO_FIFO_REC");
//							orderItemShipGrpInvRes.set("quantity", deductAmount);
//							int cost = 0;
////							BigDecimal estimatedShipCost = (BigDecimal)cost;
//							Map<String, Object> contentmap = new HashMap<String, Object>();
//							contentmap.put("orderId", orderId);
//							contentmap.put("shipGroupSeqId", "00001");
//							contentmap.put("orderItemSeqId", orderItemSeqId);
//							contentmap.put("inventoryItemId", inventoryItemId);
//							contentmap.put("reserveOrderEnumId", "INVRO_FIFO_REC");
//							contentmap.put("quantity", deductAmount);
//							contentmap.put("carrierPartyId", "Egatee");
//							contentmap.put("shipmentMethodTypeId", "Standard");
////							contentmap.put("estimatedShipCost", cost);
////							ShippingHelper.Acceptcontentparameters(contentmap);
//							Map<String, Object> result = dispatcher.runSync("Acceptcontentparameters", contentmap);
//							if(result.get("ret") == "false" || result.get("ret").equals("false")){
//								return ServiceUtil.returnError("Save OrderItemShipGrpInvRes failed ");
//							}
						}
					}
				}	
				
			}
			//添加orderrole
			//SALES_REP
			GenericValue orderRoleSalesRep = delegator.makeValue("OrderRole");
			orderRoleSalesRep.set("orderId", orderId);
			orderRoleSalesRep.set("partyId", orderHeader.getString("salesId"));
			orderRoleSalesRep.set("roleTypeId", "SALES_REP");
			toBeStored.add(orderRoleSalesRep);
			//SHIP_TO_CUSTOMER
			GenericValue orderRoleShipToCustomer = delegator.makeValue("OrderRole");
			orderRoleShipToCustomer.set("orderId", orderId);
			orderRoleShipToCustomer.set("partyId", orderHeader.getString("buyerId"));
			orderRoleShipToCustomer.set("roleTypeId", "SHIP_TO_CUSTOMER");
			toBeStored.add(orderRoleShipToCustomer);
			//BILL_TO_CUSTOMER
			GenericValue orderRoleBillToCustomer = delegator.makeValue("OrderRole");
			orderRoleBillToCustomer.set("orderId", orderId);
			orderRoleBillToCustomer.set("partyId", orderHeader.getString("buyerId"));
			orderRoleBillToCustomer.set("roleTypeId", "BILL_TO_CUSTOMER");
			toBeStored.add(orderRoleBillToCustomer);
			//SHIP_FROM_VENDOR
			GenericValue orderRoleShipFromVendor = delegator.makeValue("OrderRole");
			orderRoleShipFromVendor.set("orderId", orderId);
			orderRoleShipFromVendor.set("partyId", "EGATEE");
			orderRoleShipFromVendor.set("roleTypeId", "SHIP_FROM_VENDOR");
			toBeStored.add(orderRoleShipFromVendor);
			//BILL_FROM_VENDOR
			GenericValue orderRoleBillFromVendor = delegator.makeValue("OrderRole");
			orderRoleBillFromVendor.set("orderId", orderId);
			orderRoleBillFromVendor.set("partyId", orderHeader.getString("buyerId"));
			orderRoleBillFromVendor.set("roleTypeId", "BILL_FROM_VENDOR");
			toBeStored.add(orderRoleBillFromVendor);
			
			GenericValue orderRolePlacingCustomer = delegator.makeValue("OrderRole");
			orderRolePlacingCustomer.set("orderId", orderId);
			orderRolePlacingCustomer.set("partyId", orderHeader.getString("buyerId"));
			orderRolePlacingCustomer.set("roleTypeId", "PLACING_CUSTOMER");
			toBeStored.add(orderRolePlacingCustomer);
			
			delegator.storeAll(toBeStored);
		}
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		result.put("quantityNotReserved", quantity);
		
		return result;
	
	}
}
