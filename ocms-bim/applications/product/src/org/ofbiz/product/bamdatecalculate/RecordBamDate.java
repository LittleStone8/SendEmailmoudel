package org.ofbiz.product.bamdatecalculate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;

public class RecordBamDate {
//	public static Map<String, Object> GH_RecordBamDate(DispatchContext dctx, Map<String, ? extends Object> context) {
		public static Map<String, Object> UG_RecordBamDate(HttpServletRequest request, HttpServletResponse response) {
		
		String county = (String) getSystemSettings().get("countryId");
		if (!"UGA".equals(county)) {
			Map<String, Object> retmap = new HashMap<String, Object>();
			return retmap;
		}
			
		
	//	Delegator delegator = dctx.getDelegator();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		
		Map<String ,BimOrdersRo> totalordermap=new HashMap<String ,BimOrdersRo>();
		
		StringBuffer sql_total = new StringBuffer("SELECT "
				+ " sum(GRAND_TOTAL) AS total_goods_sum,"
				+ " count(oh.GRAND_TOTAL) AS total_valid_order,"
				+ " date(oh.CREATED_STAMP) AS date,"
				+ " sum(oi.QUANTITY) AS total_goods_count"
				+ " FROM"
				+ " ORDER_HEADER AS oh"
				+ " LEFT JOIN ORDER_ITEM AS oi ON oi.ORDER_ID = oh.ORDER_ID"
				+ " GROUP BY"
				+ " date(oh.CREATED_STAMP) DESC"); 
		StringBuffer inser_total_sql=new StringBuffer("INSERT INTO bim_orders(country,date,valid_order,goods_count,goods_sum,created_at,updated_at) VALUES ");
		boolean frist = true;
		try {
			ResultSet rs = processor.executeQuery(sql_total.toString());
			while (rs.next()) {
				BimOrdersRo tempBimOrdersRo = new BimOrdersRo();
				String total_goods_sum = rs.getString("total_goods_sum");
				String total_valid_order = rs.getString("total_valid_order");
				String total_goods_count = rs.getString("total_goods_count");
				String date = rs.getString("date");

				tempBimOrdersRo.setCountry(county);
				tempBimOrdersRo.setDate(date);
				tempBimOrdersRo.setTotal_goods_count(total_goods_count);
				tempBimOrdersRo.setTotal_goods_sum(total_goods_sum);
				tempBimOrdersRo.setTotal_valid_order(total_valid_order);
				tempBimOrdersRo.setCreated_at(new Date(System.currentTimeMillis()));
				tempBimOrdersRo.setUpdated_at(new Date(System.currentTimeMillis()));

				if (frist) {
					inser_total_sql.append(tempBimOrdersRo.toinsertsql());
					frist = false;
				} else {
					inser_total_sql.append(" ," + tempBimOrdersRo.toinsertsql());
				}

				totalordermap.put(date, tempBimOrdersRo);
			}
			processor.prepareStatement(inser_total_sql.toString());
			processor.executeUpdate();

		} catch (GenericDataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		frist=true;
		StringBuffer insersql=new StringBuffer("INSERT INTO bim_order_shops(country,date,shop_id,shop_name,valid_order,valid_order_percent,goods_count,goods_count_percent,goods_sum,goods_sum_percent,created_at,updated_at) VALUES ");
		
		
		StringBuffer sql_eve = new StringBuffer("SELECT ret.*, ps.STORE_NAME AS shop_name FROM ( SELECT sum(GRAND_TOTAL) AS goods_sum,count(oh.GRAND_TOTAL) AS valid_order,"
				+ " date(oh.CREATED_STAMP) AS date,oh.PRODUCT_STORE_ID,sum(oi.QUANTITY) as goods_count "
				+ " FROM "
				+ " ORDER_HEADER AS oh LEFT JOIN ORDER_ITEM as oi on oi.ORDER_ID=oh.ORDER_ID"
				+ " GROUP BY"
				+ " date(oh.CREATED_STAMP) DESC,"
				+ " oh.PRODUCT_STORE_ID"
				+ " ) AS ret"
				+ " LEFT JOIN PRODUCT_STORE AS ps ON ps.PRODUCT_STORE_ID = ret.PRODUCT_STORE_ID"); 
		try {
			ResultSet rs2 = processor.executeQuery(sql_eve.toString());
			while (rs2.next()) {
				
				BimOrdersShopRo tempBimOrdersShopRo=new BimOrdersShopRo();
				String goods_sum = checkvalue(rs2.getString("goods_sum"));
				String valid_order = checkvalue(rs2.getString("valid_order"));
				String goods_count = checkvalue(rs2.getString("goods_count"));
				String date = rs2.getString("date");
				String shop_name = rs2.getString("shop_name");
				String shop_id = rs2.getString("PRODUCT_STORE_ID");
				
				BimOrdersRo totaltemp = totalordermap.get(date);
				String total_goods_count = totaltemp.getTotal_goods_count();
				String total_goods_sum = totaltemp.getTotal_goods_sum();
				String total_valid_order = totaltemp.getTotal_valid_order();
				double goods_count_percent=0.0;
				double goods_sum_percent=0.0;
				double valid_order_percent=0.0;
				try {
					if(goods_count==null||total_goods_count==null||"".equals(goods_count)||"".equals(total_goods_count))
					{
						goods_count_percent=0.0;
					}
					else 
					{
						goods_count_percent = Double.valueOf(Double.valueOf(goods_count)/Double.valueOf(total_goods_count));
					}
				} catch (NumberFormatException e) {
					goods_count_percent = 0.0;
				}
				try {
					if(goods_sum==null||total_goods_sum==null||"".equals(goods_sum)||"".equals(total_goods_sum))
					{
						goods_sum_percent=0.0;
					}
					else 
					{
						goods_sum_percent = Double.valueOf(Double.valueOf(goods_sum)/Double.valueOf(total_goods_sum));
					}
				} catch (NumberFormatException e) {
					goods_sum_percent = 0.0;
				}
				try {
					if(valid_order==null||total_valid_order==null||"".equals(valid_order)||"".equals(total_valid_order))
					{
						valid_order_percent=0.0;
					}
					else 
					{
						valid_order_percent = Double.valueOf(Double.valueOf(valid_order)/Double.valueOf(total_valid_order));
					}
				} catch (NumberFormatException e) {
					valid_order_percent = 0.0;
				}
				tempBimOrdersShopRo.setCountry(county);
				tempBimOrdersShopRo.setDate(date);
				tempBimOrdersShopRo.setGoods_count(goods_count);
				tempBimOrdersShopRo.setGoods_count_percent(goods_count_percent+"");
				tempBimOrdersShopRo.setGoods_sum(goods_sum);
				tempBimOrdersShopRo.setGoods_sum_percent(goods_sum_percent+"");
				tempBimOrdersShopRo.setValid_order(valid_order);
				tempBimOrdersShopRo.setValid_order_percent(valid_order_percent+"");
				tempBimOrdersShopRo.setShop_name(shop_name);
				tempBimOrdersShopRo.setShop_id(shop_id);
				tempBimOrdersShopRo.setUpdated_at(new Date(System.currentTimeMillis()));
				tempBimOrdersShopRo.setCreated_at(new Date(System.currentTimeMillis()));
				
				if (frist) {
					insersql.append(tempBimOrdersShopRo.toinsertsql());
					frist = false;
				} else {
					insersql.append(" ," + tempBimOrdersShopRo.toinsertsql());
				}
				
			}
			processor.prepareStatement(insersql.toString());
			processor.executeUpdate();
			
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
		//
		String sql="UPDATE SEQUENCE_VALUE_ITEM SET SEQ_ID = \'0\' WHERE SEQ_NAME = \'TrackingCode\'";
		try {
			processor.prepareStatement(sql);
			processor.executeUpdate();
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}
		
		
		Map<String, Object> retmap=new HashMap<String, Object>();
		return retmap;
	}
//
//	public static Map<String, Object> UG_RegularRecords(DispatchContext dctx, Map<String, ? extends Object> context) {
//		
//		String county = (String)BaseReportEvent.getSystemSettings().get("countryId");
//		if (!"UGA".equals(county)) {
//			Map<String, Object> retmap = new HashMap<String, Object>();
//			return retmap;
//		}
//		
//		Delegator delegator = dctx.getDelegator();
//		List<RecordInventoryBean> toBeStored = new ArrayList<RecordInventoryBean>();
//		long current=System.currentTimeMillis();//当前时间毫秒数  
//		//long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-1000*3600*24;//昨天零点零分零秒的毫秒数
//		long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();//!!!!!!!!!!!!
//		Timestamp nowDate = new Timestamp(zero);
//		long index=0;
//		List<GenericValue> retlist = null;
//		List l = new ArrayList();//存放查询条件
//		l.add(EntityCondition.makeCondition("recordDate", EntityOperator.EQUALS, nowDate));
//		try {
//			retlist = delegator.findByAnd("InventoryReport",l);
//		} catch (GenericEntityException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		if(retlist==null||retlist.size()>0)//当天的已经存过了
//		{
//			Map<String, Object> retmap=new HashMap<String, Object>();
//			return retmap;
//		}
//		
//		//包装参数
//		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
//		SQLProcessor processor = new SQLProcessor(helperInfo);
//		
//		String RETAIL="RETAIL";//销售
//		String DEFAULT_COST_PRICE="DEFAULT_COST_PRICE";
//		String SPECIAL_COST_PRICE="SPECIAL_COST_PRICE";
//		StringBuffer DEFAULT_COST_PRICE_sql = new StringBuffer("select proret.*,pp.PRICE as cost,pp.PRODUCT_STORE_GROUP_ID,(select ppr.PRICE from PRODUCT_PRICE as ppr where ppr.PRODUCT_ID=proret.PRODUCT_ID and PRODUCT_STORE_GROUP_ID='RETAIL') as RETAILPRICE   from"
//				+" ("
//				+" select iret.*,pr.PRODUCT_NAME,pr.BRAND_NAME,profed.DESCRIPTION from "
//				+" PRODUCT as pr "
//				+" join "
//				+" ( " 
//				+" select  ii.PRODUCT_ID,ii.FACILITY_ID,fc.FACILITY_NAME,SUM(ii.QUANTITY_ON_HAND_TOTAL) as sum "
//				+" from  "
//				+" INVENTORY_ITEM as ii join FACILITY as fc on ii.FACILITY_ID=fc.FACILITY_ID where ii.STATUS_ID='IXF_COMPLETE' or  ii.STATUS_ID='INV_AVAILABLE' or ii.STATUS_ID is null group by ii.PRODUCT_ID,ii.FACILITY_ID "
//				+" ) as iret on iret.PRODUCT_ID=pr.PRODUCT_ID	"					
//				+"	left join "
//				+" ("
//				+"		select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
//				+"		( "
//				+"		SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID" 
//				+"		FROM "
//				+" 		PRODUCT_FEATURE_APPL c " 
//				+" 		LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
//				+" 			) as  fea "
//				+" 		group by PRODUCT_ID"
//				+" 		) as profed on profed.PRODUCT_ID=iret.PRODUCT_ID"
//				+" 		)as proret"
//				+" 		left join (select * from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'DEFAULT_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID is null or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'SPECIAL_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'EGATEE_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'RETAIL_COST_PRICE\' )  as pp on pp.PRODUCT_ID=proret.PRODUCT_ID"
//				+ " ORDER BY PRODUCT_ID,FACILITY_ID"); 
//		try {
//			ResultSet rs = processor.executeQuery(DEFAULT_COST_PRICE_sql.toString());
//			int num=0;
//			
//			long temptimestamp = System.currentTimeMillis();
//			while (rs.next()) {
//
//				RecordInventoryBean temp=new RecordInventoryBean();
//				temp.setProductId(rs.getString("PRODUCT_ID"));
//				temp.setProductStoreGroupId(rs.getString("PRODUCT_STORE_GROUP_ID"));
//				temp.setFacilityId(rs.getString("FACILITY_ID"));
//				temp.setFacilityName(rs.getString("FACILITY_NAME"));
//				temp.setRecordDate(nowDate);
//				temp.setRecortimestamp(temptimestamp+"");
//				String qty = rs.getString("sum");
//				double qtynum=0;
//				if (qty != null) {
//					try {
//						qtynum = Double.valueOf(qty);
//					} catch (NumberFormatException e) {
//						qtynum = 0;
//					}
//				}
//				String costcost=rs.getString("cost");
//				double costcostnum=0;
//				if (costcost != null) {
//					try {
//						costcostnum = Double.valueOf(costcost);
//					} catch (NumberFormatException e) {
//						costcostnum = 0;
//					}
//				}
//				String retaipr=rs.getString("RETAILPRICE");
//				double retaiprnum = 0;
//				if (retaipr != null) {
//					try {
//						retaiprnum = Double.valueOf(retaipr);
//					} catch (NumberFormatException e) {
//						// TODO Auto-generated catch block
//						retaiprnum = 0;
//					}
//				}
//				temp.setQuantityOnHandTotal(qtynum);
//				temp.setAvgCost( costcostnum);
//				temp.setDescription( rs.getString("DESCRIPTION"));
//				temp.setProductname(rs.getString("PRODUCT_NAME"));
//				temp.setBrandName(rs.getString("BRAND_NAME"));
//				temp.setPrice(retaiprnum);
//				String t=(index++)+"";
//				temp.setIndex(t);
//				toBeStored.add(temp);
//			}
//			
//			
//			
//			StringBuffer insersql=new StringBuffer("INSERT INTO INVENTORY_REPORT(PRODUCT_ID,FACILITY_ID,PRODUCT_STORE_GROUP_ID,INDEXW,RECORD_DATE,QUANTITY_ON_HAND_TOTAL,AVG_COST,PRICE,DESCRIPTION,PRODUCTNAME,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP,FACILITY_NAME,BRAND_NAME) VALUES ");
//			for(int k=0;k<toBeStored.size();k++)
//			{
//				 RecordInventoryBean tempdate = toBeStored.get(k);
//				 if(k==0)
//					 insersql.append(tempdate.toinsertsql());
//				 else 
//					 insersql.append(" ,"+tempdate.toinsertsql());
//			}
//			
//			processor.prepareStatement(insersql.toString());
//			processor.executeUpdate();
//			
//		} catch (GenericDataSourceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (GenericEntityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//
//		String sql="UPDATE SEQUENCE_VALUE_ITEM SET SEQ_ID = \'0\' WHERE SEQ_NAME = \'TrackingCode\'";
//		try {
//			processor.prepareStatement(sql);
//			processor.executeUpdate();
//		} catch (GenericDataSourceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (GenericEntityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			try{
//				processor.close();
//			}catch(Exception e){
//				
//			}
//		}
//		
//		
//		Map<String, Object> retmap=new HashMap<String, Object>();
//		return retmap;
//	}
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
		public static String checkvalue(String value)
		{
			if(value==null||"".equals(value))
				return "0.0";
			return value;
		}
		
}
