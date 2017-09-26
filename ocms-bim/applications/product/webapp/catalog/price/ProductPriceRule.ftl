
<link rel="stylesheet" type="text/css" href="/commondefine_css/category/promotion.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/need/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/skins/blue/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/ui-dialog.css">

<div id="findProductPrice" style="display:none">
	<div class="findProductPrice_box">
	   <div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
    <div class="center_content">
        <div class="search_box">
				<div class="search_box_tit">Find Product Price</div>
				<div class="search_box_content clearfix">
					<div class="item_row input_group">
				        <div class="left_label">Product ID</div>
				        <input type="text" class="productId" placeholder="Product ID / Model / Brand"/>
				        <ul class="product_ul" style="display:none"></ul>				    
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
							<td>Current Price(GHS)</td>
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
<script type="text/html" id="leftColumnContent">
<ul>  
    {{if findProductp=='Y'}}<li class="menu_active"><a href="/catalog/control/ProductPriceRule">Find Product Price</a></li>{{/if}}
	{{if findLog=='Y'}}<li><a href="/catalog/control/ProductPriceLog">Find Product Price Log</a></li> {{/if}}
	{{if batchManage=='Y'}}<li><a href="/catalog/control/BatchManagerPrice">Batch Manage Price</a></li>{{/if}}
</ul>
</script>
<script type="text/html" id="productData">
		{{if items}}
        {{each items as value i}}
            <li data-id="{{value.id}}" class="product_li">{{value.productId}}/{{value.model}}/{{value.brand}}</li>  
        {{/each}}                         
   		{{/if}}
</script>
<script type="text/html" id="priceTbodyColumn">

	{{if resultList.length>0}}
		{{each resultList as value}}
			<tr>
			 <td>{{value.producId}}</td>
			 <td>{{value.description}}</td>
             <td>{{value.currentPrice}}</td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="3">No fund data</td>
			</tr>
	{{/if}}	
			    
</script>
<script type="text/html" id="pricePage">
	
	{{if totalNum>0}}<div class="goPage"><span>GO</span><input type="text" data-big-num="{{totalPage}}" class="go_page_input"/></div>{{/if}}
	<ul class="paging clearfix" id="fenye">
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
        	"keyup #findProductPrice .productId":"searchProductId",
        	"click #findProductPrice .product_li":"selectProductId",
        	"click #findProductPrice .search_btn":"searchProductPrice",
        	"click #findProductPrice thead a":"searchProductPriceOrderBy",
        	"click #findProductPrice .paging a":"selectPriceByPage",
        	"keypress #findProductPrice .go_page_input":"goToPricePage",
        },
        __propertys__: function () {
        	this.page={};
        },
        initialize: function ($super) {
        	 $super();  
        	 this.$content=$("#findProductPrice");
        	 this.initMenuData();
        	 this.searchProductPrice(null,1);
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
	                	if(dataResult.findProductp=='Y'){
	                		$("#findProductPrice").show();
	                	}
	                	$('.left_column_content').html(template("leftColumnContent",dataResult));
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
        selectPriceByPage:function(e){
        	var t=this;
        	if($(e.currentTarget).attr('data-num')==t.page.pageNum){
        		return false;
        	}
        	if($(e.currentTarget).attr("data-type")&&$(e.currentTarget).attr("data-type")=='home'&&t.page.pageNum=="1"){
				return false;
			}
			if($(e.currentTarget).attr("data-type")&&$(e.currentTarget).attr("data-type")=='final'&&t.page.pageNum==$(e.currentTarget).attr('data-num')){
				return false;
			}
			if($(e.currentTarget).attr('data-num')<1){
				return false;
			}
			if($(e.currentTarget).attr('data-num')>t.page.totalPage){
				return false;
			}			
			t.searchProductPrice(null,$(e.currentTarget).attr('data-num'));			
			
        },
        goToPricePage:function(e){
        	var key = e.which;
			var reg=new RegExp("^[1-9]*$");
			if(key==13){					
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
					var num=$(e.currentTarget).parents(".pageBox").find(".active a").attr("data-num");
					if(num==$(e.currentTarget).val()){
						return false;
					}
			    	this.searchProductPrice(null,$(e.currentTarget).val());			    						
			    }
			}
        },
        searchProductPrice:function(e,pageNum,orderby){
        	var t=this;
        	var productId="";        	  
        	if(t.$content.find(".productId").attr("data-id")){
        		productId=t.$content.find(".productId").attr("data-id");
        	}else{
        		productId=t.$content.find(".productId").val().trim();
        	}
        	e&&e.currentTarget&&jQuery.showLoading(e.currentTarget);
        	
        	$.ajax({
                type: 'post',
                dataType: 'json',
                url: '/catalog/control/categoryFindProductPrice',
                data: {
                	productId:productId,
                	pageNum:pageNum?pageNum:1,
                	rawOffset:hoursOffset,
                	orderBy:orderby?orderby:"productId"
                },
                success: function (data) {
                    var dataResult = data;
                    e&&e.currentTarget&&jQuery.hideLoading(e.currentTarget); 
                    if(dataResult.responseMessage == 'success'){                    	
            			t.$content.find('#tbody').html(template("priceTbodyColumn",data)); 
            			var nums=[];
                    	for(var i=data.startPage; i<= data.endPage; i++){
							nums.push(i);
						}
                    	t.page={	totalNum : data.totalNum,
	            				pageNum : data.pageNum,
	            				totalPage : data.totalPage,
	            				nums : nums};
			        	t.$content.find('.pageBox').html(template('pricePage',t.page))
                    }else if(dataResult.responseMessage == 'error'){
                    	t.showToast(data.errorMessage);
                    }
                }
            });
			       	
        },
        searchProductPriceOrderBy:function(e){
        	var orderby=$(e.currentTarget).attr("data-type");
        	var pageNum=this.$content.find(".active").find("a").attr("data-num");
        	this.searchProductPrice(null,pageNum,orderby);
        }
    })
    new View();
	
})


	
	


</script>
















