
<link href="/commondefine_css/jQuery/jquery-ui.min.css" rel="stylesheet">
<div id="erp_pos">
    <input type="hidden" id="orderId" />
    <input type="hidden" id="productStoreId" />
    <input type="hidden" id="buyerId" />
    <input type="hidden" id="uomid" />
    <input type="hidden" id="editPrice" />
    <input type="hidden" id="uomid_country" />
    <input type="hidden" id="addressInfo" />
	<div class="erp_pos_head_box">
		<div class="erp_pos_header">
			<div class="erp_pos_logo"><a href="/opentaps"></a></div>
			<div class="erp_pos_title">
				<span class="header_title"></span>
			</div>
			<div class="logout_btn">Logout</div>
			<div class="user_login">Welcome, <span class="userNameContent"></span><i></i>
				<#if security.hasPermission("WRHS_INV_TI", session)>
					<em class="receiveNum receiveContent"></em>
				</#if>
			</div>
			<div class="menuList">
               	<#if security.hasPermission("WRHS_INV_TI", session)>
            		<a href="/warehouse/control/newTransferInventory?type=Receive" class="receiveTransfer" data-type="receive"><em class="receiveNumList receiveContent"></em>Transfer Item To Receive</a>
            	</#if>
                <a href="javascript:void(0)" class="updatePassword" data-type="password">Change password</a>
            </div>
		</div>
	</div>
	<div class="erp_pos_content_box">
		<div class="erp_pos_content">
			<div class="search_group">
				<div class="top_zIndex">
					<div class="sales_person_group">
						<select class="seller_select" name="seller_select" >
						</select>
					</div>
					<div class="need_shipment_group">
						<select class="need_shipment" name="need_shipment">
							<option value="">Select Shipping</option>
							<option value="Y">Need Shipping</option>
							<option value="N" selected="selected">Without Shipping</option>
						</select>
					</div>
				</div>
				<div class="btm_zIndex">
					<div class="Member">
						<input class="Member_input" type="text" placeholder="Customer Name" />
						<ul class="Member_ul" style="display:none;" tabindex="11" ></ul>
					</div>
					<div class="Phone">
						<input class="phone_input" type="text" placeholder="Customer Phone" />
						<ul class="Phone_ul" style="display:none;" tabindex="12" ></ul>
					</div>
					<div class="Address">
						<input class="address_input" type="text" placeholder="Customer Address" />
						<ul class="Address_ul" style="display:none;" tabindex="13" >
							<li class="addNewAddress">+Add New Address</li>
						</ul>
					</div>
				</div>
				<div class="error_tip_message"></div>
			</div>
			<div class="content_list">
				<div class="content_left_list">
					<div class="content_top_zIndex">
						<div class="input_group">
							<input class="product_input" type="text" placeholder="Product ID / Model / EAN / IMEI" />
							<a href="javascript:void(0);" class="add_cart">Add To</a>
							<ul class="product_ul" style="display:none;" tabindex="22"></ul>
						</div>
						<#if imei! == 'Y'>
						<div class="input_group">
							<input class="imei_input" type="text" placeholder="IMEI" />
							<a href="javascript:void(0);" class="add_imei_cart">Add To</a>
						</div>
						</#if>
						<div class="btn_group">
							<a class="payment_btn" href="javascript:void(0);">Pay Cash(F3)</a>
							<a class="invoice_btn no_pay_finish" href="javascript:void(0);">Pay Finish(F8)</a>
							<a class="empty_btn" href="javascript:void(0);">Empty Cart(F9)</a>
						</div>
					</div>
					<div class="content_btm_zIndex">
						<div class="list_header">
							<div class="cart_logo">Cart</div>
							<div class="resize_icon"></div>
							<div class="cart_total">Difference: <span class="give_change"><em class="currency"></em> <em class="difference_money">0</em></span></div>
							<div class="cart_total">Paid: <span class="paid"><em class="currency"></em> <em class="paid_money">0</em></span></div>
							<div class="cart_total">Total Amount: <span class="payment_money"><em class="currency"></em> <em class="all_money">0</em></span></div>
						</div>
						<div class="list_tab_content">
						</div>
					</div>
				</div>
				<div class="content_right_list">
					<div class="content_right_title">Recommended</div>
					<ul class="content_right_ul">
					</ul>
				</div>
				<div style="clear:both;"></div>
			</div>
		</div>
	</div>
</div>
<div class="open_new_member ui-widget-content">
	<div class="fixed_screen"></div>
	<div class="add_new_member"  id="openMember">
		<div class="new_member_icon"></div>
		<div class="new_member_title">New Customer</div>
		<div class="new_member_content">
            <div class="input_group">
                <span>Delivery address</span>
                <select class="address_select_province" name="address_select_province">
                    <option value="">select province</option>
                </select>
                <span class="addrenss_loading"></span>
                <select class="address_select_city" name="address_select_city">
                    <option value="">select city</option>
                </select>
                <span class="addrenss_loading"></span>
                <select class="address_select_area" name="address_select_area">
                    <option value="">select area</option>
                </select>
                <span class="addrenss_loading"></span>
            </div>
            <div class="input_group">
                <span>Street address</span>
                <input type="text" class="address_detail" />
            </div>
            <span class="error_info error_address"></span>
		</div>
		<div class="new_member_operator">
			<a class="confirm" href="javascript:void(0);">Confirm</a>
			<a class="cancel" href="javascript:void(0);">Cancel</a>
		</div>
	</div>
</div>
<div class="open_quick_list ui-widget-content">
	<div class="fixed_screen"></div>
	<div class="add_quick_list" id="openQuickList">
		<div class="quick_list_title"></div>
		<div class="quick_list_tit"></div>
		<div class="quick_list_content">
			<div class="row_content" style="clear:both">
				<span>Total amount</span>
				<div class="row_content_div">
					<em class="total_amount_currency"></em>
					<span class="total_amount"></span>
				</div>
				
			</div>
			<div class="row_content" style="clear:both">
				<span>Enter the amount</span>
				<input type="text" class="enter_payment" />
			</div>
		</div>
		<div class="quick_list_operator">
			<a class="confirm" href="javascript:void(0);">Confirm</a>
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
<div class="enter_imei">
    <div class="fixed_screen"></div>
    <div class="enter_imei_box">
    	<div class="enter_imei_tit"></div>
        <div class="enter_imei_title">Scan / Input 15-digits IMEI code</div>
        <div class="enter_imei_content">
            <input type="text" class="enter_imei_input" />
        </div>
        <div class="error_message"></div>
        <div class="enter_imei_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<script type="text/javascript" src="/commondefine_js/jQuery/jquery-ui.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var timeOut = null;
	var timer = 1000;
	var clickArr = [];
	var currentArr = [];
	var goodsList = {};
	function findPriceUOM(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findPriceUOM',
	        data: {},
	        success: function (data) {
	        	if(data.responseMessage == 'success'){
	        		var dataResult = data.uomMap;

	        		var currency = $('.currency');
	        		var uomidCountry = $("#uomid_country");
	        		var needShipment = $('.need_shipment');
	        		var needShipmentGroup = $('.need_shipment_group');
	        		var uomId = $('#uomid');
	        		if(dataResult.isShip == 'N'){
	        			needShipmentGroup.hide();
	        		}else if(dataResult.isShip == 'Y'){
	        			needShipmentGroup.show();
	        		}
	        		currency.html(dataResult.uomId);
	        		uomId.val(dataResult.countryId);
	        		uomId.attr('data-countryName', dataResult.countryName);
	        		uomidCountry.val(dataResult.uomId);
	        	}else if(data.responseMessage == 'error'){
                	showErrorMessage(data.errorMessage);
                }
	       	}
		});
	}
	initTableHead();
	findPriceUOM();
    function initTableHead(){
    	var str='<div class="list_tab_content_head">'+
    			'<span>No</span>'+
        		'<span>Product ID</span>'+
        		'<span class="itemDescriptionSpan">Product Description</span>'+
        		'<span>Quantity</span>'+
        		'<span>IMEI</span>'+
        		'<span>Unit price(<em class="currency"></em>)</span>'+
        		'<span>Total amount(<em class="currency"></em>)</span>'+
        		'<span>Adjust amount(<em class="currency"></em>)</span>'+
        		'<span>Edit</span></div><div class="list_tab_content_detail"></div>';
        $('.list_tab_content').html(str);
    }
	//error message
    function showErrorMessage(message, callBack){
        var errorTips = jQuery('.error_tips');
        errorTips.find('.error_tips_content span').html('').html(message);
        errorTips.show();

	    errorTips.find('.error_tips_operator').off('click','a').on('click','a',function () {
		    $('.error_tips').hide();
		    callBack && callBack();
	    })
    }

    //parseInt
    function parseNumberList(option){
        return parseInt(option, 10);
    }

    jQuery.ajax({
        url:'/wpos/control/initWebpos',
        type: 'post',
        dataType: 'json',
        contentType:"application/x-www-form-urlencoded",
        data:{
          "productStoreId":getUrlParams("productStoreId")
        },
        success: function(jsonObj){
            if(jsonObj.responseMessage == 'success'){
			   var editPrice = $('#editPrice');
            	
               var productStore = jsonObj.productStore;
               var inventoryFacilityName = productStore && productStore.storeName || "";
               
               var productStoreId = productStore && productStore.productStoreId || '';
               $("#productStoreId").val(productStoreId);

               var sellerList = jsonObj.personList;
               var contentList = '<option value="">Select Salesperson</option>';

               if(sellerList.length > 0){
                    $.each(sellerList,function(index, elem){
                        contentList += '<option value="'+ elem.partyId +'">' + elem.firstName + ' ' + elem.lastName + ' - Sales Person' + '</option>';
                    });
               }

               $(".header_title").html(inventoryFacilityName);
               
               $("#productStoreId").val(jsonObj.productStoreId);

	           $('.userNameContent').html(jsonObj.userLoginId);

               $('.seller_select').append(contentList);
               
               hasStoreManageEvt();
               editPrice.val(jsonObj.editPrice);
               
               jsonObj.productStoreId && getStoreInfo(jsonObj.productStoreId);
            }else if(jsonObj.responseMessage == "error"){
                showErrorMessage(jsonObj.errorMessage);
            }
        }
    })

    //get store
    function getStoreInfo(option){
    	$.ajax({
            type: 'post',
            dataType: 'json',
            url: '/ordermgr/control/findDefaultAddress',
            data: {
            	productStoreId : option
            },
            success: function (data) {
                var dataResult = data;

                var addressInfo = $('#addressInfo');
                dataResult.provinceId && addressInfo.attr('data-provinceId', dataResult.provinceId);
                dataResult.cityId && addressInfo.attr('data-cityId', dataResult.cityId);
                dataResult.areaId && addressInfo.attr('data-areaId', dataResult.areaId);
            }
        });
    }

    //get params
    function getUrlParams(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
      return null;
    }
    
  	//add imei to cart
    /* jQuery('.add_imei_cart').on('click', function(e){
    	 var _this = $(this);
     	
         var productStoreId = $("#productStoreId").val();
         var sellerSelect = $('.seller_select').val();
         var MemberInput = $('.Member_input').val();
         var imei = jQuery('.imei_input').val();	        
         var buyerId = $( "#buyerId" ).val();
         var memberInput = $( ".Member_input" ).val();
         var phoneInput = $( ".phone_input" ).val();
         var addressId = $( ".address_input" ).attr('data-id');
         var imeiInput = $('.imei_input').val();
         var newExp = /^[0-9]*$/;
	    
	    if(sellerSelect == '' && MemberInput == '' && phoneInput == ''){
			showErrorMessage('Please fill in sales person and customer information');
			
		    return false;
	    }

	    if(sellerSelect == ''){
			showErrorMessage('Please fill in sales person and customer information');
			
		    return false;
	    }
	    
	    if(MemberInput == '' || phoneInput == ''){
			showErrorMessage('Please fill in customer information');
			
		    return false;
	    }
	    
	    if(!newExp.test(phoneInput)){
			showErrorMessage('Custom phone incorrect');
			
    		return false;
    	}
	    
	    if($('.need_shipment').val() == 'Y'){
	    	if(!addressId){
	    		showErrorMessage('Please fill in address');
				
			    return false;
	    	}
	    }else if($('.need_shipment').val() == ''){
	    	showErrorMessage('Please select shipment');
			
		    return false;
	    }
	    
  	    if(!newExp.test(imeiInput) || imeiInput.length != 15){
 		    showErrorMessage('Please input 15-digits IMEI code');

 		    return false;
 	    } 
 	    
 	    $.showLoading(e.currentTarget);
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: "/ordermgr/control/new_gwtSuggestImeiForCart",
            data: { "productStoreId":productStoreId, query: imeiInput },
            success: function(data) {
            	$.hideLoading(e.currentTarget);
                if(data.responseMessage == 'success'){
                	var dataList = data.result[0];
                	if(dataList.atp == 1){
                		
                        jQuery.ajax({
                            url:'/wpos/control/addToCart',
                            type: 'post',
                            dataType: 'json',
                            contentType:"application/x-www-form-urlencoded",
                            data:{
                              "productStoreId":productStoreId,
                              "add_product_id":dataList.productId,
                              "imei":imeiInput,
                              "quantity":1,
                              "orderId" :$('#orderId').val(),
                              "sellerId" :$('.seller_select').val(),
                              "buyerId" :$('#buyerId').val(),
                              "firstName" : memberInput,
                              "contactNumber" : phoneInput,
                              "contactMechId" : addressId
                            },
                            success: function(jsonObj){
                           	 
                                if(jsonObj.responseMessage == "success"){
                                    $('.imei_input').val('');

                                    var list_tab_content_detail = $('.list_tab_content_detail');
                                    list_tab_content_detail.html(resetContent(jsonObj.cart));

                	                setScroll();
                                }else if(jsonObj.responseMessage == "error"){
                                	showErrorMessage(jsonObj.errorMessage);
                                }
                            }
                        })
                	}else{
                		showErrorMessage('This IMEI code is unavailable, please try other ones.');
                	}
                }else if(data.responseMessage == "error"){
                    if(data.errCode && data.errCode == 100){
                		showErrorMessage(data.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(data.errorMessage);
                	}
                }
            },
            error : function(data, status){
            	$.hideLoading(e.currentTarget);
            }
        });
    }); */

    //cart number
    $('.list_tab_content').on('blur','.enter_number',function(){

        var _this = this;

        var productStoreId = getUrlParams("productStoreId");
        var quantity = jQuery(_this).val();
        var oldQuantity = parseNumberList(jQuery(_this).parent().siblings('.enter_span').text());
        var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
        var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');
        var newErp = /^([1-9]\d*|0)$/;

        if(!newErp.test(Number(quantity))){
            showErrorMessage('Please enter the correct number');

            var enterSpanText = jQuery(_this).parent().siblings('.enter_span').text();
            jQuery(_this).val(parseNumberList(enterSpanText));

            return false;
        }else{
	        jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(quantity))
        }

        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "value":quantity,
          "type":"quantity",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart',{
        	_this : _this,
        	type : 'Quantity',
        	oldQuantity : oldQuantity
        });
    });

    $('.list_tab_content').on('click','.del',function(){

        var _this = this;
        var _thisValue = $(_this).siblings('.enter_number').val();

        if(parseNumberList(_thisValue) <= 1){

            return false;
        }

        jQuery(_this).siblings('.enter_number').val(parseNumberList(jQuery(_this).siblings('.enter_number').val()) - 1);
        jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(jQuery(_this).siblings('.enter_number').val()));

        var productStoreId = getUrlParams("productStoreId");
        var quantity = parseNumberList(jQuery(_this).siblings('.enter_number').val());
        var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
        var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');

        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "value":quantity,
          "type":"quantity",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart');
    });

    $('.list_tab_content').on('click','.add',function(){

        var _this = this;
        var oldQuantity = parseNumberList(jQuery(_this).parent().siblings('.enter_span').text());
        
        jQuery(_this).siblings('.enter_number').val(parseNumberList(jQuery(_this).siblings('.enter_number').val()) + 1)
        jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(jQuery(_this).siblings('.enter_number').val()))

        var productStoreId = getUrlParams("productStoreId");
        var quantity = parseNumberList(jQuery(_this).siblings('.enter_number').val());
        var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
        var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');

        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "value":quantity,
          "type":"quantity",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart',{
        	_this : _this,
        	type : 'add',
        	oldQuantity : oldQuantity
        });
    });

    //delete
    $('.list_tab_content').on('click','.delete_div',function(){

        var _this = this;
	    if($(_this).hasClass('delete_finish_div')){
		    $(_this).removeClass('delete_finish_div');
	    }else{
		    return false;
	    }

        var productStoreId = getUrlParams("productStoreId");
        var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
        var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');
        var productId=$(this).closest('.list_tab_content_div').attr('data-productId');
        var isImeiProduct=$(this).attr('data-type');
        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "type":"del",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart',{
        	_this : _this,
        	type : 'delete',
        	productId : productId
        },null,isImeiProduct);
    });

    //price update
    $('.list_tab_content').on('blur','.enter_input',function(){

        var _this = this;

        var productStoreId = getUrlParams("productStoreId");
        var quantity = jQuery(_this).val();
        var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
        var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');
        var newErp = /^([1-9]\d*|0)$/;

        if(!newErp.test(Number(quantity))){
            showErrorMessage('Please enter the correct number');
            jQuery(_this).val(parseNumberList(jQuery(_this).siblings('.enter_span').text()));
            return false;
        }else{
	        jQuery(_this).siblings('.enter_span').text(parseNumberList(quantity));
        }

        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "value":quantity,
          "type":"price",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart');
    });
    
  	//imei price update
    $('.list_tab_content').on('blur','.enter_price_input',function(){

    	 var _this = this;

         var productStoreId = getUrlParams("productStoreId");
         var quantity = jQuery(_this).val();
         var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
         var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');
         var newErp = /^([1-9]\d*|0)$/;

         if(!newErp.test(Number(quantity))){
             showErrorMessage('Please enter the correct number');
             jQuery(_this).val(parseNumberList(jQuery(_this).siblings('.enter_span').text()));
             return false;
         }else{
 	        jQuery(_this).siblings('.enter_span').text(parseNumberList(quantity));
         }

        updateNumber({
          "productStoreId":productStoreId,
          "orderId":orderId,
          "orderItemSeqId":orderItemSeqId,
          "value":quantity,
          "type":"price",
          "sellerId" :$('.seller_select').val(),
          "buyerId" :$('#buyerId').val()
        },'/wpos/control/modifyCart',null,null,'imei');
    });

    function updateNumber(options, url, optionList, target,type){
        var list_tab_content_detail = $('.list_tab_content_detail');
        target && $.showLoading(target);
        jQuery.ajax({
            url:url,
            type: 'post',
            dataType: 'json',
            contentType:"application/x-www-form-urlencoded",
            data:options,
            success: function(jsonObj){
                var dataResult = jsonObj;
                target && $.hideLoading(target);
                if(dataResult.responseMessage == "success"){                 	
                	$('.list_tab_content_detail').html(resetContent(dataResult.cart));
                	
                	var deleteArr = [];
                	if(optionList && optionList.type == 'delete'){
                		if(clickArr.length > 0){
                			$.each(clickArr, function(index, elem){
                				if(elem.productId != optionList.productId){
                					deleteArr.push(elem);
                				}
                			})
                		}
                	}
                	clickArr = deleteArr;
                	
	                setScroll();
                }else if(dataResult.responseMessage == "error"){
                	if(optionList && optionList.type == 'delete'){
                		$(optionList._this).addClass('delete_finish_div');
                	}
                	if(optionList && optionList.type == 'Quantity'){
                    	$(optionList._this).val(optionList.oldQuantity);
                    	$(optionList._this).parent().siblings('span').text(optionList.oldQuantity);
                	}
                	if(optionList && optionList.type == 'add'){
                    	$(optionList._this).siblings('input').val(optionList.oldQuantity);
                    	$(optionList._this).parent().siblings('span').text(optionList.oldQuantity);
                	}
                	if(dataResult.errCode && dataResult.errCode == 100){
                		showErrorMessage(dataResult.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(dataResult.errorMessage);
                	}
                }
            },
            error : function(option, status){
            	target && $.hideLoading(target);
            }
        });

    }

    //reset table
    function resetContent(data){
    	goodsList = data;
    	
        var dataList = data.orderItems;
        var orderId = data.orderId;
        var htmlContent = '';
        var no=1;

        $('.all_money').text(data.grandTotal);
        $('.difference_money').text(parseNumberList($('.paid_money').text()) - parseNumberList(data.grandTotal));
        
        var differenceTotal = parseNumberList($('.paid_money').text()) - parseNumberList(data.grandTotal);
        
        if(differenceTotal >= 0 && (dataList && dataList.length > 0)){
        	$('.invoice_btn').removeClass('no_pay_finish');
        }else{
     	   $('.invoice_btn').addClass('no_pay_finish');
        }

        $('#orderId').val(data.orderId);

        if(dataList && dataList.length > 0){
        	 var editPrice = $('#editPrice').val();
        	$.each(dataList,function(index, elem){
                var editPriceDiv = '';
	            if(editPrice == 'Y'){
	            	editPriceDiv = '<div>'+
	                    '<span class="enter_span">'+ parseNumberList(elem.unitPrice) +'</span>'+
	                    '<input type="text" class="enter_input" value="'+ parseNumberList(elem.unitPrice) +'" />'+
	                '</div>';
	            }else{
	            	editPriceDiv = '<div>'+
		                '<span>'+ parseNumberList(elem.unitPrice) +'</span>'+
	                '</div>';
	            }
            	if(elem.imei){
                    htmlContent += '<div class="list_tab_content_div" data-id="'+ orderId +'" data-productId="'+ elem.productId +'" data-orderItemSeqId="'+ elem.orderItemSeqId +'" >'+
                    '<div>'+ no +'</div>'+
                    '<div>'+ elem.productId +'</div>'+
                    '<div class="itemDescriptionDiv" title="'+ elem.itemDescription +'" >'+ elem.itemDescription +'</div>'+
                    '<div>'+
                        '<span class="">1</span>'+
                    '</div>'+
                    '<div>'+
                		'<span class="">'+ elem.imei +'</span>'+
                	'</div>'+
                	editPriceDiv + 
                    '<div>'+ parseNumberList(elem.unitPrice) * parseNumberList(elem.quantity) +'</div>'+
		               '<div>'+ parseNumberList(elem.quantity) * (parseNumberList(elem.unitPrice) - parseNumberList(elem.unitRecurringPrice)) +'</div>'+
                    '<div><a href="javascript:void(0);" class="delete_div delete_finish_div"></a></div>'+
                '</div>';
            	}else{
                    htmlContent += '<div class="list_tab_content_div" data-id="'+ orderId +'" data-productId="'+ elem.productId +'" data-orderItemSeqId="'+ elem.orderItemSeqId +'" >'+
                    '<div>'+ no +'</div>'+
                    '<div>'+ elem.productId +'</div>'+
                    '<div class="itemDescriptionDiv" title="'+ elem.itemDescription +'" >'+ elem.itemDescription +'</div>'+
                    '<div>'+
                        '<span class="enter_span">'+ parseNumberList(elem.quantity) +'</span>'+
                        '<div class="operator_number">'+
                            '<span class="del">-</span>'+
                            '<input type="text" class="enter_number" value="'+ parseNumberList(elem.quantity) +'" />'+
                            '<span class="add">+</span>'+
                        '</div>'+
                    '</div>'+
                    '<div>'+
                    	'<span>-</span>'+
                    '</div>'+
                     editPriceDiv +
                    '<div>'+ parseNumberList(elem.unitPrice) * parseNumberList(elem.quantity) +'</div>'+
		            '<div>'+ parseNumberList(elem.quantity) * (parseNumberList(elem.unitPrice) - parseNumberList(elem.unitRecurringPrice)) +'</div>'+
                    '<div><a href="javascript:void(0);" class="delete_div delete_finish_div"></a></div>'+
                '</div>';
            	}
            	no++;
            })
        }

        return htmlContent;
    }

    //pay cash
    function paymentMoneyFun(options, url, dialog, target){

        var all_money = parseNumberList($('.all_money').text());
        var difference_money = parseNumberList($('.difference_money').text());
        var paid_money = parseNumberList($('.paid_money').text());
        var enterPayment = parseNumberList(options.price);

        $.showLoading(target);
        $.ajax({
            url:url,
            type: 'post',
            dataType: 'json',
            contentType:"application/x-www-form-urlencoded",
            data:options,
            success: function(jsonObj){
                var dataResult = jsonObj;
                $.hideLoading(target);
                if(dataResult.responseMessage == "success"){

                   $('.paid_money').text(paid_money + enterPayment);
                   $('.difference_money').text((paid_money + enterPayment) - all_money);
                   
                   var differenceTotal = (paid_money + enterPayment) - all_money;
                   var div_length = $('.list_tab_content_detail .list_tab_content_div').length;
                   
                   if(differenceTotal >= 0 && div_length > 0){
                  	 	$('.invoice_btn').removeClass('no_pay_finish');
                   }else{
                	   $('.invoice_btn').addClass('no_pay_finish');
                   }

                    dialog.hide();
                }else if(dataResult.responseMessage == "error"){
                    dialog.hide();
                    if(dataResult.errCode && dataResult.errCode == 100){
                		showErrorMessage(dataResult.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(dataResult.errorMessage);
                	}
                }
            },
            error : function(options, status){
            	$.hideLoading(target);
            }
        });
    }

    //F3
    $('.payment_btn').on('click', function(){
        if(!$('#orderId').val()){
            showErrorMessage('Please add to cart');
            return false;
        }

        var $dialog = $('.open_quick_list');
        var $dialogInput = $dialog.find('.enter_payment');
        var $dialogSpan = $dialog.find('.total_amount');
        var $totalAmountCurrency = $('.total_amount_currency');
        var $uomidCountry = $("#uomid_country").val();
        var $allMoney = $('.all_money').text();

        $dialogInput.val('');
        $dialogSpan.text($allMoney);
        $totalAmountCurrency.text($uomidCountry);
        $dialog.show();
	    initOpenMemberWH($('#openQuickList'));
        $('#openQuickList').draggable({
        	cursor:'move',
        	handle:'.quick_list_tit',
        	containment: 'window'
        });
        //close dialog
        $dialog.off('click','.quick_list_title').on('click','.quick_list_title',function(){
            $dialog.hide();
        });

        //confirm
        $dialog.off('click','.confirm').on('click','.confirm',function(e){


            var $enterPayment = $dialogInput.val();

            var $listTabContentDetail = $('.list_tab_content_detail');
            var $allMoney = $('.all_money').text();
            var $orderId = $('#orderId').val();
            var productStoreId = getUrlParams("productStoreId");
            var pattern= /^([1-9]\d*|0)$/;

            if(!pattern.test($enterPayment) || $enterPayment == ''){

	            showErrorMessage('Please enter a positive integer');

                return false;
            }
            paymentMoneyFun({
              "productStoreId":productStoreId,
              "orderId":$orderId,
              "price":$enterPayment,
              "sellerId" :$('.seller_select').val(),
              "buyerId" :$('#buyerId').val()
            },"/wpos/control/payCash",$dialog,e.currentTarget);
        });
    });

    $(document).keydown(function (e) {
	    var event = e || window.e;

        if (event && event.keyCode == 114) {
            $('.payment_btn').click();
        }
        if (event && event.keyCode == 119) {
            $('.invoice_btn').on('click', function(e){
                payInvoicing(e.currentTarget);
            });
        }
	    if (event && event.keyCode == 120) {
	        $('.empty_btn').on('click', function(e){
	    	    emptyCart(e.currentTarget);
	        });
	    }
    });

    //f8
    $('.invoice_btn').on('click', function(e){
        payInvoicing(e.currentTarget);
    });

    //f9
    $('.empty_btn').on('click', function(e){
	    emptyCart(e.currentTarget);
    });

    //empty cart
    function emptyCart(target) {

	    var productStoreId = getUrlParams("productStoreId");

	    var listContent = $('.list_tab_content_detail').find('.list_tab_content_div');

	    if(listContent.length == 0){
		    showErrorMessage('No goods can be cleared');

		    return false;
	    }
	    updateNumber({
		    "productStoreId":productStoreId,
		    "orderId":$('#orderId').val(),
		    "type":"delAll",
		    "sellerId" :$('.seller_select').val(),
		    "buyerId" :$('#buyerId').val()
	    },'/wpos/control/modifyCart',null,target);
    }

    //pay invocing
    function payInvoicing(target){
	    var sellerSelect = $('.seller_select').val();
	    var MemberInput = $('.Member_input').val();
        var needShipment = $('.need_shipment').val();
        var address = $('.address_input').val();
        var listContent = $('.list_tab_content .list_tab_content_div');
        var listId = [];
        var piwikArr = [];
        
        if(needShipment == ''){
        	showErrorMessage('Please select shipment way!');
        	
        	return false;
        }
        
        if(needShipment == 'Y' && address == ''){
        	showErrorMessage('Please fill in shipment address!');
        	
        	return false;
        }
	    
	    if($('.invoice_btn').hasClass('no_pay_finish')){
	    	return false;
	    }
	    
	    if(listContent.length > 0){
	    	$.each(listContent, function(index, elem){
	    		listId.push($(elem).attr('data-productId'));
	    	});
	    }
    	$.each(clickArr, function(index, elem){
    		if(listId.indexOf(elem.productId) > -1){
    			piwikArr.push(elem);
    		}
    	});
	    
	    $.showLoading(target);
        $.ajax({
           type: 'post',
           dataType: 'json',
           url: '/wpos/control/payInvoicing',
           data : {
               "productStoreId":getUrlParams("productStoreId"),
               "orderId":$('#orderId').val(),
               "isShip" : needShipment,
               "sellerId" :$('.seller_select').val(),
               "buyerId" :$('#buyerId').val()
           },
           success: function(data) {
        	   $.hideLoading(target);
               if(data.responseMessage=="success"){
	               showErrorMessage('Payment finish success', function () {
	            	   $('.invoice_btn').addClass('no_pay_finish');
                	   var orderId = data.cart && data.cart.orderId;
                	   var isNeedShip = data.isNeedShip;
               		   var printList = [{
               			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
               			   fileName : 'commoninvoice'
               		   },{
               			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
               			   fileName : 'commoninvoice_copy'
               		   }];
                	   if(isNeedShip == 'Y'){
                		   printList.push({
                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
                			   fileName : 'returnreceipt'
                		   });
                		   printList.push({
                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
                			   fileName : 'returnreceipt_copy'
                		   });
                		   printInvocingFun(printList);
                	   }else{
                		   printInvocingFun(printList); 
                	   }
	               	   location.reload();
	               });
	               
	               	var ecommerceItem = goodsList.orderItems;
    				var title = $(".header_title").html();
    				if(piwikArr.length >0){
    					_paq.push(["trackEvent", "Total Order Created", "total order has Recommended product","total recommended count",piwikArr.length]);
    					_paq.push(["trackEvent", title+" Order Created", "order has Recommended product","recommended count",piwikArr.length]);
    					_paq.push(["trackEvent", title+" Order Created", "order total product","Total count", ecommerceItem.length]);
    				}else{
    					_paq.push(["trackEvent", "Total Order Created", "total order no Recommended product","total recommended count",0]);
    					_paq.push(["trackEvent", title+" Order Created", "order no Recommended product","recommended count",0]);
    					_paq.push(["trackEvent", title+" Order Created", "order total product","Total count", ecommerceItem.length]);
    				}
    				
    			
    				for (var i = 0; i < ecommerceItem.length; i++){
	    				var item = ecommerceItem[i];
	    				var productName = item.itemDescription;
	    				var productSKU = item.productId;
	    				var quantity = item.quantity;
	    				var price = item.unitPrice;
	    				var productCategory = title
    					_paq.push(["addEcommerceItem",productSKU, productName, productCategory, price, quantity]);
		        	}
    					_paq.push(["trackEcommerceOrder", goodsList.orderId, goodsList.grandTotal]);
    				
    				
               }else if(data.responseMessage=="error"){
                   if(data.errCode && data.errCode == 100){
	               		showErrorMessage(data.errorMessage, function(){
	               			location.reload();
	               		});
	               	}else{
	               		showErrorMessage(data.errorMessage);
	               	}
               }
           },
           error :function(option, status){
        		$.hideLoading(target);
           }
        });
    }
        
     //print invocing
     function printInvocingFun(options){
     	if(options.length == 0){
     		return false;
     	}
     	
     	if(typeof window.chrome !== 'undefined'){
     		    var link;
            	$.each(options, function(index, elem){
            		link = document.createElement('a');
            		link.href = elem.url;
            		link.target = '_blank';
            		link.click();
            	});
     	}else if(typeof window.navigator.msSaveBlob !== 'undefined'){
      		var link;
         	$.each(options, function(index, elem){
           		link = $('<a href="'+ elem.url +'" download=="'+ elem.fileName +'" title="'+ elem.fileName +'" target="_blank"></a>')
      	   	    link.get(0).click();  
         	}); 
     	}else{
     		var link;
         	$.each(options, function(index, elem){
         		link = new File([elem.url], elem.fileName, { type: 'application/force-download' });
         		window.open(URL.createObjectURL(link));
         	});
     	}
     }

    //get country
    function findGeo(option,elem, callback){
    	elem.addClass("loading").attr("disabled","disabled").next(".addrenss_loading").show();

        var list = [];
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '/ordermgr/control/findGeo',
            data: {
                geoId : option,
            },
            success: function (data) {
                var dataResult = data;
                if(dataResult.responseMessage == "success"){
                    list = dataResult.reslutList;

                    var countryContent = '';

                    list.length > 0 && $.each(list, function(index, elem){
                        countryContent+= '<option value="' + elem.id + '" data-name="'+ elem.name +'" >'+ elem.name +'</option>';
                    });
                    elem.children().not(':first-child').remove();
                    elem.append(countryContent);

                    callback && callback();
                }else{
                	if(dataResult.errCode && dataResult.errCode == 100){
                		showErrorMessage(dataResult.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(dataResult.errorMessage);
                	}
                }
                elem.removeClass("loading").removeAttr("disabled").next(".addrenss_loading").hide();
            }
        });

    }
    
    function initOpenMemberWH(options){
    	var bodyW = $(window).width();
        var bodyH = $(window).height();
        
        var elW =  options.width();
        var elH =  options.height();
	    options.css("left",(bodyW - elW)/2 + 'px');
	    options.css("top",(bodyH - elH)/2 + 'px')
    }

    //create customer
    $('.Address_ul').on('click', 'li', function(){
    	var _this = $(this);
    	var MemberInput = $('.Member_input');
    	var phoneInput = $('.phone_input');
    	var newExp = /^[0-9]*$/;
    	
    	if(MemberInput.val() == ''){
    		
    		$('.error_tip_message').text('Custom name not null');
    		$('.Address_ul').hide();
    		return false;
    	}
    	if(phoneInput.val() == ''){
    		
    		$('.error_tip_message').text('Custom phone not null');
    		$('.Address_ul').hide();
    		return false;
    	}if(!newExp.test(phoneInput.val())){
    		
    		$('.error_tip_message').text('Custom phone incorrect');
    		$('.Address_ul').hide();
    		return false;
    	}else{
    		$('.error_tip_message').text('');
    	}
    	
    	$('.Address_ul').hide();
    	if(_this.attr('data-id')){
    		
    		$('.address_input').val(_this.text());
    		$('.address_input').attr('data-id', _this.attr('data-id'));
    		
    		return false;
    	}
    	
        var openNewMember = $('.open_new_member');
        
        initOpenMemberWH($('#openMember'));
        openNewMember.show();
        $("#openMember .error_info").each(function(){
    		$(this).text('');
    	})
        $("#openMember").draggable({
        	cursor:'move',
        	handle:'.new_member_title',
        	containment: 'window'
        });

        //customer address  
        var addressInput = $('.address_detail');

        addressInput.val('');

        //init
        $('.address_select_province').children().not(':first-child').remove();
        $('.address_select_city').children().not(':first-child').remove();
        $('.address_select_area').children().not(':first-child').remove();

        //look country
        var countryId = $('#uomid').val();
        var countryName = $('#uomid').attr('data-countryname');
        var addressInfo = $('#addressInfo');

        //look province
        findGeo(countryId,$('.address_select_province'),function(){
        	
            if(addressInfo.attr('data-provinceid')){
            	$('.address_select_province').find('option[value="'+ addressInfo.attr('data-provinceid') +'"]').attr('selected', 'selected');
            	$('.address_select_province').change();
            }
            
        });

        $('.address_select_province').on('change',function(){
            var _this = $(this);
            $('.address_select_city').children().not(':first-child').remove();
            $('.address_select_area').children().not(':first-child').remove();
            
            if(_this.val() == ''){
                return false;
            }

            //look city
            findGeo(_this.val(),$('.address_select_city'),function(){
            	
                if(addressInfo.attr('data-cityid')){
                	$('.address_select_city').find('option[value="'+ addressInfo.attr('data-cityid') +'"]').attr('selected', 'selected');
                	$('.address_select_city').change();
                }
            });
        });

        $('.address_select_city').on('change',function(){
            var _this = $(this);
            $('.address_select_area').children().not(':first-child').remove();
            
            if(_this.val() == ''){
                return false;
            }
            $(".new_member_content .error_info").text('');     
            findGeo(_this.val(),$('.address_select_area'), function(){
            	
                if(addressInfo.attr('data-areaid')){
                	$('.address_select_area').find('option[value="'+ addressInfo.attr('data-areaid') +'"]').attr('selected', 'selected');
                }
            });
        });

        //close dialog
        openNewMember.off('click','.new_member_icon').on('click','.new_member_icon',function(){
            openNewMember.hide();
        });

        openNewMember.off('click','.cancel').on('click','.cancel',function(){
            openNewMember.hide();
        });

        //confirm dialog
        openNewMember.off('click','.confirm').on('click','.confirm',function(e){
        	var _this = $(this); 
        	
        	var phoneNumber = $('.phone_input').val();
        	var MemberInput = $('.Member_input').val();
        	var partyId = $('#buyerId').val();
            var address_select_country = countryId;
            var address_select_province = $('.address_select_province').val();
            var address_select_city = $('.address_select_city').val();
            var address_select_area = $('.address_select_area').val();
            var address_detail = $('.address_detail').val();
	        
	        if(address_select_country == '' || address_select_province == '' || address_select_city == '' || address_select_area == '' || address_detail == ''){
	        	
	        	$(".new_member_content .error_info").text('Street address and Delivery address must not be empty');
	        	return false;
	        }
	        
	        $.showLoading(e.currentTarget);

            $.ajax({
                url:"/wpos/control/addPostalAddress",
                type: 'post',
                dataType: 'json',
                contentType: "application/json;charset=utf-8",
                data:JSON.stringify({
                	postalCode : "000",
                    address1 : address_detail,
                    countryGeoId : address_select_country,
                    stateProvinceGeoId : address_select_province,
                    city : address_select_city,
                    countyGeoId : address_select_area,
                    firstName : MemberInput,
                    contactNumber : phoneNumber,
                    partyId : partyId
                }),
                success: function(jsonObj){
                    var dataResult = jsonObj;
                    $.hideLoading(e.currentTarget);
                    if(dataResult.success){

                        var checkedCountry = countryName;
                        var checkedProvince = $('.address_select_province').find("option:selected").attr('data-name');
                        var checkedCity = $('.address_select_city').find("option:selected").attr('data-name');
                        var checkedArea = $('.address_select_area').find("option:selected").attr('data-name');
                        var addressStr = '';

                        addressStr += checkedCountry + ' ' + checkedProvince + ' ' + checkedCity + ' ' + checkedArea + ' ' + address_detail;
                        
                        $('.address_input').val(addressStr);
                        $('.address_input').attr('data-id', dataResult.data.contactMechId);
                        $('#buyerId').val(dataResult.data.partyId);
                        $('.Address_ul').append('<li data-id="'+ dataResult.data.contactMechId +'">'+ addressStr +'</li>');

                        openNewMember.hide();
                    }else{
                    	if(dataResult.errCode && dataResult.errCode == 100){
                    		showErrorMessage(dataResult.errorMessage, function(){
                    			location.reload();
                    		});
                    	}else{
                    		showErrorMessage(dataResult.message);
                    	}
                    }
                },
                error : function(data, status){
                   	$.hideLoading(e.currentTarget);
                }
            });
        });

    });
    
    
	function myAutoComplete(dataOption){
    	
    	var Member_ul = $('.Member_ul');
    	var Phone_ul = $('.Phone_ul');
    	var Address_ul = $('.Address_ul');
        var MemberInput = $( ".Member_input" );
        var addressInput = $( ".address_input" );
        var phoneInput = $( ".phone_input" );
        var buyerId = $( "#buyerId" );
        
    	var dataList = dataOption;
    	var htmlContent = '';
    	var currentIndex = 0;
    	
    	if(dataList.length == 0){
    		
    		addressInput.val('');
    		Member_ul.hide();
    		
    		return false;
    	}
    	
    	$.each(dataList,function(index, item){
    		var valueContent = item.firstName;
    		var phoneNumber = item.contactNumber;
    		var id = item.partyId;
    		
    		htmlContent += '<li data-value="'+ valueContent +'" data-phone="'+ phoneNumber +'" data-id = "'+ id +'" >'+ valueContent +'</li>'
    	});
    	
    	Member_ul.html(htmlContent).show().focus();
    	Member_ul.find('li').eq(currentIndex).addClass('cur');
    	
    	Member_ul.find('li').click(function(){
    		var _this = $(this);
    		
    		addressInput.val('');
            phoneInput.val('');
    		
    		var dataId = _this.attr('data-id');
    		var dataValue = _this.attr('data-value');
    		var dataPhone = _this.attr('data-phone');

    		
    		buyerId.val(dataId);
    		MemberInput.val(dataValue);
    		phoneInput.val(dataPhone);
    		Member_ul.hide();
    		
    		$.each(dataList, function(index, elem){
    			if(elem.partyId == dataId){
    				var address = elem.customerAddress, str='';
    				var countryName = $('#uomid').attr('data-countryname');
    					Address_ul.children().not(':first-child').remove();

    				if(address && address.length > 0){
    					$.each(address, function(addressIndex, addressElem){
    						var detailaAddress = (countryName+ ' ' + addressElem.stateProvinceGeoId + ' ' + addressElem.city + ' ' + addressElem.countyGeoId + ' ' + addressElem.address1);
    						str+='<li data-id="'+ addressElem.contactMechId +'">'+ detailaAddress +'</li>'
    					})
    					Address_ul.append(str);
    				}
    			}
    		});
    	});
    	
    	Member_ul.keyup(function(e){
    		var keyCode = e.keyCode;
    		var length = Member_ul.find('li').length - 1;
    		if(keyCode == 40){
    			if(currentIndex != length){
    				currentIndex++;
       				Member_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				Member_ul.find('li').siblings().removeClass('cur').eq(length).addClass('cur');
    			}
    		}else if(keyCode == 38){
    			if(currentIndex != 0){
    				currentIndex--;
    				Member_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				Member_ul.find('li').siblings().removeClass('cur').eq(0).addClass('cur');
    			}
    		}else if(keyCode == 13){
    			Member_ul.find('li.cur').click();
    		}
    	});
    }
	
	function myAutoComplete_phone(dataOption){
    	
    	var Phone_ul = $('.Phone_ul');
    	var Address_ul = $('.Address_ul');
        var MemberInput = $( ".Member_input" );
        var phoneInput = $( ".phone_input" );
        var buyerId = $( "#buyerId" );
        var addressInput = $( ".address_input" );
        
    	var dataList = dataOption;
    	var htmlContent = '';
    	var currentIndex = 0;
    	
    	$.each(dataList,function(index, item){
    		var member = item.firstName;
    		var valueContent = item.contactNumber;
    		var id = item.partyId;
    		
    		htmlContent += '<li data-value="'+ valueContent +'" data-memeber="'+ member +'" data-id = "'+ id +'" >'+ valueContent +'</li>'
    	});
    	
    	Phone_ul.html(htmlContent).show().focus();
    	
    	Phone_ul.find('li').eq(currentIndex).addClass('cur');
    	
    	Phone_ul.find('li').click(function(){
    		var _this = $(this);
    		
  			MemberInput.val('');
  			addressInput.val('');
   			
    		var dataId = _this.attr('data-id');
    		var dataValue = _this.attr('data-value');
    		var dataMemeber = _this.attr('data-memeber');

    		
    		buyerId.val(dataId);
    		phoneInput.val(dataValue);
    		MemberInput.val(dataMemeber);
    		Phone_ul.hide();

    		$.each(dataList, function(index, elem){
    			if(elem.partyId == dataId){
    				var address = elem.customerAddress, str='';
    				var countryName = $('#uomid').attr('data-countryname');
    					Address_ul.children().not(':first-child').remove();

    				if(address && address.length > 0){
    					$.each(address, function(addressIndex, addressElem){
    						var detailaAddress = (countryName+ ' ' + addressElem.stateProvinceGeoId + ' ' + addressElem.city + ' ' + addressElem.countyGeoId + ' ' + addressElem.address1);
    						str+='<li data-id="'+ addressElem.contactMechId +'">'+ detailaAddress +'</li>'
    					})
    					Address_ul.append(str);
    				}
    			}
    		});
    	});
    }
	
	//select address
	
	$('.address_input').on('focus', function(){
		var Address_ul = $('.Address_ul');
		
		if(!$('#buyerId').val()){
			Address_ul.children().not(':first-child').remove();
		}
		
		Address_ul.show().focus();
	});
    
	//look phone
	
	$(".phone_input").keyup(function(event){
    	event.preventDefault();
    	event.stopPropagation();
    	
    	var PhoneInput = $( ".phone_input" ).val();
    	var MemberInput = $( ".Member_input" );
        var addressInput = $( ".address_input" );
    	var Phone_ul = $('.Phone_ul');
    	
    	if(PhoneInput == ''){
    		clearTimeout(timeOut);
    		Phone_ul.hide();
    		
    		return false;
    	}else{
    		$('.error_tip_message').text('');
    	}
    	
    	clearTimeout(timeOut);
    	timeOut = setTimeout(function(){
    		if(event.which == 9){
    			return false;
    		}
             $.ajax({
                type: 'post',
                dataType: 'json',
                url: '/wpos/control/findPersonByTelecomNumber',
                contentType: "application/json;charset=utf-8",
                data : JSON.stringify({
                	telecomNumber: PhoneInput
                }),
                success: function(data) {
                    if(data.success){
                    	$( "#buyerId" ).val('');
                    	if(data.data && data.data.length > 0){
                    		myAutoComplete_phone(data.data);
                    	}else{
                    		addressInput.val('');
                    		addressInput.attr('data-id', '');
                        	//MemberInput.val('');
                    	}
                    }else{
                    	if(data.errCode && data.errCode == 100){
                    		showErrorMessage(data.errorMessage, function(){
                    			location.reload();
                    		});
                    	}else{
                    		showErrorMessage(data.errorMessage);
                    	}
	                }
                }
            }) 
    	} ,timer); 
    });
	
	//mouseout
 	
	$(".Phone_ul").on('blur', function(){
		$(".Phone_ul").hide();
    });
	$(".Address_ul").on('blur', function(){
		$(".Address_ul").hide();
    });
	
    //look member
    $( ".Member_input" ).keyup(function(event){
    	event.preventDefault();
    	event.stopPropagation();
    	
    	var MemberInput = $( ".Member_input" ).val();
    	var PhoneInput = $( ".phone_input" );
        var addressInput = $( ".address_input" );
    	var Member_ul = $('.Member_ul');
    	
    	if(MemberInput == ''){
    		clearTimeout(timeOut);
    		Member_ul.hide();
    		
    		return false;
    	}else{
       		$('.error_tip_message').text('');
    	}
    	
    	clearTimeout(timeOut);
    	timeOut = setTimeout(function(){
    		if(event.which == 9){
    			return false;
    		}
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: '/wpos/control/findPersonByUserName',
                contentType: "application/json;charset=utf-8",
                data : JSON.stringify({
                	userName: MemberInput
                }),
                success: function(data) {
                    if(data.success){
                    	$( "#buyerId" ).val('');
                    	if(data.data && data.data.length > 0){
                    		myAutoComplete(data.data);
                    	}else{
                    		addressInput.val('');
                    		addressInput.attr('data-id', '');
                    		//PhoneInput.val('');
                    	}
                    }else{
                    	if(data.errCode && data.errCode == 100){
                    		showErrorMessage(data.errorMessage, function(){
                    			location.reload();
                    		});
                    	}else{
                    		showErrorMessage(data.errorMessage);
                    	}
	                }
                }
            }) 
    	} ,timer); 
    });

    //get right list
    $.ajax({
       type: 'post',
       dataType: 'json',
       url: '/wpos/control/findOrderPro',
       data : {
           "productStoreId":getUrlParams("productStoreId")
       },
       success: function(data) {
           if(data.responseMessage=="success"){
                var dataResult = data.productList;
                var contentList = '';
                var contentRightUl = $('.content_right_ul');
                $.each(dataResult,function(index, elem){
                    if(index < 17){
                        contentList+= '<li><a href="javascript:void(0);" title="'+ elem.productName+':'+elem.text +'" data-name="'+ elem.productName +'" data-imei="'+ elem.isImei +'" data-num="'+ elem.availableToPromiseTotal +'" data-id="'+ elem.productId +'">'+elem.productName+':'+elem.text +'</a></li>';
                    }
                });
                contentRightUl.html(contentList);
           }else if(data.responseMessage=="error"){
        	   if(data.errCode && data.errCode == 100){
	           		showErrorMessage(data.errorMessage, function(){
	           			location.reload();
	           		});
	           	}else{
	           		showErrorMessage(data.errorMessage);
	           	}
           }
       }
    });

    //logout
    $('.logout_btn').on('click', function(){
       $.ajax({
          url:"/commondefine/control/logout",
          type: 'post',
          dataType: 'json',
          contentType:"application/x-www-form-urlencoded",
          success: function(jsonObj){
              var dataResult = jsonObj;
              if(dataResult.responseMessage == "success"){
                  location.reload();
                  deleteAllCookie();
              }
          }
       });
    });
    
    //clear cookie
    function deleteAllCookie(){
    	var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
    	if (keys) {
        	for (var i = 0; i < keys.length; i++){
        		document.cookie=keys[i]+'=0;expires=' + new Date(0).toGMTString();
        	}
    	}    
	}
    
    //scroll
    function  setScroll() {
	    var content_btm_zIndex = $('.content_btm_zIndex');
	    var list_tab_content_detail = $('.list_tab_content_detail');
	    var list_tab_content_head = $('.list_tab_content_head');
	    var _content_height = content_btm_zIndex.height();

	    if(_content_height - 90 < list_tab_content_detail[0].scrollHeight){

		    list_tab_content_detail.css({
			    "height" : (_content_height - 90) + 'px'
		    });
		    list_tab_content_detail.addClass("isScroll");
		    list_tab_content_head.addClass("resetWidth");
	    }else {

		    list_tab_content_detail.css({
			    "height" : "auto"
		    });
		    list_tab_content_detail.removeClass("isScroll");
		    list_tab_content_head.removeClass("resetWidth");
	    }
    }

    $(document).on('click', function(){
		var product_ul = $('.product_ul');
    	var Member_ul = $('.Member_ul');
    	
    	Member_ul.hide();
    	product_ul.hide();
    });

    //放大效果
    $('.resize_icon').on('click', function(){
        var contentBtmZIndex = $('.content_btm_zIndex');

	    if(contentBtmZIndex.hasClass("magnify")){
		    contentBtmZIndex.removeClass("magnify");
	    }else{
		    contentBtmZIndex.addClass("magnify");
	    }

	    setScroll();
    })
    
    //add to cart 
    
    //1.Recommended click
    $('.content_right_ul').on('click','a',function(e){
    	
    	var productId = $(e.currentTarget).attr('data-id');
    	var productName = $(e.currentTarget).html();
    	var title = $(".header_title").html();
        var index =$(e.currentTarget).parent('li').index() + 1;
    
		_paq.push(["trackEvent", title+" recommendedClick", "recommendedClick", productName,productId]);
	    
    	currentArr = {
       		productId : $(e.currentTarget).attr('data-id'),
       		productName : $(e.currentTarget).attr('title'),
       		num : 1,
       		index : index
       	};
    	
    	// before rules
    	if(!addToCartRules({ type : 'list' })){
    		return false;
    	}
    	
    	//add to cart ajax 
    	addToCartEvent({
    		productId : $(e.currentTarget).attr('data-id'),
    		imeiValue : $(e.currentTarget).attr('data-imei'),
    		imeiNum : $(e.currentTarget).attr('data-num')
    	});
    });
    
    $('.add_cart').on('click', function(e){
    	
    	var productInput = $( ".product_input" ).val().trim();
    	var productStoreId = $("#productStoreId").val();
		var productInputLen = productInput.length;
		 var newExp = /^[0-9]*$/;
	
    	// before rules
    	if(!addToCartRules({ type : "input" })){
    		return false;
    	}
    	
    	//if sku or model shuold choose sku else ean or imei no choose
    	if((productInputLen == 8 || productInputLen == 13) && newExp.test(productInput)){
    		
    		addEanToCart({
    			productStoreId : productStoreId,
				productInput : productInput
    		});
    		
    	}else if(productInputLen == 15 && newExp.test(productInput)){
    		
    		addImeiToCart({
				productStoreId : productStoreId,
				productInput : productInput,
                memberInput : $( ".Member_input" ).val(),
                phoneInput : $( ".phone_input" ).val(),
                addressId : $( ".address_input" ).attr('data-id')
			});
    		
    	}else{
   		    if($('.product_input').val() == '' || !$('.product_input').attr('data-id')){
   			    showErrorMessage('Please enter the correct product name or sku');

   			    return false;
   		    }
   		    
   	    	//add to cart ajax 
   	    	addToCartEvent({
   	    		productId : $( ".product_input" ).attr('data-id'),
   	    		imeiValue : $( ".product_input" ).attr('data-imei'),
   	    		imeiNum : $( ".product_input" ).attr('data-num')
   	    	}, e);
    	}
    });
    
 	// before rules
 	
 	function addToCartRules(option){
 		
 		var productStoreId = $("#productStoreId").val();
        var sellerSelect = $('.seller_select').val();
        var MemberInput = $('.Member_input').val();
        var buyerId = $( "#buyerId" ).val();
        var memberInput = $( ".Member_input" ).val();
        var phoneInput = $( ".phone_input" ).val();
        var addressId = $( ".address_input" ).attr('data-id');
    	
        var newExp = /^[0-9]*$/;

        var add_product_id = $('.product_input').attr('data-id');
        var needShipment = $('.need_shipment').val();
        
	    if(sellerSelect == '' && MemberInput == '' && phoneInput == ''){
			showErrorMessage('Please fill in sales person and customer information');
			
		    return false;
	    }

	    if(sellerSelect == ''){
			showErrorMessage('Please fill in sales person and customer information');
			
		    return false;
	    }
	    
	    if(MemberInput == '' || phoneInput == ''){
			showErrorMessage('Please fill in customer information');
			
		    return false;
	    }
	    
	    if(!newExp.test(phoneInput)){
			showErrorMessage('Custom phone incorrect');
			
    		return false;
    	}
	    
	    if($('.need_shipment').val() == 'Y'){
	    	if(!addressId){
	    		showErrorMessage('Please fill in address');
				
			    return false;
	    	}
	    }else if($('.need_shipment').val() == ''){
	    	showErrorMessage('Please select shipment');
			
		    return false;
	    }
	    
	    return true;
 	}
 	//add to cart ajax 
 	
 	function addToCartEvent(option, evt){
        //has atp
        if(option.imeiNum > 0){
        	//is imei
            if(option.imeiValue == 'Y'){
            	
                var enterImei = $('.enter_imei');
                var enterImeiInput = $('.enter_imei_input');
                
                enterImeiInput.val('');
                enterImei.find('.error_message').text('');
                enterImei.show();
                
                enterImei.find('.enter_imei_input').focus();
                
                //close dialog
                enterImei.off('click','.enter_imei_tit').on('click','.enter_imei_tit',function(){
                    enterImei.hide();
                });
                
                //error message change
                enterImei.find('.enter_imei_input').on('keyup', function(event){
                	var enterValue = enterImeiInput.val();
                	var newExp = /^[0-9]*$/;
                	
                	if(enterValue == '' || (enterValue+'').length != 15 || !newExp.test(enterValue)){
                		enterImei.find('.error_message').text('Please enter the correct scan / input IMEI code.');
                		
                		return false;
                	}else{
                		enterImei.find('.error_message').text('');
                		
                    	if(event.which == 13){
                    		addToCartAjax({
                                "productId":option.productId,
                                "imei": enterValue,
                    		});
                    		enterImei.hide();
                    	}
                	}
                	
                });

                //confirm
                enterImei.off('click','.confirm').on('click','.confirm',function(e){
                	var enterValue = enterImeiInput.val();
                	var newExp = /^[0-9]*$/;
                	
                	if(enterValue == '' || (enterValue+'').length != 15 || !newExp.test(enterValue)){
                		enterImei.find('.error_message').text('Please enter the correct scan / input IMEI code.');
                		
                		return false;
                	}else{
                		enterImei.find('.error_message').text('');
                		
                		addToCartAjax({
                            "productId":option.productId,
                            "imei": enterValue,
                		});
                		enterImei.hide();
                	}
                });
            }else if(option.imeiValue == 'N'){
            	addToCartAjax({
                    "productId":option.productId,
                    "imei": '',
            	}, evt);
            }
        }else{
        	showErrorMessage("Insufficient inventory for this product, please try other ones.");
        	
        	return false;
        }
 	}
 	
 	// ajax
 	
    function addToCartAjax(option, e){
	    e && $.showLoading(e.currentTarget);
	    
        jQuery.ajax({
            url:'/wpos/control/addToCart',
            type: 'post',
            dataType: 'json',
            contentType:"application/x-www-form-urlencoded",
            data:{
              "productStoreId":$("#productStoreId").val(),
              "add_product_id":option.productId,
              "imei": option.imei,
              "quantity":1,
              "orderId" :$('#orderId').val(),
              "sellerId" :$('.seller_select').val(),
              "buyerId" :$('#buyerId').val(),
              "firstName" : $( ".Member_input" ).val(),
              "contactNumber" : $( ".phone_input" ).val(),
              "contactMechId" : $( ".address_input" ).attr('data-id')
            },
            success: function(jsonObj){
                var dataResult = jsonObj;
                e && $.hideLoading(e.currentTarget);
                
                if(dataResult.responseMessage == "success"){
                	
                	if(clickArr.length == 0){
                		clickArr = clickArr.concat(currentArr);
                	}else{
                		$.each(clickArr, function(index, elem){
                			if(elem.productId != currentArr.productId){
                				clickArr = clickArr.concat(currentArr);
                			}
                		});
                	}
                	
                    $('.product_input').val('');
                    $('.product_input').attr('data-id','');

                    var list_tab_content_detail = $('.list_tab_content_detail');
                    list_tab_content_detail.html('').append(resetContent(dataResult.cart));

	                setScroll();
                }else if(dataResult.responseMessage == "error"){
                	if(dataResult.errCode && dataResult.errCode == 100){
                		showErrorMessage(dataResult.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(dataResult.errorMessage);
                	}
                }
            },
            error : function(data, status){
            	e && $.hideLoading(e.currentTarget);
            }
        })
    }
 	
 	// product look 
 	
    $( ".product_input" ).keyup(function(event){
    	event.preventDefault();
    	event.stopPropagation();
    	
    	var keyCode = event.which;
    	var productStoreId = $("#productStoreId").val();
        var memberInput = $( ".Member_input" ).val();
        var phoneInput = $( ".phone_input" ).val();
        var addressId = $( ".address_input" ).attr('data-id');
    	var productInput = $( ".product_input" ).val().trim();
    	var productInputLen = productInput.length;
    	var product_ul = $('.product_ul');
    	var newExp = /^[0-9]*$/;
    	
    	if((productInputLen == 8 || productInputLen == 13) && newExp.test(productInput) && keyCode == 13){
   			clearTimeout(timeOut);
   			addEanToCart({
   				productStoreId : productStoreId,
   				productInput : productInput
   			});
    	}else if((productInputLen == 15) && newExp.test(productInput) && keyCode == 13){
   			clearTimeout(timeOut);
   			addImeiToCart({
   				productStoreId : productStoreId,
   				productInput : productInput,
                   memberInput : memberInput,
                   phoneInput : phoneInput,
                   addressId : addressId
   			});
    	}else{
    		addToCart();
    	}
    });
 	
 	function addImeiToCart(option){
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: "/ordermgr/control/new_gwtSuggestImeiForCart",
            data: { "productStoreId":option.productStoreId, query: option.productInput },
            success: function(data) {
                if(data.responseMessage == 'success'){
                	var dataList = data.result[0];
                	if(dataList.atp == 1){
                		
                        jQuery.ajax({
                            url:'/wpos/control/addToCart',
                            type: 'post',
                            dataType: 'json',
                            contentType:"application/x-www-form-urlencoded",
                            data:{
                              "productStoreId":option.productStoreId,
                              "add_product_id":dataList.productId,
                              "imei":option.productInput,
                              "quantity":1,
                              "orderId" :$('#orderId').val(),
                              "sellerId" :$('.seller_select').val(),
                              "buyerId" :$('#buyerId').val(),
                              "firstName" : option.memberInput,
                              "contactNumber" : option.phoneInput,
                              "contactMechId" : option.addressId
                            },
                            success: function(jsonObj){
                           	 
                                if(jsonObj.responseMessage == "success"){
                                    $('.product_input').val('');

                                    var list_tab_content_detail = $('.list_tab_content_detail');
                                    list_tab_content_detail.html(resetContent(jsonObj.cart));

                	                setScroll();
                                }else if(jsonObj.responseMessage == "error"){
                                	showErrorMessage(jsonObj.errorMessage);
                                }
                            }
                        })
                	}else{
                		showErrorMessage('This IMEI code is unavailable, please try other ones.');
                	}
                }else if(data.responseMessage == "error"){
                    if(data.errCode && data.errCode == 100){
                		showErrorMessage(data.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(data.errorMessage);
                	}
                }
            }
        });
 	}
 	
 	function addEanToCart(option){
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: "/ordermgr/control/new_gwtSuggestProductForCart",
            data: {
                start: '0',
                limit: '16',
                filterCol: 'text',
                sort: 'productId',
                dir: 'ASC',
                storeId : option.productStoreId,
                query: option.productInput
            },
            success: function(data) {
                if(data.responseMessage == 'success'){
                	if(data.items && data.items.length > 0){
                		addToCartEvent({
                    		productId : data.items[0].id,
                    		imeiValue : data.items[0].isImei,
                    		imeiNum : data.items[0].availableToPromiseTotal
                		});
                		
                		return false;
                	}else{
                		showErrorMessage('This EAN code no fund.');
                	}
                }else if(data.responseMessage == "error"){
                    if(data.errCode && data.errCode == 100){
                		showErrorMessage(data.errorMessage, function(){
                			location.reload();
                		});
                	}else{
                		showErrorMessage(data.errorMessage);
                	}
                }
            }
        });
 	}
 	
 	function addToCart(){
 		
    	var productInput = $( ".product_input" ).val().trim();
    	var product_ul = $('.product_ul');
    	
       	if(productInput == ''){
       		clearTimeout(timeOut);
       		product_ul.hide();
       		
       		return false;
       	}
       	clearTimeout(timeOut);
       	
       	
       	timeOut = setTimeout(function(){
           $.ajax({
               type: 'post',
               dataType: 'json',
               url: "/ordermgr/control/new_gwtSuggestProductForCart",
               data: {
                   start: '0',
                   limit: '16',
                   filterCol: 'text',
                   sort: 'productId',
                   dir: 'ASC',
                   storeId : $("#productStoreId").val(),
                   query: productInput
               },
               success: function(data) {
                   if(data.responseMessage=="success"){
                   		if(data.items.length == 0){
                   			product_ul.hide();
                   		
                   			return false;
                   		}else{
                   			myAutoComplete_product(data.items);
                   		}
                   }else if(data.responseMessage=="error"){
                   		if(data.errCode && data.errCode == 100){
                   			showErrorMessage(data.errorMessage, function(){
                   				location.reload();
                   			});
                   		}else{
                   		showErrorMessage(data.errorMessage);
                   		}
                	}
               }
           }) 
       	} ,timer);  
 	}
    
	function myAutoComplete_product(dataOption){
    	
    	var product_ul = $('.product_ul');
    	var productInput = $( ".product_input");
    	
    	var dataList = dataOption;
    	var htmlContent = '';
    	var currentIndex = 0;
    	
    	dataList = dataList.sort(function(bf, af){
    		if(bf['id'].split('-')[0] > af['id'].split('-')[0]){
    			return 1;
    		}else if(bf['id'].split('-')[0] == af['id'].split('-')[0]){
    			
    			if(bf.id < af.id){
    				if(Number(bf.availableToPromiseTotal) > 0){
    					return -1;
    				}else if(bf.availableToPromiseTotal == af.availableToPromiseTotal){
    					return -1;
    				}else{
    					return 1;
    				}
    			}else{
    				if(Number(af.availableToPromiseTotal) > 0){
    					return 1;
    				}else if(bf.availableToPromiseTotal == af.availableToPromiseTotal){
    					return 1;
           			}else{ 
           				return -1;
           			}
    			}
			}else{
				return -1;
			}
    	});
    	
    	$.each(dataList,function(index, item){
    		var value = item.id + ':' +item.text;
    		var id = item.id;
    		var availableToPromiseTotal = item.availableToPromiseTotal;
    		
    		htmlContent += '<li data-id = "'+ id +'" data-imei="'+ item.isImei +'" data-num="'+ availableToPromiseTotal +'" data-value = "'+ value +'"><span class="inventory_list">'+ availableToPromiseTotal +'</span>'+ value +'</li>'
    	});
    	
    	product_ul.html(htmlContent).show().focus();
    	
    	product_ul.find('li').eq(currentIndex).addClass('cur');
    	
    	product_ul.find('li').click(function(){
    		var _this = $(this);
    		var dataId = _this.attr('data-id');
    		var dataValue = _this.attr('data-value');
    		var dataImei = _this.attr('data-imei');
    		var dataNum = _this.attr('data-num');
    		
    		productInput.val(dataValue);
    		productInput.attr("data-id",dataId);
    		productInput.attr("data-imei",dataImei);
    		productInput.attr("data-num",dataNum);
    		
    		product_ul.hide();
    	});
    	
    	product_ul.keyup(function(e){
    		var keyCode = e.keyCode;
    		var length = product_ul.find('li').length - 1;
    		if(keyCode == 40){
    			if(currentIndex != length){
    				currentIndex++;
    				product_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				product_ul.find('li').siblings().removeClass('cur').eq(length).addClass('cur');
    			}
    		}else if(keyCode == 38){
    			if(currentIndex != 0){
    				currentIndex--;
    				product_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				product_ul.find('li').siblings().removeClass('cur').eq(0).addClass('cur');
    			}
    		}else if(keyCode == 13){
    			product_ul.find('li.cur').click();
    		}
    	});
    }
 	
    function hasStoreManageEvt(){
        jQuery.ajax({
            url:"/warehouse/control/checkUserHasStoreManage",
            type: 'post',
            dataType: 'json',
            data:{},
            success: function(res){
                if(res.resultCode == 1){
                    if(res.isStoreManage == 'N'){
                    	$('.receiveContent') && $('.receiveContent').css('display', 'none');
                    }else{
                    	$('.receiveContent') && getReceiveEvt();
                    }
                }
            }
         });
    }
    
    function getReceiveEvt(){
        jQuery.ajax({
            url:"/warehouse/control/queryTransferData",
            type: 'post',
            dataType: 'json',
            data:JSON.stringify({"type":"receive","facilityId":"","productId":"","model":"","sendDate":"","statusId":"IXF_REQUESTED","pageNum":"1"}),
            success: function(res){
                if(res.resultCode == 1){
                    if(res.totalNum > 0){
                    	$('.receiveContent').text(res.totalNum);
                    	$('.receiveContent').css('display', 'inline-block');
                    }else{
                    	$('.receiveContent').text('');
                    	$('.receiveContent').css('display', 'none');
                    }
                }
            }
         });
    }
});
</script>
<script>
	jQuery(function(){
		//显示修改密码----优化之后引用jquery
        var userLoginId = jQuery('.user_login');
        var menuList = jQuery('.menuList');
        var editPasswordDialog = jQuery('#edit_password_dialog');
        var editPasswordIcon = jQuery('.edit_password_icon');
        var save = jQuery('.save');
        var yiwillHeaderLogin = $('.yiwill_header_login');
        
        userLoginId.on('click', function(e){
        	e.stopPropagation();
        	e.preventDefault();
        	menuList.toggle();
        });
        
        menuList.find('a[data-type="password"]').on('click', function(){
        	editPasswordDialog.show();
        });
        
        editPasswordIcon.on('click', function(){
        	editPasswordDialog.hide();
        	menuList.hide();
        });
        
        jQuery(document).click(function(){ 
        	menuList.hide(); 
        }); 
        
        save.on('click', function(){
        	
       		var userLoginId = jQuery('.user_name').val();
       		var partyId = jQuery('.user_id').val();
       		var currentPassword = jQuery('.original_password').val();
       		var newPasswordVerify = jQuery('.repeat_password').val();
       		var newPassword = jQuery('.new_password').val();
       		var error_tips_span = jQuery('.edir_password_error_tips span');
       		
       		if(currentPassword == ''){
       			error_tips_span.text('Please enter original password!');
       			return false;
       		}
       		if(newPassword == ''){
       			error_tips_span.text('Please enter new password!');
       			return false;
       		}
       		if(newPasswordVerify != newPassword){
       			error_tips_span.text('Repeat password incorrect!');
       			return false;
       		}
       		
            jQuery.ajax({
                url:"/partymgr/control/newUpdatePassword",
                type: 'post',
                dataType: 'json',
                data:{
                	userLoginId : userLoginId,
                	partyId:partyId,
                	currentPassword:currentPassword,
                	newPassword:newPassword,
                	newPasswordVerify:newPasswordVerify,
                	passwordHint:null	
                },
                contentType:"application/x-www-form-urlencoded",
                success: function(jsonObj){
                    var dataResult = jsonObj;
                    if(dataResult.responseMessage == "success"){
                        location.reload();
                    }else if(dataResult.responseMessage == "error"){
                    	error_tips_span.text(dataResult.errorMessage);
                    }
                }
             });
        });
	})
</script>