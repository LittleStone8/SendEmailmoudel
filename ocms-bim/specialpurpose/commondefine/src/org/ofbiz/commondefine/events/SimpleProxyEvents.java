package org.ofbiz.commondefine.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

public class SimpleProxyEvents {

    public static String module = SessionEvents.class.getName();

    public static Map<String, String> map = new HashMap<String, String>() {
	{
	    put("/catalog/control/ListGivenClassfication","/api/product/v1/productCategory/selectProductCategoryByParentId");
	    put("/catalog/control/findAllProductFeatureTypeAndProduceFeaturAppl", "/api/product/v1/productfeature/findAllProductFeatureTypeAndProduceFeatur");
	    put("/catalog/control/anotherAddVariantProduct","/api/product/v1/product/createVariantProduct");

	    put("/catalog/control/findAllProductFeature","/api/product/v1/productFeature/findAllProductFeature");
	    put("/catalog/control/updateGoodIdentificationinfo","/api/product/v1/product/updategoodIdentificationInfo");
	    put("/catalog/control/findProductNameById","/api/product/v1/product/findProductNameById");
	    
//	    put("/catalog/control/findAllcheckedProductPrice","/api/product/v1/productPrice/findAllcheckedProductPrice");
//	    put("/catalog/control/oneKeyUpdateAllProductPrice","/api/product/v1/productPrice/oneKeyUpdateAllProductPrice");
	    
	    put("/catalog/control/editProductFeature","/api/product/v1/productFeature/editProductFeature");
	    put("/catalog/control/deleteProduceFeature","/api/product/v1/productFeature/deleteProduceFeatur");
	    put("/catalog/control/editProductFeatureType","/api/product/v1/productFeatureType/editProductFeatureType");
	    
	    put("/catalog/control/listCategoryFeatureTree","/api/product/v1/productCategory/listCategoryFeatureTreeBycategoryID");
	    put("/catalog/control/listCategoryLevelAndID","/api/product/v1/productCategory/listCategoryLevelAndID");

	    put("/catalog/control/newCreateProductFeatureType","/api/product/v1/productFeatureType/createProductFeatureType");
	    put("/catalog/control/deleteProductFeatureType","/api/product/v1/productFeatureType/deleteProductFeatureType");
	    put("/catalog/control/findProductAndCategory","/api/product/v1/product/findProductAndCategory");
	    put("/catalog/control/newAddProduceFeatur", "/api/product/v1/productFeature/addProductFeature");
	    put("/catalog/control/findTableHead", "/api/product/v1/product/findProducthasFeatureType");
	    put("/catalog/control/createProductAndGotoPrice", "/api/product/v1/product/updateProductInfo");
	    
	    put("/catalog/control/ListDefaultClassfication", "/api/product/v1/productCategory/queryAllProductCategoryAndSequenceNum");
	    put("/catalog/control/MoveClassfication", "/api/product/v1/productCategoryRollup/moveClassfication");
	    put("/catalog/control/EditClassfication", "/api/product/v1/productCategory/editClassfication");
	    put("/catalog/control/DeleteClassfication", "/api/product/v1/productCategory/deleteClassfication");
	    put("/catalog/control/CreateClassfication", "/api/product/v1/productCategory/createProductCategory");
	    put("/catalog/control/QuickListClassfication", "/api/product/v1/productCategory/quickListClassfication");
	    
	    
	    put("/catalog/control/findAllProductIsActive", "/api/product/v1/product/findAllProductIsActive");
	    put("/catalog/control/updateProductIsActive", "/api/product/v1/product/updateProductIsActive");
	    
	    put("/catalog/control/editFeatureTypeCategory", "/api/product/v1/productFeatureTypeCategory/editFeatureTypeCategory");
	    
	    put("/wpos/control/createOrUpdatePerson", "/api/security/v1/person/createOrUpdatePerson");
	    put("/wpos/control/addPostalAddress", "/api/security/v1/person/addPostalAddress");
	    put("/wpos/control/findPersonByTelecomNumber", "/api/security/v1/person/findPersonByTelecomNumber");
	    put("/wpos/control/findPersonByUserName", "/api/security/v1/person/findPersonByUserName");
	    
	    put("/ordermgr/control/findSalesOrdersList", "/api/order/v1/order/findSaleOrderList");
	    
	    put("/catalog/control/syncProductApi", "/api/product/v1/product/syncProductApi");
	    
	}
    };

    public static Map<String, Object> simpleProxy(HttpServletRequest request, HttpServletResponse response) {

	try {
	    String targetUrl = request.getRequestURI();
	    LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

	    Map<String, Object> context = new HashMap<String, Object>();
	    Map<String, Object> uomInfo = dispatcher.runSync("getSystemSettings", context);
	    
	    String proxyHost = (String) uomInfo.get("proxyHost");
	    
	    targetUrl = map.get(targetUrl);
	    
	    if (targetUrl == null) {
		return ServiceUtil.returnError("emtpy");
	    }

	    URL url = new URL(proxyHost + targetUrl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestProperty("Cookie", request.getHeader("Cookie"));

	    OutputStream send = conn.getOutputStream();
	    InputStream body = request.getInputStream();
	    IOUtils.copy(body, send);
	    send.flush();
	    send.close();
	    body.close();

	    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    PrintWriter out = response.getWriter();
	    String line;
	    while ((line = in.readLine()) != null) {
		out.println(line);
	    }
	    out.flush();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (GenericServiceException e) {
	    e.printStackTrace();
	}
	return null;
    }
    

}