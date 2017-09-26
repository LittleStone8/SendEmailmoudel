package com.opensourcestrategies.crmsfa.orders.bean;

public class CommonBean {

	private String id;
	private String des;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	@Override
	public String toString() {
		return "CommonBean [id=" + id + ", des=" + des + "]";
	}
	
	
}
