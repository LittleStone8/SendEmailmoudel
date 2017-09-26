<#if security.hasPermission("ORDERMGR_CREATE", session)>
<link href="/commondefine_css/jQuery/jquery-ui.min.css" rel="stylesheet">
<div id="create_order">
	<input type="hidden" id="uomid" />
	<input type="hidden" id='buyerId' />
    <input type="hidden" id="orderId" />
    <input type="hidden" id="addressInfo" />
	<div class="create_order_content_wap">
		<div class="create_order_content">
			<div class="create_order_baseInfo">
				<div class="create_order_baseInfo_list">
					<div>
						<div class="choose_store_group">
							<select class="choose_store" name="choose_store">
								<option value="">Select Store</option>
							</select>
						</div>
						<div class="seller_channel_group">
							<select class="seller_channel" name="seller_channel">
								<option value="">Select Channel</option>
							</select>
						</div>
						<div class="seller_person_group">
							<select class="seller_person" name="seller_person">
								<option value="">Select Salesperson</option>
							</select>
						</div>
						<div class="pay_way_group">
							<select class="pay_way" name="pay_way">
								<option value="">Select Payment Method</option>
								<option value="CASH">Cash</option>
							</select>
						</div>
						
						<div class="fix_order_group">
							<span>Fix Order</span>
							<select class="fix_order" name="fix_order">
								<option value="Y">Y</option>
								<option value="N" selected="selected">N</option>
							</select>
						</div>
						<div class="fix_date_group">
							<span>Fix Date</span>
							<input class="fix_date laydate-icon"  id="fixDate">
						</div>
						<div style="clear:both;"></div>
					</div>
					<div>
						<div class="choose_custom_group input_group">
							<input class="choose_custom" type="text" name="choose_custom" placeholder="Customer Name"/>
							<ul class="Member_ul" style="display:none;" tabindex="11" ></ul>
						</div>
						<div class="order_phone_group input_group">
							<input class="order_phone" type="text" name="order_phone" placeholder="Customer Phone"/>
							<ul class="Phone_ul" style="display:none;" tabindex="12" ></ul>
						</div>
						<div class="address_group input_group">
							<input class="address" type="text" name="address" placeholder="Customer Address"/>
							<ul class="Address_ul" style="display:none;" tabindex="13" >
								<li class="addNewAddress">+Add New Address</li>
							</ul>
						</div>
						
						<div class="need_shipment_group">
							<select class="need_shipment" name="need_shipment">
								<option value="">Select Shipping</option>
								<option value="Y">Need Shipping</option>
								<option value="N" selected="selected">Without Shipping</option>
							</select>
						</div>
						<div style="clear:both;"></div>
					</div>
				</div>
				<div class="error_tip_message"></div>
			</div>
			<div class="create_order_detail">
				<div class="create_order_detail_header">
					<div class="create_order_detail_title">
						Input Product Information
					</div>
				</div>
				<div class="create_order_detail_list">
					<div class="content_top_zIndex">
						<div class="input_group">
							<input class="product_input" type="text" placeholder="Product ID / Model / EAN / IMEI" />
							<a href="javascript:void(0);" class="add_cart">Add To</a>
							<ul class="product_ul" style="display:none;" tabindex="22"></ul>
						</div>
						<div class="input_group" id="showImei" hidden>
							<input class="imei_input" type="text" placeholder="IMEI" />
							<a href="javascript:void(0);" class="add_imei_cart">Add To</a>
						</div>
						<a href="javascript:void(0);" class="batch_add_imei">+Batch Add IMEI Product</a>
						<div class="cart_total">
						    Total Amount: <span class="payment_money"><em class="currency"></em> <em class="all_money">0</em></span>
						</div>
					</div>

                    <div class="content_btm_zIndex">
                        <div class="list_tab_content">                       
                        </div>
                        <div style="clear:both"></div>
                    </div>
				</div>
			</div>
			<div class="create_order_create">
				<a href="javascript:void(0);" class="create_order_btn no_pay_finish">Submit Order</a>
			</div>
		</div>
	</div>
</div>
<div class="open_new_member" >
	<div class="fixed_screen"></div>
	<div class="add_new_member" id="openMember">
		<div class="new_member_icon"></div>
		<div class="new_member_title">New Customer</div>
		<div class="new_member_content">
            <div class="input_group">
                <span>Delivery Address</span>
                <select class="address_select_province" name="address_select_province">
                    <option value="">Select Province</option>
                </select>
                <span class="addrenss_loading"></span>
                <select class="address_select_city" name="address_select_city">
                    <option value="">Select City</option>
                </select>
                <span class="addrenss_loading"></span>
                <select class="address_select_area" name="address_select_area">
                    <option value="">Select Area</option>
                </select>
                <span class="addrenss_loading"></span>
            </div>
            <div class="input_group">
                <span>Street Address</span>
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
<div class="open_imei_dialog">
    <div class="fixed_screen"></div>
    <div class="open_imei_dialog_box">
        <div class="open_imei_dialog_title">Batch Add Product<span class="imei_dialog_delete"></span></div>
        <div class="open_imei_dialog_content">
            <div class="imei_dialog_scan">
	            <div>Scan the Two-Dimension Code of the Batch</div>
	            <input type="text" class="imeis"/>
	            <div class="imeis_vertify"></div>
            </div>            
            <div class="imei_dialog_content">
                <div class="imeis_content_list">
            	   <div class="imeis_content_product">
            	      <div class="imeis_content_product_title">Batch Information</div>
            	      <div class="imeis_content_product_group clearfix">
            	          <div class="group_text">
            	             <span>Product ID:</span><span class="imei_productid"></span>
            	          </div>
            	          <div class="group_text">
            	             <span>Product Description:</span><span  class="imei_desc"></span>
            	          </div>
            	      </div>
            	   </div>
            	   <div class="imeis_content_desc clearfix">
            	       <table class="imei_table">
            	         <thead>
	            	         <tr>
		            	          <th style="width:50px;">No</th><th style="width:150px;">IMEI</th><th style="width:100px;">Status</th>
		            	          <th style="width:50px;">No</th><th style="width:150px;">IMEI</th><th style="width:100px;">Status</th>
	            	          </tr>  
            	         </thead>
            	         <tbody id="imei_table_tbody">
            	         </tbody>
            	                    	         
            	       </table>            	      
            	   </div>
            	   <div class="imeis_content_total">
            	   	  <div class="clearfix imeis_total">
	            	   	  <div class="imeis_available"><span>Available:</span>&nbsp;&nbsp;<span class="imeis_available_nums"></span>&nbsp;&nbsp;pcs</div>
	            	      <div class="imeis_unavailable"><span class="imeis_unavale">Unavailable:</span>&nbsp;&nbsp;<span class="imeis_unavailable_nums"></span>&nbsp;&nbsp;pcs</div>
            	   	  </div>            	      
            	      <div class="imeis_tips">*Tips:<span class="tips_red">Unavailable</span> means this product cannot be sold</div>
            	   </div>
	               <div class="open_imei_dialog_operator ">
				        <div class="clearfix dialog_btn">
				            <div class="operator_vertify"></div>
				        	<a href="javascript:void(0);" class="btn_next disabled" data-type="addCartContinue">Add &nbsp;to &nbsp;Cart &nbsp;and &nbsp;Continue</a>
				            <a href="javascript:void(0);" class="btn_next_close disabled" data-type="addCartClose">Add &nbsp;to &nbsp;Cart &nbsp;and &nbsp;&nbsp;&nbsp;Close</a>
				            <a href="javascript:void(0);" class="btn_clear disabled" data-type="clear">Clear All</a>
				        </div>            
			        </div>
            	</div>
            	
            </div>
            
        </div>
        <div class="open_imeis_loading">
           <div class="imeis_screen"></div>
           <div class="imeis_loading"></div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/commondefine_js/jQuery/jquery-ui.min.js"></script>
<script type="text/javascript" src="/commondefine_js/layDate/laydate.min.js"></script>
<script>
    $(document).ready(function(){
    	var timeOut = null;
    	var timer = 1000;
    	if(showImei == "Y"){
    		$("#showImei").show();
    	}
    	
    	$('.fix_order').on('change', function(){
    		var _this = $(this);
    		var fixDateGroup = $('.fix_date_group');
    		
    		if(_this.val() == 'Y'){
    			fixDateGroup.show();
    		}else{
    			fixDateGroup.hide();
    		}
    	});
    	
    	$('#fixDate').on('click', function(e){
    		var timeZones = $('#timeNow').val() - 0;
			laydate({
				eventArg: e,
				istime: false,
				istoday: false,
				issure: false,
				isclear: false,
				format: 'DD/MM/YYYY',
				min: new Date(timeZones - 1000*60*60*24*30),
				max: new Date(timeZones)
			});
         });

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
    	        		var needShipment = $('.need_shipment');
    	        		var needShipmentGroup = $('.need_shipment_group');
    	        		var uomId = $('#uomid');
    	        		if(dataResult.isShip == 'N'){
    	        			needShipment.attr('disabled', true);
    	        			needShipment.find('option[value="N"]').attr('selected', true);
    	        			needShipmentGroup.hide();
    	        		}else if(dataResult.isShip == 'Y'){
    	        			needShipmentGroup.show();
    	        		}
    	        		currency.html(dataResult.uomId);
    	        		uomId.val(dataResult.countryId);
    	        		uomId.attr('data-countryName', dataResult.countryName);
    	        	}else if(data.responseMessage == 'error'){
                    	showErrorMessage(data.errorMessage);
                    }
    	       	}	
    		});
    	}
    	initTableHead();
    	findPriceUOM();
        function initTableHead(type){
        	var str='<div class="list_tab_content_head">'+
        	'<span>No</span>'+
            '<span>Product ID</span>'+
            '<span class="itemDescriptionSpan">Product Description</span>'+
            '<span>Quantity</span>'+
            '<span>IMEI</span>'+
            '<span>Unit Price(<em class="currency"></em>)</span>'+
            '<span class="priceSpan">Total Amount(<em class="currency"></em>)</span>'+
            '<span class="priceSpan">Adjust Amount(<em class="currency"></em>)</span>'+
            '<span class="editSpan">Edit</span></div><div class="list_tab_content_detail"></div>';
            $('.list_tab_content').html(str);
        }
        
        function showErrorMessage(message, callBack){
            var errorTips = jQuery('.error_tips');
            errorTips.find('.error_tips_content span').html('').html(message);
            errorTips.show();

	        errorTips.find('.error_tips_operator').off('click','a').on('click','a',function () {
		        $('.error_tips').hide();
		        callBack && callBack();
	        })
        }
  
        function parseNumberList(option){
            return parseInt(option, 10);
        }

        $.ajax({
            type: 'post',
            dataType: 'json',
            url: '/ordermgr/control/findSalesStore',
            data: {},
            success: function (data) {
                var dataResult = data;
                if(dataResult.responseMessage == 'success'){
                    if(dataResult.store){
                        var chooseStore = $('.choose_store');
                        chooseStore.children().not(':first-child').remove();
                        var contentOption = '';

                        if(dataResult.store.length > 0){
                            $.each(dataResult.store,function(key,value){
                                contentOption +="<option value='"+value.productStoreId+"' data-issupportaccount='"+ value.issupportaccount +"' >"+value.storeName+"</option>";
                            });
                        }
                        chooseStore.append(contentOption);
                    }
                }else if(dataResult.responseMessage == 'error'){
                	showErrorMessage(data.errorMessage);
                }
            }
        });

        $('.choose_store').on('change', function(){
            var _this = $(this);

            $('.seller_person').children().not(':first-child').remove();
            $('.seller_channel').children().not(':first-child').remove();
            $('.pay_way option:gt(1)').remove();

            if(_this.val() == ''){
                return false;
            }

            getSaleMens(_this.val());
	        findsalechannleByStoreId(_this.val());
	        findPayWayByStoreId(_this.find('option:selected').attr('data-issupportaccount'));
			getStoreAddressInfo(_this.val());
        });
        
        //get store address
        function getStoreAddressInfo(option){
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
        
        //get cash
        function findPayWayByStoreId(option){
        	if(option == 'Y'){
        		$('.pay_way').append('<option value="CREDIT">Credit</option>');
        	}
        }

	    function findsalechannleByStoreId(option){

		    $.ajax({
			    type: 'post',
			    dataType: 'json',
			    url: '/ordermgr/control/findsalechannleByStoreId',
			    data: {
				    productStoreId : option
			    },
			    success: function (data) {
				    var dataResult = data;
				    if(dataResult.responseMessage == 'success'){
					    if(dataResult.reslutList){
						    var SalesChannels = $('.seller_channel');
						    SalesChannels.children().not(':first-child').remove();
						    var contentOption = '';
	
						    $.each(dataResult.reslutList, function(i,item){
							    contentOption+="<option value='"+item.id+"' >"+item.name+"</option>";
						    });
	
						    SalesChannels.append(contentOption);
					    }
				    }else if(dataResult.responseMessage == 'error'){
	                	showErrorMessage(data.errorMessage);
	                }
			    }
		    });

	    }

	    
        function getSaleMens(option){
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: '/ordermgr/control/findStoreSaleMens',
                data: {
                    storeId:option
                },
                success: function (data) {
                    var dataResult = data;
                    if(dataResult.responseMessage == 'success'){
	                    if(dataResult.storeSaleMens){
	                        var sellerPerson = $('.seller_person');
	                        sellerPerson.children().not(':first-child').remove();
	                        var contentOption = '';
	
	                        $.each(dataResult.storeSaleMens, function(i,item){
	                            contentOption+="<option value='"+item.id+"' >"+item.des + ' - Sales Person'+"</option>";
	                        });
	
	                        sellerPerson.append(contentOption);
	
	                    }
                    }else if(dataResult.responseMessage == 'error'){
	                	showErrorMessage(data.errorMessage);
	                }
                }

            });
        }

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
                            countryContent+= '<option value="' + elem.id + '" data-name="'+elem.name+'">'+ elem.name +'</option>';
                        });
                        elem.children().not(':first-child').remove();
                        elem.append(countryContent);
                        elem.removeClass("loading").removeAttr("disabled").next(".addrenss_loading").hide();
                        
                        callback && callback();

                    }else if(dataResult.responseMessage == "error"){
                    	elem.removeClass("loading").removeAttr("disabled").next(".addrenss_loading").hide();
						showErrorMessage(data.errorMessage);
                    }
                }
            });

        }
        function initOpenMemberWH(){
        	var bodyW = $(window).width();
            var bodyH = $(window).height();
            
            var elW =  $("#openMember").width();
            var elH =  $("#openMember").height();
            $("#openMember").css("left",(bodyW - elW)/2 + 'px');
           	$("#openMember").css("top",(bodyH - elH)/2 + 'px');
        }
        
        $('.Address_ul').on('click', 'li', function(){
        	var _this = $(this);
        	var choose_custom = $('.choose_custom');
        	var order_phone = $('.order_phone');
        	var newExp = /^[0-9]*$/;
        	
        	if(choose_custom.val() == ''){
        		
        		$('.error_tip_message').text('Custom name not null');
        		$('.Address_ul').hide();
        		return false;
        	}
        	if(order_phone.val() == ''){
        		
        		$('.error_tip_message').text('Custom phone not null');
        		$('.Address_ul').hide();
        		return false;
        	}else if(!newExp.test(order_phone.val())){
        		
        		$('.error_tip_message').text('Custom phone incorrect');
        		$('.Address_ul').hide();
        		return false;
        	}else{
        		$('.error_tip_message').text('');
        	}
        	
        	$('.Address_ul').hide();
        	if(_this.attr('data-id')){
        		
        		$('.address').val(_this.text());
        		$('.address').attr('data-id', _this.attr('data-id'));
        		
        		return false;
        	}
        	
        	var countryId = $('#uomid').val();
            var countryName = $('#uomid').attr('data-countryname');
            var addressInfo = $('#addressInfo');
            
            var openNewMember = $('.open_new_member');

            initOpenMemberWH();
            openNewMember.show();
            $("#openMember .error_info").each(function(){
        		$(this).text('');
        	})
            $("#openMember").draggable({
            	cursor:'move',
            	handle:'.new_member_title',
            	containment: 'window'
            });
           
            var addressInput = $('.address_detail');

            addressInput.val('');

            $('.address_select_province').children().not(':first-child').remove();
            $('.address_select_city').children().not(':first-child').remove();
            $('.address_select_area').children().not(':first-child').remove();

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

            openNewMember.off('click','.new_member_icon').on('click','.new_member_icon',function(){
                openNewMember.hide();
            });

            openNewMember.off('click','.cancel').on('click','.cancel',function(){
                openNewMember.hide();
            });

            openNewMember.off('click','.confirm').on('click','.confirm',function(e){
            	var _this = $(this); 
            	
            var address_select_country = countryId;
            var partyId = $('#buyerId').val();
            var address_select_province = $('.address_select_province').val();
            var address_select_city = $('.address_select_city').val();
            var address_select_area = $('.address_select_area').val();
            var address_detail = $('.address_detail').val();
            var phoneNumber = $('.order_phone').val();
        	var MemberInput = $('.choose_custom').val();
	        
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
                            var checkedProvince = $('.address_select_province option:selected').attr('data-name');
                            var checkedCity = $('.address_select_city option:selected').attr('data-name');
                            var checkedArea = $('.address_select_area option:selected').attr('data-name');
                            var addressStr = '';
	                        
	                        addressStr += checkedCountry + ' ' + checkedProvince + ' ' + checkedCity + ' ' + checkedArea + ' ' + address_detail;
	                        
	                        $('.address').val(addressStr);
	                        $('.address').attr('data-id', dataResult.data.contactMechId);
	                        $('#buyerId').val(dataResult.data.partyId);
	                        $('.Address_ul').append('<li data-id="'+ dataResult.data.partyId +'">'+ addressStr +'</li>');

                            openNewMember.hide();

                        }else{
                            showErrorMessage(dataResult.message);
                        }
                    },
                    error : function(option, status){
                    	$.hideLoading(e.currentTarget);
                    }
                });
            });

        });

		function myAutoComplete(dataOption){
        	
        	var Member_ul = $('.Member_ul');
        	var Address_ul = $('.Address_ul');
        	var Phone_ul = $('.Phone_ul');
            var chooseCustom = $( ".choose_custom" );
            var address = $( ".address" );
            var orderPhone = $( ".order_phone" );
            var buyerId = $( "#buyerId" );
            var address = $('.address');
            
        	var dataList = dataOption;
        	var htmlContent = '';
        	var currentIndex = 0;
        	
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
        		
                address.val('');
                orderPhone.val('');
        		
        		var dataId = _this.attr('data-id');
        		var dataValue = _this.attr('data-value');
        		var dataPhone = _this.attr('data-phone');
        		
        		buyerId.val(dataId);
        		chooseCustom.val(dataValue);
        		orderPhone.val(dataPhone);
        		Member_ul.hide();
        		
        		$.each(dataList, function(index, elem){
        			if(elem.partyId == dataId){
        				var addressList = elem.customerAddress, str='';
        				var countryName = $('#uomid').attr('data-countryname');
        					Address_ul.children().not(':first-child').remove();
        					
        				if(addressList && addressList.length > 0){
        					$.each(addressList, function(addressIndex, addressElem){
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
        
        $( ".choose_custom" ).keyup(function(event){
        	event.preventDefault();
        	event.stopPropagation();
        	
        	var chooseCustom = $( ".choose_custom" ).val();
        	var PhoneInput = $( ".order_phone" ).val();
            var address = $('.address');
        	var Member_ul = $('.Member_ul');
        	
        	if(chooseCustom == ''){
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
                    	userName: chooseCustom
                    }),
                    success: function(data) {
                    	if(data.success){
                        	$( "#buyerId" ).val('');
                        	if(data.data && data.data.length > 0){
                        		myAutoComplete(data.data);
                        	}else{
                        		address.val('');
                        		address.attr('data-id', '');
                        		//PhoneInput.val('');
                        	}
                        }else{
		                	showErrorMessage(data.errorMessage);
		                }
                    }
                })
        	} ,timer); 
        });
        
    	function myAutoComplete_phone(dataOption){
        	
        	var Phone_ul = $('.Phone_ul');
        	var Address_ul = $('.Address_ul');
            var MemberInput = $( ".choose_custom" );
            var phoneInput = $( ".order_phone" );
            var buyerId = $( "#buyerId" );
            var address = $('.address');
            
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
       			address.val('');
       			
        		var dataId = _this.attr('data-id');
        		var dataValue = _this.attr('data-value');
        		var dataMemeber = _this.attr('data-memeber');

        		
        		buyerId.val(dataId);
        		phoneInput.val(dataValue);
        		MemberInput.val(dataMemeber);
        		Phone_ul.hide();
        		
        		$.each(dataList, function(index, elem){
        			if(elem.partyId == dataId){
        				var addressList = elem.customerAddress, str='';
        				var countryName = $('#uomid').attr('data-countryname');
    						Address_ul.children().not(':first-child').remove();

   	    				if(addressList && addressList.length > 0){
   	    					$.each(addressList, function(addressIndex, addressElem){
   	    						var detailaAddress = (countryName+ ' ' + addressElem.stateProvinceGeoId + ' ' + addressElem.city + ' ' + addressElem.countyGeoId + ' ' + addressElem.address1);
   	    						str+='<li data-id="'+ addressElem.contactMechId +'">'+ detailaAddress +'</li>'
   	    					})
   	    					Address_ul.append(str);
   	    				}
        			}
        		});

        	});
        }
    	
    	//地址选择-----address
    	
    	$('.address').on('focus', function(){
    		var Address_ul = $('.Address_ul');
    		
    		if(!$('#buyerId').val()){
    			Address_ul.children().not(':first-child').remove();
    		}
    		
    		Address_ul.show().focus();
    	});
        
    	//电话选择----phone
    	
    	$(".order_phone").keyup(function(event){
        	event.preventDefault();
        	event.stopPropagation();
        	
        	var PhoneInput = $( ".order_phone" ).val();
        	var MemberInput = $( ".choose_custom" );
            var address = $('.address');
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
        		$( "#buyerId" ).val('');
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
                        		address.val('');
                        		address.attr('data-id', '');
                        		//MemberInput.val('');
                        	}
                        }else{
    	                	showErrorMessage(data.errorMessage);
    	                }
                    }
                })
        	} ,timer); 
        });
    	
    	//鼠标移出
     	
    	$(".Phone_ul").on('blur', function(){
    		$(".Phone_ul").hide();
        });
    	$(".Address_ul").on('blur', function(){
    		$(".Address_ul").hide();
        });
        
        function baseInformationToggle(option){
        	
        	var chooseStore = $('.choose_store');
        	var sellerChannel = $('.seller_channel');
        	var payWay =  $(".pay_way");
        	
        	chooseStore.attr('disabled', true).css('background','#EEEEEE');
        	sellerChannel.attr('disabled', true).css('background','#EEEEEE');
        	payWay.attr('disabled', true).css('background','#EEEEEE');
        	
        }

        $('.list_tab_content').on('blur','.enter_number',function(){
        	
        	var _this = this;

            var productStoreId = $(".choose_store").val();
            var quantity = jQuery(_this).val();
            var oldQuantity = parseNumberList(jQuery(_this).parent().siblings('.enter_span').text());
            var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
            var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');
            var newErp = /^([1-9]\d*|0)$/;

            if(!newErp.test(Number(quantity))){
                showErrorMessage('Please enter the correct number');
                jQuery(_this).val(parseNumberList(jQuery(_this).parent().siblings('.enter_span').text()));

                return false;
            }else{
	            jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(quantity))
            }

            updateNumber({
              "productStoreId":productStoreId,
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "value":quantity,
              "type":"quantity",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart',{
            	_this : _this,
            	type : 'Quantity',
            	oldQuantity : oldQuantity
            });
        });

        $('.list_tab_content').on('click','.del',function(){

            var _this = this;
	        if(parseNumberList(jQuery(_this).siblings('.enter_number').val()) <= 1){
		        return false;
	        }
            jQuery(_this).siblings('.enter_number').val(parseNumberList(jQuery(_this).siblings('.enter_number').val()) - 1);
            jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(jQuery(_this).siblings('.enter_number').val()));

            var productStoreId = $(".choose_store").val();
            var quantity = parseNumberList(jQuery(_this).siblings('.enter_number').val());
            var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
            var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');

            updateNumber({
              "productStoreId":productStoreId,
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "value":quantity,
              "type":"quantity",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart');
        });

        $('.list_tab_content').on('click','.add',function(){

            var _this = this;
            var oldQuantity = parseNumberList(jQuery(_this).parent().siblings('.enter_span').text());
            
            jQuery(_this).siblings('.enter_number').val(parseNumberList(jQuery(_this).siblings('.enter_number').val()) + 1)
            jQuery(_this).parent().siblings('.enter_span').text(parseNumberList(jQuery(_this).siblings('.enter_number').val()))

            var productStoreId = $(".choose_store").val();
            var quantity = parseNumberList(jQuery(_this).siblings('.enter_number').val());
            var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
            var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id');

            updateNumber({
              "productStoreId":productStoreId,
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "value":quantity,
              "type":"quantity",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart',{
            	_this : _this,
            	type : 'add',
            	oldQuantity : oldQuantity
            });
        });

        $('.list_tab_content').on('click','.delete_div',function(){
             
            var _this = this;
	        if($(_this).hasClass('delete_finish_div')){
		        $(_this).removeClass('delete_finish_div');
	        }else{
		        return false;
	        }

            var productStoreId = $(".choose_store").val();
            var orderItemSeqId = jQuery(_this).closest('.list_tab_content_div').attr('data-orderItemSeqId');
            var orderId = jQuery(_this).closest('.list_tab_content_div').attr('data-id'); 
            var isImeiProduct=$(this).attr('data-type');
            updateNumber({
              "productStoreId":productStoreId,
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "type":"del",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart',{
            	_this : _this,
            	type : 'delete'
            },isImeiProduct);
        });

        $('.list_tab_content').on('blur','.enter_input',function(){
            
            var _this = this;

            var productStoreId = $(".choose_store").val();
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
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "value":quantity,
              "type":"price",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart',{},'product');
        });
        
        $('.list_tab_content').on('blur','.enter_price_input',function(){
            
            var _this = this;

            var productStoreId = $(".choose_store").val();
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
              "posTerminalId":'',
              "orderId":orderId,
              "orderItemSeqId":orderItemSeqId,
              "value":quantity,
              "type":"price",
              "sellerId" :$('.seller_person').val(),
              "productStoreGroupId" :$('.seller_channel').val(),
              "buyerId" :$('#buyerId').val(),
              "paymentMethod" : $(".pay_way").val()
            },'/wpos/control/modifyCart',{},'imei');
        });

        function updateNumber(options, url, optionList,type){
            var list_tab_content_detail = $('.list_tab_content_detail');

            jQuery.ajax({
                url:url,
                type: 'post',
                dataType: 'json',
                contentType:"application/x-www-form-urlencoded",
                data:options,
                success: function(jsonObj){               	
                    var dataResult = jsonObj;
                    if(dataResult.responseMessage == "success"){
                    	
                    	$('.list_tab_content_detail').html(resetContent(dataResult.cart));
                        
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
                        showErrorMessage(dataResult.errorMessage);
                    }
                }
            });

        }

        function resetContent(data){

            var dataList = data.orderItems;
            var orderId = data.orderId;
            var htmlContent = '';
            var no=1;

            $('.all_money').text(data.grandTotal);
            $('#orderId').val(data.orderId);
            
            if(dataList && dataList.length > 0){
            	$('.create_order_btn').removeClass('no_pay_finish');
            }else{
         	   $('.create_order_btn').addClass('no_pay_finish');
            }

            if(dataList && dataList.length > 0){
                $.each(dataList,function(index, elem){

                	if(elem.imei){
                        htmlContent += '<div class="list_tab_content_div" data-id="'+ orderId +'" data-orderItemSeqId="'+ elem.orderItemSeqId +'" >'+
                        '<div>'+ no +'</div>'+
                        '<div>'+ elem.productId +'</div>'+
                        '<div class="itemDescriptionDiv" title="'+ elem.itemDescription +'" >'+ elem.itemDescription +'</div>'+
                        '<div>'+
                            '<span class="">1</span>'+
                        '</div>'+
                        '<div>'+
                    		'<span class="">'+ elem.imei +'</span>'+
                    	'</div>'+
                        '<div>'+
                            '<span class="enter_span">'+ parseNumberList(elem.unitPrice) +'</span>'+
                            '<input type="text" class="enter_input" value="'+ parseNumberList(elem.unitPrice) +'" />'+
                            '</div>'+
                        '<div class="priceContent">'+ parseNumberList(elem.unitPrice) * parseNumberList(elem.quantity) +'</div>'+
    		               '<div class="priceContent">'+ parseNumberList(elem.quantity) * (parseNumberList(elem.unitPrice) - parseNumberList(elem.unitRecurringPrice)) +'</div>'+
                        '<div class="priceEdit"><a href="javascript:void(0);" class="delete_div delete_finish_div"></a></div>'+
                    '</div>';
                	}else{
                        htmlContent += '<div class="list_tab_content_div" data-id="'+ orderId +'" data-orderItemSeqId="'+ elem.orderItemSeqId +'" >'+
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
                        '<div>'+
                            '<span class="enter_span">'+ parseNumberList(elem.unitPrice) +'</span>'+
                            '<input type="text" class="enter_input" value="'+ parseNumberList(elem.unitPrice) +'" />'+
                        '</div>'+
                        '<div class="priceContent">'+ parseNumberList(elem.unitPrice) * parseNumberList(elem.quantity) +'</div>'+
    		               '<div class="priceContent">'+ parseNumberList(elem.quantity) * (parseNumberList(elem.unitPrice) - parseNumberList(elem.unitRecurringPrice)) +'</div>'+
                        '<div class="priceEdit"><a href="javascript:void(0);" class="delete_div delete_finish_div"></a></div>'+
                    '</div>';
                	}
                	no++;
                })
            }

            return htmlContent;
        }
        
        function resetImeiContent(data){

            var dataList = data.orderItems;
            var orderId = data.orderId;
            var htmlContent = '';

            $('.all_money').text(data.grandTotal);
            $('#orderId').val(data.orderId);

            if(dataList && dataList.length > 0){
                $.each(dataList,function(index, elem){

                    htmlContent += '<div class="list_tab_content_div" data-id="'+ orderId +'" data-orderItemSeqId="'+ elem.orderItemSeqId +'" >'+
                           '<div>'+ elem.productId +'</div>'+
                           '<div class="itemDescriptionDiv" title="'+ elem.itemDescription +'">'+ elem.itemDescription +'</div>'+
                           '<div>'+
	                           '<span class="enter_imei_span">'+ parseNumberList(elem.quantity) +'</span>'+	                           
                           '</div>'+
                           '<div>'+
                           		'<span class="enter_imei_span">'+elem.imei +'</span>'+
                       	   '</div>'+
                           '<div>'+
                               '<span class="enter_span">'+ parseNumberList(elem.unitPrice) +'</span>'+
                               '<input type="text" class="enter_price_input" value="'+ parseNumberList(elem.unitPrice) +'" />'+
                           '</div>'+
		                   '<div>'+ parseNumberList(elem.quantity) * (parseNumberList(elem.unitPrice) - parseNumberList(elem.unitRecurringPrice)) +'</div>'+
                           '<div>'+ parseNumberList(elem.unitPrice) * parseNumberList(elem.quantity) +'</div>'+
                           '<div><a href="javascript:void(0);" class="delete_div delete_finish_div" data-type="imei"></a></div>'+
                       '</div>';
                })
            }

            return htmlContent;
        }

        $('.create_order_btn').on('click', function(e){

	        var productStoreId = $(".choose_store").val();
	        var sellerChannel = $(".seller_channel").val();
	        var sellerPerson = $(".seller_person").val();
	        var payWay = $(".pay_way").val();
	        var orderId = $('#orderId').val();
	        var chooseCustom = $('.choose_custom').val();
	        var needShipment = $('.need_shipment').val();
	        var order_phone = $('.order_phone').val();
	        var address = $('.address').val();
        	var fixOrder = $('.fix_order').val();
        	var fixDate = $('.fix_date').val();
	        
		    if($('.create_order_btn').hasClass('no_pay_finish')){
		    	return false;
		    }
		    
        	if(fixOrder == 'Y'){
        		if(fixDate == ''){
        			showErrorMessage('Please fill in fix date');

    		        return false;
        		}
        	}
		    
	        if(needShipment == ''){
	        	showErrorMessage('Please select shipment way!');
	        	
	        	return false;
	        }
	        
	        if(needShipment == 'Y' && address == ''){
	        	showErrorMessage('Please fill in shipment address!');
	        	
	        	return false;
	        }

            if(productStoreId == '' && sellerChannel == '' && sellerPerson == '' && chooseCustom == '' && order_phone ==''){
                showErrorMessage('Please fill in customer and store information');

                return false;
            }
	        if(productStoreId == '' || sellerChannel == '' || sellerPerson == ''){
	            showErrorMessage('Please fill in store information');
	
	            return false;
	        }
		    if(chooseCustom == '' && order_phone ==''){
		        showErrorMessage('Please fill in customer information');
		
		        return false;
		    }

	        if(!orderId){
		        showErrorMessage('Please add to cart');

		        return false;
	        }

            payInvoicing(e.currentTarget);
        });
        
        function formatDateToYMD(date) { 
      		var arr=date.split("/");
			var newStr = (arr.reverse()).join('-');
      		
      	  	return (new Date(newStr)); 
      	}

        //pay invocing
        function payInvoicing(target){
        	$.showLoading(target);
            $.ajax({
               type: 'post',
               dataType: 'json',
               url: '/wpos/control/payInvoicing',
               data : {
                   "productStoreId":$(".choose_store").val(),
                   "orderId":$('#orderId').val(),
                   "verify":true,
                   "posTerminalId":'',
                   'isShip': $('.need_shipment').val(),
                   "paymentMethod" : $(".pay_way").val(),
                   "buyerId" : $("#buyerId").val(),
                   "sellerId" : $(".seller_person").val(),
                   "fixOrder" : $('.fix_order').val(),
                   "fixDate" : $('.fix_order').val() == 'Y' ? formatDateToYMD($('.fix_date').val()).getTime() : ''
               },
               success: function(data) {
            	   $.hideLoading(target);
                   if(data.responseMessage=="success"){
	                   showErrorMessage('Create order success', function () {
	                	   $('.create_order_btn').addClass('no_pay_finish');
	                	   var orderId = data.cart && data.cart.orderId;
	                	   var isNeedShip = data.isNeedShip;
	                	   var paymentMethod = data.paymentMethod;
                		   var printList = [{
                			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
                			   fileName : 'commoninvoice'
                		   }];
                		   if(paymentMethod == 'CREDIT'){
	                		   printList.push({
	                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
	                			   fileName : 'returnreceipt_copy'
	                		   });
	                		   printList.push({
	                			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
	                			   fileName : 'commoninvoice_copy'
	                		   });
	                		   printList.push({
	                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
	                			   fileName : 'returnreceipt'
	                		   });
	                		   printInvocingFun(printList);
                		   }else if(paymentMethod == 'CASH'){
    	                	   if(isNeedShip == 'Y'){
    	                		   printList.push({
    	                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
    	                			   fileName : 'returnreceipt_copy'
    	                		   });
    	                		   printList.push({
    	                			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
    	                			   fileName : 'commoninvoice_copy'
    	                		   });
    	                		   printList.push({
    	                			   url : '/ordermgr/control/Thermalreturnreceipt?orderId='+orderId,
    	                			   fileName : 'returnreceipt'
    	                		   });
    	                		   printInvocingFun(printList);
    	                	   }else{
    	                		   printList.push({
    	                			   url : '/ordermgr/control/Thermalinvoice?orderId='+orderId,
    	                			   fileName : 'commoninvoice_copy'
    	                		   });
    	                		   printInvocingFun(printList); 
    	                	   }
                		   }
		                   location.reload();
	                   });
                   }else if(data.responseMessage=="error"){
                       showErrorMessage(data.errorMessage);
                   }
               },
               error : function(option, status){
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
        
        $(document).on('click', function(){
    		var product_ul = $('.product_ul');
        	var Member_ul = $('.Member_ul');
        	
        	Member_ul.hide();
        	product_ul.hide();
        });

	    function  setScroll() {
		    var content_btm_zIndex = $('.content_btm_zIndex');
		    var list_tab_content_detail = $('.list_tab_content_detail');
		    var list_tab_content_head = $('.list_tab_content_head');

		    if(list_tab_content_detail[0].scrollHeight > 375 ){

			    list_tab_content_detail.css({
				    "height" : 375 + 'px'
			    });
			    list_tab_content_detail.addClass("isScroll");
			    list_tab_content_head.addClass('resetWidth');
		    }else {

			    list_tab_content_detail.css({
				    "height" : "auto"
			    });
			    list_tab_content_detail.removeClass("isScroll");
			    list_tab_content_head.removeClass('resetWidth');
		    }
	    }
	    var imeis=[];
	    var availableImeis=[];
	    var imeisTotal=[];
	    function generateDebounceFn(fn, wait){
	    	var timeoutid =0;
	    	return function(){
	    		if(timeoutid){
	    			clearTimeout(timeoutid);
	    			timeoutid = 0;
	    		}
	    		timeoutid = setTimeout(function(){
	    			typeof fn === 'function' && fn();
	    		}, wait);
	    	}
	    }
	    var imeiNums=generateDebounceFn(function(){
	    	var imeistr=imeis.join(",");
	    	$(".open_imeis_loading").show();
	    	$.ajax({
				url:'/wpos/control/checkIMEIisAvalible',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify({"imeis":imeistr,"storeId":$(".choose_store").val()}),
	           	success:function(results){	
	           		$(".open_imeis_loading").hide();
					if(results.resultCode==1){
						var arrs=results.result;
						var availableNums=0;
						var unavailableNums=0;						
						$("#imei_table_tbody").find(".imeiVal").each(function(index,elem){							
							var val=$(elem).text();
							if($.inArray(val,arrs)!=-1){
								$(elem).next(".status").text("Unavailable").addClass("statu_unaval");
								unavailableNums++;
								return;
							}
							$(elem).next(".status").text("Available");
							availableImeis.push($(elem).text());
							availableNums++;
						})						
						$(".imeis").attr("disabled","disabled");
						$(".imeis_total").find(".imeis_available_nums").text(availableNums);
						$(".imeis_total").find(".imeis_unavailable_nums").text(unavailableNums);
						var imeiLen=$("#imei_table_tbody").find(".imeiVal").length;
						if(results.product){
							var desc=results.product.brandName+" "+results.product.model+" "+results.product.description;
							$(".imeis_content_product").find(".imei_productid").text(results.product.productId);
							$(".imeis_content_product").find(".imei_desc").text(desc);
						}						
						if(imeiLen==1){
							$(".operator_vertify").text("A single IMEI code is not supposed to input here. Please scan the two-dimension code of the batch.")	        				
	        				$(".dialog_btn .btn_clear").removeClass("disabled");
	        				return;
						}
						if(imeiLen<=3&&unavailableNums>=1){
	        				$(".operator_vertify").text("Cannot add these products to the cart as not all of them are available.")	        				
	        				$(".dialog_btn .btn_clear").removeClass("disabled");
	        				return;
	        			}
	        			if(imeiLen>3&&unavailableNums>=3){
	        				$(".operator_vertify").text("Cannot add these products to the cart, as the unavailable number is supposed to be less than 3.")
	        				$(".dialog_btn .btn_clear").removeClass("disabled");
	        				return;
	        			}
	        			$(".dialog_btn a").removeClass("disabled");
						
												
					}else{
						showErrorMessage(results.errorMessage);
					}				
				}
			})
	    },1000)
	    //批量增加IMEI进购物车
	    jQuery('.batch_add_imei').on('click', function(e){
        	var _this = $(this);
        	if(!addToCartRules()){
	    		return false;
	    	}
        	imeisTotal=[];
        	initImeiDialog();       	
        	$(".imei_dialog_content").hide();
        	$(".open_imei_dialog").css("display","table-cell");
        	$(".open_imei_dialog .imeis").val("").focus();
        	jQuery('.open_imei_dialog').off('click', '.imei_dialog_delete').on('click', '.imei_dialog_delete',function(){
				jQuery('.open_imei_dialog').hide();
			});        	 
        	var nums=1;
        	var no=1;        	
        	jQuery('.open_imei_dialog').off('keypress', '.imeis').on('keypress', '.imeis',function(e){
        		var t=this;        	
            	var key = e.which;
    		    if (key == 13&&$.trim($(this).val())) {
    		    	var $that=$(this);
    		    	var val=$.trim($that.val());   
    		    	if(val.indexOf("|")!=-1){
    		    		val=val.replace(/[\|]/g,"");
    		    	}
    		    	if(val.length>15&&val.length%15==0){//批量扫的时候
    		    		var reg=new RegExp("^[0-9]*$");
    		    		if(!reg.test(val)){
        	        		$that.next(".imeis_vertify").text('Please enter correct IMEI code');
        	        		clearTimeout(timeOut);
        	        		var timeOut=setTimeout(function(){
        	        			$that.val("");
        	        			$that.next(".imeis_vertify").text("");
        	        		},1000)    	        		
        	        		return;
        	        	}
    		    		var arrs=[];
    		    	    var str="";
    		    		for(var i=0,len=val.length;i<len;i++){
    		    			str += val[i];
    		    		    if((i+1) % 15 == 0){
    		    		    	arrs.push(str);
    		    		    	str="";
    		    		    }
    		    		}
    		    		$.each(arrs,function(index,val){
    		    			if($.inArray(val,imeis)!=-1){
            	        		$that.next(".imeis_vertify").text('This code has already been in the list');
            	        		clearTimeout(timeOut);
            	        		var timeOut=setTimeout(function(){
            	        			$that.val("");
            	        			$that.next(".imeis_vertify").text("");
            	        		},1000) 
            	        		return;
            		    	}
    		    		})
    		    		batchImeiToTable("batch");   		    		
    		    	}else{   		    		
    		    		var reg=new RegExp("^([0-9]{15})$");
        	        	if(!reg.test(val)){
        	        		$that.next(".imeis_vertify").text('Please enter correct 15-digits IMEI code');
        	        		clearTimeout(timeOut);
        	        		var timeOut=setTimeout(function(){
        	        			$that.val("");
        	        			$that.next(".imeis_vertify").text("");
        	        		},1000)    	        		
        	        		return;
        	        	}
        	        	if($.inArray(val,imeis)!=-1){
        	        		$that.next(".imeis_vertify").text('This code has already been in the list');
        	        		clearTimeout(timeOut);
        	        		var timeOut=setTimeout(function(){
        	        			$that.val("");
        	        			$that.next(".imeis_vertify").text("");
        	        		},1000) 
        	        		return;
        		    	}
    		    		batchImeiToTable("single");
    		    	}    		    	
    		    	function batchImeiToTable(type){
    		    		if($(".imei_dialog_content:hidden")){
        		    		$(".imei_dialog_content").fadeIn();
        		    	}
    		    		switch(type){
	    		    		case"batch":	    		    				        		    		
	        		    		$.each(arrs,function(index,val){
	        		    			imeis.push(val);
	            		    		addImeisToTable(val);
	            		    		nums++;
	        		    		})
	        		    		$that.val("");
	    		    			break;
	    		    		case"single":   	        	
	            		    	imeis.push(val);
	            		    	$that.val("");
	            		    	addImeisToTable(val);
	            		    	nums++;
	    		    			break;
	    		    		}
    		    	}
    		    	
    		    	
    		    }
			});
        	function addImeisToTable(val){          		
        		var str="";
        		var tr="";
        		if(no==1){
        			tr+="<tr class='tr"+nums+"'></tr>";
        			str="<td>"+nums+"</td><td class='imeiVal'>"+val+"</td><td class='status'></td>";
        			$("#imei_table_tbody").append(tr);
        			$("#imei_table_tbody").find(".tr"+nums).append(str);
        		}   
        		imeiNums();
        		var maxHeight=$(".imeis_content_desc").css("max-height").replace("px","");
        		if($(".imeis_content_desc").height()>320&&maxHeight=="none"){
        			$(".imeis_content_desc").css({"max-height":"270px","overflow-y":"scroll"});
        		}        		
        		if(no==2){	
        			str="<td>"+nums+"</td><td class='imeiVal'>"+val+"</td><td class='status'></td>";
        			$("#imei_table_tbody").find(".tr"+(nums-1)).append(str);
        			no=1;
         			return;
        		}
        		no++;        		
        	}
        	jQuery(".open_imei_dialog").off('click', '.dialog_btn a').on("click",".dialog_btn a",function(e){
        		if($(this).hasClass("disabled")){
        			return;
        		}
        		var type=$(this).attr("data-type");
        		var availNo=$(".imeis_total").find(".imeis_available_nums").text();
        		var unavailNo=$(".imeis_total").find(".imeis_unavailable_nums").text();
        		var imeiLen=$("#imei_table_tbody").find(".imeiVal").length;
        		var productInput = availableImeis.join(",");
    	    	var productStoreId = $(".choose_store").val();
    	    	var productId=$(".imei_productid").text(); 
        		switch(type){
	        		case"addCartContinue":	        			      	    	
	        	    	addImeisToCart({
	    					productStoreId : productStoreId,
	    					productInput : productInput,
	    					productId:productId,
	    	                memberInput : $( ".choose_custom" ).val(),
	    	                phoneInput : $( ".order_phone" ).val(),
	    	                addressId : $( ".address" ).attr('data-id'),
	    	                quantity:1
	    				});
	        			$(".imei_dialog_content").fadeOut();
	        			initImeiDialog();
	        			break;
	        		case"addCartClose":	        			    				        			
	        			$(".open_imei_dialog").fadeOut();	        			    	
	        	    	addImeisToCart({
	    					productStoreId : productStoreId,
	    					productInput : productInput,
	    					productId:productId,
	    	                memberInput : $( ".choose_custom" ).val(),
	    	                phoneInput : $( ".order_phone" ).val(),
	    	                addressId : $( ".address" ).attr('data-id'),
	    	                quantity:1
	    				});	        	    	
	        	    	initImeiDialog();
	        			break;
	        		case"clear":
	        			$(".imei_dialog_content").fadeOut();	 
	        			imeisTotal=[];
	        			initImeiDialog();
	        			break;
	        		}
        	})
        	function addImeisToCart(option){
        		
        		jQuery.ajax({
                    url:'/wpos/control/addToCart',
                    type: 'post',
                    dataType: 'json',
                    contentType:"application/x-www-form-urlencoded",
                    data:{
                      "productStoreId":option.productStoreId,
                      "add_product_id":option.productId,
                      "imei":option.productInput,
                      "quantity":option.quantity,
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
        	}
        	function initImeiDialog(){        
        		availableImeis=[];
        	    imeis=[];  
        	    nums=1;
        	    no=1;
        	    $(".imeis_content_desc").css({"max-height":"none","overflow-y":"hidden"});
        	    $(".imei_dialog_content").find("#imei_table_tbody").find("tr").remove();
        	    $(".imeis_content_desc").css({"max-height":"none","overflow-y":"hidden"});
        		$(".dialog_btn a").addClass("disabled");
    			$(".imeis").removeAttr("disabled");
				$(".imeis_total").find(".imeis_available_nums").text("");
				$(".imeis_total").find(".imeis_unavailable_nums").text("");
				$(".operator_vertify").text("");
        	}
        	
        	jQuery('.open_imei_dialog').off('click', '.clear').on('click', '.clear',function(){
        		$(".imeis_content").text("");
        		$(".imeis").focus();
			});
        	jQuery('.open_imei_dialog').on('click', '.next',function(){
        		var imeis=$(".imeis_content").text();
        		jQuery('.open_imei_dialog').hide();
			});
        })
	    
	    //add to cart 
	    
	    $('.add_cart').on('click', function(e){
	    	
	    	var productInput = $( ".product_input" ).val().trim();
	    	var productStoreId = $(".choose_store").val();
			var productInputLen = productInput.length;
			var newExp = /^[0-9]*$/;
		
	    	// before rules
	    	if(!addToCartRules()){
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
	                memberInput : $( ".choose_custom" ).val(),
	                phoneInput : $( ".order_phone" ).val(),
	                addressId : $( ".address" ).attr('data-id')
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
	        
            var productStoreId = $(".choose_store").val();
            var sellerChannel = $(".seller_channel").val();
            var sellerPerson = $(".seller_person").val();
            var payWay = $(".pay_way").val();
	        var chooseCustom = $('.choose_custom').val();
	        var add_product_id = jQuery('.product_input').attr('data-id');
	        var buyerId = $( "#buyerId" ).val();
	        var memberInput = $( ".choose_custom" ).val();
	        var phoneInput = $( ".order_phone" ).val();
	        var addressId = $( ".address" ).attr('data-id');
	    	
	        var newExp = /^[0-9]*$/;

            if(productStoreId == '' && sellerChannel == '' && sellerPerson == '' && payWay == '' && chooseCustom == '' && phoneInput ==''){
                showErrorMessage('Please fill in customer and store information');

                return false;
            }
            
	        if(productStoreId == '' || sellerChannel == '' || sellerPerson == '' || payWay == ''){
	            showErrorMessage('Please fill in store information');
	
	            return false;
	        }
		    if(chooseCustom == '' || phoneInput ==''){
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
	                enterImei.find('.enter_imei_input').on('keyup', function(){
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
	              "productStoreId":$(".choose_store").val(),
	              "add_product_id":option.productId,
	              "imei": option.imei,
	              "quantity":1,
                  "orderId" :$('#orderId').val(),
                  "sellerId" :$('.seller_person').val(),
                  "productStoreGroupId" :$('.seller_channel').val(),
                  "buyerId" :$('#buyerId').val(),
                  "paymentMethod" : $(".pay_way").val(),
                  "firstName" : $( ".choose_custom" ).val(),
                  "contactNumber" : $( ".order_phone" ).val(),
                  "contactMechId" : $( ".address" ).attr('data-id')
	            },
	            success: function(jsonObj){
	                var dataResult = jsonObj;
	                e && $.hideLoading(e.currentTarget);
	                
	                if(dataResult.responseMessage == "success"){
	                	
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
	    	var productStoreId = $(".choose_store").val();
	        var memberInput = $( ".choose_custom" ).val();
	        var phoneInput = $( ".order_phone" ).val();
	        var addressId = $( ".address" ).attr('data-id');
	    	var productInput = $( ".product_input" ).val().trim();
	    	var productInputLen = productInput.length;
	    	var newExp = /^[0-9]*$/;
	    	
	    	if(!productStoreId){
	    		showErrorMessage('Please choose store');
	    		$( ".product_input" ).val('');
	    		
	    		return false;
	    	}
	    	
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
	                              "productStoreGroupId" :$('.seller_channel').val(),
	                              "paymentMethod" : $(".pay_way").val(),
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
		                 storeId : $(".choose_store").val(),
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
    })
</script>
<#else>
	<div>Sorry, you do not have permission to perform this action.</div>
</#if>