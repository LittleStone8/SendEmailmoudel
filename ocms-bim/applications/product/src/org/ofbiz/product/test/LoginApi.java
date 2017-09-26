package org.ofbiz.product.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.product.product.bean.FindAllproductPriceBean;
import org.ofbiz.product.product.bean.LookupProductResultJsonBean;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.webapp.control.LoginWorker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javolution.util.FastList;







public class LoginApi {



	
	
	public static  Map<String,String> register(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> facilitfile = UtilMisc.toMap();
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String ret = LoginWorker.login(request, response);
		Map<String, String> returnmap=new HashMap<String,String>();
		returnmap.put("ret", ret);
		return returnmap;
	}
	
	
	
	
	
	
	
	
	
	

}
