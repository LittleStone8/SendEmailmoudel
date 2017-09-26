<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/adjust-quantity.css">
<div id="newAdjustQuantity" style="/* display:none */">
	<div class="newAdjustQuantity_box">
		<div class="item_warehouse">
		 	<div><span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a></div>
		</div>
    	<div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
		<div class="right_column">
	    	<div class="newAdjustQuantity_content">
	    		<div class="newAdjustQuantity_tit">Adjust Quantities</div>
	    		<div class="newAdjustQuantity_tab">
					<ul id="newAdjustQuantityTab" class="nav nav-tabs">
					    <li class="active"><a href="javascript:void(0);" data-type="Adjust">Adjust non-serial Quantities</a></li>
					    <li><a href="javascript:void(0);" data-type="AdjustImei">Adjust IMEI Quantities</a></li>
					    <li><a href="javascript:void(0);" data-type="Log">Non-serial Quantities Log</a></li>
					     <li><a href="javascript:void(0);" data-type="LogImei">IMEI Quantities Log</a></li>					    
					</ul>
	    		</div>
	    		<div class="tab-content newAdjustQuantity_list">
				    <div class="tab-pane active" id="Adjust" data-type="Adjust">
				    	<div class="adjustQuantity_search">	
				    		<div class="adjustQuantity_search_item clearfix">
					    		  	<div class="search_item_div">
					    				<div class="adjustQuantity_search_span">Product ID</div>					    				
					    				<div class="adjust_row_input clearfix">
									        <input class="productId" type="text"  />
									        <a class="adjust_product_add" href="javascript:void(0)"></a>
								        </div>
						    		</div>				    		
						    		<div class="search_item_div">
						    			<span class="adjustQuantity_search_span adjustQuantity_span_qoh">Contains QOH0</span>
						    			<input class="adjustQuantity_qoh QOH" type="checkbox" />
						    		</div>
				    		</div>			    		
				    		
				    		<div class="adjustQuantity_search_item clearfix">
				    			<div class="adjustQuantity_search_span">Model</div>
				    			<input class="adjustQuantity_search_input model" type="text" />
				    		</div>	
				    					    		
				    		<div class="adjustQuantity_search_item">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="Adjust">Search Inventory Item</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="adjustQuantity_table">
				    	    <div class="adjustQuantity_table_title">Inventory Items</div>
				    		<table class="adjust_table_box" data-type="Adjust">
				    			<thead>
				    				<tr>
				    					<th style="width:50px;"></th>
				    					<th style="width:100px;">
					    					<div class="title_content">Product ID
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="productId desc"></span>
						    					<span class="arrow_down down_active" data-type="productId asc"></span>
						    					</div>
					    					</div>
				    					</th>
				    					<th style="width:200px;">
				    						<div class="title_content">Model
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="model desc"></span>
						    					<span class="arrow_down down_active" data-type="model asc"></span>
						    					</div>
					    					</div>
				    					
				    					</th>
				    					<th style="width:150px;">Product Attribute</th>
				    					<th style="width:80px;">
				    					<div class="title_content">QOH
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="qoh desc"></span>
						    					<span class="arrow_down down_active" data-type="qoh asc"></span>
						    					</div>
					    					</div>				    					
				    					</th>
				    					<th style="width:80px;">
				    					<div class="title_content">ATP
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="atp desc"></span>
						    					<span class="arrow_down down_active" data-type="atp asc"></span>
						    					</div>
					    					</div>				    					
				    					</th>
				    					<th style="width:130px;">Adjust Quantity</th>
				    					<th style="width:130px;">Adjust Reason</th>
				    					<th style="width:130px;">Reason</th>
				    					<th style="width:120px;">Adjust Log</th>				    					
				    				</tr>
				    			</thead>
				    			<tbody id="adjustTableTbody"></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="adjust_page"></div>
				    	</div>
				    	<div class="adjust_list">
				    		<a href="javascript:void(0);" class="adjust_apply" data-type="Adjust">APPLY</a>
				    	</div>
				    </div>
				    <div class="tab-pane" id="AdjustImei" data-type="AdjustImei">
				    	<div class="adjustQuantity_search">	
				    		<div class="adjustQuantity_search_item clearfix">
					    		  	<div class="search_item_div">
					    				<div class="adjustQuantity_search_span">IMEI</div>					    				
					    				<div class="adjust_row_input clearfix">
									        <input class="imei" type="text"  />									        
								        </div>
						    		</div>				    		
						    		<div class="search_item_div">
						    			<span class="adjustQuantity_search_span">Model</span>
						    			<div class="adjust_row_input clearfix">
									        <input class="model" type="text"  />									        
								        </div>
						    		</div>
				    		</div>			    						    						    						    					    		
				    		<div class="adjustQuantity_search_item ">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="AdjustImei">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="adjustQuantity_table">
				    	    <table class="adjust_table_box" data-type="AdjustImei">
				    			<thead>
				    				<tr>
				    					<th></th>
				    					<th>IMEI</th>
				    					<th>Description</th>
				    					<th>ATP</th>
				    					<th>Status</th>
				    					<th>Adjust Reason</th>
				    					<th>Adjust log</th>				    									    					
				    				</tr>
				    			</thead>
				    			<tbody id="adjustImeiTableTbody"></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="adjust_imei_page"></div>
				    	</div>
				    	<div class="adjust_list">
				    		<a href="javascript:void(0);" class="adjust_apply" data-type="AdjustImei">APPLY</a>
				    	</div>
				    </div>
				    <div class="tab-pane" id="Log" data-type="Log">
				    	<div class="adjustQuantity_search">
				    		<div class="adjustQuantity_search_item clearfix">
					    		<div class="log_search_div">
						    		<div class="adjustQuantity_search_span">Product ID</div>
					    			<div class="adjust_row_input clearfix">
										        <input class="productId" type="text"  />
										        <a class="adjust_product_add" href="javascript:void(0)"></a>
									</div>
					    		</div>
					    		<div class="log_search_div">
						    		<div class="adjustQuantity_search_span">Adjust Reason</div>
					    			<select class="adjustQuantity_search_select reason" name="reason">
					    			   <option value="" selected>please select</option>				    									    				
					    			</select>
					    		</div>				    			
				    		</div>				    			    		
				    		<div class="adjustQuantity_search_item clearfix">
				    			<div class="adjustQuantity_search_span">Model</div>
				    			<input class="adjustQuantity_search_input model" type="text" />
				    		</div>				    		
				    		<div class="adjustQuantity_search_item">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="Log">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="adjustQuantity_table">
				    		<table class="log_table_box" data-type="Log">
				    			<thead>
				    				<tr>
				    					<th style="width:120px;">Inventory Item ID</th>
				    					<th style="width:100px;">
				    						<div class="title_content">Product ID
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="productId desc"></span>
						    					<span class="arrow_down down_active" data-type="productId asc"></span>
						    					</div>
					    					</div>
					    				</th>
					    				<th style="width:200px;">Model</th>
				    					<th style="width:130px;">
				    					  <div class="title_content">Effective Date
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="effectiveDate desc"></span>
						    					<span class="arrow_down down_active" data-type="effectiveDate asc"></span>
						    					</div>
					    					</div>
				    					</th>
				    					<th style="width:130px;">Operator</th>
				    					<th style="width:80px;">ATP Diff</th>
				    					<th style="width:130px;">Adjust Reason</th>
				    					<th style="width:80px;">
				    					   <div class="title_content">ATP
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="atp desc"></span>
						    					<span class="arrow_down down_active" data-type="atp asc"></span>
						    					</div>
					    					</div>
				    					</th>
				    					<th style="width:80px;">
				    					   <div class="title_content">QOH
						    					<div class="icon_content">
						    					<span class="arrow_up no_up_active" data-type="qoh desc"></span>
						    					<span class="arrow_down down_active" data-type="qoh asc"></span>
						    					</div>
					    					</div>
				    					</th>				    					
				    				</tr>
				    			</thead>
				    			<tbody id="logTableTbody"></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="log_page"></div>
				    	</div>				    	
				    </div>
				   <div class="tab-pane" id="LogImei" data-type="LogImei">
				    	<div class="adjustQuantity_search">
				    		<div class="adjustQuantity_search_item clearfix">
					    		<div class="search_item_div">
					    				<div class="adjustQuantity_search_span">IMEI</div>					    				
					    				<div class="adjust_row_input clearfix">
									        <input class="imei" type="text"  />									        
								        </div>
						    		</div>				    		
						    		<div class="search_item_div">
						    			<span class="adjustQuantity_search_span">Model</span>
						    			<div class="adjust_row_input clearfix">
									        <input class="model" type="text"  />									        
								        </div>
						    		</div>			    			
				    		</div>				    			    						    					    		
				    		<div class="adjustQuantity_search_item">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="LogImei">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="adjustQuantity_table">
				    		<table class="log_table_box" data-type="LogImei">
				    			<thead>
				    				<tr>
				    					<th>IMEI</th>
				    					<th>Description</th>
					    				<th>ATP</th>
				    					<th>Status</th>
				    					<th>Adjust Reason</th>				    					
				    					<th>Operator</th>				    					
				    				</tr>
				    			</thead>
				    			<tbody id="logImeiTableTbody"></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="log_imei_page"></div>
				    	</div>				    	
				    </div>
				</div>
	    	</div>
    	</div>
    	
	</div>
</div>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/html" id="leftColumnContent">
<ul>
   {{if result}}
        {{each result as value i}}
            <li><a href="{{value.url}}">{{value.text}}</a></li>    
        {{/each}}                         
   {{/if}}
</ul>
</script>
<script type="text/html" id="adjustTableColumn">
{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.productId}}" >
             <td><input type="checkbox" value="" class="tbody_checkbox"/></td>
			 <td>{{value.productId}}</td>
			 <td>{{value.model}}<input type="hidden" class="adjust_model" value="{{value.model}}"/></td>
			 <td>{{value.description}}</td>
             <td>{{value.qoh | formatDiff: 'integer'}}</td>
			 <td>{{value.atp | formatDiff: 'integer'}}<input type="hidden" class="adjust_atp" value="{{value.atp | formatDiff: 'integer'}}"/></td>
			 <td>
					<div class="operate">
						<input type="number" class="tbody_edit variance"/>
						<div class="tbody_tips_content">
	                          <span class="tbody_tips"></span>
                              <div class="tips_content">1.the number of fill in the positive number to increase the inventory "1" <br/>2.the number of negative for the reduction of inventory "-1"
						    	 <div class="tips_arrow"></div>
							  </div>
						</div>
						
					</div>
			 </td>
			 <td>
				<div class="operate">
					<select class="tbody_edit varianceReason"></select>
				</div>
			</td>
			 <td>
					<div class="operate">
						<textarea rows="3" cols="18" class="commentsTextarea"></textarea>
						<input type="text" class="tbody_edit common"/>
						<div class="tbody_tips_content">
	                          <span class="tbody_tips"></span>
                              <div class="tips_content">1.fill in the specific reasons for the adjustment
						    	 <div class="tips_arrow"></div>
							  </div>
						</div>
						
					</div>
			 </td>
             <td><a href="javascript:void(0);" class="tbody_log" data-id="{{value.productId}}" data-category="Log">LOG</a></td>
			</tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="10">no data</td>
        </tr>
    {{/if}}
</script>
<script type="text/html" id="adjustImeiTableColumn">
{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.imei}}" data-status="{{value.status}}" {{if value.status=="INV_TRANSPORTING" || value.status=="INV_PROMISED"}} data-noedit {{/if}}>
             <td><input type="checkbox" value="" class="tbody_checkbox" {{if value.status=="INV_TRANSPORTING" || value.status=="INV_PROMISED"}} disabled {{/if}}/></td>
			 <td>{{value.imei}}</td>
			 <td>{{value.model}}  {{value.description}}</td>
             <td>
                {{value.atp | formatDiff: 'integer'}}
				<input type="hidden" class="adjust_atp" value="{{value.atp | formatDiff: 'integer'}}"/>
                <input type="hidden" class="itemId" value="{{value.itemid}}"/>
			</td>
			 <td>{{value.status}}</td>			 			
			 <td>
				<div class="operate">
					<select class="tbody_edit varianceImeiReason"></select>
				</div>
			</td>			 
             <td><a href="javascript:void(0);" class="tbody_log" data-id="{{value.imei}}" data-category="LogImei">LOG</a></td>
			</tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="7">no data</td>
        </tr>
    {{/if}}
</script>
<script type="text/html" id="reasonOptions">
{{if varianceReasons}}
      {{each varianceReasons as value i}}
         <option value="{{value.varianceReasonId}}">{{value.description}}</option>
      {{/each}}
   {{/if}}
</script>
<script type="text/html" id="pageContent">
	{{if totalNum>0}}
		<div class="goPage">
			<span>GO</span>
			<input type="text" data-big-num="{{totalPage}}" class="go_page_input" data-type="{{typeClassification}}"/>
		</div>
		<ul class="paging clearfix" id="fenye" data-type="{{typeClassification}}">
            <li ><a href="javascript:void(0)"  data-type="home" data-num="1" ><span  class="page_home"></span></a></li>
            <li ><a href="javascript:void(0)"  data-num="{{pageNum-1}}" ><span  class="page_prev"></span></a></li>
			{{each nums as value}}
                {{if value==pageNum}}
                    <li class='active'><a href="javascript:void(0)" data-num="{{value}}" >{{value}} <span class='sr-only'>{{value}}</span></a></li>
                {{/if}}
                {{if value!=pageNum}}
                    <li ><a href="javascript:void(0)" data-num="{{value}}">{{value}} <span class='sr-only'>{{value}}</span></a></li>
                {{/if}}
            {{/each}}
           <li ><a href="javascript:void(0)"  data-num="{{pageNum+1}}"><span  class="page_next"></span></a></li>
           <li ><a href="javascript:void(0)" data-type="final"  data-num="{{totalPage}}"><span class="page_final"></span></a></li>
		</ul>
		<div style="clear:both;"></div>
	{{/if}}
</script>
<script type="text/html" id="logTableColumn">
{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.productId}}" >
			 <td>{{value.inventoryItemId}}</td>
             <td>{{value.productId}}</td>
			 <td>{{value.model}}  {{value.description}}</td>
			 <td>{{value.effectiveDate}}</td>
			 <td>{{value.operator}}</td>
             <td>{{value.adjustNumber | formatDiff: 'integer'}}</td>
			 <td>{{value.adjustReason}}</td>
			 <td>{{value.atp}}</td>
			 <td>{{value.qoh}}</td>
            </tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="9">no data</td>
        </tr>
    {{/if}}
</script>
<script type="text/html" id="logImeiTableColumn">
{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.imei}}" >
			 <td>{{value.imei}}</td>
			 <td>{{value.model}}  {{value.description}}</td>
			 <td>{{value.atp | formatDiff: 'integer'}}</td>
			 <td>{{value.status}}</td> 
			 <td>{{value.adjustReason}}</td>             
			 <td>{{value.operator}}</td>
            </tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="6">no data</td>
        </tr>
    {{/if}}
</script>
<script>
require.config({
	paths:{
		stockProductDialogHtml:'/commondefine_js/tempate/productStock/stockProductDialog.html?v=1',
		stockProductDialog:'/commondefine_js/tempate/productStock/stockProductDialog',
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	template.helper('formatDiff', function(price, type) {
	    if(price){
	        var arrayPrice = price.toString().split(".");
	        if(type == 'integer') {
	            return arrayPrice[0]?arrayPrice[0]:"0";
	        }
	    }else{
	    	return price;
	    }
	});
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	"click #newAdjustQuantity .adjust_product_add":"findProduct",
        	"click #newAdjustQuantityTab a":"changeType",
        	"click #newAdjustQuantity .search_confirm":"findItem",
        	"dblclick .adjustQuantity_table tr":"dbOperationAdjust",
        	"click table .arrow_up,table .arrow_down":"orderBy",
        	"change .adjustQuantity_table .tbody_checkbox":"changeOperationAdjust",
        	"mouseover #adjustTableTbody .tbody_tips_content":"showTips",
        	"mouseout #adjustTableTbody .tbody_tips_content":"hideTips",
        	"click #adjustTableTbody .common":"showReasonText",
        	"blur #adjustTableTbody .commentsTextarea":"hideReasonText",
        	'click #newAdjustQuantity .footer_page a' : "changePageEvent",
        	"click #newAdjustQuantity .adjust_apply":"applyAdjust",
        	"click #newAdjustQuantity .tbody_log":"showLogData",
        	'keypress #newAdjustQuantity .go_page_input' : "jumpPageEvent",
        },
        __propertys__: function () {
        	this.formData={};        	
        	this.reasonData=[];
        	this.reasonImeiData=[];
        	this.href="/warehouse/control/physicalInventory";
        },        
        initialize: function ($super) {
        	$super();
        	this.$content=$("#newAdjustQuantity");
        	this.initRoleData();
        	this.initAdjustTableData(1);
        },
        initRoleData:function(){
        	var t = this;
        	$.ajax({
				url:'/warehouse/control/userPermission',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){
					if(results.resultCode==1){
						$(".warehouse_name").text(results.facility);
						$('.left_column_content').html(template("leftColumnContent",results));
						$(".left_column_content").find('a[href="'+t.href+'"]').parent('li').addClass("menu_active"); 
						
 						var currentUrl = location.pathname;
						var flag = true;
						if(results.result.length > 0){
							$.each(results.result, function(index, elem){
								if(currentUrl.indexOf(elem.url) != -1){
									flag = false;
								}
							})
						}
						if(flag){
							location.href =  '/warehouse/control/selectFacilityForm';
							return false;
						} 
						
					}					
				}
			})
        },
        initReasonData:function(type){
        	var t=this;
        	var url="";
        	var data=[];
        	var reasonElem;        	
        	switch(type){
	        	case"Adjust":
	        		url="/warehouse/control/queryVarianceReason";
	        		data=t.reasonData;
	        		reasonElem=t.$content.find(".varianceReason");
	        		break;
	        	case"AdjustImei":
	        		url="/warehouse/control/queryIMEIVarianceReason";
	        		data=t.reasonImeiData;
	        		reasonElem=t.$content.find(".varianceImeiReason");
	        		break;
        	}
        	if(data.length==0){
        		$.ajax({
    				url:url,
    				type: 'post',
    	            dataType: 'json',	            
    				success:function(results){					
    					if(results.resultCode==1){
    						type=="Adjust"?t.reasonData=results:t.reasonImeiData=results;
    						reasonElem.html(template("reasonOptions",results));
    						if(type=="Adjust"){
    							t.$content.find(".reason option").length>1&&t.$content.find(".reason option").gt(0).remove();
        						t.$content.find(".reason").append(template("reasonOptions",results));
    						} 
    						
    					}					
    				}
    			})   
        	}else{        		
        		reasonElem.html(template("reasonOptions",data));   
        		if(type=="Adjust"){
        			t.$content.find(".reason option").length>1&&t.$content.find(".reason option:gt(0)").remove();
    				t.$content.find(".reason").append(template("reasonOptions",data));
        		}
        		
        	}
        	
        	     	
        },
        changeType:function(e,type){
        	var t=this;  
        	var $that;
        	var dataType;
        	if(type){
        		$("#newAdjustQuantityTab a").each(function(index,elem){
        			if($(elem).attr("data-type")==type){
        				$that=$(elem);
        				return;
        			}
        		})
        		dataType=type;
        	}else{
        		$that=$(e.currentTarget);
        		dataType=$that.attr('data-type');
        	}
        	$that.closest('li').siblings().removeClass('active');
        	$that.closest('li').addClass('active');
        	t.$content.find('.tab-pane').removeClass('active');
        	t.$content.find('.tab-pane[data-type="'+ dataType +'"]').addClass('active');
        	switch(dataType){
	        	case"Adjust":
	        		t.initAdjustTableData(1);
	        		break;	        	
	        	case"AdjustImei":
	        		t.initAdjustImeiTableData(1);
	        		break;
	        	case"Log":
	        		$("#Log").find(".productId").val("");
	        		$('#logTableTbody').html(template("logTableColumn",{result:[]}));
	        		break;
	        	case"LogImei":
	        		$("#LogImei").find(".imei").val("");
	        		$('#logImeiTableTbody').html(template("logImeiTableColumn",{result:[]}));
	        		break;
        	}        	        	
        },
        orderBy : function(e){
        	var t = this;
        	var currentTarget = e.currentTarget;
        	var orderType = $(currentTarget).attr("data-type");
            if(orderType.indexOf("desc")>-1){
            	t.$content.find(".arrow_up").removeClass("no_up_active").addClass("up_active");
            	t.$content.find(".arrow_down").removeClass("down_active").addClass("no_down_active");
            }else{
            	t.$content.find(".arrow_down").removeClass("no_down_active").addClass("down_active");
            	t.$content.find(".arrow_up").removeClass("up_active").addClass("no_up_active");
            }
        	t.clickType = orderType;
        	var type=$(currentTarget).parents("table").attr("data-type");
        	var pageNum="";
        	if(type=="Adjust"){
        		pageNum = $(".adjust_page li.active a").attr('data-num');  
        		t.initAdjustTableData(pageNum,null,orderType);
        	}else{
        		pageNum = $(".log_page li.active a").attr('data-num');
        		t.initLogTableData(pageNum,"",null,orderType);        		
        	}
    		
        },
        jumpPageEvent:function(e){
			var t=this;
			var key = e.which;
			var reg= /^\b[1-9]\d*$/;
			if(key==13){					
				var typeClassification=$(e.currentTarget).attr("data-type");
				var currentPage=$(e.currentTarget).parents(".footer_page").find("li.active a").attr("data-num");
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))
						&&$(e.currentTarget).val()!=currentPage) {
					if(typeClassification=="Adjust"){
						t.initAdjustTableData($(e.currentTarget).val());
					}else{
						t.initLogTableData($(e.currentTarget).val());
					}
			    }
			}			
		},
        findItem:function(e){   
        	var t=this;
        	var type=$(e.currentTarget).attr("data-type");  
        	switch(type){
	        	case"Adjust":
	        		t.initAdjustTableData(1,e);
	        		break;
	        	case"AdjustImei":
	        		t.initAdjustImeiTableData(1,e);
	        		break;
	        	case"Log":
	        		t.initLogTableData(1,"",e);
	        		break;
	        	case"LogImei":
	        		t.initLogImeiTableData(1,"",e);
	        		break;
        	}        	        	
        },
        findProduct:function(e){        	
        	require(['stockProductDialog'], function(stockProductDialog){
            	var aa=new stockProductDialog(
            	{            		            		
            		onSelected:function(data){
            			$(e.currentTarget).parents(".adjust_row_input").find(".productId").val(data);
            		}
            	}
            	);            	            	
            });
        },
        changePageEvent:function(e){
        	var t = this;
        	var evt = e.currentTarget;
			if(t.page.pageNum==$(evt).attr('data-num')){
				return false;
			}
			if($(evt).attr('data-num')<1){
				return false;
			}
			if($(evt).attr('data-num')>t.page.totalPage){
				return false;
			}
			var type=$(evt).closest("ul").attr("data-type");			
			switch(type){
	        	case"Adjust":
	        		t.initAdjustTableData($(evt).attr('data-num'));
	        		break;
	        	case"AdjustImei":
	        		t.initAdjustImeiTableData($(evt).attr('data-num'));
	        		break;
	        	case"Log":
	        		t.initLogTableData($(evt).attr('data-num'));
	        		break;
	        	case"LogImei":
	        		t.initLogImeiTableData($(evt).attr('data-num'));
	        		break;
	    	}   
	    	
        },
        initAdjustTableData:function(pageNum,target,type){
        	var t=this;
        	t.getParametersData("adjust");
        	t.formData.pageNum=pageNum;
        	if(type){
        		t.formData.orderBy=type;
        	}        	
        	target&&target.currentTarget&&jQuery.showLoading(target.currentTarget);
        	$.ajax({
				url:'/warehouse/control/queryPhysicalInventory',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.formData),
				success:function(results){	
					target&&target.currentTarget&&jQuery.hideLoading(target.currentTarget);
					if(results.resultCode==1){						
						$('#adjustTableTbody').html(template("adjustTableColumn",results));
						var nums=t.getPageNumberList(results);
						results.nums=nums;
						results.typeClassification="Adjust";
						t.$content.find(".adjust_page").html(template("pageContent",results));
						t.initReasonData("Adjust");
					}
				}
			})
        },
        initAdjustImeiTableData:function(pageNum,target,type){
        	var t=this;
        	t.getParametersData("adjustImei");
        	t.formData.pageNum=pageNum;        	     	
        	target&&target.currentTarget&&jQuery.showLoading(target.currentTarget);
        	$.ajax({
				url:'/warehouse/control/queryIMEIPhysicalInventory',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.formData),
				success:function(results){	
					target&&target.currentTarget&&jQuery.hideLoading(target.currentTarget);
					if(results.resultCode==1){	
						
						$('#adjustImeiTableTbody').html(template("adjustImeiTableColumn",results));
						var nums=t.getPageNumberList(results);
						results.nums=nums;
						results.typeClassification="AdjustImei";
						t.$content.find(".adjust_imei_page").html(template("pageContent",results));
						t.initReasonData("AdjustImei");
					}
				}
			})
        },
       
        showLogData:function(e){
        
        	var t=this;
        	var tab=$("#newAdjustQuantityTab");
        	var dataId=$(e.currentTarget).attr("data-id");
        	var category=$(e.currentTarget).attr("data-category");        	
        	tab.find("li").removeClass("active");
        	tab.find("li").each(function(){
        		if($(this).find("a").attr("data-type")==category){
        			$(this).addClass("active");
        		}
        	})
        	t.$content.find('.tab-pane').removeClass('active');
        	t.$content.find('.tab-pane[data-type="'+category+'"]').addClass('active');
        	if(category=="Log"){
        		$("#Log").find(".productId").val(dataId);
        		t.initLogTableData(1,dataId);
        	}else{
        		$("#LogImei").find(".imei").val(dataId);
        		t.initLogImeiTableData(1,dataId);
        	}                	
        },
        initLogTableData:function(pageNum,productId,target,type){
        	var t=this;
        	t.getParametersData("log");
        	t.formData.pageNum=pageNum;        	
        	if(productId){
        		t.formData.productId=productId;
        	}   
        	if(!t.formData.productId&&!t.formData.model){       		
        		$('#logTableTbody').html(template("logTableColumn",{result:[]}));
        		t.$content.find(".log_page").html(template("pageContent",{}));
        		t.showToast("Please fill out the product ID or model for the query");
        		return;
        	}
        	if(type){
        		t.formData.orderBy=type;
        	}        	
        	target&&target.currentTarget&&jQuery.showLoading(target.currentTarget);
        	$.ajax({ 
				url:'/warehouse/control/queryAdjustInventoryLog',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.formData),
				success:function(results){	
					target&&target.currentTarget&&jQuery.hideLoading(target.currentTarget);
					if(results.resultCode==1){						
						$('#logTableTbody').html(template("logTableColumn",results));
						var nums=t.getPageNumberList(results);
						results.nums=nums;
						results.typeClassification="Log";
						t.$content.find(".log_page").html(template("pageContent",results));						
					}
				}
			})
                	
        },
        initLogImeiTableData:function(pageNum,imei,target){
        	var t=this;
        	t.getParametersData("logImei");
        	t.formData.pageNum=pageNum;        	
        	if(imei){
        		t.formData.imei=imei;
        	}           	
        	if(!t.formData.imei&&!t.formData.model){       		
        		$('#logImeiTableTbody').html(template("logImeiTableColumn",{result:[]}));
        		t.$content.find(".log_imei_page").html(template("pageContent",{}));
        		t.showToast("Please fill out the imei or model for the query");
        		return;
        	}    	
        	target&&target.currentTarget&&jQuery.showLoading(target.currentTarget);
        	$.ajax({ 
				url:'/warehouse/control/queryIMEIAdjustInventoryLog',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.formData),
				success:function(results){	
					target&&target.currentTarget&&jQuery.hideLoading(target.currentTarget);
					if(results.resultCode==1){						
						$('#logImeiTableTbody').html(template("logImeiTableColumn",results));
						var nums=t.getPageNumberList(results);
						results.nums=nums;
						results.typeClassification="LogImei";
						t.$content.find(".log_imei_page").html(template("pageContent",results));						
					}
				}
			})
                	
        },
        applyAdjust:function(e){
        	var t=this;
        	var adjustTbody;
        	var obj=[];
        	var type=$(e.currentTarget).attr("data-type");
        	var url="";
        	switch(type){
	        	case"Adjust":
	        		url="/warehouse/control/adjustPhysicalInventory";
	        		adjustTbody=$("#adjustTableTbody");
	        		var num=false;
	            	adjustTbody.find("tr").each(function(){
	            		if($(this).find(".tbody_checkbox").is(":checked")){
	            			var productId=$(this).attr("data-id");
	            			var model=$(this).find(".adjust_model").val();
	            			var totalNumber=$(this).find(".adjust_atp").val();
	            			var adjustNumber=$(this).find(".variance").val();
	            			var varianceReasonId=$(this).find(".varianceReason").val();
	            			var common=$(this).find(".common").val();
	            			if(!adjustNumber){
	            				num=true;
	            				return;
	            			}
	            			obj.push({"productId":productId,"adjustNumber":adjustNumber,"varianceReasonId":varianceReasonId,"common":common,"totalNumber":totalNumber,"model":model});
	            		}
	            	})
	            	if(num){
	            		t.showToast("Adjust the quantity of stock can not empty");
	            		return false;
	            	}
	        		break;
	        	case"AdjustImei":
	        		adjustTbody=$("#adjustImeiTableTbody");
	        		url="/warehouse/control/adjustIMEIPhysicalInventory";
	        		adjustTbody.find("tr").each(function(){
	            		if($(this).find(".tbody_checkbox").is(":checked")){	
	            			var imei=$(this).attr("data-id");
	            			var itemid=$(this).find(".itemId").val();
	            			var varianceImeiReasonId=$(this).find(".varianceImeiReason").val();	            			
	            			obj.push({"imei":imei,"varianceImeiReasonId":varianceImeiReasonId,"itemid":itemid,});
	            		}
	            	})
	        		break;
        	}
        	
        	e.currentTarget&&jQuery.showLoading(e.currentTarget);
        	console.dir(obj);
        	$.ajax({
				url:url,
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(obj),
				success:function(results){	
					e&&e.currentTarget&&jQuery.hideLoading(e.currentTarget);
					if(results.resultCode==1){						
						t.showToast("Adjust quantities success", function(){
   							t.changeType("",type);
   						});
					}else{
						t.showToast(results.resultMsg);
					}
				}
			})
        	
        },
        showReasonText:function(e){
        	$(e.currentTarget).parent(".operate").find("textarea").show().focus();       	
        },
        hideReasonText:function(e){
        	$(e.currentTarget).hide();
        	var val=$(e.currentTarget).val();
        	$(e.currentTarget).parent(".operate").find(".common").val(val);
        },
        dbOperationAdjust:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	if(typeof($that.attr("data-noedit"))!="undefined"){
        		return;
        	}
        	var status=$that.closest("tr").attr("data-status");
        	var tCheckbox=$that.find(".tbody_checkbox");        	
        	if(!tCheckbox.is(':checked')){
        		tCheckbox.prop("checked","checked");
        		$that.find(".operate").show();
        		status?t.initReasonByStatus(status,$that):"";
        	}        	        	        	
        },
        changeOperationAdjust:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	var status=$that.closest("tr").attr("data-status");
        	if($that.is(':checked')) {
        		$that.parents("tr").find(".operate").show();
        		$that.attr("checked","checked"); 
        		status?t.initReasonByStatus(status,$that):"";
        	}else{
        		$that.parents("tr").find(".operate").hide();
        		$that.attr("checked","false");
        	}
        },
        initReasonByStatus:function(status,that){
        	var t=this;        	
        	switch(status){
	        	case"INV_AVAILABLE":	        		
	        		console.dir(t.reasonImeiData);
	        		that.closest("tr").find(".varianceImeiReason option[value='VAR_FOUND']").remove();
	        		break;
	        	case"INV_DEACTIVATED":
	        		that.closest("tr").find(".varianceImeiReason").find("option[value='VAR_DAMAGED'],option[value='VAR_LOST'],option[value='VAR_STOLEN']").remove();
	        		break;
        	}
        },
        showTips:function(e){
        	$(e.currentTarget).find(".tips_content").fadeIn("fast");;
        },
        hideTips:function(e){
        	$(e.currentTarget).find(".tips_content").hide();
        },
        getPageNumberList : function(option){
        	var t = this;
        	
			t.page = {
				pageNum : option.pageNum,
				totalPage : option.totalPage
			};
			var nums = [];
			
			for(var i=option.startPage; i<= option.endPage; i++){
				nums.push(i);
			}
			
			return nums;
        },
        getParametersData:function(type){
        	var t=this;        	       	
        	t.formData={};         	
        	switch(type){
	        	case 'adjust':
	        		t.formData={
	        			productId:$("#Adjust").find(".productId").val(),
	        			model:$("#Adjust").find(".model").val(), 
	        			flag:$("#Adjust").find(".QOH").is(":checked")?"Y":"N",
	        			orderBy:"productId"
	        		}
	        		break;
	        	case 'adjustImei':
	        		t.formData={
	        			imei:$("#AdjustImei").find(".imei").val(),
	        			model:$("#AdjustImei").find(".model").val(), 	        			
	        		}
	        		break;
	        	case 'log':
	        		t.formData={
	        			productId:$("#Log").find(".productId").val(),
	        			model:$("#Log").find(".model").val(),
	        			varianceReason:$("#Log").find(".reason").val(),
	        			orderBy:"effectiveDate desc"
	        		}
	        		break;
	        	case 'logImei':
	        		t.formData={
	        			imei:$("#LogImei").find(".imei").val(),
	        			model:$("#LogImei").find(".model").val(), 	 
	        		}
	        		break;
        	}        	   	
        },        
        
	})
	new View();
	
})




</script>

















