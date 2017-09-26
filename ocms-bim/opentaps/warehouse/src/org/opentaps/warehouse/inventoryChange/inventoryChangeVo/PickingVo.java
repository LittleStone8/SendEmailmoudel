package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class PickingVo {
	private String id;
	private String toWarehouse;
	private String productId;
	private String brandName;
	private String model;
	private String descritpion;
	private String type;
	private String transNum;
	private String scanNum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToWarehouse() {
		return toWarehouse;
	}
	public void setToWarehouse(String toWarehouse) {
		this.toWarehouse = toWarehouse;
	}
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
	public String getDescritpion() {
		return descritpion;
	}
	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	public String getScanNum() {
		return scanNum;
	}
	public void setScanNum(String scanNum) {
		this.scanNum = scanNum;
	}
	@Override
	public String toString() {
		return "PickingVo [id=" + id + ", toWarehouse=" + toWarehouse + ", productId=" + productId + ", brandName="
				+ brandName + ", model=" + model + ", descritpion=" + descritpion + ", type=" + type + ", transNum="
				+ transNum + "]";
	}
	
}
