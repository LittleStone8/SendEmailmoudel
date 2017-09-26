<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/inventory-item.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/warehouse/product-stock.css">
<style type="text/css"> body{height: auto !important;} #findContent{ width:auto !important;padding:0 !important; }</style>
<div id="batchAddMember" style="display:none">
	<div class="batchAddMember_box">
		<div class="item_warehouse">
    		<span class="warehouse_text">Warehouse:</span><span class="warehouse_name"></span><a href="/warehouse/control/selectFacilityForm">(Change)</a>
    	</div>
    	<div class="batchAddMember_search">
    		<div class="batchAddMember_search_tit">Batch Add Member</div>
  			<div class="item_row">
		        <div class="left_label">New Team Member</div>
		        <div class="item_row_input recevice_nohover">
			        <input type="text" class="newTeamMember"/>
			        <a class="item_add" href="javascript:void(0)"></a>
		        </div>
		    </div>
		    <div class="item_row">
		        <div class="left_label">Role</div>
		        <select class="role">
		        	<option value="WM">Warehouse Manager</option>
		        </select>
		    </div>
		    <div class="item_row">
		    	<a href="javascript:void(0)" class="batchAddMember_search_btn">Search</a>
		    </div>
		    <div style="clear:both;"></div>
    	</div>
    	<div class="table_box">
    		<div class="table_tit">Team Members</div>
    		<div class="table_content">
    			<table class="teamMembers" id="teamMembers">
    				<thead>
    					<tr>
    						<th>Team Member</th>
    						<th>Role</th>
    						<th>Remove</th>
    					</tr>
    				</thead>
    				<tbody>
    				</tbody>
    			</table>
    		</div>
    	</div>
    	<div class="batchAddMember_content">
    		<div class="batchAddMember_content_tit">Action scope</div>
    		<div class="batchAddMember_all_box">
				<input type="checkbox" class="selectAll" name="selectAll" id="selectAll" />
				<span class="selectSpan">Select All</span>
				<div style="clear:both;"></div>
    		</div>
    		<div class="select_all_box">
    			
    		</div>
    		<div class="operator_box">
    			<a href="javascript:void(0);" class="submit_btn">Submit</a>
    		</div>
    	</div>
	</div>
</div>
<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script type="text/html" id="logTbodyColumn">

	{{if result.length>0}}
		{{each result as value}}
			<tr>
			 <td>{{value.facilityName}}({{value.facilityId}})</td>
			 <td>Warehouse Manager</td>
			 <td><a class="facilityRemove" href="javascript:void(0);" data-facilityId="{{value.facilityId}}">Remove</a></td>
			</tr>
		{{/each}}
		{{else}}
			<tr>
			 <td colspan="3">No fund data</td>
			</tr>
	{{/if}}
			    
</script>
<script type="text/html" id="allWarehouseColumn">
{{if result}}
      {{each result as value i}}
         <div class="warehouseList_box">
				<input type="checkbox" class="singleBox" name="warehouseList" data-id="{{value.facilityId}}" {{if value.flag == true}}checked{{/if}} />
				<span class="selectSpan">{{value.facilityName}}</span>
		 </div>
      {{/each}}
   {{/if}}
	<div style="clear:both;"></div>
</script>
<script>
require.config({
	paths:{
		batchAddMemberDialogHtml:'/commondefine_js/tempate/batchAddMember/batchAddMemberDialog.html?v=1',
		batchAddMemberDialog:'/commondefine_js/tempate/batchAddMember/batchAddMemberDialog'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {
        	"click .item_add": "findMemberData",
        	"click .batchAddMember_search_btn": "searchEvent",
        	"click .submit_btn": "submitEvent",
        	"click .selectAll": "selectAllChange",
        	"click .singleBox": "selectSingleChange",
        	"click .teamMembers .facilityRemove": "facilityRemoveEvt"
        },
        __propertys__: function () {
        },
        initialize: function ($super) {
            $super(); 
            this.getAllWarehouse();
            this.initRoleData();
            this.allWarehouseArr = null;
            this.showPage();
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
					}					
				}
			})
        },
        facilityRemoveEvt : function(e){
        	var t = this;
        	
        	var evt = e.currentTarget;
        	var facilityId = $(evt).attr('data-facilityId');
        	var partyId = $('.newTeamMember').val();
        	
         	$.ajax({
				url:'/warehouse/control/removeWarehouseManageByPerson',
				type: 'post',
	            dataType: 'json',
	            data : JSON.stringify({
	            	partyId : partyId,
	            	facilityId : facilityId
	            }),
	           	success:function(results){
					if(results.resultCode==1){
						$(evt).closest('tr').remove();
					}					
				}
			}) 
        },
        showPage : function(){
        	if($('#warehouse_management').attr('data-admin') != ''){
        		$('#batchAddMember').show();
        	}else{
        		$('#batchAddMember').hide();
        	}
        },
        selectAllChange : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	if($(evt).is(':checked') == true){
        		$('.select_all_box').find('[name="warehouseList"]').prop('checked', true);
        	}else{
        		$('.select_all_box').find('[name="warehouseList"]').prop('checked', false);
        	}
        },
        selectSingleChange : function(e){
        	var t = this;
        	var evt = e.currentTarget;
        	var warehouseListContent = $('.select_all_box').find('[name="warehouseList"]').length;
        	var warehouseListContentChecked = $('.select_all_box').find('[name="warehouseList"]:checked').length;
        	
        	if(warehouseListContent == warehouseListContentChecked){
        		$('.selectAll').prop('checked', true);
        	}else{
        		$('.selectAll').prop('checked', false);
        	}
        },
        searchEvent : function(){
        	var t = this;
        	t.initWarehouseData(t.getParams());
        },
        submitEvent : function(){
        	var t = this;
        	var warehouseListContent = $('.select_all_box').find('[name="warehouseList"]:checked');
        	var idList = [];
        	var partyId = $('.newTeamMember').val();
        	if(warehouseListContent.length > 0){
        		$.each(warehouseListContent, function(index,elem){
        			idList.push($(elem).attr("data-id"));
        		})
        	}
        	if(partyId == ''){
        		t.showToast("New team member can't be empty");
        		
        		return false;
        	}
        	if(idList.length == 0){
        		t.showToast("Warehouse can't be empty");
        		
        		return false;
        	}
         	$.ajax({
				url:'/warehouse/control/addWarehouseManageByPerson',
				type: 'post',
	            dataType: 'json',
	            data : JSON.stringify({
	            	facilityId : idList.join(','),
	            	partyId : partyId
	            }),
	           	success:function(results){
					if(results.resultCode==1){
						location.reload();
					}					
				}
			}) 
        },
        findMemberData : function(e){
        	var t = this;
        	var target = e.currentTarget;
        	require(['batchAddMemberDialog'], function(batchAddMemberDialog){
            	new batchAddMemberDialog({            		            		
            		onSelected:function(data){
            			$(target).siblings(".newTeamMember").val(data);
            		}
            	});            	            	
            });
        },
        getParams : function(){
        	var params = {};
        	
        	params.partyId = $('.newTeamMember').val();
        	params.role = $('.role').val();
        	
        	return params;
        },
        initWarehouseData : function(options){
        	var t = this;
         	$.ajax({
				url:'/warehouse/control/getWarehouseManageByPerson',
				type: 'post',
	            dataType: 'json',
	            data : JSON.stringify(options),
	           	success:function(results){
					if(results.resultCode==1){
						
 						$('.teamMembers tbody').html(template('logTbodyColumn',{
							result : results.inventoryFacilitys
						}));
					}					
				}
			}) 
        },
        getAllWarehouse:function(options){
        	
        	var t = this;
         	$.ajax({
				url:'/warehouse/control/queryAllFacility',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){
					if(results.resultCode==1){
						
 						$('.select_all_box').html(template('allWarehouseColumn',{
							result : results.result
						}));
					}					
				}
			}) 
        }
	})

	new View();
})
 
</script>