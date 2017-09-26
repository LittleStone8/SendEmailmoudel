package org.ofbiz.report.report.helper.stockoutreport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockOutReportInfo {
	String productId;//产品标识
	String description;//产品描述
	String transferqty;//产品名称
	String tcdate;//
	String fromfac;//
	String tofac;//
	String INTERNAL_NAME;
	String BRAND_NAME;
	String outstocktime;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransferqty() {
		return transferqty;
	}
	public void setTransferqty(String transferqty) {
		this.transferqty = transferqty;
	}
	
	public String getTcdate() {
		return tcdate;
	}
	
	
	public String getOutstocktime() {
		return outstocktime;
	}
	public void setOutstocktime(String outstocktime) {
		
		if(outstocktime==null ||"".equals(outstocktime))
		{
			this.outstocktime="-";
			return ;
		}
		
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date ww = format1.parse(outstocktime);
			this.outstocktime = formatter.format(ww);
			
		} catch (ParseException e) {
			this.outstocktime = outstocktime.substring(0, 11);
		}
	}
	public void setTcdate(String tcdate) {
		
		if(tcdate==null ||"".equals(tcdate) || "9999-99-99 99:99:99".equals(tcdate))
		{
			this.tcdate="-";
			return ;
		}
		
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date ww = format1.parse(tcdate);
			this.tcdate = formatter.format(ww);
			
		} catch (ParseException e) {
			this.tcdate = tcdate.substring(0, 11);
		}
	}

	public String getFromfac() {
		return fromfac;
	}
	public void setFromfac(String fromfac) {
		this.fromfac = fromfac;
	}
	public String getTofac() {
		return tofac;
	}
	public void setTofac(String tofac) {
		this.tofac = tofac;
	}
	public String getINTERNAL_NAME() {
		return INTERNAL_NAME;
	}
	public void setINTERNAL_NAME(String iNTERNAL_NAME) {
		INTERNAL_NAME = iNTERNAL_NAME;
	}
	public String getBRAND_NAME() {
		return BRAND_NAME;
	}
	public void setBRAND_NAME(String bRAND_NAME) {
		BRAND_NAME = bRAND_NAME;
	}
	public String toerportstring() {
		
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
		
		return productId+","+BRAND_NAME+"|"+INTERNAL_NAME+"|"+description+","+transferqty+","+outstocktime+","+tcdate+","+fromfac+","+tofac;
	
	}
	
	
}
