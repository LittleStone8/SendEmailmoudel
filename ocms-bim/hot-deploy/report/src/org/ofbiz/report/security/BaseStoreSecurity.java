package org.ofbiz.report.security;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;

public abstract class BaseStoreSecurity {
	protected static final String MAX = "REPDATA_MAX";
	protected static final String BANANA_MASTER = "REPDATA_BAN_MAS";
	protected static final String BANANA_SINGLE = "REPDATA_BAN_SIN";
	protected static final String MTN_MASTER = "REPDATA_MTN_MAS";  
	protected static final String MTN_SINGLE = "REPDATA_MTN_SIN";
	protected static final String AIRTEL_MASTER = "REPDATA_AIR_MAS";
	protected static final String AIRTEL_SINGLE = "REPDATA_AIR_SIN";
	protected static final String UTL_MASTER = "REPDATA_UTL_MAS";  
	protected static final String UTL_SINGLE = "REPDATA_UTL_SIN";
	
	public static final String REP_INV_GH_MAN = "REP_INV_GH_MAN";
	public static final String REP_INV_UG_MAN = "REP_INV_UG_MAN";
	public static final String REP_INV_ADMIN = "REP_INV_ADMIN";
	
    protected Delegator delegator;

//	protected static final String STORE_ROLE = "STORE_MANAGER";
//    protected static final Map<String, Map<String, String>> simpleRoleEntity = UtilMisc.toMap(
//        "ORDERMGR", UtilMisc.toMap("name", "OrderRole", "pkey", "orderId"),
//        "FACILITY", UtilMisc.toMap("name", "FacilityParty", "pkey", "facilityId"),
//        "MARKETING", UtilMisc.toMap("name", "MarketingCampaignRole", "pkey", "marketingCampaignId"));

    public static final String MTN = "MTN";
    public static final String BANANA = "BANANA";
    public static final String AIRTEL = "AIRTEL";
    public static final String UTL = "UTL";
    public String type;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Delegator getDelegator() {
        return this.delegator;
    }

    public void setDelegator(Delegator delegator) {
        this.delegator = delegator;
    }
    
    //查询“安全组角色”是max，master，single

    
	/**
	 * 检查report模块权限
	 * @param delegator
	 * @param userLogin
	 * @return
	 * @throws GenericEntityException 
	 */
	public Map<String, String> getReportPermission(GenericDelegator delegator,GenericValue userLogin) throws GenericEntityException{
		if (userLogin == null){ 
			return null;
		}
        Iterator<GenericValue> iterator = findUserLoginSecurityGroupByUserLoginId(delegator, userLogin.getString("userLoginId"));		
		if(null==iterator){
			return null;
		}

		Map<String,String> conditions = getAvailableSGAndRoleType(iterator);

        return conditions;
	}
	
	
	/**
	 * 通过userLogin判断安全组
	 * @throws GenericEntityException 
	 */
    public Iterator<GenericValue> findUserLoginSecurityGroupByUserLoginId(GenericDelegator delegator,String userLoginId) throws GenericEntityException {
        List<GenericValue> collection;
        collection = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);
        // filter each time after cache retreival, ie cache will contain entire list
        if(null!=collection){
        	collection = EntityUtil.filterByDate(collection, true);
        	return collection.iterator();
        }else{
        	return null;
        }
        
    }
    
    
    /**
     * 通过userLogin所属的party来决定
     */
    public boolean securityGroupPermissionExists(String groupId, String permission) {
        GenericValue securityGroupPermissionValue = delegator.makeValue("SecurityGroupPermission",
                UtilMisc.toMap("groupId", groupId, "permissionId", permission));
        try {
            return delegator.findOne(securityGroupPermissionValue.getEntityName(), securityGroupPermissionValue, false) != null;
        } catch (GenericEntityException e) {
            return false;
        }
    }
    
    public abstract  Map<String, String> getAvailableSGAndRoleType(Iterator<GenericValue> iterator);
}
