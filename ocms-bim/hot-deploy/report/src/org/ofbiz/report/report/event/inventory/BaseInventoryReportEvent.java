package org.ofbiz.report.report.event.inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.report.report.helper.sales.BaseSalesReportHelper;
import org.ofbiz.report.security.BaseStoreSecurity;
import org.ofbiz.report.security.OperatorReportSecurity;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;

import javolution.util.FastList;
import javolution.util.FastSet;

public class BaseInventoryReportEvent {

	public static final String SGROUPTYPE_STORE = "STORE_GROUP";
	public static final String SGROUPTYPE_PRICE = "PRICE_GROUP";

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
				}else if(BaseStoreSecurity.REP_INV_GH_MAN.equals(roleType))
				{
					returnmap.put(BaseStoreSecurity.REP_INV_GH_MAN, BaseStoreSecurity.REP_INV_GH_MAN);
				}
				else if(BaseStoreSecurity.REP_INV_UG_MAN.equals(roleType))
				{
					returnmap.put(BaseStoreSecurity.REP_INV_UG_MAN,BaseStoreSecurity.REP_INV_UG_MAN);
				}
				else if(BaseStoreSecurity.REP_INV_ADMIN.equals(roleType))
				{
					returnmap.put(BaseStoreSecurity.REP_INV_ADMIN,BaseStoreSecurity.REP_INV_ADMIN);
				}
			}
			if (sGMasterConditions.size() != 0) {
				EntityCondition totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
						EntityCondition.makeCondition(sGMasterConditions, EntityOperator.OR),
						EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS, "STORE_GROUP")),
						EntityOperator.AND);

				getMasterStoreAndSG(delegator, totalCondition, sGroupBeans, storeRollUpBeans);
			}
			if (hasSingleRole) {
				EntityCondition totalCondition = null;
				if (sGSingleConditions.size() == 0) {
					totalCondition = EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS,
							"STORE_GROUP");
				} else {
					totalCondition = EntityCondition.makeCondition(
							UtilMisc.toList(EntityCondition.makeCondition(sGSingleConditions, EntityOperator.OR),
									EntityCondition.makeCondition("productStoreGroupTypeId", EntityOperator.EQUALS,
											"STORE_GROUP")),
							EntityOperator.AND);
				}
				getSingleStoreByRole(delegator, totalCondition, userLogin, sGroupBeans, storeRollUpBeans);

				// 库管员
				getWarehouseAdministratorByRole(delegator, userLogin, sGroupBeans, storeRollUpBeans,reportSecurity.getType());
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

	public static void getWarehouseAdministratorByRole(GenericDelegator delegator, GenericValue userLogin,
			List<StoreGroupBean> sGroupBeans, List<StoreRollUpBean> StoreAndGroupBeans,String type) throws GenericEntityException {
		String partyId = userLogin.getString("partyId");// 登陆用户所属partyID

		String sql = "select * from" + " (" + " select * from" + " (" + " select fpp.FACILITY_ID from"
				+ " FACILITY_PARTY_PERMISSION as fpp where fpp.PARTY_ID = 'userpartindext' and fpp.SECURITY_GROUP_ID='WRHS_MANAGER' and fpp.THRU_DATE is null ORDER BY fpp.FACILITY_ID"
				+ " )as pr" + " INNER join PRODUCT_STORE as ps on ps.INVENTORY_FACILITY_ID=pr.FACILITY_ID" + " ) as ret"
				+ " INNER join" + " ( "
				+ " select psgm.PRODUCT_STORE_ID,psgm.PRODUCT_STORE_GROUP_ID,psg.PRODUCT_STORE_GROUP_NAME from PRODUCT_STORE_GROUP_MEMBER as psgm "
				+ " INNER join PRODUCT_STORE_GROUP as psg on psg.PRODUCT_STORE_GROUP_ID=psgm.PRODUCT_STORE_GROUP_ID"
				+ " where psg.PRODUCT_STORE_GROUP_TYPE_ID='STORE_GROUP'" + " ) as finret"
				+ " on finret.PRODUCT_STORE_ID=ret.PRODUCT_STORE_ID";

		// 包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		ResultSet rs = processor.executeQuery(sql.replaceAll("userpartindext", partyId));

		try {
			while (rs.next()) {
				String PRODUCT_STORE_GROUP_ID = rs.getString("PRODUCT_STORE_GROUP_ID");
				String PRODUCT_STORE_GROUP_NAME = rs.getString("PRODUCT_STORE_GROUP_NAME");
				String PRODUCT_STORE_ID = rs.getString("PRODUCT_STORE_ID");
				String STORE_NAME = rs.getString("STORE_NAME");
				if (PRODUCT_STORE_GROUP_ID == null || "".equals(PRODUCT_STORE_GROUP_ID) || PRODUCT_STORE_ID == null
						|| "".equals(PRODUCT_STORE_ID))
					continue;
				StoreGroupBean StoreGroup = new StoreGroupBean(PRODUCT_STORE_GROUP_ID, PRODUCT_STORE_GROUP_NAME);
				if(OperatorReportSecurity.MTN.equals(type) &&  "BANANA_S".equals(PRODUCT_STORE_GROUP_ID))
					continue;
				
				if (!ishash(sGroupBeans, StoreGroup))
					sGroupBeans.add(StoreGroup);
				StoreRollUpBean Store = new StoreRollUpBean(PRODUCT_STORE_ID, PRODUCT_STORE_GROUP_ID, STORE_NAME);
				if (!ishash(StoreAndGroupBeans, Store))
					StoreAndGroupBeans.add(Store);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				processor.close();
			}catch(Exception e){
				
			}
		}

	}
	
	public static boolean ishash(List<StoreGroupBean> sGroupBeans,StoreGroupBean bean )
	{
		for(int i=0;i<sGroupBeans.size();i++)
		{
			StoreGroupBean temp = sGroupBeans.get(i);
			if(temp.getsGroupId().equals(bean.getsGroupId()))
				return true;
		}
		return false;
	}
	public static boolean ishash(List<StoreRollUpBean> sGroupBeans,StoreRollUpBean bean )
	{
		for(int i=0;i<sGroupBeans.size();i++)
		{
			StoreRollUpBean temp = sGroupBeans.get(i);
			if(temp.getStoreGroupID().equals(bean.getStoreGroupID())&& temp.getStoreID().equals(bean.getStoreID()))
				return true;
		}
		return false;
	}
}
