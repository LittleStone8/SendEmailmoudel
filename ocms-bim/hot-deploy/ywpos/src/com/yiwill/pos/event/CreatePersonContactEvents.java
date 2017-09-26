package com.yiwill.pos.event;

import java.math.MathContext;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import javolution.util.FastMap;

/**
 * Events used for processing checkout and orders.
 */
public class CreatePersonContactEvents {

	public static final String module = YWShopEvents.class.getName();

	public static final MathContext generalRounding = new MathContext(10);
 
	public static Map<String, Object> createPersonContact(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		Locale locale = UtilHttp.getLocale(request);

        
		String firstNameLocal = request.getParameter("firstNameLocal");
		String lastNameLocal = request.getParameter("lastNameLocal");
		String preferredCurrencyUomId = request.getParameter("preferredCurrencyUomId");
		String description = request.getParameter("description");
		String birthDate = request.getParameter("birthDate");
		
		Map<String, Object> context = new FastMap<String, Object>();
		context.put("firstNameLocal", firstNameLocal);
		context.put("lastNameLocal", lastNameLocal);
		context.put("firstName", firstNameLocal);
		context.put("lastName", lastNameLocal);
		context.put("preferredCurrencyUomId", preferredCurrencyUomId);
		context.put("description", description);
		context.put("birthDate", birthDate);

		try {

			Map<String, Object> map = new FastMap<String, Object>();

			Map<String, Object> serviceResults = dispatcher.runSync("createPerson", context);

			if (ServiceUtil.isError(serviceResults)) {
				return serviceResults;
			}

			map.put("createPerson", serviceResults);

			String contactPartyId = (String) serviceResults.get("partyId");

        serviceResults = dispatcher.runSync("createPartyRole", UtilMisc.toMap("partyId", contactPartyId, "roleTypeId", "CONTACT", "userLogin", userLogin));
			if (ServiceUtil.isError(serviceResults)) {
				return serviceResults;
			}

			map.put("createPartyRole", serviceResults);

			String primaryPhoneNumber = request.getParameter("primaryPhoneNumber");
			String primaryPhoneCountryCode = request.getParameter("primaryPhoneCountryCode");
			String primaryPhoneAreaCode = request.getParameter("primaryPhoneAreaCode");
			String primaryPhoneExtension = request.getParameter("primaryPhoneExtension");
			String primaryPhoneAskForName = request.getParameter("primaryPhoneAskForName");

			// create primary telecom number
			if (((primaryPhoneNumber != null) && !primaryPhoneNumber.equals(""))) {
              Map<String, Object> input = UtilMisc.<String, Object>toMap("partyId", contactPartyId, "userLogin", userLogin, "contactMechPurposeTypeId", "PRIMARY_PHONE");
				input.put("countryCode", primaryPhoneCountryCode);
				input.put("areaCode", primaryPhoneAreaCode);
				input.put("contactNumber", primaryPhoneNumber);
				input.put("extension", primaryPhoneExtension);
				input.put("askForName", primaryPhoneAskForName);
				serviceResults = dispatcher.runSync("createPartyTelecomNumber", input);
				if (ServiceUtil.isError(serviceResults)) {
					return serviceResults;
				}

				map.put("createPartyTelecomNumber", serviceResults);

				String primaryPhoneContactMechId = (String) serviceResults.get("contactMechId");
			}
	      /*
	       *  <override name="address1" optional="false"/>
        <override name="city" optional="false"/>
        <override name="postalCode" optional="false"/>
	       */
          String generalAddress1 = request.getParameter("generalAddress1");
          String generalAddress2 = request.getParameter("generalAddress2");
			String city = request.getParameter("city");
			String postalCode = request.getParameter("postalCode");
			String countryGeoId = request.getParameter("countryGeoId");
			String stateProvinceGeoId = request.getParameter("stateProvinceGeoId");
			String countyGeoId = request.getParameter("countyGeoId");



          if ((generalAddress1 != null) && !generalAddress1.equals("")) {
              Map<String, Object> input = UtilMisc.<String, Object>toMap("partyId", contactPartyId, "userLogin", userLogin, "contactMechPurposeTypeId", "GENERAL_LOCATION");
              input.put("toName", "");
              input.put("attnName", "");
              input.put("address1", generalAddress1);
              input.put("address2", generalAddress2);
              input.put("city", city);
              input.put("stateProvinceGeoId", "");
              input.put("postalCode", postalCode);
              input.put("postalCodeExt", "");
              input.put("countryGeoId", countryGeoId);
              input.put("stateProvinceGeoId", stateProvinceGeoId);
              input.put("countyGeoId", countyGeoId);
              serviceResults = dispatcher.runSync("createPartyPostalAddress", input);
              if (ServiceUtil.isError(serviceResults)) {
                  return serviceResults;
              }
              
              String contactMechId = (String) serviceResults.get("contactMechId");

              // also make this address the SHIPPING_LOCATION
              input = UtilMisc.<String, Object>toMap("partyId", contactPartyId, "userLogin", userLogin, "contactMechId", contactMechId, "contactMechPurposeTypeId", "SHIPPING_LOCATION");
              serviceResults = dispatcher.runSync("createPartyContactMechPurpose", input);
              if (ServiceUtil.isError(serviceResults)) {
                  return serviceResults;
              }
          }
          
          
	      
			return map;

		} catch (GenericServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ServiceUtil.returnError("Store Error");
	}

    
    
    
    
    
   
}