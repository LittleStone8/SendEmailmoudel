package org.ofbiz.product.enums;

public enum UsageTypeEnum {
	
		OrderCompletedWithoutPayment("Complete without Payment", "ORDER_COMPLETED_W_P"),OrderCompleted("Order Completed", "ORDER_COMPLETED"),OrderCanceled("Order Canceled", "ORDER_CANCELED"),OrderPacked("Order Packed", "ORDER_PACKED"),OrderPayed("Order Payed", "ORDER_PAYED"),OrderCreate("OrderCreate", "ORDER_CREATE"),
		TransferPicking("Transfer Picking", "TRANSFER_PICKING"),TransferCanceled("Transfer Canceled", "TRANSFER_CANCELED"),TransferCompleted("Transfer Completed", "TRANSFER_COMPLETED"),TransferRequest("Transfer Request", "TRANSFER_REQUEST"),Adjustment("Adjustment", "ADJUSTMENT"),InventoryReceive("Receive Item", "INVENTORY_RECEIVE");

	    private String name;
	    private String index;


	    private UsageTypeEnum(String name, String index) {
	      this.name = name;
	      this.index = index;
	    }


	    public static String getName(String index) {
	      for (UsageTypeEnum c : UsageTypeEnum.values()) {
	        if (c.getIndex().equals(index)) {
	          return c.name;
	        }
	      }
	      return null;
	    }


	    public String getName() {
	      return name;
	    }

	    public void setName(String name) {
	      this.name = name;
	    }

	    public String getIndex() {
	      return index;
	    }

	    public void setIndex(String index) {
	      this.index = index;
	    }

}
