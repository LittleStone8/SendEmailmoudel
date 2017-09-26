//package org.ofbiz.webpos.shopping;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.ofbiz.base.util.Debug;
//import org.ofbiz.base.util.GeneralException;
//import org.ofbiz.base.util.UtilFormatOut;
//import org.ofbiz.base.util.UtilMisc;
//import org.ofbiz.base.util.UtilProperties;
//import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.entity.Delegator;
//import org.ofbiz.entity.GenericEntityException;
//import org.ofbiz.entity.GenericValue;
//import org.ofbiz.order.shoppingcart.CartItemModifyException;
//import org.ofbiz.order.shoppingcart.ShoppingCartItem;
//import org.ofbiz.order.shoppingcart.product.ProductPromoWorker;
//import org.ofbiz.order.shoppinglist.ShoppingListEvents;
//import org.ofbiz.product.store.ProductStoreWorker;
//import org.ofbiz.webpos.shopping.bean.OrderHeader;
//import org.ofbiz.webpos.shopping.bean.OrderItem;
//import org.ofbiz.webpos.shopping.bean.OrderItemHelper;
//
//import javolution.util.FastMap;
//
//public class ShopTransaction {
//
//	
//	private ShopStore shopStore;
//	private Delegator delegator;
//	private ShopCart cart;
//	private GenericValue userLogin;
//	
//	public ShopTransaction(ShopStore shopStore) {
//		this.shopStore = shopStore;
//		this.delegator = shopStore.getDelegator();
//		this.cart = shopStore.getShopCart();
//		this.userLogin = shopStore.getUserLogin();
//	}
//	
//	
//	public Map<String, Object> addItem(BigDecimal quantity,String productId) throws GenericEntityException {
//		Map<String, Object> map = FastMap.newInstance();
//       
//		  GenericValue product = null;
//	        if (productId != null) {
//	            try {
//	                product = delegator.findByPrimaryKeyCache("Product", UtilMisc.toMap("productId", productId));
//	            } catch (GenericEntityException e) {
//	            	  map.put("success", false);
//	                  return map;
//	            }
//	        }
//		
//	        List<OrderItem> orderItemList = cart.getOrderItemHelper().getOrderItemList();
//	        for (OrderItem orderItem : orderItemList) {
//	        	if(productId.equals(orderItem.getProductId())){
//	        		
//	        		cart.getOrderItemHelper().setQuantity(orderItem,quantity);
//	        		
//	        		
//	        		map.put("orderItem", orderItem);
//	        		map.put("success", true);
//        	        return map;
//	        	}
//			}
//	        
//	        
//	        
//	        
//	        OrderItem orderItem = new OrderItem();
//	        
//	        orderItem.setProductId(product.getString("productId"));
//	        orderItem.setQuantity(quantity);
//	        
//	        
////	        ShoppingCartItem.makeItem(Integer.valueOf(0), productId, selectedAmount, quantity, null,
////                    reservStart, reservLength, reservPersons, accommodationMapId, accommodationSpotId, shipBeforeDate, shipAfterDate,
////                    features, attributes, prodCatalogId, configWrapper, itemType, itemGroup, dispatcher,
////                    this, Boolean.TRUE, Boolean.TRUE, parentProductId, Boolean.FALSE, Boolean.FALSE)
//	        
//		
//        map.put("success", true);
//        return map;
//    
//	}
//	
//	
//	
//	
//	
//
//}
