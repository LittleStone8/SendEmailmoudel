package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

import java.util.List;

public class ProductEanModel {
	private List<ProductEanVo> productList;
	private String type;
	
	public List<ProductEanVo> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductEanVo> productList) {
		this.productList = productList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ProductEanModel [productList=" + productList + ", type=" + type + "]";
	}
	
	
}
