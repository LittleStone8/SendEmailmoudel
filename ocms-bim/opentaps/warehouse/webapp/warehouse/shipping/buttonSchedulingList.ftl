<#--
 * Copyright (c) Open Source Strategies, Inc.
 * 
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
-->
<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>
<div class="driverInformation">
	<div class="driverHeader">Fill in driver information</div>
	<div class="driverContent">
		<div class="dirverName">
		<input type="text" name="driverid" class="driverid" hidden/>
			<span>Select driver</span>
			<input type="text" name="selectdriver" class="selectdriver" />
			<ul class="driver_ul" style="display:none;" tabindex="11" ></ul>
		</div>
		<div class="dirverTelephone">
			<span>Telephone</span>
			<input type="text" name="telephone" class="telephone" disabled="true"/>
		</div>
		<div class="dirverLicense">
			<span>License</span>
			<input type="text" name="license" class="license"/>
		</div>
		<div class="newDriver">
			<a href="javascript:void(0);" class="new_driver" >+New driver</a>
		</div>
	</div>
	<div class="driverOperator">
	  	<@inputSubmit title=uiLabelMap.ProductScheduleTheseRouteSegments onClick="return markSchedu()"/>
	</div>
</div>
<div class="open_new_driver">
	<div class="fixed_screen"></div>
	<div class="add_new_driver" id="openDriver">
		<div class="new_driver_icon"></div>
		<div class="new_driver_title">New Driver</div>
		<div class="new_driver_content">
			<div class="input_group">
				<span>Driver Name</span>
				<input type="text" class="firstName" />
				<span class="error_info"></span>
			</div>
			<div class="input_group">
				<span>Driver Phone</span>
				<input type="text" class="primaryPhoneNumber" />
				<span class="error_info"></span>
			</div>
		</div>
		<div class="new_driver_operator">
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
<script src="/commondefine_js/jQuery/jquery.js"></script>
<script src="/commondefine_js/loading.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
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
	
	function initOpenDriverWH(options){
		var bodyW = $(window).width();
	    var bodyH = $(window).height();
	    //获取对话框宽、高
	    var elW =  options.width();
	    var elH =  options.height();
	    options.css("left",(bodyW - elW)/2 + 'px');
	    options.css("top",(bodyH - elH)/2 + 'px')
	}
    //创建用户
    $('.new_driver').on('click', function(){
        var openNewDriver = $('.open_new_driver');
        
        initOpenDriverWH($('#openDriver'));
        openNewDriver.show();
        $("#openMember .error_info").each(function(){
    		$(this).text('');
    	})

        //客户名称跟电话     
        var telInput = $('.firstName');
        var driverInput = $('.primaryPhoneNumber');

        telInput.val('');
        driverInput.val('');
        
        
      //关闭弹窗
        openNewDriver.off('click','.new_driver_icon').on('click','.new_driver_icon',function(){
        	openNewDriver.hide();
        });

        openNewDriver.off('click','.cancel').on('click','.cancel',function(){
        	openNewDriver.hide();
        });
        //确认弹框
        openNewDriver.off('click','.confirm').on('click','.confirm',function(e){
        	var _this = $(this); 
        	
            var firstName = $('.firstName').val();
            var primaryPhoneNumber = $('.primaryPhoneNumber').val();
	        //电话号码格式	        
	        var newErp = /^[0-1]\d{9}$/;

	        if(firstName==''){
	        	$('.firstName').next(".error_info").text("Please fill in Customer name");
	        	$('.firstName').focus();
	        	return false;
	        }else{
	        	$('.firstName').next(".error_info").text('');
	        }
	        if(primaryPhoneNumber==''){
	        	$('.primaryPhoneNumber').next(".error_info").text("Please fill in Customer phone");
	        	$('.primaryPhoneNumber').focus();
	        	return false;
	        }
	     	if(!newErp.test(primaryPhoneNumber)){
	        	$('.primaryPhoneNumber').next(".error_info").text('please enter a 10-digit phone number that starts with 0 or 1');
	        	$('.primaryPhoneNumber').focus();
	            return false;
            }else{
	        	$('.primaryPhoneNumber').next(".error_info").text('');
	        }
	        $.showLoading(e.currentTarget);
            $.ajax({
                url:"/facility/control/createDriver",
                type: 'post',
                dataType: 'json',
                contentType:"application/x-www-form-urlencoded",
                data:{
                	firstName : firstName,
                    primaryPhoneNumber : primaryPhoneNumber
                },
                success: function(jsonObj){
                    var dataResult = jsonObj;
                    $.hideLoading(e.currentTarget);
                    if(dataResult.createPerson && dataResult.createPerson.responseMessage == "success"){
                    	
                    	$('[name="driverid"]').val(dataResult.createPerson.partyId);
                        $('[name="selectdriver"]').val(firstName);
	                    $('[name="telephone"]').val(primaryPhoneNumber);

	                    openNewDriver.hide();
						
                        
                    }else if(dataResult.responseMessage == "error"){
                        showErrorMessage(dataResult.errorMessage);
                    }
                },
                error : function(data, status){
                   	console.info(data);
                }
            });
        });
    });
    
	function myAutoComplete(dataOption){
    	
    	var driver_ul = $('.driver_ul');
        var selectdriver = $( ".selectdriver" );
        var telephone =  $( ".telephone" );
        var driverid =  $( ".driverid" );
        
    	var dataList = dataOption;
    	var htmlContent = '';
    	
    	if(dataList.length == 0){
    		driver_ul.hide();
    		
    		return false;
    	}
    	
    	$.each(dataList,function(index, item){
    		
    		htmlContent += '<li data-driverid="'+ item.partyid +'" data-driver="'+ item.firstname +'" data-telephone = "'+ item.telnumber +'">'+ item.firstname +'</li>'
    	});
    	
    	driver_ul.html(htmlContent).show();
    	
    	driver_ul.find('li').click(function(){
    		var _this = $(this);
    		var dataDriverid = _this.attr('data-driverid');
    		var dataDriver = _this.attr('data-driver');
    		var dataTelephone = _this.attr('data-telephone');
   		
    		driverid.val(dataDriverid);
    		selectdriver.val(dataDriver);
    		telephone.val(dataTelephone);
    		
    		driver_ul.hide();
    	});
    }
    
    //模糊查询----driver
    $( ".selectdriver" ).keyup(function(event){
    	event.preventDefault();
    	event.stopPropagation();
    	
    	var selectdriver = $( ".selectdriver" ).val();
    	var driver_ul = $('.driver_ul');
    	
    	if(selectdriver == ''){
    		driver_ul.hide();
    		
    		return false;
    	}
    	
        $.ajax({
            type: 'post',
            dataType: 'json',
            url:"/facility/control/lenovofuzzyqueryEGATEEdriver",
            data : {
            	firstName: selectdriver
            },
            success: function(data) {
                if(data.responseMessage=="success"){
                	myAutoComplete(data.retlist);
                }
            }
        })  
    });
    
    $(document).on('click', function(){
    	var driver_ul = $('.driver_ul');
    	
    	driver_ul.hide();
    });
});
</script>

<script type="text/javascript">
    function markAsShipped() {
    	
        var selectdriver = $('.selectdriver');
        var license = $('.license');
    	if(selectdriver.val()==""||license.val()=="")
    	{
    		alert("driver or license Can't be empty")
    		return false;
    	}
        var i = 0;
        while(document.SchedulingList['carrierServiceStatusId_o_' + i]) {
           document.SchedulingList['carrierServiceStatusId_o_' + i].value = "SHRSCS_SHIPPED";
           i++;
        }
        document.SchedulingList.action = "<@ofbizUrl>BatchUpdateShipmentRouteSegments?facilityId=</@ofbizUrl>";
        document.SchedulingList.submit();
        return true;
    }
    function markSchedu() {
        var selectdriver = $('.selectdriver');
        var license = $('.license');
        var driverid = $('.driverid');
        
    	if(selectdriver.val()==""||license.val()=="")
    	{
    		alert("driver or license Can't be empty");
    		return false;
    	}
    	if(driverid.val()=="")
    	{
    		alert("There is no this driver");
    		return false;
    	}
    	
        var listTableChk = $('.listTable').find('input:checkbox:checked');
        var arrList = [];
        if(listTableChk.length > 0){
        	$.each(listTableChk, function(index, elem){
        		var shipmentId = $(elem).closest('tr').find('td').eq(0).find('a').text()
        		if(shipmentId){
        			arrList.push(shipmentId);
        		}
        	})
        }else{
        	alert("Please select a shipmentId.");
        	return false;
        }
        console.log(arrList);
        console.log(arrList.join('#'));
    	
        $.ajax({
            type: 'post',
            dataType: 'json',
            url:"/facility/control/updatateudataedriverandlicesn",
            data : {
            	shipmentidlist: arrList.join('#'),
            	driverid: driverid.val(),
       			license: license.val(),
            },
            success: function(data) {
                if(data.responseMessage=="success"){

                }
            }
        })
        
        document.SchedulingList.submit();
        return true;
    }
</script>
