package com.opensourcestrategies.crmsfa.content;

import java.util.List;

import org.opentaps.base.entities.OrderHeader;
import org.opentaps.domain.order.OrderItem;

public class OrderInfoBean {
	private List<OrderItem> itemlist;
	private OrderHeader order;

	public List<OrderItem> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<OrderItem> itemlist) {
		this.itemlist = itemlist;
	}

	public OrderHeader getOrder() {
		return order;
	}
	public void setOrder(OrderHeader order) {
		this.order = order;
	}
}
