package org.ofbiz.product.feature.bean;

public class VariantFindProductBean {

	private String productId;
	private String productTypeId;
	private String internalName;
	private String productName;
	private String isVirtual;
	private String isVariant;
	private String createdDate;
	private String createdByUserLogin;
	private String lastModifiedDate;
	private String lastModifiedByUserLogin;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
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
	public String getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getIsVariant() {
		return isVariant;
	}
	public void setIsVariant(String isVariant) {
		this.isVariant = isVariant;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedByUserLogin() {
		return createdByUserLogin;
	}
	public void setCreatedByUserLogin(String createdByUserLogin) {
		this.createdByUserLogin = createdByUserLogin;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedByUserLogin() {
		return lastModifiedByUserLogin;
	}
	public void setLastModifiedByUserLogin(String lastModifiedByUserLogin) {
		this.lastModifiedByUserLogin = lastModifiedByUserLogin;
	}
	@Override
	public String toString() {
		return "VariantFindProductBean [productId=" + productId + ", productTypeId=" + productTypeId + ", internalName="
				+ internalName + ", productName=" + productName + ", isVirtual=" + isVirtual + ", isVariant="
				+ isVariant + ", createdDate=" + createdDate + ", createdByUserLogin=" + createdByUserLogin
				+ ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedByUserLogin=" + lastModifiedByUserLogin
				+ "]";
	}
	
	
	
}
