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
<#--/* @author: Michele Orru' (michele.orru@integratingweb.com) */-->


</div><#-- end of container-->


<script src=""

<script type="text/javascript" src="/commondefine/control/commloginCheck?externalLoginKey=<#if externalLoginKey?exists>${externalLoginKey}</#if>"></script>

<div id="footer">
<#if currenttime?has_content>
         <a href="ListTimezones" class="time_zones">
         <span>${currenttime}</span> - 
         <span>${ timeZoneId}</span>	</a>
    </#if>
  <div id="appDescr">YIWILL - www.yiwill.com</div>
</div>

</body>
</html>
