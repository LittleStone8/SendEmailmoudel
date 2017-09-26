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

<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />

<div class="chooseWarehouse_box">
	<#if facilities.size() != 0>
	   <form method="post" action="<@ofbizUrl>setFacility</@ofbizUrl>">
		   <div class="chooseWarehouse_list">
			   	 <select name="facilityId" class="selectBox">
			       <#list facilities ? sort_by(["facilityName"]) as facility>
			         <option value="${facility.facilityId}">${facility.facilityName}</option>
			       </#list>
			     </select>
			     <input type="submit" class="smallSubmit selectInput" value="${uiLabelMap.CommonSelect}"/>
		   </div>
	   </form>
	 </#if>
	 
	 <#if hasCreateWarehousePermission>
	   <div class="createWarehouse" style="margin-top:15px;">
	   	  <div class="createWarehouse_tip">* You can create a new warehouse if you want</div>
	   	  <div class="createWarehouse_operator"><a href="<@ofbizUrl>createWarehouseForm</@ofbizUrl>" class="tabletext">${uiLabelMap.WarehouseCreateNewWarehouse}</a></div>
	   </div>
	 </#if>
</div>
<style>
	.chooseWarehouse_box { background:#fff;padding:10px 10px 20px 10px; }
	.chooseWarehouse_box .chooseWarehouse_list { height:30px; }
	.chooseWarehouse_box .chooseWarehouse_list .selectBox { height:30px;line-height:30px;float:left;border:1px solid #8cc152;width:300px; }
	.chooseWarehouse_box .chooseWarehouse_list .selectInput{ height:30px !important;line-height:30px !important;background:#8cc152 !important;border:none !important;float:left;padding:0 10px !important;color:#fff !important; }
	.createWarehouse .createWarehouse_tip{ color:#e50038;font-size:14px;margin:20px 0 10px 0; }
	.createWarehouse .createWarehouse_operator a{ height:30px;line-height:30px;display:inline-block;padding:0 10px;background:#8cc152;color:#fff; }
</style>