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

package com.opensourcestrategies.crmsfa.content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.ofbiz.base.util.Base64;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.content.data.DataResourceWorker;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.security.Security;
import org.opentaps.common.util.UtilCommon;
import org.opentaps.common.util.UtilMessage;

import com.opensourcestrategies.crmsfa.security.CrmsfaSecurity;
/**
 * Content servlet methods.
 *
 * @author Leon Torres (leon@opensourcestrategies.com)
 */
public final class ContentEvents {

    private ContentEvents() { }

    private static final String MODULE = ContentEvents.class.getName();

    /**
     * Download content for a party.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the <code>String</code> content value.
     */
    public static String downloadPartyContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        String contentId = request.getParameter("contentId");
        String partyId = request.getParameter("partyId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // get the module corresponding to the internal party
            String module = CrmsfaSecurity.getSecurityModuleOfInternalParty(partyId, delegator);
            if (module == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // ensure association exists between our party and content (ignore role because we're using module to check for security)
            List<EntityCondition> conditions = UtilMisc.<EntityCondition>toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("partyId", partyId),
                    EntityUtil.getFilterByDateExpr());
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("ContentRole", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if userLogin has permission to view
            if (!CrmsfaSecurity.hasPartyRelationSecurity(security, module, "_VIEW", userLogin, partyId)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }

    /**
     * Download content for a case.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the <code>String</code> content value.
     */
    public static String downloadCaseContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        String contentId = request.getParameter("contentId");
        String custRequestId = request.getParameter("custRequestId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // ensure association exists between case and content
            List<EntityCondition> conditions = UtilMisc.<EntityCondition>toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("custRequestId", custRequestId),
                    EntityUtil.getFilterByDateExpr());
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("CustRequestContent", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if the userLogin has case view permission
            if (!CrmsfaSecurity.hasCasePermission(security, "_VIEW", userLogin, custRequestId)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }

    /**
     * Download content for an opportunity.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the <code>String</code> content value.
     */
    public static String downloadOpportunityContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        String contentId = request.getParameter("contentId");
        String salesOpportunityId = request.getParameter("salesOpportunityId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // ensure association exists between case and content
            List<EntityCondition> conditions = UtilMisc.<EntityCondition>toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("salesOpportunityId", salesOpportunityId),
                    EntityUtil.getFilterByDateExpr()
                    );
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("SalesOpportunityContent", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if the userLogin has case view permission
            if (!CrmsfaSecurity.hasOpportunityPermission(security, "_VIEW", userLogin, salesOpportunityId)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }

    /**
     * Download content for an activity.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the <code>String</code> content value.
     */
    public static String downloadActivityContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        String contentId = request.getParameter("contentId");
        String workEffortId = request.getParameter("workEffortId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // ensure association exists between case and content
            List<EntityCondition> conditions = UtilMisc.<EntityCondition>toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("workEffortId", workEffortId),
                    EntityUtil.getFilterByDateExpr()
                    );
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("WorkEffortContent", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if the userLogin has case view permission
            if (!CrmsfaSecurity.hasActivityPermission(security, "_VIEW", userLogin, workEffortId)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }


    /** Get the contentId and verify that we have something to download */
    private static GenericValue getDataResource(HttpServletRequest request) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        GenericValue content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", request.getParameter("contentId")));
        if (content == null) {
            return null;
        }

        GenericValue dataResource = content.getRelatedOne("DataResource");
        if (dataResource == null || !"LOCAL_FILE".equals(dataResource.get("dataResourceTypeId"))) {
            return null;
        }
        return dataResource;
    }

    /** Find the file and write it to the client stream. */
    private static String serveDataResource(HttpServletRequest request, HttpServletResponse response, GenericValue dataResource) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        Locale locale = UtilHttp.getLocale(request);
        ServletContext application = session.getServletContext();
        Map<String, Object> input = UtilMisc.<String, Object>toMap("contentId", request.getParameter("contentId"));
        try {
            String fileLocation = dataResource.getString("objectInfo");
            String fileName = dataResource.getString("dataResourceName");

            // the file name needs to be UTF8 urlencoded for the content disposition HTTP header
            fileName = "=?UTF-8?B?" + new String(Base64.base64Encode(fileName.getBytes("UTF-8")), "UTF-8") + "?=";

            if (UtilValidate.isEmpty(fileLocation)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", input, locale, MODULE);
            }

            // test if the file exists here, due to strange bugs with DataResourceWorker.streamDataResource
            File file = new File(fileLocation);
            if (!file.exists()) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", input, locale, MODULE);
            }

            // Set the headers so that the browser treats content as a download (this could be changed to use the mimeTypeId of the content for in-browser display)
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // write the file to the client browser
            OutputStream os = response.getOutputStream();
            DataResourceWorker.streamDataResource(os, delegator, dataResource.getString("dataResourceId"), "", application.getInitParameter("webSiteId"), UtilHttp.getLocale(request), application.getRealPath("/"));
            os.flush();
            return "success";
        } catch (GeneralException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        } catch (IOException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }

    /**
     * Download content for an order.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the <code>String</code> content value.
     */
    public static String downloadOrderContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        String contentId = request.getParameter("contentId");
        String orderId = request.getParameter("orderId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // ensure association exists between case and content
            List<EntityCondition> conditions = UtilMisc.<EntityCondition>toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("orderId", orderId),
                    EntityUtil.getFilterByDateExpr());
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("OrderHeaderContent", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if the userLogin has case view permission
            if (!CrmsfaSecurity.hasOrderPermission(security, "_VIEW", userLogin, orderId)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);
        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }

    /**
     * Downloads quote content.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return a <code>String</code> value
     */
    public static String downloadQuoteContent(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Security security = (Security) request.getAttribute("security");
        Locale locale = UtilHttp.getLocale(request);

        String contentId = UtilCommon.getParameter(request, "contentId");
        String quoteId = UtilCommon.getParameter(request, "quoteId");

        try {
            GenericValue dataResource = getDataResource(request);
            if (dataResource == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorContentNotFound", UtilMisc.toMap("contentId", contentId), locale, MODULE);
            }

            // ensure association exists between quote and content
            List<EntityCondition> conditions = UtilMisc.toList(
                    EntityCondition.makeCondition("contentId", contentId),
                    EntityCondition.makeCondition("quoteId", quoteId),
                    EntityUtil.getFilterByDateExpr());
            GenericValue association = EntityUtil.getFirst(delegator.findByAnd("QuoteContent", conditions));
            if (association == null) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            // check if the userLogin has quote view permission
            if (!security.hasEntityPermission("CRMSFA_QUOTE", "_VIEW", session)) {
                return UtilMessage.createAndLogEventError(request, "CrmErrorPermissionDenied", locale, MODULE);
            }

            return serveDataResource(request, response, dataResource);

        } catch (GenericEntityException e) {
            return UtilMessage.createAndLogEventError(request, e, locale, MODULE);
        }
    }
    
    
	public static String ExportOrderInfo(HttpServletRequest request, HttpServletResponse response) {
		
		
		Map<String, Object> ret = null;
		List<String> excldataList=new ArrayList<String>();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String orderId = (String) request.getParameter("orderId");
		String ordertotal = (String)request.getParameter("ordertotal");
		String querysql="SELECT"
				+ " oh.ORDER_ID,oh.STATUS_ID,oh.ORDER_DATE,oh.PRODUCT_STORE_ID,oh.BUYER_ID,oi.PRODUCT_ID,pes.FIRST_NAME_LOCAL,oi.QUANTITY,oi.ITEM_DESCRIPTION,oi.UNIT_PRICE,oh.GRAND_TOTAL,oi.UNIT_RECURRING_PRICE,oisgir.INVENTORY_ITEM_ID,ii.SERIAL_NUMBER,ps.STORE_NAME,oh.CURRENCY_UOM "
				+ " FROM "
				+ " ORDER_HEADER oh"
				+ " LEFT JOIN ORDER_ITEM AS oi ON oh.ORDER_ID = oi.ORDER_ID"
				+ " LEFT JOIN ORDER_ITEM_SHIP_GRP_INV_RES AS oisgir ON oisgir.ORDER_ID = oi.ORDER_ID"
				+ " AND oisgir.ORDER_ITEM_SEQ_ID = oi.ORDER_ITEM_SEQ_ID"
				+ " left JOIN INVENTORY_ITEM as ii on ii.INVENTORY_ITEM_ID=oisgir.INVENTORY_ITEM_ID"
				+ " left join PRODUCT_STORE as ps on ps.PRODUCT_STORE_ID=oh.PRODUCT_STORE_ID"
				+ " left JOIN PERSON as pes on pes.PARTY_ID=oh.BUYER_ID"
				+ " where oh.ORDER_ID=\'"+orderId+"\'";
		
		
		//包装参数
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);	
		ResultSet rs;
		String STATUS_ID="";
		String ORDER_DATE="";
		String STORE_NAME="";
		String BUYER_ID="";
		String GRAND_TOTAL="";
		String CURRENCY_UOM="";
		List<String> datelist = new ArrayList<String>();
		try {
			rs = processor.executeQuery(querysql);
			
			datelist = new ArrayList<String>();
			boolean isfrist=true;
			int index=1;
			while (rs.next()) {
				if(isfrist)
				{
					STATUS_ID = rs.getString("STATUS_ID");
					ORDER_DATE = rs.getString("ORDER_DATE");
					STORE_NAME = rs.getString("STORE_NAME");
					BUYER_ID = rs.getString("FIRST_NAME_LOCAL");
					GRAND_TOTAL = rs.getString("GRAND_TOTAL");
					CURRENCY_UOM = rs.getString("CURRENCY_UOM");
					isfrist=false;
				}
				
				String PRODUCT_ID = rs.getString("PRODUCT_ID");
				String ITEM_DESCRIPTION = rs.getString("ITEM_DESCRIPTION");
				ITEM_DESCRIPTION=ITEM_DESCRIPTION.replaceAll(",", " ");
				String QUANTITY = rs.getString("QUANTITY");
				String UNIT_PRICE = rs.getString("UNIT_PRICE");
				String UNIT_RECURRING_PRICE = rs.getString("UNIT_RECURRING_PRICE");
				String SERIAL_NUMBER = rs.getString("SERIAL_NUMBER");
				
				if(ITEM_DESCRIPTION==null||"".equals(ITEM_DESCRIPTION))
					ITEM_DESCRIPTION="----";
				if(SERIAL_NUMBER==null||"".equals(SERIAL_NUMBER))
					SERIAL_NUMBER="----";
				if(QUANTITY==null||"".equals(QUANTITY))
					QUANTITY="0.0";
				if(UNIT_PRICE==null||"".equals(UNIT_PRICE))
					UNIT_PRICE="0.0";
				if(UNIT_RECURRING_PRICE==null||"".equals(UNIT_RECURRING_PRICE))
					UNIT_RECURRING_PRICE="0.0";
				Double QUANTITYint = Double.parseDouble(QUANTITY);
				double UNIT_PRICEdouble = Double.parseDouble(UNIT_PRICE);
				double UNIT_RECURRING_PRICEdouble = Double.parseDouble(UNIT_RECURRING_PRICE);
				
				datelist.add(index+","+PRODUCT_ID+"-"+ITEM_DESCRIPTION+"\t,"+SERIAL_NUMBER+"\t,"+QUANTITYint.intValue()+"\t,"+CURRENCY_UOM+UNIT_PRICEdouble+"\t,"+CURRENCY_UOM+(UNIT_PRICEdouble-UNIT_RECURRING_PRICEdouble)+"\t,"+CURRENCY_UOM+(QUANTITYint*UNIT_PRICEdouble));
				
				index++;
			}
		} catch (GenericDataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GenericEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ORDER_DATE=getLocalizedDate(ORDER_DATE, request);
		
		excldataList.add("Order # "+orderId+"\t,Status:"+STATUS_ID);
		excldataList.add("");
		excldataList.add("Store,Date Ordered,Customer");
		excldataList.add(STORE_NAME+"\t,"+ORDER_DATE+"\t,"+BUYER_ID);
		excldataList.add("");
		excldataList.add("Order Items");
		excldataList.add("No.,Product,IMEI,Quantity,Unit Price,Adjustments(per unit),Subtotal");
		for(int i=0;i<datelist.size();i++)
		{
			excldataList.add(datelist.get(i));
		}
		excldataList.add("");
		excldataList.add(",,,,,Order Total:,\t"+CURRENCY_UOM+Double.parseDouble(ordertotal));
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("contentType", "text/html; charset=UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=Order#"+orderId+".zip");
		
        String retmaptemp = getTempFilepath();
        File  tempfilefolder= new File(retmaptemp);
        File  tempfile= new File(retmaptemp+"/Order#"+orderId+".csv");
        if (!tempfilefolder.exists()) {
        	tempfilefolder.mkdirs();
           }
        if (!tempfile.exists()) {
        	try {
        		tempfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
        FileOutputStream fop = null;
        try {
        	
        	
        	fop = new FileOutputStream(tempfile);
        	
            for(String data : excldataList) {
            	fop.write(data.getBytes());
            	fop.write("\n".getBytes());
            }	   
            fop.flush();
            fop.close();
        }
        catch (IOException e) {        	
            e.printStackTrace();
        }
        finally{
            try {
                if(fop != null){
                	fop.flush();
                	fop.close();
                }
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
        }
        try {
        	ZipUtilss.doCompress(tempfile, response.getOutputStream());
			tempfile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
        return "success";

	}
	
	
	public static Map<String, Object> getSystemSettings() {
		Map<String, Object> retmap=new HashMap<String, Object>();
		
		String key = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsUomid", "UGX");
		retmap.put("systemsettinguomid", key.trim());
		String isShip = UtilProperties.getPropertyValue("SystemSettings.properties", "isShip");
		String SystemSettingIsShowImei = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingIsShowImei");
		String countryId = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsCountries");
		String egateeCost = UtilProperties.getPropertyValue("SystemSettings.properties", "EgateeCost");
		String specialCost = UtilProperties.getPropertyValue("SystemSettings.properties", "SpecialCost");
		String retailCost = UtilProperties.getPropertyValue("SystemSettings.properties", "RetailCost");
		String proxyHost = UtilProperties.getPropertyValue("SystemSettings.properties", "proxyHost");
		String tempfilepath = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsTempFilePath");
		
		String SystemSettingsSendemailUserName = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailUserName");
		String SystemSettingsSendemailAuthorizationCode = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailAuthorizationCode");
		String SystemSettingsSendemailRecipient = UtilProperties.getPropertyValue("SystemSettings.properties", "SystemSettingsSendemailRecipient");
		
		
		retmap.put("isShip", isShip);
		retmap.put("imei", SystemSettingIsShowImei);
		retmap.put("countryId", countryId.trim());
		retmap.put("egateeCost", egateeCost);
		retmap.put("specialCost", specialCost);
		retmap.put("retailCost", retailCost);
		retmap.put("proxyHost", proxyHost);
		retmap.put("tempfilepath", tempfilepath);
		
		retmap.put("SystemSettingsSendemailUserName", SystemSettingsSendemailUserName.trim());
		retmap.put("SystemSettingsSendemailAuthorizationCode", SystemSettingsSendemailAuthorizationCode.trim());
		retmap.put("SystemSettingsSendemailRecipient", SystemSettingsSendemailRecipient.trim());
		
		return retmap;
	}
	public static String getTempFilepath() {
		Map<String, Object> ret = getSystemSettings();
		String tempfilepath = (String)ret.get("tempfilepath");
		if(tempfilepath==null)
			tempfilepath="";
		return tempfilepath;
	}
	public static String getLocalizedDate(String dateStr,HttpServletRequest request) {
		String d = "";
		if(!StringUtils.isEmpty(dateStr) && StringUtils.contains(dateStr, ".")){
			dateStr = dateStr.substring(0,dateStr.lastIndexOf("."));
			Locale locale = request.getLocale();
			TimeZone timeZone = getTimeZone(request);
			DateFormat dateFormat = toDateTimeFormat("dd/MM/yyyy HH:mm:ss",timeZone,locale);
			d = dateFormat.format(Timestamp.valueOf(dateStr));
	    }
		return d;
	}
	
    public static TimeZone getTimeZone(HttpServletRequest request) {
        HttpSession session = request.getSession();
        TimeZone timeZone = (TimeZone) session.getAttribute("timeZone");
        if (timeZone == null) {
            String tzId = null;
            Map<String, String> userLogin = UtilGenerics.cast(session.getAttribute("userLogin"));
            if (userLogin != null) {
                tzId = userLogin.get("lastTimeZone");
            }
            timeZone = UtilDateTime.toTimeZone(tzId);
            session.setAttribute("timeZone", timeZone);
        }
        return timeZone;
    }
    public static DateFormat toDateTimeFormat(String dateTimeFormat, TimeZone tz, Locale locale) {
        DateFormat df = null;
        if (dateTimeFormat == null) {
            df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
        } else {
            df = new SimpleDateFormat(dateTimeFormat);
        }
        df.setTimeZone(tz);
        return df;
    }
}
