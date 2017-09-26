package org.ofbiz.report.report.helper.stockoutreport;

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
	public static <T> Map<String,Object> getResults(StringBuffer sql,Integer pageNum,Integer pageSize,Class<T> clazz,Delegator delegator,boolean isexport) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		
		
		
		ResultSet rs = null;
		List list = new ArrayList();
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		
		
		StringBuffer sqlsum = new StringBuffer("select sum(transferqty) as sumNum from (");
		sqlsum.append(sql.toString() + " ) s");
		double sumNum=0;
		rs = processor.executeQuery(sqlsum.toString());
		while(rs.next()){
			String tempsum = rs.getString("sumNum");
			if(tempsum==null||"".equals(tempsum))
				tempsum="0";
			sumNum = Double.valueOf(tempsum);
		}
		
		
		
		StringBuffer sqlCount = new StringBuffer("select count(1) as totalNum from (");
		sqlCount.append(sql.toString() + " ) s");
		if(pageNum != 0 && !isexport){
			sql.append(" limit "+(pageNum-1)*pageSize+","+pageSize);
		}
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
		result.put("sumNum", sumNum);
		rs.close();
		processor.close();			
		return result;
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
}
