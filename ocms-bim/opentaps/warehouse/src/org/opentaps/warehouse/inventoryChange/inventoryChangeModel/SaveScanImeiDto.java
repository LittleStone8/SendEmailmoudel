package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

import java.sql.Timestamp;

public class SaveScanImeiDto {
	private String transhipmentShippingBillId;
	private String productId;
	private String imei;
	
	public String getTranshipmentShippingBillId() {
		return transhipmentShippingBillId;
	}
	public void setTranshipmentShippingBillId(String transhipmentShippingBillId) {
		this.transhipmentShippingBillId = transhipmentShippingBillId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
	public String toinsertsql(String operator)
	{
		Timestamp nowtime = new Timestamp(System.currentTimeMillis());
		
		return  " (\""+transhipmentShippingBillId+"\",\"Y\",\""+productId+"\",\""+imei+"\",\""+nowtime+"\",\""+nowtime+"\",\""+operator+"\",\""+operator+"\")";
		
		
	}
}
