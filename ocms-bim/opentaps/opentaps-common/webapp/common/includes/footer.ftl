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

<#-- This file is the common opentaps footer for every application that appears as a tab in OFBiz -->
</div>
</div>
<div id="yiwill_footer">
    <div class="copyright_content">
    <#if currenttime?has_content>
         <a href="ListTimezones" class="time_zones">
         <span>${currenttime}</span> - 
         <span>${ timeZoneId}</span>	</a>
    </#if>
        <p>All Right Reserved(c) 2016-2017 YIWILL - www.yiwill.com</p>
    </div>
</div>