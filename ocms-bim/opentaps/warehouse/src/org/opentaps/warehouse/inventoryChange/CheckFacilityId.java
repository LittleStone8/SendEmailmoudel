package org.opentaps.warehouse.inventoryChange;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.service.LocalDispatcher;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilConfig;

public class CheckFacilityId {
	
	public static String checkFacilityId(HttpServletRequest request,HttpServletResponse response) {
		try{
			HttpSession session = request.getSession();
			if(session.getAttribute("userLogin") == null){
				return "success";
			}
			String facilityId = (String) session.getAttribute("facilityId");
			if(facilityId == null){
				facilityId = UtilCommon.getUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY);
				if(facilityId == null){
					LocalDispatcher dispacher = (LocalDispatcher) request.getAttribute("dispatcher");
			        Delegator delegator = dispacher.getDelegator();
					GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
					SQLProcessor processor = new SQLProcessor(helperInfo);
					GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
					String partyId = (String) userLogin.get("partyId");
					String userLoginId = (String) userLogin.get("userLoginId");
					ResultSet rs = null;
					
					List<GenericValue> userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId), null);			
					List<String> groupIds = new ArrayList<String>();
					for(GenericValue userSecurityGroup : userSecurityGroups){
						groupIds.add(userSecurityGroup.getString("groupId"));
					}
					
					String sql = "";
					if(groupIds != null && groupIds.contains("WRHS_ADMIN")){
						sql = "select FACILITY_ID,FACILITY_NAME FROM FACILITY";
					}else{
						sql = "select a.FACILITY_ID,FACILITY_NAME from FACILITY a LEFT JOIN FACILITY_PARTY_PERMISSION b on a.FACILITY_ID=b.FACILITY_ID where security_group_id='WRHS_MANAGER' and THRU_DATE is null and PARTY_ID='"+partyId+"' group by a.FACILITY_ID order by a.FACILITY_NAME";
					}
					rs = processor.executeQuery(sql);
					while(rs.next()){
						facilityId = rs.getString("FACILITY_ID");
						break;
					}
				}
				UtilCommon.setUserLoginViewPreference(request, UtilConfig.SYSTEM_WIDE, UtilConfig.SET_FACILITY_FORM, UtilConfig.OPTION_DEF_FACILITY, facilityId);
				session.setAttribute("facilityId", facilityId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	
}
