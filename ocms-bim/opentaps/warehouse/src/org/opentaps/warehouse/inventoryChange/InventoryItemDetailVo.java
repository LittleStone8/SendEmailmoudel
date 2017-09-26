package org.opentaps.warehouse.inventoryChange;

import org.apache.commons.lang.StringUtils;

public class InventoryItemDetailVo {

	private String inventoryItemDetailSeqId;

	private String effectiveDate;

	private String operator;

	private String firstNameLocal;

	private String qty;

	private String atp;

	private String usageType;

	private String transferId;

	private String reasonEnumId;

	private String fromWarehouse;

	private String toWarehouse;

	private String quantityOnHandDiff;

	private String availableToPromiseDiff;

	private String orderId;

	public String getInventoryItemDetailSeqId() {
		return inventoryItemDetailSeqId;
	}

	public void setInventoryItemDetailSeqId(String inventoryItemDetailSeqId) {
		this.inventoryItemDetailSeqId = inventoryItemDetailSeqId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
//		if(!StringUtils.isEmpty(effectiveDate) && StringUtils.contains(effectiveDate, ".")){
//			this.effectiveDate = effectiveDate.substring(0,effectiveDate.lastIndexOf("."));
//		}else{
			this.effectiveDate = effectiveDate;
//		}
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getAtp() {
		return atp;
	}

	public void setAtp(String atp) {
		this.atp = atp;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getReasonEnumId() {
		return reasonEnumId;
	}

	public void setReasonEnumId(String reasonEnumId) {
		this.reasonEnumId = reasonEnumId;
	}

	public String getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(String fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public String getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(String toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public String getQuantityOnHandDiff() {
		return quantityOnHandDiff;
	}

	public void setQuantityOnHandDiff(String quantityOnHandDiff) {
		this.quantityOnHandDiff = quantityOnHandDiff;
	}

	public String getAvailableToPromiseDiff() {
		return availableToPromiseDiff;
	}

	public void setAvailableToPromiseDiff(String availableToPromiseDiff) {
		this.availableToPromiseDiff = availableToPromiseDiff;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
