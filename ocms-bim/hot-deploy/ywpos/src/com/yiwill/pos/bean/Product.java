package com.yiwill.pos.bean;

import java.io.Serializable;

public class Product implements Serializable{

	String productId;
	
	String productName;
	
	String text;
	
	String isImei;
	
	String availableToPromiseTotal;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getIsImei() {
		return isImei;
	}

	public void setIsImei(String isImei) {
		this.isImei = isImei;
	}
	
	public String getAvailableToPromiseTotal() {
		return availableToPromiseTotal;
	}

	public void setAvailableToPromiseTotal(String availableToPromiseTotal) {
		this.availableToPromiseTotal = availableToPromiseTotal;
	}

	public void  replenish()
	{
		if(productId==null||"".equals(productId))productId="";
		if(productName==null||"".equals(productName))productName="";
		if(text==null||"".equals(text))text="";
		if(isImei==null||"".equals(isImei))isImei="";
		if(availableToPromiseTotal==null||"".equals(availableToPromiseTotal))isImei="0";
	}

	
	
	
}