//package org.ofbiz.webpos.shopping;
//
//import java.util.Locale;
//
//import org.ofbiz.base.util.UtilMisc;
//import org.ofbiz.base.util.UtilProperties;
//import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.entity.Delegator;
//import org.ofbiz.entity.GenericEntityException;
//import org.ofbiz.entity.GenericValue;
//import org.ofbiz.service.LocalDispatcher;
//import org.ofbiz.service.ServiceUtil;
//
//public class ShopStore {
//
//	
//	private String posTerminalId = null;
//	private GenericValue userLogin = null;
//	private Locale locale = null;
//	private String productStoreId = null;
//	private String facilityId = null;
//	private String currencyUomId = null;
//	private Delegator delegator = null;
//	private LocalDispatcher dispatcher = null;
//	private ShopTransaction shopTransaction = null;
//	private ShopCart shopCart = null;
//	private String orderTypeId = null;
//	private GenericValue productStore;
//	private GenericValue orderType = null;
//	private String productStoreGroupId;
//	private String orderId;
//	
//	
//	public String getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
//
//	public String getProductStoreGroupId() {
//		return productStoreGroupId;
//	}
//
//	public void setProductStoreGroupId(String productStoreGroupId) {
//		this.productStoreGroupId = productStoreGroupId;
//	}
//
//	public GenericValue getOrderType() {
//		return orderType;
//	}
//
//	public void setOrderType(GenericValue orderType) {
//		this.orderType = orderType;
//	}
//
//	public String getOrderTypeId() {
//		return orderTypeId;
//	}
//
//	public void setOrderTypeId(String orderTypeId) {
//		this.orderTypeId = orderTypeId;
//	}
//
//	public GenericValue getProductStore() {
//		return productStore;
//	}
//
//	public void setProductStore(GenericValue productStore) {
//		this.productStore = productStore;
//	}
//
//	public String getPosTerminalId() {
//		return posTerminalId;
//	}
//
//	public void setPosTerminalId(String posTerminalId) {
//		this.posTerminalId = posTerminalId;
//	}
//
//	public GenericValue getUserLogin() {
//		return userLogin;
//	}
//
//	public void setUserLogin(GenericValue userLogin) {
//		this.userLogin = userLogin;
//	}
//
//	public Locale getLocale() {
//		return locale;
//	}
//
//	public void setLocale(Locale locale) {
//		this.locale = locale;
//	}
//
//	public String getProductStoreId() {
//		return productStoreId;
//	}
//
//	public void setProductStoreId(String productStoreId) {
//		this.productStoreId = productStoreId;
//	}
//
//	public String getFacilityId() {
//		return facilityId;
//	}
//
//	public void setFacilityId(String facilityId) {
//		this.facilityId = facilityId;
//	}
//
//	public String getCurrencyUomId() {
//		return currencyUomId;
//	}
//
//	public void setCurrencyUomId(String currencyUomId) {
//		this.currencyUomId = currencyUomId;
//	}
//
//	public Delegator getDelegator() {
//		return delegator;
//	}
//
//	public void setDelegator(Delegator delegator) {
//		this.delegator = delegator;
//	}
//
//	public LocalDispatcher getDispatcher() {
//		return dispatcher;
//	}
//
//	public void setDispatcher(LocalDispatcher dispatcher) {
//		this.dispatcher = dispatcher;
//	}
//
//	public ShopTransaction getShopTransaction() {
//		return shopTransaction;
//	}
//
//	public void setShopTransaction(ShopTransaction shopTransaction) {
//		this.shopTransaction = shopTransaction;
//	}
//
//	public ShopCart getShopCart() {
//		return shopCart;
//	}
//
//	public void setShopCart(ShopCart shopCart) {
//		this.shopCart = shopCart;
//	}
//
//	public ShopStore(Delegator delegator,LocalDispatcher dispatcher,GenericValue userLogin,String posTerminalId,Locale locale,
//			String productStoreId,String productStoreGroupId,String facilityId,String orderTypeId,String currencyUomId) {
//		this.delegator = delegator;
//		this.dispatcher = dispatcher;
//		this.locale = locale;
//		this.userLogin = userLogin;
//		this.productStoreId = productStoreId;
//		this.productStoreGroupId = productStoreGroupId;
//		this.posTerminalId = posTerminalId;
//		this.facilityId = facilityId;
//		this.orderTypeId = orderTypeId;
//		this.currencyUomId = currencyUomId;
//		
//		this.shopCart = new ShopCart(this);
//		
//		this.init();
//	}
//	
//	public void init(){
//		try {
//			
//			if( this.orderTypeId != null){
//				this.orderType = delegator.findByPrimaryKeyCache("OrderType", UtilMisc.toMap("orderTypeId", this.orderTypeId));
//			}
//		
//			if(this.posTerminalId != null && this.facilityId == null){
//				GenericValue posTerminal = delegator.findByPrimaryKeyCache("PosTerminal", UtilMisc.toMap("posTerminalId", this.posTerminalId));
//				this.facilityId = posTerminal.getString("facilityId");
//			}
//			
//			 this.productStore = delegator.findByPrimaryKeyCache("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
//
//			
//			
//		} catch (GenericEntityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	
//	 public ShopTransaction getCurrentTransaction() {
//	        if (UtilValidate.isEmpty(shopTransaction)) {
//	        	shopTransaction = new ShopTransaction(this);
//	        }
//	        return shopTransaction;
//	    }
//	
//	
//}
