package org.ofbiz.report.report.helper.sales;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.report.report.been.ExportSalesReport;
import org.ofbiz.report.report.been.ProductBeen;
import org.ofbiz.report.report.been.QueryDataForReportBean;
import org.ofbiz.report.report.been.ReportCategoryBean;
import org.ofbiz.report.report.been.SalesFooterBean;
import org.ofbiz.report.report.been.SalesReportBean;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;

import javolution.util.FastList;

public class BaseSalesReportHelper {
	// 0 cash,1 cheque,2POS,3MoneyBack,4transfer
		public static final String PAYMENT_METHOD_CASH = "CASH";
		public static final String PAYMENT_METHOD_CHEQUE = "CHEQUE";
		public static final String PAYMENT_METHOD_POS = "POS";
		public static final String PAYMENT_METHOD_MOBILEMONEY = "MOBILEMONEY";
		public static final String PAYMENT_METHOD_TRANSFER = "EFT_ACCOUNT";
		public static final String PAYMENT_METHOD_CREDIT = "CREDIT";
		public static final String PAYMENT_METHOD_BANKCARD = "BANK_CARK";
		
	public Map<String, Object> searchMarketByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = ServiceUtil.returnSuccess(); // 返回值map
		return map;
	}

	public Map<String, Object> exportReport(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = ServiceUtil.returnSuccess(); // 返回值map
		return map;
	}
	
	
	public static List<GenericValue> getStoreRoleBuUserLogin(GenericDelegator delegator, GenericValue userLogin)
			throws GenericEntityException {
		String partyId = userLogin.getString("partyId");// 登陆用户所属partyID

		// 查询party所属店铺条件
		List<EntityExpr> expressions = new ArrayList<EntityExpr>();
		expressions.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "STORE_MANAGER"));
		expressions.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
		EntityConditionList<EntityExpr> exprList = EntityCondition.makeCondition(expressions, EntityOperator.AND);
		
		// party所属店铺
		List<GenericValue> findList = delegator.findList("ProductStoreRole", exprList, null, null, null, false);
		findList = EntityUtil.filterByDate(findList, true);
		return findList;
	}
	


	public static List<SalesFooterBean> searchFooterBean(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
			 String beginDate, String endDate ,HttpServletRequest request) throws Exception, GenericEntityException {
		// TODO Auto-generated method stub
		
		String paymentMethodType = request.getParameter("paymentMethod");
		String  paymentMethodSql = null;
		if(paymentMethodType == null || "".equals(paymentMethodType)){
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE group by ORDER_ID ) opp";
		}else{
			paymentMethodSql = "(select * from ORDER_PAYMENT_PREFERENCE where PAYMENT_METHOD_TYPE_ID='" + paymentMethodType + "' group by ORDER_ID ) opp";
		}
		
		
		List<StoreGroupBean> sGroupBeans =  (List<StoreGroupBean>)SGandSTOmap.get("SGroup");
		List<StoreRollUpBean> storeRollUpBeans = (List<StoreRollUpBean>)SGandSTOmap.get("SGMember");
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
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date beginTime = dateFormat.parse(beginDate);
		beginDate = sdf.format(beginTime);
		Date endtiem = dateFormat.parse(endDate);
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(endtiem); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天
	    endtiem=calendar.getTime();   //这个时间就是日期往后推一天的结果 
		endDate = sdf.format(endtiem);
		
		String rawOffset = request.getParameter("rawOffset"); 
		String createDateSql = "(select date_add(oh.ORDER_DATE, interval "+ Long.parseLong(rawOffset)*1000 +" microsecond))";
		
		
		whereCause += " And " + createDateSql + " >= '" + beginDate + "' ";
		whereCause += " And " + createDateSql + " <= '" + endDate + "' ";

		
//		String sql = " SELECT "+
//			  " orderDate,payMethod,sum(everymoney) eachMoney "+
//			  " FROM ("+
//			  	" SELECT "+
//			  		" DATE_FORMAT(oh.CREATED_STAMP,'%Y-%m-%d') AS orderDate,opp.PAYMENT_METHOD_TYPE_ID AS payMethod,sum(oi.UNIT_PRICE * oi.QUANTITY) AS everymoney "+
//				" FROM "+
//				" ORDER_HEADER AS oh,ORDER_ITEM oi,PRODUCT_STORE_GROUP psg,PRODUCT_STORE_GROUP_MEMBER psgm,"
////				chenshihua	2017-7-5
////				+ "ORDER_PAYMENT_PREFERENCE AS opp "
//				+ " (SELECT * FROM ORDER_PAYMENT_PREFERENCE GROUP BY ORDER_ID) AS opp " 
//				+ " Where oh.ORDER_ID = opp.ORDER_ID "+
//				" AND oi.ORDER_ID = oh.ORDER_ID "+
//			    " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID "+
//				" AND psgm.PRODUCT_STORE_GROUP_ID = psg.PRODUCT_STORE_GROUP_ID "
//				+ " AND psg.PRODUCT_STORE_GROUP_TYPE_ID = 'STORE_GROUP'" + " AND oh.ORDER_TYPE_ID='SALES_ORDER'"
//				
////				+ " AND oh.STATUS_ID='ORDER_COMPLETED'"  
////				chenshihua	2017-7-5	
//				+ " AND (oh.STATUS_ID = 'ORDER_COMPLETED' or oh.STATUS_ID = 'ORDER_PAYMENT' or oh.STATUS_ID = 'ORDER_SHIPPED') "
//				+ " AND oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM'"
//				+ whereCause 
//				+ " group by DATE_FORMAT(oh.CREATED_STAMP, '%Y%m%d'),opp.PAYMENT_METHOD_TYPE_ID"
//				+ " Order by DATE_FORMAT(oh.CREATED_STAMP, '%Y%m%d'),oi.ORDER_ID,oi.ORDER_ITEM_SEQ_ID) t"
//				+ " group by orderDate,payMethod ";
				
//		AND (oh.STATUS_ID = 'ORDER_COMPLETED' or oh.STATUS_ID = 'ORDER_PAYMENT' or oh.STATUS_ID = 'ORDER_SHIPPED')
//		String sql = " select DATE_FORMAT(oh.CREATED_STAMP, '%Y-%m-%d') as orderDate ,opp.PAYMENT_METHOD_TYPE_ID as payMethod,sum(opp.MAX_AMOUNT) as eachMoney"
//				+ " from ORDER_HEADER as oh , ORDER_PAYMENT_PREFERENCE as opp , ORDER_ITEM oi ,PRODUCT_STORE_GROUP psg, PRODUCT_STORE_GROUP_MEMBER psgm "
//				+ " where oh.ORDER_ID=opp.ORDER_ID " + " AND oh.ORDER_ID=oi.ORDER_ID"
//				+ " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID"
//				+ " AND psgm.PRODUCT_STORE_GROUP_ID = psg.PRODUCT_STORE_GROUP_ID"
//				+ " AND psg.PRODUCT_STORE_GROUP_TYPE_ID = 'STORE_GROUP'" + " AND oh.ORDER_TYPE_ID='SALES_ORDER'"
//				+ " AND oh.STATUS_ID='ORDER_COMPLETED'" + " AND oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM'"
//				+ whereCause + " group by DATE_FORMAT(oh.CREATED_STAMP, '%Y%m%d'),opp.PAYMENT_METHOD_TYPE_ID"
//				+ " Order by DATE_FORMAT(oh.CREATED_STAMP, '%Y%m%d')";

		String sql =" SELECT DATE_FORMAT(" + createDateSql + ", '%d/%m/%Y') AS orderDate , "
				+ " opp.PAYMENT_METHOD_TYPE_ID as payMethod , "
				+ " SUM(oi.UNIT_PRICE * oi.QUANTITY) AS everymoney FROM "
				+ " ORDER_HEADER AS oh, ORDER_ITEM oi, PRODUCT_STORE_GROUP psg, PRODUCT_STORE_GROUP_MEMBER psgm, "
				+ " (SELECT * FROM ORDER_PAYMENT_PREFERENCE GROUP BY ORDER_ID) AS opp " 
 				+ " WHERE oh.ORDER_ID = opp.ORDER_ID "
 				+ " AND oi.ORDER_ID = oh.ORDER_ID "
 				+ " AND oh.PRODUCT_STORE_ID = psgm.PRODUCT_STORE_ID "
 				+ " AND psgm.PRODUCT_STORE_GROUP_ID = psg.PRODUCT_STORE_GROUP_ID "
 				+ " AND psg.PRODUCT_STORE_GROUP_TYPE_ID = 'STORE_GROUP' "
 				+ " AND oh.ORDER_TYPE_ID = 'SALES_ORDER' "
 				+ " AND (oh.STATUS_ID = 'ORDER_COMPLETED' "
 				+ " OR oh.STATUS_ID = 'ORDER_PAYMENT' "
 				+ " OR oh.STATUS_ID = 'ORDER_SHIPPED' "
 				+ " OR oh.STATUS_ID = 'ORDER_COMPLETED_W_P'"
 				+ " OR oh.STATUS_ID = 'ORDER_PACKED' ) "
 				+ " AND oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' "
 				+  whereCause
 				+ " GROUP BY DATE_FORMAT(" + createDateSql + ", '%Y%m%d') , opp.PAYMENT_METHOD_TYPE_ID "
 				+ " ORDER BY orderDate ";
		
		
		
		
		ResultSet rs = null;
		List<SalesFooterBean> markertFooterBeens = new ArrayList<SalesFooterBean>();
		try {
			Map<String, BigDecimal[]> footerMap = new LinkedHashMap<String, BigDecimal[]>();
			// 获得j结果集
			rs = processor.executeQuery(sql);
			BigDecimal[] moneyArr = null; // 0 cash,1 cheque,2 POS,3MoneyBack,4transfer
			while (rs.next()) {
				String orderDate = rs.getString("orderDate");
				String payMethod = rs.getString("payMethod");
				BigDecimal everymoney = rs.getBigDecimal("everymoney");
				
				if(markertFooterBeens.size() == 0){
					SalesFooterBean salesFooterBean = new SalesFooterBean(); 
					salesFooterBean.setDate(orderDate);
					salesFooterBean = setTotayPrice(salesFooterBean , payMethod , everymoney);
					salesFooterBean.setTodayCollection(everymoney + "");
					salesFooterBean.init();
					markertFooterBeens.add(salesFooterBean);
				}else{
					boolean falg = true;
					for (SalesFooterBean item : markertFooterBeens) {
						if(item.getDate().equals(orderDate)){
							item = setTotayPrice(item , payMethod , everymoney);
							item.setTodayCollection(new BigDecimal(item.getTodayCollection()).add(everymoney) + "");
							falg = false;
							break;
						}
					}
					if(falg){
						SalesFooterBean salesFooterBean = new SalesFooterBean(); 
						salesFooterBean.setDate(orderDate);
						salesFooterBean = setTotayPrice(salesFooterBean , payMethod , everymoney);
						salesFooterBean.setTodayCollection(everymoney  + "");
						salesFooterBean.init();
						markertFooterBeens.add(salesFooterBean);
					}
					
				}
				
			}
//			while (rs.next()) {
//				String orderDate = rs.getString("orderDate");
//
//				if (null == footerMap || !footerMap.containsKey(orderDate)) {
//					moneyArr = new BigDecimal[5];
//					for (int i = 0; i < moneyArr.length; i++) {
//						moneyArr[i] = new BigDecimal(0);
//					}
//				}
//				accumulateMoney(moneyArr, rs.getString("payMethod"), rs.getBigDecimal("eachMoney"));
//				footerMap.put(orderDate, moneyArr);
//			}
//			if (null != footerMap) {
//				for (Entry<String, BigDecimal[]> footerMapSet : footerMap.entrySet()) {
//					BigDecimal totalMoney = footerMapSet.getValue()[0].add(footerMapSet.getValue()[1])
//							.add(footerMapSet.getValue()[2]).add(footerMapSet.getValue()[3])
//							.add(footerMapSet.getValue()[4]);
//					markertFooterBeens.add(new SalesFooterBean(footerMapSet.getKey(), totalMoney,
//							footerMapSet.getValue()[0], footerMapSet.getValue()[1], footerMapSet.getValue()[2],
//							footerMapSet.getValue()[3], footerMapSet.getValue()[4]));
//				}
//			}

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
		return markertFooterBeens;
	}


	private static SalesFooterBean setTotayPrice(SalesFooterBean salesFooterBean, String payMethod, BigDecimal everymoney) {
		if (payMethod.equals(PAYMENT_METHOD_CASH)) {
			salesFooterBean.setTodayCash(everymoney + "");
		}
		if (payMethod.equals(PAYMENT_METHOD_CHEQUE)) {
			salesFooterBean.setTodayCheque(everymoney + "");
		}
		if (payMethod.equals(PAYMENT_METHOD_POS)) {
			salesFooterBean.setTodayPos(everymoney + "");
		}
		if (payMethod.equals(PAYMENT_METHOD_MOBILEMONEY)) {
			salesFooterBean.setTodayMoneyBack(everymoney + "");
		}
		if (payMethod.equals(PAYMENT_METHOD_TRANSFER)) {
			salesFooterBean.setTodayTransfer(everymoney + "");
		}
		if(payMethod.equals(PAYMENT_METHOD_CREDIT)){
			salesFooterBean.setTodayCredit(everymoney + "");
		}
		if(payMethod.equals(PAYMENT_METHOD_BANKCARD)){
			salesFooterBean.setTodayBrakCard(everymoney + "");
		}
		return salesFooterBean;
	}

	/**
	 * 一天内累加金额
	 * 
	 * @param moneyArr
	 * @param payMethod
	 * @param money
	 */
	private static void accumulateMoney(BigDecimal[] moneyArr, String payMethod, BigDecimal money) {
//		System.out.println(PAYMENT_METHOD_CASH);
//		System.out.println(payMethod);
		if (payMethod.equals(PAYMENT_METHOD_CASH)) {
			moneyArr[0]=moneyArr[0].add(money);
		}
		if (payMethod.equals(PAYMENT_METHOD_CHEQUE)) {
			moneyArr[1]=moneyArr[1].add(money);
		}
		if (payMethod.equals(PAYMENT_METHOD_POS)) {
			moneyArr[2]=moneyArr[2].add(money);
		}
		if (payMethod.equals(PAYMENT_METHOD_MOBILEMONEY)) {
			moneyArr[3]=moneyArr[3].add(money);
		}
		if (payMethod.equals(PAYMENT_METHOD_TRANSFER)) {
			moneyArr[4]=moneyArr[4].add(money);
		}
	}
	/**
	 * 联想查询产品
	 * @param delegator
	 * @param productId
	 * @return
	 * @throws GenericEntityException 
	 */
	public static List<GenericValue> searchProductsByLikeID(GenericDelegator delegator, String productId) throws GenericEntityException {
		//设置条件
		List<EntityExpr> entityConditions =  FastList.newInstance();
		entityConditions.add(EntityCondition.makeCondition("productId",EntityOperator.LIKE,"%"+productId+"%"));
		entityConditions.add(EntityCondition.makeCondition(EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, null), EntityOperator.OR, EntityCondition.makeCondition("isActive", EntityOperator.EQUALS, "Y")));
		entityConditions.add(EntityCondition.makeCondition("isVariant",EntityOperator.EQUALS,"N"));
		
		//设置要显示的字段
		Set<String> fieldsToSelect = UtilMisc.toSet("productId","internalName");

		List<GenericValue> productGVs = delegator.findList("Product", EntityCondition.makeCondition(UtilMisc.toList(entityConditions),
				EntityOperator.AND), fieldsToSelect, null, null, false);
	
		return productGVs;
	}
	
	public static EntityListIterator getSGMemberIterator(GenericDelegator delegator, EntityCondition conditions) throws GenericEntityException {
		EntityListIterator SGMIterator;
		DynamicViewEntity dynamicView = new DynamicViewEntity();
		dynamicView.addMemberEntity("PSGM", "ProductStoreGroupMember");
		dynamicView.addMemberEntity("PSG", "ProductStoreGroup");
		dynamicView.addMemberEntity("PS", "ProductStore");

		dynamicView.addAlias("PSGM", "productStoreId");
		dynamicView.addAlias("PSGM", "productStoreGroupId");
		dynamicView.addAlias("PSG", "productStoreGroupId");
		dynamicView.addAlias("PSG", "productStoreGroupName");
		dynamicView.addAlias("PSG", "productStoreGroupTypeId");
		// dynamicView.addAlias("PS", "productStoreId");
		dynamicView.addAlias("PS", "storeName");

		dynamicView.addViewLink("PSGM", "PSG", Boolean.valueOf(false),
				ModelKeyMap.makeKeyMapList("productStoreGroupId"));
		dynamicView.addViewLink("PSGM", "PS", Boolean.valueOf(false),
				ModelKeyMap.makeKeyMapList("productStoreId"));			
		
		SGMIterator = delegator.findListIteratorByCondition(dynamicView,conditions,null,null,UtilMisc.toList("productStoreId"),null);
	
		return SGMIterator;
	}
	
	
	
	
	
	
	public static SalesReportBean init(Map<String, String> paymentMap , GenericDelegator delegator , QueryDataForReportBean queryDataForReportBean , String  egateeCost , String specialCost , String retailCost) throws GenericEntityException{
		SalesReportBean salesReportBean = new SalesReportBean();
		salesReportBean.setOrderDate(queryDataForReportBean.getOrderDate());
		salesReportBean.setCategory(queryDataForReportBean.getCategroy());
		salesReportBean.setProductId(queryDataForReportBean.getProductId());
		
		if(queryDataForReportBean.getDescr() != null){
		    salesReportBean.setDescription(queryDataForReportBean.getProductInfo() + " | " + queryDataForReportBean.getDescr());
		}else{
		    salesReportBean.setDescription(queryDataForReportBean.getProductInfo());
		}
		
		BigDecimal quantity = queryDataForReportBean.getQuantity();
		salesReportBean.setQuantity(quantity+"");
		//unitPrice 卖出去时的渠道价    
		BigDecimal unitPrice = queryDataForReportBean.getUnitPrice();
		salesReportBean.setUnitPrice(unitPrice + "");
		
		//currentPrice卖出去时的实际价格（单价）
		BigDecimal currentPrice = queryDataForReportBean.getCurrentPrice();
		salesReportBean.setCurrentPrice(currentPrice + "");
		
		
		//四个不同的价格
		BigDecimal defaultCostPrice = queryDataForReportBean.getDefaultCostPrice()==null ? new BigDecimal(0) : queryDataForReportBean.getDefaultCostPrice();
		BigDecimal egateeCostPrice = queryDataForReportBean.getEgateeCostPrice()==null ? defaultCostPrice.multiply(new BigDecimal(egateeCost)) : queryDataForReportBean.getEgateeCostPrice();
		BigDecimal specialCostPrice = queryDataForReportBean.getSpecialCostPrice()==null ? defaultCostPrice.multiply(new BigDecimal(specialCost)) : queryDataForReportBean.getSpecialCostPrice();
		BigDecimal retailCostPrice = queryDataForReportBean.getRetailCostPrice()==null ? defaultCostPrice.multiply(new BigDecimal(retailCost)) : queryDataForReportBean.getRetailCostPrice();
		//平均价格
		salesReportBean.setDefaultCostPrice(defaultCostPrice + "");
		salesReportBean.setEgateeCostPrice(egateeCostPrice + "");
		salesReportBean.setSpecialCostPrice(specialCostPrice + "");
		salesReportBean.setRetailCostPrice(retailCostPrice + "");
		//总价
		BigDecimal totalDefaultCostPrice = defaultCostPrice.multiply(quantity);
		BigDecimal totalEgateeCostPrice = egateeCostPrice.multiply(quantity);
		BigDecimal totalSpecialCostPrice = specialCostPrice.multiply(quantity);
		BigDecimal totalRetailCostPrice = retailCostPrice.multiply(quantity);
		salesReportBean.setTotalDefaultCostPrice(totalDefaultCostPrice + "");
		salesReportBean.setTotalEgateeCostPrice(totalEgateeCostPrice + "");
		salesReportBean.setTotalSpecialCostPrice(totalSpecialCostPrice + "");
		salesReportBean.setTotalRetailCostPrice(totalRetailCostPrice + "");
		
		
		
		
		//SubTotal = Unit Price * Quantity
		BigDecimal subTotal = queryDataForReportBean.getCurrentPrice().multiply(queryDataForReportBean.getQuantity());
		salesReportBean.setSubTotal(subTotal + "");
		
		//Adjustment为所有单笔Adjustment累加之和
//		BigDecimal adjustment = queryDataForReportBean.getUnitPrice().subtract(queryDataForReportBean.getCurrentPrice());
//		if(adjustment.compareTo(new BigDecimal(0)) != 0){
//			adjustment = adjustment.multiply(queryDataForReportBean.getQuantity());
//		}
//		salesReportBean.setAdjustment(adjustment);
		BigDecimal adjustment = new BigDecimal(0);
		if(queryDataForReportBean.getUnitPrice().compareTo(queryDataForReportBean.getCurrentPrice()) != 0){
			adjustment = queryDataForReportBean.getUnitPrice().subtract(queryDataForReportBean.getCurrentPrice());
			adjustment = adjustment.multiply(queryDataForReportBean.getQuantity());
		}
		salesReportBean.setAdjustment(adjustment + "");
			
		//Payment填支付方式，Cash或者credit，暂时不考虑一笔订单有多种支付方式的情况
		String payment = paymentMap.get(queryDataForReportBean.getOrderId());
		salesReportBean.setPayment(payment);
		
		//Recievables应收账款，即调价后的实收 ； Recievables = SubTotal - Adjustment
		BigDecimal recievables = subTotal.add(adjustment);
		salesReportBean.setRecievables(recievables + "");
		
		//销售员
		salesReportBean.setSalesPerson(queryDataForReportBean.getSalePerson());
		//销售渠道
		salesReportBean.setSalesChannel(queryDataForReportBean.getSalesChannel());
		//店铺id
		salesReportBean.setStoreId(queryDataForReportBean.getStoreId());
		
		salesReportBean.init();
		return salesReportBean;
	}
	
	
	public static SalesReportBean edit(Map<String, String> paymentMap , GenericDelegator delegator , SalesReportBean item , QueryDataForReportBean queryDataForReportBean , String  egateeCost , String specialCost , String retailCost ) throws GenericEntityException{
		String orderId = queryDataForReportBean.getOrderId();
		//新增产品总数totalQuantity
		BigDecimal orgQuantity = new BigDecimal(item.getQuantity());
		BigDecimal addQuantity = queryDataForReportBean.getQuantity() == null ? new BigDecimal(0) : queryDataForReportBean.getQuantity();
		BigDecimal totalQuantity = addQuantity.add(orgQuantity);
		
		//加权平均UnitPrice
		BigDecimal showUnitPrice = new BigDecimal(item.getUnitPrice());
		BigDecimal newUnitPrice = queryDataForReportBean.getUnitPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getUnitPrice();
		showUnitPrice = (showUnitPrice.multiply(orgQuantity).add(newUnitPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setUnitPrice(showUnitPrice + "");
		item.setQuantity(totalQuantity + "");
		
		
		//加权平均currentPrice
		BigDecimal showCurrentPrice = new BigDecimal(item.getCurrentPrice());
		BigDecimal newCurrentPrice = queryDataForReportBean.getCurrentPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getCurrentPrice();
		showCurrentPrice = (showCurrentPrice.multiply(orgQuantity).add(newCurrentPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setCurrentPrice(showCurrentPrice + "");
		
		
		//四个成本价的加权平均
		
		//defaultCost
		BigDecimal showDefaultCostPrice = new BigDecimal(item.getDefaultCostPrice());
		BigDecimal newDefaultCostPrice = queryDataForReportBean.getDefaultCostPrice() == null ? new BigDecimal(0) : queryDataForReportBean.getDefaultCostPrice();
		showDefaultCostPrice = (showDefaultCostPrice.multiply(orgQuantity).add(newDefaultCostPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setDefaultCostPrice(showDefaultCostPrice + "");
		//egateeCost
		BigDecimal showEgateeCostPrice = new BigDecimal(item.getEgateeCostPrice());
		BigDecimal newEgateeCostPrice = queryDataForReportBean.getEgateeCostPrice() == null ? newDefaultCostPrice.multiply(new BigDecimal(egateeCost)) : queryDataForReportBean.getEgateeCostPrice();
		showEgateeCostPrice = (showEgateeCostPrice.multiply(orgQuantity).add(newEgateeCostPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setEgateeCostPrice(showEgateeCostPrice + "");
		
		//specialCostPrice
		BigDecimal showSpecialCostPrice = new BigDecimal(item.getSpecialCostPrice());
		BigDecimal newSpecialCostPrice = queryDataForReportBean.getSpecialCostPrice() == null ? newDefaultCostPrice.multiply(new BigDecimal(specialCost)) : queryDataForReportBean.getSpecialCostPrice();
		showSpecialCostPrice = (showSpecialCostPrice.multiply(orgQuantity).add(newSpecialCostPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setSpecialCostPrice(showSpecialCostPrice + "");
		
		//RetailCostPrice
		BigDecimal showRetailCostPrice = new BigDecimal(item.getRetailCostPrice());
		BigDecimal newRetailCostPrice = queryDataForReportBean.getRetailCostPrice() == null ? newDefaultCostPrice.multiply(new BigDecimal(retailCost)) : queryDataForReportBean.getRetailCostPrice();
		showRetailCostPrice = (showRetailCostPrice.multiply(orgQuantity).add(newRetailCostPrice.multiply(addQuantity))).divide(totalQuantity , 2 , RoundingMode.HALF_UP);
		item.setRetailCostPrice(showRetailCostPrice + "");
		
		
		//四个成本价的总价
		//defaultCost
		BigDecimal totalDefaultCostPrice = new BigDecimal(item.getTotalDefaultCostPrice());
		BigDecimal newTotalDefaultCostPrice = newDefaultCostPrice.multiply(addQuantity);
		totalDefaultCostPrice = totalDefaultCostPrice.add(newTotalDefaultCostPrice);
		item.setTotalDefaultCostPrice(totalDefaultCostPrice + "");
		
		//egateeCost
		BigDecimal totalEgateeCostPrice = new BigDecimal(item.getTotalEgateeCostPrice());
		BigDecimal newTotalEgateeCostPrice = newSpecialCostPrice.multiply(addQuantity);
		totalEgateeCostPrice = totalEgateeCostPrice.add(newTotalEgateeCostPrice);
		item.setTotalEgateeCostPrice(totalEgateeCostPrice + "");
		//specialCostPrice
		BigDecimal totalSpecialCostPrice = new BigDecimal(item.getTotalSpecialCostPrice());
		BigDecimal newTotalSpecialCostPrice = newSpecialCostPrice.multiply(addQuantity);
		totalSpecialCostPrice = totalSpecialCostPrice.add(newTotalSpecialCostPrice);
		item.setTotalSpecialCostPrice(totalSpecialCostPrice + "");
		
		//RetailCostPrice
		BigDecimal totalRetailCostPrice = new BigDecimal(item.getRetailCostPrice());
		BigDecimal newTotalRetailCostPrice = newRetailCostPrice.multiply(addQuantity);
		totalRetailCostPrice = totalRetailCostPrice.add(newTotalRetailCostPrice);
		item.setTotalRetailCostPrice(totalRetailCostPrice + "");
		
		
		
		
//		//计算SubTotal=Unit Price (currentPrice)* Quantity
		BigDecimal subTotal = showCurrentPrice.multiply(totalQuantity);
		item.setSubTotal(subTotal + "");
		
		
		//总的调整价格
//		BigDecimal unitAdjustment = newUnitPrice.subtract(newCurrentPrice);
//		BigDecimal adjustment = new BigDecimal(0);
//		if(unitAdjustment.compareTo(new BigDecimal(0)) != 0){
//			adjustment = unitAdjustment.multiply(addQuantity);
//		}
//		BigDecimal showAdjustment = item.getAdjustment().add(new BigDecimal(adjustment.intValue()));
//		item.setAdjustment(showAdjustment);
		
		BigDecimal unitAdjustment = new BigDecimal(0);
		if(newUnitPrice.compareTo(newCurrentPrice) != 0){
			unitAdjustment = newUnitPrice.subtract(newCurrentPrice);
		}
		BigDecimal adjustment = unitAdjustment.multiply(addQuantity);
		BigDecimal showAdjustment = new BigDecimal(item.getAdjustment()).add(new BigDecimal(adjustment.intValue()));
		item.setAdjustment(showAdjustment + "");
		
		
		
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
		item.setRecievables(recievables + "");
		
		
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
//		item.setSalesChannel(queryDataForReportBean.getSalesChannel());
//		if(salesChannel.contains(queryDataForReportBean.getSalesChannel())){
//			if(salesChannel == null || "".equals(salesChannel)){
//				item.setSalesChannel(queryDataForReportBean.getSalesChannel());
//			}else{
//				if(!salesChannel.contains(queryDataForReportBean.getSalesChannel())){
//					if( queryDataForReportBean.getSalesChannel() != null){
//						salesChannel = salesChannel + "&" + queryDataForReportBean.getSalesChannel();
//						item.setSalesChannel(salesChannel);
//					}
//				}
//			}
//		}
		
		if(!salesChannel.contains(queryDataForReportBean.getSalesChannel())){
			salesChannel = salesChannel + "&" + queryDataForReportBean.getSalesChannel();
			item.setSalesChannel(salesChannel);
		}else{
			item.setSalesChannel(queryDataForReportBean.getSalesChannel());
		}
		return item;
	
	}
	

	public static List<SalesReportBean> getSalesReport(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
			 String beginDate, String endDate,GenericValue userLogin,HttpServletRequest request) throws Exception, GenericEntityException  {
		
//		List<StoreGroupBean> sGroupBeans =  (List<StoreGroupBean>)SGandSTOmap.get("SGroup");
//		List<StoreAndGroupBean> storeAndGroupBeans = (List<StoreAndGroupBean>)SGandSTOmap.get("SGMember");
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
//		for(int k=0;k<sGroupBeans.size();k++)
//		{
//			StoreGroupBean temp = sGroupBeans.get(k);
//			if(k==0)
//			{
//				whereCause += " And ( psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
//			}
//			else 
//			{
//				whereCause += " or psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
//			}
//			if(k==(sGroupBeans.size()-1))
//				whereCause+=" ) ";
//		}
			
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
		
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date beginTime = dateFormat.parse(beginDate);
		beginDate = sdf.format(beginTime);
		Date endtiem = dateFormat.parse(endDate);
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(endtiem); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天
	    endtiem=calendar.getTime();   //这个时间就是日期往后推一天的结果 
		endDate = sdf.format(endtiem);
		
		String rawOffset = request.getParameter("rawOffset"); 
		String createDateSql = "(select date_add(oh.ORDER_DATE, interval "+ Long.parseLong(rawOffset)*1000 +" microsecond))";
		
		// if(UtilValidate.isNotEmpty(beginTSP)){
		whereCause += " And " + createDateSql + " >= '" + beginDate + "' ";
		// }
		// if(UtilValidate.isNotEmpty(storeId)){
		whereCause += " And " + createDateSql + " <= '" + endDate + "' ";
		// }
		
		String date = " opp.CREATED_STAMP >= '" + beginDate + "' ";
		date += " And opp.CREATED_STAMP <= '" + endDate + "' ";
//		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID where " + date ;
		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID ";
		
		String categorySql = " SELECT  "
				+ " pcm.PRODUCT_ID AS productId, "
				+ " tpc.DESCRIPTION AS tcategory, "
				+ " fpc.DESCRIPTION AS fcategory, "
				+ " spc.DESCRIPTION AS scategory "
				+ " FROM "
				+ " PRODUCT_CATEGORY fpc "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID ";
		
		
		List<GenericValue> productList = delegator.findAll("Product");
		Map<String, ProductBeen> productBeenMap = new HashMap<String, ProductBeen>();
		for (GenericValue genericValue : productList) {
			ProductBeen productBeen = new ProductBeen();
			productBeen.setProductId(genericValue.getString("productId"));
			productBeen.setBrandName(genericValue.getString("brandName"));
			productBeen.setInternalName(genericValue.getString("internalName"));
			productBeenMap.put(genericValue.getString("productId"), productBeen);
		}
		
		
		

		
		String sql = " select DATE_FORMAT(" + createDateSql + ", '%d/%m/%Y') AS orderDate, "
				+ " oi.PRODUCT_ID AS productId, "
				+ " DATE_FORMAT(" + createDateSql + ", '%m/%Y') AS month, "
//				+ " CONCAT(p.BRAND_NAME, ' | ', p.INTERNAL_NAME) AS productInfo, "
//				+ " p.BRAND_NAME AS brandName, "
//				+ " p.INTERNAL_NAME AS internalName, "
				+ " (SELECT  "
				+ " GROUP_CONCAT(DISTINCT DESCRIPTION "
				+ " ORDER BY PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION "
				+ " FROM "
				+ " PRODUCT_FEATURE_APPL c "
				+ " LEFT JOIN "
				+ " PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
				+ " WHERE "
				+ " c.PRODUCT_ID = oi.PRODUCT_ID) AS descr, "
				+ " oi.UNIT_PRESET_COST AS defaultCostPrice, "
				+ " oi.EGATEE_COST_PRICE AS egateeCostPrice, "
				+ " oi.SPECIAL_COST_PRICE AS specialCostPrice, "
				+ " oi.RETAIL_COST_PRICE AS retailCostPrice, "
				+ " oi.ORDER_ID AS orderId, "
				+ " oi.QUANTITY AS quantity, "
				+ " oi.UNIT_PRICE AS unitPrice, "
				+ " oh.STATUS_ID AS status, "
				+ " oi.CURRENT_PRICE AS currentPrice, "
				+ " oi.UNIT_S_PRESET_COST AS unitSPresetCost, "
				+ " oi.UNIT_PRESET_COST AS unitPresetCost, "
				+ " (SELECT  "
				+ " CONCAT(p.FIRST_NAME, ' ', p.LAST_NAME) AS salePerson "
				+ " FROM "
				+ " PERSON p "
				+ " WHERE "
				+ " PARTY_ID = oh.SALES_ID) AS salePerson, "
				+ " oh.CURRENCY_UOM AS currencyUom, "
				+ " (SELECT  "
				+ " ps.STORE_NAME AS shopName "
				+ " FROM "
				+ " PRODUCT_STORE ps "
				+ " WHERE "
				+ " PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID) AS storeName, "
				+ " (SELECT  "
				+ " per.FIRST_NAME_LOCAL "
				+ " FROM "
				+ " PERSON per "
				+ " WHERE "
				+ " PARTY_ID = oh.BUYER_ID) AS customerName, "
				+ " (select psg.PRODUCT_STORE_GROUP_NAME from PRODUCT_STORE_GROUP psg where psg.PRODUCT_STORE_GROUP_ID = oh.PRODUCT_STORE_GROUP_ID) as salesChannel, "
				+ " oh.CURRENCY_UOM AS currencyUom, "
				+ " oh.PRODUCT_STORE_ID AS storeId "
				+ " from ORDER_ITEM oi   "
				+ " join ORDER_HEADER oh on  oi.ORDER_ID = oh.ORDER_ID  "
				+ " join " + paymentMethodSql
				+ " on oi.ORDER_ID = opp.ORDER_ID "
				+ " join PRODUCT_STORE ps on ps.PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID  "
//				+ " join PRODUCT p on p.PRODUCT_ID = oi.PRODUCT_ID "
				+ " where  "
				+ " (oh.STATUS_ID = 'ORDER_COMPLETED' "
				+ " OR oh.STATUS_ID = 'ORDER_PAYMENT' "
				+ " OR oh.STATUS_ID = 'ORDER_SHIPPED' "
				+ " OR oh.STATUS_ID = 'ORDER_COMPLETED_W_P' "
				+ " OR oh.STATUS_ID = 'ORDER_PACKED') "
				+ " AND oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' "
				+ whereCause
				+ " ORDER BY " + createDateSql + " DESC ";
		
		
		ResultSet categoryRs = null;
		categoryRs = processor.executeQuery(categorySql);
		Map<String, ReportCategoryBean> categoryMap = new HashMap<String, ReportCategoryBean>();
		while (categoryRs.next()) {
			ReportCategoryBean reportCategoryBean = new ReportCategoryBean();
			reportCategoryBean.setProductId(categoryRs.getString("productId"));
			reportCategoryBean.setfCategory(categoryRs.getString("fcategory"));
			reportCategoryBean.setsCategory(categoryRs.getString("scategory"));
			reportCategoryBean.settCategory(categoryRs.getString("tcategory"));
			categoryMap.put(categoryRs.getString("productId"), reportCategoryBean);
		}
		
		
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
//				queryDataForReportBean.setProductInfo(rs.getString("productInfo"));
				ProductBeen productBeen = productBeenMap.get(rs.getString("productId"));
				queryDataForReportBean.setProductInfo(productBeen.getBrandName() + " | " + productBeen.getInternalName());
				
				queryDataForReportBean.setDescr(rs.getString("descr"));
				
				ReportCategoryBean reportCategoryBean = categoryMap.get(rs.getString("productId"));
				
				
				queryDataForReportBean.setCategroy(reportCategoryBean.gettCategory());
				
				
				queryDataForReportBean.setOrderId(rs.getString("orderId"));
				queryDataForReportBean.setQuantity(rs.getBigDecimal("quantity"));
				queryDataForReportBean.setCurrentPrice(rs.getBigDecimal("currentPrice"));
				queryDataForReportBean.setUnitPrice(rs.getBigDecimal("unitPrice"));
				queryDataForReportBean.setUnitSPresetCost(rs.getBigDecimal("unitSPresetCost"));
				queryDataForReportBean.setUnitPresetCost(rs.getBigDecimal("unitPresetCost"));
//				queryDataForReportBean.setGroupId(rs.getString("groupId"));
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
				boolean flag = true;
				if(salesReportBeanList.size() > 0){
					for (SalesReportBean item : salesReportBeanList) {
						if(item.getProductId().equals(queryDataForReportBean.getProductId()) && item.getOrderDate().equals(queryDataForReportBean.getOrderDate()) && item.getStoreId().equals(queryDataForReportBean.getStoreId())){
							
							
							item = edit(paymentMap , delegator, item, queryDataForReportBean , egateeCost , specialCost , retailCost);
							flag = false;
						}
					}
					if(flag){
						salesReportBeanList.add(init(paymentMap , delegator, queryDataForReportBean, egateeCost , specialCost , retailCost));
					}
				}else{
					
					salesReportBeanList.add(init(paymentMap , delegator, queryDataForReportBean , egateeCost , specialCost , retailCost));
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
	
	
	
	
	public static List<ExportSalesReport> getExportSalesReportBeanList(GenericDelegator delegator, String productId, Map<String, Object> SGandSTOmap,
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
		
		
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String whereCause = "";
		if (UtilValidate.isNotEmpty(productId)) {
			whereCause += " And oi.PRODUCT_ID = '" + productId + "'";
		}
	
//		for(int k=0;k<sGroupBeans.size();k++)
//		{
//			StoreGroupBean temp = sGroupBeans.get(k);
//			if(k==0)
//			{
//				whereCause += " And ( psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
//			}
//			else 
//			{
//				whereCause += " or psg.PRODUCT_STORE_GROUP_ID = '" + temp.getsGroupId() + "'";
//			}
//			if(k==(sGroupBeans.size()-1))
//				whereCause+=" ) ";
//		}
			
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
		
		
	    
	    
	    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date beginTime = dateFormat.parse(beginDate);
		beginDate = sdf.format(beginTime);
		Date endtiem = dateFormat.parse(endDate);
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(endtiem); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天
	    endtiem=calendar.getTime();   //这个时间就是日期往后推一天的结果 
		endDate = sdf.format(endtiem);
	    
		String rawOffset = request.getParameter("rawOffset"); 
		String createDateSql = "(select date_add(oh.ORDER_DATE, interval "+ Long.parseLong(rawOffset)*1000 +" microsecond))";
		
		// if(UtilValidate.isNotEmpty(beginTSP)){
		whereCause += " And " + createDateSql + " >= '" + beginDate + "' ";
		// }
		// if(UtilValidate.isNotEmpty(storeId)){
		whereCause += " And " + createDateSql + " <= '" + endDate + "' ";
		// }
		
		String date = " opp.CREATED_STAMP >= '" + beginDate + "' ";
		date += " And opp.CREATED_STAMP <= '" + endDate + "' ";
//		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID where " + date ;
		String orderPaymentSql = "  select * from ORDER_PAYMENT_PREFERENCE opp left join PAYMENT_METHOD_TYPE pmt on opp.PAYMENT_METHOD_TYPE_ID = pmt.PAYMENT_METHOD_TYPE_ID ";
		
		
		
		
		
	
		
		List<GenericValue> productList = delegator.findAll("Product");
		Map<String, ProductBeen> productBeenMap = new HashMap<String, ProductBeen>();
		for (GenericValue genericValue : productList) {
			ProductBeen productBeen = new ProductBeen();
			productBeen.setProductId(genericValue.getString("productId"));
			productBeen.setBrandName(genericValue.getString("brandName"));
			productBeen.setInternalName(genericValue.getString("internalName"));
			productBeenMap.put(genericValue.getString("productId"), productBeen);
		}
		
		
		
		String storeGruopSql = " select psgm.PRODUCT_STORE_ID as productStoreId , psgm.PRODUCT_STORE_GROUP_ID ,"
				+ " psg.PRODUCT_STORE_GROUP_NAME as groupName "
				+ " from PRODUCT_STORE_GROUP_MEMBER psgm "
				+ " left join PRODUCT_STORE_GROUP psg "
				+ " on psg.PRODUCT_STORE_GROUP_ID = psgm.PRODUCT_STORE_GROUP_ID "
				+ " where psg.PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP' ";
		ResultSet storeGruopRs = null;
		storeGruopRs = processor.executeQuery(storeGruopSql);
		Map<String, String> storeGruopMap = new HashMap<String, String>();
		while (storeGruopRs.next()) {
			storeGruopMap.put(storeGruopRs.getString("productStoreId"), storeGruopRs.getString("groupName"));
		}
		
		
		
		String categorySql = " SELECT  "
				+ " pcm.PRODUCT_ID AS productId, "
				+ " tpc.DESCRIPTION AS tcategory, "
				+ " fpc.DESCRIPTION AS fcategory, "
				+ " spc.DESCRIPTION AS scategory "
				+ " FROM "
				+ " PRODUCT_CATEGORY fpc "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY spc ON fpc.PRODUCT_CATEGORY_ID = spc.PRIMARY_PARENT_CATEGORY_ID "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY tpc ON spc.PRODUCT_CATEGORY_ID = tpc.PRIMARY_PARENT_CATEGORY_ID "
				+ " RIGHT JOIN "
				+ " PRODUCT_CATEGORY_MEMBER pcm ON tpc.PRODUCT_CATEGORY_ID = pcm.PRODUCT_CATEGORY_ID ";
		
		
		
		String sql = " select DATE_FORMAT(" + createDateSql + ", '%d/%m/%Y') AS orderDate, "
				+ " oi.PRODUCT_ID AS productId, "
				+ " DATE_FORMAT(" + createDateSql + ", '%m/%Y') AS month, "
//				+ " CONCAT(p.BRAND_NAME, ' | ', p.INTERNAL_NAME) AS productInfo, "
//				+ " p.BRAND_NAME AS brandName, "
//				+ " p.INTERNAL_NAME AS internalName, "
				+ " (SELECT  "
				+ " GROUP_CONCAT(DISTINCT DESCRIPTION "
				+ " ORDER BY PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION "
				+ " FROM "
				+ " PRODUCT_FEATURE_APPL c "
				+ " LEFT JOIN "
				+ " PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
				+ " WHERE "
				+ " c.PRODUCT_ID = oi.PRODUCT_ID) AS descr, "
				+ " oi.UNIT_PRESET_COST AS defaultCostPrice, "
				+ " oi.EGATEE_COST_PRICE AS egateeCostPrice, "
				+ " oi.SPECIAL_COST_PRICE AS specialCostPrice, "
				+ " oi.RETAIL_COST_PRICE AS retailCostPrice, "
				+ " oi.ORDER_ID AS orderId, "
				+ " oi.QUANTITY AS quantity, "
				+ " oi.UNIT_PRICE AS unitPrice, "
				+ " oh.STATUS_ID AS status, "
				+ " oi.CURRENT_PRICE AS currentPrice, "
				+ " oi.UNIT_S_PRESET_COST AS unitSPresetCost, "
				+ " oi.UNIT_PRESET_COST AS unitPresetCost, "
				+ " (SELECT  "
				+ " CONCAT(p.FIRST_NAME, ' ', p.LAST_NAME) AS salePerson "
				+ " FROM "
				+ " PERSON p "
				+ " WHERE "
				+ " PARTY_ID = oh.SALES_ID) AS salePerson, "
				+ " oh.CURRENCY_UOM AS currencyUom, "
				+ " (SELECT  "
				+ " ps.STORE_NAME AS shopName "
				+ " FROM "
				+ " PRODUCT_STORE ps "
				+ " WHERE "
				+ " PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID) AS storeName, "
				+ " (SELECT  "
				+ " per.FIRST_NAME_LOCAL "
				+ " FROM "
				+ " PERSON per "
				+ " WHERE "
				+ " PARTY_ID = oh.BUYER_ID) AS customerName, "
				+ " (select psg.PRODUCT_STORE_GROUP_NAME from PRODUCT_STORE_GROUP psg where psg.PRODUCT_STORE_GROUP_ID = oh.PRODUCT_STORE_GROUP_ID) as salesChannel, "
				+ " oh.CURRENCY_UOM AS currencyUom, "
				+ " oh.PRODUCT_STORE_ID AS storeId "
				+ " from ORDER_ITEM oi   "
				+ " join ORDER_HEADER oh on  oi.ORDER_ID = oh.ORDER_ID  "
				+ " join " + paymentMethodSql
				+ " on oi.ORDER_ID = opp.ORDER_ID "
				+ " join PRODUCT_STORE ps on ps.PRODUCT_STORE_ID = oh.PRODUCT_STORE_ID  "
//				+ " join PRODUCT p on p.PRODUCT_ID = oi.PRODUCT_ID "
				+ " where  "
				+ " (oh.STATUS_ID = 'ORDER_COMPLETED' "
				+ " OR oh.STATUS_ID = 'ORDER_PAYMENT' "
				+ " OR oh.STATUS_ID = 'ORDER_SHIPPED' "
				+ " OR oh.STATUS_ID = 'ORDER_COMPLETED_W_P' "
				+ " OR oh.STATUS_ID = 'ORDER_PACKED') "
				+ " AND oi.ORDER_ITEM_TYPE_ID = 'PRODUCT_ORDER_ITEM' "
				+ whereCause
				+ " ORDER BY " + createDateSql + " DESC ";
		
		
		ResultSet categoryRs = null;
		categoryRs = processor.executeQuery(categorySql);
		Map<String, ReportCategoryBean> categoryMap = new HashMap<String, ReportCategoryBean>();
		while (categoryRs.next()) {
			ReportCategoryBean reportCategoryBean = new ReportCategoryBean();
			reportCategoryBean.setProductId(categoryRs.getString("productId"));
			reportCategoryBean.setfCategory(categoryRs.getString("fcategory"));
			reportCategoryBean.setsCategory(categoryRs.getString("scategory"));
			reportCategoryBean.settCategory(categoryRs.getString("tcategory"));
			categoryMap.put(categoryRs.getString("productId"), reportCategoryBean);
		}
		
		
		
		ResultSet rs = null;
		ResultSet paymentMaprs = null;
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
			//支付方式
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
				String storeId = rs.getString("storeId");
				String shopGroup = storeGruopMap.get(storeId);
//				String shopGroup = rs.getString("shopGroup");
				exportSalesReportBean.setShopGroup(shopGroup);
				
				
				String shopName = rs.getString("storeName");
				exportSalesReportBean.setShopName(shopName);
				String orderId = rs.getString("orderId");
				exportSalesReportBean.setOrderId(orderId);
				String queryProductId = rs.getString("productId");
				exportSalesReportBean.setProductId(queryProductId);
				String orderDate = rs.getString("orderDate");
				exportSalesReportBean.setOrderDate(orderDate);
//				String paymentMethod = rs.getString("salesChannel");
				
				
				
				//Payment填支付方式，Cash或者credit，暂时不考虑一笔订单有多种支付方式的情况
				String payment = paymentMap.get(orderId);
				
				exportSalesReportBean.setPaymentMethod(payment);
				String customerName = rs.getString("customerName");
				exportSalesReportBean.setCustomerName(customerName);
				String acutalQuantity = rs.getString("quantity");
				exportSalesReportBean.setAcutalQuantity(acutalQuantity);
				String currentPrice = rs.getString("currentPrice");
				if(currentPrice == null){
					currentPrice = "0";
				}
				double currentPri = Double.parseDouble(currentPrice);
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
				double price = Double.parseDouble(unitPrice);
				double amount = currentPri * quantity;
				exportSalesReportBean.setAmount(amount+"");
				
				
				double adjustment = price - currentPri ;
				exportSalesReportBean.setAdjustment(adjustment*quantity + "");
				double receivables = adjustment*quantity + amount ;
				exportSalesReportBean.setReceivables(receivables + "");
//				exportSalesReportBean.setPurchaseOrSalesLedger("");
				String salePerson = rs.getString("salePerson");
				exportSalesReportBean.setSalePerson(salePerson);
				String month = rs.getString("month");
				exportSalesReportBean.setMonth(month);
				ProductBeen productBeen = productBeenMap.get(queryProductId);
//				String correctedName = rs.getString("internalName");
				String correctedName = productBeen.getInternalName();
				exportSalesReportBean.setCorrectedName(correctedName);
//				String brand = rs.getString("brandName");
				String brand = productBeen.getBrandName();
				exportSalesReportBean.setBrand(brand);
				exportSalesReportBean.setModel(correctedName);
				String description = rs.getString("descr");
				exportSalesReportBean.setDescription(description);
				
				ReportCategoryBean reportCategoryBean = categoryMap.get(rs.getString("productId"));
				
				
				
//				String category = rs.getString("fpcCategory");
				
				exportSalesReportBean.setCategory(reportCategoryBean.getfCategory());
//				String subCategory = rs.getString("spcCategory");
				exportSalesReportBean.setSubCategory(reportCategoryBean.getsCategory());
//				String thirdSubCategory = rs.getString("tpcCategory");
				exportSalesReportBean.setThirdSubCategory(reportCategoryBean.gettCategory());
				
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
				exportSalesReportBean.setSalesMethod("Offline");
				
				
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
	}
	
	
	
}
