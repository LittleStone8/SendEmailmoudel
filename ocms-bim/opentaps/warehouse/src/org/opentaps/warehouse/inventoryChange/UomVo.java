package org.opentaps.warehouse.inventoryChange;

public class UomVo {
	private String uomId;
	private String description;
	public String getUomId() {
		return uomId;
	}
	public void setUomId(String uomId) {
		this.uomId = uomId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "UomVo [uomId=" + uomId + ", description=" + description + "]";
	}
	
	

}
