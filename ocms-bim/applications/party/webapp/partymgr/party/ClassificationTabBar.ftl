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
<#-- This file has been modified by Open Source Strategies, Inc. -->

<#if security.hasEntityPermission("PARTYMGR", "_VIEW", session)>
  <#-- Main Heading -->
  <#if partyClassificationGroup?has_content>
    <#assign selected = tabButtonItem?default("void")>
    <div class="button-bar tab-bar">
      <ul>
        <li<#if selected == "EditPartyClassificationGroup"> class="selected"</#if>><a href="<@ofbizUrl>EditPartyClassificationGroup?partyClassificationGroupId=${partyClassificationGroup.partyClassificationGroupId}</@ofbizUrl>">${uiLabelMap.PartyClassificationGroups}</a></li>
        <li<#if selected == "EditPartyClassificationGroupParties"> class="selected"</#if>><a href="<@ofbizUrl>EditPartyClassificationGroupParties?partyClassificationGroupId=${partyClassificationGroup.partyClassificationGroupId}</@ofbizUrl>">${uiLabelMap.PartyParties}</a></li>
      </ul>
      <br class="clear"/>
    </div>
    <br />
  </#if>
<#else>
  <h2>${uiLabelMap.PartyMgrViewPermissionError}</h2>
</#if>