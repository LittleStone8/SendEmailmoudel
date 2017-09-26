package com.opensourcestrategies.crmsfa.orders.bean;

public class FindSalesChannel_bean {
	
	private String enumId;
	private String description;
	private String enumCode;
	private String enumTypeId;
	public String getEnumId() {
		return enumId;
	}
	public void setEnumId(String enumId) {
		this.enumId = enumId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnumCode() {
		return enumCode;
	}
	public void setEnumCode(String enumCode) {
		this.enumCode = enumCode;
	}
	public String getEnumTypeId() {
		return enumTypeId;
	}
	public void setEnumTypeId(String enumTypeId) {
		this.enumTypeId = enumTypeId;
	}
	
	@Override
	public String toString() {
		return "fingSalesChannel_bean [enumId=" + enumId + ", description=" + description + ", enumCode=" + enumCode
				+ ", enumTypeId=" + enumTypeId + "]";
	}
	
	
		
}
