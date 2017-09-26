package org.ofbiz.product.feature.newfunction.product;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.product.feature.newfunction.bean.CreateProductPageBean;
import org.ofbiz.service.ServiceUtil;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * 产品建档模块API
 * @author zhenk
 * 2017.04.05
 */
public class CreateProductEvent {
	public static final String FIRST_CLASSFICATION = "DefaultRootCategory";
	public static final String CLASSFICATION_TYPE = "EGATEE_CATEGORY";
	
	/**
	 * 1.依据指定分类ID，列出其下级类目
	 * @param request:String categoryID
	 * @param response
	 * @return
	 * success: ("categoryList", classficationInfos)
	 * error: 错误提示
	 */
	public static Map<String, Object> listClassficationsByID (HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = ServiceUtil.returnSuccess(); //返回值map
		try{
			TransactionUtil.begin();
			
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
			String productCategoryId = (String) request.getParameter("categoryID");
		    if (!UtilValidate.isNotEmpty(productCategoryId)) {
		    	return ServiceUtil.returnError("ID can't be empty");
		    }
		    
			List<GenericValue> categoryRollUpGVs = getRollUpsGVByID(delegator, productCategoryId);//依据ID查其的子目录list	
			if(categoryRollUpGVs==null){
				return ServiceUtil.returnError("query subclass list fails ");
			}
			
			List<CreateProductPageBean> pageBeens = FastList.newInstance(); // 存放所有classfication信息
			
			for (GenericValue genericValue : categoryRollUpGVs) {
				String productId = (String) genericValue.get("productCategoryId");
				
				GenericValue classficationGV = getClassficationGVbyID(delegator, productId); //依据ID查询其详情		
				if(classficationGV==null){
					return ServiceUtil.returnError("query subclass details fail ");
				}
	
				pageBeens.add(new CreateProductPageBean(productId, (String) classficationGV.get("categoryName")));
			}
	
			map.put("categoryList", pageBeens);
			TransactionUtil.commit();
		}catch (Exception e) {
			// TODO: handle exception
			
		}
		return map;
	}
	/**
	 * 依据ID查其的子目录list	
	 * @param delegator
	 * @param productCategoryId
	 * @return 如果查询失败，返回null;成功则返回 包含子类目ID的 list<GV>
	 */
	private static List<GenericValue> getRollUpsGVByID(GenericDelegator delegator, String productCategoryId) {
		List<GenericValue> categoryRollUpGVs = null; // 存放rollUp表中的所有序列GV
		List<String> orderBy = UtilMisc.toList("sequenceNum"); 		   // 依据seq字段排序
		
		try {
			// 传入父类目ID,得到所有的子类目
			categoryRollUpGVs = delegator.findByAnd("ProductCategoryRollup",
					UtilMisc.toMap("parentProductCategoryId", productCategoryId), orderBy);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryRollUpGVs;
	}
	/**
	 * 依据ID查询其详情
	 * @param delegator
	 * @param productId
	 * @return 成功则返回"ProductCategory"GV;失败返回null
	 */
	private static GenericValue getClassficationGVbyID(GenericDelegator delegator, String productId) {
		GenericValue classficationValue = null;
//		System.out.println("productId:"+productId);
		try {
			classficationValue = delegator.findOne("ProductCategory",
					UtilMisc.toMap("productCategoryId", productId), true);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return classficationValue;
	}

	/**
	 * 2.快速查找指定目录(模糊查询列表);
	 * @param request: 关键字keyWord
	 * @param response
	 * @return 成功则返回一个list1，list1每个元素为list2，list每个元素为pageBean
	 */
	public static Map<String, Object> quickListClassfication(HttpServletRequest request,
			HttpServletResponse response) {
			Map<String, Object> map = ServiceUtil.returnSuccess();
			
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");			
			String keyWord = (String) request.getParameter("keyWord");
			
			if(!UtilValidate.isNotEmpty(keyWord)){
				return ServiceUtil.returnError("Invalid input");
			}
		
            List<GenericValue> classficationGVs = getLikeClassfictionGVs(delegator, keyWord);//获取所有的类似的classfictionGV
            
            if(classficationGVs==null){
            	return ServiceUtil.returnError("fuzzy query matching failed");
            }
            
        	List<LinkedList<CreateProductPageBean>> keyWordClassfication = FastList.newInstance();//用于返回数据	
        	Map<String,GenericValue> genericValueMap = new HashMap<String, GenericValue>();//用于保存genericValue 
			for (GenericValue genericValue : classficationGVs) {			
				String ID = (String)genericValue.get("productCategoryId");
				LinkedList<CreateProductPageBean> quickClassficationRow = getQuickClassficationRow(delegator,ID,genericValueMap);
				if(quickClassficationRow==null){
					return ServiceUtil.returnError("get the query match failed ");
				}
				keyWordClassfication.add(quickClassficationRow); //查询出每一行的模糊查询；
			}			
			map.put("result", keyWordClassfication);
			return map;		
	}
	/**
	 * 获取所有的关键字模糊查询项，把GV放入list
	 * @param delegator
	 * @param keyWord
	 * @return 
	 */
	private static List<GenericValue> getLikeClassfictionGVs(GenericDelegator delegator, String keyWord) {
		List<EntityExpr> exprs = UtilMisc.toList(
				EntityCondition.makeCondition("categoryName",EntityOperator.LIKE,"%"+keyWord+"%"),
		        EntityCondition.makeCondition("productCategoryTypeId", EntityOperator.EQUALS, CLASSFICATION_TYPE),
		        EntityCondition.makeCondition("primaryParentCategoryId", EntityOperator.NOT_EQUAL, null)
		        );
		EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

		List<GenericValue> values =null;
		try {	
			values = delegator.findList("ProductCategory", condition, null, null, null, false);					
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	
	/**
	 * 查询出每一行的模糊查询
	 * @param delegator
	 * @param id
	 * @param map
	 * @return 匹配的模糊查询的GVlist中的每一行
	 */
	public static LinkedList<CreateProductPageBean> getQuickClassficationRow (GenericDelegator delegator,String id,
			Map<String,GenericValue> map){
		LinkedList<CreateProductPageBean> linklist = new LinkedList<CreateProductPageBean>();
		
		String parentID = "";
		int index=0;
		//递归，直到顶层类目是DefaultRoot
		while((!parentID.equals(FIRST_CLASSFICATION))&&index<=2){			
			if(index==0){
				parentID=id;
			}
			parentID=createPageBean(delegator,parentID,map,linklist);
			if(parentID==null){
				return null;
			}
			index++;
		}

		return linklist;
	}

	/**
	 * 创建每一个pageBean，把值放入map，如果再次创建，则从map中取
	 * @param delegator
	 * @param id
	 * @param map
	 * @param linklist
	 * @return 
	 */
	public static String createPageBean(GenericDelegator delegator, String id,
			Map<String,GenericValue> map,LinkedList<CreateProductPageBean> linklist) {
		GenericValue findOne = null;
		String parentID = null;	
		if(map.containsKey(id)){
			findOne = map.get(id);
			linklist.addFirst(new CreateProductPageBean((String)findOne.get("productCategoryId"),(String)findOne.get("categoryName")));
			return (String) findOne.get("primaryParentCategoryId");
		}else{
			try {
				findOne = delegator.findOne("ProductCategory", UtilMisc.toMap("productCategoryId", id), true);
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(findOne!=null){			
				parentID =(String) findOne.get("primaryParentCategoryId");			
				map.put(id, findOne);
				linklist.addFirst(new CreateProductPageBean((String)findOne.get("productCategoryId"),(String)findOne.get("categoryName")));
			}else{
				parentID =null;
			}
			
		}	
		return parentID;
	}
}
