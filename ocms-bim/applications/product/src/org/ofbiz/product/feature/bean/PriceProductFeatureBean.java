package org.ofbiz.product.feature.bean;

import java.util.List;
import java.util.Map;

public class PriceProductFeatureBean {

	private String productIdSku;
	private Map<String, String> productFeatureMap;
	
	public String getProductIdSku() {
		return productIdSku;
	}
	public void setProductIdSku(String productIdSku) {
		this.productIdSku = productIdSku;
	}
	public Map<String, String> getProductFeatureMap() {
		return productFeatureMap;
	}
	public void setProductFeatureMap(Map<String, String> productFeatureMap) {
		this.productFeatureMap = productFeatureMap;
	}
	@Override
	public String toString() {
		return "PriceProductFeatureBean [productIdSku=" + productIdSku + ", productFeatureMap=" + productFeatureMap
				+ "]";
	}
	
	
	
	
	
	
}
