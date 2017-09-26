package org.ofbiz.product.feature.newfunction.bean;

import java.sql.Timestamp;
//类目页面的pageBeen
public class ClassificationPageBean {
	private String categoryID;
	private String categoryName;
	private Integer sequenceNum;
	private String parentCategoryID;
	private Timestamp createTime;
		
	public ClassificationPageBean(String categoryID, String categoryName, Integer sequenceNum, String parentCategoryID,
			Timestamp createTime) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.sequenceNum = sequenceNum;
		this.parentCategoryID = parentCategoryID;
		this.createTime = createTime;
	}
	
	public ClassificationPageBean(String categoryID, String categoryName) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}

	public ClassificationPageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getSequenceNum() {
		return sequenceNum;
	}
	public void setSequenceNum(Integer sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
	public String getParentCategoryID() {
		return parentCategoryID;
	}
	public void setParentCategoryID(String parentCategoryID) {
		this.parentCategoryID = parentCategoryID;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ClassificationPageBean [categoryID=" + categoryID + ", categoryName=" + categoryName + ", sequenceNum="
				+ sequenceNum + ", parentCategoryID=" + parentCategoryID + ", createTime=" + createTime + "]";
	}
	
	
}
