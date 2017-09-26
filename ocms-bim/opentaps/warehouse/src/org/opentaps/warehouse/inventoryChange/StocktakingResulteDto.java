package org.opentaps.warehouse.inventoryChange;

public class StocktakingResulteDto {
	private String NO;
	private String PRODUCT_ID;
	private String ean;
	private String description;
	private String inventoryquantity;
	private String DIFquantity;
	private String Reason;
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInventoryquantity() {
		return inventoryquantity;
	}
	public void setInventoryquantity(String inventoryquantity) {
		this.inventoryquantity = inventoryquantity;
	}
	public String getDIFquantity() {
		return DIFquantity;
	}
	public void setDIFquantity(String dIFquantity) {
		DIFquantity = dIFquantity;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	
	public String getPRODUCT_ID() {
		return PRODUCT_ID;
	}
	public void setPRODUCT_ID(String pRODUCT_ID) {
		PRODUCT_ID = pRODUCT_ID;
	}
	@Override
	public String toString() {
		return "StocktakingResulteDto [NO=" + NO + ", ean=" + ean + ", description=" + description
				+ ", inventoryquantity=" + inventoryquantity + ", DIFquantity=" + DIFquantity + ", Reason=" + Reason
				+ "]";
	}
	public StocktakingResulteDto(String nO, String ean, String description, String inventoryquantity,
			String dIFquantity, String reason) {
		NO = nO;
		this.ean = ean;
		this.description = description;
		this.inventoryquantity = inventoryquantity;
		DIFquantity = dIFquantity;
		Reason = reason;
	}
	
}
