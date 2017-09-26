//package org.ofbiz.webpos;
//
//import java.util.Locale;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.ofbiz.base.util.Debug;
//import org.ofbiz.base.util.UtilHttp;
//import org.ofbiz.order.shoppingcart.CartItemModifyException;
//import org.ofbiz.order.shoppingcart.ShoppingCart;
//import org.ofbiz.order.shoppingcart.WebShoppingCart;
//import org.ofbiz.service.LocalDispatcher;
//
//public class ShopEventsUtil {
//
//	public static String module = ShopEventsUtil.class.getName();
//	
//	 public static ShoppingCart getCartObject(HttpServletRequest request) {
//		 	String iso = UtilHttp.getCurrencyUom(request);
//	        return getCartObject(request, null, iso);
//	    }
//	 
//	 
//	  public static ShoppingCart getCartObject(HttpServletRequest request, Locale locale, String currencyUom) {
//	        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//	        ShoppingCart cart = (ShoppingCart) request.getAttribute("shoppingCart");
//	        HttpSession session = request.getSession(true);
//	        if (cart == null) {
//	            cart = (ShoppingCart) session.getAttribute("shoppingCart");
//	        } else {
//	            session.setAttribute("shoppingCart", cart);
//	        }
//
//	        if (cart == null) {
//	            cart = new WebShoppingCart(request, locale, currencyUom);
//	            session.setAttribute("shoppingCart", cart);
//	        } else {
//	            if (locale != null && !locale.equals(cart.getLocale())) {
//	                cart.setLocale(locale);
//	            }
//	            if (currencyUom != null && !currencyUom.equals(cart.getCurrency())) {
//	                try {
//	                    cart.setCurrency(dispatcher, currencyUom);
//	                } catch (CartItemModifyException e) {
//	                    Debug.logError(e, "Unable to modify currency in cart", module);
//	                }
//	            }
//	        }
//	        return cart;
//	    }
//
//}