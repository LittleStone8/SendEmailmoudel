<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>product stock</title>
	<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/product-stock.css">
	<link href="/commondefine_css/Datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
</head>  
<body>
<div id="product_stock" style="display:none;">
   <div class="centerarea">
   		<div class="item_warehouse"><span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a></div>   
        <div id="left-content-column">
	       <div class="left_section">
	          <div class="left_section_titlebar">
	             <div class="titlebar_name">Shortcuts</div>
	          </div>
	          <div class="left_section_catalog">
	             
	          </div>
	       </div>
	    </div>
	    <div id="center-content-column">
    	<div class="center_titlebar">
            <div class="center_titlebar_name">Product Stock</div>
        </div>
        <div class="center_search">
           <form id="stockForm" class="clearfix" action="/warehouse/control/exportPurchaseInventory">
           <div class="clearfix">
           		<div class="stock_row clearfix">
				        <span>Warehouse</span>
				        <select id="facilityId" name="facilityId">
				          <option value="">Please select</option>
				        </select>                     
				</div>
               <div class="stock_row clearfix">
				        <span>Product ID</span>
				        <div class="stock_row_input recevice_nohover clearfix">
					        <input type="text" id="productId" value="" name="productId"/>
					        <a class="stock_product_add" href="javascript:void(0)"></a>
				        </div>                      
				</div>
				<div class="stock_row clearfix">
				        <span>Model</span>
				        <input type="text" id="internalName" value="" name="internalName"/>					                        
				</div>
				<div class="stock_row clearfix">
				        <span>Product Type</span>
				        <select id="productTypeId" name="productTypeId">				           
				        </select>			                        
				</div>
				<div class="stock_row clearfix">
				        <span>Category</span>
				        <div class="stock_row_input recevice_nohover clearfix">
					        <input type="text" id="productCategoryId" value="" name="productCategoryId"/>
					        <a class="stock_category_add" href="javascript:void(0)"></a>
				        </div> 				                        
				</div>
				
				<div class="stock_row clearfix">
				        <span>QOH minus Min Stock less than</span>
				        <input type="number"  min="0" id="qohm" value="" name="qohm"/>			                        
				</div>
				<div class="stock_row clearfix">
				        <span>ATP minus Min Stock less than</span>
				        <input type="number"  min="0" id="atpm" value="" name="atpm"/>
				        <input type="hidden" value="1" name="flag"/>			                        
				</div>
				<div class="stock_btn">
	              <a href="javascript:void(0);" class="btn_search">Search</a>
	            </div>	
				</div>
				
	            <div class="stock_export"> <a href="javascript:void(0);" class="btn_export" ><button type="submit">Export</button></a> </div>								
           
           </form>
           
        </div>
        <div class="stock_info">
           
           <table class="stock_info_table">
				<thead>
					<tr>
						<th>Product ID</a></th>
						<th>Description</th>
						<th>Total ATP</th> 
						<th>Total QOH</th>
						<th>Ordered Quantity</th>
						<th>Minimum Stock</th>
						<th>QOH minus Min Stock</th>
						<th>ATP minus Min Stock</th>
						<th>Usage</th>
						<th>Detail</th>
					</tr>
				</thead>
			
				<tbody id="tbody">
				    <tr>
		               <td colspan="10">0 results</td>
		            </tr>
				</tbody>
		        
			</table>
			<div id="tablePager" class="clearfix">
		     
			</div>
        </div>
    </div>
   </div>
	
	
    
        
        
    
    </div>
    
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script src="/commondefine_js/layDate/laydate.dev.js"></script>
</body>
<script type="text/html" id="facilityTypeColumn">
{{if inventoryFacilitys}}
      {{each inventoryFacilitys as value i}}
         <option value="{{value.facilityId}}">{{value.facilityName}}</option>
      {{/each}}
   {{/if}}
</script>
<script type="text/html" id="productTypeColumn">
   {{if result}}
      {{each result as value i}}
         <option value="{{value}}" {{if value=='FINISHED_GOOD'}}selected{{/if}}>{{value}}</option>
      {{/each}}
   {{/if}}
</script>
<script type="text/html" id="stockPermission">
<ul>
   {{if result}}
        {{each result as value i}}
            <li {{if value.text=='Product Stock'}}class='menu_active'{{/if}}><a href="{{value.url}}">{{value.text}}</a></li>    
        {{/each}}                          
   {{/if}}
</ul>
</script>
<script type="text/html" id="stockTbodyColumn">
	{{if result.length>0}}
		{{each result as value i}}
			<tr class="">
			 <td>{{value.productId}}</td>
			 <td>{{value.brandName}} {{if value.internalName}}|{{/if}} {{value.internalName}} {{if value.description}}|{{/if}} {{value.description}}</td>
             <td>{{value.atp | formatDiff}}</td>
			 <td>{{value.qoh | formatDiff}}</td>
             <td>{{value.orderNum | formatDiff}}</td>
			 <td>0</td>
             <td>{{value.qoh | formatDiff}}</td>
			 <td>{{value.atp | formatDiff}}</td>    
			 <td></td> 
			 <td><a href="javascript:void(0);" class="inventory_detail" data-productId="{{value.productId}}"></a></td>         
			</tr>
		{{/each}}
		{{else}}
			<tr>
               <td colspan="10">0 results</td>
            </tr>
	{{/if}}

			    
</script>
<script type="text/html" id="stockPage">
	{{if totalNum>0}}<div class="goPage"><span>GO</span><input type="text" data-big-num="{{totalPage}}" class="go_page_input"/></div>{{/if}}
	<ul class="paging clearfix" id="fenye">
	    {{if totalNum>0}}
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
           
          <li ><a href="javascript:void(0)" data-type="final"  data-num="{{totalPage}}"><span  class="page_final"></span></a></li>
		  
{{/if}}
	</ul>
    
	
</script>
<script>
require.config({
	paths:{
		stockProductDialogHtml:'/commondefine_js/tempate/productStock/stockProductDialog.html?v=1',
		stockProductDialog:'/commondefine_js/tempate/productStock/stockProductDialog',
		stockCategoryDialogHtml:'/commondefine_js/tempate/productStock/stockCategoryDialog.html?v=1',
		stockCategoryDialog:'/commondefine_js/tempate/productStock/stockCategoryDialog',
		stockDetailDialogHtml:'/commondefine_js/tempate/productStock/stockDetailDialog.html?v=1',
		stockDetailDialog:'/commondefine_js/tempate/productStock/stockDetailDialog'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	template.helper('formatDiff', function(price) {
		if(price){			
			var str=price.toString().substr(0,price.indexOf(".")+3); 
			var arrayPrice = str.split(".");
			return arrayPrice[1]=="00"?arrayPrice[0]:str;			
		}
	    return price;
	});
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	'click #left-content-column li':'selectMenu',        
            'click #stockForm .stock_product_add':'findProduct', 
            'click #stockForm .stock_category_add':'findCategory',
            'click #product_stock .btn_search':'lookUpProductStockData',
            'click #product_stock .paging a':'lookUpDataByPage',
            'keypress #product_stock .go_page_input':'lookUpDataByInput',
            'click #product_stock .btn_export':'exportData',
            'click #product_stock .inventory_detail':'lookInventoryDetail'
        },
        __propertys__: function () {
            this.codesData =[];
            this.form={};
            this.page={};
            this.formData=[];
            this.itemNum=1;
            this.permission="/warehouse/control/productStock";
        },
        initialize: function ($super) {
            $super();               
            this.initRoleData();
            this.initFacilityData();
            this.$form=$("#stockForm");            
        },
        initFacilityData:function(){
			var t=this;
			$.ajax({
				url:'/warehouse/control/queryFacility',
				type: 'post',
	            dataType: 'json',
				success:function(results){					
					var content=template("facilityTypeColumn",results);
					$("#facilityId").append(content);
					$.ajax({
						url:'/warehouse/control/queryProductType',
						type: 'post',
			            dataType: 'json',
						success:function(results){					
							var content=template("productTypeColumn",results);
							$("#productTypeId").html(content);
							//t.lookUpProductStockData(null,1);
						}
					})	
				}
			});		
		},
        initRoleData:function(){
        	var t=this;
        	$.ajax({
				url:'/warehouse/control/userPermission',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){						
					if(results.resultCode==1){						
						$(".warehouse_name").text(results.facility);
						var type=false;
						for(var i=0;i<results.result.length;i++){
							if(results.result[i].url==t.permission){
								type=true;					   
							}							
						}
						if(!type){
							window.location.href="/warehouse/control/selectFacilityForm";	
						}
						$("#product_stock").show();
						$('.left_section_catalog').html(template("stockPermission",results));
						$(".left_content_column").find("a[href='"+t.permission+"']").parent('li').addClass("menu_active");	
					}					
				}
			})
        },
        selectMenu:function(e){        	
        	$(e.currentTarget).addClass("menu_active").siblings("li").removeClass("menu_active");
        },
        lookInventoryDetail : function(e){
        	var t = this;
        	
        	var productId = $(e.currentTarget).attr("data-productId");
        	require(['stockDetailDialog'], function(stockDetailDialog){
            	var aa=new stockDetailDialog({
            		productId : productId
            	});            	            	
            });
        },
		lookUpProductStockData:function(target,pageNum){
			
			var t=this;
			console.dir(target);
			console.dir(pageNum);
			if(pageNum==undefined){
				pageNum=1;
			}
			t.renderProductStockFormData(pageNum);
			
			target&&$.showLoading(target.currentTarget);
			$.ajax({
				url:'/warehouse/control/queryPurchaseInventory',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.form),
				success:function(results){
					target&&$.hideLoading(target.currentTarget);
					t.page.pageNum=results.pageNum;
					t.page.totalPage=results.totalPage;
					t.page.totalNum=results.totalNum;
					
					$("#tbody").html(template("stockTbodyColumn",results));
					var pages=[];
					var startPage=results.startPage;
					var endPage=results.endPage;
					
					for(var i=startPage;i<=endPage;i++){
						pages.push(i);
					}
					var pageResults={
							"startPage":results.startPage,
							"endPage":results.endPage,
							"pageNum":results.pageNum,
							"totalNum":results.totalNum,
							"totalPage":results.totalPage,
							"nums":pages
					};
					$("#tablePager").html(template("stockPage",pageResults));
				}
			})
			
		},
		exportData:function(){
			
		},
		lookUpDataByPage:function(e){
			var t=this;			
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
			t.lookUpProductStockData(null,$(e.currentTarget).attr('data-num'));
		},
		lookUpDataByInput:function(e){
			
			var key = e.which;
			var reg=new RegExp("^[1-9]*$");
			if(key==13){		
				
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
			    	this.lookUpProductStockData(null,$(e.currentTarget).val());
			    }
			}
		},
		renderProductStockFormData:function(pageNum){
			
			console.dir(this.$form);
			this.form.facilityId=this.$form.find("#facilityId").val().toString();
			this.form.productId=this.$form.find("#productId").val().toString().trim();
			this.form.internalName=this.$form.find("#internalName").val().toString().trim();
			this.form.productTypeId=this.$form.find("#productTypeId").val().toString();
			this.form.productCategoryId=this.$form.find("#productCategoryId").val().toString();
			this.form.qohm=this.$form.find("#qohm").val().toString();
			this.form.atpm=this.$form.find("#atpm").val().toString();			
			this.form.pageNum=pageNum.toString();	
			this.form.flag = "1";
		},
        findProduct:function(e){
        	require(['stockProductDialog'], function(stockProductDialog){
            	var aa=new stockProductDialog(
            	{            		            		
            		onSelected:function(data){
            			$(e.currentTarget).parents(".stock_row").find("#productId").val(data);
            		}
            	}
            	);            	            	
            });
        },
        findCategory:function(e){
        	require(['stockCategoryDialog'], function(stockCategoryDialog){
            	var aa=new stockCategoryDialog(
            	{            		            		
            		onSelected:function(data){
            			$(e.currentTarget).parents(".stock_row").find("#productCategoryId").val(data);
            		}
            	}
            	);            	            	
            });
        }
        
    
      
	})
	
	
	new View();
})
 
</script>






