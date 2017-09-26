package org.ofbiz.storegroup.bean;

public class StoreGroupBean {
	private String sGroupId;
	private String sGroupName;

	public String getsGroupName() {
		return sGroupName;
	}
	public void setsGroupName(String sGroupName) {
		this.sGroupName = sGroupName;
	}
	public String getsGroupId() {
		return sGroupId;
	}
	public void setsGroupId(String sGroupId) {
		this.sGroupId = sGroupId;
	}
	public StoreGroupBean(String sGroupId,String sGroupName) {
		this.sGroupId = sGroupId;
		this.sGroupName = sGroupName;

	}
	public StoreGroupBean() {
	}
	@Override
	public String toString() {
		return "StoreGroupBean [sGroupName=" + sGroupName + ", sGroupId=" + sGroupId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sGroupId == null) ? 0 : sGroupId.hashCode());
		result = prime * result + ((sGroupName == null) ? 0 : sGroupName.hashCode());
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
		StoreGroupBean other = (StoreGroupBean) obj;
		if (sGroupId == null) {
			if (other.sGroupId != null)
				return false;
		} else if (!sGroupId.equals(other.sGroupId))
			return false;
		if (sGroupName == null) {
			if (other.sGroupName != null)
				return false;
		} else if (!sGroupName.equals(other.sGroupName))
			return false;
		return true;
	}
	
	
}
