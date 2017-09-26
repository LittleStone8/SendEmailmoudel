package com.yiwill.pos.bean;

public class IMEIProductVo {
	private String productId;
	private String brandName;
	private String model;
	private String description;
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
	@Override
	public String toString() {
		return "IMEIProductVo [productId=" + productId + ", brandName=" + brandName + ", model=" + model
				+ ", description=" + description + "]";
	}
	
	
}
