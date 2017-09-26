//package org.ofbiz.webpos.shopping.bean;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
//public class OrderItem implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5182302925284211719L;
//	private String orderId;
//	private String orderItemSeqId;
//	private String externalId;
//	private String orderItemTypeId;
//	private String orderItemGroupSeqId;
//	private boolean isItemGroupPrimary = false;
//	private String fromInventoryItemId;
//	private String budgetId;
//	private String budgetItemSeqId;
//	private String productId;
//	private String supplierProductId;
//	private String productFeatureId;
//	private String prodCatalogId;
//	private String productCategoryId;
//	private boolean isPromo = false;
//	private String quoteId;
//	private String quoteItemSeqId;
//	private String shoppingListId;
//	private String shoppingListItemSeqId;
//	private String subscriptionId;
//	private String deploymentId;
//	private BigDecimal quantity;
//	private BigDecimal cancelQuantity;
//	private BigDecimal selectedAmount;
//	private BigDecimal unitPrice;
//	private BigDecimal unitListPrice;
//	private BigDecimal unitAverageCost;
//	private BigDecimal unitRecurringPrice;
//	private boolean isModifiedPrice = false;
//	private String recurringFreqUomId;
//	private String itemDescription;
//	private String comments;
//	private String correspondingPoId;
//	private String statusId;
//	private String syncStatusId;
//	private Date estimatedShipDate;
//	private Date estimatedDeliveryDate;
//	private Date autoCancelDate;
//	private Date dontCancelSetDate;
//	private String dontCancelSetUserLogin;
//	private Date shipBeforeDate;
//	private Date shipAfterDate;
//	private Date cancelBackOrderDate;
//	private String overrideGlAccountId;
//	private String salesOpportunityId;
//	private String changeByUserLoginId;
//	private Date lastUpdatedStamp;
//	private Date lastUpdatedTxStamp;
//	private Date createdStamp;
//	private Date createdTxStamp;
//	private String acctgTagEnumId1;
//	private String acctgTagEnumId2;
//	private String acctgTagEnumId3;
//	private String acctgTagEnumId4;
//	private String acctgTagEnumId5;
//	private String acctgTagEnumId6;
//	private String acctgTagEnumId7;
//	private String acctgTagEnumId8;
//	private String acctgTagEnumId9;
//	private String acctgTagEnumId10;
//	
//	
//	public String getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
//	public String getOrderItemSeqId() {
//		return orderItemSeqId;
//	}
//	public void setOrderItemSeqId(String orderItemSeqId) {
//		this.orderItemSeqId = orderItemSeqId;
//	}
//	public String getExternalId() {
//		return externalId;
//	}
//	public void setExternalId(String externalId) {
//		this.externalId = externalId;
//	}
//	public String getOrderItemTypeId() {
//		return orderItemTypeId;
//	}
//	public void setOrderItemTypeId(String orderItemTypeId) {
//		this.orderItemTypeId = orderItemTypeId;
//	}
//	public String getOrderItemGroupSeqId() {
//		return orderItemGroupSeqId;
//	}
//	public void setOrderItemGroupSeqId(String orderItemGroupSeqId) {
//		this.orderItemGroupSeqId = orderItemGroupSeqId;
//	}
//	public boolean isItemGroupPrimary() {
//		return isItemGroupPrimary;
//	}
//	public void setItemGroupPrimary(boolean isItemGroupPrimary) {
//		this.isItemGroupPrimary = isItemGroupPrimary;
//	}
//	public String getFromInventoryItemId() {
//		return fromInventoryItemId;
//	}
//	public void setFromInventoryItemId(String fromInventoryItemId) {
//		this.fromInventoryItemId = fromInventoryItemId;
//	}
//	public String getBudgetId() {
//		return budgetId;
//	}
//	public void setBudgetId(String budgetId) {
//		this.budgetId = budgetId;
//	}
//	public String getBudgetItemSeqId() {
//		return budgetItemSeqId;
//	}
//	public void setBudgetItemSeqId(String budgetItemSeqId) {
//		this.budgetItemSeqId = budgetItemSeqId;
//	}
//	public String getProductId() {
//		return productId;
//	}
//	public void setProductId(String productId) {
//		this.productId = productId;
//	}
//	public String getSupplierProductId() {
//		return supplierProductId;
//	}
//	public void setSupplierProductId(String supplierProductId) {
//		this.supplierProductId = supplierProductId;
//	}
//	public String getProductFeatureId() {
//		return productFeatureId;
//	}
//	public void setProductFeatureId(String productFeatureId) {
//		this.productFeatureId = productFeatureId;
//	}
//	public String getProdCatalogId() {
//		return prodCatalogId;
//	}
//	public void setProdCatalogId(String prodCatalogId) {
//		this.prodCatalogId = prodCatalogId;
//	}
//	public String getProductCategoryId() {
//		return productCategoryId;
//	}
//	public void setProductCategoryId(String productCategoryId) {
//		this.productCategoryId = productCategoryId;
//	}
//	public boolean isPromo() {
//		return isPromo;
//	}
//	public void setPromo(boolean isPromo) {
//		this.isPromo = isPromo;
//	}
//	public String getQuoteId() {
//		return quoteId;
//	}
//	public void setQuoteId(String quoteId) {
//		this.quoteId = quoteId;
//	}
//	public String getQuoteItemSeqId() {
//		return quoteItemSeqId;
//	}
//	public void setQuoteItemSeqId(String quoteItemSeqId) {
//		this.quoteItemSeqId = quoteItemSeqId;
//	}
//	public String getShoppingListId() {
//		return shoppingListId;
//	}
//	public void setShoppingListId(String shoppingListId) {
//		this.shoppingListId = shoppingListId;
//	}
//	public String getShoppingListItemSeqId() {
//		return shoppingListItemSeqId;
//	}
//	public void setShoppingListItemSeqId(String shoppingListItemSeqId) {
//		this.shoppingListItemSeqId = shoppingListItemSeqId;
//	}
//	public String getSubscriptionId() {
//		return subscriptionId;
//	}
//	public void setSubscriptionId(String subscriptionId) {
//		this.subscriptionId = subscriptionId;
//	}
//	public String getDeploymentId() {
//		return deploymentId;
//	}
//	public void setDeploymentId(String deploymentId) {
//		this.deploymentId = deploymentId;
//	}
//	public BigDecimal getQuantity() {
//		return quantity;
//	}
//	public void setQuantity(BigDecimal quantity) {
//		this.quantity = quantity;
//	}
//	public BigDecimal getCancelQuantity() {
//		return cancelQuantity;
//	}
//	public void setCancelQuantity(BigDecimal cancelQuantity) {
//		this.cancelQuantity = cancelQuantity;
//	}
//	public BigDecimal getSelectedAmount() {
//		return selectedAmount;
//	}
//	public void setSelectedAmount(BigDecimal selectedAmount) {
//		this.selectedAmount = selectedAmount;
//	}
//	public BigDecimal getUnitPrice() {
//		return unitPrice;
//	}
//	public void setUnitPrice(BigDecimal unitPrice) {
//		this.unitPrice = unitPrice;
//	}
//	public BigDecimal getUnitListPrice() {
//		return unitListPrice;
//	}
//	public void setUnitListPrice(BigDecimal unitListPrice) {
//		this.unitListPrice = unitListPrice;
//	}
//	public BigDecimal getUnitAverageCost() {
//		return unitAverageCost;
//	}
//	public void setUnitAverageCost(BigDecimal unitAverageCost) {
//		this.unitAverageCost = unitAverageCost;
//	}
//	public BigDecimal getUnitRecurringPrice() {
//		return unitRecurringPrice;
//	}
//	public void setUnitRecurringPrice(BigDecimal unitRecurringPrice) {
//		this.unitRecurringPrice = unitRecurringPrice;
//	}
//	public boolean isModifiedPrice() {
//		return isModifiedPrice;
//	}
//	public void setModifiedPrice(boolean isModifiedPrice) {
//		this.isModifiedPrice = isModifiedPrice;
//	}
//	public String getRecurringFreqUomId() {
//		return recurringFreqUomId;
//	}
//	public void setRecurringFreqUomId(String recurringFreqUomId) {
//		this.recurringFreqUomId = recurringFreqUomId;
//	}
//	public String getItemDescription() {
//		return itemDescription;
//	}
//	public void setItemDescription(String itemDescription) {
//		this.itemDescription = itemDescription;
//	}
//	public String getComments() {
//		return comments;
//	}
//	public void setComments(String comments) {
//		this.comments = comments;
//	}
//	public String getCorrespondingPoId() {
//		return correspondingPoId;
//	}
//	public void setCorrespondingPoId(String correspondingPoId) {
//		this.correspondingPoId = correspondingPoId;
//	}
//	public String getStatusId() {
//		return statusId;
//	}
//	public void setStatusId(String statusId) {
//		this.statusId = statusId;
//	}
//	public String getSyncStatusId() {
//		return syncStatusId;
//	}
//	public void setSyncStatusId(String syncStatusId) {
//		this.syncStatusId = syncStatusId;
//	}
//	public Date getEstimatedShipDate() {
//		return estimatedShipDate;
//	}
//	public void setEstimatedShipDate(Date estimatedShipDate) {
//		this.estimatedShipDate = estimatedShipDate;
//	}
//	public Date getEstimatedDeliveryDate() {
//		return estimatedDeliveryDate;
//	}
//	public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
//		this.estimatedDeliveryDate = estimatedDeliveryDate;
//	}
//	public Date getAutoCancelDate() {
//		return autoCancelDate;
//	}
//	public void setAutoCancelDate(Date autoCancelDate) {
//		this.autoCancelDate = autoCancelDate;
//	}
//	public Date getDontCancelSetDate() {
//		return dontCancelSetDate;
//	}
//	public void setDontCancelSetDate(Date dontCancelSetDate) {
//		this.dontCancelSetDate = dontCancelSetDate;
//	}
//	public String getDontCancelSetUserLogin() {
//		return dontCancelSetUserLogin;
//	}
//	public void setDontCancelSetUserLogin(String dontCancelSetUserLogin) {
//		this.dontCancelSetUserLogin = dontCancelSetUserLogin;
//	}
//	public Date getShipBeforeDate() {
//		return shipBeforeDate;
//	}
//	public void setShipBeforeDate(Date shipBeforeDate) {
//		this.shipBeforeDate = shipBeforeDate;
//	}
//	public Date getShipAfterDate() {
//		return shipAfterDate;
//	}
//	public void setShipAfterDate(Date shipAfterDate) {
//		this.shipAfterDate = shipAfterDate;
//	}
//	public Date getCancelBackOrderDate() {
//		return cancelBackOrderDate;
//	}
//	public void setCancelBackOrderDate(Date cancelBackOrderDate) {
//		this.cancelBackOrderDate = cancelBackOrderDate;
//	}
//	public String getOverrideGlAccountId() {
//		return overrideGlAccountId;
//	}
//	public void setOverrideGlAccountId(String overrideGlAccountId) {
//		this.overrideGlAccountId = overrideGlAccountId;
//	}
//	public String getSalesOpportunityId() {
//		return salesOpportunityId;
//	}
//	public void setSalesOpportunityId(String salesOpportunityId) {
//		this.salesOpportunityId = salesOpportunityId;
//	}
//	public String getChangeByUserLoginId() {
//		return changeByUserLoginId;
//	}
//	public void setChangeByUserLoginId(String changeByUserLoginId) {
//		this.changeByUserLoginId = changeByUserLoginId;
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
//	public String getAcctgTagEnumId1() {
//		return acctgTagEnumId1;
//	}
//	public void setAcctgTagEnumId1(String acctgTagEnumId1) {
//		this.acctgTagEnumId1 = acctgTagEnumId1;
//	}
//	public String getAcctgTagEnumId2() {
//		return acctgTagEnumId2;
//	}
//	public void setAcctgTagEnumId2(String acctgTagEnumId2) {
//		this.acctgTagEnumId2 = acctgTagEnumId2;
//	}
//	public String getAcctgTagEnumId3() {
//		return acctgTagEnumId3;
//	}
//	public void setAcctgTagEnumId3(String acctgTagEnumId3) {
//		this.acctgTagEnumId3 = acctgTagEnumId3;
//	}
//	public String getAcctgTagEnumId4() {
//		return acctgTagEnumId4;
//	}
//	public void setAcctgTagEnumId4(String acctgTagEnumId4) {
//		this.acctgTagEnumId4 = acctgTagEnumId4;
//	}
//	public String getAcctgTagEnumId5() {
//		return acctgTagEnumId5;
//	}
//	public void setAcctgTagEnumId5(String acctgTagEnumId5) {
//		this.acctgTagEnumId5 = acctgTagEnumId5;
//	}
//	public String getAcctgTagEnumId6() {
//		return acctgTagEnumId6;
//	}
//	public void setAcctgTagEnumId6(String acctgTagEnumId6) {
//		this.acctgTagEnumId6 = acctgTagEnumId6;
//	}
//	public String getAcctgTagEnumId7() {
//		return acctgTagEnumId7;
//	}
//	public void setAcctgTagEnumId7(String acctgTagEnumId7) {
//		this.acctgTagEnumId7 = acctgTagEnumId7;
//	}
//	public String getAcctgTagEnumId8() {
//		return acctgTagEnumId8;
//	}
//	public void setAcctgTagEnumId8(String acctgTagEnumId8) {
//		this.acctgTagEnumId8 = acctgTagEnumId8;
//	}
//	public String getAcctgTagEnumId9() {
//		return acctgTagEnumId9;
//	}
//	public void setAcctgTagEnumId9(String acctgTagEnumId9) {
//		this.acctgTagEnumId9 = acctgTagEnumId9;
//	}
//	public String getAcctgTagEnumId10() {
//		return acctgTagEnumId10;
//	}
//	public void setAcctgTagEnumId10(String acctgTagEnumId10) {
//		this.acctgTagEnumId10 = acctgTagEnumId10;
//	}
//
//	
//}
