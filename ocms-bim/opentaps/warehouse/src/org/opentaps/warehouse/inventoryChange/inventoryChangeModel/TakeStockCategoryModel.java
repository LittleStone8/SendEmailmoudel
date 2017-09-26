package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class TakeStockCategoryModel {
	private String takeStockId; 
	private String imeiOrEan; 
	private String quantity;
	public String getTakeStockId() {
		return takeStockId;
	}
	public void setTakeStockId(String takeStockId) {
		this.takeStockId = takeStockId;
	}
	public String getImeiOrEan() {
		return imeiOrEan;
	}
	public void setImeiOrEan(String imeiOrEan) {
		this.imeiOrEan = imeiOrEan;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "TakeStockCategoryModel [takeStockId=" + takeStockId + ", imeiOrEan=" + imeiOrEan + ", quantity="
				+ quantity + "]";
	} 
	
}
