<div id="index">
	<div class="index_content_box">
		<div class="index_content">
			<div class="index_content_top">
				<span class="index_module"></span>
				<span class="index_description"></span>
			</div>
			<div class="index_content_ctr">
				<div class="left_btn"></div>
				<div class="catalogues_content">
                    <#if apps?exists>
                      <#assign appIndex = 0 />
                      <div class="catalogues_content_first">
                      <#list apps as app>
                            <#if (!app.hide?exists || app.hide != "Y") && app.linkUrl?has_content>
                                 <#assign appIndex = appIndex + 1 />

                                     <#if app.applicationId == 'catalog'>
                                      <div class="group">
                                        <a class="catalog" href="/catalog/control/Classfication" data-desc="Product Catalog">Catalog</a>
                                      </div>
                                    </#if>
                                     <#if app.applicationId == 'ordermgr'>
                                      <div class="group">
                                        <a class="order_management" href="/ordermgr/control/findSalesOrders" data-desc="My Orders, Create Order, Find Orders" >Order Management</a>
                                      </div>
                                    </#if>
                                     <#if app.applicationId == 'warehouse'>
                                     <div class="group">
	                                     <#if security.hasPermission("WRHS_INV_RAI", session)>
								       		 <a class="warehouse" href="/warehouse/control/receiveInventoryItem" data-desc="Inventory, Shipping, Manufacturing">Warehouse</a>
								    		<#else>
								       		 <a class="warehouse" href="/warehouse/control/newFindInventoryItem" data-desc="Inventory, Shipping, Manufacturing">Warehouse</a>
								   		</#if>
                                     </div>
                                    </#if>
                                    <#-- 
                                     <#if app.applicationId == 'purchasing'>
                                      <div class="group">
                                        <a class="purchasing" href="javascript:void(0);" data-desc="Suppliers, Planning, and Purchasing">Purchasing</a>
                                      </div>
                                    </#if> -->
                                     <#if app.applicationId == 'party'>
                                      <div class="group margin_right">
                                        <a class="store" href="/Store/control/main" data-desc="Store、Role">Store</a>
                                      </div>
                                    </#if>
                                    <#-- 
                                     <#if app.applicationId == 'crmsfa'>
                                      <div class="group">
                                        <a class="crm_management" href="javascript:void(0);" data-desc="CRM, Customer Service, Order Entry, Marketing">CRM Management</a>
                                      </div>
                                    </#if>-->
                                     <#if app.applicationId == 'party'>
                                      <div class="group">
                                        <a class="party_admin" href="/partymgr/control/findparty" data-desc="Find Parties、Party Classification Group List、Security Groups List">Party Admin</a>
                                      </div>
                                    </#if>
                                     <#if app.applicationId == 'webpos'>
                                      <div class="group">
                                        <a class="web_pos" href="/wpos/control/main" data-desc="Web POS">Web POS</a>
                                      </div>
                                    </#if> 
                                     
                                    <#if app.applicationId == 'report'>
		                         		<#if security.hasPermission("REPMENU_SALE", session)>	
		                         		<div class="group">
	                                        <a class="financials" href="/report/control/BANSalesReport" data-desc="data_report">Report</a>
	                                    </div>
					                    <#elseif security.hasPermission("REPMENU_OP_INV", session)>
					                    <div class="group">
	                                        <a class="financials" href="/report/control/BANInventoryReport" data-desc="data_report">Report</a>
	                                    </div>
					                 	<#elseif security.hasPermission("WRHS_VIEW_REPORT", session)>
					                 	<div class="group">
	                                        <a class="financials" href="/report/control/StockinReport" data-desc="data_report">Report</a>
	                                    </div>
					                 	<#elseif security.hasPermission("REPMENU_OP_INV", session)>
					                 	<div class="group">
	                                        <a class="financials" href="/report/control/OperatorInventoryReport" data-desc="data_report">Report</a>
	                                    </div>
					                 	<#elseif security.hasPermission("REPMENU_OP_SALE", session)>
					                 	<div class="group">
	                                        <a class="financials" href="/report/control/OperatorSalesReport" data-desc="data_report">Report</a>
	                                    </div>
					                 	<#elseif security.hasPermission("WRHS_VIEW_MTNREPORT", session)>
					                 	<div class="group">
	                                        <a class="financials" href="/report/control/OperatorStockinReport" data-desc="data_report">Report</a>
	                                    </div>
					                 </#if>		                        	
		                         </#if>
                            </#if>
                      </#list>
                      </div>
                    </#if>
				</div>
				<div class="right_btn"></div>
			</div>
			<div class="index_content_btm">
				<a class="left_icon selected" href="javascript:void(0);"></a>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

$(document).ready(function(){

    var index_module = $('.index_module')
    var index_description = $('.index_description');

    index_module.text($('.catalogues_content a:first').text());
    index_description.text($('.catalogues_content a:first').attr("data-desc"));

	$('.catalogues_content a').hover(function(){
	    var _this = $(this);
	    index_module.text(_this.text());
	    index_description.text(_this.attr("data-desc"));
	})
	
});

</script>
