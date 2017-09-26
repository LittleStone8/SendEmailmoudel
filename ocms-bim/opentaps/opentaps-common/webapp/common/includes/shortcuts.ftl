<#--
 * Copyright (c) Open Source Strategies, Inc.
 *
 * Opentaps is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Opentaps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Opentaps.  If not, see <http://www.gnu.org/licenses/>.
-->

<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>

<#if shortcutGroups?has_content>
  <#list shortcutGroups as sg>
    <@leftSection title=uiLabelMap.get(sg.uiLabel?default("OpentapsShortcuts"))>
      <ul>
      <#list sg.allowedShortcuts as shortcut>
          <#if sg.showAsDisabled() || shortcut.showAsDisabled()>
            <li onclick="selectMenu(this)" class="disabled" ><div>${uiLabelMap.get(shortcut.uiLabel!)}</div></li>
          <#else>
            <#assign shortcutClass = (      shortcut.linkUrl?contains(parameters.thisRequestUri) )?   string("class=\"menu_active\"", "")/>
            <#if shortcut.linkUrl ?contains("backOrderedItems") || shortcut.linkUrl ?contains("ReceiveInventoryAgainstPurchaseOrder") || shortcut.linkUrl ?contains("findShipmentReceipt") || shortcut.linkUrl ?contains("stockMoves") || shortcut.linkUrl ?contains("traceInventory") || shortcut.linkUrl ?contains("FindFacilityLocation")>
            	<#else>
            	<#if 	shortcut.linkUrl ?contains("FindFacilityTransfers") && parameters.thisRequestUri ?contains("MassTransfer")
            		||	shortcut.linkUrl ?contains("physicalInventory") && parameters.thisRequestUri ?contains("FindFacilityPhysicalInventory")
            	>
            		<#assign  shortcutClass="class=\"menu_active\"" />
            	<#else>
            	</#if >
            	
            <#if shortcut.linkUrl ?contains("findInventoryItem") || shortcut.linkUrl ?contains("receiveInventoryItem") || shortcut.linkUrl ?contains("physicalInventory") || shortcut.linkUrl ?contains("FindFacilityTransfers") || shortcut.linkUrl ?contains("manageLots")
            ||shortcut.linkUrl ?contains("IncomingShipments")||shortcut.linkUrl ?contains("createPicklist")||shortcut.linkUrl ?contains("openPicklists")||shortcut.linkUrl ?contains("pickedPicklists")||shortcut.linkUrl ?contains("PackOrder")||shortcut.linkUrl ?contains("QuickScheduleShipmentRouteSegment")
            
            >
            	
            
          		
          		<#if shortcut.linkUrl ?contains("receiveInventoryItem") && security.hasPermission("WRHS_INV_RAI", session)>
            		<li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >Receive Items</a></li>
          		</#if>
          		
          		
          	</#if> 	
          		 
          		
          			
          	</#if>
          	
          </#if>
          </#list>
      
      
        <#list sg.allowedShortcuts as shortcut>
          <#if sg.showAsDisabled() || shortcut.showAsDisabled()>
            <li onclick="selectMenu(this)" class="disabled" ><div>${uiLabelMap.get(shortcut.uiLabel!)}</div></li>
          <#else>
            <#assign shortcutClass = (      shortcut.linkUrl?contains(parameters.thisRequestUri) )?   string("class=\"menu_active\"", "")/>
            <#if shortcut.linkUrl ?contains("backOrderedItems") || shortcut.linkUrl ?contains("ReceiveInventoryAgainstPurchaseOrder") || shortcut.linkUrl ?contains("findShipmentReceipt") || shortcut.linkUrl ?contains("stockMoves") || shortcut.linkUrl ?contains("traceInventory") || shortcut.linkUrl ?contains("FindFacilityLocation")>
            	<#else>
            	<#if 	shortcut.linkUrl ?contains("FindFacilityTransfers") && parameters.thisRequestUri ?contains("MassTransfer")
            		||	shortcut.linkUrl ?contains("physicalInventory") && parameters.thisRequestUri ?contains("FindFacilityPhysicalInventory")
            	>
            		<#assign  shortcutClass="class=\"menu_active\"" />
            	<#else>
            	</#if >
            	
            <#if shortcut.linkUrl ?contains("findInventoryItem") || shortcut.linkUrl ?contains("receiveInventoryItem") || shortcut.linkUrl ?contains("physicalInventory") || shortcut.linkUrl ?contains("FindFacilityTransfers") || shortcut.linkUrl ?contains("manageLots")
            ||shortcut.linkUrl ?contains("IncomingShipments")||shortcut.linkUrl ?contains("createPicklist")||shortcut.linkUrl ?contains("openPicklists")||shortcut.linkUrl ?contains("pickedPicklists")||shortcut.linkUrl ?contains("PackOrder")||shortcut.linkUrl ?contains("QuickScheduleShipmentRouteSegment")
            
            >
            	
            	<#if shortcut.linkUrl ?contains("newFindInventoryItem") && security.hasPermission("WRHS_INV_FII", session)>
            		<li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >${uiLabelMap.get(shortcut.uiLabel!)}</a></li>
          		</#if>
          		
          	
          		
          		<#if shortcut.linkUrl ?contains("physicalInventory") && security.hasPermission("WRHS_INV_AQ", session)>
            		<li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >${uiLabelMap.get(shortcut.uiLabel!)}</a></li>
          		</#if>
          		
          		<#if shortcut.linkUrl ?contains("newTransferInventory") && security.hasPermission("WRHS_INV_TI", session)>
            		<li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >${uiLabelMap.get(shortcut.uiLabel!)}</a></li>
          		</#if>
          		
          		<#if shortcut.linkUrl ?contains("manageLots") && security.hasPermission("WRHS_INV_ML", session)>
            		<li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >${uiLabelMap.get(shortcut.uiLabel!)}</a></li>
          		</#if>
          		
          		 <#else>
          		    <li  onclick="selectMenu(this)" ${shortcutClass}><a href="<@ofbizUrl>${shortcut.linkUrl}</@ofbizUrl>" >${uiLabelMap.get(shortcut.uiLabel!)}</a></li>
          	</#if> 	
          		 
          		 <#if shortcut.linkUrl ?contains("manageLots")>
                  
              		<#if security.hasPermission("WRHS_INV_FII", session) || security.hasPermission("WRHS_ADMIN", session)>
              			<li><a href="/warehouse/control/productStock">Product Stock</a></li>
              		</#if>
              		<#if security.hasPermission("WRHS_INV_CHECK", session) || security.hasPermission("WRHS_ADMIN", session)>
              			<li><a href="/warehouse/control/checkStock">Check Stock</a></li>
              		</#if>
              		<#if security.hasPermission("WRHS_INV_MPM", session) || security.hasPermission("WRHS_ADMIN", session)>
              			<li><a href="/warehouse_web/index.html#changeproductmodel">Modify Product Model</a></li>
              		</#if>
 				 </#if> 
          		
          			
          	</#if>
          	
          </#if>
          </#list>

      </ul>
    </@leftSection>
  </#list>
</#if>

<script>
window.onload=function(){
	if(window.location.href.indexOf("viewOrders")>-1){		
		var leftTopDom=document.getElementById("left-content-column");
		var leftADom=leftTopDom.getElementsByTagName("a");
		for(var i=0; i<leftADom.length;i++){
			if(leftADom[i].innerHTML.indexOf("Order")>-1){			
				leftADom[i].parentNode.className="menu_active";
			}			
		}
		
	}
}
function isContains(str, substr) {
    return str.contains(substr);
}
function selectMenu(t,name){		
	for(var i=0; i<t.parentNode.children.length;i++){
		t.parentNode.children[i].className="";
	}
	t.className="menu_active";
	
}
function viewOrders(){
          this.location.href="/warehouse/control/viewOrders?facilityId="+facility_tran_002+"&statusId=Completed";
}
</script>


