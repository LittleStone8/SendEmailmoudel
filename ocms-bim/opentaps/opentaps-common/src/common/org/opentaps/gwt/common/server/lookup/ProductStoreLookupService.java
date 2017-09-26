/*
 * Copyright (c) Open Source Strategies, Inc.
 *
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opentaps.gwt.common.server.lookup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.opentaps.base.entities.ProductStore;
import org.opentaps.foundation.entity.EntityInterface;
import org.opentaps.foundation.infrastructure.InfrastructureException;
import org.opentaps.gwt.common.client.lookup.configuration.ProductStoreLookupConfiguration;
import org.opentaps.gwt.common.server.HttpInputProvider;
import org.opentaps.gwt.common.server.InputProviderInterface;

import javolution.util.FastList;
import javolution.util.FastSet;

/**
 * The RPC service used to populate Product Store autocompleters widgets.
 */
public class ProductStoreLookupService extends EntityLookupAndSuggestService {

    protected ProductStoreLookupService(InputProviderInterface provider) {
        super(provider,
              Arrays.asList(ProductStoreLookupConfiguration.OUT_PRODUCT_STORE_ID,
                      ProductStoreLookupConfiguration.OUT_STORE_NAME));
    }

    /**
     * AJAX event to suggest Case Priority.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the resulting JSON response
     * @throws InfrastructureException if an error occurs
     */
    public static String suggestProductStores(HttpServletRequest request, HttpServletResponse response) throws InfrastructureException {
        InputProviderInterface provider = new HttpInputProvider(request);
        JsonResponse json = new JsonResponse(response);
        ProductStoreLookupService service = new ProductStoreLookupService(provider);
        //获取登录信息
        GenericValue userLoginGV = (GenericValue) request.getSession().getAttribute("userLogin");
        String partyId = (String) userLoginGV.get("partyId");
        if(request.getRequestURI().contains("/warehouse/control")){
        	service.suggestProductStores(request);
        }else if(partyId!= null && !partyId.equals("admin")){
        	service.suggestProductStores(request,partyId);
        }else{
        	service.suggestProductStores();
        }
        return json.makeSuggestResponse(ProductStoreLookupConfiguration.OUT_PRODUCT_STORE_ID, service);
    }

    private List<ProductStore> suggestProductStores(HttpServletRequest request, String partyId) {
    	Delegator delegator = (Delegator) request.getAttribute("delegator");	
    	List<EntityExpr> expressions = new ArrayList<EntityExpr>();
		expressions.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
		expressions.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "MANAGER"));
		expressions.add(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null));
		EntityConditionList<EntityExpr> exprList = EntityCondition.makeCondition(expressions, EntityOperator.AND);
		
		Set<String> productStoreId = FastSet.newInstance();
		// party所属店铺
		try {
			List<GenericValue> findList = delegator.findList("ProductStoreRole", exprList, null, null, null, false);
			for (GenericValue genericValue : findList) {
				productStoreId.add(genericValue.getString("productStoreId"));
			}
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return findList(ProductStore.class, EntityCondition.makeCondition("productStoreId", EntityOperator.IN, productStoreId));		
	}

	/**
     * Gets all Case status.
     * @return the list of Case status <code>StatusItem</code>
     */
    public List<ProductStore> suggestProductStores() {
    	  String organizationPartyId = UtilProperties.getPropertyValue("opentaps", "organizationPartyId");
          return findList(ProductStore.class, EntityCondition.makeCondition(ProductStore.Fields.payToPartyId.name(), EntityOperator.EQUALS, organizationPartyId));    
    }
    /**
     * Gets all Case status.
     * @return the list of Case status <code>StatusItem</code>
     */
    public List<ProductStore> suggestProductStores(HttpServletRequest request) {
    	 GenericValue facility = (GenericValue) request.getSession().getAttribute("facility");
         String facilityId = facility.getString("facilityId");
        return findList(ProductStore.class, EntityCondition.makeCondition("inventoryFacilityId", EntityOperator.EQUALS, facilityId));
       
    }

    @Override
    public String makeSuggestDisplayedText(EntityInterface suggestStatus) {
        return suggestStatus.getString(ProductStoreLookupConfiguration.OUT_STORE_NAME);
    }
}
