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

<div class="navigation-header">
 <ul class="tabs">
 </ul>
</div>
<div class="sectionTabBorder"></div>
<script type="text/javascript">
    var baseUrl = location.pathname;
        baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf('/')); 
        storeHash = location.search.indexOf('externalLoginKey')>=0 ? '': location.search;
    var secondCategory = {
    	'catelog':[{
    		'key': '${uiLabelMap.CategoryManagement}',
    		'url': baseUrl + '/main'
    	    },{
    		'key': '${uiLabelMap.Feature}',
    		'url': baseUrl + '/EditFeatureCategories'
    	    },{
    		'key': '${uiLabelMap.PriceRules}',
    		'url': baseUrl + '/FindProductPriceRules'
    	    },{
    		'key': '${uiLabelMap.ShippingMethods}',
    		'url': baseUrl + '/ListShipmentMethodTypes'
    	    },{
    	    'key': '${uiLabelMap.Classfication}',
    		'url': baseUrl + '/Classfication'
    	    },{
        	'key': '${uiLabelMap.CreateProduct}',
        	'url': baseUrl + '/CreateProduct'
        	}
    	],
    	'partymgr':[{
    		'key':'${uiLabelMap.Home}',
    		'url': baseUrl + '/main'
    	    },{
            'key':'${uiLabelMap.Party}',
    		'url': baseUrl + '/findparty'
    	    },{
            'key':'${uiLabelMap.MyCcommunication}',
    		'url': baseUrl + '/MyCommunicationEvents'
    	    },{
    		'key':'${uiLabelMap.Communication}',
    		'url': baseUrl + '/FindCommunicationEvents'
    	    },{
    		'key':'${uiLabelMap.Visits}',
    		'url': baseUrl + '/showvisits'
    	    },{
    		'key':'${uiLabelMap.Classify}',
    		'url': baseUrl + '/showclassgroups'
    	    },{
    		'key':'${uiLabelMap.Security}',
    		'url': baseUrl + '/FindSecurityGroup'
    	    },{
    		'key':'${uiLabelMap.AddressMatchMap}',
    		'url': baseUrl + '/addressMatchMap'
    	    },{
    		'key':'${uiLabelMap.Invitation}',
    		'url': baseUrl + '/partyInvitation'
    	    }],	
    	'Store':[{
            'key':'${uiLabelMap.Store}',
    		'url': baseUrl + '/main'       
    	},{
            'key':'${uiLabelMap.Role}',
    		'url': baseUrl + '/FindProductStoreRoles'+storeHash
    	},{
            'key':'${uiLabelMap.Promotion}',
    		'url': baseUrl + '/EditProductStorePromos'+storeHash
    	},{
            'key':'${uiLabelMap.Catalog}',
    		'url': baseUrl + '/EditProductStoreCatalogs'+storeHash
    	},{
            'key':'${uiLabelMap.Site}',
    		'url': baseUrl + '/EditProductStoreWebSites'+storeHash 
    	},{
            'key':'${uiLabelMap.Shipment}',
    		'url': baseUrl + '/EditProductStoreShipSetup'+storeHash
    	},{
            'key':'${uiLabelMap.ScanningEstimate}',
    		'url': baseUrl + '/EditProductStoreShipmentCostEstimates'+storeHash
    	},{
            'key':'${uiLabelMap.Pay}',
    		'url': baseUrl + '/EditProductStorePaySetup'+storeHash
    	},{
            'key':'${uiLabelMap.FinancialAccount}',
    		'url': baseUrl + '/EditProductStoreFinAccountSettings'+storeHash
    	},{
            'key':'${uiLabelMap.Email}',
    		'url': baseUrl + '/EditProductStoreEmails'+storeHash
    	},{
            'key':'${uiLabelMap.Replacement}',
    		'url': baseUrl + '/editProductStoreKeywordOvrd'+storeHash
    	},{
            'key':'${uiLabelMap.Segement}',
    		'url': baseUrl + '/ViewProductStoreSegments'+storeHash
    	},{
            'key':'${uiLabelMap.DealerPayment}',
    		'url': baseUrl + '/EditProductStoreVendorPayments'+storeHash
    	},{
            'key':'${uiLabelMap.DealerShippments}',
    		'url': baseUrl + '/EditProductStoreVendorShipments'+storeHash
    	}
    	]    
    } 



    document.observe('dom:loaded', function() {
    	var dom = document.getElementsByClassName('navigation-header')[0];
    	var ulDom = dom.getElementsByClassName('tabs')[0];
        if(baseUrl.indexOf('catalog')>=0){
        	var html = '', items = null;
        	items = secondCategory['catelog'];
        	for(var ind = 0;ind<items.length;ind++){
        		var className = location.pathname.indexOf(items[ind]['url'])>=0?'select':'';       		
                html += '<li class='+className+'><a href="'+items[ind]['url']+'">'+items[ind]['key']+'</a></li>';
        	}
        	ulDom.innerHTML = html;
        	return;

        }
        if(baseUrl.indexOf('partymgr')>=0){
        	var html = '', items = null;
        	items = secondCategory['partymgr'];
        	for(var ind=0;ind<items.length;ind++){
        		var className = location.pathname.indexOf(items[ind]['url'])>=0?'select':'';
        	    html += '<li class='+className+'><a href="'+items[ind]['url']+'">'+items[ind]['key']+'</a></li>';
        	}
        	ulDom.innerHTML = html;
        	return;
        }
        if(baseUrl.indexOf('Store')>=0){
        	var html = '', items = null;
        	items = secondCategory['Store'];
        	for(var ind = 0 ;ind<items.length;ind++){
        		var className = location.pathname.indexOf(items[ind]['url'])>=0?'select':'';
        	    html += '<li class='+className+'><a href="'+items[ind]['url']+'">'+items[ind]['key']+'</a></li>';
        	}
        	ulDom.innerHTML = html;
        	return;
        }  
    });
</script>


