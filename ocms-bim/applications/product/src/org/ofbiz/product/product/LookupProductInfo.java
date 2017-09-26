package org.ofbiz.product.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.product.product.bean.FacilityResultJsonBean;
import org.ofbiz.product.product.bean.LookupProductResultJsonBean;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import org.ofbiz.product.product.ZipUtils;






public class LookupProductInfo {


	public static String newCreateProductPrice(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String productId = request.getParameter("productId");
		String productPriceTypeId = request.getParameter("productPriceTypeId");
		String productPricePurposeId = request.getParameter("productPricePurposeId");
		String currencyUomId = request.getParameter("currencyUomId");
		String productStoreGroupId = request.getParameter("productStoreGroupId");
		BigDecimal price = new BigDecimal(request.getParameter("price"));
		String termUomId = request.getParameter("termUomId");
		String customPriceCalcService = request.getParameter("customPriceCalcService");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		Timestamp fromDate = new Timestamp(new Date().getTime());
		request.setAttribute("fromDate", fromDate);
		try {
			TransactionUtil.begin();
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
			reqMap.put("price", price);
			reqMap.put("productId", productId);
			reqMap.put("productPriceTypeId", productPriceTypeId);
			reqMap.put("productPricePurposeId", productPricePurposeId);
			reqMap.put("currencyUomId", currencyUomId);
			reqMap.put("productStoreGroupId", productStoreGroupId);
			reqMap.put("termUomId", termUomId);
			reqMap.put("customPriceCalcService", customPriceCalcService);
			reqMap.put("fromDate", fromDate);
			 if (userLogin != null) {
				 reqMap.put("userLogin", userLogin);
		        }
			dispatcher.runSync("createProductPrice", reqMap);
			TransactionUtil.commit();
		} catch (Exception e) {
			try {
                TransactionUtil.rollback();
	         } catch (GenericTransactionException e2) {
	         request.setAttribute("_ERROR_MESSAGE_", e2.getMessage());
	         return "error";
	         }
			e.printStackTrace();
		}
		return null;
    }

	public static String findfeaturesByproductid(HttpServletRequest request, HttpServletResponse response) {
		/*<#assign itemBilling = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(itemBillings)>
		*/
		HttpSession session = request.getSession();
		String productId = request.getParameter("productId");
		if(productId==null)
			return "";
		String sql="select * from (SELECT b.product_id, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID ) as descr FROM PRODUCT_FEATURE a"+
				" LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID"+
				" GROUP BY product_id )  as ret where product_id=\'para\'";		
		sql=sql.replaceAll("para", productId);
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		try {
			ResultSet rs = processor.executeQuery(sql);
			while(rs.next()){
				String descr = rs.getString("descr");
				if(descr!=null)
					return descr;
				else 
					return "";
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
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		return "";
    }
	
	public static Map<String, Object> LookupWarehouseInf(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> facilitfile = UtilMisc.toMap();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		try {
			List<GenericValue> retlist = delegator.findByAnd("Facility",facilitfile);
			List<FacilityResultJsonBean> resultWarehouseInfoList = new ArrayList<FacilityResultJsonBean>();
			for (int i = 0; i < retlist.size(); i++) {
				GenericValue genericValue = retlist.get(i);
				FacilityResultJsonBean tempbean=new FacilityResultJsonBean();
				tempbean.setFacilityId(genericValue.getString("facilityId"));
				tempbean.setFacilityName(genericValue.getString("facilityName"));
				resultWarehouseInfoList.add(tempbean);
			}
			map.put("resultWarehouseInfoList", resultWarehouseInfoList);
			
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	
	
	public static List<String> LookupLevelnoe(SQLProcessor processor,String cagid) {
		List<String> retlist=new ArrayList<String>();
		String sql=" SELECT "
				+ " 	x.* "
				+ " FROM "
				+ "	PRODUCT_CATEGORY_ROLLUP x"
				+ " WHERE"
				+ " 	x.PARENT_PRODUCT_CATEGORY_ID IN ("
				+ " 		SELECT"
				+ "			b.PRODUCT_CATEGORY_ID"
				+ "		FROM"
				+ "			PRODUCT_CATEGORY a"
				+ "		RIGHT JOIN PRODUCT_CATEGORY_ROLLUP b ON a.PRODUCT_CATEGORY_ID = b.PARENT_PRODUCT_CATEGORY_ID"
				+ "		WHERE"
				+ "			a.PRODUCT_CATEGORY_ID = \'indexder\'"
				+ "	 )";
		sql=sql.replaceAll("indexder", cagid);
		ResultSet rs = null;
		try {
			rs = processor.executeQuery(sql);
			while(rs.next()){
				retlist.add(rs.getString("PRODUCT_CATEGORY_ID"));
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
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		return retlist;
	}
	
	public static List<String> LookupLeveltwo(SQLProcessor processor,String cagid) {
		List<String> retlist=new ArrayList<String>();
		String sql=" SELECT "
				+ " 	b.PRODUCT_CATEGORY_ID "
				+ " FROM "
				+ "	PRODUCT_CATEGORY a"
				+ " RIGHT JOIN PRODUCT_CATEGORY_ROLLUP b ON a.PRODUCT_CATEGORY_ID = b.PARENT_PRODUCT_CATEGORY_ID"
				+ " WHERE"
				+ " a.PRODUCT_CATEGORY_ID = 'indexder' ";
		sql=sql.replaceAll("indexder", cagid);
		ResultSet rs = null;
		try {
			rs = processor.executeQuery(sql);
			while(rs.next()){
				retlist.add(rs.getString("PRODUCT_CATEGORY_ID"));
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
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		return retlist;
	}
	
	public static Map<String, Object> LenovoSKU(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = ServiceUtil.returnSuccess(); //返回值map	
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<LookupProductResultJsonBean> resultPoductPriceList = new ArrayList<LookupProductResultJsonBean>();
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		try {
			String productId = request.getParameter("productId");
			
			String wwww="select * from "
					+ " ("
					+ " SELECT"
					+ " b.product_id,"
					+ " GROUP_CONCAT("
					+ " DISTINCT DESCRIPTION"
					+ " ORDER BY"
					+ " a.PRODUCT_FEATURE_TYPE_ID"
					+ " ) as descr"
					+ " FROM"
					+ " PRODUCT_FEATURE a"
					+ " LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID"
					+ " GROUP BY"
					+ " product_id"
					+ " ) as t1  left join PRODUCT_ASSOC as pa on t1.product_id=pa.PRODUCT_ID_TO"
					+ " where pa.PRODUCT_ID_TO like \'%"+productId+"%\'";
					
			
			ResultSet rs = null;
			rs = processor.executeQuery(wwww);
			try {
				int index=0;
				while(rs.next()){ 
					if(index++>10)
						break;
					LookupProductResultJsonBean tempinfo = new LookupProductResultJsonBean();

					tempinfo.setProductId(rs.getString("PRODUCT_ID_TO"));
					tempinfo.setDescription(rs.getString("descr"));
					tempinfo.replenish();
					resultPoductPriceList.add(tempinfo);
				}
			} catch (SQLException e) {
				return ServiceUtil.returnError("query fail");
			}
			map.put("resultPoductPriceList", resultPoductPriceList);
			
			
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("query fail");
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		return map;

	}
	
	
	public static Map<String, Object> LookupProductInf(HttpServletRequest request, HttpServletResponse response) {
		long t1 = System.currentTimeMillis();
		Map<String, Object> map = ServiceUtil.returnSuccess(); //返回值map	
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<LookupProductResultJsonBean> resultPoductPriceList = new ArrayList<LookupProductResultJsonBean>();
		
		GenericValue userLogin = (GenericValue)request.getSession().getAttribute("userLogin");
		String TimeZonestring = userLogin.getString("lastTimeZone");
		if(TimeZonestring==null||"".equals(TimeZonestring))
			TimeZonestring="Africa/Kampala";
		TimeZone timeZones = TimeZone.getTimeZone(TimeZonestring);
		
		
		int totalnumber=0;
		int evepage=30;
		//新增是否启用条件
		String isActive = request.getParameter("isActive");
		String isActiveSql;
		if ("N".equals(isActive)){
			isActiveSql = " AND pr.IS_ACTIVE = 'N' ";
		}else{
			isActiveSql = " AND (pr.IS_ACTIVE = 'Y' OR pr.IS_ACTIVE IS NULL)";
		}
		
		
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		try {
			String productId = request.getParameter("productId");
			String productTypeId = request.getParameter("productTypeId");
			
			String ean = request.getParameter("EAN");
			
			String productCategoryId1 = request.getParameter("productCategoryId1");
			String productCategoryId2 = request.getParameter("productCategoryId2");
			String productCategoryId = request.getParameter("productCategoryId");
			
			String pagestr = request.getParameter("page");
			int page;
			try {
				page = Integer.valueOf(pagestr);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				page=1;
			}
			
			
			List<String> catelist=new ArrayList<String>();;
			if(productCategoryId1==null||"".equals(productCategoryId1))
				catelist=new ArrayList<String>();
			else if(productCategoryId2==null||"".equals(productCategoryId2))
				catelist=LookupLevelnoe(processor,productCategoryId1);
			else if(productCategoryId==null||"".equals(productCategoryId))
				catelist=LookupLeveltwo(processor,productCategoryId2);
			else 
			{
				catelist=new ArrayList<String>();
				catelist.add(productCategoryId);
			}
			String internalName = request.getParameter("internalName");
	
			String begintime = request.getParameter("begintime");
			String endtime = request.getParameter("endtime");
			
			
			String wwww="	select proret.* "
			+ " ,(SELECT PRODUCT_PRICE.PRICE from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_ID=proret.PRODUCT_ID1 and  PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID='DEFAULT_COST_PRICE' group by PRODUCT_PRICE.PRODUCT_ID ) as price"
			+ " ,(SELECT PRODUCT_PRICE.CURRENCY_UOM_ID from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_ID=proret.PRODUCT_ID1 and  PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID='DEFAULT_COST_PRICE' group by PRODUCT_PRICE.PRODUCT_ID)  as uomid "
			+ " ,(SELECT PRODUCT_ID from PRODUCT_ASSOC where PRODUCT_ASSOC.PRODUCT_ID_TO=proret.PRODUCT_ID1 group by PRODUCT_ASSOC.PRODUCT_ID_TO) as pr"
			+ " from "
			+"	("
			+"	select * from"
			+"	("
			+"		select "
			+"		pr.PRODUCT_ID as PRODUCT_ID1,pa.PRODUCT_ID as PRODUCT_ID2,pa.PRODUCT_ID_TO,pc.PRODUCT_CATEGORY_ID,pa.PRODUCT_ASSOC_TYPE_ID,"
			+"		pr.BRAND_NAME,pr.PRODUCT_NAME,pro.CATEGORY_NAME ,profed.DESCRIPTION"
			+"		from "
			+"			PRODUCT as pr "
			+"			join (select * from PRODUCT_CATEGORY_MEMBER as pcm group by pcm.PRODUCT_CATEGORY_ID,pcm.PRODUCT_ID)as pc on pr.PRODUCT_ID=pc.PRODUCT_ID  "
			+"			join PRODUCT_CATEGORY as pro on pro.PRODUCT_CATEGORY_ID=pc.PRODUCT_CATEGORY_ID "
			+"			left join PRODUCT_ASSOC as pa on pa.PRODUCT_ID=pr.PRODUCT_ID "
			+"			left join "
			+"				( "
			+"				 select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
			+"				 ("
			+"				 	SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID "
			+"				 	FROM"
			+"				 	PRODUCT_FEATURE_APPL c "
			+"					LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
			+"				 ) as  fea"
			+"				 group by PRODUCT_ID"
			+"				 ) as profed on profed.PRODUCT_ID=pr.PRODUCT_ID"
			+"			where "
			+"			pr.PRODUCT_ID like \'%PRODUCT_ID_index%\'" + isActiveSql
			+"  inserttheconditions "
			+"	)  as ret where ret.PRODUCT_ASSOC_TYPE_ID=\'PRODUCT_VARIANT\' or ret.PRODUCT_ASSOC_TYPE_ID is null"
			+"	) as proret "
			+" GROUP BY PRODUCT_ID1,PRODUCT_CATEGORY_ID";
			
			wwww=wwww.replaceAll("PRODUCT_ID_index", productId);
			StringBuffer inserttheconditions = new StringBuffer(""); 
			maketimeCondition(inserttheconditions,"pr.CREATED_DATE",begintime,endtime,timeZones.getRawOffset());
			makeCondition(inserttheconditions,"pr.PRODUCT_TYPE_ID",productTypeId);
			//makeCondition(inserttheconditions,"pr.EAN",ean);
			makeConditionList(inserttheconditions,"pc.PRODUCT_CATEGORY_ID",catelist);
			makeConditionlike(inserttheconditions,"pr.PRODUCT_NAME",internalName);
			wwww=wwww.replaceAll("inserttheconditions", inserttheconditions.toString());
			
			
			//增加EAN码查询
			if(ean!=null&&!"".equals(ean))
			{
				wwww=" select wewqeq.*,gi.ID_VALUE as ean from ( " +wwww +" ) as wewqeq left join GOOD_IDENTIFICATION as gi on wewqeq.PRODUCT_ID1=gi.PRODUCT_ID where gi.GOOD_IDENTIFICATION_TYPE_ID=\'EAN\' and gi.ID_VALUE=\'"+ean+"\'";
			}
			
			
			ResultSet rs = null;
			rs = processor.executeQuery(wwww);
			int index=-1;
			try {
				while(rs.next()){
					LookupProductResultJsonBean tempinfo = new LookupProductResultJsonBean();
					
					if(rs.getString("PRODUCT_ID_TO")!=null)//已经变形，抛弃
						continue;
					index++;
					if(index<page*evepage&&index>=(page-1)*evepage)
					{
						tempinfo.setProductId(rs.getString("PRODUCT_ID1"));
						tempinfo.setProductCategoryId(rs.getString("CATEGORY_NAME"));
						tempinfo.setBrandName(rs.getString("BRAND_NAME"));
						tempinfo.setInternalName(rs.getString("PRODUCT_NAME"));
						tempinfo.setDescription(rs.getString("DESCRIPTION"));
						tempinfo.setCost(rs.getString("price"));
						tempinfo.setCurrencyUomId(rs.getString("uomid"));
						tempinfo.setSPU(rs.getString("pr"));
						if(tempinfo.getSPU()==null)
							tempinfo.setSPU(rs.getString("PRODUCT_ID1"));
						tempinfo.replenish();
						resultPoductPriceList.add(tempinfo);
					}
				}
			} catch (SQLException e) {
				return ServiceUtil.returnError("query fail");
			}
			totalnumber=index+1;
			map.put("totalnumber", totalnumber);
			map.put("evepage", evepage);
			map.put("currentpage", page);
			map.put("resultPoductPriceList", resultPoductPriceList);
			
			
		} catch (GenericEntityException e) {
			return ServiceUtil.returnError("query fail");
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		return map;

	}
	
	 
	

	public static String Exportproductlist(HttpServletRequest request, HttpServletResponse response) {
		long t1 = System.currentTimeMillis();
		Map<String, Object> map = ServiceUtil.returnSuccess(); //返回值map	
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<LookupProductResultJsonBean> resultPoductPriceList = new ArrayList<LookupProductResultJsonBean>();
		
		GenericValue userLogin = (GenericValue)request.getSession().getAttribute("userLogin");
		String TimeZonestring = userLogin.getString("lastTimeZone");
		if(TimeZonestring==null||"".equals(TimeZonestring))
			TimeZonestring="Africa/Kampala";
		TimeZone timeZones = TimeZone.getTimeZone(TimeZonestring);
		
		
		
		int totalnumber=0;
		int evepage=30;
		//新增是否启用条件
		String isActive = request.getParameter("isActive");
		String isActiveSql;
		if ("N".equals(isActive)){
			isActiveSql = " AND pr.IS_ACTIVE = 'N' ";
		}else{
			isActiveSql = " AND (pr.IS_ACTIVE = 'Y' OR pr.IS_ACTIVE IS NULL)";
		}
		
		
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		try {
			String productId = request.getParameter("productId");
			String productTypeId = request.getParameter("productTypeId");
			
			String ean = request.getParameter("EAN");
			
			String productCategoryId1 = request.getParameter("productCategoryId1");
			String productCategoryId2 = request.getParameter("productCategoryId2");
			String productCategoryId = request.getParameter("productCategoryId");
			
			String pagestr = request.getParameter("page");
			int page;
			try {
				page = Integer.valueOf(pagestr);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				page=1;
			}
			
			
			List<String> catelist=new ArrayList<String>();;
			if(productCategoryId1==null||"".equals(productCategoryId1))
				catelist=new ArrayList<String>();
			else if(productCategoryId2==null||"".equals(productCategoryId2))
				catelist=LookupLevelnoe(processor,productCategoryId1);
			else if(productCategoryId==null||"".equals(productCategoryId))
				catelist=LookupLeveltwo(processor,productCategoryId2);
			else 
			{
				catelist=new ArrayList<String>();
				catelist.add(productCategoryId);
			}
			String internalName = request.getParameter("internalName");
	
			String begintime = request.getParameter("begintime");
			String endtime = request.getParameter("endtime");
			
			
			String wwww="	select proret.* "
			+ " ,(SELECT PRODUCT_PRICE.PRICE from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_ID=proret.PRODUCT_ID1 and  PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID='DEFAULT_COST_PRICE' group by PRODUCT_PRICE.PRODUCT_ID ) as price"
			+ " ,(SELECT PRODUCT_PRICE.CURRENCY_UOM_ID from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_ID=proret.PRODUCT_ID1 and  PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID='DEFAULT_COST_PRICE' group by PRODUCT_PRICE.PRODUCT_ID)  as uomid "
			+ " ,(SELECT PRODUCT_ID from PRODUCT_ASSOC where PRODUCT_ASSOC.PRODUCT_ID_TO=proret.PRODUCT_ID1 group by PRODUCT_ASSOC.PRODUCT_ID_TO) as pr"
			+ " from "
			+"	("
			+"	select * from"
			+"	("
			+"		select "
			+"		pr.PRODUCT_ID as PRODUCT_ID1,pa.PRODUCT_ID as PRODUCT_ID2,pa.PRODUCT_ID_TO,pc.PRODUCT_CATEGORY_ID,pa.PRODUCT_ASSOC_TYPE_ID,"
			+"		pr.BRAND_NAME,pr.PRODUCT_NAME,pro.CATEGORY_NAME ,profed.DESCRIPTION"
			+"		from "
			+"			PRODUCT as pr "
			+"			join (select * from PRODUCT_CATEGORY_MEMBER as pcm group by pcm.PRODUCT_CATEGORY_ID,pcm.PRODUCT_ID)as pc on pr.PRODUCT_ID=pc.PRODUCT_ID  "
			+"			join PRODUCT_CATEGORY as pro on pro.PRODUCT_CATEGORY_ID=pc.PRODUCT_CATEGORY_ID "
			+"			left join PRODUCT_ASSOC as pa on pa.PRODUCT_ID=pr.PRODUCT_ID "
			+"			left join "
			+"				( "
			+"				 select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
			+"				 ("
			+"				 	SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID "
			+"				 	FROM"
			+"				 	PRODUCT_FEATURE_APPL c "
			+"					LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
			+"				 ) as  fea"
			+"				 group by PRODUCT_ID"
			+"				 ) as profed on profed.PRODUCT_ID=pr.PRODUCT_ID"
			+"			where "
			+"			pr.PRODUCT_ID like \'%PRODUCT_ID_index%\'" + isActiveSql
			+"  inserttheconditions "
			+"	)  as ret where ret.PRODUCT_ASSOC_TYPE_ID=\'PRODUCT_VARIANT\' or ret.PRODUCT_ASSOC_TYPE_ID is null"
			+"	) as proret "
			+" GROUP BY PRODUCT_ID1,PRODUCT_CATEGORY_ID";
			
			wwww=wwww.replaceAll("PRODUCT_ID_index", productId);
			StringBuffer inserttheconditions = new StringBuffer(""); 
			maketimeCondition(inserttheconditions,"pr.CREATED_DATE",begintime,endtime,timeZones.getRawOffset());
			makeCondition(inserttheconditions,"pr.PRODUCT_TYPE_ID",productTypeId);
			//makeCondition(inserttheconditions,"pr.EAN",ean);
			makeConditionList(inserttheconditions,"pc.PRODUCT_CATEGORY_ID",catelist);
			makeConditionlike(inserttheconditions,"pr.PRODUCT_NAME",internalName);
			wwww=wwww.replaceAll("inserttheconditions", inserttheconditions.toString());
			
			//增加EAN码查询
			if(ean!=null&&!"".equals(ean))
			{
				wwww=" select wewqeq.*,gi.ID_VALUE as ean from ( " +wwww +" ) as wewqeq left join GOOD_IDENTIFICATION as gi on wewqeq.PRODUCT_ID1=gi.PRODUCT_ID where gi.GOOD_IDENTIFICATION_TYPE_ID=\'EAN\' and gi.ID_VALUE=\'"+ean+"\'";
			}
			
			
			
			ResultSet rs = null;
			rs = processor.executeQuery(wwww);
			int index=-1;
			try {
				while(rs.next()){
					LookupProductResultJsonBean tempinfo = new LookupProductResultJsonBean();
					
					if(rs.getString("PRODUCT_ID_TO")!=null)//已经变形，抛弃
						continue;
					tempinfo.setProductId(rs.getString("PRODUCT_ID1"));
					tempinfo.setProductCategoryId(rs.getString("CATEGORY_NAME"));
					tempinfo.setBrandName(rs.getString("BRAND_NAME"));
					tempinfo.setInternalName(rs.getString("PRODUCT_NAME"));
					tempinfo.setDescription(rs.getString("DESCRIPTION"));
					tempinfo.setCost(rs.getString("price"));
					tempinfo.setCurrencyUomId(rs.getString("uomid"));
					tempinfo.setSPU(rs.getString("pr"));
					if (tempinfo.getSPU() == null)
						tempinfo.setSPU(rs.getString("PRODUCT_ID1"));
					tempinfo.replenish();
					resultPoductPriceList.add(tempinfo);
				}
			} catch (SQLException e) {
				return "success";
			}
			totalnumber=index+1;
			map.put("totalnumber", totalnumber);
			map.put("evepage", evepage);
			map.put("currentpage", page);
			map.put("resultPoductPriceList", resultPoductPriceList);
			
			
		} catch (GenericEntityException e) {
			return "success";
		}
		
		
		
		List<String> dataList = new ArrayList<String>();
		dataList.add("No.,SKU,Model,Description,Category,Brand,Currency,Cost");
		for(int j=0;j<resultPoductPriceList.size();j++)
		{
			LookupProductResultJsonBean tempobjet = resultPoductPriceList.get(j);
			
        	String description = tempobjet.getDescription();
        	if(description == null){
        		description = "";
        	}else{
        		description = description.replace("\r", "");
        		description = description.replace(",", " ");
        	}
			dataList.add((j+1)+","+tempobjet.getProductId()+","+tempobjet.getInternalName()+","+description+","+tempobjet.getProductCategoryId()+","+tempobjet.getBrandName()+","+tempobjet.getCurrencyUomId()+","+tempobjet.getCost());
		}
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//设置日期格式
        SimpleDateFormat filedf = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		
      //yzl20170711导出的csv文件进行压缩成izp
        response.addHeader("Content-Disposition", "attachment; filename=Product_List"+df.format(new Date())+".zip");
		
        String retmaptemp = getTempFilepath();
        File  tempfilefolder= new File(retmaptemp);
        File  tempfile= new File(retmaptemp+"/Product_List"+filedf.format(new Date())+".csv");
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
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
        return "success";
        //end

	}
	
//
//	Map<String, String> facilitfile = UtilMisc.toMap();
//	facilitfile.put("facilityName", facilityname);
//	List<GenericValue> retlist = delegator.findByAnd("Facility",facilitfile);
//	String facilityId="";
//	if(retlist.size()>0)
//		facilityId=retlist.get(0).getString("facilityId");
//
//
//	String dateStart = "2004-03-25 00:00:00";
//	String dateEnd   = "2004-03-26 00:00:00";
//	
//	    
//	List l = new ArrayList();//存放查询条件
//	
//	
//	
//	
//	if(begintime!=null&&!"".equals(begintime))
//		l.add(new EntityExpr("createdDate",EntityOperator.GREATER_THAN,begintime));
//	if(endtime!=null&&!"".equals(endtime))
//		l.add(new EntityExpr("createdDate",EntityOperator.LESS_THAN,endtime));
//	if(productId!=null&&!"".equals(productId))
//		l.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%"+productId+"%"));
//	if(productTypeId!=null&&!"".equals(productTypeId))
//		l.add(EntityCondition.makeCondition("productTypeId", EntityOperator.LIKE, "%"+productTypeId+"%"));
//	if(productCategoryId!=null&&!"".equals(productCategoryId))
//		l.add(EntityCondition.makeCondition("productCategoryId", EntityOperator.EQUALS, productCategoryId));
//	if(internalName!=null&&!"".equals(internalName))
//		l.add(EntityCondition.makeCondition("productName", EntityOperator.LIKE, "%"+internalName+"%"));
//	if(facilityId!=null&&!"".equals(facilityId))
//		l.add(EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
//	if(brandname!=null&&!"".equals(brandname))
//		l.add(EntityCondition.makeCondition("brandName", EntityOperator.LIKE, "%"+brandname+"%"));
//
//	
////	List<GenericValue> productPriceList111=  delegator.findByAnd("Product",l);
////	for (int i = 0; i < productPriceList111.size(); i++) {
////		GenericValue temp = productPriceList111.get(i);
////		System.out.println(temp);
////	}
//	
////	List l1 = new ArrayList();//存放查询条件
////		
////	l1.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%GZ-1000%"));
////	l1.add(EntityCondition.makeCondition("productCategoryId", EntityOperator.LIKE, "%100%"));
//	
//	List<GenericValue> productPriceList=  delegator.findByAnd("LookUpProductByCategory",l);
//	List<GenericValue> productPriceList2=  delegator.findByAnd("Product",l);
//	
//	//EntityConditionList condition = EntityCondition.makeCondition(UtilMisc.toList(l), EntityOperator.LIKE);
//	//List<GenericValue> productPriceList= delegator.findList("Product",condition , null, null, null, false);
//	
//	for (int i = 0; i < productPriceList.size(); i++) {
//		GenericValue genericValue = productPriceList.get(i);
//		LookupProductResultJsonBean tempinfo = new LookupProductResultJsonBean();
//		tempinfo.setProductId(genericValue.getString("productId"));
//		tempinfo.setProductTypeId(genericValue.getString("productTypeId"));
//		tempinfo.setProductCategoryId(genericValue.getString("productCategoryId"));
//		tempinfo.setProductCategoryId(genericValue.getString("categoryName"));
//		
//		tempinfo.setOriginGeoId(genericValue.getString("originGeoId"));
//		tempinfo.setBrandName((genericValue.getString("brandName")));
//		tempinfo.setInternalName(genericValue.getString("productName"));
//		tempinfo.replenish();
//		//resultPoductPriceList.add(tempinfo);
//	}
//	String ju1=JSON.toJSONString(resultPoductPriceList);
	
	
	
	
//	public static String newUpdateProductPrice(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			HttpSession session = request.getSession();
//			String productId = request.getParameter("productId");
//			String productPriceTypeId = request.getParameter("productPriceTypeId");
//			String productPricePurposeId = request.getParameter("productPricePurposeId");
//			String currencyUomId = request.getParameter("currencyUomId");
//			String productStoreGroupId = request.getParameter("productStoreGroupId");
//			BigDecimal price = new BigDecimal(request.getParameter("price"));
//			String termUomId = request.getParameter("termUomId");
//			String customPriceCalcService = request.getParameter("customPriceCalcService");
//			Timestamp fromDate = new Timestamp(Long.valueOf(request.getParameter("fromDate")));
//			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//			Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
//			reqMap.put("price", price);
//			reqMap.put("productId", productId);
//			reqMap.put("productPriceTypeId", productPriceTypeId);
//			reqMap.put("productPricePurposeId", productPricePurposeId);
//			reqMap.put("currencyUomId", currencyUomId);
//			reqMap.put("productStoreGroupId", productStoreGroupId);
//			reqMap.put("termUomId", termUomId);
//			reqMap.put("customPriceCalcService", customPriceCalcService);
//			reqMap.put("fromDate", fromDate);
//			if (userLogin != null) {
//				 reqMap.put("userLogin", userLogin);
//		        }
//			dispatcher.runAsync("updateProductPrice", reqMap);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//    }
//	
	
	public static void makeConditionlike(StringBuffer sql,String key,String value){
		if (value != null && !"".equals(value)) {
			sql.append(" and " + key + " like" + "\'%" + value + "%\'");
		}
	}
	
	
	public static void makeCondition(StringBuffer sql,String key,String value){
		if (value != null && !"".equals(value)) {
			sql.append(" and " + key + "=" + "\'" + value + "\'");
		}
	}
	public static void maketimeCondition(StringBuffer sql,String key,String begintime,String endtime,long jetlag){
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		Date begindate = null;
		Date enddate = null;
		if (begintime != null &&!"".equals(begintime)) {
			try {
				begindate = format.parse(begintime);
				if (begindate != null) {
					begindate.setTime(begindate.getTime() - jetlag);
					sql.append(" and " + key + ">=" + "\'" + format.format(begindate) + "\'");
				}else 
				{
					sql.append(" and " + key + ">=" + "\'" + begindate + "\'");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		
		
		if (endtime != null &&!"".equals(endtime)) {
			try {
				enddate = format.parse(endtime);
				if (enddate != null) {
					enddate.setTime(enddate.getTime() - jetlag);
					sql.append(" and " + key + "<=" + "\'" + format.format(enddate) + "\'");
				} else 
				{
					sql.append(" and " + key + "<=" + "\'" + enddate + "\'");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void makeConditionList(StringBuffer sql,String key,List<String> value){
		for(int i=0;i<value.size();i++)
		{
			if(i==0)
				sql.append(" and (" + key + "=" + "\'" + value.get(i) + "\'");
			else 
				sql.append(" or " + key + "=" + "\'" + value.get(i) + "\'");
			if(i==value.size()-1)
				sql.append(" )");
		}
	}

	public static Map<String, Object> getSystemSettings() {
		Map<String, Object> retmap=new HashMap<String, Object>();
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key);
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		String tempfilepath = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsTempFilePath");
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId);
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		retmap.put("tempfilepath", tempfilepath);
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
