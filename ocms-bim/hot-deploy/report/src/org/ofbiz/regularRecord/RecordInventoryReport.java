package org.ofbiz.regularRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.service.DispatchContext;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class RecordInventoryReport {

	
		
	public static int addsum=500; 
	
	
	public static Map<String, Object> GH_RegularRecords(DispatchContext dctx, Map<String, ? extends Object> context) {
		String county = (String)BaseReportEvent.getSystemSettings().get("countryId");
		if (!"GHA".equals(county)) {
			Map<String, Object> retmap = new HashMap<String, Object>();
			return retmap;
		}
		Delegator delegator = dctx.getDelegator();
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);	
		
		List<RecordInventoryBean> toBeStored = new ArrayList<RecordInventoryBean>();
		long current=System.currentTimeMillis();//当前时间毫秒数  
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-1000*3600*24;//昨天零点零分零秒的毫秒数
		Timestamp nowDate = new Timestamp(zero);
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(nowDate);
		
		String tablename="INVENTORY_REPORT"+yyyyMM;
		long index=0;
		String querysql="select count(1) as sum from "+tablename+" where "+tablename+".RECORD_DATE=\'"+nowDate+"\'";
		ResultSet rs;
		int sumitme = 0;
		try {
			rs = processor.executeQuery(querysql);
			sumitme = 0;
			while (rs.next()) {
				sumitme=Integer.parseInt(rs.getString("sum"));
			}
		} catch (GenericDataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			System.out.println("table "+tablename+" is not exist.");
			System.out.println("now begin to create a new form.");
			cteaeINVENTORY_REPORTtabble(tablename,processor);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(sumitme>0)//当天的已经存过了
		{
			Map<String, Object> retmap=new HashMap<String, Object>();
			return retmap;
		}
		
		
		String RETAIL="RETAIL";//销售
		String DEFAULT_COST_PRICE="DEFAULT_COST_PRICE";
		String SPECIAL_COST_PRICE="SPECIAL_COST_PRICE";
		StringBuffer DEFAULT_COST_PRICE_sql = new StringBuffer("select proret.*,pp.PRICE as cost,pp.PRODUCT_STORE_GROUP_ID,(select ppr.PRICE from PRODUCT_PRICE as ppr where ppr.PRODUCT_ID=proret.PRODUCT_ID and PRODUCT_STORE_GROUP_ID='RETAIL') as RETAILPRICE   from"
							+" ("
							+" select iret.*,pr.PRODUCT_NAME,pr.BRAND_NAME,profed.DESCRIPTION from "
							+" PRODUCT as pr "
							+" join "
							+" ( " 
							+" select  ii.PRODUCT_ID,ii.FACILITY_ID,fc.FACILITY_NAME,SUM(ii.QUANTITY_ON_HAND_TOTAL) as sum "
							+" from  "
							+" INVENTORY_ITEM as ii join FACILITY as fc on ii.FACILITY_ID=fc.FACILITY_ID where ii.STATUS_ID='IXF_COMPLETE' or  ii.STATUS_ID='INV_AVAILABLE' or ii.STATUS_ID is null group by ii.PRODUCT_ID,ii.FACILITY_ID "
							+" ) as iret on iret.PRODUCT_ID=pr.PRODUCT_ID	"					
							+"	left join "
							+" ("
							+"		select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
							+"		( "
							+"		SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID" 
							+"		FROM "
							+" 		PRODUCT_FEATURE_APPL c " 
							+" 		LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
							+" 			) as  fea "
							+" 		group by PRODUCT_ID"
							+" 		) as profed on profed.PRODUCT_ID=iret.PRODUCT_ID"
							+" 		)as proret"
							+" 		left join (select * from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'DEFAULT_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID is null or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'SPECIAL_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'EGATEE_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'RETAIL_COST_PRICE\' )  as pp on pp.PRODUCT_ID=proret.PRODUCT_ID"
							+ " ORDER BY PRODUCT_ID,FACILITY_ID"); 
		try {
			rs = processor.executeQuery(DEFAULT_COST_PRICE_sql.toString());
			int num=0;
			long temptimestamp = System.currentTimeMillis();
			while (rs.next()) {
				RecordInventoryBean temp=new RecordInventoryBean();
				temp.setProductId(rs.getString("PRODUCT_ID"));
				temp.setProductStoreGroupId(rs.getString("PRODUCT_STORE_GROUP_ID"));
				temp.setFacilityId(rs.getString("FACILITY_ID"));
				temp.setFacilityName(rs.getString("FACILITY_NAME"));
				temp.setRecortimestamp(temptimestamp+"");
				temp.setRecordDate(nowDate);
				
				String qty = rs.getString("sum");
				double qtynum=0;
				if (qty != null) {
					try {
						qtynum = Double.valueOf(qty);
					} catch (NumberFormatException e) {
						qtynum = 0;
					}
				}
				String costcost=rs.getString("cost");
				double costcostnum=0;
				if (costcost != null) {
					try {
						costcostnum = Double.valueOf(costcost);
					} catch (NumberFormatException e) {
						costcostnum = 0;
					}
				}
				String retaipr=rs.getString("RETAILPRICE");
				double retaiprnum = 0;
				if (retaipr != null) {
					try {
						retaiprnum = Double.valueOf(retaipr);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						retaiprnum = 0;
					}
				}
				temp.setQuantityOnHandTotal(qtynum);
				temp.setAvgCost( costcostnum);
				temp.setDescription( rs.getString("DESCRIPTION"));
				temp.setProductname(rs.getString("PRODUCT_NAME"));
				temp.setBrandName(rs.getString("BRAND_NAME"));
				temp.setPrice(retaiprnum);
				String t=(index++)+"";
				temp.setIndex(t);
				toBeStored.add(temp);
			}
			
			
			adddata(toBeStored,index);
			int conut=0;
			StringBuffer insersql=new StringBuffer("INSERT INTO "+tablename+"(PRODUCT_ID,FACILITY_ID,PRODUCT_STORE_GROUP_ID,INDEXW,RECORD_DATE,QUANTITY_ON_HAND_TOTAL,AVG_COST,PRICE,DESCRIPTION,PRODUCTNAME,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP,FACILITY_NAME,BRAND_NAME) VALUES ");
			
			for(int k=0;k<toBeStored.size();k++)
			{
				 RecordInventoryBean tempdate = toBeStored.get(k);
				 if(conut==0)
				 {
					 insersql.append(tempdate.toinsertsql());
				 }
				 else 
				 {
					 insersql.append(" ,"+tempdate.toinsertsql());
				 }
				 conut++;
				 if(conut>=addsum)
				 {
					conut=0;
					processor.prepareStatement(insersql.toString());
					processor.executeUpdate();
					
					insersql.delete(0, insersql.length());
					insersql.append("INSERT INTO "+tablename+"(PRODUCT_ID,FACILITY_ID,PRODUCT_STORE_GROUP_ID,INDEXW,RECORD_DATE,QUANTITY_ON_HAND_TOTAL,AVG_COST,PRICE,DESCRIPTION,PRODUCTNAME,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP,FACILITY_NAME,BRAND_NAME) VALUES ");
				 }
			}
			if(conut>0)
			{
				processor.prepareStatement(insersql.toString());
				processor.executeUpdate();
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

	public static Map<String, Object> UG_RegularRecords(DispatchContext dctx, Map<String, ? extends Object> context) {
		
		String county = (String)BaseReportEvent.getSystemSettings().get("countryId");
		if (!"UGA".equals(county)) {
			Map<String, Object> retmap = new HashMap<String, Object>();
			return retmap;
		}
		Delegator delegator = dctx.getDelegator();
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);	
		
		List<RecordInventoryBean> toBeStored = new ArrayList<RecordInventoryBean>();
		long current=System.currentTimeMillis();//当前时间毫秒数  
		//long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-1000*3600*24;//昨天零点零分零秒的毫秒数
		long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
		Timestamp nowDate = new Timestamp(zero);
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(nowDate);
		
		String tablename="INVENTORY_REPORT"+yyyyMM;
		long index=0;
		String querysql="select count(1) as sum from "+tablename+" where "+tablename+".RECORD_DATE=\'"+nowDate+"\'";
		ResultSet rs;
		int sumitme = 0;
		try {
			rs = processor.executeQuery(querysql);
			sumitme = 0;
			while (rs.next()) {
				sumitme=Integer.parseInt(rs.getString("sum"));
			}
		} catch (GenericDataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			System.out.println("table "+tablename+" is not exist.");
			System.out.println("now begin to create a new form.");
			cteaeINVENTORY_REPORTtabble(tablename,processor);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(sumitme>0)//当天的已经存过了
		{
			Map<String, Object> retmap=new HashMap<String, Object>();
			return retmap;
		}
		
		
		String RETAIL="RETAIL";//销售
		String DEFAULT_COST_PRICE="DEFAULT_COST_PRICE";
		String SPECIAL_COST_PRICE="SPECIAL_COST_PRICE";
		StringBuffer DEFAULT_COST_PRICE_sql = new StringBuffer("select proret.*,pp.PRICE as cost,pp.PRODUCT_STORE_GROUP_ID,(select ppr.PRICE from PRODUCT_PRICE as ppr where ppr.PRODUCT_ID=proret.PRODUCT_ID and PRODUCT_STORE_GROUP_ID='RETAIL') as RETAILPRICE   from"
							+" ("
							+" select iret.*,pr.PRODUCT_NAME,pr.BRAND_NAME,profed.DESCRIPTION from "
							+" PRODUCT as pr "
							+" join "
							+" ( " 
							+" select  ii.PRODUCT_ID,ii.FACILITY_ID,fc.FACILITY_NAME,SUM(ii.QUANTITY_ON_HAND_TOTAL) as sum "
							+" from  "
							+" INVENTORY_ITEM as ii join FACILITY as fc on ii.FACILITY_ID=fc.FACILITY_ID where ii.STATUS_ID='IXF_COMPLETE' or  ii.STATUS_ID='INV_AVAILABLE' or ii.STATUS_ID is null group by ii.PRODUCT_ID,ii.FACILITY_ID "
							+" ) as iret on iret.PRODUCT_ID=pr.PRODUCT_ID	"					
							+"	left join "
							+" ("
							+"		select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
							+"		( "
							+"		SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID" 
							+"		FROM "
							+" 		PRODUCT_FEATURE_APPL c " 
							+" 		LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
							+" 			) as  fea "
							+" 		group by PRODUCT_ID"
							+" 		) as profed on profed.PRODUCT_ID=iret.PRODUCT_ID"
							+" 		)as proret"
							+" 		left join (select * from PRODUCT_PRICE where PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'DEFAULT_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID is null or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'SPECIAL_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'EGATEE_COST_PRICE\' or PRODUCT_PRICE.PRODUCT_STORE_GROUP_ID=\'RETAIL_COST_PRICE\' )  as pp on pp.PRODUCT_ID=proret.PRODUCT_ID"
							+ " ORDER BY PRODUCT_ID,FACILITY_ID"); 
		try {
			rs = processor.executeQuery(DEFAULT_COST_PRICE_sql.toString());
			int num=0;
			long temptimestamp = System.currentTimeMillis();
			while (rs.next()) {
				RecordInventoryBean temp=new RecordInventoryBean();
				temp.setProductId(rs.getString("PRODUCT_ID"));
				temp.setProductStoreGroupId(rs.getString("PRODUCT_STORE_GROUP_ID"));
				temp.setFacilityId(rs.getString("FACILITY_ID"));
				temp.setFacilityName(rs.getString("FACILITY_NAME"));
				temp.setRecortimestamp(temptimestamp+"");
				temp.setRecordDate(nowDate);
				
				String qty = rs.getString("sum");
				double qtynum=0;
				if (qty != null) {
					try {
						qtynum = Double.valueOf(qty);
					} catch (NumberFormatException e) {
						qtynum = 0;
					}
				}
				String costcost=rs.getString("cost");
				double costcostnum=0;
				if (costcost != null) {
					try {
						costcostnum = Double.valueOf(costcost);
					} catch (NumberFormatException e) {
						costcostnum = 0;
					}
				}
				String retaipr=rs.getString("RETAILPRICE");
				double retaiprnum = 0;
				if (retaipr != null) {
					try {
						retaiprnum = Double.valueOf(retaipr);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						retaiprnum = 0;
					}
				}
				temp.setQuantityOnHandTotal(qtynum);
				temp.setAvgCost( costcostnum);
				temp.setDescription( rs.getString("DESCRIPTION"));
				temp.setProductname(rs.getString("PRODUCT_NAME"));
				temp.setBrandName(rs.getString("BRAND_NAME"));
				temp.setPrice(retaiprnum);
				String t=(index++)+"";
				temp.setIndex(t);
				toBeStored.add(temp);
			}
			
			
			adddata(toBeStored,index);
			int conut=0;
			StringBuffer insersql=new StringBuffer("INSERT INTO "+tablename+"(PRODUCT_ID,FACILITY_ID,PRODUCT_STORE_GROUP_ID,INDEXW,RECORD_DATE,QUANTITY_ON_HAND_TOTAL,AVG_COST,PRICE,DESCRIPTION,PRODUCTNAME,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP,FACILITY_NAME,BRAND_NAME) VALUES ");
			
			for(int k=0;k<toBeStored.size();k++)
			{
				 RecordInventoryBean tempdate = toBeStored.get(k);
				 if(conut==0)
				 {
					 insersql.append(tempdate.toinsertsql());
				 }
				 else 
				 {
					 insersql.append(" ,"+tempdate.toinsertsql());
				 }
				 conut++;
				 if(conut>=addsum)
				 {
					conut=0;
					processor.prepareStatement(insersql.toString());
					processor.executeUpdate();
					
					insersql.delete(0, insersql.length());
					insersql.append("INSERT INTO "+tablename+"(PRODUCT_ID,FACILITY_ID,PRODUCT_STORE_GROUP_ID,INDEXW,RECORD_DATE,QUANTITY_ON_HAND_TOTAL,AVG_COST,PRICE,DESCRIPTION,PRODUCTNAME,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP,FACILITY_NAME,BRAND_NAME) VALUES ");
				 }
			}
			if(conut>0)
			{
				processor.prepareStatement(insersql.toString());
				processor.executeUpdate();
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
	
	public static void  adddata(List<RecordInventoryBean> toBeStored,long cout)
	{
		List<RecordInventoryBean> addlist =new ArrayList<RecordInventoryBean>();
		
		int sumprice = 0;
		int kkk=0;
		String lastproduct = "";
		String lastfacilityId ="";
		int[] ary = {0,0,0,0}; 
		RecordInventoryBean lasttemp=null;
		String[] stringarry={"DEFAULT_COST_PRICE","SPECIAL_COST_PRICE","EGATEE_COST_PRICE","RETAIL_COST_PRICE"};
		
		
		for(int i=0;i<toBeStored.size();i++)
		{
			
			RecordInventoryBean temp = toBeStored.get(i);
			
			String ProductStoreGroupId = temp.getProductStoreGroupId();
			
			int index;
			if("DEFAULT_COST_PRICE".equals(ProductStoreGroupId))
			{
				index=0;
			}else if("SPECIAL_COST_PRICE".equals(ProductStoreGroupId))
			{
				index=1;
			}else if("EGATEE_COST_PRICE".equals(ProductStoreGroupId))
			{
				index=2;
			}else if("RETAIL_COST_PRICE".equals(ProductStoreGroupId))
			{
				index=3;
			}else 
			{
				if(ProductStoreGroupId==null )
				{
					RecordInventoryBean last=null;
					RecordInventoryBean next=null;
					if(i>0)
						last=toBeStored.get(i-1);
					if(i<toBeStored.size()-1) 
						next=toBeStored.get(i+1);
					
					if((last ==null || !last.getFacilityId().equals(temp.getFacilityId()) || !last.getProductId().equals(temp.getProductId())) && ( next==null || !next.getFacilityId().equals(temp.getFacilityId())  || !next.getProductId().equals(temp.getProductId())    ))
					{
						for (int j = 0; j < 4; j++) {
							RecordInventoryBean addtemp;
							try {
								addtemp = (RecordInventoryBean) temp.clone();
								addtemp.setProductStoreGroupId(stringarry[j]);
								addtemp.setPrice(0);
								String t = (cout++) + "";
								addtemp.setIndex(t);
								addlist.add(addtemp);

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					
					
				}
				continue;
			}
			
			
			if( lastfacilityId.equals(temp.getFacilityId()) && lastproduct.equals(temp.getFacilityId()) )
			{
				sumprice++;
			}else 
			{
				
				if(sumprice!=4 && lasttemp!=null)
				{
					for(int j=0;j<4;j++)
					{
						if(ary[j]!=1)
						{
							RecordInventoryBean addtemp;
							try {
								addtemp = (RecordInventoryBean) lasttemp.clone();
								addtemp.setProductStoreGroupId(stringarry[j]);
								addtemp.setPrice(0);
								String t=(cout++)+"";
								addtemp.setIndex(t);
								addlist.add(addtemp);
								
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  
							
						}
						ary[j]=0;
					}
				}
				sumprice=1;
			}
			
			ary[index]=1;
			
			lasttemp=temp;
			lastproduct=temp.getFacilityId();
			lastfacilityId=temp.getFacilityId();
			
			
			
			
			
			if(i== (toBeStored.size()-1))
			{
				if(sumprice!=4 && lasttemp!=null)
				{
					for(int j=0;j<4;j++)
					{
						if(ary[j]!=1)
						{
							RecordInventoryBean addtemp;
							try {
								addtemp = (RecordInventoryBean) temp.clone();
								addtemp.setProductStoreGroupId(stringarry[j]);
								addtemp.setPrice(0);
								String t=(cout++)+"";
								addtemp.setIndex(t);
								addlist.add(addtemp);
								
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  
							
						}
						ary[j]=0;
					}
				}
			}
			
		}
		
		toBeStored.addAll(addlist);
	}
	
	
//	public static void  checkdata(List<RecordInventoryBean> toBeStored)
//	{
//		String lastproduct = "";
//		String lastfacilityId ="";
//		int sumprice=0;;
//		
//		for(int i=0;i<toBeStored.size();i++)
//		{
//			RecordInventoryBean temp = toBeStored.get(i);
//			
//			if( lastfacilityId.equals(temp.getFacilityId()) && lastproduct.equals(temp.getFacilityId()) )
//			{
//				
//				sumprice++;
//			}else 
//			{
//				if(sumprice<4)
//				{
//					System.out.println("---------------------");
//				}
//				sumprice=0;
//			}
//		}
//	}
	
	
	
	
	
	
	public static Map<String, Object> RegularRecords___(DispatchContext dctx, Map<String, ? extends Object> context) {
		Delegator delegator = dctx.getDelegator();
		List<GenericValue> toBeStored = new ArrayList<GenericValue>();
		long current=System.currentTimeMillis();//当前时间毫秒数  
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-1000*3600*24;//昨天零点零分零秒的毫秒数
		Timestamp nowDate = new Timestamp(zero);
		
		List<GenericValue> retlist = null;
		List l = new ArrayList();//存放查询条件
		l.add(EntityCondition.makeCondition("recordDate", EntityOperator.EQUALS, nowDate));
		try {
			retlist = delegator.findByAnd("InventoryReport",l);
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(retlist==null||retlist.size()>0)//当天的已经存过了
		{
			Map<String, Object> retmap=new HashMap<String, Object>();
			return retmap;
		}
		
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		String RETAIL="RETAIL";//销售
		
		
		StringBuffer sql = new StringBuffer("select iret.*,pr.PRODUCT_NAME,profed.DESCRIPTION from "
							+" PRODUCT as pr"
							+" join"
							+" ( "
							+" select  ii.PRODUCT_ID,ii.FACILITY_ID,SUM(ii.QUANTITY_ON_HAND_TOTAL) as sum,AVG(ii.UNIT_COST) as avgcost "
							+" from " 
							+" INVENTORY_ITEM as ii group by ii.PRODUCT_ID,ii.FACILITY_ID "
							+" ) as iret on iret.PRODUCT_ID=pr.PRODUCT_ID "
							+" left join "
							+" (	"					
							+"	select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from"
							+" ( "
							+"		SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID "
							+"		FROM "
							+"		PRODUCT_FEATURE_APPL c" 
							+"		LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID "
							+" ) as  fea " 
							+" group by PRODUCT_ID"
							+" ) as profed on profed.PRODUCT_ID=iret.PRODUCT_ID"); 
		try {
			ResultSet rs = processor.executeQuery(sql.toString());
			while (rs.next()) {
				//DEFAULT_COST_PRICE
				GenericValue productPriceChange = delegator.makeValue("InventoryReport");
				String productIdstr = rs.getString("PRODUCT_ID");
				productPriceChange.set("productId", productIdstr);
				productPriceChange.set("facilityId", rs.getString("FACILITY_ID"));
				productPriceChange.set("recordDate", nowDate);
				productPriceChange.set("quantityOnHandTotal", rs.getString("sum"));
				//productPriceChange.set("avgCost", rs.getString("avgcost"));
				productPriceChange.set("description", rs.getString("DESCRIPTION"));
				productPriceChange.set("productname", rs.getString("PRODUCT_NAME"));
				List l3 = new ArrayList();//存放查询条件
				l3.add(EntityCondition.makeCondition("productId", productIdstr));
				l3.add(EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, RETAIL));
				List<GenericValue> retlist3 = delegator.findByAnd("ProductPrice",l3);
				if(retlist3.size()>0)
				{
					productPriceChange.set("price", retlist3.get(0).getString("price"));
				}
				toBeStored.add(productPriceChange);
				
				
				
				
			}
			delegator.storeAll(toBeStored);
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
		Map<String, Object> retmap=new HashMap<String, Object>();
		return retmap;
	}
	
	
	public static Map<String, Object> CheckTheTime(DispatchContext dctx, Map<String, ? extends Object> context) {
		TimeZone timeZoneTyokyo = TimeZone.getTimeZone("GMT+3");
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		TimeZone cuurent = calendar.getTimeZone();
		
		Delegator delegator = dctx.getDelegator();
		
		List<GenericValue> retlist = null;
		List l = new ArrayList();//存放查询条件
		l.add(EntityCondition.makeCondition("serviceName", EntityOperator.EQUALS, "regularRecordInventoryReport"));
		l.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "SERVICE_PENDING"));
		try {
			retlist = delegator.findByAnd("JobSandbox",l);
			if(retlist.size()>0)
			{
				GenericValue temp = retlist.get(0);
				String rumtime=temp.getString("runTime");
				Date dt2=null;
				SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					dt2 = format.parse(rumtime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long chineseMills = dt2.getTime() + (timeZoneTyokyo.getRawOffset() - cuurent.getRawOffset());
				Timestamp nowDate = new Timestamp(chineseMills);
				temp.set("runTime",nowDate );
				List<GenericValue> toBeStored = new ArrayList<GenericValue>();
				toBeStored.add(temp);
				delegator.storeAll(toBeStored);
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Object> retmap=new HashMap<String, Object>();
		return retmap;
	}
	
	
	public static void cteaeINVENTORY_REPORTtabble(String table,SQLProcessor processor)
	{
		String creteatsql="CREATE TABLE "+table+" SELECT * FROM INVENTORY_REPORT WHERE 1=2";
		try {
			processor.prepareStatement(creteatsql);
			processor.executeUpdate();
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
