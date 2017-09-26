package com.yiwill.pos;

import java.util.Locale;
import java.util.Map;

import org.apache.tools.ant.types.resources.selectors.Date;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.yiwill.pos.shop.YiWillCart;

public class YiWillCartContext {

	public static String module = YiWillCartContext.class.getName();
	
	private GenericValue userLogin;
	private String orderId;
	private Delegator delegator;
	private LocalDispatcher dispatcher;
	private String productStoreId;
	private String productStoreGroupId;
	private String webSiteId;
	private Locale locale;
	private String currencyUom;
	private String billToCustomerPartyId;
	private String supplierPartyId;
	private YiWillCart cart;
	
	private String orderTypeId = null;
	private GenericValue productStore;
	private GenericValue orderType = null;
	private String facilityId = null;
	private String posTerminalId = null;
	
	
	public GenericValue getProductStore() {
		return productStore;
	}

	public YiWillCart getCart() {
		return cart;
	}


	public YiWillCartContext(Delegator delegator,LocalDispatcher dispatcher,
			GenericValue userLogin,
			String productStoreId,String productStoreGroupId,
			String webSiteId,Locale locale,String currencyUom,String posTerminalId,String facilityId) {
		
		this.delegator = delegator;
		this.dispatcher = dispatcher;
		this.userLogin = userLogin;
		
		this.productStoreId = productStoreId;
		this.productStoreGroupId = productStoreGroupId;
		this.webSiteId = webSiteId;
		this.currencyUom = currencyUom;
		this.locale = locale;
		try {
			
			if( this.orderTypeId != null){
				this.orderType = delegator.findByPrimaryKeyCache("OrderType", UtilMisc.toMap("orderTypeId", this.orderTypeId));
			}
		
			if(this.posTerminalId != null && this.facilityId == null){
				GenericValue posTerminal = delegator.findByPrimaryKeyCache("PosTerminal", UtilMisc.toMap("posTerminalId", this.posTerminalId));
				this.facilityId = posTerminal.getString("facilityId");
			}
			this.productStore = delegator.findByPrimaryKeyCache("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
			
			if(productStore.getString("defaultLocaleString") != null){
				this.locale =new Locale(productStore.getString("defaultLocaleString"));
			}
			
			if(productStore.getString("defaultCurrencyUomId") != null){
				this.currencyUom = productStore.getString("defaultCurrencyUomId");
			}
			
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	 public YiWillCart getCartObject(String orderId,GenericValue userLogin) throws Exception {
	    	if(orderId == null || orderId.length() ==0){
	    		 cart = new YiWillCart(delegator, productStoreId,productStoreGroupId,webSiteId,locale, currencyUom,null,null);
	    		 cart.addItemGroup("default", null);
//	    				   cart.setOrderId(getNextOrderId());
	    	}else{
	    		Map<String, Object> outMap;
				try {
					outMap = dispatcher.runSync("loadYiWillCartForUpdate",UtilMisc.<String, Object>toMap("orderId", orderId,"userLogin", userLogin));
					
					if(ServiceUtil.isError(outMap)){
						 throw new Exception(ServiceUtil.getErrorMessage(outMap));
					}
					
					cart =(YiWillCart) outMap.get("yiWillCart");
				} catch (GenericServiceException e) {
					e.printStackTrace();
				}
	    	}
			GenericValue salesUser = cart.getSalesUser();
			if(salesUser == null){
				cart.setSalesUser(userLogin);
			}
			return cart;
	 }
	 

}