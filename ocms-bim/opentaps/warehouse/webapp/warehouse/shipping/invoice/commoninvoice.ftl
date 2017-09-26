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

<#-- This file has been modified from the version included with the Apache licensed OFBiz product application -->
<#-- This file has been modified by Open Source Strategies, Inc. -->

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

<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>
<@import location="component://opentaps-common/webapp/common/order/infoMacros.ftl"/>
<fo:block  font-size="24pt">
<#if company?has_content>
${company.getString("groupName")?if_exists}
</#if>
</fo:block>
<fo:block font-size="22pt" margin-bottom="10pt">
<#if ProductStoretemp?has_content>
${ProductStoretemp.getString("storeName")?if_exists}
</#if>
</fo:block>


<fo:block font-size="20pt">
	<fo:inline>
		<#if buyerperson?has_content>
			Customer: ${buyerperson.getString("firstNameLocal")?if_exists}    
		</#if>
	</fo:inline>
	<fo:inline margin-left="20pt" display="inline-block">
		<#if saleperson?has_content>
			Sales: ${saleperson.getString("firstName")?if_exists} ${saleperson.getString("lastName")?if_exists}
		</#if>
	</fo:inline>
</fo:block>


<fo:block font-size="20pt">
<#if OrderHeaderr?has_content>
INVOICE: CI${OrderHeaderr.getString("orderId")?if_exists}    
</#if>

<#if OrderHeaderr?has_content>
Date: ${orderDate?if_exists}
</#if>
</fo:block>

<fo:block font-size="20pt"> 
-------------------------------------------------------------------
</fo:block>


<#list productlist as temp>
<fo:block font-size="14pt">
${temp.getString("quantity").replace(".000000", "")?if_exists}*${temp.getString("productId")?if_exists} ${temp.getString("itemDescription")?if_exists} ${temp.getString("currentPrice")?if_exists} | ${temp.getString("unitRecurringPrice")?if_exists} |  ${temp.getString("unitPrice")?if_exists} 
</fo:block>
</#list>
<fo:block font-size="20pt"  text-align="right">
Total: ${OrderHeaderr.getString("currencyUom")?if_exists}  ${total?if_exists} 
</fo:block>
<fo:block font-size="20pt">
------------------------------------------------------------------
</fo:block>

<fo:block font-size="20pt">
Payment
</fo:block>

<#if CASHpay!=0>

<fo:block font-size="18pt">
Cash Paid:${OrderHeaderr.getString("currencyUom")?if_exists} ${CASHpay?if_exists}
</fo:block>

<fo:block font-size="18pt">
Change:${OrderHeaderr.getString("currencyUom")?if_exists} ${CHANGE?if_exists}
</fo:block>
</#if>

<#if cardpay!=0>
<fo:block font-size="18pt">
Purchase on account:${OrderHeaderr.getString("currencyUom")?if_exists} ${cardpay?if_exists}
</fo:block>
</#if>






<fo:block font-size="20pt" margin-top="10pt">
VAT INCLUSIVE
</fo:block>

<fo:block font-size="20pt" margin-top="40pt">
Thank you,Please visit again.
</fo:block>

<fo:block font-size="20pt">
Please keep you till slip as proof of purchases
</fo:block>
<fo:block  margin-top="20pt" font-size="20pt">
 Goods sold are not returnable
</fo:block>
<#--
<#list retlist as temp>
   <fo:table table-layout="fixed" space-after.optimum="10pt">
    <fo:table-column/>
    <fo:table-column/>
    <fo:table-body>
          <fo:table-row>
        <fo:table-cell>
          <fo:block font-size="14pt">Tracking Code :${temp[0].trackingCode}</fo:block>
        </fo:table-cell>
        <fo:table-cell>
          <fo:block text-align="right">
            <fo:instream-foreign-object>
              <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns" message="${temp[0].trackingCode.replaceAll("EGATEE", "")}">
                <barcode:code39><barcode:height>12mm</barcode:height></barcode:code39>
              </barcode:barcode>
            </fo:instream-foreign-object>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
      <fo:table-row>
        <fo:table-cell>
          <fo:block font-size="14pt">${uiLabelMap.OrderOrder} :${orderId}</fo:block>
        </fo:table-cell>
        <fo:table-cell>
          <fo:block text-align="right">
            <fo:instream-foreign-object>
              <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns" message="${orderId}">
                <barcode:code39><barcode:height>8mm</barcode:height></barcode:code39>
              </barcode:barcode>
            </fo:instream-foreign-object>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
      
      <fo:table-row>
        <fo:table-cell>
          <fo:block font-size="14pt">ShipmentId :${temp[0].shipmentId}</fo:block>
        </fo:table-cell>
        <fo:table-cell>
          <fo:block text-align="right">
            <fo:instream-foreign-object>
              <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns" message="${temp[0].shipmentId}">
                <barcode:code39><barcode:height>8mm</barcode:height></barcode:code39>
              </barcode:barcode>
            </fo:instream-foreign-object>
          </fo:block>
        </fo:table-cell>
      </fo:table-row>
    </fo:table-body>
  </fo:table>
 
   <fo:table table-layout="fixed" space-after.optimum="10pt">
    <fo:table-column column-width="proportional-column-width(4)"/>
    <fo:table-column column-width="proportional-column-width(1)"/>
    <fo:table-column column-width="proportional-column-width(2)"/>
    <fo:table-body>
      <fo:table-row>
        <#assign rowsToSpan = 2/>
         <#if shipByDate?has_content>
          <#assign rowsToSpan = rowsToSpan + 1/>
        </#if>
        <#if billingWeight?has_content>
          <#assign rowsToSpan = rowsToSpan + 1/>
        </#if>
        
        <fo:table-cell number-rows-spanned="${rowsToSpan}">
        

        
        <#if address?exists>
          <#assign address = address>
          <fo:block>${uiLabelMap.CommonTo}:         <#if customerrr?has_content>
        <fo:block>Customer: ${customerrr.firstNameLocal?default("N/A")}</fo:block>
        </#if>${address.toName?if_exists}</fo:block>
          <#if address.attnName?has_content>
          <fo:block>${uiLabelMap.CommonAttn}: ${address.attnName?if_exists}</fo:block>
          </#if>
          <fo:block> ${address.address1?if_exists},${address.city?if_exists} </fo:block>
   		  <fo:block> ${address.countyGeoId?if_exists},${address.stateProvinceGeoId?if_exists}, ${address.countryGeoId?if_exists}  </fo:block>

          <#if phoneNumber?exists>
            <fo:block>${uiLabelMap.OpentapsPhoneNumber}: ${phoneNumber.contactNumber}</fo:block>
          </#if>
        <#else>
          <fo:block>${uiLabelMap.CommonTo}: ${uiLabelMap.OpentapsUnknown}</fo:block>
        </#if>
        <#if facilityyy?has_content>
        <fo:block>From: ${facilityyy.facilityName?default("N/A")}</fo:block>
        </#if>
        </fo:table-cell>
      </fo:table-row>
      <fo:table-row>
        <fo:table-cell><fo:block font-weight="bold">${uiLabelMap.OpentapsShipVia}</fo:block></fo:table-cell>
        <fo:table-cell><fo:block>${shipmentMethodTypeId} /${carrierPartyId}</fo:block></fo:table-cell>
      </fo:table-row>
      <#if shipByDate?has_content>
      <fo:table-row>
        <fo:table-cell><fo:block font-weight="bold">Ship Date</fo:block></fo:table-cell>
        <fo:table-cell><fo:block>${shipByDate?default("N/A")}</fo:block></fo:table-cell>
      </fo:table-row>
      </#if>
      
      
      
      
      
      <#if billingWeight?has_content>
      <fo:table-row>
        <fo:table-cell><fo:block font-weight="bold">Weight:</fo:block></fo:table-cell>
        <fo:table-cell><fo:block>${billingWeight?default("N/A")}/${billingWeightUomId?default("N/A")}</fo:block></fo:table-cell>
      </fo:table-row>
      </#if>
    </fo:table-body>
  </fo:table>
 <fo:block space-before="20pt" space-after="20pt">
</fo:block>
  <fo:table table-layout="fixed">
    <fo:table-column column-width="proportional-column-width(3)"/>
    <fo:table-column column-width="proportional-column-width(5)"/>
    <fo:table-column column-width="proportional-column-width(2)"/>

    <fo:table-header>
      <fo:table-row font-weight="bold">
        <fo:table-cell background-color="#D4D0C8" height="20pt" display-align="center" border-top-style="solid" border-bottom-style="solid">
          <fo:block>${uiLabelMap.ProductProduct}</fo:block>
        </fo:table-cell>
        <fo:table-cell background-color="#D4D0C8" height="20pt" display-align="center" border-top-style="solid" border-bottom-style="solid">
          <fo:block>${uiLabelMap.CommonDescription}</fo:block>
        </fo:table-cell>
        <fo:table-cell background-color="#D4D0C8" height="20pt" display-align="center" border-top-style="solid" border-bottom-style="solid">
          <fo:block>QTY</fo:block>
        </fo:table-cell>
      </fo:table-row>        
    </fo:table-header>
    <fo:table-body>

      <#list temp as line>
        <#if ((line_index % 2) == 0)>
          <#assign rowColor = "white">
        <#else>
          <#assign rowColor = "#CCCCCC">
        </#if>

      <fo:table-row>
        <fo:table-cell background-color="${rowColor}">
          <fo:block>${line.productId}</fo:block>
        </fo:table-cell>
        <fo:table-cell background-color="${rowColor}">
          <fo:block>${Static["org.opentaps.common.order.UtilOrder"].findfeaturesByproductid(delegator, line.productId)?if_exists}</fo:block>
        </fo:table-cell>
        <fo:table-cell background-color="${rowColor}">
          <fo:block>${line.quantity?if_exists}</fo:block>
        </fo:table-cell>
      </fo:table-row>
      </#list>

  </fo:table-body>
</fo:table>
 
 
 
 
 
 
 
 
 
 <fo:block font-size="14pt" text-align="right" margin-top="40pt">Signature:____________</fo:block>
   <#if temp_has_next><fo:block break-before="page"/>
   </#if>
</#list>
-->