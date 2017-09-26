package org.opentaps.warehouse.inventoryChange;

public class PersonVo {
	
	private String partyId;
	private String firstName;
	
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
	@Override
	public String toString() {
		return "PersonVo [partyId=" + partyId + ", firstName=" + firstName + "]";
	}
	
	
}
