package org.ofbiz.product.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;

public class GoodIdentificationModifyLog {
	
	public static String ExportGoodIdentificationModifyLog(HttpServletRequest request,
			HttpServletResponse response) {
		List<GoodIdentificationModifyLogBean> ret;
		try {
			TransactionUtil.begin();
			ret=ExportGoodIdentificationModifyLogByConditions(request, response);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return "Export error";
		}

		
		List<String> dataList=new ArrayList<String>();
		
		dataList.add("Product ID,ModifyTime,BeforeValue,AfterValue,Operator");
		
		if(ret!=null)
		{
			for(int i=0;i<ret.size();i++)
			{
				dataList.add(ret.get(i).toerportstring());
			}
		}
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//设置日期格式
        SimpleDateFormat filedf = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");//设置日期格式
        response.addHeader("Content-Disposition", "attachment; filename=GoodIdentificationModifyLog"+df.format(new Date())+".zip");
		
        String retmaptemp = getTempFilepath();
        File  tempfilefolder= new File(retmaptemp);
        File  tempfile= new File(retmaptemp+"/GoodIdentificationModifyLog"+filedf.format(new Date())+".csv");
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

	private static List<GoodIdentificationModifyLogBean> ExportGoodIdentificationModifyLogByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		
		List<GoodIdentificationModifyLogBean> retlist =new ArrayList<GoodIdentificationModifyLogBean>();
		
		String sql="select giml.*,gi.PRODUCT_ID from GOOD_IDENTIFICATION_MODIFY_LOG as giml LEFT join GOOD_IDENTIFICATION as gi on gi.ID=giml.GOOD_IDENTIFICATION_ID";		
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		try {
			ResultSet rs = processor.executeQuery(sql);
			while(rs.next()){
				GoodIdentificationModifyLogBean temp=new GoodIdentificationModifyLogBean();
				temp.setProductid(rs.getString("PRODUCT_ID"));
				temp.setBeforeean(rs.getString("BEFORE_VALUE"));
				temp.setAfterean(rs.getString("AFTER_VALUE"));
				temp.setOperator(rs.getString("OPERATOR"));
				temp.setModifyTime(rs.getString("MODIFY_TIME"));
				retlist.add(temp);
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
		
		return retlist ;
	}
	public static Map<String, Object> getSystemSettings() {
		Map<String, Object> retmap=new HashMap<String, Object>();
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key.trim());
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		String tempfilepath = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsTempFilePath");
		
		String SystemSettingsSendemailUserName = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailUserName");
		String SystemSettingsSendemailAuthorizationCode = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailAuthorizationCode");
		String SystemSettingsSendemailRecipient = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailRecipient");
		
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId.trim());
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		retmap.put("tempfilepath", tempfilepath);
		
		retmap.put("SystemSettingsSendemailUserName", SystemSettingsSendemailUserName.trim());
		retmap.put("SystemSettingsSendemailAuthorizationCode", SystemSettingsSendemailAuthorizationCode.trim());
		retmap.put("SystemSettingsSendemailRecipient", SystemSettingsSendemailRecipient.trim());
		
		return retmap;
	}
	public static String getTempFilepath() {
		Map<String, Object> ret = getSystemSettings();
		String tempfilepath = (String)ret.get("tempfilepath");
		if(tempfilepath==null)
			tempfilepath="";
		return tempfilepath;
	}
}
