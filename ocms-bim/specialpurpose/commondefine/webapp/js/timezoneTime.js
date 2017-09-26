var timeNow = $("#timeNow").val();
var hoursOffset = ~~$("#rawOffset").val();

var getTimezoneTime = function(time,fmt) {
    var currentHours = 0,
    	dateMS,msPerHour = 36E5;
    
    var time = time.replace(/-/g,"/");
  
    var tempDate;
    if(fmt){
    	 fmt = fmt || 'yyyy-MM-dd';
    	    var obj = {y: 0, M: 1, d: 0, H: 0, h: 0, m: 0, s: 0, S: 0};
    	    fmt.replace(/([^yMdHmsS]*?)(([yMdHmsS])\3*)([^yMdHmsS]*?)/g, function(m, $1, $2, $3, $4, idx, old){
    	    	    time = time.replace(new RegExp($1+'(\\d{'+$2.length+'})'+$4), function(_m, _$1){
    	                    obj[$3] = parseInt(_$1);
    	                    return '';
    	                });
    	                return '';
    	            });
    	    
    	            obj.M--; // 月份    	           
    	            tempDate = new Date(obj.y, obj.M, obj.d, obj.H, obj.m, obj.s);
    	            if(obj.S !== 0){
    	            	tempDate.setMilliseconds(obj.S);	
    	            }
    }else{
    	  tempDate = new Date(time);
    }
     hoursOffset = hoursOffset || 0;
//    hoursOffset = hoursOffset * msPerHour;
    currentHours = tempDate.getTime() - hoursOffset + ( ( -tempDate.getTimezoneOffset() / 60 ) * msPerHour );    
    dateMS = tempDate.setTime( currentHours  );
    return (new Date( dateMS )).getTime();
//    return currentHours + hoursOffset;
}

var getFixNewDate = function(time,fmt) {
	 var currentHours = 0,
 		dateMS,msPerHour = 36E5;
		    
	 var tempDate = new Date(time);
	  hoursOffset = hoursOffset || 0;
	// hoursOffset = hoursOffset * msPerHour;
	 currentHours = tempDate.getTime() + hoursOffset - ( ( -tempDate.getTimezoneOffset() / 60 ) * msPerHour );    
	  tempDate.setTime( currentHours  );
	 
	 var o = {
			    "M+": tempDate.getMonth() + 1, //月份 
		        "d+": tempDate.getDate(), //日 
		        "h+": tempDate.getHours(), //小时 
		        "m+": tempDate.getMinutes(), //分 
		        "s+": tempDate.getSeconds(), //秒 
		        "q+": Math.floor((tempDate.getMonth() + 3) / 3), //季度 
		        "S": tempDate.getMilliseconds() //毫秒 
		    };
		    if (/(Y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (tempDate.getFullYear() + "").substr(4 - RegExp.$1.length));
		    for (var k in o){
		    	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		    }
	 
	 return fmt;
	
}
var formatDate=function(now){
	var year=now.getFullYear(); 
    var month=(now.getMonth()+1) > 9 ? (now.getMonth()+1) : '0' + (now.getMonth()+1); 
    var date=(now.getDate()) > 9 ? (now.getDate()) : '0' + (now.getDate());    
    return date + "/" + month + '/' + year; 
}





