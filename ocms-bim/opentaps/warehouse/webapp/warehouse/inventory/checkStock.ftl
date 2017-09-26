<link rel="stylesheet" type="text/css"
	href="/commondefine_css/warehouse/check-stock.css">
<div id="newCheckStock" style="display:none">
	<div class="newCheckStock_box">
		<div class="item_warehouse">
			<div>
				<span class="warehouse_text">Warehouse:</span><span
					class="warehouse_name"></span><a
					href="/warehouse/control/selectFacilityForm">(Change)</a>
			</div>
		</div>
		<div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
		<div class="right_column">
			<div class="newCheckStock_content">
				<div class="newCheckStock_tit">Check Stock</div>
				<div class="newCheckStock_tab">
					<ul id="newCheckStockTab" class="nav nav-tabs">
						<li class="active"><a href="javascript:void(0);"
							data-type="IMEI">Start IMEI</a></li>
						<li><a href="javascript:void(0);" data-type="EAN">Start
								Non-serialized</a></li>
						<li><a href="javascript:void(0);" data-type="Log">Check
								Log</a></li>		
						<li><a href="javascript:void(0);" data-type="Download">Download
								Non-serialized Sample</a></li>
						
					</ul>
				</div>
				<div class="tab-content newCheckStock_list">
					<div class="tab-pane active" id="IMEI" data-type="IMEI">
						<div class="newCheckStock_operate">
							<div class="newCheckStock_operate_table">
								<table id="imeiTable">
									<thead>
									<tr>
										<th class="operate_no">NO.</th>
										<th class="operate_val">IMEI</th>
										<th class="operate_no">NO.</th>
										<th class="operate_val">IMEI</th>
										<th class="operate_no">NO.</th>
										<th class="operate_val">IMEI</th>
									</tr>
									</thead>
									<tbody id="imeiTableTbody">
									<tr>
										<td class="operate_no">1</td>
										<td class="operate_val"><input type="text" class="imei" /></td>
										<td class="operate_no">2</td>
										<td class="operate_val"><input type="text" class="imei" /></td>
										<td class="operate_no">3</td>
										<td class="operate_val"><input type="text" class="imei"
											data-count="yes" /></td>
									</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="newCheckStock_btn clearfix">
							<div class="btn_div">
								<a href="javascript:void(0)" class="btn_check disabled" data-type="IMEI">Check</a>
							</div>
							<div class="btn_div">
								<a href="javascript:void(0)" class="btn_continue disabled"
									data-type="IMEI">Continue</a>
							</div>
							<div class="btn_bar">
								<div class="bar">
									<span class="track-wrap"> <span class="track track1"></span>
									</span> <span class="track-wrap"> <span class="track track2"></span>
									</span>
								</div>
								<div class="newCheckStock_bar">
									<div class="newCheckStock_bar_tips">checking,please do not
										close the page</div>
								</div>
							</div>
							
						</div>
						<div class="newCheckStock_result_table" style="display: none">
							<div class="newCheckStock_result_title clearfix">
								<span>Result</span><a href="javascript:void(0)" class="export" data-stockId="" data-isImei="">export</a>
							</div>
							<table class="newCheckStock_result_table_box" data-type="IMEI">
								<thead>
									<tr>
										<th style="width: 80px;">NO.</th>
										<th style="width: 150px;">IMEI</th>
										<th style="width: 250px;">Description</th>
										<th style="width: 130px;">
											  <span class="thead_text">Reason</span>
											  <div class="thead_reason">
											  	  <span class="thead_tips"></span>
												  <div class="tips_content">
												  1.Inventory Loss: This IMEI is on the list of BIM system, but has not been scanned.<br>2.Inventory Profit: This IMEI is not on the list of BIM system, but was scanned.
												  <div class="tips_arrow"></div>					    	 
												  </div>
											 </div>
										</th>
										<th style="width: 130px;">Status</th>
									</tr>
								</thead>
								<tbody id="checkImeiDataTable">
								</tbody>
							</table>
						</div>
						<div class="footer_page">
							<div class="imei_result_page"></div>
						</div>
					</div>
					<div class="tab-pane" id="EAN" data-type="EAN">
						<div class="newCheckStock_operate">
						    <div class="newCheckStock_operate_search clearfix">
						       <input type="text" class="operate_search_ean" placeholder="Model / product id / EAN "/>
						       <div class="operate_search_add"><a href="javascript:void(0)" class="search_add">ADD</a></div>
						       <ul class="product_ul" style="display:none;" tabindex="22"></ul>
						    </div>
							<div class="newCheckStock_operate_table">
							    <div class="operate_ean_title">
							      <div class="operate_ean_list clearfix">
								       <div class="operate_ean_group">
								          <div class="ean_group_num"><span>NO.</span></div>
								          <div class="ean_group_product"><span>Product</span></div>
								          <div class="ean_group_quantity"><span>Quantity</span></div>
								       </div>
								       <div class="operate_ean_group">
								          <div class="ean_group_num"><span>NO.</span></div>
								          <div class="ean_group_product"><span>Product</span></div>
								          <div class="ean_group_quantity"><span>Quantity</span></div>
								       </div>
								       <div class="operate_ean_group">
								          <div class="ean_group_num"><span>NO.</span></div>
								          <div class="ean_group_product"><span>Product</span></div>
								          <div class="ean_group_quantity"><span>Quantity</span></div>
								       </div>
							       </div>
							    </div>
							    <div id="eanList" class="clearfix">
							       	   <div class="operate_ean_group">
								          <div class="ean_group_num" data-num="1"><span>1</span><input type="hidden" class="productnum" value="1"/></div>
								          <div class="ean_group_product"><span></span><input type="hidden" class="productid"/></div>
								          <div class="ean_group_quantity"><input type="text" class="quantity"/></div>
								          <div class="ean_mask"><a class="ean_mask_delete" href="javascript:void(0);"></a></div>
								       </div>
								       <div class="operate_ean_group">
								          <div class="ean_group_num" data-num="2"><span>2</span><input type="hidden" class="productnum" value="2"/></div>
								          <div class="ean_group_product"><span></span><input type="hidden" class="productid"/></div>
								          <div class="ean_group_quantity"><input type="text" class="quantity"/></div>
								          <div class="ean_mask"><a class="ean_mask_delete" href="javascript:void(0);"></a></div>
								       </div>
								       <div class="operate_ean_group">
								          <div class="ean_group_num" data-num="3"><span>3</span><input type="hidden" class="productnum" value="3"/></div>
								          <div class="ean_group_product"><span></span><input type="hidden" class="productid"/></div>
								          <div class="ean_group_quantity"><input type="text" class="quantity"/></div>
								          <div class="ean_mask"><a class="ean_mask_delete" href="javascript:void(0);"></a></div>
								       </div>								       
							    </div>
								
							</div>
						</div>
						<div class="newCheckStock_btn">
							<div class="btn_div">
								<a href="javascript:void(0)" class="btn_check disabled" data-type="EAN">Check</a>
							</div>
							<div class="btn_div">
								<a href="javascript:void(0)" class="btn_continue disabled"
									data-type="EAN">Continue</a>
							</div>
							<div class="btn_bar">
								<div class="bar">
									<span class="track-wrap"> <span class="track track1"></span>
									</span> <span class="track-wrap"> <span class="track track2"></span>
									</span>
								</div>
								<div class="newCheckStock_bar">
									<div class="newCheckStock_bar_tips">checking,please do not
										close the page</div>
								</div>
							</div>
							
						</div>
						<div class="newCheckStock_result_table"  style="display: none"> 
							<div class="newCheckStock_result_title clearfix">
								<span>Result</span><a href="javascript:void(0)" class="export" data-stockId="" data-isImei="">export</a>
							</div>
							<table class="newCheckStock_result_table_box" data-type="EAN">
								<thead>
									<tr>
										<th style="width: 80px;">NO.</th>
										<th style="width: 350px;">Product ID</th>
										<th style="width: 130px;">Description</th>
										<th style="width: 150px;">Inventory quantity</th>
										<th style="width: 100px;">Dif quantity</th>
										<th style="width: 130px;">
											<span class="thead_text">Reason</span>
											  <div class="thead_reason">
											  	  <span class="thead_tips"></span>
												  <div class="tips_content">
												  1.Inventory Loss: This IMEI is on the list of BIM system, but has not been scanned.<br>2.Inventory Profit: This IMEI is not on the list of BIM system, but was scanned.
												  <div class="tips_arrow"></div>					    	 
												  </div>
											 </div>
										</th>
									</tr>
								</thead>
								<tbody id="checkEanDataTable">
								</tbody>
							</table>
						</div>
						<div class="footer_page">
							<div class="ean_result_page"></div>
						</div>
					</div>
					<div class="tab-pane" id="Log" data-type="Log">
						<div class="log_content">
							<div class="log_search">	
					    		<div class="log_search_item clearfix">
						    		  	<div class="search_item_div">
						    				<div class="log_search_span">Check Time From</div>					    				
						    				<div class="log_row_input clearfix">
										        <input class="checkTimeFrom log_date laydate-icon" type="text"  />									        
									        </div>
							    		</div>				    		
							    		<div class="search_item_div">
							    			<span class="log_search_span">Check Time To</span>
							    			<div class="log_row_input clearfix">
										        <input class="checkTimeTo log_date laydate-icon" type="text"  />									        
									        </div>
							    		</div>
							    		<div class="search_item_div">
							    			<span class="log_search_span">Warehouse</span>
							    			<div class="log_row_select clearfix">
										        <select class="facilityId">										           
										        </select>									        
									        </div>
							    		</div>
							    		<div class="search_item_div">
							    			<span class="log_search_span">Result</span>
							    			<div class="log_row_select clearfix">
										        <select class="resultType ">
										           <option value="">All</option>
										           <option value="Perfectly Match">Perfectly Match</option>
										           <option value="Abnormal">Abnormal</option>
										        </select>								        
									        </div>
							    		</div>
					    		</div>			    						    						    						    					    		
					    		<div class="log_search_item ">
					    			<a href="javascript:void(0);" class="search_confirm">Search</a>
					    		</div>
					    		<div style="clear:both;"></div>
					    	</div>
							<div class="log_table">
					    	    <table class="log_table_box" data-type="AdjustImei">
					    			<thead>
					    				<tr>
					    					<th>Check Time</th>
					    					<th>Warehouse</th>
					    					<th>Type</th>
					    					<th>Operator</th>
					    					<th>Result</th>
					    					<th>More Information</th>					    							    									    					
					    				</tr>
					    			</thead>
					    			<tbody id="logTableTbody"></tbody>
					    		</table>
					    	</div>
					    	<div class="footer_page">
					    		<div class="log_imei_page"></div>
					    	</div>
						</div>
					</div>
					<div class="tab-pane" id="Download" data-type="Download">
					   <div class="download_ean">
					      <div class="download_ean_tips">
					      1.This checklist is for non-serialized product checking.<br>
						  2.If EAN code cannot be scanned, please input the EAN code manually.
					      </div>
					      <div class="download_btn_div">
								<a href="javascript:void(0)" class="btn_download" >Download</a>
							</div>
					   </div>
					</div>
					
				</div>
			</div>
			<div class="newCheckStock_log_record" style="display: none">
				<div class="log_record_return">
					<span class="return_arrow"></span><span>return</span>
				</div>
				<div class="log_record_content">
					<div class="record_content_count">
					    <div class="record_content_count_desc clearfix">
					        <div class=""><span class="desc_bold">Time&nbsp;&nbsp;:&nbsp;&nbsp;</span><span class="desc_time"></span></div>
					        <div class=""><span class="desc_bold">Warehouse&nbsp;&nbsp;:&nbsp;&nbsp;</span><span class="desc_warehouse"></span></div>
					        <div class=""><span class="desc_bold">Product Type&nbsp;&nbsp;:&nbsp;&nbsp;</span><span class="desc_type"></span></div>
					        <div class=""><span class="desc_bold">Operator&nbsp;&nbsp;:&nbsp;&nbsp;</span><span class="desc_operator"></span></div>
					    </div>
					    <div class="record_content_count_title">
					       <span>Input of Checking</span>
					    </div>
						<table id="scodeLogTable">

						</table>
					</div>
					<div class="record_content_table">
						<div class="newCheckStock_result_title clearfix">
							<span>Result of Checking</span><a href="javascript:void(0)" data-stockId="" data-isImei="" class="export">export</a>
						</div>
						<table class="newCheckStock_result_table_box" id="dateLogTable">

						</table>
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
<script type="text/html" id="imeiScanTemplate">
{{if type=="scan"}}
<tr data-num="{{imeiScanNum+3}}">
									  
									 <td class="operate_no">{{imeiScanNum+1}}</td>
				    		          <td class="operate_val"><input type="text" class="imei" value="{{if imeiScan}}{{imeiScan[0]}}{{/if}}"/></td>
				    		          <td class="operate_no">{{imeiScanNum+2}}</td>
				    		          <td class="operate_val"><input type="text" class="imei" value="{{if imeiScan}}{{imeiScan[1]}}{{/if}}"/></td>
				    		          <td class="operate_no">{{imeiScanNum+3}}</td>
				    		          <td class="operate_val"><input type="text" class="imei" data-count="yes" value="{{if imeiScan}}{{imeiScan[2]}}{{/if}}"/></td>
				    		       </tr>
{{/if}}
{{if type == "show"}}
<tr>
				    		          <td class="operate_no">NO.</td>
				    		          <td class="operate_val">IMEI</td>
				    		          <td class="operate_no">NO.</td>
				    		          <td class="operate_val">IMEI</td>
				    		          <td class="operate_no">NO.</td>
				    		          <td class="operate_val">IMEI</td>
				    		       </tr>
                                   {{each date as value index}}
								   <tr>
									 {{each value.imei as val i}}
									  <td class="operate_no">{{val.no}}</td>
				    		          <td class="operate_val">{{val.val}}</td>
				    		          {{/each}}
                                      </tr>
                                   {{/each}}
{{/if}}
{{if type == "scanShow"}}
{{if imeis.length>0}}
{{each imeis as value index}}
								   <tr>
									 {{each value.dates as val i}}
									  <td class="operate_no">{{val.no}}</td>
				    		          <td class="operate_val">{{val.val}}</td>
				    		          {{/each}}
                                      </tr>
                                   {{/each}}
{{/if}}
{{/if}}
</script>
<script type="text/html" id="eanProductTemplate">
{{each items as value i}}
   <li data-id="{{value.id}}" {{if i == 0 }}class="cur"{{/if}} data-value="{{value.text}}">{{value.id}}:{{value.text}}</li>
{{/each}}
</script>
<script type="text/html" id="eanProductDivTemplate">
<div class="operate_ean_group">
								          <div class="ean_group_num" data-num="{{eanScanNum+1}}"><span>{{eanScanNum+1}}</span><input type="hidden" value="{{eanScanNum+1}}" class="productnum"/></div>
								          <div class="ean_group_product"><span>{{model}}</span><input type="hidden" class="productid" value="{{productId}}"/></div>
								          <div class="ean_group_quantity"><input type="text" class="quantity"/></div>
								          <div class="ean_mask"><a class="ean_mask_delete" href="javascript:void(0);"></a></div>
								       </div>
</script>
<script type="text/html" id="eanScanTemplate">
{{if type=="scan"}}
<tr data-num="{{eanScanNum+3}}">
				    		       	 <td class="operate_no">{{eanScanNum+1}}<input type="hidden" value="{{eanScanNum+1}}" class="eanNo"/></td>
				    		          <td class="operate_val"><span></span><input class="productid" type="hidden"/></td>
				    		          <td class="operate_num"><input type="text" class="quantity"/></td>
				    		          <td class="operate_no">{{eanScanNum+2}}<input type="hidden" value="{{eanScanNum+2}}" class="eanNo"/></td>
				    		          <td class="operate_val"><span></span><input class="productid" type="hidden"/></td>
				    		          <td class="operate_num"><input type="text" class="quantity"/></td>
				    		          <td class="operate_no">{{eanScanNum+3}}<input type="hidden" value="{{eanScanNum+3}}" class="eanNo"/></td>
				    		          <td class="operate_val"><span></span><input class="productid" type="hidden"/></td>
				    		          <td class="operate_num"><input type="text" class="quantity" data-count="yes"/></td>
				    		       </tr>
{{/if}}
{{if type == "show"}}
<tr>
				    		          <td class="operate_no">NO.</td>
										<td class="operate_val">Product</td>
										<td class="operate_num">Quantity</td>
										<td class="operate_no">NO.</td>
										<td class="operate_val">Product</td>
										<td class="operate_num">Quantity</td>
										<td class="operate_no">NO.</td>
										<td class="operate_val">Product</td>
										<td class="operate_num">Quantity</td>
                                   {{each date as value index}}
								   <tr>
									 {{each value.dates as val i}}
									   <td class="operate_no">{{val.no}}</td>
				    		          <td class="operate_val">{{val.ean}}</td>
				    		          <td class="operate_num">{{val.quantity | formatDiff}}</td>
				    		          {{/each}}
                                      </tr>
                                   {{/each}}
{{/if}}
</script>
<script type="text/html" id="imeiTableColumn">
{{if type == "scan"}}
{{if result.result.length>0}}
		{{each result.result as value i}}
			<tr>
             <td>{{i+1}}</td>
			 <td>{{value.imei}}</td>
			 <td>{{value.model}} {{value.description}}</td>
			 <td>{{value.reasonType}}</td>
             <td>{{value.reason}}</td>			 		 
			</tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="5">no data</td>
        </tr>
    {{/if}}
{{/if}}
{{if type == "show"}}
<thead>
				    				<tr>
				    					<th style="width:80px;">NO.</th>
				    					<th style="width:350px;">IMEI</th>
				    					<th style="width:130px;">Description</th>
				    					<th style="width:130px;">Reason</th>
				    					<th style="width:130px;">Status</th>			
				    				</tr>
				    			</thead>
				    			<tbody id="dateLogTable">
                                 {{if date.takeStockDetail}}
                                 {{each date.takeStockDetail as value i}}
				    			   <tr>
				    			      <td>{{i+1}}</td>
				    			      <td>{{value.imeiOrEan}}</td>
				    			      <td>{{value.description}}</td>
				    			      <td>{{value.reason}}</td>
				    			      <td>{{value.status}}</td>
				    			   </tr>	
                                  {{/each}}
                                  {{/if}}
                                  {{if date.takeStockDetail.length==0}}
									<tr>
				    			      <td colspan="5">Well done, the checked product perfectly match with those in the system.</td> 
				    			   </tr>
                                  {{/if}}			    			   
				    			</tbody>
{{/if}}
</script>
<script type="text/html" id="eanTableColumn">
{{if type == "scan"}}
{{if result.resultlist.length>0}}
		{{each result.resultlist as value i}}
			<tr>
             <td>{{i+1}}</td>
			 <td>{{value.ean}}</td>
			 <td>{{value.model}} {{value.description}}</td>
			 <td>{{value.inventoryquantity | formatDiff}}</td>
             <td>{{value.dIFquantity | formatDiff}}</td>	
			 <td>{{value.reason}}</td>
			</tr>
		{{/each}}
		
	{{/if}}
	{{if result.length==0}}
        <tr>
            <td colspan="7">no data</td>
        </tr>
    {{/if}}
{{/if}}
{{if type == "show"}}
								  <thead>
				    				<tr>
				    					<th style="width:80px;">NO.</th>
				    					<th style="width:100px;">Product ID</th>
				    					<th style="width:250px;">Description</th>
				    					<th style="width:100px;">Inventory quantity</th>
				    					<th style="width:100px;">Dif quantity</th>	
				    					<th style="width:130px;">Reason</th>			
				    				</tr>
				    			</thead>
								{{each date.takeStockDetail as value index}}
								   <tr>
				    			      <td>{{index+1}}</td>
				    			      <td>{{value.imeiOrEan}}</td>
				    			      <td>{{value.description}}</td>
				    			      <td>{{value.inventoryQuantity | formatDiff}}</td>
				    			      <td>{{value.difQuantity | formatDiff}}</td>
				    			      <td>{{value.reason}}</td>
				    			   </tr>
                                   {{/each}} 
{{/if}}
</script>
<script type="text/html" id="imeiListContent">
	{{each result as value index}}
			<tr>
             {{each value as val i}}
             	<td>{{val.no}}</td>
			 	<td>{{val.imei}}</td>	
             {{/each}}		 
			</tr>
		{{/each}}
</script>
<script type="text/html" id="eanListContent">
	{{each result as value index}}
			<tr>
             {{each value as val i}}
             	<td>{{val.no}}</td>
			 	<td>{{val.ean}}</td>
				<td>{{val.quantity}}</td>	
             {{/each}}		 
			</tr>
		{{/each}}
</script>
<script type="text/html" id="logWarehouseTemplate">
     {{each inventoryFacilitys as value i}}
        <option value="{{value.facilityId}}" {{if defaultFacilityId == value.facilityId }}selected{{/if}} >{{value.facilityName}}</option>
     {{/each}}
</script>
<script type="text/html" id="logTableColumn">
{{if result.length>0}}
		{{each result as val index}}
			<tr>             
             	<td class="detail_time">{{val.createTime | formatDate}}</td>
				<td class="detail_facilityName">{{val.facilityName}}</td>
				<td class="detail_type">{{if val.isIMEI == 'Y'}}IMEI{{else}}EAN{{/if}}</td>
			 	<td class="detail_operator">{{val.operator}}</td>
				<td>{{val.resultType}}</td>	
				<td><a href="javascript:void(0);" class="log_table_detail" data-stockId="{{val.takeStockId}}">Details</a></td>	
			</tr>
		{{/each}}
        {{else}}
			<tr>
				<td colspan="6">no data</td> 
			</tr>
{{/if}}
</script>
<script type="text/html" id="logListContent">

	<div class="log_content_list">
					    	     <div class="list_title"><span class="list_title_date">{{nowMonth}}</span><span class="list_title_arrow click" data-now="{{nowMonth}}"></span></div>
					    	     
								<div class="list_content clearfix" {{if currentDate == nowMonth}}style="display:block;"{{/if}}>
								{{if currentDate == nowMonth}}
							    {{each results as val index}}
					    	        <div class="list_content_short" data-stockId="{{val.takeStockId}}" data-category="{{if val.isIMEI == 'Y'}}IMEI{{else}}EAN{{/if}}">
					    	            {{val.createTime}}
					    	               {{if val.isIMEI == 'Y'}}IMEI{{else}}EAN{{/if}} {{val.operator}}
                                        <input type="hidden" class="list_time" value="{{val.createTime}}"  />
					    	            <input type="hidden" class="list_type" {{if val.isIMEI == 'Y'}}value="IMEI"{{else}} value="EAN"{{/if}} />
										<input type="hidden" class="list_operator" value="{{val.operator}}" />
								  </div>
					    	    {{/each}}
								{{/if}}
					    	     </div>
				    	     </div>
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
<script type="text/javascript" src="/commondefine_js/timezoneTime.js"></script>
<script>
require.config({
	paths:{
		stockProductDialogHtml:'/commondefine_js/tempate/productStock/stockProductDialog.html?v=1',
		stockProductDialog:'/commondefine_js/tempate/productStock/stockProductDialog',
		layDate:'/commondefine_js/layDate/laydate'
	}
})
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {	
	template.helper('formatDiff', function(price) {
		if(price){							
			return parseInt(price);			
		}
	    return price;
	});
	template.helper('formatDate', function(date) {
	    if(date){
	        var nowDate=getFixNewDate(parseInt(date),"dd/MM/YYYY hh:mm:ss");	        
	        return nowDate;
	    }else{
	    	return date;
	    }
	});
	var View = Inherit.Class(AbstractView, {
		el: 'document',
        events: {      	
        	'click #left-content-column li':'selectMenu',
        	"click #newCheckStockTab a":"changeType",
        	"mouseover #newCheckStock .thead_reason":"showTips",
        	"mouseout #newCheckStock .thead_reason":"hideTips",
        	"keypress .operate_val input,.operate_num input":"scanCode",
        	"keyup #IMEI .imei":"changeImei",
        	"mouseenter #eanList .ean_group_product":"showEanDelete",
        	"mouseleave #eanList .ean_group_product":"hideEanDelete",
        	"mouseenter #eanList .ean_mask":"showEanIcon",
        	"mouseleave #eanList .ean_mask":"hideEanIcon",
        	"click #eanList .ean_mask_delete":"deleteEanProduct",
        	"keyup #EAN .ean":"changeEan",
        	"keyup #EAN .operate_search_ean":"searchProductByEan",
        	"click #EAN .newCheckStock_operate_search li":"clickProductTo",
        	"keyup #EAN .newCheckStock_operate_search .product_ul":"changeProductTo",
        	"click #EAN .search_add":"searchProductToAdd",
        	"click .newCheckStock_result_table .export,.record_content_table .export":"exportData",
        	"click .btn_check":"checkData",
        	"click .btn_continue":"continueStartCheck",
        	"click #Log .list_title_arrow":"showShortLog",
        	"click #Log .log_table_detail":"showLogDetail",
        	'click #Log .log_date':'showDate',
        	"click #Log .search_confirm":"searchLog",
        	"click #Download .download_btn_div":"downloadTemplate",
        	"click .newCheckStock_log_record .log_record_return":"returnLog",
        },
        __propertys__: function () {
        	this.formData={}; 
        	this.imeiData=[];
        	this.eanData=[];
        	this.imeiScanNum=3;
        	this.eanScanNum=3;
        	this.type="IMEI";
        	this.imeiCheckServices=null;
        	this.eanCheckServices=null;
        	this.permission="/warehouse/control/checkStock";
        	this.timer = 1000;
        	this.timeOut=null;
        },        
        initialize: function ($super) {
        	$super();
        	this.$content=$("#newCheckStock");
        	this.currentDate=this.getNowMonth();     
        	this.initRoleData();
        	this.initEvent();
        },
        initEvent:function(){
        	$(document).on('click', function(){
        		var product_ul = $('.product_ul');            	
            	product_ul.hide();
            });
        },
        hideProductUl:function(e){        	
        	var product_ul = $('#EAN .product_ul');
        	product_ul.hide();
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
						$("#newCheckStock").show();
						$('.left_column_content').html(template("leftColumnContent",results));
						$(".left_column_content").find("a[href='"+t.permission+"']").parent('li').addClass("menu_active");												
					}					
				}
			})
        },
        initWarehouseData:function(){
        	var t=this;
        	$.ajax({
				url:'/warehouse/control/queryFacility',
				type: 'post',
	            dataType: 'json',
	           	success:function(results){			           		
					if(results.resultCode==1){
						t.facility=results.defaultFacilityId;
						$('#Log .facilityId').html(template("logWarehouseTemplate",results));
						t.initLogData(1);
					}					
				}
			})
        },
        selectMenu:function(e){        	
        	$(e.currentTarget).addClass("menu_active").siblings("li").removeClass("menu_active");
        },
        changeType:function(e,type){
        	var t=this;        	
        	var t=this;  
        	var $that;
        	var dataType;
        	if(type){
        		$("#newCheckStockTab a").each(function(index,elem){
        			if($(elem).attr("data-type")==type){
        				$that=$(elem);
        				return;
        			}
        		})
        		dataType=type;
        		t.$content.find(".newCheckStock_content").show();
            	t.$content.find(".newCheckStock_log_record").hide();
        	}else{
        		$that=$(e.currentTarget);
        		dataType=$that.attr('data-type');
        	}
        	$that.closest('li').addClass('active').siblings().removeClass('active');
        	t.$content.find('.tab-pane').removeClass('active');
        	t.$content.find('.tab-pane[data-type="'+ dataType +'"]').addClass('active');
        	
        	t.type=dataType;
        	switch(dataType){
	        	case"IMEI":
	        		$("#imeiTableTbody").html(template("imeiScanTemplate",{imeiScanNum:0,type:"scan"}));
	        		$(".newCheckStock_result_table").hide();
	        		$("#IMEI").find(".btn_check").addClass("disabled");
	        		t.imeiData=[];
	        		break;	        	  
	        	case"EAN":
	        		$("#eanList").find(".operate_ean_group").each(function(index,elem){
	        			if(index>2){
	        				$(elem).remove();
	        				return;
	        			}
	        			var product=$(elem).find(".ean_group_product");
	        			product.find("span").text("");
	        			product.find(".productid").val("");
	        			$(elem).find(".quantity").val("");
	        		})
	        		$(".newCheckStock_result_table").hide();
	        		$("#EAN").find(".btn_check").addClass("disabled");
	        		t.eanData=[];
	        		break;
	        	case"Log":	        			        		
	        		t.initWarehouseData();
	        		break;
        	}        	        	
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
        showTips:function(e){
        	$(e.currentTarget).find(".tips_content").fadeIn("fast");;
        },
        hideTips:function(e){
        	$(e.currentTarget).find(".tips_content").hide();
        },
        initLog:function(){
        	var t=this;        
        	jQuery.ajax({
				url:'/warehouse/control/findTakeStockLogByYears',
				type: 'post',				
	            dataType: 'json',
	            data:JSON.stringify({"years":t.currentDate}),
				success:function(results){	
					if(results.resultCode==1){    
						results=results.result;
						var startMonth=8;
						for(var i=0;i<results.length;i++){
							results[i].createTime=getFixNewDate(parseInt(results[i].createTime),"dd/MM/YYYY hh:mm:ss");
						}						
			        	var nowMonth=t.currentDate.split("-")[1];			        	
			        	for(var i=nowMonth;i>=8;i--){
			        		var month;			        		
			        		if(parseInt(i)<10){
			        			month="0"+parseInt(i);
			        		}
			        		var nowVal=t.currentDate.split("-")[0]+"-"+month;
			        		t.$content.find(".log_content").append(template("logListContent",{results:results,currentDate:t.currentDate,nowMonth:nowVal}));
			        	}
					}
				}
			})
        			
        	
        },
        initLogData:function(pageNum,target){
        	var t=this;
        	t. getParametersLogData();
        	t.logData.pageNum=pageNum;
        	target&&target.currentTarget&&jQuery.showLoading(target.currentTarget);
        	jQuery.ajax({
				url:'/warehouse/control/findTakeStockLogByCondition',
				type: 'post',				
	            dataType: 'json',
	            data:JSON.stringify(t.logData),
				success:function(results){
					target&&target.currentTarget&&jQuery.hideLoading(target.currentTarget);
					console.dir(results);
					if(results.resultCode==1){    
						$("#Log").find("#logTableTbody").html(template("logTableColumn",results));
					}
				}
			})
        },
        getParametersLogData:function(){
        	var t=this;
        	var checkTimeFrom=$("#Log").find(".checkTimeFrom").val()?(getTimezoneTime($("#Log").find(".checkTimeFrom").val(),"dd/MM/yyyy HH:mm:ss")).toString():"";
        	var checkTimeTo=$("#Log").find(".checkTimeTo").val()?(getTimezoneTime($("#Log").find(".checkTimeTo").val(),"dd/MM/yyyy HH:mm:ss")).toString():"";
        	var facilityId=$("#Log").find(".facilityId").val()?$("#Log").find(".facilityId").val():t.facility;
        	var resultType=$("#Log").find(".resultType").val();
        	t.logData={
        			checkTimeFrom:checkTimeFrom,
                	checkTimeTo:checkTimeTo,
                	facilityId:facilityId,
                	resultType:resultType
        		};
        },
        searchLog:function(e){
        	var t=this;
        	var form=$("#Log").find(".checkTimeFrom").val();
        	var to=$("#Log").find(".checkTimeTo").val();
        	if(form&&to){
        		form=(getTimezoneTime($("#Log").find(".checkTimeFrom").val(),"dd/MM/yyyy HH:mm:ss")).toString();
        		to=(getTimezoneTime($("#Log").find(".checkTimeTo").val(),"dd/MM/yyyy HH:mm:ss")).toString();
        		if(form-to>0){
        			t.showToast("Check Time From date can't be greater than Check Time To date");
        			return;
        		}
        	}        	
        	t.initLogData(1,e);
        }, 
        searchProductByEan:function(e){
        	var t=this;        	
        	
        	var $that=$(e.currentTarget);
        	var searchProductLen=$that.val().length;
        	var keyCode=e.which;
        	var newExp = /^[0-9]*$/;
        	var product_ul = $('#EAN .product_ul');
        	
        	if((searchProductLen == 8 || searchProductLen == 13) && newExp.test(searchProductLen) && keyCode == 13){
        		clearTimeout(t.timeOut);
        		ajaxSearchProduct({productInput : $that.val()});
	    	}else{
	    		searchProduct({    				
    				productInput : $that.val()
    			});
	    	}
        	function searchProduct(option){        		
        		clearTimeout(t.timeOut);
    	       	t.timeOut = setTimeout(function(){
    	       		ajaxSearchProduct(option);
    	       	} ,t.timer);
        	}  	
        	function ajaxSearchProduct(option){
        		$.ajax({
		             type: 'post',
		             dataType: 'json',
		             url: "/ordermgr/control/new_gwtSuggestProductForCart",
		             data: {
		                 start: '0',
		                 limit: '16',
		                 filterCol: 'text',
		                 sort: 'productId',
		                 dir: 'ASC',
		                 query: option.productInput
		             },
		             success: function(data) {
		                 if(data.responseMessage=="success"){
		                 		if(data.items.length == 0){
		                 			product_ul.hide();
		                 			return false;
		                 		}else{
		                 			product_ul.html(template("eanProductTemplate",data)).show().focus();
		                 		}
		                 }else if(data.responseMessage=="error"){	                 		
	                 			t.showToast(data.errorMessage,function(){
	                 				$that.val("");
	                 			});	                 		
	                 		
		              	}
		             }
		         }) 
        	}
        	
        },
        showEanDelete:function(e){
        	e.stopPropagation();
        	var group=$(e.currentTarget).closest(".operate_ean_group").find(".ean_mask");
        	if(group.is(":hidden")){
        		$(e.currentTarget).closest(".operate_ean_group").find(".ean_mask").show();
        	}
        },
        hideEanDelete:function(e){
        	e.stopPropagation();
        	$(e.currentTarget).closest(".operate_ean_group").find(".ean_mask").hide();
        	
        },
        showEanIcon:function(e){
        	$(e.currentTarget).show();        	
        },
        hideEanIcon:function(e){
           $(e.currentTarget).hide();
        },
        deleteEanProduct:function(e){
        	var t=this;
        	$(e.currentTarget).closest(".operate_ean_group").fadeOut("fast",function(){
        		var val=$(this).find(".productid").val();
        		var index=$.inArray(val,t.eanData);
        		t.eanData.splice(index,1);
        		$(this).remove();
        		var no=0;
        		$("#eanList").find(".ean_group_num").each(function(index,elem){
        			$(elem).find("span").text(index+1);
        			$(elem).find(".productnum").val(index+1);
        			no++;
        		})
        		if(no%3!=0){
        			t.eanScanNum=no;
        			$('#eanList').append(template("eanProductDivTemplate",{eanScanNum:t.eanScanNum}));
        		}
        	})        	
        },
        clickProductTo:function(e){
        	var _this = $(e.currentTarget);
        	var productInput=$("#EAN").find(".operate_search_ean");
        	var product_ul=$("#EAN").find(".product_ul");
    		var dataId = _this.attr('data-id');
    		var dataValue = _this.attr('data-value');
    		var dataImei = _this.attr('data-imei');
    		var dataNum = _this.attr('data-num');
    		
    		productInput.val(dataValue);
    		productInput.attr("data-id",dataId);
    		productInput.attr("data-imei",dataImei);
    		productInput.attr("data-num",dataNum);
    		product_ul.hide();
        },
        changeProductTo:function(e){
        	var keyCode = e.witch;
        	var productInput=$("#EAN").find(".operate_search_ean");
        	var product_ul=$("#EAN").find(".product_ul");
    		var length = product_ul.find('li').length - 1;
    		if(keyCode == 40){
    			if(currentIndex != length){
    				currentIndex++;
    				product_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				product_ul.find('li').siblings().removeClass('cur').eq(length).addClass('cur');
    			}
    		}else if(keyCode == 38){
    			if(currentIndex != 0){
    				currentIndex--;
    				product_ul.find('li').siblings().removeClass('cur').eq(currentIndex).addClass('cur');
    			}else{
    				product_ul.find('li').siblings().removeClass('cur').eq(0).addClass('cur');
    			}
    		}else if(keyCode == 13){
    			product_ul.find('li.cur').click();
    		}
        },
        searchProductToAdd:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	var input=$("#EAN").find(".operate_search_ean");
        	var text=input.val();
        	var productId=input.attr("data-id");
        	if(!productId){
        		t.showToast("Please choose the correct product information",function(){
        			input.val("");
	    		});
	    		return;
        	}
        	if($.inArray(productId,t.eanData)!=-1){
        		t.showToast("This code has already been in the list",function(){
        			input.val("");
	    		});
	    		return;
        	}        	
        	var flag=true;        	
        	$("#eanList .operate_ean_group").find(".ean_group_product").each(function(index,elem){
        		var ean=$(elem).find(".productid");
        		var productText=$(elem).find("span");           		
        		if(!ean.val()){        			
        			ean.val(productId);
        			t.eanData.push(productId);
        			productText.text(text);
        			$(elem).next(".ean_group_quantity").find(".quantity").focus();
        			if($("#EAN").find(".btn_check").hasClass("disabled")){
	    	    		$("#EAN").find(".btn_check").removeClass("disabled");
	    	    	}        			        			    			
        			flag=false;
        			return false;
        		}
        	});
        	t.eanScanNum=parseInt($("#eanList .operate_ean_group:last").find(".ean_group_num").attr("data-num"));
        	/* if(no%3!=0&&flag){        		
        		$('#eanList').append(template("eanProductDivTemplate",{eanScanNum:t.eanScanNum,productId:productId,model:text}));
        	} */
        	if(flag){
        		for(var i=0;i<3;i++){
        			if(i==0){
        				$('#eanList').append(template("eanProductDivTemplate",{eanScanNum:t.eanScanNum,productId:productId,model:text}));
        			}else{
        				$('#eanList').append(template("eanProductDivTemplate",{eanScanNum:t.eanScanNum}));
        			}
        			
        			t.eanScanNum++;
        		}
        	}
        	input.val("");
        },
        changeEan:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	var nowValue=$that.val();
        	var preValue=$that.attr("preValue");        	
        	if(!nowValue&&preValue){        		
        		var index=$.inArray(preValue,t.eanData);
        		t.eanData.splice(index,1);
        		$that.attr("preValue","");
        	}  
        	
        },
        changeImei:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	var nowValue=$that.val();
        	var preValue=$that.attr("preValue");        	
        	if(!nowValue&&preValue){
        		var index=$.inArray(preValue,t.imeiData);
        		t.imeiData.splice(index,1);
        		$that.attr("preValue","");
        	}       	        	
        },
        scanCode:function(e){
        	var t=this;       
        	var $that=$(e.currentTarget);
        	var key = e.which;        	
		    if (key == 13&&$.trim($that.val())) {		    	
		    	var thatVal=$.trim($that.val());
		    	switch(t.type){
			    	case"IMEI":
			    		t.codeImei($that,thatVal);
			    		break;
			    	case"EAN":
			    		t.codeEan($that,thatVal);
			    		break;
		    	}

		    }
        },
        codeImei:function($that,thatVal){
        	var t=this;
        	if(thatVal.indexOf("|")!=-1){
        		thatVal=thatVal.replace(/[\|]/g,"");
	    	}
        	if(thatVal.length>15&&thatVal.length%15==0){//批量扫的时候    
        		var reg=new RegExp("^[0-9]*$");
            	if(!reg.test(thatVal)){
            		t.showToast("Please enter correct IMEI code",function(){
    	    			$that.val("").focus();
    	    		});
    	    		return;
            	} 
        		var arrs=[];
	    	    var str="";
	    		for(var i=0,len=thatVal.length;i<len;i++){
	    			str += thatVal[i];
	    		    if((i+1) % 15 == 0){
	    		    	arrs.push(str);
	    		    	str="";
	    		    }
	    		}
	    		var tag=true;	    		
	    		for(var v=0;v<arrs.length;v++){
					if($.inArray(arrs[v],t.imeiData)!=-1){
						tag=false;
	    	    		t.showToast("This code has already been in the list",function(){
	    	    			$that.val("").focus();
	    	    		});
	    	    		break;
	    	    	}
		    	}
	    		imeiToTemplate(tag,"batch");
        	}else{
        		var reg=new RegExp("^([0-9]{15})$");
            	if(!reg.test(thatVal)){
            		t.showToast("Please enter correct 15-digits IMEI code",function(){
    	    			$that.val("").focus();
    	    		});
    	    		return;
            	}            	
    	    	if($.inArray(thatVal,t.imeiData)!=-1){
    	    		t.showToast("This code has already been in the list",function(){
    	    			$that.val("").focus();
    	    		});
    	    		return;
    	    	}
    	    	if(thatVal.indexOf("012345678912456")>-1){
    	    		$that.val("");
		    		return false;
		    	}
    	    	imeiToTemplate(true,"single"); 
        	}        	
        	function imeiToTemplate(flag,type){
        		if(flag){
		    		switch(type){
			    		case"batch":
			    			var emptyImei=$that.closest("tr").find(".imei");
			    			var i=1;
			    			$that.val(arrs[0]);		
			    			
			    			$.each(emptyImei,function(index,elem){
			    				if(!$(elem).val()){
			    					$(elem).val(arrs[i]);
			    					$(elem).attr("preValue",arrs[i]);
			    					t.imeiData.push(arrs[i]);
			    					i++;
			    				}
			    			})
			    			if(t.imeiData.length>0&&$("#IMEI").find(".btn_check").hasClass("disabled")){
			    	    		$("#IMEI").find(".btn_check").removeClass("disabled");
			    	    	}
			    			arrs.splice(0,i);
			    			if(arrs.length>0){
			    				var imeiScans=[];
			    				$.each(arrs,function(index,val){
			    					imeiScans.push(val);
			    					t.imeiData.push(val);
 							       if((index+1)%3==0){
 							    	  $('#imeiTableTbody').append(template("imeiScanTemplate",{imeiScanNum:t.imeiScanNum,imeiScan:imeiScans,type:"scan"}));
 							    	 t.imeiScanNum=parseInt($('#imeiTableTbody').find("tr:last").attr("data-num"));
 							    	  imeiScans=[];
 							    	 return;
 							       }
 							       if(index==arrs.length-1){
 							    	  $('#imeiTableTbody').append(template("imeiScanTemplate",{imeiScanNum:t.imeiScanNum,imeiScan:imeiScans,type:"scan"}));
 							       }
			    				})
			    			}
			    			var lastImeiVal=$("#imeiTableTbody").find(".imei:last").val();			    			
			    			t.imeiScanNum=parseInt($("#imeiTableTbody").find("tr:last").attr("data-num"));
			    			if(lastImeiVal){			    				
			    				$('#imeiTableTbody').append(template("imeiScanTemplate",{imeiScanNum:t.imeiScanNum,imeiScan:"",type:"scan"}));
			    				t.imeiScanNum=parseInt($("#imeiTableTbody").find("tr:last").attr("data-num"));
			    				$("#imeiTableTbody").find("tr:last").find(".imei").eq(0).focus();
			    				return;
			    			}
			    			var lastImei=$("#imeiTableTbody").find("tr:last").find(".imei");
			    			$.each(lastImei,function(index,val){
			    				if(!$(val).val()){
			    					$(val).focus();
			    					return false;
			    				}
			    			}) 
			    			break;
			    		case"single":
			    			var no=0;
			    			$that.attr("preValue",$that.val());
			    	    	t.imeiData.push(thatVal);
			    	    	if(t.imeiData.length>0&&$("#IMEI").find(".btn_check").hasClass("disabled")){
			    	    		$("#IMEI").find(".btn_check").removeClass("disabled");
			    	    	}
			    	    	var imei=$that.closest(".operate_val").next().next().find(".imei");
			    	    	if(imei.length>0){
			    	    		imei.focus();
			    	    	}else{			    	    		
			    	    		if($that.closest("tr").next().length==0){
			    	    			$('#imeiTableTbody').append(template("imeiScanTemplate",{imeiScanNum:t.imeiScanNum,type:"scan"}));	
			    	    		}	
			    	    		t.imeiScanNum=parseInt($that.closest("tr").next().attr("data-num"));
			    	    		$that.closest("tr").next().find("td:eq(1)").find(".imei").focus();
			    	    		no++;
			    	    	}	       
			    			break;
			    		}
		    		
		    	}
        	}
        	    	
        },
        
        codeEan:function($that,thatVal){
        	var t=this;
        	var no=0;
        	var className=$that.attr("class");
        	var reg=new RegExp("(^([0-9]{8})$)|(^([0-9]{13})$)");
        	if(className=="ean"&&!reg.test(thatVal)){
        		t.showToast("Please enter correct EAN code",function(){
	    			$that.val("");
	    		});
	    		return;
        	}
        	var regQuantity=new RegExp("^[1-9]\\d*$");
        	if(className=="quantity"&&!regQuantity.test(thatVal)){
        		t.showToast("Please enter a positive integer for the quantity",function(){
        			$that.focus().val("");
        		});
        		return;
        	}        	
        	if(className=="ean"&&$.inArray(thatVal,t.eanData)!=-1){
        		t.showToast("This code has already been in the list",function(){
	    			$that.val("");
	    		});
	    		return;
        	}
        	$that.attr("preValue",thatVal);
        	if(className=="ean"){
        		t.eanData.push(thatVal);
        	}        	
        	if($that.val()&&$("#EAN").find(".btn_check").hasClass("disabled")){
	    		$("#EAN").find(".btn_check").removeClass("disabled");
	    	}
	    	var nextQuantityElem=$that.closest(".operate_val").next().find(".quantity");
	    	var nextEanElem=$that.closest(".operate_num").next().next().find(".ean");
	    	if(nextQuantityElem.length>0){
	    		nextQuantityElem.focus();
	    		return;
	    	}
	    	if(nextEanElem.length>0){
	    		nextEanElem.focus();
	    		return;
	    	}	    	
	    	if($that.attr("data-count")=="yes"){	    		
	    		if($that.closest("tr").next().length==0){
	    			$('#eanTableTbody').append(template("eanScanTemplate",{eanScanNum:t.eanScanNum,type:"scan"}));
	    		}
	    		$('#eanTableTbody').find("tr:last").find("td:eq(1)").find(".ean").focus();
	    		t.eanScanNum=parseInt($that.closest("tr").next().attr("data-num"));	 
	    		
	    		no++;
	    	}
        },
        downloadTemplate:function(e){ 
        	var link;
        	if(typeof window.chrome !== 'undefined'){
        		link = document.createElement('a');
        		link.href = "/commondefine_file/barcode-non-serialized.xls";
        		link.target = '_self';
        		link.download="";
        		link.click();
        	}else if(typeof window.navigator.msSaveBlob !== 'undefined'){
        		link = $('<a href="/commondefine_file/barcode-non-serialized.xls" download=="" title="" target="_parent"></a>')
      	   	    link.get(0).click();  
	     	}else{	     		
	     		link = new File(["/commondefine_file/barcode-non-serialized.xls"], "", { type: 'application/force-download' });
         		window.open(URL.createObjectURL(link));
	     	}
        },
        exportData:function(e){
        	var takeStockId = $(e.currentTarget).attr("data-stockId");
        	var isIMEI = $(e.currentTarget).attr("data-isImei");
        	window.open("/warehouse/control/exportTakeStockLog?takeStockId="+takeStockId+"&isIMEI="+isIMEI,'_parent');
        },
        checkData:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	if($that.hasClass("disabled")){
        		return;
        	}
        	var obj=t.paramData(t.type);
        	var flag=t.validateData(obj);
        	if(flag){
        		$that.closest(".newCheckStock_btn").find(".btn_bar").show();
            	$that.closest(".newCheckStock_btn").find(".btn_continue").removeClass("disabled");
            	e&&e.currentTarget&&jQuery.showLoading(e.currentTarget);
            	switch(t.type){
    	        	case"IMEI":
    	        		t.imeiCheckServices=$.ajax({
    	    				url:'/warehouse/control/checkIMEIInventory',
    	    				type: 'post',
    	    	            dataType: 'json',
    	    	            data:JSON.stringify(obj),
    	    				success:function(results){	
    	    					e.currentTarget&&jQuery.hideLoading(e.currentTarget);     					
    	    					if(results.resultCode==1){ 
    	    						$("#IMEI").find(".newCheckStock_result_table").show();
    	        	        		$("#checkImeiDataTable").html(template("imeiTableColumn",{type:"scan",result:results})) ;
    	    						$("#IMEI").find(".export").attr({"data-stockId":results.takeStockId,"data-isImei":results.isIMEI});
    	    					}
    	    					$that.closest(".newCheckStock_btn").find(".btn_continue").addClass("disabled");
	        	        		$that.closest(".newCheckStock_btn").find(".btn_bar").hide();
    	    					t.showToast(results.resultMsg);
    	    				}
    	    			});
    	        		break;
    	        	case"EAN":
    	        		t.eanCheckServices=$.ajax({
    	    				url:'/warehouse/control/checkPhysicalInventory',
    	    				type: 'post',
    	    	            dataType: 'json',
    	    	            data:JSON.stringify(obj),
    	    				success:function(results){	
    	    					e.currentTarget&&jQuery.hideLoading(e.currentTarget);    
    	    					if(results.resultCode==1){ 
    	    						t.showToast(results.resultMsg);
    	    						$("#EAN").find(".newCheckStock_result_table").show();
    	        	        		$("#checkEanDataTable").html(template("eanTableColumn",{type:"scan",result:results}))
    	    						$("#EAN").find(".export").attr({"data-stockId":results.takeStockId,"data-isImei":results.isIMEI});
    	    					}else{
    	    						t.showToast(results.errorMessage);
    	    					}
    	    					$that.closest(".newCheckStock_btn").find(".btn_continue").addClass("disabled");
	        	        		$that.closest(".newCheckStock_btn").find(".btn_bar").hide();
    	    					
    	    				}
    	    			})
    	        		
    	        		break;
            	}
        	}
        	
        },           
        validateData:function(objs){
        	var t=this;
        	var flag=true;
        	switch(t.type){
	        	case"IMEI":
	        		if(!objs.imei){
	        			t.showToast("The IMEI code cannot be empty.");
	        			flag=false;
	        		}
	        		break;
	        	case"EAN":	 
	        		console.dir("--------ean");
	        		console.dir(objs);
	        		if(objs.length<=0){
	        			t.showToast("The product cannot be empty.");
	        			flag=false;
	        		}
	        		$.each(objs,function(index,val){	        			
	        			if(!val.Quantity||val.Quantity==0){
	        				t.showToast("Please enter a positive integer for the quantity.");
	        				flag=false;
	        				return;
	        			}
	        		})
	        		break;
	    	}
        	return flag;
        },
        continueStartCheck:function(e){
        	var t=this;
        	var $that=$(e.currentTarget);
        	var flag="1";
        	var type=$that.attr("data-type");
        	if($that.hasClass("disabled")){
        		return;
        	}
        	var checkTarget=$that.closest(".newCheckStock_btn").find(".btn_check");
        	switch(type){
	        	case"IMEI":		        		
	        		if(t.imeiCheckServices!=null){
	        			t.imeiCheckServices.abort();
	        			t.imeiCheckServices=null;
	        		}
	        		break;
	        	case"EAN":
	        		if(t.eanCheckServices!=null){
	        			t.eanCheckServices.abort();
	        			t.eanCheckServices=null;
	        		}
	        		break;
	        	}
        	$that.addClass("disabled");
        	checkTarget&&$.hideLoading(checkTarget);        	
        	$that.closest(".newCheckStock_btn").find(".btn_bar").hide();
        },       
        showLogDetail:function(e){
        	var t=this;        	
        	var parentElem=$(e.currentTarget).closest("tr");        	
        	var stockId=$(e.currentTarget).attr("data-stockId");        	
        	var time=parentElem.find(".detail_time").text();
        	var type=parentElem.find(".detail_type").text();
        	var facilityName=parentElem.find(".detail_facilityName").text();
        	var operator=parentElem.find(".detail_operator").text();        	        	
        	t.$content.find(".newCheckStock_content").hide();
        	t.$content.find(".newCheckStock_log_record").show();
        	t.logImeiData(stockId,time,type,operator,facilityName);                	
        },
        returnLog:function(e){
        	var t=this;       	
        	var id=$(e.currentTarget).attr("data-id");
        	t.changeType("","Log");
        },
        getNowMonth:function(){
        	var timeNow = document.getElementById('timeNow');
        	var nowDate;   
        	var date;
            if(timeNow){
            	nowDate=getFixNewDate(parseInt(timeNow.value),"dd/MM/YYYY");
            	date=nowDate.split("/")[2]+"-"+nowDate.split("/")[1];
            	return date;
            }    
        },
        
        paramData:function(){       
            var t=this;
            switch(t.type){
	            case"IMEI":
	            	var formatData={"imei":""};
	                $("#imeiTableTbody").find(".imei").each(function(index,elem){
	             	   var val=$.trim($(elem).val());
	             	   formatData.imei+=val?val+",":"";
	                })
	             	return formatData;
	            	break;
	            case"EAN":	            	
	            	var objs=[];	                             
	                $("#eanList").find(".operate_ean_group").each(function(key,elem){
	                	var obj={};	   
	                	if($.trim($(elem).find(".productid").val())){
	                		obj.NO=$.trim($(elem).find(".productnum").val());	                	
	                		obj.productid=$.trim($(elem).find(".productid").val());
	                		obj.description=$.trim($(elem).find(".ean_group_product span").text());	                	
	                		obj.Quantity=$.trim($(elem).find(".quantity").val());
	                		objs.push(obj);	 
	                	}                			               	
	                })
	             	return objs;
	            	break;
	            }
            
         }, 
        logImeiData:function(stockId,time,type,operator,facilityName){        	
        	var t=this;
        	var results;
        	$(".log_record_content").find(".export").attr({"data-stockId":stockId,"data-isImei":type=="IMEI"?"Y":"N"});
        	$(".record_content_count_desc").find(".desc_time").text(time);
        	$(".record_content_count_desc").find(".desc_warehouse").text(facilityName);
    		$(".record_content_count_desc").find(".desc_type").text(type);
    		$(".record_content_count_desc").find(".desc_operator").text(operator);
        	$.ajax({
				url:'/warehouse/control/findTakeStockDetailLog',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify({"takeStockId":stockId}),
	           	success:function(results){	
					if(results.resultCode==1){						
						switch(type){
				        	case"IMEI":
				        		var imeiScans=[];
				        		var list={"imei":[]};
				        		$.each(results.takeStockCategory,function(key,val){					        			
				        			list.imei.push({"no":key+1,"val":val.imeiOrEan});
									if((key+1)%3==0){
										imeiScans.push(list);
					        			list={"imei":[]};
					        		}
					        		if(key==results.takeStockCategory.length-1){					        			
					        			if(list.imei.length<3&&list.imei.length>0){
					        				var len=key+1;	
					        				for(var i=3-list.imei.length;i>0;i--){
					        					list.imei.push({"no":++len,"val":""});
					        				}
					        			}
					        			imeiScans.push(list);
					        		}
				        		})
				        		
				        		$("#scodeLogTable").html(template("imeiScanTemplate",{type:"show",date:imeiScans}));
				        		$("#dateLogTable").html(template("imeiTableColumn",{type:"show",date:results}))
				        		break;
				        	case"EAN":
				        		var eanScans=[];
				        		var list={"dates":[]};
								$.each(results.takeStockCategory,function(key,val){
									list.dates.push({"no":key+1,"ean":val.imeiOrEan,"quantity":val.quantity});
									if((key+1)%3==0){
										eanScans.push(list);
					        			list={"dates":[]};
					        		}
					        		if(key==results.takeStockCategory.length-1){					        						        			
					        			if(list.dates.length<3&&list.dates.length>0){
					        				var len=key+1;		
					        				for(var i=3-list.dates.length;i>0;i--){
					        					list.dates.push({"no":++len,"ean":"","quantity":""});
					        				}
					        			} 
					        			eanScans.push(list);
					        		}
				        		})
				        		
				        		$("#scodeLogTable").html(template("eanScanTemplate",{type:"show",date:eanScans}));
				        		$("#dateLogTable").html(template("eanTableColumn",{type:"show",date:results}))
				        		break;
			        	}
					}					
				}
			})
        	
        }
        
                
        
	})
	new View();
	
})




</script>

















