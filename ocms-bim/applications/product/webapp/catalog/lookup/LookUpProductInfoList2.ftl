<link href="/commondefine_css/Datetimepicker/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
<div id="product_list">
    <div class="product_list_content_wap">
        <div class="product_list_content">
            <div class="product_list_search">
                <div class="product_list_title">
                    <span>Basic information query</span>
                </div>
                <div class="product_list_condition">
                    <form class="search_form" id="search_form" action="/catalog/control/Exportproductlist">
                        <div class="base_condition">
                            <div class="product_flag">
                                <span>SKU</span>
                                <input class="product_flag_input" type="text" name="productId" id="productId" autocomplete="off" />
                                <ul class="productId_ul" style="display:none;" tabindex="11" ></ul>
                            </div>
                            <div class="product_flag">
                                <span>Product Type</span>
                                <input class="product_flag_input" type="text" name="productTypeId" id="productTypeId" readonly="true" value="FINISHED_GOOD" />
                            </div>
                            <div class="name_type">
								<span>Model</span>
                                <input class="name_type_input" type="text" name="name_type_input" id="internalName" />
                            </div>
                        </div>
                        <div class="type_condition">
                            <span>Category</span>
                            <select class="catalogues_first" name="productCategoryId" id="select_1">
                                <option value="">please select</option>
                            </select>
                            <select class="catalogues_second" name="productCategoryId1" id="select_2">
                            	<option value="">please select</option>
                            </select>
                            <select class="catalogues_third" name="productCategoryId2" id="select_3">
                            	<option value="">please select</option>
                            </select>
                        </div>
                        <div class="time_condition">
                            <span>Creation Time</span>
                            <div>
                                <input type="text" class="some_class" value="" id="some_class_1" name="begintime"/>
                            </div>
                            <em>-</em>
                            <div>
                                <input type="text" class="some_class" value="" id="some_class_2" name="endtime"/>
                            </div>
                        </div>
                    </form>
                    <div class="search_list">
                        <a href="javascript:void(0);" class="search_btn" id="search_btn">search</a>
                    </div>
                </div>
            </div>
            <div class="product_list_detail">
                <div class="product_list_detail_title">
                    <span>Product list</span>
                    <div class="excel_list">
                        <a href="javascript:void(0);" class="export">Export</a>
                    </div>
                </div>
                <div class="seller_reports_tableArea">
                    <table class="product_list_table" id="product_list_table">
                    	<thead>
                    		<tr>
                    			<th>No.</th>
                    			<th>SKU</th>
                    			<th>Model</th>
                    			<th>Description</th>
                    			<th>Category</th>
                    			<th>Brand</th>
                    			<th>Currency</th>
                    			<th>Cost</th>
                    			<th>Edit Cost</th>
                    			<th>Edit Info</th>
                    		</tr>
                    	</thead>
                    	<tbody>
                    		<tr>
                    			<td colspan="10">0 results</td>
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
        <div class="error_tips_title">Tip Message</div>
        <div class="error_tips_content">
            <span></span>
        </div>
        <div class="error_tips_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	var timeOut = null;
	var timer = 1000;
	
	function myAutoComplete(dataOption){
    	
    	var productId = $('#productId');
        var productId_ul = $( ".productId_ul" );
        
    	var dataList = dataOption;
    	var htmlContent = '';
    	
    	if(dataList.length == 0){
    		productId_ul.hide();
    		
    		return false;
    	}
    	
    	$.each(dataList,function(index, item){
    		htmlContent += '<li data-productId="'+ item.productId +'" data-description="'+ item.description +'">'+ item.productId + ':' +item.description +'</li>'
        	
    	});
    	
    	productId_ul.html(htmlContent).show();
    	
    	productId_ul.find('li').click(function(){
    		var _this = $(this);
    		var productIdval = _this.attr('data-productId');
    		productId.val(productIdval);
    		productId_ul.hide();
    	});
    }
	
	$(document).click(function(){
		var driver_ul = $('.productId_ul');
		
		driver_ul.hide();
	})
	
    //模糊查询----driver
    $( "#productId" ).keyup(function(event){
    	
    	event.preventDefault();
    	event.stopPropagation();
    	
    	var selectdriver = $( "#productId" ).val();
    	var driver_ul = $('.productId_ul');
    	
    	if(selectdriver == ''){
    		clearTimeout(timeOut);
    		driver_ul.hide();
    		
    		return false;
    	}
    	clearTimeout(timeOut);
    	timeOut = setTimeout(function(){
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: '/catalog/control/LenovoSKU',
                data : {
                	productId: selectdriver
                },
                success: function(data) {
                    if(data.responseMessage=="success"){
                    	myAutoComplete(data.resultPoductPriceList);
                    }
                }
            }) 
    	},timer) 
    });
	
	
	
	//错误信息
    function showErrorMessage(message, callBack){
        var errorTips = jQuery('.error_tips');
        errorTips.find('.error_tips_content span').html('').html(message);
        errorTips.show();

	    errorTips.find('.error_tips_operator').off('click','a').on('click','a',function () {
		    $('.error_tips').hide();
		    callBack && callBack();
	    })
    }
	
	//调用日历插件
	$('.some_class').datetimepicker();
	
    $.ajax({
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        url: '/catalog/control/ListGivenClassfication?categoryID=DefaultRootCategory',
        success: function(data) {
        	if(data.responseMessage == 'success'){
        		var categoryList = data.categoryList;
        		var str = '';
        		categoryList.length > 0 && $.each(categoryList, function(index, elem){
        			str += "<option value='" + elem.categoryID + "'>" + elem.categoryName + "</option>";
        		});
        		$("#select_1").append(str);
        		
        	}else if(data.responseMessage == 'error'){
        		showErrorMessage(data.errorMessage);
        	}
        }
    });

    $("#select_1").change(function() {
    	
        var selectedList = $(this).children('option:selected').val();
    	$("#select_2").children().not(':first-child').remove();
    	$("#select_3").children().not(':first-child').remove();
    	
        if(selectedList == ''){
        	return false;
        }
        
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            url: '/catalog/control/ListGivenClassfication?categoryID=' + selectedList,
            success: function(data) {
            	
            	if(data.responseMessage == 'success'){
            		var categoryList = data.categoryList;
            		var str = '';
            		categoryList.length > 0 && $.each(categoryList, function(index, elem){
            			str += "<option value='" + elem.categoryID + "'>" + elem.categoryName + "</option>";
            		});
            		$("#select_2").append(str);
            		
            	}else if(data.responseMessage == 'error'){
            		showErrorMessage(data.errorMessage);
            	}
            }
        });
    });

    $("#select_2").change(function() {
        var selectedList = $(this).children('option:selected').val();
        
        $("#select_3").children().not(':first-child').remove();
        if(selectedList == ''){
        	return false;
        }
        
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            url: '/catalog/control/ListGivenClassfication?categoryID=' + selectedList,
            success: function(data) {

            	if(data.responseMessage == 'success'){
            		var categoryList = data.categoryList;
            		var str = '';
            		categoryList.length > 0 && $.each(categoryList, function(index, elem){
            			str += "<option value='" + elem.categoryID + "'>" + elem.categoryName + "</option>";
            		});
            		$("#select_3").append(str);
            		
            	}else if(data.responseMessage == 'error'){
            		showErrorMessage(data.errorMessage);
            	}
            }
        });
    });

    $('.export').click(function(e) {
    	
    	document.getElementById("search_form").submit();   
    	
    	
    	
   /*  	var currentDateStr = getNowFormatDate();
    	
		$(".product_list_table").table2excel({
			exclude: ".noExl",
			name: "Product List",
			filename: "Product List " + currentDateStr,
			fileext: ".xls",
			exclude_img: true,
			exclude_links: true,
			exclude_inputs: true
		}); */
		
    });

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
        return currentdate;
    }
    
	//搜索
	$('.search_btn').on('click', function(e){
		var _this = $(this);
		var currentPage = 1;
		var pageSize = 30;
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		},e.currentTarget);
	});
	
	//点击页签
	$('.tableFooter').on('click', 'a.homePage', function(){
		var _this = $(this);
		var currentPage = _this.attr('data-id');
		var pageSize = 30;
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	}).on('click', 'a.previousPage', function(){
		var currentPage = parseInt($('.tableFooter').find('.currentPageList').attr('data-id'), 10) - 1;
		var pageSize = 30;
		
		if(currentPage < 1){
			
			showErrorMessage('No previous page');
			
			return false;
		}
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	}).on('click', 'a.nextPage', function(){
		var currentPage = parseInt($('.tableFooter').find('.currentPageList').attr('data-id'), 10) + 1;
		var pageSize = 30;
		var limitPage = parseInt($('.tableFooter').find('.endPage').attr('data-id'), 10);
		
		if(currentPage > limitPage){
			
			showErrorMessage('No next page');
			
			return false;
		}
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	}).on('click', 'a.endPage', function(){
		var _this = $(this);
		var currentPage = parseInt(_this.attr('data-id'),10);
		var pageSize = 30;
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	}).on('click', 'a.pageList', function(){
		var _this = $(this);
		var currentPage = parseInt(_this.attr('data-id'),10);
		var pageSize = 30;
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	}).on('click', 'a.confirm', function(){
		var _this = $(this);
		var currentPage = parseInt(_this.siblings('input').val(),10);
		
		var newExp = /^[1-9]\d*$/;
		if(!newExp.test(currentPage)){
			showErrorMessage('Please enter positive integer and greater than zero!');
			
			return false;
		}
		var pageSize = 30;
		
		getData({
			currentPage : currentPage,
			pageSize : pageSize
		});
	});
	
    function getData(options, target) {
    	
    	var productId = $("input[id='productId']").val();
        var productTypeId = $("#productTypeId").val();
        var internalName = $("input[id='internalName']").val();
        var brandname = $("input[id='brandname']").val();
        var begintime = $("input[id='some_class_1']").val();
        var endtime = $("input[id='some_class_2']").val();

        if (begintime > endtime) {
        	showErrorMessage('The start time must not be greater than the end time!')
            return false;
        }
        var productCategoryId1 = $("#select_1").children('option:selected').val();
        var productCategoryId2 = $("#select_2").children('option:selected').val();
        var productCategoryId = $("#select_3").children('option:selected').val();
        var productCategoryIdname = $("#select_3").children('option:selected').text();
        
        target && $.showLoading(target);
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '/catalog/control/LookupProductInfoAjax',
            data: {
                productId: productId,
                productTypeId: productTypeId,
                internalName: internalName,
                productCategoryId: productCategoryId,
                productCategoryId1: productCategoryId1,
                productCategoryId2: productCategoryId2,
                productCategoryIdname: productCategoryIdname,
                brandname: brandname,
                begintime: begintime,
                endtime: endtime,
                page:options.currentPage,
            },
            success: function(data) {
            	target && $.hideLoading(target);
            	if(data.responseMessage == 'success'){
            		
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
    	var firstPageInfo = '<div class="pageList"><a href="javascript:void(0);" class="homePage" data-id="1">Home page</a><a href="javascript:void(0);" class="previousPage">Previous page</a>';
    	var secondPageInfo = '<a href="javascript:void(0);" class="nextPage">Next page</a><a href="javascript:void(0);" class="endPage" data-id="'+ totalPage +'">End page</a>';
    	var changePageInfo = '<a href="javascript:void(0);">Jump to</a><input type="text" class="enterPage" /><a href="javascript:void(0);" class="confirm">confirm</a></div><div class="totalNumber">Results: '+ dataList.totalnumber +'</div>'
    	var pageListInfo = '';
    	var trInfo = '';
    	var tableFooter = $('.tableFooter');
    	var tableTbody = productListTable.find('tbody');
    	
    	if(options.currentPage > totalPage){
    		showErrorMessage('Please enter Legal page number');
    		
    		return false;
    	}
    	
    	for(var i = 0; i < dataList.resultPoductPriceList.length; i++){
    		trInfo += '<tr>'+ 
    				'<td>'+ (i + 1) +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].productId +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].internalName +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].description +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].productCategoryId +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].brandName +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].currencyUomId +'</td>'+
    				'<td>'+ dataList.resultPoductPriceList[i].cost +'</td>'+
    				'<td><a href="/catalog/control/FindProductPriceRulesTest?productId='+dataList.resultPoductPriceList[i].sPU+'" target="_black">Edit Cost</a></td>'+
    				'<td><a href="/catalog/control/BulkAddProductFeatures?productId='+dataList.resultPoductPriceList[i].sPU +'" target="_black">Edit Info</a></td>'+
    		'</tr>';
    	}
    	
    	tableTbody.html(trInfo);
    	
    	if(totalPage <= 6){
    		for(var i = 1; i <= totalPage; i++){
    			if(options.currentPage == i){
    				pageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
    			}else{
    				pageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
    			}
    		}
    		
    		var info = firstPageInfo + pageListInfo + secondPageInfo + changePageInfo;
    		
    		tableFooter.html(info);
    	}else{
    		var leftPageListInfo = '';
    		var rightPageListInfo = '';
    		var commonPageInfo = '';
    		
    		if(options.currentPage <= 3){
    			for(var i=1; i<=totalPage; i++){
        			if(i <= 3){
            			if(options.currentPage == i){
            				leftPageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
            			}else{
            				leftPageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
            			}
        			}
        			
        			if(i >= totalPage - 2){
        				rightPageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
        			}
        		}
        		var info = firstPageInfo + leftPageListInfo + '<a href="javascript:void(0);">...</a>' + rightPageListInfo + secondPageInfo + changePageInfo;

        		tableFooter.html(info);
    		}else if(options.currentPage >= totalPage - 2){
    			for(var i=1; i<=totalPage; i++){
        			if(i <= 3){
            			leftPageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
        			}
        			
        			if(i >= totalPage - 2){
            			if(options.currentPage == i){
            				rightPageListInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
            			}else{
            				rightPageListInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
            			}
        			}
        		}
        		var info = firstPageInfo + leftPageListInfo + '<a href="javascript:void(0);">...</a>' + rightPageListInfo + secondPageInfo + changePageInfo;

        		tableFooter.html(info);
    		}else{
    			for(var i=options.currentPage - 1; i< options.currentPage + 2; i++){
           			if(options.currentPage == i){
           				commonPageInfo += '<a href="javascript:void(0);" class="pageList currentPageList" data-id="'+ i +'">'+i+'</a>';
           			}else{
           				commonPageInfo += '<a href="javascript:void(0);" class="pageList" data-id="'+ i +'">'+i+'</a>';
           			}
    			}
        		var info = firstPageInfo + '<a href="javascript:void(0);" class="pageList" data-id="1">1</a><a href="javascript:void(0);">...</a>' + commonPageInfo + '<a href="javascript:void(0);">...</a><a href="javascript:void(0);" class="pageList" data-id="'+ totalPage +'">'+ totalPage +'</a>' + secondPageInfo + changePageInfo;

        		tableFooter.html(info);
    		}
    	}	
    }
});
</script>
<script type="text/javascript" src="/commondefine_js/Datetimepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="/commondefine_js/jquery.table2excel.js"></script>