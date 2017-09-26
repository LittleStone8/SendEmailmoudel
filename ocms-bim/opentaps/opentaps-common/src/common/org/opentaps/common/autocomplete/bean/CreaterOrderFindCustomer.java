package org.opentaps.common.autocomplete.bean;

/**
 *创建订单查找用户 
 *chenshihua
 *2017-3-27 
 */
public class CreaterOrderFindCustomer {

	private String partyId;
	private String firstName;
	private String lastName;
	private String groupName;
	private String primaryCountryCode;
	private String primaryAreaCode;
	private String primaryContactNumber;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPrimaryCountryCode() {
		return primaryCountryCode;
	}
	public void setPrimaryCountryCode(String primaryCountryCode) {
		this.primaryCountryCode = primaryCountryCode;
	}
	public String getPrimaryAreaCode() {
		return primaryAreaCode;
	}
	public void setPrimaryAreaCode(String primaryAreaCode) {
		this.primaryAreaCode = primaryAreaCode;
	}
	public String getPrimaryContactNumber() {
		return primaryContactNumber;
	}
	public void setPrimaryContactNumber(String primaryContactNumber) {
		this.primaryContactNumber = primaryContactNumber;
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
		return "CreaterOrderFindCustomer [partyId=" + partyId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", groupName=" + groupName + ", primaryCountryCode=" + primaryCountryCode + ", primaryAreaCode="
				+ primaryAreaCode + ", primaryContactNumber=" + primaryContactNumber + ", primaryAddress1="
				+ primaryAddress1 + ", primaryAddress2=" + primaryAddress2 + "]";
	}
	
	
	
	
}
