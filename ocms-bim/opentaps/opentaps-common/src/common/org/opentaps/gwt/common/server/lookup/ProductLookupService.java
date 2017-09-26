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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.opentaps.base.entities.ProductAndGoodIdentification;
import org.opentaps.foundation.entity.EntityInterface;
import org.opentaps.foundation.infrastructure.InfrastructureException;
import org.opentaps.gwt.common.client.lookup.configuration.ProductLookupConfiguration;
import org.opentaps.gwt.common.server.HttpInputProvider;
import org.opentaps.gwt.common.server.InputProviderInterface;

/**
 * The RPC service used to populate the Product autocompleters widgets.
 */
public class ProductLookupService extends EntityLookupAndSuggestService {

    protected ProductLookupService(InputProviderInterface provider) {
        super(provider, ProductLookupConfiguration.LIST_OUT_FIELDS);
    }

    /**
     * AJAX event to suggest Product.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the JSON response
     * @throws InfrastructureException if an error occurs
     */
    public static String suggestProduct(HttpServletRequest request, HttpServletResponse response) throws InfrastructureException {
        InputProviderInterface provider = new HttpInputProvider(request);
        JsonResponse json = new JsonResponse(response);
        ProductLookupService service = new ProductLookupService(provider);
        service.suggestProduct(request,service);
        return json.makeSuggestResponse(ProductLookupConfiguration.OUT_PRODUCT_ID, service);
    }

    /**
     * Suggests a list of <code>Product</code>.
     * @return the list of <code>Product</code>, or <code>null</code> if an error occurred
     */
    public void suggestProduct(HttpServletRequest request,ProductLookupService service) {

       /* EntityCondition activeCondition = EntityCondition.makeCondition(EntityOperator.OR,
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, null),
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, "Y")
                );

        return findSuggestMatchesAnyOf(ProductAndGoodIdentification.class, ProductLookupConfiguration.LIST_LOOKUP_FIELDS, activeCondition);*/
    	String start = request.getParameter("start");
    	String limit = request.getParameter("limit");
    	String query = request.getParameter("query");
    	if(query != null){
    		query = query.trim();
    	}
    	GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
    	GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//sql
		StringBuffer sqlcount = new StringBuffer("select count(1) as count from (");
		StringBuffer sql = new StringBuffer("select x.product_id,y.productAttr,x.internal_name FROM PRODUCT x  LEFT JOIN (SELECT b.product_id, GROUP_CONCAT(DISTINCT a.DESCRIPTION ORDER BY a.PRODUCT_FEATURE_TYPE_ID) as productAttr FROM PRODUCT_FEATURE a LEFT JOIN PRODUCT_FEATURE_APPL b ON a.PRODUCT_FEATURE_ID = b.PRODUCT_FEATURE_ID GROUP BY product_id) y on x.product_id=y.product_id WHERE (x.PRODUCT_ID LIKE '%"+query+"%'  OR INTERNAL_NAME LIKE '%"+query+"%' OR BRAND_NAME LIKE '%"+query+"%') AND (x.IS_ACTIVE = 'Y' OR x.IS_ACTIVE IS NULL) AND x.IS_VARIANT = 'N' ");
		sqlcount.append(sql+") x");
		sql.append(" limit "+start+","+limit);
		ResultSet rs = null;
		List<ProductAndGoodIdentification> result = new ArrayList<ProductAndGoodIdentification>();
		try {
			rs = processor.executeQuery(sqlcount.toString());
			while(rs.next()){
				int count = Integer.parseInt(rs.getString("count"));
				service.setResultTotalCount(count);
			}
			rs = processor.executeQuery(sql.toString());
			while(rs.next()){
				ProductAndGoodIdentification productAndGoodIdentification = new ProductAndGoodIdentification();
				productAndGoodIdentification.setProductId(rs.getString("product_id"));
				String productAttr = rs.getString("productAttr");
				if(productAttr != null){
					productAndGoodIdentification.setInternalName(rs.getString("internal_name")+": ("+productAttr+")");
				}else{
					productAndGoodIdentification.setInternalName(rs.getString("internal_name"));
				}
				result.add(productAndGoodIdentification);
			}
		} catch (GenericDataSourceException e) {
			e.printStackTrace();
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{			
			try {
				rs.close();
				processor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		service.setResults(result);
    }

    /**
     * AJAX event to suggest Product.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the JSON response
     * @throws InfrastructureException if an error occurs
     */
    public static String suggestProductForCart(HttpServletRequest request, HttpServletResponse response) throws InfrastructureException {
        InputProviderInterface provider = new HttpInputProvider(request);
        JsonResponse json = new JsonResponse(response);
        ProductLookupService service = new ProductLookupService(provider);
        service.suggestProductForCart();
        return json.makeSuggestResponse(ProductLookupConfiguration.OUT_PRODUCT_ID, service);
    }
    
    /**
     * Suggests a list of <code>Product</code>.
     * @return the list of <code>Product</code>, or <code>null</code> if an error occurred
     */
    public List<ProductAndGoodIdentification> suggestProductForCart() {

        EntityCondition activeCondition = EntityCondition.makeCondition(EntityOperator.OR,
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, null),
                    EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isActive.name(), EntityOperator.EQUALS, "Y")
                );
        EntityCondition filterOutVirtualCondition = EntityCondition.makeCondition(EntityOperator.AND,
                EntityCondition.makeCondition(ProductAndGoodIdentification.Fields.isVirtual.name(), EntityOperator.NOT_EQUAL, "Y"),
                activeCondition);
        return findSuggestMatchesAnyOf(ProductAndGoodIdentification.class, ProductLookupConfiguration.LIST_LOOKUP_FIELDS, filterOutVirtualCondition);
    }

    @Override
    public String makeSuggestDisplayedText(EntityInterface product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getString(ProductLookupConfiguration.OUT_PRODUCT_ID)).append(":").append(product.getString(ProductLookupConfiguration.OUT_INTERNAL_NAME));
        return sb.toString();
    }

}
