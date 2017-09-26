package org.ofbiz.regularRecord;

import java.sql.Timestamp;

public class RecordInventoryBean implements Cloneable{

	public String productId;
	public String productStoreGroupId;
	public String facilityId;
	public String facilityName;
	public Timestamp recordDate;
	public double quantityOnHandTotal;
	public double avgCost;
	public String description;
	public String productname;
	public double price;
	public String brandName;
	public String index;
	public String recortimestamp;
	
	
	public String getRecortimestamp() {
		return recortimestamp;
	}
	public void setRecortimestamp(String recortimestamp) {
		this.recortimestamp = recortimestamp;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductStoreGroupId() {
		return productStoreGroupId;
	}
	public void setProductStoreGroupId(String productStoreGroupId) {
		this.productStoreGroupId = productStoreGroupId;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public Timestamp getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Timestamp recordDate) {
		this.recordDate = recordDate;
	}
	public double getQuantityOnHandTotal() {
		return quantityOnHandTotal;
	}
	public void setQuantityOnHandTotal(double quantityOnHandTotal) {
		this.quantityOnHandTotal = quantityOnHandTotal;
	}
	public double getAvgCost() {
		return avgCost;
	}
	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public RecordInventoryBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String toinsertsql()
	{
		Timestamp nowtime = new Timestamp(System.currentTimeMillis());
		
		return  " (\""+productId+"\",\""+facilityId+"\",\""+productStoreGroupId+"\",\""+index+"\",\""+recordDate+"\",\""+quantityOnHandTotal+"\",\""+avgCost+"\",\""+price+"\",\""+description+"\",\""+productname+"\",\""+nowtime+"\",\""+nowtime+"\",\""+nowtime+"\",\""+nowtime+"\",\""+facilityName+"\",\""+brandName+"\")";
		
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
