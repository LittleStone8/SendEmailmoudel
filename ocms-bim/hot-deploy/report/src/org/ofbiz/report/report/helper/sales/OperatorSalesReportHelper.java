package org.ofbiz.report.report.helper.sales;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.report.report.been.ExportSalesReport;
import org.ofbiz.report.report.been.QueryDataForReportBean;
import org.ofbiz.report.report.been.SalesReportBean;
import org.ofbiz.report.report.been.SalesTableBean;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;
public class OperatorSalesReportHelper {
	public static List<SalesTableBean> searchTableBean(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
			 String beginDate, String endDate,GenericValue userLogin) throws Exception, GenericEntityException {
		List<StoreGroupBean> sGroupBeans =  (List<StoreGroupBean>)SGandSTOmap.get("SGroup");
		List<StoreRollUpBean> storeRollUpBeans = (List<StoreRollUpBean>)SGandSTOmap.get("SGMember");
		
		
		// TODO Auto-generated method stub
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String whereCause = "";
		if (UtilValidate.isNotEmpty(productId)) {
			whereCause += " And oi.PRODUCT_ID = '" + productId + "'";
		}
		for(int k=0;k<sGroupBeans.size();k++)
		{
			StoreGroupBean temp = sGroupBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			else 
			{
				whereCause += " or psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			if(k==(sGroupBeans.size()-1))
				whereCause+=" ) ";
		}
			
		for(int k=0;k<storeRollUpBeans.size();k++)
		{
			StoreRollUpBean tempSB = storeRollUpBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			else 
			{
				whereCause += " or oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			if(k==(storeRollUpBeans.size()-1))
				whereCause+=" ) ";
			
		}
		// if(UtilValidate.isNotEmpty(beginTSP)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') >= '" + beginDate + "' ";
		// }
		// if(UtilValidate.isNotEmpty(storeId)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') <= '" + endDate + "' ";
		// }
		String sql = " select DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') as orderDate," + " oi.PRODUCT_ID  as productId,"
		
				//2017/05/12 yzl增加对产品描述的查询 begin
				+ " ( select GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION "
				+ " from PRODUCT_FEATURE_APPL c LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID"
				+ " where c.PRODUCT_ID=oi.PRODUCT_ID"
				+ "  )as descr,"
				// end
				
				+ " sum(oi.QUANTITY)  as quantity,"
			    + " AVG(oi.CURRENT_PRICE) as unitPrice,"
				+ " AVG(oi.UNIT_S_PRESET_COST) as cost," //(oi.UNIT_S_PRESET_COST)
				+ " sum(oi.CURRENT_PRICE*oi.QUANTITY) AS totalPrice,"
				+ " SUM(oi.UNIT_PRICE*oi.QUANTITY)  as actualAmount ,"  //unit_price 记录打折促销后的总价  
				+ " psg.PRODUCT_STORE_GROUP_ID 	as groupId,"
				+ " oh.PRODUCT_STORE_ID  as storeId "
				+ " from ORDER_ITEM oi,ORDER_HEADER oh,PRODUCT_STORE_GROUP psg,PRODUCT_STORE_GROUP_MEMBER psgm "
				+ " where oi.ORDER_ID = oh.ORDER_ID " + " AND oh.STATUS_ID ='ORDER_COMPLETED' "
				+ " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID "
				+ " And oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' " 
				+ " AND psgm.PRODUCT_STORE_GROUP_ID=psg.PRODUCT_STORE_GROUP_ID "
				+ " And psg.PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP'" + whereCause
				+ " group by DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d'),oi.PRODUCT_ID,psg.PRODUCT_STORE_GROUP_ID,oh.PRODUCT_STORE_ID";
		ResultSet rs = null;
		List<SalesTableBean> markertTableBeens = new ArrayList<SalesTableBean>();
		try {
			// 获得j结果集
			rs = processor.executeQuery(sql);
			while (rs.next()) {
				SalesTableBean markertTableBeen = new SalesTableBean();
				markertTableBeen.setDate(rs.getString("orderDate"));
				markertTableBeen.setProductId(rs.getString("productId"));
				markertTableBeen.setDescribe(rs.getString("descr"));
				markertTableBeen.setQuantity(rs.getBigDecimal("quantity"));
				markertTableBeen.setUnitPrice(rs.getBigDecimal("unitPrice"));
				markertTableBeen.setActualCost(rs.getBigDecimal("cost"));
				markertTableBeen.setTotalPrice(rs.getBigDecimal("totalPrice"));
				markertTableBeen.setActualCollection(rs.getBigDecimal("actualAmount"));
				markertTableBeen.setGroupId(rs.getString("groupId"));
				markertTableBeen.setStoreId(rs.getString("storeId"));
				markertTableBeens.add(markertTableBeen);
			}
		} finally {
			if (processor != null) {
				try {
					processor.close();
				} catch (GenericDataSourceException e) {
					e.printStackTrace();
				}
				processor = null;
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
		}
		return markertTableBeens;
	}
	public static List<SalesReportBean> getOperatorSalesRepor(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
			 String beginDate, String endDate,GenericValue userLogin , HttpServletRequest request) throws Exception, GenericEntityException  {
		
		List<StoreGroupBean> sGroupBeans = (List<StoreGroupBean>)SGandSTOmap.get("SGroup");
		List<StoreRollUpBean> storeRollUpBeans = (List<StoreRollUpBean>)SGandSTOmap.get("SGMember");
		String paymentMethodType = request.getParameter("paymentMethod");
		String  paymentMethodSql = null;
		if(paymentMethodType == null || "".equals(paymentMethodType)){
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE group by ORDER_ID ) opp";
		}else{
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE where PAYMENT_METHOD_TYPE_ID='" + paymentMethodType + "' group by ORDER_ID ) opp";
		}
		
		// TODO Auto-generated method stub
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String whereCause = "";
		if (UtilValidate.isNotEmpty(productId)) {
			whereCause += " And oi.PRODUCT_ID = '" + productId + "'";
		}
		for(int k=0;k<sGroupBeans.size();k++)
		{
			StoreGroupBean temp = sGroupBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			else 
			{
				whereCause += " or psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			if(k==(sGroupBeans.size()-1))
				whereCause+=" ) ";
		}
			
		for(int k=0;k<storeRollUpBeans.size();k++)
		{
			StoreRollUpBean tempSB = storeRollUpBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			else 
			{
				whereCause += " or oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			if(k==(storeRollUpBeans.size()-1))
				whereCause+=" ) ";
			
		}
		// if(UtilValidate.isNotEmpty(beginTSP)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') >= '" + beginDate + "' ";
		// }
		// if(UtilValidate.isNotEmpty(storeId)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') <= '" + endDate + "' ";
		// }
		
		String date = " DATE_FORMAT(opp.CREATED_STAMP,'%Y-%m-%d') >= '" + beginDate + "' ";
		date += " And DATE_FORMAT(opp.CREATED_STAMP,'%Y-%m-%d') <= '" + endDate + "' ";
		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID where " + date ;
		
		
		
		
		
		
		String sql = "  select DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') as orderDate, oi.PRODUCT_ID  as productId, "
				+ " DATE_FORMAT(oh.CREATED_STAMP, '%Y%m') AS month, "
				+ " (select CONCAT(p.BRAND_NAME,' | ',p.INTERNAL_NAME) as productInfo from PRODUCT p where p.PRODUCT_ID =oi.PRODUCT_ID ) as productInfo, "
				+ " (select p.BRAND_NAME from PRODUCT p where p.PRODUCT_ID = oi.PRODUCT_ID) AS brandName , "
				+ " (select p.INTERNAL_NAME from PRODUCT p where p.PRODUCT_ID = oi.PRODUCT_ID) AS internalName, "
				+ " ( select GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION "
				+ " from PRODUCT_FEATURE_APPL c LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID "
				+ " where c.PRODUCT_ID=oi.PRODUCT_ID )as descr, "
//				+ " (select  CONCAT(fpc.DESCRIPTION,'->',spc.DESCRIPTION,'->',tpc.DESCRIPTION) AS category from "
				+ " (select  tpc.DESCRIPTION AS category from "
				+ " PRODUCT_CATEGORY fpc left join "
				+ " PRODUCT_CATEGORY spc on "
				+ " fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID "
				+ " left join PRODUCT_CATEGORY tpc on "
				+ " spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID "
				+ " left join PRODUCT_CATEGORY_MEMBER pcm on "
				+ " tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID "
				+ " where PRODUCT_ID = oi.PRODUCT_ID) as category, "
				+ " (SELECT fpc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS fpcCategory, "
				+ " (SELECT spc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS spcCategory, "
				+ "	(SELECT tpc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS tpcCategory, "
				+ " oi.UNIT_PRESET_COST as defaultCostPrice , oi.EGATEE_COST_PRICE as egateeCostPrice , oi.SPECIAL_COST_PRICE as specialCostPrice , oi.RETAIL_COST_PRICE as retailCostPrice , "
				+ " oi.ORDER_ID as orderId , oi.QUANTITY as quantity , oi.UNIT_PRICE as unitPrice , oh.STATUS_ID as status , "
				+ " oi.CURRENT_PRICE as currentPrice, oi.UNIT_S_PRESET_COST as unitSPresetCost , oi.UNIT_PRESET_COST as unitPresetCost , "
				+ " (select CONCAT(p.FIRST_NAME, ' ', p.LAST_NAME) as salePerson from PERSON p where PARTY_ID = oh.SALES_ID) as salePerson, "
				+ " (select psg.PRODUCT_STORE_GROUP_NAME from PRODUCT_STORE_GROUP psg where psg.PRODUCT_STORE_GROUP_ID = oh.PRODUCT_STORE_GROUP_ID) as salesChannel, "
				+ " psg.PRODUCT_STORE_GROUP_ID as groupId , oh.CURRENCY_UOM as currencyUom , "
				+ " (select ps.STORE_NAME as shopName from PRODUCT_STORE ps where PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID) AS storeName , "
				+ " (SELECT psg.PRODUCT_STORE_GROUP_NAME FROM (select * from PRODUCT_STORE_GROUP where PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP') psg left join PRODUCT_STORE_GROUP_MEMBER psgm on psg.PRODUCT_STORE_GROUP_ID = psgm.PRODUCT_STORE_GROUP_ID where psgm.PRODUCT_STORE_ID=oh.PRODUCT_STORE_ID) as shopGroup , "
				+ " (select per.FIRST_NAME_LOCAL from PERSON per where PARTY_ID = oh.BUYER_ID) as customerName , "
				+ " psg.PRODUCT_STORE_GROUP_ID as groupId , oh.CURRENCY_UOM as currencyUom , oh.PRODUCT_STORE_ID  as storeId "
				+ " from ORDER_ITEM oi,ORDER_HEADER oh,PRODUCT_STORE_GROUP psg,PRODUCT_STORE_GROUP_MEMBER psgm , "
				+ paymentMethodSql
				+ " where oi.ORDER_ID = oh.ORDER_ID "
				+ " AND oi.ORDER_ID = opp.ORDER_ID "
				+ " AND (oh.STATUS_ID = 'ORDER_COMPLETED' or oh.STATUS_ID = 'ORDER_PAYMENT' or oh.STATUS_ID = 'ORDER_SHIPPED' "
				+ " or oh.STATUS_ID = 'ORDER_COMPLETED_W_P' or oh.STATUS_ID = 'ORDER_PACKED') "
				+ " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID "
				+ " And oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' "
				+ " AND psgm.PRODUCT_STORE_GROUP_ID=psg.PRODUCT_STORE_GROUP_ID "
				+ " And psg.PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP' "
				+ whereCause
				+ " order by DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') desc ";
		 
		
		
		ResultSet rs = null;
		ResultSet paymentMaprs = null;
		List<SalesReportBean> salesReportBeanList = new ArrayList<SalesReportBean>();
		List<QueryDataForReportBean> queryDataForReportBeanList = new ArrayList<QueryDataForReportBean>();
		Map<String, String> paymentMap = new HashMap<String, String>();
		try {
			
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> context = new HashMap<String, Object>();
			Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
			String egateeCost = (String) uomInfo.get("egateeCost");
			String specialCost = (String) uomInfo.get("specialCost");
			String retailCost = (String) uomInfo.get("retailCost");
			
			
			// 获得j结果集
			rs = processor.executeQuery(sql);
			paymentMaprs = processor.executeQuery(orderPaymentSql);
			while (paymentMaprs.next()) {
				if(paymentMap.containsKey(paymentMaprs.getString("ORDER_ID"))){
					String paymentOrderId = paymentMaprs.getString("ORDER_ID");
					String paymentDescription = paymentMap.get(paymentOrderId);
					if(paymentDescription.indexOf(paymentMaprs.getString("DESCRIPTION")) < 0){
						paymentDescription = paymentMap.get(paymentOrderId) + "&" + paymentMaprs.getString("DESCRIPTION");
						paymentMap.put(paymentOrderId, paymentDescription);
					}
				}else{
					String paymentOrderId = paymentMaprs.getString("ORDER_ID");
					String paymentDescription = paymentMaprs.getString("DESCRIPTION");
					paymentMap.put(paymentOrderId, paymentDescription);
				}
				
			}
			
			
			
			while (rs.next()) {
				
				
				QueryDataForReportBean queryDataForReportBean = new QueryDataForReportBean();
				
				queryDataForReportBean.setOrderDate(rs.getString("orderDate"));
				queryDataForReportBean.setProductId(rs.getString("productId"));
				queryDataForReportBean.setProductInfo(rs.getString("productInfo"));
				queryDataForReportBean.setDescr(rs.getString("descr"));
				queryDataForReportBean.setCategroy(rs.getString("category"));
				queryDataForReportBean.setOrderId(rs.getString("orderId"));
				queryDataForReportBean.setQuantity(rs.getBigDecimal("quantity"));
				queryDataForReportBean.setCurrentPrice(rs.getBigDecimal("currentPrice"));
				queryDataForReportBean.setUnitPrice(rs.getBigDecimal("unitPrice"));
				queryDataForReportBean.setUnitSPresetCost(rs.getBigDecimal("unitSPresetCost"));
				queryDataForReportBean.setUnitPresetCost(rs.getBigDecimal("unitPresetCost"));
				queryDataForReportBean.setGroupId(rs.getString("groupId"));
				queryDataForReportBean.setStoreId(rs.getString("storeId"));
				queryDataForReportBean.setSalePerson(rs.getString("salePerson"));
				queryDataForReportBean.setCurrencyUom(rs.getString("currencyUom"));
				queryDataForReportBean.setDefaultCostPrice(rs.getBigDecimal("defaultCostPrice"));
				queryDataForReportBean.setEgateeCostPrice(rs.getBigDecimal("egateeCostPrice"));
				queryDataForReportBean.setSpecialCostPrice(rs.getBigDecimal("specialCostPrice"));
				queryDataForReportBean.setRetailCostPrice(rs.getBigDecimal("retailCostPrice"));
				queryDataForReportBean.setSalesChannel(rs.getString("salesChannel"));
				queryDataForReportBeanList.add(queryDataForReportBean);
				
			}
			
			
			for (QueryDataForReportBean queryDataForReportBean : queryDataForReportBeanList) {
				
				ExportSalesReport exportSalesReportBean = new ExportSalesReport();
				
				
				boolean flag = true;
				if(salesReportBeanList.size() > 0){
					for (SalesReportBean item : salesReportBeanList) {
						if(item.getProductId().equals(queryDataForReportBean.getProductId()) && item.getOrderDate().equals(queryDataForReportBean.getOrderDate()) && item.getStoreId().equals(queryDataForReportBean.getStoreId())){
							
							
							item = BaseSalesReportHelper.edit(paymentMap , delegator, item, queryDataForReportBean , egateeCost , specialCost , retailCost);
							flag = false;
						}
					}
					if(flag){
						salesReportBeanList.add(BaseSalesReportHelper.init(paymentMap , delegator, queryDataForReportBean , egateeCost , specialCost , retailCost));
					}
				}else{
					
					salesReportBeanList.add(BaseSalesReportHelper.init(paymentMap , delegator, queryDataForReportBean ,  egateeCost , specialCost , retailCost));
				}
			}
		} finally {
			if (processor != null) {
				try {
					processor.close();
				} catch (GenericDataSourceException e) {
					e.printStackTrace();
				}
				processor = null;
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
		}
		return salesReportBeanList;
	}
	
	/*public static SalesReportBean init(Map<String, String> paymentMap , GenericDelegator delegator , QueryDataForReportBean queryDataForReportBean , String  egateeCost , String specialCost , String retailCost) throws GenericEntityException{
		SalesReportBean salesReportBean = new SalesReportBean();
		salesReportBean.setOrderDate(queryDataForReportBean.getOrderDate());
		salesReportBean.setCategory(queryDataForReportBean.getCategroy());
		salesReportBean.setProductId(queryDataForReportBean.getProductId());
		salesReportBean.setDescription(queryDataForReportBean.getProductInfo() + " | " + queryDataForReportBean.getDescr());
		BigDecimal quantity = queryDataForReportBean.getQuantity();
		salesReportBean.setQuantity(queryDataForReportBean.getQuantity());
		//unitPrice 卖出去时的渠道价    
		BigDecimal unitPrice = queryDataForReportBean.getUnitPrice();
		salesReportBean.setUnitPrice(unitPrice);
		
		//currentPrice卖出去时的实际价格（单价）
		BigDecimal currentPrice = queryDataForReportBean.getCurrentPrice();
		salesReportBean.setCurrentPrice(currentPrice);
		
		
		BigDecimal defaultCostPrice = queryDataForReportBean.getDefaultCostPrice()==null ? new BigDecimal(0) : queryDataForReportBean.getDefaultCostPrice();
		BigDecimal specialCostPrice = queryDataForReportBean.getSpecialCostPrice()==null ? defaultCostPrice.multiply(new BigDecimal(specialCost)) : queryDataForReportBean.getSpecialCostPrice();
		//平均价格
		salesReportBean.setSpecialCostPrice(specialCostPrice);
		//总价
		BigDecimal totalSpecialCostPrice = specialCostPrice.multiply(quantity);
		salesReportBean.setTotalSpecialCostPrice(totalSpecialCostPrice);
		
		
		
		
		
		//SubTotal=Unit Price * Quantity
		BigDecimal subTotal = queryDataForReportBean.getCurrentPrice().multiply(queryDataForReportBean.getQuantity());
		salesReportBean.setSubTotal(subTotal);
		
		//Adjustment为所有单笔Adjustment累加之和
		BigDecimal adjustment = new BigDecimal(0);
		if(queryDataForReportBean.getUnitPrice().compareTo(queryDataForReportBean.getCurrentPrice()) != 0){
			adjustment = queryDataForReportBean.getUnitPrice().subtract(queryDataForReportBean.getCurrentPrice());
			adjustment = adjustment.multiply(queryDataForReportBean.getQuantity());
		}
		salesReportBean.setAdjustment(adjustment);
		
		//Payment填支付方式，Cash或者credit，暂时不考虑一笔订单有多种支付方式的情况
		String payment = paymentMap.get(queryDataForReportBean.getOrderId());
		salesReportBean.setPayment(payment);
		
		
		
		
		//Recievables应收账款，即调价后的实收 ； Recievables = SubTotal - Adjustment
		BigDecimal recievables = subTotal.add(adjustment);
		salesReportBean.setRecievables(recievables);
		
		//销售员
		salesReportBean.setSalesPerson(queryDataForReportBean.getSalePerson());
		//销售渠道
		salesReportBean.setSalesChannel(queryDataForReportBean.getSalesChannel());
		//店铺id
		salesReportBean.setStoreId(queryDataForReportBean.getStoreId());
		
		salesReportBean.operatorInit();
		return salesReportBean;
	}
	
	
	
public static SalesReportBean edit(Map<String, String> paymentMap , GenericDelegator delegator , SalesReportBean item , QueryDataForReportBean queryDataForReportBean , String  egateeCost , String specialCost , String retailCost) throws GenericEntityException{
		String orderId = queryDataForReportBean.getOrderId();
		//新增产品总数totalQuantity
		BigDecimal orgQuantity = item.getQuantity();
		BigDecimal addQuantity = queryDataForReportBean.getQuantity() == null ? new BigDecimal(0) : queryDataForReportBean.getQuantity();
		BigDecimal totalQuantity = addQuantity.add(orgQuantity);
		
		//加权平均UnitPrice
		BigDecimal showUnitPrice = item.getUnitPrice();
		BigDecimal newUnitPrice = queryDataForReportBean.getUnitPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getUnitPrice();
		showUnitPrice = (showUnitPrice.multiply(orgQuantity).add(newUnitPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setUnitPrice(showUnitPrice);
		item.setQuantity(totalQuantity);
		
		
		//加权平均currentPrice
		BigDecimal showCurrentPrice = item.getCurrentPrice();
		BigDecimal newCurrentPrice = queryDataForReportBean.getCurrentPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getCurrentPrice();
		showCurrentPrice = (showCurrentPrice.multiply(orgQuantity).add(newCurrentPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setCurrentPrice(showCurrentPrice);
		
		
		//specialCostPrice
		BigDecimal showSpecialCostPrice = item.getSpecialCostPrice();
		BigDecimal newSpecialCostPrice = queryDataForReportBean.getSpecialCostPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getSpecialCostPrice();
		showSpecialCostPrice = (showSpecialCostPrice.multiply(orgQuantity).add(newSpecialCostPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setSpecialCostPrice(showSpecialCostPrice);
		
		//totalspecialCostPrice
		BigDecimal totalSpecialCostPrice = item.getTotalSpecialCostPrice();
		BigDecimal newTotalSpecialCostPrice = newSpecialCostPrice.multiply(addQuantity);
		totalSpecialCostPrice = totalSpecialCostPrice.add(newTotalSpecialCostPrice);
		item.setSpecialCostPrice(totalSpecialCostPrice);
		
						
		
//		//计算SubTotal=Unit Price * Quantity
		BigDecimal subTotal = showCurrentPrice.multiply(totalQuantity);
		item.setSubTotal(subTotal);
		
		
		//总的调整价格
		BigDecimal unitAdjustment = new BigDecimal(0);
		if(newUnitPrice.compareTo(newCurrentPrice) != 0){
			unitAdjustment = newUnitPrice.subtract(newCurrentPrice);
		}
		BigDecimal adjustment = unitAdjustment.multiply(addQuantity);
		BigDecimal showAdjustment = item.getAdjustment().add(new BigDecimal(adjustment.intValue()));
		item.setAdjustment(showAdjustment);
		
		//支付方式
		String payment = item.getPayment();
		String str = paymentMap.get(orderId);
		if(str != null){
			if(str.indexOf("&") >= 0){
				String[] strList = str.split("&");
				for (int i = 0; i < strList.length; i++) {
					if(payment.indexOf(strList[i]) < 0 ){
						if(payment == null || "".equals(payment)){
							payment = strList[i];
						}else{
							payment = payment + "&" + strList[i];
						}
					}
				}
			}else{
				if(payment.indexOf(str) < 0){
					if(payment == null || "".equals(payment)){
						payment = str;
					}else{
						payment = payment + "&" +str;
					}
				}
			}
		}
		item.setPayment(payment);
		
		
		
		BigDecimal recievables = subTotal.add(showAdjustment);
		item.setRecievables(recievables);
		
		
		//销售员
		String salePerson = item.getSalesPerson();
		String newsalePerson = queryDataForReportBean.getSalePerson() == null ? null : queryDataForReportBean.getSalePerson();
		if(newsalePerson != null){
			if(!salePerson.contains(newsalePerson)){
				if(salePerson == null || "".equals(salePerson)){
					item.setSalesPerson(queryDataForReportBean.getSalePerson());
				}else{
					salePerson = salePerson + "&" + queryDataForReportBean.getSalePerson();
					item.setSalesPerson(salePerson);
				}
			}
		}
		
		
		//销售渠道
		String salesChannel = item.getSalesChannel();
		item.setSalesChannel(queryDataForReportBean.getSalesChannel());
		if(salesChannel.contains(queryDataForReportBean.getSalesChannel())){
			if(salesChannel == null || "".equals(salesChannel)){
				item.setSalesChannel(queryDataForReportBean.getSalesChannel());
			}else{
				if(!salesChannel.contains(queryDataForReportBean.getSalesChannel())){
					if( queryDataForReportBean.getSalesChannel() != null){
						salesChannel = salesChannel + "&" + queryDataForReportBean.getSalesChannel();
						item.setSalesChannel(salesChannel);
					}
				}
			}
		}
		return item;
	
	}
	public static List<ExportSalesReport> getOperatorExportSalesReportBeanList(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
			 String beginDate, String endDate,GenericValue userLogin ,HttpServletRequest request) throws Exception, GenericEntityException  {
		
		List<StoreGroupBean> sGroupBeans = (List<StoreGroupBean>)SGandSTOmap.get("SGroup");
		List<StoreRollUpBean> storeRollUpBeans = (List<StoreRollUpBean>)SGandSTOmap.get("SGMember");
		String paymentMethodType = request.getParameter("paymentMethod");
		String  paymentMethodSql = null;
		if(paymentMethodType == null || "".equals(paymentMethodType)){
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE group by ORDER_ID ) opp";
		}else{
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE where PAYMENT_METHOD_TYPE_ID='" + paymentMethodType + "' group by ORDER_ID ) opp";
		}
		
		
		// TODO Auto-generated method stub
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String whereCause = "";
		if (UtilValidate.isNotEmpty(productId)) {
			whereCause += " And oi.PRODUCT_ID = '" + productId + "'";
		}
		for(int k=0;k<sGroupBeans.size();k++)
		{
			StoreGroupBean temp = sGroupBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			else 
			{
				whereCause += " or psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
			}
			if(k==(sGroupBeans.size()-1))
				whereCause+=" ) ";
		}
			
		for(int k=0;k<storeRollUpBeans.size();k++)
		{
			StoreRollUpBean tempSB = storeRollUpBeans.get(k);
			if(k==0)
			{
				whereCause += " And ( oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			else 
			{
				whereCause += " or oh.PRODUCT_STORE_ID = '" + tempSB.getStoreID() + "'";
			}
			if(k==(storeRollUpBeans.size()-1))
				whereCause+=" ) ";
			
		}
		// if(UtilValidate.isNotEmpty(beginTSP)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') >= '" + beginDate + "' ";
		// }
		// if(UtilValidate.isNotEmpty(storeId)){
		whereCause += " And DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') <= '" + endDate + "' ";
		// }
		
		String date = " DATE_FORMAT(opp.CREATED_STAMP,'%Y-%m-%d') >= '" + beginDate + "' ";
		date += " And DATE_FORMAT(opp.CREATED_STAMP,'%Y-%m-%d') <= '" + endDate + "' ";
		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID where " + date ;
		
		
		
		
		
		
		String sql = "  select DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') as orderDate, oi.PRODUCT_ID  as productId, "
				+ " DATE_FORMAT(oh.CREATED_STAMP, '%Y%m') AS month, "
				+ " (select CONCAT(p.BRAND_NAME,' | ',p.INTERNAL_NAME) as productInfo from PRODUCT p where p.PRODUCT_ID =oi.PRODUCT_ID ) as productInfo, "
				+ " (select p.BRAND_NAME from PRODUCT p where p.PRODUCT_ID = oi.PRODUCT_ID) AS brandName , "
				+ " (select p.INTERNAL_NAME from PRODUCT p where p.PRODUCT_ID = oi.PRODUCT_ID) AS internalName, "
				+ " ( select GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) as DESCRIPTION "
				+ " from PRODUCT_FEATURE_APPL c LEFT JOIN PRODUCT_FEATURE d on c.PRODUCT_FEATURE_ID=d.PRODUCT_FEATURE_ID "
				+ " where c.PRODUCT_ID=oi.PRODUCT_ID )as descr, "
				+ " (select  CONCAT(fpc.DESCRIPTION,'->',spc.DESCRIPTION,'->',tpc.DESCRIPTION) AS category from "
				+ " PRODUCT_CATEGORY fpc left join "
				+ " PRODUCT_CATEGORY spc on "
				+ " fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID "
				+ " left join PRODUCT_CATEGORY tpc on "
				+ " spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID "
				+ " left join PRODUCT_CATEGORY_MEMBER pcm on "
				+ " tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID "
				+ " where PRODUCT_ID = oi.PRODUCT_ID) as category, "
				+ " (SELECT fpc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS fpcCategory, "
				+ " (SELECT spc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS spcCategory, "
				+ "	(SELECT tpc.DESCRIPTION FROM PRODUCT_CATEGORY fpc LEFT JOIN PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID LEFT JOIN PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID WHERE PRODUCT_ID = oi.PRODUCT_ID) AS tpcCategory, "
				+ " oi.UNIT_PRESET_COST as defaultCostPrice , oi.EGATEE_COST_PRICE as egateeCostPrice , oi.SPECIAL_COST_PRICE as specialCostPrice , oi.RETAIL_COST_PRICE as retailCostPrice , "
				+ " oi.ORDER_ID as orderId , oi.QUANTITY as quantity , oi.UNIT_PRICE as unitPrice , oh.STATUS_ID as status , "
				+ " oi.CURRENT_PRICE as currentPrice, oi.UNIT_S_PRESET_COST as unitSPresetCost , oi.UNIT_PRESET_COST as unitPresetCost , "
				+ " (select CONCAT(p.FIRST_NAME, ' ', p.LAST_NAME) as salePerson from PERSON p where PARTY_ID = oh.SALES_ID) as salePerson, "
				+ " (select psg.PRODUCT_STORE_GROUP_NAME from PRODUCT_STORE_GROUP psg where psg.PRODUCT_STORE_GROUP_ID = oh.PRODUCT_STORE_GROUP_ID) as salesChannel, "
				+ " psg.PRODUCT_STORE_GROUP_ID as groupId , oh.CURRENCY_UOM as currencyUom , "
				+ " (select ps.STORE_NAME as shopName from PRODUCT_STORE ps where PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID) AS storeName , "
				+ " (SELECT psg.PRODUCT_STORE_GROUP_NAME FROM (select * from PRODUCT_STORE_GROUP where PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP') psg left join PRODUCT_STORE_GROUP_MEMBER psgm on psg.PRODUCT_STORE_GROUP_ID = psgm.PRODUCT_STORE_GROUP_ID where psgm.PRODUCT_STORE_ID=oh.PRODUCT_STORE_ID) as shopGroup , "
				+ " (select per.FIRST_NAME_LOCAL from PERSON per where PARTY_ID = oh.BUYER_ID) as customerName , "
				+ " psg.PRODUCT_STORE_GROUP_ID as groupId , oh.CURRENCY_UOM as currencyUom , oh.PRODUCT_STORE_ID  as storeId "
				+ " from ORDER_ITEM oi,ORDER_HEADER oh,PRODUCT_STORE_GROUP psg,PRODUCT_STORE_GROUP_MEMBER psgm , "
				+ paymentMethodSql
				+ " where oi.ORDER_ID = oh.ORDER_ID "
				+ " AND oi.ORDER_ID = opp.ORDER_ID "
				+ " AND (oh.STATUS_ID = 'ORDER_COMPLETED' or oh.STATUS_ID = 'ORDER_PAYMENT' or oh.STATUS_ID = 'ORDER_SHIPPED' or oh.STATUS_ID = 'ORDER_COMPLETED_W_P') "
				+ " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID "
				+ " And oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' "
				+ " AND psgm.PRODUCT_STORE_GROUP_ID=psg.PRODUCT_STORE_GROUP_ID "
				+ " And psg.PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP' "
				+ whereCause
				+ " order by DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') desc ";
		 
		
		
		ResultSet rs = null;
		ResultSet paymentMaprs = null;
		List<SalesReportBean> salesReportBeanList = new ArrayList<SalesReportBean>();
		List<QueryDataForReportBean> queryDataForReportBeanList = new ArrayList<QueryDataForReportBean>();
		List<ExportSalesReport> exportSalesReportBeanList = new ArrayList<ExportSalesReport>();
		Map<String, String> paymentMap = new HashMap<String, String>();
		try {
			
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> context = new HashMap<String, Object>();
			Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
			String egateeCost = (String) uomInfo.get("egateeCost");
			String specialCost = (String) uomInfo.get("specialCost");
			String retailCost = (String) uomInfo.get("retailCost");
			
			
			// 获得j结果集
			rs = processor.executeQuery(sql);
			paymentMaprs = processor.executeQuery(orderPaymentSql);
			while (paymentMaprs.next()) {
				if(paymentMap.containsKey(paymentMaprs.getString("ORDER_ID"))){
					String paymentOrderId = paymentMaprs.getString("ORDER_ID");
					String paymentDescription = paymentMap.get(paymentOrderId);
					if(paymentDescription.indexOf(paymentMaprs.getString("DESCRIPTION")) < 0){
						paymentDescription = paymentMap.get(paymentOrderId) + "&" + paymentMaprs.getString("DESCRIPTION");
						paymentMap.put(paymentOrderId, paymentDescription);
					}
				}else{
					String paymentOrderId = paymentMaprs.getString("ORDER_ID");
					String paymentDescription = paymentMaprs.getString("DESCRIPTION");
					paymentMap.put(paymentOrderId, paymentDescription);
				}
				
			}
			
			
			while (rs.next()) {
				
				
				
				
				
				ExportSalesReport exportSalesReportBean = new ExportSalesReport();
				exportSalesReportBean.setCompanyName("EGATE-CIRCLE SYNC");
				String shopGroup = rs.getString("shopGroup");
				exportSalesReportBean.setShopGroup(shopGroup);
				String shopName = rs.getString("storeName");
				exportSalesReportBean.setShopName(shopName);
				String orderId = rs.getString("orderId");
				exportSalesReportBean.setOrderId(orderId);
				
				String queryProductId = rs.getString("productId");
				exportSalesReportBean.setProductId(queryProductId);
				
				String orderDate = rs.getString("orderDate");
				exportSalesReportBean.setOrderDate(orderDate);
				String paymentMethod = rs.getString("salesChannel");
				exportSalesReportBean.setPaymentMethod(paymentMethod);
				String customerName = rs.getString("customerName");
				exportSalesReportBean.setCustomerName(customerName);
				String acutalQuantity = rs.getString("quantity");
				exportSalesReportBean.setAcutalQuantity(acutalQuantity);
				
				String currentPrice = rs.getString("currentPrice");
				if(currentPrice == null){
					currentPrice = "0";
				}
				String unitPrice = rs.getString("unitPrice");
				exportSalesReportBean.setUnitPrice(currentPrice);
				String unit = rs.getString("currencyUom");
				exportSalesReportBean.setUnit(unit);
				if(acutalQuantity == null){
					acutalQuantity = "0";
				}
				if(unitPrice == null){
					unitPrice = "0";
				}
				double quantity = Double.parseDouble(acutalQuantity);
				double uPrice = Double.parseDouble(unitPrice);
				double currentPri = Double.parseDouble(currentPrice);
				double amount = quantity * currentPri;
				exportSalesReportBean.setAmount(amount+"");
				
				
				double adjustment = uPrice - currentPri ;
				exportSalesReportBean.setAdjustment(adjustment + "");
				double receivables = adjustment*quantity + amount ;
				exportSalesReportBean.setReceivables(receivables + "");
//				exportSalesReportBean.setPurchaseOrSalesLedger("");
				String salePerson = rs.getString("salePerson");
				exportSalesReportBean.setSalePerson(salePerson);
				String month = rs.getString("month");
				exportSalesReportBean.setMonth(month);
				String correctedName = rs.getString("internalName");
				exportSalesReportBean.setCorrectedName(correctedName);
				String brand = rs.getString("brandName");
				exportSalesReportBean.setBrand(brand);
				exportSalesReportBean.setModel(correctedName);
				String description = rs.getString("descr");
				exportSalesReportBean.setDescription(description);
				String category = rs.getString("fpcCategory");
				exportSalesReportBean.setCategory(category);
				String subCategory = rs.getString("spcCategory");
				exportSalesReportBean.setSubCategory(subCategory);
				String thirdSubCategory = rs.getString("tpcCategory");
				exportSalesReportBean.setThirdSubCategory(thirdSubCategory);
				
				String unitDefaultCost = rs.getString("defaultCostPrice");
				exportSalesReportBean.setUnitDefaultCost(unitDefaultCost);
				if(unitDefaultCost == null){
					unitDefaultCost = "0";
				}
				double unitDefaultCostPrice = Double.parseDouble(unitDefaultCost);
				double totalDefaultCost = unitDefaultCostPrice * quantity;
				exportSalesReportBean.setTotalDefaultCost(totalDefaultCost + "");
				
				String unitEgateeCost = rs.getString("egateeCostPrice");
				if(unitEgateeCost == null){
					double egateeCostRate = Double.parseDouble(egateeCost);
					unitEgateeCost = egateeCostRate*unitDefaultCostPrice + "";
				}
				double totalEgateeCostPrice = Double.parseDouble(unitEgateeCost);
				double totalEgateeCost = totalEgateeCostPrice * quantity;
				exportSalesReportBean.setUnitEgateeCost(unitEgateeCost);
				exportSalesReportBean.setTotalEgateeCost(totalEgateeCost + "");
				
				String unitSpecialCost = rs.getString("specialCostPrice");
				if(unitSpecialCost == null){
					double specialCostRate = Double.parseDouble(specialCost);
					unitSpecialCost = specialCostRate*unitDefaultCostPrice +"";
				}
				double totalSpecialCostPrice = Double.parseDouble(unitSpecialCost);
				double totalSpecialCost = totalSpecialCostPrice * quantity;
				exportSalesReportBean.setUnitSpecialCost(unitSpecialCost);
				exportSalesReportBean.setTotalSpecialCost(totalSpecialCost + "");
				
				String unitRetailCost = rs.getString("retailCostPrice");
				if(unitRetailCost == null){
					double retailCostRate = Double.parseDouble(retailCost);
					unitRetailCost = retailCostRate*unitDefaultCostPrice + "";
				}
				double totalRetailCostPrice = Double.parseDouble(unitSpecialCost);
				double totalRetailCost = totalRetailCostPrice * quantity;
				exportSalesReportBean.setUnitRetailCost(unitRetailCost);
				exportSalesReportBean.setTotalRetailCost(totalRetailCost + "");
				String salesMethodType = rs.getString("salesChannel");
				exportSalesReportBean.setSalesMethodType(salesMethodType);
				exportSalesReportBean.setSalesMethod("线下");
				exportSalesReportBean.Init();
				exportSalesReportBeanList.add(exportSalesReportBean);
				
			}
			
			
			
			
		} finally {
			if (processor != null) {
				try {
					processor.close();
				} catch (GenericDataSourceException e) {
					e.printStackTrace();
				}
				processor = null;
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
		}
		return exportSalesReportBeanList;
		
		
	}*/
	
}