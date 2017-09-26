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
package com.yiwill.pos.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.store.ProductStoreWorker;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.webapp.control.LoginWorker;

import com.yiwill.pos.util.PosWorker;

/**
 * Events used for processing checkout and orders.
 */
public class YWShopLoginFindTerminalEvents {

    public static final String module = YWShopLoginFindTerminalEvents.class.getName();

    public static Map<String, Object> findTerminal(HttpServletRequest request, HttpServletResponse response) {

//		HttpSession session = request.getSession();
//		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		Delegator delegator = (Delegator) request.getAttribute("delegator");
//		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

		String productStoreId = request.getParameter("productStoreId");
		Map<String, Object> map = new HashMap<String, Object>();

		if (productStoreId != null && productStoreId.length() >0) {
			GenericValue productStore = ProductStoreWorker.getProductStore(productStoreId, delegator);
			
			if(productStore == null){
				return ServiceUtil.returnError("no productStore");
			}
			String facilityId = productStore.getString("inventoryFacilityId");
			if (facilityId != null) {
				try {
					List<GenericValue> posTerminals = delegator.findList("PosTerminal",
							EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS, facilityId), null, null,
							null, false);
					
					Map<String, Object> successMap = ServiceUtil.returnSuccess();
					successMap.put("posTerminals", posTerminals);
					return successMap;
				} catch (GenericEntityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return ServiceUtil.returnError("no facilityId");
			}
		} else {
			return ServiceUtil.returnError("productStoreId is empty");
		}
		 return ServiceUtil.returnError("unknow");

	}
   
}