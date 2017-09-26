<div id="product_catalogues">
	<div class="product_catalogues_content_wap">
		<div class="product_catalogues_content">
			<div class="product_catalogues_content_box">
				<div class="product_catalogues_title">
					<h4 class="catalogues_title">Select Category</h4>
				</div>
				<div class="product_catalogues_search">
					<span class="search-tips">Select Category</span>
					<div class="search-group">
						<input type="text" class="search_input" name="search_enter" />
						<span class="search_btn">Quick Search</span>
					</div>
				</div>
				<div class="product_catalogues_position">
					<span class="choose_title">Your Position:</span>
					<div class="had_choose"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="product_catalogues_choose">
					<div class="product_catalogues_choose_first">
						<div class="first_catalogues_search">
							<input type="text" class="first_catalogues_input" name="first_catalogues_enter" placeholder="Enter Name">
							<span class="first_catalogues_btn"></span>
						</div>
						<ul class="first_catalogues_ul">
						</ul>
						<div class="create_first_catalogues">Add First-Level Category</div>
					</div>
					<div class="first_point"></div>
					<div class="product_catalogues_choose_second">
						<div class="second_catalogues_search">
							<input type="text" class="second_catalogues_input" name="second_catalogues_enter" placeholder="Enter Name">
							<span class="second_catalogues_btn"></span>
						</div>
						<ul class="second_catalogues_ul">
						</ul>
						<div class="create_second_catalogues">Add Second-Level Category</div>
					</div>
					<div class="second_point"></div>
					<div class="product_catalogues_choose_third">
						<div class="third_catalogues_search">
							<input type="text" class="third_catalogues_input" name="third_catalogues_enter" placeholder="Enter Name">
							<span class="third_catalogues_btn"></span>
						</div>
						<ul class="third_catalogues_ul">
						</ul>
						<div class="create_third_catalogues">Add Third-Level Category</div>
					</div>
				</div>
				<div class="product_catalogues_result">
					<ul class="product_catalogues_result_ul"></ul>
				</div>
			</div>
			<div class="product_catalogues_next">
				<a href="javascript:void(0)" class="next_step">
					<span class="next_title">Next</span>
					<span class="next_detail">Fill in Product Details</span>
				</a>
			</div>
		</div>
	</div>
</div>
<div class="open_dialog">
    <div class="fixed_screen"></div>
    <div class="open_dialog_box">
        <div class="open_dialog_title"></div>
        <div class="open_dialog_content">
            <span>Category Name</span>
            <input type="text" class="enter_catalogues" />
        </div>
        <div class="open_dialog_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<div class="error_tips">
    <div class="fixed_screen"></div>
    <div class="error_tips_box">
        <div class="error_tips_title">Error Info.</div>
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
	    //错误信息提示
	    function showErrorMessage(message){
            var errorTips = jQuery('.error_tips');
            errorTips.find('.error_tips_content span').html('').html(message);
            errorTips.show();
	    }

		var firstLi = [],secondLi = [],thirdLi = [];
		//下一步
		jQuery('.next_step').on('click',function(){
		    var firstSelected = jQuery('.first_catalogues_ul').find('li.selected a').attr('data-id');
		    var secondSelected = jQuery('.second_catalogues_ul').find('li.selected a').attr('data-id');
		    var thirdSelected = jQuery('.third_catalogues_ul').find('li.selected a').attr('data-id');

		    if(firstSelected && secondSelected && thirdSelected){
		       window.location.href = '/catalog/control/BulkAddProductFeatures?firstId='+ firstSelected +'&secondId='+ secondSelected +'&thirdId='+ thirdSelected;
		    }else{
                showErrorMessage('Cannot sumbit without specifying three categories which your product belong to!')
		    }
		});
        //关闭错误弹框
        jQuery('.error_tips .error_tips_operator').on('click', 'a', function(){
            jQuery('.error_tips').hide();
        })
		//三级联动

		function getFirstCatalogues(){
			var firstCataloguesUl = jQuery(".first_catalogues_ul");
			var responseData,
				liContent='';
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
	 		    		var listData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		responseData = listData;
			    		firstLi = listData;
			    		jQuery.each(responseData, function(index, elem){
			    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
			    		});
			    		firstCataloguesUl.html('').append(liContent);
			    	}else{
			    	    var errorMessage = res.message;
			    	    showErrorMessage(errorMessage);
			    	}
			    }
			});
		}
		
		function getSecondCatalogues(categoryId, callBack){
			var secondCataloguesUl = jQuery(".second_catalogues_ul");
			var responseData,
				liContent='';
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
	 		    		var listData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
	 		    		responseData = listData;
			    		secondLi = listData;
			    		jQuery.each(responseData, function(index, elem){
			    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
			    		});
			    		secondCataloguesUl.html('').append(liContent);

			    		callBack && callBack();
			    	}else{
                        var errorMessage = res.message;
			    	    showErrorMessage(errorMessage);
                    }
			    }
			});
		}
		
		function getThirdCatalogues(categoryId, callBack){
			var thirdCataloguesUl = jQuery(".third_catalogues_ul");
			var responseData,
				liContent='';
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
	 		    		var listData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		responseData = listData;
			    		thirdLi = listData;
			    		jQuery.each(responseData, function(index, elem){
			    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
			    		});
			    		thirdCataloguesUl.html('').append(liContent);

			    		callBack && callBack();
			    	}else{
                        var errorMessage = res.message;
			    	    showErrorMessage(errorMessage);
                    }
			    }
			});
		}
		
		//初始化一级目录
		getFirstCatalogues();

		function loadSecondCategory(_this, callBack){
            var categoryId = jQuery(_this).attr('data-id'),
                categoryName = jQuery(_this).attr('data-name');
            var hadChoose = jQuery('.had_choose');

            jQuery(_this).parent().siblings().removeClass('selected');
            jQuery(_this).parent().addClass('selected');

            //一级目录切换时 先清空三级类目，然后再根据二级类目去选择三级类目
            jQuery('.third_catalogues_ul').html('');

            if(categoryId){
                hadChoose.html('').append('<a href="javascript:void(0)" class="first_a">' + categoryName + '</a>');
                getSecondCatalogues(categoryId, callBack);
            }
		}
		
		jQuery(".first_catalogues_ul").on('click','a',function(){
			var _this = this;

		    loadSecondCategory(_this);
		});

		function loadThirdCategory(_this, callBack){
		    var categoryId = jQuery(_this).attr('data-id'),
                categoryName = jQuery(_this).attr('data-name');
            var hadChoose = jQuery('.had_choose');

            jQuery(_this).parent().siblings().removeClass('selected');
            jQuery(_this).parent().addClass('selected');

            if(categoryId){
                hadChoose.find('a:first-child').nextAll().remove();
                hadChoose.find('a:first-child').after('<span class="second_span"> > </span><a href="javascript:void(0)" class="second_a">' + categoryName + '</a>');
                getThirdCatalogues(categoryId, callBack);
            }
		};
		
		jQuery(".second_catalogues_ul").on('click','a',function(){
			var _this = this;

			loadThirdCategory(_this);
		});

		function loadSelectCategory(_this, callBack){

            var categoryId = jQuery(_this).attr('data-id'),
                categoryName = jQuery(_this).attr('data-name');
            var hadChoose = jQuery('.had_choose');

            jQuery(_this).parent().siblings().removeClass('selected');
            jQuery(_this).parent().addClass('selected');

            if(categoryId){
                hadChoose.find('.third_span').remove();
                hadChoose.find('.third_a').remove();
                hadChoose.append('<span class="third_span"> > </span><a href="javascript:void(0)" class="third_a">' + categoryName + '</a>');
            }

            callBack && callBack();
		}

		jQuery(".third_catalogues_ul").on('click','a',function(){
			var _this = this;

			loadSelectCategory(_this);
		});
		
		jQuery('.first_catalogues_btn').on('click', function(){
			var categoryId = jQuery('.first_catalogues_input').val();
			var liLength = firstLi.length;
			var params = [],liContent='';
			
			if(liLength > 0){
				jQuery.each(firstLi, function(index,elem){
					if((elem['categoryName'].toLowerCase()).indexOf(categoryId.toLowerCase()) > -1){
						params.push(elem);
					}
				})
				jQuery.each(params, function(index, elem){
	    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
	    		});
				jQuery('.first_catalogues_ul').html('').append(liContent);
				
			}else{
				return false;
			}
		});
		jQuery('.second_catalogues_btn').on('click', function(){
			var categoryId = jQuery('.second_catalogues_input').val();
			var liLength = secondLi.length;
			var params = [],liContent='';
			
			if(liLength > 0){
				jQuery.each(secondLi, function(index,elem){
					if((elem['categoryName'].toLowerCase()).indexOf(categoryId.toLowerCase()) > -1){
						params.push(elem);
					}
				})
				jQuery.each(params, function(index, elem){
	    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
	    		});
				jQuery('.second_catalogues_ul').html('').append(liContent);
			}else{
				return false;
			}
		});
		jQuery('.third_catalogues_btn').on('click', function(){
			var categoryId = jQuery('.third_catalogues_input').val();
			var liLength = thirdLi.length;
			var params = [],liContent='';
			
			if(liLength > 0){
				jQuery.each(thirdLi, function(index,elem){
					if((elem['categoryName'].toLowerCase()).indexOf(categoryId.toLowerCase()) > -1){
						params.push(elem);
					}
				})
				jQuery.each(params, function(index, elem){
	    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem['categoryId']  +'" data-name="'+ elem['categoryName']  +'">'+ elem['categoryName'] +'</a></li>';
	    		});
				jQuery('.third_catalogues_ul').html('').append(liContent);
			}else{
				return false;
			}
		})
		
		//创建类目
		jQuery('.create_first_catalogues').on('click', function(){
			//弹出框
			jQuery('.enter_catalogues').val('');
			jQuery('.open_dialog').show();
			
			jQuery('.open_dialog').off('click','.confirm').on('click','.confirm',function(e){
				var enterCatalogues = jQuery(".enter_catalogues").val();
				var firstCataloguesUl = jQuery(".first_catalogues_ul");
				if(enterCatalogues == ''){
				   jQuery('.open_dialog').hide();
				   showErrorMessage('Category name cannot be empty!');
                   return false;
				}
				if(/(<(.*)>)|(<\/(.*)>)/.test(enterCatalogues)){
				   jQuery('.open_dialog').hide();
				   showErrorMessage('Cannot enter labels or scripts!');
                   return false;
				}
				$.showLoading(e.currentTarget);
				jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    url: '/catalog/control/CreateClassfication',
				    contentType: "application/json;charset=utf-8",
					data:JSON.stringify({
						categoryJSONArr:[{
							"categoryId" : null ,
							"categoryName" : enterCatalogues,
							"parentCategoryId" : 'DefaultRootCategory',
							"sequenceNum" : firstLi.length ? firstLi.length + 1 : 1,
							"createTime" : (new Date()).getTime()
						}]
					}),
				    success: function(data){
				    	$.hideLoading(e.currentTarget);
		 		    	if(data.success){
		 		    		var liContent ='';
		 		    		liContent='<li class="selected"><a href="javascript:void(0);" data-id="'+ data.data.newCategoryId  +'" data-name="'+ enterCatalogues  +'">'+ enterCatalogues +'</a></li>';
		 		    		
			    			firstLi.push({
			    				categoryName : enterCatalogues,
			    				categoryId : data.data.newCategoryId,
			    				sequenceNum : firstLi.length ? firstLi.length + 1 : 1
			    			});
			    			firstCataloguesUl.find('li').removeClass('selected');

		 		    		firstCataloguesUl.prepend(liContent);

		 		    		firstCataloguesUl.find('li.selected a').click();
		 		    		
		 		    		jQuery('.open_dialog').hide();
				    	}else{
				    	    jQuery('.open_dialog').hide();
                            var errorMessage = data.message;

                            showErrorMessage(errorMessage);
                        }
				    },
				    error : function(option, status){
				    	$.hideLoading(e.currentTarget);
				    }
				});
			})
			
			jQuery('.open_dialog').off('click','.open_dialog_title').on('click','.open_dialog_title',function(){
				jQuery('.open_dialog').hide();
			})
		});
		
		
		jQuery('.create_second_catalogues').on('click', function(){
            if(jQuery(".first_catalogues_ul").find('li.selected').length == 0){
                   showErrorMessage('Please select first-level category!');

                   return false;
            }
			//弹出框
			jQuery('.enter_catalogues').val('');
			jQuery('.open_dialog').show();
			
			jQuery('.open_dialog').off('click','.confirm').on('click','.confirm',function(e){
				var enterCatalogues = jQuery(".enter_catalogues").val();
				var secondCataloguesUl = jQuery(".second_catalogues_ul");
                if(enterCatalogues == ''){
                   jQuery('.open_dialog').hide();
                   showErrorMessage('Category name cannot be empty!');

                   return false;
                }
				if(/(<(.*)>)|(<\/(.*)>)/.test(enterCatalogues)){
				   jQuery('.open_dialog').hide();
				   showErrorMessage('Cannot enter labels or scripts!');
                   return false;
				}
                $.showLoading(e.currentTarget);
				jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    url: '/catalog/control/CreateClassfication',
				    contentType: "application/json;charset=utf-8",
					data:JSON.stringify({
						categoryJSONArr:[{
							"categoryId" : null,
							"categoryName" : enterCatalogues,
							"parentCategoryId" : jQuery(".first_catalogues_ul").find('li.selected a').attr('data-id'),
							"sequenceNum" : secondLi.length ? secondLi.length + 1 : 1,
							"createTime" : (new Date()).getTime()
						}]
					}),
				    success: function(data){
				    	$.hideLoading(e.currentTarget);
		 		    	if(data.success){
		 		    		var liContent ='';
			    			liContent='<li class="selected"><a href="javascript:void(0);" data-id="'+ data.data.newCategoryId  +'" data-name="'+ enterCatalogues  +'">'+ enterCatalogues +'</a></li>';
		 		    		
			    			secondLi.push({
			    				categoryName : enterCatalogues,
			    				categoryId : data.data.newCategoryId,
			    				sequenceNum : secondLi.length ? secondLi.length + 1 : 1
			    			});

			    			secondCataloguesUl.find('li').removeClass('selected');
			    			secondCataloguesUl.prepend(liContent);

			    			secondCataloguesUl.find('li.selected a').click();
			    			jQuery('.open_dialog').hide();
				    	}else {
				    	    jQuery('.open_dialog').hide();
                            var errorMessage = data.message;

                            showErrorMessage(errorMessage);
                        }
				    },
				    error : function(option, status){
				    	$.hideLoading(e.currentTarget);
				    }
				});
			})
			
			jQuery('.open_dialog').off('click', '.open_dialog_title').on('click', '.open_dialog_title', function(){
				jQuery('.open_dialog').hide();
			})
		})
		jQuery('.create_third_catalogues').on('click', function(){
            if(jQuery(".second_catalogues_ul").find('li.selected').length == 0){
                   showErrorMessage('Please select second-level category!')

                   return false;
            }
			//弹出框
			jQuery('.enter_catalogues').val('');
			jQuery('.open_dialog').show();
			
			jQuery('.open_dialog').off('click','.confirm').on('click','.confirm',function(e){
				var enterCatalogues = jQuery(".enter_catalogues").val();
				var thirdCataloguesUl = jQuery(".third_catalogues_ul");

                if(enterCatalogues == ''){
                   jQuery('.open_dialog').hide();
                   showErrorMessage('Category name cannot be empty!');

                   return false;
                }
				if(/(<(.*)>)|(<\/(.*)>)/.test(enterCatalogues)){
				   jQuery('.open_dialog').hide();
				   showErrorMessage('Cannot enter labels or scripts!');
                   return false;
				}
                $.showLoading(e.currentTarget);
				jQuery.ajax({
				    type: 'post',
				    dataType: 'json',
				    contentType: "application/json;charset=utf-8",
				    url: '/catalog/control/CreateClassfication',
					data:JSON.stringify({
						categoryJSONArr:[{
							"categoryId" : null,
							"categoryName" : enterCatalogues,
							"parentCategoryId" : jQuery(".second_catalogues_ul").find('li.selected a').attr('data-id'),
							"sequenceNum" : thirdLi.length ? thirdLi.length + 1 : 1,
							"createTime" : (new Date()).getTime()
						}]
					}),
				    success: function(data){
				    	$.hideLoading(e.currentTarget);
		 		    	if(data.success){
			    			var liContent ='';
			    			liContent='<li class="selected"><a href="javascript:void(0);" data-id="'+ data.data.newCategoryId  +'" data-name="'+ enterCatalogues  +'">'+ enterCatalogues +'</a></li>';
		 		    		
			    			thirdLi.push({
			    				categoryName : enterCatalogues,
			    				categoryId : data.data.newCategoryId,
			    				sequenceNum : thirdLi.length ? thirdLi.length + 1 : 1
			    			});
			    			thirdCataloguesUl.find('li').removeClass('selected');

			    			thirdCataloguesUl.prepend(liContent);

			    			thirdCataloguesUl.find('li.selected a').click();

			    			jQuery('.open_dialog').hide();
				    	}else {
				    	    jQuery('.open_dialog').hide();
                            var errorMessage = data.message;

                            showErrorMessage(errorMessage);
                        }
				    },
				    error : function(option, status){
				    	$.hideLoading(e.currentTarget);
				    }
				});
			})
			
			jQuery('.open_dialog').off('click','.open_dialog_title').on('click','.open_dialog_title',function(){
				jQuery('.open_dialog').hide();
			})
			
		})
		
		//快速查询
		jQuery(".search_btn").on('click', function(e){
			var searchInput = jQuery(".search_input").val();
			var productCataloguesResult = jQuery(".product_catalogues_result");
			var productCataloguesChoose = jQuery(".product_catalogues_choose");
			var productCataloguesResultUl = jQuery(".product_catalogues_result_ul");
			
			$.showLoading(e.currentTarget);
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType: "application/json;charset=utf-8",
			    url: '/catalog/control/QuickListClassfication',
			    data:JSON.stringify({
						"keyWord" : searchInput
					}),
			    success: function(data){
			    	$.hideLoading(e.currentTarget);
	 		    	if(data.success){
	 		    		productCataloguesResult.html('').show();
	 		    		productCataloguesChoose.hide();
	 		    		var paramsData = data.data.categoryIdAndNameList;
	 		    		var liContent ='<ul>';
	 		    		
	 		    		if(paramsData.length > 0){
	 		    			liContent+='<span class="find_catalogues">You have found:'+ paramsData.length +' results</span>';
	 		    			var datas = [];
	 		    			jQuery.each(paramsData, function(index, elem){
	 		    				var categoryIdList=[],categoryNameList=[];
		 		    			jQuery.each(elem, function(sortIndex, sortElem){
		 		    				categoryIdList.push(sortElem["categoryId"]);
		 		    				categoryNameList.push(sortElem["categoryName"]);
		 		    			});
		 		    			datas.push({
		 		    				categoryId : categoryIdList.join('|'),
		 		    				categoryName : categoryNameList.join('>>'),
		 		    				categoryNameAttr : categoryNameList.join('|'),
		 		    			})
	 		    			});
	 		    			
	 		    			jQuery.each(datas, function(index, elem){
		 		    			liContent+='<li><a href="javascript:void(0);" data-id="'+ elem.categoryId  +'" data-name="'+ elem.categoryNameAttr  +'">'+ elem.categoryName +'</a></li>';
	 		    			});
	 		    			liContent += '</ul>';
	 		    			
			    			productCataloguesResult.html('').append(liContent);
	 		    		}else{
	 		    			productCataloguesResult.html('').append('<span class="no_found">0 result!</span>');	
	 		    			jQuery(".had_choose").html('');
	 		    		}
			    	}else if(data.responseMessage=='error'){
                        var errorMessage = data.errorMessage;

                        showErrorMessage(errorMessage);
                    }
			    },
			    error : function(option, status){
			    	$.hideLoading(e.currentTarget);
			    }
			});
		});
		
		jQuery(".product_catalogues_result").on('click', 'a' ,function(){
			var cataloguesIdList = jQuery(this).attr("data-id");
			var cataloguesNameList = jQuery(this).attr("data-name");
			var productCataloguesResult = jQuery(".product_catalogues_result");
			var productCataloguesChoose = jQuery(".product_catalogues_choose");
			var firstCataloguesUl = jQuery(".first_catalogues_ul");
			var secondCataloguesUl = jQuery(".second_catalogues_ul");
			var thirdCataloguesUl = jQuery(".third_catalogues_ul");
			var hadChoose = jQuery(".had_choose");
			var IdGroup = cataloguesIdList.split('|'),
				nameGroup=cataloguesNameList.split('|');
			var chooseContent= '';
			
			productCataloguesChoose.show();
			productCataloguesResult.hide();
			
			if(IdGroup.length == 1){

				hadChoose.html('').append('<a href="javascript:void(0)" class="first_a">' + nameGroup[0] + '</a>');
				firstCataloguesUl.find('li').removeClass('selected');
				secondCataloguesUl.find('li').removeClass('selected');
				thirdCataloguesUl.find('li').removeClass('selected');

                firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]').parent().addClass('selected');
                firstCataloguesUl.animate({scrollTop:firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]').offset().top - firstCataloguesUl.find('a:first').offset().top },1000);
                jQuery('.first_catalogues_ul a[data-id="'+ IdGroup[0] +'"]').click();

			}else if(IdGroup.length == 2){
				hadChoose.html('').append(
						'<a href="javascript:void(0)" class="first_a">' + nameGroup[0] + '</a>'+
						'<span class="second_span"> > </span>'+
						'<a href="javascript:void(0)" class="second_a">' + nameGroup[1] + '</a>'
						);+
                firstCataloguesUl.find('li').removeClass('selected');
                secondCataloguesUl.find('li').removeClass('selected');
                thirdCataloguesUl.find('li').removeClass('selected');

                firstCataloguesUl.animate({scrollTop:firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]').offset().top - firstCataloguesUl.find('a:first').offset().top },1000);

                loadSecondCategory(firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]'), function(){
                    secondCataloguesUl.find('li a[data-id="'+ IdGroup[1] +'"]').parent().addClass('selected');
                    secondCataloguesUl.animate({scrollTop:secondCataloguesUl.find('li a[data-id="'+ IdGroup[1] +'"]').offset().top - secondCataloguesUl.find('a:first').offset().top },1000);
                    jQuery('.second_catalogues_ul a[data-id="'+ IdGroup[1] +'"]').click();
                });

			}else if(IdGroup.length == 3){
				hadChoose.html('').append(
						'<a href="javascript:void(0)" class="first_a">' + nameGroup[0] + '</a>'+
						'<span class="second_span"> > </span>'+
						'<a href="javascript:void(0)" class="second_a">' + nameGroup[1] + '</a>'+
						'<span class="third_span"> > </span>'+
						'<a href="javascript:void(0)" class="third_a">' + nameGroup[2] + '</a>'
						);
                firstCataloguesUl.find('li').removeClass('selected');
                secondCataloguesUl.find('li').removeClass('selected');
                thirdCataloguesUl.find('li').removeClass('selected');

                firstCataloguesUl.animate({scrollTop:firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]').offset().top - firstCataloguesUl.find('a:first').offset().top },1000);

                loadSecondCategory(firstCataloguesUl.find('li a[data-id="'+ IdGroup[0] +'"]'), function(){
                    secondCataloguesUl.find('li a[data-id="'+ IdGroup[1] +'"]').parent().addClass('selected');
                    secondCataloguesUl.animate({scrollTop:secondCataloguesUl.find('li a[data-id="'+ IdGroup[1] +'"]').offset().top - secondCataloguesUl.find('a:first').offset().top },1000);

                    loadThirdCategory(jQuery('.second_catalogues_ul a[data-id="'+ IdGroup[1] +'"]'), function(){
						thirdCataloguesUl.find('li a[data-id="'+ IdGroup[2] +'"]').parent().addClass('selected');
						thirdCataloguesUl.animate({scrollTop:thirdCataloguesUl.find('li a[data-id="'+ IdGroup[2] +'"]').offset().top - thirdCataloguesUl.find('a:first').offset().top },1000);
						loadSelectCategory(jQuery('.third_catalogues_ul a[data-id="'+ IdGroup[2] +'"]'));
                    })
                });
			}
		})
	});
	
</script>