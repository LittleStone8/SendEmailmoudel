<div id="content_box">
	<div class="content_list">
		<div class="content_form_list">
			<form class="form-horizontal form-content">
				<div class="group">
					<input type="text" id="username" maxlength="30" name="username" placeholder="Username" />
				</div>
				<div class="group password-group">
					<input type="password" maxlength="30" id="password" name="password" placeholder="Password" />
				</div>
				<div class="error-group">
					<p></p>
				</div>
				<div class="group login-group">
					<a href="javascript:void(0);" class="login-btn">Login</a>
				</div>
				<!---<div class="group help-group">
					<a href="javascript:void(0);" class="forget-password">Forget Password?</a>
				</div>--->
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		
		$('.login-btn').on('click', function(e){

	        var username = $('#username');
	        var password = $('#password');
	        var errorTips = $('.error-group p');

	        if(username.val() == ''){
	            errorTips.text('Please enter user name!');

	            return false;
	        }
	        if(password.val() == ''){
	            errorTips.text('Please enter your password!');

	            return false;
	        }	        
	        loginFun({
	           userName:username.val(),
	            userPassword:password.val()
	        }, '/commondefine/control/login',errorTips,e.currentTarget);
	    });


//		contentType: "application/json;charset=utf-8",
//		data:JSON.stringify({							
//			categoryJSONArr:categoryArr
// 		}),
		
		
	    function loginFun(options, url, elem,target){
	    	target && $.showLoading(target);
	        $.ajax({
	            url:url,
	            type: 'post',
	            dataType: 'json',
	            contentType:"application/json;charset=utf-8",
	            data:JSON.stringify(options),
	            success: function(jsonObj){
	            	var dataResult = jsonObj;
	                if(dataResult.responseMessage == "success"){
	                    location.reload();
	                }else if(dataResult.responseMessage == "error"){
	                	target&&$.hideLoading(target);     	
	                    elem.text(dataResult.errorMessage);
	                }
	            }
	        })
	    }
	     
	    //enter键登录
	    $(document).on('keyup',function(event) {  
	        if(window.event.keyCode == 13){  
	        	$('.login-btn').click();
	        }     
	    }); 
	});

</script>