package org.ofbiz.report.report.been;
/**
 * 销售表报界面ProductBeen
 * @author zhenk
 *
 */
public class ProductBeen {
	private String productId;
	private String brandName;
	private String internalName;
	private String productInnerName;
	
	public ProductBeen() {
		super();
	}
	
	public ProductBeen(String productId, String productInnerName) {
		super();
		this.productId = productId;
		this.productInnerName = productInnerName;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
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
	public String getProductInnerName() {
		return productInnerName;
	}
	public void setProductInnerName(String productInnerName) {
		this.productInnerName = productInnerName;
	}
	
	
	
}
