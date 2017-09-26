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

<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<#-- This file has been modified from the version included with the Apache-licensed OFBiz facility application -->
<#-- This file has been modified by Open Source Strategies, Inc. -->
<#if security.hasEntityPermission("WRHS", "_INV_TI", session)>

<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>

<@frameSection title="${uiLabelMap.ProductInventoryTransfersFor} ${facility?exists?string((facility.facilityName)!,'')} [${uiLabelMap.CommonId}:${facilityId?if_exists}]">

<#if activeOnly>
  <a href="<@ofbizUrl>FindFacilityTransfers?facilityId=${facilityId}&activeOnly=false</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductActiveAndInactive}</a>
<#else>
  <a href="<@ofbizUrl>FindFacilityTransfers?facilityId=${facilityId}&activeOnly=true</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductActiveOnly}</a>
</#if>
<#--<a href="<@ofbizUrl>FindFacilityTransfers?facilityId=${facilityId}&completeRequested=true</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductCompleteRequestedTransfers}</a>-->
<#--<a href="<@ofbizUrl>TransferInventoryItem?facilityId=${facilityId}</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductInventoryTransfer}</a>-->
<#--<a href="/warehouse/control/MassTransfer?facilityId=${facilityId}" class="buttontext">${uiLabelMap.ProductInventoryTransfer}</a>-->
<a href="/warehouse/control/newTransferInventory" class="buttontext">${uiLabelMap.ProductInventoryTransfer}</a>
<br/>
<#if toTransfers?has_content>
  <br/>
  <@frameSection title="Receive by:&nbsp;${facility?exists?string((facility.facilityName)!,'')} [${uiLabelMap.CommonId}:${facilityId?if_exists}]" innerStyle="padding:0;border:0">
    <table class="listTable">
      <tr class="listTableHeader">
        <td><a href="javascript:orders('INVENTORY_TRANSFER_ID')">${uiLabelMap.ProductTransferId}</a></td>
        <td><a href="javascript:orders('inventory_item_id')">${uiLabelMap.ProductItem}</a></td>
        <td><a href="javascript:orders('x.product_id')">${uiLabelMap.ProductProductId}</a></td>
        <td><a href="javascript:orders('internal_name')">Model</a></td>
        <td><a href="javascript:orders('productAttributes')">Products Attributes</a></td>
        <td><a href="javascript:orders('QUANTITY_ON_HAND_TOTAL')">${uiLabelMap.ProductSerialQty}</a></td>
        <td>Sent by</td>
        <td><a href="javascript:orders('send_date')">${uiLabelMap.CommonSendDate}</a></td>
        <td><a href="javascript:orders('STATUS_ID')">${uiLabelMap.CommonStatus}</a></td>
        <td>&nbsp;</td>
      </tr>

      <#list toTransfers as transfer>
        <#-- <#assign inventoryItem = transfer.getRelatedOneCache("InventoryItem")?if_exists/>
        <#if inventoryItem?has_content>
          <#assign product = inventoryItem.getRelatedOneCache("Product")?if_exists/>
        </#if>
        <#assign fac = transfer.getRelatedOneCache("Facility")?if_exists/> -->
        <tr class="${tableRowClass(transfer_index)}">
          <@displayLinkCell href="TransferInventoryItem?inventoryTransferId=${transfer.inventoryTransferId!}" text=transfer.inventoryTransferId! />
       <#--   <@displayLinkCell href="EditInventoryItem?inventoryItemId=${transfer.inventoryItemId!}" text=transfer.inventoryItemId! /> -->
         <@displayCell text=transfer.inventoryItemId! />
          <@displayCell text=transfer.productId! />
          <@displayCell text=transfer.internalName! />
          <@displayCell text=transfer.description! />
          <#-- <#if inventoryItem?exists && inventoryItem.inventoryItemTypeId.equals("NON_SERIAL_INV_ITEM")>
          
            <@displayCell text="${(transfer.quantityOnHandTotal)!}" />
          <#elseif inventoryItem?exists && inventoryItem.inventoryItemTypeId.equals("SERIALIZED_INV_ITEM")>
            <@displayCell text=transfer.serialNumber! />
          </#if> -->
          <@displayCell text="${(transfer.quantityOnHandTotal)!}" />
          <@displayCell text="${(transfer.facilityName)!}&nbsp;[${(transfer.facilityId)!}]" />
          <@displayDateCell date=transfer.sendDate! />
          <td>
            <#if (transfer.statusId)?exists>
<#--               <#assign transferStatus = transfer.getRelatedOneCache("StatusItem")?if_exists />
 -->              <@display text="${transfer.statusId}" />
            </#if>
          </td>
          <td align="center">
          <#if transfer.statusId != "IXF_CANCELLED" && transfer.statusId != "IXF_COMPLETE" && transfer.statusId != "INV_AVAILABLE">
            <@displayLink href="TransferInventoryItem?inventoryTransferId=${transfer.inventoryTransferId!}" text=uiLabelMap.CommonEdit class="buttontext"/>
          </#if>
          </td>
        </tr>
      </#list>
    </table>
  </@frameSection>
</#if>

<#if fromTransfers?has_content>
  <#if completeRequested??>
    <form name="CompleteRequested" method="post" action="CheckInventoryForCompleteRequestedTransfers?completeRequested=true&facilityId=${facility.facilityId}">
  </#if>
  <@frameSection title="Sent by:&nbsp;${facility?exists?string((facility.facilityName)!,'')} [${uiLabelMap.CommonId}:${facilityId?if_exists}]" innerStyle="padding:0;border:0">
    <table class="listTable">
      <tr class="listTableHeader">
        <td><a href="javascript:orders('INVENTORY_TRANSFER_ID')">${uiLabelMap.ProductTransferId}</a></td>
        <td><a href="javascript:orders('inventory_item_id')">${uiLabelMap.ProductItem}</a></td>
        <td><a href="javascript:orders('x.product_Id')">${uiLabelMap.ProductProductId}</a></td>
        <td><a href="javascript:orders('internal_name')">Model</a></td>
        <td><a href="javascript:orders('productAttributes')">Products Attributes</a></td>
        <td><a href="javascript:orders('QUANTITY_ON_HAND_TOTAL')">${uiLabelMap.ProductSerialQty}</a></td>
        <td>Receive by</td>
        <td><a href="javascript:orders('send_date')">${uiLabelMap.CommonSendDate}</a></td>
        <#if !completeRequested??>
          <td><a href="javascript:orders('STATUS_ID')">${uiLabelMap.CommonStatus}</a></td>
        </#if>
        <td align="center">
          <#if completeRequested??><@inputMultiSelectAll form="CompleteRequested" /><#else>&nbsp;</#if>
        </td>
      </tr>

      <#list fromTransfers as transfer>
       <#--  <#assign inventoryItem = transfer.getRelatedOneCache("InventoryItem")?if_exists/>
        <#if inventoryItem?has_content>
          <#assign product = inventoryItem.getRelatedOneCache("Product")?if_exists/>
        </#if> 
        <#assign fac = transfer.getRelatedOneCache("ToFacility")?if_exists/>-->
        <tr class="${tableRowClass(transfer_index)}">
          <@displayLinkCell href="TransferInventoryItem?inventoryTransferId=${transfer.inventoryTransferId!}" text=transfer.inventoryTransferId! />
         <#-- <@displayLinkCell href="EditInventoryItem?inventoryItemId=${transfer.inventoryItemId!}" text=transfer.inventoryItemId! /> -->
           <@displayCell text=transfer.inventoryItemId! />
          <@displayCell text=transfer.productId! />
          <@displayCell text=transfer.internalName! />
          <@displayCell text=transfer.description! />
            <#-- <#if inventoryItem?exists && inventoryItem.inventoryItemTypeId.equals("NON_SERIAL_INV_ITEM")>
            <@displayCell text="${(inventoryItem.quantityOnHandTotal)!}" />
          <#elseif inventoryItem?exists && inventoryItem.inventoryItemTypeId.equals("SERIALIZED_INV_ITEM")>
            <@displayCell text=inventoryItem.serialNumber! />
          </#if> -->
          <@displayCell text="${transfer.quantityOnHandTotal!}" />
          <@displayCell text="${(transfer.facilityName)!}&nbsp;[${(transfer.facilityIdTo)!}]" />
          <@displayDateCell date=transfer.sendDate! />
          <#if !completeRequested ??>
            <td>
              <#if (transfer.statusId)?exists>
               <#--  <#assign transferStatus = transfer.getRelatedOneCache("StatusItem")?if_exists /> -->
                <@display text="${transfer.statusId}" />
              </#if>
            </td>
          </#if>
         <#if transfer.statusId != "IXF_CANCELLED" && transfer.statusId != "IXF_COMPLETE">
          
          <td align="center">
            <#if completeRequested??>
              <@inputHidden index=transfer_index name="inventoryTransferId" value=transfer.inventoryTransferId!/>
              <@inputHidden index=transfer_index name="inventoryItemId" value=transfer.inventoryItemId!/>
              <@inputHidden index=transfer_index name="statusId" value="IXF_COMPLETE"/>
              <@inputMultiCheck index=transfer_index/>
            <#else>
              <@displayLink href="TransferInventoryItem?inventoryTransferId=${transfer.inventoryTransferId!}" text=uiLabelMap.CommonEdit class="buttontext"/>
            </#if>
          </td>
          </#if>
        </tr>
        <#assign rowCount = transfer_index + 1/>
      </#list>

      <#if completeRequested??>
        <tr>
          <td colspan="8" align="right">
            <@inputHidden name="_rowCount" value="${rowCount}"/>
            <@inputHiddenUseRowSubmit />
            <@inputHidden name="forceComplete" value="${parameters.forceComplete?default(\"false\")}"/>
            <#if parameters.forceComplete?default("false") == "true">
              <@inputConfirm title=uiLabelMap.WarehouseForceComplete form="CompleteRequested" />
            <#else>
              <@inputSubmit title=uiLabelMap.ProductComplete />
            </#if>
          </td>
        </tr>
      </#if>
    </table>
  </@frameSection>
  <#if completeRequested??>
    </form>
  </#if>
</#if>

</@frameSection>
<script>
	function orders(orderBy){
		if(location.href.indexOf("DESC") == -1){
			orderBy = orderBy + " DESC"
		}
		location.href = "/warehouse/control/FindFacilityTransfers?facilityId="+facility_tran_002+"&orderBy="+orderBy
	}
</script>
<#else>
  ${uiLabelMap.OpentapsError_PermissionDenied}
</#if>