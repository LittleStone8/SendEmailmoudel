package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class TranferInventoryVo {
	
	private String inventoryTransferId;
	private String inventoryItemId;
	private String productId;
	private String model;
	private String description;
	private String transferNumber;
	private String sendDate;
	private String statusId;
	private String facilityId;
	private String facilityName;
	private String quantityOnHandTotal;
	private String comments;
	private String imei;
	
	
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getInventoryTransferId() {
		return inventoryTransferId;
	}
	public void setInventoryTransferId(String inventoryTransferId) {
		this.inventoryTransferId = inventoryTransferId;
	}
	public String getInventoryItemId() {
		return inventoryItemId;
	}
	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransferNumber() {
		return transferNumber;
	}
	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getQuantityOnHandTotal() {
		return quantityOnHandTotal;
	}
	public void setQuantityOnHandTotal(String quantityOnHandTotal) {
		this.quantityOnHandTotal = quantityOnHandTotal;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "TranferInventoryVo [inventoryTransferId=" + inventoryTransferId + ", inventoryItemId=" + inventoryItemId
				+ ", productId=" + productId + ", model=" + model + ", description=" + description + ", transferNumber="
				+ transferNumber + ", sendDate=" + sendDate + ", statusId=" + statusId + ", facilityId=" + facilityId
				+ ", facilityName=" + facilityName + ", quantityOnHandTotal=" + quantityOnHandTotal + ", comments="
				+ comments + ", imei=" + imei + "]";
	}
	
	
	
}
