package com.opensourcestrategies.crmsfa.orders.bean;

import java.math.BigDecimal;
import java.util.List;

public class ShippingInfoBean {
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String customerPartyId;
	private String address1;
	private String address2;
	private String countyGeo;
	private String stateProvinceGeo;
	private String city;
	private String countryGeo;
	private String promisedDatetime;
	private String totalAdjustments;
	private String contactNumber;
	private String currencyUom;
	private List<Double> adjustmentsList;
	
	
	
	public String getFirstNameLocal() {
		return firstNameLocal;
	}
	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}
	public String getMiddleNameLocal() {
		return middleNameLocal;
	}
	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}
	public String getLastNameLocal() {
		return lastNameLocal;
	}
	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}
	public String getCustomerPartyId() {
		return customerPartyId;
	}
	public void setCustomerPartyId(String customerPartyId) {
		this.customerPartyId = customerPartyId;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCountyGeo() {
		return countyGeo;
	}
	public void setCountyGeo(String countyGeo) {
		this.countyGeo = countyGeo;
	}
	public String getStateProvinceGeo() {
		return stateProvinceGeo;
	}
	public void setStateProvinceGeo(String stateProvinceGeo) {
		this.stateProvinceGeo = stateProvinceGeo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryGeo() {
		return countryGeo;
	}
	public void setCountryGeo(String countryGeo) {
		this.countryGeo = countryGeo;
	}
	public String getPromisedDatetime() {
		return promisedDatetime;
	}
	public void setPromisedDatetime(String promisedDatetime) {
		this.promisedDatetime = promisedDatetime;
	}
	public String getTotalAdjustments() {
		return totalAdjustments;
	}
	public void setTotalAdjustments(String totalAdjustments) {
		this.totalAdjustments = totalAdjustments;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getCurrencyUom() {
		return currencyUom;
	}
	public void setCurrencyUom(String currencyUom) {
		this.currencyUom = currencyUom;
	}
	public List<Double> getAdjustmentsList() {
		return adjustmentsList;
	}
	public void setAdjustmentsList(List<Double> adjustmentsList) {
		this.adjustmentsList = adjustmentsList;
	}

	
	
	
	
}
