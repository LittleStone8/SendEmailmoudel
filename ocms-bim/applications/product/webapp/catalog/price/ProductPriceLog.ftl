
<link rel="stylesheet" type="text/css" href="/commondefine_css/category/promotion.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/need/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/skins/blue/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/ui-dialog.css">

<div id="priceLog" style="display:none">
	<div class="priceLog_box">
	   <div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
    <div class="center_content">
        <div id="findProductPriceLog">
            <div class="search_box">
				<div class="search_box_tit">Find Product Price Log</div>
				<div class="search_box_content clearfix">
					<div class="item_row input_group">
				        <div class="left_label">Product ID</div>
				        <input type="text" class="productId" placeholder="Product ID / Model / Brand"/>
				        <ul class="product_ul" style="display:none">
				        </ul>
				    </div>				   
				    <div class="item_row">
				        <div class="left_label">Store</div>
				        <select class="inventoryFacility">
				           <option selected value="">please select</option>
				        </select>
				    </div>
				    <div class="item_row item_row_date">
				        <div class="left_label">Last Modified Date</div>
				        <input type="text" class="lastDate laydate-icon" id="start"/>
				        <span>-</span>
				        <input type="text" class="lastDate laydate-icon" id="end"/>
				    </div>
				    <div class="item_row">
				        <a href="javascript:void(0)" class="search_btn">Search</a>
				    </div>
				    
				</div>
			</div>
			<div class="list_box">			    		
				<table class="list_box_table">
					<thead>
						<tr>
							<td><a href="javascript:void(0)" data-type="producId"><span>Product ID</span><span class="table_arrow"></span></a></td>
							<td><a href="javascript:void(0)" data-type="description"><span>Description</span><span class="table_arrow"></span></a></td>
							<td><a href="javascript:void(0)" data-type="store"><span>Store</span><span class="table_arrow"></span></a></td>
							<td>Current Price(GHS)</td>
							<td><a href="javascript:void(0)" data-type="lastModifiedDate"><span>Last Modified Date</span><span class="table_arrow"></span></a></td>	
							<td></td>							
						</tr>
					</thead>
					<tbody id="tbody">
					</tbody>
				</table>
				<div class="pageBox clearfix"></div>
			</div>
        </div>
        <div id="findProductPriceLogDetail" style="display:none;">
            <div class="search_box">
				<div class="search_box_tit clearfix"><a class="log_return clearfix"><span class="log_detail_return"></span><span>Return</span></a><span class="log_detail_title">Find Product Price Log</span></div>				
			</div>
			<div class="list_box">				
				<table class="list_box_table" id="logPriceTable">
					<thead>
						<tr>
							<td>Product ID</td>
							<td>Description</td>
							<td>Facility</td>
							<td>Current Price(GHC)</td>
							<td>Commencement Date</td>
							<td>Last Modified Date</td>
							<td>Operator</td>													
						</tr>
					</thead>
					<tbody id="tbody">
					</tbody>
				</table>
				<div class="pageBox clearfix"></div>
			</div>
        </div>
			
    </div>
	</div>
    
</div>
<script type="text/html" id="storeType">
		{{if store}}
        {{each store as value i}}
            <option value="{{value.productStoreId}}">{{value.storeName}}</option>    
        {{/each}}                         
   		{{/if}}
</script>
<script type="text/html" id="productData">
		{{if items}}
        {{each items as value i}}
            <li data-id="{{value.id}}" class="product_li">{{value.productId}}/{{value.model}}/{{value.brand}}</li>  
        {{/each}}                         
   		{{/if}}
</script>
<script type="text/html" id="leftColumnContent">
<ul>
    {{if findProductp=='Y'}}<li><a href="/catalog/control/ProductPriceRule">Find Product Price</a></li>{{/if}}
	{{if findLog=='Y'}}<li class="menu_active"><a href="/catalog/control/ProductPriceLog">Find Product Price Log</a></li> {{/if}}
	{{if batchManage=='Y'}}<li><a href="/catalog/control/BatchManagerPrice">Batch Manage Price</a></li>{{/if}}
</ul>
</script>
<script type="text/html" id="priceTbodyColumn">

	{{if resultList.length>0}}
		{{each resultList as value}}
			<tr>
			 <td>{{value.producId}}</td>
			 <td>{{value.description}}</td>
             <td>{{value.store}}</td>
			 <td>{{value.currentPrice}}</td>
			 <td>{{value.lastModifiedDate}}</td>
			 <td style="width:90px;"><a class="log_cz" href="javascript:void(0)" data-num="{{value.producId}}" data-store="{{value.storeId}}">log</a></td>			
           </tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="6">No data found.</td>
			</tr>
	{{/if}}	 
</script>
<script type="text/html" id="priceDetailTbodyColumn">

	{{if resultList.length>0}}
		{{each resultList as value}}
			<tr {{if value.isInUse=="Y"}}class="detail_select"{{/if}}>
			 <td>{{value.productId}}</td>
			 <td>{{value.description}}</td>
             <td>{{value.facility}}</td>
			 <td>{{value.currentPrice}}</td>
			 <td>{{value.commencementDate}}</td>
			<td>{{value.lastModifieldDate}}</td>
			<td>{{value.operationer}}</td>
             </tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="7">No fund data</td>
			</tr>
	{{/if}}			 
</script>
<script type="text/html" id="pricePage">
	
	{{if totalNum>0}}<div class="goPage"><span>GO</span><input type="text" data-big-num="{{totalPage}}" data-type="{{type}}" class="go_page_input"/></div>{{/if}}
	<ul class="paging clearfix" id="fenye" data-type="{{type}}">
	    {{if totalNum>0}}
              <li ><a href="javascript:void(0)"  data-type="home" data-num="1" ><span  class="page_home"></span></a></li>
            
                 <li ><a href="javascript:void(0)"  data-num="{{pageNum-1}}" ><span  class="page_prev"></span></a></li>
                
			
			{{each nums as value}}
                {{if value==pageNum}}
                    <li class='active'><a href="javascript:void(0)" data-num="{{value}}" >{{value}}</a></li>
                {{/if}}
                {{if value!=pageNum}}
                    <li ><a href="javascript:void(0)" data-num="{{value}}">{{value}}</a></li>
                {{/if}}
            {{/each}}
           
           <li ><a href="javascript:void(0)"  data-num="{{pageNum+1}}"><span  class="page_next"></span></a></li>
           
          <li ><a href="javascript:void(0)" data-type="final"  data-num="{{totalPage}}"><span  class="page_final"></span></a></li>
		  
{{/if}}
	</ul>
    
	
</script>

<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/javascript" src="/commondefine_js/timezoneTime.js"></script>
<script>
require.config({
	paths:{	
		layDate:'/commondefine_js/layDate/laydate.min'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	var View = Inherit.Class(AbstractView, {
		el: 'document',
        events: {        	
        	"click #findProductPriceLog .log_cz" : "showLogDetai",
        	"click #findProductPriceLog #start":"showStartDate",
        	"click #findProductPriceLog #end":"showEndDate",
        	"keyup #findProductPriceLog .productId":"searchProductId",
        	"click #findProductPriceLog .product_li":"selectProductId",
        	"click #findProductPriceLog thead a":"searchProductPriceLogOrderBy",
        	"click #findProductPriceLog .search_btn":"selectPriceLog",
        	"click  .paging a":"selectPriceLogByPage",
        	"keypress  .go_page_input":"goToPriceLogPage",
        	"click #findProductPriceLogDetail .log_return":"returnToLog"
        },
        __propertys__: function () {        	
        	this.timeOut = null;
        	this.timer = 1000;
        	this.log={};
        	this.detail={};
        },
        initialize: function ($super) {
        	 $super();         	
        	 this.$content=$("#findProductPriceLog");
        	 this.$content.find("#start").val(this.setNowDate());
        	 this.$content.find("#end").val(this.setNowDate());
        	 this.initMenuData();
        	 this.initFormData();
        	 this.selectPriceLog(null,1);
        	 this.otherEvent();
        },       
        initMenuData:function(){
        	$.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/permission',	            
	            success: function (data) {
	                var dataResult = data;
	                if(dataResult.responseMessage == 'success'){
	                	if(dataResult.findLog=='Y'){
	                		$("#priceLog").show();
	                	}
	                	$('.left_column_content').html(template("leftColumnContent",dataResult));
	                }
	            }
	        });
        	
        },  
        initFormData:function(){
        	var t=this;
        	$.ajax({
                type: 'post',
                dataType: 'json',
                url: '/catalog/control/initProductPriceRule',                
                success: function (data) {
                    var dataResult = data;
                    if(dataResult.responseMessage == 'success'){
                    	//$('.left_column_content').html(template("leftColumnContent",{}));
                    	t.$content.find('.inventoryFacility').append(template("storeType",data)); 
                    }
                }
            });
        },  
        otherEvent:function(){ 
        	var t=this;
        	$(document).click(function(){
        		t.$content.find('.product_ul').hide();        		        		
        	})
        },
        searchProductId:function(e){
        	var t=this;
        	var query=$(e.currentTarget).val().trim();
        	var product_ul=t.$content.find(".product_ul");
        	if(query == ''){
	    		clearTimeout(t.timeOut);
	    		product_ul.hide();
	    		return false;
	    	}
        	clearTimeout(t.timeOut);
	    	t.timeOut = setTimeout(function(){
		        $.ajax({
		            type: 'post',
		            dataType: 'json',
		            url: '/catalog/control/categoryPriceRuleFindProduct',
		            data: {
		            	query:query
		            },
		            success: function (data) {
		                var dataResult = data;
		                if(dataResult.responseMessage == 'success'){
		                   product_ul.show().html(template("productData",data))
		                }
		            }
		        }); 
	    	} ,t.timer);  
        },
        selectProductId:function(e){
        	var dataId = $(e.currentTarget).attr('data-id');
    		var dataValue = $(e.currentTarget).text();    		
    		this.$content.find(".productId").val(dataValue);
    		this.$content.find(".productId").attr("data-id",dataId);    		
    		this.$content.find(".product_ul").hide();
        },
        showStartDate:function(e){        	
        	var t=this;        	
        	require(['layDate'], function(laydate){         		
        		console.dir(laydate.now());
        		laydate({
   				 	  eventArg: e,
		   			  format: 'DD/MM/YYYY',
		   			  max:$("#end").val(),
		   			  istime: true,
		   			  istoday: false,            					   			  
		   			});
        	})
        }, 
        showEndDate:function(e){
        	var t=this;        	
        	require(['layDate'], function(laydate){        		
        		laydate( {
	    			  eventArg: e,
	      			  format: 'DD/MM/YYYY',
	      			  min: $("#start").val(),
	      			  istime: true,
	      			  istoday: false,     			 
      				});
        	})
        }, 
        selectPriceLog:function(e,pageNum,orderBy){
        	var t=this;
        	var productId="";        	  
        	if(t.$content.find(".productId").attr("data-id")){
        		productId=t.$content.find(".productId").attr("data-id");
        	}else{
        		productId=t.$content.find(".productId").val().trim();
        	}
        	var store=t.$content.find(".inventoryFacility").val();
        	var startDate=t.$content.find("#start").val();
        	var endDate=t.$content.find("#end").val();
        	e&&e.currentTarget&&jQuery.showLoading(e.currentTarget);
        	$.ajax({
                type: 'post',
                dataType: 'json',
                url: '/catalog/control/priceRuleFindProductPrice',
                data: {
                	productId:productId,
                	storeId:store,
                	beginDate:startDate,
    				endDate:endDate,
    				rawOffset:hoursOffset,
    				isAsc:"Y",
    				orderBy:orderBy?orderBy:"productId",
    				pageNum:pageNum?pageNum:1   				
                },
                success: function (data) {
                	e&&e.currentTarget&&jQuery.hideLoading(e.currentTarget);
                    var dataResult = data;
                    if(dataResult.responseMessage == 'success'){
                    	t.$content.find('#tbody').html(template("priceTbodyColumn",data));
                    	var nums=[];
                    	for(var i=data.startPage; i<= data.endPage; i++){
							nums.push(i);
						}
                        t.log={
                        		totalNum : data.totalNum,
                				pageNum : data.pageNum,
                				totalPage : data.totalPage,
                				nums : nums,
                				type:'log'
                			};
                    	t.$content.find('.pageBox').html(template('pricePage',t.log))
                    }else if(dataResult.responseMessage == 'error'){
                    	t.showToast(data.errorMessage);
                    }
                }
            });
        	
        	
        },
        selectPriceLogByPage:function(e){
        	var t=this;       	
        	var type=$(e.currentTarget).parents("ul").attr("data-type");
			if($(e.currentTarget).attr('data-num')==t[type].pageNum){
        		return false;
        	}
        	if($(e.currentTarget).attr("data-type")&&$(e.currentTarget).attr("data-type")=='home'&&t[type].pageNum=="1"){
				return false;
			}
			if($(e.currentTarget).attr("data-type")&&$(e.currentTarget).attr("data-type")=='final'&&t[type].pageNum==$(e.currentTarget).attr('data-num')){
				return false;
			}
			if($(e.currentTarget).attr('data-num')<1){
				return false;
			}
			if($(e.currentTarget).attr('data-num')>t[type].totalPage){
				return false;
			}
			if(type=="log"){
				t.selectPriceLog(null,$(e.currentTarget).attr('data-num'));
			}else if(type=="detail"){
				t.selectPriceLogDetail($(e.currentTarget).attr('data-num'),t.productId,t.storeId);
			}
			
        },
        goToPriceLogPage:function(e){
        	var t=this;
        	var key = e.which;
			var reg=new RegExp("^[1-9]*$");
			if(key==13){	
				
				
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
					var num=$(e.currentTarget).parents(".pageBox").find(".active a").attr("data-num");
					if(num==$(e.currentTarget).val()){
						return false;
					}
			    	if($(e.currentTarget).attr("data-type")=="log"){
			    		t.selectPriceLog(null,$(e.currentTarget).val());
			    	}else if($(e.currentTarget).attr("data-type")=="detail"){
			    		t.selectPriceLogDetail($(e.currentTarget).val(),t.productId,t.storeId);
			    	}
					
			    }
			}
        },
        showLogDetai:function(e){
        	$("#findProductPriceLog").hide();
        	$("#findProductPriceLogDetail").show();
        	this.$content=$("#findProductPriceLogDetail");
        	var productId=$(e.currentTarget).attr("data-num");
        	var storeId=$(e.currentTarget).attr("data-store");
        	this.storeId=storeId;
        	this.productId=productId;
        	console.dir(this.productId+"======="+this.storeId)
        	
        	this.selectPriceLogDetail("1",productId,storeId);
        },
        selectPriceLogDetail:function(pageNum,productId,storeId){
        	var t=this;
        	$.ajax({
                type: 'post',
                dataType: 'json',
                url: '/catalog/control/productPriceRuleLog',
                data: {
                	productId:productId,
                	storeId:storeId,
                	rawOffset:hoursOffset,
                	pageNum:pageNum
                },
                success: function (data) {               	
                    var dataResult = data;
                    if(dataResult.responseMessage == 'success'){
                    	t.$content.find('#tbody').html(template("priceDetailTbodyColumn",data));                    	
            			var nums=[];
                    	for(var i=data.startPage; i<= data.endPage; i++){
							nums.push(i);
						}
                    	t.detail={
                        		totalNum : data.totalNum,
                				pageNum : data.pageNum,
                				totalPage : data.totalPage,
                				nums : nums,
                				type:'detail'
                			};
                    	t.$content.find('.pageBox').html(template('pricePage',t.detail));
                    }else if(dataResult.responseMessage == 'error'){
                    	t.showToast(data.errorMessage);
                    }
                }
            });        	
        },
        returnToLog:function(){
        	$("#findProductPriceLogDetail").hide();
        	$("#findProductPriceLog").show();
        	this.$content=$("#findProductPriceLog");
        	this.selectPriceLog(null,1); 
        },        
        setNowDate:function(){        	        	
        	var timeZones = document.getElementById('timeZones');
        	var nowDate;
            if(timeZones){
                var timeStr = timeZones.value;
                nowDate = new Date(timeStr);
                if(!isNaN(nowDate)) {
                    return this.format(nowDate);
                }
            }
            nowDate=new Date();  
            return this.format(nowDate);
		},
		format:function(d){
			var d = new Date(),str = '';
        	str += d.getDate()<10?"0"+d.getDate()+'/':d.getDate()+'/'; 
        	str += d.getMonth() + 1<10?"0"+(d.getMonth() + 1)+'/':d.getMonth() + 1+'/';
        	str += d.getFullYear();        	            	           	         	 	 
        	return str; 
		}  ,  
        searchProductPriceLogOrderBy:function(e){       	
        	var orderby=$(e.currentTarget).attr("data-type");
        	var pageNum=this.$content.find(".active").find("a").attr("data-num");     
        	this.selectPriceLog(null,pageNum,orderby);
        }
    })
    new View();
	
	 
	
	　	 $.ajax({
            type: 'post',
            dataType: 'json',
            url: '/catalog/control/permission',
            data: {},
            success: function (data) {
                var dataResult = data;
                if(dataResult.responseMessage == 'success'){
                   
                }else if(dataResult.responseMessage == 'error'){
                	showErrorMessage(data.errorMessage);
                }
            }
        });
        
})


        
 
       


</script>

















