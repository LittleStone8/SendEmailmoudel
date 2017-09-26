package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockinReportVo {
	private String productId;
	private String brandName;
	private String model;
	private String description;
	private String unitCost;
	private String receiveQuantity;
	private String transferQuantity;
	private String receiveTime;
	private String facilityName;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
		if(this.unitCost == null){
			this.unitCost = "0";
		}
	}
	public String getReceiveQuantity() {
		return receiveQuantity;
	}
	public void setReceiveQuantity(String receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
	}
	public String getTransferQuantity() {
		return transferQuantity;
	}
	public void setTransferQuantity(String transferQuantity) {
		this.transferQuantity = transferQuantity;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) throws ParseException {
		this.receiveTime = receiveTime;
		if(this.receiveTime != null){
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	Date date = dateFormat.parse(this.receiveTime);
	    	dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    	this.receiveTime = dateFormat.format(date);
		}
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	@Override
	public String toString() {
		return "StockinReportVo [productId=" + productId + ", brandName=" + brandName + ", model=" + model
				+ ", description=" + description + ", unitCost=" + unitCost + ", receiveQuantity=" + receiveQuantity
				+ ", transferQuantity=" + transferQuantity + ", receiveTime=" + receiveTime + ", facilityName="
				+ facilityName + "]";
	}
	
	
	
}
