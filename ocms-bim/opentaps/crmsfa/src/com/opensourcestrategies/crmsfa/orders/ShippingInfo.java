package com.opensourcestrategies.crmsfa.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.ServiceUtil;

import com.opensourcestrategies.crmsfa.orders.bean.ShippingInfoBean;

public class ShippingInfo {

	public static Map<String, Object> shippingInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orderId = request.getParameter("orderId");
			if(orderId == null || "".equals(orderId)){
				return ServiceUtil.returnError("not orderId");
			}
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			
			GenericValue OrderHeader = delegator.findOne("OrderHeader" , UtilMisc.toMap("orderId", orderId), true);
			if(OrderHeader == null){
				return ServiceUtil.returnError("cano't find order");
			}
			
			String partyId = OrderHeader.getString("buyerId");
			ShippingInfoBean shippingInfoBean = new ShippingInfoBean();
			
			Object isNeedShip = OrderHeader.get("isNeedShip");
			GenericValue person = delegator.findOne("Person" , UtilMisc.toMap("partyId", partyId), true);
			if(person != null){
				shippingInfoBean.setFirstNameLocal(person.getString("firstNameLocal"));
				shippingInfoBean.setMiddleNameLocal(person.getString("middleNameLocal"));
				shippingInfoBean.setLastNameLocal(person.getString("lastNameLocal"));
				
				if("Y".equals(isNeedShip)){
				    	    GenericValue partySupplementalData = delegator.findOne("PartySupplementalData" , UtilMisc.toMap("partyId", partyId), true);
        				    String primaryPostalAddressId = OrderHeader.getString("contactMechId");
        				    if(primaryPostalAddressId == null || primaryPostalAddressId.length() == 0){
        					primaryPostalAddressId = partySupplementalData.getString("primaryPostalAddressId");
        				    }
					if(primaryPostalAddressId != null){
						GenericValue postalAddress = delegator.findOne("PostalAddress", UtilMisc.toMap("contactMechId", primaryPostalAddressId), true);
						if(!(postalAddress == null)){
							shippingInfoBean.setAddress1(postalAddress.getString("address1"));
							shippingInfoBean.setAddress2(postalAddress.getString("address2"));
							GenericValue geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", postalAddress.getString("countyGeoId")), true);
							if(geo != null){
								shippingInfoBean.setCountyGeo(geo.getString("geoName"));
							}
							geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", postalAddress.getString("stateProvinceGeoId")), true);
							if(geo != null){
								shippingInfoBean.setStateProvinceGeo(geo.getString("geoName"));
							}
							geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", postalAddress.getString("city")), true);
							if(geo != null){
								shippingInfoBean.setCity(geo.getString("geoName"));
							}
							geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", postalAddress.getString("countryGeoId")), true);
							if(geo != null){
								shippingInfoBean.setCountryGeo(geo.getString("geoName"));
							}
						}
					}
					String primaryTelecomNumberId = partySupplementalData.getString("primaryTelecomNumberId");
					if(primaryTelecomNumberId != null){
						GenericValue telecomNumber = delegator.findOne("TelecomNumber", UtilMisc.toMap("contactMechId", primaryTelecomNumberId), true);
						if(telecomNumber != null){
							shippingInfoBean.setContactNumber(telecomNumber.getString("contactNumber"));
						}
					}
					List<GenericValue> orderItemShipGrpInvResList = delegator.findByAnd("OrderItemShipGrpInvRes", UtilMisc.toMap("orderId", orderId));
					if(orderItemShipGrpInvResList.size() > 0){
						GenericValue orderItemShipGrpInvRes = EntityUtil.getFirst(orderItemShipGrpInvResList);
						shippingInfoBean.setPromisedDatetime(orderItemShipGrpInvRes.getString("promisedDatetime"));
					}
				}
			}
			
			List<GenericValue> orderItemList = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId));
//			BigDecimal totalAdjustments = new BigDecimal(0);
//			BigDecimal unitPrice = new BigDecimal(0);
//			List<String> adjustmentsList = new ArrayList<String>();
			/*for (GenericValue orderItem : orderItemList) {
				String price = orderItem.getString("unitPrice");
				String unitRecurringPrice = orderItem.getString("unitRecurringPrice");
				String quantity = orderItem.getString("quantity");
				if(price == null || "".equals(price)){
					price = "0";
				}
				if(unitRecurringPrice == null || "".equals(unitRecurringPrice)){
					unitRecurringPrice = "0";
				}
				if(quantity == null || "".equals(quantity)){
					quantity = "0";
				}
				totalAdjustments = totalAdjustments.add(new BigDecimal(unitRecurringPrice).subtract(new BigDecimal(price)).multiply(new BigDecimal(quantity)));   
				String str = new BigDecimal(unitRecurringPrice).subtract(new BigDecimal(price)).toString();
				str = str.substring(0, str.indexOf("."));
				if(new BigDecimal(unitRecurringPrice).subtract(new BigDecimal(price)).compareTo(new BigDecimal("0")) == 1){
					adjustmentsList.add("-" + str);
				}else if(new BigDecimal(unitRecurringPrice).subtract(new BigDecimal(price)).compareTo(new BigDecimal("0")) == -1){
					adjustmentsList.add("+" + str);
				}else{
					adjustmentsList.add("0");
				}
				
				
			}*/
			
			double totalAdjustments = 0;
			double unitPrice = 0;
			List<Double> adjustmentsList = new ArrayList<Double>();
			for (GenericValue orderItem : orderItemList) {
				String price = orderItem.getString("unitPrice");
				String unitRecurringPrice = orderItem.getString("unitRecurringPrice");
				String quantity = orderItem.getString("quantity");
				if(price == null || "".equals(price)){
					price = "0";
				}
				if(unitRecurringPrice == null || "".equals(unitRecurringPrice)){
					unitRecurringPrice = "0";
				}
				if(quantity == null || "".equals(quantity)){
					quantity = "0";
				}
				totalAdjustments = totalAdjustments + (Double.parseDouble(price) - Double.parseDouble(unitRecurringPrice)) * Double.parseDouble(quantity);
				adjustmentsList.add(Double.parseDouble(price) - Double.parseDouble(unitRecurringPrice));
			}
			
			
			
			shippingInfoBean.setAdjustmentsList(adjustmentsList);
			shippingInfoBean.setCustomerPartyId(OrderHeader.getString("buyerId"));
			shippingInfoBean.setCurrencyUom(OrderHeader.getString("currencyUom"));
			String total = totalAdjustments + "";
			shippingInfoBean.setTotalAdjustments(total);
			
			Map<String, Object> resuletMap = ServiceUtil.returnSuccess("query success");
			resuletMap.put("shippingInfo", shippingInfoBean);
			return resuletMap;
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("error");
		}
		
	}
	
	
	
}
