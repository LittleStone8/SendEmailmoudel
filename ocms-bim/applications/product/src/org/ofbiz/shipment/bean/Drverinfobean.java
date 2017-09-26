package org.ofbiz.shipment.bean;

public class Drverinfobean {
	private String partyid;
	private String firstname;
	private String telnumber;
	public String getPartyid() {
		return partyid;
	}
	public void setPartyid(String partyid) {
		this.partyid = partyid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getTelnumber() {
		return telnumber;
	}
	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}
	public Drverinfobean(String partyid, String firstname, String telnumber) {
		super();
		this.partyid = partyid;
		this.firstname = firstname;
		this.telnumber = telnumber;
	}
	public Drverinfobean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void reforming()
	{
		if(partyid==null)partyid="";
		if(firstname==null)firstname="";
		if(telnumber==null)telnumber="";
	}
}
