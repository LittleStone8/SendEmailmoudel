package org.opentaps.gwt.common.server.lookup;

import java.lang.reflect.Field;
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
 * 分页查询工具类
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
		//储存结果
		Map<String,Object> result = new HashMap<String, Object>();
		List list = new ArrayList();
		//获取JDBC连接通道对象
		GenericHelperInfo helperInfo = delegator.getGroupHelperInfo("org.ofbiz");
		SQLProcessor processor = new SQLProcessor(helperInfo);
		//总页数的sql
		StringBuffer sqlCount = new StringBuffer("select count(1) as totalNum from (");
		sqlCount.append(sql.toString() + " ) s");
		//拼接分页
		if(pageNum != 0){
			sql.append(" limit "+(pageNum-1)*pageSize+","+pageSize);
		}
		//执行sql
		ResultSet rs = null;
		rs = processor.executeQuery(sqlCount.toString());
		//总页
		Integer pageTotalNums = 0;
		//总数
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
		
		//查询内容结果集
		rs = processor.executeQuery(sql.toString());
		//获取封装类的属性集合
		Field[] fields = clazz.getDeclaredFields();
		while(rs.next()){
			//封装结果对象
			Object obj = clazz.newInstance();				
			for(Field field : fields){
				//属性名称
				String fieldName = field.getName();
				//方法名称
				String MethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				Method method = clazz.getMethod(MethodName, String.class);
				//注入属性
				method.invoke(obj, rs.getString(fieldName));
			}
			list.add(obj);
		}
		//设置结果集
		result.put("result",list);
		//总页数
		result.put("totalPage",pageTotalNums);
		//结果总数
		result.put("totalNum",count);	
		//设置起始页与最终页
		Integer startPage = (pageNum >= pageTotalNums-2 && pageTotalNums >=5) ? (pageTotalNums-4) : (pageNum > 1 && pageNum-1>=2) ? (pageNum-2) : 1;
		Integer endPage = (startPage == 1 && pageTotalNums >=5) ? 5 :(pageTotalNums >pageNum && pageTotalNums-pageNum >= 2 && pageNum != 1) ? (pageNum + 2) :  pageTotalNums;
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		//当前页
		result.put("pageNum", pageNum);
		//关闭资源
		rs.close();
		processor.close();			
		return result;
	}
}
