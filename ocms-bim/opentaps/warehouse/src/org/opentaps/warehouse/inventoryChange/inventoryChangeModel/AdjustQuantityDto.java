package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class AdjustQuantityDto {
	private String productId;
	private String adjustNumber;
	private String varianceReasonId;
	private String common;
	private String totalNumber;
	private String model;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAdjustNumber() {
		return adjustNumber;
	}
	public void setAdjustNumber(String adjustNumber) {
		this.adjustNumber = adjustNumber;
	}
	public String getVarianceReasonId() {
		return varianceReasonId;
	}
	public void setVarianceReasonId(String varianceReasonId) {
		this.varianceReasonId = varianceReasonId;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
	public String getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "AdjustQuantityDto [productId=" + productId + ", adjustNumber=" + adjustNumber + ", varianceReasonId="
				+ varianceReasonId + ", common=" + common + ", totalNumber=" + totalNumber + ", model=" + model + "]";
	}
	
	
}
