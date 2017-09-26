package com.yiwill.pos.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.service.ServiceUtil;

public class PosWorker {

	 public static GenericValue findProductStoreByProductStoreId(Delegator delegator,String productStoreId) {
			try {
				return delegator.findOne("PosTerminal",  UtilMisc.toMap("productStoreId", productStoreId),false);
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	 }
	
	 public static GenericValue findTerminalByTerminalId(Delegator delegator,String posTerminalId) {
				try {
					return delegator.findOne("PosTerminal",  UtilMisc.toMap("posTerminalId", posTerminalId),false);
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;
	    }
	 
	 public static List<GenericValue> findTerminalByProductStoreId(Delegator delegator,String productStoreId) {
	        GenericValue productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
			String facilityId = productStore.getString("inventoryFacilityId");
			if (facilityId != null) {
				try {
					List<GenericValue> posTerminals = delegator.findList("PosTerminal",
							EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null, null,
							null, false);
					return posTerminals;
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			return null;
	    }
	
	 
	  public static List<GenericValue> getProductStoreByUser(Delegator delegator,GenericValue userLogin) {
		  List<GenericValue> productStoreList = new ArrayList<GenericValue>();
		  Set<GenericValue> productStoreListSet =  new HashSet<GenericValue>();
		  String userLoginId = userLogin.getString("partyId");
     	  try {
     		 List<GenericValue> productStoreRoles = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("partyId", userLoginId), UtilMisc.toList("-fromDate"));
     		 productStoreRoles = EntityUtil.filterByDate(productStoreRoles, true);
				for (GenericValue genericValue : productStoreRoles) {
					String	 productStoreId = genericValue.getString("productStoreId");
					 GenericValue productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
//					 productStoreList.add(productStore);
					 productStoreListSet.add(productStore);
				}
	   		} catch (GenericEntityException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
     	productStoreList.addAll(productStoreListSet);
     	  return productStoreList;
	  }
	  
	  public static List<GenericValue> getPosTerminalByProductStore(Delegator delegator,GenericValue productStore) {
		  
		  if(productStore == null){
			  return null;
		  }
		  
		  	String facilityId = productStore.getString("inventoryFacilityId");
			List<GenericValue> posTerminals = new ArrayList<GenericValue>();
			if (facilityId != null) {
				try {
					posTerminals = delegator.findList("PosTerminal",
							EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null,
							null, null, false);
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return posTerminals;
	  }
	  
	
	 public static Map<String, List<GenericValue>> autoGetTerminalAndProductStoreByUser(Delegator delegator,GenericValue userLogin) {


			Map<String, List<GenericValue>> map = new HashMap<String, List<GenericValue>>();

			if (userLogin == null) {
				return null;
			}
			try {
				List<GenericValue> productStoreList = getProductStoreByUser(delegator,userLogin);
				if (productStoreList != null && productStoreList.size() == 1) {
					GenericValue productStore = EntityUtil.getFirst(productStoreList);
					String facilityId = productStore.getString("inventoryFacilityId");
					List<GenericValue> posTerminals;
					if (facilityId != null) {
						posTerminals = delegator.findList("PosTerminal",
								EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null,
								null, null, false);
					} else {
						posTerminals = delegator.findList("PosTerminal", null, null, null, null, false);
					}
					map.put("posTerminals", posTerminals);
				}
				map.put("productStoreList", productStoreList);
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
	 
	 
	 
	 
	 
	 
	 public static Map<String, Object> autoCheckByUser(Delegator delegator,GenericValue userLogin,String productStoreId) {
		 
		 
		 List<GenericValue> productStoreList = getProductStoreByUser(delegator,userLogin);
		 
		 
		 Map<String, Object> map = ServiceUtil.returnSuccess();
		 
		 
		 GenericValue rProductStore = null;
		 
		 
		 if(productStoreId != null){
				boolean productStoreCheck = false;
				for (GenericValue productStore : productStoreList) {
					String _productStoreId = productStore.getString("productStoreId");
					if(productStoreCheck){
						break;
					}
					if(_productStoreId.equals(productStoreId)){
						productStoreCheck = true;
						rProductStore = productStore;
					}
				}
				if(productStoreCheck){
					map.put("productStoreId", productStoreId);
					map.put("facilityId", rProductStore.getString("inventoryFacilityId"));
					map.put("currencyUom", rProductStore.getString("defaultCurrencyUomId"));
				}else{
					Map<String, Object> productStoreListError = ServiceUtil.returnError("productStoreCheck false");
					
					Collections.sort(productStoreList, new Comparator<GenericValue>(){  
				            public int compare(GenericValue o1, GenericValue o2) {  
				        	String storeName1 = o1.getString("storeName");
				        	String storeName2 = o2.getString("storeName");
				        	if(storeName1 == null){
				        	    return 1;  
				        	}
				        	if(storeName2 == null){
				        	    return -1;  
				        	}
				        	char storeName1Char = storeName1.toCharArray()[0];
				        	char storeName2Char = storeName2.toCharArray()[0];
                                                if(storeName1Char > storeName2Char ){
                                                    return 1;  
                                                }
                                                if(storeName1Char == storeName2Char ){
                                                    return 0;  
                                                }
				                return -1;  
				            }  
				        });   
					
					productStoreListError.put("productStoreList", productStoreList);
					return productStoreListError;
				}
		}
		
		
		 
		 
		if (productStoreList.size() == 1) {
			rProductStore = EntityUtil.getFirst(productStoreList);
			String facilityId = rProductStore.getString("inventoryFacilityId");
			if (facilityId != null) {
				map.put("facilityId", facilityId);
				map.put("currencyUom", rProductStore.getString("defaultCurrencyUomId"));
			}else{
				return ServiceUtil.returnError("productStore no facilityId");
			}
		}
		
//		GenericValue rPosTerminal = null;
//		if(posTerminalId != null){
//			rPosTerminal = findTerminalByTerminalId(delegator, posTerminalId);
//			if(rPosTerminal != null){
//				String facilityId = rPosTerminal.getString("facilityId");
//				map.put("facilityId", facilityId);
//				map.put("currencyUom", rProductStore.getString("defaultCurrencyUomId"));
//				 for (GenericValue productStore : productStoreList) {
//					  String _facilityId = rProductStore.getString("inventoryFacilityId");
//					  if(_facilityId.equals(facilityId)){
//						  map.put("posTerminalId", posTerminalId);
//						  break;
//					  }
//				}
//			}
//		}
		
//		if(rProductStore != null){
//			List<GenericValue> posTerminalList = findTerminalByProductStoreId(delegator, rProductStore.getString("productStoreId"));
//			if(posTerminalList != null && posTerminalList.size() ==1){
//				GenericValue posTerminal = EntityUtil.getFirst(posTerminalList);
//				map.put("posTerminal", posTerminal.getString("posTerminalId"));
//			}
//		} 
			
	
		 map.put("productStoreList", productStoreList);
		 return map;
	 }
	 
	 
}