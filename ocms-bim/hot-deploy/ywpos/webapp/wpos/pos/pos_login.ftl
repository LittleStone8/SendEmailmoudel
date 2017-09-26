<div id="pos_content_box">
	<div class="pos_content_list">
		<div class="pos_content_form_list">
			<form class="form-horizontal pos_form_content">
				
				
				<div class="group">
					<input type="text" id="userName" name="userName" value="<#if userLogin?has_content>${userLogin.userLoginId}</#if>" <#if userLogin?has_content>readOnly="readonly"</#if> placeholder="userName" />
				</div>
				<div class="group" style="<#if userLogin?has_content>display: none;</#if>">
					<input type="password" id="password" name="password" placeholder="Password" />
				</div>
				
				<div class="group stone_select stone_select_d" style="<#if userLogin?has_content><#else>display: none;</#if>">
					<label for="stone_select">Choose Store</label>
					<select id="stone_select" name="stone">
					</select>
				</div>
				
				<div class="error-group">
					<p id="errorMes"></p>
				</div>
				<div class="group pos_login_group">
					<a href="javascript:void(0);" class="pos_login_btn">LOGIN</a>
				</div>
				<div class="group">
					<a href="/opentaps/control/main" class="back_to_home">Back Home</a>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){ 

	var option_template ="<% _.each(options, function(option,key){  %>"+
				'<option value ="<%- option.val %>"><%- option.name %></option>'+
			  "<% }) %>";
	
	var option_compiled = _.template(option_template);
	
	function handleErrorMes(mes){
		$("#errorMes").html(mes);
	};

var mloginFun = function(){	    
		$(".pos_login_group a").html('CONFIRM');
		var confirmA=$(".pos_login_group a");
        var userName = $("#userName").val();
        var password = $("#password").val();
        var productStoreId = $("#stone_select").val();
        
        console.log(productStoreId);
        
        
        $.showLoading(confirmA);
        jQuery.ajax({
              url:'/wpos/control/mlogin',
              type:"POST",
              contentType:"application/json;charset=utf-8",
              data:JSON.stringify({
                    "userName":userName,
                    "userPassword":password,
                    "productStoreId":productStoreId
              }),
              dataType:"json",
              success: function(jsonObj){            	  
            	  $.hideLoading(confirmA);
                  handleErrorMes('');
                  if(jsonObj.productStoreList && jsonObj.productStoreList.length){
                	  if(jsonObj.productStoreList.length == 1){
                		  $('.pos_login_group').click();
                	  }
                      var obj = {};
                      obj.options = [];
                      var productStoreList = jsonObj.productStoreList && (jsonObj.productStoreList).sort(function(a, b){
                    	  if(a.storeName > b.storeName){
                    		  return 1;
                    	  }else{
                    		  return -1
                    	  }
                      });
                      for(var i= 0,len=productStoreList.length;i<len;i++){
                          var productStore = productStoreList[i];
                          var tmpObj = {};
                          tmpObj.val =  productStore.productStoreId;
                          tmpObj.name =   productStore.storeName;
                          obj.options.push(tmpObj);
                      }
                      $("#stone_select").html(option_compiled(obj));

                      $("#userName").prop('disabled','disabled').prop('readonly','readonly');
                      $("#password").prop('disabled','disabled').prop('readonly','readonly').parent().hide();
                      $(".stone_select_d").show().fadeIn();
                      
                      return;
                  }else if(jsonObj.checkAll){
                      window.location.href= "/wpos/control/webpos?productStoreId="+productStoreId;
                  }else{
                    handleErrorMes(jsonObj.errorMessage || 'Incorrect username or password');
                 }

             }
        });
    }



<#if userLogin?has_content>
	mloginFun();
</#if>

	$(".pos_login_group").click(mloginFun);
	
    //enter键登录
    $(document).on('keyup',function(event) {  
        if(window.event.keyCode == 13){  
        	$('.pos_login_group').click();
        }     
    }); 
});
</script>
<script type="text/javascript" src="/commondefine_js/underscore.js"></script>
<script type="text/javascript" src="/commondefine_js/yw_comm.js"></script>
