package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdjustIMEIInventoryLogVo {
	private String imei;
	private String description;
	private String model;
	private String atp;
	private String status;
	private String operator;
	private String adjustReason;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getAtp() {
		return atp;
	}
	public void setAtp(String atp) {
		this.atp = atp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAdjustReason() {
		return adjustReason;
	}
	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}
	@Override
	public String toString() {
		return "AdjustIMEIInventoryLogVo [imei=" + imei + ", description=" + description + ", model=" + model + ", atp="
				+ atp + ", status=" + status + ", operator=" + operator + ", adjustReason=" + adjustReason + "]";
	}
	
	
	
	
	
}
