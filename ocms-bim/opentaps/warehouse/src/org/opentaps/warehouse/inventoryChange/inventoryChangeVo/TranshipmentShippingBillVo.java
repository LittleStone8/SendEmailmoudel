package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TranshipmentShippingBillVo {
	private String id;
	private String status;
	private String toWarehouse;
	private String createTime;
	private String lastUpdateTime;
	private String createUserLoginId;
	private String lastUpdateUserLoginid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToWarehouse() {
		return toWarehouse;
	}
	public void setToWarehouse(String toWarehouse) {
		this.toWarehouse = toWarehouse;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getCreateUserLoginId() {
		return createUserLoginId;
	}
	public void setCreateUserLoginId(String createUserLoginId) {
		this.createUserLoginId = createUserLoginId;
	}
	public String getLastUpdateUserLoginid() {
		return lastUpdateUserLoginid;
	}
	public void setLastUpdateUserLoginid(String lastUpdateUserLoginid) {
		this.lastUpdateUserLoginid = lastUpdateUserLoginid;
	}
	@Override
	public String toString() {
		return "TranshipmentShippingBillVo [id=" + id + ", status=" + status + ", toWarehouse=" + toWarehouse
				+ ", createTime=" + createTime + ", lastUpdateTime=" + lastUpdateTime + ", createUserLoginId="
				+ createUserLoginId + ", lastUpdateUserLoginid=" + lastUpdateUserLoginid + "]";
	}
	
}
