package org.ofbiz.storegroup.bean;

import java.sql.Timestamp;

public class StoreAndGroupBean {
	private String storeID;
	private String storeGroupID;
	private String storeName;
	private String storeGroupName;
	private Timestamp createTime;
	
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getStoreGroupID() {
		return storeGroupID;
	}
	public void setStoreGroupID(String storeGroupID) {
		this.storeGroupID = storeGroupID;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreGroupName() {
		return storeGroupName;
	}
	public void setStoreGroupName(String storeGroupName) {
		this.storeGroupName = storeGroupName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public StoreAndGroupBean() {
	}
	
	
	public StoreAndGroupBean(String storeID, String storeGroupID, String storeName) {
		this.storeID = storeID;
		this.storeGroupID = storeGroupID;
		this.storeName = storeName;
	}
	public StoreAndGroupBean(String storeID, String storeGroupID, String storeName, String storeGroupName,
			Timestamp createTime) {
		this.storeID = storeID;
		this.storeGroupID = storeGroupID;
		this.storeName = storeName;
		this.storeGroupName = storeGroupName;
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "StoreAndGroupPageBeen [storeID=" + storeID + ", storeGroupID=" + storeGroupID + ", storeName="
				+ storeName + ", storeGroupName=" + storeGroupName + ", createTime=" + "createTime]";
	}
}
