package org.ofbiz.report.report.been;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ExportSalesReport {
	
	private String companyName;
	private String shopGroup;
	private String shopName;
	private String orderId;
	private String productId;
	private String orderDate;
	private String paymentMethod;
	private String customerName;
	private String acutalQuantity;
	private String unitPrice;
	private String unit;
	private String amount ;
	private String adjustment ;
	private String receivables ;
	private String purchaseOrSalesLedger;
	private String salePerson;
	private String month;
	private String correctedName;
	private String brand;
	private String model;
	private String description;
	private String category;
	private String subCategory;
	private String thirdSubCategory;
	private String unitDefaultCost;
	private String totalDefaultCost;
	private String unitEgateeCost;
	private String totalEgateeCost;
	private String unitSpecialCost;
	private String totalSpecialCost;
	private String unitRetailCost;
	private String totalRetailCost;
	private String salesMethodType;
	private String salesMethod;
	private String orderStatusId;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getShopGroup() {
		return shopGroup;
	}
	public void setShopGroup(String shopGroup) {
		this.shopGroup = shopGroup;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAcutalQuantity() {
		return acutalQuantity;
	}
	public void setAcutalQuantity(String acutalQuantity) {
		this.acutalQuantity = acutalQuantity;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}
	public String getReceivables() {
		return receivables;
	}
	public void setReceivables(String receivables) {
		this.receivables = receivables;
	}
	public String getPurchaseOrSalesLedger() {
		return purchaseOrSalesLedger;
	}
	public void setPurchaseOrSalesLedger(String purchaseOrSalesLedger) {
		this.purchaseOrSalesLedger = purchaseOrSalesLedger;
	}
	public String getSalePerson() {
		return salePerson;
	}
	public void setSalePerson(String salePerson) {
		this.salePerson = salePerson;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCorrectedName() {
		return correctedName;
	}
	public void setCorrectedName(String correctedName) {
		this.correctedName = correctedName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getThirdSubCategory() {
		return thirdSubCategory;
	}
	public void setThirdSubCategory(String thirdSubCategory) {
		this.thirdSubCategory = thirdSubCategory;
	}
	public String getUnitDefaultCost() {
		return unitDefaultCost;
	}
	public void setUnitDefaultCost(String unitDefaultCost) {
		this.unitDefaultCost = unitDefaultCost;
	}
	public String getTotalDefaultCost() {
		return totalDefaultCost;
	}
	public void setTotalDefaultCost(String totalDefaultCost) {
		this.totalDefaultCost = totalDefaultCost;
	}
	public String getUnitEgateeCost() {
		return unitEgateeCost;
	}
	public void setUnitEgateeCost(String unitEgateeCost) {
		this.unitEgateeCost = unitEgateeCost;
	}
	public String getTotalEgateeCost() {
		return totalEgateeCost;
	}
	public void setTotalEgateeCost(String totalEgateeCost) {
		this.totalEgateeCost = totalEgateeCost;
	}
	public String getUnitSpecialCost() {
		return unitSpecialCost;
	}
	public void setUnitSpecialCost(String unitSpecialCost) {
		this.unitSpecialCost = unitSpecialCost;
	}
	public String getTotalSpecialCost() {
		return totalSpecialCost;
	}
	public void setTotalSpecialCost(String totalSpecialCost) {
		this.totalSpecialCost = totalSpecialCost;
	}
	public String getUnitRetailCost() {
		return unitRetailCost;
	}
	public void setUnitRetailCost(String unitRetailCost) {
		this.unitRetailCost = unitRetailCost;
	}
	public String getTotalRetailCost() {
		return totalRetailCost;
	}
	public void setTotalRetailCost(String totalRetailCost) {
		this.totalRetailCost = totalRetailCost;
	}
	public String getSalesMethodType() {
		return salesMethodType;
	}
	public void setSalesMethodType(String salesMethodType) {
		this.salesMethodType = salesMethodType;
	}
	public String getSalesMethod() {
		return salesMethod;
	}
	public void setSalesMethod(String salesMethod) {
		this.salesMethod = salesMethod;
	}
	public String getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(String orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	
	
	public void Init(){
		if(companyName==null){companyName="";}
		if(shopGroup==null){shopGroup="";}
		if(shopName==null){shopName="";}
		if(orderId==null){orderId="";}
		if(orderDate==null){orderDate="";}
		if(paymentMethod==null){paymentMethod="";}
		if(customerName==null){customerName="";}
		if(acutalQuantity==null){acutalQuantity="";}
		if(unit==null){unit="";}
		if(amount==null){amount="";}
		if(adjustment==null){adjustment="";}
		if(receivables==null){receivables="";}
		if(purchaseOrSalesLedger==null){purchaseOrSalesLedger="";}
		if(salePerson==null){salePerson="";}
		if(month==null){month="";}
		if(correctedName==null){correctedName="";}
		if(brand==null){brand="";}
		if(model==null){model="";}
		if(description==null){description="";}
		if(category==null){category="";}
		if(subCategory==null){subCategory="";}
		if(thirdSubCategory==null){thirdSubCategory="";}
		if(unitDefaultCost==null){unitDefaultCost="";}
		if(totalDefaultCost==null){totalDefaultCost="";}
		if(unitEgateeCost==null){unitEgateeCost="";}
		if(totalEgateeCost==null){totalEgateeCost="";}
		if(unitSpecialCost==null){unitSpecialCost="";}
		if(totalSpecialCost==null){totalSpecialCost="";}
		if(unitRetailCost==null){unitRetailCost="";}
		if(totalRetailCost==null){totalRetailCost="";}
		if(salesMethodType==null){salesMethodType="";}
		if(salesMethod==null){salesMethod="";}
		
	}
	
	
	public String adminExportString() {
		
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		return companyName + "," + shopGroup + "," + shopName
				+ "," + orderId + "," + productId + "," + orderDate + "," + paymentMethod
				+ "," + "\"" + customerName + "\"" + "," + "\"" + acutalQuantity + "\"" + "," + "\"" + unitPrice + "\""
				+ "," + unit + "," + "\"" + amount + "\"" + "," + "\"" + adjustment + "\"" 
				+ "," + "\"" + receivables + "\"" + "," + purchaseOrSalesLedger + "," + salePerson
				+ "," + month + "," + correctedName + "," + brand + "," + model
				+ "," + description + "," + category + "," + subCategory
				+ "," + thirdSubCategory + "," + "\"" + unitDefaultCost + "\""
				+ "," + "\"" + totalDefaultCost + "\"" + "," + "\"" + unitEgateeCost + "\""
				+ "," + "\"" + totalEgateeCost + "\"" + "," + "\"" + unitSpecialCost + "\"" + "," + "\"" + totalSpecialCost + "\""
				+ "," + "\"" + unitRetailCost + "\"" + "," + "\"" + totalRetailCost + "\"" + ","
				+ salesMethodType + "," + salesMethod + ",";
	}
	
	public String storeManageToStringUGAMax() {
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		return companyName + "," + shopGroup + "," + shopName
				+ "," + orderId + "," + productId + "," + orderDate + "," + paymentMethod
				+ "," + "\"" + customerName + "\"" + "," + "\"" + acutalQuantity + "\"" + "," + "\"" + unitPrice + "\""
				+ "," + unit + "," + "\"" + amount + "\"" + "," + "\"" + adjustment + "\"" 
				+ "," + "\"" + receivables + "\"" + "," + purchaseOrSalesLedger + "," + salePerson
				+ "," + month + "," + correctedName + "," + brand + "," + model
				+ "," + description + "," + category + "," + subCategory
				+ "," + thirdSubCategory + "," + "\"" + unitSpecialCost + "\"" 
				+ "," + "\"" + totalSpecialCost + "\"" + "," + salesMethodType + "," + salesMethod + ",";
	}
	
	public String storeManageToStringUGAMin() {
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		return companyName + "," + shopGroup + "," + shopName
				+ "," + orderId + "," + productId + "," + orderDate + "," + paymentMethod
				+ "," + "\"" + customerName + "\"" + "," + "\"" + acutalQuantity + "\"" + "," + "\"" + unitPrice + "\""
				+ "," + unit + "," + "\"" + amount + "\"" + "," + "\"" + adjustment + "\"" 
				+ "," + "\"" + receivables + "\"" + "," + purchaseOrSalesLedger + "," + salePerson
				+ "," + month + "," + correctedName + "," + brand + "," + model
				+ "," + description + "," + category + "," + subCategory
				+ "," + thirdSubCategory  
				+ "," + salesMethodType + "," + salesMethod + ",";
	}
	
	
	public String storeManageToStringGHA() {
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		return companyName + "," + shopGroup + "," + shopName
				+ "," + orderId + "," + productId + "," + orderDate + "," + paymentMethod
				+ "," + "\"" + customerName + "\"" + "," + "\"" + acutalQuantity + "\"" + "," + "\"" + unitPrice +"\"" 
				+  "," + unit + "," + "\"" + amount + "\"" + "," + "\"" + adjustment + "\"" + 
				"," + "\"" + receivables + "\"" + "," + purchaseOrSalesLedger + "," + salePerson
				+ "," + month + "," + correctedName + "," + brand + "," + model
				+ "," + description + "," + category + "," + subCategory
				+ "," + thirdSubCategory + "," + "\"" + unitDefaultCost + "\"" 
				+ "," + "\"" + totalDefaultCost + "\"" + "," + salesMethodType 
				+ "," + salesMethod + ",";
	}
	
	
	public String operatorManageToString() {
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		return companyName + "," + shopGroup + "," + shopName
				+ "," + orderId + "," + productId + "," + orderDate + "," + paymentMethod
				+ "," + "\"" + customerName + "\"" + "," + "\"" + acutalQuantity + "\"" + "," + "\"" + unitPrice + "\""
				+ "," + unit + "," + "\"" + amount + "\"" + "," + "\"" + adjustment + "\"" 
				+ "," + "\"" + receivables + "\"" + "," + purchaseOrSalesLedger + "," + salePerson
				+ "," + month + "," + correctedName + "," + brand + "," + model
				+ "," + description + "," + category + "," + subCategory
				+ "," + thirdSubCategory + "," + "\"" + unitRetailCost + "\"" 
				+ "," + "\"" + totalRetailCost + "\"" + "," + salesMethodType + "," + salesMethod + ",";
	}
	
	@Override
	public String toString() {
		return "ExportSalesReport [companyName=" + companyName + ", shopGroup=" + shopGroup + ", shopName=" + shopName
				+ ", orderId=" + orderId + ", orderDate=" + orderDate + ", paymentMethod=" + paymentMethod
				+ ", customerName=" + customerName + ", acutalQuantity=" + acutalQuantity + ", unitPrice=" + unitPrice
				+ ", unit=" + unit + ", amount=" + amount + ", adjustment=" + adjustment + ", receivables="
				+ receivables + ", purchaseOrSalesLedger=" + purchaseOrSalesLedger + ", salePerson=" + salePerson
				+ ", month=" + month + ", correctedName=" + correctedName + ", brand=" + brand + ", model=" + model
				+ ", description=" + description + ", category=" + category + ", subCategory=" + subCategory
				+ ", thirdSubCategory=" + thirdSubCategory + ", unitDefaultCost=" + unitDefaultCost
				+ ", totalDefaultCost=" + totalDefaultCost + ", unitEgateeCost=" + unitEgateeCost + ", totalEgateeCost="
				+ totalEgateeCost + ", unitSpecialCost=" + unitSpecialCost + ", totalSpecialCost=" + totalSpecialCost
				+ ", unitRetailCost=" + unitRetailCost + ", totalRetailCost=" + totalRetailCost + ", salesMethodType="
				+ salesMethodType + ", salesMethod=" + salesMethod + "]";
	}
	
	
	
	public String formatTosepara(String data) {
		if(data == null || "".equals(data)){
			data = "0";
		}
        DecimalFormat df = new DecimalFormat(",###,##0.00"); 
        return df.format(new BigDecimal(data));
    }
	
	public void parseMoneyAndQuantity(){
		acutalQuantity = formatTosepara(acutalQuantity);
		unitPrice = formatTosepara(unitPrice);
		amount = formatTosepara(amount);
		adjustment = formatTosepara(adjustment);
		receivables = formatTosepara(receivables);
		unitDefaultCost = formatTosepara(unitDefaultCost);
		totalDefaultCost = formatTosepara(totalDefaultCost);
		unitEgateeCost = formatTosepara(unitEgateeCost);
		totalEgateeCost = formatTosepara(totalEgateeCost);
		unitSpecialCost = formatTosepara(unitSpecialCost);
		totalSpecialCost = formatTosepara(totalSpecialCost);
		unitRetailCost = formatTosepara(unitRetailCost);
		totalRetailCost = formatTosepara(totalRetailCost);
		
	}
	
}
