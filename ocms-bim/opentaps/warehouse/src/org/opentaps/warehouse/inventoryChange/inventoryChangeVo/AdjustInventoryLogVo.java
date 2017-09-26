package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdjustInventoryLogVo {
	private String productId;
	private String model;
	private String effectiveDate;
	private String operator;
	private String adjustNumber;
	private String adjustReason;
	private String qoh;
	private String atp;
	private String inventoryItemId;
	private String description;
	
	
	
	public String getAtp() {
		return atp;
	}
	public void setAtp(String atp) {
		this.atp = atp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) throws ParseException {
		this.effectiveDate = effectiveDate;
		if(this.effectiveDate != null){
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	Date date = dateFormat.parse(this.effectiveDate);
	    	dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    	this.effectiveDate = dateFormat.format(date);
		}
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAdjustNumber() {
		return adjustNumber;
	}
	public void setAdjustNumber(String adjustNumber) {
		this.adjustNumber = adjustNumber;
	}
	public String getAdjustReason() {
		return adjustReason;
	}
	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}
	public String getQoh() {
		return qoh;
	}
	public void setQoh(String qoh) {
		this.qoh = qoh;
	}
	public String getInventoryItemId() {
		return inventoryItemId;
	}
	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}
	@Override
	public String toString() {
		return "AdjustInventoryLogVo [productId=" + productId + ", model=" + model + ", effectiveDate=" + effectiveDate
				+ ", operator=" + operator + ", adjustNumber=" + adjustNumber + ", adjustReason=" + adjustReason
				+ ", qoh=" + qoh + ", atp=" + atp + ", inventoryItemId=" + inventoryItemId + ", description="
				+ description + "]";
	}
	
	
}
