package org.ofbiz.report.report.event.sales;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.report.report.been.ExportSalesReport;
import org.ofbiz.report.report.been.SalesFooterBean;
import org.ofbiz.report.report.been.SalesReportBean;
import org.ofbiz.report.report.been.SalesTableBean;
import org.ofbiz.report.report.event.base.BaseReportEvent;
import org.ofbiz.report.report.helper.sales.OperatorSalesReportHelper;
import org.ofbiz.report.report.helper.sales.SalesReportHelper;
import org.ofbiz.report.report.helper.sales.BaseSalesReportHelper;
import org.ofbiz.report.security.OperatorReportSecurity;
import org.ofbiz.security.Security;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.storegroup.bean.StoreGroupBean;
import org.ofbiz.storegroup.bean.StoreRollUpBean;
import org.ofbiz.util.ParaUtils;
import org.ofbiz.util.ZipUtils;

public class OperatorSalesReportEvent {

	/**
	 * 联想查询product
	 * 
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
		OperatorReportSecurity operatorReportSecurity = new OperatorReportSecurity(delegator);

		return BaseSalesReportEvent.searchStoreBySecurity(delegator, userLogin, operatorReportSecurity);
	}

	public static void getMasterStoreAndSG(GenericDelegator delegator, EntityCondition totalCondition,
			List<StoreGroupBean> sGroupBeans, List<StoreRollUpBean> storeRollUpBeans) throws GenericEntityException {
		BaseSalesReportEvent.getMasterStoreAndSG(delegator, totalCondition, sGroupBeans, storeRollUpBeans);

	}

	public static void getSingleStoreByRole(GenericDelegator delegator, EntityCondition sGroupConditions,
			GenericValue userLogin, List<StoreGroupBean> sGroupBeans, List<StoreRollUpBean> storeRollUpBeans)
			throws GenericEntityException {
		BaseSalesReportEvent.getSingleStoreByRole(delegator, sGroupConditions, userLogin, sGroupBeans,
				storeRollUpBeans);
	}

	/**
	 * 条件查询报表
	 * 
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
			// chenshihua 2017-6-30 修改返回参数
			/*
			 * tableBeans = OperatorSalesReportHelper.searchTableBean(delegator,
			 * productId, contion, beginDate, endDate, userLogin);
			 */
			salesReportBeanList = BaseSalesReportHelper.getSalesReport(delegator, productId, contion,
					beginDate, endDate, userLogin, request);
			footerBeans = BaseSalesReportHelper.searchFooterBean(delegator, productId, contion, beginDate, endDate , request);
			TransactionUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}

		for (SalesReportBean salesReportBean : salesReportBeanList) {
			salesReportBean.parseMoneyAndQuantity();
			salesReportBean.operatorInit();
		}
		map.put("tableBean", salesReportBeanList);
		for (SalesFooterBean item : footerBeans) {
			item.parseMoneyAndQuantity();
		}
		map.put("footerBean", footerBeans);

		return map;
	}

	// 获取校验后的店铺组与店铺，后直接加入SQL中
	public static Map<String, Object> getSGandSTR(GenericDelegator delegator, GenericValue userLoginGV, String sGroupId,
			String storeId) {
		Map<String, Object> ret = searchAvailableStoresAndSGroups(delegator, userLoginGV);

		return BaseSalesReportEvent.checkAvailableStoreScope(sGroupId, storeId, ret);

	}

	// 导出销售表表
	public static Map<String, Object> getExportOperatorSalesReport(HttpServletRequest request, HttpServletResponse response) {
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
			exportSalesReportList = BaseSalesReportHelper.getExportSalesReportBeanList(delegator, productId, contion,
					beginDate, endDate, userLogin, request);
			TransactionUtil.commit();
			map.put("exportSalesReportList", exportSalesReportList);
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("search error");
		}

		return map;
	}

	public static String operatorExportSalesReport(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if("detail".equals(type)){
			return operatorExportDetailSalesReport(request, response);
		}else if("summary".equals(type)){
			return BaseSalesReportEvent.exportPaymentSummary(request, response);
		}else{
			return "error";
		}
	}
		
		
	
	
	public static String operatorExportDetailSalesReport(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> receiveMap = getExportOperatorSalesReport(request, response);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		if ("error".equals(receiveMap.get("responseMessage"))) {
			return "error";
		}

		List<ExportSalesReport> exportSalesReportList = (List<ExportSalesReport>) receiveMap.get("exportSalesReportList");
		List<String> dataList = new ArrayList<String>();
		
		dataList.add("Company Name,Shop Group,Shop Name,Order Id,producId,Date,Payment Method,Customer Name,"
				+ "Acutal Quantity,Unit Price,Unit,Amount,Adjustment,Receivables,Purchase/Sales Ledger,"
				+ "SalePerson,MONTH,CORRECTED NAME,Brand,Model,Description,Category,SubCategory,"
				+ "Third SubCategory,UNIT COST,TOTAL COST,SalesMethod,Sales Method");
//			dataList.add("公司名称,店铺组,店名称,订单号,日期,支付方式,客户名称,销售数量 ,单价,单位,金额,调整价,应收账款 ,Purchase/Sales Ledger,"
//					+ "销售员,期间,品名,品牌,型号,属性,产品类别,小类, ,单位成本 ,成本 ,渠道,销售方式");
		for (ExportSalesReport item : exportSalesReportList) {
			item.parseMoneyAndQuantity();
			String str = item.operatorManageToString();
			dataList.add(str);
		}

		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html; charset=UTF-8");
		response.setContentType("application/octet-stream");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");// 设置日期格式
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
				// TODO Auto-generated catch block
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
	}

}
