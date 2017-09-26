package org.ofbiz.product.feature.newfunction.classification.event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.product.feature.newfunction.bean.ClassificationPageBean;
import org.ofbiz.product.feature.newfunction.classification.helper.ClassficationHelper;
import org.ofbiz.product.feature.newfunction.security.SecurityCheck;
import org.ofbiz.product.feature.newfunction.util.ProductUtils;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;

/*
 * 分类管理模块
 * 创建分类（兄弟，孩子）
 * 编辑分类
 * 批量修改分类（顺序）
 * 删除分类
 */
public class ClassficationEvent {
	/**
	 * 1.创建类目
	 * 
	 * @param request
	 *            :JSON.Stringtify(categoryJSONArr)
	 * @param response
	 * @return
	 */
	public static Map<String, Object> createProductClassification(HttpServletRequest request,
			HttpServletResponse response) {
		String updateMode = "CREATE";
		if (!SecurityCheck.hasEntityPermission(request, updateMode)) {
			return ServiceUtil.returnError("no permission");
		}

		JSONObject json = ProductUtils.getJsonByRequest(request); // 获取前台JSON
		JSONArray jsonArray = json.getJSONArray("categoryJSONArr");
		if (UtilValidate.isEmpty(jsonArray)) {
			return ServiceUtil.returnError("info error");
		}
		List<ClassificationPageBean> pageBeens = jsonArray.toJavaList(ClassificationPageBean.class);

		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		return ClassficationHelper.createOrUpdateCategory(delegator, pageBeens);
	}

	/**
	 * 2.查出所有分类包括明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> listAllDefaultClassfication(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");

		// if (!SecurityCheck.hasEntityPermission(request, "VIEW")) {
		// return ServiceUtil.returnError("no permission");
		// }

		Map<String, Object> map = ServiceUtil.returnSuccess();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String sql = "SELECT "
				+ "pc.PRODUCT_CATEGORY_ID, pc.PRODUCT_CATEGORY_TYPE_ID, pc.CATEGORY_NAME, pc.DESCRIPTION, pcr.SEQUENCE_NUM,pcr.PARENT_PRODUCT_CATEGORY_ID,pcr.FROM_DATE"
				+ " FROM " + "PRODUCT_CATEGORY pc LEFT JOIN PRODUCT_CATEGORY_ROLLUP pcr " + " ON "
				+ "pc.PRODUCT_CATEGORY_ID=pcr.PRODUCT_CATEGORY_ID" + " WHERE "
				+ "pc.PRODUCT_CATEGORY_TYPE_ID = 'EGATEE_CATEGORY'" + " order by  pcr.SEQUENCE_NUM";
		ResultSet rs = null;

		try {
			// 获得j结果集
			rs = processor.executeQuery(sql);

			while (rs.next()) {
				ClassificationPageBean classification = new ClassificationPageBean(rs.getString("PRODUCT_CATEGORY_ID"),
						rs.getString("CATEGORY_NAME"), rs.getInt("SEQUENCE_NUM"),
						rs.getString("PARENT_PRODUCT_CATEGORY_ID"), rs.getTimestamp("FROM_DATE"));
				map.put(rs.getString("PRODUCT_CATEGORY_ID"), classification);
			}
		} catch (GenericDataSourceException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (GenericEntityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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

		return map;
	}

	/**
	 * 3 修改分类名称.
	 * 
	 * @param request：
	 *            categoryID,categoryName,parentProductCategoryId,sequenceNum
	 * @param response
	 * @return
	 */
	public static Map<String, Object> editClassfication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 判断权限
		if (!SecurityCheck.hasEntityPermission(request, "UPDATE")) {
			return ServiceUtil.returnError("no permission to update");
		}

		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		String productCategoryId = ProductUtils.getParameter(request, "categoryID");
		String categoryName = ProductUtils.getParameter(request, "categoryName");
		String parentCategoryID = ProductUtils.getParameter(request, "parentCategoryID");
		if (UtilValidate.isEmpty(productCategoryId) || (UtilValidate.isEmpty(categoryName))
				|| (UtilValidate.isEmpty(parentCategoryID))) {
			return ServiceUtil.returnError("categoryID Or parent categoryID is null");
		}
		return ClassficationHelper.updateCategoryName(delegator, productCategoryId, categoryName, parentCategoryID);
	}

	/**
	 * 4 刪除类目 productCategoryId，parentCategoryId
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> deleteClassfication(HttpServletRequest request, HttpServletResponse response) {
		
		//判断权限
		String updateMode = "DELETE";
		boolean flag = SecurityCheck.hasEntityPermission(request, updateMode);
		if (!flag) {
            return ServiceUtil.returnError("no permission to delete");
        }
		
		Map<String, Object> map = ServiceUtil.returnSuccess();

		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");

		String productCategoryId = (String) request.getParameter("categoryID");
		String parentCategoryId = (String) request.getParameter("parentProductCategoryId");

		if (productCategoryId == null || parentCategoryId == null) {
			return ServiceUtil.returnError("info error");
		}

		// 先判断有没有子目录，如果有就返回，如果没有，就判断是否有产品
//		GenericValue categoryRollupValue = delegator.makeValue("ProductCategoryRollup",
//				UtilMisc.toMap("parentProductCategoryId", productCategoryId));
//
//		GenericValue productMemberValue = delegator.makeValue("ProductCategoryMember",
//				UtilMisc.toMap("productCategoryId", productCategoryId));

		List<GenericValue> childCategorys = null; // 子类目
		List<GenericValue> childProducts = null; //类目子成员 
		try {
			TransactionUtil.begin();
			childCategorys = delegator.findList("ProductCategoryRollup", 
					EntityCondition.makeCondition("parentProductCategoryId", EntityOperator.EQUALS,productCategoryId), null, null, null, false);			
			// 如果分类数为0,则没有子分类
			
			if (null==childCategorys||childCategorys.size()!=0) {
				return ServiceUtil.returnError("cannot delete it which the category has child-category");
			}

			childProducts = delegator.findList("ProductCategoryMember", 
					EntityCondition.makeCondition("productCategoryId", EntityOperator.EQUALS,productCategoryId), null, null, null, false);			

			// 如果产品数不为0,则没有子产品
			if (childProducts==null||childProducts.size()!=0) {
				return ServiceUtil.returnError("cannot delete it which the category has products");
			}

			GenericValue ProductCategoryRollupValue = delegator.makeValue("ProductCategoryRollup", UtilMisc
					.toMap("productCategoryId", productCategoryId, "parentProductCategoryId", parentCategoryId));

			GenericValue productCategoryValue = delegator.makeValue("ProductCategory", UtilMisc
					.toMap("productCategoryId", productCategoryId, "primaryParentCategoryId", parentCategoryId));

			delegator.removeByAnd("ProductCategoryRollup", ProductCategoryRollupValue);
			delegator.removeByAnd("ProductCategory", productCategoryValue);
			TransactionUtil.commit();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return ServiceUtil.returnError("delete failed");
		}

		return map;

	}

	/**
	 * 5 移动，批量更新序列
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> moveClassfication(HttpServletRequest request, HttpServletResponse response) {

		// 判断权限
		String updateMode = "UPDATE";
		boolean flag = SecurityCheck.hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission to move");
		}

		Map<String, Object> map = ServiceUtil.returnSuccess();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");

		JSONObject json = ProductUtils.getJsonByRequest(request);// 获取前台JSON
		if (UtilValidate.isEmpty(json)) {
			return ServiceUtil.returnError("json error");
		}

		JSONArray jsonArray = json.getJSONArray("categoryJSONArr");

		List<ClassificationPageBean> pageBeens = jsonArray.toJavaList(ClassificationPageBean.class);

		List<GenericValue> productCategoryRollupValues = FastList.newInstance();

		for (ClassificationPageBean pageBeen : pageBeens) {
			String CategoryId = pageBeen.getCategoryID();

			GenericValue ProductCategoryRollupValue = delegator.makeValue("ProductCategoryRollup",
					UtilMisc.toMap("productCategoryId", CategoryId, "parentProductCategoryId",
							pageBeen.getParentCategoryID(), "fromDate", pageBeen.getCreateTime(), "sequenceNum",
							pageBeen.getSequenceNum()));
			productCategoryRollupValues.add(ProductCategoryRollupValue);
		}

		try {
			TransactionUtil.begin();
			delegator.storeAll(productCategoryRollupValues);
			TransactionUtil.commit();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			return ServiceUtil.returnError("move failed");
		}

		return map;
	}

	public static Map<String, Object> listClassficationsByID(HttpServletRequest request, HttpServletResponse response) {
		return null;

	}
}
