package org.opentaps.warehouse.inventoryChange;

public class LotVo {
	private String lotId;
	private String createDate;
	private String expirationDate;
	private String uomId;
	private String quantity;
	private String comments;
	
	public String getLotId() {
		return lotId;
	}
	public void setLotId(String lotId) {
		this.lotId = lotId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getUomId() {
		return uomId;
	}
	public void setUomId(String uomId) {
		this.uomId = uomId;
	}
	@Override
	public String toString() {
		return "LotVo [lotId=" + lotId + ", createDate=" + createDate + ", expirationDate=" + expirationDate
				+ ", uomId=" + uomId + ", quantity=" + quantity + ", comments=" + comments + "]";
	}
	
	
	
}
