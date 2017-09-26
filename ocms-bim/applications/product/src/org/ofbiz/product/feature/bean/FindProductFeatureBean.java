package org.ofbiz.product.feature.bean;

public class FindProductFeatureBean {

	private String productFeatureId;
	private String productFeatureTypeId;
	private String description;
	
	
	public String getProductFeatureId() {
		return productFeatureId;
	}
	public void setProductFeatureId(String productFeatureId) {
		this.productFeatureId = productFeatureId;
	}
	public String getProductFeatureTypeId() {
		return productFeatureTypeId;
	}
	public void setProductFeatureTypeId(String productFeatureTypeId) {
		this.productFeatureTypeId = productFeatureTypeId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "FindProductFeatureBean [productFeatureId=" + productFeatureId + ", productFeatureTypeId="
				+ productFeatureTypeId + ", description=" + description + "]";
	}
	

}
