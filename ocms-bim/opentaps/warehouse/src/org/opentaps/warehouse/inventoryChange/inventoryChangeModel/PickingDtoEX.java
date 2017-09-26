package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class PickingDtoEX {
	private String productId;
	private String transNum;
	private String isIMEI;
	private String imei;
	private String quantity;
	private String EAN;
	private String result;
	private String reason;
	
	
	public String getEAN() {
		return EAN;
	}
	public void setEAN(String eAN) {
		EAN = eAN;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	public String getIsIMEI() {
		return isIMEI;
	}
	public void setIsIMEI(String isIMEI) {
		this.isIMEI = isIMEI;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public String toString() {
		return "PickingDto [productId=" + productId + ", transNum=" + transNum + ", isIMEI=" + isIMEI + ", imei=" + imei
				+ ", quantity=" + quantity + ", EAN=" + EAN + "]";
	}
	
	
}
