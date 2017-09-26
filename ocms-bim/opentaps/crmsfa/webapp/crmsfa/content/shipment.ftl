<#--
 * Copyright (c) Open Source Strategies, Inc.
 * 
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
-->

<script type="text/javascript">
var salemeninfo;
function Ajax(){ 
	  var xmlHttpReq = null;  //声明一个空对象用来装入XMLHttpRequest
	  var target=document.getElementById("saletrid");
	  if (window.ActiveXObject){//IE5 IE6是以ActiveXObject的方式引入XMLHttpRequest的
	    xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	  } 
	  else if (window.XMLHttpRequest){//除IE5 IE6 以外的浏览器XMLHttpRequest是window的子对象
	    xmlHttpReq = new XMLHttpRequest();//实例化一个XMLHttpRequest
	  }
	  if(xmlHttpReq != null){  //如果对象实例化成功 
	    xmlHttpReq.open("POST","/crmsfa/control/FindSalesmanByOderid?oderId="+${order.orderId},true);
	    xmlHttpReq.send("oderId="+${order.orderId});
		xmlHttpReq.onreadystatechange= function() {  
	          if(xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
	        	  sss=JSON.parse(xmlHttpReq.responseText); 
	        	  document.getElementById('FirstName').innerHTML = sss.salesmaninfo.fristname;
	        	  document.getElementById('MiddleName').innerHTML = sss.salesmaninfo.milldename;
	        	  document.getElementById('LastName').innerHTML = sss.salesmaninfo.lastname;
	        	  
	        	  var endRow	="<tr class=\"rowLightGray\"><td class=\"tabletext\">"+sss.salesmaninfo.firstname+ "</td><td class=\"tabletext\">"+sss.salesmaninfo.middlename+ "</td><td class=\"tabletext\">"+sss.salesmaninfo.lastname+ "</td>";
					
	        	  
	        	  salemeninfo=sss.salesmaninfo
	          }  
	 	}
	  }
	}
window.onload=function(){
	Ajax();
	
}
	
	
	
	
</script>
<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>



<@frameSectionHeader title="Shipment" extra=extraOptions/>

<table class="crmsfaListTable">
  <tbody>
    <tr class="crmsfaListTableHeader">
      <td class="tableheadtext" >Shipped Time</td>
      <td class="tableheadtext" >Via</td>
      <td class="tableheadtext" >traceing code</td>

    <tr class="rowLightGray" id="saletrid">
      <td class="tabletext" id="FirstName"></td>
      <td class="tabletext" id="MiddleName"></td>
      <td class="tabletext" id="LastName"></td>
    </tr>

  </tbody>
</table>
