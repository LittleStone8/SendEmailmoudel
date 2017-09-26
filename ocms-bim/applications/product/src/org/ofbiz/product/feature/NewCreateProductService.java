package org.ofbiz.product.feature;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.feature.bean.FindProductFeatureAndProductFeatureType;
import org.ofbiz.product.feature.bean.FindProductFeatureBean;
import org.ofbiz.product.feature.bean.PriceProductFeatureBean;
import org.ofbiz.product.feature.bean.ProductFeatureApplBean;
import org.ofbiz.product.feature.bean.ProductIdSku;
import org.ofbiz.security.Security;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import sun.print.resources.serviceui;

public class NewCreateProductService {

	
	public static final String resource = "ProductErrorUiLabels";
	
	/*
	 * 新增产品特性 
	 * view:ProductFeatureAppl
	 * 
	 * chenshihua
	 * 2017-4-7
	 */
	public static String applyFeatureToProduct(HttpServletRequest request, HttpServletResponse response){
		try {
			TransactionUtil.begin();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue productFeature = delegator.makeValue("ProductFeatureAppl");
			productFeature.set("productId", request.getParameter("productId"));
			productFeature.set("productFeatureId", request.getParameter("productFeatureId"));
			productFeature.set("fromDate", new Timestamp(new Date().getTime()));
			productFeature.set("productFeatureApplTypeId", "SELECTABLE_FEATURE");
			toBeStored.add(productFeature);
			delegator.storeAll(toBeStored);
			TransactionUtil.commit();
		} catch (GenericEntityException e) {
			try {
				TransactionUtil.rollback();
			} catch (GenericTransactionException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> findProductAndCategory(HttpServletRequest request, HttpServletResponse response) {
			try {
				Delegator delegator = (Delegator) request.getAttribute("delegator");
				String productId = request.getParameter("productId");
				if(productId == null || productId.equals("")){
					return null;
				}else{
					Map<String, Object> reslutMap = ServiceUtil.returnSuccess("data returned success ");
					//查找三级分类
					GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId",productId), false);
					List<GenericValue> thirdProductCategoryList = delegator.findByAnd("ProductCategoryMember", UtilMisc.toMap("productId",product.getString("productId")));
					GenericValue thirdProductCategory = EntityUtil.getFirst(thirdProductCategoryList);
					reslutMap.put("thirdId", thirdProductCategory.getString("productCategoryId"));
					
					//查找二级分类
					List<GenericValue> secondProductCategoryRollupList = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("productCategoryId",thirdProductCategory.getString("productCategoryId")));
					GenericValue secondProductCategoryRollup = EntityUtil.getFirst(secondProductCategoryRollupList);
					GenericValue secondProductCategory = delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId",secondProductCategoryRollup.getString("parentProductCategoryId")), false);
					reslutMap.put("secondId", secondProductCategoryRollup.getString("parentProductCategoryId"));
					//查找第一级
					List<GenericValue> firstProductCategoryRollupList = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("productCategoryId",secondProductCategory.getString("productCategoryId")));
					GenericValue firstProductCategoryRollup = EntityUtil.getFirst(firstProductCategoryRollupList);
					GenericValue firstProductCategory = delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId",firstProductCategoryRollup.getString("parentProductCategoryId")), false);
					reslutMap.put("firstId", firstProductCategoryRollup.getString("parentProductCategoryId"));
					
					
					
					
					
//					reslutMap.put("third_ProductCategoryId", thirdProductCategoryBean);
//					reslutMap.put("second_ProductCategoryId", secondProductCategoryBean);
//					reslutMap.put("first_ProductCategoryId", firstProductCategoryBean);
					reslutMap.put("productId", productId);
					reslutMap.put("brandName", product.getString("brandName"));
					reslutMap.put("internalName", product.getString("productName"));
					return reslutMap;
				}
				
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ServiceUtil.returnError("query fails");
			}
			
    }
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	
	
	public static Map<String, Object> skuFindParentProductAndCategory(HttpServletRequest request, HttpServletResponse response) {
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String productId = request.getParameter("productId");
			List<GenericValue> productAssocList = delegator.findByAnd("ProductAssoc",  UtilMisc.toMap("productIdTo",productId));
			if(productAssocList.size() > 0){
				GenericValue productAssoc = EntityUtil.getFirst(productAssocList);
				productId = productAssoc.getString("productId");
			}
			if(productId == null || productId.equals("")){
				return null;
			}else{
				
				GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId",productId), false);
				if(product == null){
					return ServiceUtil.returnError("query fails");
				}
				Map<String, Object> reslutMap = ServiceUtil.returnSuccess("data returned success ");
				reslutMap.put("productId", productId);
				reslutMap.put("brandName", product.getString("brandName"));
				reslutMap.put("internalName", product.getString("productName"));
				
				//查找三级分类
				List<GenericValue> thirdProductCategoryList = delegator.findByAnd("ProductCategoryMember", UtilMisc.toMap("productId",product.getString("productId")));
				if(thirdProductCategoryList.size() > 0){
					GenericValue thirdProductCategory = EntityUtil.getFirst(thirdProductCategoryList);
					reslutMap.put("thirdId", thirdProductCategory.getString("productCategoryId"));
					//查找二级分类
					List<GenericValue> secondProductCategoryRollupList = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("productCategoryId",thirdProductCategory.getString("productCategoryId")));
					GenericValue secondProductCategoryRollup = EntityUtil.getFirst(secondProductCategoryRollupList);
					GenericValue secondProductCategory = delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId",secondProductCategoryRollup.getString("parentProductCategoryId")), false);
					reslutMap.put("secondId", secondProductCategoryRollup.getString("parentProductCategoryId"));
					//查找第一级
					List<GenericValue> firstProductCategoryRollupList = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("productCategoryId",secondProductCategory.getString("productCategoryId")));
					GenericValue firstProductCategoryRollup = EntityUtil.getFirst(firstProductCategoryRollupList);
					GenericValue firstProductCategory = delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId",firstProductCategoryRollup.getString("parentProductCategoryId")), false);
					reslutMap.put("firstId", firstProductCategoryRollup.getString("parentProductCategoryId"));
				}
				
				return reslutMap;
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fails");
		}
		
}
	
	
//	/*
//	 * 新增产品
//	 * 
//	 * chenshihua
//	 * 2017-4-7
//	 */
//	public static String createProduct(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			Delegator delegator = (Delegator) request.getAttribute("delegator");
//			String productId = request.getParameter("productId");
//			List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId",productId));
//			if(productList.size() == 0 || productList.equals(1)){
//				TransactionUtil.begin();
//				HttpSession session = request.getSession();
//				System.out.println("xmen ");
//				GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
////				Delegator delegator = (Delegator) request.getAttribute("delegator");
//				List<GenericValue> toBeStored = new ArrayList<GenericValue>();
//				
//				Map<String, Object> reqMap = UtilHttp.getParameterMap(request);
//				GenericValue product = delegator.makeValue("Product");
//				product.setFields(reqMap);
//				product.set("productId", request.getParameter("productId"));
//				product.set("productTypeId", request.getParameter("productTypeId"));
//				product.set("internalName", request.getParameter("internalName"));
//				product.set("productName", request.getParameter("productName"));
////				createdDate  lastModifiedDate	createdByUserLogin	lastModifiedByUserLogin userLogin.get("userLoginId")
//				Timestamp nowTime = new Timestamp(new Date().getTime());
//				product.set("createdDate", nowTime);
//				product.set("lastModifiedDate", nowTime);
//				product.set("createdByUserLogin", userLogin.get("userLoginId"));
//				product.set("lastModifiedByUserLogin", userLogin.get("userLoginId"));
//			
////				delegator.store(product);
//				toBeStored.add(product);
//				delegator.storeAll(toBeStored);
//				TransactionUtil.commit();
//			}else{
//				request.setAttribute("msg", "product is exist");
//				return "erro";
//			}
//		
//		} catch (GenericEntityException e) {
//			try {
//				TransactionUtil.rollback();
//			} catch (GenericTransactionException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		return null;
//    }
	
	
	
	
	
	

	/**
	 * 查询所有的产品特性返回productFeatureTypeId和description的map
	 * 
	 * PRODUCT_FEATURE_TYPE 
	 * 
	 * chenshihua
	 * 2017-4-8
	 */
	public static Map<String, Object> findAllProductFeatureType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> productFeatureTypeMap = new HashMap<String, Object>();
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> allProductFeatureType = delegator.findAll("ProductFeatureType");
			for (GenericValue genericValue : allProductFeatureType) {
				productFeatureTypeMap.put(genericValue.getString("productFeatureTypeId"), genericValue.getString("description"));
			}
//			request.setAttribute("productFeatureTypeMap", productFeatureTypeMap);
			return productFeatureTypeMap;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Create fails");
		}
		
	}
	
	/**
	 * 创建新的ProductFeatureType
	 * PRODUCT_FEATURE_TYPE   插入(id,描述)  
	 * chenshihua 
	 * 2017-4-8
	 * 
	 */
	public static Map<String, Object> createProductFeatureType(HttpServletRequest request, HttpServletResponse response) {
		
		//判断权限
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
            return ServiceUtil.returnError("no permission to add");
        }
		List<String> productFeatureTypeList	 = new ArrayList<String>();
		String productFeatureTypeId = null;
		try {
			
			// 要验证的字符串
			String description = request.getParameter("productFeatureType_description");
//		    
//		    
//			if(!rs1 || rs2){
//				return ServiceUtil.returnError("Please use the letter 、Numbers and  blank space，but Not all is blank space");
//			}
			
			
			
//		    String regEx2 = "^[\\s?]+$";
//		    // 编译正则表达式
//		    Pattern pattern1 = Pattern.compile(regEx1);
//		    Pattern pattern2 = Pattern.compile(regEx2);
//		    // 忽略大小写的写法
//		    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//		    Matcher matcher1 = pattern1.matcher(description);
//		    Matcher matcher2 = pattern2.matcher(description);
//		    // 字符串是否与正则表达式相匹配
//		    boolean rs1 = matcher1.matches();
//		    boolean rs2 = matcher2.matches();
//		    
//		    
//			if(!rs1 || rs2){
//				return ServiceUtil.returnError("Please use the letter 、Numbers and  blank space，but Not all is blank space");
//			}
			
			
			
			Map<String, Object> productFeatureTypeMap = findAllProductFeatureType(request,response);
			if(!productFeatureTypeMap.containsValue(description)){
				Delegator delegator = (Delegator) request.getAttribute("delegator");
				List<GenericValue> toBeStored = new ArrayList<GenericValue>();
				GenericValue productFeature = delegator.makeValue("ProductFeatureType");
				productFeature.put("description", description);
				productFeatureTypeId = description.toUpperCase();
				productFeatureTypeId = productFeatureTypeId.replace(" ", "_");
				productFeature.put("productFeatureTypeId", productFeatureTypeId);
				productFeatureTypeList.add(productFeatureTypeId);
				productFeatureTypeList.add(description);
				toBeStored.add(productFeature);
				delegator.storeAll(toBeStored);
				Map<String, Object> resultMap = ServiceUtil.returnSuccess("Creating success");
				resultMap.put("productFeatureTypeList", productFeatureTypeList);
				return resultMap;
			}else{
				return ServiceUtil.returnError("The attribute of to create already exists");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("creating fiels");
		}
		
	}
	
	
	
	
	/**
	 *  查询PRODUCT_FEATURE 
	 * 
	 * （PRODUCT_FEATURE_TYPE_ID）
	 * chenshihua	
	 * 2017-4-8
	 * 
	 */
	public static Map<String , Object> newFindProduceFeatur(HttpServletRequest request, HttpServletResponse response) {
		Map<String , Object> tagerProductFeatureMap = new HashMap<String , Object>();
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String productFeatureTypeId = request.getParameter("productFeatureTypeId");
			List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
			for (GenericValue genericValue : productFeatureList) {
				tagerProductFeatureMap.put(genericValue.getString("productFeatureId"), genericValue.getString("description"));
			}
			return tagerProductFeatureMap;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fails");
		}
		
	}
	
	
	
	
	/**
	 * PRODUCT_FEATURE 插入
	 * 
	 * 需要参数：froduceFeatur_description 和  productFeatureTypeId
	 * 
	 * 需要修改 ProductFeature的 view 参数
	 * productFeatureId
	 * productFeatureTypeId
	 * productFeatureCategoryId
	 * description
	 * 
	 * 
	 * 
	 * chenshihua 
	 * 2017-4-8
	 */
	public static Map<String, Object> newAddProduceFeatur(HttpServletRequest request, HttpServletResponse response) {
//		
//		主键自增，typeid，PRODUCT_FEATURE_TYPE_ID（固定），描述）
//		
		
		try {
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to add");
	        }
			
			// 要验证的字符串
			String description = request.getParameter("froduceFeatur_description");
//			// 邮箱验证规则
//			String regEx1 = "^[0-9a-zA-Z\\s\\.?]+$";
//			String regEx2 = "^[\\s?]+$";
//			// 编译正则表达式
//			Pattern pattern1 = Pattern.compile(regEx1);
//			Pattern pattern2 = Pattern.compile(regEx2);
//			// 忽略大小写的写法
//			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//			Matcher matcher1 = pattern1.matcher(description);
//			Matcher matcher2 = pattern2.matcher(description);
//			// 字符串是否与正则表达式相匹配
//			boolean rs1 = matcher1.matches();
//			boolean rs2 = matcher2.matches();
//					    
//					    
//			if(!rs1 || rs2){
//				return ServiceUtil.returnError("Please use the letter 、Numbers and  blank space，but Not all is blank space");
//			}
//			
			List<String> resultProduceFeaturList = new ArrayList<String>();
//			String description = request.getParameter("froduceFeatur_description");
			String productFeatureTypeId = request.getParameter("productFeatureTypeId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> productFeatureTypeIdList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
			List<String> IdList = new ArrayList<String>();
			for (GenericValue genericValue : productFeatureTypeIdList) {
				IdList.add(genericValue.getString("description"));
			}
			if(IdList.size() > 0 && IdList.contains(description)){
				return ServiceUtil.returnError("The attribute value of to create already exists");
			}
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue productFeature = delegator.makeValue("ProductFeature");
			
			productFeature.put("description", description);
			productFeature.put("productFeatureTypeId", productFeatureTypeId);
			String productFeatureId = productFeatureTypeId + description;
			productFeature.put("productFeatureId", productFeatureId);
			productFeature.put("productFeatureCategoryId", "other");
			resultProduceFeaturList.add(productFeatureTypeId);
			resultProduceFeaturList.add(description);
//			resultProduceFeaturMap.put(productFeatureTypeId, description);
			toBeStored.add(productFeature);
//			resultProduceFeaturMap.put(productFeatureTypeId, description);
			delegator.storeAll(toBeStored);
			Map<String, Object> resultProduceFeaturMap = ServiceUtil.returnSuccess("create attribute value success ");
			resultProduceFeaturMap.put("resultProduceFeaturList", resultProduceFeaturList);
			return resultProduceFeaturMap;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("falis");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 插入产品新特性
	 * 需要传入productId和productFeatureId
	 * 这两个参数是数据库里的外键，必须是关联表存在的数据
	 * 
	 * chenshihua
	 * 2017-4-8
	 */
	public static Map<String, Object> newAddProduceFeaturAppl(HttpServletRequest request, HttpServletResponse response) {
	
		try {
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to add");
	        }
			
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			List<GenericValue> ProductFeatureAppl = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap(
					"productId",request.getParameter("productId"),
					"productFeatureId",request.getParameter("productFeatureId")));
			if(ProductFeatureAppl.size() == 0){
				GenericValue productFeature = delegator.makeValue("ProductFeatureAppl");
				String productId = request.getParameter("productId");
				productFeature.set("productId", productId);
				String productFeatureId = request.getParameter("productFeatureId");
				productFeature.set("productFeatureId", productFeatureId);
				productFeature.set("fromDate", new Timestamp(new Date().getTime()));
				productFeature.set("productFeatureApplTypeId", "SELECTABLE_FEATURE");
				toBeStored.add(productFeature);
				delegator.storeAll(toBeStored);
			}else{
				return ServiceUtil.returnError("create fails");
			}
			return ServiceUtil.returnError("create success");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create fails");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询 PRODUCT_FEATURE_APPL 
	 * 
	 * 通过productId查询
	 * 
	 * 返回List<ProductFeatureApplBean>
	 * 
	 * chenshihua
	 * 2017-4-8
	 * 
	 */
	public static List<ProductFeatureApplBean> newFindProduceFeaturAppl(HttpServletRequest request, HttpServletResponse response) {
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> ProductFeatureAppl = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId",request.getParameter("productId")));
			List<ProductFeatureApplBean> ProductFeatureApplBeanList = new ArrayList<ProductFeatureApplBean>();
			for (GenericValue genericValue : ProductFeatureAppl) {
				ProductFeatureApplBean productFeatureApplBean = new ProductFeatureApplBean();
				productFeatureApplBean.setProductId(genericValue.getString("productId"));
				productFeatureApplBean.setProductFeatureId(genericValue.getString("productFeatureId"));
				productFeatureApplBean.setProductFeatureApplTypeId(genericValue.getString("productFeatureApplTypeId"));
				productFeatureApplBean.setFromDate(genericValue.getString("fromDate"));
				productFeatureApplBean.setFromDate(genericValue.getString("thruDate"));
				ProductFeatureApplBeanList.add(productFeatureApplBean);
			}
			return ProductFeatureApplBeanList;
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	
	
	
	/*
	 *掺入新建产品
	 *
	 *需要的主要参数：	productId
	 *				productTypeId
	 *				internalName
	 *				productName
	 *
	 *
	 *chenshihua
	 *2017-4-10
	 *
	 */
	public static Map<String, Object> newAddProduct(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			HttpSession session = request.getSession();
			
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to add");
	        }
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			GenericValue product = delegator.makeValue("Product");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String productId = request.getParameter("productId");
			product.put("productId", productId);
			product.put("productTypeId", request.getParameter("productTypeId"));
			product.put("internalName", request.getParameter("internalName"));
			product.put("productName", request.getParameter("productName"));
			Timestamp nowTime = new Timestamp(new Date().getTime());
			product.put("createdDate", nowTime);
			product.put("lastModifiedDate", nowTime);
			String userLoginId = userLogin.getString("userLoginId");
			product.put("createdByUserLogin", userLoginId);
			product.put("lastModifiedByUserLogin", userLoginId);
			toBeStored.add(product);
			
			GenericValue productCategoryMember = delegator.makeValue("ProductCategoryMember");
			productCategoryMember.put("productCategoryId", request.getParameter("productCategoryId"));
			productCategoryMember.put("productId", productId);
			productCategoryMember.put("fromDate", nowTime);
			toBeStored.add(productCategoryMember);
			delegator.storeAll(toBeStored);
			return ServiceUtil.returnError("Create product success");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Create product fails");
		}
	}
	
	
	
	
	/**
	 * 添加新产品
	 * @param delegator
	 * @param productId
	 * @param nowTime
	 * @param internalName
	 * @param brandName
	 * @param userLoginId
	 * @param productCategoryId
	 * @param isVirtual
	 * @param isVariant
	 * @return
	 */
	public static Map<String, Object> newAddProduct(Delegator delegator,String productId, Timestamp nowTime, String internalName, String brandName,String userLoginId,String productCategoryId ,String isVirtual,String isVariant) {
		
		try {
			if(productId == null || "".equals(productId)){
				productId = delegator.getNextSeqId("Product");
			}
			List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId",productId));
			GenericValue product;
			if(productList.size() > 0){
				product = EntityUtil.getFirst(productList);
			}else{
				product = delegator.makeValue("Product");
			}
			
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			product.put("productId", productId);
			product.put("productTypeId", "FINISHED_GOOD");
			product.put("internalName", internalName);
			product.put("productName", internalName);
			product.put("brandName", brandName);
			
			
			
			product.put("createdDate", nowTime);
			product.put("lastModifiedDate", nowTime);
			product.put("createdByUserLogin", userLoginId);
			product.put("lastModifiedByUserLogin", userLoginId);
			if(isVirtual != null){
				product.put("isVirtual", isVirtual);
			}
			if(isVariant != null){
				product.put("isVariant", isVariant);
			}
			toBeStored.add(product);
			
			GenericValue productCategoryMember ; 
			List<GenericValue> productCategoryMemberList = delegator.findByAnd("ProductCategoryMember", UtilMisc.toMap("productId",productId));
			if(productCategoryMemberList.size() > 0){
				delegator.removeByAnd("ProductCategoryMember", UtilMisc.toMap("productId",productId));
			}
			productCategoryMember = delegator.makeValue("ProductCategoryMember");
			productCategoryMember.put("fromDate", nowTime);
			
			
			productCategoryMember.put("productCategoryId", productCategoryId);
			productCategoryMember.put("productId", productId);
			
			toBeStored.add(productCategoryMember);
			
			Map<String, Object> msg = ServiceUtil.returnSuccess("create success");
			delegator.storeAll(toBeStored);
			msg.put(productId, productCategoryMember);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create fails");
		}

	}
	
	
	
	/**
	 * 通过productId查询产品
	 * 
	 */
	public static Map<String, Object> newFindProductById(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId",request.getParameter("productId")));
			GenericValue genericValue = EntityUtil.getFirst(productList);
			Map<String, Object> productMap = new HashMap<String, Object>();
			productMap.put("productId", genericValue.getString("productId"));
			productMap.put("productTypeId", genericValue.getString("productTypeId"));
			productMap.put("internalName", genericValue.getString("internalName"));
			productMap.put("productName", genericValue.getString("productName"));
			productMap.put("isVirtual", genericValue.getString("isVirtual"));
			productMap.put("isVariant", genericValue.getString("isVariant"));
			productMap.put("createdDate", genericValue.getString("createdDate"));
			productMap.put("createdByUserLogin", genericValue.getString("createdByUserLogin"));
			productMap.put("lastModifiedDate", genericValue.getString("lastModifiedDate"));
			productMap.put("lastModifiedByUserLogin", genericValue.getString("lastModifiedByUserLogin"));
			
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("productMap", productMap);
			return resultMap;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fails");
		}
	
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 保存变型产品和课变型产品的信息
	 * 不变形产品也可以保存
	 * @param request
	 * @param response
	 * var productList =[{
	 *						'productId':'lemon',
	 *						'productTypeId':'FINISHED_GOOD',
	 *						'internalName':'柠檬-6666',
	 *						'productName':'柠檬-55555',
	 *						'productCategoryId':'分类一'
	 *					
	 *					  }];
	 *
	 *	var productFeature = [{'COLOR':['10001','10004','10003']},{'TYPE':['3011','3010']}];
	 * 
	 * @return
	 */
	public static Map<String, Object> anotherAddVariantProduct(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String userLoginId = userLogin.getString("userLoginId");
			
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to edit");
	        }
			
			
			//获取数据
			StringBuffer jsonStringBuffer = new StringBuffer();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
	
			String object =  jsonStringBuffer.toString();
			JSONObject json = JSON.parseObject(object);
			
			JSONArray productJsonArr = json.getJSONArray("productList");
			JSONArray productFeatureJsonArr = json.getJSONArray("productFeature");
			
			JSONObject jsonObject = productJsonArr.getJSONObject(0);
			String productCategoryId = jsonObject.getString("productCategoryId");
			if(productCategoryId == null){
				return ServiceUtil.returnError("Create fails,Please select product category");
			}
			
			
			String productId = jsonObject.getString("productId");
			if(productId == null || "".equals(productId)){
				productId = delegator.getNextSeqId("Product");
			}
			
			
			String internalName = jsonObject.getString("internalName");
			String brandName = jsonObject.getString("brandName");
			
			
			// 要验证的字符串
//			String description = request.getParameter("productFeatureType_description");
//			// 邮箱验证规则
//			String regEx1 = "^[0-9a-zA-Z_\\-\\s?]+$";
//			String regEx2 = "^[\\s?]+$";
//			String regEx3 = "^[0-9a-zA-Z_\\-?]+$";
//			// 编译正则表达式
//			Pattern pattern1 = Pattern.compile(regEx1);
//			Pattern pattern2 = Pattern.compile(regEx2);
//			Pattern pattern3 = Pattern.compile(regEx3);
////			Pattern pattern4 = Pattern.compile(regEx2);
////			Pattern pattern5 = Pattern.compile(regEx1);
//			// 忽略大小写的写法
//			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//			Matcher matcher1 = pattern1.matcher(internalName);
//			Matcher matcher2 = pattern2.matcher(internalName);
//			Matcher matcher3 = pattern1.matcher(brandName);
//			Matcher matcher4 = pattern2.matcher(brandName);
//			Matcher matcher5 = pattern3.matcher(productId);
//			// 字符串是否与正则表达式相匹配
//			boolean rs1 = matcher1.matches();
//			boolean rs2 = matcher2.matches();
//			boolean rs3 = matcher3.matches();
//			boolean rs4 = matcher4.matches();
//			boolean rs5 = matcher5.matches();
//					    
//					    
//			if(!rs1 || rs2){
//			return ServiceUtil.returnError("Please fill in Name And Model or Brand Name in letter,number,'-','_' and blank space .not all blank space ");
//			}	
//			if(!rs3 || rs4){
//				return ServiceUtil.returnError("Please fill in Name And Model or Brand Name in letter,number,'-','_' and blank space .not all blank space ");
//			}
//			if(!rs5){
//				return ServiceUtil.returnError("Please fill in ProductId in letter,number,'-'or'_'");
//			}
			
			
			
////			Pattern pattern4 = Pattern.compile(regEx2);
////			Pattern pattern5 = Pattern.compile(regEx1);
//			// 忽略大小写的写法
//			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//			Matcher matcher1 = pattern1.matcher(internalName);
//			Matcher matcher2 = pattern2.matcher(internalName);
//			Matcher matcher3 = pattern1.matcher(brandName);
//			Matcher matcher4 = pattern2.matcher(brandName);
//			Matcher matcher5 = pattern3.matcher(productId);
//			// 字符串是否与正则表达式相匹配
//			boolean rs1 = matcher1.matches();
//			boolean rs2 = matcher2.matches();
//			boolean rs3 = matcher3.matches();
//			boolean rs4 = matcher4.matches();
//			boolean rs5 = matcher5.matches();
//					    
//					    
//			if(!rs1 || rs2){
//			return ServiceUtil.returnError("Please fill in Name And Model or Brand Name in letter,number,'-','_' and blank space .not all blank space ");
//			}	
//			if(!rs3 || rs4){
//				return ServiceUtil.returnError("Please fill in Name And Model or Brand Name in letter,number,'-','_' and blank space .not all blank space ");
//			}
//			if(!rs5){
//				return ServiceUtil.returnError("Please fill in ProductId in letter,number,'-'or'_'");
//			}
			
			
			
			String isVirtual = null;
			String isVariant = null;
			GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId",productId), false);
			if(product == null){
				isVirtual = "N";
				isVariant = "N";
			}
			
			Timestamp nowTime = new Timestamp(new Date().getTime());
			Map<String, Object> map = newAddProduct(delegator, productId, nowTime, internalName, brandName, userLoginId, productCategoryId,isVirtual,isVariant);
			
			//当没有传变型属性值过来的时候，直接创造产品。返回产品Id
			if(productFeatureJsonArr.size() <= 0){
//				String internalName = jsonObject.getString("internalName");
//				String brandName = jsonObject.getString("productName");
				Map<String, Object> resulteMsg = ServiceUtil.returnSuccess("create success");
				resulteMsg.put("productId", productId);
				return resulteMsg;
			}
			
			
			
			JSONArray gertarList = new JSONArray();
			for (Object productFeatureobject : productFeatureJsonArr) {
				String productFeatureString = productFeatureobject.toString();
				JSONObject json1 = JSON.parseObject(productFeatureString);
				Set<String> set = json1.keySet();
				for (String string : set) {
					Object str = json1.get(string);
					JSONArray jsonArray = JSON.parseArray(str.toString());
					gertarList.add(jsonArray);
					
				}
				
			}
			
			//将特性重新组合起来，形成新的sku信息组合
			JSONArray tergarList = new JSONArray();
			JSONArray list = getFeatureProduct(gertarList);
			for (Object productFeature : list) {
				Map<String, Object> productFeatureId = new HashMap<String, Object>();
				productFeatureId.put("productId", productId);
				productFeatureId.put("productCategoryId", jsonObject.getString("productCategoryId"));
				productFeatureId.put("internalName", jsonObject.getString("internalName"));
				productFeatureId.put("productName", jsonObject.getString("internalName"));
				productFeatureId.put("brandName", jsonObject.getString("brandName"));
				productFeatureId.put("productFeatureId", productFeature);
				tergarList.add(productFeatureId);
			}
			//调用addEntityToList方法将数据写到数据库
			Map<String, Object> productIdSkuMap = new HashMap<String, Object>();
			if(tergarList.size() > 0){
				JSONObject json1 = tergarList.getJSONObject(0);
//				String productId = json1.getString("productId");
//				List<GenericValue> checkExist = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId", productId));
				productIdSkuMap = addEntityToList(request,tergarList,delegator,userLoginId,toBeStored,0);
			}
			Map<String, Object> resulteMsg;
			if(productIdSkuMap.containsKey("responseMessage") || productIdSkuMap.containsKey("successMessage")){
				resulteMsg = ServiceUtil.returnSuccess("create success");
			}else{
				resulteMsg = ServiceUtil.returnSuccess("create success");
			}
			resulteMsg.put("productId", productId);
			return resulteMsg;
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create fails");
		}		
	}
	/**
	 * 拼接组合productFeature组合
	 * @param testList
	 * @return
	 */
	public static JSONArray getFeatureProduct(JSONArray testList){
		JSONArray resultList = new JSONArray(); 
		if(testList.size() == 0){
			return null;
		}
		else if(testList.size() == 1){
			JSONArray list = (JSONArray) testList.get(0);
			for (Object string : list) {
				JSONArray newList = new JSONArray();
				newList.add(string);
				resultList.add(newList);
			}
			testList.remove(0);
			return resultList;
		}else{
			//获取第一个数组
			JSONArray newList = new JSONArray();
			JSONArray list = (JSONArray) testList.get(0);
			testList.remove(0);
			resultList = getFeatureProduct(testList);
			
			for (Object list2 : resultList) {
				String list3 = list2.toString();
				JSONArray list4 = JSON.parseArray(list3);
				for (Object string : list) {
					JSONArray newitem = new JSONArray();
					
					for (Object string2 : list4) {
						newitem.add(string2);
					}
					newitem.add(string);
					newList.add(newitem);
				}
			}
			return newList;
		}	
	}


	
	
	
	
	
	
	/*
	 * 
	 * 插入或者跟新产品变型
	 * 需要参数：	productId	productTypeId	internalName	productName		
	 * 			ProductFeatureId
	 * 
	 * chenshihua
	 * 2017-4-10
	 */
	public static Map<String, Object> addVariantProduct(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			HttpSession session = request.getSession();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String userLoginId = userLogin.getString("userLoginId");
			
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to add");
	        }
			
			StringBuffer jsonStringBuffer = new StringBuffer();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
			String productPriceList =  jsonStringBuffer.toString();
			JSONArray json = JSON.parseArray(productPriceList);
			Map<String, Object> productIdSkuMap = new HashMap<String, Object>();
			if(json.size() > 0){
				JSONObject json1 = json.getJSONObject(0);
				String productId = json1.getString("productId");
				List<GenericValue> checkExist = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId", productId));
				productIdSkuMap = addEntityToList(request,json,delegator,userLoginId,toBeStored,checkExist.size());
				
			}
			
			return ServiceUtil.returnSuccess("create success");
//			delegator.storeAll(toBeStored);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create fails");
		}
		
		
		
		
	}
	
	
	
	//把json数据添加到List<GenericValue>里
	/**
	 * 将已经变型的产品的数据写到数据库里
	 * @param request
	 * @param json	存有一个sku产品的JSONArray数据
	 * @param delegator
	 * @param userLoginId
	 * @param toBeStored
	 * @param index	数据库中存在多少个已经变型的产品
	 * 
	 * chenshihua
	 */
	public static Map<String, Object> addEntityToList(HttpServletRequest request,JSONArray json,Delegator delegator,String userLoginId,List<GenericValue> toBeStored,int index){
		Map<String, Object> productIdSkuMap = new HashMap<String, Object>();
		boolean flag = false;
		try {
			Timestamp nowTime = new Timestamp(new Date().getTime());
			
			//查询数据库里原有的productIdTo对应的ProductFeature			保存在findOldProductFeatureList中
			JSONObject jsonObject = json.getJSONObject(0);
			String productId = jsonObject.getString("productId");
			String productCategoryId = jsonObject.getString("productCategoryId");
			List<GenericValue> productIdToList = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productId", productId));
			List<String> productIdTo = new LinkedList<String>();
			if(productIdToList.size() > 0){
				for (GenericValue item : productIdToList) {
					productIdTo.add(item.getString("productIdTo"));
				}
			}
			List<List<String>> findOldProductFeatureList = new ArrayList<List<String>>();
			for (String string : productIdTo) {
				List<String> list  = new ArrayList<String>();
				List<GenericValue> oldProductFeatureList = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId", string));
				for (GenericValue genericValue : oldProductFeatureList) {
					list.add(genericValue.getString("productFeatureId"));
				}
				findOldProductFeatureList.add(list);
			}
			
			
			for (String string : productIdTo) {
				JSONObject jsonObj = json.getJSONObject(0);
				String internalName = jsonObj.getString("internalName");
				String brandName = jsonObj.getString("brandName");
				GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", string), false);
				product.put("productName", internalName);
				product.put("internalName", internalName);
				product.put("brandName", brandName);
				toBeStored.add(product);
				
				
				
				
				GenericValue productCategoryMember ; 
				List<GenericValue> productCategoryMemberList = delegator.findByAnd("ProductCategoryMember", UtilMisc.toMap("productId",string));
				if(productCategoryMemberList.size() > 0){
					delegator.removeByAnd("ProductCategoryMember", UtilMisc.toMap("productId",string));
				}
				productCategoryMember = delegator.makeValue("ProductCategoryMember");
				productCategoryMember.put("fromDate", nowTime);
				productCategoryMember.put("productCategoryId", productCategoryId);
				productCategoryMember.put("productId", string);
				
				toBeStored.add(productCategoryMember);
				
				
			}
			//遍历发送过来的ProductFeatureList 
			for (int i = 0; i < json.size(); i++) {
				ProductIdSku productIdSku = new ProductIdSku();
				List<String> productFeatureDescriptionList = new ArrayList<String>();
				JSONObject jsonObj = json.getJSONObject(i);
				String productFeatureId = jsonObj.getString("productFeatureId");
				JSONArray jsonarry = JSONArray.parseArray(productFeatureId);
				String prodId = null;
				
				//判断遍历中的ProductFeature组合中数据库中是否存在
				
				flag = ieeq(findOldProductFeatureList,jsonarry);
				if(findOldProductFeatureList.size() > 0){
					if(!flag){
						continue;
					}
				}
				
				
				
				
				/*
				 * 
				 * 如果没有父类产品，创建父类产品
				 * 
				 */
				if(index == 0){
//					jsonObj.getString("productId");
					String internalName = jsonObj.getString("internalName");
					String brandName = jsonObj.getString("brandName");
					String isVirtual = "Y";
					String isVariant = "Y";
					Map<String, Object> resultMap = newAddProduct(delegator,productId,nowTime,internalName,brandName,userLoginId,productCategoryId,isVirtual,isVariant);
					if(resultMap.get("responseMessage").equals("error")){
						return ServiceUtil.returnError("create fails");
					}
				}
				
				//自动生成ProductId
				if(productIdTo.size() > 0){
					
					do {
						index = index+1;
						prodId = productId + "-" + index;
					} while (productIdTo.contains(prodId));
					
				}else{
					index = index+1;
					prodId = productId + "-" + index;
				}
				
			
				
				
				//插入或修改变型产品	entity-name:product
				GenericValue productSku;
				
				List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId", prodId));
				if(productList.size() > 0){
					productSku = EntityUtil.getFirst(productList);
				}else{
					productSku = delegator.makeValue("Product");
					productSku.put("createdDate", nowTime);
					productSku.put("createdByUserLogin", userLoginId);
				}
				productSku.put("productId", prodId);
				productSku.put("productTypeId", "FINISHED_GOOD");
				productSku.put("productName", jsonObj.getString("internalName"));
				productSku.put("internalName", jsonObj.getString("internalName"));
				productSku.put("brandName", jsonObj.getString("brandName"));	
				productSku.put("isVirtual", "N");
				productSku.put("isVariant", "N");
				productSku.put("lastModifiedDate", nowTime);
				productSku.put("lastModifiedByUserLogin", userLoginId);
				toBeStored.add(productSku);
				
				
				
				
				
				
				/*
				 * 在productCategoryMember中新增和修改新增的变型产品和分类的关系
				 */
				GenericValue productCategoryMember;
				productCategoryId = jsonObj.getString("productCategoryId");
				List<GenericValue>productCategoryMemberList = delegator.findByAnd("ProductCategoryMember", UtilMisc.toMap("productId", prodId,"productCategoryId",productCategoryId));
				if(productCategoryMemberList.size() > 0){
					productCategoryMember = EntityUtil.getFirst(productCategoryMemberList);
					Map<String, Object> reqproductCategoryMemberMap = UtilHttp.getParameterMap(request);
					reqproductCategoryMemberMap.put("productId", prodId);
					reqproductCategoryMemberMap.put("productCategoryId", productCategoryId);
					reqproductCategoryMemberMap.put("fromDate", productCategoryMember.getString("fromDate"));
					productCategoryMember = delegator.findOne("ProductCategoryMember", reqproductCategoryMemberMap, false);
					reqproductCategoryMemberMap.clear();
					
				}else{
					productCategoryMember = delegator.makeValue("ProductCategoryMember");
				}
				
				productCategoryMember.put("productCategoryId", productCategoryId);
				productCategoryMember.put("productId", prodId);
				productCategoryMember.put("fromDate", nowTime);
				toBeStored.add(productCategoryMember);
				
				
				
				//插入或修改	entity-name:productFeatureAppl
				GenericValue productFeatureAppl;
				
				for (Object object : jsonarry) {
					String pFId = (String) object;
					List<GenericValue> productFeatureApplList = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId", prodId,"productFeatureId",pFId));
					if(productFeatureApplList.size() > 0){
						productFeatureAppl = EntityUtil.getFirst(productFeatureApplList);
						Map<String, Object> reqProductFeatureApplMap = UtilHttp.getParameterMap(request);
						reqProductFeatureApplMap.put("productId", prodId);
						reqProductFeatureApplMap.put("productFeatureId", pFId);
						reqProductFeatureApplMap.put("fromDate", productFeatureAppl.getString("fromDate"));
						productFeatureAppl = delegator.findOne("ProductFeatureAppl", reqProductFeatureApplMap, false);
						reqProductFeatureApplMap.clear();
					}else{
						productFeatureAppl = delegator.makeValue("ProductFeatureAppl");
						productFeatureAppl.put("fromDate", nowTime);
					}
					productFeatureAppl.put("productId", prodId);
					productFeatureAppl.put("productFeatureId", pFId);
					productFeatureAppl.put("productFeatureApplTypeId", "SELECTABLE_FEATURE");
					toBeStored.add(productFeatureAppl);
					
					List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureId",pFId));
					GenericValue productFeature = EntityUtil.getFirst(productFeatureList);
					productFeatureDescriptionList.add(productFeature.getString("description"));
					
				}
				
				productIdSku.setProductIdSku(prodId);
				productIdSku.setProductFeature_description(productFeatureDescriptionList);
				
				
				
				//插入或修改	entity-name:productAssoc
				GenericValue productAssoc;
				List<GenericValue> productAssoclList = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productId", productId,"productIdTo",prodId));
				if(productAssoclList.size() > 0){
					productAssoc = EntityUtil.getFirst(productAssoclList);
					Map<String, Object> reqProductAssocMap = UtilHttp.getParameterMap(request);
					reqProductAssocMap.put("productId", productId);
					reqProductAssocMap.put("productIdTo", prodId);
					reqProductAssocMap.put("productAssocTypeId", "PRODUCT_VARIANT");
					reqProductAssocMap.put("fromDate", productAssoc.getString("fromDate"));
					productAssoc = delegator.findOne("ProductAssoc", reqProductAssocMap, false);
					reqProductAssocMap.clear();
				}else{
					productAssoc = delegator.makeValue("ProductAssoc");
					productAssoc.put("fromDate", nowTime);
				}
				productAssoc.put("productId", productId);
				productAssoc.put("productIdTo", prodId);
				productAssoc.put("productAssocTypeId", "PRODUCT_VARIANT");
				toBeStored.add(productAssoc);
				
				
				productIdSkuMap.put(prodId, productIdSku);
			}
			delegator.storeAll(toBeStored);
			if(flag){
				return ServiceUtil.returnSuccess("create success");
			}
			return ServiceUtil.returnSuccess("create success");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("create fails");
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 查找ProductFeature和ProductFeatureType所有的数据
	 * 
	 * chenshihua
	 * 2017-4-11
	 */
	public static Map<String, Object> findAllProductFeatureTypeAndProduceFeaturAppl(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> productFeatureMap = new HashMap<String, Object>();
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			List<GenericValue> allProductFeatureType = delegator.findAll("ProductFeatureType");
			for (GenericValue ProductFeatureType : allProductFeatureType) {
				FindProductFeatureAndProductFeatureType object = new FindProductFeatureAndProductFeatureType();
				String productFeatureTypeId = ProductFeatureType.getString("productFeatureTypeId");
				object.setProductFeatureTypeId(productFeatureTypeId);
				object.setDescription(ProductFeatureType.getString("description"));
				
				
				List<FindProductFeatureBean> productFeatureBeanList = new ArrayList<FindProductFeatureBean>();
				List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
				if (productFeatureList.size()>0) {
					for (GenericValue genericValue : productFeatureList) {
						FindProductFeatureBean productFeature = new FindProductFeatureBean();
						productFeature.setProductFeatureId(genericValue.getString("productFeatureId"));
						productFeature.setDescription(genericValue.getString("description"));
						productFeature.setProductFeatureTypeId(genericValue.getString("productFeatureTypeId"));
						productFeatureBeanList.add(productFeature);
					}
					object.setProductFeatureList(productFeatureBeanList);
				}
				productFeatureMap.put(ProductFeatureType.getString("productFeatureTypeId"), object);
			}
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("productFeatureMap", productFeatureMap);
			return resultMap;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query fails");
		}
		
	}
	
	
	
	
	/**
	 * 
	 * 查询表头，查询一个可变型产品中的sku总共有多少个属性
	 * 
	 * chenshihua
	 * @param 	productId 	可变型产品的id
	 * 
	 */
	public static Map<String, Object> findTableHead(HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String receiveProductId = request.getParameter("productId");
			List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId",receiveProductId));
			
			if(productList.size() > 0){
				List<GenericValue> productAssocList = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productId",receiveProductId));
				//判定productAssoc该产品中是否存在变型
				Map<String, Object> resultMap;
				if(productAssocList.size() > 0 ){
					List<String> productIdToList = new ArrayList<String>();
					for (GenericValue productAssoc : productAssocList) {
						productAssoc.getString("productIdTo");
						productIdToList.add(productAssoc.getString("productIdTo"));
					}
					List<GenericValue> ProductFeaturelist = delegator.findAll("ProductFeature");
					Map<String, String> ProductFeatureMap = new HashMap<String, String>();
					for (GenericValue genericValue : ProductFeaturelist) {
						ProductFeatureMap.put(genericValue.getString("productFeatureId"), genericValue.getString("description"));
					}
					List<String> list = new ArrayList<String>();
					for (String productIdTo : productIdToList) {
//						Map<String, String> description = new HashMap<String, String>();
						List<GenericValue> ProductFeatureApplList = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId",productIdTo));
						for (GenericValue ProductFeatureAppl : ProductFeatureApplList) {
							String productFeatureId = ProductFeatureAppl.getString("productFeatureId");
							list.add(productFeatureId);
						}
					}
					List<String> productFeatureTypeList = new LinkedList<String>();
					for (String productFeatureId : list) {
						GenericValue ProductFeaturelList = delegator.findOne("ProductFeature", UtilMisc.toMap("productFeatureId",productFeatureId), false);
						if(!productFeatureTypeList.contains(ProductFeaturelList.getString("productFeatureTypeId"))){
							productFeatureTypeList.add(ProductFeaturelList.getString("productFeatureTypeId"));
						}
					}
					
					
					Map<String, Object> productFeatureMap = new HashMap<String, Object>();
					//productFeatureTypeList得到productId所有的productFeatureTypeId
					
					//获取productFeatureTypeId和productFeatureType的dscription  以及productFeatureType 的所有子集属性
					for (String productFeatureTypeId : productFeatureTypeList) {
						GenericValue ProductFeatureType = delegator.findOne("ProductFeatureType",  UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId), false);
						FindProductFeatureAndProductFeatureType findProductFeatureAndProductFeatureType = new FindProductFeatureAndProductFeatureType();
						
						List<FindProductFeatureBean> findProductFeatureBeanList = new ArrayList<FindProductFeatureBean>();
						//获取productFeature中的信息放到findProductFeatureBean中
						List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeature",  UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
						for (GenericValue genericValue : productFeatureList) {
							FindProductFeatureBean findProductFeatureBean = new FindProductFeatureBean();
							findProductFeatureBean.setDescription(genericValue.getString("description"));
							findProductFeatureBean.setProductFeatureId(genericValue.getString("productFeatureId"));
							findProductFeatureBean.setProductFeatureTypeId(genericValue.getString("productFeatureTypeId"));
							findProductFeatureBeanList.add(findProductFeatureBean);
						}
						
						
						findProductFeatureAndProductFeatureType.setProductFeatureTypeId(productFeatureTypeId);
						findProductFeatureAndProductFeatureType.setDescription(ProductFeatureType.getString("description"));
						findProductFeatureAndProductFeatureType.setProductFeatureList(findProductFeatureBeanList);
						productFeatureMap.put(productFeatureTypeId, findProductFeatureAndProductFeatureType);
					}
					resultMap = ServiceUtil.returnSuccess("query success");
					resultMap.put("productFeatureMap", productFeatureMap);
					return resultMap;
				}else{
					resultMap = ServiceUtil.returnSuccess("The product is not variant");
					return resultMap;
				}
			}else{
				return ServiceUtil.returnError("Can't find the product ");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Can't find the product ");
		}
	}
	
	
	
	
	
	
	
	/**
	 * 查询一个可变型产品的所有sku以及属性值
	 * chenshihua
	 * @param productId 	可变型产品的id
	 */
	public static Map<String, Object> findAllProductFeature(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			Map<String, Object> PriceProductFeatureMap = new HashMap<String, Object>();
			List<PriceProductFeatureBean> successMessageList = new ArrayList<PriceProductFeatureBean>();
			
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String receiveProductId = request.getParameter("productId");
//			if(receiveProductId == null || receiveProductId.equals("")){
//				return ServiceUtil.returnError("没有找到productId");
//			}
			//判断是否存在物品
			List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId",receiveProductId));
			if(productList.size() > 0){
				List<GenericValue> productAssocList = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productId",receiveProductId));
				//判定productAssoc该产品中是否存在变型
				if(productAssocList.size() > 0 ){
					List<String> productIdToList = new ArrayList<String>();
					for (GenericValue productAssoc : productAssocList) {
						productAssoc.getString("productIdTo");
						productIdToList.add(productAssoc.getString("productIdTo"));
					}
					
					//查询ProductFeature 中的  productId和ProductFeatureId
					Map<String, String> ProductFeatureMap = new HashMap<String, String>();
					List<GenericValue> allProductFeature = delegator.findAll("ProductFeature");
					for (GenericValue ProductFeature : allProductFeature) {
						ProductFeatureMap.put(ProductFeature.getString("productFeatureId"), ProductFeature.getString("description"));
					}
					
					
					for (String productIdTo : productIdToList) {
						PriceProductFeatureBean priceProductFeatureBean = new PriceProductFeatureBean();
						Map<String, String> description = new HashMap<String, String>();
						List<GenericValue> ProductFeatureApplList = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productId",productIdTo));
						for (GenericValue ProductFeatureAppl : ProductFeatureApplList) {
							
							//返回给list的map
							
							
							String productFeatureId = ProductFeatureAppl.getString("productFeatureId");
//							String productFeatureDescription = ProductFeatureAppl.getString("productFeatureDescription");
							priceProductFeatureBean.setProductIdSku(productFeatureId);
							String productFeatureDescript = ProductFeatureMap.get(productFeatureId);
							List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureId",productFeatureId));
							GenericValue productFeature = EntityUtil.getFirst(productFeatureList);
							String productFeatureTypeId = productFeature.getString("productFeatureTypeId");
							
							List<GenericValue> productFeatureTypeList = delegator.findByAnd("ProductFeatureType", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
							GenericValue productFeatureType = EntityUtil.getFirst(productFeatureTypeList);
							description.put(productFeatureType.getString("description"), productFeatureDescript);
//							PriceProductFeatureMap.put(productId, priceProductFeatureBean);
							
						}
						priceProductFeatureBean.setProductIdSku(productIdTo);
						priceProductFeatureBean.setProductFeatureMap(description);
						PriceProductFeatureMap.put(productIdTo, priceProductFeatureBean);
						successMessageList.add(priceProductFeatureBean);
					}
				}else{
					GenericValue product = EntityUtil.getFirst(productList);
					Map<String, Object> productFeatureMap = new HashMap<String, Object>();
					productFeatureMap.put("productIdSku", receiveProductId);
					PriceProductFeatureMap.put(product.getString("productId"), productFeatureMap);
				}
			}
//			return ServiceUtil.returnSuccess(successMessageList);
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("PriceProductFeatureMap", PriceProductFeatureMap);
			return resultMap;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query success");
		}
	}
	
	
	
	

	/**
	 * 删除sku
	 * @param request
	 * @param productSKUId 产品sku  id
	 * @return
	 */
	public static Map<String, Object> deleteSKU(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			
			//判断权限
			String updateMode = "DELETE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to delete");
	        }
			
			
			String productId = request.getParameter("productSKUId");
			if(productId == null || "".equals(productId)){
				return ServiceUtil.returnError("Can't get productId");
			}
			List<GenericValue> InventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId",productId));
//			GenericValue InventoryItem = EntityUtil.getFirst(InventoryItemList);
			if(InventoryItemList.size() > 0){
				return ServiceUtil.returnError("This goods in stock，Can't delete ");
			}else{
				int deleteProductFeatureAppl = delegator.removeByAnd("ProductFeatureAppl", UtilMisc.toMap("productId",productId));
				int deleteProductAssoc = delegator.removeByAnd("ProductAssoc", UtilMisc.toMap("productIdTo",productId));
				Map<String, Object> resultMap = ServiceUtil.returnSuccess("Delete success");
				resultMap.put("deleteProductFeatureAppl", deleteProductFeatureAppl);
				resultMap.put("deleteProductAssoc", deleteProductAssoc);
				return resultMap;
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Delete fails");
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 删除属性，删除当前的属性和属性值。如果属性值有 被使用则无法删除
	 * chenshihua
	 * @param productFeatureTypeId	
	 * 
	 */
	public static Map<String, Object> deleteProductFeatureType(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String productFeatureTypeId = request.getParameter("productFeatureTypeId");
			List<GenericValue> productFeatureType = delegator.findByAnd("ProductFeatureType", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
			List<GenericValue> productFeature = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
			
			//判断权限
			String updateMode = "DELETE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission to delete");
	        }
			
			
			List<String> productFeatureList = new ArrayList<String>();
			for (GenericValue genericValue : productFeature) {
				String ProductFeatureId = genericValue.getString("productFeatureId");
				productFeatureList.add(ProductFeatureId);
			}
			if(productFeatureType.size() <= 0 ){
				return ServiceUtil.returnError("Without data ，Delete fails");
			}else{
				Map<String, Object> reslutMap = new HashMap<String, Object>();
				for (String string : productFeatureList) {
					List<GenericValue> list = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productFeatureId",string));
					if(list.size() > 0){
						reslutMap.put(string, list);
					}
				}
				if(reslutMap.size() > 0){
					return ServiceUtil.returnError("Delete fails ,have product in the use of the attribute value ");
				}else{
					delegator.removeByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
					delegator.removeByAnd("ProductFeatureType", UtilMisc.toMap("productFeatureTypeId",productFeatureTypeId));
					return ServiceUtil.returnSuccess("delete success");
				}
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("delete fails");
		}
	}
	
	/*
	 * 判断一个list是否包含另外一个list
	 */
	public static boolean ieeq(List<List<String>> findOldProductFeatureList,JSONArray jsonarry){
		
		boolean flag = true;
		
		List<String> newProductFeatureList = new ArrayList<String>();
		for (Object item : jsonarry) {
			newProductFeatureList.add((String)item);
		}
		
		for (List<String> itemList : findOldProductFeatureList) {
			flag = flag&&compareList(itemList, newProductFeatureList);
		}
		return flag;
		
	}
	
	
	
	public static boolean compareList (List<String> itemList,List<String> newProductFeatureList){
		if(itemList.size() != newProductFeatureList.size()){
			return true;
		}
		for (String item : itemList) {
			if(!newProductFeatureList.contains(item)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	public static GenericValue findProductById(Delegator delegator, String productId) {
		try {
			GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId",productId),true);
			return product;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public static Map<String, Object> findProductNameById(HttpServletRequest request, HttpServletResponse response) {
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String productId = request.getParameter("productId");
			GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId",productId),true);
			if(product != null){
				Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
				Map<String, Object> map = new HashMap<String, Object>();
				List<Object> resultList = new ArrayList<Object>();
				map.put("productId", product.getString("productId"));
				map.put("internalName", product.getString("internalName"));
				map.put("brandName", product.getString("brandName"));
				resultMap.put("result", map);
				return resultMap;
			}else{
				return ServiceUtil.returnError("query fails");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return ServiceUtil.returnError("query fails");
		}
		
	}
	
	
	
	public static Map<String, Object> createProductAndGotoPrice(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			//判断权限
			String updateMode = "CREATE";
			boolean flag = hasEntityPermission(request, updateMode);
			if (!flag) {
	            return ServiceUtil.returnError("no permission");
	        }
			
			HttpSession session = request.getSession();
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			GenericValue product = delegator.makeValue("Product");
			List<GenericValue> toBeStored = new ArrayList<GenericValue>();
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
			String productId = request.getParameter("productId");
			if(productId == null || "".equals(productId)){
				return ServiceUtil.returnError("please input productId");
			}
			String productTypeId = request.getParameter("productTypeId");
			String internalName = request.getParameter("internalName");
			String productName = request.getParameter("internalName");
			String brandName = request.getParameter("brandName");
			String userLoginId = userLogin.getString("userLoginId");
			String productCategoryId = request.getParameter("productCategoryId");
			Timestamp nowTime = new Timestamp(new Date().getTime());
			
			List<GenericValue> productAssocList = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productId",productId));
			List<String> productIdList = new ArrayList<String>();
			for (GenericValue productAssoc : productAssocList) {
				productIdList.add(productAssoc.getString("productIdTo"));
			}
			productIdList.add(productId);
			for (String item : productIdList) {
				newAddProduct(delegator, item, nowTime, internalName, brandName, userLoginId, productCategoryId, null, null);
			}

			return ServiceUtil.returnSuccess("save product success");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("save product fails");
		}
	}
	
	
	public static boolean hasEntityPermission(HttpServletRequest request ,String updateMode ){
		String errMsg=null;
		Security security = (Security) request.getAttribute("security");
		if (!security.hasEntityPermission("CATALOG", "_" + updateMode, request.getSession())) {
            Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
            errMsg = UtilProperties.getMessage(resource,"productevents.not_sufficient_permissions", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return false;
        }else{
        	return true;
        }
	}
		
	
	
	
	public static Map<String, Object> hasProductPriceRule(HttpServletRequest request, HttpServletResponse response) {
		try {
			String productId = request.getParameter("productId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
		
			List<GenericValue> productPriceCondList = delegator.findByAnd("ProductPriceCond", UtilMisc.toMap("condValue",productId));
			System.out.println(productPriceCondList.size());
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			if(productPriceCondList.isEmpty()){
				resultMap.put("hasProductPriceRule", "N");
				return resultMap;
			}else{
				resultMap.put("hasProductPriceRule", "Y");
				return resultMap;
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
		
	}
	
	public static Map<String, Object> hasProductPrice(HttpServletRequest request, HttpServletResponse response) {
		try {
			String productId = request.getParameter("productId");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
		
			List<GenericValue> productPriceList = delegator.findByAnd("ProductPrice", UtilMisc.toMap("productId",productId));
			System.out.println(productPriceList.size());
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			if(productPriceList.isEmpty()){
				resultMap.put("hasProductPrice", "N");
				return resultMap;
			}else{
				resultMap.put("hasProductPrice", "Y");
				return resultMap;
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
		
	}
	
	
	public static Map<String, Object> hasInventoryItem(HttpServletRequest request, HttpServletResponse response) {
		String productId = request.getParameter("productId");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		String sql = "SELECT COUNT(1) AS count FROM INVENTORY_ITEM WHERE PRODUCT_ID='' AND QUANTITY_ON_HAND_TOTAL>0 AND IFNULL(STATUS_ID,'')!='INV_PROMISED'";
		ResultSet rs = null;
		Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
		try {		
			rs = processor.executeQuery(sql);		
			while(rs.next()){
				if(rs.getInt("count") > 0){
					resultMap.put("hasInventoryItem", "Y");
				}else{
					resultMap.put("hasInventoryItem", "N");
				}
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				processor.close();
			} catch (GenericDataSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultMap;
		
	}
		
	
		
	
}
