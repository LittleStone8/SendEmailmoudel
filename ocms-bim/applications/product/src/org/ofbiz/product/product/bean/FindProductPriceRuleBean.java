package org.ofbiz.product.product.bean;

public class FindProductPriceRuleBean {

	
	private String ProducId;
	private String description;
	private String store;
	private String currentPrice;
	private String lastModifiedDate;
	private String storeId;
	public String getProducId() {
		return ProducId;
	}
	public void setProducId(String producId) {
		ProducId = producId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	
}
