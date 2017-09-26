package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class ProductEanVo {
	private String product;
	private String type;
	private String value;
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ProductEanVo [product=" + product + ", type=" + type + ", value=" + value + "]";
	}
	
	
}
