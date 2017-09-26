<link href="/commondefine_css/Datetimepicker/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
<div id="inventory_report">
	<div class="inventory_report_content_wap">
		<div class="inventory_report_content">
			<div class="inventory_report_search">
				<div class="inventory_report_title">
					<span>Inventory Of Store</span>
				</div>
				<div class="inventory_report_condition"> 
					<form class="search_form" id="search_form" action="/report/control/ExportBANInventoryReport">
						<div class="base_condition">
							<div class="inventory_report_flag">
								<span>Store Group</span>
								<select class="inventory_report_flag_select"  name="productStoreGroupId" id="inventory_report_flag_select">
									<option value= >Select Store Group</option>
								</select>
							</div>
							<div class="inventory_report_type">
								<span>Store</span>
								<select class="inventory_report_type_select"  name="productStoreId" id="inventory_report_type_select2">
									<option value= >Select Store</option>
								</select>
							</div>
							<div class="inventory_report_name_type">
								<span>SKU</span>
								<input class="inventory_report_name_type_input"  name="productId" id="productId">
							</div>
							
							<div class="inventory_report_time">
								<span>Date</span>
								<input class="inventory_report_time_input report_time"  name="endtime" id="beginDate">
							</div>
							
						</div>
					</form>
					<div class="search_list">
						<a href="javascript:void(0);" class="search_btn">Search</a>
					</div>
				</div>
			</div>
			<div class="seller_report_detail">
                <div class="seller_report_detail_title">
                    <span class="seller_report_tit">Inventory Report</span>
                    <span class="seller_report_operator">
                        <a href="javascript:void(0);" class="export" >Export</a>
                    </span>
                </div>
                <div class="seller_report_detail_body">
                    <table class="product_list_table" id="product_list_table">
                        <thead>
                        <tr>          
                        	<th>Date</th>        
                            <th>SKU</th>
                            <th>Brand</th>
                            <th>Model</th>
                            <th>Description</th>
                            <th>Warehouse / Store</th>
                            <th>QTY</th>
                            <th>Unit Cost</th>
                            <th>Unit Retail Price</th>
                            <th>Sub Total Cost</th>
                            <th>Sub Total Retail Price</th>
                        </tr>
                        </thead>
                    	<tbody>
                    		<tr>
                    			<td colspan="11">0 results</td>
                    		</tr>
                    	</tbody>
                    </table>
                    <div class="tableFooter"></div>
                </div>
            </div>
		</div>
	</div>
</div>
<div class="error_tips">
    <div class="fixed_screen"></div>
    <div class="error_tips_box">
        <div class="error_tips_title">Message</div>
        <div class="error_tips_content">
            <span></span>
        </div>
        <div class="error_tips_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<script>
$(document).ready(function(){
	
	var SGroup=[];
	var Storeinfo=[];
	
	//初始化
	initSGroup();
	initCalendar();
	
	//错误信息
    function showErrorMessage(message){
        var errorTips = jQuery('.error_tips');
        errorTips.find('.error_tips_content span').html('').html(message);
        errorTips.show();

	    errorTips.find('.error_tips_operator a').unbind('click');
	    errorTips.find('.error_tips_operator').on('click','a',function () {
		    $('.error_tips').hide();
	    })
    }
	
	$("#inventory_report_flag_select").change(function () {  
		
	    $("#inventory_report_type_select2 option:not(:first)").remove();
	    
	    var selectedList = $(this).children('option:selected').val();
	    var strt = '';
	    
	    if(Storeinfo.length > 0){
	    	$.each(Storeinfo, function(index, elem){
	    		if(elem.storeGroupID == selectedList){
		    		strt += "<option value='"+elem.storeID+"'>"+elem.storeName+"</option>";
	    		}
	    	});
	    	$("#inventory_report_type_select2").append(strt); 
	    }
	}); 
	
	//搜索
	$('.search_btn').on('click', function(){
		var target=$(this);
		
		LookupInventoryReport({
			currentPage : 1,
			pageSize : 30
		},target);
	});
	//点击页签
	$('.tableFooter').on('click', 'a.homePage', function(){
		var _this = $(this);
		var currentPage = _this.attr('data-id');
		
		LookupInventoryReport({
			currentPage : currentPage,
			pageSize : 30
		});
	}).on('click', 'a.previousPage', function(){
		var currentPage = parseInt($('.tableFooter').find('.currentPageList').attr('data-id'), 10) - 1;
		
		if(currentPage < 1){
			
			showErrorMessage('No previous page');
			
			return false;
		}
		
		LookupInventoryReport({
			currentPage : currentPage,
			pageSize : 30
		});
	}).on('click', 'a.nextPage', function(){
		var currentPage = parseInt($('.tableFooter').find('.currentPageList').attr('data-id'), 10) + 1;
		var limitPage = parseInt($('.tableFooter').find('.endPage').attr('data-id'), 10);
		
		if(currentPage > limitPage){
			
			showErrorMessage('No next page');
			
			return false;
		}
		
		LookupInventoryReport({
			currentPage : currentPage,
			pageSize : 30
		});
	}).on('click', 'a.endPage', function(){
		var _this = $(this);
		var currentPage = parseInt(_this.attr('data-id'),10);
		
		LookupInventoryReport({
			currentPage : currentPage,
			pageSize : 30
		});
	}).on('click', 'a.pageList', function(){
		var _this = $(this);
		var currentPage = parseInt(_this.attr('data-id'),10);
		
		LookupInventoryReport({
			currentPage : currentPage,
			pageSize : 30
		});
	}).on('keypress', 'input.enterPage', function(e){
		var _this = $(this);
		var key = e.which;
		
		var currentPage = parseInt(_this.val(),10);
		if(key==13){
			
			var newExp = /^[1-9]\d*$/;
			if(!newExp.test(currentPage)){
				showErrorMessage('Please enter positive integer and greater than zero!');
				
				return false;
			}
			
			LookupInventoryReport({
				currentPage : currentPage,
				pageSize : 30
			});
		}
	});
	
	function LookupInventoryReport(options, target){
		
		var productStoreGroupId = $("#inventory_report_flag_select").children('option:selected').val();  
		var productStoreId = $("#inventory_report_type_select2").children('option:selected').val();  
		var productId = $("input[id='productId']").val().trim();
		var endtime = $("input[id='beginDate']").val();
		
		var facilityname = $("#facilityname").children('option:selected').text();
		//$(".product_list_table tr:not(:first)").remove();
		target&&$.showLoading(target);
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/report/control/LookupInventoryReport_BANINV',
	        data: {
	        	productStoreGroupId:productStoreGroupId,
				productStoreId:productStoreId,
				productId:productId,
				endtime:endtime,
				page:options.currentPage
			},
            success: function(data) {
            	
            	target && $.hideLoading(target);
            	if(data.responseMessage == 'success'){
            		$(".product_list_table tr:not(:first)").remove();
                    var dataResult = data;

                    formatDataForPage(dataResult, options);
            		
            	}else if(data.responseMessage == 'error'){
            		showErrorMessage(data.errorMessage);
            	}
            },
            error : function(option, status){
            	target && $.hideLoading(target);
            }	
    	});
	}
	
	
	function formatNumber(num, precision, separator) {
    var parts;
    // 判断是否为数字
    if (!isNaN(parseFloat(num)) && isFinite(num)) {
        // 把类似 .5, 5. 之类的数据转化成0.5, 5, 为数据精度处理做准, 至于为什么
        // 不在判断中直接写 if (!isNaN(num = parseFloat(num)) && isFinite(num))
        // 是因为parseFloat有一个奇怪的精度问题, 比如 parseFloat(12312312.1234567119)
        // 的值变成了 12312312.123456713
        num = Number(num);
        // 处理小数点位数
        num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
        // 分离数字的小数部分和整数部分
        parts = num.split('.');
        // 整数部分加[separator]分隔, 借用一个著名的正则表达式
        parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || ','));

        return parts.join('.');
    }
    return NaN;
}
	
	
    function formatDataForPage(datas, options){
    	var productListTable = $('.product_list_table');
    	var colsLength = productListTable.find('thead th').length;
    	var optionsList = options;
    	var dataList = datas;
    	
    	if(dataList.totalnumber == 0){
    		productListTable.find('tbody').html('<tr><td colspan="'+ colsLength +'">Not found data</td></tr>');
    		
    		$(".tableFooter").hide();
    		
    		return false;
    	}else{
    		$(".tableFooter").show();
    	}
    	
    	var totalPage = dataList.totalnumber % dataList.evepage == 0 ? parseInt(dataList.totalnumber / dataList.evepage, 10) : parseInt(dataList.totalnumber / dataList.evepage, 10)  + 1;
    	var firstPageInfo = '<div class="pageList"><a href="javascript:void(0);" class="homePage" data-id="1"><span class="page_home"></span></a><a href="javascript:void(0);" class="previousPage"><span class="page_prev"></span></a>';
    	var secondPageInfo = '<a href="javascript:void(0);" class="nextPage"><span class="page_next"></span></a><a href="javascript:void(0);" class="endPage" data-id="'+ totalPage +'"><span class="page_final"></span></a>';
    	var changePageInfo = '<a href="javascript:void(0);">GO</a><input type="text" class="enterPage" /></div><div class="totalNumber">Results: '+ dataList.totalnumber +'</div>'
    	var pageListInfo = '';
    	var trInfo = '';
    	var tableFooter = $('.tableFooter');
    	var tableTbody = productListTable.find('tbody');
    	
    	if(options.currentPage > totalPage){
    		showErrorMessage('Please enter Legal page number');
    		
    		return false;
    	}
    	
    	for(var i = 0; i < dataList.LookupInventoryReportFormslist.length; i++){
    		var qty = parseInt(dataList.LookupInventoryReportFormslist[i].quantityOnHandTotal, 10);
    		var unitCost = null, price = null, totalunitCost = null, totalprice = null;
    		if(!isNaN(dataList.LookupInventoryReportFormslist[i].unitCost)){
    			unitCost = Number(dataList.LookupInventoryReportFormslist[i].unitCost).toFixed(2);
    			totalunitCost = (unitCost * qty).toFixed(2);
    			unitCost=formatNumber(unitCost);
	    		totalunitCost=formatNumber(totalunitCost);
    		}else{
    			unitCost = dataList.LookupInventoryReportFormslist[i].unitCost;
    			totalunitCost = dataList.LookupInventoryReportFormslist[i].unitCost;
    		}
    		if(!isNaN(dataList.LookupInventoryReportFormslist[i].unitCost)){
    			price = Number(dataList.LookupInventoryReportFormslist[i].price).toFixed(2);
    			totalprice = (price * qty).toFixed(2);
    			price=formatNumber(price);
	    		totalprice=formatNumber(totalprice);
    		}else{
    			price = dataList.LookupInventoryReportFormslist[i].price;
    			totalprice = dataList.LookupInventoryReportFormslist[i].price;
    		}
    		trInfo += '<tr>'+ 
    				'<td>'+ dataList.time +'</td>'+
    				'<td>'+ dataList.LookupInventoryReportFormslist[i].productId +'</td>'+
    				'<td>'+ dataList.LookupInventoryReportFormslist[i].brandName +'</td>'+
    				'<td>'+ dataList.LookupInventoryReportFormslist[i].productname +'</td>'+
    				'<td>'+ dataList.LookupInventoryReportFormslist[i].description +'</td>'+
    				'<td>'+ dataList.LookupInventoryReportFormslist[i].facilityName +'</td>'+
    				'<td>'+ formatNumber(qty) +'</td>'+
    				'<td>'+ unitCost +'</td>'+
    				'<td>'+ price +'</td>'+
    				'<td>'+ totalunitCost +'</td>'+
    				'<td>'+ totalprice +'</td>'+
    		'</tr>';
    	}
    	
    	tableTbody.html(trInfo);
    	
    	if(totalPage > 5){
        	if(options.currentPage < 4){
        		for(var i=1; i<=5; i++){
        			if(options.currentPage == i){
        				pageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
        			}else{
        				pageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
        			}
        		}
        	}else{   		
        		if(totalPage - options.currentPage <= 2){
        			for(var i= totalPage-4 ; i<= totalPage; i++){
            			if(options.currentPage == i){
            				pageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
            			}else{
            				pageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
            			}
            		}
        		}else{
        			for(var i=options.currentPage-2; i<= options.currentPage + 2; i++){
            			if(options.currentPage == i){
            				pageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
            			}else{
            				pageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
            			}
            		}
        		}
        		
        	}
    	}else{
    		for(var i=1; i<=totalPage; i++){
    			if(options.currentPage == i){
    				pageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
    			}else{
    				pageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
    			}
    		}
    	}
    	
		var info = firstPageInfo + pageListInfo + secondPageInfo + changePageInfo;

		tableFooter.html(info);
    }
	
	function initCalendar(){
		$('#beginDate').datetimepicker({
	        language:'en',
	        format: 'dd/mm/yyyy',//显示格式
	        minView: "month",//设置只显示到月份
	        initialDate: new Date(new Date()-1000*60*60*24),//初始化当前日期
	        autoclose: true,//选中自动关闭
	        todayBtn: true, //显示今日按钮
	        maxView:"year",
	        endDate: new Date(new Date()-1000*60*60*24)
		});
		$("#beginDate").val(formatDate(new Date(new Date()-1000*60*60*24)));
	}

	function formatDate(date) {  
	    var y = date.getFullYear();  
	    var m = date.getMonth() + 1;  
	    m = m < 10 ? '0' + m : m;  
	    var d = date.getDate();  
	    d = d < 10 ? ('0' + d) : d;  
	    return d + '/' + m + '/' + y;  
	}
	

	$('.export').click(function(e) {
		
    	$("#search_form").submit();
    	return false;
    });
	

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "/";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }      
        var currentdate = strDate + seperator1 + month + seperator1 + date.getFullYear()
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
        return currentdate;
    }   
    
    //初始化店铺租
    function initSGroup(){
    	jQuery.ajax({
    		dataType:'json',
    		type: 'post',
    		data:{
    			groupType : "STORE_GROUP_Warehouse"
    		},
    		url: '/report/control/SearchAvailableStoresAndSGroups_BANINV',
    		success: function (data) {
	  			if(data.responseMessage=="success"){
	  				var storegroups_info = data.SGroup;
	  				var store_info = data.SGMember;
	  				
    				var storegroups_info_content = '';
    				var store_info_content = '';
    				
    				if(storegroups_info.length > 0){
    					$.each(storegroups_info,function(index, elem){
    						storegroups_info_content +=  "<option value='"+elem.sGroupId+"'>"+elem.sGroupName+"</option>";
    					});
    					
    					SGroup = storegroups_info;
    					
    					$("#inventory_report_flag_select").append(storegroups_info_content);
    				}
    				
    				var productStoreGroupId = $("#inventory_report_flag_select").children('option:selected').val(); 
    				if(store_info.length > 0){
    					$.each(store_info,function(index, elem){
    						if(elem.storeGroupID == productStoreGroupId){
        						store_info_content +=  "<option value='"+elem.storeID+"'>"+elem.storeName+"</option>";
    						}
    					});
    					
    					Storeinfo = store_info;
    					
    					$("#inventory_report_type_select2").append(store_info_content);
    				}
	  			}else if(data.responseMessage=="error"){
	  				showErrorMessage(data.errorMessage);
	  				$(".search_list").hide();
	  			}
	  		}    
    	});
    }

});
</script>