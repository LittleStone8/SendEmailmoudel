package org.ofbiz.product.product.bean;

import java.math.BigDecimal;
import java.util.Date;

public class FindAllproductPriceBean {

	private String productId;
	private String productPriceTypeId;
	private String productPricePurposeId;
	private String currencyUomId;
	private String productStoreGroupID;
	private String fromDate;
	private String thruDate;
	private String price;
	private String customPriceCalcService;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductPriceTypeId() {
		return productPriceTypeId;
	}
	public void setProductPriceTypeId(String productPriceTypeId) {
		this.productPriceTypeId = productPriceTypeId;
	}
	public String getProductPricePurposeId() {
		return productPricePurposeId;
	}
	public void setProductPricePurposeId(String productPricePurposeId) {
		this.productPricePurposeId = productPricePurposeId;
	}
	public String getCurrencyUomId() {
		return currencyUomId;
	}
	public void setCurrencyUomId(String currencyUomId) {
		this.currencyUomId = currencyUomId;
	}
	public String getProductStoreGroupID() {
		return productStoreGroupID;
	}
	public void setProductStoreGroupID(String productStoreGroupID) {
		this.productStoreGroupID = productStoreGroupID;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getThruDate() {
		return thruDate;
	}
	public void setThruDate(String thruDate) {
		this.thruDate = thruDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCustomPriceCalcService() {
		return customPriceCalcService;
	}
	public void setCustomPriceCalcService(String customPriceCalcService) {
		this.customPriceCalcService = customPriceCalcService;
	}
	@Override
	public String toString() {
		return "FindAllproductPriceBean [productId=" + productId + ", productPriceTypeId=" + productPriceTypeId
				+ ", productPricePurposeId=" + productPricePurposeId + ", currencyUomId=" + currencyUomId
				+ ", productStoreGroupID=" + productStoreGroupID + ", fromDate=" + fromDate + ", thruDate=" + thruDate
				+ ", price=" + price + ", customPriceCalcService=" + customPriceCalcService + "]";
	}
	
	
	
	





	
	
	
	
	
	
}
