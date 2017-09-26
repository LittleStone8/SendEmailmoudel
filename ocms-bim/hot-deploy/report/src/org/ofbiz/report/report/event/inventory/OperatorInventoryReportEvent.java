package org.ofbiz.report.report.event.inventory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.report.report.helper.inventory.InventoryReportFormsResultJsonBean;
import org.ofbiz.report.report.helper.inventory.InventoryReportHelper;
import org.ofbiz.report.security.OperatorReportSecurity;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.util.ZipUtils;

public class OperatorInventoryReportEvent {


	/**
	 * 库存报表初始化，查询可用店铺和店铺租
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchAvailableStoresAndSGroups_MTN(HttpServletRequest request,
			HttpServletResponse response) {

		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		return searchAvailableStoresAndSGroups_MTN(delegator, userLogin);
	}

	public static Map<String, Object> searchAvailableStoresAndSGroups_MTN(GenericDelegator delegator,
			GenericValue userLogin) {
		OperatorReportSecurity operatorReportSecurity = new OperatorReportSecurity(delegator);
		operatorReportSecurity.setType(OperatorReportSecurity.MTN);
		return BaseInventoryReportEvent.searchStoreBySecurity(delegator, userLogin,operatorReportSecurity);
	}

	/**
	 * 条件查询MTN库存报表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> search_SPECIALMarketByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		
		Map<String, Object> returnmap;
		try {
			TransactionUtil.begin();
			OperatorReportSecurity baneportSecurity = new OperatorReportSecurity(delegator);
			Map<String, Object> securityret = BaseInventoryReportEvent.searchStoreBySecurity(delegator, userLogin,baneportSecurity);
			returnmap=InventoryReportHelper.LookupInventoryReportForms_SPECIAL_COST_PRICE(request, response, securityret,false);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}
		return returnmap;
	}

	public static Map<String, Object> Exportsearch_SPECIALMarketByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		
		Map<String, Object> returnmap;
		try {
			TransactionUtil.begin();
			OperatorReportSecurity baneportSecurity = new OperatorReportSecurity(delegator);
			Map<String, Object> securityret = BaseInventoryReportEvent.searchStoreBySecurity(delegator, userLogin,baneportSecurity);
			returnmap=InventoryReportHelper.LookupInventoryReportForms_SPECIAL_COST_PRICE(request, response, securityret,true);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}
		return returnmap;
	}
	
	public static String ExportOperatorInventoryReport(HttpServletRequest request, HttpServletResponse response) {
		
		
		Map<String, Object> ret = Exportsearch_SPECIALMarketByConditions(request,response);
		List<InventoryReportFormsResultJsonBean> retlist =(List<InventoryReportFormsResultJsonBean>)ret.get("LookupInventoryReportFormslist");
		List<String> dataList=new ArrayList<String>();
		
		dataList.add("Date,SKU,Brand,Model,Description,Facility/Store,QTY,Unit Cost,Unit Retail Price,Sub Total Cost,Sub Total Retail Price");
		
		String Exporttime=(String)ret.get("time");
		
		String oum = (String)BaseReportEvent.getSystemSettings().get("systemsettinguomid");
		if(retlist!=null)
		{
			for(int i=0;i<retlist.size();i++)
			{
				dataList.add(retlist.get(i).toerportstring(Exporttime,oum));
			}
		}
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat filedf = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        response.addHeader("Content-Disposition", "attachment; filename=InventoryReport"+df.format(new Date())+".zip");
		
        String retmaptemp = BaseReportEvent.getTempFilepath();
        File  tempfilefolder= new File(retmaptemp);
        File  tempfile= new File(retmaptemp+"/InventoryReport"+filedf.format(new Date())+".csv");
        if (!tempfilefolder.exists()) {
        	tempfilefolder.mkdirs();
           }
        if (!tempfile.exists()) {
        	try {
        		tempfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
        FileOutputStream fop = null;
        try {
        	
        	
        	fop = new FileOutputStream(tempfile);
        	
            for(String data : dataList) {
            	fop.write(data.getBytes());
            	fop.write("\n".getBytes());
            }	   
            fop.flush();
            fop.close();
        }
        catch (IOException e) {        	
            e.printStackTrace();
        }
        finally{
            try {
                if(fop != null){
                	fop.flush();
                	fop.close();
                }
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
        }
        try {
			ZipUtils.doCompress(tempfile, response.getOutputStream());
			tempfile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
        return "success";

	}

}
