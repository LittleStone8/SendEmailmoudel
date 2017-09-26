<div id='searchOrders'>
	<div class="searchOrders_wap">
		<div class="searchOrders_content">
			<div class="searchOrders_title">Search By</div>
			<div class="searchOrders_condition">
				<div class="condition_group">
					<span>Store</span>
					<select class="store" name="store">
						<option value="">All</option>
					</select>
				</div>
				<div class="condition_group">
					<span>Product</span>
					<input type="text" name="product" data-type="product" class="product" />
					<ul class="productId_ul" style="display:none;" tabindex="11" ></ul>
				</div>
				<div class="condition_group">
					<span>Customer</span>
					<input type="text" name="customer" data-type="customer" class="customer" />
					<ul class="customerId_ul" style="display:none;" tabindex="12" ></ul>
				</div>
				<div class="condition_group">
					<span>Order ID</span>
					<input type="text" name="orderId" class="orderId" />
				</div>
				<div class="condition_group">
					<span>Status</span>
					<select class="status" name="status">
						<option value="">All</option>
						<option value="ORDER_COMPLETED" selected="selected">Completed</option>
						<option value="ORDER_CANCELLED">Cancelled</option>
					</select>
				</div>
				<div class="condition_group">
					<span>From Date</span>
					<input type="text" name="fromDate" class="fromDate laydate-icon" />
				</div>
				<div class="condition_group">
					<span>Thru Date</span>
					<input type="text" name="thruDate" class="thruDate laydate-icon" />
				</div>
				<div class="condition_group">
					<span>Payment Method</span>
					<select class="paymentWay" name="paymentWay">
						<option value="">All</option>
						<option value="CASH">Cash</option>
						<option value="CREDIT">Credit</option>
					</select>
				</div>
				<div class="condition_group">
					<span>Fix Order</span>
					<select class="fixOrder" name="fixOrder">
						<option value="">All</option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
				</div>
				<div style="clear:both;"></div>
				<div class="search_btn"><a href="javascript:void(0);" class="searchOrders_btn">Search</a></div>
			</div>
		</div>

		<div class="searchOrders_list">
			<table class="searchOrders_table">
				<thead>
					<tr>
						<th><span class="title_content">Ordered Date<div class="icon_content"><span class="arrow_up" data-type="orderDate ASC"></span><span class="arrow_down" data-type="orderDate DESC"></span></div></span></th>
						<th class="textAlignLeft"><span class="title_content">Order ID<div class="icon_content"><span class="arrow_up" data-type="itemOrderId ASC"></span><span class="arrow_down" data-type="itemOrderId DESC"></span></div></span></th>
						<th class="textAlignLeft">Purchased Goods</th>
						<th><span class="title_content">Customer<div class="icon_content"><span class="arrow_up" data-type="customer ASC"></span><span class="arrow_down" data-type="customer DESC"></span></div></span></th>
						<th class="textAlignRight"><span class="title_content">Amount(<span class="current_uom"></span>)<div class="icon_content"><span class="arrow_up" data-type="grandTotal ASC"></span><span class="arrow_down" data-type="grandTotal DESC"></span></div></span></th>
						<th><span class="title_content">Store<div class="icon_content"><span class="arrow_up" data-type="storeName ASC"></span><span class="arrow_down" data-type="storeName DESC"></span></div></span></th>
						<th>Status</th>
						<th>Fix Order</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div id="tablePager"></div>
		</div>
	</div>
</div>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/html" id="listContent">

	{{if result.length>0}}
	{{each result as value i}}
	<tr>
		<td>{{value.orderDate}}</td>
		<td class="textAlignLeft"><a href="/crmsfa/control/orderview?orderId={{value.itemOrderId}}" target="_blank">{{value.itemOrderId}}</a></td>
		<td class="textAlignLeft"><a class="purchasedGoods" href="/crmsfa/control/orderview?orderId={{value.itemOrderId}}" target="_blank">{{value.purchasedGoods}}</a></td>
		<td>{{value.customer}}</td>
		<td class="textAlignRight">{{value.grandTotal | formatAmount }}</td>
		<td>{{value.storeName}}</td>	
		<td>{{value.statusDescription}}</td>	
		{{if value.isFixOrder == 'N' }} 
			<td>-</td>
			{{else}}
			<td>{{value.isFixOrder}}</td>
		{{ /if }}
	</tr>
	{{/each}}
	{{ else }}
	<tr>
		<td colspan="8">No fund data</td>
	</tr>
	{{/if}}
	
</script>
<script type="text/html" id="pageContent">
	{{if totalNum>0}}
		<div class="goPage">
			<span>GO</span>
			<input type="text" data-big-num="{{totalPage}}" class="go_page_input"/>
		</div>
		<ul class="paging clearfix" id="fenye">
            <li ><a href="javascript:void(0)"  data-type="home" data-num="1" ><span  class="page_home"></span></a></li>
            <li ><a href="javascript:void(0)"  data-num="{{pageNum-1}}" ><span  class="page_prev"></span></a></li>
			{{each nums as value}}
                {{if value==pageNum}}
                    <li class='active'><a href="javascript:void(0)" data-num="{{value}}" >{{value}} <span class='sr-only'>{{value}}</span></a></li>
                {{/if}}
                {{if value!=pageNum}}
                    <li ><a href="javascript:void(0)" data-num="{{value}}">{{value}} <span class='sr-only'>{{value}}</span></a></li>
                {{/if}}
            {{/each}}
           <li ><a href="javascript:void(0)"  data-num="{{pageNum+1}}"><span  class="page_next"></span></a></li>
           <li ><a href="javascript:void(0)" data-type="final"  data-num="{{totalPage}}"><span class="page_final"></span></a></li>
		</ul>
		<div style="clear:both;"></div>
	{{/if}}
</script>
<script>
require.config({
	paths:{
		layDate:'/commondefine_js/layDate/laydate'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	template.config("escape", false);
	template.helper('formatAmount', function(value) {
	    if(value){
		    var parts;
		    if (!isNaN(parseFloat(value)) && isFinite(value)) {
		    	
		        parts = value.toString().split('.');
		        parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (','));
				
		        return parts.join('.');
		    }
		    return NaN;
	    }else{
	    	return value;
	    }
	});
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	'click .searchOrders_condition .fromDate, .searchOrders_condition .thruDate':'showDate',
        	'click #tablePager .paging a' : "changePageEvent",
        	'keypress #tablePager .goPage .go_page_input' : "jumpPageEvent",
        	'click .searchOrders_condition .searchOrders_btn' : "getTableData",
        	'keyup .product' : 'lookAutoCompelete',
        	'keyup .customer' : 'lookAutoCompelete',
        	"click .searchOrders_table thead .arrow_up, .searchOrders_table thead .arrow_down":"orderBy",
        },
        __propertys__: function () {
        }, 
        initialize: function ($super) {
            $super();
            
            this.initStore();
            this.initUom();
            this.initEvent();
            this.getTableData(null,"1");
            
			this.timeOut = null;
			this.timer = 1000;
			this.clickType = null;
        },
        initEvent : function(){
        	
        	$(document).click(function(){
        		$('.productId_ul').hide();
        		$('.customerId_ul').hide();
        	});
        },
        showDate:function(e){
 		   require(['layDate'], function(layDate){
 			   layDate({
 				   eventArg: e,
 				   istime: true,
 				   format: 'DD/MM/YYYY'
 			   });
 		   });
        },
         orderBy : function(e){
         	var t = this;
         	var currentTarget = e.currentTarget;
         	var type = $(currentTarget).attr("data-type");

         	var pageNum = $("#tablePager li.active a").attr('data-num');
         	
         	t.clickType = type;
     		t.getTableData(null, pageNum, type);
         },
         changePageEvent:function(e){
         	var t = this;
         	var evt = e.currentTarget;
 			if(t.page.pageNum==$(evt).attr('data-num')){
 				return false;
 			}
 			if($(evt).attr('data-num')<1){
 				return false;
 			}
 			if($(evt).attr('data-num')>t.page.totalPage){
 				return false;
 			}
 	    	t.getTableData(null, $(evt).attr('data-num'));
 		},
 		jumpPageEvent:function(e){
 			var t=this;
 			var key = e.which;
 			var reg= /^\b[1-9]\d*$/;
 			if(key==13){					
 				if (
 						$(e.currentTarget).val() 
 						&& reg.test($(e.currentTarget).val())
 						&& $(e.currentTarget).val() <= parseInt($(e.currentTarget).attr("data-big-num"))
 					) {
 					
 					t.getTableData(null, $(e.currentTarget).val());
 			    }
 			}
 		},
 		lookAutoCompelete : function(e){
			var t = this;
	    	var value = $(e.currentTarget).val().trim();
	    	var ul = $(e.currentTarget).siblings('ul');
	    	
	    	if(value == ''){
	    		$(e.currentTarget).attr('data-id', '');
	    		
	    		clearTimeout(t.timeOut);
	    		ul.hide();
	    		
	    		return false;
	    	}
	    	clearTimeout(t.timeOut);
	    	t.timeOut = setTimeout(function(){
	    		if($(e.currentTarget).attr('data-type') == 'product'){
		            $.ajax({
		                type: 'post',
		                dataType: 'json',
		                url: '/ordermgr/control/gwtSuggestProduct',
		                data : {
		                	start:0,
		                	limit:12,
		                	filterCol:"text",
		                	query:value,
		                	sort:"productId",
		                	dir:"ASC"
		                },
		                success: function(data) {
		                	if(data.items && data.items.length > 0){
		                		t.myAutoComplete(e, data.items);
		                	}else{
		                		ul.hide();
		                	}
		                }
		            })
	    		}else if($(e.currentTarget).attr('data-type') == 'customer'){
	    			$.ajax({
	    		        type: 'post',
	    		        dataType: 'json',
	    		        url: '/ordermgr/control/LookupAutoComplete',
	    		        data: {
	    		        	param : value,
	    		        },
	    		        success: function (data) {
		                	if(data.customerList && data.customerList.length > 0){
		                		t.myAutoComplete(e, data.customerList);
		                	}else{
		                		ul.hide();
		                	}
	    		       	}	
	    	    	});
	    		} 
	    	},t.timer);
		},
		myAutoComplete : function(e, dataOption){
			var t = this;
			
	    	var value = $(e.currentTarget).val().trim();
	    	var ul = $(e.currentTarget).siblings('ul');
	        
	    	var dataList = dataOption;
	    	var htmlContent = '';
	    	
	    	if($(e.currentTarget).attr('data-type') == 'product'){
		    	$.each(dataList,function(index, item){
		    		if(index > 0){
			    		htmlContent += '<li title="'+ item.text +'" data-productId="'+ item.id +'" data-text="'+ item.text +'">'+ item.text +'</li>'
		    		}        	
		    	});
	    	}else if($(e.currentTarget).attr('data-type') == 'customer'){
	    		$.each(dataList,function(index, item){
	    			htmlContent += '<li title="'+ item.firstName + ":" + item.primaryPhoneNumber +'" data-productId="'+ item.partyId +'" data-text="'+ item.firstName +'">'+ item.firstName + ":" + item.primaryPhoneNumber +'</li>';
	    		});
	    	}
	    	
	    	ul.html(htmlContent).show();
	    	
	    	ul.find('li').click(function(){
	    		var _this = $(this);
	    		var productIdval = _this.attr('data-text');
	    		var dataId = _this.attr('data-productId');
	    		
	    		$(e.currentTarget).val(productIdval);
	    		$(e.currentTarget).attr('data-id', dataId);
	    		
	    		ul.hide();
	    	});
	    },
 		initUom : function(){
 			var t = this;
 			
    		$.ajax({
    	        type: 'post',
    	        dataType: 'json',
    	        url: '/catalog/control/findPriceUOM',
    	        data: {},
    	        success: function (data) {
    	        	if(data.responseMessage == 'success'){
    	        		var dataResult = data.uomMap;
    	        		var currentUom = $('.current_uom');

    	        		currentUom.html(dataResult.uomId);
    	        	}else if(data.responseMessage == 'error'){
                    	showErrorMessage(data.errorMessage);
                    }
    	       	}	
    		});
 		},
 		initStore : function(){
 			var t = this;
 			
 	        $.ajax({
 	            type: 'post',
 	            dataType: 'json',
 	            url: '/ordermgr/control/findSalesStore',
 	            data: {},
 	            success: function (data) {
 	                var dataResult = data;
 	                if(dataResult.responseMessage == 'success'){
 	                    if(dataResult.store){
 	                        var store = $('.store');
 	                        var contentOption = '';
 	                       
 	                        store.children().not(':first-child').remove();

 	                        if(dataResult.store.length > 0){
 	                            $.each(dataResult.store,function(key,value){
 	                                contentOption +="<option value='"+value.productStoreId+"' >"+value.storeName+"</option>";
 	                            });
 	                        }
 	                        store.append(contentOption);
 	                    }
 	                }else if(dataResult.responseMessage == 'error'){
 	                	showErrorMessage(data.errorMessage);
 	                }
 	            }
 	        });
 		},
         getTableData : function(e, pageNum, type){
        	 var t = this;
        	 var pageNumList = pageNum ? pageNum : "1";
        	 if(!this.getParams(pageNumList)){
        		 return false;
        	 }
			 var commonParams = $.extend({}, this.getParams(pageNumList), {
				 orderBy : 	e ? 'orderDate DESC' : (t.clickType	? t.clickType : 'orderDate DESC')
			 });
			
 			 e && $.showLoading($(e.currentTarget));
			 $.ajax({
				url:'/ordermgr/control/findSalesOrdersList',
				type: 'post',
          		dataType: 'json',
          		contentType : 'application/json;charset=utf-8',
          		data:JSON.stringify(commonParams),
         		success:function(results){
         			e && $.hideLoading($(e.currentTarget));         	
					if(results.success){						
					   if(results.data.salesOrderBOList && results.data.salesOrderBOList.length > 0){
							$.each(results.data.salesOrderBOList, function(index, elem){
								var purchasedGoods = [];
								purchasedGoods=elem.productDesc.replace("null","").split(" -- ");						
								if(purchasedGoods.length == 1){
									elem.purchasedGoods = purchasedGoods.join('');
								}else if(purchasedGoods.length <= 3){
									elem.purchasedGoods = purchasedGoods.join('<br/>');
								}else{
									elem.purchasedGoods = ((purchasedGoods.slice(0,3)).concat('...')).join('<br/>');
								}
							})
							
						}
						
						$('.searchOrders_list .searchOrders_table tbody').html(template("listContent",{
	        				result : results.data.salesOrderBOList ? results.data.salesOrderBOList : []
	        			}));
						
						if(results.data.pageMap){
							$('#tablePager').html(template('pageContent',{
								totalNum : results.data.pageMap.totalNum,
								pageNum : results.data.pageMap.pageNum,
								totalPage : results.data.pageMap.totalPage,
								nums : t.getPageNumberList(results.data.pageMap)
		   					}));
						}
						
					}else{
						t.showToast(results.errorMessage);
					}
	         	},
	 			error : function(){
	 				e && $.hideLoading($(e.currentTarget));
	 			}
  			}); 
         },
         getParams : function(number){
        	var t = this;
        	
        	if(t.formatDateToYMD($('.thruDate').val()) < t.formatDateToYMD($('.fromDate').val())){
        		if(t.formatDateToYMD($('.thruDate').val())){
            		t.showToast("The End Date can't beyond the begin Date");
            		
            		return false;
        		}
        	}
        	 
	      	var params = {
	      		productStoreId  : $('.store').val(),
	      		productId : $('.product').attr('data-id') ? $('.product').attr('data-id') : '',
	      		partyId : $('.customer').attr('data-id') ? $('.customer').attr('data-id') : '',
				orderId : $('.orderId').val().trim(),
				statusId : $('.status').val(),
				fromDate : t.getTimeZonesEvent($('.fromDate').val()) == '' ? 0 : t.getTimeZonesEvent($('.fromDate').val()),
				thruDate : t.getTimeZonesEvent($('.thruDate').val()) == '' ? 0 : t.getTimeZonesEvent($('.thruDate').val()),
				paymentWay : $('.paymentWay').val(),
				isFixOrder : $('.fixOrder').val(),
				shipment : "",
				pageN : number,
				rawOffset: $('#rawOffset').val(),
				pageSize : "20"
	      	}
         	
         	return params;
         },
         getPageNumberList : function(option){
         	var t = this;
         	
 			t.page = {
 				pageNum : option.pageNum,
 				totalPage : option.totalPage
 			};
 			var nums = [];
 			
 			if(option.totalPage <= 5){
 				for(var i=1; i<= option.totalPage; i++){
 	 				nums.push(i);
 	 			}
 			}else{
 	 			if(option.pageNum - 2 <= 0){
 	 	 			for(var i=1; i<= 5; i++){
 	 	 				nums.push(i);
 	 	 			}
 	 			}else if(option.pageNum + 2 >= option.totalPage){
 	 	 			for(var i=option.totalPage - 4; i<= option.totalPage; i++){
 	 	 				nums.push(i);
 	 	 			}
 	 			}else{
 	 				for(var i=option.pageNum - 2; i<= option.pageNum + 2; i++){
 	 	 				nums.push(i);
 	 	 			}
 	 			}
 			}
 			
 			return nums;
         },
         formatDateToYMD : function(date) { 
        	if(!date) return date;
        	
       		var arr=date.split("/");
       		var str="";
       		for(var i=arr.length;i>0;i--){
       			str+=arr[i-1]+"/";
       		}  	    
       	  	return str; 
       	},
       	getTimeZonesEvent : function(date){
       		if(!date) return date;
       		
       		var arr=date.split("/");
       		
       		return (new Date(arr[2]+'-'+arr[1]+'-'+arr[0])).getTime();
       	}
	});

	new View();
})
</script>