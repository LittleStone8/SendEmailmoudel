package org.ofbiz.commondefine.comm;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilURL;
import org.ofbiz.base.util.UtilXml;
import org.owasp.esapi.errors.EncodingException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;

public class LangUtil {
	
	private static Map<String,JSONObject> langJsonMap = new HashMap<String,JSONObject>();
	
    public static JSONObject xml2json(String xmlFilename){
    	
    	JSONObject langJson = langJsonMap.get("xmlFilename");
    	if(langJson != null){
    		return langJson;
    	}
		try {
			 JSONObject json = new JSONObject();
			 URL xmlUrl = UtilURL.fromFilename(xmlFilename);
			 Document	resourceDocument = UtilXml.readXmlDocument(xmlUrl, false);
	         Element resourceElem = resourceDocument.getDocumentElement();
	         for (Node propertyNode : UtilXml.childNodeList(resourceElem.getFirstChild())) {
	             if (propertyNode instanceof Element) {
	                 Element propertyElem = (Element) propertyNode;
	                 String labelKey = StringUtil.defaultWebEncoder.canonicalize(propertyElem.getAttribute("key"));
	                 JSONObject langValueJson = new JSONObject();
	                 for (Node valueNode : UtilXml.childNodeList(propertyElem.getFirstChild())) {
	                     if (valueNode instanceof Element) {
	                         Element valueElem = (Element) valueNode;
	                         String localeName = valueElem.getAttribute("xml:lang");
	                         String labelValue = StringUtil.defaultWebEncoder.canonicalize(UtilXml.nodeValue(valueElem.getFirstChild()));
	                         langValueJson.put(localeName, labelValue);
	                     } else if (valueNode instanceof Comment) {
	                     }
	                 }
	                 json.put(labelKey, langJson);
	             } else if (propertyNode instanceof Comment) {
	             }
	         }
	         langJsonMap.put(xmlFilename, json);
	         return json;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
    }

   

}