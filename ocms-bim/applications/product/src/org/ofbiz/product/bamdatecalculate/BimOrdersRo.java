package org.ofbiz.product.bamdatecalculate;

import java.sql.Date;

public class BimOrdersRo {
	private String country;
	private String date;
	private String total_valid_order;
	private String total_goods_count;
	private String total_goods_sum;
	private Date created_at;
	private Date updated_at;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTotal_valid_order() {
		return total_valid_order;
	}
	public void setTotal_valid_order(String total_valid_order) {
		this.total_valid_order = total_valid_order;
	}
	public String getTotal_goods_count() {
		return total_goods_count;
	}
	public void setTotal_goods_count(String total_goods_count) {
		this.total_goods_count = total_goods_count;
	}
	public String getTotal_goods_sum() {
		return total_goods_sum;
	}
	public void setTotal_goods_sum(String total_goods_sum) {
		this.total_goods_sum = total_goods_sum;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	
	public String toinsertsql()
	{
		return  " (\""+country+"\",\""+date+"\",\""+total_valid_order+"\",\""+total_goods_count+"\",\""+total_goods_sum+"\",\""+created_at+"\",\""+updated_at+"\")";
	}

}
