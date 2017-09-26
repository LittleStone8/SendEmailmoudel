package org.ofbiz.storegroup.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreAndGroupBean;

import javolution.util.FastList;

public class StoreGroupHelper {
	/**
	 * 条件查询店铺店铺组关系
	 * @param delegator
	 * @param productStoreId
	 * @param productStoreGroupId
	 * @return
	 * @throws GenericDataSourceException
	 * @throws GenericEntityException
	 */
	public static List<StoreAndGroupBean> searchSGMemberByConditions(GenericDelegator delegator, String productStoreId,
			String productStoreGroupId) throws Exception {
		ResultSet rs = null;
		SQLProcessor processor = null;
		List<StoreAndGroupBean> storeAndGroupBeans = FastList.newInstance();
		try {
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			processor = new SQLProcessor(helperInfo);
			StringBuilder sqlbul = new StringBuilder("select sgm.PRODUCT_STORE_GROUP_ID,sg.PRODUCT_STORE_GROUP_NAME,sgm.PRODUCT_STORE_ID,s.STORE_NAME "
					+ " from PRODUCT_STORE s,PRODUCT_STORE_GROUP sg,PRODUCT_STORE_GROUP_MEMBER sgm "
					+ " where sgm.PRODUCT_STORE_GROUP_ID =sg.PRODUCT_STORE_GROUP_ID "
					+ " and sgm.PRODUCT_STORE_ID=s.PRODUCT_STORE_ID ");

			if (UtilValidate.isNotEmpty(productStoreId)) {
				sqlbul.append(" And sgm.PRODUCT_STORE_ID = '" + productStoreId + "'");
			}
			if (UtilValidate.isNotEmpty(productStoreGroupId)) {
				sqlbul.append(" And sgm.PRODUCT_STORE_GROUP_ID = '" + productStoreGroupId + "'");
			}
			sqlbul.append(" Group by sgm.PRODUCT_STORE_GROUP_ID,sgm.PRODUCT_STORE_ID");
			// 获得j结果集
			rs = processor.executeQuery(sqlbul.toString());
		
			while (rs.next()) {
				StoreAndGroupBean storeAndGroupBean = new StoreAndGroupBean(rs.getString("PRODUCT_STORE_ID"),
						rs.getString("PRODUCT_STORE_GROUP_ID"), rs.getString("STORE_NAME"),
						rs.getString("PRODUCT_STORE_GROUP_NAME"),null);
				storeAndGroupBeans.add(storeAndGroupBean);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (processor != null) {
				try {
					processor.close();
				} catch (GenericDataSourceException e) {
					e.printStackTrace();
				}
			}
		}
		return storeAndGroupBeans;
	}
	
	public static int removeAllSGMembers(GenericDelegator delegator, List<GenericValue> sGroupGVs) throws GenericEntityException{
		int removeAll = delegator.removeAll(sGroupGVs);
		return removeAll;
	}
			
	
	public static int storeAllSGMembers(GenericDelegator delegator, List<GenericValue> sGroupGVs) throws GenericEntityException{
		int storeAll = delegator.storeAll(sGroupGVs);
		return storeAll;
	}
	
	

}
