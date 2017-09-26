package org.ofbiz.regularEmail;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.service.DispatchContext;
 
public class UGSendmai {
     
	
	
	public static Map<String, Object> UG_RegularSendmail(DispatchContext dctx, Map<String, ? extends Object> context) {

		Map<String, Object> retmap = new HashMap<String, Object>();

		String county = (String)BaseReportEvent.getSystemSettings().get("countryId");
		if (!"UGA".equals(county)) {
			return retmap;
		}
		
		
		
		Date dNow = new Date();  
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);  
		dBefore = calendar.getTime();  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		String defaultStartDate = sdf.format(dBefore);    
		defaultStartDate = defaultStartDate+" 00:00:00";
		String defaultEndDate = defaultStartDate.substring(0,10)+" 23:59:59";

		// String sql = " select ret.PRODUCT_STORE_ID as productstoreid ,(select
		// PRODUCT_STORE.STORE_NAME from PRODUCT_STORE where
		// PRODUCT_STORE.PRODUCT_STORE_ID=ret.PRODUCT_STORE_ID) as
		// shop,sum(ret.unp) as summary,count(ret.ORDER_ID) as qty from "
		// + " ( "
		// + " select oh.*,sum(oi.UNIT_PRICE) as unp from ORDER_HEADER as oh
		// join ORDER_ITEM as oi on oh.ORDER_ID=oi.ORDER_ID where
		// oh.CREATED_STAMP >=\'"+defaultStartDate+"\' and
		// oh.CREATED_STAMP<=\'"+defaultEndDate+"\' and
		// (oh.STATUS_ID='ORDER_COMPLETED' or oh.STATUS_ID='ORDER_PACKED' or
		// oh.STATUS_ID='ORDER_PAYMENT' or oh.STATUS_ID='ORDER_SHIPPED' or
		// oh.STATUS_ID='ORDER_COMPLETED_W_P' ) group by oi.ORDER_ID "
		// + " ) as ret where ret.PRODUCT_STORE_ID not in (select
		// PRODUCT_STORE_GROUP_MEMBER.PRODUCT_STORE_ID from
		// PRODUCT_STORE_GROUP_MEMBER where
		// PRODUCT_STORE_GROUP_MEMBER.PRODUCT_STORE_GROUP_ID='TEST') group by
		// ret.PRODUCT_STORE_ID order by qty asc";

		String sql = "select rrr.PRODUCT_STORE_ID as productstoreid,rrr.STORE_NAME as shop,ret.unp as summary,ret.www as qty from   "
				+

				" (select ps.PRODUCT_STORE_ID,ps.STORE_NAME from PRODUCT_STORE  as ps  where ps.PRODUCT_STORE_ID not in  (select PRODUCT_STORE_GROUP_MEMBER.PRODUCT_STORE_ID from PRODUCT_STORE_GROUP_MEMBER where PRODUCT_STORE_GROUP_MEMBER.PRODUCT_STORE_GROUP_ID='TEST')  ) as rrr left join   "
				+

				" (   "
				+ " 	select oh.PRODUCT_STORE_ID,sum(oh.GRAND_TOTAL) as unp,count(oh.GRAND_TOTAL)  as www from ORDER_HEADER as oh where 1=1    and  oh.CREATED_STAMP >=\'"
				+ defaultStartDate + "\'  and  oh.CREATED_STAMP<=\'" + defaultEndDate
				+ "\' and (oh.STATUS_ID='ORDER_COMPLETED' or  oh.STATUS_ID='ORDER_PACKED' or oh.STATUS_ID='ORDER_PAYMENT' or oh.STATUS_ID='ORDER_SHIPPED' or oh.STATUS_ID='ORDER_COMPLETED_W_P' )  GROUP by oh.PRODUCT_STORE_ID "
				+

				"	) as ret on ret.PRODUCT_STORE_ID=rrr.PRODUCT_STORE_ID order by www asc ";

		Delegator delegator = dctx.getDelegator();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		double totalsummary = 0;
		long totalqty = 0;
		List<Emaildatabean> retlist = new ArrayList<Emaildatabean>();
		try {
			ResultSet rs = processor.executeQuery(sql);
			while (rs.next()) {
				String productstroeid = rs.getString("productstoreid");
				String shopname = rs.getString("shop");
				String summary = rs.getString("summary");
				if (summary == null || "".equals(summary))
					summary = "0";
				String qty = rs.getString("qty");
				if (qty == null || "".equals(qty))
					qty = "0";
				totalsummary += Double.valueOf(summary);
				totalqty += Long.valueOf(qty);

				retlist.add(new Emaildatabean(productstroeid, shopname, summary, qty));
			}
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sengemail(retlist,totalsummary,totalqty);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return retmap;
	}
	
	
	
	
	
	private static void sengemail(List<Emaildatabean> retlist, double totalsummary, long totalqty) throws MessagingException{
		
		Map<String, Object> ret = BaseReportEvent.getSystemSettings();
		String SystemSettingsCountries = (String)ret.get("countryId");
		String SystemSettingsSendemailUserName = (String)ret.get("SystemSettingsSendemailUserName");
		String SystemSettingsSendemailAuthorizationCode = (String)ret.get("SystemSettingsSendemailAuthorizationCode");
		String SystemSettingsSendemailRecipient = (String)ret.get("SystemSettingsSendemailRecipient");
		
		
		Properties prop=new Properties();
		prop.put("mail.host","smtp.qiye.163.com" );
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		//使用java发送邮件5步骤
		//1.创建sesssion
		Session session=Session.getInstance(prop);
		//开启session的调试模式，可以查看当前邮件发送状态
		session.setDebug(true);


		//2.通过session获取Transport对象（发送邮件的核心API）
		Transport ts=session.getTransport();
		//3.通过邮件用户名密码链接
		ts.connect(SystemSettingsSendemailUserName, SystemSettingsSendemailAuthorizationCode);


		//4.创建邮件

		Message msg=createSimpleMail(session,SystemSettingsCountries,SystemSettingsSendemailUserName,SystemSettingsSendemailAuthorizationCode,SystemSettingsSendemailRecipient,retlist,totalsummary,totalqty);


		//5.发送电子邮件

		ts.sendMessage(msg, msg.getAllRecipients());
		
		ts.close();
		
		
		
	
	}
	
	public static MimeMessage createSimpleMail(Session session,String SystemSettingsCountries,String SystemSettingsSendemailUserName,String SystemSettingsSendemailAuthorizationCode,String SystemSettingsSendemailRecipient,List<Emaildatabean> retlist, double totalsummary, long totalqty) throws AddressException,MessagingException{
		//创建邮件对象
		MimeMessage mm=new MimeMessage(session);
		//设置发件人
		mm.setFrom(new InternetAddress(SystemSettingsSendemailUserName));
		//设置收件人
		
		String[] Recipient = SystemSettingsSendemailRecipient.split(";");
		Address[] addresses = new Address[Recipient.length];
		for(int i=0;i<Recipient.length;i++)
		{
			addresses[i]= new InternetAddress(Recipient[i]);
		}
		
		mm.setRecipients(Message.RecipientType.TO, addresses);
		
		
		
	//	mm.setRecipient(Message.RecipientType.TO, new InternetAddress("zhenli.yuan@yiwill.com"));
		
		
		
		
		Date dNow = new Date();  
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);  
		dBefore = calendar.getTime();  
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy"); 
		String defaultStartDate = sdf.format(dBefore);    
		
		String subject="";
		if(SystemSettingsCountries.equals("UGA"))
			subject=defaultStartDate+" Uganda Daily Sales Report In BIM (bim.yiwill.com)";
		else 
			subject=defaultStartDate+" Ghana Daily Sales Report In BIM (gh.bim.yiwill.com)";

		mm.setSubject(subject);
        StringBuffer theMessage = new StringBuffer();
     //   theMessage.append("<h2><font color=red>"+subject+"</font></h2>");
        theMessage.append("<h2>"+subject+"</h2>");
        theMessage.append("<hr>");
        theMessage.append("<table border='1' cellspacing='0'><thead><tr style='height:30px;line-height:30px;'><th style='text-align:center;padding:0 10px;'>Shop</th><th style='text-align:center;padding:0 10px;'>Payment Summary</th><th style='text-align:center;padding:0 10px;'>Orders Quantity</th></tr></thead><tbody>");
		
		for (int i = 0; i < retlist.size(); i++) {
			Emaildatabean temp = retlist.get(i);
			
			Integer qtrw = Integer.valueOf(temp.getQty());
			
			if(qtrw < 10){
				theMessage.append("<tr style='color:#f00;text-align:center;height:30px;line-height:30px;'><td>" + temp.getProductname() + "</td><td>" + temp.getSummary() + "</td><td>"+ temp.getQty() + "</td>");
			}else{
				theMessage.append("<tr style='height:30px;text-align:center;line-height:30px;'><td>" + temp.getProductname() + "</td><td>" + temp.getSummary() + "</td><td>"+ temp.getQty() + "</td>");
			}
			
		}
        
		theMessage.append("<tr style='font-weight:bold;text-align:center;height:30px;'><td>Total</td><td>" + totalsummary + "</td><td>"+ totalqty + "</td>");
        
        theMessage.append("</tbody></table>");  
        
        theMessage.append("<div style='margin-top:10px;'><span style='color:#FF0000;font-weight:bold;'>*Hot part</span> means order quantity less than 10</div>");  
        
		if(SystemSettingsCountries.equals("UGA"))
			theMessage.append("<div style='margin-top:10px;font-size:16px;'>* More information you can get from <a href='bim.yiwill.com/report/control/BANSalesReport' style='color:#00f;font-weight:bold;'>BIM</a></div> ");  
		else 
			theMessage.append("<div style='margin-top:10px;font-size:16px;'>* More information you can get from <a href='gh.bim.yiwill.com/report/control/BANSalesReport' style='color:#00f;font-weight:bold;'>BIM</a></div> ");  
        
        
        
        
		mm.setContent(theMessage.toString(), "text/html;charset=gbk");

		return mm;

		}
	
	
	
	
	
	
	
	
	
	
	
	
}