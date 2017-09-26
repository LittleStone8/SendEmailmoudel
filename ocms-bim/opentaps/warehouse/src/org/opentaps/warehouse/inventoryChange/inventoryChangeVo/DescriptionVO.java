package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class DescriptionVO {
	private String productId;
	private String ean;
	private String description;
	private String brand;
	private String model;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "DescriptionVO [productId=" + productId + ", ean=" + ean + ", description=" + description + ", brand="
				+ brand + ", model=" + model + "]";
	}
	
	public String getcompletelydescription()
	{
		String desc="";
		if(brand!=null&&!"".equals(brand))
			desc+=brand+"|";
		if(model!=null&&!"".equals(model))
			desc+=model+"|";
		if(description!=null&&!"".equals(description))
			desc+=description+"|";
		
		return desc;
	}
	
}
