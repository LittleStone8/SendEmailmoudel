//package org.ofbiz.webpos.shopping.bean;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
//import org.ofbiz.entity.GenericValue;
//
//public class OrderHeader implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -4742061344213034212L;
//	
//	private String orderId;
//	private String orderTypeId;
//	private String orderName;
//	private String externalId;
//	private String salesChannelEnumId;
//	private Date orderDate;
//	private boolean priority = false;
//	private Date entryDate;
//	private Date pickSheetPrintedDate;
//	private String visitId;
//	private String statusId;
//	private String createdBy;
//	private String firstAttemptOrderId;
//	private String currencyUom;
//	private String syncStatusId;
//	private String billingAccountId;
//	private String originFacilityId;
//	private String webSiteId;
//	private String productStoreId;
//	private String terminalId;
//	private String transactionId;
//	private String autoOrderShoppingListId;
//	private boolean needsInventoryIssuance = false;
//	private boolean isRushOrder = false;
//	private String internalCode;
//	private BigDecimal remainingSubTotal;
//	private BigDecimal grandTotal;
//	private boolean isViewed = false;
//	private Date lastUpdatedStamp;
//	private Date lastUpdatedTxStamp;
//	private Date createdStamp;
//	private Date createdTxStamp;
//	private String billFromPartyId;
//	private String billToPartyId;
//	
//	
//	public String getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
//	public String getOrderTypeId() {
//		return orderTypeId;
//	}
//	public void setOrderTypeId(String orderTypeId) {
//		this.orderTypeId = orderTypeId;
//	}
//	public String getOrderName() {
//		return orderName;
//	}
//	public void setOrderName(String orderName) {
//		this.orderName = orderName;
//	}
//	public String getExternalId() {
//		return externalId;
//	}
//	public void setExternalId(String externalId) {
//		this.externalId = externalId;
//	}
//	public String getSalesChannelEnumId() {
//		return salesChannelEnumId;
//	}
//	public void setSalesChannelEnumId(String salesChannelEnumId) {
//		this.salesChannelEnumId = salesChannelEnumId;
//	}
//	public Date getOrderDate() {
//		return orderDate;
//	}
//	public void setOrderDate(Date orderDate) {
//		this.orderDate = orderDate;
//	}
//	public boolean isPriority() {
//		return priority;
//	}
//	public void setPriority(boolean priority) {
//		this.priority = priority;
//	}
//	public Date getEntryDate() {
//		return entryDate;
//	}
//	public void setEntryDate(Date entryDate) {
//		this.entryDate = entryDate;
//	}
//	public Date getPickSheetPrintedDate() {
//		return pickSheetPrintedDate;
//	}
//	public void setPickSheetPrintedDate(Date pickSheetPrintedDate) {
//		this.pickSheetPrintedDate = pickSheetPrintedDate;
//	}
//	public String getVisitId() {
//		return visitId;
//	}
//	public void setVisitId(String visitId) {
//		this.visitId = visitId;
//	}
//	public String getStatusId() {
//		return statusId;
//	}
//	public void setStatusId(String statusId) {
//		this.statusId = statusId;
//	}
//	public String getCreatedBy() {
//		return createdBy;
//	}
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
//	public String getFirstAttemptOrderId() {
//		return firstAttemptOrderId;
//	}
//	public void setFirstAttemptOrderId(String firstAttemptOrderId) {
//		this.firstAttemptOrderId = firstAttemptOrderId;
//	}
//	public String getCurrencyUom() {
//		return currencyUom;
//	}
//	public void setCurrencyUom(String currencyUom) {
//		this.currencyUom = currencyUom;
//	}
//	public String getSyncStatusId() {
//		return syncStatusId;
//	}
//	public void setSyncStatusId(String syncStatusId) {
//		this.syncStatusId = syncStatusId;
//	}
//	public String getBillingAccountId() {
//		return billingAccountId;
//	}
//	public void setBillingAccountId(String billingAccountId) {
//		this.billingAccountId = billingAccountId;
//	}
//	public String getOriginFacilityId() {
//		return originFacilityId;
//	}
//	public void setOriginFacilityId(String originFacilityId) {
//		this.originFacilityId = originFacilityId;
//	}
//	public String getWebSiteId() {
//		return webSiteId;
//	}
//	public void setWebSiteId(String webSiteId) {
//		this.webSiteId = webSiteId;
//	}
//	public String getProductStoreId() {
//		return productStoreId;
//	}
//	public void setProductStoreId(String productStoreId) {
//		this.productStoreId = productStoreId;
//	}
//	public String getTerminalId() {
//		return terminalId;
//	}
//	public void setTerminalId(String terminalId) {
//		this.terminalId = terminalId;
//	}
//	public String getTransactionId() {
//		return transactionId;
//	}
//	public void setTransactionId(String transactionId) {
//		this.transactionId = transactionId;
//	}
//	public String getAutoOrderShoppingListId() {
//		return autoOrderShoppingListId;
//	}
//	public void setAutoOrderShoppingListId(String autoOrderShoppingListId) {
//		this.autoOrderShoppingListId = autoOrderShoppingListId;
//	}
//	public boolean isNeedsInventoryIssuance() {
//		return needsInventoryIssuance;
//	}
//	public void setNeedsInventoryIssuance(boolean needsInventoryIssuance) {
//		this.needsInventoryIssuance = needsInventoryIssuance;
//	}
//	public boolean isRushOrder() {
//		return isRushOrder;
//	}
//	public void setRushOrder(boolean isRushOrder) {
//		this.isRushOrder = isRushOrder;
//	}
//	public String getInternalCode() {
//		return internalCode;
//	}
//	public void setInternalCode(String internalCode) {
//		this.internalCode = internalCode;
//	}
//	public BigDecimal getRemainingSubTotal() {
//		return remainingSubTotal;
//	}
//	public void setRemainingSubTotal(BigDecimal remainingSubTotal) {
//		this.remainingSubTotal = remainingSubTotal;
//	}
//	public BigDecimal getGrandTotal() {
//		return grandTotal;
//	}
//	public void setGrandTotal(BigDecimal grandTotal) {
//		this.grandTotal = grandTotal;
//	}
//	public boolean isViewed() {
//		return isViewed;
//	}
//	public void setViewed(boolean isViewed) {
//		this.isViewed = isViewed;
//	}
//	public Date getLastUpdatedStamp() {
//		return lastUpdatedStamp;
//	}
//	public void setLastUpdatedStamp(Date lastUpdatedStamp) {
//		this.lastUpdatedStamp = lastUpdatedStamp;
//	}
//	public Date getLastUpdatedTxStamp() {
//		return lastUpdatedTxStamp;
//	}
//	public void setLastUpdatedTxStamp(Date lastUpdatedTxStamp) {
//		this.lastUpdatedTxStamp = lastUpdatedTxStamp;
//	}
//	public Date getCreatedStamp() {
//		return createdStamp;
//	}
//	public void setCreatedStamp(Date createdStamp) {
//		this.createdStamp = createdStamp;
//	}
//	public Date getCreatedTxStamp() {
//		return createdTxStamp;
//	}
//	public void setCreatedTxStamp(Date createdTxStamp) {
//		this.createdTxStamp = createdTxStamp;
//	}
//	public String getBillFromPartyId() {
//		return billFromPartyId;
//	}
//	public void setBillFromPartyId(String billFromPartyId) {
//		this.billFromPartyId = billFromPartyId;
//	}
//	public String getBillToPartyId() {
//		return billToPartyId;
//	}
//	public void setBillToPartyId(String billToPartyId) {
//		this.billToPartyId = billToPartyId;
//	}
//	
//}
