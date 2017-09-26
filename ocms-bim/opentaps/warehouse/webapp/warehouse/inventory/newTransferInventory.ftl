<link rel="stylesheet" type="text/css" href="/commondefine_js/select2/select2.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/transfer-inventory.css">
<div id="newTransferInventory" style="display:none">
	<input type="hidden" id="permission" />
	<div class="newTransferInventory_box">
		<div class="item_warehouse">
			<div><span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a></div>
		</div>
		<div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
		<div class="right_column">
			<div class="newTransferInventory_content">
				<div class="newTransferInventory_tit">Transfer Inventory List</div>
				<div class="newTransferInventory_tab">
					<ul id="newTransferInventoryTab" class="nav nav-tabs">
						<li class="active"><a href="javascript:void(0);" data-type="Sent">Sent</a></li>
						<li><a href="javascript:void(0);" data-type="Receive"><em class="receiveNum"></em>Received</a></li>
						<li><a href="javascript:void(0);" data-type="transferInventory">Transfer Inventory</a></li>
						<li class="isOldTransfer"><a href="javascript:void(0);" data-type="Picking">Picking</a></li>
					</ul>
	    		</div>
	    		<div class="tab-content newTransferInventory_list">
				    <div class="tab-pane active" data-type="Sent">
				    	<div class="sent_search">
				    		<div class="sent_search_item">
				    			<div class="sent_search_span">Status</div>
				    			<select class="sent_search_select statusId" name="status">				    				
				    				<option value="IXF_REQUESTED">IXF_REQUESTED</option>
				    				<option value="IXF_COMPLETE">IXF_COMPLETE</option>
				    				<option value="IXF_CANCELLED">IXF_CANCELLED</option>
				    				<option value="">ALL</option>
				    			</select>
				    		</div>
				    		<div class="sent_search_item">
				    			<div class="sent_search_span">Product ID</div>
				    			<input class="sent_search_input productId" type="text"  />
				    		</div>
				    		<div class="sent_search_item">
				    			<div class="sent_search_span">Sent Date</div>
				    			<input class="sent_search_input js_sndate laydate-icon sent_date sendDate" type="text" />
				    		</div>
				    		<div class="sent_search_item">
				    			<div class="sent_search_span">Model</div>
				    			<input class="sent_search_input model" type="text" />
				    		</div>
				    		<div class="sent_search_item last_item">
				    			<div class="sent_search_span">Warehouse</div>
				    			<select class="sent_search_select sent_warehouse facilityId" name="warehouse">
				    				<option value="">Please select</option>
				    			</select>
				    		</div>
				    		<div class="sent_search_item">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="Sent">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="sent_table">
				    		<table class="sent_table_box">
				    			<thead>
				    				<tr>
				    					<th><input type="checkbox" class="selectAll" /></th>
				    					<th>Transfer ID</th>
				    					<th>Inventory Item ID</th>
				    					<th>Product ID</th>
				    					<th>Model</th>
				    					<th>Products Attributes</th>
				    					<th>IMEI</th>
				    					<th>Received By</th>
				    					<th>Transfer Quantity</th>
				    					<th>Sent Date</th>
				    					<th>Status</th>
				    					<th>Comments</th>
				    				</tr>
				    			</thead>
				    			<tbody></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="footer_total"><em class="sent_total_size total">0</em> items selected</div>
				    		<div class="sent_page"></div>
				    	</div>
				    	<div class="operator_list">
				    		<a href="javascript:void(0);" class="sent_complete no_checked" data-type='Sent'>Cancel</a>
				    	</div>
				    </div>
				    <div class="tab-pane" data-type="Receive">
				    	<div class="receive_search">
				    		<div class="receive_search_item">
				    			<div class="receive_search_span">Status</div>
				    			<select class="receive_search_select statusId" name="status">
				    				<option value="IXF_REQUESTED">IXF_REQUESTED</option>
				    				<option value="IXF_COMPLETE">IXF_COMPLETE</option>
				    				<option value="IXF_CANCELLED">IXF_CANCELLED</option>
				    				<option value="">ALL</option>
				    			</select>
				    		</div>
				    		<div class="receive_search_item">
				    			<div class="receive_search_span">Product ID</div>
				    			<input class="receive_search_input productId" type="text" />
				    		</div>
				    		<div class="receive_search_item">
				    			<div class="receive_search_span">Sent Date</div>
				    			<input class="receive_search_input js_sndate laydate-icon receive_date sendDate" type="text" />
				    		</div>
				    		<div class="receive_search_item">
				    			<div class="receive_search_span">Model</div>
				    			<input class="receive_search_input model" type="text" />
				    		</div>
				    		<div class="receive_search_item last_item">
				    			<div class="receive_search_span">Warehouse</div>
				    			<select class="receive_search_select receive_warehouse facilityId" name="warehouse">
				    				<option value="">Please select</option>
				    			</select>
				    		</div>
				    		<div class="receive_search_item">
				    			<a href="javascript:void(0);" class="search_confirm" data-type="Receive">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="receive_table">
				    		<table class="receive_table_box">
				    			<thead>
				    				<tr>
				    					<th><input type="checkbox" class="selectAll" /></th>
				    					<th>Transfer ID</th>
				    					<th>Item</th>
				    					<th>Product ID</th>
				    					<th>Model</th>
				    					<th>Products Attributes</th>
				    					<th>IMEI</th>
				    					<th>Sent By</th>
				    					<th>Transfer Quantity</th>
				    					<th>Sent Date</th>
				    					<th>Status</th>
				    					<th>Comments</th>
				    				</tr>
				    			</thead>
				    			<tbody></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="footer_total"><em class="receive_total_size total">0</em> items selected</div>
				    		<div class="receive_page"></div>
				    	</div>
				    	<div class="operator_list">
				    		<a href="javascript:void(0);" class="receive_complete no_checked" data-type='Receive'>Complete</a>
				    	</div>
				    </div>
				    <div class="tab-pane" data-type="transferInventory">
				    	<div class="transferInventory_search">
				    		<div class="transferInventory_search_item">
				    			<div class="transferInventory_search_span">Product ID</div>
				    			<select id="productIdNum" class="product_select">
									<option value="1">Contain</option>
									<option value="2">Beginning</option>
									<option value="3">Equal</option>
								</select>
				    			<input class="transferInventory_search_input productId" type="text" />
				    		</div>
				    		<div class="transferInventory_search_item">
				    			<div class="transferInventory_search_span">Brand</div>
				    			<select id="brandNum" class="product_select">
									<option value="1">Contain</option>
									<option value="2">Beginning</option>
									<option value="3">Equal</option>
								</select>
				    			<input class="transferInventory_search_input brand" type="text" />
				    		</div>
				    		<div class="transferInventory_search_item">
				    			<div class="transferInventory_search_span">Model</div>
				    			<select id="modelNum" class="product_select">
									<option value="1">Contain</option>
									<option value="2">Beginning</option>
									<option value="3">Equal</option>
								</select>
				    			<input class="transferInventory_search_input model" type="text" />
				    		</div>
				    		<div class="transferInventory_search_item">
				    			<a href="javascript:void(0);" class="select_confirm" data-type="transferInventory">Search</a>
				    		</div>
				    		<div style="clear:both;"></div>
				    	</div>
				    	<div class="transferInventory_table">
				    		<table class="transferInventory_table_box">
				    			<thead>
				    				<tr>
				    					<th><input type="checkbox" class="selectAll" /></th>
				    					<th>Product ID</th>
				    					<th>Brand</th>
				    					<th>Model</th>
				    					<th>Products Attributes</th>
<!-- 					    					<th>ATP</th>
					    					<th>Quantity Be Reserved</th> -->
				    					<th>Available For Transfer</th>
				    					<th>Transfer Quantity</th>
				    				</tr>
				    			</thead>
				    			<tbody></tbody>
				    		</table>
				    	</div>
				    	<div class="footer_page">
				    		<div class="footer_total"><em class="transferInventory_total_size total">0</em> items selected</div>
				    		<div class="transferInventory_page"></div>
				    	</div>
				    	<div class="transferInventory_warehouse">
				    		<div class="transferInventory_warehouse_item">
				    			<div class="transferInventory_warehouse_span">Transfer to Warehouse</div>
				    			<select class="transferInventory_warehouse_select transferInventory_add_warehouse">
				    			</select>
				    		</div>
				    		<a href="javascript:void(0);" class="add_confirm">ADD</a>
				    		<div style="clear:both;"></div>
				    	</div>
				    </div>
				    <div class="tab-pane" data-type="Picking">
				    	<div class="transferInventory_imei">
							<div class="transferInventory_imei_title"><span class="returnPage">&lt; Return</span></div>
							<div class="transferInventory_imei_content"></div>
							<div class="transferInventory_imei_operator">
								<a href="javascript:void(0);" class="submit_imei">Submit</a>
								<a href="javascript:void(0);" class="submit_later_imei">Submit Later</a>
							</div>
						</div>
						<div class="transferInventory_items">
							<div class="picking_search">
								<div class="picking_search_item">
									<div class="picking_search_span">Tracking ID</div>
									<input class="picking_search_input trackingId" type="text" />
								</div>
								<div class="picking_search_item">
									<div class="picking_search_span">Warehouse</div>
									<select class="picking_search_select picking_warehouse facilityId" name="warehouse">
					    				<option value="">Please select</option>
					    			</select>
								</div>
								<div class="picking_search_item">
									<a href="javascript:void(0);" class="select_confirm" data-type="Picking">Search</a>
								</div>
								<div style="clear:both;"></div>
							</div>
							<div class="picking_table">
								<table class="picking_table_box">
									<thead>
										<tr>
											<th style="width:5%"><input type="checkbox" class="selectAll" /></th>
											<th style="width:15%">Tracking ID</th>
											<th style="width:25%">Received By</th>
											<th style="width:55%">Sent Time</th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
							</div>
							<div class="footer_page">
								<div class="footer_total"><em class="picking_total_size total">0</em> items selected</div>
								<div class="picking_page"></div>
							</div>
							<div class="picking_operator">
								<a href="javascript:void(0);" class="picking_confirm">Picking</a>
								<a href="javascript:void(0);" class="cancel_confirm">cancel</a>
							</div>
						</div>
					</div>
				</div>
	    	</div>
    	</div>
    	<div class="right_list right_hide">
    		<div class="right_list_content"></div>
    		
    		<div class="right_list_operator">
    			<a href="javascript:void(0);" class="printList isNewTransfer">Print</a>
    			<a href="javascript:void(0);" class="confirmList">Confirm</a>
    		</div>
    		<div class="right_list_print" style="display:none;"></div>
    	</div>
	</div>
</div>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/html" id="transferImeiList">
	{{if result.length>0}}
	{{each result as value}}
	<div class="imei_transferListTabContent">
		<div class="imei_warehouseHead">
			<div class="imei_warehouseName" data-id="{{value.transNum}}" data-warehouse="{{value.warehouse}}">
				<span class="inputContent"><input type="checkbox" class="inputList" {{if value.disabled }} disabled="true" {{/if}} /></span>
				<span class="transNumContent">Tracking ID:{{value.transNum}}</span>
				<span class="warehouseContent">Transfer To: {{value.warehouse}}</span>
			</div>
		</div>	
		<div class="imei_warehouseContent">
			<table class="imei_warehouseTable">
				<thead>
					<tr>
						<th>No.</th>
						<th>Product</th>
						<th>IMEI</th>
						<th>Transfer Quantity</th>
						<th>Scan Quantity</th>
						<th>Operation</th>
					</tr>
				</thead>
				<tbody>
					{{if value.pickingVo.length>0}}
					{{each value.pickingVo as listContent i}}
					<tr data-productId="{{listContent.productId}}" data-imei="{{listContent.type | formatImei }}">
						<td>{{i+1}}</td>
						<td class="tdDescritpion">{{listContent.brandName}} {{listContent.model}} {{listContent.descritpion}}</td>
						<td class="tdImei">{{listContent.type | formatImei }}</td>
						<td class="tdTransNum">{{listContent.transNum | formatDiff: 'integer'}}</td>
						{{if listContent.type == 'NON_SERIAL_INV_ITEM'}}
							<td>----</td>
							<td>----</td>
						{{else}}
							<td><span {{if listContent.scanNum == 0}}class="imeiTansferList noTransfer" {{else}} class="imeiTansferList" {{/if}}>{{listContent.scanNum}}</span></td>
							<td><a href="javascript:void(0);" data-model="{{listContent.model}}" data-transNum="{{listContent.transNum | formatDiff: 'integer'}}" class="openScan">Scan</a></td>
						{{/if}}
					</tr>
					{{/each}}
					{{/if}}
				</tbody>
			</table>
		</div>	
	</div>
	{{/each}}
	{{/if}}
</script>
<script type="text/html" id="newTransferPrint">
{{if result.length>0}}
	{{each result as value i}}
	<div class="print_transferListTabContent" style="margin-bottom:20px;">
		<div class="print_warehouseHead">
			<span class="print_warehouseName" style="color:#1FBCD2;vertical-align:bottom;font-size:16px;margin-bottom:20px;display:inline-block;">{{value.transNum}} {{value.warehouse}}</span>
			<img id="imgcode{{i}}" class="imgcode" style="width:220px;height:80px;display:inline-block;" />
		</div>	
		<div class="print_warehouseContent">
			<table class="print_warehouseTable" style="width:100%;" border="1" cellpadding="0" cellspacing="0">
				<thead>
					<tr style="height:30px;line-height:30px;">
						<th style="width:40%;text-align:center;">Description</th>
						<th style="width:30%;text-align:center;">IMEI</th>
						<th style="width:30%;text-align:center;">Trans Quantity</th>
					</tr>
				</thead>
				<tbody>
				{{if value.pickingVo.length>0}}
					{{each value.pickingVo as listContent}}
						<tr style="height:30px;line-height:30px;">
							{{if listContent.tdDescritpion }}
								<td style="width:40%;text-align:center;">{{listContent.tdDescritpion}}</td>
								{{else}}
								<td style="width:40%;text-align:center;">{{listContent.brandName}} {{listContent.model}} {{listContent.descritpion}}</td>
							{{/if}}
							<td style="width:30%;text-align:center;">{{listContent.type | formatImei }}</td>
							<td style="width:30%;text-align:center;">{{listContent.transNum | formatDiff: 'integer'}}</td>
						</tr>
					{{/each}}
				{{/if}}
				</tbody>
			</table>
		</div>	
	</div>
	{{/each}}
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
<script type="text/html" id="sentContent">

	{{if result.length>0}}
	{{each result as value i}}
	<tr>
		<td><input type="checkbox" class="checkList" data-transferId="{{value.inventoryTransferId}}" {{if value.statusId != 'IXF_REQUESTED'}}disabled{{/if}}/></td>
		<td>{{value.inventoryTransferId}}</td>
		<td>{{value.inventoryItemId}}</td>
		<td>{{value.productId}}</td>
		<td>{{value.model}}</td>
		<td>{{value.description}}</td>
		{{if value.imei }}
		<td>{{ value.imei }}</td>
		{{else}}
		<td>----</td>
		{{/if}}	
		<td>{{value.facilityName}}[{{value.facilityId}}]</td>
		{{if value.transferNumber }}
		<td>{{value.transferNumber | formatDiff: 'integer'}}</td>
		{{else}}
		<td>{{value.quantityOnHandTotal | formatDiff: 'integer'}}</td>
		{{/if}}			 
		<td>{{value.sendDate}}</td>
		<td>{{value.statusId}}</td>
		<td>
			<input type="text" class="comments" value="{{value.comments}}" />
			<textarea rows="5" cols="48" class="commentsTextarea" tabindex="{{i}}"></textarea>
		</td>
	</tr>
	{{/each}}
	{{else}}
	<tr>
		<td colspan="12">No fund data</td>
	</tr>
	{{/if}}
	
</script>
<script type="text/html" id="pickingContent">

	{{if result.length>0}}
	{{each result as value i}}
	<tr>
		<td><input type="checkbox" class="checkList" data-id="{{value.id}}"/></td>
		<td>{{value.id}}</td>
		<td>{{value.toWarehouse}}</td>			 
		<td>{{value.createTime}}</td>
	</tr>
	{{/each}}
	{{else}}
	<tr>
		<td colspan="8">No fund data</td>
	</tr>
	{{/if}}
	
</script>
<script type="text/html" id="transferInventoryContent">

	{{if result.length>0}}
		{{each result as value i}}
			<tr>
			 <td><input type="checkbox" class="checkList" data-id="{{value.productId}}" /></td>
			 <td>{{value.productId}}</td>
			 <td>{{value.brandName}}</td>
			 <td>{{value.model}}</td>
             <td>{{value.description}}</td>
			 {{if value.pickingNum }}
				<td><span class="totalQty">{{value.transferAtp}}</span></td>
				{{else}}
				<td><span class="totalQty">{{value.atp | formatDiff: 'integer'}}</span></td>
			 {{/if}}
			 <td>
				 <div class="transferQty_content hide_enter_qty">
					<input type="text" class="enter_qty" value=""/>
				 </div>
			 </td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="7">No fund data</td>
			</tr>
	{{/if}}
			    
</script>
<script type="text/html" id="sentPageContent">
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
		<div style="clear:both;"></div>
	{{/if}}
</script>
<script type="text/html" id="transferListTabColumn">
	<div class="transferListTabContent">
		<div class="warehouseHead">
			<div class="warehouseName" data-faclityId="{{warehouseId}}">{{warehouseName}}</div>
			<div class="warehouseIcon"></div>
		</div>	
		<div class="warehouseContent">
			<table class="warehouseTable">
				<thead>
					<tr>
						<th>Product ID</th>
						<th>Brand</th>
						<th>Model</th>
						<th>Products Attributes</th>
						<th>Qty</th>
						<th>Edit</th>
					</tr>
				</thead>
				<tbody>
	{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.productId}}" data-qty="{{value.enterQty}}" data-brandName="{{value.brandName}}" data-model="{{value.model}}" data-description="{{value.description}}">
			 <td>{{value.productId}}</td>
			 <td>{{value.brandName}}</td>
			 <td>{{value.model}}</td>
             <td>{{value.description}}</td>
			 <td><div class="dataQty">{{value.enterQty}}<div></td>
			 <td><div class="list_icon"></div></td>
			</tr>
		{{/each}}
	{{/if}}
				</tbody>
			</table>
		</div>	
		<div class="footerIndex">
			<span class="footertabIndex">{{itemNum}}</span>
		</div>
	</div>
</script>
<script type="text/html" id="printTabColumn">
	{{if result.length>0}}
	{{each result as value i}}
	<div class="print_transferListTabContent" style="margin-bottom:20px;">
		<div class="print_warehouseHead" style="margin-bottom:10px;">
			<div class="print_warehouseName">{{value.faclityName}}({{value.faclityid}})</div>
		</div>	
		<div class="print_warehouseContent">
			<table class="print_warehouseTable" style="width:100%;">
				<thead>
					<tr>
						<th style="width:15%;text-align:center;">Product ID</th>
						<th style="width:20%;text-align:center;">Brand</th>
						<th style="width:25%;text-align:center;">Model</th>
						<th style="width:30%;text-align:center;">Products Attributes</th>
						<th style="width:10%;text-align:center;">Qty</th>
					</tr>
				</thead>
				<tbody>
	{{if value.list.length>0}}
		{{each value.list as listContent}}
			<tr>
			 <td style="width:15%;text-align:center;">{{listContent.productId}}</td>
			 <td style="width:20%;text-align:center;">{{listContent.brandName}}</td>
			 <td style="width:25%;text-align:center;">{{listContent.model}}</td>
             <td style="width:30%;text-align:center;">{{listContent.description}}</td>
			 <td style="width:10%;text-align:center;">{{listContent.transferNum}}</td>
			</tr>
		{{/each}}
	{{/if}}
				</tbody>
			</table>
		</div>	
	</div>
		{{/each}}
	{{/if}}
</script>
<script type="text/html" id="facilityTypeColumn">
{{if result}}
      {{each result as value i}}
         <option value="{{value.facilityId}}" {{if value.facilityId==defaultFacilityId}}selected{{/if}}>{{value.facilityName}}</option>
      {{/each}}
   {{/if}}
</script>
<script type="text/html" id="trContentColumn">
	{{if result.length>0}}
		{{each result as value i}}
			<tr data-id="{{value.productId}}" data-qty="{{value.enterQty}}" data-brandName="{{value.brandName}}" data-model="{{value.model}}" data-description="{{value.description}}">
			 <td>{{value.productId}}</td>
			 <td>{{value.brandName}}</td>
			 <td>{{value.model}}</td>
             <td>{{value.description}}</td>
			 <td><div class="dataQty">{{value.enterQty}}<div></td>
			 <td><div class="list_icon"></div></td>
			</tr>
		{{/each}}
	{{/if}}
</script>
<script>
require.config({
	paths:{
		select2 : '/commondefine_js/select2/select2',
		layDate:'/commondefine_js/layDate/laydate.min',
		editImeiDialogHtml:'/commondefine_js/tempate/transferInventory/editImeiDialog.html',
		editImeiDialog:'/commondefine_js/tempate/transferInventory/editImeiDialog',
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
	template.helper('formatImei', function(value) {
	    if(value){
	    	return (value == 'NON_SERIAL_INV_ITEM' ? 'N' : 'Y');
	    }else{
	    	return value;
	    }
	});
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	"click #newTransferInventoryTab a" : "initTypeEvent",
        	"click .sent_search .search_confirm,.receive_search .search_confirm,.transferInventory_search .select_confirm,.picking_search .select_confirm" : "getTableData",
        	'click .sent_search .js_sndate, .receive_search .js_sndate':'showDate',
        	"click .sent_table .selectAll, .receive_table .selectAll, .transferInventory_table .selectAll, .picking_table .selectAll" : "selectTableAll",
        	"click .sent_table .checkList, .receive_table .checkList, .transferInventory_table .checkList, .picking_table .checkList" : "selectCheckList",
        	"click .sent_table .comments, .receive_table .comments" : "updateComments",
        	"blur .sent_table .commentsTextarea, .receive_table .commentsTextarea" : "updateCommentsTextarea",
        	"click .transferInventory_table_box .add_qty" : "addQtyEvent",
        	"click .transferInventory_table_box .reduce_qty" : "reduceQtyEvent",
        	"blur .transferInventory_table_box .enter_qty" : "enterQtyEvent",
        	"click .transferInventory_warehouse .add_confirm" : "addTransferListEvent",
        	"click .right_list .warehouseIcon" : "transferListTabContentEvent",
        	"click .right_list .list_icon" : "transferListIconEvent",
        	'click .tab-pane .footer_page a' : "changePageEvent",
        	'keypress .tab-pane .footer_page .go_page_input' : "jumpPageEvent",
        	"click .tab-pane .sent_complete, .tab-pane .receive_complete" : "operatorAjaxEvent",
        	"click .right_list .confirmList" : "transferConfirmEvent",
        	"click .transferInventory_imei .returnPage, .transferInventory_imei .submit_later_imei" : "submitLaterImeiEvent",
        	"click .transferInventory_imei .submit_imei" : "submitImeiEvent",
        	"click .picking_operator .cancel_confirm" : "cancelPickingEvent",
        	"click .picking_operator .picking_confirm" : "confirmPickingEvent",
        	"keyup .transferInventory_imei_content .productDesc" : "changEanEvent",
        	"keyup .transferInventory_imei_content .productImei" : "changImeiEvent",
        	"click .right_list .printList" : "printInvocing",
        	"click .transferInventory_imei .openScan" : "openScanDialog",
        },
        __propertys__: function () {
        },
        showDate:function(e){
 		   require(['layDate'], function(layDate){
 			   layDate({
 				   eventArg: e,
 				   istime: true,
 				   format: 'DD/MM/YYYY'
 				   //format: 'YYYY-MM-DD hh:mm:ss'
 			   });
 		   });
        },  
        initialize: function ($super) {
            $super();
            this.initRoleData();
            this.initFacilityData(); 
            //this.initEvent(); 
            this.initPermissionData(); 
            this.initType();
            this.initCircle();
            this.getTableData(null, $('.tab-pane.active').attr('data-type'));
            this.data = null;
            this.currentData = [];
            this.isOldTransfer = null;
            
        },
        getUrlParams : function(name) {
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	        var r = window.location.search.substr(1).match(reg);
	        if (r != null) return unescape(r[2]);
	        return null;
	    },
	    initCircle : function(){
	    	var t = this;
	        $.ajax({
	            url:"/warehouse/control/checkUserHasStoreManage",
	            type: 'post',
	            dataType: 'json',
	            data:{},
	            success: function(res){
	                if(res.resultCode == 1){
	                    if(res.isStoreManage == 'N'){
	                    	$('.receiveNum').css('display', 'none');
	                    }else{
	                    	t.getReceiveEvt();
	                    }
	                }
	            }
	         });
	    },
	    getReceiveEvt: function(){
	    	var t = this;
	    	var getParams = t.getParams('Receive', '1');
            $.ajax({
                url:"/warehouse/control/queryTransferData",
                type: 'post',
                dataType: 'json',
                data:JSON.stringify(getParams),
                success: function(res){
                    if(res.resultCode == 1){
                        if(res.totalNum > 0){
                        	$('.receiveNum').css('display', 'inline-block');
                        }else{
                        	$('.receiveNum').css('display', 'none');
                        }
                    }
                }
             });
	    },
	    initType: function(){
	    	var t = this;
	    	var type = t.getUrlParams('type');
	    	if(type){
	    		
	    		$('.newTransferInventory_tab').find('a').closest('li').removeClass('active');
	    		$('.newTransferInventory_tab').find('a[data-type="'+ type +'"]').closest('li').addClass('active');
	    		
	    		$('.newTransferInventory_list').find('.tab-pane').removeClass('active');
	    		$('.newTransferInventory_list').find('.tab-pane[data-type="'+ type +'"]').addClass('active');
	    		
	    		t.getTableData();
	    	}
	    },
        changEanEvent : function(e){
        	var t = this;
        	var evt = $(e.currentTarget);
        	var value = evt.val();
        	var reg= /^[0-9]\d*$/;
        	
        	var len8 = value.length == 8 ? true : false;
        	var len13 = value.length == 13 ? true : false;
        	
        	if(value == ''){
        		evt.removeClass('redBorder');
        	}else{
        		if(reg.test(value)){
                	if(len8 || len13){
                		evt.removeClass('redBorder');
                	}else{
                		evt.addClass('redBorder');
                	}
        		}else{
        			evt.addClass('redBorder');
        		}
        	}
        },
        filterEvt : function(str) { 
        	return str.replace(/[-|\r\n\s+,]/g, "");
        },
        changImeiEvent : function(e){
        	var t = this;
        	var evt = $(e.currentTarget);
        	var productImeiNum = $(e.currentTarget).closest('tr').find('.productImeiNum span');
        	
        	var value = t.filterEvt(evt.val());
        	var num = evt.closest('tr').find('.tdTransNum').text();
        	var isImei = evt.closest('tr').find('.tdImei').text();
        	var ImeiArr = [];
        	var reg= /^[0-9]\d*$/;
        	
        	if(isImei == 'Y'){
            	if(value.length > 15){
            		var valueList = (value.split(',')).join('');
            		var len = valueList.length / 15;
                	if(reg.test(valueList)){
                		if(valueList.length % 15 == 0){
                			if(len <= num){
                				evt.removeClass('redBorder');
                			}else{
                				evt.addClass('redBorder');
                			}
                		}else{
                			evt.addClass('redBorder');
                		}
                	}else{
                		evt.addClass('redBorder');
                	}
                	
            		for(var i=0; i<Math.floor(len); i++){
            			ImeiArr.push(valueList.substr(i*15,15));
            		}
            		if(Math.floor(len) != Math.ceil(len)){
            			ImeiArr.push(valueList.substr(Math.floor(len)*15, (valueList.length-Math.floor(len)*15)));
            		}
            		
            		ImeiArr = t.removeDuplicatedItem(ImeiArr);
            		
            		evt.val(ImeiArr.join(','));
            		
            		productImeiNum.text(ImeiArr.length);
            		
            		if(ImeiArr.length > parseInt(num, 10)){
            			t.showToast('The quantity must be less than or equal to transfer quantity.');
            		}
            		
            	}else{
            		if(value.length > 0){
            			productImeiNum.text(1);
            		}else{
            			productImeiNum.text('');
            		}
            		if(reg.test(value)){
            			if(value.length == 15){
            				if(num >= 1){
            					evt.removeClass('redBorder');
            				}else{
            					evt.addClass('redBorder');
            				}
            			}else{
            				evt.addClass('redBorder');
            			}
            		}else{
            			evt.addClass('redBorder');
            		}
            	}
        	}else if(isImei == 'N'){
        		if(reg.test(value)){
        			if(value == num){
        				evt.removeClass('redBorder');
        			}else{
        				evt.addClass('redBorder');
        			}
        		}else{
        			evt.addClass('redBorder');
        		}
        	}
        },
        openScanDialog : function(e){
        	var t = this;
        	
        	require(['editImeiDialog'], function(editImeiDialog){
            	new editImeiDialog({
            		currentModel: $(e.currentTarget).attr("data-model"),
            		currentNum : $(e.currentTarget).attr("data-transNum"),
            		trackingId : $(e.currentTarget).closest(".imei_transferListTabContent").find('.imei_warehouseName').attr('data-id'),
            		productId : $(e.currentTarget).closest("tr").attr('data-productid'),
            		onSelected:function(data){
            			if(data != 0){
            				$(e.currentTarget).closest("tr").find(".imeiTansferList").removeClass('noTransfer');
            			}else{
            				$(e.currentTarget).closest("tr").find(".imeiTansferList").addClass('noTransfer');
            			}
            			$(e.currentTarget).closest("tr").find(".imeiTansferList").text(data);
            			
            			var imeiTransferListTabContent = $(e.currentTarget).closest('.imei_transferListTabContent');
            			
            			if(imeiTransferListTabContent.find('.noTransfer').length == 0){
            				imeiTransferListTabContent.find('.inputList').prop('disabled', false);
            			}else{
            				imeiTransferListTabContent.find('.inputList').prop('disabled', true);
            			}
            		}
            	});
            });
        },
        removeDuplicatedItem : function(arr) {
            var tmp = {},
                ret = [];

            for (var i = 0, j = arr.length; i < j; i++) {
                if (!tmp[arr[i]]) {
                    tmp[arr[i]] = 1;
                    ret.push(arr[i]);
                }
            }

            return ret;
        },
        confirmPickingEvent : function(e){
        	var t =this;
        	
        	var checkedSize = $('.picking_total_size').text();
        	var tableList = $('.tab-pane.active table tbody');
        	var checkedList = tableList.find('.checkList:checked');
        	var arrList = [];
        	
        	if(checkedSize == 0){
        		t.showToast('Please select the transhipment order you want to pick.');
        		
        		return false;
        	}
        	
        	if(checkedSize > 5){
        		t.showToast('You can choose at most 5 transfer bills once.');
        		
        		return false;
        	}
        	
        	$.each(checkedList, function(index, elem){
        		arrList.push($(elem).attr('data-id'));
        	});
        	
        	e && $.showLoading($(e.currentTarget));
           	$.ajax({
   				url:'/warehouse/control/PickingByTranshipmentShippingBillId',
   				type: 'post',
   	            dataType: 'json',
   	         	data : JSON.stringify({
   	         		trackingId : arrList.join(',')
   	         	}),
   	           	success:function(results){
   	           		e && $.hideLoading($(e.currentTarget));
   	           	
   					if(results.resultCode==1){
						t.toogleShow(0);
						
						if(results.result.length > 0){
							var resultList = $.each(results.result, function(index, elem){
								var pickingVo = elem.pickingVo;
								var flag = false;
								$.each(pickingVo, function(indexSrc, elemSrc){
									if(elemSrc.type == "SERIALIZED_INV_ITEM" && elemSrc.scanNum == 0){
										flag = true;
									}
								});
								if(flag){
									elem.disabled = true;
								}
							});
							results.result = resultList;
							$('.transferInventory_imei_content').html(template("transferImeiList",{
					        	result : results.result
					        }));
						}
   					}else{
   						t.showToast(results.resultMsg);
   					}
   	           	},
   	           	error : function(option, status){
    				e && $.hideLoading($(e.currentTarget));
    			}
           	});
        },
        cancelPickingEvent : function(e){
        	var t =this;
        	
        	var checkedSize = $('.picking_total_size').text();
        	var tableList = $('.tab-pane.active table tbody');
        	var checkedList = tableList.find('.checkList:checked');
        	var arrList = [];
        	
        	if(checkedSize == 0){
        		t.showToast('Please select the transhipment order you want to cancel');
        		
        		return false;
        	}
        	
        	$.each(checkedList, function(index, elem){
        		arrList.push($(elem).attr('data-id'));
        	});
        	
        	e && $.showLoading($(e.currentTarget));
           	$.ajax({
   				url:'/warehouse/control/cancelTranshipmentShippingBill',
   				type: 'post',
   	            dataType: 'json',
   	         	data : JSON.stringify({
   	         		trackingId : arrList.join(',')
   	         	}),
   	           	success:function(results){
   	           		e && $.hideLoading($(e.currentTarget));
   	           		
   					if(results.resultCode==1){
   						t.toogleShow(1);
   						
   						t.showToast("Picking cancel success", function(){
   							t.getTableData();
						});
   					}else{
   						t.showToast(results.resultMsg);
   					}
   	           	},
   	           	error : function(option, status){
    				e && $.hideLoading($(e.currentTarget));
    			}
           	});
        },
        submitImeiEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
			var imeiArr = [];
			var confirmArr = [];
			var imeiTransferListTabContent = $('.transferInventory_imei .inputList:checked').closest('.imei_transferListTabContent');
        	
        	if($(evt).hasClass('no_checked')){
        		return false;
        	}
        	
        	if(imeiTransferListTabContent.length == 0){
        		t.showToast('Please select your products');
        		return false;
        	}
			
			$.each(imeiTransferListTabContent, function(index, elem){
				
				var imeiTrackingId = $(elem).find('.imei_warehouseName').attr('data-id');
				var warehouse = $(elem).find('.imei_warehouseName').attr('data-warehouse');
				var imeiWarehouseTableTr = $(elem).find('.imei_warehouseTable tbody tr');
				var pickingDtos = [];
				var confirmDtos = [];
				
				$.each(imeiWarehouseTableTr, function(indexTr, elemTr){
					if($(elemTr).find('.tdImei').text() == 'N'){
						pickingDtos.push({
							productId : $(elemTr).attr('data-productid'),
							transNum : $(elemTr).find('.tdTransNum').text(),
							isIMEI : $(elemTr).find('.tdImei').text(),
							//imei : $(elemTr).find('.tdImei').text() == 'N' ? $(elemTr).find('.tdImei').text() : $(elemTr).find('.productImei').val().trim(),
							imei : '',
							EAN : ''
						})
					}
					
					confirmDtos.push({
						productId : $(elemTr).attr('data-productid'),
						transNum : $(elemTr).find('.tdImei').text() == 'N' ? $(elemTr).find('.tdTransNum').text() : $(elemTr).find('.imeiTansferList').text(),
						type : $(elemTr).find('.tdImei').text() == 'N' ? 'NON_SERIAL_INV_ITEM' : 'SERIALIZED_INV_ITEM',
						tdDescritpion : $(elemTr).find('.tdDescritpion').text()
					});
				});
				
				imeiArr.push({
					transhipmentShippingBillId : imeiTrackingId,
 					pickingDtos : pickingDtos
				});
				
				//submit print
				confirmArr.push({
					transNum : imeiTrackingId,
					pickingVo : confirmDtos,
					warehouse : warehouse
				});
				
			});
			$('.transferInventory_imei').find('.submit_imei').addClass('no_checked');
			
			e && $.showLoading($(e.currentTarget));
			$.ajax({
				url:'/warehouse/control/startPicking',
				type: 'post',
				dataType: 'json',
				data : JSON.stringify(imeiArr),
				success:function(results){
					e && $.hideLoading($(e.currentTarget));
					
					if(results.resultCode==1){
						
			        	$('.right_list_print').html(template("newTransferPrint",{
				        	result : confirmArr
			        	}));
 				        	
			        	$.each(confirmArr, function(index, elem){
			        		$('#imgcode'+index).JsBarcode(elem.transNum, {
			        			fontSize:24,
			        			fontOptions:"bold"
			        		});
			        	});
			        	
			       		//new print
			        	var openWindow = window.open("", "Print");
			       		
						openWindow.document.title = "Print";
						
		        		var arr = [];
		        		
		        		var headContent = '<div style="width:100%;height:30px;line-height:30px;font-size:22px;text-align:center;color:#1FBCD2;">Actual Transfer Bills</div>';
		        		
		        		arr.push('<script>');
		        		arr.push("var imageLen = document.querySelectorAll('img').length;");
						arr.push("document.querySelectorAll('img')[imageLen - 1].onload=function(){ window.print();window.close(); }");
						arr.push('<\/script>');
						
		        		openWindow.document.write(headContent + $('.right_list_print').html() + arr.join(' '));
			        	
		        		window.location.href="/warehouse/control/newTransferInventory?type=Picking";
						//location.reload();
					}else if(results.resultCode == -2){
						$('.transferInventory_imei').find('.submit_imei').removeClass('no_checked');
						
						if(results.result && results.result.length){
							var str = [];
							$.each(results.result, function(index, elem){
								str.push('<tr><td>'+ elem.imei + '</td><td>' + elem.description +'</td></tr>') 
							});
							
							t.showConfirm('<table id="confirmTable">' + 
								'<thead><tr><td>IMEI</td><td>Description</td></tr></thead>' +
								'<tbody>'+ str.join('') +'</tbody>'+
								'</table>', null, null, {
								title: 'Error Message',
								okValue: 'Confirm',
								cancelValue: 'Cancel',
								cancel : false,
								width: '600px'
							});
						}
					}else{
						$('.transferInventory_imei').find('.submit_imei').removeClass('no_checked');
						
						t.showToast(results.resultMsg);
					}
				},
   	           	error : function(option, status){
    				e && $.hideLoading($(e.currentTarget));
    			}
			})
        },
        submitLaterImeiEvent : function(){
        	var t = this;
        	t.checkedTabEvent("Picking");
        	t.toogleShow(1);
        	
        	$('.picking_table_box').find('tbody input').attr("checked", false);
        },
        checkedTabEvent : function(options){
			$('#newTransferInventoryTab').find('a[data-type="'+ options +'"]').closest('li').siblings('li').removeClass('active');
			$('#newTransferInventoryTab').find('a[data-type="'+ options +'"]').closest('li').addClass('active');
			
			$('.newTransferInventory_list').find('.tab-pane').removeClass('active');
			$('.newTransferInventory_list').find('.tab-pane[data-type="'+ options +'"]').addClass('active');
		},
        transferConfirmEvent : function(e){
        	var t =this;
        	var evt = e.currentTarget;
        	
        	if($(evt).hasClass('no_checked')){
        		return false;
        	}
        	
        	var transferListTabContent = $('.right_list .transferListTabContent');
        	var contentTr = [];
        	$.each(transferListTabContent, function(index, elem){
        		var faclityid = $(elem).find('.warehouseName').attr('data-faclityid');
        		var trList = $(elem).find('tbody tr');
        		$.each(trList, function(trIndex, trElem){
        			contentTr.push({
        				facilityTo : faclityid,
        				productId : $(trElem).attr('data-id'),
        				transferNum : $(trElem).attr('data-qty'),
        				brandName : $(trElem).attr('data-brandname'),
        				model : $(trElem).attr('data-model'),
        				description : $(trElem).attr('data-description'),
        			});
        		})
        		
        	});
        	$('.right_list').find('.confirmList').addClass('no_checked');
        	
        	if($('#permission').val() == 'Y'){
               	$.ajax({
       				url:'/warehouse/control/toPickUpInventory',
       				type: 'post',
       	            dataType: 'json',
       	         	data : JSON.stringify(contentTr),
       	           	success:function(results){
       					if(results.resultCode==1){
       						$('.right_list').addClass('right_hide');
       						
       						t.toogleShow(0);
       						t.checkedTabEvent('Picking');
       						t.openRightContent();
       						t.getImeiList(results.trackingId);
       						t.newPrintInvocing(results.trackingId);
       						
       						$('.right_list').find('.confirmList').removeClass('no_checked');
       						t.currentData = [];
       						$('.right_list_content').html('');
       					}else{
       						$('.right_list').find('.confirmList').removeClass('no_checked');
       						
       						t.showToast(results.resultMsg);
       					}
       	           	}
               	})
        	}else if($('#permission').val() == 'N'){
               	$.ajax({
       				url:'/warehouse/control/inventoryTransShipment',
       				type: 'post',
       	            dataType: 'json',
       	         	data : JSON.stringify(contentTr),
       	           	success:function(results){
       					if(results.resultCode==1){
       						t.showToast("Stock transfer success", function(){
       							window.location.href="/warehouse/control/newTransferInventory?type=transferInventory";
       							//location.reload();
       						});
       					}else{
       						$('.right_list').find('.confirmList').removeClass('no_checked');
       						
       						t.showToast(results.resultMsg);
       					}
       	           	}
               	})
        	}
        },
        getImeiList : function(option){
        	var t = this;
        	
        	t.getTrackingList(option, 'list');
        },
        newPrintInvocing : function(option){
        	var t = this;
        	
        	t.getTrackingList(option, 'print');
        },
        getTrackingList : function(option, type){
        	var t =this;
        	
           	$.ajax({
   				url:'/warehouse/control/PickingByTranshipmentShippingBillId',
   				type: 'post',
   	            dataType: 'json',
   	         	data : JSON.stringify({
   	         		trackingId : option
   	         	}),
   	           	success:function(results){
   					if(results.resultCode==1){
   						
						if(results.result.length == 0){
 							return false;
 						}
   						if(type == 'list'){
							var resultList = $.each(results.result, function(index, elem){
								var pickingVo = elem.pickingVo;
								var flag = false;
								$.each(pickingVo, function(indexSrc, elemSrc){
									if(elemSrc.type == "SERIALIZED_INV_ITEM" && elemSrc.scanNum == 0){
										flag = true;
									}
								});
								if(flag){
									elem.disabled = true;
								}
							});
							results.result = resultList;
   				        	$('.transferInventory_imei_content').html(template("transferImeiList",{
   				        		result : results.result
   				        	}));
   						}else if(type == 'print'){
   							
   				        	$('.right_list_print').html(template("newTransferPrint",{
   				        		result : results.result
   				        	}));
   				        	
 				        	$.each(results.result, function(index, elem){
 				        		$('#imgcode'+index).JsBarcode(elem.transNum, {
 				        			fontSize:24,
 				        			fontOptions:"bold"
 				        		});
 				        	});
   				       		
   				       		//new print
   				        	var openWindow = window.open("", "Print");
   				       		
   							openWindow.document.title = "Print";
   							
   			        		var arr = [];
   			        		
   			        		var headContent = '<div style="width:100%;height:30px;line-height:30px;font-size:22px;text-align:center;color:#1FBCD2;">Transfer Bills</div>';
   			        		
   			        		arr.push('<script>');
   			        		arr.push("var imageLen = document.querySelectorAll('img').length;");
   							arr.push("document.querySelectorAll('img')[imageLen - 1].onload=function(){ window.print();window.close(); }");
   							arr.push('<\/script>');
   							
   			        		openWindow.document.write(headContent + $('.right_list_print').html() + arr.join(' '));
   						}
   					}else{
   						t.showToast(results.resultMsg);
   					}
   	           	}
           	});

        },
		toogleShow : function(type){
			var t = this;
			
			if(type == 0){
				$('.transferInventory_items').css('display','none');
				$('.transferInventory_imei').css('display','block');
			}else if(type == 1){
				$('.transferInventory_items').css('display','block');
				$('.transferInventory_imei').css('display','none');
			}
		},
        operatorAjaxEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var tableList = $('.tab-pane.active table tbody');
        	var trList = tableList.find('.checkList:checked');
        	var dataType = $('.tab-pane.active').attr('data-type');
        	var arrList = [];
        	if(trList.length == 0){
        		return false;
        	}
        	
        	if($(evt).hasClass('no_checked')){
        		return false;
        	}
        	
        	$.each(trList, function(index, elem){
        		arrList.push({
        			transferId : $(elem).attr('data-transferId'),
        			statusId : dataType == 'Sent' ? 'IXF_CANCELLED' : 'IXF_COMPLETE',
        			comments : $(elem).closest('tr').find('.comments').val()
        		});
        	});

        	$('.tab-pane.active').find('.operator_list a').addClass('no_checked');
           	$.ajax({
   				url:'/warehouse/control/completeOrCancelledTransfer',
   				type: 'post',
   	            dataType: 'json',
   	         	data : JSON.stringify(arrList),
   	           	success:function(results){
   					if(results.resultCode==1){
   						if(dataType == 'Sent'){
   	   						t.showToast("Sent success", function(){
   	   							window.location.href="/warehouse/control/newTransferInventory?type=Sent";
   	   							//location.reload();
   	   						});
   						}else{
   	   						t.showToast("Receive success", function(){
   	   							window.location.href="/warehouse/control/newTransferInventory?type=Receive";
   	   							//location.reload();
   	   						});
   						}
   					}
   	           	}
           	})
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

	    	t.getTableData(null, $(evt).closest('.tab-pane.active').attr('data-type'),$(evt).attr('data-num'));
	    	
	    	t.initHeight();
		},
		jumpPageEvent:function(e){
			var t=this;
			var key = e.which;
			var reg= /^\b[1-9]\d*$/;
			if(key==13){					
				if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
					t.getTableData(null, $(e.currentTarget).closest('.tab-pane.active').attr('data-type'), $(e.currentTarget).val());
			    }
			}
			
			t.initHeight();
		},
        transferListIconEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var transferListTabContent = $(evt).closest('.transferListTabContent');
        	var height = transferListTabContent.height();
        	var deleteItem = [{
	        		productId : $(evt).closest('tr').attr('data-id'),
	    			enterQty : parseInt($(evt).closest('tr').attr('data-qty'), 10)
        		}];
        	
        	if(transferListTabContent.find('tbody tr').length == 1){
        		var trHeight = $(evt).closest('tr').height();
        		$(evt).closest('tr').remove();
        		
        		t.operatorQtyEvent(deleteItem);
        		
        		transferListTabContent.remove();
        		t.initHeight(height + trHeight);
        		
            	if($('.right_list .transferListTabContent').length == 0){
            		
            		$('.right_list').addClass('right_hide');
            		t.openRightContent();
            		
            		return false;
            	}
            	
            	$('.right_list .transferListTabContent').each(function(index, elem){
            		$(elem).find('.footertabIndex').text(index + 1);
            	});
        		
        		return false;
        	}else{
        		var trHeight = $(evt).closest('tr').height();
        		$(evt).closest('tr').remove();
        		
        		t.operatorQtyEvent(deleteItem);
        		t.initHeight(trHeight);
        	}
        	
        },
        transferListTabContentEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var height =  $(evt).closest('.transferListTabContent').height();
        	var deleteList = $(evt).closest('.transferListTabContent').find('tbody tr');
        	
        	$(evt).closest('.transferListTabContent').remove();
        	
        	var deleteItem = [];
        	$.each(deleteList, function(index, elem){
        		deleteItem.push({
        			productId : $(elem).attr('data-id'),
        			enterQty : parseInt($(elem).attr('data-qty'), 10)
        		})
        	});
        	
        	t.operatorQtyEvent(deleteItem);
        	
        	t.initHeight(height);
        	if($('.right_list .transferListTabContent').length == 0){
        		
        		$('.right_list').addClass('right_hide');
        		t.openRightContent();
        		
        		return false;
        	}
        	
        	$('.right_list .transferListTabContent').each(function(index, elem){
        		$(elem).find('.footertabIndex').text(index + 1);
        	});
        	
        },
        operatorQtyEvent : function(options){
        	var t = this;
        	
        	if(options.length > 0){
        		for(var i = 0; i < options.length; i++){
        			for(var j = 0; j< t.currentData.length; j++){
        				if(options[i].productId == (t.currentData)[j].productId){
        					(t.currentData)[j].enterQty -= options[i].enterQty;
        				}
        			}
        		}
        	}
        	
        	//delete after add 
        	var trList = $('.tab-pane.active table tbody tr');
     	
        	for(var i = 0; i < options.length; i++){
            	for(var j = 0; j < trList.length; j++){
            		var dataId = $(trList[j]).find('.checkList').attr('data-id');
            		if(options[i].productId == dataId){
            			var qtyEle = $(trList[j]).find(".totalQty");
            			qtyEle.text(parseInt(qtyEle.text()) + options[i].enterQty);
            		}
            	}
        	}
        },
        initHeight : function(height){
        	var body = document.body.clientHeight - 55;
        	var rightColumn = $('.right_column').height();
        	
        	if(!$('.right_list').hasClass('right_hide')){
    			var rightList = $('.right_list').height();
    			var yiwillHeader = $('#yiwill_header').height();
    			
    			if(body < rightList){
    				$('.newTransferInventory_box').height(rightList);
    			}else{
    				if(height){
    					if((body-height-97) > rightColumn){
    						$('.newTransferInventory_box').height(body-height-97);
    					}else{
    						$('.newTransferInventory_box').height(rightColumn);
    					}
    				}else{
    					if((body-97) > rightList){
    						$('.newTransferInventory_box').height(body-97);
    					}else{
    						$('.newTransferInventory_box').height(rightList);
    					}
    				}
    			}
    		}
        },
        getWarehouseListTabData : function(){
        	var t =this;
        	var params = {};
        	var lisrArr = [];
        	
        	var tableList = $('.tab-pane.active');
        	var checkedList = tableList.find('.checkList:checked');
        	
        	var warehouseName = $('.transferInventory_add_warehouse option:selected').text();
        	var warehouseId = $('.transferInventory_add_warehouse option:selected').val();
        	
        	$.each(checkedList, function(index, elem){
    			var trList = $(elem).closest('tr');
    			var enterQty = parseInt(trList.find('.enter_qty').val() ,10);
    			
            	$.each(t.data, function(indexData, elemData){
            		if(elemData.productId == $(elem).attr('data-id')){
            			elemData.enterQty = enterQty;
            			var content = $.extend({}, elemData)
            			lisrArr.push(content);
            		}
            	});
        	});
        	
        	params.warehouseName = warehouseName;
        	params.warehouseId = warehouseId;
        	params.result = lisrArr;
        	params.itemNum = $('.right_list .transferListTabContent').length + 1;
        	
        	return params;
        },
        uniqueArr : function(Array_list){
        	var t = this;
        	
        	var res = new Array();
        	var json = {};
	       	for(var i = 0; i < Array_list.length; i++){
	       	   if(!json[Array_list[i].productId]){
	       		   var arrList = $.extend({}, Array_list[i]);
		       	   res.push(arrList);
		       	   
		       	   json[arrList.productId] = 1;
	       	   }else{
	       		   $.each(res, function(index, elem){
	       			   if(elem.productId == Array_list[i].productId){
	       					elem.enterQty += Array_list[i].enterQty;
	       			   }
	       		   });
	       	   }
	       	}
	       	
	       	return res;
        },
        addTransferListEvent : function(e){
        	var t = this;

        	var rightList = $('.right_list');
        	var tableList = $('.tab-pane.active');
        	var checkedList = tableList.find('.checkList:checked');
        	var flag = false;
        	var isExist = false;
        	
        	var wareHouseId = $('.transferInventory_add_warehouse').val();
        	if(rightList.find('.transferListTabContent').length == 5){
        		var wareHouseIdList = [];
        		$.each($('.right_list .transferListTabContent .warehouseName'), function(index, elem){
        			wareHouseIdList.push($(elem).attr('data-faclityid'));
        		})
        		if(wareHouseIdList.indexOf(wareHouseId) > -1){
        			isExist = true;
        		}
        		if(!isExist){
            		t.showToast("There are five transfer tabs");
            		
                	//clear checkbox checked
                	$('.tab-pane.active').find('table .checkList,table .selectAll').prop('checked', false);
                	
            		//reset value null
            		$('.tab-pane.active').find('table .enter_qty').val("").parent('.transferQty_content').addClass('hide_enter_qty');
            		
            		return false;
        		}
        	}
        	
        	if(checkedList.length == 0){
        		t.showToast("Please select transfer items");
        		
        		return false;
        	}
        	
        	$.each(checkedList, function(index, elem){
        		if($(elem).closest('tr').find('.enter_qty').val() == ''){
        			flag = true;
        		}
        		
        		var enterQty = parseInt($(elem).closest('tr').find('.enter_qty').val(), 10);
        		var reg= /^\b[1-9]\d*$/;
        		
        		if(!reg.test(enterQty)){
        			flag = true;
        		}
        	});
        	
        	if(flag){
        		t.showToast("Please enter the correct number of transfers");
        		
        		return false;
        	}
        	
    		//add after reduce qty
    		var flag_list = false;
    		$.each(checkedList, function(index, elem){
    			var trList = $(elem).closest('tr');
    			var totalQty = parseInt(trList.find('.totalQty').text() ,10);
    			var enterQty = parseInt(trList.find('.enter_qty').val() ,10);
    			
    			if(totalQty < enterQty || totalQty == 0){
    				flag_list = true;
    				
            		return false;
    			}

    			trList.find('.totalQty').text(totalQty - enterQty);
    		});
    		
    		if(flag_list){
    			t.showToast("Inventory shortage");
    			
    			return false;
    		}
        	
    		//if exist tabs , add tr
    		var transferListTabContent = $('.right_list .transferListTabContent');
    		if(transferListTabContent.length == 0){
    			$('.right_list_content').append(template("transferListTabColumn",t.getWarehouseListTabData()));
    		}else if(transferListTabContent.length > 0){
    			var faclityIdList = [];
    			
    			var currentFaclityId = t.getWarehouseListTabData().warehouseId;
    			$('.right_list .transferListTabContent').each(function(index, elem){
    				faclityIdList.push($(elem).find('.warehouseName').attr("data-faclityId"));
            	});
    			
    			if(faclityIdList.indexOf(currentFaclityId) > -1){
    				var tbodyParent = $('.right_list').find('.warehouseName[data-faclityId="'+ currentFaclityId +'"]').closest('.transferListTabContent');
    				var productIdList = [];
    				var listContent = [];
    				
    				$.each(tbodyParent.find('tbody tr'), function(indexSrc, elemSrc){
    					productIdList.push($(elemSrc).attr('data-id'));
    				});
    				
    				$.each(t.getWarehouseListTabData().result, function(indexResult, elemResult){
    					if(productIdList.indexOf(elemResult.productId) > -1){
    						var dataTr =  tbodyParent.find('tr[data-id="'+ elemResult.productId +'"]');
    						var dataQty =  parseInt(dataTr.attr('data-qty'), 10);
    						
    						dataQty += elemResult.enterQty;
    						
    						dataTr.attr('data-qty', dataQty);
    						dataTr.find('.dataQty').text(dataQty);
    					}else{
    						listContent.push(elemResult);
    					}
    				});
    				tbodyParent.find('tbody').append(template("trContentColumn",{
    					result : listContent,
    		        	warehouseName : t.getWarehouseListTabData().warehouseName,
    	        		warehouseId : t.getWarehouseListTabData().warehouseId,
    	        		itemNum :t.getWarehouseListTabData().itemNum
    				}));
    			}else{
    				$('.right_list_content').append(template("transferListTabColumn",t.getWarehouseListTabData()));
    			}
    		}
    		
        	var currentTabData = t.currentData;
        	var dataList = $.extend({}, t.getWarehouseListTabData());
        	t.currentData = t.uniqueArr(currentTabData.concat(dataList.result));
    		
        	//clear checkbox checked
        	$('.tab-pane.active').find('table .checkList,table .selectAll').prop('checked', false);
        	
    		//reset value null
    		$('.tab-pane.active').find('table .enter_qty').val("").parent('.transferQty_content').addClass('hide_enter_qty');
        	
    		$('.right_list').removeClass('right_hide');
    		t.openRightContent();
    		
    		t.initHeight();
        },
        addQtyEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var oldEnterQty = parseInt($(evt).siblings('input').val(), 10);
        	var totalQty = parseInt($(evt).closest('tr').find('.totalQty').text(), 10);
        	var newEnterQty = oldEnterQty + 1;
        	if(newEnterQty > totalQty){
        		$(evt).siblings('input').val(oldEnterQty);
        		return false;
        	}
        	$(evt).siblings('input').val(newEnterQty);
        	//$(evt).closest('tr').find('.totalQty').text(totalQty - newEnterQty);
        },
        reduceQtyEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var oldEnterQty = parseInt($(evt).siblings('input').val(), 10);
        	var totalQty = parseInt($(evt).closest('tr').find('.totalQty').text(), 10);
        	var newEnterQty = oldEnterQty - 1;
           	if(newEnterQty < 1){
           		$(evt).siblings('input').val(oldEnterQty);
           		return false;
           	}
           	$(evt).siblings('input').val(newEnterQty);
           	//$(evt).closest('tr').find('.totalQty').text(totalQty + newEnterQty);
        },
        enterQtyEvent : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var totalQty = parseInt($(evt).closest('tr').find('.totalQty').text(), 10);
        	
        	var enterQty = $(evt).val();
        	var newExp = /^\b[1-9]\d*$/;
        	if(!newExp.test(enterQty)){
        		t.showToast("Please enter the correct number");
        		
        		$(evt).val("");
        		return false;
        	}
        	if(enterQty > totalQty){
        		t.showToast("Please enter the correct number");
        		
        		$(evt).val("");
        		return false;
        	}
        	//$(evt).closest('tr').find('.totalQty').text(totalQty - enterQty);
        },
        showPage : function(){
        	$('#newTransferInventory').show();
        },
        selectTableAll : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var tableList = $('.tab-pane.active');
        	var footerTotal = tableList.find('.total');
        	
        	if($(evt).is(':checked') == true){
        		tableList.find('.checkList').not(':disabled').prop('checked', true);
        		if(tableList.find('.checkList:checked').length > 0){
        			tableList.find('.operator_list a').removeClass('no_checked');
        			tableList.find('.transferQty_content').removeClass('hide_enter_qty');
        		}
        	}else{
        		tableList.find('.checkList').prop('checked', false);
        		tableList.find('.operator_list a').addClass('no_checked');
        		tableList.find('.transferQty_content').addClass('hide_enter_qty');
        		tableList.find('.transferQty_content input').val('');
    		}
        	footerTotal.text(tableList.find('.checkList:checked').length);
        },
        selectCheckList : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var tableList = $('.tab-pane.active');
        	var countList = tableList.find('.checkList').length;
        	var checkedList = tableList.find('.checkList:checked').length;
        	var disabledList = tableList.find('.checkList:disabled').length;
        	var footerTotal = tableList.find('.total');
        	
        	if(countList == (checkedList+disabledList)){
        		tableList.find('.selectAll').prop('checked', true);
        	}else{
        		tableList.find('.selectAll').prop('checked', false);
        	}
        	footerTotal.text(tableList.find('.checkList:checked').length);
        	
           	if(checkedList != 0){
           		tableList.find('.operator_list a').removeClass('no_checked');
           	}else{
           		tableList.find('.operator_list a').addClass('no_checked');
           	}
           	
           	if($(evt).is(':checked') == true){
           		$(evt).closest('tr').find('.transferQty_content').removeClass('hide_enter_qty');
           	}else{
           		$(evt).closest('tr').find('.transferQty_content').addClass('hide_enter_qty');
           		$(evt).closest('tr').find('.transferQty_content input').val('');
           	}
        },
        updateComments : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var commentsTextArea = $(evt).siblings('.commentsTextarea');
        	
        	commentsTextArea.val($(evt).val());
        	commentsTextArea.css({
        		position : "absolute",
        		top : "0",
        		right : "0"
        	}).show();
        	
        	commentsTextArea.focus();
        	
        },
        updateCommentsTextarea : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	
        	var comments = $(evt).siblings('.comments');
        	
        	comments.val($(evt).val());
        	$(evt).hide();
        },
        initLeft:function(){
        	this.showPage();
        	
        	var left=$(".yiwill_header_list").offset().left;
        	var centerLeft=parseInt($(".left_column").width())+left+15;  
        	$(".left_column").css("left",left);
        	$(".right_column").css("margin-left",centerLeft+4);
        },
        openRightContent:function(){
        	if($('.right_list').hasClass('right_hide')){//打开状态
        		$(".right_column").css("padding-right",15);
        	}else{//关闭状态
        		$(".right_column").css("padding-right",430);
        	}
        	
        },
        initEvent:function(){
        	var t=this;
        	$(window).resize(function(){
        		t.openRightContent();
        		t.initLeft();
        	}) 
        },
        initFacilityData:function(){
			var t=this;
			$.ajax({
				url:'/warehouse/control/findAllFacility',
				type: 'post',
	            dataType: 'json',
				success:function(results){					
					var content=template("facilityTypeColumn",results);
					$(".sent_warehouse").append(content);
					$(".receive_warehouse").append(content);
					$(".transferInventory_add_warehouse").html(content);
					$(".picking_warehouse").append(content);
					
					require(['select2'], function(select2){
						$(".sent_warehouse").select2();
						$(".receive_warehouse").select2();
						$(".transferInventory_add_warehouse").select2();
						$(".picking_warehouse").select2();
					});
				}
			});		
		},
		reductNumberList : function(List){
        	var t = this;
        	
        	if(List.length > 0){
        		for(var i = 0; i < List.length; i++){
        			for(var j = 0; j< t.currentData.length; j++){
        				if(List[i].productId == (t.currentData)[j].productId){
        					List[i].qty -= (t.currentData)[j].enterQty;
        				}
        			}
        		}
        	}
        	
        	return List;
		},
		getTableData : function(e, type, number){
			var t = this;
			var typeList = type || $('.tab-pane.active').attr('data-type');
			var numberList = number || "1";
			
			e && $.showLoading($(e.currentTarget));
			if(typeList == 'Sent'){
				var sentCommonParams = this.getParams(typeList, numberList);
					
	           	$.ajax({
	   				url:'/warehouse/control/queryTransferData',
	   				type: 'post',
	   	            dataType: 'json',
	   	         	data : JSON.stringify(sentCommonParams),
	   	           	success:function(results){
	   	           		e && $.hideLoading($(e.currentTarget));
	   	           	
	   					if(results.resultCode==1){
	   						
	   						$('.sent_table_box tbody').html(template("sentContent",{
	   		            		result : results.result
	   		            	}));
	   						$('.sent_page').html(template('sentPageContent',{
	   							totalNum : results.totalNum,
	   							pageNum : results.pageNum,
	   							totalPage : results.totalPage,
	   							nums : t.getPageNumberList(results)
	   		    			}));
	   					}
	   	           	},
        			error : function(option, status){
        				e && $.hideLoading($(e.currentTarget));
        			}
	           	});
				
			}else if(typeList == 'Receive'){
				var receiveCommonParams = this.getParams(typeList, numberList);
				
	           	$.ajax({
	   				url:'/warehouse/control/queryTransferData',
	   				type: 'post',
	   	            dataType: 'json',
	   	         	data : JSON.stringify(receiveCommonParams),
	   	           	success:function(results){
	   	           		e && $.hideLoading($(e.currentTarget));
	   	           	
	   					if(results.resultCode==1){
	   						
	   						$('.receive_table_box tbody').html(template("sentContent",{
	   		            		result : results.result
	   		            	}));
	   						$('.receive_page').html(template('sentPageContent',{
	   							totalNum : results.totalNum,
	   							pageNum : results.pageNum,
	   							totalPage : results.totalPage,
	   							nums : t.getPageNumberList(results)
	   		    			}));
	   					}
	   	           	},
        			error : function(option, status){
        				e && $.hideLoading($(e.currentTarget));
        			}
	           	});
				
			}else if(typeList == 'Picking'){
        		var pickingCommonParams = this.getParams(typeList, numberList);
        		
        		$.ajax({
        			url:'/warehouse/control/queryTranshipmentShippingBill',
        			type: 'post',
        			dataType: 'json',
        			data : JSON.stringify(pickingCommonParams),
        			success:function(results){
        				e && $.hideLoading($(e.currentTarget));
        				
        				if(results.resultCode==1){
        					
        					$('.picking_table_box tbody').html(template("pickingContent",{
        						result : results.result
        					}));
        					$('.picking_page').html(template('sentPageContent',{
        						totalNum : results.totalNum,
        						pageNum : results.pageNum,
        						totalPage : results.totalPage,
        						nums : t.getPageNumberList(results)
        					}));
        				}
        			},
        			error : function(option, status){
        				e && $.hideLoading($(e.currentTarget));
        			}
        		});
        	}else if(typeList == 'transferInventory'){
				var transferInventoryCommonParams = this.getParams(typeList, numberList);
				
	        	$.ajax({
	   				url:'/warehouse/control/findProductByFacility',
	   				type: 'post',
	   	            dataType: 'json',
	   	         	data : JSON.stringify(transferInventoryCommonParams),
	   	           	success:function(results){
	   	           		e && $.hideLoading($(e.currentTarget));
	   	           	
	   					if(results.resultCode==1){
	   						t.data = results.result;
	   						var dataList = [];
	   						
   							var currentListData = t.currentData;
 	   						if(currentListData.length > 0 && results.result.length > 0){
	   							$.each(currentListData, function(index, elem){
		   							$.each(results.result, function(indexSrc, elemSrc){
		   								if(elem.productId == elemSrc.productId){
		   									elemSrc.atp = parseInt(elemSrc.atp) - parseInt(elem.enterQty);
		   								}
		   							});
	   							});
	   						}
	   						
	   						if(results.result.length > 0){
		   						$.each(results.result, function(indexSrc, elemSrc){
		   							if(t.isOldTransfer == 'Y'){
		   								if(parseInt(elemSrc.atp) - parseInt(elemSrc.pickingNum) > 0){
		   									elemSrc.transferAtp = (parseInt(elemSrc.atp) - parseInt(elemSrc.pickingNum));
		   									
		   									dataList.push(elemSrc);
		   								}
		   							}else{
		   								elemSrc.transferAtp = parseInt(elemSrc.atp);
		   							}
	   							});
	   						}
	   						
							$('.transferInventory_table_box tbody').html(template("transferInventoryContent",{
			            		result : (t.isOldTransfer == 'Y' ? dataList : results.result)
			            	}));
							$('.transferInventory_page').html(template('sentPageContent',{
								totalNum : results.totalNum,
								pageNum : results.pageNum,
								totalPage : results.totalPage,
								nums : t.getPageNumberList(results)
	   		    			}));
	   					}
	   	           	},
        			error : function(option, status){
        				e && $.hideLoading($(e.currentTarget));
        			}
	        	});
			}
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
        formatDate: function(date) {  
        	if(!date){ return date; }
		    	
		    return date.split('/')[2] + '-' + date.split('/')[1] + '-' + date.split('/')[0];  
		},
        getParams : function(type, number){
        	var t = this;
        	var params = null;
        	switch(type){
        		case 'Sent':
        			params = {
        				type : "sent",
        				facilityId : $('.sent_search .facilityId').val(),
        				productId : $('.sent_search .productId').val().trim(),
        				model : $('.sent_search .model').val().trim(),
        				sendDate : t.formatDate($('.sent_search .sendDate').val()),
        				statusId : $('.sent_search .statusId').val(),
        				pageNum : number
        			}
        		  break;
        		case 'Receive':
        			params = {
        				type : "receive",
        				facilityId : $('.receive_search .facilityId').val(),
        				productId : $('.receive_search .productId').val().trim(),
        				model : $('.receive_search .model').val().trim(),
        				sendDate : t.formatDate($('.receive_search .sendDate').val()),
        				statusId : $('.receive_search .statusId').val(),
        				pageNum : number
    				}
        		  break;
        		case 'Picking':
            		params = {
            			trackingId : $('.picking_search .trackingId').val().trim(),
            			receivedBy : $('.picking_search .facilityId').val(),
            			pageNum : number
            		}
            		break;
        		case 'transferInventory':
        			params = {
        				productId  : $('.transferInventory_search .productId').val().trim(),
        				productIdNum : $('.transferInventory_search #productIdNum').val(),
        				productIdStatu : "1",
        				brandName : $('.transferInventory_search .brand').val().trim(),
        				brandNameNum : $('.transferInventory_search #brandNum').val(),
        				brandNameStatu : "1",
        				internalName : $('.transferInventory_search .model').val(),
        				internalNameNum : $('.transferInventory_search #modelNum').val(),
        				internalNameStatu : "1",
        				pageNum : number,
        				pageSize : "15"
        			}
        		  break;
        	}
        	
        	return params;
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
						$(".left_column_content").find('a[href="/warehouse/control/newTransferInventory"]').parent('li').addClass("menu_active"); 
						
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
						
						t.initLeft();
					}					
				}
			})
        },
        initPermissionData : function(){
        	var t = this;
        	
        	$.ajax({
				url:'/warehouse/control/newTransferOrOldTransfer',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){
	           		if(results.resultCode==1){
	           			if(results.isNew == 'Y'){
	           				$('.isOldTransfer').show();
	           				$('.isNewTransfer').hide();
	           			}else{
	           				$('.isOldTransfer').hide();
	           				$('.isNewTransfer').show();
	           			}
	           			$('#permission').val(results.isNew);
	           			t.isOldTransfer = results.isNew;
	           			t.initEvent();
	           		}
	           	}
        	});
        },
        initTypeEvent: function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var dataType = $(evt).attr('data-type');
        	var parentLi = $(evt).closest('li');
        	
        	$(evt).closest('li').siblings().removeClass('active');
        	$(evt).closest('li').addClass('active');
        	
        	$('.newTransferInventory_list .tab-pane').removeClass('active');
        	$('.newTransferInventory_list .tab-pane[data-type="'+ dataType +'"]').addClass('active');
        	
        	t.getTableData(null, $('.tab-pane.active').attr('data-type'));
        	
    		$('.right_list').addClass('right_hide');

        	$(".right_column").css("padding-right",15);
        },
        printInvocing : function(option){
        	var t = this;
         	var transferListTabContent = $('.right_list .transferListTabContent');
        	var printData = [];
        	$.each(transferListTabContent, function(index, elem){
        		var faclityid = $(elem).find('.warehouseName').attr('data-faclityid');
        		var faclityName = $(elem).find('.warehouseName').text();
        		var trList = $(elem).find('tbody tr');
        		var printArr = {};
        		var printList = [];
        		$.each(trList, function(trIndex, trElem){
        			printList.push({
            			productId : $(trElem).attr('data-id'),
            			transferNum : $(trElem).attr('data-qty'),
            			brandName : $(trElem).attr('data-brandName'),
            			model: $(trElem).attr('data-model'),
            			description: $(trElem).attr('data-description')
        			})
        		});
        		
        		printArr.list = printList;
        		printArr.faclityid = faclityid;
        		printArr.faclityName = faclityName;
        		
        		printData.push(printArr);
        	});
        	
        	$('.right_list_print').html(template("printTabColumn",{
        		result : printData
        	}));
       		
       		//new print
        	var openWindow = window.open("", "Print");
       		
			openWindow.document.title = "Print";
			
			openWindow.window.document.body.innerHTML = $('.right_list_print').html();
       		
       		openWindow.window.print();
       		
       		openWindow.window.close();
        }
	});

	new View();
})
</script>