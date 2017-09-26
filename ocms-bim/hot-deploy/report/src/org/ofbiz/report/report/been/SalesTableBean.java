package org.ofbiz.report.report.been;

import java.math.BigDecimal;

public class SalesTableBean {
	//-- '下单时间，商品名称，商品描述,初始数量，单价?，实际成本?，总价，实际收款?，店铺租，店铺名称'
	private String date;
	private String productId;
	private String describe;
	private BigDecimal quantity;
	private BigDecimal unitPrice;
	private BigDecimal actualCost;
	private BigDecimal totalPrice;
	private BigDecimal actualCollection;
	private String groupId;
	private String storeId;
	
	public SalesTableBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SalesTableBean(String date, String productId, String describe, BigDecimal quantity, BigDecimal unitPrice,
			BigDecimal actualCost, BigDecimal totalPrice, BigDecimal actualCollection, String groupId, String storeId) {
		super();
		this.date = date;
		this.productId = productId;
		this.describe = describe;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.actualCost = actualCost;
		this.totalPrice = totalPrice;
		this.actualCollection = actualCollection;
		this.groupId = groupId;
		this.storeId = storeId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getActualCost() {
		return actualCost;
	}
	public void setActualCost(BigDecimal actualCost) {
		this.actualCost = actualCost;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public BigDecimal getActualCollection() {
		return actualCollection;
	}
	public void setActualCollection(BigDecimal actualCollection) {
		this.actualCollection = actualCollection;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
