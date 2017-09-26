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
<#if latestnews?exists>
<div class="rss-frame-section">
  <div class="gray-panel-header">Latest News</div>
  <div class="rss-frame-section-body" style="text-align: center;">
    <table width="98%" border="0" cellpadding="0" cellspacing="2">
            <tbody>
       <#list latestnews as news>
            <tr>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
        </#list>
            <tr>
              <td colspan="2" align="left">
                <span class="rss-tabletext">
                <a href="#" target="_blank">易为控股</a>
                </span>
                <br/>
              </td>
            </tr>
    </tbody></table>            
  </div>
</div>
 </#if> 
 