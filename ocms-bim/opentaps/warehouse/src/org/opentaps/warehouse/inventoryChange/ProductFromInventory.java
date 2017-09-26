package org.opentaps.warehouse.inventoryChange;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 连接product表与inventoryItem表实体类
 * @author wei.he
 *
 */
public class ProductFromInventory {
	private String productId;
	private String brandName;
	private String internalName;
	private BigDecimal quantityOnHandTotal;
	private BigDecimal qoh;
	

	private String description;
	
	private String inventoryItemId;
	private String serialNumber;
	private String lotId;
	private Timestamp datetimeReceived;
	private String statusId;
	private String inventoryTransferId;
	private String facilityIdTo;
	private String facilityId;
	private Timestamp sendDate;
	private String facilityName;
	
	public ProductFromInventory() {
		// TODO Auto-generated constructor stub
	}

	public ProductFromInventory(String productId, String brandName, String internalName,
			BigDecimal quantityOnHandTotal) {
		super();
		this.productId = productId;
		this.brandName = brandName;
		this.internalName = internalName;
		this.quantityOnHandTotal = quantityOnHandTotal;
		
	}
	
	
	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public String getInventoryTransferId() {
		return inventoryTransferId;
	}

	public void setInventoryTransferId(String inventoryTransferId) {
		this.inventoryTransferId = inventoryTransferId;
	}

	public String getFacilityIdTo() {
		return facilityIdTo;
	}

	public void setFacilityIdTo(String facilityIdTo) {
		this.facilityIdTo = facilityIdTo;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public BigDecimal getQoh() {
		return qoh;
	}


	public void setQoh(BigDecimal qoh) {
		this.qoh = qoh;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public BigDecimal getQuantityOnHandTotal() {
		return quantityOnHandTotal;
	}

	public void setQuantityOnHandTotal(BigDecimal quantityOnHandTotal) {
		this.quantityOnHandTotal = quantityOnHandTotal;
	}

	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
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

	public Timestamp getDatetimeReceived() {
		return datetimeReceived;
	}

	public void setDatetimeReceived(Timestamp datetimeReceived) {
		this.datetimeReceived = datetimeReceived;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
}
