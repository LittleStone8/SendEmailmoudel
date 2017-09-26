package org.ofbiz.report.report.helper.inventory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.ofbiz.base.util.UtilProperties;

public class InventoryReportFormsResultJsonBean {

	String productId;//产品标识
	String quantityOnHandTotal;//现货数量
	String facility;//仓库
	String unitCost;//现货数量
	String price;//零售价格
	String description;//产品描述
	String productname;//产品名称
	String facilityName;//
	String brandName;//
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getQuantityOnHandTotal() {
		return quantityOnHandTotal;
	}
	public void setQuantityOnHandTotal(String quantityOnHandTotal) {
		this.quantityOnHandTotal = quantityOnHandTotal;
	}
	public String getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public void  replenish()
	{
		if(productId==null||"".equals(productId))productId="---";
		if(quantityOnHandTotal==null||"".equals(quantityOnHandTotal))quantityOnHandTotal="---";
		if(unitCost==null||"".equals(unitCost))unitCost="---";
		if(price==null||"".equals(price))price="---";
		if(productname==null||"".equals(productname)||"null".equals(productname))productname="---";
		if(description==null||"".equals(description)||"null".equals(description))description="---";
		if(brandName==null||"".equals(brandName)||"null".equals(brandName))brandName="---";
		if(facilityName==null||"".equals(facilityName)||"null".equals(facilityName))facilityName="---";
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public String toerportstring(String time,String oum)
	{
		if(time==null)
			time="";
		double qty=0;
		try {
			qty = Double.valueOf(quantityOnHandTotal);
		} catch (NumberFormatException e1) {
			qty=0;
		}
		Double doubleunitCost=0.0;
		try {
			doubleunitCost = Double.valueOf(unitCost);
		} catch (NumberFormatException e) {
			doubleunitCost=0.0;
		}
		Double doubleprice=0.0;
		try {
			doubleprice = Double.valueOf(price);
		} catch (NumberFormatException e) {
			doubleprice=0.0;
		}
		Double SubTotalCost = doubleunitCost*qty;
		Double SubTotalRetailPrice = doubleprice*qty;
    	if(description == null){
    		description = "";
    	}else{
    		description = description.replace("\r", "");
    		description = description.replace(",", " ");
    	}
    	String strSubTotalRetailPrice;
		if(SubTotalRetailPrice.intValue()-SubTotalRetailPrice==0)
    		strSubTotalRetailPrice=SubTotalRetailPrice.intValue()+"";
    	else 
    		strSubTotalRetailPrice=SubTotalRetailPrice+"";
		
		String strSubTotalCost;
		if(SubTotalCost.intValue()-SubTotalCost==0)
			strSubTotalCost=SubTotalCost.intValue()+"";
    	else 
    		strSubTotalCost=SubTotalCost+"";
		
		String strdoubleprice;
		if(doubleprice.intValue()-doubleprice==0)
			strdoubleprice=doubleprice.intValue()+"";
    	else 
    		strdoubleprice=doubleprice+"";
		
		String strdoubleunitCost;
		if(doubleunitCost.intValue()-doubleunitCost==0)
			strdoubleunitCost=doubleunitCost.intValue()+"";
    	else 
    		strdoubleunitCost=doubleunitCost+"";
		
		
    	String SubTotalRetailPricestr = fmtMicrometer(strSubTotalRetailPrice,oum);
    	String SubTotalCoststr = fmtMicrometer(strSubTotalCost+"",oum);
    	String pricestr = fmtMicrometer(strdoubleprice+"",oum);
    	String unitCoststr = fmtMicrometer(strdoubleunitCost+"",oum);
    	String quantityOnHandTotalstr = fmtMicrometer(qty+"");
    	
    	
    	
		return time.replaceAll("-", ".")+","+productId+","+brandName+","+productname+","+description+","+facilityName+","+quantityOnHandTotalstr+","+unitCoststr+","+pricestr+","+SubTotalCoststr+","+SubTotalRetailPricestr;
	}
	
	
	
	public static String fmtMicrometer(String text,String oum)  
	{  
	    DecimalFormat df = null;  
	    if(text.indexOf(".") > 0)  
	    {  
	        if(text.length() - text.indexOf(".")-1 == 0)  
	        {  
	            df = new DecimalFormat("###,##0.");  
	        }else if(text.length() - text.indexOf(".")-1 == 1)  
	        {  
	            df = new DecimalFormat("###,##0.0");  
	        }else  
	        {  
	            df = new DecimalFormat("###,##0.00");  
	        }  
	    }else   
	    {  
	        df = new DecimalFormat("###,##0");  
	    }  
	    double number = 0.0;  
	    try {  
	         number = Double.parseDouble(text);  
	    } catch (Exception e) {  
	        number = 0.0;  
	    }  
	    return "\""+df.format(number)+" "+oum+"\"";  
	} 
	public static String fmtMicrometer(String text)  
	{  
	    DecimalFormat df = null;  
	    if(text.indexOf(".") > 0)  
	    {  
	        if(text.length() - text.indexOf(".")-1 == 0)  
	        {  
	            df = new DecimalFormat("###,##0.");  
	        }else if(text.length() - text.indexOf(".")-1 == 1)  
	        {  
	            df = new DecimalFormat("###,##0.0");  
	        }else  
	        {  
	            df = new DecimalFormat("###,##0.00");  
	        }  
	    }else   
	    {  
	        df = new DecimalFormat("###,##0");  
	    }  
	    double number = 0.0;  
	    try {  
	         number = Double.parseDouble(text);  
	    } catch (Exception e) {  
	        number = 0.0;  
	    }  
	    return "\""+df.format(number)+"\"";  
	}  
}
