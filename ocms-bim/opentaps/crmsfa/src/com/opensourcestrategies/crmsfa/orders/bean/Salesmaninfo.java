package com.opensourcestrategies.crmsfa.orders.bean;

public class Salesmaninfo {
	private String fristname;
	private String milldename;
	private String lastname;
	public String getFristname() {
		return fristname;
	}
	public void setFristname(String fristname) {
		this.fristname = fristname;
	}
	public String getMilldename() {
		return milldename;
	}
	public void setMilldename(String milldename) {
		this.milldename = milldename;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Salesmaninfo(String fristname, String milldename, String lastname) {
		super();
		this.fristname = fristname;
		this.milldename = milldename;
		this.lastname = lastname;
	}
	public Salesmaninfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void replich()
	{
		fristname=fristname==null?"":fristname;
		milldename=milldename==null?"":milldename;
		lastname=lastname==null?"":lastname;
	}
	
}
