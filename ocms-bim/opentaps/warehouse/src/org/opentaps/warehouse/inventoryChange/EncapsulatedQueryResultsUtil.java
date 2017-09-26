package org.opentaps.warehouse.inventoryChange;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.datasource.GenericHelperInfo;
import org.ofbiz.entity.jdbc.SQLProcessor;

/**
 * @author wei.he
 *
 */
public class EncapsulatedQueryResultsUtil {
	
	/**
	 * 分页查询
	 * @param sql
	 * @param pageNum
	 * @param pageSize
	 * @param limit
	 * @param clazz
	 * @param delegator
	 * @return
	 * @throws Exception 
	 */
	public static <T> Map<String,Object> getResults(StringBuffer sql,Integer pageNum,Integer pageSize,Class<T> clazz,Delegator delegator) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		List list = new ArrayList();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		StringBuffer sqlCount = new StringBuffer("select count(1) as totalNum from (");
		sqlCount.append(sql.toString() + " ) s");
		if(pageNum != 0){
			sql.append(" limit "+(pageNum-1)*pageSize+","+pageSize);
		}
		ResultSet rs = null;
		rs = processor.executeQuery(sqlCount.toString());
		Integer pageTotalNums = 0;
		Integer count = 0;
		while(rs.next()){
			count = Integer.parseInt(rs.getString("totalNum"));
			
			if(count / pageSize > 0){
				if(count % pageSize > 0){
					pageTotalNums = count / pageSize + 1;						
				}
				else{
					pageTotalNums = count / pageSize;
				}
			}else{
				pageTotalNums = 1;
			}
		}
		
		rs = processor.executeQuery(sql.toString());
		list = makeObject(rs, clazz);
		result.put("result",list);
		result.put("totalPage",pageTotalNums);
		result.put("totalNum",count);	
		Integer startPage = (pageNum >= pageTotalNums-2 && pageTotalNums >=5) ? (pageTotalNums-4) : (pageNum > 1 && pageNum-1>=2) ? (pageNum-2) : 1;
		Integer endPage = (startPage == 1 && pageTotalNums >=5) ? 5 :(pageTotalNums >pageNum && pageTotalNums-pageNum >= 2 && pageNum != 1) ? (pageNum + 2) :  pageTotalNums;
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("pageNum", pageNum);
		rs.close();
		processor.close();			
		return result;
	}
	public static <T> Map<String,Object> getResults_(StringBuffer sql,Integer pageNum,Integer pageSize,Class<T> clazz,Delegator delegator) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		List list = new ArrayList();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		StringBuffer sqlCount = new StringBuffer("select count(1) as totalNum from (");
		sqlCount.append(sql.toString() + " ) s");
		if(pageNum != 0){
			sql.append(" limit "+(pageNum-1)*pageSize+","+pageSize);
		}
		ResultSet rs = null;
		rs = processor.executeQuery(sqlCount.toString());
		Integer pageTotalNums = 0;
		Integer count = 0;
		while(rs.next()){
			count = Integer.parseInt(rs.getString("totalNum"));
			
			if(count / pageSize > 0){
				if(count % pageSize > 0){
					pageTotalNums = count / pageSize + 1;						
				}
				else{
					pageTotalNums = count / pageSize;
				}
			}else{
				pageTotalNums = 1;
			}
		}
		
		rs = processor.executeQuery(sql.toString());
		list = makeObject_(rs, clazz);
		result.put("result",list);
		result.put("totalPage",pageTotalNums);
		result.put("totalNum",count);	
		Integer startPage = (pageNum >= pageTotalNums-2 && pageTotalNums >=5) ? (pageTotalNums-4) : (pageNum > 1 && pageNum-1>=2) ? (pageNum-2) : 1;
		Integer endPage = (startPage == 1 && pageTotalNums >=5) ? 5 :(pageTotalNums >pageNum && pageTotalNums-pageNum >= 2 && pageNum != 1) ? (pageNum + 2) :  pageTotalNums;
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("pageNum", pageNum);
		rs.close();
		processor.close();			
		return result;
	}
	public static <T>List getResults_(StringBuffer sql,Class<T> clazz,Delegator delegator) throws Exception{
		List list = new ArrayList();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		ResultSet rs = null;
		rs = processor.executeQuery(sql.toString());
		list = makeObject_(rs, clazz);
		rs.close();
		processor.close();			
		return list;
	}
	
	public static <T> List<T> makeObject(ResultSet rs,Class<T> clazz) throws Exception{
		List list = new ArrayList();
		Field[] fields = clazz.getDeclaredFields();
		while(rs.next()){
			Object obj = clazz.newInstance();				
			for(Field field : fields){
				String fieldName = field.getName();
				String MethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				Method method = clazz.getMethod(MethodName, String.class);
				method.invoke(obj, rs.getString(fieldName));
			}
			list.add(obj);
		}
		return list;
	}
	public static <T> List<T> makeObject_(ResultSet rs,Class<T> clazz) throws Exception{
		List list = new ArrayList();
		Field[] fields = clazz.getDeclaredFields();
		while(rs.next()){
			Object obj = clazz.newInstance();				
			for(Field field : fields){
				String fieldName = field.getName();
				String MethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				Method method = clazz.getMethod(MethodName, String.class);
				try {
					String ww = rs.getString(fieldName);
				} catch (Exception e) {
					continue;
				}
				method.invoke(obj, rs.getString(fieldName));
			}
			list.add(obj);
		}
		return list;
	}
}
