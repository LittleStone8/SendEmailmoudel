<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/inventory-item.css">
<style type="text/css"> body{height: auto !important;} </style>
<div id="inventoryLog">
	<div class="inventoryLog_box">
	    <div class="item_warehouse">
    		<span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a>
    	</div>
		<div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
		<div class="right_column">
			<div class="inventoryLog_tit">Inventory log[<span class="inventoryId"></span>]</div>
			<div class="inventoryLog_content">
				<table class="inventoryLog_table">
					<thead>
						<tr>
							<td>Inventory Item Seq Id</td>
							<td>Effective Date</td>
							<td>Operator</td>
							<td>QTY Diff</td>
							<td>ATP Diff</td>
							<td>Usage Type</td>
							<td>Order Id</td>
							<td>From Warehouse</td>
							<td>To Warehouse</td>
							<td>Transfer Id</td>
							<td>Adjust Reason</td>
							<td>QTY</td>
							<td>ATP</td>
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

	{{if result.length>0}}
		{{each result as value}}
			<tr>
			 <td>{{value.inventoryItemDetailSeqId}}</td>
			 <td>{{value.effectiveDate}}</td>
			 <td>{{value.operator}}</td>
			 <td>{{value.quantityOnHandDiff | formatDiff: 'integer'}}</td>
             <td>{{value.availableToPromiseDiff | formatDiff: 'integer'}}</td>
			 <td>{{value.usageType}}</td>
			 <td>{{value.orderId}}</td>
             <td>{{value.fromWarehouse}}</td>
			 <td>{{value.toWarehouse}}</td>
			 <td>{{value.transferId}}</td>
             <td>{{value.reasonEnumId}}</td>
             <td>{{value.qty  | formatDiff: 'integer'}}</td>
			 <td>{{value.atp  | formatDiff: 'integer'}}</td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="13">No fund data</td>
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
require(['Inherit', 'AbstractView', 'template'], function (Inherit, AbstractView, template) {
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
        	"click .paging a" : "pageChangeEvent",
        	"keypress .go_page_input" : "jumpChangeEvent"
        },
        __propertys__: function () {
        },
        initialize: function ($super) {
            $super();   
            this.initTableData({
            	inventoryItemId :this.initGetParams('inventoryItemId'),
            	pageNum : "1",
            	pageSize : "20"
            });
            this.initRoleData();
            this.initInventoryId();
            this.$page=$(".pageBox");	
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
        	$.ajax({
				url:'/warehouse/control/userPermission',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){
					if(results.resultCode==1){
						$(".warehouse_name").text(results.facility);
						$('.left_column_content').html(template("leftColumnContent",results));
					}					
				}
			})
        },
        initTableData:function(options){
        	var t = this;
         	$.ajax({
				url:'/warehouse/control/findInventoryItemDetail',
				type: 'post',
	            dataType: 'json',
	            data : JSON.stringify(options),
	           	success:function(results){
					if(results.resultCode==1){
						t.page = {
							pageNum : results.pageNum,
							totalPage : results.totalPage
						}
						var nums = [];
						$('.inventoryLog_table tbody').html(template('logTbodyColumn',{
							result : results.result
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
			t.initTableData({
				inventoryItemId :t.initGetParams('inventoryItemId'),
				pageNum : $(evt).attr('data-num'),
            	pageSize : "20"
			});
		},
        jumpChangeEvent:function(e){
			var t=this;
			var key = e.which;
			var reg=new RegExp("^[1-9]*$");
			if(key==13){					
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
			    	t.initTableData({
			    		inventoryItemId :t.initGetParams('inventoryItemId'),
			    		pageNum : $(e.currentTarget).val(),
		            	pageSize : "20"
			    	});
			    }
			}
		}
	})

	new View();
})
 
</script>