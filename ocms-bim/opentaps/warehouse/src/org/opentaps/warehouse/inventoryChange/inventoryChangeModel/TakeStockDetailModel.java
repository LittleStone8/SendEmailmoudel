package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class TakeStockDetailModel {
	private String takeStockDetailId;
	private String takeStockId;
	private String imeiOrEan;
	private String description;
	private String inventoryQuantity;
	private String difQuantity;
	private String reason;
	private String status;
	public String getTakeStockDetailId() {
		return takeStockDetailId;
	}
	public void setTakeStockDetailId(String takeStockDetailId) {
		this.takeStockDetailId = takeStockDetailId;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInventoryQuantity() {
		return inventoryQuantity;
	}
	public void setInventoryQuantity(String inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
	public String getDifQuantity() {
		return difQuantity;
	}
	public void setDifQuantity(String difQuantity) {
		this.difQuantity = difQuantity;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "TakeStockDetailModel [takeStockDetailId=" + takeStockDetailId + ", takeStockId=" + takeStockId
				+ ", imeiOrEan=" + imeiOrEan + ", description=" + description + ", inventoryQuantity="
				+ inventoryQuantity + ", difQuantity=" + difQuantity + ", reason=" + reason + ", status=" + status
				+ "]";
	}
	
	
}
