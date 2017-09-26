<div id="product_document">
	<div class="product_document_content_wap">
		<div class="product_document_content">
			<div class="product_document_baseInfo">
				<div class="product_document_baseInfo_header">
					<div class="product_document_baseInfo_title">
						Add Basic Information
					</div>
					<div class="product_document_baseInfo_global">
						<a class="global_synchronize" href="javascript:void(0);">b2r_sync</a>
					</div>
				</div>
				<div class="product_document_baseInfo_list">
					<div class="product_flag">
						<span>Product ID :</span>
						<span class="product_flag_input" name="product_flag_input"></span>
					</div>
					<div class="product_type">
						<span>Product Type :</span>
						<span class="product_type_select" name="product_type_select" data-value="FINISHED_GOOD">FINISHED_GOOD</span>
					</div>
					<div class="name_type">
						<span>Model</span>
						<input class="name_type_input" type="text" name="name_type_input" />
					</div>
					<div class="brand_name">
						<span>Brand Name</span>
						<input class="brand_name_input" type="text" name="brand_name_flag" />
					</div>
				</div>
			</div>
			<div class="product_document_catalogues">
				<div class="product_document_catalogues_header">
					<div class="product_document_catalogues_title">
						Product Category
					</div>
				</div>
				<div class="product_document_catalogues_list">
					<div class="reset_type">
						<span>Reselect Category</span>
						<select class="catalogues_first" name="catalogues_first" >
						    <option value="">Please Select</option>
						</select>
						<select class="catalogues_second" name="catalogues_second" >
						    <option value="">Please Select</option>
						</select>
						<select class="catalogues_third" name="catalogues_third" >
						    <option value="">Please Select</option>
						</select>
					</div>
				</div>
			</div>
			<div class="product_document_add">
				<div class="product_document_add_header">
					<div class="product_document_add_title">
						Add Attributes
					</div>
				</div>
				<div class="add_attr">
					<table class="add_attr_table">
						<thead>
							<tr>
								<th>Attributes</th>
								<th>Attribute Values</th>
								
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div class="operator_list">
						<a class="add_attr_btn" href="javascript:void(0);">+Add Attributes</a>
						<a class="add_attr_submit" href="javascript:void(0);">Generate SKU</a>
					</div>
				</div>
				<div class="product_detail_list">
					<table class="detail_list_table">
						<thead>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="product_document_create">
			    <a href="javascript:void(0);" class="save_product">Save Info</a>
				<a href="javascript:void(0);" class="create_product">Edit Price</a>
			</div>
			
		</div>
	</div>
</div>
<div class="ean_delete_dialog">
    <div class="fixed_screen"></div>
    <div class="confirm_dialog_box">
        <div class="confirm_dialog_title"></div>
        <div class="confirm_dialog_content">
            <span class="feature_type">Do you want to delete the existed code and input a new one?</span>           
        </div>
        <div class="confirm_dialog_operator">
            <a href="javascript:void(0);" class="ean_delete_confirm">Confirm</a>
            <a href="javascript:void(0);" class="cancel">Cancel</a>
        </div>
    </div>
</div>
<div class="open_dialog">
    <div class="fixed_screen"></div>
    <div class="open_dialog_box">
        <div class="open_dialog_title"></div>
        <div class="open_dialog_content">
            <span>Attribute Name</span>
            <input type="text" class="create_attr" maxlength="20" />
        </div>
        <div class="open_dialog_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<div class="confirm_dialog">
    <div class="fixed_screen"></div>
    <div class="confirm_dialog_box">
        <div class="confirm_dialog_title"></div>
        <div class="confirm_dialog_content">
            <span class="feature_type">Are you sure to delete feature type <em></em>? This action cannot be recovered!</span>
            <span class="product_feature">Are you sure to delete product feature <em></em>? This action cannot be recovered!</span>
        </div>
        <div class="confirm_dialog_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
            <a href="javascript:void(0);" class="cancel">Cancel</a>
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

	$(document).ready(function(){		

	    //全局开发，控制属性是否可以被点击
	    if(getUrlParams('productId')){
	    	$(".product_flag_input").text(getUrlParams('productId'));
	        findAllCheckedAttr();
	    }else{
	        //初始化默认不让input选中
/* 	        getAllAttr(function(){
	            var allSelectedItem = jQuery('.add_attr_table').find('.attr_group.selected');
	            allSelectedItem.removeClass('selected');
	            allSelectedItem.find('.select-attr').removeClass('select-attr');
	            jQuery('.add_attr_table').find('td.selected').removeClass('selected');
	       }); */
	        
	        getFeatureList({
				categoryId : getUrlParams("thirdId"),
				parentCategoryId : getUrlParams("secondId")
	        } ,function(){
	            var allSelectedItem = jQuery('.add_attr_table').find('.attr_group.selected');
	            allSelectedItem.removeClass('selected');
	            allSelectedItem.find('.select-attr').removeClass('select-attr');
	            jQuery('.add_attr_table').find('td.selected').removeClass('selected');
	       });
	    }
    
    //初始化一级
    if(getUrlParams("productId")){
    	findProductAndCategory();
    	$('.product_document_create').show();
    }else{
        getFirstCatalogues(
           function(){
               selectedList({
                   firstId : getUrlParams("firstId"),
                   secondId : getUrlParams("secondId"),
                   thirdId : getUrlParams("thirdId")
               })
           }
        );
	}
    
    //catrgory change event
    $('.catalogues_first').on('change', function(){
		
    	if(!$(".product_flag_input").text()){
        	if($('.catalogues_first').val() == ''){
        		$('.add_attr_table tbody').html('');
        	}else{
            	getSecondCatalogues($('.catalogues_first').val(), function(){
            		getFeatureList({
    					categoryId : $('.catalogues_first').val(),
    					parentCategoryId : 'DefaultRootCategory'
    				});
                });
        	}
    	}    	
    });
    $('.catalogues_second').on('change', function(){
    	
    	if(!$(".product_flag_input").text()){
        	if($('.catalogues_second').val() == ''){
        		if($('.catalogues_first').val() == ''){
        			$('.add_attr_table tbody').html('');
        		}else{
        			getSecondCatalogues($('.catalogues_first').val(), function(){
                		getFeatureList({
        					categoryId : $('.catalogues_first').val(),
        					parentCategoryId : 'DefaultRootCategory'
        				});
                    });
        		}
        	}else{
            	getThirdCatalogues($('.catalogues_second').val(), function(){
            		getFeatureList({
    					categoryId : $('.catalogues_second').val(),
    					parentCategoryId : $('.catalogues_first').val()
    				});
                });
        	}
    	}
    });
    $('.catalogues_third').on('change', function(){
		var _this = $(this);
		if(!$(".product_flag_input").text()){
			if(_this.val() == ''){
				if($('.catalogues_second').val() == ''){
					$('.add_attr_table tbody').html('');
				}else{
					getThirdCatalogues($('.catalogues_second').val(), function(){
	            		getFeatureList({
	    					categoryId : $('.catalogues_second').val(),
	    					parentCategoryId : $('.catalogues_first').val()
	    				});
	                });
				}
			}else{
				getFeatureList({
					categoryId : _this.val(),
					parentCategoryId : $('.catalogues_second').val()
				});
			}
		}
    }); 
    //ean扫描
    var EANDialog=$(".ean_delete_dialog");
    var eanVal="";
    $("#product_document").on("focus",".ean_input",function(e){    	
    	var $that=$(this);
    	if($.trim($that.val())){
    		eanVal=$that;
    		EANDialog.show();	    	   	
	    }
    })    
    EANDialog.on("click",".ean_delete_confirm",function(){	    		
    	eanVal.val("");
    	eanVal.focus();
    	EANDialog.hide();	    
	});
    EANDialog.on("click",".cancel,.confirm_dialog_title",function(){
    	EANDialog.hide();	
	    var nextInput=eanVal.closest("tr").next().find(".ean_input");
		if(!nextInput.val()){
		    nextInput.focus();
		}
	});	 
    $("#product_document").on("keypress",".ean_input",function(e){
    	var $that=$(this);  	    
    	var key = e.which;
	    if (key == 13&&$.trim($(this).val())) {	
	    	var reg=new RegExp("(^([0-9]{8})$)|(^([0-9]{13})$)"); 
        	if(!reg.test($.trim($(this).val()))){
        		showErrorMessage($.trim($(this).val())+'  is not correct EAN code,Please enter correct code');
        		$(this).val("");
        		return;
        	}
	    	var nextInput=$that.closest("tr").next().find(".ean_input");
	    	if(nextInput.length>0&&!nextInput.val()){
	    		nextInput.focus();
	    	}else{	    		
	    		$that.blur();
	    	}
	    }
    })
	
	    //获取参数
	    function getUrlParams(name) {
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	        var r = window.location.search.substr(1).match(reg);
	        if (r != null) return unescape(r[2]);
	        return null;
	    }
	    //分类选中
	    function selectedList(options){
	        if(options.firstId && options.secondId && options.thirdId){
	            var firstId = options.firstId;
	            var secondId = options.secondId;
	            var thirdId = options.thirdId;
	
	            jQuery(".catalogues_first").find('option[value="'+ firstId +'"]').prop("selected","selected");
	            getSecondCatalogues(firstId, function(){
	                jQuery(".catalogues_second").find('option[value="'+ secondId +'"]').prop("selected","selected");
	                getThirdCatalogues(secondId, function(){
	                    jQuery(".catalogues_third").find('option[value="'+ thirdId +'"]').prop("selected","selected");
	                });
	            });
	        }
	    }
	    
	    function getFeatureList(options, callBack){
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/listCategoryFeatureTree',
	            data: JSON.stringify(options),
	            success: function (res) {
	                var dataResult = res.data.reverse();
	                if(res.success){
	                	var trContent = '';
	                	var dataList = [];
	                	
	                	if(dataResult[0] && dataResult[0].featureTypeAndFeatureVO){
	                		dataList = dataList.concat(dataResult[0].featureTypeAndFeatureVO);
	                	}
	                	if(dataResult[1] && dataResult[1].featureTypeAndFeatureVO){
	                		dataList = dataList.concat(dataResult[1].featureTypeAndFeatureVO);
	                	}
	                	if(dataResult[2] && dataResult[2].featureTypeAndFeatureVO){
	                		dataList = dataList.concat(dataResult[2].featureTypeAndFeatureVO);
	                	}
	                	
	                	if(dataList && dataList.length){
	                		$.each(dataList, function(index, elem){
	                			if(elem.featureVOList && elem.featureVOList.length){
		                            var attrValueList = '';
		                            $.each(elem.featureVOList, function(indexSrc, elemSrc){
		                                attrValueList+= '<div class="attr_group"  data-parentId="'+elem.productFeatureTypeId+'" data-id="'+elemSrc.featureID+'">'+
		                                				'<a href="javascript:void(0)" class="common-attr">' + elemSrc.featureName + '</a><div class="operator_list"><span class="first_span"></span><span class="second_span"></span></div></div>';
		                            });
		                            
		                            trContent+= '<tr>' +
		                            	'<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a" data-id="'+ elem.productFeatureTypeId +'">'+ elem.productFeatureName +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
		                            	'<td>'+
		                            		attrValueList +
		                            		'<span class="add_attr_value">+Add</span>'+
		                            	'</td>'+
		                            '</tr>';
	                			}else{
	                				trContent+= '<tr>' +
		                            	'<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a" data-id="'+ elem.productFeatureTypeId +'">'+ elem.productFeatureName +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
		                            	'<td>'+
		                            		'<span class="add_attr_value">+Add</span>'+
		                            	'</td>'+
		                            '</tr>';
	                			}
	                		});
	                		jQuery('.add_attr_table tbody').html(trContent);
	                	}else{
	                		jQuery('.add_attr_table tbody').html('');
	                	}
	
	                    callBack && callBack();
	                }else{
	                	showErrorMessage(res.message);
	                }
	            }
	        });
	    }
	    
	    //初始化获取属性跟属性
	    function getAllAttr(callBack){
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/findAllProductFeatureTypeAndProduceFeaturAppl',
	            data: {},
	            success: function (res) {
	                var dataResult = res.data;
	                if(res.success){
	                    var trContent = '';
	
	                    jQuery.each(dataResult.productFeatureMap,function(key,value){
	                        var attrType = value;
	                        if(attrType.productFeatureList){
	                            var attrValueList = '';
	                            jQuery.each(attrType.productFeatureList, function(index, elem){
	                                attrValueList+= '<div class="attr_group"  data-parentId="'+elem.productFeatureTypeId+'" data-id="'+elem.productFeatureId+'">'+
	                                '<a href="javascript:void(0)" class="common-attr">' + elem.description + '</a><div class="operator_list"><span class="first_span"></span><span class="second_span"></span></div></div>';
	                            });
	                            trContent+= '<tr>' +
	                            '<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a" data-id="'+ attrType.productFeatureTypeId +'">'+ attrType.description +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
	                            '<td>'+
	                            attrValueList +
	                            '<span class="add_attr_value">+Add</span>'+
	                            '</td>'+
	                            '</tr>';
	                        }else{
	                            trContent+= '<tr>' +
	                            '<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a" data-id="'+ attrType.productFeatureTypeId +'">'+ attrType.description +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
	                            '<td>'+
	                            '<span class="add_attr_value">+Add</span>'+
	                            '</td>'+
	                            '</tr>';
	                        }
	                    });
	
	                    jQuery('.add_attr_table tbody').html('').append(trContent);
	
	                    callBack && callBack();
	                }else {
	                    showErrorMessage(res.message);
	                }
	            }
	        });
	    }
	    //错误信息
	    function showErrorMessage(message){
	
	        var errorTips = jQuery('.error_tips');
	        errorTips.find('.error_tips_content span').html('').html(message);
	        errorTips.show();
	    }
		//三级联动
		function getFirstCatalogues(callBack){
			var cataloguesFirst = jQuery(".catalogues_first");
			var responseData,
				optionContent='';;
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : 'DefaultRootCategory'
				}),
				
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList) && (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		jQuery.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</option>';
			    		});
			    		cataloguesFirst.children().not(':first-child').remove();
			    		cataloguesFirst.append(optionContent);
			    		callBack && callBack();
			    	}else {
			    	    showErrorMessage(res.message);
			    	}
			    }
			});
		}
	
		function getSecondCatalogues(categoryId, callBack){
			var cataloguesSecond = jQuery(".catalogues_second");
	        if(!categoryId){
	            cataloguesSecond.children().not(':first-child').remove();
	
	            return false;
	        }
	
			var responseData,
				optionContent='';;
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : categoryId
				}),
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList) && (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		jQuery.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</a></li>';
			    		});
			    		cataloguesSecond.children().not(':first-child').remove();
			    		cataloguesSecond.append(optionContent);
	
			    		callBack && callBack();
			    	}else {
	
			    	    showErrorMessage(res.message);
			    	}
			    }
			});
		}
	
		function getThirdCatalogues(categoryId, callBack){
	
			var cataloguesThird = jQuery(".catalogues_third");
	        if(!categoryId){
	            cataloguesThird.children().not(':first-child').remove();
	
	            return false;
	        }
			var responseData,
				optionContent='';
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : categoryId
				}),
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList) && (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		jQuery.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</option>';
			    		});
			    		cataloguesThird.children().not(':first-child').remove();
			    		cataloguesThird.append(optionContent);
	
			    		callBack && callBack();
			    	}else{
			    	    showErrorMessage(res.message);
			    	}
			    }
			});
		}
	
	    function findProductAndCategory(){
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/findProductAndCategory',
	            contentType : 'application/json;charset=utf-8',
	            data:JSON.stringify({
					 productId:getUrlParams("productId"),
				}),
	            success: function (data) {
	                var dataResult = data.data;
	                var name_type_input = $('.name_type_input');
	                var brand_name_input = $('.brand_name_input');
	                if(data.success){
	                    name_type_input.val(dataResult.internalName);
	                    brand_name_input.val(dataResult.brandName);
                    	getFirstCatalogues(
                       		function(){
	                            selectedList({
	                                firstId : dataResult.firstId,
	                                secondId : dataResult.secondId,
	                                thirdId : dataResult.thirdId
	                            })
                        	}
                    	);
	                    findAllProductFeature(getUrlParams("productId"))
	                }else{
	                    showErrorMessage(data.message);
	                }
	            }
	        });
	    }
	
	    function findAllCheckedAttr(){
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            contentType : 'application/json;charset=utf-8',
	            url: '/catalog/control/findTableHead',
	            data:JSON.stringify({
					 productId :$(".product_flag_input").text(),
				}),
	            success: function (res) {
	                var dataResult = res.data;
	                if(res.success){
	                    $('.add_attr_btn').remove();
	
	                    var trContent = '';
	
	                    jQuery.each(dataResult.resultMap,function(key,value){
	                        var attrType = value;
	                        if(attrType.productFeatureList){
	                            var attrValueList = '';
	                            jQuery.each(attrType.productFeatureList, function(index, elem){
	                                attrValueList+= '<div class="attr_group" data-parentId="'+elem.productFeatureTypeId+'" data-id="'+elem.productFeatureId+'" >'+
	                                '<a href="javascript:void(0)"  class="common-attr">' + elem.description + '</a><div class="operator_list"><span class="first_span"></span><span class="second_span"></span></div></div>';
	                            });
	                            trContent+= '<tr>' +
	                            '<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a common-attr" data-id="'+ attrType.productFeatureTypeId +'">'+ attrType.description +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div></span><i class="pointer"></i></div></td>' +
	                            '<td>'+
	                            attrValueList +
	                            '<span class="add_attr_value">+Add</span>'+
	                            '</td>'+
	                            '</tr>';
	                        }else{
	                            trContent+= '<tr>' +
	                            '<td class="attr_tit_group"><div><a href="javascript:void(0)" class="attr_type_a common-attr" data-id="'+ attrType.productFeatureTypeId +'">'+ attrType.description +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
	                            '<td>'+
	                            '<span class="add_attr_value">+Add</span>'+
	                            '</td>'+
	                            '</tr>';
	                        }
	                    });
	                    
	                    jQuery('.add_attr_table tbody').html('').append(trContent);
	                    
	                }else {
	                    showErrorMessage(res.message);
	                }
	            }
	        });
	    }
	    
	    function findAllProductFeature(){
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            contentType : 'application/json;charset=utf-8',
	            url: '/catalog/control/findAllProductFeature',
	            data:JSON.stringify({
					 productId : jQuery('.product_flag_input').text(),
				}),
	            success: function (res) {
	                var dataResult = res.data;
	                if(res.success){
	                    var headContent = '',bodyContent='';
	                    var titles =[];
	
	                    jQuery.each(dataResult.priceProductFeatureMap, function(key, value){
	                        var keyValue = value.productFeatureMap;
	                        jQuery.each(keyValue, function(Fkey, Fvalue){
	                            if(titles.indexOf(Fkey) == -1){
	                                titles.push(Fkey);
	                            }
	                        })
	                    })	                   
	                    jQuery.each(dataResult.priceProductFeatureMap,function(key, value){
	                        var dataKey = value;
	                        bodyContent+='<tr>';
	                        var keyArr = [],valueArr=dataKey.productFeatureMap;
	
	                        jQuery.each(dataKey.productFeatureMap,function(Fkey, Fvalue){
	                            keyArr.push(Fkey);
	                        })
	                        for(var i = 0 ;i< titles.length; i++){
	                            if(keyArr.indexOf(titles[i]) > -1){
	                                bodyContent+= '<td>'+ valueArr[titles[i]] +'</td>';
	                            }else{
	                                bodyContent+= '<td></td>';
	                            }
	                        }
	                        bodyContent+= '<td>'+ dataKey.productIdSku +'</td>';
	                        bodyContent+= '<td><input type="text" class="ean_input" preValue="'+dataKey.EAN+'" value="'+dataKey.EAN+'" data-sku="'+dataKey.productIdSku+'"/></td>';
	                        if(dataKey.sync){
	                        	bodyContent+= '<td><a href="javascript:void(0);"  class="sync_success"></a></td>';
	                        }else{
	                        	bodyContent+= '<td><a href="javascript:void(0);"  class="sync_fail"></a></td>';
	                        }	                        
		                    bodyContent+= '<td>';
		                    if(dataKey.isActive=='Y'){
	                        	bodyContent+='<label class="sku_switch" data-type="Y" data-sku="'+dataKey.productIdSku+'"><span class="switch_core switch_no"><span class="switch_button"></span></span><div class="switch_left"><span>Enabled</span></div><div class="switch_right" style="display:none;"><span>Disabled</span></div></label>';
		                    }else{
		                    	bodyContent+='<label class="sku_switch" data-type="N" data-sku="'+dataKey.productIdSku+'"><span class="switch_core switch_off"><span class="switch_button"></span></span><div class="switch_left" style="display:none;"><span>Enabled</span></div><div class="switch_right"><span>Disabled</span></div></label>';
		                    }
	                        '</td>';
	                        bodyContent+='</tr>'
	                    })
	
	                    for(var i = 0; i<titles.length; i++){
	                        headContent+= '<th>'+ titles[i] +'</th>';
	                    }
	                    headContent+='<th>Product Id(SKU)</th><th>EAN</th><th style="width:20px;">Sync</th><th>Operation</th>';
	
	                    $('.detail_list_table thead').html('').append('<tr>'+ headContent +'</tr>');
	                    $('.detail_list_table tbody').html('').append(bodyContent);
	                    skuOperation();
	                }else {
	                     var errorMessage = res.message;
			    	    showErrorMessage(errorMessage);
	                }
	            }
	        });
	    }
	    function skuOperation(){
	    	$(".product_detail_list").on("click",".sku_switch",function(){
	    		var $that=$(this);
	    		var productIdSku=$that.attr("data-sku");
	    		var type=$that.attr("data-type"); 
	    		if(type=="Y"){
	    			type="N";
	    		}else{
	    			type="Y";
	    		}
	    		
            	jQuery.ajax({
    	            type: 'post',
    	            dataType: 'json',
    	            contentType : 'application/json;charset=utf-8',
    	            url: '/catalog/control/updateProductIsActive',
    	            data:JSON.stringify({
					 	productId:productIdSku,
    	            	isActive:type
					}),
    	            success: function (data) {
    	                var dataResult = data;
    	                console.dir(data);
    	                if(dataResult.success){
    	                	$that.attr("data-type",type);   	                	
    	                	if(dataResult.data.isActive=="Y"){
    	                		showErrorMessage("Enabled");
    	                		$that.find(".switch_core").removeClass("switch_off").addClass("switch_no");           		
    	                		$that.find(".switch_right").hide();
    	                		$that.find(".switch_left").show();   	                		
    	                	}else{
    	                		showErrorMessage("Disable");
    	                		$that.find(".switch_core").removeClass("switch_no").addClass("switch_off");
    	                		$that.find(".switch_left").hide();
    	                		$that.find(".switch_right").show();
    	                	}
    	                }else{
    	                	showErrorMessage(dataResult.message);
    	                }    	                
    	            }
    	        });
            })
	    }
	
	    function deleteSKU(that,options){
	
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/deleteSKU',
	            data: {
	                productSKUId:options,
	            },
	            success: function (data) {
	                var dataResult = data;
	                if(dataResult.responseMessage == 'success'){
	                    jQuery(that).closest('tr').remove();
	                }else if(dataResult.responseMessage == 'error'){
	                    showErrorMessage(dataResult.errorMessage);
	                }
	            }
	        });
	    }
	    
	    function deleteProductFeatureType(that,options){
	    	var confirmDialog = $('.confirm_dialog');
	    	confirmDialog.show();
	    	
	    	if(options.parentId){
	    		confirmDialog.find('.product_feature em').text(options.text);
	    		confirmDialog.find('.feature_type').css('display', 'none');
	    		confirmDialog.find('.product_feature').css('display', 'block');
	    	}else{
	    		confirmDialog.find('.feature_type em').text(options.text);
	    		confirmDialog.find('.product_feature').css('display', 'none');
	    		confirmDialog.find('.feature_type').css('display', 'block');
	    	}
	    	confirmDialog.off('click','.confirm_dialog_title').on('click','.confirm_dialog_title',function(){
	    		confirmDialog.hide();
	        });
	    	confirmDialog.off('click','.cancel').on('click','.cancel',function(){
	    		confirmDialog.hide();
	        });
	    	confirmDialog.off('click','.confirm').on('click','.confirm',function(e){
	    		$.showLoading(e.currentTarget);
	    		
		    	if(options.parentId){
			        jQuery.ajax({
			            type: 'post',
			            dataType: 'json',
			            contentType : 'application/json;charset=utf-8',
			            url: '/catalog/control/deleteProduceFeature',
			            data:JSON.stringify({
			            	productFeatureId:options.id
						}),
			            success: function (res) {
			                var dataResult = res.data;
			                $.hideLoading(e.currentTarget);
			                
			                if(res.success){
			                	var selectAttrLen = jQuery(that).closest('td').find('.select-attr').length;
			                	if(selectAttrLen == 1){
			                		if(jQuery(that).closest('.operator_list').siblings('a').hasClass('select-attr')){
			                			jQuery(that).closest('td').siblings('td').removeClass('selected');
			                		}
			                	}
			                	jQuery(that).closest('.attr_group').remove();
			                	
			                	confirmDialog.hide();
			                }else {
			                    showErrorMessage(res.message);
			                    confirmDialog.hide();
			                }
			            },
			            error:function(){
			            	$.hideLoading(e.currentTarget);
			            }
			        });
		    	}else{
			        jQuery.ajax({
			            type: 'post',
			            dataType: 'json',
			            contentType : 'application/json;charset=utf-8',
			            url: '/catalog/control/deleteProductFeatureType',
			            data:JSON.stringify({
							 productFeatureTypeId:options.id
						}),
			            success: function (res) {
			                var dataResult = res.data;
			                $.hideLoading(e.currentTarget);
			                
			                if(res.success){
			                    jQuery(that).closest('tr').remove();
			                    
			                    confirmDialog.hide();
			                }else {
			                    showErrorMessage(res.message);
			                    confirmDialog.hide();
			                }
			            },
			            error:function(){
			            	$.hideLoading(e.currentTarget);
			            }
			        });
		    	}
	        });
	    }
	
	    //关闭错误弹框
	    jQuery('.error_tips .error_tips_operator').on('click', 'a', function(){
	        jQuery('.error_tips').hide();
	    })
	
	    //创建产品
	    jQuery(".create_product").on('click', function(){
	        window.location.href = '/catalog/control/FindProductPriceRulesTest?productId='+ jQuery('.product_flag_input').text();
	    });
	
		jQuery(".catalogues_first").on('change', function(){
			var cataloguesFirstValue = jQuery(this).val();
	
	        jQuery(".catalogues_second").children().not(':first-child').remove();
	        jQuery(".catalogues_third").children().not(':first-child').remove();
			getSecondCatalogues(cataloguesFirstValue);
		})
	
		jQuery(".catalogues_second").on('change', function(){
			var cataloguesSecondValue = jQuery(this).val();
	
	        jQuery(".catalogues_third").children().not(':first-child').remove();
			getThirdCatalogues(cataloguesSecondValue);
		})
	
		//添加
		jQuery('.add_attr_btn').on('click', function(){
			var attrTr = jQuery('.add_attr_table');
			var trlist = attrTr.find('tbody tr:last-child');
			var firstValue = $('.catalogues_first').val();
			var secondValue = $('.catalogues_second').val();
			var thirdValue = $('.catalogues_third').val();
			var categoryId = thirdValue == '' ? (secondValue == '' ? (firstValue == '' ? '' : firstValue) : secondValue) : thirdValue;
			
			if(categoryId == ''){
				showErrorMessage('Cannot add attributes without a given category, please select categroy first.');
				
				return false;
			}
			//弹出
			jQuery('.create_attr').val('');
			jQuery('.open_dialog').show();
	
			jQuery('.open_dialog').off('click', '.open_dialog_title').on('click', '.open_dialog_title',function(){
				jQuery('.open_dialog').hide();
			});
	
			jQuery('.open_dialog').off('click', '.confirm').on('click', '.confirm',function(e){
				var createAttr = jQuery(".create_attr").val();
	            if(createAttr == ''){
	               jQuery('.open_dialog').hide();
	               showErrorMessage('Attribute can not be empty');
	
	               return false;
	            }

	            $.showLoading(e.currentTarget);
				jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    contentType : 'application/json;charset=utf-8',
				    url: '/catalog/control/newCreateProductFeatureType',
				    data:JSON.stringify({
						"description" : createAttr,
						"categoryId" : categoryId
					}),
				    success: function(res){
				    	$.hideLoading(e.currentTarget);
	                    if(res.success){
	                        var currentName = res.data;
	                        var html =  '<tr>' +
	                                        '<td><div class="attr_tit_group"><a href="javascript:void(0)" class="attr_type_a" data-id="'+ currentName.productFeatureTypeId +'">'+ currentName.description +'</a><div class="attr_operator_list"><span class="attr_first_span"></span><span class="attr_second_span"></span></div><i class="pointer"></i></div></td>' +
	                                        '<td>'+
	                                            '<span class="add_attr_value">+Add</span>'+
	                                        '</td>'+
	                                    '</tr>';
	                        trlist.after(html);
	                        jQuery('.open_dialog').hide();
	                    }else {
	                        showErrorMessage(res.message);
	                    }
				    },
				    error : function(option, status){
				    	$.hideLoading(e.currentTarget);
				    }
				});
			});
		});
	
	    jQuery('.detail_list_table').on('click','.attr_delete_btn',function(){
	        var _this = this;
	        var data = jQuery(_this).attr('data-id');
	        deleteSKU(_this,data);
	    });
		
		//save product info
		jQuery('.save_product').on('click',function(e){
	
		    var productTypeSelect = $('.product_type_select').attr('data-value');
		    var nameTypeInput = $('.name_type_input').val();
		    var brandNameInput = $('.brand_name_input').val();
		    var cataloguesFirst = $('.catalogues_first').val();
		    var cataloguesSecond = $('.catalogues_second').val();
		    var cataloguesThird = $('.catalogues_third').val();
		    
		    var datas={productList:[],type:"EAN"};
		    $(".ean_input").each(function(){
		    	var productId=$(this).attr("data-sku");
		    	var EAN=$(this).val();
		    	var preEan=$(this).attr("preValue");
		    	if(EAN!=preEan){
		    		datas.productList.push({"product":productId,"type":"EAN","value":EAN});
		    	}	
		    })		
		    
	        if(productTypeSelect == '' || nameTypeInput=='' || brandNameInput=='' ){
	            showErrorMessage('Please fill in the basic information of the product!');
	
	            return false;
	        }
	
	        if(cataloguesFirst =='' || cataloguesSecond=='' || cataloguesThird  == ''){
	            showErrorMessage('Please select catalogues');
	
	            return false;
	        }
	        $.showLoading(e.currentTarget);
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/createProductAndGotoPrice',
				data:JSON.stringify({
					productId :jQuery('.product_flag_input').text(),
	            	productTypeId :jQuery('.product_type_input').val(),
	            	internalName :jQuery('.name_type_input').val(),
	                brandName :jQuery('.brand_name_input').val(),
	            	productCategoryId : jQuery('.catalogues_third').val()
				}),
			    success: function(data){
			    	$.hideLoading(e.currentTarget);
			        var dataResult = data;
			        if(dataResult.success){	   
					    if(datas.productList.length==0){
					    	showErrorMessage('Save success.');
				            return false;
					    }
					    updateEanInfo(datas);
	                   
			        }
			    },
			    error : function(option, status){
			    	$.hideLoading(e.currentTarget);
			    }
			});
		});
		function updateEanInfo(datas){
			 jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    contentType : 'application/json;charset=utf-8',
				    url: '/catalog/control/updateGoodIdentificationinfo',
					data:JSON.stringify(datas),
				    success: function(data){
				        if(data.success==true){
				        	showErrorMessage('Save success.');
				        }else{
				        	showErrorMessage('Save error.'+data.message);
				        }
				    	
				    }						   
				});
		}
		/**
	     * global synchronize
	     */
	     jQuery('.global_synchronize').on('click',function(e){
	    	 var productId=$(".product_flag_input").text();
	    	 $.showLoading(e.currentTarget);
	    	 jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    contentType: "application/json;charset=utf-8",
				    url: '/catalog/control/syncProductApi',
				    data: JSON.stringify({
				    	productId:productId
					}),
				    success: function(res){
				    	$.hideLoading(e.currentTarget); 
				    	if(res.errCode==0){
				    		showErrorMessage('Global synchronize success!');
				    		findAllProductFeature();
				    	}else{
				    		showErrorMessage('Global synchronize fail!');
				    	}
				    },
				    error : function(option, status){
				    	$.hideLoading(e.currentTarget);
				    }
				}); 
	     })
	
	    /**
	     * generate SKU button function event
	     */
		jQuery('.add_attr_submit').on('click',function(e){
	        var productTypeSelect = $('.product_type_select').attr('data-value');
	        var nameTypeInput = $('.name_type_input').val();
	        var brandNameInput = $('.brand_name_input').val();
	        if(productTypeSelect == '' || nameTypeInput=='' || brandNameInput=='' ){
	            showErrorMessage('Please fill in the basic information of the product!');
	            return false;
	        }
	
	        var cataloguesFirst = $('.catalogues_first').val();
	        var cataloguesSecond = $('.catalogues_second').val();
	        var cataloguesThird = $('.catalogues_third').val();
	        if(cataloguesFirst =='' || cataloguesSecond=='' || cataloguesThird  == ''){
	            showErrorMessage('Please select catalogues');
	            return false;
	        }
	
	        var productFlagInput = $('.product_flag_input').text();
	        var productFeatureIds = [];
	        var allSelectAttrTrEle = jQuery('.add_attr_table tbody tr td.selected').closest('tr');
	
	        //如果当存在指定产品productid时，需要严格限制每项必须至少有一个选中�	        //说明：在未生成productid时允许allSelectAttrTrEle为空值，默认生成一个默认SKU
	        var isOk = true;
	        if(getUrlParams("productId") || productFlagInput){
	            var allAttrTrEle = jQuery('.add_attr_table tr a.common-attr').closest('tr');
	            if(allSelectAttrTrEle.length <1 || allSelectAttrTrEle.length !== allAttrTrEle.length) {
	                isOk = false;
	            }
	            //当前td存在selected元素，说明必定存在选中属性和属性值，否则必须选中属性�	            
	            if(!isOk){
	                showErrorMessage('The attribute corresponding to the attribute value can not be empty!');
	                return false;
	            }
	        }
	
			allSelectAttrTrEle.length > 0 && jQuery.each(allSelectAttrTrEle, function(index, elem){
			    var attrValueArr = [],attr={};
	            var $ele = jQuery(elem);
	
	            var attrName = $ele.find('td:first-child a').attr('data-id');
	            var allSelectedAttrValue =  $ele.find('.attr_group.selected');
	            allSelectedAttrValue.each(function(attrIdx, attrV){
	                attrValueArr.push(jQuery(attrV).attr('data-id'));
	            });
	            attr[attrName] = attrValueArr;
	            productFeatureIds.push(attr);
			});
			$.showLoading(e.currentTarget);
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType: "application/json;charset=utf-8",
			    url: '/catalog/control/anotherAddVariantProduct',
			    data: JSON.stringify({
			        productList : [{
	                    productId :jQuery('.product_flag_input').text(),
	                    productTypeId :jQuery('.product_type_input').val(),
	                    internalName :jQuery('.name_type_input').val(),
	                    brandName :jQuery('.brand_name_input').val(),
	                    productCategoryId : jQuery('.catalogues_third').val()
			        }],
					productFeature :productFeatureIds
				}),
			    success: function(res){
			    	$.hideLoading(e.currentTarget);
			        var dataResult = res.data;
			        if(res.success){
			            $(".product_flag_input").text(dataResult.productId);
			            findAllProductFeature();
			            findAllCheckedAttr();
			            if(dataResult.productId){
			            	$('.product_document_create').show();
			            }
			        }else{
			            showErrorMessage(res.message);
			        }
			    },
			    error : function(option, status){
			    	$.hideLoading(e.currentTarget);
			    }
			});
		});
	
	    jQuery('.add_attr_table').on('click', '.common-attr', function(e) {
	        var target = jQuery(e.currentTarget),
	                p = target.parent(),
	                ptd = target.parents('td'),
	                ptr = target.parents('tr');
	        if(target.hasClass('select-attr')){
	            target.removeClass('select-attr').parent().removeClass('selected');
	        }else{
	            target.addClass('select-attr').parent().addClass('selected');
	        }
	
	        //计算当前属性是否选中至少一个值，如果需要选中页头
	        var allChkAttr = ptd.find('.attr_group.selected');
	        var attrheader = ptr.find('.attr_type_a').parent().parent('td');
	        if(allChkAttr.length >0) {
	            if(!attrheader.hasClass('selected')){
	                attrheader.addClass('selected');
	            }
	        }else{
	            if(attrheader.hasClass('selected')){
	                attrheader.removeClass('selected');
	            }
	        }
	    });
	    jQuery('.add_attr_table').on('click','.add_attr_value', function(){
	        var _this = this;
	        var trlist = jQuery(_this).closest('td');
	
	        jQuery('.create_attr').val('');
	        jQuery('.open_dialog').show();
	
	        jQuery('.open_dialog').off('click','.open_dialog_title').on('click','.open_dialog_title',function(){
	            jQuery('.open_dialog').hide();
	        });
	
	        jQuery('.open_dialog').off('click','.confirm').on('click','.confirm',function(e){
	            var createAttrValue = jQuery(".create_attr").val();
	            var parentId = jQuery(_this).parent().siblings('td').find('a').attr('data-id');
	            if(createAttrValue == ''){
	                jQuery('.open_dialog').hide();
	                showErrorMessage('The attribute value can not be empty');
	
	                return false;
	            }
	            $.showLoading(e.currentTarget);
	            jQuery.ajax({
	                type: 'post',
	                dataType: 'json',
	                contentType : 'application/json;charset=utf-8',
	                url: '/catalog/control/newAddProduceFeatur',
	                data:JSON.stringify({
						"description" : createAttrValue,
	                    "productFeatureTypeId" : parentId
					}),
	                success: function(res){
	                	$.hideLoading(e.currentTarget);
	                    if(res.success){
	                        var html = '';
	                        if(jQuery(_this).parent().siblings('td').hasClass('selected')){
	                            html =  '<div class="attr_group" data-parentId="'+parentId+'" data-id="'+parentId+createAttrValue+'">'+
	                            '<a href="javascript:void(0)"  class="common-attr">' + createAttrValue + '</a><div class="operator_list"><span class="first_span"></span><span class="second_span"></span></div></div>';
	                        }else{
	                            html =  '<div class="attr_group"  data-parentId="'+parentId+'" data-id="'+parentId+createAttrValue+'">'+
	                            '<a href="javascript:void(0)"  class="common-attr">' + createAttrValue + '</a><div class="operator_list"><span class="first_span"></span><span class="second_span"></span></div></div>';
	                        }
	                        jQuery(_this).before(html);
	                        jQuery('.open_dialog').hide();
	                    }else {
	                        showErrorMessage(res.message);
	                    }
	                },
	                error : function(option, status){
	                	$.hideLoading(e.currentTarget);
	                }
	            });
	        });
	    });
	    
	    jQuery('.add_attr_table').on('click','.attr_second_span',function(){
		    var _this = this;
		    var productFeatureTypeId = jQuery(_this).closest('.attr_operator_list').siblings('a').attr('data-id');
		    var productFeatureTypeText = jQuery(_this).closest('.attr_operator_list').siblings('a').text();
	
	        deleteProductFeatureType(_this, {
	        	id : productFeatureTypeId,
	        	text : productFeatureTypeText
	        });
		});
	    
	    jQuery('.add_attr_table').on('click','.attr_first_span',function(){
		    var _this = this;
		    var productFeatureTypeId = jQuery(_this).closest('.attr_operator_list').siblings('a').attr('data-id');
		    var productFeatureTypeText = jQuery(_this).closest('.attr_operator_list').siblings('a').text();
		    
	        editAttrFun(_this, {
	        	id : productFeatureTypeId,
	        	text : productFeatureTypeText
	        })
		});
	    
	    jQuery('.add_attr_table').on('click','.second_span',function(){
		    var _this = this;
		    var productFeatureTypeId = jQuery(_this).closest('.attr_group').attr('data-id');
		  	var productFeatureTypeParentId = jQuery(_this).closest('.attr_group').attr('data-parentid');
		  	var productFeatureTypeText = jQuery(_this).parent('.operator_list').siblings('a').text();
		  	
	        deleteProductFeatureType(_this, {
	        	id : productFeatureTypeId,
	        	parentId : productFeatureTypeParentId,
	        	text : productFeatureTypeText
	        });
		});
	    
	    jQuery('.add_attr_table').on('click','.first_span',function(){
		    var _this = this;
		    var productFeatureTypeId = jQuery(_this).closest('.attr_group').attr('data-id');
		    var productFeatureTypeText = jQuery(_this).parent('.operator_list').siblings('a').text();
		    var productFeatureTypeParentId = jQuery(_this).closest('.attr_group').attr('data-parentid');
	        
	        editAttrFun(_this,{
	        	parentId : productFeatureTypeParentId,
	        	id : productFeatureTypeId,
	        	text : productFeatureTypeText
	        })
		});
	    
	    function editAttrFun(that, options){
	    	if(options){
	    		
			    $('.open_dialog .create_attr').val(options.text);
		        $('.open_dialog').show();
	    		
				$('.open_dialog').off('click', '.open_dialog_title').on('click', '.open_dialog_title',function(){
					$('.open_dialog').hide();
				});
		
				$('.open_dialog').off('click', '.confirm').on('click', '.confirm',function(e){
					var createAttr = $(".open_dialog .create_attr").val();
					
		            if(createAttr == ''){
		               $('.open_dialog').hide();
		               showErrorMessage('Attribute can not be empty');
		
		               return false;
		            }
		            
					if(/(<(.*)>)|(<\/(.*)>)/.test($(".open_dialog .create_attr").val())){
					   showErrorMessage('Cannot enter labels or scripts!');
					   
	                   return false;
					}
					
					$.showLoading(e.currentTarget);
		    		if(options.parentId){
				        jQuery.ajax({
				            type: 'post',
				            dataType: 'json',
				            contentType : 'application/json;charset=utf-8',
				            url: '/catalog/control/editProductFeature',
				            data:JSON.stringify({
				            	productFeatureId:options.id,
				            	productFeatureTypeId:options.parentId,
				            	description : $('.open_dialog .create_attr').val()
							}),
				            success: function (res) {
				                var dataResult = res.data;
				                $.hideLoading(e.currentTarget);
				                
				                if(res.success){
				                    $(that).closest('.operator_list').siblings('a').text($('.open_dialog .create_attr').val());
				                    $('.open_dialog').hide();
				                }else {
				                    showErrorMessage(res.message);
				                }
				            },
				            error:function(){
				            	$.hideLoading(e.currentTarget);
				            }
				        });
		    		}else{
				        jQuery.ajax({
				            type: 'post',
				            dataType: 'json',
				            contentType : 'application/json;charset=utf-8',
				            url: '/catalog/control/editProductFeatureType',
				            data:JSON.stringify({
				            	productFeatureTypeID : options.id,
								productFeatureTypeName : $('.open_dialog .create_attr').val()
							}),
				            success: function (res) {
				                var dataResult = res.data;
				                $.hideLoading(e.currentTarget);
				                
				                if(res.success){
				                    $(that).closest('.attr_operator_list').siblings('a').text($('.open_dialog .create_attr').val());
				                    $('.open_dialog').hide();
				                }else {
				                    showErrorMessage(res.message);
				                }
				            },
				            error:function(){
				            	$.hideLoading(e.currentTarget);
				            }
				        });
		    		}
				})
	    	}else{
	    		return false;
	    	}
	    }
	});
</script>

