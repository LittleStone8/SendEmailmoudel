package org.opentaps.warehouse.inventoryChange;

import org.apache.commons.lang.StringUtils;

public class InventoryItemVo {
	
	private String inventoryItemId;
	
	private String productId;
	
	private String internalName;
	
	private String serialNumber;
	
	private String lotId;
	
	private String datetimeReceived;
	
	private String statusId;
	
	private String quantityOnHandTotal;
	
	private String description;

	private String facilityName;
	
	
	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
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

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getDatetimeReceived() {
		return datetimeReceived;
	}

	public void setDatetimeReceived(String datetimeReceived) {
		this.datetimeReceived = datetimeReceived;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getQuantityOnHandTotal() {
		return quantityOnHandTotal;
	}

	public void setQuantityOnHandTotal(String quantityOnHandTotal) {
		this.quantityOnHandTotal = quantityOnHandTotal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(!StringUtils.isEmpty(description)){
			this.description = description.replaceAll("\r", ""); 
		}else{
			this.description = description;
		}
	}
	
}
