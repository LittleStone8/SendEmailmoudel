<link rel="stylesheet" type="text/css" href="/commondefine_js/select2/select2.css">
<link href="/commondefine_css/yw_main.css" rel="stylesheet">
<link href="/commondefine_css/Datetimepicker/bootstrap.min.css" rel="stylesheet">
<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
<div id="stockinReport">
	<div class="stockinReport_box">
		<div class="stockinReport_content">
			<div class="stockinReport_search">
				<div class="stockinReport_title">
					<span>Operator Stockin Report</span>
				</div>
				<div class="stockinReport_condition"> 
					<form class="search_form" id="search_form" action="/warehouse/control/exportOperatorsReceivedData">
						<div class="base_condition">
							<div class="stockinReport_productId">
								<div>Product ID</div>
								<input class="stockinReport_input"  name="productId" id="productId">
								<ul class="productId_ul" style="display:none;" tabindex="11" ></ul>
							</div>
							
							<div class="stockinReport_beginDate">
								<div>Start Date</div>
								<input class="stockinReport_input laydate-icon some_class report_time"  id="beginDate" readonly="readonly">
								<input type="hidden"  name="startDate">
							</div>
							
							<div class="stockinReport_endDate">
								<div>End Date</div>
								<input class="stockinReport_input laydate-icon some_class report_time"  id="endDate" readonly="readonly">
								<input type="hidden"  name="endDate">
							</div>
							
							<div class="stockinReport_facility">
								<div>Warehouse</div>
								<select class="stockinReport_select"  name="facility" id="facility">
									<option value= >Please select</option>
								</select>
							</div>
						</div>
					</form>
					<div class="search_list">
						<a href="javascript:void(0);" class="search_btn">Search</a>
					</div>
				</div>
			</div>
			<div class="stockinReport_detail">
                <div class="stockinReport_detail_title">
                    <span class="stockinReport_tit">Operator Stockin Report</span>
                    <span class="stockinReport_operator">
                        <a href="javascript:void(0);" class="export" >Export</a>
                    </span>
                </div>
                <div class="stockinReport_detail_body">
                    <table class="stockinReport_list_table" id="stockinReport_list_table">
                        <thead>
                        <tr>          
                        	<th>Product ID</th>
                            <th>Description</th>
                            <th>Unit Cost</th>
                            <th>Received Quantity</th>
                            <th>Transfer Quantity</th>
                            <th>Total Quantity</th>
                            <th>Received Time</th>
                            <th>Warehouse</th>
                        </tr>
                        </thead>
                    	<tbody>
                    		<tr>
                    			<td colspan="8">0 results</td>
                    		</tr>
                    	</tbody>
                    </table>
                    <div id="tablePager"></div>
                </div>
            </div>
		</div>
	</div>
</div>
<script type="text/javascript" src="/commondefine_js/jquery.js"></script>
<script type="text/javascript" src="/commondefine_js/Datetimepicker/bootstrap-datetimepicker.js"></script>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/html" id="PageContent">
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
<script type="text/html" id="stockinContent">

	{{if result.length>0}}
		{{each result as value i}}
			<tr>
			 <td>{{value.productId}}</td>
			 <td>{{value.brandName}} {{if value.model}}|{{/if}} {{value.model}} {{if value.description}}|{{/if}} {{value.description}}</td>
			 <td>{{value.unitCost | formatDiff: 'decimal'}}</td>
             <td>{{value.receiveQuantity | formatDiff: 'integer'}}</td>
			 <td>{{value.transferQuantity | formatDiff: 'integer'}}</td>
			 <td>{{value.totalQuantity | formatDiff: 'integer'}}</td>
			 <td>{{value.receiveTime}}</td>
			 <td>{{value.facilityName}}</td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="8">No fund data</td>
			</tr>
	{{/if}}
			    
</script>
<script type="text/html" id="facilityTypeColumn">
{{if inventoryFacilitys}}
      {{each inventoryFacilitys as value}}
         <option value="{{value.facilityId}}">{{value.facilityName}}</option>
      {{/each}}
   {{/if}}
</script>
<script>
	initCalendar();
	
	function initCalendar(){
  		var timeZones = $('#timeNow').val() - 0;
  		
  		$("#beginDate").val(formatDate(new Date(timeZones - 1000*60*60*48)));
  		$("#endDate").val(formatDate(new Date(timeZones - 1000*60*60*24)));
  		
  		$('#beginDate').datetimepicker({
  	        language:'en',
  	        format: 'dd/mm/yyyy',//显示格式
  	        minView: "month",//设置只显示到月份
  	        initialDate: new Date(timeZones),
  	        autoclose: true,//选中自动关闭
  	        todayBtn: true, //显示今日按钮
  	        maxView:"year",
  	        startDate: new Date(2017,6,26),
  	        endDate: new Date(timeZones)
  		});
  		
  		$('#endDate').datetimepicker({
  	        language:'en',
  	        format: 'dd/mm/yyyy',//显示格式
  	        minView: "month",//设置只显示到月份
  	        initialDate: new Date(timeZones),
  	        autoclose: true,//选中自动关闭
  	        todayBtn: true, //显示今日按钮
  	        maxView:"year",
  	        startDate: new Date(2017,6,26),
  	        endDate: new Date(timeZones + 1000*60*60*24)
  		});
  	}

  	function formatDate(date) {  
  	    var y = date.getFullYear(); 
  	    var m = date.getMonth() + 1;  
  	    	m = m < 10 ? '0' + m : m;  
  	    var d = date.getDate();  
  	    	d = d < 10 ? ('0' + d) : d;  
  	  	return d + '/' + m + '/' + y; 
  	}
</script>
<script>
require.config({
	paths:{
		select2 : '/commondefine_js/select2/select2'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	template.helper('formatDiff', function(price, type) {
	    if(price){
	        if(type == 'integer') {
	        	var arrayPrice = price.toString().split(".");
	            return arrayPrice[0]?arrayPrice[0]:"0";
	        }else if(type == 'decimal'){
	        	var arrayPrice = Number(price);
	        	return (typeof arrayPrice === 'number' ? arrayPrice.toFixed(2) : arrayPrice);
		    }
	    }else{
	    	return price;
	    }
	});
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	'keyup #productId' : 'lookAutoCompelete',
        	'click #tablePager .paging a' : "changePageEvent",
        	'keypress #tablePager .goPage .go_page_input' : "jumpPageEvent",
        	'click .stockinReport_search .search_btn' : "getTableData",
        	'click .stockinReport_detail .export' : "exportTableData",
        },
        __propertys__: function () {
        }, 
        initialize: function ($super) {
            $super();
            this.initFacilityData();
            this.initEvent();
            this.getTableData(null, "1");
            
			this.timeOut = null;
			this.timer = 1000;
        },
        initEvent : function(){
        	
        	$(document).click(function(){
        		$('.productId_ul').hide();
        	});
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
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
					t.getTableData(null, $(e.currentTarget).val());
			    }
			}
		},
		lookAutoCompelete : function(e){
			var t = this;
	    	var productId = $('#productId').val().trim();
	    	var productId_ul = $('.productId_ul');
	    	
	    	if(productId == ''){
	    		clearTimeout(t.timeOut);
	    		productId_ul.hide();
	    		
	    		return false;
	    	}
	    	clearTimeout(t.timeOut);
	    	t.timeOut = setTimeout(function(){
	            $.ajax({
	                type: 'post',
	                dataType: 'json',
	                url: '/warehouse/control/queryProductByCondition',
	                data : JSON.stringify({
	                	params: productId+''
	                }),
	                success: function(data) {
	                	if(data.resultCode == 1){
	                    	t.myAutoComplete(data.result);
	                    }
	                }
	            }) 
	    	},t.timer);
		},
		myAutoComplete : function(dataOption){
			var t = this;
			
	    	var productId = $('#productId');
	        var productId_ul = $( ".productId_ul" );
	        
	    	var dataList = dataOption;
	    	var htmlContent = '';
	    	
	    	if(dataList.length == 0){
	    		productId_ul.hide();
	    		
	    		return false;
	    	}
	    	
	    	$.each(dataList,function(index, item){
	    		htmlContent += '<li data-productId="'+ item.productId +'">'+ item.productId + ':' +item.brandName + ' ' + item.internalName +'</li>'
	        	
	    	});
	    	
	    	productId_ul.html(htmlContent).show();
	    	
	    	productId_ul.find('li').click(function(){
	    		var _this = $(this);
	    		var productIdval = _this.attr('data-productId');
	    		productId.val(productIdval);
	    		productId_ul.hide();
	    	});
	    },
        initFacilityData:function(){
			var t=this;
			$.ajax({
				url:'/warehouse/control/queryFacility',
				type: 'post',
	            dataType: 'json',
				success:function(results){					
					var content=template("facilityTypeColumn",results);
					$(".stockinReport_select").append(content);
					
					require(['select2'], function(select2){
						$(".stockinReport_select").select2();
					});
				}
			});		
		},
		exportTableData : function(e){
			var t = this;
			var commonParams = t.getParams();
			$('[name="startDate"]').val(commonParams.startDate);
			$('[name="endDate"]').val(commonParams.endDate);
			
			$('#search_form').submit();
		},
		getTableData : function(e, pageNum){
			var t = this;
			var pageNumList = pageNum ? pageNum : "1";
			if(!t.getParams()){
				return false;
			}
			var commonParams = $.extend({},{ pageNum : pageNumList }, t.getParams());
			
			e && $.showLoading($(e.currentTarget));
           	$.ajax({
   				url:'/warehouse/control/queryOperatorsReportData',
   				type: 'post',
   	            dataType: 'json',
   	         	data : JSON.stringify(commonParams),
   	           	success:function(results){
   	           		e && $.hideLoading($(e.currentTarget));
   	           		
   					if(results.resultCode==1){
   						
   						if(results.result.length > 0){
   							$.each(results.result, function(index, elem){
   								elem.totalQuantity = (parseInt(elem.receiveQuantity,10) + parseInt(elem.transferQuantity, 10));
   							});
   						}
   						$('#stockinReport_list_table tbody').html(template("stockinContent",{
   		            		result : results.result
   		            	}));
   						$('#tablePager').html(template('PageContent',{
   							totalNum : results.totalNum,
   							pageNum : results.pageNum,
   							totalPage : results.totalPage,
   							nums : t.getPageNumberList(results)
   		    			}));
   					}
   	           	},
   	            error : function(option, status){
   	            	e && $.hideLoading($(e.currentTarget));
   	            }
           	});
		},
		getPageNumberList : function(option){
        	var t = this;
        	
			t.page = {
				pageNum : option.pageNum,
				totalPage : option.totalPage
			};
			var nums = [];
			
			for(var i=option.startPage; i<= option.endPage; i++){
				nums.push(i);
			}
			
			return nums;
        },
        getParams : function(){
        	var t = this;
        	
        	if(t.formatDateToYMD($('#beginDate').val()) >= t.formatDateToYMD($('#endDate').val())){
        		
        		t.showToast("The End Date can't beyond the beginn Date");
        		
        		return false;
        	}
        	
        	if(!$('#beginDate').val()){
        		
        		t.showToast("Beginn date can't be null");
        		
        		return false;
        	}
        	
        	if(!$('#endDate').val()){
        		
        		t.showToast("End date can't be null");
        		
        		return false;
        	}
        	
        	return {
 				facilityId : $('#facility').val(),
 				productId : $('#productId').val(),
 				startDate : t.formatDateEvt($('#beginDate').val()),
 				endDate : t.formatDateEvt($('#endDate').val()),
 				pageSize: "15"
 			}
        },
        formatDateEvt : function(date){
        	if(!date) {
        		return date;
        	}
        	var dateList = date.split('/');
        	
        	return dateList[2] + '-' + dateList[1] + '-' + dateList[0];
        },
        formatDateToYMD : function(date) { 
      		var arr=date.split("/");
      		var str="";
      		for(var i=arr.length;i>0;i--){
      			str+=arr[i-1]+"/";
      		}  	    
      	  	return str; 
      	},
	});

	new View();
})
</script>