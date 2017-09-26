package org.ofbiz.report.security;

import java.util.Iterator;
import java.util.Map;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;

import javolution.util.FastMap;

/**
 * 判断报表一级菜单权限
 * 
 */
public class OperatorReportSecurity extends BaseStoreSecurity {
	
	protected OperatorReportSecurity() {
	}

	public OperatorReportSecurity(Delegator delegator) {
		this.delegator = delegator;
	}

	@Override
	/**
	 * Map<SGName, RoleType> 
	 */

	public Map<String, String> getAvailableSGAndRoleType(Iterator<GenericValue> iterator) {
		GenericValue userLoginSecurityGroup = null;
		Map<String, String> conditions = FastMap.newInstance();
		while (iterator.hasNext()) {
			userLoginSecurityGroup = iterator.next();
			String userGroupID = userLoginSecurityGroup.getString("groupId");
			
			if (userGroupID.equals(MAX)){
				conditions.put("MTN_S", "master");
				conditions.put("AIRTEL_S", "master");
				conditions.put("UTL_S", "master");
				break;
			}
			if (userGroupID.equals(MTN_MASTER)) {
				conditions.put("MTN_S", "master");
			}
			if (userGroupID.equals(AIRTEL_MASTER)) {
				conditions.put("AIRTEL_S", "master");
			}
			if (userGroupID.equals(UTL_MASTER)) {
				conditions.put("UTL_S", "master");
			}
			if (userGroupID.equals(MTN_SINGLE)) {
				if (!conditions.containsKey("MTN_S")) {
					conditions.put("MTN_S", "single");
				}
			}
			if (userGroupID.equals(AIRTEL_SINGLE)) {
				if (!conditions.containsKey("AIRTEL_S")) {
					conditions.put("AIRTEL_S", "single");
				}
			}
			if (userGroupID.equals(UTL_SINGLE)) {
				if (!conditions.containsKey("UTL_S")) {
					conditions.put("UTL_S", "single");
				}
			}
			
			/*yzl20170705*/
			if (userGroupID.equals(REP_INV_GH_MAN)) {
				if (!conditions.containsKey(REP_INV_GH_MAN)) {
					conditions.put(REP_INV_GH_MAN, REP_INV_GH_MAN);
				}
			}
			if (userGroupID.equals(REP_INV_UG_MAN)) {
				if (!conditions.containsKey(REP_INV_UG_MAN)) {
					conditions.put(REP_INV_UG_MAN, REP_INV_UG_MAN);
				}
			}
			if (userGroupID.equals(REP_INV_ADMIN)) {
				if (!conditions.containsKey(REP_INV_ADMIN)) {
					conditions.put(REP_INV_ADMIN, REP_INV_ADMIN);
				}
			}
			
		}
		return conditions;
	}

}
