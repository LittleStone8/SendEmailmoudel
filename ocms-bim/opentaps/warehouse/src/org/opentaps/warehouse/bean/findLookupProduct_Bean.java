package org.opentaps.warehouse.bean;

public class findLookupProduct_Bean {
	
//	[GenericEntity:ProductAndGoodIdentification][brandName,null()][internalName,1000000000(java.lang.String)][productId,100(java.lang.String)][productTypeId,FINISHED_GOOD(java.lang.String)]
//	[GenericEntity:ProductAndGoodIdentification][brandName,null()][internalName,自动覆盖(java.lang.String)][productId,10081(java.lang.String)][productTypeId,FINISHED_GOOD(java.lang.String)]
//	[GenericEntity:ProductAndGoodIdentification][brandName,null()][internalName,10082(java.lang.String)][productId,10082(java.lang.String)][productTypeId,FINISHED_GOOD(java.lang.String)]
//	[GenericEntity:ProductAndGoodIdentification][brandName,null()][internalName,10083(java.lang.String)][productId,10083(java.lang.String)][productTypeId,FINISHED_GOOD(java.lang.String)]
//	[GenericEntity:ProductAndGoodIdentification][brandName,null()][internalName,null()][productId,A1(java.lang.String)][productTypeId,GOOD(java.lang.String)]
	
	//产品id   	PRODUCT----PRODUCT_ID
	private String productId;
	//品牌名称	 	PRODUCT----BRAND_NAME
	private String brandName;
	//内部名称     	PRODUCT----INTERNAL_NAME
	private String internalName;
	//产品类型    	PRODUCT----PRODUCT_TYPE_ID
	private String productTypeId;
	
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
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	@Override
	public String toString() {
		return "productInfo [productId=" + productId + ", brandName=" + brandName + ", internalName=" + internalName
				+ ", productTypeId=" + productTypeId + "]";
	}
	
	
	
}
