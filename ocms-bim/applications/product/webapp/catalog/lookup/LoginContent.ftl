
	<meta charset="UTF-8">
	<title>product list</title>



<div id="content_box">
	<div class="content_list">
		<div class="content_form_list">
			<form class="form-horizontal form-content">
				<div class="group">
					<input type="text" id="username" maxlength="30" name="username" placeholder="用户名" id="username"/>
				</div>
				<div class="group password-group">
					<input type="password" maxlength="30" id="password" name="password" placeholder="密码" id="password"/>
				</div>
				<div class="error-group">
					<p id="resultinfo"></p>
				</div>
				<div class="group login-group">
					<a href="javascript:Loginjs();" class="login-btn">登录</a>
				</div>
				<div class="group help-group">
					<a href="javascript:void(0);" class="forget-password">忘记密码?</a>
				</div>
			</form>
		</div>
	</div>
</div>



<script>
	
	
		function Loginjs(){
		
			
		var username = $("input[id='username']").val();
		var password = $("input[id='password']").val();
		
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/LoginAjax?USERNAME='+username+'&PASSWORD='+password,
	        data: {
	        	USERNAME:username,
				PASSWORD:password,
					},
	        success: function (data) {
	        if(data.ret=="success")
	       	 	$("#resultinfo").html("登录成功.");
	       	 else 
	       	 	$("#resultinfo").html("登录失败,用户名或密码错误.");
	       	}	
    	});
	}
	
</script>
































