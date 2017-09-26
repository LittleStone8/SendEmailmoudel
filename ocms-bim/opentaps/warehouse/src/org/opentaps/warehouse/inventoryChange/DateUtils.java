package org.opentaps.warehouse.inventoryChange;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilGenerics;

public class DateUtils {

	public static String getLocalizedDate(String dateStr,HttpServletRequest request) {
		String d = "";
		if(!StringUtils.isEmpty(dateStr) && StringUtils.contains(dateStr, ".")){
			dateStr = dateStr.substring(0,dateStr.lastIndexOf("."));
			Locale locale = request.getLocale();
			TimeZone timeZone = getTimeZone(request);
			DateFormat dateFormat = toDateTimeFormat("yyyy-MM-dd HH:mm:ss",timeZone,locale);
			d = dateFormat.format(Timestamp.valueOf(dateStr));
	    }
		return d;
	}
	
    public static TimeZone getTimeZone(HttpServletRequest request) {
        HttpSession session = request.getSession();
        TimeZone timeZone = (TimeZone) session.getAttribute("timeZone");
        if (timeZone == null) {
            String tzId = null;
            Map<String, String> userLogin = UtilGenerics.cast(session.getAttribute("userLogin"));
            if (userLogin != null) {
                tzId = userLogin.get("lastTimeZone");
            }
            timeZone = UtilDateTime.toTimeZone(tzId);
            session.setAttribute("timeZone", timeZone);
        }
        return timeZone;
    }
    
    public static DateFormat toDateTimeFormat(String dateTimeFormat, TimeZone tz, Locale locale) {
        DateFormat df = null;
        if (dateTimeFormat == null) {
            df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
        } else {
            df = new SimpleDateFormat(dateTimeFormat);
        }
        df.setTimeZone(tz);
        return df;
    }

}
