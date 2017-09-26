package org.ofbiz.storegroup.bean;

public class StoreRollUpBean{
	private String storeID;
	private String storeGroupID; 
	private String storeName;
	
	public StoreRollUpBean(String storeID, String storeGroupID, String storeName) {
		this.storeID = storeID;
		this.storeGroupID = storeGroupID;
		this.storeName = storeName;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((storeGroupID == null) ? 0 : storeGroupID.hashCode());
		result = prime * result + ((storeID == null) ? 0 : storeID.hashCode());
		result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreRollUpBean other = (StoreRollUpBean) obj;
		if (storeGroupID == null) {
			if (other.storeGroupID != null)
				return false;
		} else if (!storeGroupID.equals(other.storeGroupID))
			return false;
		if (storeID == null) {
			if (other.storeID != null)
				return false;
		} else if (!storeID.equals(other.storeID))
			return false;
		if (storeName == null) {
			if (other.storeName != null)
				return false;
		} else if (!storeName.equals(other.storeName))
			return false;
		return true;
	}
	
}
