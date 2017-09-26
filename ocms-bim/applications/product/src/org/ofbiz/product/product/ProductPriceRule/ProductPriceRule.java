package org.ofbiz.product.product.ProductPriceRule;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.product.product.bean.FindProductPriceRuleBean;
import org.ofbiz.product.product.bean.LastmodifieledDataBean;
import org.ofbiz.product.product.bean.ProductpriceLogBean;
import org.ofbiz.security.Security;
import org.ofbiz.service.ServiceUtil;

public class ProductPriceRule {

	
	public static final String resource = "ProductErrorUiLabels";
	
	
	//manage查看价格  
	public static Map<String, Object> priceRuleFindProductPrice(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			//查看log的权限
			if(!hasEntityPermission(request, "FIND_LOG")){
				 return ServiceUtil.returnError("you have no permission ");
			}
			
			
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			HttpSession session = request.getSession();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String username = (String) userLogin.get("userLoginId");
			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username), false);
			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId")));
			List<String> storeIds = new ArrayList<String>();
			for (GenericValue genericValue : resultList) {
				String queryStoreId = genericValue.getString("productStoreId");
				if(!storeIds.contains(queryStoreId)){
					storeIds.add(queryStoreId);
				}
			}
			
			
			String productId = request.getParameter("productId");
			String storeId = request.getParameter("storeId");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			String pageN = request.getParameter("pageNum");
			if(pageN == null){
				pageN = "1";
			}
			String orderBy = request.getParameter("orderBy");
			String isAsc = request.getParameter("isAsc");
			
			Long rawOffset = Long.parseLong(request.getParameter("rawOffset"))*1000;
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
			
			if(endtiem.compareTo(beginTime) == -1){
				return ServiceUtil.returnError("please input right date");
			}
			
			//是否有产品id
			String productIdSql = null;
			if(productId != null && !"".equals(productId)){
				productIdSql = " pprt.PRODUCT_ID = '" + productId +"' and ";
			}
			if(productIdSql == null){
				productIdSql = "";
			}
			
			//选择店铺
			String storeIdSql = null;
			if(storeId != null && !"".equals(storeId)){
				storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeId +"' ";
			}else{
				for (int i = 0; i < storeIds.size(); i++) {
					if(i == 0){
						if(storeIds.size() == 1){
							if(storeIds.size() == 1){
								storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
							}else{
								storeIdSql = "( pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
							}
//							storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
						}else{
							storeIdSql = "( pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
						}
//						storeIdSql = "( pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
					}else if(i == storeIds.size()-1){
						storeIdSql = storeIdSql + "  or pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "')";
					}else{
						storeIdSql = storeIdSql + " or pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
					}
				}
			}
			
			//选择创建日期
			String beginDateSql = null;
			if(beginDate != null && !"".equals(beginDate)){
				
				beginDateSql = "and "+"(SELECT DATE_ADD(ppr.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+" >= '" + beginDate + "' ";
			}else{
				beginDateSql = "";
			}
			String endDateSql = null;
			if(endDate != null && !"".equals(endDate)){
				endDateSql = "and "+"(SELECT DATE_ADD(ppr.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+" <= '" + endDate + "' ";
			}else{
				endDateSql = "";
			}
			
			//排序
			String orderSql = null;
			if("productId".equals(orderBy)){
				orderSql = " order by  productId ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}else if("description".equals(orderBy)){
				orderSql = " order by brandName , internalName , descr ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}else if("store".equals(orderBy)){
				orderSql = " order by  storeName ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}
//			else if("currentPrice".equals(orderBy)){
//				orderSql = " order by  currentPrice ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			//最后的更改时间一最近的创建时间来计算
//			}else if("lastModifiedDate".equals(orderBy)){
//				orderSql = " order by  createDate ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			}
			if(orderSql== null){
				orderSql ="";
			}

			//分页
			String limitcont = " limit " + (Integer.parseInt(pageN) - 1)*20 + " , 20 ";

			
			String selectCount = " SELECT count(*) AS totalNum FROM ";
			String selectAll = " SELECT * FROM ";
			
			
			
			String sql = " (SELECT  "
					+ " a.PRODUCT_PRICE_RULE_ID, "
					+ " (SELECT p.BRAND_NAME FROM PRODUCT p WHERE p.PRODUCT_ID = a.PRODUCT_ID) AS brandName, "
					+ " (SELECT   p.INTERNAL_NAME FROM PRODUCT p WHERE p.PRODUCT_ID = a.PRODUCT_ID) AS internalName, "
					+ " (SELECT GROUP_CONCAT(DISTINCT DESCRIPTION ORDER BY PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION "
					+ " FROM PRODUCT_FEATURE_APPL c LEFT JOIN PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
					+ " WHERE c.PRODUCT_ID = a.PRODUCT_ID) AS descr, "
					+ " a.PRODUCT_ID AS productId, "
					+ " a.PRODUCT_STORE_ID as productStoreId, "
					+" (select ps.STORE_NAME from PRODUCT_STORE ps where ps.PRODUCT_STORE_ID = a.PRODUCT_STORE_ID) as storeName, "
					+ " a.AMOUNT AS currentPrice, "
					+ " DATE_FORMAT("+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+", '%d/%m/%Y') AS createDate, "
					+ " "+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+" AS createTime "
					+ " FROM "
					+ " (SELECT ppr.PRODUCT_PRICE_RULE_ID, "
					+ " pprt.PRODUCT_ID, "
					+ " pprt.PRODUCT_STORE_ID, "
					+ " ppa.AMOUNT, "
					+ " ppr.CREATED_TX_STAMP"
					+ " FROM "
					+ " PRODUCT_PRICE_RULE AS ppr JOIN (SELECT ppc1.PRODUCT_PRICE_RULE_ID AS PRODUCT_PRICE_RULE_ID, "
					+ " ppc1.COND_VALUE AS PRODUCT_ID, "
					+ " ppc2.COND_VALUE AS PRODUCT_STORE_ID "
					+ " FROM "
					+ " PRODUCT_PRICE_COND ppc1 "
					+ " JOIN PRODUCT_PRICE_COND ppc2 ON (ppc1.PRODUCT_PRICE_RULE_ID = ppc2.PRODUCT_PRICE_RULE_ID "
					+ " AND ppc1.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_ID' "
					+ " AND ppc2.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_STORE_ID')) AS pprt ON (pprt.PRODUCT_PRICE_RULE_ID = ppr.PRODUCT_PRICE_RULE_ID) "
					+ " JOIN PRODUCT_PRICE_ACTION AS ppa ON (ppr.PRODUCT_PRICE_RULE_ID = ppa.PRODUCT_PRICE_RULE_ID "
					+ " AND ppa.PRODUCT_PRICE_ACTION_TYPE_ID = 'PRICE_POL') "
					+ " WHERE " + productIdSql + storeIdSql + beginDateSql + endDateSql
					+ " AND ppr.FROM_DATE <= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
					+ " AND (ppr.THRU_DATE >= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
					+ " OR ppr.THRU_DATE IS NULL) "
					+ " ORDER BY "+"(SELECT DATE_ADD(ppr.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+" DESC) AS a "
					+ " GROUP BY a.PRODUCT_ID , a.PRODUCT_STORE_ID) as pp " + orderSql;
			
			
			
			String lastModifusql = " (SELECT * FROM  "
					+ " (SELECT  "
					+ " a.PRODUCT_PRICE_RULE_ID, "
					+ " a.PRODUCT_ID AS productId, "
					+ " a.PRODUCT_STORE_ID AS productStoreId, "
					+ " DATE_FORMAT("+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+", '%d/%m/%Y') AS createDate, "
					+ " (SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND))"+" AS createTime "
					+ " FROM "
					+ " (SELECT  "
					+ " ppr.PRODUCT_PRICE_RULE_ID, "
					+ " pprt.PRODUCT_ID, "
					+ " pprt.PRODUCT_STORE_ID, "
					+ " ppa.AMOUNT, "
					+ " ppr.THRU_DATE, "
					+ " ppr.FROM_DATE, "
					+ " ppr.CREATED_TX_STAMP "
					+ " FROM "
					+ " (SELECT  "
					+ " ppc1.PRODUCT_PRICE_RULE_ID AS PRODUCT_PRICE_RULE_ID, "
					+ " ppc1.COND_VALUE AS PRODUCT_ID, "
					+ " ppc2.COND_VALUE AS PRODUCT_STORE_ID "
					+ " FROM "
					+ " PRODUCT_PRICE_COND ppc1 "
					+ " JOIN PRODUCT_PRICE_COND ppc2 ON (ppc1.PRODUCT_PRICE_RULE_ID = ppc2.PRODUCT_PRICE_RULE_ID "
					+ " AND ppc1.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_ID' "
					+ " AND ppc2.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_STORE_ID')) AS pprt  "
					+ " JOIN PRODUCT_PRICE_RULE AS ppr ON (pprt.PRODUCT_PRICE_RULE_ID = ppr.PRODUCT_PRICE_RULE_ID) "
					+ " JOIN PRODUCT_PRICE_ACTION AS ppa ON (ppr.PRODUCT_PRICE_RULE_ID = ppa.PRODUCT_PRICE_RULE_ID "
					+ " AND ppa.PRODUCT_PRICE_ACTION_TYPE_ID = 'PRICE_POL') "
					+ " WHERE "
					+ productIdSql + storeIdSql + beginDateSql + endDateSql
					+ " AND ppr.FROM_DATE <= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
					+ " AND (ppr.THRU_DATE >= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
					+ " OR ppr.THRU_DATE IS NULL) "
					+ " ORDER BY (SELECT DATE_ADD(ppr.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) DESC) AS a) pppc group by pppc.productId , pppc.productStoreId) as las" ;
			
			ResultSet rs = null;
			rs = processor.executeQuery(selectCount + lastModifusql);
			Map<String, Object> map = ServiceUtil.returnSuccess();
			Integer totalNum = 0;
			while (rs.next()) {
				totalNum = Integer.parseInt(rs.getString("totalNum"));
			}
			
			//分页
			Integer endPage = 0;
			Integer resultCode = 1;
			Integer startPage = 0;
			Integer totalPage = 0;
			
			if(pageN == null || "".equals(pageN)){
				pageN = "1";
			}
			Integer pageNum = Integer.parseInt(pageN);
			
			if(totalNum <= 1){
				totalPage = 1;
			}else{
				totalPage = (totalNum - 1 ) / 20 + 1;
			}
			if((int) pageNum <= 3){
				startPage = 1;
				if(totalPage >= 5){
					endPage = 5;
				}else{
					endPage = totalPage;
				}
			}else{
				startPage = pageNum - 2;
				if((totalPage - 2 ) < pageNum){
					endPage = totalPage;
				}else{
					endPage = pageNum + 2;
				}
			}
			
			map.put("endPage", endPage);
			map.put("pageNum", pageNum);
			map.put("resultCode", resultCode);
			map.put("startPage", startPage);
			map.put("totalNum", totalNum);
			map.put("totalPage", totalPage);
			
			
			
			
			//最后修改的价格规则
			rs = processor.executeQuery(selectAll +  lastModifusql  + limitcont);
			Map<String, String> lastmodifieledDataMap = new HashMap<String, String>();
			List<LastmodifieledDataBean> LastmodifieledDataBeanList = new ArrayList<LastmodifieledDataBean>();
			while (rs.next()) {
				String LastmodifieledDataProductPriceRuleId = rs.getString("PRODUCT_PRICE_RULE_ID");
				String LastmodifieledDataCreateDate = rs.getString("createDate");
				String LastmodifieledDataProductStoreId = rs.getString("productStoreId");
				String LastmodifieledDataProductId = rs.getString("productId");
				LastmodifieledDataBean lastmodifieledData = new LastmodifieledDataBean();
				lastmodifieledData.setCreateDate(LastmodifieledDataCreateDate);
				lastmodifieledData.setProductId(LastmodifieledDataProductId);
				lastmodifieledData.setProductPriceRuleId(LastmodifieledDataProductPriceRuleId);
				lastmodifieledData.setProductStoreId(LastmodifieledDataProductStoreId);
				LastmodifieledDataBeanList.add(lastmodifieledData);
			}
			
			
			rs = processor.executeQuery(selectAll + sql);
			List<FindProductPriceRuleBean> findProductPricePomotonBeanList = new ArrayList<FindProductPriceRuleBean>();
			int index = 0;
			while (rs.next()) {
				if(index >= 20){
					break;
				}
				FindProductPriceRuleBean findProductPricePomotonBean = new FindProductPriceRuleBean();
				String ruleId = rs.getString("PRODUCT_PRICE_RULE_ID");
				String currentPrice = rs.getString("currentPrice");
				currentPrice = formatTosepara(currentPrice);
				findProductPricePomotonBean.setCurrentPrice(currentPrice);
//				String description = rs.getString("brandName") + " | " +  rs.getString("internalName") + " | " + rs.getString("descr"); 
				
				String description = rs.getString("brandName") + " | " +  rs.getString("internalName"); 
				String descr = rs.getString("descr");
				if(descr != null){
					description = description + " | " + rs.getString("descr");
				}
				
				findProductPricePomotonBean.setDescription(description);
				
				//判断是否是启用状态
				boolean falg = true;
				for (LastmodifieledDataBean item : LastmodifieledDataBeanList) {
					if(item.getProductId().equals(rs.getString("productId")) && item.getProductStoreId().equals(rs.getString("productStoreId")) ){
						findProductPricePomotonBean.setLastModifiedDate(item.getCreateDate());
						falg = false;
						break;
					}
				}
				if(falg){
					continue;
				}
				
				findProductPricePomotonBean.setProducId(rs.getString("productId"));
				findProductPricePomotonBean.setStore(rs.getString("storeName"));
				findProductPricePomotonBean.setStoreId(rs.getString("productStoreId"));
				findProductPricePomotonBeanList.add(findProductPricePomotonBean);
				index ++;
			}
			
			map.put("resultList", findProductPricePomotonBeanList);
			
			return map;
			
		} catch (ParseException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}catch (SQLException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
	}
	
	
	
	//店长查看价格
	public static Map<String, Object> categoryFindProductPrice(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			//店长查看价格规则权限
			if(!hasEntityPermission(request, "FIND")){
				 return ServiceUtil.returnError("you have no permission ");
			}
			
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			HttpSession session = request.getSession();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String username = (String) userLogin.get("userLoginId");
			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username), false);
			List<GenericValue> resultList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("partyId", user.getString("partyId")));
			List<String> storeIds = new ArrayList<String>();
			for (GenericValue genericValue : resultList) {
				String queryStoreId = genericValue.getString("productStoreId");
				if(!storeIds.contains(queryStoreId)){
					storeIds.add(queryStoreId);
				}
			}
			
			
			String productId = request.getParameter("productId");
			String storeId = request.getParameter("storeId");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			String pageN = request.getParameter("pageNum");
			String orderBy = request.getParameter("orderBy");
			String isAsc = request.getParameter("isAsc");
			
			
//			SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
//			
//			Date beginTime = dateFormat.parse(beginDate);
//			Date endtiem = dateFormat.parse(endDate);
////			System.out.println(beginTime);
////			System.out.println(endtiem);
//			if(endtiem.compareTo(beginTime) == -1){
//				return ServiceUtil.returnError("please input right date");
//			}
			
			//是否有产品id
			String productIdSql = null;
			if(productId != null && !"".equals(productId)){
				productIdSql = " pprt.PRODUCT_ID = '" + productId +"' and ";
			}
			if(productIdSql == null){
				productIdSql = "";
			}
			
			//选择店铺
			String storeIdSql = null;
			if(storeId != null && !"".equals(storeId)){
				storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeId +"' ";
			}else{
				for (int i = 0; i < storeIds.size(); i++) {
					if(i == 0){
						if(storeIds.size() == 1){
							storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
						}else{
							storeIdSql = "( pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "' ";
						}
					}else if(i == storeIds.size()-1){
						storeIdSql = storeIdSql + "or pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "')";
					}else{
						storeIdSql = storeIdSql + " or pprt.PRODUCT_STORE_ID = '" + storeIds.get(i) + "'";
					}
				}
			}
			
			
			Long rawOffset = Long.parseLong(request.getParameter("rawOffset"))*1000;
			
			
			//排序
			String orderSql = null;
			if("productId".equals(orderBy)){
				orderSql = " order by  productId ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}else if("description".equals(orderBy)){
				orderSql = " order by brandName , internalName , descr ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}
//			else if("store".equals(orderBy)){
//				orderSql = " order by  storeName ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			}else if("currentPrice".equals(orderBy)){
//				orderSql = " order by  currentPrice ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			//最后的更改时间一最近的创建时间来计算
//			}else if("lastModifiedDate".equals(orderBy)){
//				orderSql = " order by  createDate ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			}
			if(orderSql== null){
				orderSql ="";
			}

			if(pageN == null){
				pageN = "1";
			}
			//分页
			String limitcont = " limit " + (Integer.parseInt(pageN) - 1)*20 + " , 20 ";

			
			String selectCount = " SELECT count(*) AS totalNum FROM ";
			String selectAll = " SELECT * FROM ";
			
			
			String countProductIdSql = null;
			if(productId != null && !"".equals(productId)){
				countProductIdSql = " ii.PRODUCT_ID = '" + productId +"' and ";
			}
			if(countProductIdSql == null){
				countProductIdSql = "";
			}
			
			String countSql = " select "
					+ " count(*) AS totalNum  "
					+ " from   "          
					+ " (SELECT DISTINCT  "
					+ " ii.PRODUCT_ID AS PRODUCT_ID, pp.PRICE AS PRICE , pprt.PRODUCT_STORE_ID   "
					+ " FROM  "
					+ " INVENTORY_ITEM ii  "
					+ " LEFT JOIN PRODUCT_STORE pprt ON ii.FACILITY_ID = pprt.INVENTORY_FACILITY_ID  "
					+ " LEFT JOIN PRODUCT_PRICE pp ON ii.PRODUCT_ID = pp.PRODUCT_ID  "
					+ " WHERE  "  +  countProductIdSql + storeIdSql
					+ " and pp.PRODUCT_STORE_GROUP_ID = 'RETAIL') ips ";
			
			String priceSql = " select  "
					+ " ips.PRODUCT_ID as productId, "
					+ " ips.PRICE as price, "
					+ " ips.PRODUCT_STORE_ID as productStoreId, "
					+ " (SELECT  "
					+ " p.BRAND_NAME "
					+ " FROM "
					+ " PRODUCT p "
					+ " WHERE "
					+ " p.PRODUCT_ID = ips.PRODUCT_ID) AS brandName, "
					+ " (SELECT  "
					+ " p.INTERNAL_NAME "
					+ " FROM "
					+ " PRODUCT p "
					+ " WHERE "
					+ " p.PRODUCT_ID = ips.PRODUCT_ID) AS internalName, "
					+ " (SELECT  "
					+ " GROUP_CONCAT(DISTINCT DESCRIPTION "
					+ " ORDER BY PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION "
					+ " FROM "
					+ " PRODUCT_FEATURE_APPL c "
					+ " LEFT JOIN "
					+ " PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
					+ " WHERE "
					+ " c.PRODUCT_ID = ips.PRODUCT_ID) AS descr "
					+ " from "
					+ " (SELECT DISTINCT "
					+ " ii.PRODUCT_ID AS PRODUCT_ID, pp.PRICE AS PRICE , pprt.PRODUCT_STORE_ID  "
					+ " FROM "
					+ " INVENTORY_ITEM ii "
					+ " LEFT JOIN PRODUCT_STORE pprt ON ii.FACILITY_ID = pprt.INVENTORY_FACILITY_ID "
					+ " LEFT JOIN PRODUCT_PRICE pp ON ii.PRODUCT_ID = pp.PRODUCT_ID "
					+ " WHERE "  +  countProductIdSql + storeIdSql
					+ " and pp.PRODUCT_STORE_GROUP_ID = 'RETAIL') as ips " + orderSql;
			
			
			
			
			
			ResultSet rs = null;
			
			
			
			rs = processor.executeQuery(countSql);
			Map<String, Object> map = ServiceUtil.returnSuccess();
			Integer totalNum = 0;
			while (rs.next()) {
				totalNum = Integer.parseInt(rs.getString("totalNum"));
			}
			
			Integer endPage = 0;
			Integer resultCode = 1;
			Integer startPage = 0;
			Integer totalPage = 0;
			
			if(pageN == null || "".equals(pageN)){
				pageN = "1";
			}
			Integer pageNum = Integer.parseInt(pageN);
			
			if(totalNum <= 1){
				totalPage = 1;
			}else{
				totalPage = (totalNum - 1 ) / 20 + 1;
			}
			if((int) pageNum <= 3){
				startPage = 1;
				if(totalPage >= 5){
					endPage = 5;
				}else{
					endPage = totalPage;
				}
			}else{
				startPage = pageNum - 2;
				if((totalPage - 2 ) < pageNum){
					endPage = totalPage;
				}else{
					endPage = pageNum + 2;
				}
			}
			
			map.put("endPage", endPage);
			map.put("pageNum", pageNum);
			map.put("resultCode", resultCode);
			map.put("startPage", startPage);
			map.put("totalNum", totalNum);
			map.put("totalPage", totalPage);
			
			rs = processor.executeQuery(priceSql + limitcont);
			List<FindProductPriceRuleBean> findProductPricePomotonBeanList = new ArrayList<FindProductPriceRuleBean>();
			int index = 0;
			while (rs.next()) {
				FindProductPriceRuleBean findProductPriceRuleBean = new FindProductPriceRuleBean();
				String queryProductId = rs.getString("productId");
				String queryProductStoreId = rs.getString("productStoreId");
				if(queryProductId != null){
					productIdSql = " pprt.PRODUCT_ID = '" + queryProductId +"' and ";
				}else{
					productIdSql = " ";
				}
				if(queryProductStoreId != null){
					storeIdSql = " pprt.PRODUCT_STORE_ID = '" + queryProductStoreId +"' and ";
				}else{
					storeIdSql = "";
				}
				
				
				
				
				String sql = " select * from  " 
						+ " (SELECT  " 
						+ " ppr.PRODUCT_PRICE_RULE_ID as productPriceRuleId, " 
						+ " pprt.PRODUCT_ID as productId, " 
						+ " pprt.PRODUCT_STORE_ID as productStoreId, " 
						+ " ppa.AMOUNT as amount, " 
						+ " ppr.THRU_DATE, " 
						+ " ppr.FROM_DATE, " 
						+ " (SELECT DATE_ADD(ppr.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) "+ " as CREATED_TX_STAMP " 
						+ " FROM " 
						+ " (SELECT  " 
						+ " ppc1.PRODUCT_PRICE_RULE_ID AS PRODUCT_PRICE_RULE_ID, " 
						+ " ppc1.COND_VALUE AS PRODUCT_ID, " 
						+ " ppc2.COND_VALUE AS PRODUCT_STORE_ID " 
						+ " FROM " 
						+ " PRODUCT_PRICE_COND ppc1 " 
						+ " JOIN PRODUCT_PRICE_COND ppc2 ON (ppc1.PRODUCT_PRICE_RULE_ID = ppc2.PRODUCT_PRICE_RULE_ID " 
						+ " AND ppc1.INPUT_PARAM_ENUM_ID = 'PRIP_PRODUCT_ID' " 
						+ " AND ppc2.INPUT_PARAM_ENUM_ID = 'PRIP_PRODUCT_STORE_ID')) AS pprt  " 
						+ " JOIN PRODUCT_PRICE_RULE AS ppr ON (pprt.PRODUCT_PRICE_RULE_ID = ppr.PRODUCT_PRICE_RULE_ID) " 
						+ " JOIN PRODUCT_PRICE_ACTION AS ppa ON (ppr.PRODUCT_PRICE_RULE_ID = ppa.PRODUCT_PRICE_RULE_ID " 
						+ " AND ppa.PRODUCT_PRICE_ACTION_TYPE_ID = 'PRICE_POL') " 
						+ " WHERE "  + productIdSql + storeIdSql
			            + " ppr.FROM_DATE <= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
			            + " AND (ppr.THRU_DATE >= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
			            + " OR ppr.THRU_DATE IS NULL) " 
			            + " ORDER BY ppr.CREATED_TX_STAMP DESC) pppc " 
			            + " GROUP BY pppc.productId , pppc.productStoreId " ;
				
				String productPriceRuleId =null;
				String productPriceRuleProductId =null;
				String amount =null;
				String productPriceRuleProductStoreId =null;
				ResultSet result = null;
				result = processor.executeQuery(sql);
				while (result.next()) {
//					System.out.println(index++);
					productPriceRuleId = result.getString("productPriceRuleId");
					productPriceRuleProductId = result.getString("productId");
//					System.out.println(productPriceRuleProductId);
					amount = result.getString("amount");
					productPriceRuleProductStoreId = rs.getString("productStoreId");
				}
				String productPriceProductId =rs.getString("productId");
				String productPriceProductStoreId =rs.getString("productStoreId");
				String currentPrice = null;
				if(productPriceProductId.equals(productPriceRuleProductId) && productPriceProductStoreId.equals(productPriceRuleProductStoreId)){
					currentPrice = amount;
//					findProductPriceRuleBean.setCurrentPrice(amount);
				}else{
					currentPrice = rs.getString("price");
//					findProductPriceRuleBean.setCurrentPrice(rs.getString("price"));
				}
				
				currentPrice = formatTosepara(currentPrice);
				findProductPriceRuleBean.setCurrentPrice(currentPrice);
				
				String description = rs.getString("brandName") + " | " +  rs.getString("internalName"); 
				String descr = rs.getString("descr");
				if(descr != null){
					description = description + " | " + rs.getString("descr");
				}
				findProductPriceRuleBean.setDescription(description);
				findProductPriceRuleBean.setProducId(rs.getString("productId"));
				findProductPricePomotonBeanList.add(findProductPriceRuleBean);
			}
			
			map.put("resultList", findProductPricePomotonBeanList);
			
			return map;
		} catch (GenericDataSourceException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (SQLException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
	}
	
	
	
	
	
	public static Map<String, Object> productPriceRuleLog(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			//查看log的权限
			if(!hasEntityPermission(request, "FIND_LOG")){
				 return ServiceUtil.returnError("you have no permission ");
			}
			
			
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			HttpSession session = request.getSession();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String username = (String) userLogin.get("userLoginId");
			GenericValue user = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username), false);
			
			String productId = request.getParameter("productId");
			String storeId = request.getParameter("storeId");
			String pageN = request.getParameter("pageNum");
			if(pageN == null){
				pageN = "1";
			}
			String orderBy = request.getParameter("orderBy");
			String isAsc = request.getParameter("isAsc");
			
			String productIdSql = null;
			if(productId != null && !"".equals(productId)){
				productIdSql = " pprt.PRODUCT_ID = '" + productId +"' and ";
			}
			if(productIdSql == null){
				return ServiceUtil.returnError("cannot get productId");
			}
			
			//选择店铺
			String storeIdSql = null;
			if(storeId != null && !"".equals(storeId)){
				storeIdSql = " pprt.PRODUCT_STORE_ID = '" + storeId +"' ";
			}else{
				return ServiceUtil.returnError("cannot get storeId");
			}
			
			
			//排序
			String orderSql = null;
			if("productId".equals(orderBy)){
				orderSql = " order by  productId ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}else if("description".equals(orderBy)){
				orderSql = " order by brandName , internalName , descr ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}else if("store".equals(orderBy)){
				orderSql = " order by  storeName ";
				if("Y".equals(isAsc)){
					orderSql += " ASC ";
				}else{
					orderSql += " DESC ";
				}
			}
//			else if("currentPrice".equals(orderBy)){
//				orderSql = " order by  currentPrice ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			//最后的更改时间一最近的创建时间来计算
//			}else if("lastModifiedDate".equals(orderBy)){
//				orderSql = " order by  createDate ";
//				if("Y".equals(isAsc)){
//					orderSql += " ASC ";
//				}else{
//					orderSql += " DESC ";
//				}
//			}
			if(orderSql== null){
				orderSql ="";
			}
			
			Long rawOffset = Long.parseLong(request.getParameter("rawOffset"))*1000;
			
			//分页
			String limitcont = " limit " + (Integer.parseInt(pageN) - 1)*20 + " , 20 ";

			
			String selectCount = " SELECT count(*) AS totalNum FROM ";
			String selectAll = " SELECT * FROM ";
			
			
			
			
			String sql = " (SELECT  "
					+ " a.PRODUCT_PRICE_RULE_ID, "
					+ " (SELECT  "
					+ " p.BRAND_NAME "
					+ " FROM "
					+ " PRODUCT p "
					+ " WHERE "
					+ " p.PRODUCT_ID = a.PRODUCT_ID) AS brandName, "
					+ " (SELECT  "
					+ " p.INTERNAL_NAME "
					+ " FROM "
					+ " PRODUCT p "
					+ " WHERE "
					+ " p.PRODUCT_ID = a.PRODUCT_ID) AS internalName, "
					+ " (SELECT  "
					+ " GROUP_CONCAT(DISTINCT DESCRIPTION "
					+ " ORDER BY PRODUCT_FEATURE_TYPE_ID) AS DESCRIPTION "
					+ " FROM "
					+ " PRODUCT_FEATURE_APPL c "
					+ " LEFT JOIN PRODUCT_FEATURE d ON c.PRODUCT_FEATURE_ID = d.PRODUCT_FEATURE_ID "
					+ " WHERE "
					+ " c.PRODUCT_ID = a.PRODUCT_ID) AS descr, "
					+ " a.PRODUCT_ID AS productId, "
					+ " a.PRODUCT_STORE_ID AS productStoreId, "
					+ " (SELECT  "
					+ " ps.STORE_NAME "
					+ " FROM "
					+ " PRODUCT_STORE ps "
					+ " WHERE "
					+ " ps.PRODUCT_STORE_ID = a.PRODUCT_STORE_ID) AS storeName, "
					+ " (select distinct f.FACILITY_NAME from PRODUCT_STORE ps join INVENTORY_ITEM ii "
					+ " on ps.INVENTORY_FACILITY_ID=ii.FACILITY_ID join FACILITY f "
					+ " on f.FACILITY_ID = ii.FACILITY_ID where ps.PRODUCT_STORE_ID = a.PRODUCT_STORE_ID) as facility, "
					+ " a.AMOUNT AS currentPrice , "
					+ " a.CREATED_USER as createdUser ,  "
					+ " DATE_FORMAT("+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) "+ ", '%d/%m/%Y') AS createDate,  "
					+ " DATE_FORMAT(a.FROM_DATE, '%d/%m/%Y') AS fromDate, "
					+ " DATE_FORMAT(a.THRU_DATE, '%d/%m/%Y') AS thruDate, "
					+ " "+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) "+ " AS createTime "
					+ " FROM "
					+ " (SELECT  "
					+ " ppr.PRODUCT_PRICE_RULE_ID, "
					+ " pprt.PRODUCT_ID, "
					+ " pprt.PRODUCT_STORE_ID, "
					+ " ppa.AMOUNT, "
					+ " ppr.THRU_DATE, "
					+ " ppr.FROM_DATE, "
					+ "  ppr.CREATED_USER, "
					+ " ppr.CREATED_TX_STAMP "
					+ " FROM "
					+ " PRODUCT_PRICE_RULE AS ppr "
					+ " JOIN (SELECT  "
					+ " ppc1.PRODUCT_PRICE_RULE_ID AS PRODUCT_PRICE_RULE_ID, "
					+ " ppc1.COND_VALUE AS PRODUCT_ID, "
					+ " ppc2.COND_VALUE AS PRODUCT_STORE_ID "
					+ " FROM "
					+ " PRODUCT_PRICE_COND ppc1 "
					+ " JOIN PRODUCT_PRICE_COND ppc2 ON (ppc1.PRODUCT_PRICE_RULE_ID = ppc2.PRODUCT_PRICE_RULE_ID "
					+ " AND ppc1.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_ID' "
					+ " AND ppc2.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_STORE_ID')) AS pprt ON (pprt.PRODUCT_PRICE_RULE_ID = ppr.PRODUCT_PRICE_RULE_ID) "
					+ " JOIN PRODUCT_PRICE_ACTION AS ppa ON (ppr.PRODUCT_PRICE_RULE_ID = ppa.PRODUCT_PRICE_RULE_ID "
					+ " AND ppa.PRODUCT_PRICE_ACTION_TYPE_ID = 'PRICE_POL') "
					+ " WHERE " + productIdSql + storeIdSql
					+ " ORDER BY ppr.CREATED_TX_STAMP DESC) AS a ) AS re";
			
			
			ResultSet rs = null;
			rs = processor.executeQuery(selectCount + sql);
			Map<String, Object> map = ServiceUtil.returnSuccess();
			Integer totalNum = 0;
			while (rs.next()) {
				totalNum = Integer.parseInt(rs.getString("totalNum"));
			}
			
			Integer endPage = 0;
			Integer resultCode = 1;
			Integer startPage = 0;
			Integer totalPage = 0;
			
			if(pageN == null || "".equals(pageN)){
				pageN = "1";
			}
			Integer pageNum = Integer.parseInt(pageN);
			
			if(totalNum <= 1){
				totalPage = 1;
			}else{
				totalPage = (totalNum - 1 ) / 20 + 1;
			}
			if((int) pageNum <= 3){
				startPage = 1;
				if(totalPage >= 5){
					endPage = 5;
				}else{
					endPage = totalPage;
				}
			}else{
				startPage = pageNum - 2;
				if((totalPage - 2 ) < pageNum){
					endPage = totalPage;
				}else{
					endPage = pageNum + 2;
				}
			}
			map.put("endPage", endPage);
			map.put("pageNum", pageNum);
			map.put("resultCode", resultCode);
			map.put("startPage", startPage);
			map.put("totalNum", totalNum);
			map.put("totalPage", totalPage);
			
			
			
			String lastmodifieldDataSql = " SELECT  "
				+ " a.PRODUCT_PRICE_RULE_ID, "
				+ " (SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) "+ " AS createTime, "
				+ " DATE_FORMAT("+"(SELECT DATE_ADD(a.CREATED_TX_STAMP,INTERVAL " + rawOffset + " MICROSECOND)) "+ ", '%d/%m/%Y') AS createDate "
				+ " FROM "
				+ " (SELECT  "
				+ " ppr.PRODUCT_PRICE_RULE_ID, "
				+ " pprt.PRODUCT_ID, "
				+ " pprt.PRODUCT_STORE_ID, "
				+ " ppa.AMOUNT, "
				+ " ppr.CREATED_TX_STAMP "
				+ " FROM "
				+ " PRODUCT_PRICE_RULE AS ppr "
				+ " JOIN (SELECT  ppc1.PRODUCT_PRICE_RULE_ID AS PRODUCT_PRICE_RULE_ID,ppc1.COND_VALUE AS PRODUCT_ID, ppc2.COND_VALUE AS PRODUCT_STORE_ID "
				+ " FROM PRODUCT_PRICE_COND ppc1 "
				+ " JOIN PRODUCT_PRICE_COND ppc2 ON (ppc1.PRODUCT_PRICE_RULE_ID = ppc2.PRODUCT_PRICE_RULE_ID AND ppc1.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_ID'AND ppc2.`INPUT_PARAM_ENUM_ID` = 'PRIP_PRODUCT_STORE_ID'))  "
				+ " AS pprt  "
				+ " ON (pprt.PRODUCT_PRICE_RULE_ID = ppr.PRODUCT_PRICE_RULE_ID) "
				+ " JOIN PRODUCT_PRICE_ACTION AS ppa  "
				+ " ON (ppr.PRODUCT_PRICE_RULE_ID = ppa.PRODUCT_PRICE_RULE_ID AND ppa.PRODUCT_PRICE_ACTION_TYPE_ID = 'PRICE_POL') "
				+ " WHERE " + productIdSql + storeIdSql
				+ " AND ppr.FROM_DATE <= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
				+ " AND (ppr.THRU_DATE >= "+"(SELECT DATE_ADD(CURDATE(),INTERVAL " + rawOffset + " MICROSECOND)) "
				+ " OR ppr.THRU_DATE IS NULL) "
				+ " ORDER BY ppr.CREATED_TX_STAMP DESC) AS a "
				+ " GROUP BY a.PRODUCT_ID , a.PRODUCT_STORE_ID ";
			
			rs = processor.executeQuery(lastmodifieldDataSql);
			Map<String, String> careateDateMap = new HashMap<String, String>();
			while (rs.next()) {
				String productPriceRuleId = rs.getString("PRODUCT_PRICE_RULE_ID");
				String createDate = rs.getString("createDate");
				careateDateMap.put(productPriceRuleId, createDate);
			}	
			
			
			
			rs = processor.executeQuery(selectAll + sql + limitcont);
			List<ProductpriceLogBean> resultList = new ArrayList<ProductpriceLogBean>();
			while (rs.next()) {
				ProductpriceLogBean productpriceLog = new ProductpriceLogBean();
				String productPriceRuleId = rs.getString("PRODUCT_PRICE_RULE_ID");
				String fromDate = rs.getString("fromDate");
				String thruDate = rs.getString("thruDate");
				productpriceLog.setCommencementDate(fromDate + "--" + thruDate);
				productpriceLog.setCurrentPrice(rs.getString("currentPrice"));
				String description = rs.getString("brandName") + " | " +  rs.getString("internalName"); 
				String descr = rs.getString("descr");
				if(descr != null){
					description = description + " | " + rs.getString("descr");
				}
				productpriceLog.setDescription(description);
				productpriceLog.setLastModifieldDate(rs.getString("createDate"));
				for (Map.Entry<String, String> entry : careateDateMap.entrySet()) {  
					if(productPriceRuleId.equals(entry.getKey())){
						productpriceLog.setIsInUse("Y");
					}else{
						productpriceLog.setIsInUse("N");
					}  
				}  
				
				productpriceLog.setOperationer(rs.getString("createdUser"));
				productpriceLog.setProductId(rs.getString("productId"));
				productpriceLog.setStoreName(rs.getString("storeName"));
				productpriceLog.setFacility(rs.getString("facility"));
				resultList.add(productpriceLog);
			}	
			map.put("resultList", resultList);
			return map;
			
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
		
	}
	
	
	public static String formatTosepara(String data) {
		if(data == null || "".equals(data)){
			data = "0";
		}
        DecimalFormat df = new DecimalFormat(",###,##0.00"); 
        return df.format(new BigDecimal(data));
    }
	
	
	
	public static boolean hasEntityPermission(HttpServletRequest request ,String updateMode ){
		String errMsg=null;
		Security security = (Security) request.getAttribute("security");
		if (!security.hasEntityPermission("PPR", "_" + updateMode, request.getSession())) {
            Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
            errMsg = UtilProperties.getMessage(resource,"productevents.not_sufficient_permissions", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return false;
        }else{
        	return true;
        }
	}
}
