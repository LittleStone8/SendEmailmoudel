package org.opentaps.warehouse.shipment.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.base.entities.OrderHeader;
import org.opentaps.foundation.entity.Entity;
import org.opentaps.foundation.service.ServiceException;


public class AutoprintCommoninvoice implements Printable {  

	static Entity order;
	static List<GenericEntity> productlist;
	static GenericEntity buyerperson;
	static GenericEntity saleperson;
	static GenericEntity company;
	static GenericEntity facilityyy;
	static GenericEntity OrderHeaderr;
	static GenericEntity ProductStoretemp;
	static String orderDate;
	static String total;
	static String CASHpay;
	static String cardpay;
	static String CHANGE;
	public static Map<String, Object> Autoprintcommoninvoice(DispatchContext dctx, Map<String, ? extends Object> context) throws ServiceException{
		Map<String, Object> results = ServiceUtil.returnSuccess();
		Map<String, Object> commoninvoiceinfo = (Map<String, Object>) context.get("commoninvoiceinfo");
		
		order = (Entity) commoninvoiceinfo.get("order");
		productlist = (List<GenericEntity>) commoninvoiceinfo.get("productlist");
		buyerperson = (GenericEntity) commoninvoiceinfo.get("buyerperson");
		saleperson = (GenericEntity) commoninvoiceinfo.get("saleperson");
		company = (GenericEntity) commoninvoiceinfo.get("company");
		facilityyy = (GenericEntity) commoninvoiceinfo.get("facilityyy");
		OrderHeaderr = (GenericEntity) commoninvoiceinfo.get("OrderHeaderr");
		ProductStoretemp = (GenericEntity) commoninvoiceinfo.get("ProductStoretemp");
		orderDate = (String) commoninvoiceinfo.get("orderDate");
		total = (String) commoninvoiceinfo.get("total");
		CASHpay = (String) commoninvoiceinfo.get("CASHpay");
		cardpay = (String) commoninvoiceinfo.get("cardpay");
		CHANGE = (String) commoninvoiceinfo.get("CHANGE");
		
		int height=0;
		if(productlist!=null)
			
        height = 320 +productlist.size()*50;  
		else 
			return results;
        Book book = new Book();  
  
        PageFormat pf = new PageFormat();  
        pf.setOrientation(PageFormat.PORTRAIT);  
  
        Paper p = new Paper();  
        p.setSize(230, height);  
        p.setImageableArea(0, 0, 230, height );  
        pf.setPaper(p);  
  
        book.append(new AutoprintCommoninvoice(), pf);  
  
        PrinterJob job = PrinterJob.getPrinterJob();  
        job.setPageable(book);  
        try {  
            job.print();  
        } catch (PrinterException e) {  
            System.out.println("================打印出现异常");  
        }
		
		
		return results;
		
	}
	
	@Override  
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {  
  
        if (page > 0) {  
            return NO_SUCH_PAGE;  
        }  
        int height=0;
        Graphics2D g2d = (Graphics2D) g;  
        Font f6 = new Font("Default", Font.PLAIN, 6);
        Font f8 = new Font("Default", Font.PLAIN, 8);
        Font f10 = new Font("Default", Font.PLAIN, 10);
        Font f12 = new Font("Default", Font.PLAIN, 12);
        Font f14 = new Font("Default", Font.PLAIN, 14);
        Font f16 = new Font("Default", Font.PLAIN, 16);
        
        g2d.setFont(f16);  
        g2d.drawString(company.getString("groupName"), 7, height+10);  
        height=height+10;
        
        g2d.drawString(ProductStoretemp.getString("storeName"), 7, height+20);  
        height=height+20;
        
        g2d.setFont(f10);
        g2d.drawString("Customer:" + buyerperson.getString("firstNameLocal"), 7, height+30);  
        height=height+30;
        g2d.drawString("Sales:" + saleperson.getString("firstName") + " "+saleperson.getString("lastName"), 7, height+15);  
        height=height+15;
        
        
        g2d.setFont(f10);
        g2d.drawString("INVOICE: CI" + OrderHeaderr.getString("orderId"), 7, height+20);  
        height=height+20;
        g2d.drawString("Date:" + orderDate, 7, height+15);  
        height=height+15;
        
        
        
        g2d.setFont(f10);
        g2d.drawString("-----------------------------------------------------------", 7, height+10);   
        height=height+10;
        
        g2d.setFont(f8);
        for(int i=0;i<productlist.size();i++)
        {
        	GenericEntity temp = productlist.get(i);
        	g2d.drawString(temp.getString("quantity").replace(".000000", "")+" * "+temp.getString("productId")+" "+temp.getString("itemDescription"), 7, height+20);  
        	height=height+20;
        	g2d.drawString(temp.getString("currentPrice")+" | "+temp.getString("unitRecurringPrice")+" | "+temp.getString("unitPrice"), 70, height+10);  
        	height=height+10;
        }
        
        g2d.setFont(f12);
        g2d.drawString("Total:" +OrderHeaderr.getString("currencyUom")+ total, 7, height+20);  
        height=height+20;
        
        
        g2d.setFont(f10);
        g2d.drawString("-----------------------------------------------------------", 7, height+10);  
        height=height+10;
        
        g2d.setFont(f12);
        g2d.drawString("Payment", 7, height+30); 
        height=height+30;
        
        if(CASHpay!=null&&!"".equals(CASHpay)&&!"0".equals(CASHpay))
        {
        	g2d.drawString("Cash Paid:"+OrderHeaderr.getString("currencyUom")+CASHpay, 7, height+13);  
        	height=height+13;
        	g2d.drawString("Change:"+OrderHeaderr.getString("currencyUom")+CHANGE, 7, height+13); 
        	height=height+13;
        }
        if(cardpay!=null&&!"".equals(cardpay)&&!"0".equals(cardpay))
        {
        	g2d.drawString("Purchase on account:"+OrderHeaderr.getString("currencyUom")+cardpay, 7, height+13); 
        	height=height+13;
        }
        
        g2d.drawString("VAT INCLUSIVE", 7, height+40); 
        height=height+40;
        g2d.drawString("Thank you,Please visit again.", 7, height+20);
        height=height+20;
        
        g2d.setFont(f8);
        g2d.drawString("Please keep you till slip as proof of purchases", 7, height+20);
        height=height+20;
        
        
        g2d.drawString("Goods sold are not returnable", 7, height+20);
        height=height+20;
        
        
        return PAGE_EXISTS;  
    }  
  
}
