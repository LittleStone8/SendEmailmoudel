package org.ofbiz.product.feature.bean;

import java.util.List;

public class ProductIdSku {

	private String ProductIdSku;
	private List<String> productFeature_description;
	
	public String getProductIdSku() {
		return ProductIdSku;
	}
	public void setProductIdSku(String productIdSku) {
		ProductIdSku = productIdSku;
	}
	public List<String> getProductFeature_description() {
		return productFeature_description;
	}
	public void setProductFeature_description(List<String> productFeature_description) {
		this.productFeature_description = productFeature_description;
	}
	@Override
	public String toString() {
		return "ProductIdSku [ProductIdSku=" + ProductIdSku + ", productFeature_description="
				+ productFeature_description + "]";
	}

}
