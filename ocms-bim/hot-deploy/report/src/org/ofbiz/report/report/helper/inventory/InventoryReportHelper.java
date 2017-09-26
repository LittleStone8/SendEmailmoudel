package org.ofbiz.report.report.helper.inventory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.report.security.BaseStoreSecurity;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreAndGroupBean;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;

import javolution.util.FastList;


public class InventoryReportHelper {


	public static SimpleDateFormat segmentationFormat = new SimpleDateFormat("yyyyMMdd");
	public static String segmentationTime = "20170901";
	
	public static Map<String, Object> LookupInventoryReportForms_DEFAULT_COST_PRICE(HttpServletRequest request, HttpServletResponse response,Map<String, Object> ret,boolean isExport) {
		Map<String, Object> map = ServiceUtil.returnSuccess();
		List<InventoryReportFormsResultJsonBean> resultList = new ArrayList<InventoryReportFormsResultJsonBean>();
		
		
		long segmentationTimelong = 0;
		try {
			segmentationTimelong = segmentationFormat.parse(segmentationTime).getTime();
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		int totalnumber=0;
		int evepage=30;
		String pagestr = request.getParameter("page");
		int page;
		try {
			page = Integer.valueOf(pagestr);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			page=1;
		}
		
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericDelegator delegatorGD = (GenericDelegator) request.getAttribute("delegator");	
		GenericValue userLoginGV = (GenericValue) request.getSession().getAttribute("userLogin");  
		try {
			String productStoreGroupId = request.getParameter("productStoreGroupId");
			String productStoreId = request.getParameter("productStoreId");
			String productIdParameter = request.getParameter("productId");
			String endtime = request.getParameter("endtime");
			String DEFAULT_COST_PRICE="DEFAULT_COST_PRICE";
			String RETAIL_COST_PRICE="RETAIL_COST_PRICE";
			map.put("time", endtime);
			//2017-04-21
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
			try {
				date = sdf.parse(endtime);
			} catch (ParseException e) {
				return ServiceUtil.returnError("Time error");
			}
            Timestamp nowDate = new Timestamp(date.getTime());
            String tablename="INVENTORY_REPORT";
            if(nowDate.getTime()>=segmentationTimelong)
            {
        		String yyyyMM = new SimpleDateFormat("yyyyMM").format(nowDate);
        		tablename="INVENTORY_REPORT"+yyyyMM;
            }
			String isUGMAN=(String)ret.get(BaseStoreSecurity.REP_INV_UG_MAN);
			String isGHMAN=(String)ret.get(BaseStoreSecurity.REP_INV_GH_MAN);
			String isADMIN=(String)ret.get(BaseStoreSecurity.REP_INV_ADMIN);
			
    		List<StoreGroupBean> sGroupBeans =  (List<StoreGroupBean>)ret.get("SGroup");
    		List<StoreRollUpBean> StoreAndGroupBeans = (List<StoreRollUpBean>)ret.get("SGMember");
    		List<StoreRollUpBean> checkretlist=new ArrayList<StoreRollUpBean>();
    		if(StoreAndGroupBeans==null||StoreAndGroupBeans.size()<0)
    		{
    			return ServiceUtil.returnError("You don't have permission to query this");
    		}
            if("".equals(productStoreId)&&"".equals(productStoreGroupId))//都不选，查所有店铺组下的所有店铺
            {
            	checkretlist=StoreAndGroupBeans;
            }else if("".equals(productStoreId)&&!"".equals(productStoreGroupId))//选店铺组，查该店铺组下所有店铺
            {
            	for(int i=0;i<StoreAndGroupBeans.size();i++)
            	{
            		StoreRollUpBean temp = StoreAndGroupBeans.get(i);
            		if(productStoreGroupId.equals(temp.getStoreGroupID()))
            		{
            			checkretlist.add(temp);
            		}
            	}	
            }else //查单个店铺
            {
            	for(int i=0;i<StoreAndGroupBeans.size();i++)
            	{
            		StoreRollUpBean temp = StoreAndGroupBeans.get(i);
            		if(productStoreGroupId.equals(temp.getStoreGroupID())&&productStoreId.equals(temp.getStoreID()))
            		{
            			checkretlist.add(temp);
            			break;
            		}
            	}	
            }
            if(checkretlist.size()<=0)
            {
				map.put("LookupInventoryReportFormslist", null);//空，代表没权限查目标店铺报表，前台直接报错
				return map;
            }
            
			String sql="SELECT * from PRODUCT_STORE as ps where ps.PRODUCT_STORE_ID=\'"+checkretlist.get(0).getStoreID()+"\'";
			String cons="";
			for(int k=1;k<checkretlist.size();k++)
			{
				cons+="or ps.PRODUCT_STORE_ID=\'"+checkretlist.get(k).getStoreID()+"\'";
			}
			sql+=cons;
			
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			ResultSet rs = processor.executeQuery(sql);
			List<String> facid=new ArrayList<String>();
			try {
				while (rs.next()) {
					String inventoryFacilityId=rs.getString("INVENTORY_FACILITY_ID");
					if(inventoryFacilityId!=null)
						facid.add(inventoryFacilityId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(productIdParameter==null)
				productIdParameter="";
			String sqlt="SELECT * from "+tablename+" as ir where ir.PRODUCT_ID like \'%"+productIdParameter+"%\' and ir.RECORD_DATE =\'"+nowDate+"\' ";
			String addstr="";
			if(facid.size()<=0)
			{
				map.put("LookupInventoryReportFormslist", resultList);
				map.put("totalnumber", totalnumber);
				map.put("evepage", evepage);
				map.put("currentpage", page);
				return map;
			}
			for(int k=0;k<facid.size();k++)
			{
				String tempsgi = facid.get(k);
				if(k==0)
				{
					addstr+=" ir.FACILITY_ID=\'"+tempsgi+"\' ";
				}
				else 
				{
					addstr+=" or ir.FACILITY_ID=\'"+tempsgi+"\' ";
				}
			}
			if(!"".equals(addstr))
			{
				sqlt+="and ( "+addstr +")";
			}
			
			rs=processor.executeQuery(sqlt);
			int index=-1;
			try {
				while (rs.next()) {
					if ( isADMIN!= null) {
						String productStoreGroupIdtemp = rs.getString("PRODUCT_STORE_GROUP_ID");
						if (productStoreGroupIdtemp != null && !DEFAULT_COST_PRICE.equals(productStoreGroupIdtemp))
							continue;
						
						index++;
						if (isExport|| (index < page * evepage && index >= (page - 1) * evepage)) {
							String productId = rs.getString("PRODUCT_ID");
							String quantityOnHandTotal = rs.getString("QUANTITY_ON_HAND_TOTAL");
							String unitCost = rs.getString("AVG_COST");
							String price = rs.getString("PRICE");
							String description = rs.getString("DESCRIPTION");
							String productname = rs.getString("PRODUCTNAME");
							String facility = rs.getString("FACILITY_ID");
							String facilityName = rs.getString("FACILITY_NAME");
							String brandName = rs.getString("BRAND_NAME");

							InventoryReportFormsResultJsonBean tempretbean = new InventoryReportFormsResultJsonBean();
							tempretbean.setProductId(productId);
							tempretbean.setQuantityOnHandTotal(quantityOnHandTotal);
							tempretbean.setUnitCost(unitCost);
							tempretbean.setPrice(price);
							tempretbean.setDescription(description);
							tempretbean.setProductname(productname);
							tempretbean.setFacility(facility);
							tempretbean.setFacilityName(facilityName);
							tempretbean.setBrandName(brandName);
							tempretbean.replenish();
							resultList.add(tempretbean);
						}
					}
					else if ( isGHMAN!= null) {
						String productStoreGroupIdtemp = rs.getString("PRODUCT_STORE_GROUP_ID");
						if (productStoreGroupIdtemp != null && !DEFAULT_COST_PRICE.equals(productStoreGroupIdtemp))
							continue;
						index++;
						if (isExport|| (index < page * evepage && index >= (page - 1) * evepage)) {
							String productId = rs.getString("PRODUCT_ID");
							String quantityOnHandTotal = rs.getString("QUANTITY_ON_HAND_TOTAL");
							String unitCost = rs.getString("AVG_COST");
							String price = rs.getString("PRICE");
							String description = rs.getString("DESCRIPTION");
							String productname = rs.getString("PRODUCTNAME");
							String facility = rs.getString("FACILITY_ID");
							String facilityName = rs.getString("FACILITY_NAME");
							String brandName = rs.getString("BRAND_NAME");

							InventoryReportFormsResultJsonBean tempretbean = new InventoryReportFormsResultJsonBean();
							tempretbean.setProductId(productId);
							tempretbean.setQuantityOnHandTotal(quantityOnHandTotal);
							tempretbean.setUnitCost("-----");
							tempretbean.setPrice(price);
							tempretbean.setDescription(description);
							tempretbean.setProductname(productname);
							tempretbean.setFacility(facility);
							tempretbean.setFacilityName(facilityName);
							tempretbean.setBrandName(brandName);
							tempretbean.replenish();
							resultList.add(tempretbean);
						}
					}else if(isUGMAN != null)
					{
						String productStoreGroupIdtemp = rs.getString("PRODUCT_STORE_GROUP_ID");
						if (productStoreGroupIdtemp != null && !RETAIL_COST_PRICE.equals(productStoreGroupIdtemp))
							continue;
						index++;
						if (isExport|| (index < page * evepage && index >= (page - 1) * evepage)) {
							String productId = rs.getString("PRODUCT_ID");
							String quantityOnHandTotal = rs.getString("QUANTITY_ON_HAND_TOTAL");
							String unitCost = rs.getString("AVG_COST");
							String price = rs.getString("PRICE");
							String description = rs.getString("DESCRIPTION");
							String productname = rs.getString("PRODUCTNAME");
							String facility = rs.getString("FACILITY_ID");
							String facilityName = rs.getString("FACILITY_NAME");
							String brandName = rs.getString("BRAND_NAME");

							InventoryReportFormsResultJsonBean tempretbean = new InventoryReportFormsResultJsonBean();
							tempretbean.setProductId(productId);
							tempretbean.setQuantityOnHandTotal(quantityOnHandTotal);
							tempretbean.setUnitCost(unitCost);
							tempretbean.setPrice(price);
							tempretbean.setDescription(description);
							tempretbean.setProductname(productname);
							tempretbean.setFacility(facility);
							tempretbean.setFacilityName(facilityName);
							tempretbean.setBrandName(brandName);
							tempretbean.replenish();
							resultList.add(tempretbean);
						}
					}
					
				}
				totalnumber=index+1;
				map.put("totalnumber", totalnumber);
				map.put("evepage", evepage);
				map.put("currentpage", page);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("LookupInventoryReportFormslist", resultList);
		return map;
	}
	
	
	
	public static Map<String, Object> LookupInventoryReportForms_SPECIAL_COST_PRICE(HttpServletRequest request, HttpServletResponse response,Map<String, Object> ret,boolean isExport) {
		
		long segmentationTimelong = 0;
		try {
			segmentationTimelong = segmentationFormat.parse(segmentationTime).getTime();
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		int totalnumber=0;
		int evepage=30;
		String pagestr = request.getParameter("page");
		int page;
		try {
			page = Integer.valueOf(pagestr);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			page=1;
		}
		
		
		Map<String, Object> map = ServiceUtil.returnSuccess();
		List<InventoryReportFormsResultJsonBean> resultList = new ArrayList<InventoryReportFormsResultJsonBean>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericDelegator delegatorGD = (GenericDelegator) request.getAttribute("delegator");	
		GenericValue userLoginGV = (GenericValue) request.getSession().getAttribute("userLogin");  
		try {
			String productStoreGroupId = request.getParameter("productStoreGroupId");
			String productStoreId = request.getParameter("productStoreId");
			String productIdParameter = request.getParameter("productId");
			String endtime = request.getParameter("endtime");
			String SPECIAL_COST_PRICE="SPECIAL_COST_PRICE";
			map.put("time", endtime);
			//2017-04-21
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
			try {
				date = sdf.parse(endtime);
			} catch (ParseException e) {
				return ServiceUtil.returnError("Time error");
			}
            Timestamp nowDate = new Timestamp(date.getTime());
            String tablename="INVENTORY_REPORT";
            if(nowDate.getTime()>=segmentationTimelong)
            {
        		String yyyyMM = new SimpleDateFormat("yyyyMM").format(nowDate);
        		tablename="INVENTORY_REPORT"+yyyyMM;
            }
            
    		List<StoreGroupBean> sGroupBeans =  (List<StoreGroupBean>)ret.get("SGroup");
    		List<StoreRollUpBean> StoreAndGroupBeans = (List<StoreRollUpBean>)ret.get("SGMember");
    		List<StoreRollUpBean> checkretlist=new ArrayList<StoreRollUpBean>();
    		
    		
    		if(StoreAndGroupBeans==null||StoreAndGroupBeans.size()<0)
    		{
    			return ServiceUtil.returnError("You don't have permission to query this");
    		}
            if("".equals(productStoreId)&&"".equals(productStoreGroupId))//都不选，查所有店铺组下的所有店
            {
            	checkretlist=StoreAndGroupBeans;
            }else if("".equals(productStoreId)&&!"".equals(productStoreGroupId))//选店铺组，查该店铺组下所有店
            {
            	for(int i=0;i<StoreAndGroupBeans.size();i++)
            	{
            		StoreRollUpBean temp = StoreAndGroupBeans.get(i);
            		if(productStoreGroupId.equals(temp.getStoreGroupID()))
            		{
            			checkretlist.add(temp);
            		}
            	}	
            }else //查单个店
            {
            	for(int i=0;i<StoreAndGroupBeans.size();i++)
            	{
            		StoreRollUpBean temp = StoreAndGroupBeans.get(i);
            		if(productStoreGroupId.equals(temp.getStoreGroupID())&&productStoreId.equals(temp.getStoreID()))
            		{
            			checkretlist.add(temp);
            			break;
            		}
            	}	
            }
            if(checkretlist.size()<=0)
            {
            	return ServiceUtil.returnError("You don't have permission to query this");
            }
            
			String sql="SELECT * from PRODUCT_STORE as ps where ps.PRODUCT_STORE_ID=\'"+checkretlist.get(0).getStoreID()+"\'";
			String cons="";
			for(int k=1;k<checkretlist.size();k++)
			{
				cons+="or ps.PRODUCT_STORE_ID=\'"+checkretlist.get(k).getStoreID()+"\'";
			}
			sql+=cons;
			
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			ResultSet rs = processor.executeQuery(sql);
			List<String> facid=new ArrayList<String>();
			try {
				while (rs.next()) {
					String inventoryFacilityId=rs.getString("INVENTORY_FACILITY_ID");
					if(inventoryFacilityId!=null)
						facid.add(inventoryFacilityId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(productIdParameter==null)
				productIdParameter="";
			String sqlt="SELECT * from "+tablename+" as ir where ir.PRODUCT_ID like \'%"+productIdParameter+"%\' and ir.RECORD_DATE =\'"+nowDate+"\' ";
			String addstr="";
			if(facid.size()<=0)
			{
				map.put("LookupInventoryReportFormslist", resultList);
				map.put("totalnumber", totalnumber);
				map.put("evepage", evepage);
				map.put("currentpage", page);
				return map;
			}
			for(int k=0;k<facid.size();k++)
			{
				String tempsgi = facid.get(k);
				if(k==0)
				{
					addstr+=" ir.FACILITY_ID=\'"+tempsgi+"\' ";
				}
				else 
				{
					addstr+=" or ir.FACILITY_ID=\'"+tempsgi+"\' ";
				}
			}
			if(!"".equals(addstr))
			{
				sqlt+="and ( "+addstr +")";
			}
			
			rs=processor.executeQuery(sqlt);
			int index=-1;
			try {
				while (rs.next()) {
					String productStoreGroupIdtemp = rs.getString("PRODUCT_STORE_GROUP_ID");
					if(productStoreGroupIdtemp!=null&&!SPECIAL_COST_PRICE.equals(productStoreGroupIdtemp))
						continue;
					index++;
					if (isExport || (index < page * evepage && index >= (page - 1) * evepage)) {
						String productId = rs.getString("PRODUCT_ID");
						String quantityOnHandTotal = rs.getString("QUANTITY_ON_HAND_TOTAL");
						String unitCost = rs.getString("AVG_COST");
						String price = rs.getString("PRICE");
						String description = rs.getString("DESCRIPTION");
						String productname = rs.getString("PRODUCTNAME");
						String facility = rs.getString("FACILITY_ID");
						String facilityName = rs.getString("FACILITY_NAME");
						String brandName = rs.getString("BRAND_NAME");

						InventoryReportFormsResultJsonBean tempretbean = new InventoryReportFormsResultJsonBean();
						tempretbean.setProductId(productId);
						tempretbean.setQuantityOnHandTotal(quantityOnHandTotal);
						tempretbean.setUnitCost(unitCost);
						tempretbean.setPrice(price);
						tempretbean.setDescription(description);
						tempretbean.setProductname(productname);
						tempretbean.setFacility(facility);
						tempretbean.setFacilityName(facilityName);
						tempretbean.setBrandName(brandName);
						tempretbean.replenish();
						resultList.add(tempretbean);
					}
				}
				totalnumber=index+1;
				map.put("totalnumber", totalnumber);
				map.put("evepage", evepage);
				map.put("currentpage", page);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("LookupInventoryReportFormslist", resultList);
		return map;
	}
	
	
	
	public static Map<String, Object> LookupInventoryReportForms(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<InventoryReportFormsResultJsonBean> resultList = new ArrayList<InventoryReportFormsResultJsonBean>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		try {
			String productStoreGroupId = request.getParameter("productStoreGroupId");
			String productStoreId = request.getParameter("productStoreId");
			String productIdParameter = request.getParameter("productId");
			String endtime = request.getParameter("endtime");
			//2017-04-21
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
			try {
				date = sdf.parse(endtime);
			} catch (ParseException e) {
				return ServiceUtil.returnError("Time error");
			}
            System.out.println(date.getTime());
            Timestamp nowDate = new Timestamp(date.getTime());
			
			if(productStoreGroupId==null||productStoreId==null||"".equals(productStoreId)||"".equals(productStoreGroupId))
			{
				map.put("LookupInventoryReportFormslist", resultList);
				return map;
			}
			List l1 = new ArrayList();//存放查询条件
			l1.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId));
			List<GenericValue> retlist1 = delegator.findByAnd("ProductStore",l1);
			GenericValue temp1 = retlist1.get(0);
			String defaultCurrencyUomId = temp1.getString("defaultCurrencyUomId");
			String inventoryFacilityId = temp1.getString("inventoryFacilityId");
			
			String RETAIL="RETAIL";
			List l = new ArrayList();//存放查询条件
			if(productIdParameter!=null&&!"".equals(productIdParameter))
				l.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%"+productIdParameter+"%"));
			l.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, inventoryFacilityId));
			l.add(EntityCondition.makeCondition("recordDate", EntityOperator.EQUALS, nowDate));
			List<GenericValue> retlist2 = delegator.findByAnd("InventoryReport",l);
			for(int i=0;i<retlist2.size();i++)
			{
				GenericValue temp = retlist2.get(i);
				String productId = temp.getString("productId");
				String quantityOnHandTotal = temp.getString("quantityOnHandTotal");
				String unitCost = temp.getString("avgCost");
				String price = temp.getString("price");
				String description = temp.getString("description");
				String productname = temp.getString("productname");
				InventoryReportFormsResultJsonBean tempretbean = new InventoryReportFormsResultJsonBean();
				tempretbean.setProductId(productId);
				tempretbean.setQuantityOnHandTotal(quantityOnHandTotal);
				tempretbean.setUnitCost(unitCost);
				tempretbean.setPrice(price);
				tempretbean.setDescription(description);
				tempretbean.setProductname(productname);
				tempretbean.replenish();
				resultList.add(tempretbean);
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("LookupInventoryReportFormslist", resultList);
		return map;
	}
	
	
	public static List<GenericValue> chenckdata(List<GenericValue> retlist2)
	{
		List<GenericValue> qweq = new ArrayList<GenericValue>();
		Map<String,GenericValue> map=new HashMap<String,GenericValue>();
		for(int i=0;i<retlist2.size();i++)
		{
			GenericValue temp = retlist2.get(i);
			String productId = temp.getString("productId");
			if(map.containsKey(productId))
			{
				GenericValue temp2 = map.get(productId);
				String createdDate2 = temp2.getString("datetimeReceived");
				String createdDate1=  temp.getString("datetimeReceived");
				
				
				if(createdDate2!=null&&!"".equals(createdDate2)&&createdDate1!=null&&!"".equals(createdDate1))
				if(createdDate2.length()>createdDate1.length())
					map.put(productId, temp);
			}
			else 
			{
				String createdtemp=  temp.getString("datetimeReceived");
				if(createdtemp!=null)
					map.put(productId, temp);
			}
		}
		for (Map.Entry<String, GenericValue> entry : map.entrySet()) {
			 qweq.add(entry.getValue());
		}
		return qweq;
	}
	
	
	
	
}
