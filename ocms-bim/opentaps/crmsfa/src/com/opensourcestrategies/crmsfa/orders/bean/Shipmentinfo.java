package com.opensourcestrategies.crmsfa.orders.bean;

public class Shipmentinfo {
	private String shippedtime;
	private String via;
	private String traceingcode;
	private String shipmentid;
	public String getShippedtime() {
		return shippedtime;
	}
	public void setShippedtime(String shippedtime) {
		this.shippedtime = shippedtime;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getTraceingcode() {
		return traceingcode;
	}
	public void setTraceingcode(String traceingcode) {
		this.traceingcode = traceingcode;
	}
	public String getShipmentid() {
		return shipmentid;
	}
	public void setShipmentid(String shipmentid) {
		this.shipmentid = shipmentid;
	}
	public void replich()
	{
		shippedtime=shippedtime==null?"":shippedtime;
		via=via==null?"":via;
		traceingcode=traceingcode==null?"":traceingcode;
		shipmentid=shipmentid==null?"":shipmentid;
	}
	
}
