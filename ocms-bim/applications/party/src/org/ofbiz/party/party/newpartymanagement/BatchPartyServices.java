package org.ofbiz.party.party.newpartymanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.party.party.newpartymanagement.bean.Partyinfo;
import org.ofbiz.party.party.newpartymanagement.bean.SecurityGroupinfo;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BatchPartyServices {
	
	
	public static Map<String, Object> Findpartyanduserlogin(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String, Object>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		JSONObject json = makeParams(request);	
		
		String partyIdName = json.getString("partyIdName");
		String partyIdNameNum = json.getString("partyIdNameNum");
		String partyIdNameStatus = json.getString("partyIdNameStatus");
		
		String firstName = json.getString("firstName");
		String firstNameNum = json.getString("firstNameNum");
		String firstNameStatus = json.getString("firstNameStatus");
		
		String lastName = json.getString("lastName");
		String lastNameNum = json.getString("lastNameNum");
		String lastNameStatus = json.getString("lastNameStatus");
		
		String userLogin = json.getString("userLogin");
		String userLoginNum = json.getString("userLoginNum");
		String userLoginStatus = json.getString("userLoginStatus");
		
		
		String pageNum = (String) json.get("pageNum");
		if(pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		String pageSize = (String) json.get("pageSize");
		if(pageSize == null || pageSize.equals("")){
			pageSize = "10";
		}
		StringBuffer sql = new StringBuffer("select pe.PARTY_ID as partyId,pe.FIRST_NAME as firstName,pe.MIDDLE_NAME as middlename,pe.LAST_NAME as lastName,tt.USER_LOGIN_ID as userLoginId from ( select pa.PARTY_ID,ul.USER_LOGIN_ID from PARTY as pa join USER_LOGIN as ul on pa.PARTY_ID=ul.PARTY_ID ) as tt join PERSON  as pe on pe.PARTY_ID=tt.PARTY_ID  WHERE 1=1 ");

		makeCondition(sql, "pe.PARTY_ID", partyIdNameNum, partyIdName, partyIdNameStatus);
		makeCondition(sql, "pe.FIRST_NAME", firstNameNum, firstName, firstNameStatus);
		makeCondition(sql, "pe.LAST_NAME", lastNameNum, lastName, lastNameStatus);
		makeCondition(sql, "tt.USER_LOGIN_ID", userLoginNum, userLogin, userLoginStatus);
		sql.append(" group by partyId ");
		
		try{
			result = EncapsulatedQueryResultsUtil.getResults(sql, Integer.valueOf(pageNum), Integer.valueOf(pageSize), Partyinfo.class, delegator);
			result.put("resultCode", 1);
			result.put("resultMsg", "Successful operation");
		}catch(Exception e){
			result.put("resultCode", -1);
			result.put("resultMsg", "System exception, please contact the administrator");
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static Map<String, Object> Findpartyanduserlogin__(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		Map<String, Object> returnmap = ServiceUtil.returnSuccess();
		String PartyId = request.getParameter("PartyId");
		String FirstName = request.getParameter("FirstName");
		String LastName = request.getParameter("LastName");
		
		String sql="select * from PARTY as pa join USER_LOGIN as ul on pa.PARTY_ID=ul.PARTY_ID join PERSON  as pe where PERSON.PARTY_ID=ul.PARTY_ID ";// where pa.PARTY_ID='admin'"
		boolean isfrist=true;
		if(PartyId!=null &&"".equals(PartyId))
		{
			if(isfrist)
			{
				sql+=" where pe.PARTY_ID=\'"+PartyId+"\'";
				isfrist=false;
			}
			else 
			{
				sql+=" pe.PARTY_ID=\'"+PartyId+"\'";
			}
				
		}
		if(FirstName!=null &&"".equals(FirstName))
		{
			if(isfrist)
			{
				sql+=" where pe.FIRST_NAME=\'"+FirstName+"\'";
				isfrist=false;
			}
			else 
			{
				sql+=" pe.FIRST_NAME=\'"+FirstName+"\'";
			}
		}
		if(LastName!=null &&"".equals(LastName))
		{
			if(isfrist)
			{
				sql+=" where pe.LAST_NAME=\'"+LastName+"\'";
				isfrist=false;
			}
			else 
			{
				sql+=" pe.LAST_NAME=\'"+LastName+"\'";
			}
		}
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<Partyinfo> Partyinfolist=new ArrayList<Partyinfo>();
		try {
			ResultSet rs = processor.executeQuery(sql);
			while (rs.next()) {
				Partyinfo temp = new Partyinfo();
				String PARTY_ID = rs.getString("PARTY_ID");
				String FIRST_NAME = rs.getString("FIRST_NAME");
				String LAST_NAME = rs.getString("LAST_NAME");
				String USER_LOGIN_ID = rs.getString("USER_LOGIN_ID");
				temp.setPartyId(PARTY_ID);
				temp.setFirstName(FIRST_NAME);
				temp.setLastName(LAST_NAME);
				temp.setUserLoginId(USER_LOGIN_ID);
				Partyinfolist.add(temp);
			}
			returnmap.put("Partyinfolist", Partyinfolist);
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnmap;
	}
	
	
	
	public static Map<String, Object> Findloginbypartyid(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> returnmap = ServiceUtil.returnSuccess();
		String party=request.getParameter("partyId");
		if(party==null||"".equals(party))
			return returnmap;
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String sql="select * from USER_LOGIN where USER_LOGIN.PARTY_ID=\'"+party+"\'";
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<Partyinfo> SecurityGroupinfolist=new ArrayList<Partyinfo>();
		try {
			ResultSet rs = processor.executeQuery(sql);
			while (rs.next()) {
				Partyinfo temp = new Partyinfo();
				String USER_LOGIN_ID = rs.getString("USER_LOGIN_ID");
				String PARTY_ID = rs.getString("PARTY_ID");
				temp.setPartyId(PARTY_ID);
				temp.setUserLoginId(USER_LOGIN_ID);
				SecurityGroupinfolist.add(temp);
			}
			returnmap.put("SecurityGroupinfolist", SecurityGroupinfolist);
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnmap;
	}
	
	
	
	public static Map<String, Object> FindSecurityGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> returnmap = ServiceUtil.returnSuccess();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		
		String sql="select * from SECURITY_GROUP ";
		
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		List<SecurityGroupinfo> SecurityGroupinfolist=new ArrayList<SecurityGroupinfo>();
		try {
			ResultSet rs = processor.executeQuery(sql);
			while (rs.next()) {
				SecurityGroupinfo temp = new SecurityGroupinfo();
				String GROUP_ID = rs.getString("GROUP_ID");
				String DESCRIPTION = rs.getString("DESCRIPTION");
				temp.setGroupId(GROUP_ID);
				temp.setDescription(DESCRIPTION);
				SecurityGroupinfolist.add(temp);
			}
			returnmap.put("SecurityGroupinfolist", SecurityGroupinfolist);
		} catch (GenericDataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnmap;
	}
	
	static void makeCondition(StringBuffer sql,String name,String Num,String value,String flag){
		if(value == null || value.equals("")) return ;
		switch(Integer.parseInt(Num)){
		case 1:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") like '%"+value.toUpperCase()+"%'");
			}else{
				sql.append(" and "+name+" like '%"+value+"%'");
			}
			break;
		case 2:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") like '"+value.toUpperCase()+"%'");
			}else{
				sql.append(" and "+name+" like '"+value+"%'");
			}
			break;
		case 3:
			if(flag.equals("1")){
				sql.append(" and upper("+name+") ='"+value.toUpperCase()+"'");
			}else{
				sql.append(" and "+name+"='"+value+"'");
			}
			break;
		}
	}
	public static JSONObject makeParams(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		StringBuffer jsonStringBuffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonStringBuffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}		
		String productPriceList =  jsonStringBuffer.toString();
		JSONObject json = JSON.parseObject(productPriceList);
		return json;
	}
	
	
	public static Map<String, Object> BatchAddSecurityGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> returnmap = ServiceUtil.returnSuccess();
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		JSONObject json = makeParams(request);	
									
		
		JSONArray securityGrouplist = json.getJSONArray("securityGrouplist");
		JSONArray Partyinfolist = json.getJSONArray("partyinfolist");
		
		List<GenericValue> toBeStored = new ArrayList<GenericValue>();
		
		for(int i=0;i<Partyinfolist.size();i++)
		{
			JSONObject temp = Partyinfolist.getJSONObject(i);
			for(int j=0;j<securityGrouplist.size();j++)
			{
				JSONObject sectemp = securityGrouplist.getJSONObject(j);
				GenericValue UserLoginSecurityGroupbean = delegator.makeValue("UserLoginSecurityGroup");
				UserLoginSecurityGroupbean.set("userLoginId",  temp.getString("userLoginId"));
				UserLoginSecurityGroupbean.set("groupId", sectemp.getString("groupId"));
				UserLoginSecurityGroupbean.set("fromDate",sectemp.getString("fromDate"));
				UserLoginSecurityGroupbean.set("thruDate",sectemp.getString("thruDate"));
				toBeStored.add(UserLoginSecurityGroupbean);
				
			}
			
		}

	try {
		if(toBeStored.size()>=1)
			delegator.storeAll(toBeStored);
	} catch (GenericEntityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return returnmap;
	}

}
