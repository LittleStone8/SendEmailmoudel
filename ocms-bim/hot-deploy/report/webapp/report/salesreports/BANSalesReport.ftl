<link href="/commondefine_css/DataTable/jquery.dataTables.min.css" rel="stylesheet">
<link href="/commondefine_css/Datetimepicker/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
<link href="/commondefine_css/jQuery/jquery-ui.css" rel="stylesheet">
<div id="seller_reports">
	<input type="hidden" id="countryId" />
	<input type="hidden" id="role" />
	<div class="seller_reports_wap">
		<div class="seller_reports_content">
			<div class="seller_reports_choose">
				<div class="seller_reports_choose_title">Sales Report</div>
				<div class="seller_reports_choose_content">
					<form id="search_form" class="search_form" action="/report/control/ExportSalesReport">
						<div class="condition">
							<div class="product_flag">
								<span>Product ID</span> <input type="text" id="productIdInput"
									name="productId"></input>
							</div>
	
							<div class="choose_store_group">
								<span>Store Groups</span> <select id="selectGroup" name="sGroupId">
									<option value= >Select Store Groups</option>
								</select>
							</div>
	
							<div class="choose_store">
								<span>Store</span> 
								<select id="selectStore" name="storeId">
									<option value= >Select Store</option>
								</select>
							</div>
	
							<div class="start_time">
								<span>Start Date</span> <input type="text" class="some_class report_time" name="beginDate"
									id="beginDate"  />
							</div>
	
							<div class="end_time">
								<span>End Date</span> <input type="text" class="some_class report_time" name="endDate"
									id="endDate"  />
							</div>
							
							<div class="choose_payment_method">
								<span>Payment Method</span> 
								<select id="paymentMethod" name="paymentMethod">
									<option value="" >Select Payment Method</option>
									<option value="CASH">Cash</option>
									<option value="CREDIT">Credit</option>
								</select>
							</div>
							
							<input type="hidden" value="" id="reportRawOffset" name="rawOffset"/>
							
							<input type="hidden" value="" id="type" name="type"/>
	
							<div id="searchButton" class="operator_list">
								<a href="javascript:void(0);" class="searchTableList">Search</a>
							</div>
						</div>
					</form>
				</div>
			</div>

			<div class="seller_reports_detail">
				<div class="seller_reports_first">
					<div class="seller_reports_first_title">
						<span class="seller_reports_first_tit">Sales Details</span> 
						<a href="javascript:void(0);" class="export export_myTable">Export</a>
					</div>
					<div class="seller_reports_tableArea">
						<table id="myTable" class="myTable"></table>
					</div>
				</div>
				<div class="seller_reports_second">
					<div class="seller_reports_second_title">
						<span class="seller_reports_second_tit">Payment Summary</span> 
						<a href="javascript:void(0);" class="export export_myTotal">Export</a>
					</div>
					<div class="seller_reports_tableArea">
						<table id="myTotal" class="myTotal"></table>
					</div>
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
<script type="text/javascript" src="/commondefine_js/timezoneTime.js"></script>
<script>
$(document).ready(function(){
	
	var SGroup=[];
	var Storeinfo=[];
	var DataTableList = [];
	$("#reportRawOffset").val(hoursOffset);
	
    initSGroup();
	initCalendar();
	initToTalTable();
  	initAutoComplete();
  	initGetReportTableHead_BAN();
  	
  	function initGetReportTableHead_BAN(){
        $.ajax({  
            url : "/report/control/GetReportTableHead_BAN",  
            type : "post",  
            dataType : "json",  
            data : {}, 
            success: function(data) {  
                if(data.responseMessage=="success"){
                	var dataResult = data.tableHeadList;
                		DataTableList = data.tableHeadList;
                	var countryId = data.countryId;
                	var role = data.role;
                	var SDataTable = [];
                	$.each(dataResult, function(index, elem){
                		SDataTable.push({
                			"title" : elem
                		});
                	});
                	initTable(SDataTable);
                	$('#countryId').val(countryId);
                	$('#role').val(role);
                }  
            }
      	}); 
  	}
  	
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
  	
	$("#selectGroup").change(function () {  
		
	    $("#selectStore option:not(:first)").remove();
	    
	    var selectedList = $(this).children('option:selected').val();
	    var strt = '';
	    
	    if(Storeinfo.length > 0){
	    	$.each(Storeinfo, function(index, elem){
	    		if(elem.storeGroupID == selectedList){
		    		strt += "<option value='"+elem.storeID+"'>"+elem.storeName+"</option>";
	    		}
	    	});
	    	$("#selectStore").append(strt); 
	    }
	}); 
  	
  	function initAutoComplete(){
  	  $("#productIdInput").autocomplete({  
  	        max : 10,// 显示数量  
  	        autoFill : false,  
  	        scroll : true, // 当结果集大于默认高度时是否使用卷轴显� 
  	        highlight : true,  
  	        highlightItem: true,  
  	        scroll : true,  
  	        matchContains : true,  
  	        multiple :false,  
  	       	source: function( request, response ) {  
	           $.ajax({  
	                 url : "/report/control/AutoSearchProduct",  
	                 type : "post",  
	                 dataType : "json",  
	                 data : {
	                	 productId:$("#productIdInput").val().trim()
	                 },
	                   
	                success: function(data) {  
	                     if(data.responseMessage=="success"){
	                         response( $.map( data.productBeans, function( item ) {  
	                             return {  
	                               label: item.productInnerName+"["+item.productId+"]" ,  
	                               value: item.productId
	                             }  
	                       }));  
	                    }
	                }  
	           });  
	        },  
	        focus: function( event, ui ) {  
	              return false;  
	            },  
	        select: function( event, ui ) {  
	            $("#productIdInput").val( ui.item.value );  
	            return false;  
	        }
	     });  
  	}
  	
    //初始化店铺租
  	function initSGroup(){
  		jQuery.ajax({
  			dataType:'json',
  			type: 'post',
  			data:{
  				groupType : "STORE_GROUP"
  			},
  			url: '/report/control/SearchAvailableStoresAndSGroups_BANSALE',
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
  						
  						$("#selectGroup").append(storegroups_info_content);
  					}
  					
  					var productStoreGroupId = $("#selectGroup").children('option:selected').val(); 
  					if(store_info.length > 0){
  						$.each(store_info,function(index, elem){
  							if(elem.storeGroupID == productStoreGroupId){
  	    						store_info_content +=  "<option value='"+elem.storeID+"'>"+elem.storeName+"</option>";
  							}
  						});
  						
  						Storeinfo = store_info;
  						
  						$("#selectStore").append(store_info_content);
  					}			
  				}else if(data.responseMessage=="error"){
  					showErrorMessage(data.errorMessage);
  	 				$(".searchButton").hide();
  				}
  			}      
  		});
  	}


    //初始化上面表
  	function initTable(options){	
  		var tableData = {};
  		 $('#myTable').dataTable({
  			 "date" : tableData,
  			 "columns": options,
  		     "pagingType": "full_numbers",
  		     "bLengthChange": false,
  		     "aLengthMenu": [10],
  		     "autoWidth": true,
  		     "Info": true,
  		     "searching": false,
  		     "paging": true,
  	         "oLanguage": {
  	           "oPaginate": {
  	               "sFirst": "Home Page",
  	               "sPrevious": " Previous ",
  	               "sNext": " Next ",
  	               "sLast": " Last "
  	           },
  	           "sLengthMenu": "Display _MENU_ results",
  	           "sZeroRecords": "Sorry, nothing have been found",
  	           "sEmptyTable": "0 results ",
  	           "sLoadingRecords": "Loading Data...",
  	           "sInfo": "Total _TOTAL_ results",
  	           "sInfoEmpty": "Total 0 results"
  	         }
  		 }); 
  	}

  	//初始化下面表
  	function initToTalTable(){	
  		var totalData = {};
  		 $('#myTotal').dataTable({
  			 "date" : totalData,
  			 "columns": [
  	            { "title": "Date" },
  	            { "title": "Total Payment" },
  	            { "title": "Cash" },
  	            { "title": "Cheque"},
  	            { "title": "Bank Card"},
  	            { "title": "Mobile Money"},
  	            { "title": "Credit"},
  	            { "title": "Telegraphic Transfer"}
  		     ],
  		     "pagingType": "full_numbers",
  		     "bLengthChange": false,
  		     "aLengthMenu": [10],
  		     "autoWidth": true,
  		     "Info": true,
  		     "searching": false,
  		     "paging": true,
  	         "oLanguage": {
  	           "oPaginate": {
  	               "sFirst": "Home Page",
  	               "sPrevious": " Previous ",
  	               "sNext": " Next ",
  	               "sLast": " Last "
  	           },
  	           "sLengthMenu": "Display _MENU_ results",
  	           "sZeroRecords": "Sorry, nothing have been found",
  	           "sEmptyTable": "0 results ",
  	           "sLoadingRecords": "Loading Data...",
  	           "sInfo": "Total _TOTAL_ results",
  	           "sInfoEmpty": "Total 0 results"
  	         }
  		 }); 
  	}
  	
	$('.searchTableList').on('click', function(){
		var target=$(this);
		
		var beginDate = $('#beginDate').val();
		var endDate = $('#endDate').val();			
		if(formatDateToYMD(beginDate) > formatDateToYMD(endDate)){			
			showErrorMessage("Start date can't be greater than end date")			
			return false;
		}		
		searchTable(target);
	});
 	
  	function searchTable(target){
  		target&&$.showLoading(target);
  		jQuery.ajax({
  			dataType:'json',
  			type: 'post',
  		
  			data:{		
  				productId:$("#productIdInput").val(),
  				sGroupId:$("#selectGroup").val(),
  				storeId:$("#selectStore").val(),
  				beginDate:$("#beginDate").val(),
  				endDate:$("#endDate").val(),
  				rawOffset:hoursOffset,
  				paymentMethod : $('#paymentMethod').val()
  			},
  			url: '/report/control/SearchMarketByConditions_BAN',
  			success: function (data) {
  				target&&$.hideLoading(target);
  				if(data.responseMessage=='success'){
  					var mydata = data.tableBean;
  					var footerDate = data.footerBean;
  					var tableHeadList = data.tableHeadList;
  					
  					var tableData = getTableDate(mydata);
  					var totalDate = getTotalDate(footerDate);
  					$('#myTable').dataTable().fnClearTable();   //将数据清
  					$('#myTotal').dataTable().fnClearTable();   //将数据清
  					if(tableData.length != 0){
  						 $('#myTable').dataTable().fnAddData(tableData,true);  //数据必须是json对象或json对象数组  
  					}
  					
  					if(totalDate.length != 0){
  						 $('#myTotal').dataTable().fnAddData(totalDate,true);  //数据必须是json对象或json对象数组  
  					}
  		      　	}
  			}      
  		});

  	}

  	//获取table格式数据
  	function getTableDate(mydata){
  		
  		var tableData = [];
  		$.each(mydata,function(index,elem){
  		    var rowDate = [];
  		    if($('#countryId').val() == 'GHA'){
	  		    if($('#role').val() == "GHA_MANAGE"){
	  	  	  		   rowDate.push(elem.orderDate,elem.category,elem.productId,elem.description,elem.quantity,
	  	  		    		elem.currentPrice,elem.subTotal,elem.adjustment,elem.payment,
	  	  		    		elem.recievables,elem.salesChannel
	  	  		    );
  	  		    }else if($('#role').val() == "GHA_ADMIN"){
	  	  	  		   rowDate.push(elem.orderDate,elem.category,elem.productId,elem.description,elem.quantity,
  	  		    		elem.currentPrice,elem.defaultCostPrice,elem.subTotal,elem.totalDefaultCostPrice,elem.adjustment,elem.payment,
  	  		    		elem.recievables,elem.salesChannel
	  	  		    );
  	  		    }
  	  		    
  		    }else if($('#countryId').val() == 'UGA'){
  		    	if($('#role').val() == "UGA_MIN_MANAGE"){
	  	  	  		   rowDate.push(elem.orderDate,elem.category,elem.productId,elem.description,elem.quantity,
	  	  		    		elem.currentPrice,elem.subTotal,elem.adjustment,elem.payment,
	  	  		    		elem.recievables,elem.salesChannel
	  	  		    );
  	  		    }else if($('#role').val() == "UGA_MAX_MANAGE"){
  	  	  		    rowDate.push(elem.orderDate,elem.category,elem.productId,elem.description,elem.quantity,
  	  	  		    		elem.currentPrice,elem.retailCostPrice,elem.subTotal,elem.totalRetailCostPrice,elem.adjustment,elem.payment,
  	  	  		    		elem.recievables,elem.salesChannel
  	  	  		    );
  		    	}else if($('#role').val() == "UGA_SUPER_ADMIN"){
  	  	  		    rowDate.push(elem.orderDate,elem.category,elem.productId,elem.description,elem.quantity,
  	  	  		    		elem.currentPrice,elem.subTotal,elem.retailCostPrice,elem.specialCostPrice,
  	  	  		    		elem.egateeCostPrice,elem.defaultCostPrice,elem.adjustment,elem.payment,
  	  	  		    		elem.recievables,elem.salesChannel
  	  	  		    );
  		    	}
  		    }

		    $.each(SGroup,function(srcIndex, srcElem){
		    	if(srcElem.sGroupId == elem.groupId){
		    		rowDate.push(srcElem.sGroupName);
		    	}
		    })
		    $.each(Storeinfo,function(srcIndex, srcElem){
		    	if(srcElem.storeID == elem.storeId){
		    		rowDate.push(srcElem.storeName);
		    	}
		    })
  		    tableData.push(rowDate);
  		});
  		return tableData;
  	}

  	//初始化第二个table数据
  	function getTotalDate(footerDate){
  		var tableData2 = [];
  		$.each(footerDate,function(index,elem){
  		    var rowDate = [];
  		    rowDate.push(
  		    		elem.date, elem.todayCollection,elem.todayCash,elem.todayCheque,
  		    		elem.todayBrakCard,elem.todayMoneyBack,elem.todayCredit,elem.todayTransfer
  		    );
  		    tableData2.push(rowDate);
  		});
  		return tableData2;
  	}
  	//initCalendar
  	function initCalendar(){
  		var timeZones = $('#timeNow').val() - 0;
  		
  		$('.some_class').datetimepicker({
  	        language:'en',
  	        format: 'dd/mm/yyyy',//显示格式
  	        minView: "month",//设置只显示到月份
  	        initialDate: new Date(timeZones),//初始化当前日�
  	        autoclose: true,//选中自动关闭
  	        todayBtn: true, //显示今日按钮
  	        maxView:"year",
  	        startDate: new Date(2017,3,19),
  	        endDate: new Date(timeZones)
  		});
  		$("#beginDate").val(formatDate(new Date(timeZones)));
  		$("#endDate").val(formatDate(new Date(timeZones))) ;
  	}

  	function formatDate(date) {  
  	    var y = date.getFullYear();  
  	    var m = date.getMonth() + 1;  
  	    m = m < 10 ? '0' + m : m;  
  	    var d = date.getDate();  
  	    d = d < 10 ? ('0' + d) : d;
  	   	return d + '/' + m + '/' + y; 
  	}; 
  	function formatDateToYMD(date) { 
  		var arr=date.split("/");
  		var str="";
  		for(var i=arr.length;i>0;i--){
  			str+=arr[i-1]+"/";
  		}  	    
  	  	return str; 
  	};
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
    
 	$('.export_myTable').click(function(e) {
		$('#type').val('detail');
    	$("#search_form").submit();   
    });
 	
    $('.export_myTotal').click(function(e) {
    	$('#type').val('summary');
    	$("#search_form").submit();  
    });
	
});
</script>
<script src="/commondefine_js/DataTable/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/commondefine_js/jQuery/jquery-ui.js" charset="UTF-8"></script>