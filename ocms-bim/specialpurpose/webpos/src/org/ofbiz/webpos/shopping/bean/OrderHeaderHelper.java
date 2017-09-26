//package org.ofbiz.webpos.shopping.bean;
//
//import org.ofbiz.entity.GenericValue;
//
//public class OrderHeaderHelper {
//
//	private OrderHeader orderHeader;
//	
//	private GenericValue orderHeaderG;
//	
//	public OrderHeader getOrderHeader() {
//		return orderHeader;
//	}
//
//	public void setOrderHeader(OrderHeader orderHeader) {
//		this.orderHeader = orderHeader;
//	}
//
//	public GenericValue getOrderHeaderG() {
//		return orderHeaderG;
//	}
//
//	public void setOrderHeaderG(GenericValue orderHeaderG) {
//		this.orderHeaderG = orderHeaderG;
//	}
//
//	public OrderHeaderHelper(OrderHeader orderHeader) {
//		this.orderHeader = orderHeader;
//	}
//	
//	public void parseGenericValue(GenericValue orderHeaderG) {
//		orderHeader = new OrderHeader();
//		orderHeader.setOrderId(orderHeaderG.getString("orderId"));
//		orderHeader.setOrderTypeId(orderHeaderG.getString("orderTypeId"));
//		orderHeader.setOrderName(orderHeaderG.getString("orderName"));
//		orderHeader.setExternalId(orderHeaderG.getString("externalId"));
//		orderHeader.setSalesChannelEnumId(orderHeaderG.getString("salesChannelEnumId"));
//		orderHeader.setOrderDate(orderHeaderG.getDate("orderDate"));
//		orderHeader.setPriority(orderHeaderG.getBoolean("priority"));
//		orderHeader.setEntryDate(orderHeaderG.getDate("entryDate"));
//		orderHeader.setPickSheetPrintedDate(orderHeaderG.getDate("pickSheetPrintedDate"));
//		orderHeader.setVisitId(orderHeaderG.getString("visitId"));
//		orderHeader.setStatusId(orderHeaderG.getString("statusId"));
//		orderHeader.setCreatedBy(orderHeaderG.getString("createdBy"));
//		orderHeader.setFirstAttemptOrderId(orderHeaderG.getString("firstAttemptOrderId"));
//		orderHeader.setCurrencyUom(orderHeaderG.getString("currencyUom"));
//		orderHeader.setSyncStatusId(orderHeaderG.getString("syncStatusId"));
//		orderHeader.setBillingAccountId(orderHeaderG.getString("billingAccountId"));
//		orderHeader.setOriginFacilityId(orderHeaderG.getString("originFacilityId"));
//		orderHeader.setWebSiteId(orderHeaderG.getString("webSiteId"));
//		orderHeader.setProductStoreId(orderHeaderG.getString("productStoreId"));
//		orderHeader.setTerminalId(orderHeaderG.getString("terminalId"));
//		orderHeader.setTransactionId(orderHeaderG.getString("transactionId"));
//		orderHeader.setAutoOrderShoppingListId(orderHeaderG.getString("autoOrderShoppingListId"));
//		orderHeader.setNeedsInventoryIssuance(orderHeaderG.getBoolean("needsInventoryIssuance"));
//		orderHeader.setRushOrder(orderHeaderG.getBoolean("isRushOrder"));
//		orderHeader.setInternalCode(orderHeaderG.getString("internalCode"));
//		orderHeader.setRemainingSubTotal(orderHeaderG.getBigDecimal("remainingSubTotal"));
//		orderHeader.setGrandTotal(orderHeaderG.getBigDecimal("grandTotal"));
//		orderHeader.setViewed(orderHeaderG.getBoolean("isViewed"));
//		orderHeader.setLastUpdatedStamp(orderHeaderG.getDate("lastUpdatedStamp"));
//		orderHeader.setLastUpdatedTxStamp(orderHeaderG.getDate("lastUpdatedTxStamp"));
//		orderHeader.setCreatedStamp(orderHeaderG.getDate("createdStamp"));
//		orderHeader.setCreatedTxStamp(orderHeaderG.getDate("createdTxStamp"));
//		orderHeader.setBillFromPartyId(orderHeaderG.getString("billFromPartyId"));
//		orderHeader.setBillToPartyId(orderHeaderG.getString("billToPartyId"));
//	}
//	
//}
