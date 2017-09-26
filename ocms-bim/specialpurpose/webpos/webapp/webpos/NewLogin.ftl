




<div class="container">
      <form class="form-signin">
        <h2 class="form-signin-heading">Login</h2>
        
        <div class="input-group"> 
        	<div class="input-group-addon"><div class="glyphicon glyphicon-user"></div></div> 
    		<input type="text" class="form-control" id="userName" name="USERNAME" placeholder="user name"> 
		</div>
		
		<div class="input-group"> 
        	<div class="input-group-addon"><div class="glyphicon glyphicon-lock"></div></div> 
        	<input type="password" class="form-control" id="password" name="PASSWORD" placeholder="password"> 
		</div>
        <button id="login_next" class="btn btn-lg btn-primary btn-block" type="button">Next</button>
      </form>

</div>






<script language="JavaScript" type="text/javascript">


window.onload = function(){

		var productStore_template ='<div id="productStore_modal" class="modal fade">\
								<div class="modal-dialog">\
									<div class="modal-content">\
										<div class="modal-header">\
											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>\
											<h4 class="modal-title">请选择对应的店铺于poss机</h4>\
										</div>\
										<div class="modal-body">\
												<div class="row">\
													 <div class="input-group"> \
											        	<div class="input-group-addon"><div class="glyphicon glyphicon-th-list"></div></div> \
												        	<select id="productStoreList" class="form-control">\
															    <ul id="language_list_ul" class="list-group">\
																	<% _.each(productStoreList, function(productStore,key){  %>\
																		<option value ="<%- productStore.productStoreId %>"><%- productStore.storeName %></option>\
																	<% }) %>\
																</ul>\
															</select>\
													</div>\
													<div class="input-group"> \
											        	<div class="input-group-addon"><div class="glyphicon glyphicon-th-list"></div></div> \
											        	<div id="posTerminalDiv">\
											        		<select id="posTerminalList" class="form-control">\
															</select>\
											        	</div>\
													</div>\
													 <button id="login_confirm" class="btn btn-lg btn-primary btn-block" type="button">确认</button>\
												</div>\
										</div>\
									</div>\
								</div>\
							</div>';
						
	var terminal_option_template ='<select id="posTerminalList" class="form-control">\
									<% _.each(posTerminals, function(posTerminal,key){  %>\
									 <option value ="<%- posTerminal.posTerminalId %>"><%- posTerminal.terminalName %></option>\
								  	<% }) %>\
								  </select>';				
		
	var productStore_compiled = _.template(productStore_template);
	var terminal_option_compiled = _.template(terminal_option_template);
    

	jQuery("#login_next").click(function(){
	
		var userName = jQuery("#userName").val();
		var password = jQuery("#password").val();
	
		jQuery.ajax({
				  url:'/webpos/control/login_step2',
				  type:"POST",
				  data:{
				  	"USERNAME":userName,
				  	"PASSWORD":password
				  },
				  dataType:"json",
				  success: function(jsonObj){
					    console.log(jsonObj);
					    var productStore_html = productStore_compiled(jsonObj);
					    
    					jQuery("#boot_modal").append(productStore_html);
    					
    					jQuery("#productStore_modal").modal()
    					
					    setTimeout(function(){
					    	jQuery("#productStoreList").selectpicker({
					    		title:"请选择一个店铺",
					    		liveSearch:true
					    	});
					    	
					    	jQuery("#posTerminalList").selectpicker({
					    		title:"请选择一个pos机器",
					    		liveSearch:true
					    	});
					    	
							jQuery('#productStoreList').on('changed.bs.select', function (e) {
					    		console.log(e);
					    		
					    		console.log(jQuery('#productStoreList').val());
					    		
					    		var productStoreId = jQuery('#productStoreList').val();
					    		
					    		if(productStoreId){
						    		jQuery.ajax({
										  url:'/webpos/control/find_terminal',
										  type:"POST",
										  data:{
										  	"productStoreId":productStoreId
										  },
										  dataType:"json",
										  success: function(jsonObj){
											  	if(jsonObj.posTerminals){
											  		jQuery("#posTerminalDiv").html(terminal_option_compiled(jsonObj));
											  		jQuery("#posTerminalList").selectpicker({
											    		title:"请选择一个pos机器",
											    		liveSearch:true
											    	});
											  	}
				  					    	}	
								   		  }); 	
					    		}
					    		
							});
							
					    },0);
					    
					    
					    jQuery("#login_confirm").click(function(){
					    
					    	var posTerminalId = jQuery("#posTerminalList").val();
					    
						    if(posTerminalId){
						    
						    
						    	jQuery.ajax({
										  url:'/webpos/control/login_step3',
										  type:"POST",
										  data:{
										  	"posTerminalId":posTerminalId
										  },
										  dataType:"json",
										  success: function(jsonObj){
										  		if(jsonObj.success){
										  			window.location.href = "/webpos/control/new_shop";
										  		}
				  					    	}	
								   		  }); 	
								   		  
								   		  
						    }
					    
					    });
					    
					    
					    
					    
					    
				  }
				})
	});






}

</script>


<style type="text/css">

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: normal;
}
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  /*margin-bottom: 10px;*/
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

</style>