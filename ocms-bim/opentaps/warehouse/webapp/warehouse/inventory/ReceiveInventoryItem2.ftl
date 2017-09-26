<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>inventory report</title>
	<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/inventory-item.css">

</head>  
<body style="display:none">
<div class="item_warehouse"><div><span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a></div></div>
<div id="item_contentarea">
    
    <div class="left_content_column">
       <div class="left_section">
          <div class="left_section_titlebar">
             <div class="titlebar_name">Shortcuts</div>
          </div>
          <div class="left_section_catalog">
             
          </div>
       </div>
    </div>
    <div class="center_main center_active">
    <div class="center_content_column">
        <div class="center_titlebar">
           <div class="center_titlebar_name">Receive Items</div>
        </div>
        <div class="center_tab ">
           <ul class="tab_nav clearfix">
           		<li class="active"><a>IMEI/SN</a><input type="hidden" value="SERIALIZED_INV_ITEM"/></li>
           		<li><a>Non-Serialized</a><input type="hidden" value="NON_SERIAL_INV_ITEM"/></li>
           </ul>
        </div>
        <div id="receive_item">	        
	        <div class="item_div" data-type="SERIALIZED_INV_ITEM">
	            <div class="item_row_receive clearfix"> 
			         <a  type="button" class="receive" href="javascript:void(0)">Receive</a>
			         <a  type="button" class="add" href="javascript:void(0)">Add</a>
			     </div>
	        </div>
	        <div class="item_div" data-type="NON_SERIAL_INV_ITEM" style="display:none" >
	            <div class="item_row_receive clearfix"> 
			        <a  type="button" class="receive" href="javascript:void(0)">Receive</a>
			        <a  type="button" class="add" href="javascript:void(0)">Add</a>
			    </div>
	        </div>
        </div>
         
        
    </div>
    
    
    
    </div>
    <div class="right_content_column right_active">
       
    </div>
</div>

<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>



</body>
<script type="text/html" id="itemPermission">
<ul>
   {{if result}}
        {{each result as value i}}
            <li><a href="{{value.url}}">{{value.text}}</a></li>    
        {{/each}}                         
   {{/if}}
</ul>
</script>
<script type="text/html" id="NON_SERIAL_INV_ITEM">
<div class="receive_item_form recevice_nohover_close">
               <div class="item_close"></div> 
				<div class="item_arrow more_down"></div>
                <div class="item_num"><span class="num_triangle"></span><span class="num_text">{{itemNum}}</span></div>
			   	<form class="clearfix" data-num="{{itemNum}}">                   
					<div class="item_row clearfix">
				        <span>Product ID</span>
						
                       		 <span class="item_row_span" {{if !productId}}style="display:none;"{{/if}}>{{productDesc}}</span>
						
						
				        <div class="item_row_input recevice_nohover clearfix" {{if productId}}style="display:none;"{{/if}}>
					        <input type="text" class="productId" value="{{productId}}" ean="{{ean}}"/>
					        <a class="item_add" href="javascript:void(0)" data-type="productId"></a>
				        </div>
						
                        <span class="item_verify" data-type="productId"></span>				       
				      </div>
					  <div class="item_row clearfix">
        				<span>EAN</span>
        				<input type="text" class="ean"/>
        				<span class="item_verify" data-type="ean"></span>
      				  </div>					 
				      <div class="item_row clearfix">
        				<span>Quantity Received</span>
        				<input type="text" class="quantityAccepted"/>
        				<span class="item_verify quantityVerify" data-type="quantityAccepted"></span>
      				  </div>
                      <div class="item_more" style="display:none">
                           <div class="item_row clearfix">
				              <span>Item Description</span>
				              <input type="text" class="itemDescription"/>
				           </div>
		                   <div class="item_row clearfix">
				               <span>Date Received</span>
			                   <input class="item_row_input item_date laydate-icon recevice_nohover clearfix js_sndate" value="{{nowTime}}"/>
						   </div>
	                       <div class="item_row clearfix">
                               <span>Per Unit Price</span>
                               <input type="text" class="unitCost"/>
							   <span>{{uomId}}</span>
                               <span class="item_verify" data-type="unitCost"></span>
                           </div> 
	                       <div class="item_row clearfix">
                               <span>Lot Id</span>
         <div class="item_row_input recevice_nohover clearfix">
            <input type="text" class="lotId"/>
            <a class="item_lot_add" href="javascript:void(0)"  data-type="lotId"></a>
         </div>	
         <a class="item_lot_create" href="javascript:void(0)">Create New</a>
      </div>
      
       
</div>
				     
				      
				     
			   </form>
				

			</div>
</script>
<script type="text/html" id="SERIALIZED_INV_ITEM">
<div class="receive_item_form recevice_nohover_close">
				<div class="item_close"></div> 
				<div class="item_arrow more_down"></div>
				<div class="item_num"><span class="num_triangle"></span><span class="num_text">{{itemNum}}</span></div>
			   	
				<form class="clearfix" data-num="{{itemNum}}">
					<div class="item_row clearfix">
				        <span>Product ID</span>
				        <span class="item_row_span" {{if !productId}}style="display:none;"{{/if}}>{{productDesc}}</span>
				        <div class="item_row_input recevice_nohover clearfix" {{if productId}}style="display:none;"{{/if}}>
					        <input type="text" class="productId" value="{{productId}}" ean="{{ean}}"/>
					        <a class="item_add" href="javascript:void(0)" data-type="productId"></a>
				        </div>
                        <span class="item_verify" data-type="productId"></span>				       
				    </div>
					<div class="item_row clearfix">
        				<span>EAN</span>
        				<input type="text" class="ean"/>
        				<span class="item_verify" data-type="ean"></span>
      				  </div>
					<div class="item_row">
        					<span>Quantity Received</span>
        					<input type="text" class="quantityAccepted" disabled/>																       
      				</div>
                    <div class="item_row_code clearfix">
						<div class="row_imei"><span class="imeiCode">IMEI / SN</span></div>
						<div class="code_div ">
            				<input type="text" class="flick"/>
						</div>
						<span class="item_verify" data-type="serialNumber"></span>
						<div class="code_total"></div>        
     				</div>
					<div class="item_more" style="display:none;">
						<div class="item_row clearfix">
                        	<span>Date Received</span>
							<input class="item_row_input item_date  laydate-icon recevice_nohover clearfix js_recdate" value="{{nowTime}}" />
						</div> 
                        <div class="item_row">
        					<span>Per Unit Price</span>
        					<input type="text" class="unitCost"/>
							<span>{{uomId}}</span>
							<span class="item_verify" data-type="unitCost"></span>				       
      					</div>
      
						</div>	  
				</form>
</div>
</script>

<script type="text/html" id="codes">		      
		{{if codes&&codes.length>0}}
	       {{each codes as value i}}
              <div class="code_group">
              <div class="serial_number">{{value}}<input type="hidden" value="{{value}}"/></div>
              <span class="code_close"></span>
              </div>
	       {{/each}}
        {{/if}}
		
    
		
</script>
<script type="text/javascript" src="/commondefine_js/timezoneTime.js"></script>
<script>
require.config({
	paths:{
		findProductHtml:'/commondefine_js/tempate/inventoryItem/findProduct.html',
		findProduct:'/commondefine_js/tempate/inventoryItem/findProduct',
		findLotHtml:'/commondefine_js/tempate/inventoryItem/findLot.html',
		findLot:'/commondefine_js/tempate/inventoryItem/findLot',
		createLotDialogHtml:'/commondefine_js/tempate/inventoryItem/createLotDialog.html',
		createLotDialog:'/commondefine_js/tempate/inventoryItem/createLotDialog',
		layDate:'/commondefine_js/layDate/laydate'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
           'click .left_content_column li':'selectMenu',
        	'click #receive_item .item_add': 'findProductLotData',
        	'click #receive_item .add': 'addItemTemplate',
           'blur #receive_item .item_row_input':'leaveClearHigh',
           'focus .item_row_input input':'focusInput',
           'click #receive_item .item_close':'closeItem',
           'click .center_tab li':'changeType',
           'click #receive_item .item_arrow':'showItem',
           'click #receive_item .item_lot_add':'findProductLotData',
           'click #receive_item .item_lot_create':'createLot',
           'keypress #receive_item .flick':'flickAddIMEICodes',
           "keypress #receive_item .ean":"flickAddEANCodes",
          'click .item_row_receive .receive':'receiveItem',
           'click .right_content_column .right_arrow':'showHideIds',
           'click #receive_item .code_close':'closeCode',
           'input #receive_item .productId':'changeProductId',
           'change #receive_item .unitCost':'changeUnitCost',
           'change #receive_item .quantityAccepted':'changeQuantityAccepted',
           "change #receive_item .ean":"changeEan",
           'click #receive_item .item_date_icon':'showDate',
			'click #receive_item .js_recdate, #receive_item .js_sndate':'showDate'
        },
        __propertys__: function () {
            this.codesData =[];
            this.codesEANData=[];
            this.formData=[];
            this.itemNum=1;
            this.type="SERIALIZED_INV_ITEM";
            this.uomId="";
            this.href="/warehouse/control/receiveInventoryItem";
        },
        initialize: function ($super) {
            $super();   
            this.initData();
            this.initLeft();//左侧三级菜单距离   
            this.initType(this.type);
           	this.initShowProductId("","productId");
            this.initEvent();                          
        },
        initType:function(type){
        	var t=this;
             $("#receive_item").find(".item_div").each(function(){
            	 if($(this).attr("data-type")==type){
            		 t.$form=$(this);
            		$(this).find(".receive").before(template(type,{itemNum:t.itemNum,nowTime:t.currentTime(),uomId:t.uomId}));               		
            	 }            	    
             })       	
        },
        initData:function(){
        	var t=this;
        	$.ajax({
				url:'/warehouse/control/userPermission',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){	
					if(results.resultCode==1){
						$(".warehouse_name").text(results.facility);
						$('.left_section_catalog').html(template("itemPermission",results));
						$(".left_content_column").find("a[href='"+t.href+"']").parent('li').addClass("menu_active");
					}					
				}
			})
			$.ajax({
				url:'/catalog/control/findPriceUOM',
				async: false,
				type: 'post',
	            dataType: 'json',
	           	success:function(results){	
					if(results.responseMessage=="success"){
						t.uomId=results.uomMap.uomId;						
					}				
				}
			})
        },
        
        showItem:function(e){
        	var item_more=$(e.currentTarget).parent(".receive_item_form").find(".item_more");        
        	if(item_more.is(':hidden')){
        		item_more.fadeIn();
        		$(e.currentTarget).removeClass("more_down").addClass("more_up");
        	}else{
        		item_more.fadeOut();
        		$(e.currentTarget).removeClass("more_up").addClass("more_down");
        	}        	
        },
        selectMenu:function(e){
        	$(e.currentTarget).addClass("menu_active").siblings("li").removeClass("menu_active");
        },
        initEvent:function(){
        	var t=this;
        	$(window).resize(function(){
        		t.initLeft();
        	})       	
        	t.$form.on("mouseover",".receive_item_form",function(){
        		$(this).removeClass("recevice_nohover_close").addClass("recevice_hover").find(".item_close").show();
        	})
        	t.$form.on("mouseleave",".receive_item_form",function(){
        		$(this).removeClass("recevice_hover").addClass("recevice_nohover_close").find(".item_close").hide();
        	})
        },
        closeItem:function(e){
        	var t=this;        	
        	if(t.$form.find(".receive_item_form").length>1){
        		var item=$(e.currentTarget).parent(".receive_item_form");
        		item.fadeOut("fast",function(){        			
        			$(this).remove();        			        			
        			t.itemNum--;       			
        			var index=$.inArray(item.find(".ean").val(),t.codesEANData);
    				if(item.find(".ean").val()&&index!=-1){
    					t.codesEANData.splice(index,1);
    				}
    				var items=t.$form.find(".receive_item_form");
        			$.each(items,function(index,elem){        				
        				$(elem).find(".num_text").text(index+1);
        				$(elem).find("form").attr("data-num",index+1);
        				
        			})
                	
        		})
        	       		
        	}
        	
        },
        closeCode:function(e){
        	var t=this;
        	$.each(t.codesData,function(index,elem){
        		if(elem.codes==$(e.currentTarget).parent(".code_group").find("input").val()){
        	    	t.codesData.splice(index,1);
        	    	return false;
        	    }
        	})             
        	var currentLength=$(e.currentTarget).closest(".code_total").find("input").length-1;
			$(e.currentTarget).closest("form").find(".quantityAccepted").val(currentLength);
        	$(e.currentTarget).closest(".code_group").remove();           	        	
        },
       showDate:function(e){
		   require(['layDate'], function(layDate){
			   layDate({
				   eventArg: e,
				   istime: true,
				   format: 'DD/MM/YYYY hh:mm:ss'
			   });
		   });
       },
       addItemTemplate:function(e){
    	   var t=this;
    	   var tag=false;
    	   if(t.$form.find(".productId").length==5){
					t.showToast("you can only receive five items");
					return false;
			}			            					             				             				
 			if(!tag){		             				         		             				       		             				
 				var content=template(t.type,{"itemNum":t.$form.find(".receive_item_form").length+1,"nowTime":t.currentTime(),"uomId":t.uomId})         		             				
 				t.$form.find(".receive").before(content);	         		             				          						            				        		             					
 				return;
 			}
       },
        changeProductId:function(e){        	
        	this.ruleSets("productId",$(e.currentTarget).val(),e);
        },
        changeUnitCost:function(e){        	
        	var reg=new RegExp("^(0|([1-9]\\d*))(\\.\\d+)?$");
    		var verify="unitCost is illegal";
        	this.ruleSets("unitCost",$(e.currentTarget).val(),e,reg,verify);
        },
        changeQuantityAccepted:function(e){
        	var reg=new RegExp("^[0-9]*[1-9][0-9]*$");        	       	
        	var verify="please fill in  integer";
        	this.ruleSets("quantityAccepted",$(e.currentTarget).val(),e,reg,verify);
        },        
        changeEan:function(e){
        	var reg=new RegExp("(^([0-9]{8})$)|(^([0-9]{13})$)");
        	var verify="please enter correct EAN code";
        	this.ruleSets("ean",$(e.currentTarget).val(),e,reg,verify);         		
        },
        ruleSets:function(name,value,e,reg,verify){
        	var resetVal="";
        	switch(name){
	        	case"productId":
	        		if(e instanceof jQuery){
	            		e.parent(".item_row_input").removeClass("recevice_vertify").siblings(".item_verify").text(resetVal);
	            	}
	            	if(value){
	            		$(e.currentTarget).parent(".item_row_input").removeClass("recevice_vertify").next(".item_verify").text(resetVal);
	            	}
	        		break;
	        	case"unitCost":
	        		if(reg.test(value)){
	        			$(e.currentTarget).removeClass("recevice_vertify").siblings(".item_verify").text(resetVal);
	        		}else{
	        			$(e.currentTarget).siblings(".item_verify").text(verify);
	        		}
	        		break;
	        	case"quantityAccepted":
	        		if(value&&!reg.test(value)){
	            		$(e.currentTarget).hasClass("recevice_vertify")?"":$(e.currentTarget).addClass("recevice_vertify");
	            		$(e.currentTarget).next(".item_verify").text(verify);
	            	}else if(value&&reg.test(value)){
	            		$(e.currentTarget).removeClass("recevice_vertify").siblings(".item_verify").text(resetVal);
	            	}
	        		break;	 
	        	case"ean":	        		
	        		if(value&&!reg.test(value)){
	            		$(e.currentTarget).hasClass("recevice_vertify")?"":$(e.currentTarget).addClass("recevice_vertify");
	            		$(e.currentTarget).next(".item_verify").text(verify);
	            	}else{
	            		$(e.currentTarget).removeClass("recevice_vertify").siblings(".item_verify").text(resetVal);
	            	}
	        		break;	 
        	}
        },
        receiveItem:function(target){           	
        	var t=this;
        	t.formateData();
        	if(t.formData.length<1){
        		return;
        	}
        	var isPass=t.validateFormData();
        	if(isPass){
        		target.currentTarget&&jQuery.showLoading(target.currentTarget);
        		jQuery.ajax({
    				url:'/warehouse/control/receiveSingleInventoryProduct',
    				type: 'post',
    	            dataType: 'json',
    	            data:JSON.stringify(t.formData),
    				success:function(results){	
    					target.currentTarget&&jQuery.hideLoading(target.currentTarget);     					
    					if(results.resultCode==1){    
    						t.showToast(results.resultMsg);
    						$(target.currentTarget).siblings(".receive_item_form").remove();
    						t.initType(t.type);
    					}
    					
    					if(results.errorProducts&&results.errorProducts.length>0){
    						t.showToast(results.errorMessage);
    						var productIds=results.errorProducts;
    						$("#receive_item").find(".item_div:eq(0) .productId").each(function(index,elem){
    							if($.inArray($(this).val(), productIds)>-1){
    								$(this).closest("form").find(".ean").addClass("recevice_vertify").next(".item_verify").text(results.errorMessage);
    							}
    						})
    						return;
    					}
    					t.showToast(results.resultMsg);
    				}
    			})
    			
        	}
        	t.formData=[];   
        	t.codesData =[];
            t.codesEANData=[];
        },
        validateFormData:function(){
        	var t=this;
        	var isPass=true;           	
        	for(var i=0;i<t.formData.length;i++){
        		var ifVerify="";
        		if(t.formData[i].productId==""){        		   
            		t.toggleMessage("productId",i,"product ID can not be empty");
        		    isPass=false;
        		}
        		if(t.formData[i].serialNumber==""){
        			t.toggleMessage("serialNumber",i,"serialNumber can not be empty");
        		    isPass=false;
        		}
        		
				if(t.formData[i].inventoryItemTypeId=="NON_SERIAL_INV_ITEM"&&t.formData[i].quantityAccepted==""){				
            		t.toggleMessage("quantityAccepted",i,"item Type if is Non-Serialized,quantityAccepted can not be empty");
        		    isPass=false;
        		}
				var reg=new RegExp("(^([0-9]{8})$)|(^([0-9]{13})$)");
				if(t.formData[i].inventoryItemTypeId=="NON_SERIAL_INV_ITEM"&&t.formData[i].ean&&!reg.test(t.formData[i].ean)){            		
            		t.toggleMessage("ean",i,"please enter correct EAN code");
        		    isPass=false;
            	}
				var quantityReg=new RegExp("^[0-9]*[1-9][0-9]*$");
				if(t.formData[i].inventoryItemTypeId=="NON_SERIAL_INV_ITEM"&&t.formData[i].quantityAccepted&&!quantityReg.test(t.formData[i].quantityAccepted)){				
            		t.toggleMessage("quantityAccepted",i,"please fill in  integer");
        		    isPass=false;
        		}
        		var reg=new RegExp("^(0|([1-9]\\d*))(\\.\\d+)?$");
        		if(t.formData[i].unitCost&&!reg.test(t.formData[i].unitCost)){        			
            		t.toggleMessage("unitCost",i,"please used Arabic numerals");
        			isPass=false;
        		}
        		
				
        	}
        	return isPass;
        },
        toggleMessage:function(name,index,text,status){
        	var t=this;
        	t.$form.find("form").eq(index).find(".item_verify").each(function(){
        		if($(this).attr("data-type")==name){        			
        			switch(name){
	        			case"productId":
	        				$(this).closest(".item_row").find(".item_row_input").addClass("recevice_vertify");
	        				break;
	        			case"serialNumber":	        				
	        				$(this).closest(".item_row_code").find(".flick").addClass("recevice_vertify");
	        				break;
	        			default:
	        				$(this).closest(".item_row").find("input").addClass("recevice_vertify");
	        				break;
        			}        			
    				$(this).text(text);
    			}
        	})
        },
        formateData:function(){
        	
        	var t=this;
 
        	t.$form.find(".receive_item_form").each(function(index){
        		var itemForms={};
        		var $that=$(this);
        		itemForms.productId=$that.find(".productId").val().trim();
        		itemForms.inventoryItemTypeId=t.type;  
        		itemForms.ean=$that.find(".ean").val();
        		if(t.type=="NON_SERIAL_INV_ITEM"){
            		itemForms.lotId=$that.find(".lotId").val().trim();
            		itemForms.quantityAccepted=$that.find(".quantityAccepted").val();
            	}else{
            		itemForms.serialNumber="";
                	$that.find(".serial_number").each(function(){
            			itemForms.serialNumber+=$(this).find("input").val()+",";
            		})
            	}            	
            	itemForms.itemDescription=$that.find(".itemDescription").val();             	            	
            	itemForms.datetimeReceived=(getTimezoneTime($that.find(".item_date").val(),"dd/MM/yyyy HH:mm:ss")).toString();            	
        		itemForms.unitCost=$that.find(".unitCost").val();
        		itemForms.uomId=t.uomId;
            	t.formData.push(itemForms);
        	})        	
        },
        leaveClearHigh:function(e){
        	$(e.currentTarget).removeClass("recevice_hover").addClass("recevice_nohover");
        },
        
        changeType:function(e){            	
        	var t=this;        	
        	
        	$(e.currentTarget).addClass("active").siblings("li").removeClass("active");
        	var type=$(e.currentTarget).find("input").val();        	
        	$("#receive_item").find(".item_div").each(function(){
        		if($(this).attr("data-type")==type&&$(this).is(":hidden")){
        			t.itemNum=1;
        			$(this).find(".receive_item_form").remove();
        			$(this).find(".receive").before(template(type,{itemNum:t.itemNum,"nowTime":t.currentTime(),"uomId":t.uomId}));
        			$(this).show().siblings().hide(); 
        			t.type=type;
        			t.codesEANData=[];
        			t.$form=$(this);
        			t.initEvent();
        			return false;
        		}
        	})         	
        },
        showHideIds:function(e){
        	var t=this;
        	if($(".right_content_column").attr('class').indexOf('right_active')>-1){
        		t.openRightContent(0);
        	}else{
        		t.openRightContent(1);
            	t.initShowProductId(e.currentTarget,"productId");
        	}       	
        },
        
        initShowProductId:function(target,type){
        	var t=this;
        	switch (type){
	   		 case "productId":
	   			 require(['findProduct'], function(findProduct){
	             	var aa=new findProduct(
	             	{
	             		el:$(".right_content_column"),
	             		target:target,
	             		onSelected:function(data){	             			
             						 t.$form.find(".receive_item_form").mouseleave();
             						 if(data.target){//如果是点击图标选择的product
             							t.showProductModelDesc($(data.target),data);
             							return;
             						 }
             						var tag=false;
     	             				if(t.$form.find(".productId").length==5){
     	             					t.showToast("you can only receive five items");
     	             					return false;
     	             				}
     	             				t.$form.find(".productId").each(function(){ 
     		             			      if($(this).val()==data.productId){		             			    	  
     		             			    	  $(this).parents(".receive_item_form").mouseover();
     		             			    	  tag=true;
     		             			    	  return false;
     		             			      }
     		             			      if(!$(this).val()){         		             			    	  
     		             			    	t.showProductModelDesc($(this),data);
                 							tag=true;
                 							return false;
     		             			      }
     		             			})	             					             				             				
     		             			if(!tag){		             				         		             				       		             				
     		             				var content=template(t.type,{"productId":data.productId,"productDesc":data.productDesc,"itemNum":t.$form.find(".receive_item_form").length+1,"nowTime":t.currentTime(),"ean":data.EAN,"uomId":t.uomId})         		             				
     		             				t.$form.find(".receive").before(content);	         		             				          						            				        		             					
     		             				return;
     		             			} 	       	             			         	             			
         	             			if(t.$form.find(".receive_item_form").length>1){
         	             				t.$form.find(".receive_item_form").css("border-bottom","1px solid #dbdbdb");
         	             			}         	             			          	             		
	             		}
	             	}
	             	);

	             });
	   			 break;
	   		 case "lotId":
	   			require(['findLot'], function(findLot){
	            	var aa=new findLot(
	            	{
	            		el:$(".right_content_column"),
	            		onSelected:function(data){	   
	            			var isHave=false;
	            			if(data){
	            				t.$form.find(".lotId").each(function(){
	            					if($(this).val()==data){           						
	            						isHave=true;
	            						return false;
	            					}
	            				})
	            				if(isHave){
	            					t.showToast("lot id can not repeat");
	            					$(target).prev(".lotId").focus();
	            					return false;
	            				}
	            				$(target).prev(".lotId").val(data);
	            				}
	            			t.openRightContent(0);
	            			
	            		}
	            		
	            	}
	            	);
	            	
	            	
	            });
	   			 break;
	   	 	}
        },
        showProductModelDesc:function(self,data){
        	var t=this;
        	
        	var itemRow=self.closest(".item_row");
        	var ean=itemRow.closest(".receive_item_form").find(".ean").val();        	
        	if(data.EAN&&ean&&ean!=data.EAN){
        		var str="<div class='validateEanText'>Sorry, the EAN code cannot match with this product ID .<br>"+
        		"1. First please check the EAN code again.<br>2. If the EAN code input is right, please contact the adminstator.</div>";
     			$("#item_contentarea").append(str);
        		 clearTimeout(timeOut);
         		var timeOut=setTimeout(function(){
         			$("#item_contentarea").find(".validateEanText").remove();
         		},2000);
        		return;
        	}
        	itemRow.find(".productId").val(data.productId).attr("ean",data.EAN);
			itemRow.find(".item_row_span").show().text(data.productDesc);
			itemRow.find(".item_row_input").hide();
			itemRow.find(".item_verify").text("");	
        },
        findProductLotData:function(e){        	
        	var t=this;
        	$(e.currentTarget).parent(".item_row_input").removeClass("recevice_nohover").addClass("recevice_hover");
        	var havaIds=$(".right_arrow").attr("data-ids");
        	if(havaIds==$(e.currentTarget).attr("data-type")&&$('.right_content_column').attr('class').indexOf('right_active')>-1){//右侧内容和箭头指示内容一致并且已经是打开状态
        		if($(e.currentTarget).prev().val()==""){//如果没值
        			t.initShowProductId(e.currentTarget,$(e.currentTarget).attr("data-type"),true);            		
        		}else{//如果有值
        			 t.openRightContent(1);
        		}        		
        	}else{
        		 t.openRightContent(1);
    			if($(e.currentTarget).prev().val()==""){
        			t.initShowProductId(e.currentTarget,$(e.currentTarget).attr("data-type"));            		
        		}else{
        			t.initShowProductId(e.currentTarget,$(e.currentTarget).attr("data-type"));  
        		}
        		
        	}      	
        },
        setProductId:function(e){
        	this.productDialog.remove();
			$("#productId").val($(e.currentTarget).text());
        },
       
        createLot:function(e){
        	var t=this;
        	require(['createLotDialog'], function(createLotDialog){
            	var aa=new createLotDialog(
            	{            		
            		nowTime:t.currentTime(),
            		onSelected:function(data){
            			$(e.currentTarget).parents(".item_row").find(".lotId").val(data);
            		}
            	}
            	);
            	
            	
            });
        },
        flickAddEANCodes:function(e){
        	var t=this;        
        	var $that=$(e.currentTarget);
        	var key = e.which;
		    if (key == 13&&$.trim($that.val())) {
		    	console.dir("----------------------")
		    	console.dir(t.codesEANData)
		    	var val=$.trim($that.val());
		    	var reg=new RegExp("(^([0-9]{8})$)|(^([0-9]{13})$)");
		    	var verify="please enter correct EAN code";
	        	if(!reg.test(val)){
	        		$that.hasClass("recevice_vertify")?"":$that.addClass("recevice_vertify");
	        		$that.next(".item_verify").text(verify);
		    		return;
	        	}
	        	var prev=$that.closest(".item_row").prev();
	        	var ean=prev.find(".productId").attr("ean");	        	
	        	if(ean&&ean!=val){//先选product并有ean，ean不等于输入的ean，提示	        		
	        		var str="<div class='validateEanText'>Sorry, the EAN code cannot match with this product ID .<br>"+
	        		"1. First please check the EAN code again.<br>2. If the EAN code input is right, please contact the adminstator.</div>";
	     			$("#item_contentarea").append(str);
	        		 clearTimeout(timeOut);
	         		var timeOut=setTimeout(function(){
	         			$("#item_contentarea").find(".validateEanText").remove();
	         			$that.val("");	
	         		},2000);
	        		return;
	        	}
	        	if(ean&&ean==val){//先选product并有ean，ean等于输入的ean，匹配
	        		$that.closest(".item_row").next().find(".quantityAccepted").focus();
	        		$that.closest(".item_row").siblings().find(".flick").focus();
	        		t.codesEANData.push(val);
	        		return;
	        	}
	        	t.showProductDescByEan($that,val);  	
		    }		   
        },
        showProductDescByEan:function($that,val){
        	var t=this;
        	$.ajax({
				url:'/warehouse/control/findProductByEAN',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify({"EAN":val}),
				success:function(results){						
					var itemRow=$that.closest(".receive_item_form");
					var productId=itemRow.find(".productId").val();
					if(results.flag){//输入的ean有product						
						if(productId&&results.isNew=="Y"){//先选product并没有ean，输入的ean有product，提示
							var str="<div class='validateEanText'>Sorry, the EAN code cannot match with this product ID .<br>"+
			        		"1. First please check the EAN code again.<br>2. If the EAN code input is right, please contact the adminstator.</div>";
			     			$("#item_contentarea").append(str);
			        		 clearTimeout(timeOut);
			         		var timeOut=setTimeout(function(){
			         			$("#item_contentarea").find(".validateEanText").remove();
			         			$that.val("");	
			         		},2000);						
							return false;
						}				
						if($.inArray(val,t.codesEANData)!=-1){//选择的ean重复了,提示
							t.showToast("EAN is duplicated. Please re-scan the code",function(){
								$that.val("");
							});							
							return false;
						}
						var brandName=results.result.brandName;
						var internalName=results.result.internalName?" | "+results.result.internalName:"";
						var productAttributes=results.result.productAttributes?" | "+results.result.productAttributes:"";
						var productDesc=brandName+internalName+productAttributes;
						itemRow.find(".productId").val(results.result.productId).attr("ean",val);
						itemRow.find(".item_row_span").show().text(productDesc);
						itemRow.find(".item_row_input").hide();
						itemRow.find(".item_verify").text("");	
						$that.closest(".item_row").next().find(".quantityAccepted").focus();
						$that.closest(".item_row").siblings().find(".flick").focus();
						t.codesEANData.push(val);
						
					}else{//输入的ean没有product
						var prev=$that.closest(".item_row").prev();
			        	$that.removeClass("recevice_vertify").siblings(".item_verify").text("");
			        	t.codesEANData.push(val);
			        	if(!productId){//先输入ean，没有product,没有选择product
			        		prev.find(".item_verify").text("The EAN code will be match with this product id, please choose carefully.");
				        	prev.find(".item_add").addClass("item_add_click");
				        	$that.closest(".item_row").prev().find(".productId").focus();
				        	return;
			        	}
			        	if(productId&&results.isNew=="Y"){//先输入ean，没有product，选择的product也没有ean,提示通知匹配
				        	t.showToast("The EAN code will be match with this product id");			
			        	}        	
			        	$that.closest(".item_row").next().find(".quantityAccepted").focus();
			        	$that.closest(".item_row").siblings().find(".flick").focus();
			        	
					}
				}
			});
        	
        },
        flickAddIMEICodes:function(e){
        	var t=this;        	
        	var key = e.which;        	
		    if (key == 13&&$.trim($(e.currentTarget).val())) {
		    	var val=$.trim($(e.currentTarget).val());		    	
		    	if(val.indexOf("|")!=-1){
		    		val=val.replace(/[\|]/g,"");
		    	}
		    	if(val.length>15&&val.length%15==0){//批量扫的时候
		    		var reg=new RegExp("^[0-9]*$");
		        	if(!reg.test(val)){
		        		t.showToast("Please enter correct IMEI code",function(){
		        			$(e.currentTarget).val("");
			    		});
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
		    		var tag=true;
	    			for(var i=0;i<t.codesData.length;i++){
	    				for(var v=0;v<arrs.length;v++){
							if(t.codesData[i].codes==arrs[v]){		    			
				    			tag=false; 
				    			t.showToast("IMEI is duplicated. Please re-scan the code",function(){
				    				$(e.currentTarget).val("");
				    			});		    			
				    			break;
				    		}
				    	}
	    			}	    			
	    			addImeiToCode(tag,"batch");
					
		    	}else{
		    		var reg=new RegExp("^([0-9]{15})$");
		        	if(!reg.test(val)){
		        		t.showToast("Please enter correct 15-digits IMEI code",function(){
		        			$(e.currentTarget).val("");
			    		});
			    		return;
		        	}
			    	var type=true;
			    	var value={"codes":[]};
			    	for(var i=0;i<t.codesData.length;i++){
			    		if(t.codesData[i].codes==val){		    			
			    			type=false; 
			    			t.showToast("IMEI is duplicated. Please re-scan the code",function(){
			    				$(e.currentTarget).val("");
			    			});		    			
			    			break;
			    		}
			    	}
			    	if(val.indexOf("012345678912456")>-1){
			    		$(e.currentTarget).val("");
			    		return false;
			    	}
			    	
			    	addImeiToCode(type,"single");
		    	}
		    	
		    	function addImeiToCode(flag,type){
		    		if(flag){
			    		switch(type){
				    		case"batch":
				    			$.each(arrs,function(index,val){
			    					var value={"codes":[]};
			    					value.codes.push(val);			    		
							    	$(e.currentTarget).closest(".code_div").next().next(".code_total").prepend(template("codes",value));
							        t.codesData.push(value);			       
			    				})
				    			break;
				    		case"single":
				    			value.codes.push($(e.currentTarget).val());			    		
						    	$(e.currentTarget).closest(".code_div").next().next(".code_total").prepend(template("codes",value));
						        t.codesData.push(value);			       
				    			break;
				    		}
			    		$(e.currentTarget).val("");
	    				if($(e.currentTarget).closest(".code_div").next().text()!=""){ 
				    		$(e.currentTarget).removeClass("recevice_vertify").closest(".code_div").next().text("");
				    	}	    				
				        var currentLength=$(e.currentTarget).closest(".code_div ").siblings(".code_total").find("input").length;
				        $(e.currentTarget).closest("form").find(".quantityAccepted").val(currentLength);
			    	}
		    	}
		    	
		    	
		    	
		    }		   
        },
        focusInput:function(e){
        	$(e.currentTarget).parent(".item_row_input").removeClass("recevice_nohover").addClass("recevice_hover");
        },
       
        initLeft:function(status){
        	$(document.body).show();
        	var left=$(".yiwill_header_list").offset().left;
        	var centerLeft=parseInt($(".left_content_column").width())+left+30;        	        	
        	$(".left_content_column").css("left",left<=0?"10px":left);
        	$(".center_main").css("margin-left",centerLeft-10);
        },
        openRightContent:function(type){
        	if(type==1){//打开状态
        		$(".center_main").removeClass("center_no_active").addClass("center_active");
            	$(".right_content_column").removeClass("right_no_active").addClass("right_active");
        	}else{//关闭状态
        		$(".center_main").removeClass("center_active").addClass("center_no_active");
            	$(".right_content_column").removeClass("right_active").addClass("right_no_active");
    			
        	}
        	
        },
        currentTime:function(){  
        	var timeNow = document.getElementById('timeNow');
        	var nowDate;        	
            if(timeNow){
            	nowDate=getFixNewDate(parseInt(timeNow.value),"dd/MM/YYYY hh:mm:ss");
            }           
           return nowDate;
		},       
        
      
	})
	
	
	new View();
})
 
</script>






