package org.ofbiz.product.bamdatecalculate;

import java.sql.Date;

public class BimOrdersShopRo {

	
	private String country;
	private String date;
	private String shop_id;
	private String shop_name;
	private String valid_order;
	private String valid_order_percent;
	private String goods_count;
	private String goods_count_percent;
	private String goods_sum;
	private String goods_sum_percent;
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
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getValid_order() {
		return valid_order;
	}
	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}
	public String getValid_order_percent() {
		return valid_order_percent;
	}
	public void setValid_order_percent(String valid_order_percent) {
		this.valid_order_percent = valid_order_percent;
	}
	public String getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(String goods_count) {
		this.goods_count = goods_count;
	}
	public String getGoods_count_percent() {
		return goods_count_percent;
	}
	public void setGoods_count_percent(String goods_count_percent) {
		this.goods_count_percent = goods_count_percent;
	}
	public String getGoods_sum() {
		return goods_sum;
	}
	public void setGoods_sum(String goods_sum) {
		this.goods_sum = goods_sum;
	}
	public String getGoods_sum_percent() {
		return goods_sum_percent;
	}
	public void setGoods_sum_percent(String goods_sum_percent) {
		this.goods_sum_percent = goods_sum_percent;
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
		return  " (\""+country+"\",\""+date+"\",\""+shop_id+"\",\""+shop_name+"\",\""+valid_order+"\",\""+valid_order_percent+"\",\""+goods_count+"\",\""+goods_count_percent+"\",\""+goods_sum+"\",\""+goods_sum_percent+"\",\""+created_at+"\",\""+updated_at+"\")";
	}
	
}
