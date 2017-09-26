package org.ofbiz.product.feature.bean;

public class FindProductCategoryById {
	
	private String productCategoryId;
	private String categoryName;
	private String parentProductCategoryId;
	public String getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getParentProductCategoryId() {
		return parentProductCategoryId;
	}
	public void setParentProductCategoryId(String parentProductCategoryId) {
		this.parentProductCategoryId = parentProductCategoryId;
	}
	@Override
	public String toString() {
		return "FindProductCategoryById [productCategoryId=" + productCategoryId + ", categoryName=" + categoryName
				+ ", parentProductCategoryId=" + parentProductCategoryId + "]";
	}
	
	
	
	
}
