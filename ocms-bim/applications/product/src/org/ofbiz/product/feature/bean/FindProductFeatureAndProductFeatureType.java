package org.ofbiz.product.feature.bean;

import java.util.List;

public class FindProductFeatureAndProductFeatureType {

	private String productFeatureTypeId;
	private String description;
	private List<FindProductFeatureBean> productFeatureList;
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
	public List<FindProductFeatureBean> getProductFeatureList() {
		return productFeatureList;
	}
	public void setProductFeatureList(List<FindProductFeatureBean> productFeatureList) {
		this.productFeatureList = productFeatureList;
	}
	@Override
	public String toString() {
		return "FindProductFeatureAndProductFeatureType [productFeatureTypeId=" + productFeatureTypeId
				+ ", description=" + description + ", productFeatureList=" + productFeatureList + "]";
	}
	
	
}
