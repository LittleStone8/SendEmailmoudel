package com.yiwill.pos.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.common.CommonWorkers;
import org.ofbiz.common.DataModelConstants;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.order.shoppingcart.ShoppingCart;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;

import com.yiwill.pos.shop.YiWillCart;
import com.yiwill.pos.shop.YiWillCart.CartShipInfo;
import com.yiwill.pos.shop.YiWillCheckOutHelper;
import com.yiwill.pos.shop.YiWillProductPromoWorker;
import com.yiwill.pos.shop.YiWillShippingEvents;

import javolution.util.FastList;
import javolution.util.FastMap;

public class YiWillStoreCartServices {

	public static final String module = YiWillStoreCartServices.class.getName();

	public static final String resource_error = "OrderErrorUiLabels";

	private static String getNextOrderId(Delegator delegator, YiWillCart cart) {
		try {

			String productStoreId = cart.getProductStoreId();
			if (productStoreId != null) {
				GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId", productStoreId), true);
				String orgPartyId = productStore.getString("payToPartyId");
				GenericValue partyAcctgPreference = null;
				Long orderId = 0l;
				String orderSequenceEnumId = null;
				if (orgPartyId != null) {
					partyAcctgPreference = delegator.findByPrimaryKeyCache("PartyAcctgPreference",
							UtilMisc.toMap("partyId", orgPartyId));
					orderSequenceEnumId = partyAcctgPreference.getString("orderSequenceEnumId");
				}
//				if (orderSequenceEnumId != null && orderSequenceEnumId.equals("ODRSQ_ENF_SEQ")) {
//					String lastOrderNumber = partyAcctgPreference.getString("lastOrderNumber");
//
//					if (lastOrderNumber != null && lastOrderNumber.length() > 0) {
//						Long lastOrderNumberLong = Long.valueOf(lastOrderNumber);
//						orderId = lastOrderNumberLong++;
//					} else {
//						orderId = 1l;
//						partyAcctgPreference.set("lastOrderNumber", orderId);
//					}
//					partyAcctgPreference.store();
//				} else {
					orderId = delegator.getNextSeqIdLong("OrderHeader");
//				}
				String orderNumberPrefix = productStore.getString("orderNumberPrefix") != null
						? productStore.getString("orderNumberPrefix") : "";
				String orderIdPrefix = partyAcctgPreference.getString("orderIdPrefix") != null
						? partyAcctgPreference.getString("orderIdPrefix") : "";
				return orderNumberPrefix + orderIdPrefix + orderId;
			}
		} catch (GenericEntityException e) {
		}
		return delegator.getNextSeqId("OrderHeader");
	}

	public static Map<String, Object> saveUpdatedCartToOrder(DispatchContext dctx, Map<String, Object> context)
			throws GeneralException {

		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dctx.getDelegator();

		GenericValue userLogin = (GenericValue) context.get("userLogin");
		YiWillCart cart = (YiWillCart) context.get("cart");

		Map<String, Object> result = null;
		try {
			saveUpdatedCartToOrder(dispatcher, delegator, cart, userLogin);
			result = ServiceUtil.returnSuccess();
			// result.put("shoppingCart", cart);
		} catch (GeneralException e) {
			Debug.logError(e, module);
			result = ServiceUtil.returnError(e.getMessage());
		}
		result.put("cart", cart);
		return result;
	}

	private static Map<String, Object> saveUpdatedCartToOrder(LocalDispatcher dispatcher, Delegator delegator,
			YiWillCart cart, GenericValue userLogin) throws GeneralException {
		// get/set the shipping estimates. if it's a SALES ORDER, then return an
		// error if there are no ship estimates

		
		
		
		if (cart.isReadOnlyCart()) {
			return ServiceUtil.returnError("isReadOnlyCart");
		}

		java.sql.Timestamp nowTimestamp = UtilDateTime.nowTimestamp();

		YiWillCheckOutHelper coh = new YiWillCheckOutHelper(delegator, dispatcher, cart);

		String initialStatus = "ORDER_CREATED";

		String orderId = cart.getOrderId();
		GenericValue orderHeader = null;
		if (orderId == null) {
			orderId = getNextOrderId(delegator, cart);
			cart.setOrderId(orderId);
			Map orderHeaderMap = UtilMisc.toMap("orderId", orderId, "orderTypeId", cart.getOrderType(), "orderDate",
					cart.getOrderDate(), "entryDate", nowTimestamp, "statusId", initialStatus, "billingAccountId",
					cart.getBillingAccountId());
			orderHeaderMap.put("orderName", cart.getOrderName());
			orderHeaderMap.put("needsInventoryIssuance", "Y");

			orderHeader = delegator.makeValue("OrderHeader", orderHeaderMap);
			
			
			try {
				delegator.create(orderHeader);
			} catch (GenericEntityException e) {
				Debug.logError(e, "Cannot create OrderHeader entity; problems with insert", module);
				return ServiceUtil.returnError("OrderOrderCreationFailedPleaseNotifyCustomerService");
			}
		} else {
			orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), true);
		}
		
		
		GenericValue buyerUser = cart.getUserLogin();
		if(buyerUser != null){
			orderHeader.set("buyerId", buyerUser.getString("partyId"));
			orderHeader.set("billToPartyId", buyerUser.getString("partyId"));
		}
		
		GenericValue salesUser = cart.getSalesUser();
		
		if(salesUser != null){
			orderHeader.set("salesId", salesUser.getString("partyId"));
			orderHeader.set("billFromPartyId", salesUser.getString("partyId"));
		}
		
		
		String contactMechId = cart.getShippingContactMechId();
		
		if(contactMechId !=null && contactMechId.length() >0){
		    orderHeader.set("contactMechId", contactMechId);
		}
		
		orderHeader.set("salesChannelEnumId", cart.getChannelType());
		orderHeader.set("currencyUom", cart.getCurrency());
		orderHeader.set("firstAttemptOrderId", cart.getFirstAttemptOrderId());
		orderHeader.set("grandTotal", cart.getGrandTotal());
		// orderHeader.set("visitId",cart. context.get("visitId"));
		orderHeader.set("internalCode", cart.getInternalCode());
		orderHeader.set("externalId", cart.getExternalId());
		orderHeader.set("originFacilityId", cart.getFacilityId());
		orderHeader.set("productStoreId", cart.getProductStoreId());
		orderHeader.set("productStoreGroupId", cart.getProductStoreGroupId());
		orderHeader.set("transactionId", cart.getTransactionId());
		orderHeader.set("terminalId", cart.getTerminalId());
		orderHeader.set("autoOrderShoppingListId", cart.getAutoOrderShoppingListId());
		orderHeader.set("webSiteId", cart.getWebSiteId());
				
		orderHeader.set("isNeedShip", cart.getIsNeedShip());
		
		
		Locale locale = cart.getLocale();

		Map context = cart.makeCartMap(dispatcher, true);

		try {
			coh.calcAndAddTax();
		} catch (GeneralException e) {
			Debug.logError(e, module);
			throw new GeneralException(e.getMessage());
		}

		List<Map> modifiedItems = FastList.newInstance();
		List<GenericValue> toStore = new LinkedList<GenericValue>();
		List<GenericValue> toAddList = new ArrayList<GenericValue>();

		// toAddList.addAll(cart.makeAllAdjustments());

		cart.clearAllPromotionAdjustments();
		YiWillProductPromoWorker.doPromotions(cart, dispatcher);

		// validate the payment methods
		// Map validateResp = coh.validatePaymentMethods();
		// if (ServiceUtil.isError(validateResp)) {
		// throw new
		// GeneralException(ServiceUtil.getErrorMessage(validateResp));
		// }

		toStore.add(orderHeader);
		toStore.addAll(cart.makeAllAdjustments());
		
		// String shipGroupSeqId = null;
		// long groupIndex = cart.getShipInfoSize();
		// List orderAdjustments = new ArrayList();
		// for (long itr = 1; itr <= groupIndex; itr++) {
		// shipGroupSeqId = UtilFormatOut.formatPaddedNumber(itr, 5);
		// List<GenericValue> removeList = new ArrayList<GenericValue>();
		// for (GenericValue stored: (List<GenericValue>)toStore) {
		// if ("OrderAdjustment".equals(stored.getEntityName())) {
		// if (("SHIPPING_CHARGES".equals(stored.get("orderAdjustmentTypeId"))
		// ||
		// "SALES_TAX".equals(stored.get("orderAdjustmentTypeId"))) &&
		// stored.get("orderId").equals(orderId) &&
		// stored.get("shipGroupSeqId").equals(shipGroupSeqId)) {
		// // Removing objects from toStore list for old Shipping and Handling
		// Charges Adjustment and Sales Tax Adjustment.
		// removeList.add(stored);
		// }
		// if (stored.get("comments") != null &&
		// ((String)stored.get("comments")).startsWith("Added manually by")) {
		// // Removing objects from toStore list for Manually added Adjustment.
		// removeList.add(stored);
		// }
		// }
		// }
		// toStore.removeAll(removeList);
		// }

		// for (GenericValue toAdd: (List<GenericValue>)toAddList) {
		// if ("OrderAdjustment".equals(toAdd.getEntityName())) {
		// if (toAdd.get("comments") != null &&
		// ((String)toAdd.get("comments")).startsWith("Added manually by") &&
		// (("PROMOTION_ADJUSTMENT".equals(toAdd.get("orderAdjustmentTypeId")))
		// ||
		// ("SHIPPING_CHARGES".equals(toAdd.get("orderAdjustmentTypeId"))) ||
		// ("SALES_TAX".equals(toAdd.get("orderAdjustmentTypeId"))))) {
		// toStore.add(toAdd);
		// }
		// }
		// }

		// Creating objects for New Shipping and Handling Charges Adjustment and
		// Sales Tax Adjustment
		// toStore.addAll(cart.makeAllShipGroupInfos());
		toStore.addAll(cart.makeAllOrderPaymentInfos(dispatcher));
		toStore.addAll(cart.makeAllOrderItemAttributes(orderId, ShoppingCart.FILLED_ONLY));

		// get the empty order item atrributes from the cart and remove them
		List<GenericValue> toRemove = FastList.newInstance();
		toRemove.addAll(cart.makeAllOrderItemAttributes(orderId, ShoppingCart.EMPTY_ONLY));

		// set the orderId & other information on all new value objects
		List dropShipGroupIds = FastList.newInstance(); // this list will
														// contain the ids of
		List<GenericValue> oldOrderItemList = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId));

		List<GenericValue> orderItemList = cart.makeOrderItems(dispatcher);
		
		if (oldOrderItemList != null) {
			for (GenericValue oldOrderItem : oldOrderItemList) {
				String oldorderItemSeqId = oldOrderItem.getString("orderItemSeqId");
				boolean type = true;
				for (GenericValue orderItem : orderItemList) {
					String orderItemSeqId = orderItem.getString("orderItemSeqId");
					if (orderItemSeqId.equals(oldorderItemSeqId)) {
						type = false;
					}
				}
				if (type) {
				    List<GenericValue> oldOrderItemShipGroupAssocList = delegator.findByAnd("OrderItemShipGroupAssoc", UtilMisc.toMap("orderId", orderId));
				    	toRemove.addAll(oldOrderItemShipGroupAssocList);
					toRemove.add(oldOrderItem);
				}
			}
		}
		
		List<GenericValue> orderPaymentList = delegator.findByAnd("OrderPaymentPreference", UtilMisc.toMap("orderId", orderId));
		for (GenericValue orderPayment : orderPaymentList) {
		    System.out.println("@@@@@  "+ orderPayment.getString("orderPaymentPreferenceId"));
		    toRemove.add(orderPayment);
		}

		
		//鏌ヨ浠锋牸姣旂巼
		Map<String, Object> scontext = new HashMap<String, Object>();
		Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", scontext);
		String egateeCost = (String) uomInfo.get("egateeCost");
		String specialCost = (String) uomInfo.get("specialCost");
		String retailCost = (String) uomInfo.get("retailCost");
		
		for (GenericValue orderItem : orderItemList) {

			Map priceContext = FastMap.newInstance();
			priceContext.put("currencyUomId", cart.getCurrency());
			priceContext.put("productStoreGroupId", cart.getProductStoreGroupId());
			priceContext.put("quantity", orderItem.get("quantity"));
			// priceContext.put("amount", this.getSelectedAmount());
			GenericValue product = delegator.findByPrimaryKeyCache("Product",
					UtilMisc.toMap("productId", orderItem.getString("productId")));
			priceContext.put("product", product);
			// priceContext.put("prodCatalogId", this.getProdCatalogId());
			priceContext.put("webSiteId", cart.getWebSiteId());
			priceContext.put("productStoreId", cart.getProductStoreId());
			// priceContext.put("agreementId", cart.getAgreementId());
			priceContext.put("productPricePurposeId", "PURCHASE");
			priceContext.put("checkIncludeVat", "Y");
			Map priceResult = dispatcher.runSync("calculateProductPrice", priceContext);

			if (ServiceUtil.isError(priceResult)) {
				return priceResult;
			}

			// GenericValue unitPresetCostG = delegator.findOne("ProductPrice",
			// UtilMisc.toMap("productStoreGroupId",
			// "DEFAULT_COST_PRICE","currencyUomId",
			// cart.getCurrency(),"productId",
			// orderItem.getString("productId")), false);
			// GenericValue unitSPresetCostG = delegator.findOne("ProductPrice",
			// UtilMisc.toMap("productStoreGroupId",
			// "SPECIAL_COST_PRICE","currencyUomId",
			// cart.getCurrency(),"productId",
			// orderItem.getString("productId")), false);

			List<GenericValue> unitPresetCostGList = delegator.findByAnd("ProductPrice",
					UtilMisc.toMap("productStoreGroupId", "DEFAULT_COST_PRICE", "currencyUomId", cart.getCurrency(),
							"productId", orderItem.getString("productId")));
			
			List<GenericValue> unitSPresetCostGList = delegator.findByAnd("ProductPrice",
					UtilMisc.toMap("productStoreGroupId", "SPECIAL_COST_PRICE", "currencyUomId", cart.getCurrency(),
							"productId", orderItem.getString("productId")));

			List<GenericValue> unitEPresetCostGList = delegator.findByAnd("ProductPrice",
				UtilMisc.toMap("productStoreGroupId", "EGATEE_COST_PRICE", "currencyUomId", cart.getCurrency(),
						"productId", orderItem.getString("productId")));
			
			List<GenericValue> unitRPresetCostGList = delegator.findByAnd("ProductPrice",
				UtilMisc.toMap("productStoreGroupId", "RETAIL_COST_PRICE", "currencyUomId", cart.getCurrency(),
					"productId", orderItem.getString("productId")));
			
//			Map<String, Object> context = new HashMap<String, Object>();
//			Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
//			String egateeCost = (String) uomInfo.get("egateeCost");
//			String specialCost = (String) uomInfo.get("specialCost");
//			String retailCost = (String) uomInfo.get("retailCost");
			
			BigDecimal unitPresetCostPrice = new BigDecimal(0);
			if (unitPresetCostGList != null && unitPresetCostGList.size() > 0) {
				GenericValue unitPresetCostG = EntityUtil.getFirst(unitPresetCostGList);
				unitPresetCostPrice = unitPresetCostG.getBigDecimal("price");
				orderItem.set("unitPresetCost", unitPresetCostPrice);
				orderItem.set("defaultCostPrice", unitPresetCostPrice);
			}
			
			if (unitRPresetCostGList != null && unitRPresetCostGList.size() > 0) {
				GenericValue unitRPresetCostG = EntityUtil.getFirst(unitRPresetCostGList);
				orderItem.set("retailCostPrice", unitRPresetCostG.get("price"));
			}else{
				BigDecimal retailCostPrice = unitPresetCostPrice.multiply(new BigDecimal(retailCost));
				orderItem.set("retailCostPrice", retailCostPrice);
			}
			
			if (unitEPresetCostGList != null && unitEPresetCostGList.size() > 0) {
				GenericValue unitEPresetCostG = EntityUtil.getFirst(unitEPresetCostGList);
				orderItem.set("egateeCostPrice", unitEPresetCostG.get("price"));
			}else{
				BigDecimal egateeCostPrice = unitPresetCostPrice.multiply(new BigDecimal(egateeCost));
				orderItem.set("egateeCostPrice", egateeCostPrice);
			}

			if (unitSPresetCostGList != null && unitSPresetCostGList.size() > 0) {
				GenericValue unitSPresetCostG = EntityUtil.getFirst(unitSPresetCostGList);
				BigDecimal unitSPresetCostPrice = unitSPresetCostG.getBigDecimal("price");
				orderItem.set("unitSPresetCost",unitSPresetCostPrice );
				orderItem.set("specialCostPrice", new BigDecimal(unitSPresetCostPrice.toString()));
			}else{
				BigDecimal specialCostPrice = unitPresetCostPrice.multiply(new BigDecimal(specialCost));
				orderItem.set("unitSPresetCost", specialCostPrice);
				orderItem.set("specialCostPrice", specialCostPrice);
			}
			
			

			orderItem.set("currentPrice", priceResult.get("basePrice"));
		}

		toStore.addAll(orderItemList);
		
		List<GenericValue> orderContactMechList = cart.makeAllOrderContactMechs();
		List<GenericValue> orderItemShipGroupList = cart.makeAllShipGroupInfos();
		toStore.addAll(orderContactMechList);
		toStore.addAll(orderItemShipGroupList);

		Iterator tsi = toStore.iterator();
		// toStore.addAll(cart.makeOrderItems());

		while (tsi.hasNext()) {
			GenericValue valueObj = (GenericValue) tsi.next();
			valueObj.set("orderId", orderId);
			if ("OrderItemShipGroup".equals(valueObj.getEntityName())) {
				// ship group
				if (valueObj.get("carrierRoleTypeId") == null) {
					valueObj.set("carrierRoleTypeId", "CARRIER");
				}
				if (!UtilValidate.isEmpty(valueObj.get("supplierPartyId"))) {
					dropShipGroupIds.add(valueObj.getString("shipGroupSeqId"));
				}
			} else if ("OrderAdjustment".equals(valueObj.getEntityName())) {
				// shipping / tax adjustment(s)
				if (UtilValidate.isEmpty(valueObj.get("orderItemSeqId"))) {
					valueObj.set("orderItemSeqId", DataModelConstants.SEQ_ID_NA);
				}
				valueObj.set("orderAdjustmentId", delegator.getNextSeqId("OrderAdjustment"));
				valueObj.set("createdDate", UtilDateTime.nowTimestamp());
				valueObj.set("createdByUserLogin", userLogin.getString("userLoginId"));
			} else if ("OrderPaymentPreference".equals(valueObj.getEntityName())) {
				if (valueObj.get("orderPaymentPreferenceId") == null) {
					valueObj.set("orderPaymentPreferenceId", delegator.getNextSeqId("OrderPaymentPreference"));
					valueObj.set("createdDate", UtilDateTime.nowTimestamp());
					valueObj.set("createdByUserLogin", userLogin.getString("userLoginId"));
				}
				if (valueObj.get("statusId") == null) {
					valueObj.set("statusId", "PAYMENT_NOT_RECEIVED");
				}
			} else if ("OrderItem".equals(valueObj.getEntityName())) {
				// ignore promotion items. They are added/canceled automatically
				if ("Y".equals(valueObj.getString("isPromo"))) {
					continue;
				}

				GenericValue oldOrderItem = null;
				try {
					oldOrderItem = delegator.findByPrimaryKey("OrderItem", UtilMisc.toMap("orderId",
							valueObj.getString("orderId"), "orderItemSeqId", valueObj.getString("orderItemSeqId")));
				} catch (GenericEntityException e) {
					Debug.logError(e, module);
					throw new GeneralException(e.getMessage());
				}
				if (UtilValidate.isNotEmpty(oldOrderItem)) {
					String oldItemDescription = oldOrderItem.getString("itemDescription") != null
							? oldOrderItem.getString("itemDescription") : "";
					BigDecimal oldQuantity = oldOrderItem.getBigDecimal("quantity") != null
							? oldOrderItem.getBigDecimal("quantity") : BigDecimal.ZERO;
					BigDecimal oldUnitPrice = oldOrderItem.getBigDecimal("unitPrice") != null
							? oldOrderItem.getBigDecimal("unitPrice") : BigDecimal.ZERO;

					boolean changeFound = false;
					Map modifiedItem = FastMap.newInstance();
					if (!oldItemDescription.equals(valueObj.getString("itemDescription"))) {
						modifiedItem.put("itemDescription", oldItemDescription);
						changeFound = true;
					}
					BigDecimal quantityDif = valueObj.getBigDecimal("quantity").subtract(oldQuantity);
					BigDecimal unitPriceDif = valueObj.getBigDecimal("unitPrice").subtract(oldUnitPrice);
					if (unitPriceDif.compareTo(BigDecimal.ZERO) != 0 || quantityDif.compareTo(BigDecimal.ZERO) != 0) {
						modifiedItem.put("quantity", quantityDif);
						modifiedItem.put("unitPrice", unitPriceDif);
						changeFound = true;
					}
					if (changeFound) {
						modifiedItem.put("orderId", valueObj.getString("orderId"));
						modifiedItem.put("orderItemSeqId", valueObj.getString("orderItemSeqId"));
						modifiedItem.put("changeTypeEnumId", "ODR_ITM_UPDATE");
						modifiedItems.add(modifiedItem);
					}
				} else {
					Map appendedItem = FastMap.newInstance();
					appendedItem.put("orderId", valueObj.getString("orderId"));
					appendedItem.put("orderItemSeqId", valueObj.getString("orderItemSeqId"));
					appendedItem.put("quantity", valueObj.getBigDecimal("quantity"));
					appendedItem.put("changeTypeEnumId", "ODR_ITM_APPEND");
					modifiedItems.add(appendedItem);
				}
			}
		}
		Debug.log("To Store Contains: " + toStore, module);

		// remove any order item attributes that were set to empty
		try {
			delegator.removeAll(toRemove, true);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			throw new GeneralException(e.getMessage());
		}

		// store the new items/adjustments/order item attributes
		try {
			delegator.storeAll(toStore);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			throw new GeneralException(e.getMessage());
		}

		// store the OrderItemChange
		if (UtilValidate.isNotEmpty(modifiedItems)) {
			for (Map modifiendItem : modifiedItems) {
				Map orderItemChangeMap = UtilMisc.toMap("orderId", modifiendItem.get("orderId"), "orderItemSeqId",
						modifiendItem.get("orderItemSeqId"), "itemDescription", modifiendItem.get("itemDescription"),
						"quantity", modifiendItem.get("quantity"), "unitPrice", modifiendItem.get("unitPrice"),
						"changeTypeEnumId", modifiendItem.get("changeTypeEnumId"), "reasonEnumId",
						modifiendItem.get("reasonEnumId"), "changeComments", modifiendItem.get("changeComments"));

				orderHeader = delegator.makeValue("OrderItemChange", orderItemChangeMap);
				orderHeader.set("changeDatetime", nowTimestamp);
				orderHeader.set("changeUserLogin", userLogin.getString("userLoginId"));

				String orderItemChangeId = delegator.getNextSeqId("OrderItemChange");
				orderHeader.set("orderItemChangeId", orderItemChangeId);
				orderHeader.create();
			}
		}

		// make the order item object map & the ship group assoc list
		// List orderItemShipGroupAssoc = new LinkedList();
		// Map itemValuesBySeqId = new HashMap();
		// Iterator oii = toStore.iterator();
		// while (oii.hasNext()) {
		// GenericValue v = (GenericValue) oii.next();
		// if ("OrderItem".equals(v.getEntityName())) {
		// itemValuesBySeqId.put(v.getString("orderItemSeqId"), v);
		// } else if ("OrderItemShipGroupAssoc".equals(v.getEntityName())) {
		// orderItemShipGroupAssoc.add(v);
		// }
		// }
		// reserve the inventory
		// String productStoreId = cart.getProductStoreId();
		// String orderTypeId = cart.getOrderType();
		// List resErrorMessages = new LinkedList();
		// try {
		// Debug.log("Calling reserve inventory...", module);
		// reserveInventory(delegator, dispatcher, userLogin, locale,
		// orderItemShipGroupAssoc, dropShipGroupIds, itemValuesBySeqId,
		// orderTypeId, productStoreId, resErrorMessages);
		// } catch (GeneralException e) {
		// Debug.logError(e, module);
		// throw new GeneralException(e.getMessage());
		// }
		//
		// if (resErrorMessages.size() > 0) {
		// throw new
		// GeneralException(ServiceUtil.getErrorMessage(ServiceUtil.returnError(resErrorMessages)));
		// }
		Map map = ServiceUtil.returnSuccess();
		map.put("cart", cart);
		return map;
	}

	// public static void reserveInventory(Delegator delegator, LocalDispatcher
	// dispatcher, GenericValue userLogin, Locale locale, List
	// orderItemShipGroupInfo, List dropShipGroupIds, Map itemValuesBySeqId,
	// String orderTypeId, String productStoreId, List resErrorMessages) throws
	// GeneralException {

	public static Map<String, Object> reserveInventory(DispatchContext dctx, Map<String, Object> context)
			throws GeneralException {

		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dctx.getDelegator();
		GenericValue userLogin = (GenericValue) context.get("userLogin");

		YiWillCart cart = (YiWillCart) context.get("cart");

		String orderTypeId = cart.getOrderType();
		String facilityId = cart.getFacilityId();
		String productStoreId = cart.getProductStoreId();
		
		String status = cart.getOrderStatusId();
		
		String orderId = cart.getOrderId();
		Boolean isNeedShip = cart.getIsNeedShip();

		
		if(orderId == null){
			return ServiceUtil.returnError("orderId empty");
		}
		
		
		boolean isImmediatelyFulfilled = false;
		GenericValue productStore = null;
		if (UtilValidate.isNotEmpty(productStoreId)) {
			try {
				productStore = delegator.findByPrimaryKeyCache("ProductStore",
						UtilMisc.toMap("productStoreId", productStoreId));
			} catch (GenericEntityException e) {
			}
		}
		if (productStore != null) {
			isImmediatelyFulfilled = "Y".equals(productStore.getString("isImmediatelyFulfilled"));
		}
//		boolean reserveInventory = ("SALES_ORDER".equals(orderTypeId));
//		if (reserveInventory && isImmediatelyFulfilled) {
		boolean reserveInventory = false;
		if (isImmediatelyFulfilled) {
			reserveInventory = isImmediatelyFulfilled;
		}
		List<GenericValue> orderItemList = cart.makeOrderItems(dispatcher);
		for (GenericValue orderItem : orderItemList) {
			String itemStatus = orderItem.getString("statusId");
			if ("ITEM_REJECTED".equals(itemStatus) || "ITEM_CANCELLED".equals(itemStatus) || "ITEM_COMPLETED".equals(itemStatus)) {
				Debug.logInfo("Order item [" + orderItem.getString("orderId")+" / "+ orderItem.getString("orderItemSeqId") + "] is not in a proper status for reservation",module);
				continue;
			}
			if (UtilValidate.isNotEmpty(orderItem.getString("productId")) && !"RENTAL_ORDER_ITEM".equals(orderItem.getString("orderItemTypeId"))) {
				GenericValue product = orderItem.getRelatedOne("Product");
				if (product == null) {
					Debug.logError("Error when looking up product in reserveInventory service", module);
					continue;
				}
				if (reserveInventory) {
					// reserve the product
					Map reserveInput = new HashMap();
					reserveInput.put("productStoreId", productStoreId);
					reserveInput.put("productId", orderItem.getString("productId"));
					reserveInput.put("orderId", orderId);
					reserveInput.put("orderItemSeqId", orderItem.getString("orderItemSeqId"));
					// reserveInput.put("shipGroupSeqId",
					// orderItemShipGroupAssoc.getString("shipGroupSeqId"));
//					reserveInput.put("facilityId", facilityId);
//					reserveInput.put("quantity", orderItem.getString("quantity"));
					reserveInput.put("isNeedShip", isNeedShip);
					reserveInput.put("quantity", orderItem.getBigDecimal("quantity"));
					reserveInput.put("userLogin", userLogin);
					reserveInput.put("imei", orderItem.getString("imei"));
					reserveInput.put("status", status);
					Map reserveResult = dispatcher.runSync("yiWillReserveStoreInventory", reserveInput);
					
					if (ServiceUtil.isError(reserveResult)) {
						return reserveResult;
					}
				}
			}
			
		}
		
		Map<String, Object> outMap = dispatcher.runSync("storeYiWillOrder",
			UtilMisc.<String, Object>toMap("cart", cart, "userLogin", userLogin));
		if (ServiceUtil.isError(outMap)) {
			return outMap;
		}
		
		
		
		java.sql.Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
		
		GenericValue orderPaymentPreference = delegator.makeValidValue("OrderPaymentPreference");
		orderPaymentPreference.set("orderPaymentPreferenceId", delegator.getNextSeqId("OrderPaymentPreference"));
		orderPaymentPreference.set("orderId", cart.getOrderId());
		orderPaymentPreference.set("statusId", "PAYMENT_CHANGE");
		orderPaymentPreference.set("paymentMethodTypeId", cart.getPaymentMethodTypeIds().get(0).toString());
		orderPaymentPreference.set("maxAmount",cart.getPaymentTotal().subtract(cart.getGrandTotal()).negate());
		orderPaymentPreference.create();
		
		
		GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), true);
		orderHeader.set("captcha",new java.util.Random().nextInt(899999)+100000+"");
		orderHeader.set("statusId", cart.getOrderStatusId());
		
		
		
		System.out.println(cart.getOrderDate());
		if(cart.getOrderDate() != null){//如果属于补录的订单
			orderHeader.set("orderDate", cart.getOrderDate());
			orderHeader.set("isFixOrder", "Y");
		}else{
			orderHeader.set("orderDate", nowTimestamp);
		}
		
		orderHeader.store();
		Map<String, Object> map = ServiceUtil.returnSuccess();
		map.put("cart", cart); 
		return map;
	}

	public static String getProductName(GenericValue product, GenericValue orderItem) {
		if (UtilValidate.isNotEmpty(product.getString("productName"))) {
			return product.getString("productName");
		} else {
			return orderItem.getString("itemDescription");
		}
	}
}
