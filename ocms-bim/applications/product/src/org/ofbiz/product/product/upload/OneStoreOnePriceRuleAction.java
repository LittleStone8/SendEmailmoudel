package org.ofbiz.product.product.upload;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.ServiceUtil;

public class OneStoreOnePriceRuleAction {

    private List<GenericValue> toBeStoredList = new ArrayList<GenericValue>();
    private Delegator delegator;

    private Map<String, GenericValue> storemap = new HashMap<String, GenericValue>();

    public OneStoreOnePriceRuleAction(Delegator delegator) {
	this.delegator = delegator;
    }

    public Map<String, Object> preCheck(String productId, String storeName,String price,String model,String brand,String description) {
	List<String> errorList = new ArrayList<String>();
	Map<String, Object> productRes = checkproductId(productId, model, brand, description);
	if (ServiceUtil.isError(productRes)) {
	    errorList.add(ServiceUtil.getErrorMessage(productRes));
	}
	Map<String, Object> storeRes = checkstoreName(storeName);
	if (ServiceUtil.isError(storeRes)) {
	    errorList.add(ServiceUtil.getErrorMessage(storeRes));
	}
	
	try {
	    if(price == null){
		 errorList.add("price ");
	    }else{
		BigDecimal priceBigDecimal = new BigDecimal(price);
		if(priceBigDecimal.compareTo(priceBigDecimal.ZERO) <= 0){
		    errorList.add("price ");
		}
	    }
	    
	} catch (Exception e) {
	    errorList.add("price ");
	}
	
	if (errorList.size() > 0) {
	    return ServiceUtil.returnError(errorList);
	}

	Map<String, Object> map = ServiceUtil.returnSuccess();
	map.put("product", productRes.get("product"));
	map.put("store", storeRes.get("store"));
	return map;
    }

    public Map<String, Object> addOneStoreOnePricePromo(String productId, String storeName, String price,
	    Timestamp fromDate, Timestamp thruDate,String createdUserStr,String model,String brand,String description) {

	// Map<String, Object> productRes = checkproductId(productId);
	// if(ServiceUtil.isError(productRes)){
	// return productRes;
	// }
	// Map<String, Object> storeRes = checkproductId(productId);
	// if(ServiceUtil.isError(storeRes)){
	// return storeRes;
	// }

	Map<String, Object> preCheckRes = preCheck(productId, storeName,price,model,brand,description);
	if (ServiceUtil.isError(preCheckRes)) {
	    System.out.println("preCheckRes "+ServiceUtil.getErrorMessage(preCheckRes));
	    return preCheckRes;
	}

//	BigDecimal amount = new BigDecimal(price);

	GenericValue product = (GenericValue) preCheckRes.get("product");
	GenericValue store = (GenericValue) preCheckRes.get("store");

	String productPriceRuleId = delegator.getNextSeqId("ProductPriceRule");

	String productStoreId = store.getString("productStoreId");

	// 产品价格规则
	createProductPriceRule(productPriceRuleId,fromDate,thruDate,createdUserStr);
	// 产品价格条件
	createProductPriceCond(productPriceRuleId, productId,productStoreId);

	// 产品价格动作
	createProductPriceAction(productPriceRuleId,price);

	// //促销
	// createProductPromo(productPromoId);
	// //产品店铺促销关联
	// createProductStorePromoAppl(productPromoId,productStoreId);
	// //产品促销规则
	// createProductPromoRule(productPromoId);
	// //产品促销条件
	// createProductPromoCond(productPromoId);
	// //产品促销动作
	// createProductPromoAction(productPromoId,amount);
	// //产品促销产品
	// createProductPromoProduct(productPromoId,productId);
	//
	// createProductPromoContent(productPromoId, fromDate, thruDate);

	Map<String, Object> storeAllRes = storeAll();
	return storeAllRes;
    }

    
    
    public void createProductPriceAction(String productPriceRuleId,String price){
    	GenericValue productPriceAction = delegator.makeValue("ProductPriceAction");
    	productPriceAction.put("productPriceRuleId", productPriceRuleId);
    	productPriceAction.put("productPriceActionSeqId", "01");
    	productPriceAction.put("productPriceActionTypeId", "PRICE_POL");
    	productPriceAction.put("amount", new BigDecimal(price));
    	toBeStoredList.add(productPriceAction);
    }
    
    
    public  void createProductPriceCond(String productPriceRuleId,String productId,String productStoreId){
    	GenericValue productPriceConds = delegator.makeValue("ProductPriceCond");
    	productPriceConds.put("productPriceRuleId", productPriceRuleId);
    	productPriceConds.put("productPriceCondSeqId", "01");
    	productPriceConds.put("inputParamEnumId", "PRIP_PRODUCT_STORE_ID");
    	productPriceConds.put("operatorEnumId", "PRC_EQ");
    	productPriceConds.put("condValue", productStoreId);
    	
    	GenericValue productPriceCondp = delegator.makeValue("ProductPriceCond");
    	productPriceCondp.put("productPriceRuleId", productPriceRuleId);
    	productPriceCondp.put("productPriceCondSeqId", "02");
    	productPriceCondp.put("inputParamEnumId", "PRIP_PRODUCT_ID");
    	productPriceCondp.put("operatorEnumId", "PRC_EQ");
    	productPriceCondp.put("condValue", productId);
    	
    	toBeStoredList.add(productPriceConds);
    	toBeStoredList.add(productPriceCondp);
    }
    
    
    public  void createProductPriceRule(String productPriceRuleId,Timestamp fromDate,Timestamp thruDate,String createdUserStr){
    	GenericValue productPriceRule = delegator.makeValue("ProductPriceRule");
    	productPriceRule.put("productPriceRuleId", productPriceRuleId);
    	productPriceRule.put("ruleName", productPriceRuleId);
    	productPriceRule.put("isSale", "Y");
    	productPriceRule.put("fromDate", fromDate);
    	productPriceRule.put("thruDate", thruDate);
    	productPriceRule.put("createdUser", createdUserStr);
 	toBeStoredList.add(productPriceRule);
    }




    private Map<String, Object> storeAll() {
	try {
	    
	    System.out.println("toBeStoredList.size()   "+toBeStoredList.size());
	    delegator.storeAll(toBeStoredList);
	    Map<String, Object> map = ServiceUtil.returnSuccess();
	    return map;
	} catch (GenericEntityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return ServiceUtil.returnError("storeAll  " + e.getMessage());
	}
    }

    private Map<String, Object> checkstoreName(String storeName) {
	try {
	    GenericValue store = storemap.get("storeName");
	    if (store == null) {
		// store = delegator.findOne("ProductStore", false,
		// UtilMisc.toMap("storeName", storeName));
		List<GenericValue> storeList = delegator.findByAnd("ProductStore",
			UtilMisc.toMap("storeName", storeName));
		if (storeList != null && storeList.size() > 0) {
		    store = EntityUtil.getFirst(storeList);
		    if (store == null) {
			return ServiceUtil.returnError("storeName  ");
		    }
		    storemap.put(storeName, store);
		} else {
		    return ServiceUtil.returnError("storeName  ");
		}
	    }
	    Map<String, Object> map = ServiceUtil.returnSuccess();
	    map.put("store", store);
	    return map;
	} catch (GenericEntityException e) {
	    e.printStackTrace();
	}
	return ServiceUtil.returnError("storeName  ");
    }

    private Map<String, Object> checkproductId(String productId,String model,String brand,String description) {
	try {
	    // GenericValue product = delegator.findOne("Product", false,
	    // UtilMisc.toMap("productId", productId));
	    List<GenericValue> productList = delegator.findByAnd("Product", UtilMisc.toMap("productId", productId));
	    if (productList != null && productList.size() > 0) {
		GenericValue product = EntityUtil.getFirst(productList);
		if (product == null) {
		    return ServiceUtil.returnError("Product ");
		}
		
		String brandName = product.getString("brandName");
		String internalName = product.getString("internalName");
		if(!internalName.equals(model)){
		    return ServiceUtil.returnError("model ");
		}
		
		if(!brandName.equals(brand)){
		    return ServiceUtil.returnError("brand ");
		}
		
		Map<String, Object> map = ServiceUtil.returnSuccess();
		map.put("product", product);
		return map;
	    } else {
		return ServiceUtil.returnError("Product  ");
	    }

	} catch (GenericEntityException e) {
	    e.printStackTrace();
	}
	return ServiceUtil.returnError("Product ");
    }
    
    

}
