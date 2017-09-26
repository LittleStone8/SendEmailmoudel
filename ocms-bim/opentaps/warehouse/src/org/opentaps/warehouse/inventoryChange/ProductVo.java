package org.opentaps.warehouse.inventoryChange;

public class ProductVo {
	private String productId;
	private String brandName;
	private String internalName;
	
	private String productTypeId;
	private String productAttributes;
	
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
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getProductAttributes() {
		return productAttributes;
	}
	public void setProductAttributes(String productAttributes) {
		this.productAttributes = productAttributes;
	}
	
	
}
