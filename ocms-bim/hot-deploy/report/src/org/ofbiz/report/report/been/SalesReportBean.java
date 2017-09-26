package org.ofbiz.report.report.been;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SalesReportBean {
	private String orderDate;
	private String Category;
	private String productId;
	private String description;
	
	
	/*private BigDecimal quantity;
	private BigDecimal unitPrice;
	private BigDecimal unitCost;
	private BigDecimal currentPrice;
	private BigDecimal subTotal;
	private BigDecimal defaultCostPrice;
	private BigDecimal egateeCostPrice;
	private BigDecimal specialCostPrice;
	private BigDecimal retailCostPrice;
	private BigDecimal totalDefaultCostPrice;
	private BigDecimal totalEgateeCostPrice;
	private BigDecimal totalSpecialCostPrice;
	private BigDecimal totalRetailCostPrice;
	private BigDecimal orgCost;
	private BigDecimal adjustment;
	private BigDecimal recievables;*/
	//BigDecimal--------------------
	private String quantity;
	private String unitPrice;
	private String unitCost;
	private String currentPrice;
	private String subTotal;
	private String defaultCostPrice;
	private String egateeCostPrice;
	private String specialCostPrice;
	private String retailCostPrice;
	private String totalDefaultCostPrice;
	private String totalEgateeCostPrice;
	private String totalSpecialCostPrice;
	private String totalRetailCostPrice;
	private String orgCost;
	private String adjustment;
	private String recievables;
	//BigDecimal----------------
	private String payment;
	private String salesPerson;
	private String salesChannel;
	private String storeId;
	

	
	
	public String getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	public String getCategory() {
		return Category;
	}


	public void setCategory(String category) {
		Category = category;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getQuantity() {
		return quantity;
	}


	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	public String getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getUnitCost() {
		return unitCost;
	}


	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
	}


	public String getCurrentPrice() {
		return currentPrice;
	}


	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}


	public String getSubTotal() {
		return subTotal;
	}


	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}


	public String getDefaultCostPrice() {
		return defaultCostPrice;
	}


	public void setDefaultCostPrice(String defaultCostPrice) {
		this.defaultCostPrice = defaultCostPrice;
	}


	public String getEgateeCostPrice() {
		return egateeCostPrice;
	}


	public void setEgateeCostPrice(String egateeCostPrice) {
		this.egateeCostPrice = egateeCostPrice;
	}


	public String getSpecialCostPrice() {
		return specialCostPrice;
	}


	public void setSpecialCostPrice(String specialCostPrice) {
		this.specialCostPrice = specialCostPrice;
	}


	public String getRetailCostPrice() {
		return retailCostPrice;
	}


	public void setRetailCostPrice(String retailCostPrice) {
		this.retailCostPrice = retailCostPrice;
	}


	public String getTotalDefaultCostPrice() {
		return totalDefaultCostPrice;
	}


	public void setTotalDefaultCostPrice(String totalDefaultCostPrice) {
		this.totalDefaultCostPrice = totalDefaultCostPrice;
	}


	public String getTotalEgateeCostPrice() {
		return totalEgateeCostPrice;
	}


	public void setTotalEgateeCostPrice(String totalEgateeCostPrice) {
		this.totalEgateeCostPrice = totalEgateeCostPrice;
	}


	public String getTotalSpecialCostPrice() {
		return totalSpecialCostPrice;
	}


	public void setTotalSpecialCostPrice(String totalSpecialCostPrice) {
		this.totalSpecialCostPrice = totalSpecialCostPrice;
	}


	public String getTotalRetailCostPrice() {
		return totalRetailCostPrice;
	}


	public void setTotalRetailCostPrice(String totalRetailCostPrice) {
		this.totalRetailCostPrice = totalRetailCostPrice;
	}


	public String getOrgCost() {
		return orgCost;
	}


	public void setOrgCost(String orgCost) {
		this.orgCost = orgCost;
	}


	public String getAdjustment() {
		return adjustment;
	}


	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}


	public String getPayment() {
		return payment;
	}


	public void setPayment(String payment) {
		this.payment = payment;
	}


	public String getRecievables() {
		return recievables;
	}


	public void setRecievables(String recievables) {
		this.recievables = recievables;
	}


	public String getSalesPerson() {
		return salesPerson;
	}


	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}


	public String getSalesChannel() {
		return salesChannel;
	}


	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}


	public String getStoreId() {
		return storeId;
	}


	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	public void init(){
		if(orderDate==null){orderDate="";}
		if(Category==null){Category="";}
		if(productId==null){productId="";}
		if(description==null){description="";}
		if(quantity==null){quantity="";}
		if(unitPrice==null){unitPrice="";}
//		if(unitCost==null){unitCost=new BigDecimal(0);}
		if(currentPrice==null){currentPrice="";}
		if(subTotal==null){subTotal="";}
		if(defaultCostPrice==null){defaultCostPrice="";}
		if(egateeCostPrice==null){egateeCostPrice="";}
		if(specialCostPrice==null){specialCostPrice="";}
		if(retailCostPrice==null){retailCostPrice="";}
		
		if(totalDefaultCostPrice==null){totalDefaultCostPrice="";}
		if(totalEgateeCostPrice==null){totalEgateeCostPrice="";}
		if(totalSpecialCostPrice==null){totalSpecialCostPrice="";}
		if(totalRetailCostPrice==null){totalRetailCostPrice="";}
		
//		if(orgCost==null){orgCost=new BigDecimal(0);}
		if(adjustment==null){adjustment="";}
		if(payment==null){payment="";}
		if(recievables==null){recievables="";}
		if(salesPerson==null){salesPerson="";}
		if(salesChannel==null){salesChannel="";}
		if(storeId==null){storeId="";}
		
	}
	
	//隐藏运营商的数据
	public void operatorInit(){
		if(orderDate==null){orderDate="";}
		if(Category==null){Category="";}
		if(productId==null){productId="";}
		if(description==null){description="";}
		if(quantity==null){quantity="";}
		if(unitPrice==null){unitPrice="";}
//		if(unitCost==null){unitCost=new BigDecimal(0);}
		if(currentPrice==null){currentPrice="";}
		if(subTotal==null){subTotal="";}
		if(defaultCostPrice!=null){defaultCostPrice=null;}
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice==null){specialCostPrice="";}
		if(retailCostPrice!=null){retailCostPrice=null;}
//		if(orgCost==null){orgCost=new BigDecimal(0);}
		
		if(totalDefaultCostPrice!=null){totalDefaultCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice==null){totalSpecialCostPrice="";}
		if(totalRetailCostPrice!=null){totalRetailCostPrice=null;}
		
		
		if(adjustment==null){adjustment="";}
		if(payment==null){payment="";}
		if(recievables==null){recievables="";}
		if(salesPerson==null){salesPerson="";}
		if(salesChannel==null){salesChannel="";}
		if(storeId==null){storeId="";}
	}
	
	
	public void hideNeedlessDataUGA(){
		if(defaultCostPrice!=null){defaultCostPrice=null;}
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice!=null){specialCostPrice=null;}
		if(totalDefaultCostPrice!=null){totalDefaultCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice!=null){totalSpecialCostPrice=null;}
	}
	
	public void hideNeedlessDataGHA(){
		if(defaultCostPrice!=null){defaultCostPrice=null;}
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice!=null){specialCostPrice=null;}
		if(totalDefaultCostPrice!=null){totalDefaultCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice!=null){totalSpecialCostPrice=null;}
	}
	
	public void hideNeedlessDataGHAAdmin(){
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice!=null){specialCostPrice=null;}
		if(retailCostPrice!=null){retailCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice!=null){totalSpecialCostPrice=null;}
		if(totalRetailCostPrice!=null){totalRetailCostPrice=null;}
	}
	public void hideNeedlessDataGHAManage(){
		if(defaultCostPrice!=null){defaultCostPrice=null;}
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice!=null){specialCostPrice=null;}
		if(retailCostPrice!=null){retailCostPrice=null;}
		if(totalDefaultCostPrice!=null){totalDefaultCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice!=null){totalSpecialCostPrice=null;}
		if(totalRetailCostPrice!=null){totalRetailCostPrice=null;}
	}
	public void hideNeedlessDataUGAManage(){
		if(defaultCostPrice!=null){defaultCostPrice=null;}
		if(egateeCostPrice!=null){egateeCostPrice=null;}
		if(specialCostPrice!=null){specialCostPrice=null;}
		if(retailCostPrice!=null){retailCostPrice=null;}
		if(totalDefaultCostPrice!=null){totalDefaultCostPrice=null;}
		if(totalEgateeCostPrice!=null){totalEgateeCostPrice=null;}
		if(totalSpecialCostPrice!=null){totalSpecialCostPrice=null;}
		if(totalRetailCostPrice!=null){totalRetailCostPrice=null;}
	}
	
	
	public String formatTosepara(String data) {
		if(data == null || "".equals(data)){
			data = "0";
		}
        DecimalFormat df = new DecimalFormat(",###,##0.00"); 
        return df.format(new BigDecimal(data));
    }
	
	public void parseMoneyAndQuantity(){
		quantity = formatTosepara(quantity);
		unitPrice = formatTosepara(unitPrice);
		unitCost = formatTosepara(unitCost);
		currentPrice = formatTosepara(currentPrice);
		subTotal = formatTosepara(subTotal);
		defaultCostPrice = formatTosepara(defaultCostPrice);
		egateeCostPrice = formatTosepara(egateeCostPrice);
		specialCostPrice = formatTosepara(specialCostPrice);
		retailCostPrice = formatTosepara(retailCostPrice);
		totalDefaultCostPrice = formatTosepara(totalDefaultCostPrice);
		totalEgateeCostPrice = formatTosepara(totalEgateeCostPrice);
		totalSpecialCostPrice = formatTosepara(totalSpecialCostPrice);
		totalRetailCostPrice = formatTosepara(totalRetailCostPrice);
		orgCost = formatTosepara(orgCost);
		adjustment = formatTosepara(adjustment);
		recievables = formatTosepara(recievables);
	}
	
	
	
}
