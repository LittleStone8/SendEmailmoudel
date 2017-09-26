<script src="/commondefine_js/jquery.js"></script> 

<#-- chenshihua		2017-3-23 -->
<script> 
	window.onload=function(){
		
		<#-- 获取user所属店铺和店铺名称 -->
		var index = 0;	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findSalesStore',
	        data: {},
	        success: function (data) {
	        	var index = 0;
	        	$.each(data.store,function(key,value){
	        		var id = "store_"+index;
	        		$("#salesStore").append("<option id=\""+id+"\" value=\""+key+"\" >"+value+"</option>");
	        		index++;
				});
	       	}
    	});
		
		
		
		
		 
		
		<#-- 获取销售渠道  -->
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findSalesChannel',
	        data: {},
	        success: function (data) {
	        	var index = 0;
	        	jQuery.each(data.enumList, function(i,item){
	        		var id = "Channel_"+index;
	          	 	$("#salesChannelEnumId").append("<option id=\""+id+"\" value=\""+item.enumId+"\" >"+item.description+"</option>");
	          	  	index++;
				});
	       	}
    	});
    	
	
	}	
</script> 

<#-- 查询店铺销售员   chenshihua	2017-3-24 -->
<script type="text/JavaScript">
	function gradeChange(){
	    $("option").filter("#salemen").remove();
	    $.ajax({
		    type: 'post',
		    dataType: 'json',
		    url: '/ordermgr/control/findStoreSaleMens',
		    data: {storeId:document.getElementById("salesStore").options[document.getElementById("salesStore").selectedIndex].value},
			success: function (data) {
				var id = "salemen";
				jQuery.each(data.storeSaleMens, function(i,item){
					$("#saleMens").append("<option id=\""+id+"\"  value=\""+item+"\" >"+item+"</option>");
				});
			}
		          
	    });
	}
</script>


<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<#-- 自动匹配客户名查询客户      	chenshihua  2017-3-27 -->
<script>
  $( function() {
    $( "#customer" ).autocomplete({
      source: function( request, response ) {
        $.ajax( {
          url: "/ordermgr/control/new_getAutoCompleteClientPartyIds",
          dataType: "json",
          data: {
            term: request.term
          },
          success: function( data ) {
          	var customer = data.customerList;
          	var namelist = [];
          	jQuery.each(customer, function(i,item){
          		item.value = item.firstName +" "+ item.lastName;
          		namelist.push(item);
				});
			response(namelist);
          }
        } );
      },
      minLength: 2,
      select: function( event, ui ) {
      	var contact = ui.item.primaryCountryCode+ui.item.primaryAreaCode+ui.item.primaryContactNumber;
      	var address = ui.item.primaryAddress1 + ui.item.primaryAddress2;
      	$("#contact").empty();
      	$("#address").empty();
      	
      	$("#contact").attr("value",contact);
      	$("#address").attr("value",address);
      	
      }
    });
  });
  
  
  
  
  
  	function findAllClient(){    	
    	$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/ordermgr/control/findAllClient',
	        data: {},
	        success: function (data) {
	        	
	       	}
    	});
	}

	
	
	function findAllProduct(){    	
    	$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/ordermgr/control/findAllProduct',
	        data: {},
	        success: function (data) {
	        	
	       	}
    	});
	}
	
	function gwtSuggestCustomers(){    	
    	$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/gwtSuggestCustomers',
	        data: {
	        	start:"0",
				limit:"10",
				filterCol:"text",
				query:"te",
				sort:"partyId",
				dir:"ASC"
	        },
	        success: function (data) {
	        	
	       	}
    	});
	}
	
	<#-- 
		String primaryPhoneNumber = request.getParameter("primaryPhoneNumber");
	   	String primaryPhoneCountryCode = request.getParameter("primaryPhoneCountryCode");
	   	String primaryPhoneAreaCode = request.getParameter("primaryPhoneAreaCode");
	   	String primaryPhoneExtension = request.getParameter("primaryPhoneExtension");
	   	String primaryPhoneAskForName = request.getParameter("primaryPhoneAskForName");
	   	
	   	String firstNameLocal = request.getParameter("firstNameLocal");
    	String lastNameLocal = request.getParameter("lastNameLocal");
    	String preferredCurrencyUomId = request.getParameter("preferredCurrencyUomId");
    	String description = request.getParameter("description");
    	String birthDate = request.getParameter("birthDate");
	  -->
	
	function createPersonContact(){    	
    	$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/ordermgr/control/createPersonContact',
	        data: {
	        	firstNameLocal:'jjude',
	        	lastNameLocal:'carl',
	        	preferredCurrencyUomId:'',
	        	description:'a customer',
	        	birthDate:'',
	        	
	        	primaryPhoneNumber:'13615544614',
	        	primaryPhoneCountryCode:'',
	        	primaryPhoneAreaCode:'',
	        	primaryPhoneExtension:'7488888888888',
	        	primaryPhoneAskForName:'',
	        },
	        success: function (data) {
	        	
	       	}
    	});
	}
  
  
  
</script>



<#-- 新建立窗口查询用户 -->
<script>
	function newLookupClients(){
		url = "/ordermgr/control/newLookupClients";
		window.open('/ordermgr/control/LookupClients', '' , 'height=600, width=700, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'); 
	}

</script>


<script>

$( function() {
	$( "#product" ).autocomplete({
		source: function( request, response ) {
			$.ajax( {
				url: "/ordermgr/control/new_gwtSuggestProductForCart",
          		dataType: "json",
          		data: {
          			start: '0',
					limit: '10',
					filterCol: 'text',
					sort: 'productId',
					dir: 'ASC',
			        query: request.term
          		},
          		success: function(data) {
          			var products = data.items;
          			var productlist = [];
          			jQuery.each(products, function(i,item){
          				item.value = item.id +":"+ item.text;
          				productlist.push(item);
					});
					response(productlist);
          		}
			});
		},
		minLength: 2,
		select: function( event, ui ) {
		}
	});
});
  
  
  
  
  
  
  
  
  
$(function() {
	<#-- 获取单价和显示数量 -->
	$("#product").blur(function() {
		var product = $(this).val();
		var productId = product.split(":",2)[0];
		var producttext = product.split(":",2)[1];
		
		$.ajax({
		    type: 'post',
		    dataType: 'json',
		    url: "/ordermgr/control/new_gwtFindProductInfoForCart",
		    data: {
				start: '0',
				limit: '0',
				noPager: 'Y',
				productId: productId,
				sort:'productId',
				dir:'ASC'	    
		    },
			success: function (data) {
				
				var products = data.items;
				jQuery.each(products, function(i,item){
          			var pirce = item.unitPrice;
          			var quantity = $("#quantity").val();
          			if(quantity == ""){
          				quantity = 1;
          			}
          			$("#description").attr("value",item.description);
          			$("#quantity").attr("value",quantity);
          			$("#pirce").attr("value",pirce);
          			$("#total").attr("value",quantity*pirce);
					});
				}
		          
	    	});
		});
	
	<#-- 修改数量 -->
	$("#quantity").blur(function() {
		$("#total").attr("value",$("#quantity").val()*$("#pirce").val());
	});
	
	
	
	
	
	
	
	
}) 
</script>






































<div >
	<div style="background:#C0C0C0 ; color=#000000 ">选择门店信息</div>
	<table>
		<tr>
			<td>选择店铺</td>
			<td>
				<select name="salesStore" class="selectBox" id="salesStore" onchange="gradeChange()">
					<option selected="selected" >---请选择---</option>
				</select>
			</td>		
			<td>渠道销售</td>
			<td>
				<select name="salesChannelEnumId" class="selectBox" id="salesChannelEnumId">
              		
            	</select>
			</td>
			
			
		

			
			
			<td>选择销售员</td>
			<td>
				<select name="saleMens" class="selectBox" id="saleMens">
					<option selected="selected">---请选择---</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>支付方式</td>
			<td>
				<select>
				<option >现金</option>
				<option >信用卡</option>
				<option >购物券</option>
				</select>
			</td>
		</tr>
	</table>
</div>




















<div >
	<div style="background:#C0C0C0 ; color=#000000 ">选择门店信息</div>
	<table>
		<div>
			<tr>
				<td>
					<label for="birds">选择客户 </label>
					
  					<input id="customer">
				</td>
				<td>
					<a href="javascript:newLookupClients()">
						<img src="/images/fieldlookup.gif" alt="Lookup" border="0" height="14" width="15" >
					</a>
				</td>
				<td>电话</td>
				<td id="contact_td">
					<input type="text" id="contact"></input>
				</td>
				<td>送货地址</td>
				<td id="address_td">
					<input type="text" id="address"></input>
				</td>
				
				
				<td>订单备注</td>
				<td>
					<input type="text"></input>
				</td>
			</tr>
		</div>
	</table>
</div>











<div>
	<div style="background:#C0C0C0 ; color=#000000 ">订单明细</div>
</div>
<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>产品</th>
                <th>描述</th>
                <th>数量</th>
                <th>单价</th>
                <th>调整</th>
                <th>合计</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<#-- /ordermgr/control/gwtSuggestProductForCart -->
				<td>
					<div class="x-form-field-wrap  x-trigger-wrap-focus" id="" style="width: 129px; visibility: visible;">
						<input type="hidden" name="productId" id="productId" value="">
						<input type="text" size="24" autocomplete="off" id="product" class=" x-form-text x-form-field  x-form-focus" style="width: 112px;">
						<img src="/crmsfagwt/org.opentaps.gwt.crmsfa.crmsfa/clear.cache.gif" class="x-form-trigger x-form-arrow-trigger " id="addproductimg">
					</div>
				</td>
				<td><input size="24" id="description" disabled="true" style="width: 300px;"></input></td>
				<td><input size="24" id="quantity" value="" style="width: 50px;"></input></td>
				<td><input size="24" disabled="true" id="pirce" value="" style="width: 100px;"></td>
				<td><input size="24" id="" value="" style="width: 100px;"></td>
				<td><input size="24" disabled="true" id="total" value="" style="width: 100px;"></td>
            </tr>
            <tr>
				<td>1,002</td>
				<td>amet</td>
				<td>consectetur</td>
				<td>adipiscing</td>
				<td>elit</td>
				<td>sit</td>
            </tr>
            
		</tbody>
	</table>
</div>


<div >
	<table>
		<tr>
			<td>
				<button type="button" id="commit">保存所有</button>
			</td>
		</tr>
	</table>
</div>
<input type="button"  name="button" Onclick="findAllClient()" value="查询所有的客户">
<input type="button"  name="button" Onclick="findAllProduct()" value="查询所有的产品">
<input type="button"  name="button" Onclick="createPersonContact()" value="创建用户">
<input type="button"  name="button" Onclick="findCustomerByName()" value="通过名字查询">

<input type="button"  name="button" Onclick="findCustomerByTeleCom()" value="通过名字查询">


<input type="button"  name="button" Onclick="gwtSuggestCustomers()" value="通过名字查询gwtSuggestCustomers">