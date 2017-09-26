package org.ofbiz.webpos;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.webapp.control.LoginWorker;

public class WebPosEventsV2 {

	public static String module = WebPosEventsV2.class.getName();

	public static Map<String, Object> findTerminal(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		String productStoreId = request.getParameter("productStoreId");
		Map<String, Object> map = new HashMap<String, Object>();

		if (userLogin != null) {
			String userLoginId = userLogin.getString("userLoginId");
			GenericValue productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
			String facilityId = productStore.getString("inventoryFacilityId");
			if (facilityId != null) {
				try {
					List<GenericValue> posTerminals = delegator.findList("PosTerminal",
							EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null, null,
							null, false);
					map.put("posTerminals", posTerminals);
					map.put("success", true);
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				map.put("errmsg", "no facilityId");
				map.put("success", false);
			}

		} else {
			map.put("errmsg", "user not login");
			map.put("success", false);
		}

		return map;

	}

	public static Map<String, Object> loginStep2(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		Map<String, Object> map = new HashMap<String, Object>();

		String responseString = LoginWorker.login(request, response);

		if ("success".equals(responseString)) {
			session.removeAttribute("shoppingCart");
			session.removeAttribute("webPosSession");
			GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

			if (userLogin == null) {
				map.put("errmsg", "user not login");
				map.put("success", false);
				return map;
			}

			try {
				List<GenericValue> productStoreList = ProductStoreWorker.getProductStoreIdV2(request);
				if (productStoreList.size() == 1) {
					GenericValue productStore = EntityUtil.getFirst(productStoreList);
					String facilityId = productStore.getString("inventoryFacilityId");
					List<GenericValue> posTerminals;
					if (facilityId != null) {
						posTerminals = delegator.findList("PosTerminal",
								EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null,
								null, null, false);
					} else {
						posTerminals = delegator.findList("PosTerminal", null, null, null, null, false);
					}
					if (posTerminals.size() == 1) {
						GenericValue posTerminal = EntityUtil.getFirst(posTerminals);
						String posTerminalId = posTerminal.getString("posTerminalId");
						WebPosEvents.getWebPosSession(request, posTerminalId);
					}
					map.put("posTerminals", posTerminals);
				}
				map.put("productStoreList", productStoreList);
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			map.put("success", false);
		}
		return map;

	}

	public static Map<String, Object> loginStep3(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		Map<String, Object> map = new HashMap<String, Object>();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		String productStoreId = request.getParameter("productStoreId");
		String posTerminalId = request.getParameter("posTerminalId");
		if (userLogin != null) {
			session.removeAttribute("shoppingCart");
			session.removeAttribute("webPosSession");
			WebPosEvents.getWebPosSession(request, posTerminalId);
			map.put("success", true);
		} else {
			map.put("success", false);
		}

		return map;

	}

}