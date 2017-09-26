//package org.ofbiz.webpos.shopping;
//
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.ofbiz.base.util.UtilMisc;
//import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.entity.Delegator;
//import org.ofbiz.entity.GenericEntityException;
//import org.ofbiz.entity.GenericValue;
//import org.ofbiz.order.shoppingcart.ShoppingCart.CartShipInfo;
//import org.ofbiz.service.LocalDispatcher;
//import org.ofbiz.webpos.shopping.bean.OrderAdjustment;
//import org.ofbiz.webpos.shopping.bean.OrderAdjustmentHelper;
//import org.ofbiz.webpos.shopping.bean.OrderHeader;
//import org.ofbiz.webpos.shopping.bean.OrderHeaderHelper;
//import org.ofbiz.webpos.shopping.bean.OrderItem;
//import org.ofbiz.webpos.shopping.bean.OrderItemHelper;
//
//import javolution.util.FastList;
//import javolution.util.FastMap;
//
//public class ShopCart {
//	
//	
//	private List<OrderItem> orderItemList = FastList.newInstance();
//	private List<OrderAdjustment> orderAdjustmentList = FastList.newInstance();
//	private List<CartShipInfo> shipInfo = FastList.<CartShipInfo> newInstance();
//	
//	private OrderHeader orderHeader = new OrderHeader(); 
//	private OrderHeaderHelper orderHeaderHelper = new OrderHeaderHelper(orderHeader); 
//	private OrderItemHelper orderItemHelper = new OrderItemHelper(orderItemList); 
//	private OrderAdjustmentHelper orderAdjustmentHelper = new OrderAdjustmentHelper(orderAdjustmentList); 
//	
//	private String orderId;
//	private Delegator delegator;
//	private ShopStore shopStore;
//	private GenericValue userLogin;
//	private LocalDispatcher dispatcher;
//	
//	
//	public OrderHeaderHelper getOrderHeaderHelper() {
//		return orderHeaderHelper;
//	}
//
//
//	public void setOrderHeaderHelper(OrderHeaderHelper orderHeaderHelper) {
//		this.orderHeaderHelper = orderHeaderHelper;
//	}
//
//
//	public OrderItemHelper getOrderItemHelper() {
//		return orderItemHelper;
//	}
//
//
//	public void setOrderItemHelper(OrderItemHelper orderItemHelper) {
//		this.orderItemHelper = orderItemHelper;
//	}
//
//
//	public OrderAdjustmentHelper getOrderAdjustmentHelper() {
//		return orderAdjustmentHelper;
//	}
//
//
//	public void setOrderAdjustmentHelper(OrderAdjustmentHelper orderAdjustmentHelper) {
//		this.orderAdjustmentHelper = orderAdjustmentHelper;
//	}
//
//
//	public String getOrderId() {
//		return orderId;
//	}
//
//
//	public void setOrderId(String orderId) {
//		this.orderId = orderId;
//	}
//
//
//	public ShopStore getShopStore() {
//		return shopStore;
//	}
//
//
//	public void setShopStore(ShopStore shopStore) {
//		this.shopStore = shopStore;
//	}
//
//
//	public GenericValue getUserLogin() {
//		return userLogin;
//	}
//
//
//	public void setUserLogin(GenericValue userLogin) {
//		this.userLogin = userLogin;
//	}
//
//
//	public ShopCart(ShopStore shopStore) {
//		this.shopStore = shopStore;
//		this.userLogin = shopStore.getUserLogin();
//		this.delegator = shopStore.getDelegator();
//		this.dispatcher = shopStore.getDispatcher();
//		this.orderId = shopStore.getOrderId();
//		
//		if(orderId == null || orderId.length() == 0){
//			createOrder();
//		}else{
//			try {
//				GenericValue orderHeaderG = delegator.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
//				orderHeaderHelper.parseGenericValue(orderHeaderG);
//
//				List<GenericValue> orderItemG = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId));
//				orderItemHelper.parseListGenericValue(orderItemG);
//				
//			} catch (GenericEntityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//			
//			
//			
//		}
//		
//	}
//	
//	
//private String getNextOrderId(){
//			try {
//				GenericValue productStore = shopStore.getProductStore();
//				String orgPartyId = shopStore.getProductStore().getString("payToPartyId");
//				 GenericValue partyAcctgPreference;
//			
//					partyAcctgPreference = delegator.findByPrimaryKeyCache("PartyAcctgPreference", UtilMisc.toMap("partyId", orgPartyId));
//				
//				 
//					 String orderSequenceEnumId = partyAcctgPreference.getString("orderSequenceEnumId");
//					 
//					 
//					 Long orderId = 0l;
//					 
//					 if(orderSequenceEnumId.equals("ODRSQ_ENF_SEQ")){
//						 String lastOrderNumber = partyAcctgPreference.getString("lastOrderNumber");
//						 
//						 if(lastOrderNumber != null && lastOrderNumber.length() >0){
//							 Long lastOrderNumberLong = Long.valueOf(lastOrderNumber);
//							 
//							 orderId = lastOrderNumberLong++;
//						 }else{
//							 orderId = 1l;
//							 partyAcctgPreference.set("lastOrderNumber", orderId);
//						 }
//						 
//						 partyAcctgPreference.store();
//			
//				 }else{
//					 orderId = delegator.getNextSeqIdLong("OrderHeader");
//				 }
//					 
//				 String orderNumberPrefix = productStore.getString("orderNumberPrefix");
//				 String orderIdPrefix = partyAcctgPreference.getString("orderIdPrefix");
//				 return orderNumberPrefix+orderIdPrefix+orderId;
//			 } catch (GenericEntityException e) {
//				
//			}
//		 return delegator.getNextSeqId("OrderHeader");
//	}
//	
//
//
//
//	public Map<String, Object> createOrder(){
//		Map<String, Object> map = FastMap.newInstance();
//		
//		GenericValue orderType = shopStore.getOrderType();
//		
//		GenericValue productStore = null;
//		if(orderType != null && orderType.getString("orderTypeId").equals("SALES_ORDER")){
//			 productStore = shopStore.getProductStore();
//		}
//		 boolean isImmediatelyFulfilled = false;
//		 if (productStore != null) {
//	            isImmediatelyFulfilled = "Y".equals(productStore.getString("isImmediatelyFulfilled"));
//	        }
//		 
//		 if(UtilValidate.isEmpty(orderId)){
//			 orderId = getNextOrderId();
//		 }
//		 
//		 List<GenericValue> toBeStored = new LinkedList<GenericValue>();
//		 
//		 Date orderDate =  new Date();
//		 
//		 String orderTypeId = shopStore.getOrderTypeId();
//		 String initialStatus = "ORDER_CREATED";
//		 
//		 Map<String, String> orderHeaderMap = UtilMisc.toMap("orderId", orderId, "orderTypeId",orderTypeId,
//	                "orderDate", orderDate, "entryDate", orderDate,
//	                "statusId", initialStatus, "billingAccountId", null);
//         orderHeaderMap.put("orderName", "testName");
//         if (isImmediatelyFulfilled) {
//            orderHeaderMap.put("needsInventoryIssuance", "Y");
//         }
//         GenericValue orderHeaderG = delegator.makeValue("OrderHeader", orderHeaderMap);
//		 
////         String salesChannelEnumId = (String) context.get("salesChannelEnumId");
////         if ((salesChannelEnumId == null) || salesChannelEnumId.equals("UNKNWN_SALES_CHANNEL")) {
////             // try the default store sales channel
////             if (orderTypeId.equals("SALES_ORDER") && (productStore != null)) {
//         String salesChannelEnumId = productStore.getString("defaultSalesChannelEnumId");
////             }
////             // if there's still no channel, set to unknown channel
////             if (salesChannelEnumId == null) {
////                 salesChannelEnumId = "UNKNWN_SALES_CHANNEL";
////             }
////         }		
//         orderHeaderG.set("salesChannelEnumId", salesChannelEnumId);
//         try {
//			delegator.create(orderHeaderG);
//			orderHeaderHelper.parseGenericValue(orderHeaderG);
//		} catch (GenericEntityException e) {
//			e.printStackTrace();
//		}
//         
//         String orderStatusSeqId = delegator.getNextSeqId("OrderStatus");
//         GenericValue orderStatus = delegator.makeValue("OrderStatus", UtilMisc.toMap("orderStatusId", orderStatusSeqId));
//         orderStatus.set("orderId", orderId);
//         orderStatus.set("statusId", initialStatus);
//         orderStatus.set("statusDatetime", orderDate);
//         orderStatus.set("statusUserLogin", userLogin.getString("userLoginId"));
//         toBeStored.add(orderStatus);
//         try {
//			delegator.storeAll(toBeStored);
//		} catch (GenericEntityException e) {
//			e.printStackTrace();
//		}
//       
//         map.put("orderId", orderId);
//         map.put("success", true);
//        return map;
//    
//	}
//	
//	 
//	 
//}
