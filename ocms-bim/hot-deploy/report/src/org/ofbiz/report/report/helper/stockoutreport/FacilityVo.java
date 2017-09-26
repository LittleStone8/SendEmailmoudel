package org.ofbiz.report.report.helper.stockoutreport;

public class FacilityVo implements Comparable<FacilityVo>{
	
	private String facilityId;
	private String facilityName;
	
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	@Override
	public String toString() {
		return "FacilityVo [facilityId=" + facilityId + ", facilityName=" + facilityName + "]";
	}
	public int compareTo(FacilityVo facilityVo) {
		String otherFacilityName = facilityVo.getFacilityName();
		if(facilityName == null || otherFacilityName == null){
			return 1;
		}
		return facilityName.compareToIgnoreCase(otherFacilityName);
	}
	
}
