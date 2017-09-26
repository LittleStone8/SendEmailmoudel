<link rel="stylesheet" type="text/css" href="/commondefine_js/select2/select2.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/inventory-item.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/product-stock.css">
<style type="text/css"> body{height: auto !important;} #findContent{ width:auto !important; } .select2-selection.select2-selection--single{ border:1px solid #dbdbdb;border-radius:0; } </style>
<div id="newFindInventoryItem" style="display:none">
	<div class="newFindInventoryItem_box">
	    <div class="item_warehouse">
    		<span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a>
    	</div>
		<div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
		<div class="right_column">
			<div class="search_box">
				<div class="search_box_tit">Find Inventory Item</div>
				<div class="search_box_content">
					<div class="item_row">
				        <div class="left_label">Product ID</div>
				        <div class="item_row_input recevice_nohover">
					        <input type="text" class="productId" value=""/>
					        <a class="item_add" href="javascript:void(0)" data-type="productId"></a>
				        </div>
				    </div>
				    <div class="item_row">
				        <div class="left_label">Lot ID</div>
				        <div class="item_row_input recevice_nohover">
					        <input type="text" class="lotId" value=""/>
					        <a class="item_lot_add" href="javascript:void(0)" data-type="lotId"></a>
				        </div>
				    </div>
				    <div class="item_row">
				        <div class="left_label">IMEI/Serial Number</div>
				        <input type="text" class="serialNumber"/>
				    </div>
				    <div class="item_row">
				        <div class="left_label">Model</div>
				        <input type="text" class="Model"/>
				    </div>
				    <div class="item_row">
				        <div class="left_label">Inventory Warehouse</div>
				        <select class="inventoryFacility">
				        </select>
				    </div>
				    <div class="item_row">
				        <div class="left_label">Contains QOH=0</div>
				        <input type="checkbox" class="containsQTY0"/>
				    </div>
				    <div class="item_row">
				        <a href="javascript:void(0)" class="search_btn">Search Inventory Item</a>
				    </div>
				    <div style="clear:both;"></div>
				</div>
			</div>
			<div class="list_box">
				<div class="list_box_tit">Inventory Items</div>
				<table class="list_box_table">
					<thead>
						<tr>
							<td style="width:145px;"><div class="title_content">Inventory Item ID<div class="icon_content"><span class="arrow_up" data-type="inventoryItemId ASC"></span><span class="arrow_down" data-type="inventoryItemId DESC"></span></div></div></td>
							<td style="width:95px;"><div class="title_content">Product ID<div class="icon_content"><span class="arrow_up" data-type="productId ASC"></span><span class="arrow_down" data-type="productId DESC"></span></div></div></td>
							<td style="width:130px;"><div class="title_content">Model<div class="icon_content"><span class="arrow_up" data-type="internalName ASC"></span><span class="arrow_down" data-type="internalName DESC"></span></div></div></td>
							<td style="width:155px;"><div class="title_content">Product Attribute<div class="icon_content"><span class="arrow_up" data-type="description ASC"></span><span class="arrow_down" data-type="description DESC"></span></div></div></td>
							<td style="width:155px;"><div class="title_content">IMEI/Serial Number<div class="icon_content"><span class="arrow_up" data-type="serialNumber ASC"></span><span class="arrow_down" data-type="serialNumber DESC"></span></div></div></td>
							<td style="width:85px;"><div class="title_content">Lot ID<div class="icon_content"><span class="arrow_up" data-type="lotId ASC"></span><span class="arrow_down" data-type="lotId DESC"></span></div></div></td>
							<td style="width:110px;"><div class="title_content">Date Received<div class="icon_content"><span class="arrow_up" data-type="datetimeReceived ASC"></span><span class="arrow_down" data-type="datetimeReceived DESC"></span></div></div></td>
							<td style="width:95px;"><div class="title_content">Status<div class="icon_content"><span class="arrow_up" data-type="statusId ASC"></span><span class="arrow_down" data-type="statusId DESC"></span></div></div></td>
							<td style="width:65px;"><div class="title_content">QOH<div class="icon_content"><span class="arrow_up" data-type="quantityOnHandTotal ASC"></span><span class="arrow_down" data-type="quantityOnHandTotal DESC"></span></div></div></td>
							<td>Inventory Warehouse</td>
							<td>Inventory Log</td>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<div class="pageBox"></div>
			</div>
		</div>
		<div style="clear:both;"></div>
	</div>
	<div style="clear:both;"></div>
</div>

<script type="text/html" id="logTbodyColumn">

	{{if result.result.length>0}}
		{{each result.result as value}}
			<tr>
			 {{if result.showEditPage}}
			 <td><a class="inventoryLog_a" href="/warehouse/control/EditInventoryItem?inventoryItemId={{value.inventoryItemId}}">{{value.inventoryItemId}}</a></td>
			 {{else}}
			 <td>{{value.inventoryItemId}}</td>
			 {{/if}}
			 <td style="width:90px;"><a class="inventoryLog_a product_id_evt" data-productId="{{value.productId}}" data-performFind="Y">{{value.productId}}</a></td>
             <td>{{value.internalName}}</td>
			 <td>{{value.description}}</td>
			 <td>{{value.serialNumber}}</td>
             <td>{{value.lotId}}</td>
			 <td>{{value.datetimeReceived}}</td>
			 <td>{{value.statusId}}</td>
			 {{if value.statusId != "INV_PROMISED"}}
				<td>{{value.quantityOnHandTotal | formatDiff : 'integer'}}</td>
				{{else}}
				<td>0</td>
			 {{/if}}
			 <td>{{value.facilityName}}</td>
			 <td><a class="inventoryLog_a" href="/warehouse/control/inventoryLog?inventoryItemId={{value.inventoryItemId}}">Log</a></td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="11">No fund data</td>
			</tr>
	{{/if}}
			    
</script>
<script type="text/html" id="leftColumnContent">
<ul>
   {{if result}}
        {{each result as value i}}
            <li><a href="{{value.url}}">{{value.text}}</a></li>    
        {{/each}}                         
   {{/if}}
</ul>
</script>
<script type="text/html" id="facilityTypeColumn">
<option value="{{defaultIdList}}">All</option>
{{if inventoryFacilitys}}
      {{each inventoryFacilitys as value i}}
         <option value="{{value.facilityId}}" {{if value.facilityId==defaultFacilityId}}selected{{/if}}>{{value.facilityName}}</option>
      {{/each}}
   {{/if}}
</script>
<script type="text/html" id="pageListContent">
	{{if totalNum>0}}
		<div class="goPage">
			<span>GO</span>
			<input type="text" data-big-num="{{totalPage}}" class="go_page_input"/>
		</div>
		<ul class="paging clearfix" id="fenye">
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
	{{/if}}
</script>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script>
require.config({
	paths:{
		dialogFindLotHtml:'/commondefine_js/tempate/inventoryItem/dialogFindLot.html?v=1',
		dialogFindLot:'/commondefine_js/tempate/inventoryItem/dialogFindLot',
		stockProductDialogHtml:'/commondefine_js/tempate/productStock/stockProductDialog.html?v=1',
		stockProductDialog:'/commondefine_js/tempate/productStock/stockProductDialog',
		select2 : '/commondefine_js/select2/select2'
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
        	"click .pageBox .paging a" : "pageChangeEvent",
        	"keypress .pageBox .go_page_input" : "jumpChangeEvent",
        	"click .search_btn" : "searchListEvent",
        	"click .item_add": "findProductLotData",
        	"click .item_lot_add":"findProductLotData",
        	"click .list_box_table thead .arrow_up, .list_box_table thead .arrow_down":"orderBy",
        	"click .list_box_table .product_id_evt" : "findProductLotDataByProductId"
        },
        __propertys__: function () {
        },
        initialize: function ($super) {
            $super();  
            this.initFacilityData(); 
            this.initRoleData();
            this.initInventoryId();
            this.$page=$(".pageBox");	
            this.clickType = null;
        },
        initFacilityData:function(){
			var t=this;
			$.ajax({
				url:'/warehouse/control/queryFacility',
				type: 'post',
	            dataType: 'json',
				success:function(results){
					var idList = [];
					if(results.inventoryFacilitys && results.inventoryFacilitys.length){
						$.each(results.inventoryFacilitys, function(index, elem){
							idList.push(elem.facilityId);
						});
					}
					results.defaultIdList = idList.join(',');
					var content=template("facilityTypeColumn",results);
					$(".inventoryFacility").html(content);
					require(['select2'], function(select2){
						$(".inventoryFacility").select2();
					});
					
					var commonParams = $.extend({},t.getParams(), {
            			pageNum : "1",
            			pageSize : "30"
            		});
            		t.initTableData(null, commonParams);
				}
			});		
		},
		searchListEvent : function(e){
			var t = this;
			var commonParams = $.extend({},t.getParams(), {
    			pageNum : "1",
    			pageSize : "30"
    		});
    		t.initTableData(e, commonParams);
		},
		findProductLotDataByProductId : function(e){
			var t = this;
			
        	$('.productId').val($(e.currentTarget).attr('data-productId'));
        	
			var commonParams = $.extend({},t.getParams(), {
    			pageNum : "1",
    			pageSize : "30",
    			productId : $(e.currentTarget).attr('data-productId'),
    			performFind : $(e.currentTarget).attr('data-performFind')
    		});
    		t.initTableData(null, commonParams);
		},
		findProductLotData : function(e){
			var t = this;
			t.initShowProductId(e.currentTarget,$(e.currentTarget).attr("data-type"));  
		},
		initShowProductId : function(target, type){
			var t=this;
			switch(type){
				case "productId":
		        	require(['stockProductDialog'], function(stockProductDialog){
		            	var aa=new stockProductDialog(
		            	{            		            		
		            		onSelected:function(data){
		            			$(target).siblings(".productId").val(data);
		            		}
		            	}
		            	);            	            	
		            });
					break;
		   		case "lotId":
		        	require(['dialogFindLot'], function(dialogFindLot){
		            	var aa=new dialogFindLot(
		            	{            		            		
		            		onSelected:function(data){
		            			$(target).siblings(".lotId").val(data);
		            		}
		            	}
		            	);            	            	
		            });
		   			break;
			}
		},
        getParams : function(){
        	var params = {};
        	
        	params.productId = $('.productId').val().trim();
        	params.internalName = $('.Model').val().trim();
        	params.serialNumber = $('.serialNumber').val();
        	params.lotId = $('.lotId').val().trim();
        	params.facilityIdToFind = $('.inventoryFacility').val();
        	params.flag = $('.containsQTY0').is(':checked') ? 'Y' : 'N';
        	params.orderBy = 'datetimeReceived desc';
        	
        	return params;
        },
        orderBy : function(e){
        	var t = this;
        	var currentTarget = e.currentTarget;
        	var type = $(currentTarget).attr("data-type");

        	t.clickType = type;
        	var pageNum = $("#fenye li.active a").attr('data-num');
        	var paramsList = t.getParams();
			var commonParams = $.extend({},t.getParams(), {
				orderBy : type,
				pageNum : pageNum,
    			pageSize : "30"
    		});
    		t.initTableData(null, commonParams);
        },
        initInventoryId : function(){
        	var t = this;
        	var inventoryId = $('.inventoryId');
        	inventoryId.html(t.initGetParams('inventoryItemId'));
        },
        initGetParams : function(name){
           var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
           var r = window.location.search.substr(1).match(reg);
           if (r != null) return unescape(r[2]);
           return null;
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
						$(".left_column_content").find("a[href='/warehouse/control/newFindInventoryItem']").parent('li').addClass("menu_active"); 
						
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
							location.href = '/warehouse/control/selectFacilityForm';
							return false;
						} 
						
						$("#newFindInventoryItem").show();
					}					
				}
			})
        },
        initTableData:function(e, options){
        	var t = this;
        	
        	e && $.showLoading($(e.currentTarget));
         	$.ajax({
				url:'/warehouse/control/findInventoryItemWithPage',
				type: 'post',
	            dataType: 'json',
	            data : JSON.stringify(options),
	           	success:function(results){
	           		e && $.hideLoading($(e.currentTarget));
	           		
					if(results.resultCode==1){
						t.page = {
							pageNum : results.pageNum,
							totalPage : results.totalPage
						}
						var nums = [];
						
						$('.list_box_table tbody').html(template('logTbodyColumn',{
							result : results
						}));
						for(var i=results.startPage; i<= results.endPage; i++){
							nums.push(i);
						}
						$('.pageBox').html(template('pageListContent',{
							totalNum : results.totalNum,
							pageNum : results.pageNum,
							totalPage : results.totalPage,
							nums : nums
						}));
					}					
				},
    			error : function(option, status){
    				e && $.hideLoading($(e.currentTarget));
    			}
			}) 
        },
        pageChangeEvent:function(e){
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
			var commonParams = $.extend({},t.getParams(), {
    			pageNum : $(evt).attr('data-num'),
    			pageSize : "30"
    		});
			if(t.clickType){
				commonParams.orderBy = t.clickType;
			}
    		t.initTableData(null, commonParams);
		},
        jumpChangeEvent:function(e){
			var t=this;
			var key = e.which;
			var reg=new RegExp("^[1-9]*$");
			if(key==13){					
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
					var commonParams = $.extend({},t.getParams(), {
		    			pageNum : $(e.currentTarget).val(),
		    			pageSize : "30"
		    		});
					if(t.clickType){
						commonParams.orderBy = t.clickType;
					}
					t.initTableData(null, commonParams);
			    }
			}
		}
	})

	new View();
})
 
</script>