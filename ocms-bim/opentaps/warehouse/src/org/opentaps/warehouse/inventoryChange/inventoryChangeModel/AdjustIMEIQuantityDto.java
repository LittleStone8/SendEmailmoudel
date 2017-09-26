package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class AdjustIMEIQuantityDto {
	private String imei;
	private String varianceImeiReasonId;
	private String itemid;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVarianceImeiReasonId() {
		return varianceImeiReasonId;
	}
	public void setVarianceImeiReasonId(String varianceImeiReasonId) {
		this.varianceImeiReasonId = varianceImeiReasonId;
	}
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	@Override
	public String toString() {
		return "AdjustIMEIQuantityDto [imei=" + imei + ", varianceImeiReasonId=" + varianceImeiReasonId + "]";
	}

	
}
