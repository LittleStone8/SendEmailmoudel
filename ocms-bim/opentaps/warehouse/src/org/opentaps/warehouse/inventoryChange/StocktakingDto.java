package org.opentaps.warehouse.inventoryChange;

public class StocktakingDto {
	
	private String NO;
	private String EAN;
	private String Quantity;
	private String productid;
	private String description;
	
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getEAN() {
		return EAN;
	}
	public void setEAN(String eAN) {
		EAN = eAN;
	}
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "StocktakingDto [NO=" + NO + ", EAN=" + EAN + ", Quantity=" + Quantity + "]";
	}
}
