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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.config.ProductConfigWorker;
import org.ofbiz.product.config.ProductConfigWrapper;
import org.ofbiz.product.product.ProductWorker;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;

/**
 * A facade over the
 * {@link org.ofbiz.order.YiWillCart.YiWillCart YiWillCart}
 * providing catalog and product services to simplify the interaction
 * with the cart directly.
 */
public class YiWillCartHelper {

    public static final String resource = "OrderUiLabels";
    public static String module = YiWillCartHelper.class.getName();
    public static final String resource_error = "OrderErrorUiLabels";

    // The shopping cart to manipulate
    private YiWillCart cart = null;

    // The entity engine delegator
    private Delegator delegator = null;

    // The service invoker
    private LocalDispatcher dispatcher = null;

    /**
     * Changes will be made to the cart directly, as opposed
     * to a copy of the cart provided.
     *
     * @param cart The cart to manipulate
     */
    public YiWillCartHelper(Delegator delegator, LocalDispatcher dispatcher, YiWillCart cart) {
        this.dispatcher = dispatcher;
        this.delegator = delegator;
        this.cart = cart;

        if (delegator == null) {
            this.delegator = dispatcher.getDelegator();
        }
        if (dispatcher == null) {
            throw new IllegalArgumentException("Dispatcher argument is null");
        }
        if (cart == null) {
            throw new IllegalArgumentException("YiWillCart argument is null");
        }
    }

    /** Event to add an item to the shopping cart. */
    public Map addToCart(String catalogId, String shoppingListId, String shoppingListItemSeqId, String productId,
            String productCategoryId, String itemType, String itemDescription,
            BigDecimal price, BigDecimal amount, BigDecimal quantity,
            java.sql.Timestamp reservStart, BigDecimal reservLength, BigDecimal reservPersons,
            java.sql.Timestamp shipBeforeDate, java.sql.Timestamp shipAfterDate,
            ProductConfigWrapper configWrapper, String itemGroupNumber, Map context, String parentProductId,String imei) {

        return addToCart(catalogId,shoppingListId,shoppingListItemSeqId,productId,
                productCategoryId,itemType,itemDescription,price,amount,quantity,
                reservStart,reservLength,reservPersons,null,null,shipBeforeDate,shipAfterDate,
                configWrapper,itemGroupNumber,context,parentProductId,imei);
    }

    /** Event to add an item to the shopping cart with accommodation. */
    public Map addToCart(String catalogId, String shoppingListId, String shoppingListItemSeqId, String productId,
            String productCategoryId, String itemType, String itemDescription,
            BigDecimal price, BigDecimal amount, BigDecimal quantity,
            java.sql.Timestamp reservStart, BigDecimal reservLength, BigDecimal reservPersons, String accommodationMapId,String accommodationSpotId,
            java.sql.Timestamp shipBeforeDate, java.sql.Timestamp shipAfterDate,
            ProductConfigWrapper configWrapper, String itemGroupNumber, Map context, String parentProductId,String imei) {
        Map result = null;
        Map attributes = null;
        String pProductId = null;
        pProductId = parentProductId;
        // price sanity check
        if (productId == null && price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            String errMsg = UtilProperties.getMessage(resource_error, "cart.price_not_positive_number", this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        // quantity sanity check
        if (quantity.compareTo(BigDecimal.ONE) < 0) {
            String errMsg = UtilProperties.getMessage(resource_error, "cart.quantity_not_positive_number", this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        // amount sanity check
        if (amount != null && amount.compareTo(BigDecimal.ZERO) < 0) {
            String errMsg = UtilProperties.getMessage(resource_error, "cart.amount_not_positive_number", this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        // check desiredDeliveryDate syntax and remove if empty
        String ddDate = (String) context.get("itemDesiredDeliveryDate");
        if (!UtilValidate.isEmpty(ddDate)) {
            try {
                java.sql.Timestamp.valueOf((String) context.get("itemDesiredDeliveryDate"));
            } catch (IllegalArgumentException e) {
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderInvalidDesiredDeliveryDateSyntaxError",this.cart.getLocale()));
            }
        } else {
            context.remove("itemDesiredDeliveryDate");
        }

        // remove an empty comment
        String comment = (String) context.get("itemComment");
        if (UtilValidate.isEmpty(comment)) {
            context.remove("itemComment");
        }

        // stores the default desired delivery date in the cart if need
        if (!UtilValidate.isEmpty(context.get("useAsDefaultDesiredDeliveryDate"))) {
            cart.setDefaultItemDeliveryDate((String) context.get("itemDesiredDeliveryDate"));
        } else {
            // do we really want to clear this if it isn't checked?
            cart.setDefaultItemDeliveryDate(null);
        }

        // stores the default comment in session if need
        if (!UtilValidate.isEmpty(context.get("useAsDefaultComment"))) {
            cart.setDefaultItemComment((String) context.get("itemComment"));
        } else {
            // do we really want to clear this if it isn't checked?
            cart.setDefaultItemComment(null);
        }

        // Create a HashMap of product attributes - From YiWillCartItem.attributeNames[]
        for (int namesIdx = 0; namesIdx < YiWillCartItem.attributeNames.length; namesIdx++) {
            if (attributes == null)
                attributes = new HashMap();
            if (context.containsKey(YiWillCartItem.attributeNames[namesIdx])) {
                attributes.put(YiWillCartItem.attributeNames[namesIdx], context.get(YiWillCartItem.attributeNames[namesIdx]));
            }
        }

        // check for required amount flag; if amount and no flag set to 0
        GenericValue product = null;
        if (productId != null) {
            try {
                product = delegator.findByPrimaryKeyCache("Product", UtilMisc.toMap("productId", productId));
            } catch (GenericEntityException e) {
                Debug.logError(e, "Unable to lookup product : " + productId, module);
            }
            if (product == null || product.get("requireAmount") == null || "N".equals(product.getString("requireAmount"))) {
                amount = null;
            }
            Debug.logInfo("carthelper productid " + productId,module);
            Debug.logInfo("parent productid " + pProductId,module);
            //if (product != null && !"Y".equals(product.getString("isVariant")))
            //    pProductId = null;

        }

        // Get the additional features selected for the product (if any)
        Map selectedFeatures = UtilHttp.makeParamMapWithPrefix(context, null, "FT", null);
        Iterator selectedFeaturesTypes = selectedFeatures.keySet().iterator();
        Map additionalFeaturesMap = FastMap.newInstance();
        while (selectedFeaturesTypes.hasNext()) {
            String selectedFeatureType = (String)selectedFeaturesTypes.next();
            String selectedFeatureValue = (String)selectedFeatures.get(selectedFeatureType);
            if (UtilValidate.isNotEmpty(selectedFeatureValue)) {
                GenericValue productFeatureAndAppl = null;
                try {
                    productFeatureAndAppl = EntityUtil.getFirst(EntityUtil.filterByDate(delegator.findByAnd("ProductFeatureAndAppl",
                                                                                    UtilMisc.toMap("productId", productId,
                                                                                                   "productFeatureTypeId", selectedFeatureType,
                                                                                                   "productFeatureId", selectedFeatureValue))));
                } catch (GenericEntityException gee) {
                    Debug.logError(gee, module);
                }
                if (UtilValidate.isNotEmpty(productFeatureAndAppl)) {
                    productFeatureAndAppl.set("productFeatureApplTypeId", "STANDARD_FEATURE");
                }
                additionalFeaturesMap.put(selectedFeatureType, productFeatureAndAppl);
            }
        }

        // add or increase the item to the cart
        try {
            int itemId = -1;
            if (productId != null) {

                       itemId = cart.addOrIncreaseItem(productId, amount, quantity, reservStart, reservLength,
                                                reservPersons, accommodationMapId, accommodationSpotId, shipBeforeDate, shipAfterDate, additionalFeaturesMap, attributes,
                                                catalogId, configWrapper, itemType, itemGroupNumber, pProductId, dispatcher,imei);

            } else {
                itemId = cart.addNonProductItem(itemType, itemDescription, productCategoryId, price, quantity, attributes, catalogId, itemGroupNumber, dispatcher);
            }

            // set the shopping list info
            if (itemId > -1 && shoppingListId != null && shoppingListItemSeqId != null) {
                YiWillCartItem item = cart.findCartItem(itemId);
                item.setShoppingList(shoppingListId, shoppingListItemSeqId);
            }
        } catch (Exception e) {
            if (cart.getOrderType().equals("PURCHASE_ORDER")) {
                String errMsg = UtilProperties.getMessage(resource_error, "cart.product_not_valid_for_supplier", this.cart.getLocale());
                errMsg = errMsg + " (" + e.getMessage() + ")";
                result = ServiceUtil.returnError(errMsg);
            } else {
                result = ServiceUtil.returnError(e.getMessage());
            }
            return result;
        }

        // Indicate there were no critical errors
        result = ServiceUtil.returnSuccess();
        return result;
    }

    public Map addToCartFromOrder(String catalogId, String orderId, String[] itemIds, boolean addAll, String itemGroupNumber) {
        ArrayList errorMsgs = new ArrayList();
        Map result;
        String errMsg = null;

        if (orderId == null || orderId.length() <= 0) {
            errMsg = UtilProperties.getMessage(resource_error,"cart.order_not_specified_to_add_from", this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        boolean noItems = true;
        List itemIdList = null;
        Iterator itemIter = null;
        YiWillCartOrderReadHelper orderHelper = new YiWillCartOrderReadHelper(delegator, orderId);
        if (addAll) {
            itemIdList = orderHelper.getOrderItems();
        } else {
            if (itemIds != null) {
                itemIdList = Arrays.asList(itemIds);
            }
        }
        if (UtilValidate.isNotEmpty(itemIdList)) {
            itemIter = itemIdList.iterator();
        }

        String orderItemTypeId = null;
        String productId = null;
        if (itemIter != null && itemIter.hasNext()) {
            while (itemIter.hasNext()) {
                GenericValue orderItem = null;
                Object value = itemIter.next();
                if (value instanceof GenericValue) {
                    orderItem = (GenericValue) value;
                } else {
                    String orderItemSeqId = (String) value;
                    orderItem = orderHelper.getOrderItem(orderItemSeqId);
                }
                orderItemTypeId = orderItem.getString("orderItemTypeId");
                productId = orderItem.getString("productId");
                // do not store rental items
                if (orderItemTypeId.equals("RENTAL_ORDER_ITEM"))
                    continue;
                // never read: int itemId = -1;
                if (UtilValidate.isNotEmpty(productId) && orderItem.get("quantity") != null) {
                    BigDecimal amount = orderItem.getBigDecimal("selectedAmount");
                    ProductConfigWrapper configWrapper = null;
                    String aggregatedProdId = null;
                    if ("AGGREGATED_CONF".equals(ProductWorker.getProductTypeId(delegator, productId))) {
                        try {
                            GenericValue instanceProduct = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
                            String configId = instanceProduct.getString("configId");
                            aggregatedProdId = ProductWorker.getInstanceAggregatedId(delegator, productId);
                            configWrapper = ProductConfigWorker.loadProductConfigWrapper(delegator, dispatcher, configId, aggregatedProdId, cart.getProductStoreId(), catalogId, cart.getWebSiteId(), cart.getCurrency(), cart.getLocale(), cart.getAutoUserLogin());
                        } catch (GenericEntityException e) {
                            errorMsgs.add(e.getMessage());
                        }

                    }
                    try {
                        this.cart.addOrIncreaseItem(UtilValidate.isNotEmpty(aggregatedProdId) ? aggregatedProdId :  productId, amount, orderItem.getBigDecimal("quantity"),
                                null, null, null, null, null, null, null, catalogId, configWrapper, orderItemTypeId, itemGroupNumber, null, dispatcher);
                        noItems = false;
                    } catch (Exception e) {
                        errorMsgs.add(e.getMessage());
                    }
                }
            }
            if (errorMsgs.size() > 0) {
                result = ServiceUtil.returnError(errorMsgs);
                result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
                return result; // don't return error because this is a non-critical error and should go back to the same page
            }
        } else {
            noItems = true;
        }

        if (noItems) {
            result = ServiceUtil.returnSuccess();
            result.put("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error,"OrderNoItemsFoundToAdd", this.cart.getLocale()));
            return result; // don't return error because this is a non-critical error and should go back to the same page
        }

        result = ServiceUtil.returnSuccess();
        return result;
    }

    /**
     * Adds all products in a category according to quantity request parameter
     * for each; if no parameter for a certain product in the category, or if
     * quantity is 0, do not add.
     * If a _ign_${itemGroupNumber} is appended to the name it will be put in that group instead of the default in the request parameter in itemGroupNumber
     *
     * There are 2 options for the syntax:
     *  - name="quantity_${productId}" value="${quantity}
     *  - name="product_${whatever}" value="${productId}" (note: quantity is always 1)
     */
    public Map addToCartBulk(String catalogId, String categoryId, Map context) {
        String itemGroupNumber = (String) context.get("itemGroupNumber");
        // use this prefix for the main structure such as a checkbox or a text input where name="quantity_${productId}" value="${quantity}"
        String keyPrefix = "quantity_";
        // use this prefix for a different structure, useful for radio buttons; can have any suffix, name="product_${whatever}" value="${productId}" and quantity is always 1
        String productQuantityKeyPrefix = "product_";

        // If a _ign_${itemGroupNumber} is appended to the name it will be put in that group instead of the default in the request parameter in itemGroupNumber
        String ignSeparator = "_ign_";

        // iterate through the context and find all keys that start with "quantity_"
        Iterator entryIter = context.entrySet().iterator();
        while (entryIter.hasNext()) {
            Map.Entry entry = (Map.Entry) entryIter.next();
            String productId = null;
            String quantStr = null;
            String itemGroupNumberToUse = itemGroupNumber;
            if (entry.getKey() instanceof String) {
                String key = (String) entry.getKey();
                //Debug.logInfo("Bulk Key: " + key, module);

                int ignIndex = key.indexOf(ignSeparator);
                if (ignIndex > 0) {
                    itemGroupNumberToUse = key.substring(ignIndex + ignSeparator.length());
                    key = key.substring(0, ignIndex);
                }

                if (key.startsWith(keyPrefix)) {
                    productId = key.substring(keyPrefix.length());
                    quantStr = (String) entry.getValue();
                } else if (key.startsWith(productQuantityKeyPrefix)) {
                    productId = (String) entry.getValue();
                    quantStr = "1";
                } else {
                    continue;
                }
            } else {
                continue;
            }

            if (UtilValidate.isNotEmpty(quantStr)) {
                BigDecimal quantity = BigDecimal.ZERO;

                try {
                    quantity = new BigDecimal(quantStr);
                } catch (NumberFormatException nfe) {
                    quantity = BigDecimal.ZERO;
                }
                if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        if (Debug.verboseOn()) Debug.logVerbose("Bulk Adding to cart [" + quantity + "] of [" + productId + "] in Item Group [" + itemGroupNumber + "]", module);
                        this.cart.addOrIncreaseItem(productId, null, quantity, null, null, null, null, null, null, null, catalogId, null, null, itemGroupNumberToUse, null, dispatcher);
                    } catch (Exception e) {
                        return ServiceUtil.returnError(e.getMessage());
                    } 
                }
            }
        }

        //Indicate there were no non critical errors
        return ServiceUtil.returnSuccess();
    }

    /**
     * Adds a set of requirements to the cart.
     */
    public Map addToCartBulkRequirements(String catalogId, Map context) {
        NumberFormat nf = NumberFormat.getNumberInstance(this.cart.getLocale());
        String itemGroupNumber = (String) context.get("itemGroupNumber");
        // check if we are using per row submit
        boolean useRowSubmit = (!context.containsKey("_useRowSubmit"))? false :
                "Y".equalsIgnoreCase((String)context.get("_useRowSubmit"));

        // check if we are to also look in a global scope (no delimiter)
        //boolean checkGlobalScope = (!context.containsKey("_checkGlobalScope"))? false :
        //        "Y".equalsIgnoreCase((String)context.get("_checkGlobalScope"));

        // The number of multi form rows is retrieved
        int rowCount = UtilHttp.getMultiFormRowCount(context);

        // assume that the facility is the same for all requirements
        String facilityId = (String) context.get("facilityId_o_0");
        if (UtilValidate.isNotEmpty(facilityId)) {
            cart.setFacilityId(facilityId);
        }

        // now loop throw the rows and prepare/invoke the service for each
        for (int i = 0; i < rowCount; i++) {
            String productId = null;
            String quantStr = null;
            String requirementId = null;
            String thisSuffix = UtilHttp.MULTI_ROW_DELIMITER + i;
            boolean rowSelected = (!context.containsKey("_rowSubmit" + thisSuffix))? false :
                    "Y".equalsIgnoreCase((String)context.get("_rowSubmit" + thisSuffix));

            // make sure we are to process this row
            if (useRowSubmit && !rowSelected) {
                continue;
            }

            // build the context
            if (context.containsKey("productId" + thisSuffix)) {
                productId = (String) context.get("productId" + thisSuffix);
                quantStr = (String) context.get("quantity" + thisSuffix);
                requirementId = (String) context.get("requirementId" + thisSuffix);
                GenericValue requirement = null;
                try {
                    requirement = delegator.findByPrimaryKey("Requirement", UtilMisc.toMap("requirementId", requirementId));
                } catch (GenericEntityException gee) {
                }
                if (requirement == null) {
                    return ServiceUtil.returnError("Requirement with id [" + requirementId + "] doesn't exist.");
                }

                if (UtilValidate.isNotEmpty(quantStr)) {
                    BigDecimal quantity = BigDecimal.ZERO;
                    try {
                        quantity = (BigDecimal) ObjectType.simpleTypeConvert(quantStr, "BigDecimal", null, cart.getLocale());
                    } catch (GeneralException ge) {
                        quantity = BigDecimal.ZERO;
                    }
                    if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                        Iterator items = this.cart.iterator();
                        boolean requirementAlreadyInCart = false;
                        while (items.hasNext() && !requirementAlreadyInCart) {
                            YiWillCartItem sci = (YiWillCartItem)items.next();
                            if (sci.getRequirementId() != null && sci.getRequirementId().equals(requirementId)) {
                                requirementAlreadyInCart = true;
                                continue;
                            }
                        }
                        if (requirementAlreadyInCart) {
                            if (Debug.warningOn()) Debug.logWarning(UtilProperties.getMessage(resource_error, "OrderTheRequirementIsAlreadyInTheCartNotAdding", UtilMisc.toMap("requirementId",requirementId), cart.getLocale()), module);
                            continue;
                        }
                        try {
                            if (Debug.verboseOn()) Debug.logVerbose("Bulk Adding to cart requirement [" + quantity + "] of [" + productId + "]", module);
                            int index = this.cart.addOrIncreaseItem(productId, null, quantity, null, null, null, requirement.getTimestamp("requiredByDate"), null, null, null, catalogId, null, null, itemGroupNumber, null, dispatcher);
                            YiWillCartItem sci = (YiWillCartItem)this.cart.items().get(index);
                            sci.setRequirementId(requirementId);
                        } catch (Exception e) {
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }
                }
            }
        }
        //Indicate there were no non critical errors
        return ServiceUtil.returnSuccess();
    }

    /**
     * Adds all products in a category according to default quantity on ProductCategoryMember
     * for each; if no default for a certain product in the category, or if
     * quantity is 0, do not add
     */
    public Map addCategoryDefaults(String catalogId, String categoryId, String itemGroupNumber) {
        ArrayList errorMsgs = new ArrayList();
        Map result = null;
        String errMsg = null;

        if (categoryId == null || categoryId.length() <= 0) {
            errMsg = UtilProperties.getMessage(resource_error,"cart.category_not_specified_to_add_from", this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        Collection prodCatMemberCol = null;

        try {
            prodCatMemberCol = delegator.findByAndCache("ProductCategoryMember", UtilMisc.toMap("productCategoryId", categoryId));
        } catch (GenericEntityException e) {
            Debug.logWarning(e.toString(), module);
            Map messageMap = UtilMisc.toMap("categoryId", categoryId);
            messageMap.put("message", e.getMessage());
            errMsg = UtilProperties.getMessage(resource_error,"cart.could_not_get_products_in_category_cart", messageMap, this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        if (prodCatMemberCol == null) {
            Map messageMap = UtilMisc.toMap("categoryId", categoryId);
            errMsg = UtilProperties.getMessage(resource_error,"cart.could_not_get_products_in_category", messageMap, this.cart.getLocale());
            result = ServiceUtil.returnError(errMsg);
            return result;
        }

        BigDecimal totalQuantity = BigDecimal.ZERO;
        Iterator pcmIter = prodCatMemberCol.iterator();

        while (pcmIter.hasNext()) {
            GenericValue productCategoryMember = (GenericValue) pcmIter.next();
            BigDecimal quantity = productCategoryMember.getBigDecimal("quantity");

            if (quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0) {
                try {
                    this.cart.addOrIncreaseItem(productCategoryMember.getString("productId"),
                            null, quantity, null, null, null, null, null, null, null,
                            catalogId, null, null, itemGroupNumber, null, dispatcher);
                    totalQuantity = totalQuantity.add(quantity);
                } catch (Exception e) {
                    errorMsgs.add(e.getMessage());
                } 
            }
        }
        if (errorMsgs.size() > 0) {
            result = ServiceUtil.returnError(errorMsgs);
            result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
            return result; // don't return error because this is a non-critical error and should go back to the same page
        }

        result = ServiceUtil.returnSuccess();
        result.put("totalQuantity", totalQuantity);
        return result;
    }

  public Map changeItemQuantityByOrderItemSeqId(String orderItemSeqId,String quantity) {
	  List<YiWillCartItem> itemList = this.cart.items();
		  for (int i = 0; i < itemList.size(); i++) {
			  YiWillCartItem item = itemList.get(i);
			  if(item.getOrderItemSeqId().equals(orderItemSeqId)){
				  try {
					  BigDecimal quantitya =  new BigDecimal(quantity);
					  item.setQuantity(quantitya, dispatcher, this.cart);
					  Map<String, Object> result = ServiceUtil.returnSuccess();
					  return result;
					  
				} catch (Exception e) {
//					int count = 0;
//					try {
//						Delegator delegator = (Delegator) request.getAttribute("delegator");
//						//包装参数
//						GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
//						SQLProcessor processor = new SQLProcessor(helperInfo);
//						String queryByNameSql = "select oh.ORDER_ID , ii.AVAILABLE_TO_PROMISE_TOTAL , ps.PRODUCT_STORE_ID , ii.FACILITY_ID from (select * from ORDER_ITEM where ORDER_ID=\'param1\' and ORDER_ITEM_SEQ_ID=\'param2\') as oi "
//											+" left join ORDER_HEADER oh "
//											+" on oi.ORDER_ID = oh.ORDER_ID " 
//											+" left join PRODUCT_STORE ps "
//											+" on oh.PRODUCT_STORE_ID = ps.PRODUCT_STORE_ID "
//											+" left join FACILITY f "
//											+" on ps.INVENTORY_FACILITY_ID = f.FACILITY_ID "
//											+" left join (select * from INVENTORY_ITEM where PRODUCT_ID=\'param3\') ii "
//											+" on f.FACILITY_ID = ii.FACILITY_ID ";
//				
//						queryByNameSql=queryByNameSql.replaceAll("param1", this.cart.getOrderId());
//						queryByNameSql=queryByNameSql.replaceAll("param2", orderItemSeqId);
//						String productId = request.getParameter("add_product_id");
//						queryByNameSql=queryByNameSql.replaceAll("param3", productId);
//						ResultSet rs = processor.executeQuery(queryByNameSql);
//						while (rs.next()) {
//							String num = rs.getString("AVAILABLE_TO_PROMISE_TOTAL");
//							count = count + Integer.parseInt(num); 
//						}
//						
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
					
					
					
					return ServiceUtil.returnError("change item error "+item.getName() +" "+orderItemSeqId +" inventory not enough " );
				}
			  }
		}
	  return ServiceUtil.returnError("no found");
}
  public Map changeItemPriceByOrderItemSeqId(String orderItemSeqId,String value) {
	  List<YiWillCartItem> itemList = this.cart.items();
	  for (int i = 0; i < itemList.size(); i++) {
		  YiWillCartItem item = itemList.get(i);
		  if(item.getOrderItemSeqId().equals(orderItemSeqId)){
			  try {
				  
				  BigDecimal oldPrice = item.getBasePrice();
				  BigDecimal quantitya =  new BigDecimal(value);
				  
				  
				  
				  
                 if (oldPrice.compareTo(quantitya) != 0) {
//                     if (security.hasEntityPermission("ORDERMGR", "_CREATE", userLogin)) {
                         if (item != null) {
                             item.setBasePrice(quantitya); // this is quantity because the parsed number variable is the same as quantity
                             item.setDisplayPrice(quantitya); // or the amount shown the cart items page won't be right
                             item.setIsModifiedPrice(true); // flag as a modified price
                         }
//                     }
                 }
				   
//				  item.setQuantity(quantitya, dispatcher, this.cart);
				  Map<String, Object> result = ServiceUtil.returnSuccess();
				  return result;
				  
			  } catch (Exception e) {
				  return ServiceUtil.returnError("change item error "+item.getName() +" "+orderItemSeqId);
			  }
		  }
	  }
	  return ServiceUtil.returnError("no found");
  }
  
  
  public Map deleteItemByOrderItemSeqId(String orderItemSeqId) {
	  List<YiWillCartItem> itemList = this.cart.items();
	  for (int i = 0; i < itemList.size(); i++) {
		  YiWillCartItem item = itemList.get(i);
		  if(item.getOrderItemSeqId().equals(orderItemSeqId)){
			  try {

				  this.cart.removeCartItem(i, dispatcher);
				  Map<String, Object> result = ServiceUtil.returnSuccess();
				  return result;
				  
			  } catch (Exception e) {
				  return ServiceUtil.returnError("remove item error "+item.getName() +" "+orderItemSeqId);
			  }
		  }
	  }
	  return ServiceUtil.returnError("no found");
  }
  public Map deleteAllItem() {
	  List<YiWillCartItem> itemList = this.cart.items();
	  for (int i = 0; i < itemList.size(); i++) {
		  YiWillCartItem item = itemList.get(i);
			  try {
				  this.cart.removeCartItem(0, dispatcher);
			  } catch (Exception e) {
				  return ServiceUtil.returnError("remove item error "+item.getName());
			  }
	  }
	  
	  Map<String, Object> result = ServiceUtil.returnSuccess();
	  return result;
  }

    /** Empty the shopping cart. */
    public boolean clearCart() {
        this.cart.clear();
        return true;
    }

    /** Returns the shopping cart this helper is wrapping. */
    public YiWillCart getCartObject() {
        return this.cart;
    }

    public GenericValue getFeatureAppl(String productId, String optionField, String featureId) {
        if (delegator == null) {
            throw new IllegalArgumentException("No delegator available to lookup ProductFeature");
        }

        Map fields = UtilMisc.toMap("productId", productId, "productFeatureId", featureId);
        if (optionField != null) {
            int featureTypeStartIndex = optionField.indexOf('^') + 1;
            int featureTypeEndIndex = optionField.lastIndexOf('_');
            if (featureTypeStartIndex > 0 && featureTypeEndIndex > 0) {
                fields.put("productFeatureTypeId", optionField.substring(featureTypeStartIndex, featureTypeEndIndex));
            }
        }

        GenericValue productFeatureAppl = null;
        List features = null;
        try {
            features = delegator.findByAnd("ProductFeatureAndAppl", fields, UtilMisc.toList("-fromDate"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return null;
        }

        if (features != null) {
            if (features.size() > 1) {
                features = EntityUtil.filterByDate(features);
            }
            productFeatureAppl = EntityUtil.getFirst(features);
        }

        return productFeatureAppl;
    }

    public String getRemoveFeatureTypeId(String optionField) {
        if (optionField != null) {
            int featureTypeStartIndex = optionField.indexOf('^') + 1;
            int featureTypeEndIndex = optionField.lastIndexOf('_');
            if (featureTypeStartIndex > 0 && featureTypeEndIndex > 0) {
                return optionField.substring(featureTypeStartIndex, featureTypeEndIndex);
            }
        }
        return null;
    }
    /**
     * Select an agreement
     *
     * @param agreementId
     */
    public Map selectAgreement(String agreementId) {
        Map result = null;
        GenericValue agreement = null;

        if ((this.delegator == null) || (this.dispatcher == null) || (this.cart == null)) {
            result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderDispatcherOrDelegatorOrCartArgumentIsNull",this.cart.getLocale()));
            return result;
        }

        if ((agreementId == null) || (agreementId.length() <= 0)) {
            result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderNoAgreementSpecified",this.cart.getLocale()));
            return result;
        }

        try {
            agreement = this.delegator.findByPrimaryKeyCache("Agreement",UtilMisc.toMap("agreementId", agreementId));
        } catch (GenericEntityException e) {
            Debug.logWarning(e.toString(), module);
            result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderCouldNotGetAgreement",UtilMisc.toMap("agreementId",agreementId),this.cart.getLocale()) + UtilProperties.getMessage(resource_error,"OrderError",this.cart.getLocale()) + e.getMessage());
            return result;
        }

        if (agreement == null) {
            result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderCouldNotGetAgreement",UtilMisc.toMap("agreementId",agreementId),this.cart.getLocale()));
        } else {
            // set the agreement id in the cart
            cart.setAgreementId(agreementId);
            try {
                // set the currency based on the pricing agreement
                List agreementItems = agreement.getRelated("AgreementItem", UtilMisc.toMap("agreementItemTypeId", "AGREEMENT_PRICING_PR"), null);
                if (agreementItems.size() > 0) {
                    GenericValue agreementItem = (GenericValue) agreementItems.get(0);
                    String currencyUomId = (String) agreementItem.get("currencyUomId");
                    if (UtilValidate.isNotEmpty(currencyUomId)) {
                        try {
                            cart.setCurrency(dispatcher,currencyUomId);
                        } catch (Exception ex) {
                            result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderSetCurrencyError",this.cart.getLocale()) + ex.getMessage());
                            return result;
                        }
                    }
                }
            } catch (GenericEntityException e) {
                Debug.logWarning(e.toString(), module);
                result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderCouldNotGetAgreementItemsThrough",UtilMisc.toMap("agreementId",agreementId),this.cart.getLocale()) + UtilProperties.getMessage(resource_error,"OrderError",this.cart.getLocale()) + e.getMessage());
                return result;
            }

            try {
                 // clear the existing order terms
                 cart.removeOrderTerms();
                 // set order terms based on agreement terms
                 List agreementTerms = EntityUtil.filterByDate(agreement.getRelated("AgreementTerm"));
                 if (agreementTerms.size() > 0) {
                      for (int i = 0; agreementTerms.size() > i;i++) {
                           GenericValue agreementTerm = (GenericValue) agreementTerms.get(i);
                           String termTypeId = (String) agreementTerm.get("termTypeId");
                           BigDecimal termValue = agreementTerm.getBigDecimal("termValue");
                           Long termDays = (Long) agreementTerm.get("termDays");
                           String textValue = agreementTerm.getString("textValue");
                           cart.addOrderTerm(termTypeId, termValue, termDays, textValue);
                      }
                  }
            } catch (GenericEntityException e) {
                  Debug.logWarning(e.toString(), module);
                  result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderCouldNotGetAgreementTermsThrough",UtilMisc.toMap("agreementId",agreementId),this.cart.getLocale())  + UtilProperties.getMessage(resource_error,"OrderError",this.cart.getLocale()) + e.getMessage());
                  return result;
            }
        }
        return result;
    }

    public Map setCurrency(String currencyUomId) {
        Map result = null;

        try {
            this.cart.setCurrency(this.dispatcher,currencyUomId);
            result = ServiceUtil.returnSuccess();
         } catch (Exception ex) {
             result = ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderSetCurrencyError",this.cart.getLocale()) + ex.getMessage());
             return result;
         }
        return result;
    }

    public Map addOrderTerm(String termTypeId, BigDecimal termValue, Long termDays) {
        return addOrderTerm(termTypeId, termValue, termDays, null);
    }

    public Map addOrderTerm(String termTypeId, BigDecimal termValue,Long termDays, String textValue) {
        Map result = null;
        this.cart.addOrderTerm(termTypeId,termValue,termDays,textValue);
        result = ServiceUtil.returnSuccess();
        return result;
    }

    public Map removeOrderTerm(int index) {
        Map result = null;
        this.cart.removeOrderTerm(index);
        result = ServiceUtil.returnSuccess();
        return result;
    }


    
}
