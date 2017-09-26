package org.opentaps.warehouse.inventoryChange;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockCategoryModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeModel.TakeStockDetailModel;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.DescriptionVO;
import org.opentaps.warehouse.inventoryChange.inventoryChangeVo.StocktakingVo;



public class SerializationStockTaking {

	private static String INVENTORY_LOSS="inventory loss";
	private static String INVENTORY_PROFIT="inventory profit";
	
	public static String DownloadNonSerializedsample(HttpServletRequest request, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=NonSerializedsample.zip");
        String retmaptemp = "test.xml";
        request.getServletPath();
       String path= request.getSession().getServletContext().getRealPath("/");
       String[] ttt = path.split("opentaps");
       String wwww = ttt[0]+"specialpurpose\\commondefine\\webapp\\file\\test.xml";
        
       File  tempfilefolder= new File(wwww);
        try {
			ZipUtils.doCompress(tempfilefolder, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
	
	public static Map<String,Object> checkPhysicalInventory(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		List<StocktakingDto> params = InventoryTransfer.makeTransferParams(request, StocktakingDto.class);
		Map<String, StocktakingDto> paramsmap = new HashMap<String, StocktakingDto>();
		
		if(params==null)
		{
			result.put("resultCode", -1);
			result.put("resultMsg", "Parameter error,Please have a check.");
			return result;
		}
		for(int i=0;i<params.size();i++)
		{
			StocktakingDto temp=params.get(i);
			if(temp.getProductid()!=null&&!"".equals(temp.getProductid()))
			{
				if(paramsmap.containsKey(temp.getProductid()))
				{
					StocktakingDto tempart = paramsmap.get(temp.getProductid());
					int sumtemp = Integer.parseInt(tempart.getQuantity()+Integer.parseInt(temp.getQuantity()));
					tempart.setQuantity(sumtemp+"");
				}
				else 
				{
					paramsmap.put(temp.getProductid(), temp);
				}
			}
			
		}
		try {
			TransactionUtil.begin();
			String facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
			
			if(facilityId==null||"".equals(facilityId))
			{
				result.put("resultCode", -1);
				result.put("resultMsg", "You don't have permission to do the operation.");
				return result;
			}
			StringBuffer sql =new StringBuffer( "select * from  ( SELECT " + " ret.*, gi.ID_VALUE AS ean,profed.DESCRIPTION as description, " + 
							" gi.GOOD_IDENTIFICATION_TYPE_ID AS type "
							+ " ,pr.INTERNAL_NAME as model, pr.BRAND_NAME as brand" + 
							" FROM " + 
							"	( " + 
							"		SELECT " +
							"			ii.PRODUCT_ID, " + 
							"			ii.FACILITY_ID, " + 
							"			SUM( " + 
							"				ii.AVAILABLE_TO_PROMISE_TOTAL "+ 
							"			) AS sum " + 
							"		FROM " + 
							"			INVENTORY_ITEM AS ii " + 
							"   where ii.FACILITY_ID="+facilityId+
							"	and ii.INVENTORY_ITEM_TYPE_ID=\'NON_SERIAL_INV_ITEM\'"+ 
							"		GROUP BY "+ 
							"			ii.PRODUCT_ID, " + 
							"			ii.FACILITY_ID " + " ) AS ret "+ 
							" LEFT JOIN GOOD_IDENTIFICATION AS gi ON gi.PRODUCT_ID = ret.PRODUCT_ID "
							+ " left join (select PRODUCT_ID,GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION from "
							+ " ( SELECT  c.PRODUCT_ID as PRODUCT_ID,c.PRODUCT_FEATURE_ID as PRODUCT_FEATURE_ID,DESCRIPTION,d.PRODUCT_FEATURE_TYPE_ID as PRODUCT_FEATURE_TYPE_ID"
							+ " FROM "
							+ " PRODUCT_FEATURE_APPL c "
							+ " LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
							+ " 	) as  fea "
							+ " group by PRODUCT_ID"
							+ " ) as profed on profed.PRODUCT_ID=ret.PRODUCT_ID"
							+ " 	LEFT JOIN PRODUCT as pr on pr.PRODUCT_ID=ret.PRODUCT_ID "
							+ " ) as rett"); 
//			for (Map.Entry<String, StocktakingDto> entry : paramsmap.entrySet()) {
//				   sql.append("or ean="+entry.getKey());
//			}
			
			List<StocktakingVo> queryRetList = EncapsulatedQueryResultsUtil.getResults_(sql,StocktakingVo.class, delegator);
			
			
			List<StocktakingResulteDto> retlist=new ArrayList<StocktakingResulteDto>();
			
			for(int i=0;i<queryRetList.size();i++)
			{
				StocktakingVo StocktakingVotemp = queryRetList.get(i);
				
				StocktakingDto StocktakingDtotemp=paramsmap.get(StocktakingVotemp.getPRODUCT_ID());
				
				paramsmap.remove(StocktakingVotemp.getPRODUCT_ID());
				
				double sum1 = 0;
				double sum2 = 0;
				double dif = 0;
				String Reason="";
				try {
					if(StocktakingDtotemp!=null)
						sum1 = Double.valueOf(StocktakingDtotemp.getQuantity());
					sum2 = Double.valueOf(StocktakingVotemp.getSum());
					dif=Math.abs(sum1-sum2);
					if(sum1>sum2)
					{
						Reason=INVENTORY_PROFIT;
					}else 
					{
						Reason=INVENTORY_LOSS;
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String desc="";
				if(StocktakingVotemp.getBrand()!=null&&!"".equals(StocktakingVotemp.getBrand()))
					desc+=StocktakingVotemp.getBrand()+"|";
				if(StocktakingVotemp.getModel()!=null&&!"".equals(StocktakingVotemp.getModel()))
					desc+=StocktakingVotemp.getModel()+"|";
				if(StocktakingVotemp.getDescription()!=null&&!"".equals(StocktakingVotemp.getDescription()))
					desc+=StocktakingVotemp.getDescription()+"|";
				StocktakingVotemp.setDescription(desc);
				
				
				StocktakingResulteDto StocktakingResulteDtotemp= new StocktakingResulteDto(null,StocktakingVotemp.getPRODUCT_ID(),StocktakingVotemp.getDescription(),sum2+"",dif+"",Reason);
				
				
				if(dif!=0)//
					retlist.add(StocktakingResulteDtotemp);
			}
			
			if (paramsmap.size() >= 0) {

				for (Map.Entry<String, StocktakingDto> entry : paramsmap.entrySet()) {
					StocktakingDto tempvalue = entry.getValue();
					StocktakingResulteDto StocktakingResulteDtotemp= new StocktakingResulteDto(null,tempvalue.getProductid(),WarehouseCommon.getDescriptionbyproductid(delegator, tempvalue.getProductid()),0+"",tempvalue.getQuantity()+"",INVENTORY_PROFIT);
					retlist.add(StocktakingResulteDtotemp);
				}

			}
			
			String resultType = "";
			if(retlist.size() == 0){
				resultType = "Perfectly Match";
			}else{
				resultType = "Abnormal";
			}
			
			String takeStockId = WarehouseCommon.insertTakeStock(delegator, System.currentTimeMillis(), WarehouseCommon.getLocalYears(request), WarehouseCommon.getUserLoginIdByRequest(request), "N",facilityId,resultType);
			
			
			//记录日志
			
			WarehouseCommon.insertBatchTakeStockCategory(delegator,conversion(params),takeStockId,"N");
			WarehouseCommon.insertBatchTakeStockDetail(delegator,conversion2(retlist),takeStockId,"N");
			
			
//			for(int i=0;i<params.size();i++)
//			{
//				params.get(i)
//				WarehouseCommon.insertTakeStockCategory(delegator, takeStockId, params.g, quantity);
//			}
//			for(int i=0;i<retlist.size();i++)
//			{
//				StocktakingResulteDto rettemp = retlist.get(i);
//				WarehouseCommon.insertTakeStockDetail(delegator, takeStockId, rettemp.getEan(), rettemp.getDescription(), rettemp.getReason(),null, new BigDecimal(Integer.valueOf(rettemp.getInventoryquantity())), new BigDecimal(Integer.valueOf(rettemp.getDIFquantity())));
//				
//			}
			
			
			result.put("resultlist", retlist);
			result.put("takeStockId", takeStockId);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
			TransactionUtil.commit();
		} catch (GenericTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<TakeStockCategoryModel>  conversion(List<StocktakingDto> params)
	{
		List<TakeStockCategoryModel> ret =new ArrayList<TakeStockCategoryModel>();
		for(int i=0;i<params.size();i++)
		{
			StocktakingDto temp = params.get(i);
			TakeStockCategoryModel addtemp=new TakeStockCategoryModel();
			addtemp.setImeiOrEan(temp.getDescription());
			addtemp.setQuantity(temp.getQuantity());
			ret.add(addtemp);
		}
		return ret;
	}
	public static List<TakeStockDetailModel>  conversion2(List<StocktakingResulteDto>  params)
	{
		List<TakeStockDetailModel> ret =new ArrayList<TakeStockDetailModel>();
		for(int i=0;i<params.size();i++)
		{
			StocktakingResulteDto temp = params.get(i);
			TakeStockDetailModel addtemp=new TakeStockDetailModel();
			
			addtemp.setDescription(temp.getDescription());
			addtemp.setDifQuantity(temp.getDIFquantity());
			addtemp.setImeiOrEan(temp.getEan());
			addtemp.setInventoryQuantity(temp.getInventoryquantity());
			addtemp.setReason(temp.getReason());
			addtemp.setStatus(null);
			ret.add(addtemp);
		}
		return ret;
	}
}
