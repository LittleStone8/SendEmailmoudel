//package org.ofbiz.webpos.shopping.bean;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.ofbiz.base.util.UtilMisc;
//import org.ofbiz.base.util.UtilNumber;
//import org.ofbiz.base.util.UtilProperties;
//import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.entity.GenericValue;
//import org.ofbiz.order.order.OrderReadHelper;
//import org.ofbiz.order.shoppingcart.CartItemModifyException;
//import org.ofbiz.order.shoppingcart.ShoppingCartItem;
//import org.ofbiz.webpos.shopping.ShopCart;
//
//import javolution.util.FastList;
//
//public class OrderItemHelper {
//
//	
//	
//	  public static final int scale = 2;
//	  public static final int rounding = BigDecimal.ROUND_HALF_UP;
//	  public static final BigDecimal ZERO = (BigDecimal.ZERO).setScale(scale, rounding);
//	  public static final BigDecimal percentage = (new BigDecimal("0.01")).setScale(scale, rounding);
//	
//	private List<OrderItem> orderItemList;
//	private List<GenericValue> orderItemG;
//	
//	
//	public List<OrderItem> getOrderItemList() {
//		return orderItemList;
//	}
//
//	public void setOrderItemList(List<OrderItem> orderItemList) {
//		this.orderItemList = orderItemList;
//	}
//
//	public List<GenericValue> getOrderItemG() {
//		return orderItemG;
//	}
//
//	public void setOrderItemG(List<GenericValue> orderItemG) {
//		this.orderItemG = orderItemG;
//	}
//
//	public OrderItemHelper(List<OrderItem> orderItemList) {
//		this.orderItemList = orderItemList;
//	}
//
//	public void clearOrderItem() {
//		orderItemList.clear();
//	}
//	
//	
//	public void clearAllItemStatus() {
//		 Iterator lineIter = orderItemList.iterator();
//        while (lineIter.hasNext()) {
//        	OrderItem item = (OrderItem) lineIter.next();
//            item.setStatusId(null);
//        }
//	}
//	
//	private BigDecimal getBasePrice(OrderItem item) {
//		 BigDecimal unitPrice = item.getUnitPrice();
//		 return unitPrice;
//    }
//	 
//	private BigDecimal getRentalAdjustment(OrderItem item) {
//	        return BigDecimal.ONE;
//	    }
//	 
//	 
//	private BigDecimal getOtherAdjustments(OrderItem item) {
//			
//		BigDecimal quantity = item.getQuantity();
//		BigDecimal unitPrice = getBasePrice(item);
//		
//		
//		  BigDecimal adjTotal = ZERO;
//
////	        if (UtilValidate.isNotEmpty(adjustments)) {
////	            List<GenericValue> filteredAdjs = filterOrderAdjustments(adjustments, includeOther, includeTax, includeShipping, forTax, forShipping);
////	            Iterator<GenericValue> adjIt = filteredAdjs.iterator();
////
////	            while (adjIt.hasNext()) {
////	                GenericValue orderAdjustment = adjIt.next();
////
////	                adjTotal = adjTotal.add(OrderReadHelper.calcItemAdjustment(orderAdjustment, quantity, unitPrice));
////	            }
////	        }
//	        return adjTotal;
//	        
//	        
//	    }
//	 
//	 
//	 public BigDecimal getItemSubTotal(OrderItem item) {
//		 BigDecimal unitPrice = item.getUnitPrice();
//		 BigDecimal quantity = item.getQuantity();
//		 
//         return unitPrice.multiply(quantity).multiply(getRentalAdjustment(item)).add(getOtherAdjustments(item));
//	 }
//	
//	
//	 public BigDecimal getSubTotal() {
//	        BigDecimal itemsTotal = BigDecimal.ZERO;
//	        Iterator i = orderItemList.iterator();
//	        while (i.hasNext()) {
//	        	OrderItem item = (OrderItem) i.next();
//	        	getItemSubTotal(item);
////	            itemsTotal = itemsTotal.add(((ShopCart) i.next()).getItemSubTotal());
//	        }
//	        return itemsTotal;
//	    }
//
//	 
//	public void parseListGenericValue(List<GenericValue> orderItemG) {
//		clearOrderItem();
//		for (GenericValue genericValue : orderItemG) {
//			OrderItem orderItem = new OrderItem();
//			
//			orderItem.setOrderId(genericValue.getString("orderId"));
//			orderItem.setOrderItemSeqId(genericValue.getString("orderItemSeqId"));
//			orderItem.setExternalId(genericValue.getString("externalId"));
//			orderItem.setOrderItemTypeId(genericValue.getString("orderItemTypeId"));
//			orderItem.setOrderItemGroupSeqId(genericValue.getString("orderItemGroupSeqId"));
//			orderItem.setItemGroupPrimary(genericValue.getBoolean("isItemGroupPrimary"));
//			orderItem.setFromInventoryItemId(genericValue.getString("fromInventoryItemId"));
//			orderItem.setBudgetId(genericValue.getString("budgetId"));
//			orderItem.setBudgetItemSeqId(genericValue.getString("budgetItemSeqId"));
//			orderItem.setProductId(genericValue.getString("productId"));
//			orderItem.setSupplierProductId(genericValue.getString("supplierProductId"));
//			orderItem.setProductFeatureId(genericValue.getString("productFeatureId"));
//			orderItem.setProdCatalogId(genericValue.getString("prodCatalogId"));
//			orderItem.setProductCategoryId(genericValue.getString("productCategoryId"));
//			orderItem.setPromo(genericValue.getBoolean("isPromo"));
//			orderItem.setQuoteId(genericValue.getString("quoteId"));
//			orderItem.setQuoteItemSeqId(genericValue.getString("quoteItemSeqId"));
//			orderItem.setShoppingListId(genericValue.getString("shoppingListId"));
//			orderItem.setShoppingListItemSeqId(genericValue.getString("shoppingListItemSeqId"));
//			orderItem.setSubscriptionId(genericValue.getString("subscriptionId"));
//			orderItem.setDeploymentId(genericValue.getString("deploymentId"));
//			orderItem.setQuantity(genericValue.getBigDecimal("quantity"));
//			orderItem.setCancelQuantity(genericValue.getBigDecimal("cancelQuantity"));
//			orderItem.setSelectedAmount(genericValue.getBigDecimal("selectedAmount"));
//			orderItem.setUnitPrice(genericValue.getBigDecimal("unitPrice"));
//			orderItem.setUnitListPrice(genericValue.getBigDecimal("unitListPrice"));
//			orderItem.setUnitAverageCost(genericValue.getBigDecimal("unitAverageCost"));
//			orderItem.setUnitRecurringPrice(genericValue.getBigDecimal("unitRecurringPrice"));
//			orderItem.setModifiedPrice(genericValue.getBoolean("isModifiedPrice"));
//			orderItem.setRecurringFreqUomId(genericValue.getString("recurringFreqUomId"));
//			orderItem.setItemDescription(genericValue.getString("itemDescription"));
//			orderItem.setComments(genericValue.getString("comments"));
//			orderItem.setCorrespondingPoId(genericValue.getString("correspondingPoId"));
//			orderItem.setStatusId(genericValue.getString("statusId"));
//			orderItem.setSyncStatusId(genericValue.getString("syncStatusId"));
//			orderItem.setEstimatedShipDate(genericValue.getDate("estimatedShipDate"));
//			orderItem.setEstimatedDeliveryDate(genericValue.getDate("estimatedDeliveryDate"));
//			orderItem.setAutoCancelDate(genericValue.getDate("autoCancelDate"));
//			orderItem.setDontCancelSetDate(genericValue.getDate("dontCancelSetDate"));
//			orderItem.setDontCancelSetUserLogin(genericValue.getString("dontCancelSetUserLogin"));
//			orderItem.setShipBeforeDate(genericValue.getDate("shipBeforeDate"));
//			orderItem.setShipAfterDate(genericValue.getDate("shipAfterDate"));
//			orderItem.setCancelBackOrderDate(genericValue.getDate("cancelBackOrderDate"));
//			orderItem.setOverrideGlAccountId(genericValue.getString("overrideGlAccountId"));
//			orderItem.setSalesOpportunityId(genericValue.getString("salesOpportunityId"));
//			orderItem.setChangeByUserLoginId(genericValue.getString("changeByUserLoginId"));
//			orderItem.setLastUpdatedStamp(genericValue.getDate("lastUpdatedStamp"));
//			orderItem.setLastUpdatedTxStamp(genericValue.getDate("lastUpdatedTxStamp"));
//			orderItem.setCreatedStamp(genericValue.getDate("createdStamp"));
//			orderItem.setCreatedTxStamp(genericValue.getDate("createdTxStamp"));
//			orderItem.setAcctgTagEnumId1(genericValue.getString("acctgTagEnumId1"));
//			orderItem.setAcctgTagEnumId2(genericValue.getString("acctgTagEnumId2"));
//			orderItem.setAcctgTagEnumId3(genericValue.getString("acctgTagEnumId3"));
//			orderItem.setAcctgTagEnumId4(genericValue.getString("acctgTagEnumId4"));
//			orderItem.setAcctgTagEnumId5(genericValue.getString("acctgTagEnumId5"));
//			orderItem.setAcctgTagEnumId6(genericValue.getString("acctgTagEnumId6"));
//			orderItem.setAcctgTagEnumId7(genericValue.getString("acctgTagEnumId7"));
//			orderItem.setAcctgTagEnumId8(genericValue.getString("acctgTagEnumId8"));
//			orderItem.setAcctgTagEnumId9(genericValue.getString("acctgTagEnumId9"));
//			orderItem.setAcctgTagEnumId10(genericValue.getString("acctgTagEnumId10"));
//			orderItemList.add(orderItem);
//		}
//		
//	}
//	 
//	public void updatePrice(){
//		
//		
//	}
//	
//	public void setQuantity(OrderItem orderItem, BigDecimal quantity) {
//		// TODO Auto-generated method stub
//			 if (orderItem.getQuantity().compareTo(quantity) == 0) {
//		            return;
//		     }
//			 if (orderItem.isPromo()) {
//				 return;
//			 }
//			 
//			 orderItem.setQuantity(quantity);
//			 
////	        if (updateProductPrice) {
////	            this.updatePrice(dispatcher, cart);
////	        }
////
////	        // apply/unapply promotions
////	        if (triggerExternalOps) {
////	            ProductPromoWorker.doPromotions(cart, dispatcher);
////	        }
////
////	        if (!"PURCHASE_ORDER".equals(cart.getOrderType())) {
////	            // store the auto-save cart
////	            if (triggerExternalOps && ProductStoreWorker.autoSaveCart(delegator, productStoreId)) {
////	                try {
////	                    ShoppingListEvents.fillAutoSaveList(cart, dispatcher);
////	                } catch (GeneralException e) {
////	                    Debug.logWarning(e, UtilProperties.getMessage(resource_error,"OrderUnableToStoreAutoSaveCart", locale));
////	                }
////	            }
////	        }
////	        // set the item ship group
////	        if (resetShipGroup) {
////	            cart.clearItemShipInfo(this);
////	            cart.setItemShipGroupQty(this, quantity, 0);
////	        }
//			 
//			 
//	}
//	
//	 
//	
//}
