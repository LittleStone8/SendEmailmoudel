package org.ofbiz.commondefine.comm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.webapp.control.ConfigXMLReader.Event;
import org.ofbiz.webapp.control.ConfigXMLReader.RequestMap;
import org.ofbiz.webapp.event.EventHandler;
import org.ofbiz.webapp.event.EventHandlerException;

import com.alibaba.fastjson.JSONObject;

import javolution.util.FastMap;

public class JsonHandler implements EventHandler {

    public static String module = JsonHandler.class.getName();

    private Map<String, Class<?>> eventClassMap = FastMap.newInstance();

    
    
    public void init(ServletContext context) throws EventHandlerException {
    	
    	
    }
    
    public String invoke(Event event, RequestMap requestMap, HttpServletRequest request, HttpServletResponse response) throws EventHandlerException {
    	 Class<?> eventClass = this.eventClassMap.get(event.path);
    	 if (eventClass == null) {
             synchronized (this) {
                 eventClass = this.eventClassMap.get(event.path);
                 if (eventClass == null) {
                     try {
                         ClassLoader loader = Thread.currentThread().getContextClassLoader();
                         eventClass = loader.loadClass(event.path);
                     } catch (ClassNotFoundException e) {
                         Debug.logError(e, "Error loading class with name: " + event.path + ", will not be able to run event...", module);
                     }
                     if (eventClass != null) {
                         eventClassMap.put(event.path, eventClass);
                     }
                 }
             }
         }
    	 
    	 if (Debug.verboseOn()) Debug.logVerbose("[Set path/method]: " + event.path + " / " + event.invoke, module);

         Class<?>[] paramTypes = new Class<?>[] {HttpServletRequest.class, HttpServletResponse.class};

         Debug.logVerbose("*[[Event invocation]]*", module);
         Object[] params = new Object[] {request, response};
    	 
         try {
             Method m = eventClass.getMethod(event.invoke, paramTypes);
             
             Object obj = m.invoke(null, params);
             if(obj == null){
            	 return "success";
             }
             Map<String,Object> attrMap = (Map<String,Object>) obj;
             
             String jsonStr = JSONObject.toJSONString(attrMap);
             if (jsonStr == null) {
                 throw new EventHandlerException("JSON Object was empty; fatal error!");
             }

             response.setContentType("application/json");
             
             response.setContentLength(jsonStr.getBytes("UTF8").length);

             Writer out = response.getWriter();
                 out.write(jsonStr);
                 out.flush();
             
         } catch (java.lang.reflect.InvocationTargetException e) {
             Throwable t = e.getTargetException();

             if (t != null) {
                 Debug.logError(t, "Problems Processing Event", module);
                 throw new EventHandlerException("Problems processing event: " + t.toString(), t);
             } else {
                 Debug.logError(e, "Problems Processing Event", module);
                 throw new EventHandlerException("Problems processing event: " + e.toString(), e);
             }
         } catch (Exception e) {
             Debug.logError(e, "Problems Processing Event", module);
             throw new EventHandlerException("Problems processing event: " + e.toString(), e);
         }
    	 return "success";
    }
    
    
}