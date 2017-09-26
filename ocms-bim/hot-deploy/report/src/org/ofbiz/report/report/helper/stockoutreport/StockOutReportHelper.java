package org.ofbiz.report.report.helper.stockoutreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.util.ZipUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class StockOutReportHelper {
	
	
	public static Map<String, Object> serachStockOutReportByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> returnmap;
		try {
			TransactionUtil.begin();
			returnmap=StockOutReportByConditions(request, response,false);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}

		return returnmap;
	}
	
	
	
	
	
	
	
	public static String ExportStockOutReportByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> ret;
		try {
			TransactionUtil.begin();
			ret=StockOutReportByConditions(request, response, true);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return "Export error";
		}

		
		List<StockOutReportInfo> retlist =(List<StockOutReportInfo>)ret.get("result");
		List<String> dataList=new ArrayList<String>();
		
		dataList.add("Product ID,Description,Transfer Quantity,OutStock time,Transfer Complete Date,From,To");
		
		if(retlist!=null)
		{
			for(int i=0;i<retlist.size();i++)
			{
				dataList.add(retlist.get(i).toerportstring());
			}
		}
		dataList.add("Total Quantity: "+ret.get("sumNum")+" pcs");
		
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//设置日期格式
        SimpleDateFormat filedf = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        response.addHeader("Content-Disposition", "attachment; filename=StockOutReport"+df.format(new Date())+".zip");
		
        String retmaptemp = BaseReportEvent.getTempFilepath();
        File  tempfilefolder= new File(retmaptemp);
        File  tempfile= new File(retmaptemp+"/StockOutReport"+filedf.format(new Date())+".csv");
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
	

	public static Map<String, Object> StockOutReportByConditions(HttpServletRequest request, HttpServletResponse response,boolean isExport) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		
		
		String pageNum = "";
		String pageSize = "";
		String productid = "";
		String facilityId = "";
		String startdate = "";
		String enddate = "";
		String form="";
		String to="";
		String model="";
		
		Map<String, Object> facilitylistmap = queryFacility(request, response);
		List<FacilityVo> facilitylist=(List<FacilityVo>)facilitylistmap.get("inventoryFacilitys");
		if(facilitylist.size()<=0)
		{
			result.put("resultCode", -1);
			result.put("resultMsg", "You don't have permission to query data.");
			return result;
		}
		
		JSONObject json = makeParams(request);
		if(json!=null)
		{
			productid = json.getString("productid");
			model = json.getString("model");
			form = json.getString("form");
			startdate = json.getString("startDate");
			enddate = json.getString("endDate");
			pageNum = json.getString("pageNum");
			to = json.getString("to");
			pageSize = json.getString("pageSize");
		}else 
		{
			productid = request.getParameter("productid").trim();
			model = request.getParameter("model").trim();
			form = request.getParameter("form").trim();
			to = request.getParameter("to").trim();
			startdate = request.getParameter("startDate").trim();
			enddate = request.getParameter("endDate").trim();
			pageNum = request.getParameter("pageNum");
			pageSize = request.getParameter("pageSize");
		}
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		
		String addsql="";
		
		String sql = "SELECT	ret.*, profed.DESCRIPTION,	pr.BRAND_NAME,	pr.INTERNAL_NAME FROM	( select tttt.* from " + " ( " + " select * from " +
				" ( " + " select  "
				+ " (select ii.PRODUCT_ID from INVENTORY_ITEM as ii where ii.INVENTORY_ITEM_ID=it.INVENTORY_ITEM_ID) as productid ,sum(it.TRANSFER_NUMBER) as transferqty,max(  ifnull(it.RECEIVE_DATE,'9999-99-99 99:99:99')) AS tcdate,it.SEND_DATE as outstocktime,"
				+ " (select FACILITY.FACILITY_NAME from FACILITY where FACILITY.FACILITY_ID=it.FACILITY_ID) as fromfac,( select FACILITY.FACILITY_NAME from FACILITY where FACILITY.FACILITY_ID=it.FACILITY_ID_TO) as tofac from INVENTORY_TRANSFER  as it where totempindex ( it.STATUS_ID =\'IXF_COMPLETE\' or it.STATUS_ID =\'IXF_REQUESTED\' )  tempindex "
				+ " group by  date(it.SEND_DATE),it.FACILITY_ID,it.FACILITY_ID_TO,(select ii.PRODUCT_ID from INVENTORY_ITEM as ii where ii.INVENTORY_ITEM_ID=it.INVENTORY_ITEM_ID) "
				+ " ) as ret "
				+ " 	) as tttt  where 1=1 indexxxx ) as ret"
				+ " LEFT JOIN ( "
	+" SELECT PRODUCT_ID,		GROUP_CONCAT(			DISTINCT DESCRIPTION			ORDER BY				PRODUCT_FEATURE_TYPE_ID		) AS DESCRIPTION	FROM		(			SELECT				c.PRODUCT_ID AS PRODUCT_ID, "
	+" 			c.PRODUCT_FEATURE_ID AS PRODUCT_FEATURE_ID,				DESCRIPTION,				d.PRODUCT_FEATURE_TYPE_ID AS PRODUCT_FEATURE_TYPE_ID			FROM				PRODUCT_FEATURE_APPL c			LEFT JOIN PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
	+"	) AS fea	GROUP BY		PRODUCT_ID) AS profed ON profed.PRODUCT_ID = ret.productid LEFT JOIN PRODUCT AS pr ON pr.PRODUCT_ID = ret.productid  where 1=1 ";
		
		if(model!=null&&!"".equals(model))
			sql+=" and INTERNAL_NAME like \'%"+model+"%\'";
		if(productid!=null&&!"".equals(productid))
			sql+=" and productid like \'%"+productid+"%\'";
		
		if(to!=null&&!"".equals(to))
		{
			sql=sql.replaceAll("totempindex", "  it.FACILITY_ID_TO = \'"+to+"\' and ");
		}else 
		{
			sql=sql.replaceAll("totempindex", "");
		}
		
		if(form!=null&&!"".equals(form))
			sql=sql.replaceAll("tempindex", " and it.FACILITY_ID = \'"+form+"\'");
		else 
		{
			String tempstring="";
			for(int k=0;k<facilitylist.size();k++)
			{
				FacilityVo facility = facilitylist.get(k);
				String tempfacilityId = facility.getFacilityId();
				if(k==0)
					tempstring+=" and ( it.FACILITY_ID = \'"+tempfacilityId+"\' ";
				else 
					tempstring+=" or it.FACILITY_ID = \'"+tempfacilityId+"\' ";
			}
			tempstring+=" ) ";
			sql=sql.replaceAll("tempindex", tempstring);
		}
			sql=sql.replaceAll("tempindex", " ");
			
			
		if(startdate!=null&&!"".equals(startdate))
			addsql+=" and outstocktime >= \'"+startdate+"\'";
		if(enddate!=null&&!"".equals(enddate))
			addsql+=" and outstocktime <= \'"+enddate+"\'";
		
		sql=sql.replaceAll("indexxxx", addsql);
		
		Delegator delegator = (Delegator) request.getAttribute("delegator");

			try {
				result = EncapsulatedQueryResultsUtil.getResults(new StringBuffer(sql), Integer.parseInt(pageNum), Integer.parseInt(pageSize), StockOutReportInfo.class, delegator,isExport);
				result.put("resultCode", 1);
				result.put("resultMsg", "Successful operation");	
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				result.put("resultCode", -1);
				result.put("resultMsg", "System exception, please contact the administrator");
				e1.printStackTrace();
			}
		
		
		
//		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
//		SQLProcessor processor = new SQLProcessor(helperInfo);
//		int index=-1;
//		int totalqty=0;
//			try {
//				ResultSet rs = processor.executeQuery(sql);
//				
//				while (rs.next()) {
//					index++;
//					String traqtystr = rs.getString("traqty");
//					Integer qty=0;
//					try {
//						qty = Integer.valueOf(traqtystr);
//					} catch (NumberFormatException e) {
//					}
//					totalqty+=qty;
//					if (isExport|| (index < page * evepage && index >= (page - 1) * evepage)) {
//						String productidstr = rs.getString("productid");
//						String fromfacstr = rs.getString("fromfac");
//						String tcdatestr = rs.getString("tcdate");
//						String tofacstr = rs.getString("tofac");
//						String INTERNAL_NAMEstr = rs.getString("INTERNAL_NAME");
//						if(INTERNAL_NAMEstr==null||"".equals(INTERNAL_NAMEstr))
//							INTERNAL_NAMEstr="---";
//						String BRAND_NAMEstr = rs.getString("BRAND_NAME");
//						if(BRAND_NAMEstr==null||"".equals(BRAND_NAMEstr))
//							BRAND_NAMEstr="---";
//						String DESCRIPTION = rs.getString("DESCRIPTION");
//						if(DESCRIPTION==null||"".equals(DESCRIPTION))
//							DESCRIPTION="---";
//						
//						StockOutReportInfo temp = new StockOutReportInfo();
//						temp.setComdate(tcdatestr);
//						temp.setDescription(BRAND_NAMEstr+"|"+INTERNAL_NAMEstr+"|"+DESCRIPTION);
//						temp.setFromfaclityc(fromfacstr);
//						temp.setTofaclityc(tofacstr);
//						temp.setProductId(productidstr);
//						temp.setTransferqty(traqtystr);
//						resultList.add(temp);
//					}
//					
//				}
//			} catch (GenericDataSourceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (GenericEntityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//	
//		totalnumber = index + 1;
//		map.put("totalnumber", totalnumber);
//		map.put("evepage", evepage);
//		map.put("currentpage", page);
//		map.put("totalqty", totalqty);
//		map.put("LookupInventoryReportFormslist", resultList);
		return result;
	}

	public static JSONObject makeParams(HttpServletRequest request){		
		HttpSession session = request.getSession();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		StringBuffer jsonStringBuffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}		
		String productPriceList =  jsonStringBuffer.toString();
		JSONObject json = JSON.parseObject(productPriceList);
		return json;
	}
	 public static Map<String,Object> queryFacility(HttpServletRequest request, HttpServletResponse response) {
		 	Map<String,Object> result = new HashMap<String, Object>();
		 	LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
	        Delegator delegator = dispacher.getDelegator();
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
			String partyId = (String) userLogin.get("partyId");
			String userLoginId = (String) userLogin.get("userLoginId");			
			try {
				String facilityId = getUserLoginViewPreference(request, "opentaps", "selectFacilityForm", "facilityId");				
				String sql = makeSqlToFindFacility(delegator, userLoginId, partyId);
				List<FacilityVo> inventoryFacilitys = getFacilityByPermission(delegator, sql);				
				result.put("inventoryFacilitys", inventoryFacilitys);
				result.put("defaultFacilityId", facilityId);
				result.put("resultCode", 1);
				result.put("resultMsg", "Successful operation");
			} catch (Exception e) {
				result.put("resultCode", -1);
				result.put("resultMsg", "System exception, please contact the administrator");
				e.printStackTrace();
			}
			return result;
	 }
	 public static String makeSqlToFindFacility(Delegator delegator,String userLoginId,String partyId) throws GenericEntityException{
		 String sql = "";
		 List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);			
		 List<String> groupIds = new ArrayList<String>();
		 for(GenericValue userSecurityGroup : userSecurityGroups){
			 groupIds.add(userSecurityGroup.getString("groupId"));
		 }
		 if(groupIds != null && groupIds.contains("WRHS_ADMIN")){
			 sql = "select FACILITY_ID,FACILITY_NAME FROM FACILITY";
		 }else{
			 sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID";
		 }
		 return sql;
	 }
		public static List<FacilityVo> getFacilityByPermission(Delegator delegator,String sql) throws Exception{
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			List<FacilityVo> inventoryFacilitys = new ArrayList<FacilityVo>();		
			ResultSet rs = processor.executeQuery(sql);
			while(rs.next()){
				FacilityVo facilityVo = new FacilityVo();
				facilityVo.setFacilityId(rs.getString("FACILITY_ID"));
				facilityVo.setFacilityName(rs.getString("FACILITY_NAME"));
				inventoryFacilitys.add(facilityVo);
			}
			Collections.sort(inventoryFacilitys);
			rs.close();
			processor.close();
			return inventoryFacilitys;
		}
	    public static String getUserLoginViewPreference(HttpServletRequest request, String applicationName, String screenName, String option) throws GenericEntityException {
	        GenericValue userLogin = getUserLogin(request);
	        Delegator delegator = (Delegator) request.getAttribute("delegator");
	        GenericValue pref = delegator.findByPrimaryKeyCache("UserLoginViewPreference", UtilMisc.toMap("userLoginId", userLogin.get("userLoginId"), "applicationName", applicationName, "screenName", screenName, "preferenceName", option));
	        if (pref == null) {
	            return null;
	        }
	        return pref.getString("preferenceValue");
	    }
	    public static GenericValue getUserLogin(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        return (GenericValue) session.getAttribute("userLogin");
	    }
}
