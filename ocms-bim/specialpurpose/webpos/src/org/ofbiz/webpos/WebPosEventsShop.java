package org.ofbiz.webpos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.order.shoppingcart.CartItemModifyException;
import org.ofbiz.order.shoppingcart.ItemNotFoundException;
import org.ofbiz.order.shoppingcart.ShoppingCart;
import org.ofbiz.order.shoppingcart.ShoppingCartEvents;
import org.ofbiz.order.shoppingcart.ShoppingCartHelper;
import org.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.ofbiz.product.catalog.CatalogWorker;
import org.ofbiz.product.product.ProductWorker;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.webpos.session.WebPosSession;
import org.ofbiz.webpos.transaction.WebPosTransaction;

import javolution.util.FastList;


public class WebPosEventsShop {

    public static String module = WebPosEventsShop.class.getName();

//    public static Map<String,Object> addToCart(HttpServletRequest request, HttpServletResponse response) {
//		HttpSession session = request.getSession(true);
//    	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//    	Delegator delegator = (Delegator) request.getAttribute("delegator");
//    	Map<String,Object> map =  new HashMap<String,Object>();
//    	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//    	ShoppingCartEvents.addToCart(request, response);
//    	 ShoppingCart shoppingCart = ShoppingCartEvents.getCartObject(request);
//    	 
////     	shoppingCart.setUserLogin(null);
////     	shoppingCart.setAutoUserLogin(null);
//     	
////    	 shoppingCart.addOrIncreaseItem(productId, selectedAmount, quantity, reservStart, reservLength, reservPersons, shipBeforeDate, shipAfterDate, features, attributes, prodCatalogId, configWrapper, itemType, itemGroupNumber, parentProductId, dispatcher)
//    	 try {
//    		
//			shoppingCart.addOrIncreaseItem("GZ-1005", null, BigDecimal.ONE, null, null, null, null, null, null, null,null, null, null, null, null,dispatcher);
//			
//		} catch (CartItemModifyException e) {
//			e.printStackTrace();
//		} catch (ItemNotFoundException e) {
//			e.printStackTrace();
//		}
//    	 
//    	 
//    	 List<ShoppingCartItem> cartList =  shoppingCart.items();
//    	 map.put("shoppingCart", shoppingCart);
//    	 map.put("cartList", cartList);
//    	 
//    	return map;
//    }
    
    public static Map<String,Object> payCash(HttpServletRequest request, HttpServletResponse response) throws GeneralException {
    	HttpSession session = request.getSession(true);
    	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
    	Map<String,Object> map =  new HashMap<String,Object>();
    	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
    	
    	ShoppingCart shoppingCart = ShoppingCartEvents.getCartObject(request);
    	
    	WebPosSession webPosSession = WebPosEvents.getWebPosSession(request, null);
    	
    	WebPosTransaction webPosTransaction = webPosSession.getCurrentTransaction();
    	
    	String amount = request.getParameter("amount");
    	Boolean allowNegative = false;
    	Boolean allowPositive = true;
    	Integer minDecimal = 0;
    	Integer maxDecimal = 3;
    	
    	if(UtilValidate.isDouble(amount, allowNegative, allowPositive, minDecimal, maxDecimal)){
    		String paymentType = "CASH";
    		String refNum = "";
    		String authCode = "";
    		webPosTransaction.clearPayments();
    		webPosTransaction.processAmount(amount);
    		webPosTransaction.addPayment(paymentType, new BigDecimal(amount), refNum, authCode);
		}
    	     
    	
    	map.put("shoppingCart", shoppingCart);
    	
    	return map;
    }
    
    
    public static Map<String,Object> checkSSSSS(HttpServletRequest request, HttpServletResponse response) throws GeneralException {
    	HttpSession session = request.getSession(true);
    	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
    	Map<String,Object> map =  new HashMap<String,Object>();
    	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
    	WebPosSession webPosSession = WebPosEvents.getWebPosSession(request, null);
    	WebPosTransaction webPosTransaction = webPosSession.getCurrentTransaction();
    	
    	ShoppingCart shoppingCart = ShoppingCartEvents.getCartObject(request);
    	
    	map.put("shoppingCart", shoppingCart);
    	
    	return map;
    }
    
    
    
    
    public static Map<String,Object> changeLoginUser(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
    	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
    	Map<String,Object> map =  new HashMap<String,Object>();
    	
//    	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
    	
    	String userId = request.getParameter("userId");
    	ShoppingCart shoppingCart = ShoppingCartEvents.getCartObject(request);
    	
	  try {
		GenericValue userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", userId));
     	
		shoppingCart.setUserLogin(userLogin,dispatcher);
     	shoppingCart.setAutoUserLogin(userLogin,dispatcher);
     	shoppingCart.handleNewUser(dispatcher);
     	
	  } catch (GenericEntityException e) {
			e.printStackTrace();
	  } catch (CartItemModifyException e) {
			e.printStackTrace();
	  }
    	map.put("shoppingCart", shoppingCart);
    	return map;
    }
    
    
    
    public static Map<String,Object> changeStore(HttpServletRequest request, HttpServletResponse response) {
  		
    	Map<String,Object> map =  new HashMap<String,Object>();
    	
//    		HttpSession session = request.getSession(true);
//      	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//      	Delegator delegator = (Delegator) request.getAttribute("delegator");
//      	String productStoreId = request.getParameter("productStoreId");
//      	ShoppingCart shoppingCart = ShoppingCartEvents.getCartObject(request);
//      	
//      	shoppingCart.clear();
//      	shoppingCart.setProductStoreId(productStoreId);
//      	
//      	map.put("shoppingCart", shoppingCart);
      	return map;
      }
    
    
    
    
    public static Map<String, Object> addToCart(HttpServletRequest request, HttpServletResponse response) {

    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	
    Delegator delegator = (Delegator) request.getAttribute("delegator");
    LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
    
    ShoppingCart cart = ShoppingCartEvents.getCartObject(request);
    ShoppingCartHelper cartHelper = new ShoppingCartHelper(delegator, dispatcher, cart);
    
    String parentProductId = null;
    String itemType = null;
    String itemDescription = null;
    String productCategoryId = null;
    BigDecimal price = null;
  
    BigDecimal quantity = BigDecimal.ZERO;

    String catalogId = CatalogWorker.getCurrentCatalogId(request);
    Locale locale = UtilHttp.getLocale(request);

    Map<String, Object> paramMap = UtilHttp.getCombinedMap(request);

    String itemGroupNumber = (String) paramMap.get("itemGroupNumber");
    
    String productId = (String) paramMap.get("add_product_id");
    
    if (UtilValidate.isEmpty(productId)) {
    	map.put("success", false);
    	map.put("errMsg", "empty productId");
    	return map;
    }else{
    	 try {
             String pId = ProductWorker.findProductId(delegator, productId);
             if (pId != null) {
                 productId = pId;
             }
         } catch (Throwable e) {
        	map.put("success", false);
         	map.put("errMsg", "err productId");
         	return map;
         }
    	
    }
    

    //Check for virtual products
    if (ProductWorker.isVirtual(delegator, productId)) {
    	map.put("success", false);
     	map.put("errMsg", "no Virtual product");
     	return map;
    }

    String priceStr = (String) paramMap.get("price");
    

    String quantityStr = null;
    
    if (paramMap.containsKey("QUANTITY")) {
        quantityStr = (String) paramMap.remove("QUANTITY");
    } else if (paramMap.containsKey("quantity")) {
        quantityStr = (String) paramMap.remove("quantity");
    }
    if (UtilValidate.isEmpty(quantityStr)) {
        quantityStr = "1";  
    }
    price = new BigDecimal(priceStr);
    quantity = new BigDecimal(quantityStr);


    String selectedAmountStr = null;
    if (paramMap.containsKey("ADD_AMOUNT")) {
        selectedAmountStr = (String) paramMap.remove("ADD_AMOUNT");
    } else if (paramMap.containsKey("add_amount")) {
        selectedAmountStr = (String) paramMap.remove("add_amount");
    }
    BigDecimal amount = null;
    if (UtilValidate.isNotEmpty(selectedAmountStr)) {
    	amount = new BigDecimal(selectedAmountStr);
    } else {
        amount = BigDecimal.ZERO;
    }

    GenericValue productStore = ProductStoreWorker.getProductStore(request);
    if (productStore != null) {
        String addToCartRemoveIncompat = productStore.getString("addToCartRemoveIncompat");
        String addToCartReplaceUpsell = productStore.getString("addToCartReplaceUpsell");
        try {
            if ("Y".equals(addToCartRemoveIncompat)) {
                List productAssocs = null;
                EntityCondition cond = EntityCondition.makeCondition(UtilMisc.toList(
                        EntityCondition.makeCondition(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId), EntityOperator.OR, EntityCondition.makeCondition("productIdTo", EntityOperator.EQUALS, productId)),
                        EntityCondition.makeCondition("productAssocTypeId", EntityOperator.EQUALS, "PRODUCT_INCOMPATABLE")), EntityOperator.AND);
                productAssocs = delegator.findList("ProductAssoc", cond, null, null, null, false);
                productAssocs = EntityUtil.filterByDate(productAssocs);
                List productList = FastList.newInstance();
                Iterator iter = productAssocs.iterator();
                while (iter.hasNext()) {
                    GenericValue productAssoc = (GenericValue) iter.next();
                    if (productId.equals(productAssoc.getString("productId"))) {
                        productList.add(productAssoc.getString("productIdTo"));
                        continue;
                    }
                    if (productId.equals(productAssoc.getString("productIdTo"))) {
                        productList.add(productAssoc.getString("productId"));
                        continue;
                    }
                }
                Iterator sciIter = cart.iterator();
                while (sciIter.hasNext()) {
                    ShoppingCartItem sci = (ShoppingCartItem) sciIter.next();
                    if (productList.contains(sci.getProductId())) {
                        try {
                            cart.removeCartItem(sci, dispatcher);
                        } catch (CartItemModifyException e) {
                            Debug.logError(e.getMessage(), module);
                        }
                    }
                }
            }
            if ("Y".equals(addToCartReplaceUpsell)) {
                List productList = null;
                EntityCondition cond = EntityCondition.makeCondition(UtilMisc.toList(
                        EntityCondition.makeCondition("productIdTo", EntityOperator.EQUALS, productId),
                        EntityCondition.makeCondition("productAssocTypeId", EntityOperator.EQUALS, "PRODUCT_UPGRADE")), EntityOperator.AND);
                productList = delegator.findList("ProductAssoc", cond, UtilMisc.toSet("productId"), null, null, false);
                if (productList != null) {
                    Iterator sciIter = cart.iterator();
                    while (sciIter.hasNext()) {
                        ShoppingCartItem sci = (ShoppingCartItem) sciIter.next();
                        if (productList.contains(sci.getProductId())) {
                            try {
                                cart.removeCartItem(sci, dispatcher);
                            } catch (CartItemModifyException e) {
                                Debug.logError(e.getMessage(), module);
                            }
                        }
                    }
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), module);
        }
    }

     cartHelper.addToCart(catalogId, null, null, productId, productCategoryId,
            itemType, itemDescription, price, amount, quantity, null, null, null,
            null, null,
            null, null, null, itemGroupNumber, paramMap, parentProductId);
    
     List<ShoppingCartItem> cartList =  cart.items();
	 map.put("shoppingCart", cart);
	 map.put("cartList", cartList);
    
    return map;
    
}
    
    
    
    
    
    
    

   
}