package org.ofbiz.product.feature.bean;

public class ProductFeatureApplBean {

	
	
	private String productId;
	private String productFeatureId;
	private String productFeatureApplTypeId;
	private String fromDate;
	private String thruDate;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductFeatureId() {
		return productFeatureId;
	}
	public void setProductFeatureId(String productFeatureId) {
		this.productFeatureId = productFeatureId;
	}
	public String getProductFeatureApplTypeId() {
		return productFeatureApplTypeId;
	}
	public void setProductFeatureApplTypeId(String productFeatureApplTypeId) {
		this.productFeatureApplTypeId = productFeatureApplTypeId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getThruDate() {
		return thruDate;
	}
	public void setThruDate(String thruDate) {
		this.thruDate = thruDate;
	}
	@Override
	public String toString() {
		return "ProductFeatureApplBean [productId=" + productId + ", productFeatureId=" + productFeatureId
				+ ", productFeatureApplTypeId=" + productFeatureApplTypeId + ", fromDate=" + fromDate + ", thruDate="
				+ thruDate + "]";
	}
	
	
	

}
