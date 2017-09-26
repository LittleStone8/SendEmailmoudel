<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>
<@import location="component://opentaps-common/webapp/common/order/infoMacros.ftl"/>

<#-- This file has been modified by Open Source Strategies, Inc. -->

<#if order?exists>
      
<#if order.externalId?has_content>
  <#assign externalOrder = "(" + order.externalId + ")"/>
</#if>

<#-- Approve Order action; we need check permission when it is a purchase order -->
<#if (order.isCreated() || order.isProcessing() || order.isOnHold()) && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session))>
  <#assign actionAction><@actionForm form="approveOrderAction" text="${uiLabelMap.OrderApproveOrder}"/></#assign>
  <@form name="approveOrderAction" url="changeOrderItemStatus" orderId=order.orderId statusId="ITEM_APPROVED" />
<#-- Hold Order action; we need check permission when it is a purchase order -->
<#elseif (order.isApproved() && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session)))>
  <#assign actionAction><@actionForm form="holdOrderAction" text="${uiLabelMap.OrderHold}"/></#assign>
  <@form name="holdOrderAction" url="changeOrderStatus" orderId=order.orderId statusId="ORDER_HOLD" />
</#if>

<#-- if (order.isOpen() && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session))) 
<#if (security.hasPermission("BIM_ORDER_CANCEL", session))&&((order.isOpen()&&(!order.ispacked())&&order.istoday()||(order.isPayment())) && (order.isSalesOrder()&&(!order.ispacked())&&order.istoday() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session)))>
 -->
 
 <#if (security.hasPermission("BIM_ORDER_CANCEL", session)&&order.istoday()&&!order.isNeedShipment()&&!order.isCancelled() || security.hasPermission("BIM_ORDER_CANCEL", session)&&order.istoday()&&order.isNeedShipment()&& order.isPayment()&&!order.isCancelled()   ||
 	   security.hasPermission("BIM_ORDER_CANCEL_30", session)&&order.is30day()&&!order.isNeedShipment()&&!order.isCancelled() || security.hasPermission("BIM_ORDER_CANCEL_30", session)&&order.is30day()&&order.isNeedShipment()&& order.isPayment()&&!order.isCancelled()
 
     )>
  <#assign cancelOrderAction><@actionForm form="cancelOrderAction" text="${uiLabelMap.OpentapsCancelOrder}"/></#assign>
  <@form name="cancelOrderAction" url="cancelOrderItem" orderId="${order.orderId}" />
</#if>

<#-- Complete Order action; we need check permission when it is a purchase order -->
<#-- if (order.isApproved()||(order.isShipped())) && !order.uncompleteItems?has_content && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session))
<#if (security.hasPermission("BIM_ORDER_COMPLETE", session))&&(order.isApproved()||(order.isShipped())) && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session)) && !order.isChargeOrder()>
 -->
 <#if (security.hasPermission("BIM_ORDER_COMPLETE", session)  && !order.isChargeOrder() && order.isNeedShipment() && order.isShipped())  >
  <#assign completeOptionAction><@actionForm form="completeOrderAction" text="${uiLabelMap.OrderCompleteOrder}"/></#assign>
  <@form name="completeOrderAction" url="changeOrderStatus" orderId=order.orderId statusId="ORDER_COMPLETED" />
</#if>

<#-- Complete Order action; we need check permission when it is a purchase order -->
<#-- if (order.isApproved()||(order.isShipped())) && !order.uncompleteItems?has_content && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session))
<#if (security.hasPermission("BIM_ORDER_COMPLETE", session))&&(order.isApproved()||(order.isShipped())) && (order.isSalesOrder() || security.hasEntityPermission("PRCH", "_ORD_APPRV", session)) && order.isChargeOrder()>
-->
<#if (security.hasPermission("BIM_ORDER_COMPLETE", session)  && order.isChargeOrder() && order.isNeedShipment() && order.isShipped() ) >
  <#assign Completewithoutpayment><@actionForm form="Completewithoutpayment" text="Complete without payment"/></#assign>
  <@form name="Completewithoutpayment" url="changeOrderStatus" orderId=order.orderId statusId="ORDER_COMPLETED_W_P" />
</#if>


<#-- Order PDF action -->
<#assign pdfAction><@actionForm form="orderPdfAction" text="${uiLabelMap.OpentapsContentType_ApplicationPDF}"/></#assign>
<#if order.isSalesOrder()>
  <#assign orderReportId = "SALESORDER" />
<#else>
  <#assign orderReportId = "PRUCHORDER" />
</#if>
<@form name="orderPdfAction" target="_blank" method="get" url="order.pdf" orderId=order.orderId reportType="application/pdf" reportId="${orderReportId}"/>
<#-- Order Picklist PDF action -->
<#if order.isPickable()>
  <#assign picklistAction><@actionForm form="orderPicklistPdfAction" text="${uiLabelMap.ProductPickList}"/></#assign>
  <@form name="orderPicklistPdfAction" target="_blank" method="get" url="shipGroups.pdf" orderId=order.orderId />
</#if>
<#-- Order Email action -->
<#assign emailOrderAction><@actionForm form="orderEmailAction" text="${uiLabelMap.CommonEmail}"/></#assign>
<@form name="orderEmailAction" url="writeOrderEmail" orderId=order.orderId />

<#-- <#if actionAction?has_content || completeOptionAction?has_content ||cancelOrderAction?has_content>  
  <#assign separatorLineAction><option value="">${uiLabelMap.OpentapsDefaultActionSeparator}</option></#assign>
</#if> -->
<!-- 
<#assign orderfalg>
	<#if !order.isNeedShipment()>
	</#if>
	<#if !order.ispacked()>
	</#if>
</#assign>

<#assign orderfalg1>
	<#if (order.isApproved()||(order.isShipped()))>
	a
	</#if>
</#assign>
 -->

<#assign extraOptions>
  <@selectActionForm name="orderActions" prompt="${uiLabelMap.CommonSelectOne}">
    ${cancelOrderAction?if_exists}
    ${completeOptionAction?if_exists}
    ${Completewithoutpayment?if_exists}
    ${separatorLineAction?if_exists}

  </@selectActionForm>
</#assign>
<#--
    ${cancelOrderAction?if_exists}
    ${completeOptionAction?if_exists}
    ${separatorLineAction?if_exists}
    ${picklistAction?if_exists}
    ${pdfAction?if_exists}
    ${emailOrderAction?if_exists}
-->

<@frameSection title="${uiLabelMap.OrderOrder} #${order.orderId} ${externalOrder?if_exists} ${uiLabelMap.CommonInformation}" extra=extraOptions>
<div class="screenlet">
    <div class="screenlet-body">
	    <table width="100%" class="orderInfoTable" border="0" cellpadding="1" cellspacing="0">
	      <#-- order name -->
	      <#if order.isOpen()>
	        <@form name="updateOrdrNameHiddenForm" url="updateOrderHeader" orderId=order.orderId>
	          <@infoRowNested title=uiLabelMap.OrderOrderName >
	            <@inputText name="orderName" default=order.orderName />
	            <@inputSubmit title=uiLabelMap.CommonUpdate />
	          </@infoRowNested>
	        </@form>
	      <#else>
	        <@infoRow title=uiLabelMap.OrderOrderName content=order.orderName?default("") /> 
	      </#if>
	      <@infoSepBar/>
	      <#-- order date --> 
	      <@infoRow title=uiLabelMap.OrderDateOrdered content=order.orderDate?default("") />
	      <@infoSepBar/>
	      <#-- order status history -->
	      <@infoRowNested title=uiLabelMap.OrderStatusHistory>
	        <div class="tabletext">${uiLabelMap.OrderCurrentStatus}: ${order.status.get("description",locale)}</div>
	        <#if order.orderStatuses?has_content>
	          <hr class="sepbar"/>
	          <#list order.orderStatuses as orderStatus>
	          	 
	            <div class="tabletext">
	              ${getLocalizedDate(orderStatus.statusDatetime, "DATE_TIME")}: ${orderStatus.statusItem.get("description",locale)} ${uiLabelMap.CommonBy} ${orderStatus.statusUserLogin?default("unknown")}
	            </div>
	          </#list>
	        </#if>
	      </@infoRowNested>
	     
	      <#if order.internalCode?has_content>
	        <@infoSepBar/>
	        <@infoRow title=uiLabelMap.OrderInternalCode content=order.internalCode />
	      </#if>
	      <#if order.primaryPoNumber?has_content>
	        <@infoSepBar/>
	        <@infoRow title=uiLabelMap.OpentapsPONumber content=order.primaryPoNumber />
	      </#if>
	      <#-- sales channel is only for sales order -->
	      <#if order.isSalesOrder()>
	        <@infoSepBar/>
	        <#if order.salesChannelEnumId?has_content>
	          <@infoRow title=uiLabelMap.OrderSalesChannel content=(order.salesChannel.get("description",locale))?default(uiLabelMap.CommonNA) />
	        <#else/>
	          <@infoRow title=uiLabelMap.OrderSalesChannel content=uiLabelMap.CommonNA />
	        </#if>
	      </#if>
	      <#if order.commissionAgents?has_content>
	        <@infoSepBar/>
	        <@infoRowNested title=uiLabelMap.CrmCommissionRep>
	          <div class="tabletext">
	            <#list order.commissionAgents as party>
	              ${party.name}<#if party_has_next><br/></#if>
	            </#list>
	          </div>
	        </@infoRowNested>
	      </#if>
	
	      <#if order.distributorOrderRole?exists>
	        <@infoSepBar/>
	        <@infoRowNested title=uiLabelMap.OrderDistributor>
	          <div class="tabletext">
	            ${order.distributor.getNameForDate(order.orderDate).fullName?default("[${uiLabelMap.OrderPartyNameNotFound}]")}
	          </div>
	        </@infoRowNested>
	      </#if>
	
	      <#if order.affiliateOrderRole?exists>
	        <@infoSepBar/>
	        <@infoRowNested title=uiLabelMap.OrderAffiliate>
	          <div class="tabletext">
	            ${order.affiliate.getNameForDate(order.orderDate).fullName?default("[${uiLabelMap.OrderPartyNameNotFound}]")}
	          </div>
	        </@infoRowNested>
	      </#if>
	
	      <#if orderContentWrapper.get("IMAGE_URL")?has_content>
	        <@infoSepBar/>
	        <@infoRowNested title=uiLabelMap.OrderImage>
	          <div class="tabletext">
	            <a href="<@ofbizUrl>viewimage?orderId=${order.orderId}&orderContentTypeId=IMAGE_URL</@ofbizUrl>" target="_orderImage" class="buttontext">${uiLabelMap.OrderViewImage}</a>
	          </div>
	        </@infoRowNested>
	      </#if>
	
      <@infoSepBar/>
		<@infoRow title="StroeName" content=(order.getOrderStroe()) />
	    </table>
	</div>
</div>
</@frameSection>

</#if> <#-- end of if order?exists -->

<script>
	var dateText = jQuery('.orderInfoTable tbody tr')[2];
	var dateContent = (jQuery(dateText).find('td:last-child .tabletext').text()).split('.')[0];
	var time = dateContent.replace(/-/g,"/");
	var date = new Date(time);
	
	function formatDate(dateContent) {  
	    var y = dateContent.getFullYear();  
	    var M = dateContent.getMonth() + 1;  
	    	M = M < 10 ? '0' + M : M;  
	    var d = dateContent.getDate();  
	    	d = d < 10 ? ('0' + d) : d; 
	    var h = dateContent.getHours();  
	        h = h < 10 ? ('0' + h) : h; 
	    var m = dateContent.getMinutes();
	        m = m < 10 ? ('0' + m) : m; 
	    var s = dateContent.getSeconds();
	        s = s < 10 ? ('0' + s) : s; 	 
	    return d + '/' + M + '/' + y + ' ' + h + ':' + m + ':' + s; 
	}; 
	
	jQuery(dateText).find('td:last-child .tabletext').text(formatDate(date));
	
</script>

