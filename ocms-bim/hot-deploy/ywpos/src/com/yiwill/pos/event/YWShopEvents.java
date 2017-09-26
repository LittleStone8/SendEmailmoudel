package com.yiwill.pos.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.catalog.CatalogWorker;
import org.ofbiz.product.config.ProductConfigWorker;
import org.ofbiz.product.config.ProductConfigWrapper;
import org.ofbiz.product.product.ProductWorker;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiwill.pos.YiWillCartContext;
import com.yiwill.pos.bean.IMEIProductVo;
import com.yiwill.pos.bean.Product;
import com.yiwill.pos.shop.YiWillCart;
import com.yiwill.pos.shop.YiWillCart.CartShipInfo;
import com.yiwill.pos.shop.YiWillCartHelper;
import com.yiwill.pos.shop.YiWillCartItem;
import com.yiwill.pos.shop.YiWillCheckOutHelper;
import com.yiwill.pos.util.HttpsUtils;
import com.yiwill.pos.util.PosWorker;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * Events used for processing checkout and orders.
 */
public class YWShopEvents {

	public static final String module = YWShopEvents.class.getName();

	public static final MathContext generalRounding = new MathContext(10);

	public static Map<String, Object> getCartObject(HttpServletRequest request) {

		//  ORDERMGR_CREATE
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission");
		}

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Locale locale = UtilHttp.getLocale(request);

		if (userLogin == null) {
			return ServiceUtil.returnError("user not login");
		}

		String orderId = request.getParameter("orderId");
		String productStoreId = request.getParameter("productStoreId");
//		String posTerminalId = request.getParameter("posTerminalId");
		String productStoreGroupId = request.getParameter("productStoreGroupId");
		String webSiteId = request.getParameter("webSiteId");

		String sellerId = request.getParameter("sellerId");
		String buyerId = request.getParameter("buyerId");
		
		String contactMechId = request.getParameter("contactMechId");
		
		
		try {
		    Map<String, Object> getSystemSettingsContext = new HashMap<String, Object>();
		    Map<String, Object> sysInfo = dispatcher.runSync("getSystemSettings", getSystemSettingsContext);
		    String proxyHost = (String) sysInfo.get("proxyHost");
		    
		    String firstName = request.getParameter("firstName");
			String contactNumber = request.getParameter("contactNumber");
			
			if((buyerId != null && buyerId.length() == 0 ) 
				&& ( firstName != null && firstName.length() > 0)
				&& (contactNumber != null &&  contactNumber.length() > 0)
				){
			    JSONObject  json = new JSONObject();
			    json.put("firstName", firstName);
			    json.put("contactNumber", contactNumber);
		 
			    String postString =  json.toJSONString();
			    
			    String cookie =  request.getHeader("Cookie");
			    Map<String, String> header = new HashMap<String, String>();
				   			   header.put("Cookie", cookie);
			    
			    String createStr =  HttpsUtils.post(proxyHost+"/api/security/v1/person/createOrUpdatePerson", header,postString);
			    System.out.println(createStr);
			    
			    if(createStr == null ){
				return ServiceUtil.returnError("createOrUpdatePerson error ");
			    }
			    JSONObject createJson = JSON.parseObject(createStr);
			    
			    Boolean success = createJson.getBoolean("success");
			    if(success != null && success){
				JSONObject createData = createJson.getJSONObject("data");
				buyerId = createData.getString("partyId");
			    }else{
				String message = createJson.getString("message");
				if(message == null ){
				    message = "createOrUpdatePerson error 01";
				}
				return ServiceUtil.returnError(message);
			    }
			    
			}
			
		} catch (GenericServiceException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		 
		
		
		
		
		
		
		String imei = request.getParameter("imei");
		
		String facilityId = null;
		String currencyUom = null;

		Map<String, Object> check = PosWorker.autoCheckByUser(delegator, userLogin, productStoreId);
		if (ServiceUtil.isError(check)) {
			return check;
		} else {
//			posTerminalId = (String) check.get("posTerminalId");
			productStoreId = (String) check.get("productStoreId");
			facilityId = (String) check.get("facilityId");
			currencyUom = (String) check.get("currencyUom");
			if (UtilValidate.isEmpty(productStoreId) || UtilValidate.isEmpty(facilityId)
					|| UtilValidate.isEmpty(currencyUom)) {
				// session.removeAttribute("userLogin");
				return ServiceUtil.returnError("check fail");
			}
		}

		if (productStoreGroupId == null || productStoreGroupId.length() == 0) {
			productStoreGroupId = "RETAIL";
		}

		try {

			YiWillCartContext context = new YiWillCartContext(delegator, dispatcher, userLogin, productStoreId,
					productStoreGroupId, webSiteId, locale, currencyUom, "", facilityId);

			YiWillCart cart = context.getCartObject(orderId, userLogin);

			GenericValue sellerUser = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", sellerId));
			if (sellerUser != null) {
				cart.setSalesUser(sellerUser);
				cart.setBillFromVendorPartyId(sellerId);
			}
			GenericValue buyerUser = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", buyerId));

			if (buyerUser != null) {
				cart.setUserLogin(buyerUser, dispatcher);
				cart.setBillToCustomerPartyId(buyerId);
				cart.setShippingContactMechId(contactMechId);
			}
			cart.setChannelType(productStoreGroupId);
			
			if(cart.getOrderStatusId() != null && !cart.getOrderStatusId().equals("ORDER_CREATED")){
			    cart.setReadOnlyCart(true);
			}
			
			Map<String, Object> map = ServiceUtil.returnSuccess();
			map.put("cart", cart);
			map.put("context", context);
			return map;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError(e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError(e.getMessage());
		}
//		return ServiceUtil.returnError("Error");
	}

	public static Map<String, Object> initWebpos(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = ServiceUtil.returnSuccess();
		HttpSession session = request.getSession();
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		Locale locale = UtilHttp.getLocale(request);
		if (userLogin == null) {
			return ServiceUtil.returnError("user not login");
		}

		String productStoreId = request.getParameter("productStoreId");
//		String posTerminalId = request.getParameter("posTerminalId");

		if (!UtilValidate.isEmpty(productStoreId) ) {

			Map<String, Object> cartMap = getCartObject(request);
			YiWillCart cart = null;
			YiWillCartContext context;

			if (ServiceUtil.isError(cartMap)) {
				return cartMap;
			} else {
				cart = (YiWillCart) cartMap.get("cart");
				context = (YiWillCartContext) cartMap.get("context");
			}

			if (cart == null) {
				ServiceUtil.returnError("cart empty");
			}

		} else {
			Map<String, List<GenericValue>> resMap = PosWorker.autoGetTerminalAndProductStoreByUser(delegator,
					userLogin);

			List<GenericValue> productStoreList = resMap.get("productStoreList");
//			List<GenericValue> posTerminals = resMap.get("posTerminals");
//			if (UtilValidate.isEmpty(productStoreList) || UtilValidate.isEmpty(posTerminals)) {
//				return ServiceUtil.returnError("posTerminals not mach");
//			}
//			if (posTerminals.size() != 1 || productStoreList.size() != 1) {
//				return ServiceUtil.returnError("posTerminals not mach.");
//			}
			productStoreId = productStoreList.get(0).getString("productStoreId");
//			posTerminalId = posTerminals.get(0).getString("posTerminalId");
		}

		map.put("productStoreId", productStoreId);
//		map.put("posTerminalId", posTerminalId);

		try {
			GenericValue productStore = delegator.findOne("ProductStore",
					UtilMisc.toMap("productStoreId", productStoreId), false);
//			GenericValue posTerminal = delegator.findOne("PosTerminal", UtilMisc.toMap("posTerminalId", posTerminalId),
//					false);
			map.put("productStore", productStore);
//			map.put("posTerminal", posTerminal);
//			GenericValue facility = delegator.findOne("Facility", true, UtilMisc.toMap("facilityId", posTerminal.getString("facilityId")));
//			String facilityName;
//			if(facility != null){
//				facilityName = facility.getString("facilityName");
//			}else{
//				facilityName = "";
//			}
//			map.put("facilityName", facilityName);
			List<GenericValue> personList = new ArrayList<GenericValue>();
			List<GenericValue> productStoreRolesList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("productStoreId", productStoreId , "roleTypeId" , "SALES_REP"));
			for (GenericValue productStoreRole : productStoreRolesList) {
				GenericValue person = productStoreRole.getRelatedOne("Person");
				if(person != null){
					personList.add(person);
				}
			}
			map.put("personList", personList);
			
			
			Map<String, Object> context = new HashMap<String, Object>();
			Map<String, Object> info = dispatcher.runSync("getSystemSettings", context);
			String countryId = (String) info.get("countryId");
			List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole",UtilMisc.toMap("productStoreId", productStoreId , "roleTypeId" , "MANAGER" , "partyId" , userLogin.getString("partyId")));
			String editPrice = "Y";
			if("GHA".equals(countryId) && productStoreRoleList.size() > 0){
					editPrice = "N";
			}
			map.put("editPrice", editPrice);
			
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		map.put("userLoginId", userLogin.get("userLoginId"));
		return map;
	}
	public static JSONObject makeParams(HttpServletRequest request){		
		HttpSession session = request.getSession();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		StringBuffer jsonStringBuffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}		
		String productPriceList =  jsonStringBuffer.toString();
		JSONObject json = JSON.parseObject(productPriceList);
		return json;
	}
	public static Map<String,Object> checkIMEIisAvalible(HttpServletRequest request,HttpServletResponse response){
    	Map<String, Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		JSONObject json = makeParams(request);
		String imeis = json.getString("imeis");
		String storeId = json.getString("storeId");
		String[] IMEIS = imeis.split(",");
		List<String> noAvailableImei = new ArrayList<String>();
		String productIMEI = "";
		ResultSet rs = null;
		try {
			GenericValue productStore = delegator.findByPrimaryKey("ProductStore", UtilMisc.toMap("productStoreId", storeId));
			String facilityId = productStore.getString("inventoryFacilityId");
			for(String imei : IMEIS){
					EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
						EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei),EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_AVAILABLE"),
						EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId));
						
					List<GenericValue> inventoryItemList = delegator.findByCondition("InventoryItem",
							conditions, null, null);
					if(inventoryItemList == null || inventoryItemList.size() == 0){
						noAvailableImei.add(imei);
					}else{
						productIMEI = imei;
					}
				
			}
			String sql = "SELECT Y.PRODUCT_ID AS productId,Y.BRAND_NAME as brandName,Y.INTERNAL_NAME AS model,Z.description FROM "
					+ " (SELECT * FROM INVENTORY_ITEM WHERE SERIAL_NUMBER='"+productIMEI+"') X LEFT JOIN PRODUCT Y ON X.PRODUCT_ID=Y.PRODUCT_ID LEFT JOIN ( SELECT b.PRODUCT_ID, GROUP_CONCAT( DISTINCT DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) AS description FROM "
					+ " PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) Z ON X.PRODUCT_ID = Z.PRODUCT_ID ";
			rs = processor.executeQuery(sql);
			while(rs.next()){
				IMEIProductVo pv = new IMEIProductVo();
				pv.setProductId(rs.getString("productId"));
				pv.setBrandName(rs.getString("brandName"));
				pv.setModel(rs.getString("model"));
				pv.setDescription(rs.getString("description"));
				result.put("product", pv);
			}
			result.put("result", noAvailableImei);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		} finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				processor.close();
			} catch (GenericDataSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
    }
	public static Map<String, Object> addToCart(HttpServletRequest request, HttpServletResponse response) {

		
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission");
		}

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			ServiceUtil.returnError("cart empty");
		}

		YiWillCartHelper cartHelper = new YiWillCartHelper(delegator, dispatcher, cart);
		Map result = null;
		String productId = null;

		String itemType = null;
		String itemDescription = null;

		String reservStartStr = null;
		String reservEndStr = null;
		java.sql.Timestamp reservStart = null;
		java.sql.Timestamp reservEnd = null;
		String reservLengthStr = null;
		BigDecimal reservLength = null;
		String reservPersonsStr = null;
		BigDecimal reservPersons = null;
		String accommodationMapId = null;
		String accommodationSpotId = null;
		String shipBeforeDateStr = null;
		String shipAfterDateStr = null;
		java.sql.Timestamp shipBeforeDate = null;
		java.sql.Timestamp shipAfterDate = null;

		// not used right now: Map attributes = null;
		String catalogId = CatalogWorker.getCurrentCatalogId(request);
		Locale locale = UtilHttp.getLocale(request);
		NumberFormat nf = NumberFormat.getNumberInstance(locale);

		// Get the parameters as a MAP, remove the productId and quantity
		// params.
		Map paramMap = UtilHttp.getCombinedMap(request);

		String itemGroupNumber = (String) paramMap.get("itemGroupNumber");

		// Get shoppingList info if passed
		String shoppingListId = (String) paramMap.get("shoppingListId");
		String shoppingListItemSeqId = (String) paramMap.get("shoppingListItemSeqId");
		if (paramMap.containsKey("ADD_PRODUCT_ID")) {
			productId = (String) paramMap.remove("ADD_PRODUCT_ID");
		} else if (paramMap.containsKey("add_product_id")) {
			Object object = paramMap.remove("add_product_id");
			try {
				productId = (String) object;
			} catch (ClassCastException e) {
				productId = (String) ((List) object).get(0);
			}
		}
		
		String imeiNum = request.getParameter("imei");
		if(imeiNum != null && !imeiNum.equals("") && !imeiNum.contains(",")){
			if(imeiNum != null && !"".equals(imeiNum)){
				List<GenericValue> inventoryItemList = null;
				try {
					String productStoreId = (String) paramMap.get("productStoreId");
					GenericValue productStore = delegator.findByPrimaryKey("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
					String facilityId = productStore.getString("inventoryFacilityId");
					inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId" , productId , "statusId" , "INV_AVAILABLE" , "serialNumber" , imeiNum,"facilityId",facilityId));
				} catch (GenericEntityException e2) {
					e2.printStackTrace();
				}
				if(!inventoryItemList.isEmpty()){
					if(inventoryItemList.size() > 1){
						Map<String, Object> resultMap = ServiceUtil.returnError("This IMEI code is unavailable, please try other ones.");
						return resultMap; 
					}
					for (GenericValue genericValue : inventoryItemList) {
						String availableToPromiseTotal = genericValue.getString("availableToPromiseTotal");
						if(new BigDecimal(availableToPromiseTotal).compareTo(new BigDecimal("0")) > 1){
							Map<String, Object> resultMap = ServiceUtil.returnError("This IMEI code is unavailable, please try other ones.");
							return resultMap;
						}else if(new BigDecimal(availableToPromiseTotal).compareTo(new BigDecimal("0")) == 0){
							return ServiceUtil.returnError("This IMEI is unavailable, please try other ones.");
						}
					}
				}else{
					return ServiceUtil.returnError("This IMEI code is unavailable, please try other ones.");
				}
			}
		}
		
		String parentProductId = null;
		if (paramMap.containsKey("PRODUCT_ID")) {
			parentProductId = (String) paramMap.remove("PRODUCT_ID");
		} else if (paramMap.containsKey("product_id")) {
			parentProductId = (String) paramMap.remove("product_id");
		}

		if (paramMap.containsKey("ADD_ITEM_TYPE")) {
			itemType = (String) paramMap.remove("ADD_ITEM_TYPE");
		} else if (paramMap.containsKey("add_item_type")) {
			itemType = (String) paramMap.remove("add_item_type");
		}

		if (UtilValidate.isEmpty(productId)) {
			// before returning error; check make sure we aren't adding a
			// special item type
			if (UtilValidate.isEmpty(itemType)) {
				return ServiceUtil.returnError("noProductInfoPassed");
			}
		} else {
			try {
				String pId = ProductWorker.findProductId(delegator, productId);
				if (pId != null) {
					productId = pId;
				}
			} catch (Throwable e) {
				Debug.logWarning(e, module);
			}
		}

		// check for an itemDescription
		if (paramMap.containsKey("ADD_ITEM_DESCRIPTION")) {
			itemDescription = (String) paramMap.remove("ADD_ITEM_DESCRIPTION");
		} else if (paramMap.containsKey("add_item_description")) {
			itemDescription = (String) paramMap.remove("add_item_description");
		}
		if (itemDescription != null && itemDescription.length() == 0) {
			itemDescription = null;
		}

		// Get the ProductConfigWrapper (it's not null only for configurable
		// items)
		ProductConfigWrapper configWrapper = null;
		configWrapper = ProductConfigWorker.getProductConfigWrapper(productId, cart.getCurrency(), request);

		if (configWrapper != null) {
			if (paramMap.containsKey("configId")) {
				try {
					configWrapper.loadConfig(delegator, (String) paramMap.remove("configId"));
				} catch (Exception e) {
					Debug.logWarning(e, "Could not load configuration", module);
				}
			} else {
				// The choices selected by the user are taken from request and
				// set in the wrapper
				ProductConfigWorker.fillProductConfigWrapper(configWrapper, request);
			}
			if (!configWrapper.isCompleted()) {
				return ServiceUtil.returnError("configureProductBeforeAddingToCart");
			} else {
				ProductConfigWorker.storeProductConfigWrapper(configWrapper, delegator);
			}
		}

		// get the selected amount
		String selectedAmountStr = null;
		if (paramMap.containsKey("ADD_AMOUNT")) {
			selectedAmountStr = (String) paramMap.remove("ADD_AMOUNT");
		} else if (paramMap.containsKey("add_amount")) {
			selectedAmountStr = (String) paramMap.remove("add_amount");
		}

		// parse the amount
		BigDecimal amount = null;
		if (UtilValidate.isNotEmpty(selectedAmountStr)) {
			try {
				amount = (BigDecimal) ObjectType.simpleTypeConvert(selectedAmountStr, "BigDecimal", null, locale);
			} catch (Exception e) {
				Debug.logWarning(e, "Problem parsing amount string: " + selectedAmountStr, module);
				amount = null;
			}
		} else {
			amount = BigDecimal.ZERO;
		}


		String productCategoryId = null;
		if (paramMap.containsKey("ADD_CATEGORY_ID")) {
			productCategoryId = (String) paramMap.remove("ADD_CATEGORY_ID");
		} else if (paramMap.containsKey("add_category_id")) {
			productCategoryId = (String) paramMap.remove("add_category_id");
		}
		if (productCategoryId != null && productCategoryId.length() == 0) {
			productCategoryId = null;
		}

		BigDecimal quantity = BigDecimal.ZERO;
		String quantityStr = null;
		if (paramMap.containsKey("QUANTITY")) {
			quantityStr = (String) paramMap.remove("QUANTITY");
		} else if (paramMap.containsKey("quantity")) {
			quantityStr = (String) paramMap.remove("quantity");
		}
		if (UtilValidate.isEmpty(quantityStr)) {
			quantityStr = "1"; // default quantity is 1
		}
		try {
			quantity = (BigDecimal) ObjectType.simpleTypeConvert(quantityStr, "BigDecimal", null, locale);
		} catch (Exception e) {
			Debug.logWarning(e, "Problems parsing quantity string: " + quantityStr, module);
			quantity = BigDecimal.ONE;
		}

		BigDecimal price = null;
		String priceStr = null;
		if (paramMap.containsKey("PRICE")) {
			priceStr = (String) paramMap.remove("PRICE");
		} else if (paramMap.containsKey("price")) {
			priceStr = (String) paramMap.remove("price");
		}
		if (priceStr == null) {
			priceStr = "0"; // default price is 0
		}
		try {
			price = (BigDecimal) ObjectType.simpleTypeConvert(priceStr, "BigDecimal", null, locale);
		} catch (Exception e) {
			Debug.logWarning(e, "Problems parsing price string: " + priceStr, module);
			price = null;
		}

		String imei = (String) paramMap.get("imei");
		
//		delegator.findByAnd("",  UtilMisc.toMap("productCategoryId", categoryId));
//		EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
//			EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, imei));
//		List<GenericValue> inventoryItemList = delegator.findByCondition("InventoryItem",conditions, null, null);
		
		String facilityId = context.getProductStore().getString("inventoryFacilityId");
		if(imei == null  ||  "".equals(imei)){
			GenericValue pro = check(delegator, productId, facilityId);		
			if(pro != null ){
			    productId =    pro.getString("productId");
			    imei =  pro.getString("serialNumber");
			}
		}
		
		 if(imei != null && !imei.equals("")){
	        	String[] imeis = imei.split(",");
	        	for(String IMEI : imeis){
	        		try {
	        			EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
							EntityCondition.makeCondition("serialNumber", EntityOperator.EQUALS, IMEI),EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_AVAILABLE"));
					
						List<GenericValue> inventoryItemList;
						
							inventoryItemList = delegator.findByCondition("InventoryItem",
									conditions, null, null);
						
						if(inventoryItemList == null || inventoryItemList.size() == 0){
							Debug.logWarning(imei+" is not available!", module);
							return ServiceUtil.returnError(IMEI+" is not available!");
						}
	        		} catch (GenericEntityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		result = cartHelper.addToCart(catalogId, shoppingListId, shoppingListItemSeqId, productId, productCategoryId,
	        				itemType, itemDescription, price, amount, quantity, reservStart, reservLength, reservPersons,
	        				accommodationMapId, accommodationSpotId, shipBeforeDate, shipAfterDate, configWrapper, itemGroupNumber,
	        				paramMap, parentProductId,IMEI);
	        	}
	        }else{
	        	result = cartHelper.addToCart(catalogId, shoppingListId, shoppingListItemSeqId, productId, productCategoryId,
	    				itemType, itemDescription, price, amount, quantity, reservStart, reservLength, reservPersons,
	    				accommodationMapId, accommodationSpotId, shipBeforeDate, shipAfterDate, configWrapper, itemGroupNumber,
	    				paramMap, parentProductId,imei);
	        }
		

		if (ServiceUtil.isError(result)) {
			return result;
		}
		// cart.setDefaultCheckoutOptions(dispatcher);

		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));

			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();

			YiWillCart outCart = (YiWillCart) outMap.get("cart");
			map.put("cart", outCart.makeCartMap(dispatcher, true));
			return map;
		} catch (GenericServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> modifyCart(HttpServletRequest request, HttpServletResponse response) {

		
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission");
		}

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			ServiceUtil.returnError("cart empty");
		}

		Security security = (Security) request.getAttribute("security");
		YiWillCartHelper cartHelper = new YiWillCartHelper(delegator, dispatcher, cart);

		Map paramMap = UtilHttp.getParameterMap(request);

		String type = (String) paramMap.get("type");
		String orderItemSeqId = (String) paramMap.get("orderItemSeqId");
		String value = (String) paramMap.get("value");

		Map results = null;
		if (type.trim().toUpperCase().equals("DEL")) {
			results = cartHelper.deleteItemByOrderItemSeqId(orderItemSeqId);
		}
		if (type.trim().toUpperCase().equals("DELALL")) {
			results = cartHelper.deleteAllItem();
		}
		if (type.trim().toUpperCase().equals("QUANTITY")) {
			results = cartHelper.changeItemQuantityByOrderItemSeqId(orderItemSeqId, value);
		}

		if (type.trim().toUpperCase().equals("PRICE")) {
			results = cartHelper.changeItemPriceByOrderItemSeqId(orderItemSeqId, value);
		}

		if (ServiceUtil.isError(results)) {
			return results;
		}

		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
					UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));

			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();
			YiWillCart outCart = (YiWillCart) outMap.get("cart");
			map.put("cart", outCart.makeCartMap(dispatcher, true));
			return map;
		} catch (GenericServiceException e1) {
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> payCash(HttpServletRequest request, HttpServletResponse response) {

		
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission");
		}

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			ServiceUtil.returnError("cart empty");
		}

		YiWillCartHelper cartHelper = new YiWillCartHelper(delegator, dispatcher, cart);

		Map paramMap = UtilHttp.getParameterMap(request);

		String price = (String) paramMap.get("price");
		// BigDecimal amount

		BigDecimal _price = new BigDecimal(price);
		
		String paymentMethod = (String) paramMap.get("paymentMethod");
		
		if("CASH".equals(paymentMethod)  || paymentMethod == null){
			cart.clearPayment("CASH");
			cart.addPaymentAmount("CASH", _price);
		}
		if("CREDIT".equals(paymentMethod)){
			//cart.clearPayment("CREDIT");
			cart.addPaymentAmount("CREDIT", _price);
		}
		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
					UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));

			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();
			YiWillCart outCart = (YiWillCart) outMap.get("cart");
			map.put("cart", outCart.makeCartMap(dispatcher, true));
			return map;
		} catch (GenericServiceException e1) {
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> payInvoicing(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException {

		
		String updateMode = "CREATE";
		boolean flag = hasEntityPermission(request, updateMode);
		if (!flag) {
			return ServiceUtil.returnError("no permission");
		}

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			ServiceUtil.returnError("cart empty");
		}

		
		
		if(cart.isReadOnlyCart()){
		    return ServiceUtil.returnError("You can't edit this cart. it changed!!!!");
		}
		
		GenericValue supplementalData = delegator.findByPrimaryKey("PartySupplementalData", UtilMisc.toMap("partyId", cart.getBillToCustomerPartyId()));
		if(supplementalData == null){
			return ServiceUtil.returnError("supplementalData not mach.");
		}
		String primaryPostalAddressId = supplementalData.getString("primaryPostalAddressId");
		String primaryTelecomNumberId = supplementalData.getString("primaryTelecomNumberId");
		
		
//		if(primaryPostalAddressId != null){
//			GenericValue postalAddress = delegator.findByPrimaryKey("PostalAddress", UtilMisc.toMap("contactMechId", primaryPostalAddressId));
//		}
		
		if(primaryPostalAddressId != null){
		    cart.addContactMech("SHIPPING_LOCATION",primaryPostalAddressId);
		}
		
		
		cart.setCarrierPartyId("EGATEE");
		
		YiWillCartHelper cartHelper = new YiWillCartHelper(delegator, dispatcher, cart);

		Map paramMap = UtilHttp.getParameterMap(request);

		String verify  =(String) paramMap.get("verify");
		
		String isShip = (String)paramMap.get("isShip");
		
		
		
	   	boolean verifyB = Boolean.parseBoolean(verify);
	   	
		BigDecimal grandTotal = cart.getGrandTotal();
		
		if(verifyB){
		    //cart.addPaymentAmount("CASH", grandTotal);
		    
		    String paymentMethod = (String) paramMap.get("paymentMethod");
			
			if("CASH".equals(paymentMethod) || paymentMethod == null){
				//cart.clearPayment("CASH");
				cart.addPaymentAmount("CASH", grandTotal);
			}
			if("CREDIT".equals(paymentMethod)){
				cart.addPaymentAmount("CREDIT", grandTotal);
			}
		}
		
		
		BigDecimal paymentTotal = cart.getPaymentTotal();
		
		//补录订单的日期	chenshihua	2017-8-9
		String fixOrder = request.getParameter("fixOrder");
		String fixDate = request.getParameter("fixDate");
		if("Y".equals(fixOrder)){
			if( Long.parseLong(fixDate) > (System.currentTimeMillis()-Long.parseLong("2678400000"))){
				java.sql.Timestamp fixTimestamp = UtilDateTime.getTimestamp(fixDate);
				System.out.println(fixTimestamp);
				cart.setOrderDate(fixTimestamp);
			}else{
				return ServiceUtil.returnError("please select right date");
			}
		}	
	   	if(paymentTotal.compareTo(grandTotal) < 0){
			return ServiceUtil.returnError("paymentTotal < grandTotal");
		}
	   	
	   	if(isShip.equals("Y")){
	   	    cart.setOrderStatusId("ORDER_PAYMENT");
        	    cart.setIsNeedShip(isShip.equals("Y"));
        	   cart.setShipmentMethodTypeId("STANDARD");
        	   cart.setDefaultShipAfterDate(new Timestamp(new Date().getTime()));
        	   cart.setDefaultShipBeforeDate(new Timestamp(new Date().getTime()));
        	   
        	   List<CartShipInfo> shipGroupList = cart.getShipGroups();
        	   
        	   if(shipGroupList != null){
        	       for (CartShipInfo cartShipInfo : shipGroupList) {
        		   cartShipInfo.setContactMechId(primaryPostalAddressId);
        		   cartShipInfo.setTelecomContactMechId(primaryTelecomNumberId);
        	       }
        	   }
        	  
	   	}else{
	   		String paymentMethod = (String) paramMap.get("paymentMethod");
	   		if("CASH".equals(paymentMethod) || paymentMethod == null){
	   			cart.setOrderStatusId("ORDER_COMPLETED");
	   		}
	   		if("CREDIT".equals(paymentMethod)){
	   			cart.setOrderStatusId("ORDER_COMPLETED_W_P");
	   		}
	   	}
	   	

		try {
		    
        		Map<String, Object>  outMap = dispatcher.runSync("yiWillReserveInventory",UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));
			if (ServiceUtil.isError(outMap)) {
				return outMap;
			}
			
			
			Map<String, Object> map = ServiceUtil.returnSuccess();
			YiWillCart outCart = (YiWillCart) outMap.get("cart");
			map.put("cart", cart.makeCartMap(dispatcher, true));
			map.put("paymentMethod",(String)paramMap.get("paymentMethod"));
			map.put("isNeedShip",(String)paramMap.get("isShip"));
			return map;
		} catch (GenericServiceException e1) {
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("yiWillReserveInventory Error");
	}

	public static Map<String, Object> changeProductStoreGroupId(HttpServletRequest request,
			HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			return ServiceUtil.returnError("cart empty");
		}

		Map paramMap = UtilHttp.getParameterMap(request);
		String productStoreGroupId = (String) paramMap.get("productStoreGroupId");

		cart.setProductStoreGroupId(productStoreGroupId);

		try {
			cart.reCountPrice(dispatcher);
		} catch (Exception e) {
			return ServiceUtil.returnError("change price error");
		}

		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
					UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));

			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();
			// map.put("cart", outMap.get("cart"));
			map.put("success", true);
			return map;
		} catch (GenericServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> changeUser(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			return ServiceUtil.returnError("cart empty");
		}

		Map paramMap = UtilHttp.getParameterMap(request);

		String partyId = (String) paramMap.get("partyId");

		try {

			List<GenericValue> UserLoginList = delegator.findByAnd("UserLogin", UtilMisc.toMap("userLoginId", partyId));
			if (UserLoginList != null && UserLoginList.size() > 0) {
				GenericValue cUserLogin = EntityUtil.getFirst(UserLoginList);
				cart.setUserLogin(cUserLogin, dispatcher);
			}

		} catch (Exception e) {
			return ServiceUtil.returnError("change price error");
		}

		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
					UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));
			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();
			map.put("cart", outMap.get("cart"));
			return map;
		} catch (GenericServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> changeSales(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			return ServiceUtil.returnError("cart empty");
		}

		Map paramMap = UtilHttp.getParameterMap(request);

		String salesId = (String) paramMap.get("salesId");

		try {

			List<GenericValue> salesUserList = delegator.findByAnd("UserLogin", UtilMisc.toMap("userLoginId", salesId));
			if (salesUserList != null && salesUserList.size() > 0) {
				GenericValue cSalesUser = EntityUtil.getFirst(salesUserList);
				cart.setSalesUser(cSalesUser);
			}

		} catch (Exception e) {
			return ServiceUtil.returnError("change price error");
		}

		try {
			Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
					UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));
			if (ServiceUtil.isError(outMap)) {
				return cartMap;
			}
			Map<String, Object> map = ServiceUtil.returnSuccess();
			map.put("cart", outMap.get("cart"));
			return map;
		} catch (GenericServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

	public static Map<String, Object> findOrderPro(HttpServletRequest request, HttpServletResponse response) {

		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		Map<String, Object> map = ServiceUtil.returnSuccess();

		// findOrderPro

		Map<String, Object> cartMap = getCartObject(request);
		YiWillCart cart = null;
		YiWillCartContext context;

		if (ServiceUtil.isError(cartMap)) {
			return cartMap;
		} else {
			cart = (YiWillCart) cartMap.get("cart");
			context = (YiWillCartContext) cartMap.get("context");
		}

		if (cart == null) {
			return ServiceUtil.returnError("cart empty");
		}

		String productStoreId = request.getParameter("productStoreId");

		try {
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			String sql = "select distinct  p.PRODUCT_ID , p.INTERNAL_NAME from "
						+" (select oi.PRODUCT_ID from "
						+" (select * from ORDER_HEADER where PRODUCT_STORE_ID = \'param\') as oh "
						+" join ORDER_ITEM as oi "
						+" on oh.ORDER_ID = oi.ORDER_ID order by oh.ORDER_DATE desc) as o "
						+" join PRODUCT as p "
						+" on p.PRODUCT_ID = o.PRODUCT_ID limit 20  ";
			sql = sql.replaceAll("param", productStoreId);

			ResultSet rs = processor.executeQuery(sql);
			List<Product> productList = new ArrayList<Product>();
			while (rs.next()) {
				Product product = new Product();
				product.setProductId(rs.getString("PRODUCT_ID"));
				product.setProductName(rs.getString("INTERNAL_NAME"));
				List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeatureAppl",
						UtilMisc.toMap("productId", rs.getString("PRODUCT_ID")));
				if (productFeatureList.size() > 0) {
					String text = "";
					for (GenericValue genericValue : productFeatureList) {
						GenericValue productFeature = delegator.findOne("ProductFeature", true,
								UtilMisc.toMap("productFeatureId", genericValue.getString("productFeatureId")));
						text = text + productFeature.getString("description") + " ";
					}
					product.setText(text);
				}
				
				if(productStoreId != null){
					GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), true);
					String facilityId = productStore.getString("inventoryFacilityId");
					String isImei = "N";
					List<GenericValue> inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("facilityId", facilityId , "productId" , rs.getString("PRODUCT_ID") ));
					BigDecimal total = new BigDecimal(0);
					for (GenericValue inventoryItem : inventoryItemList) {
						String availableToPromiseTotal = inventoryItem.getString("availableToPromiseTotal");
						if(new BigDecimal(availableToPromiseTotal).compareTo(new BigDecimal("0")) == 1){
							total = total.add(new BigDecimal(availableToPromiseTotal));
						}
						String imei = inventoryItem.getString("serialNumber");
						if(imei!=null && !"".equals(imei)){
							isImei="Y";
						}
					}
					product.setIsImei(isImei);
					String num = total.toString();
					if(!"0".equals(num)){
//						map.put("availableToPromiseTotal", num.substring(0, num.indexOf(".")));
						product.setAvailableToPromiseTotal(num.substring(0, num.indexOf(".")));
					}else{
//						map.put("availableToPromiseTotal", "0");
						product.setAvailableToPromiseTotal("0");
					}
					
					
//					inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId" , rs.getString("PRODUCT_ID")));
//					for (GenericValue inventoryItem : inventoryItemList) {
//						String imei = inventoryItem.getString("serialNumber");
//						if(imei!=null && !"".equals(imei)){
//							isImei="Y";
//						}
//					}
					
					
				}
				
				
				
				
				
				
				product.replenish();
				productList.add(product);
			}
			
//			List<String> orderBy = UtilMisc.toList("-orderDate");
//			List<GenericValue> findOrderProList = delegator.findByAnd("findOrderPro",
//					UtilMisc.toMap("productStoreId", productStoreId), orderBy);
//			List<GenericValue> findOrderProList = delegator.findByAnd("findOrderPro",
//					UtilMisc.toMap("productStoreId", productStoreId));
//			
//			List<Product> productList = new ArrayList<Product>();
//			int index = 0;
//			for (GenericValue findOrderPro : findOrderProList) {
//				if (index > 20) {
//					break;
//				}
//
//				index++;
//				GenericValue productG = findOrderPro.getRelatedOne("Product");
//				Product product = new Product();
//				product.setProductId(productG.getString("productId"));
//				product.setProductName(productG.getString("internalName"));
//
//				List<GenericValue> productFeatureList = delegator.findByAnd("ProductFeatureAppl",
//						UtilMisc.toMap("productId", productG.getString("productId")));
//				if (productFeatureList.size() > 0) {
//					String text = "";
//					for (GenericValue genericValue : productFeatureList) {
//						GenericValue productFeature = delegator.findOne("ProductFeature", true,
//								UtilMisc.toMap("productFeatureId", genericValue.getString("productFeatureId")));
//						text = text + productFeature.getString("description") + " ";
//					}
//					product.setText(text);
//				}
//				product.replenish();
//
//				Map<String, Object> priceContext = FastMap.newInstance();
//				priceContext.put("currencyUomId", cart.getCurrency());
//				priceContext.put("productStoreGroupId", cart.getProductStoreGroupId());
//				priceContext.put("quantity", new BigDecimal(1));
//				// priceContext.put("amount", this.getSelectedAmount());
//				// GenericValue product =
//				// delegator.findByPrimaryKeyCache("Product",
//				// UtilMisc.toMap("productId",
//				// orderItem.getString("productId")));
//				priceContext.put("product", productG);
//				// priceContext.put("prodCatalogId", this.getProdCatalogId());
//				// priceContext.put("webSiteId", cart.getWebSiteId());
//				priceContext.put("productStoreId", productStoreId);
//				// priceContext.put("agreementId", cart.getAgreementId());
//				priceContext.put("productPricePurposeId", "PURCHASE");
//				priceContext.put("checkIncludeVat", "Y");
//				Map<String, Object> priceResult = null;
//				try {
//					priceResult = dispatcher.runSync("calculateProductPrice", priceContext);
//				} catch (GenericServiceException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				if (ServiceUtil.isError(priceResult)) {
//					return priceResult;
//				}
//
////				productList.add(product);
//			}

			map.put("productList", productList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static final String resource = "ProductErrorUiLabels";

	/**
	 * 
	 * 
	 * @param request
	 * @param updateMode
	 * @return
	 */
	public static boolean hasEntityPermission(HttpServletRequest request, String updateMode) {
		String errMsg = null;
		// ORDERMGR_CREATE
		Security security = (Security) request.getAttribute("security");
		if (!security.hasEntityPermission("ORDERMGR", "_" + updateMode, request.getSession())) {
			Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
			errMsg = UtilProperties.getMessage(resource, "productevents.not_sufficient_permissions", messageMap,
					UtilHttp.getLocale(request));
			request.setAttribute("_ERROR_MESSAGE_", errMsg);
			return false;
		} else {
			return true;
	}
    }
	
	
	public static GenericValue check(Delegator delegator,String productId,String facilityId){
	    try {
//		delegator.findByAnd("",  UtilMisc.toMap("productCategoryId", categoryId));
		EntityCondition conditions = EntityCondition.makeCondition(EntityOperator.AND,
			EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
			EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId),
			EntityCondition.makeCondition("availableToPromiseTotal", EntityOperator.GREATER_THAN, "0"),
			EntityCondition.makeCondition(EntityCondition.makeCondition(EntityOperator.OR,
    					EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, null),
    					EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "INV_AVAILABLE"),
    					EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "IXF_COMPLETE")
				)
			));
		    List<GenericValue> inventoryItemList = delegator.findByCondition("InventoryItem",conditions, null, null);
		    if(inventoryItemList == null){
			return null;
		    }
		    
		    boolean type = true;
		    for (GenericValue inventoryItem : inventoryItemList) {
			if(inventoryItem.getString("inventoryItemTypeId").equals("NON_SERIAL_INV_ITEM") && 
				inventoryItem.getBigDecimal("availableToPromiseTotal").compareTo(new BigDecimal("0")) > 0){
			    type = false;
			    return null;
			}
		    }
		    
		    if(type){
			if(inventoryItemList != null && inventoryItemList.size() > 0){
			    return   inventoryItemList.get(0);
			}else{
			    return  null;
			}
		     }
	    } catch (GenericEntityException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    
	    
	    
	    return null;
	}
	
	
	
}