package org.opentaps.warehouse.inventoryChange;

public class PurchaseInventoryVo {
	private String productId;
	private String internalName;
	private String brandName;
	private String qoh;
	private String atp;
	private String description;
	private String orderNum;
	private String facilityName;
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getQoh() {
		return qoh;
	}
	public void setQoh(String qoh) {
		this.qoh = qoh;
		if(this.qoh == null || this.qoh.equals("")){
			this.qoh = "0";
		}
	}
	public String getAtp() {
		return atp;
	}
	public void setAtp(String atp) {
		this.atp = atp;
		if(this.atp == null || this.atp.equals("")){
			this.atp = "0";
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
		if(this.orderNum == null || this.orderNum.equals("")){
			this.orderNum = "0";
		}
	}
	@Override
	public String toString() {
		return "PurchaseInventoryVo [productId=" + productId + ", internalName=" + internalName + ", brandName="
				+ brandName + ", qoh=" + qoh + ", atp=" + atp + ", description=" + description + ", orderNum="
				+ orderNum + ", facilityName=" + facilityName + "]";
	}
	
	
}
