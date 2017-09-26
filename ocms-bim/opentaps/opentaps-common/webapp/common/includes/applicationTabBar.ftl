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
<#-- Copyright (c) Open Source Strategies, Inc. -->

<#--
 *  Copyright (c) 2003 The Open For Business Project - www.ofbiz.org
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 *  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 *  OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *@author     Leon Torres (leon@opensourcestrategies.com)
 *@version    $Id: $
-->

<#-- This file contains the tab bar for an opentaps section, which allows selection of a section. -->

<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>

<#if userLogin?exists && applicationSections?exists>
<ul class="sectionTabBar">
  <#list applicationSections as section>

    <#if section.isExternal?exists && "Y" == section.isExternal?upper_case>
      <#assign url=section.linkUrl + "?" + response.encodeURL(externalKeyParam)/>
    <#else>
      <#assign url="/" + opentapsApplicationName + "/control/" + section.linkUrl/>
    </#if>

    <#if sectionName?exists && section.tabId == sectionName>
      <#assign tabClass = "sectionTabButtonSelected"/>
    <#else>
      <#assign tabClass = "sectionTabButtonUnselected"/>
    </#if>
    
    <#if section.showAsDisabled()>
      <#assign title="<span class=\"disabled\">${uiLabelMap.get(section.uiLabel)}</span>" />
    <#else>
      <#assign title="<a href=\"${url}\">${uiLabelMap.get(section.uiLabel)}</a>" />
    </#if>
    <#if title ?contains("warehouse/control/myHomeMain") || title ?contains("warehouse/control/shippingMain")>
    
    <#else>
    <li class="${tabClass}"><@frameSectionHeader title=title/></li>
    </#if>
  </#list>
</ul>
<div class="sectionTabBorder"></div>
</#if>