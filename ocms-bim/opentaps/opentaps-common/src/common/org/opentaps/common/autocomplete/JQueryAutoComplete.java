package org.opentaps.common.autocomplete;

import static org.opentaps.common.autocomplete.UtilAutoComplete.AC_DEFAULT_RESULTS_SIZE;
import static org.opentaps.common.autocomplete.UtilAutoComplete.AC_FIND_OPTIONS;
import static org.opentaps.common.autocomplete.UtilAutoComplete.AP_PARTY_ORDER_BY;
import static org.opentaps.common.autocomplete.UtilAutoComplete.ac_activePartyCondition;
import static org.opentaps.common.autocomplete.UtilAutoComplete.ac_clientRoleCondition;
import static org.opentaps.common.autocomplete.UtilAutoComplete.makeSelectionJSONResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.entity.util.EntityUtil;
import org.opentaps.common.autocomplete.AutoComplete.PartySelectionBuilder;
import org.opentaps.common.autocomplete.bean.CreaterOrderFindCustomer;
import org.opentaps.common.party.PartyReader;
import org.opentaps.common.util.UtilCommon;

import javolution.util.FastList;

public class JQueryAutoComplete {
	
	private static final String MODULE = AutoComplete.class.getName();
	
	public static final List<String> RESULT_LIST = Arrays.asList("partyId", "groupName", "firstName", "lastName","primaryCountryCode","primaryAreaCode","primaryContactNumber","primaryAddress1","primaryAddress2");
//																									COUNTRY_CODE-countryCode	AREA_CODE-areaCode	CONTACT_NUMBER-contactNumber
	
	
	
	public static String new_getAutoCompleteClientPartyIds(HttpServletRequest request, HttpServletResponse response) {
		 EntityCondition condition = EntityCondition.makeCondition(EntityOperator.AND,
                 EntityUtil.getFilterByDateExpr(),
                 ac_activePartyCondition,
                 ac_clientRoleCondition);
		 List<CreaterOrderFindCustomer> customerList = autocompletePartyIdsByCondition(condition, "PartyFromSummaryByRelationship", request, response);
		 request.setAttribute("customerList", customerList);
		 return "success";
	}	
	
	
	
	
	private static List<CreaterOrderFindCustomer> autocompletePartyIdsByCondition(EntityCondition condition, String entityName, HttpServletRequest request, HttpServletResponse response) {
		 List<CreaterOrderFindCustomer> customerList = new ArrayList<CreaterOrderFindCustomer>();
        GenericValue userLogin = UtilCommon.getUserLogin(request);
        if (userLogin == null) {
            Debug.logError("Failed to retrieve the login user from the session.", MODULE);
            return null;
        }

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        
        String keyword = request.getParameter("term");
        if (keyword == null) {
            Debug.log("Ignored the empty keyword string.", MODULE);
            return null;
        }
        keyword = keyword.trim();

        List<GenericValue> parties = FastList.newInstance();
        if (keyword.length() > 0) try {
            // get result as a list iterator (transaction block is to work around a bug in entity engine)
            TransactionUtil.begin();
            EntityListIterator iterator = delegator.findListIteratorByCondition(entityName, condition, null, RESULT_LIST, AP_PARTY_ORDER_BY, AC_FIND_OPTIONS);
            // perform the search
            customerList = searchPartyName(iterator, keyword);
            // clean up
            iterator.close();
            TransactionUtil.commit();
        } catch (GenericEntityException e) {
            Debug.logError(e, MODULE);
            return null;
        }

        // write the JSON data to the response stream
        return customerList;
    }

	
	 /**
     * Search parties which name is matching the search string.
     * @param iterator an <code>Iterator</code> of parties
     * @param searchString a <code>String</code> value
     * @return a <code>List</code> value
     * @exception GenericEntityException if an error occurs
     */
    private static List<CreaterOrderFindCustomer> searchPartyName(Iterator<GenericValue> iterator, String searchString) throws GenericEntityException {
        ArrayList<GenericValue> parties = new ArrayList<GenericValue>();
        List<CreaterOrderFindCustomer> customerList = new ArrayList<CreaterOrderFindCustomer>();
        // format the search string for matching
        searchString = searchString.toUpperCase();

        int results = 0;
        GenericValue party = null;
        String compositeName;

        while (((party = iterator.next()) != null) && (results <= AC_DEFAULT_RESULTS_SIZE)) {

            compositeName = PartyReader.getPartyCompositeName(party).toUpperCase();

            // search the composite name which matches partyId, groupName, firstName and lastName
            if (compositeName.indexOf(searchString) > -1) {
                parties.add(party);
                results++;
                continue;
            }
        }
        for (GenericValue genericValue : parties) {
			CreaterOrderFindCustomer customer = new CreaterOrderFindCustomer();
        	customer.setPartyId((String) genericValue.get("partyId"));
        	customer.setFirstName((String) genericValue.get("firstName"));
        	customer.setLastName((String) genericValue.get("lastName"));
        	customer.setGroupName((String) genericValue.get("groupName"));
        	customer.setPrimaryCountryCode((String) genericValue.get("primaryCountryCode"));
        	customer.setPrimaryAreaCode((String) genericValue.get("primaryAreaCode"));
        	customer.setPrimaryContactNumber((String) genericValue.get("primaryContactNumber"));
        	customer.setPrimaryAddress1((String) genericValue.get("primaryAddress1"));
        	customer.setPrimaryAddress1((String) genericValue.get("primaryAddress2"));
        	customerList.add(customer);
        	System.out.println(genericValue);
			
		}
        return customerList;
    }
	
	
	
}
