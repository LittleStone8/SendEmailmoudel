package org.ofbiz.product.product;

public class GoodIdentificationModifyLogBean {
	
	private String productid;
	private String beforeean;
	private String afterean;
	private String operator;
	private String modifyTime;
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getBeforeean() {
		return beforeean;
	}
	public void setBeforeean(String beforeean) {
		this.beforeean = beforeean;
	}
	public String getAfterean() {
		return afterean;
	}
	public void setAfterean(String afterean) {
		this.afterean = afterean;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String toerportstring() {
		return productid+"\t,"+modifyTime+"\t,"+beforeean+"\t,"+afterean+"\t,"+operator;
	} 

	
	
}
