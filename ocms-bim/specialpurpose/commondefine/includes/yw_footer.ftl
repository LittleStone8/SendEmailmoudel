<div id="boot_modal"></div>
<div id="yiwill_footer">
    <div class="copyright_content">
    <#if currenttime?has_content>
         <a href="ListTimezones" class="time_zones">
         <span>${currenttime}</span> - 
         <span>${ timeZoneId}</span>	</a>
    </#if>
        <p>All Right Reserved(c) 2016-2017 YIWILL - www.yiwill.com</p>
    </div>
</div>
<script>
	jQuery(function(){
		//显示修改密码----优化之后引用jquery
        var userLoginId = jQuery('.userLoginId');
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
	})
</script>
<!-- Piwik -->
<script type="text/javascript">
  var _paq = _paq || [];
  /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
   <#if userLogin?has_content>
            _paq.push(['setUserId', '${userLogin.userLoginId}']);
   </#if>
  _paq.push(['trackPageView']);
  _paq.push(['trackPageView']);
  _paq.push(['enableLinkTracking']);
  (function() {
    var u="//${piwikHost}<#if piwikPort?has_content>:${piwikPort}</#if>/";
    _paq.push(['setTrackerUrl', u+'piwik.php']);
    _paq.push(['setSiteId', '${piwikSiteId}']);
    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
  })();
</script>
<!-- End Piwik Code -->
</body>
</html>

