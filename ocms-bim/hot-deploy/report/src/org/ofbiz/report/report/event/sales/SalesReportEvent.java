package org.ofbiz.report.report.event.sales;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.report.report.been.SalesFooterBean;
import org.ofbiz.report.report.been.SalesReportBean;
import org.ofbiz.report.report.been.ExportSalesReport;
import org.ofbiz.report.report.been.SalesTableBean;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.report.report.helper.sales.SalesReportHelper;
import org.ofbiz.report.report.helper.sales.BaseSalesReportHelper;
import org.ofbiz.report.security.ReportSecurity;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;
import org.ofbiz.util.ParaUtils;
import org.ofbiz.util.ZipUtils;

public class SalesReportEvent {

	/**
	 * 联想查询product
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchProductsByLikeID(HttpServletRequest request, HttpServletResponse response) {
		return BaseSalesReportEvent.searchProductsByLikeID(request, response);
	}

	

	/**
	 * 销售报表初始化，查询可用店铺和店铺租
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchAvailableStoresAndSGroups(HttpServletRequest request,
			HttpServletResponse response) {

		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		return searchAvailableStoresAndSGroups(delegator, userLogin);
	}

	public static Map<String, Object> searchAvailableStoresAndSGroups(GenericDelegator delegator,
			GenericValue userLogin) {
		ReportSecurity baneportSecurity = new ReportSecurity(delegator);
		return BaseSalesReportEvent.searchStoreBySecurity(delegator, userLogin,baneportSecurity);
	}




	//chenshihua 2017-7-3 edit
	/**
	 * 条件查询报表
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> searchMarketByConditions(HttpServletRequest request,
			HttpServletResponse response) {
		GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		String productId = ParaUtils.getParameter(request, "productId");

		String sGroupId = ParaUtils.getParameter(request, "sGroupId");
		if (sGroupId == null)
			sGroupId = "";
		String storeId = ParaUtils.getParameter(request, "storeId");
		if (storeId == null)
			storeId = "";
		String beginDate = ParaUtils.getParameter(request, "beginDate");
		String endDate = ParaUtils.getParameter(request, "endDate");

		if (UtilValidate.isEmpty(beginDate) || (UtilValidate.isEmpty(endDate))) {
			return ServiceUtil.returnError("time info error");
		} else {
			beginDate = beginDate.replace(" ", "");
			endDate = endDate.replace(" ", "");
		}
		List<SalesTableBean> tableBeans = null;
		List<SalesFooterBean> footerBeans = null;
		List<SalesReportBean> salesReportBeanList = null;
		Map<String, Object> map = ServiceUtil.returnSuccess();
		try {
			TransactionUtil.begin();
			Map<String, Object> contion = getSGandSTR(delegator, userLogin, sGroupId, storeId);
			//chenshihua	2017-6-27	修改返回参数
			/*tableBeans = SalesReportHelper.searchTableBean(delegator, productId, contion, beginDate, endDate,
					userLogin);*/
			//footerBeans
			footerBeans = BaseSalesReportHelper.searchFooterBean(delegator, productId, contion, beginDate, endDate , request);
			for (SalesFooterBean item : footerBeans) {
				item.parseMoneyAndQuantity();
			}
			map.put("footerBean", footerBeans);
			
			
			//获取国家id
			LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> context = new HashMap<String, Object>();
			Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
			String countryId = (String) uomInfo.get("countryId");
			
			BaseSalesReportHelper baseSalesReportHelper = new BaseSalesReportHelper();
			
			TransactionUtil.commit();
			Security security = (Security) request.getAttribute("security");
			//加纳报表查询
			if("GHA".equals(countryId)){
				if(security.hasEntityPermission("REPORTS", "_ORG_COST", userLogin)){
					List<SalesReportBean> salesReportBeanList_GHA = baseSalesReportHelper.getSalesReport(delegator, productId, contion, beginDate, endDate,
							userLogin , request);
					for (SalesReportBean salesReportBean : salesReportBeanList_GHA) {
						salesReportBean.parseMoneyAndQuantity();
						salesReportBean.hideNeedlessDataGHAAdmin();
					}
					List<String> tableHeadList_GHA = Arrays.asList("Order Time", "Category", "Product Id" , 
							"Description" , "Quantity" , "Unit Price" , "Unit Cost" , "Sub Total" , "Cost" ,
							"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
					map.put("tableHeadList", tableHeadList_GHA);
					map.put("role" , "GHA_ADMIN");
					map.put("tableBean", salesReportBeanList_GHA);
					
					return map;
				}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
					List<SalesReportBean> salesReportBeanList_GHA = baseSalesReportHelper.getSalesReport(delegator, productId, contion, beginDate, endDate,
							userLogin , request);
					for (SalesReportBean salesReportBean : salesReportBeanList_GHA) {
						salesReportBean.parseMoneyAndQuantity();
						salesReportBean.hideNeedlessDataGHAManage();
					}
					List<String> tableHeadList_GHA = Arrays.asList("Order Time", "Category", "Product Id" , 
							"Description" , "Quantity" , "Unit Price" , "Sub Total" ,
							"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
					map.put("tableHeadList", tableHeadList_GHA);
					map.put("role" , "GHA_MANAGE");
					map.put("tableBean", salesReportBeanList_GHA);
					return map;
				}
			}else if("UGA".equals(countryId)){
				if(security.hasEntityPermission("REPORTS", "_ALL_COST", userLogin)){
					//乌干达超级管理员
					List<SalesReportBean> salesReportBeanList_S = baseSalesReportHelper.getSalesReport(delegator, productId, contion, beginDate, endDate,
							userLogin , request);
					for (SalesReportBean salesReportBean : salesReportBeanList_S) {
						salesReportBean.parseMoneyAndQuantity();
					}
					List<String> tableHeadList_S = Arrays.asList("Order Time", "Category", "Product Id" ,  
							"Description" , "Quantity" , "Unit Price" ,  "Sub Total" , 
							"Retail Cost", "Operator Cost", "Egatee Cost", "Org Cost",
							"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
					map.put("tableHeadList", tableHeadList_S);
					map.put("tableBean", salesReportBeanList_S);
					map.put("role" , "UGA_SUPER_ADMIN");
					return map;
				}else if(security.hasEntityPermission("REPORTS", "_RET_COST", userLogin)){
					//乌干达角色判定
					List<SalesReportBean> salesReportBeanList_UGA = baseSalesReportHelper.getSalesReport(delegator, productId, contion, beginDate, endDate,
							userLogin , request);
					//乌干达店长可视数据
					for (SalesReportBean salesReportBean : salesReportBeanList_UGA) {
						salesReportBean.parseMoneyAndQuantity();
						salesReportBean.hideNeedlessDataUGA();
					}
					List<String> tableHeadList_UGA = Arrays.asList("Order Time", "Category", "Product Id" , 
							"Description" , "Quantity" , "Unit Price" , "Unit Cost" , "Sub Total" , "Cost" ,
							"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
					map.put("tableHeadList", tableHeadList_UGA);
					map.put("role" , "UGA_MAX_MANAGE");
					map.put("tableBean", salesReportBeanList_UGA);
					return map;
				}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
					//乌干达角色判定
					List<SalesReportBean> salesReportBeanList_UGA = baseSalesReportHelper.getSalesReport(delegator, productId, contion, beginDate, endDate,
							userLogin , request);
					//乌干达店长可视数据
					for (SalesReportBean salesReportBean : salesReportBeanList_UGA) {
						salesReportBean.parseMoneyAndQuantity();
						salesReportBean.hideNeedlessDataUGAManage();
					}
					List<String> tableHeadList_UGA = Arrays.asList("Order Time", "Category", "Product Id" , 
							"Description" , "Quantity" , "Unit Price" , "Sub Total" ,
							"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
					map.put("tableHeadList", tableHeadList_UGA);
					map.put("role" , "UGA_MIN_MANAGE");
					map.put("tableBean", salesReportBeanList_UGA);
					return map;
				}
				
			}
			return ServiceUtil.returnError("have no permission");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}
		
	}

	// 获取校验后的店铺组与店铺，后直接加入SQL中
	public static Map<String, Object> getSGandSTR(GenericDelegator delegator, GenericValue userLoginGV, String sGroupId,
			String storeId) {
		Map<String, Object> ret = searchAvailableStoresAndSGroups(delegator, userLoginGV);
		
		return BaseSalesReportEvent.checkAvailableStoreScope(sGroupId, storeId, ret);
	}
	
	
	
	
	//导出销售表表
		public static Map<String, Object> getExportSalesReport(HttpServletRequest request,
				HttpServletResponse response) {
			GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
			GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

			String productId = ParaUtils.getParameter(request, "productId");

			String sGroupId = ParaUtils.getParameter(request, "sGroupId");
			if (sGroupId == null)
				sGroupId = "";
			String storeId = ParaUtils.getParameter(request, "storeId");
			if (storeId == null)
				storeId = "";
			String beginDate = ParaUtils.getParameter(request, "beginDate");
			String endDate = ParaUtils.getParameter(request, "endDate");

			if (UtilValidate.isEmpty(beginDate) || (UtilValidate.isEmpty(endDate))) {
				return ServiceUtil.returnError("time info error");
			} else {
				beginDate = beginDate.replace(" ", "");
				endDate = endDate.replace(" ", "");
			}
			List<SalesTableBean> tableBeans = null;
			List<SalesFooterBean> footerBeans = null;
			List<ExportSalesReport> exportSalesReportList = null;
			Map<String, Object> map = ServiceUtil.returnSuccess();
			try {
				TransactionUtil.begin();
				Map<String, Object> contion = getSGandSTR(delegator, userLogin, sGroupId, storeId);
				exportSalesReportList = BaseSalesReportHelper.getExportSalesReportBeanList(delegator, productId, contion, beginDate, endDate,
						userLogin , request);
				TransactionUtil.commit();
				map.put("exportSalesReportList", exportSalesReportList);
			} catch (Exception e) {
				e.printStackTrace();
				return ServiceUtil.returnError("search error");
			}
			
			
			
			
			
			return map;
		}
		
		public static String exportSalesReport(HttpServletRequest request,
				HttpServletResponse response) {
			String type = request.getParameter("type");
			if("summary".equals(type)){
				return BaseSalesReportEvent.exportPaymentSummary(request, response);
			}else if("detail".equals(type)){
				return exportDetailSalesReport(request, response);
			}else{
				return "error";
			}
			
		}
		
		
		public static String exportDetailSalesReport(HttpServletRequest request,
				HttpServletResponse response) {
			try {
				Map<String, Object> receiveMap = getExportSalesReport(request , response);
				GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
				if("error".equals(receiveMap.get("responseMessage"))){
					return "error";
				}
				
				List<ExportSalesReport> exportSalesReportList = (List<ExportSalesReport>)receiveMap.get("exportSalesReportList");
				List<String> dataList = new ArrayList<String>();
				Security security = (Security) request.getAttribute("security");
				
				
				
				
				//获取国家id
				LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
				Map<String, Object> context = new HashMap<String, Object>();
				Map<String, Object> uomInfo;
				uomInfo = dispatcher.runSync("getSystemSettings", context);
				String countryId = (String) uomInfo.get("countryId");
				
				//加纳报表
				if("GHA".equals(countryId)){
					if(security.hasEntityPermission("REPORTS", "_ORG_COST", userLogin)){
						dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
								+ "Actual Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
								+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
								+ "Third SubCategory,UNIT COST,TOTAL COST,SalesMethod,Sales Method");
//						dataList.add("公司名称,店铺组,店名称,订单号,产品ID,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//								+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位成本 ,成本 ,渠道,销售方式");
						for (ExportSalesReport item : exportSalesReportList) {
							item.parseMoneyAndQuantity();
							String str = item.storeManageToStringGHA();
							dataList.add(str);
						}
					}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
						dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
								+ "Actual Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
								+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
								+ "Third SubCategory,SalesMethod,Sales Method");
//						dataList.add("公司名称,店铺组,店名称,订单号,产品ID,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//								+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位零售成本 ,零售成本 ,渠道,销售方式");
						for (ExportSalesReport item : exportSalesReportList) {
							item.parseMoneyAndQuantity();
							System.out.println(item.toString());
							String str = item.storeManageToStringUGAMin();
							dataList.add(str);
						}
					}
					
				}else if("UGA".equals(countryId)){
					if(security.hasEntityPermission("REPORTS", "_ALL_COST", userLogin)){
						dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
								+ "Actual Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
								+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
								+ "Third SubCategory,UNIT RETAIL COST,TOTAL RETAIL COST,UNIT EGATEE COST,TOTAL EGATEE COST,"
								+ "UNIT SPECIAL COST,TOTAL SPECIAL COST,UNIT ORG COST,TOTAL ORG COST,SalesMethod,Sales Method");
//						dataList.add("公司名称,店铺组,店名称,订单号,产品ID,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//								+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位零售成本 ,零售成本,单位EGATEE成本,EGATEE成本,单位运营商成本,运营商成本,单位进货成本,进货成本,渠道,销售方式");
						for (ExportSalesReport item : exportSalesReportList) {
							item.parseMoneyAndQuantity();
							String str = item.adminExportString();
							dataList.add(str);
						}
					}else if(security.hasEntityPermission("REPORTS", "_RET_COST", userLogin)){
						dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
								+ "Actual Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
								+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
								+ "Third SubCategory,UNIT RETAIL COST,TOTAL RETAIL COST,SalesMethod,Sales Method");
//						dataList.add("公司名称,店铺组,店名称,订单号,产品ID,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//								+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位零售成本 ,零售成本 ,渠道,销售方式");
						for (ExportSalesReport item : exportSalesReportList) {
							item.parseMoneyAndQuantity();
							System.out.println(item.toString());
							String str = item.storeManageToStringUGAMax();
							dataList.add(str);
						}
					}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
						dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
								+ "Actual Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
								+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
								+ "Third SubCategory,SalesMethod,Sales Method");
//						dataList.add("公司名称,店铺组,店名称,订单号,产品ID,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//								+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位零售成本 ,零售成本 ,渠道,销售方式");
						for (ExportSalesReport item : exportSalesReportList) {
							item.parseMoneyAndQuantity();
							System.out.println(item.toString());
							String str = item.storeManageToStringUGAMin();
							dataList.add(str);
						}
					}
				}
				
				
				
				
				response.setCharacterEncoding("UTF-8");
		        response.setHeader("contentType", "text/html; charset=UTF-8");
		        response.setContentType("application/octet-stream");
		        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//设置日期格式
		        SimpleDateFormat filedf = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");//设置日期格式
		        //yzl20170711导出的csv文件进行压缩成izp
		        response.addHeader("Content-Disposition", "attachment; filename=SalesReport"+df.format(new Date())+".zip");
				
		        String retmaptemp = BaseReportEvent.getTempFilepath();
		        File  tempfilefolder= new File(retmaptemp);
		        File  tempfile= new File(retmaptemp+"/SalesReport"+filedf.format(new Date())+".csv");
		        if (!tempfilefolder.exists()) {
		        	tempfilefolder.mkdirs();
		           }
		        if (!tempfile.exists()) {
		        	try {
		        		tempfile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
		           }
		        FileOutputStream fop = null;
		        try {
		        	
		        	
		        	fop = new FileOutputStream(tempfile);
		        	
		            for(String data : dataList) {
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
					ZipUtils.doCompress(tempfile, response.getOutputStream());
					tempfile.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return "success";
		        //end

			} catch (GenericServiceException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		//报表查询返回表头		chenshihua	2017-7-3
		public static Map<String, Object> getReportTableHead(HttpServletRequest request,
				HttpServletResponse response) {
			Map<String, Object> map = ServiceUtil.returnSuccess();
			try {
				
				GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
				//获取国家id
				LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
				Map<String, Object> context = new HashMap<String, Object>();
				Map<String, Object> uomInfo;
				uomInfo = dispatcher.runSync("getSystemSettings", context);
				String countryId = (String) uomInfo.get("countryId");
				//乌干达角色判定
				Security security = (Security) request.getAttribute("security");
				//加纳报表查询
				if("GHA".equals(countryId)){
					//REPORTS_ORG_COST   REPORTS_ORG_COST	REPORTS_ORG_COST
					if(security.hasEntityPermission("REPORTS", "_ORG_COST", userLogin)){
						List<String> tableHeadList = Arrays.asList("Order Time", "Category", "Product Id" , 
								"Description" , "Quantity" , "Unit Price" , "Unit Cost" , "Sub Total" , "Cost" ,
								"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
						map.put("tableHeadList", tableHeadList);
						map.put("role" , "GHA_ADMIN");
						map.put("countryId", countryId);
						return map;
					}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
						List<String> tableHeadList = Arrays.asList("Order Time", "Category", "Product Id" , 
								"Description" , "Quantity" , "Unit Price" ,  "Sub Total" , 
								"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
						map.put("tableHeadList", tableHeadList);
						map.put("role" , "GHA_MANAGE");
						map.put("countryId", countryId);
						return map;
					}
				}else if("UGA".equals(countryId)){
					if(security.hasEntityPermission("REPORTS", "_ALL_COST", userLogin)){
						List<String> tableHeadList = Arrays.asList("Order Time", "Category", "Product Id" ,  
								"Description" , "Quantity" , "Unit Price" ,  "Sub Total" , 
								"Retail Cost", "Operator Cost", "Egatee Cost", "Org Cost",
								"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
						map.put("tableHeadList", tableHeadList);
						map.put("role" , "UGA_SUPER_ADMIN");
						map.put("countryId", countryId);
						return map;
					}else if(security.hasEntityPermission("REPORTS", "_RET_COST", userLogin)){
						List<String> tableHeadList = Arrays.asList("Order Time", "Category", "Product Id" , 
								"Description" , "Quantity" , "Unit Price" , "Unit Cost" , "Sub Total" , "Cost" ,
								"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
						map.put("tableHeadList", tableHeadList);
						map.put("role" , "UGA_MAX_MANAGE");
						map.put("countryId", countryId);
						return map;
					}else if(security.hasEntityPermission("REPORTS", "_NONE_COST", userLogin)){
						List<String> tableHeadList = Arrays.asList("Order Time", "Category", "Product Id" , 
								"Description" , "Quantity" , "Unit Price" ,  "Sub Total" , 
								"Adjustment" , "Payment" , "Receivables" , "Sales Channel" , "Store");
						map.put("tableHeadList", tableHeadList);
						map.put("role" , "UGA_MIN_MANAGE");
						map.put("countryId", countryId);
						return map;
					}
				}else{
					return ServiceUtil.returnError("have no permission");
				}
				
			} catch (GenericServiceException e) {
				e.printStackTrace();
			}
			return ServiceUtil.returnError("have no permission");
		}
		
		
		
		public static String test(HttpServletRequest request,HttpServletResponse response) {
			
			try {
				GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
				GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
				SQLProcessor prosor = new SQLProcessor(helperInfo);
				String partyId = request.getParameter("partyId");
				String sql = " SELECT  "
						+" per.PARTY_ID AS partyId, "
						+" oh.ORDER_ID AS orderId, "
						+" tn1.CONTACT_MECH_ID AS tn1ContactMechId, "
						+" pa1.CONTACT_MECH_ID AS pa1ContactMechId, "
						+" tn2.CONTACT_MECH_ID AS tn2ContactMechId, "
						+" pa2.CONTACT_MECH_ID AS pa2ContactMechId "
						+" FROM "
						+" PERSON per "
						+" LEFT JOIN "
						+" ORDER_HEADER oh ON per.PARTY_ID = oh.BUYER_ID "
						+" LEFT JOIN "
						+" PARTY_CONTACT_MECH pcm ON pcm.PARTY_ID = per.PARTY_ID "
						+" LEFT JOIN "
						+" TELECOM_NUMBER tn1 ON tn1.CONTACT_MECH_ID = pcm.CONTACT_MECH_ID "
						+" LEFT JOIN "
						+" POSTAL_ADDRESS pa1 ON pa1.CONTACT_MECH_ID = pcm.CONTACT_MECH_ID "
						+" LEFT JOIN "
						+" PARTY_SUPPLEMENTAL_DATA AS psd ON per.PARTY_ID = psd.PARTY_ID "
						+" LEFT JOIN "
						+" TELECOM_NUMBER tn2 ON tn2.CONTACT_MECH_ID = psd.PRIMARY_TELECOM_NUMBER_ID "
						+" LEFT JOIN "
						+" POSTAL_ADDRESS pa2 ON pa2.CONTACT_MECH_ID = psd.PRIMARY_POSTAL_ADDRESS_ID "
						+" WHERE "
						+" per.PARTY_ID = 'param' ";
				sql=sql.replaceAll("param", partyId);
				ResultSet rs = null;
				rs = prosor.executeQuery(sql);
				
				List<String> tnContactMechIdList = new ArrayList<String>();
				List<String> paContactMechIdList = new ArrayList<String>();
				List<String> orderIdList = new ArrayList<String>();
				while (rs.next()) {
					String orderId = rs.getString("orderId");
					String tn1ContactMechId = rs.getString("tn1ContactMechId");
					String pa1ContactMechId = rs.getString("pa1ContactMechId");
					String tn2ContactMechId = rs.getString("tn2ContactMechId");
					String pa2ContactMechId = rs.getString("pa2ContactMechId");
					
					if(orderId != null && !orderIdList.contains(orderId)){
						orderIdList.add(orderId);
					}
					

					
					if(pa2ContactMechId != null && !paContactMechIdList.contains(pa2ContactMechId)){
						paContactMechIdList.add(pa2ContactMechId);
					}
					if(tn2ContactMechId != null && !tnContactMechIdList.contains(tn2ContactMechId)){
						tnContactMechIdList.add(tn2ContactMechId);
					}
					if(pa1ContactMechId != null && !paContactMechIdList.contains(pa1ContactMechId)){
						paContactMechIdList.add(pa1ContactMechId);
					}
					if(tn1ContactMechId != null && !tnContactMechIdList.contains(tn2ContactMechId)){
						tnContactMechIdList.add(tn1ContactMechId);
					}
					

				}
				
				for (String item : orderIdList) {
					System.out.println("delete from ORDER_ITEM_SHIP_GROUP_ASSOC where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_ITEM_SHIP_GRP_INV_RES where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_ITEM where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_ITEM_SHIP_GROUP where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_ROLE where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_PAYMENT_PREFERENCE where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_STATUS where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_CONTACT_MECH where ORDER_ID='" + item +"';");
					System.out.println("delete from ORDER_HEADER where ORDER_ID='" + item +"';");
				}
				for (String item : paContactMechIdList) {
					System.out.println("delete from POSTAL_ADDRESS where CONTACT_MECH_ID='" + item +"';");
				}
				for (String item : tnContactMechIdList) {
					System.out.println("delete from TELECOM_NUMBER where CONTACT_MECH_ID='" + item +"';");
				}
				
				System.out.println("delete from PARTY_SUPPLEMENTAL_DATA where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PARTY_CONTACT_MECH where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PERSON where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PARTY_CONTACT_MECH_PURPOSE where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PARTY_ROLE where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PARTY_STATUS where PARTY_ID='" + partyId +"';");
				System.out.println("delete from PARTY where PARTY_ID='" + partyId +"';");
				
				
			} catch (GenericDataSourceException e) {
				e.printStackTrace();
			} catch (GenericEntityException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
}
