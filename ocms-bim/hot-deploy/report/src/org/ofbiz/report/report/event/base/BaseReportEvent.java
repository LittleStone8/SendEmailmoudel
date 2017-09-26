package org.ofbiz.report.report.event.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.report.report.been.ProductBeen;
import org.ofbiz.report.report.helper.sales.BaseSalesReportHelper;
import org.ofbiz.report.security.BaseStoreSecurity;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;
import org.ofbiz.util.ParaUtils;

import javolution.util.FastList;
import javolution.util.FastSet;

public class BaseReportEvent {

	public static final String SGROUPTYPE_STORE = "STORE_GROUP";
	public static final String SGROUPTYPE_PRICE = "PRICE_GROUP";

	/**
	 * 联想查询product
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchProductsByLikeID(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = ServiceUtil.returnSuccess();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");

		String productId = ParaUtils.getParameter(request, "productId");
		if (UtilValidate.isNotEmpty("productId")) {
			List<ProductBeen> productBeans = FastList.newInstance();
			List<GenericValue> productGVs = null;
			try {
				TransactionUtil.begin();
				productGVs = BaseSalesReportHelper.searchProductsByLikeID(delegator, productId);
				TransactionUtil.commit();
			} catch (Exception e) {
				e.printStackTrace();
				return ServiceUtil.returnError("查询失败");
			}

			if (null != productGVs) {
				for (GenericValue genericValue : productGVs) {
					productBeans.add(new ProductBeen(genericValue.getString("productId"),
							genericValue.getString("internalName")));
				}
			}
			map.put("productBeans", productBeans);

		}
		return map;
	}

	/**
	 * 通过role来判断所拥有店铺和店铺租
	 * 
	 * @param delegator
	 * @param sGroupConditions
	 * @param sGconditions
	 * @param userLogin
	 * @param sGroupBeans
	 * @param storeAndGroupBeans
	 * @throws GenericEntityException
	 */
	public static void getSingleStoreByRole(GenericDelegator delegator, EntityCondition sGroupConditions,
			GenericValue userLogin, List<StoreGroupBean> sGroupBeans, List<StoreRollUpBean> storeRollUpBeans)
			throws GenericEntityException {
		EntityCondition totalCondition;

		List<GenericValue> findList = BaseSalesReportHelper.getStoreRoleBuUserLogin(delegator, userLogin);

		// 存放storeID的集合,去重
		Set<String> productStoreIds = FastSet.newInstance();
		if (null != findList && findList.size() != 0) {
			for (GenericValue genericValue : findList) {
				productStoreIds.add(genericValue.getString("productStoreId"));
			}
			// 店铺租查询条件
			List<EntityExpr> conditions = new ArrayList<EntityExpr>();
			for (String storeId : productStoreIds) {
				conditions.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, storeId));
			}
			// 总查询条件
			totalCondition = EntityCondition.makeCondition(
					UtilMisc.toList(EntityCondition.makeCondition(conditions, EntityOperator.OR), sGroupConditions),
					EntityOperator.AND);
			GenericValue nextSGMemberGV = null;
			EntityListIterator SGMIterator = null;
			try {
				SGMIterator = BaseSalesReportHelper.getSGMemberIterator(delegator, totalCondition);

				while ((nextSGMemberGV = SGMIterator.next()) != null) {
					String sGroupName = nextSGMemberGV.getString("productStoreGroupName");
					String sGroupId = nextSGMemberGV.getString("productStoreGroupId");

					StoreGroupBean storeGroupBean = new StoreGroupBean(sGroupId, sGroupName);
					if (!sGroupBeans.contains(storeGroupBean)) {
						sGroupBeans.add(storeGroupBean);
					}
					// 创建店铺bean
					StoreRollUpBean storeRollUpBean = new StoreRollUpBean(nextSGMemberGV.getString("productStoreId"),
							sGroupId, nextSGMemberGV.getString("storeName"));
					if (!storeRollUpBeans.contains(storeRollUpBean)) {
						storeRollUpBeans.add(storeRollUpBean);
					}
				}
			} finally {
				if (null != SGMIterator) {
					try {
						SGMIterator.close();
					} catch (GenericEntityException ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}
				}
			}

		}

	}

	/**
	 * 查找管理员的店铺和店铺租
	 * 
	 * @param delegator
	 * @param map
	 * @param availableSGroups
	 * @return
	 * @throws GenericEntityException
	 */
	public static void getMasterStoreAndSG(GenericDelegator delegator, EntityCondition totalCondition,
			List<StoreGroupBean> sGroupBeans, List<StoreRollUpBean> storeRollUpBeans) throws GenericEntityException {
		// 封装返回参数
		EntityListIterator SGMIterator = null;
		GenericValue nextSGMemberGV = null;
		try {
			SGMIterator = BaseSalesReportHelper.getSGMemberIterator(delegator, totalCondition);
			while ((nextSGMemberGV = SGMIterator.next()) != null) {
				String sGroupName = nextSGMemberGV.getString("productStoreGroupName");
				String sGroupId = nextSGMemberGV.getString("productStoreGroupId");
				// 创建店铺租bean
				StoreGroupBean tempsgb = new StoreGroupBean(sGroupId, sGroupName);
				if (!sGroupBeans.contains(tempsgb)) {
					sGroupBeans.add(tempsgb);
				}
				// 创建店铺bean
				StoreRollUpBean sagbtemp = new StoreRollUpBean(nextSGMemberGV.getString("productStoreId"), sGroupId,
						nextSGMemberGV.getString("storeName"));
				if (!storeRollUpBeans.contains(sagbtemp)) {
					storeRollUpBeans.add(sagbtemp);
				}

			}
		} finally {
			if (null != SGMIterator) {
				try {
					SGMIterator.close();
				} catch (GenericEntityException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
			}

		}
	}

	public static Map<String, Object> checkAvailableStoreScope(String sGroupId, String storeId,
			Map<String, Object> ret) {

		List<StoreGroupBean> sGroupBeans = (List<StoreGroupBean>) ret.get("SGroup");
		List<StoreRollUpBean> storeRollUpBeans = (List<StoreRollUpBean>) ret.get("SGMember");

		List<StoreGroupBean> tempGB = FastList.newInstance();
		List<StoreRollUpBean> tempSAGB = FastList.newInstance();

		if ("".equals(sGroupId) && "".equals(storeId))// 都不选，查全部
		{
			tempGB = sGroupBeans;
			tempSAGB = storeRollUpBeans;
		} else if (!"".equals(sGroupId) && "".equals(storeId))// 查询选定店铺组下所有的店铺的情况
		{
			for (StoreGroupBean sGroupBean : sGroupBeans) {
				StoreGroupBean tempsGroupBean = sGroupBean;
				if (sGroupId.equals(tempsGroupBean.getsGroupId())) {
					tempGB.add(tempsGroupBean);
				}
			}

			for (StoreRollUpBean storeRollUpBean : storeRollUpBeans) {
				StoreRollUpBean tempStoreAndGroupBean = storeRollUpBean;
				if (sGroupId.equals(tempStoreAndGroupBean.getStoreGroupID())) {
					tempSAGB.add(tempStoreAndGroupBean);
				}
			}

		} else {
			for (StoreGroupBean sGroupBean : sGroupBeans) {
				StoreGroupBean tempsGroupBean = sGroupBean;
				if (sGroupId.equals(tempsGroupBean.getsGroupId())) {
					tempGB.add(tempsGroupBean);
				}
			}

			for (StoreRollUpBean storeRollUpBean : storeRollUpBeans) {
				StoreRollUpBean tempStoreAndGroupBean = storeRollUpBean;
				if (storeId.equals(tempStoreAndGroupBean.getStoreID())) {
					tempSAGB.add(tempStoreAndGroupBean);
				}
			}
		}
		Map<String, Object> returnmap = new HashMap<String, Object>();
		returnmap.put("SGroup", tempGB);
		returnmap.put("SGMember", tempSAGB);
		return returnmap;
	}

	/**
	 * 查找可用店铺
	 * 
	 * @param delegator
	 * @param userLogin
	 * @param baneportSecurity
	 * @return
	 */
	public static Map<String, Object> searchStoreBySecurity(GenericDelegator delegator, GenericValue userLogin,
			BaseStoreSecurity reportSecurity) {
		Map<String, Object> returnmap = ServiceUtil.returnSuccess();
		boolean okay = true;
		List<StoreGroupBean> sGroupBeans = FastList.newInstance();
		List<StoreRollUpBean> storeRollUpBeans = FastList.newInstance();

		try {
			TransactionUtil.begin();
			Map<String, String> availableSGroups = reportSecurity.getReportPermission(delegator, userLogin);
			if (availableSGroups.size() == 0) {
				okay = false;
				return ServiceUtil.returnError("you donot have any View Permission for this report");
			}

			boolean hasSingleRole = false;
			List<EntityCondition> sGMasterConditions = FastList.newInstance();// 角色为master的安全组
			List<EntityCondition> sGSingleConditions = FastList.newInstance();// 角色为master的安全组
			for (Map.Entry<String, String> entry : availableSGroups.entrySet()) {
				String reportSGroup = entry.getKey();
				String roleType = entry.getValue();

				if ("master".equals(roleType)) {
					sGMasterConditions.add(
							EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, reportSGroup));
				} else if ("single".equals(roleType)) {
					hasSingleRole = true;
					if(!reportSGroup.equals("BANANA_S")){
					sGSingleConditions.add(
							EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, reportSGroup));
					}
				}
			}
			if (sGMasterConditions.size() != 0) {
				EntityCondition totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
						EntityCondition.makeCondition(sGMasterConditions, EntityOperator.OR),
						EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS, "STORE_GROUP")),
						EntityOperator.AND);

				getMasterStoreAndSG(delegator, totalCondition, sGroupBeans, storeRollUpBeans);
			}
			if (hasSingleRole ) {
				EntityCondition totalCondition = null;
				if(sGSingleConditions.size() == 0){
					 totalCondition = 
							EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS, "STORE_GROUP");
				}else{
				 totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
						EntityCondition.makeCondition(sGSingleConditions, EntityOperator.OR),
						EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS, "STORE_GROUP")),
						EntityOperator.AND);
				}
				getSingleStoreByRole(delegator, totalCondition, userLogin, sGroupBeans, storeRollUpBeans);
			}

			if (0 == sGroupBeans.size() || 0 == storeRollUpBeans.size()) {
				okay = false;
				return ServiceUtil.returnError("no store or group could be found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return ServiceUtil.returnError("search store and group error");
		} finally {
			if (!okay) {
				try {
					TransactionUtil.rollback();
				} catch (GenericTransactionException gte) {
					gte.printStackTrace();
				}
			} else {
				try {
					TransactionUtil.commit();
				} catch (GenericTransactionException gte) {
					gte.printStackTrace();
				}
			}
		}

		returnmap.put("SGroup", sGroupBeans);
		returnmap.put("SGMember", storeRollUpBeans);
		return returnmap;
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
