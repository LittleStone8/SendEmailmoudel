package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class TakeStockModel {
	private String takeStockId;
	private String createTime;
	private String createYears;
	private String operator;
	private String isIMEI;
	private String facilityName;
	private String resultType;
	
	
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getTakeStockId() {
		return takeStockId;
	}
	public void setTakeStockId(String takeStockId) {
		this.takeStockId = takeStockId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateYears() {
		return createYears;
	}
	public void setCreateYears(String createYears) {
		this.createYears = createYears;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getIsIMEI() {
		return isIMEI;
	}
	public void setIsIMEI(String isIMEI) {
		this.isIMEI = isIMEI;
	}
	@Override
	public String toString() {
		return "TakeStockModel [takeStockId=" + takeStockId + ", createTime=" + createTime + ", createYears="
				+ createYears + ", operator=" + operator + ", isIMEI=" + isIMEI + ", facilityName=" + facilityName
				+ ", resultType=" + resultType + "]";
	}
	
	
	
}
