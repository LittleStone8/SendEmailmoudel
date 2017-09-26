<div id="storeAddress">
	<div class="input_group clearfix">
	  <span>Delivery address</span>  
	                <select id="address_select_province" name="address_select_province">
	                    <option value="">select province</option>
	                </select>
	                <span class="addrenss_loading"></span>
	                <select id="address_select_city" name="address_select_city">
	                    <option value="">select city</option>
	                </select>
	                <span class="addrenss_loading"></span>
	                <select id="address_select_area" name="address_select_area">
	                    <option value="">select area</option>
	                </select>
	                <span class="addrenss_loading"></span>
	                <span class="error_info error_address" id="address_info"></span>              
	</div> 
	<div class="new_member_operator">
		<a class="confirm" id="confirm" href="javascript:void(0);">Confirm</a>
	</div>
</div>
<script>
window.onload = function(){
	var storeId=GetQueryString("productStoreId");
	var provinceGroup = document.getElementById("address_select_province");
	var cityGroup = document.getElementById("address_select_city");
	var areaGroup = document.getElementById("address_select_area");
	var errorinfo = document.getElementById("address_info");
	var confirm = document.getElementById("confirm");
	findDefaultAddress();
	var defaultProvince="";var defaultCity="";var defaultArea="";
	
	provinceGroup.onchange=function(){
		if(this.value){
			errorinfo.innerText="";
			findGeo(this.value,cityGroup);
		}		
	}
	cityGroup.onchange=function(){
		if(this.value){
			findGeo(this.value,areaGroup);
		}
	}
	confirm.onclick=function(){
		var provinceId=provinceGroup.value;
		var cityId=cityGroup.value;
		var areaId=areaGroup.value;
		if(!provinceId){
			errorinfo.innerText="please select provinces";
			return false;
		}
		jQuery.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/Store/control/updateStoreAddress',
	        data: {
	        	provinceId : provinceId,
	        	cityId:cityId,
	        	areaId:areaId,
	        	productStoreId:storeId
	        },
	        success: function (data) {
	            var dataResult = data;
	            console.dir(data);
	            if(dataResult.resultCode == 1){
	                alert(dataResult.resultMsg);
	                location.reload();
	            }
	            
	        }
	    });
	}
	function findDefaultAddress(){
		jQuery.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findDefaultAddress',
	        data: {
	        	productStoreId : storeId
	        },
	        success: function (data) {
	           var dataResult = data;
	           console.dir(data);	            
	           defaultProvince=data.provinceId;
	           defaultCity=data.cityId;
	           defaultArea=data.areaId;
	           findGeo("",provinceGroup,defaultProvince);
	           findGeo(defaultProvince,cityGroup,defaultCity);
	           findGeo(defaultCity,areaGroup,defaultArea);
	        }
	    });
	}	

	function GetQueryString(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	 function findGeo(option,elem,defaultVal){
		 	elem.setAttribute("class","loading");
		 	elem.setAttribute("disabled","disabled");
		 	elem.next(".addrenss_loading").style.display="block";
	        var list = [];
	        jQuery.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/Store/control/findGeo',
	            data: {
	               geoId : option,
	            },
	            success: function (data) {
	                var dataResult = data;
	                if(dataResult.responseMessage == "success"){
	                    list = dataResult.reslutList;
	                    elem.children[0].nextElementSibling&&elem.children[0].nextElementSibling.remove();	                    
	                    var childs = elem.children; 
	                    for(var i = 0; i < childs.length; i++) { 
	                    	  if(i>0){
	                    		elem.removeChild(childs[i]); 
	                    	}	                    	  
	                   }	  
	                    var str="";		
	                    list.length > 0 && jQuery.each(list, function(index, val){	                    		                    													
							if(val.id==defaultVal){																	
								str+="<option value='"+val.id+"' data-name='"+val.name+"' selected>"+val.name+"</option>";
	                    	}else{
	                    		str+="<option value='"+val.id+"' data-name='"+val.name+"'>"+val.name+"</option>";
	                    	}
							
	                     });
	                    elem.innerHTML=str;
	                }
	                
	                elem.className.replace("loading","");
	                elem.removeAttribute("disabled");
	                elem.next(".addrenss_loading").style.display="none";                
	            }
	        });

	    }
	
}
</script>