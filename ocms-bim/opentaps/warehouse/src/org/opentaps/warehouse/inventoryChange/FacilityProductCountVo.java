package org.opentaps.warehouse.inventoryChange;

public class FacilityProductCountVo {
	private String productId;
	private String model;
	private String brandName;
	private String atp;
	private String description;
	private String inventoryItemTypeId;
	private String pickingNum;
	
	
	
	public String getPickingNum() {
		return pickingNum;
	}
	public void setPickingNum(String pickingNum) {
		this.pickingNum = pickingNum;
		if(this.pickingNum == null){
			this.pickingNum = "0";
		}
	}
	public String getInventoryItemTypeId() {
		return inventoryItemTypeId;
	}
	public void setInventoryItemTypeId(String inventoryItemTypeId) {
		this.inventoryItemTypeId = inventoryItemTypeId;
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
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
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
	@Override
	public String toString() {
		return "FacilityProductCountVo [productId=" + productId + ", model=" + model + ", brandName=" + brandName
				+ ", atp=" + atp + ", description=" + description + ", inventoryItemTypeId=" + inventoryItemTypeId
				+ ", pickingNum=" + pickingNum + "]";
	}
	
	
}
