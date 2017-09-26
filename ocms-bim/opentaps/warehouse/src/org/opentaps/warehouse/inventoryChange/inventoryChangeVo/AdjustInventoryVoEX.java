package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class AdjustInventoryVoEX {
	private String productId;
	private String model;
	private String description;
	private String qoh;
	private String atp;
	private String imei;
	private String status;
	private String itemid;
	
	public String getAtp() {
		return atp;
	}
	public void setAtp(String atp) {
		this.atp = atp;
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
	public String getQoh() {
		return qoh;
	}
	public void setQoh(String qoh) {
		this.qoh = qoh;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if(status==null||"".equals(status))
			this.status="INV_TRANSPORTING";
		else 
			this.status = status;
	}
	
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	@Override
	public String toString() {
		return "AdjustInventoryVo [productId=" + productId + ", model=" + model + ", description=" + description
				+ ", qoh=" + qoh + ", atp=" + atp + "]";
	}
	
	
}
