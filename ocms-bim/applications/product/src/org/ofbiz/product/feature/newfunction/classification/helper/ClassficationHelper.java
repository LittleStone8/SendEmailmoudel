package org.ofbiz.product.feature.newfunction.classification.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.UtilMisc;
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
import org.ofbiz.product.feature.newfunction.bean.CreateProductPageBean;
import org.ofbiz.service.ServiceUtil;

import javolution.util.FastList;

public class ClassficationHelper {

	/**
	 * 判断此category在本级目录下重名,如果存在则 false；
	 * 
	 * @param delegator
	 * @param parentCategoryID
	 * @param targetCategoryName
	 * @return
	 * @throws Exception 
	 */
	public static Boolean checkRepeat(GenericDelegator delegator, String parentCategoryID, String targetCategoryName) throws Exception {
		List<CreateProductPageBean> productPageBeans = searchCategoryById(delegator, parentCategoryID);
		List<String> checkRepeatList = FastList.newInstance();
		for (CreateProductPageBean productPageBean : productPageBeans) {
			String originCategoryName = productPageBean.getCategoryName();
			if(originCategoryName.matches("(<(.*)>)|(</(.*)>)")){
				return false;
			}
			if (originCategoryName.equals(targetCategoryName)) {
				return false;
			}
			if (checkRepeatList.contains(originCategoryName)) {
				return false;
			} else {
				checkRepeatList.add(originCategoryName);
			}
		}
		return true;
	}

	/**
	 * 创建或更新类目，层级关系,如果创建，则返回新创建的ID。 由于类目有顺序，所以为批量创建
	 * 
	 * @param delegator
	 * @param pageBeens
	 * @return
	 */
	public static Map<String, Object> createOrUpdateCategory(GenericDelegator delegator,
			List<ClassificationPageBean> pageBeens) {
		Map<String, Object> map = ServiceUtil.returnSuccess();
		// 遍历原前台数据，确保ID后,批量更新；
		String newCategoryID = null;
		Boolean flag = true;

		List<GenericValue> productCategoryRollupValues = FastList.newInstance();
		try {
			TransactionUtil.begin();
			for (ClassificationPageBean pageBeen : pageBeens) {
				String categoryID = pageBeen.getCategoryID();
				if (null == categoryID) {
					if (!checkRepeat(delegator, pageBeen.getParentCategoryID(), pageBeen.getCategoryName())) {
						flag = false;
						return ServiceUtil.returnError("classification name cannot be same Or name is illegal");
					}
					categoryID = createCategory(delegator, pageBeen);
					newCategoryID = categoryID;
				}
				if (null == pageBeen.getCategoryName()) {
					flag = false;
					return ServiceUtil.returnError("name cannot be empty");
				}
				Timestamp now = new Timestamp(System.currentTimeMillis());
				GenericValue productCategoryRollupValue = delegator.makeValue("ProductCategoryRollup",
						UtilMisc.toMap("productCategoryId", categoryID, "parentProductCategoryId",
								pageBeen.getParentCategoryID(), "fromDate", now, "sequenceNum",
								pageBeen.getSequenceNum()));

				productCategoryRollupValues.add(productCategoryRollupValue);
			}

			delegator.storeAll(productCategoryRollupValues);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (flag == false) {
				try {
					TransactionUtil.rollback();
				} catch (GenericTransactionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (flag == true) {
				try {
					TransactionUtil.commit();
				} catch (GenericTransactionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		map.put("newCategoryId", newCategoryID);
		return map;
	}

	/**
	 * 创建pageBean，主键自增长，返回新创建ID
	 * 
	 * @param delegator
	 * @param pageBeen
	 * @return
	 * @throws GenericEntityException
	 */
	private static String createCategory(GenericDelegator delegator, ClassificationPageBean pageBeen)
			throws GenericEntityException {
		String newProductCategoryId = delegator.getNextSeqId("ProductCategory");
		pageBeen.setCategoryID(newProductCategoryId);
		GenericValue productCategoryValue = delegator.makeValue("ProductCategory",
				UtilMisc.toMap("productCategoryId", newProductCategoryId, "productCategoryTypeId", "EGATEE_CATEGORY",
						"primaryParentCategoryId", pageBeen.getParentCategoryID(), "categoryName",
						pageBeen.getCategoryName()));

		GenericValue createdValue = delegator.create(productCategoryValue);
		return createdValue == null ? null : createdValue.getString("productCategoryId");
	}

	/**
	 * 根据父类ID查子类目GV
	 * 
	 * @param delegator
	 * @param categoryId
	 * @return
	 * @throws GenericDataSourceException 
	 * @throws GenericEntityException
	 */
	public static List<CreateProductPageBean> searchCategoryById(GenericDelegator delegator, String categoryId) throws Exception {
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String sql =" select p1.PRODUCT_CATEGORY_ID as categoryId,p2.PRODUCT_CATEGORY_ID as parentCategoryId,p2.PRODUCT_CATEGORY_TYPE_ID,p1.CATEGORY_NAME as categoryName, "
		+           " pcr.PRODUCT_CATEGORY_ID,pcr.PARENT_PRODUCT_CATEGORY_ID,pcr.FROM_DATE "
		+			" from PRODUCT_CATEGORY p1, PRODUCT_CATEGORY p2, PRODUCT_CATEGORY_ROLLUP pcr "
		+			" where p1.PRODUCT_CATEGORY_ID=pcr.PRODUCT_CATEGORY_ID "
		+			" and p2.PRODUCT_CATEGORY_ID = pcr.PARENT_PRODUCT_CATEGORY_ID "
		+			" and p1.PRODUCT_CATEGORY_TYPE_ID = 'EGATEE_CATEGORY' "
		+			" and p2.PRODUCT_CATEGORY_ID = '"+categoryId+"'";
		ResultSet rs = null;
		List<CreateProductPageBean> productPageBeans = new ArrayList<CreateProductPageBean>();
		try {
			// 获得j结果集
			rs = processor.executeQuery(sql);
			while (rs.next()) {
				CreateProductPageBean productPageBean = new CreateProductPageBean();
				productPageBean.setCategoryID(rs.getString("categoryId"));
				productPageBean.setCategoryName(rs.getString("categoryName"));
				if(!productPageBeans.contains(productPageBean)){
					productPageBeans.add(productPageBean);
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
		return productPageBeans;
	}
	/**
	 * 更新目录名称
	 * @param delegator
	 * @param productCategoryId
	 * @param categoryName
	 * @param parentCategoryID 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> updateCategoryName(GenericDelegator delegator, String productCategoryId,String categoryName, String parentCategoryID) throws Exception{
		if(!checkRepeat(delegator, parentCategoryID, categoryName)){
			return ServiceUtil.returnError("categoryName cannot be same Or name is illegal");
		}
		
		EntityCondition condition = EntityCondition.makeCondition("productCategoryId", EntityOperator.EQUALS,productCategoryId);
		// 创建分类
		GenericValue productCategoryValue = delegator.makeValue("ProductCategory",
				UtilMisc.toMap("productCategoryTypeId", "EGATEE_CATEGORY", "categoryName", categoryName));	
		try {
			TransactionUtil.begin();
			delegator.storeByCondition("ProductCategory", productCategoryValue, condition);			
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
			return ServiceUtil.returnError("edit name failed");
		}
		return ServiceUtil.returnSuccess();
	}
}
