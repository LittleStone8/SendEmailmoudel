package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

public class AdjustInventoryVo {
	private String productId;
	private String model;
	private String description;
	private String qoh;
	private String atp;
	
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
	@Override
	public String toString() {
		return "AdjustInventoryVo [productId=" + productId + ", model=" + model + ", description=" + description
				+ ", qoh=" + qoh + ", atp=" + atp + "]";
	}
	
	
}
