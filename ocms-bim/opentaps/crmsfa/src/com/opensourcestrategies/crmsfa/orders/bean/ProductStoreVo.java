package com.opensourcestrategies.crmsfa.orders.bean;

public class ProductStoreVo {
	private String productStoreId;
	private String storeName;
	private String issupportaccount;
	public String getProductStoreId() {
		return productStoreId;
	}
	public void setProductStoreId(String productStoreId) {
		this.productStoreId = productStoreId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getIssupportaccount() {
		return issupportaccount;
	}
	public void setIssupportaccount(String issupportaccount) {
		this.issupportaccount = issupportaccount;
	}
	@Override
	public String toString() {
		return "ProductStoreVo [productStoreId=" + productStoreId + ", storeName=" + storeName
				+ ", issupportaccount=" + issupportaccount + "]";
	}
	
	

}
