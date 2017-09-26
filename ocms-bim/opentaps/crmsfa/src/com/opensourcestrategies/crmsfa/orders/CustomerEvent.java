package com.opensourcestrategies.crmsfa.orders;

import static org.opentaps.common.autocomplete.UtilAutoComplete.AC_FIND_OPTIONS;
import static org.opentaps.common.autocomplete.UtilAutoComplete.AP_PARTY_ORDER_BY;
import static org.opentaps.common.autocomplete.UtilAutoComplete.ac_activePartyCondition;
import static org.opentaps.common.autocomplete.UtilAutoComplete.ac_clientRoleCondition;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.ServiceUtil;
import org.opentaps.common.autocomplete.AutoComplete;
import org.opentaps.common.autocomplete.bean.CreaterOrderFindCustomer;
import org.opentaps.common.util.UtilCommon;

import com.opensourcestrategies.crmsfa.orders.bean.CreateOrderFindCustomer;
import com.opensourcestrategies.crmsfa.orders.bean.CreateOrderGeo;


public class CustomerEvent {

	private static final String MODULE = AutoComplete.class.getName();

	public static final List<String> RESULT_LIST = Arrays.asList("partyId", "groupName", "firstName", "lastName",
			"primaryCountryCode", "primaryAreaCode", "primaryContactNumber", "primaryAddress1", "primaryAddress2");
	// COUNTRY_CODE-countryCode AREA_CODE-areaCode CONTACT_NUMBER-contactNumber

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> findAllClient(HttpServletRequest request, HttpServletResponse response) {
		EntityCondition condition = EntityCondition.makeCondition(EntityOperator.AND, EntityUtil.getFilterByDateExpr(),
				ac_activePartyCondition, ac_clientRoleCondition);
		List<CreaterOrderFindCustomer> customerList = autocompletePartyIdsByCondition(condition,
				"PartyFromSummaryByRelationship", request, response);
		request.setAttribute("customerList", customerList);
		Map<String, Object> reslutMap = ServiceUtil.returnSuccess("query success");
		reslutMap.put("customerList", customerList);
		return reslutMap;
	}

	private static List<CreaterOrderFindCustomer> autocompletePartyIdsByCondition(EntityCondition condition,
			String entityName, HttpServletRequest request, HttpServletResponse response) {
		List<CreaterOrderFindCustomer> customerList = new ArrayList<CreaterOrderFindCustomer>();
		GenericValue userLogin = UtilCommon.getUserLogin(request);
		if (userLogin == null) {
			Debug.logError("Failed to retrieve the login user from the session.", MODULE);
			return null;
		}

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		try {
			// get result as a list iterator (transaction block is to work
			// around a bug in entity engine)
			TransactionUtil.begin();
			EntityListIterator iterator = delegator.findListIteratorByCondition(entityName, condition, null,
					RESULT_LIST, AP_PARTY_ORDER_BY, AC_FIND_OPTIONS);
			// perform the search
			while (iterator.hasNext()) {
				GenericValue genericValue = (GenericValue) iterator.next();
				CreaterOrderFindCustomer customer = new CreaterOrderFindCustomer();
				customer.setPartyId((String) genericValue.get("partyId"));
				customer.setFirstName((String) genericValue.get("firstName"));
				customer.setLastName((String) genericValue.get("lastName"));
				customer.setGroupName((String) genericValue.get("groupName"));
				customer.setPrimaryCountryCode((String) genericValue.get("primaryCountryCode"));
				customer.setPrimaryAreaCode((String) genericValue.get("primaryAreaCode"));
				customer.setPrimaryContactNumber((String) genericValue.get("primaryContactNumber"));
				customer.setPrimaryAddress1((String) genericValue.get("primaryAddress1"));
				customer.setPrimaryAddress2((String) genericValue.get("primaryAddress2"));
				customerList.add(customer);
			}
			iterator.close();
			TransactionUtil.commit();
		} catch (GenericEntityException e) {
			Debug.logError(e, MODULE);
			return null;
		}

		return customerList;
	}


	
	
	
	public static Map<String, Object> findGeo(HttpServletRequest request, HttpServletResponse response) {
		try {
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			String geoId = request.getParameter("geoId");
			if(geoId == null || geoId.equals("")){
				List<GenericValue> GeoList = delegator.findByAnd("Geo", UtilMisc.toMap("geoTypeId", "COUNTRY"));
				int index = 0;
				if(GeoList.size() > 0){
					
					Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
					List<Object> reslutList = new ArrayList<Object>();
					for (GenericValue geo : GeoList) {
						Map<Integer, Object> map = new HashMap<Integer, Object>();
						CreateOrderGeo createOrderGeo = new CreateOrderGeo();
						if(!geo.getString("geoId").equals("UGA") && !geo.getString("geoId").equals("GHA")){
							continue;
						}
						//过滤没有下级的地�
						List<GenericValue> geoAssocList = delegator.findByAnd("GeoAssoc", UtilMisc.toMap("geoId", geo.getString("geoId")));
						if(geoAssocList.size() == 0){
							continue;
						}
						createOrderGeo.setId(geo.getString("geoId"));
						createOrderGeo.setName(geo.getString("geoName"));
						map.put(index++, createOrderGeo);
						reslutList.add(createOrderGeo);
						
					}
					resultMap.put("reslutList", reslutList);
					return resultMap;
				}
				else{
					return ServiceUtil.returnError("query failed");
				}
			}else{
				int index = 0;
				List<GenericValue> geoAssocList = delegator.findByAnd("GeoAssoc", UtilMisc.toMap("geoId", geoId));
				if(geoAssocList.size() > 0){
					Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
					List<Object> reslutList = new ArrayList<Object>();
					for (GenericValue geoAssoc : geoAssocList) {
						GenericValue geo = delegator.findOne("Geo", true , UtilMisc.toMap("geoId", geoAssoc.getString("geoIdTo")));
//						resultMap.put(geo.getString("geoId"), geo.getString("geoName"));
						
						Map<Integer, Object> map = new HashMap<Integer, Object>();
						CreateOrderGeo createOrderGeo = new CreateOrderGeo();
						createOrderGeo.setId(geo.getString("geoId"));
						createOrderGeo.setName(geo.getString("geoName"));
						map.put(index++, createOrderGeo);
						reslutList.add(createOrderGeo);
					}
					resultMap.put("reslutList", reslutList);
					return resultMap;
				}else{
					return ServiceUtil.returnError("query failed");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> LookupAutoCompleteByName(HttpServletRequest request, HttpServletResponse response) {
		try {
			String name = request.getParameter("name");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String sql = " select pe.PARTY_ID , pe.FIRST_NAME , pe.LAST_NAME , tn.CONTACT_NUMBER , tn.AREA_CODE , tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 "
						+" from (select * from PARTY where PARTY_TYPE_ID = \'PERSON\') as p "
						+" join (select * from PERSON WHERE LAST_NAME_LOCAL like \'%param%\' or FIRST_NAME_LOCAL like \'%param%\' or PARTY_ID like \'%param%\') as pe "
						+" on p.PARTY_ID = pe.PARTY_ID "
						+" join PARTY_SUPPLEMENTAL_DATA as psd "
						+" on pe.PARTY_ID = psd.PARTY_ID "
						+" join TELECOM_NUMBER as tn "
						+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID "
						+" join POSTAL_ADDRESS as pa "
						+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID "
						+" join PARTY_ROLE as pr on pr.PARTY_ID=pe.PARTY_ID "
						+" where pr.ROLE_TYPE_ID=\'CONTACT\' "
						+ "limit 10 ";
	
			sql=sql.replaceAll("param", name);
		
			ResultSet rs = processor.executeQuery(sql);
			
			//rs是否有�
			List<Object> customerList = new ArrayList<Object>();
			while(rs.next()){
				CreateOrderFindCustomer createOrderFindCustomer = new CreateOrderFindCustomer();
				createOrderFindCustomer.setPartyId(rs.getString("PARTY_ID"));
				createOrderFindCustomer.setFirstName(rs.getString("FIRST_NAME"));
				createOrderFindCustomer.setLastName(rs.getString("LAST_NAME"));
				createOrderFindCustomer.setPrimaryAddress1(rs.getString("ADDRESS1"));
				createOrderFindCustomer.setPrimaryAddress2(rs.getString("ADDRESS2"));
				createOrderFindCustomer.setPrimaryPhoneAreaCode(rs.getString("AREA_CODE"));
				createOrderFindCustomer.setPrimaryPhoneCountryCode(rs.getString("COUNTRY_CODE"));
				createOrderFindCustomer.setPrimaryPhoneNumber(rs.getString("CONTACT_NUMBER"));
				createOrderFindCustomer.replenish();
				customerList.add(createOrderFindCustomer);
			}
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("customerList", customerList);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
		
	}
	public static Map<String, Object> LookupAutoCompleteByName_accurate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String name = request.getParameter("name");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String sql = " select pe.PARTY_ID , pe.FIRST_NAME , pe.LAST_NAME , tn.CONTACT_NUMBER , tn.AREA_CODE , tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 "
						+" from (select * from PARTY where PARTY_TYPE_ID = \'PERSON\') as p "
						+" join (select * from PERSON WHERE  PARTY_ID = \'param\') as pe "
						+" on p.PARTY_ID = pe.PARTY_ID "
						+" join PARTY_SUPPLEMENTAL_DATA as psd "
						+" on pe.PARTY_ID = psd.PARTY_ID "
						+" join TELECOM_NUMBER as tn "
						+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID "
						+" join POSTAL_ADDRESS as pa "
						+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID limit 10 ";
	
			sql=sql.replaceAll("param", name);
		
			ResultSet rs = processor.executeQuery(sql);
			
			//rs是否有�
			List<Object> customerList = new ArrayList<Object>();
			while(rs.next()){
				CreateOrderFindCustomer createOrderFindCustomer = new CreateOrderFindCustomer();
				createOrderFindCustomer.setPartyId(rs.getString("PARTY_ID"));
				createOrderFindCustomer.setFirstName(rs.getString("FIRST_NAME"));
				createOrderFindCustomer.setLastName(rs.getString("LAST_NAME"));
				createOrderFindCustomer.setPrimaryAddress1(rs.getString("ADDRESS1"));
				createOrderFindCustomer.setPrimaryAddress2(rs.getString("ADDRESS2"));
				createOrderFindCustomer.setPrimaryPhoneAreaCode(rs.getString("AREA_CODE"));
				createOrderFindCustomer.setPrimaryPhoneCountryCode(rs.getString("COUNTRY_CODE"));
				createOrderFindCustomer.setPrimaryPhoneNumber(rs.getString("CONTACT_NUMBER"));
				createOrderFindCustomer.replenish();
				customerList.add(createOrderFindCustomer);
			}
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("customerList", customerList);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
		
	}
	
	
	public static Map<String, Object> LookupAutoCompleteByTelecom(HttpServletRequest request, HttpServletResponse response) {
		try {
			String telecomNumber = request.getParameter("telecomNumber");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			String sql = " select pe.PARTY_ID , pe.FIRST_NAME , pe.LAST_NAME , tn.CONTACT_NUMBER , tn.AREA_CODE , tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 "
						+" from (select * from PARTY where PARTY_TYPE_ID = \'PERSON\') as p "
						+" join (select * from PERSON where FIRST_NAME <> '' and FIRST_NAME_LOCAL <> '') as pe "
						+" on p.PARTY_ID = pe.PARTY_ID "
						+" join PARTY_SUPPLEMENTAL_DATA as psd "
						+" on pe.PARTY_ID = psd.PARTY_ID "
						+" join (select * from TELECOM_NUMBER where CONTACT_NUMBER like \'%param%\') as tn "
						+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID "
						+" join POSTAL_ADDRESS as pa "
						+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID limit 10 ";
			sql=sql.replaceAll("param", telecomNumber);
		
			ResultSet rs = processor.executeQuery(sql);
			
			//rs是否有�
			List<Object> customerList = new ArrayList<Object>();
			while(rs.next()){
				CreateOrderFindCustomer createOrderFindCustomer = new CreateOrderFindCustomer();
				createOrderFindCustomer.setPartyId(rs.getString("PARTY_ID"));
				createOrderFindCustomer.setFirstName(rs.getString("FIRST_NAME"));
				createOrderFindCustomer.setLastName(rs.getString("LAST_NAME"));
				createOrderFindCustomer.setPrimaryAddress1(rs.getString("ADDRESS1"));
				createOrderFindCustomer.setPrimaryAddress2(rs.getString("ADDRESS2"));
				createOrderFindCustomer.setPrimaryPhoneAreaCode(rs.getString("AREA_CODE"));
				createOrderFindCustomer.setPrimaryPhoneCountryCode(rs.getString("COUNTRY_CODE"));
				createOrderFindCustomer.setPrimaryPhoneNumber(rs.getString("CONTACT_NUMBER"));
				createOrderFindCustomer.replenish();
				customerList.add(createOrderFindCustomer);
			}
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("customerList", customerList);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
		
	}	
	
	
	/**
	 * 模糊查询 
	 * 
	 * @param param 查询的参�
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> LookupAutoComplete(HttpServletRequest request, HttpServletResponse response) {
		try {
			String param = request.getParameter("param");
			Delegator delegator = (Delegator) request.getAttribute("delegator");
			//包装参数
			GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
			SQLProcessor processor = new SQLProcessor(helperInfo);
			
			
			
			
//			String queryByNameSql = " select pe.PARTY_ID , pe.FIRST_NAME_LOCAL , pe.LAST_NAME_LOCAL , tn.CONTACT_NUMBER , tn.AREA_CODE , tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 , pa.COUNTRY_GEO_ID , pa.STATE_PROVINCE_GEO_ID , pa.CITY , pa.COUNTY_GEO_ID " 
//					+" from (select * from PARTY where PARTY_TYPE_ID = \'PERSON\') as p "
//					+" join (select * from PERSON WHERE FIRST_NAME_LOCAL like \'%param%\' or LAST_NAME_LOCAL like \'%param%\' order by FIRST_NAME_LOCAL)  as pe "
//					+" on p.PARTY_ID = pe.PARTY_ID "
//					+" join PARTY_SUPPLEMENTAL_DATA as psd "
//					+" on pe.PARTY_ID = psd.PARTY_ID "
//					+" join TELECOM_NUMBER as tn "
//					+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID "
//					+" LEFT join POSTAL_ADDRESS as pa "
//					+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID order by pe.FIRST_NAME_LOCAL limit 10  ";
//			
			String queryByNameSql = " select "
					+" pe.PARTY_ID , pe.FIRST_NAME_LOCAL , pe.LAST_NAME_LOCAL , tn.CONTACT_NUMBER , tn.AREA_CODE , "
					+" tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 , pa.COUNTRY_GEO_ID , pa.STATE_PROVINCE_GEO_ID , pa.CITY , pa.COUNTY_GEO_ID, "
					+" (select GEO_NAME from GEO where GEO_ID=pa.COUNTRY_GEO_ID) AS countryGeoName, "
					+ "(select GEO_NAME from GEO where GEO_ID=pa.STATE_PROVINCE_GEO_ID) AS stateProvinceGeoName, "
					+" (select GEO_NAME from GEO where GEO_ID=pa.CITY) AS cityName, "
					+ "(select GEO_NAME from GEO where GEO_ID=pa.COUNTY_GEO_ID) AS countyGeoName "
					+" from "
					+" (select * from PERSON WHERE FIRST_NAME_LOCAL like \'%param%\' limit 10)  pe "
					+" left join PARTY p  on p.PARTY_ID = pe.PARTY_ID "
					+" left join PARTY_SUPPLEMENTAL_DATA as psd  "
					+" on pe.PARTY_ID = psd.PARTY_ID  "
					+" left join TELECOM_NUMBER as tn  "
					+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID  "
					+" left join POSTAL_ADDRESS as pa  "
					+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID order by pe.FIRST_NAME_LOCAL ";
			 
	
			queryByNameSql=queryByNameSql.replaceAll("param", param);
		
			ResultSet rs1 = processor.executeQuery(queryByNameSql);
			
			//rs是否有�
			List<Object> customerList = new ArrayList<Object>();
			while(rs1.next()){
				CreateOrderFindCustomer createOrderFindCustomer = new CreateOrderFindCustomer();
				createOrderFindCustomer.setPartyId(rs1.getString("PARTY_ID"));
				createOrderFindCustomer.setFirstName(rs1.getString("FIRST_NAME_LOCAL"));
				createOrderFindCustomer.setLastName(rs1.getString("LAST_NAME_LOCAL"));
				String address1 = (rs1.getString("countryGeoName") == null ? "" : rs1.getString("countryGeoName"))
						+ (rs1.getString("stateProvinceGeoName")== null ? "" : " " + rs1.getString("stateProvinceGeoName"))
						+ (rs1.getString("cityName") == null ? "" : " " + rs1.getString("cityName"))
						+ (rs1.getString("countyGeoName") == null ? "" : " " + rs1.getString("countyGeoName"))
						+ (rs1.getString("ADDRESS1") == null ? "" : " " + rs1.getString("ADDRESS1"));

				
				//chenshihua	2017-7-7
//				GenericValue Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs1.getString("COUNTRY_GEO_ID")), false);
//				if(Geo != null){
//					address1 = Geo.getString("geoName");
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs1.getString("STATE_PROVINCE_GEO_ID")), false);
//				if(Geo != null){
//					address1 = address1  + Geo.getString("geoName") + " ";
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs1.getString("CITY")), false);
//				if(Geo != null){
//					address1 = address1 + Geo.getString("geoName") + " ";
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs1.getString("COUNTY_GEO_ID")), false);
//				if(Geo != null){
//					address1 = address1 + Geo.getString("geoName") + " ";
//				}
//				
//				if(rs1.getString("ADDRESS1") != null) {
//					address1 = address1  + rs1.getString("ADDRESS1")+ " ";	
//				}
				createOrderFindCustomer.setPrimaryAddress1(address1);
				createOrderFindCustomer.setPrimaryAddress2(rs1.getString("ADDRESS2"));
				createOrderFindCustomer.setPrimaryPhoneAreaCode(rs1.getString("AREA_CODE"));
				createOrderFindCustomer.setPrimaryPhoneCountryCode(rs1.getString("COUNTRY_CODE"));
				createOrderFindCustomer.setPrimaryPhoneNumber(rs1.getString("CONTACT_NUMBER"));
				createOrderFindCustomer.replenish();
				customerList.add(createOrderFindCustomer);
			}
			
			
			String sql = "	select   pe.PARTY_ID,"
					+" pe.FIRST_NAME_LOCAL, "
				   +" pe.LAST_NAME_LOCAL, "
				   +" tn.CONTACT_NUMBER, "
				   +" tn.AREA_CODE, "
				   +" tn.COUNTRY_CODE, "
				   +" pa.ADDRESS1, "
				   +" pa.ADDRESS2, "
				   +" pa.COUNTRY_GEO_ID, "
				   +" pa.STATE_PROVINCE_GEO_ID, "
				   +" pa.CITY, "
				   +" pa.COUNTY_GEO_ID ,"
				   +" (select GEO_NAME from GEO where GEO_ID=pa.COUNTRY_GEO_ID) AS countryGeoName, "
				   +" (select GEO_NAME from GEO where GEO_ID=pa.STATE_PROVINCE_GEO_ID) AS stateProvinceGeoName, "
				   +" (select GEO_NAME from GEO where GEO_ID=pa.CITY) AS cityName, "
				   +" (select GEO_NAME from GEO where GEO_ID=pa.COUNTY_GEO_ID) AS countyGeoName  " 
				   +" from ( SELECT * FROM TELECOM_NUMBER as tn1 WHERE tn1.CONTACT_NUMBER LIKE \'%param%\'	ORDER BY tn1.CONTACT_NUMBER limit 10 ) tn "
				   +" left join PARTY_SUPPLEMENTAL_DATA psd on (psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID) "
				   +" left join POSTAL_ADDRESS AS pa ON (pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID) "
				   +" left join (SELECT * FROM PARTY WHERE PARTY_TYPE_ID = 'PERSON') p on (p.PARTY_ID = psd.PARTY_ID) "
				   +" join (SELECT *FROM PERSON WHERE FIRST_NAME_LOCAL <> '') pe on (p.PARTY_ID = pe.PARTY_ID) ";
			
			//chenshihua	2017-7-7
//			String sql = " select pe.PARTY_ID , pe.FIRST_NAME_LOCAL , pe.LAST_NAME_LOCAL , tn.CONTACT_NUMBER , tn.AREA_CODE , tn.COUNTRY_CODE , pa.ADDRESS1 , pa.ADDRESS2 , pa.COUNTRY_GEO_ID , pa.STATE_PROVINCE_GEO_ID , pa.CITY , pa.COUNTY_GEO_ID  "
//					+" from (select * from PARTY where PARTY_TYPE_ID = \'PERSON\') as p "
//					+" join (select * from PERSON where FIRST_NAME <> '' and FIRST_NAME_LOCAL <> '') as pe "
//					+" on p.PARTY_ID = pe.PARTY_ID "
//					+" join PARTY_SUPPLEMENTAL_DATA as psd "
//					+" on pe.PARTY_ID = psd.PARTY_ID "
//					+" join (select * from TELECOM_NUMBER where CONTACT_NUMBER like \'%param%\' order by CONTACT_NUMBER) as tn "
//					+" on psd.PRIMARY_TELECOM_NUMBER_ID = tn.CONTACT_MECH_ID "
//					+" LEFT join POSTAL_ADDRESS as pa "
//					+" on pa.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID order by tn.CONTACT_NUMBER limit 10 ";
//			
			
			sql=sql.replaceAll("param", param);
	
			ResultSet rs2 = processor.executeQuery(sql);
		
			//rs是否有�
			while(rs2.next()){
				CreateOrderFindCustomer createOrderFindCustomer = new CreateOrderFindCustomer();
				createOrderFindCustomer.setPartyId(rs2.getString("PARTY_ID"));
				createOrderFindCustomer.setFirstName(rs2.getString("FIRST_NAME_LOCAL"));
				createOrderFindCustomer.setLastName(rs2.getString("LAST_NAME_LOCAL"));
				
				String address1 = (rs2.getString("countryGeoName")  == null ? "" : rs2.getString("countryGeoName"))
						+ (rs2.getString("stateProvinceGeoName")  == null ? "" : " " + rs2.getString("stateProvinceGeoName"))
						+ (rs2.getString("cityName")  == null ? "" : " " + rs2.getString("cityName"))
						+ (rs2.getString("countyGeoName")  == null ? "" : " " + rs2.getString("countyGeoName"))
						+ (rs2.getString("ADDRESS1")  == null ? "" : " " + rs2.getString("ADDRESS1"));
				
				
				
//				GenericValue Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs2.getString("COUNTRY_GEO_ID")), false);
//				if(Geo != null){
//					address1 = Geo.getString("geoName");
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs2.getString("STATE_PROVINCE_GEO_ID")), false);
//				if(Geo != null){
//					address1 = address1  + Geo.getString("geoName") + " ";
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs2.getString("CITY")), false);
//				if(Geo != null){
//					address1 = address1 + Geo.getString("geoName") + " ";
//				}
//				Geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", rs2.getString("COUNTY_GEO_ID")), false);
//				if(Geo != null){
//					address1 = address1 + Geo.getString("geoName") + " ";
//				}
//				if(rs2.getString("ADDRESS1") != null)
//				address1 = address1  + rs2.getString("ADDRESS1")+ " ";
				
				createOrderFindCustomer.setPrimaryAddress1(address1);
				createOrderFindCustomer.setPrimaryAddress2(rs2.getString("ADDRESS2"));
				createOrderFindCustomer.setPrimaryPhoneAreaCode(rs2.getString("AREA_CODE"));
				createOrderFindCustomer.setPrimaryPhoneCountryCode(rs2.getString("COUNTRY_CODE"));
				createOrderFindCustomer.setPrimaryPhoneNumber(rs2.getString("CONTACT_NUMBER"));
				createOrderFindCustomer.replenish();
				customerList.add(createOrderFindCustomer);
			}
			int index = customerList.size();
			while(index > 10){
				customerList.remove(5);
				index--;
			}
			Map<String, Object> resultMap = ServiceUtil.returnSuccess("query success");
			resultMap.put("customerList", customerList);
			return resultMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("query failed");
		}
	}
		
	public static Map<String,Object> findDefaultAddress(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		String productStoreId = request.getParameter("productStoreId");
		try {
			GenericValue productStore = delegator.findByPrimaryKey("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
			result.put("provinceId", productStore.getString("provinceId"));
			result.put("cityId", productStore.getString("cityId"));
			result.put("areaId", productStore.getString("areaId"));
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return result;
	}

}
