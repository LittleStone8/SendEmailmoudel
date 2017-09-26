package org.ofbiz.commondefine.service;

import java.util.Map;

import org.ofbiz.entity.Delegator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;

import com.alibaba.fastjson.JSONObject;

import javolution.util.FastMap;

public class ReceiveJson {

	/**
	 * 
	 * @param dctx
	 * @param json
	 */
	public static void receiveJson(DispatchContext dctx, JSONObject json) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Map<String, Object> result = new FastMap<String, Object>();
		
        Short param = json.getShort("data");
        
        System.out.println(param);;
        System.out.println("         0_o          ");
	        
	   
    }
	
}
