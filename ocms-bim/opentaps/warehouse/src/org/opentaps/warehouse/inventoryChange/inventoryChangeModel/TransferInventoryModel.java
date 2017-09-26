package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class TransferInventoryModel {
	
	private String productId;
	private String transferNum;
	private String facilityTo;
	private String brandName;
	private String model;
	private String description;
	
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getTransferNum() {
		return transferNum;
	}
	public void setTransferNum(String transferNum) {
		this.transferNum = transferNum;
	}
	public String getFacilityTo() {
		return facilityTo;
	}
	public void setFacilityTo(String facilityTo) {
		this.facilityTo = facilityTo;
	}
	@Override
	public String toString() {
		return "TransferInventoryDto [productId=" + productId + ", transferNum=" + transferNum + ", facilityTo="
				+ facilityTo + ", brandName=" + brandName + ", model=" + model + ", description=" + description + "]";
	}
	
	

}
