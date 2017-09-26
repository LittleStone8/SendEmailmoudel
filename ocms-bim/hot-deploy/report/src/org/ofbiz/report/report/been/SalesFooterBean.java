package org.ofbiz.report.report.been;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SalesFooterBean {
	private String date;
	private String todayCollection;
	private String todayCash;
	private String todayCheque;
	private String todayPos;
	private String todayMoneyBack;
	private String todayTransfer;
	private String todayBrakCard;
	private String todayCredit;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTodayCollection() {
		return todayCollection;
	}
	public void setTodayCollection(String todayCollection) {
		this.todayCollection = todayCollection;
	}
	public String getTodayCash() {
		return todayCash;
	}
	public void setTodayCash(String todayCash) {
		this.todayCash = todayCash;
	}
	public String getTodayCheque() {
		return todayCheque;
	}
	public void setTodayCheque(String todayCheque) {
		this.todayCheque = todayCheque;
	}
	public String getTodayPos() {
		return todayPos;
	}
	public void setTodayPos(String todayPos) {
		this.todayPos = todayPos;
	}
	public String getTodayMoneyBack() {
		return todayMoneyBack;
	}
	public void setTodayMoneyBack(String todayMoneyBack) {
		this.todayMoneyBack = todayMoneyBack;
	}
	public String getTodayTransfer() {
		return todayTransfer;
	}
	public void setTodayTransfer(String todayTransfer) {
		this.todayTransfer = todayTransfer;
	}
	public String getTodayBrakCard() {
		return todayBrakCard;
	}
	public void setTodayBrakCard(String todayBrakCard) {
		this.todayBrakCard = todayBrakCard;
	}
	public String getTodayCredit() {
		return todayCredit;
	}
	public void setTodayCredit(String todayCredit) {
		this.todayCredit = todayCredit;
	}

	
	
	public void init(){
		if(date==null){date="";}
		if(todayCollection==null){todayCollection=null;}
		if(todayCash==null){todayCash=null;}
		if(todayCheque==null){todayCheque=null;}
		if(todayPos==null){todayPos=null;}
		if(todayMoneyBack==null){todayMoneyBack=null;}
		if(todayTransfer==null){todayTransfer=null;}
		if(todayBrakCard==null){todayBrakCard=null;}
		if(todayCredit==null){todayCredit=null;}
		
	}

	public String formatTosepara(String data) {
		if(data == null || "".equals(data)){
			data = "0";
		}
        DecimalFormat df = new DecimalFormat(",###,##0.00"); 
        return df.format(new BigDecimal(data));
    }
	
	public void parseMoneyAndQuantity(){
		todayCollection = formatTosepara(todayCollection);
		todayCash = formatTosepara(todayCash);
		todayCheque = formatTosepara(todayCheque);
		todayPos = formatTosepara(todayPos);
		todayMoneyBack = formatTosepara(todayMoneyBack);
		todayTransfer = formatTosepara(todayTransfer);
		todayBrakCard = formatTosepara(todayBrakCard);
		todayCredit = formatTosepara(todayCredit);
	}
	public String paymentSummaryToString() {		
		return date + "," + "\"" + todayCollection + "\"" + "," + "\"" + todayCash + "\""
				+ "," + "\"" + todayCheque + "\"" + "," + "\"" + todayBrakCard + "\"" 
				+ "," + "\"" + todayMoneyBack + "\"" + "," + "\"" + todayCredit + "\""
				+ "," + "\"" + todayTransfer + "\"" + ",";
	}
	
	
}
