package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class IMEIInventoryCheckingVo {
	private String imei;
	private String reason;
	private String description;
	private String model;
	private String brandName;
	private String reasonType;
	
	
	
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	@Override
	public String toString() {
		return "IMEIInventoryCheckingVo [imei=" + imei + ", reason=" + reason + ", description=" + description
				+ ", model=" + model + ", brandName=" + brandName + ", reasonType=" + reasonType + "]";
	}
	
}	
