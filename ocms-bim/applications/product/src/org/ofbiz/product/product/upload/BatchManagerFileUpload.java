package org.ofbiz.product.product.upload;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ofbiz.base.util.FileUtil;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.Security;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BatchManagerFileUpload {

    public static final String resource = "ProductErrorUiLabels";

    @SuppressWarnings("unchecked")
    public static Map<String, Object> batchManagerFileUpload(HttpServletRequest request, HttpServletResponse response) {
	
    	if(!hasEntityPermission(request, "BATCH_MANAGE")){
    		return ServiceUtil.returnError("you have no permission ");
    	}
    	
	Delegator delegator = (Delegator) request.getAttribute("delegator"); 
	
	// boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, FileUtil.getFile("runtime/tmp")));
	List<FileItem> lst = null;
	try {
	    lst = dfu.parseRequest(request);
	} catch (FileUploadException e4) {
	}
	if (lst.size() == 0) {
	    System.out.println("[uploadImage]     lst.size()  ");
	}
	// 对所有请求信息进行判断
	Iterator iter = lst.iterator();
	while (iter.hasNext()) {
	    FileItem item = (FileItem) iter.next();
	    // 信息为普通的格式
	    // if (item.isFormField()) {
	    // String fieldName = item.getFieldName();
	    // String value = item.getString();
	    // System.out.println("value "+value);
	    // }else{
	    byte[] itemByte = item.get();
	    InputStream sbs = new ByteArrayInputStream(itemByte);
	    try {
		
		
		System.out.println(item.getContentType());
		System.out.println(item.getFieldName());
		System.out.println(item.getName());

		String fileName = item.getName();
		String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
		
		System.out.println(prefix);
		
		
		JSONArray jsonArr = new JSONArray();
		JSONArray errJsonObj = new JSONArray();
		
		
		 Workbook wb = null;
		
		if("xls".equals(prefix)){
		    POIFSFileSystem fs = new POIFSFileSystem(sbs);
		     wb = new HSSFWorkbook(fs);
		}else if("xlsx".equals(prefix)){
		    wb = new XSSFWorkbook(sbs);
		}
		
		
		
		Sheet sheet = wb.getSheetAt(0);
		
		
		int rowNum = sheet.getPhysicalNumberOfRows();
		
		for (int i = 1; i < rowNum; i++) {
		    Row row = sheet.getRow(i);
		    JSONObject jsonObj = new JSONObject();
		    		    
		    String productId = getCellFormatValue(row.getCell(0));
		    String internalName = getCellFormatValue(row.getCell(1));
		    String description = getCellFormatValue(row.getCell(2));
		    String brandName = getCellFormatValue(row.getCell(3));
		    String storeName = getCellFormatValue(row.getCell(4));
		    String price = getCellFormatValue(row.getCell(5));
		    
		    if(productId != null && productId.trim().length() >0){
			OneStoreOnePriceRuleAction  anction=   new OneStoreOnePriceRuleAction(delegator);
			    Map<String, Object> preCheckRes = anction.preCheck(productId,storeName,price,internalName,brandName,description);
			    if(ServiceUtil.isError(preCheckRes)){
				int j = i+1;
				errJsonObj.add("line "+j+"  "+ServiceUtil.getErrorMessage(preCheckRes)+" does not exist，Please check again");
			    }
			    jsonObj.put("productId", getCellFormatValue(row.getCell(0)));
			    jsonObj.put("internalName", getCellFormatValue(row.getCell(1)));
			    jsonObj.put("description", getCellFormatValue(row.getCell(2)));
			    jsonObj.put("brandName",getCellFormatValue(row.getCell(3)));
			    jsonObj.put("storeName", getCellFormatValue(row.getCell(4)));
			    jsonObj.put("price",getCellFormatValue(row.getCell(5)));
			    jsonArr.add(jsonObj);
		    }
		   
		}
		
		
		
		if(jsonArr.size() == 0 && errJsonObj.size() ==0 ){
		    errJsonObj.add("empty !!!!!!!!!!!!");
		}
		
		
		if(jsonArr.size() >0 ){
		    
		    List<String> tmpList = new ArrayList<String>();
		    for (int i = 0; i < jsonArr.size(); i++) {
			 JSONObject tmpObj = jsonArr.getJSONObject(i);
			 String productId= tmpObj.getString("productId");
			 if(tmpList.contains(productId)){
			     errJsonObj.add("multi-"+productId +"  ");
			 }else{
			     tmpList.add(productId);
			 }
		    }
		}
		
		Map<String, Object> map = ServiceUtil.returnSuccess();
		map.put("excel", jsonArr);
		
		map.put("errList", errJsonObj);
		
		return map;
	    } catch (IOException e1) {
		e1.printStackTrace();
		return ServiceUtil.returnError(e1.getMessage());
	    } catch (Exception e) {
		e.printStackTrace();
		return ServiceUtil.returnError(e.getMessage());
	    }
	    // }
	}
	return ServiceUtil.returnError("no file");
    }
    
    
    public static Map<String, Object> batchUpload(HttpServletRequest request, HttpServletResponse response) {
	
    	if(!hasEntityPermission(request, "BATCH_MANAGE")){
    		return ServiceUtil.returnError("you have no permission ");
    	}
    	
	Delegator delegator = (Delegator) request.getAttribute("delegator"); 
	
	HttpSession session = request.getSession();
	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
	if (userLogin == null) {
		return ServiceUtil.returnError("user not login");
	}
	String createdUserStr = userLogin.getString("userLoginId");
	
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
	String productListStr =  jsonStringBuffer.toString();
	JSONObject json = JSON.parseObject(productListStr);
	
	JSONArray productList = json.getJSONArray("productList");
	
		for (int i = 0; i < productList.size(); i++) {
		    
		     JSONObject product = productList.getJSONObject(i);
		     
		     String productId = product.getString("productId");
		     String description =product.getString("description");
		     String storeName =product.getString("storeName");
		     
		     String brandName =product.getString("brandName");
		     String internalName =product.getString("internalName");
		     String price =product.getString("price");
		     Date fromDate =  product.getDate("fromDate");
		     Timestamp fromTime = null;
		     if(fromDate !=null){
			 fromTime = new Timestamp(fromDate.getTime());
		     }
		     Date thruDate =  product.getDate("thruDate");
		     Timestamp thruTime = null;
		     if(thruDate !=null){
			 thruTime = new Timestamp(thruDate.getTime());
		     }
		     OneStoreOnePriceRuleAction  anction=   new OneStoreOnePriceRuleAction(delegator);
		     Map<String, Object> addRes = anction.addOneStoreOnePricePromo(productId, storeName, price, fromTime, thruTime,createdUserStr,internalName,brandName,description);
		    
		}
		Map<String, Object> map = ServiceUtil.returnSuccess();
		return map;
    }
    
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        
        if (cell != null) {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                }
                else {
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            case HSSFCell.CELL_TYPE_STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
    
    public static boolean hasEntityPermission(HttpServletRequest request ,String updateMode ){
		String errMsg=null;
		Security security = (Security) request.getAttribute("security");
		if (!security.hasEntityPermission("PPR", "_" + updateMode, request.getSession())) {
            Map<String, String> messageMap = UtilMisc.toMap("updateMode", updateMode);
            errMsg = UtilProperties.getMessage(resource,"productevents.not_sufficient_permissions", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return false;
        }else{
        	return true;
        }
	}

}
