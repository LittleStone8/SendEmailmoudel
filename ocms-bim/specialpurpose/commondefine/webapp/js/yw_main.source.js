//(function(){
//	
////	var nav_template = '<div><ui>\
////						<% _.each(data, function(webapp){  %>\
////							<li><a href="<%- webapp.linkUrl%>" title="<%- webapp.name %>"><%- webapp.name %></a></li>\
////					    <% }) %>\
////			    	</ui></div>';
//	
//	var nav_template ='<nav class="navbar">\
//						    <div class="container-fluid">\
//						    <div class="navbar-header">\
//								<a href="/opentaps/">\
//									<img alt="" src="/commondefine_img/yiwill_logo.png" border="0"/>\
//			  					</a>\
//						    </div>\
//							<div class="navbar-collapse collapse">\
//					        <ul class="nav navbar-nav">\
//								<% _.each(navInfoList, function(navInfo){  %>\
//								<% if(navInfo.linkUrl.indexOf("crmsfa") == -1 && navInfo.linkUrl.indexOf("purchasing") == -1 && navInfo.linkUrl.indexOf("ofbizsetup") == -1 && navInfo.linkUrl.indexOf("thirdpartyservice") == -1 ){ %>\
//									<li><a href="<%- navInfo.linkUrl.replace("/warehouse/control/main","/warehouse/control/inventoryMain")%>" title="<%- navInfo.name %>"><%- navInfo.name %></a></li>\
//								<% }}) %>\
//					        </ul>\
//							<ul class="nav navbar-nav navbar-right">\
//								<li class="dropdown">\
//									<a href="#" class="dropdown-toggle" data-toggle="dropdown">\
//											<%- languageMap.commonPreferences %>\
//										<b class="caret"></b>\
//									</a>\
//									<ul class="dropdown-menu">\
//										<li><a id="language"><%- languageMap.commonLanguageTitle %> - <%- locale %></a></li>\
//										<li><a href="/catalog/control/ListTimezones" id="timezone"><%- new Date(timeNow).toLocaleString() %> - <%- timeZoneDisplayName %></a></li>\
//									</ul>\
//								</li>\
//						        <li><span><%- languageMap.commonWelcome %>, <%- userLoginId %> </span></li>\
//						        <li><a id="logout" href="#">退出</a></li>\
//					        </ul>\
//					      </div>\
//						  </div>\
//						</nav>';
//		
//	var nav_compiled = _.template(nav_template);
//	
//	
//	var language_template = '<div id="language_modal" class="modal fade">\
//								<div class="modal-dialog">\
//									<div class="modal-content">\
//										<div class="modal-header">\
//											<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>\
//											<h4 class="modal-title"><%- languageMap.commonLanguageTitle %></h4>\
//										</div>\
//										<div class="modal-body">\
//												<ul id="language_list_ul" class="list-group">\
//													<% _.each(availableMap, function(value,key){  %>\
//														<button type="button" lang="<%- key %>" class="list-group-item"><span class="col-md-4 col-md-offset-4"><%- key %> <%- value %> </span></button >\
//													<% }) %>\
//												</ul>\
//										</div>\
//									</div>\
//								</div>\
//							</div>';
//	
//	var language_compiled = _.template(language_template);
//	
//	
//	
//	
//	jQuery(document).ready(function($){
//
//		 $.ajax({
//			  url:'/commondefine/control/findAppInfo',
//			  type:"get",
//			  data:{
//			  },
//			  contentType:"application/json; charset=utf-8",
//			  dataType:"json",
//			  success: function(jsonObj){
//				    var navInfoList = jsonObj.navInfoList;
//				    for (var i = 0,len= navInfoList.length; i < len; i++) {
//				    	var navInfo = navInfoList[i];
//				    	navInfo.linkUrl += "?externalLoginKey=" + jsonObj.externalLoginKey;
//					}
//				    var nav_html = nav_compiled(jsonObj);
//				    $("#yw_nav").html(nav_html);
//				    
//				    var language_html = language_compiled(jsonObj);
//				    $("#boot_modal").append(language_html);
//			  }
//			})
//			
//		$("#yw_nav").delegate("#language","click", function() {
//			$("#language_modal").modal()
//		});
//		 
//		 $("#yw_nav").delegate("#logout","click", function() {
//			 
//			  $.ajax({
//				  url:'/commondefine/control/logout',
//				  type:"get",
//				  data:{
//				  },
//				  contentType:"application/json; charset=utf-8",
//				  dataType:"json",
//				  success: function(jsonObj){
//					  
//				  }
//				})
//				
//		 });
//		 
//		 $("#boot_modal").delegate("#language_list_ul button","click", function(e) {
//			 $.ajax({
//				  url:'/commondefine/control/setSessionLocale',
//				  type:"get",
//				  data:{
//				  	  "newLocale" : $(this).attr('lang')
//				  },
//				  contentType:"application/json; charset=utf-8",
//				  dataType:"json",
//				  success: function(jsonObj){
//					  var externalLoginKey = jsonObj.externalLoginKey;
//					  var queryObj = yi_comm.fromQueryString(location.search);
//					  queryObj.externalLoginKey = externalLoginKey;
//					  location.search = "?"+yi_comm.toQueryString(queryObj);
//				  }
//				})
//			 
//		 });
//		
//		
//	});
//	
//	
//	
//	
//	
//	
//})();