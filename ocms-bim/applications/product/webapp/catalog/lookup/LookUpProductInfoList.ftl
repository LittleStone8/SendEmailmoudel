<link href="/commondefine_css/Datetimepicker/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
<div id="product_list">
    <div class="product_list_content_wap">
        <div class="product_list_content">
            <div class="product_list_search">
                <div class="product_list_title">
                    <span>Basic Information Query</span>
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
                            <div class="condition_flag_first">
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
                            <div class="condition_flag_second">
                            	<span>EAN</span>
                                <input class="EAN" type="text" name="EAN" id="EAN" />
                            </div>
                        </div>
                        <div class="time_condition clearfix">
                           <div class="condition_flag_first">
	                            <span>Created Time</span>
	                            <div>
	                                <input type="text" class="some_class" value="" id="some_class_1" name="begintime"/>
	                            </div>
	                            <em>-</em>
	                            <div>
	                                <input type="text" class="some_class" value="" id="some_class_2" name="endtime"/>
	                            </div>
                            </div>
	                        <div class="condition_flag_second">
	                        	<span>Product status</span>
	                         	<div>
		                            <select class="is_active_select" name="isActive" id="select_4">
		                            	<option value="Y" selected>Enabled</option>
		                            	<option value="N">Disabled</option>
		                            </select>
	                            </div>
	                        </div> 
                        </div>
                        
                          
                            
                    </form>
                    <div class="search_list">
                        <a href="javascript:void(0);" class="search_btn" id="search_btn">Search</a>
                    </div>
                </div>
            </div>
            <div class="product_list_detail">
                <div class="product_list_detail_title">
                    <span>Product List</span>
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
    	
    	var selectdriver = $( "#productId" ).val().trim();
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
	$('.some_class').datetimepicker({
		format: 'dd/mm/yyyy hh:mm:ss',
	});
	
    $.ajax({
        type: 'post',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        url: '/catalog/control/ListGivenClassfication',
        data:JSON.stringify({							
			"categoryId": "DefaultRootCategory"
		}),
        success: function(data) {
        	if(data.success){
        		var categoryList = (data.data.categoryList).sort(function(a, b){
	    			if(a.sequenceNum == b.sequenceNum){
	    				return a.categoryId - b.categoryId;
	    			}else{
	    				return a.sequenceNum - b.sequenceNum;
	    			}
	    		});
        		var str = '';
        		categoryList.length > 0 && $.each(categoryList, function(index, elem){
        			str += "<option value='" + elem.categoryId + "'>" + elem.categoryName + "</option>";
        		});
        		$("#select_1").append(str);
        		
        	}else{
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
            url: '/catalog/control/ListGivenClassfication',
            data:JSON.stringify({
				"categoryId" : selectedList
			}),
            success: function(data) {
            	
            	if(data.success){
            		var categoryList = (data.data.categoryList).sort(function(a, b){
 		    			if(a.sequenceNum == b.sequenceNum){
 		    				return a.categoryId - b.categoryId;
 		    			}else{
 		    				return a.sequenceNum - b.sequenceNum;
 		    			}
    	    		});
            		var str = '';
            		categoryList.length > 0 && $.each(categoryList, function(index, elem){
            			str += "<option value='" + elem.categoryId + "'>" + elem.categoryName + "</option>";
            		});
            		$("#select_2").append(str);
            		
            	}else {
            		showErrorMessage(data.message);
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
            url: '/catalog/control/ListGivenClassfication',
            data:JSON.stringify({
				"categoryId" : selectedList
			}),
            success: function(data) {

            	if(data.success){
            		var categoryList = (data.data.categoryList).sort(function(a, b){
 		    			if(a.sequenceNum == b.sequenceNum){
 		    				return a.categoryId - b.categoryId;
 		    			}else{
 		    				return a.sequenceNum - b.sequenceNum;
 		    			}
    	    		});
            		var str = '';
            		categoryList.length > 0 && $.each(categoryList, function(index, elem){
            			str += "<option value='" + elem.categoryId + "'>" + elem.categoryName + "</option>";
            		});
            		$("#select_3").append(str);
            		
            	}else{
            		showErrorMessage(data.message);
            	}
            }
        });
    });

    $('.export').click(function(e) {
    	
    	document.getElementById("search_form").submit();   

    });

    
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
			var pageSize = 30;
			
			getData({
				currentPage : currentPage,
				pageSize : pageSize
			});
		}
	});
	
    function getData(options, target) {
    	
    	var productId = $("input[id='productId']").val();
        var productTypeId = $("#productTypeId").val();
        var internalName = ($("input[id='internalName']").val()).trim();
        var brandname = $("input[id='brandname']").val();
        var begintime = $("input[id='some_class_1']").val();
        var endtime = $("input[id='some_class_2']").val();
        var EAN = $("input[id='EAN']").val();
		var isActive = $("#select_4").children('option:selected').val(); 
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
                EAN:EAN,
                endtime: endtime,
                page:options.currentPage,
                isActive:isActive,
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
});
</script>
<script type="text/javascript" src="/commondefine_js/Datetimepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="/commondefine_js/jquery.table2excel.js"></script>