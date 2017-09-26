package org.opentaps.gwt.common.server.lookup.bean;

public class CreateOrderProductInfo {

	private String productId;
	private String internalName;
	private String productName;
	private String brandName;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	@Override
	public String toString() {
		return "CreateOrderProductInfo [productId=" + productId + ", internalName=" + internalName + ", productName="
				+ productName + ", brandName=" + brandName + "]";
	}
	
	
	
	
	
	

}
