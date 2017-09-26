package org.ofbiz.storegroup.bean;

public class StoreBean {
	private String storeId;
	private String storeName;

	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	@Override
	public String toString() {
		return "StoreBean [storeName=" + storeName + ", storeId=" + storeId + "]";
	}
	public StoreBean() {
	}
	public StoreBean(String storeId,String storeName) {
		this.storeId = storeId;
		this.storeName = storeName;

	}
	
}
