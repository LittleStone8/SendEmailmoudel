package com.yiwill.pos.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.order.shoppingcart.ShoppingCart;
import org.ofbiz.product.config.ProductConfigWorker;
import org.ofbiz.product.config.ProductConfigWrapper;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.yiwill.pos.shop.YiWillCart;
import com.yiwill.pos.shop.YiWillCart.CartShipInfo;
import com.yiwill.pos.shop.YiWillCartItem;
import com.yiwill.pos.shop.YiWillCartOrderReadHelper;

import javolution.util.FastMap;

public class YiWillLoadCartServices {

    public static final String module = YiWillLoadCartServices.class.getName();

    
    public static Map loadCartForUpdate(DispatchContext dctx, Map context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();

        String orderId = (String) context.get("orderId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        YiWillCart cart = null;
        Map result = null;
        try {
            cart = loadCartForUpdate(dispatcher, delegator, userLogin, orderId);
            result = ServiceUtil.returnSuccess();
            result.put("yiWillCart", cart);
        } catch (GeneralException e) {
            Debug.logError(e, module);
            result = ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
    }
    
    private static YiWillCart loadCartForUpdate(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String orderId) throws GeneralException {
        // load the order into a shopping cart
        Map loadCartResp = null;
//        try {
//            loadCartResp = dispatcher.runSync("loadCartFromOrder", UtilMisc.<String, Object>toMap("orderId", orderId,
//                                                                                  "skipInventoryChecks", Boolean.TRUE, // the items are already reserved, no need to check again
//                                                                                  "skipProductChecks", Boolean.TRUE, // the products are already in the order, no need to check their validity now
//                                                                                  "userLogin", userLogin));
            
            
            
            loadCartResp =  loadCartFromOrder(dispatcher, delegator, userLogin, orderId, true, true,false,null);
            
            
//        } catch (GenericServiceException e) {
//            Debug.logError(e, module);
//            throw new GeneralException(e.getMessage());
//        }
        if (ServiceUtil.isError(loadCartResp)) {
            throw new GeneralException(ServiceUtil.getErrorMessage(loadCartResp));
        }

        YiWillCart cart = (YiWillCart) loadCartResp.get("yiWillCart");
        if (cart == null) {
            throw new GeneralException("Error loading shopping cart from order [" + orderId + "]");
        } else {
            cart.setOrderId(orderId);
        }

        List shipGroupAssocs = null;
        try {
            shipGroupAssocs = delegator.findByAnd("OrderItemShipGroupAssoc", UtilMisc.toMap("orderId", orderId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GeneralException(e.getMessage());
        }
        // cancel existing inventory reservations
//        if (shipGroupAssocs != null) {
//            Iterator iri = shipGroupAssocs.iterator();
//            while (iri.hasNext()) {
//                GenericValue shipGroupAssoc = (GenericValue) iri.next();
//                String orderItemSeqId = shipGroupAssoc.getString("orderItemSeqId");
//                String shipGroupSeqId = shipGroupAssoc.getString("shipGroupSeqId");
//
//                Map cancelCtx = UtilMisc.toMap("userLogin", userLogin, "orderId", orderId);
//                cancelCtx.put("orderItemSeqId", orderItemSeqId);
//                cancelCtx.put("shipGroupSeqId", shipGroupSeqId);
//
//                Map cancelResp = null;
//                try {
//                    cancelResp = dispatcher.runSync("cancelOrderInventoryReservation", cancelCtx);
//                } catch (GenericServiceException e) {
//                    Debug.logError(e, module);
//                    throw new GeneralException(e.getMessage());
//                }
//                if (ServiceUtil.isError(cancelResp)) {
//                    throw new GeneralException(ServiceUtil.getErrorMessage(cancelResp));
//                }
//            }
//        }

        // cancel promo items -- if the promo still qualifies it will be added by the cart
        List promoItems = null;
        try {
            promoItems = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId, "isPromo", "Y"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GeneralException(e.getMessage());
        }
//        if (promoItems != null) {
//            Iterator pii = promoItems.iterator();
//            while (pii.hasNext()) {
//                GenericValue promoItem = (GenericValue) pii.next();
//                // Skip if the promo is already cancelled
//                if ("ITEM_CANCELLED".equals(promoItem.get("statusId"))) {
//                    continue;
//                }
//                Map cancelPromoCtx = UtilMisc.toMap("orderId", orderId);
//                cancelPromoCtx.put("orderItemSeqId", promoItem.getString("orderItemSeqId"));
//                cancelPromoCtx.put("userLogin", userLogin);
//                Map cancelResp = null;
//                try {
//                    cancelResp = dispatcher.runSync("cancelOrderItemNoActions", cancelPromoCtx);
//                } catch (GenericServiceException e) {
//                    Debug.logError(e, module);
//                    throw new GeneralException(e.getMessage());
//                }
//                if (ServiceUtil.isError(cancelResp)) {
//                    throw new GeneralException(ServiceUtil.getErrorMessage(cancelResp));
//                }
//            }
//        }

        // cancel exiting authorizations
//        Map releaseResp = null;
//        try {
//            releaseResp = dispatcher.runSync("releaseOrderPayments", UtilMisc.<String, Object>toMap("orderId", orderId, "userLogin", userLogin));
//        } catch (GenericServiceException e) {
//            Debug.logError(e, module);
//            throw new GeneralException(e.getMessage());
//        }
//        if (ServiceUtil.isError(releaseResp)) {
//            throw new GeneralException(ServiceUtil.getErrorMessage(releaseResp));
//        }

        // cancel other (non-completed and non-cancelled) payments
        List paymentPrefsToCancel = null;
        try {
            List exprs = UtilMisc.toList(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_RECEIVED"));
            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_CANCELLED"));
            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_DECLINED"));
            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_SETTLED"));
            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_REFUNDED"));
            EntityCondition cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            paymentPrefsToCancel = delegator.findList("OrderPaymentPreference", cond, null, null, null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GeneralException(e.getMessage());
        }
        if (paymentPrefsToCancel != null) {
            Iterator oppi = paymentPrefsToCancel.iterator();
            while (oppi.hasNext()) {
                GenericValue opp = (GenericValue) oppi.next();
                try {
                    opp.set("statusId", "PAYMENT_CANCELLED");
                    opp.store();
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    throw new GeneralException(e.getMessage());
                }
            }
        }

        // remove the adjustments
        try {
            List adjExprs = new LinkedList();
            adjExprs.add(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
            List exprs = new LinkedList();
            exprs.add(EntityCondition.makeCondition("orderAdjustmentTypeId", EntityOperator.EQUALS, "PROMOTION_ADJUSTMENT"));
            exprs.add(EntityCondition.makeCondition("orderAdjustmentTypeId", EntityOperator.EQUALS, "SHIPPING_CHARGES"));
            exprs.add(EntityCondition.makeCondition("orderAdjustmentTypeId", EntityOperator.EQUALS, "SALES_TAX"));
            adjExprs.add(EntityCondition.makeCondition(exprs, EntityOperator.OR));
            EntityCondition cond = EntityCondition.makeCondition(adjExprs, EntityOperator.AND);
            delegator.removeByCondition("OrderAdjustment", cond);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GeneralException(e.getMessage());
        }

        return cart;
    }
    
    
//    public static Map<String, Object>loadCartFromOrder(DispatchContext dctx, Map<String, Object> context) {
    	public static Map<String, Object>loadCartFromOrder(LocalDispatcher dispatcher,Delegator delegator,
    			GenericValue userLogin,String orderId,Boolean skipInventoryChecks,Boolean skipProductChecks,boolean includePromoItems,Locale locale) {
//        LocalDispatcher dispatcher = dctx.getDispatcher();
//        Delegator delegator = dctx.getDelegator();
//
//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//        String orderId = (String) context.get("orderId");
//        Boolean skipInventoryChecks = (Boolean) context.get("skipInventoryChecks");
//        Boolean skipProductChecks = (Boolean) context.get("skipProductChecks");
//        boolean includePromoItems = Boolean.TRUE.equals(context.get("includePromoItems"));
//        Locale locale = (Locale) context.get("locale");

        if (UtilValidate.isEmpty(skipInventoryChecks)) {
            skipInventoryChecks = Boolean.FALSE;
        }
        if (UtilValidate.isEmpty(skipProductChecks)) {
            skipProductChecks = Boolean.FALSE;
        }

        // get the order header
        GenericValue orderHeader = null;
        try {
            orderHeader = delegator.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }

        // initial require cart info
        YiWillCartOrderReadHelper orh = new YiWillCartOrderReadHelper(orderHeader);
        String productStoreId = orh.getProductStoreId();
        String productStoreGroupId = orh.getProductStoreGroupId();
        String orderTypeId = orh.getOrderTypeId();
        String currency = orh.getCurrency();
        String website = orh.getWebSiteId();
        String currentStatusString = orh.getCurrentStatusString();

        // create the cart
        YiWillCart cart = new YiWillCart(delegator, productStoreId,productStoreGroupId, website, locale, currency);
        cart.setDoPromotions(!includePromoItems);
        cart.setOrderType(orderTypeId);
        cart.setChannelType(orderHeader.getString("salesChannelEnumId"));
        cart.setInternalCode(orderHeader.getString("internalCode"));
        cart.setOrderDate(orderHeader.getTimestamp("orderDate"));
        cart.setOrderId(orderHeader.getString("orderId"));
        cart.setOrderName(orderHeader.getString("orderName"));
        cart.setOrderStatusId(orderHeader.getString("statusId"));
        
        cart.setIsNeedShip(orderHeader.getBoolean("isNeedShip"));
        
        cart.setOrderStatusString(currentStatusString);

        try {
        	 List<GenericValue> buyerUserList = delegator.findByAnd("Party", UtilMisc.toMap("partyId",orderHeader.getString("buyerId")));
             if(buyerUserList != null && buyerUserList.size() >0){
             	GenericValue buyerUser = EntityUtil.getFirst(buyerUserList);
             	cart.setUserLogin(buyerUser, dispatcher);
             }
             List<GenericValue> salesUserList = delegator.findByAnd("Party", UtilMisc.toMap("partyId",orderHeader.getString("salesId")));
	   	     if(salesUserList != null && salesUserList.size() >0){
	   	        	GenericValue cSalesUser = EntityUtil.getFirst(salesUserList);
	   	        	cart.setSalesUser(cSalesUser);
	   	     }
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }

        // set the order name
        String orderName = orh.getOrderName();
        if (orderName != null) {
            cart.setOrderName(orderName);
        }

        // set the role information
        GenericValue placingParty = orh.getPlacingParty();
        if (placingParty != null) {
            cart.setPlacingCustomerPartyId(placingParty.getString("partyId"));
        }

        GenericValue billFromParty = orh.getBillFromParty();
        if (billFromParty != null) {
            cart.setBillFromVendorPartyId(billFromParty.getString("partyId"));
        }

        GenericValue billToParty = orh.getBillToParty();
        if (billToParty != null) {
            cart.setBillToCustomerPartyId(billToParty.getString("partyId"));
        }

        GenericValue shipToParty = orh.getShipToParty();
        if (shipToParty != null) {
            cart.setShipToCustomerPartyId(shipToParty.getString("partyId"));
        }

        GenericValue endUserParty = orh.getEndUserParty();
        if (endUserParty != null) {
            cart.setEndUserCustomerPartyId(endUserParty.getString("partyId"));
            cart.setOrderPartyId(endUserParty.getString("partyId"));
        }

        // load order attributes
        List<GenericValue> orderAttributesList = null;
        try {
            orderAttributesList = delegator.findByAnd("OrderAttribute", UtilMisc.toMap("orderId", orderId));
            if (UtilValidate.isNotEmpty(orderAttributesList)) {
                for (GenericValue orderAttr : orderAttributesList) {
                    String name = orderAttr.getString("attrName");
                    String value = orderAttr.getString("attrValue");
                    cart.setOrderAttribute(name, value);
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }

        // load the payment infos
        List<GenericValue> orderPaymentPrefs = null;
        try {
        	
        	
        	
//            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
//            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_RECEIVED"));
//            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_CANCELLED"));
//            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_DECLINED"));
//            exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_SETTLED"));
//            EntityCondition cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
//            orderPaymentPrefs = delegator.findList("OrderPaymentPreference", cond, null, null, null, false);
            
            
            orderPaymentPrefs = delegator.findByAnd("OrderPaymentPreference", UtilMisc.toMap("orderId", orderId));
            
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }
        if (UtilValidate.isNotEmpty(orderPaymentPrefs)) {
            Iterator<GenericValue> oppi = orderPaymentPrefs.iterator();
            while (oppi.hasNext()) {
                GenericValue opp = (GenericValue) oppi.next();
                String paymentId = opp.getString("paymentMethodId");
                if (paymentId == null) {
                    paymentId = opp.getString("paymentMethodTypeId");
                }
                BigDecimal maxAmount = opp.getBigDecimal("maxAmount");
                String overflow = opp.getString("overflowFlag");

                YiWillCart.CartPaymentInfo cpi = null;

                if ((overflow == null || !"Y".equals(overflow)) && oppi.hasNext()) {
                    cpi = cart.addPaymentAmount(paymentId, maxAmount);
                    Debug.log("Added Payment: " + paymentId + " / " + maxAmount, module);
                } else {
                    cpi = cart.addPaymentAmount(paymentId, maxAmount);
                    Debug.log("Added Payment: " + paymentId + " / [no max]", module);
                }
                // for finance account the finAccountId needs to be set
//                if ("FIN_ACCOUNT".equals(paymentId)) {
//                    cpi.finAccountId = opp.getString("finAccountId");
//                }
                // set the billing account and amount
//                cart.setBillingAccount(orderHeader.getString("billingAccountId"), orh.getBillingAccountMaxAmount());
            }
        } else {
            Debug.log("No payment preferences found for order #" + orderId, module);
        }

        List<GenericValue> orderItemShipGroupList = orh.getOrderItemShipGroups();
        for (GenericValue orderItemShipGroup: orderItemShipGroupList) {
            // should be sorted by shipGroupSeqId
            int newShipInfoIndex = cart.addShipInfo();

            // shouldn't be gaps in it but allow for that just in case
            String cartShipGroupIndexStr = orderItemShipGroup.getString("shipGroupSeqId");
            int cartShipGroupIndex = NumberUtils.toInt(cartShipGroupIndexStr);

            if (newShipInfoIndex != (cartShipGroupIndex - 1)) {
                int groupDiff = cartShipGroupIndex - cart.getShipGroupSize();
                for (int i = 0; i < groupDiff; i++) {
                    newShipInfoIndex = cart.addShipInfo();
                }
            }

            CartShipInfo cartShipInfo = cart.getShipInfo(newShipInfoIndex);

            cartShipInfo.shipAfterDate = orderItemShipGroup.getTimestamp("shipAfterDate");
            cartShipInfo.shipBeforeDate = orderItemShipGroup.getTimestamp("shipByDate");
            cartShipInfo.shipmentMethodTypeId = orderItemShipGroup.getString("shipmentMethodTypeId");
            cartShipInfo.carrierPartyId = orderItemShipGroup.getString("carrierPartyId");
            cartShipInfo.supplierPartyId = orderItemShipGroup.getString("supplierPartyId");
            cartShipInfo.setMaySplit(orderItemShipGroup.getBoolean("maySplit"));
            cartShipInfo.giftMessage = orderItemShipGroup.getString("giftMessage");
            cartShipInfo.setContactMechId(orderItemShipGroup.getString("contactMechId"));
            cartShipInfo.shippingInstructions = orderItemShipGroup.getString("shippingInstructions");
            cartShipInfo.setFacilityId(orderItemShipGroup.getString("facilityId"));
            cartShipInfo.setVendorPartyId(orderItemShipGroup.getString("vendorPartyId"));
            cartShipInfo.setShipGroupSeqId(orderItemShipGroup.getString("shipGroupSeqId"));
        }

        List<GenericValue> orderItems = orh.getValidOrderItems();
//        long nextItemSeq = 0;
        if (UtilValidate.isNotEmpty(orderItems)) {
            for (GenericValue item : orderItems) {
                // get the next item sequence id
                String orderItemSeqId = item.getString("orderItemSeqId");
                orderItemSeqId = orderItemSeqId.replaceAll("\\P{Digit}", "");
                // get product Id
                String productId = item.getString("productId");
                GenericValue product = null;
                // creates survey responses for Gift cards same as last Order created
                Map surveyResponseResult = null;
                try {
                    product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                    if ("DIGITAL_GOOD".equals(product.getString("productTypeId"))) {
                        Map<String, Object> surveyResponseMap = FastMap.newInstance();
                        Map<String, Object> answers = FastMap.newInstance();
                        List<GenericValue> surveyResponseAndAnswers = delegator.findByAnd("SurveyResponseAndAnswer", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId));
                        if (UtilValidate.isNotEmpty(surveyResponseAndAnswers)) {
                            String surveyId = EntityUtil.getFirst(surveyResponseAndAnswers).getString("surveyId");
                            for (GenericValue surveyResponseAndAnswer : surveyResponseAndAnswers) {
                                answers.put((surveyResponseAndAnswer.get("surveyQuestionId").toString()), surveyResponseAndAnswer.get("textResponse"));
                            }
                            surveyResponseMap.put("answers", answers);
                            surveyResponseMap.put("surveyId", surveyId);
                            surveyResponseResult = dispatcher.runSync("createSurveyResponse", surveyResponseMap);
                        }
                    }
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                } catch (GenericServiceException e) {
                    Debug.logError(e.toString(), module);
                    return ServiceUtil.returnError(e.toString());
                }
//                try {
//                    long seq = Long.parseLong(orderItemSeqId);
//                    if (seq > nextItemSeq) {
//                        nextItemSeq = seq;
//                    }
//                } catch (NumberFormatException e) {
//                    Debug.logError(e, module);
//                    return ServiceUtil.returnError(e.getMessage());
//                }

                // do not include PROMO items
                if (!includePromoItems && item.get("isPromo") != null && "Y".equals(item.getString("isPromo"))) {
                    continue;
                }

                // not a promo item; go ahead and add it in
                BigDecimal amount = item.getBigDecimal("selectedAmount");
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }
                BigDecimal quantity = item.getBigDecimal("quantity");
                if (quantity == null) {
                    quantity = BigDecimal.ZERO;
                }

                BigDecimal unitPrice = null;
                if ("Y".equals(item.getString("isModifiedPrice"))) {
                    unitPrice = item.getBigDecimal("unitPrice");
                }

                int itemIndex = -1;
                if (item.get("productId") == null) {
                    // non-product item
                    String itemType = item.getString("orderItemTypeId");
                    String desc = item.getString("itemDescription");
                    try {
                        // TODO: passing in null now for itemGroupNumber, but should reproduce from OrderItemGroup records
                        itemIndex = cart.addNonProductItem(itemType, desc, null, unitPrice, quantity, null, null, null, dispatcher);
                    } catch (Exception e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(e.getMessage());
                    }
                } else {
                    // product item
                    String prodCatalogId = item.getString("prodCatalogId");

                    //prepare the rental data
                    Timestamp reservStart = null;
                    BigDecimal reservLength = null;
                    BigDecimal reservPersons = null;
                    String accommodationMapId = null;
                    String accommodationSpotId = null;

                    GenericValue workEffort = null;
                    String workEffortId = orh.getCurrentOrderItemWorkEffort(item);
                    if (workEffortId != null) {
                        try {
                            workEffort = delegator.findByPrimaryKey("WorkEffort", UtilMisc.toMap("workEffortId", workEffortId));
                        } catch (GenericEntityException e) {
                            Debug.logError(e, module);
                        }
                    }
                    if (workEffort != null && "ASSET_USAGE".equals(workEffort.getString("workEffortTypeId"))) {
                        reservStart = workEffort.getTimestamp("estimatedStartDate");
                        reservLength = YiWillCartOrderReadHelper.getWorkEffortRentalLength(workEffort);
                        reservPersons = workEffort.getBigDecimal("reservPersons");
                        accommodationMapId = workEffort.getString("accommodationMapId");
                        accommodationSpotId = workEffort.getString("accommodationSpotId");

                    }    //end of rental data

                    //check for AGGREGATED products
                    ProductConfigWrapper configWrapper = null;
                    String configId = null;
                    try {
                        product = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
                        if ("AGGREGATED_CONF".equals(product.getString("productTypeId"))) {
                            List<GenericValue>productAssocs = delegator.findByAnd("ProductAssoc", UtilMisc.toMap("productAssocTypeId", "PRODUCT_CONF", "productIdTo", product.getString("productId")));
                            productAssocs = EntityUtil.filterByDate(productAssocs);
                            if (UtilValidate.isNotEmpty(productAssocs)) {
                                productId = EntityUtil.getFirst(productAssocs).getString("productId");
                                configId = product.getString("configId");
                            }
                        }
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                    }

                    if (UtilValidate.isNotEmpty(configId)) {
                        configWrapper = ProductConfigWorker.loadProductConfigWrapper(delegator, dispatcher, configId, productId, productStoreId, prodCatalogId, website, currency, locale, userLogin);
                    }
                    String imei = item.getString("imei");
                    try {
                        itemIndex = cart.addItemToEnd(productId, amount, quantity, unitPrice, reservStart, reservLength, reservPersons,accommodationMapId,accommodationSpotId, null, null, prodCatalogId, configWrapper, item.getString("orderItemTypeId"), dispatcher, null, unitPrice == null ? null : false, skipInventoryChecks, skipProductChecks,imei);
                    } catch (Exception e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(e.getMessage());
                    } 
                }

                // flag the item w/ the orderItemSeqId so we can reference it
                YiWillCartItem cartItem = cart.findCartItem(itemIndex);
                cartItem.setIsPromo(item.get("isPromo") != null && "Y".equals(item.getString("isPromo")));
                cartItem.setOrderItemSeqId(item.getString("orderItemSeqId"));

                try {
                    cartItem.setItemGroup(cart.addItemGroup(item.getRelatedOneCache("OrderItemGroup")));
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }
                // attach surveyResponseId for each item
                if (UtilValidate.isNotEmpty(surveyResponseResult)){
                    cartItem.setAttribute("surveyResponseId",surveyResponseResult.get("surveyResponseId"));
                }
                // attach addition item information
                cartItem.setStatusId(item.getString("statusId"));
                cartItem.setItemType(item.getString("orderItemTypeId"));
                cartItem.setItemComment(item.getString("comments"));
                cartItem.setQuoteId(item.getString("quoteId"));
                cartItem.setQuoteItemSeqId(item.getString("quoteItemSeqId"));
                cartItem.setProductCategoryId(item.getString("productCategoryId"));
                cartItem.setDesiredDeliveryDate(item.getTimestamp("estimatedDeliveryDate"));
                cartItem.setShipBeforeDate(item.getTimestamp("shipBeforeDate"));
                cartItem.setShipAfterDate(item.getTimestamp("shipAfterDate"));
                cartItem.setShoppingList(item.getString("shoppingListId"), item.getString("shoppingListItemSeqId"));
                cartItem.setIsModifiedPrice("Y".equals(item.getString("isModifiedPrice")));
                cartItem.setName(item.getString("itemDescription"));

                // load order item attributes
                List<GenericValue> orderItemAttributesList = null;
                try {
                    orderItemAttributesList = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId));
                    if (UtilValidate.isNotEmpty(orderAttributesList)) {
                        for(GenericValue orderItemAttr : orderItemAttributesList) {
                            String name = orderItemAttr.getString("attrName");
                            String value = orderItemAttr.getString("attrValue");
                            cartItem.setOrderItemAttribute(name, value);
                        }
                    }
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                // load order item contact mechs
                List<GenericValue> orderItemContactMechList = null;
                try {
                    orderItemContactMechList = delegator.findByAnd("OrderItemContactMech", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId));
                    if (UtilValidate.isNotEmpty(orderItemContactMechList)) {
                        for (GenericValue orderItemContactMech : orderItemContactMechList) {
                            String contactMechPurposeTypeId = orderItemContactMech.getString("contactMechPurposeTypeId");
                            String contactMechId = orderItemContactMech.getString("contactMechId");
                            cartItem.addContactMech(contactMechPurposeTypeId, contactMechId);
                        }
                    }
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                // set the PO number on the cart
                cart.setPoNumber(item.getString("correspondingPoId"));

                List<GenericValue> itemAdjustments = orh.getOrderItemAdjustments(item);
                if (itemAdjustments != null) {
                    for(GenericValue itemAdjustment : itemAdjustments) {
                        cartItem.addAdjustment(itemAdjustment);
                    }
                }
            }

            // setup the OrderItemShipGroupAssoc records
            if (UtilValidate.isNotEmpty(orderItems)) {
                int itemIndex = 0;
                for (GenericValue item : orderItems) {

                    // set the item's ship group info
                    List<GenericValue> shipGroupAssocs = orh.getOrderItemShipGroupAssocs(item);
                    for (int g = 0; g < shipGroupAssocs.size(); g++) {
                        GenericValue sgAssoc = (GenericValue) shipGroupAssocs.get(g);
                        BigDecimal shipGroupQty = YiWillCartOrderReadHelper.getOrderItemShipGroupQuantity(sgAssoc);
                        if (shipGroupQty == null) {
                            shipGroupQty = BigDecimal.ZERO;
                        }

                        String cartShipGroupIndexStr = sgAssoc.getString("shipGroupSeqId");
                        int cartShipGroupIndex = NumberUtils.toInt(cartShipGroupIndexStr);

                        cartShipGroupIndex = cartShipGroupIndex - 1;
                        if (cartShipGroupIndex > 0) {
                            cart.positionItemToGroup(itemIndex, shipGroupQty, 0, cartShipGroupIndex, false);
                        }

                        cart.setItemShipGroupQty(itemIndex, shipGroupQty, cartShipGroupIndex);
                    }
                    itemIndex ++;
                }
            }

            // set the item seq in the cart
//            if (nextItemSeq > 0) {
//                try {
//                    cart.setNextItemSeq(nextItemSeq);
//                } catch (GeneralException e) {
//                    Debug.logError(e, module);
//                    return ServiceUtil.returnError(e.getMessage());
//                }
//            }
        }

        if (includePromoItems) {
            for (String productPromoCode: orh.getProductPromoCodesEntered()) {
                cart.addProductPromoCode(productPromoCode, dispatcher);
            }
            for (GenericValue productPromoUse: orh.getProductPromoUse()) {
                cart.addProductPromoUse(productPromoUse.getString("productPromoId"), productPromoUse.getString("productPromoCodeId"), productPromoUse.getBigDecimal("totalDiscountAmount"), productPromoUse.getBigDecimal("quantityLeftInActions"));
            }
        }

        List adjustments = orh.getOrderHeaderAdjustments();
        // If applyQuoteAdjustments is set to false then standard cart adjustments are used.
        if (!adjustments.isEmpty()) {
            // The cart adjustments are added to the cart
            cart.getAdjustments().addAll(adjustments);
        }

        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("yiWillCart", cart);
        return result;
    }
    

}
