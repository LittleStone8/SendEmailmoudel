/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
/* This file has been modified by Open Source Strategies, Inc. */
package com.yiwill.pos.shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericPK;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.catalog.CatalogWorker;
import org.ofbiz.product.config.ProductConfigWorker;
import org.ofbiz.product.config.ProductConfigWrapper;
import org.ofbiz.product.product.ProductWorker;
import org.ofbiz.product.store.ProductStoreSurveyWrapper;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.webapp.control.RequestHandler;
import org.ofbiz.webapp.website.WebSiteWorker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * Shopping cart events.
 */
public class YiWillCartEvents {

    public static String module = YiWillCartEvents.class.getName();
    public static final String resource = "OrderUiLabels";
    public static final String resource_error = "OrderErrorUiLabels";

    private static final String NO_ERROR = "noerror";
    private static final String NON_CRITICAL_ERROR = "noncritical";
    private static final String ERROR = "error";

    public static final MathContext generalRounding = new MathContext(10);

    public static String addProductPromoCode(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        YiWillCart cart = getCartObject(request);
        String productPromoCodeId = request.getParameter("productPromoCodeId");
        if (UtilValidate.isNotEmpty(productPromoCodeId)) {
            String checkResult = cart.addProductPromoCode(productPromoCodeId, dispatcher);
            if (UtilValidate.isNotEmpty(checkResult)) {
                request.setAttribute("_ERROR_MESSAGE_", checkResult);
                return "error";
            }
        }
        return "success";
    }

    /** Event to add an item to the shopping cart. */
    public static String addToCart(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        YiWillCart cart = getCartObject(request);
        YiWillCartHelper cartHelper = new YiWillCartHelper(delegator, dispatcher, cart);
        String controlDirective = null;
        Map result = null;
        String productId = null;
        String parentProductId = null;
        String itemType = null;
        String itemDescription = null;
        String productCategoryId = null;
        String priceStr = null;
        BigDecimal price = null;
        String quantityStr = null;
        BigDecimal quantity = BigDecimal.ZERO;
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

        // Get the parameters as a MAP, remove the productId and quantity params.
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
        if (paramMap.containsKey("PRODUCT_ID")) {
            parentProductId = (String) paramMap.remove("PRODUCT_ID");
        } else if (paramMap.containsKey("product_id")) {
            parentProductId = (String) paramMap.remove("product_id");
        }

        Debug.logInfo("adding item product " + productId, module);
        Debug.logInfo("adding item parent product " + parentProductId, module);

        if (paramMap.containsKey("ADD_CATEGORY_ID")) {
            productCategoryId = (String) paramMap.remove("ADD_CATEGORY_ID");
        } else if (paramMap.containsKey("add_category_id")) {
            productCategoryId = (String) paramMap.remove("add_category_id");
        }
        if (productCategoryId != null && productCategoryId.length() == 0) {
            productCategoryId = null;
        }

        if (paramMap.containsKey("ADD_ITEM_TYPE")) {
            itemType = (String) paramMap.remove("ADD_ITEM_TYPE");
        } else if (paramMap.containsKey("add_item_type")) {
            itemType = (String) paramMap.remove("add_item_type");
        }

        if (UtilValidate.isEmpty(productId)) {
            // before returning error; check make sure we aren't adding a special item type
            if (UtilValidate.isEmpty(itemType)) {
                request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.noProductInfoPassed", locale));
                return "success"; // not critical return to same page
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

        // Get the ProductConfigWrapper (it's not null only for configurable items)
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
                // The choices selected by the user are taken from request and set in the wrapper
                ProductConfigWorker.fillProductConfigWrapper(configWrapper, request);
            }
            if (!configWrapper.isCompleted()) {
                // The configuration is not valid
                request.setAttribute("product_id", productId);
                request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.configureProductBeforeAddingToCart", locale));
                return "product";
            } else {
                // load the Config Id
                ProductConfigWorker.storeProductConfigWrapper(configWrapper, delegator);
            }
        }


        //Check for virtual products
        if (ProductWorker.isVirtual(delegator, productId)) {

            if ("VV_FEATURETREE".equals(ProductWorker.getProductVirtualVariantMethod(delegator, productId))) {
                // get the selected features.
                List<String> selectedFeatures = new LinkedList<String>();
                java.util.Enumeration paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = (String) paramNames.nextElement();
                    if (paramName.startsWith("FT")) {
                        selectedFeatures.add(request.getParameterValues(paramName)[0]);
                    }
                }

                // check if features are selected
                if (UtilValidate.isEmpty(selectedFeatures)) {
                    request.setAttribute("product_id", productId);
                    request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.chooseVariationBeforeAddingToCart", locale));
                    return "product";
                }

                String variantProductId = ProductWorker.getVariantFromFeatureTree(productId, selectedFeatures, delegator);
                if (UtilValidate.isNotEmpty(variantProductId)) {
                    productId = variantProductId;
                } else {
                    request.setAttribute("product_id", productId);
                    request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.incompatibilityVariantFeature", locale));
                    return "product";
                }

            } else {
                request.setAttribute("product_id", productId);
                request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.chooseVariationBeforeAddingToCart", locale));
                return "product";
            }
        }

        // get the override price
        if (paramMap.containsKey("PRICE")) {
            priceStr = (String) paramMap.remove("PRICE");
        } else if (paramMap.containsKey("price")) {
            priceStr = (String) paramMap.remove("price");
        }
        if (priceStr == null) {
            priceStr = "0";  // default price is 0
        }

        // get the renting data
        if ("ASSET_USAGE".equals(ProductWorker.getProductTypeId(delegator, productId))) {
            if (paramMap.containsKey("reservStart")) {
                reservStartStr = (String) paramMap.remove("reservStart");
                if (reservStartStr.length() == 10) // only date provided, no time string?
                    reservStartStr += " 00:00:00.000000000"; // should have format: yyyy-mm-dd hh:mm:ss.fffffffff
                if (reservStartStr.length() > 0) {
                    try {
                        reservStart = java.sql.Timestamp.valueOf(reservStartStr);
                    } catch (Exception e) {
                        Debug.logWarning(e, "Problems parsing Reservation start string: "
                                + reservStartStr, module);
                        reservStart = null;
                        request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.rental.startDate", locale));
                        return "error";
                    }
                } else reservStart = null;
            }

            if (paramMap.containsKey("reservEnd")) {
                reservEndStr = (String) paramMap.remove("reservEnd");
                if (reservEndStr.length() == 10) // only date provided, no time string?
                    reservEndStr += " 00:00:00.000000000"; // should have format: yyyy-mm-dd hh:mm:ss.fffffffff
                if (reservEndStr.length() > 0) {
                    try {
                        reservEnd = java.sql.Timestamp.valueOf(reservEndStr);
                    } catch (Exception e) {
                        Debug.logWarning(e, "Problems parsing Reservation end string: " + reservEndStr, module);
                        reservEnd = null;
                        request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.rental.endDate", locale));
                        return "error";
                    }
                } else reservEnd = null;
            }

            if (reservStart != null && reservEnd != null) {
                reservLength = new BigDecimal(UtilDateTime.getInterval(reservStart, reservEnd)).divide(new BigDecimal("86400000"), generalRounding);
            }

            if (reservStart != null && paramMap.containsKey("reservLength")) {
                reservLengthStr = (String) paramMap.remove("reservLength");
                // parse the reservation Length
                try {
                    reservLength = (BigDecimal) ObjectType.simpleTypeConvert(reservLengthStr, "BigDecimal", null, locale);
                } catch (Exception e) {
                    Debug.logWarning(e, "Problems parsing reservation length string: "
                            + reservLengthStr, module);
                    reservLength = BigDecimal.ONE;
                    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderReservationLengthShouldBeAPositiveNumber", locale));
                    return "error";
                }
            }

            if (reservStart != null && paramMap.containsKey("reservPersons")) {
                reservPersonsStr = (String) paramMap.remove("reservPersons");
                // parse the number of persons
                try {
                    reservPersons = (BigDecimal) ObjectType.simpleTypeConvert(reservPersonsStr, "BigDecimal", null, locale);
                } catch (Exception e) {
                    Debug.logWarning(e, "Problems parsing reservation number of persons string: " + reservPersonsStr, module);
                    reservPersons = BigDecimal.ONE;
                    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderNumberOfPersonsShouldBeOneOrLarger", locale));
                    return "error";
                }
            }

            //check for valid rental parameters
            if (UtilValidate.isEmpty(reservStart) && UtilValidate.isEmpty(reservLength) && UtilValidate.isEmpty(reservPersons)) {
                request.setAttribute("product_id", productId);
                request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.enterBookingInforamtionBeforeAddingToCart", locale));
                return "product";
            }

            //check accommodation for reservations
            if ((paramMap.containsKey("accommodationMapId")) && (paramMap.containsKey("accommodationSpotId"))) {
                accommodationMapId = (String) paramMap.remove("accommodationMapId");
                accommodationSpotId = (String) paramMap.remove("accommodationSpotId");
            }
        }

        // get the quantity
        if (paramMap.containsKey("QUANTITY")) {
            quantityStr = (String) paramMap.remove("QUANTITY");
        } else if (paramMap.containsKey("quantity")) {
            quantityStr = (String) paramMap.remove("quantity");
        }
        if (UtilValidate.isEmpty(quantityStr)) {
            quantityStr = "1";  // default quantity is 1
        }

        // parse the price
        try {
            price = (BigDecimal) ObjectType.simpleTypeConvert(priceStr, "BigDecimal", null, locale);
        } catch (Exception e) {
            Debug.logWarning(e, "Problems parsing price string: " + priceStr, module);
            price = null;
        }

        // parse the quantity
        try {
            quantity = (BigDecimal) ObjectType.simpleTypeConvert(quantityStr, "BigDecimal", null, locale);
        } catch (Exception e) {
            Debug.logWarning(e, "Problems parsing quantity string: " + quantityStr, module);
            quantity = BigDecimal.ONE;
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

        // check for required amount
        if ((ProductWorker.isAmountRequired(delegator, productId)) && (amount == null || amount.doubleValue() == 0.0)) {
            request.setAttribute("product_id", productId);
            request.setAttribute("_EVENT_MESSAGE_", UtilProperties.getMessage(resource_error, "cart.addToCart.enterAmountBeforeAddingToCart", locale));
            return "product";
        }

        // get the ship before date (handles both yyyy-mm-dd input and full timestamp)
        shipBeforeDateStr = (String) paramMap.remove("shipBeforeDate");
        if (UtilValidate.isNotEmpty(shipBeforeDateStr)) {
            if (shipBeforeDateStr.length() == 10) shipBeforeDateStr += " 00:00:00.000";
            try {
                shipBeforeDate = java.sql.Timestamp.valueOf(shipBeforeDateStr);
            } catch (IllegalArgumentException e) {
                Debug.logWarning(e, "Bad shipBeforeDate input: " + e.getMessage(), module);
                shipBeforeDate = null;
            }
        }

        // get the ship after date (handles both yyyy-mm-dd input and full timestamp)
        shipAfterDateStr = (String) paramMap.remove("shipAfterDate");
        if (UtilValidate.isNotEmpty(shipAfterDateStr)) {
            if (shipAfterDateStr.length() == 10) shipAfterDateStr += " 00:00:00.000";
            try {
                shipAfterDate = java.sql.Timestamp.valueOf(shipAfterDateStr);
            } catch (IllegalArgumentException e) {
                Debug.logWarning(e, "Bad shipAfterDate input: " + e.getMessage(), module);
                shipAfterDate = null;
            }
        }

        // check for an add-to cart survey
        List surveyResponses = null;
        if (productId != null) {
            String productStoreId = ProductStoreWorker.getProductStoreId(request);
            List productSurvey = ProductStoreWorker.getProductSurveys(delegator, productStoreId, productId, "CART_ADD", parentProductId);
            if (UtilValidate.isNotEmpty(productSurvey)) {
                // TODO: implement multiple survey per product
                GenericValue survey = EntityUtil.getFirst(productSurvey);
                String surveyResponseId = (String) request.getAttribute("surveyResponseId");
                if (surveyResponseId != null) {
                    surveyResponses = UtilMisc.toList(surveyResponseId);
                } else {
                    String origParamMapId = UtilHttp.stashParameterMap(request);
                    Map<String, Object> surveyContext = UtilMisc.<String, Object>toMap("_ORIG_PARAM_MAP_ID_", origParamMapId);
                    GenericValue userLogin = cart.getUserLogin();
                    String partyId = null;
                    if (userLogin != null) {
                        partyId = userLogin.getString("partyId");
                    }
                    String formAction = "/additemsurvey";
                    String nextPage = RequestHandler.getOverrideViewUri(request.getPathInfo());
                    if (nextPage != null) {
                        formAction = formAction + "/" + nextPage;
                    }
                    ProductStoreSurveyWrapper wrapper = new ProductStoreSurveyWrapper(survey, partyId, surveyContext);
                    request.setAttribute("surveyWrapper", wrapper);
                    request.setAttribute("surveyAction", formAction); // will be used as the form action of the survey
                    return "survey";
                }
            }
        }
        if (surveyResponses != null) {
            paramMap.put("surveyResponses", surveyResponses);
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
                        YiWillCartItem sci = (YiWillCartItem) sciIter.next();
                        if (productList.contains(sci.getProductId())) {
                            try {
                                cart.removeCartItem(sci, dispatcher);
                            } catch (Exception e) {
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
                            YiWillCartItem sci = (YiWillCartItem) sciIter.next();
                            if (productList.contains(sci.getProductId())) {
                                try {
                                    cart.removeCartItem(sci, dispatcher);
                                } catch (Exception e) {
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
        String imei = (String) paramMap.get("imei");
        result = cartHelper.addToCart(catalogId, shoppingListId, shoppingListItemSeqId, productId, productCategoryId,
                    itemType, itemDescription, price, amount, quantity, reservStart, reservLength, reservPersons,
                    accommodationMapId, accommodationSpotId,
                    shipBeforeDate, shipAfterDate, configWrapper, itemGroupNumber, paramMap, parentProductId,imei);
        // Translate the parameters and add to the cart   
        return "success";
    }
    
    

    

    public static String quickCheckoutOrderWithDefaultOptions(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        YiWillCart cart = getCartObject(request);

        // Set the cart's default checkout options for a quick checkout
        cart.setDefaultCheckoutOptions(dispatcher);

        return "success";
    }

//    /** Delete an item from the shopping cart. */
//    public static String deleteFromCart(HttpServletRequest request, HttpServletResponse response) {
//        YiWillCart cart = getCartObject(request);
//        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//        YiWillCartHelper cartHelper = new YiWillCartHelper(null, dispatcher, cart);
//        Map paramMap = UtilHttp.getParameterMap(request);
//        Map result = cartHelper.deleteFromCart(paramMap);
//        return "success";
//    }

//    /** Update the items in the shopping cart. */
//    public static String modifyCart(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession();
//        YiWillCart cart = getCartObject(request);
//        Locale locale = UtilHttp.getLocale(request);
//        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//        Security security = (Security) request.getAttribute("security");
//        YiWillCartHelper cartHelper = new YiWillCartHelper(null, dispatcher, cart);
//        String controlDirective;
//        Map result;
//        // not used yet: Locale locale = UtilHttp.getLocale(request);
//
//        Map paramMap = UtilHttp.getParameterMap(request);
//
//        String removeSelectedFlag = request.getParameter("removeSelected");
//        String selectedItems[] = request.getParameterValues("selectedItem");
//        boolean removeSelected = ("true".equals(removeSelectedFlag) && selectedItems != null && selectedItems.length > 0);
//        result = cartHelper.modifyCart(security, userLogin, paramMap, removeSelected, selectedItems, locale);
//            return "success";
//    }

    /** Empty the shopping cart. */
    public static String clearCart(HttpServletRequest request, HttpServletResponse response) {
        YiWillCart cart = getCartObject(request);
        cart.clear();
        return "success";
    }


    public static YiWillCart getCartObject(HttpServletRequest request) {
        return  getCartObject(request,null,null);
    }
    
    public static YiWillCart getCartObject(HttpServletRequest request, Locale locale, String currencyUom) {
        return new YiWillCart();
    }
    
    


    /** Initialize order entry from an existing order **/
    public static String loadCartFromOrder(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue)session.getAttribute("userLogin");
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String orderId = request.getParameter("orderId");

        YiWillCart cart = null;
        try {
        	
            Map<String, Object> outMap = dispatcher.runSync("loadYiWillCartFromOrder",
                                                UtilMisc.<String, Object>toMap("orderId", orderId,
                                                        "skipProductChecks", Boolean.TRUE,
                                                        "userLogin", userLogin));
            
            if (!ServiceUtil.isSuccess(outMap)) {
                request.setAttribute("_ERROR_MESSAGE_", ServiceUtil.getErrorMessage(outMap));
                return "error";
             }

            cart = (YiWillCart) outMap.get("YiWillCart");

            cart.removeAdjustmentByType("SALES_TAX");
            cart.removeAdjustmentByType("PROMOTION_ADJUSTMENT");
            String shipGroupSeqId = null;
            long groupIndex = cart.getShipInfoSize();
            List orderAdjustmentList = new ArrayList();
            List orderAdjustments = new ArrayList();
            orderAdjustments = cart.getAdjustments();
            try {
                orderAdjustmentList = delegator.findList("OrderAdjustment", EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId), null, null, null, false);
            } catch (Exception e) {
                Debug.logError(e, module);
            }
            for (long itr = 1; itr <= groupIndex; itr++) {
                shipGroupSeqId = UtilFormatOut.formatPaddedNumber(1, 5);
                List<GenericValue> duplicateAdjustmentList = new ArrayList<GenericValue>();
                for (GenericValue adjustment: (List<GenericValue>)orderAdjustmentList) {
                    if ("PROMOTION_ADJUSTMENT".equals(adjustment.get("orderAdjustmentTypeId"))) {
                        cart.addAdjustment(adjustment);
                    }
                    if ("SALES_TAX".equals(adjustment.get("orderAdjustmentTypeId"))) {
                        if (adjustment.get("description") != null
                                    && ((String)adjustment.get("description")).startsWith("Tax adjustment due")) {
                                cart.addAdjustment(adjustment);
                            }
                        if ( adjustment.get("comments") != null
                                && ((String)adjustment.get("comments")).startsWith("Added manually by")) {
                            cart.addAdjustment(adjustment);
                        }
                    }
                }
                for (GenericValue orderAdjustment: (List<GenericValue>)orderAdjustments) {
                    if ("OrderAdjustment".equals(orderAdjustment.getEntityName())) {
                        if (("SHIPPING_CHARGES".equals(orderAdjustment.get("orderAdjustmentTypeId"))) &&
                                orderAdjustment.get("orderId").equals(orderId) &&
                                orderAdjustment.get("shipGroupSeqId").equals(shipGroupSeqId) && orderAdjustment.get("comments") == null) {
                            // Removing objects from list for old Shipping and Handling Charges Adjustment and Sales Tax Adjustment.
                            duplicateAdjustmentList.add(orderAdjustment);
                        }
                    }
                }
                orderAdjustments.removeAll(duplicateAdjustmentList);
            }
        } catch (GenericServiceException exc) {
            request.setAttribute("_ERROR_MESSAGE_", exc.getMessage());
            return "error";
        }

        cart.setAttribute("addpty", "Y");
//        session.setAttribute("YiWillCart", cart);
//        session.setAttribute("productStoreId", cart.getProductStoreId());
//        session.setAttribute("orderMode", cart.getOrderType());
//        session.setAttribute("orderPartyId", cart.getOrderPartyId());

        // Since we only need the cart items, so set the order id as null
        cart.setOrderId(null);
        return "success";
    }

   

   

    /** Initialize order entry **/
    public static String initializeOrderEntry(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue)session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);

        String productStoreId = request.getParameter("productStoreId");

        if (UtilValidate.isNotEmpty(productStoreId)) {
            session.setAttribute("productStoreId", productStoreId);
        }
        YiWillCart cart = getCartObject(request);

        // TODO: re-factor and move this inside the YiWillCart constructor
        String orderMode = request.getParameter("orderMode");
        if (orderMode != null) {
            cart.setOrderType(orderMode);
            session.setAttribute("orderMode", orderMode);
        } else {
            request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error,"OrderPleaseSelectEitherSaleOrPurchaseOrder", locale));
            return "error";
        }

        // check the selected product store
        GenericValue productStore = null;
        if (UtilValidate.isNotEmpty(productStoreId)) {
            productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
            if (productStore != null) {

                // check permission for taking the order
                boolean hasPermission = false;
                if ((cart.getOrderType().equals("PURCHASE_ORDER")) && (security.hasEntityPermission("ORDERMGR", "_PURCHASE_CREATE", session))) {
                    hasPermission = true;
                } else if (cart.getOrderType().equals("SALES_ORDER")) {
                    if (security.hasEntityPermission("ORDERMGR", "_SALES_CREATE", session)) {
                        hasPermission = true;
                    } else {
                        // if the user is a rep of the store, then he also has permission
                        List storeReps = null;
                        try {
                            storeReps = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"),
                                                            "partyId", userLogin.getString("partyId"), "roleTypeId", "SALES_REP"));
                        } catch (GenericEntityException gee) {
                            //
                        }
                        storeReps = EntityUtil.filterByDate(storeReps);
                        if (UtilValidate.isNotEmpty(storeReps)) {
                            hasPermission = true;
                        }
                    }
                }

                if (hasPermission) {
                    cart = getCartObject(request, null, productStore.getString("defaultCurrencyUomId"));
                } else {
                    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToTakeOrdersForThisStore", locale));
                    cart.clear();
                    session.removeAttribute("orderMode");
                    return "error";
                }
                cart.setProductStoreId(productStoreId);
            } else {
                cart.setProductStoreId(null);
            }
        }

        if ("SALES_ORDER".equals(cart.getOrderType()) && UtilValidate.isEmpty(cart.getProductStoreId())) {
            request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error,"OrderAProductStoreMustBeSelectedForASalesOrder", locale));
            cart.clear();
            session.removeAttribute("orderMode");
            return "error";
        }

        String salesChannelEnumId = request.getParameter("salesChannelEnumId");
        if (UtilValidate.isNotEmpty(salesChannelEnumId)) {
            cart.setChannelType(salesChannelEnumId);
        }

        // set party info
        String partyId = request.getParameter("supplierPartyId");
        cart.setAttribute("supplierPartyId", partyId);
        String originOrderId = request.getParameter("originOrderId");
        cart.setAttribute("originOrderId", originOrderId);

        if (!UtilValidate.isEmpty(request.getParameter("partyId"))) {
            partyId = request.getParameter("partyId");
        }
        String userLoginId = request.getParameter("userLoginId");
        if (partyId != null || userLoginId != null) {
            if (UtilValidate.isEmpty(partyId) && UtilValidate.isNotEmpty(userLoginId)) {
                GenericValue thisUserLogin = null;
                try {
                    thisUserLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", userLoginId));
                } catch (GenericEntityException gee) {
                    //
                }
                if (thisUserLogin != null) {
                    partyId = thisUserLogin.getString("partyId");
                } else {
                    partyId = userLoginId;
                }
            }
            if (UtilValidate.isNotEmpty(partyId)) {
                GenericValue thisParty = null;
                try {
                    thisParty = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", partyId));
                } catch (GenericEntityException gee) {
                    //
                }
                if (thisParty == null) {
                    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error,"OrderCouldNotLocateTheSelectedParty", locale));
                    return "error";
                } else {
                    cart.setOrderPartyId(partyId);
                    if ("PURCHASE_ORDER".equals(cart.getOrderType())) {
                        cart.setBillFromVendorPartyId(partyId);
                    }
                }
            } else if (partyId != null && partyId.length() == 0) {
                cart.setOrderPartyId("_NA_");
                partyId = null;
            }
        } else {
            partyId = cart.getPartyId();
            if (partyId != null && partyId.equals("_NA_")) partyId = null;
        }

        return "success";
    }


    
}
