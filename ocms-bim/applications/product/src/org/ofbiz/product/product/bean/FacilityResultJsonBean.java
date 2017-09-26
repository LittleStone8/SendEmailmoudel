package org.ofbiz.product.product.bean;

import java.math.BigDecimal;
import java.util.Date;

public class FacilityResultJsonBean {


	String FacilityId="";
	String FacilityName="";
	public String getFacilityId() {
		return FacilityId;
	}
	public void setFacilityId(String facilityId) {
		FacilityId = facilityId;
	}
	public String getFacilityName() {
		return FacilityName;
	}
	public void setFacilityName(String facilityName) {
		FacilityName = facilityName;
	}
	public FacilityResultJsonBean(String facilityId, String facilityName) {
		super();
		FacilityId = facilityId;
		FacilityName = facilityName;
	}
	public FacilityResultJsonBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
