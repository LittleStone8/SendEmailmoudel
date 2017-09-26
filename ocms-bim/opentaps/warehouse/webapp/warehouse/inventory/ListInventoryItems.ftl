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
<#if security.hasEntityPermission("WRHS", "_INV_FII", session)>
<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>

<input id="orderBy001" type="hidden"/>
<table class="listTable">
  <tbody>
    <tr class="listTableHeader">
      <td><a href="javascript:orders('INVENTORY_ITEM_ID')">${uiLabelMap.ProductInventoryItemId}</a></td>
  <#--   <td>${uiLabelMap.ProductLocation}</td> -->  
      <td><a href="javascript:orders('product_Id')">${uiLabelMap.ProductProductId}</a></td>
      <td><a href="javascript:orders('INTERNAL_NAME')">Model</a></td>
      <td><a href="javascript:orders('description')">Product Attribute</a></td>
      <td><a href="javascript:orders('SERIAL_NUMBER')">${uiLabelMap.ProductSerialNumber}</a></td>
      <td><a href="javascript:orders('LOT_ID')">${uiLabelMap.ProductLotId}</a></td>
      <td><a href="javascript:orders('DATETIME_RECEIVED')">${uiLabelMap.ProductDateReceived}</a></td>
      <td><a href="javascript:orders('STATUS_ID')">${uiLabelMap.CommonStatus}</a></td>
      <td><a href="javascript:orders('QUANTITY_ON_HAND_TOTAL')">${uiLabelMap.WarehouseQuantityQTY}</a></td>
      <td><a href="javascript:void(0)">Inventory Facility</a></td>
      <td><a href="javascript:void(0)">Inventory Log</a></td>
    </tr>
    <#if inventoryItems?has_content>
      <#list inventoryItems as item>
        <tr class="${tableRowClass(item_index)}">
        <#if security.hasPermission("WRHS_INV_EDIT", session)>
           <@displayLinkCell href="EditInventoryItem?inventoryItemId=${item.inventoryItemId}" text=item.inventoryItemId />
        <#else>
          <@displayCell text=item.inventoryItemId! />
        </#if>
      <#--  <@displayLinkCell href="findInventoryItem?locationSeqId=${item.locationSeqId!}&amp;performFind=Y" text=item.locationSeqId! />  -->  
          <@displayLinkCell href="findInventoryItem?productId=${item.productId}&amp;performFind=Y" text=item.productId />
          <@displayCell text=item.internalName! />
          <@displayCell text=item.description! />
          <@displayCell text=item.serialNumber! />
          <@displayCell text=item.lotId! />
          <@displayDateCell date=item.datetimeReceived! />
          <@displayCell text=item.statusId! />
          <@displayCell text="${item.quantityOnHandTotal!}" />
          <@displayCell text=selectFacilityName! />
          <@displayLinkCell href="inventoryLog?inventoryItemId=${item.inventoryItemId}" text='Log' />
        </tr>
        <#-- display accounting tags associated with this item -->
        <#if tagTypesPerOrg?has_content>
          <#assign tagTypes = tagTypesPerOrg.get(item.ownerPartyId) />
          <#if tagTypes?has_content>
          <#assign hasSetAccountingTags = Static["org.opentaps.common.util.UtilAccountingTags"].hasSetAccountingTags(item,tagTypes)>
           <#if hasSetAccountingTags>
            <tr class="${tableRowClass(item_index)}">
              <td colspan="11">
                <i><@accountingTagsDisplay tags=tagTypes entity=item /></i>
              </td>
            </tr>
           </#if>
          </#if>
        </#if>
      </#list>
    </#if>
  </tbody>
</table>
<script>
function orders(orderBy){
	if(location.href.indexOf("DESC") == -1){
		orderBy = orderBy + " DESC"
	}
	var productId= jQuery("#productId").val()
	var internalName= jQuery("#internalName").val()
	var serialNumber= jQuery("#serialNumber").val()
	var lotId= jQuery("input[name='lotId']").val()
	var facilityIdToFind = jQuery("#inventoryFacility").val()
	location.href="/warehouse/control/findInventoryItem?productId="+productId+"&internalName="+internalName+"&serialNumber="+serialNumber+"&lotId="+lotId+"&orderBy="+orderBy+"&facilityIdToFind="+facilityIdToFind;
}
	
</script>
</#if>