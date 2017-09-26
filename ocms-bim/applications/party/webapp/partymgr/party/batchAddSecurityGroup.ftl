<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>add batch security group</title>
	<link rel="stylesheet" type="text/css" href="/commondefine_css/party/batchadd-security-group.css">
</head>  
<body>
<div id="add_security_group">
   <div class="party_user_login">
       <div class="party_title">Party id and User login</div>
       <div class="party_form">
           <div class="form_options clearfix">
              <div class="form_row clearfix">
                <span>Party ID</span>
                <div class="form_row_input clearfix">
					        <input type="text" id="partyId" value=""/>
					        <a class="find_party" href="javascript:void(0)" ></a>
				</div>
              </div>
              <div class="form_row">
                <span>User Login</span>
                <select id="userLoginId">
                    <option selected value="">please select</option>                  
                </select>
              </div>
              <div class="form_btn"><a href="javascript:void(0)" class="party_add">ADD</a></div>
           </div>
           <div class="form_group" id="partyUserGroup" style="display:none;">
               
              
           </div>
       </div>
   </div>
   <div class="security_group">
   		<div class="security_group_title">Security Group</div>
   		<div class="group_form">
   		    <div class="form_options clearfix">
   		        <div class="form_row_group">
   		           <span>Group</span>
   		           <select id="securityGroup">
   		              
   		           </select>
   		        </div>
	   		    <div class="form_row clearfix">
	                <span>From Date</span>
	                <input type="text" class="form_row_input laydate-icon sercurity_date" id="fromDate" value=""/>
	            </div>
	            <div class="form_row clearfix">
	                <span>Thru Date</span>
	                <input type="text" class="form_row_input laydate-icon sercurity_date" id="thruDate" value=""/>
	            </div>	
	            <div class="form_btn"><a href="javascript:void(0)" class="security_add">ADD</a></div>
   		    </div>
   		</div>
   		<div class="form_group">
   		   <div class="form_group_title clearfix">
   		       <div class="title_row_one">Group</div>
   		       <div class="title_row_two">From Date</div>
   		       <div class="title_row_three">Thru Date</div>
   		       <div class="title_row_four"></div>
   		   </div>
   		   <div class="group_content" id="securityGroupGather">   		         		   	  
   		   </div>
   		   
   		</div>
   </div>
   <div class="group_btn">
	   <div class="btn_div clearfix">
	      <a href="javascript:void(0);" class="btn_cancel">Cancel</a>
      	  <a href="javascript:void(0);" class="btn_submit">Submit</a>
	   </div>						      
   </div>
</div>

<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
</body>
<script type="text/html" id="securityGroupType">
		{{if SecurityGroupinfolist}}
        {{each SecurityGroupinfolist as value i}}
            <option value="{{value.groupId}}">{{value.groupId}} | {{value.description}}</option>    
        {{/each}}                         
   		{{/if}}
</script>
<script type="text/html" id="userLoginType">
		{{if SecurityGroupinfolist}}
        {{each SecurityGroupinfolist as value i}}
            <option value="{{value.userLoginId}}">{{value.userLoginId}}</option>    
        {{/each}}                         
   		{{/if}}
</script>
<script type="text/html" id="partyUserLogin">
		<div class="party_group">
                  <div class="party_data">
                     <span>{{partyId}}</span>:<span>{{userLoginId}}</span><input type="hidden" value=""/>
				     <input type="hidden" class="partyId" value="{{partyId}}"/>
                     <input type="hidden" class="userLoginId" value="{{userLoginId}}"/>
                  </div>
                  <span class="party_close"></span>
		</div>
</script>
<script type="text/html" id="securityGroupColumn">
		<div class="form_group_content clearfix">
	   		       <div class="content_row_one">
	   		           <span>{{securityGroup}}</span><input type="hidden" class="groupId" value="{{securityGroup}}"/>
	   		       </div>
	   		       <div class="content_row_two">
	   		           <span>{{fromDate}}</span><input type="hidden" class="fromDate" value="{{fromDate}}"/>
	   		       </div>
	   		       <div class="content_row_three">
	   		           <span>{{thruDate}}</span><input type="hidden" class="thruDate" value="{{thruDate}}"/>
	   		       </div>
	   		       <div class="content_row_four">
	   		           <a href="javascript:void(0)" class="security_remove">Remove</a>
	   		       </div>
   		   	  </div>
</script>
<script>
require.config({
	paths:{
		findPartyDialogHtml:'/commondefine_js/tempate/batchAddSecurityGroup/batchAddSecurityGroupDialog.html',		
		findPartyDialog:'/commondefine_js/tempate/batchAddSecurityGroup/batchAddSecurityGroupDialog',
		layDate:'/commondefine_js/layDate/laydate.min'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {	
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	'click #add_security_group .find_party':'showParty',
        	'click #add_security_group .party_add':'addPartyUserLogin',
        	'click #add_security_group .party_close':'deletePartyUserLogin',
        	'click #add_security_group .sercurity_date':'showDate',
        	'click #add_security_group .security_add':'addSecurityGroup',
        	'click #add_security_group .security_remove':'removeSecurityGroup',
        	'click #add_security_group .btn_submit':'submitSecurityGroup'        	
        },
        __propertys__: function () {
            
        },
        initialize: function ($super) {
        	$super();   
        	this.$security=$("#add_security_group");
        	this.initSecurityGroupData();
        },
        initSecurityGroupData:function(){
			var t=this;
			$.ajax({
				url:'/partymgr/control/FindSecurityGroupInfo',
				type: 'post',
	            dataType: 'json',
				success:function(results){			
					console.dir(results);
					t.$security.find("#securityGroup").html(template("securityGroupType",results));
					
				}
			})
		},
        showDate:function(e){
 		   require(['layDate'], function(layDate){
 			   layDate({
 				   eventArg: e,
 				   istime: true,
 				   format: 'YYYY-MM-DD hh:mm:ss'
 			   });
 		   });
        },  
        showParty:function(){  
        	var t=this;
        	require(['findPartyDialog'], function(findPartyDialog){
             	var partyDialog=new findPartyDialog(
             	{
             		onSelected:function(partyId){
             			t.$security.find("#partyId").val(partyId);             			
             			$.ajax({
            				url:'/partymgr/control/Findloginbypartyid?partyId='+partyId,
            				type: 'post',           	            
            				success:function(results){			
            					console.dir(results);
            					if(results.responseMessage=="success"){
            						t.$security.find("#userLoginId").append(template("userLoginType",results));
            					}
            					
            					
            				}
            			})
             		}
             	}
             )
           })
        },
        addPartyUserLogin:function(){
        	var t=this;
        	var isRepeat=false;
        	var partyId=t.$security.find("#partyId").val();
        	var userLoginId=t.$security.find("#userLoginId").val();
        	if(!partyId||!userLoginId){
        		t.showToast("party id or user login can not empty!");
    			return false;
        	}
        	t.$security.find(".party_data").each(function(){
        		var partyIdExist=$(this).find(".partyId").val();
        		var userLoginIdExist=$(this).find(".userLoginId").val();
        		if(partyId==partyIdExist&&userLoginId==userLoginIdExist){
        			t.showToast("party id and user login can not repeat!");
        			isRepeat=true;
        			return false;
        		}
        	})
        	if(!isRepeat){
        		this.$security.find("#partyUserGroup").show().append(template("partyUserLogin",{"partyId":partyId,"userLoginId":userLoginId}));
        	}
        	
        },
        deletePartyUserLogin:function(e){        	
        	$(e.currentTarget).parent(".party_group").fadeOut("fast",function(){
        		$(this).remove();        		
            	if($("#partyUserGroup").find(".party_group").length==0){
            		$("#partyUserGroup").hide();
            	}
        	});
        	
        },
        addSecurityGroup:function(){
        	var t=this;
        	var isRepeat=false;
        	var isEmpty=false;
        	var groupId=t.$security.find("#securityGroup").val(); 
        	var fromDate=t.$security.find("#fromDate").val();  
        	var thruDate=t.$security.find("#thruDate").val();  
			t.$security.find(".form_group_content").each(function(){        		
        		var groupIdExist=$(this).find(".groupId").val();       		
        		if(groupId==groupIdExist){
        			t.showToast("security group can not repeat!");
        			isRepeat=true;
        			return false;
        		}
        	});
			if(isRepeat){
				return false;
			}
			if(!fromDate||!thruDate){
				t.showToast("form date or thru date can not empty!");
				isEmpty=true;
    			return false;
			}
			if(isEmpty){
				return false;
			}		
			t.$security.find("#securityGroupGather").append(template("securityGroupColumn",
	        			{"securityGroup":groupId,"fromDate":fromDate,"thruDate":thruDate}
	        ));
			
        	
        },
        removeSecurityGroup:function(e){
        	$(e.currentTarget).parent().parent(".form_group_content").fadeOut("fast",function(){
        		$(this).remove();
        	});
        },        
        submitSecurityGroup:function(e){
        	var t=this;
        	var form={};
        	var partyinfolist=[];
        	var securityGrouplist=[];
        	t.$security.find(".party_data").each(function(){
        		var partyId=$(this).find(".partyId").val();
        		var userLoginId=$(this).find(".userLoginId").val();
        		partyinfolist.push({"partyId":partyId,"userLoginId":userLoginId});
        	})
        	t.$security.find(".form_group_content").each(function(){
        		
        		var groupId=$(this).find(".groupId").val();
        		var fromDate=$(this).find(".fromDate").val();
        		var thruDate=$(this).find(".thruDate").val();
        		securityGrouplist.push({"groupId":groupId,"fromDate":fromDate,"thruDate":thruDate});
        	});
        	form={"partyinfolist":partyinfolist,"securityGrouplist":securityGrouplist};
        	if(partyinfolist.length==0||securityGrouplist.length==0){
        		t.showToast("user login or security group can not empty!");
        		return false;
        	}
        	e.currentTarget&&jQuery.showLoading(e.currentTarget);
        	$.ajax({
				url:'/partymgr/control/BatchAddSecurityGroupInfo',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(form),
				success:function(results){	 
					e.currentTarget&&jQuery.hideLoading(e.currentTarget);
					console.dir(results);
					if(results.responseMessage=="success"){
						t.showToast(results.responseMessage);
						location.reload();
					}
					
					
				}
			})
        	
        }
        
	})
	new View();
})

</script>