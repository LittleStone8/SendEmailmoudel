package com.opensourcestrategies.crmsfa.orders.bean;

public class CreateOrderFindCustomer {

	private String partyId;
	private String firstName;
	private String lastName;
	private String primaryPhoneNumber;
	private String primaryPhoneCountryCode;
	private String primaryPhoneAreaCode;
	private String primaryAddress1;
	private String primaryAddress2;
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPrimaryPhoneNumber() {
		return primaryPhoneNumber;
	}
	public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
		this.primaryPhoneNumber = primaryPhoneNumber;
	}
	public String getPrimaryPhoneCountryCode() {
		return primaryPhoneCountryCode;
	}
	public void setPrimaryPhoneCountryCode(String primaryPhoneCountryCode) {
		this.primaryPhoneCountryCode = primaryPhoneCountryCode;
	}
	public String getPrimaryPhoneAreaCode() {
		return primaryPhoneAreaCode;
	}
	public void setPrimaryPhoneAreaCode(String primaryPhoneAreaCode) {
		this.primaryPhoneAreaCode = primaryPhoneAreaCode;
	}
	public String getPrimaryAddress1() {
		return primaryAddress1;
	}
	public void setPrimaryAddress1(String primaryAddress1) {
		this.primaryAddress1 = primaryAddress1;
	}
	public String getPrimaryAddress2() {
		return primaryAddress2;
	}
	public void setPrimaryAddress2(String primaryAddress2) {
		this.primaryAddress2 = primaryAddress2;
	}
	@Override
	public String toString() {
		return "createOrderFindCustomer [partyId=" + partyId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", primaryPhoneNumber=" + primaryPhoneNumber + ", primaryPhoneCountryCode=" + primaryPhoneCountryCode
				+ ", primaryPhoneAreaCode=" + primaryPhoneAreaCode + ", primaryAddress1=" + primaryAddress1
				+ ", primaryAddress2=" + primaryAddress2 + "]";
	}
	
	public void  replenish()
	{
		if(partyId==null||"".equals(partyId))partyId="";
		if(firstName==null||"".equals(firstName))firstName="";
		if(lastName==null||"".equals(lastName))lastName="";
		if(primaryPhoneNumber==null||"".equals(primaryPhoneNumber))primaryPhoneNumber="";
		if(primaryPhoneCountryCode==null||"".equals(primaryPhoneCountryCode))primaryPhoneCountryCode="";
		if(primaryPhoneAreaCode==null||"".equals(primaryPhoneAreaCode))primaryPhoneAreaCode="";
		if(primaryAddress1==null||"".equals(primaryAddress1))primaryAddress1="";
		if(primaryAddress2==null||"".equals(primaryAddress2))primaryAddress2="";
	}
	
	
}
