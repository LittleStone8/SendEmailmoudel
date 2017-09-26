//package org.ofbiz.webpos.shopping.bean;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
//public class OrderAdjustment implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 6149024969099506408L;
//	
//	private String orderAdjustmentId;
//	private String orderAdjustmentTypeId;
//	private String orderId;
//	private String orderItemSeqId;
//	private String shipGroupSeqId;
//	private String comments;
//	private String description;
//	private String amount;
//	private String recurringAmount;
//	private String productPromoId;
//	private String productPromoRuleId;
//	private String productPromoActionSeqId;
//	private String productFeatureId;
//	private String correspondingProductId;
//	private String taxAuthorityRateSeqId;
//	private String sourceReferenceId;
//	private String sourcePercentage;
//	private String customerReferenceId;
//	private String primaryGeoId;
//	private String secondaryGeoId;
//	private String exemptAmount;
//	private String taxAuthGeoId;
//	private String taxAuthPartyId;
//	private String overrideGlAccountId;
//	private String includeInTax;
//	private String includeInShipping;
//	private String createdDate;
//	private String createdByUserLogin;
//	private String originalAdjustmentId;
//	private String amountPerQuantity;
//	private String percentage;
//	private String lastUpdatedStamp;
//	private String lastUpdatedTxStamp;
//	private String createdStamp;
//	private String createdTxStamp;
//	private String appliesToQuantity;
//	private String neverProrate;
//	public String getOrderAdjustmentId() {
//		return orderAdjustmentId;
//	}
//	public void setOrderAdjustmentId(String orderAdjustmentId) {
//		this.orderAdjustmentId = orderAdjustmentId;
//	}
//	public String getOrderAdjustmentTypeId() {
//		return orderAdjustmentTypeId;
//	}
//	public void setOrderAdjustmentTypeId(String orderAdjustmentTypeId) {
//		this.orderAdjustmentTypeId = orderAdjustmentTypeId;
//	}
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
//	public String getShipGroupSeqId() {
//		return shipGroupSeqId;
//	}
//	public void setShipGroupSeqId(String shipGroupSeqId) {
//		this.shipGroupSeqId = shipGroupSeqId;
//	}
//	public String getComments() {
//		return comments;
//	}
//	public void setComments(String comments) {
//		this.comments = comments;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public String getAmount() {
//		return amount;
//	}
//	public void setAmount(String amount) {
//		this.amount = amount;
//	}
//	public String getRecurringAmount() {
//		return recurringAmount;
//	}
//	public void setRecurringAmount(String recurringAmount) {
//		this.recurringAmount = recurringAmount;
//	}
//	public String getProductPromoId() {
//		return productPromoId;
//	}
//	public void setProductPromoId(String productPromoId) {
//		this.productPromoId = productPromoId;
//	}
//	public String getProductPromoRuleId() {
//		return productPromoRuleId;
//	}
//	public void setProductPromoRuleId(String productPromoRuleId) {
//		this.productPromoRuleId = productPromoRuleId;
//	}
//	public String getProductPromoActionSeqId() {
//		return productPromoActionSeqId;
//	}
//	public void setProductPromoActionSeqId(String productPromoActionSeqId) {
//		this.productPromoActionSeqId = productPromoActionSeqId;
//	}
//	public String getProductFeatureId() {
//		return productFeatureId;
//	}
//	public void setProductFeatureId(String productFeatureId) {
//		this.productFeatureId = productFeatureId;
//	}
//	public String getCorrespondingProductId() {
//		return correspondingProductId;
//	}
//	public void setCorrespondingProductId(String correspondingProductId) {
//		this.correspondingProductId = correspondingProductId;
//	}
//	public String getTaxAuthorityRateSeqId() {
//		return taxAuthorityRateSeqId;
//	}
//	public void setTaxAuthorityRateSeqId(String taxAuthorityRateSeqId) {
//		this.taxAuthorityRateSeqId = taxAuthorityRateSeqId;
//	}
//	public String getSourceReferenceId() {
//		return sourceReferenceId;
//	}
//	public void setSourceReferenceId(String sourceReferenceId) {
//		this.sourceReferenceId = sourceReferenceId;
//	}
//	public String getSourcePercentage() {
//		return sourcePercentage;
//	}
//	public void setSourcePercentage(String sourcePercentage) {
//		this.sourcePercentage = sourcePercentage;
//	}
//	public String getCustomerReferenceId() {
//		return customerReferenceId;
//	}
//	public void setCustomerReferenceId(String customerReferenceId) {
//		this.customerReferenceId = customerReferenceId;
//	}
//	public String getPrimaryGeoId() {
//		return primaryGeoId;
//	}
//	public void setPrimaryGeoId(String primaryGeoId) {
//		this.primaryGeoId = primaryGeoId;
//	}
//	public String getSecondaryGeoId() {
//		return secondaryGeoId;
//	}
//	public void setSecondaryGeoId(String secondaryGeoId) {
//		this.secondaryGeoId = secondaryGeoId;
//	}
//	public String getExemptAmount() {
//		return exemptAmount;
//	}
//	public void setExemptAmount(String exemptAmount) {
//		this.exemptAmount = exemptAmount;
//	}
//	public String getTaxAuthGeoId() {
//		return taxAuthGeoId;
//	}
//	public void setTaxAuthGeoId(String taxAuthGeoId) {
//		this.taxAuthGeoId = taxAuthGeoId;
//	}
//	public String getTaxAuthPartyId() {
//		return taxAuthPartyId;
//	}
//	public void setTaxAuthPartyId(String taxAuthPartyId) {
//		this.taxAuthPartyId = taxAuthPartyId;
//	}
//	public String getOverrideGlAccountId() {
//		return overrideGlAccountId;
//	}
//	public void setOverrideGlAccountId(String overrideGlAccountId) {
//		this.overrideGlAccountId = overrideGlAccountId;
//	}
//	public String getIncludeInTax() {
//		return includeInTax;
//	}
//	public void setIncludeInTax(String includeInTax) {
//		this.includeInTax = includeInTax;
//	}
//	public String getIncludeInShipping() {
//		return includeInShipping;
//	}
//	public void setIncludeInShipping(String includeInShipping) {
//		this.includeInShipping = includeInShipping;
//	}
//	public String getCreatedDate() {
//		return createdDate;
//	}
//	public void setCreatedDate(String createdDate) {
//		this.createdDate = createdDate;
//	}
//	public String getCreatedByUserLogin() {
//		return createdByUserLogin;
//	}
//	public void setCreatedByUserLogin(String createdByUserLogin) {
//		this.createdByUserLogin = createdByUserLogin;
//	}
//	public String getOriginalAdjustmentId() {
//		return originalAdjustmentId;
//	}
//	public void setOriginalAdjustmentId(String originalAdjustmentId) {
//		this.originalAdjustmentId = originalAdjustmentId;
//	}
//	public String getAmountPerQuantity() {
//		return amountPerQuantity;
//	}
//	public void setAmountPerQuantity(String amountPerQuantity) {
//		this.amountPerQuantity = amountPerQuantity;
//	}
//	public String getPercentage() {
//		return percentage;
//	}
//	public void setPercentage(String percentage) {
//		this.percentage = percentage;
//	}
//	public String getLastUpdatedStamp() {
//		return lastUpdatedStamp;
//	}
//	public void setLastUpdatedStamp(String lastUpdatedStamp) {
//		this.lastUpdatedStamp = lastUpdatedStamp;
//	}
//	public String getLastUpdatedTxStamp() {
//		return lastUpdatedTxStamp;
//	}
//	public void setLastUpdatedTxStamp(String lastUpdatedTxStamp) {
//		this.lastUpdatedTxStamp = lastUpdatedTxStamp;
//	}
//	public String getCreatedStamp() {
//		return createdStamp;
//	}
//	public void setCreatedStamp(String createdStamp) {
//		this.createdStamp = createdStamp;
//	}
//	public String getCreatedTxStamp() {
//		return createdTxStamp;
//	}
//	public void setCreatedTxStamp(String createdTxStamp) {
//		this.createdTxStamp = createdTxStamp;
//	}
//	public String getAppliesToQuantity() {
//		return appliesToQuantity;
//	}
//	public void setAppliesToQuantity(String appliesToQuantity) {
//		this.appliesToQuantity = appliesToQuantity;
//	}
//	public String getNeverProrate() {
//		return neverProrate;
//	}
//	public void setNeverProrate(String neverProrate) {
//		this.neverProrate = neverProrate;
//	}
//}
