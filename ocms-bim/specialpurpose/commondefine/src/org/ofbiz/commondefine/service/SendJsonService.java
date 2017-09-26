package org.ofbiz.commondefine.service;

import java.util.Map;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import com.alibaba.fastjson.JSONObject;

public class SendJsonService {

	
	public static Map<String, Object> sendJson(DispatchContext dctx, Map context) {
        try {
        	LocalDispatcher dispatcher = dctx.getDispatcher();
            Delegator delegator = dctx.getDelegator();
            String data = (String) context.get("data");
            GenericValue userLogin = (GenericValue) context.get("userLogin");
            JSONObject json = JSONObject.parseObject(data);
//			dispatcher.runAsync("receiveJson", json);
			return ServiceUtil.returnSuccess("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ServiceUtil.returnError("错了！");
		}
        
    }
	
}
