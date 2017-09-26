<script type="text/javascript">
var salemeninfo;
var Customerinfo;
function Ajaxsalemeninfo(){ 
	  var xmlHttpReq = null;  //声明一个空对象用来装入XMLHttpRequest
	  var target=document.getElementById("saletrid");
	  if (window.ActiveXObject){//IE5 IE6是以ActiveXObject的方式引入XMLHttpRequest的
	    xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	  } 
	  else if (window.XMLHttpRequest){//除IE5 IE6 以外的浏览器XMLHttpRequest是window的子对象
	    xmlHttpReq = new XMLHttpRequest();//实例化一个XMLHttpRequest
	  }
	  if(xmlHttpReq != null){  //如果对象实例化成功 
		  
		var orid=  '${order.orderId}';
	    xmlHttpReq.open("POST","/crmsfa/control/FindSalesmanByOderid?oderId="+orid,true);
	    xmlHttpReq.send("oderId="+orid);
		xmlHttpReq.onreadystatechange= function() {  
	          if(xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
	        	  sss=JSON.parse(xmlHttpReq.responseText); 
	        	  document.getElementById('FirstName').innerHTML = sss.salesmaninfo.fristname;
	        	  document.getElementById('MiddleName').innerHTML = sss.salesmaninfo.milldename;
	        	  document.getElementById('LastName').innerHTML = sss.salesmaninfo.lastname;
	        	  var endRow	="<tr class=\"rowLightGray\"><td class=\"tabletext\">"+sss.salesmaninfo.firstname+ "</td><td class=\"tabletext\">"+sss.salesmaninfo.middlename+ "</td><td class=\"tabletext\">"+sss.salesmaninfo.lastname+ "</td>";
	          }  
	 	}
	  }
	}
	
	/*
function AjaxCustomerinfo(){ 
	  var xmlHttpReq = null;  //声明一个空对象用来装入XMLHttpRequest
	  var target=document.getElementById("saletrid");
	  if (window.ActiveXObject){//IE5 IE6是以ActiveXObject的方式引入XMLHttpRequest的
	    xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	  } 
	  else if (window.XMLHttpRequest){//除IE5 IE6 以外的浏览器XMLHttpRequest是window的子对象
	    xmlHttpReq = new XMLHttpRequest();//实例化一个XMLHttpRequest
	  }
	  if(xmlHttpReq != null){  //如果对象实例化成功 
		  
		var orid=  '${order.orderId}';
	    xmlHttpReq.open("POST","/crmsfa/control/FindCusermanByOderid?oderId="+orid,true);
	    xmlHttpReq.send("oderId="+orid);
		xmlHttpReq.onreadystatechange= function() {  
	          if(xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
	        	  sss=JSON.parse(xmlHttpReq.responseText);   
	        	  if(sss.responseMessage=="success"&& sss.customerList.length>0)
	        	  {
	        		  document.getElementById('cusName').innerHTML = sss.customerList[0].firstName;
		        	  document.getElementById('Phone').innerHTML = sss.customerList[0].primaryPhoneNumber;
		        	  document.getElementById('address').innerHTML = sss.customerList[0].primaryAddress1;
		        	  var endRow	="<tr class=\"rowLightGray\"><td class=\"tabletext\">"+sss.customerList[0].firstName+ "</td><td class=\"tabletext\">"+sss.customerList[0].primaryPhoneNumber+ "</td><td class=\"tabletext\">"+sss.customerList[0].primaryAddress1+ "</td>";
	        	  }
	        	  
	          }  
	 	}
	  }
	}	
	*/
function AjaxShipmentinfo(){
		
		var orid=  '${order.orderId}';
		
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/crmsfa/control/FindShipmentinfoByOderid',
	        data: {
	        	oderId:orid,
					},
	        success: function (data) {
      		  jsonobjects = eval(data.shipmentinfolist);
    		  for(k=0;k<jsonobjects.length;k++)
    			 {
    			  var endRow	="<tr class=\"rowLightGray\"><td class=\"tabletext\">"+jsonobjects[k].shipmentid+ "</td><td class=\"tabletext\">"+jsonobjects[k].shippedtime+ "</td><td class=\"tabletext\">"+jsonobjects[k].via+ "</td><td class=\"tabletext\">"+jsonobjects[k].traceingcode+ "</td>";
	        	  $("#shipmentinfotable tr:last").after(endRow);
    			  
    			  
    			 }
	       	}	
    	});	
	}		
window.onload=function(){
	Ajaxsalemeninfo();
	//AjaxCustomerinfo();
	AjaxShipmentinfo();
}
</script>