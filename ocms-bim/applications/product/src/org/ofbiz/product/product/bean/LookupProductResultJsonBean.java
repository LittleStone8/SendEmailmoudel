package org.ofbiz.product.product.bean;

import java.math.BigDecimal;
import java.util.Date;

public class LookupProductResultJsonBean {

	private String productId;//产品标示
	private String productTypeId;//产品类型
	private String productCategoryId;//分类
	private String internalName;//名称
	private String brandName;//品牌
	private String originGeoId;//原产地
	private String currencyUomId;//币值
	private String facilityname;//所属仓名
	private String cost;//成本
	private String SPU;//SPU
	private String description;//描述
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	
	public String getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public String getOriginGeoId() {
		return originGeoId;
	}
	public void setOriginGeoId(String originGeoId) {
		this.originGeoId = originGeoId;
	}
	public String getCurrencyUomId() {
		return currencyUomId;
	}
	public void setCurrencyUomId(String currencyUomId) {
		this.currencyUomId = currencyUomId;
	}
	

	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public String getFacilityname() {
		return facilityname;
	}
	public void setFacilityname(String facilityname) {
		this.facilityname = facilityname;
	}
	
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public LookupProductResultJsonBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSPU() {
		return SPU;
	}
	public void setSPU(String sPU) {
		SPU = sPU;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void  replenish()
	{
		if(productId==null||"".equals(productId))productId="---";
		if(productTypeId==null||"".equals(productTypeId))productTypeId="---";
		if(productCategoryId==null||"".equals(productCategoryId))productCategoryId="---";
		if(internalName==null||"".equals(internalName))internalName="---";
		if(brandName==null||"".equals(brandName))brandName="---";
		if(originGeoId==null||"".equals(originGeoId))originGeoId="---";
		if(currencyUomId==null||"".equals(originGeoId))currencyUomId="---";
		if(facilityname==null||"".equals(facilityname))facilityname="---";
		if(cost==null||"".equals(cost))cost="---";
		if(SPU==null||"".equals(SPU))SPU="---";
		if(description==null||"".equals(description))description="---";
	}
}
