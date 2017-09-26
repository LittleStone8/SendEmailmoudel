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
<script src="/commondefine_js/jquery.js"></script> 

<#-- chenshihua		2017-4-7 -->
<script>
	
	function findPriceUOM(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findPriceUOM',
	        data: {
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	function newUpdatePassword(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/partymgr/control/newUpdatePassword',
	        data: {
				userLoginId:'jonsen',
				partyId:'10220',
				currentPassword:'222222',
				newPassword:'111111',
				newPasswordVerify:'111111',
				passwordHint:'123',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	
	
	
	
	
	function newCreateProduct(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/newCreateProduct',
	        data: {
	        	productId:'xmen-a',
				productTypeId:'FINISHED_GOOD',
				internalName:'XMen-1',
				productName:'xxx',
					},
	        success: function (data) {
	        	alert("成功新增产品");
	       	}	
    	});
	}
	<#-- 
	function createPersonContact(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/wpos/control/createPersonContact',
	        data: {
	        	firstNameLocal:'jjude',
	        	address1:'sfdfhfg',
	        	primaryPhoneNumber:'13615544614',
					},
	        success: function (data) {
	       	}	
    	});
	}
	  -->
	

	
	function findAllProductFeatureType(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/findAllProductFeatureType',
	        data: {},
	        success: function (data) {
	        	alert("查询所有的ProductFeatureType成功");
	       	}	
    	});
	}
	
	
	
	
	function findGeo(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findGeo',
	        data: {
	        	geoId : 'USA',
	        },
	        success: function (data) {
	       	}	
    	});
	}
	
	function LookupAutoCompleteByName(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/LookupAutoCompleteByName',
	        data: {
	        	name : 'w',
	        },
	        success: function (data) {
	       	}	
    	});
	}
	function LookupAutoCompleteByTelecom(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/LookupAutoCompleteByTelecom',
	        data: {
	        	telecomNumber : '1',
	        },
	        success: function (data) {
	       	}	
    	});
	}
	function LookupAutoComplete(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/LookupAutoComplete',
	        data: {
	        	param : '1',
	        },
	        success: function (data) {
	       	}	
    	});
	}
	
	<#-- 创建产品特"productFeatureType_description='颜色'或"'尺寸' -->
	function newCreateProductFeatureType(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/newCreateProductFeatureType',
	        data: {
	        	productFeatureType_description:'abbc',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	 
	 <#-- 查询颜色下的所有颜"	productFeatureTypeId='COLOR' -->
	function newFindProduceFeatur(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/newFindProduceFeatur',
	        data: {
	        	productFeatureTypeId:'COLOR',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	
	
	
	<#-- "Produce_Featur 添加产品的特"  	froduceFeatur_description='白色'		froduceFeatur_description='50cm'-->
	function newAddProduceFeatur(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/newAddProduceFeatur',
	        data: {
	        	froduceFeatur_description:'灰色',
	        	productFeatureTypeId:'COLOR',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	
	
	function deleteProductFeatureType(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/deleteProductFeatureType',
	        data: {
	        	productFeatureTypeId:'VVVVVVV',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	
	<#-- 在product_feature_apll表中添加产品特性关"
		productFeatureId：颜色的id
	  -->
	function newAddProduceFeaturAppl(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/newAddProduceFeaturAppl',
	        data: {
	        	<#-- 这里的数据必须是数据库里有的数据 -->
				productId:'xmen',
				<#-- productFeatureId：产品特性ID -->
				productFeatureId:'10003',
				<#--  
				productFeatureApplTypeId:'SELECTABLE_FEATURE',
				-->
					},
	        success: function (data) {
	        	
	        	alert("新添加特性成");
	       	}	
    	});
	}
	
	
	
	function newFindProduceFeaturAppl(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/newFindProduceFeaturAppl',
	        data: {
	        	froduceFeatur_description:'五颜六色',
	        	productFeatureTypeId:'COLOR',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	
	
	
	
	<#-- 跟新UpdateProduct
		参数：productId	productTypeId	internalName	productName
		
	  -->
	function newAddProduct(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/newAddProduct',
	        data: {
	        	productId:'lemon-02',
	        	productTypeId:'FINISHED_GOOD',
	        	internalName:'shamateshamate',
	        	productName:'shamateshamate',
	        	productCategoryId:'分类一',
	        },
	        success: function (data) {
	        	alert("添加ProductFeatureType成功");
	       	}	
    	});
	}
	
	
	
	function newFindProductById(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/newFindProductById',
	        data: {
	        	productId:'lemon',
	        },
	        success: function (data) {
	        	alert("查询product成功");
	       	}	
    	});
	}
	
	
	<#-- 
		* 插入或者跟新产品变"	 	* 需要参数：	productId 产品标示		productTypeId产品类型	internalName名称型号	productName品牌名称		
		 * 			productCategoryId第三个分类id	productCategoryId属性id	productFeatureId':['10002','10003']属性值ID
		 map
		 
		 
		var featureList = [{'':'红色','':'','':''},{'':'','':''}];
		 
		
	  -->
	
	function addVariantProduct(){
	alert("123546");
		var productList =[{'productId':'lemon','productTypeId':'FINISHED_GOOD',
							'internalName':'柠檬-111',
							'productName':'柠檬-8888',
							'productCategoryId':'分类一',
							'productFeatureId':['10002','10003']},
							
						 {'productId':'lemon','productTypeId':'FINISHED_GOOD',
							'internalName':'柠檬-6666',
							'productName':'柠檬-55555',
							'productCategoryId':'分类一',
							'productFeatureId':['10001','10004']}];
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/addVariantProduct',
	        data: JSON.stringify({
	        	productList:productList,
					}),
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	
	
	
	
	
	
	
	function anotherAddVariantProduct(){
		var productList =[{
							'productId':'lemon',
							'productTypeId':'FINISHED_GOOD',
							'internalName':'柠檬-6666',
							'productName':'柠檬-55555',
							'productCategoryId':'分类一'
						
						  }];
	
		var productFeature = [{'COLOR':['10001','10004','10003']},{'TYPE':['3011','3010']}];
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/anotherAddVariantProduct',
	        data: JSON.stringify({
	        	productList:productList,
	        	productFeature:productFeature,
					}),
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	
	
	
	
	function newFindPrfindAllProductFeatureTypeAndProduceFeaturApploductById(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/findAllProductFeatureTypeAndProduceFeaturAppl',
	        data: {       
	        },
	        success: function (data) {
	        	alert("查询product成功");
	       	}	
    	});
	}
 
	function findTableHead(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findTableHead',
	        data: {    
	        	productId :'123', 
	        },
	        success: function (data) {
	        	alert("查询成功");
	       	}	
    	});
	}
	
	
	function findAllcheckedProductPrice(){
		var productList = ["GZ-1001","GZ-1004","lemon"];
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/findAllcheckedProductPrice',
	        data: JSON.stringify({
	        	productList : productList,   
					}),
	        success: function (data) {
	        	alert("查询product成功");
	       	}	
    	});
	}
	function findAllcheckedProductPrice(){
		var productList = ["GZ-1001","GZ-1004","lemon"];
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/findAllcheckedProductPrice',
	        data: JSON.stringify({
	        	productList : productList,   
					}),
	        success: function (data) {
	        	alert("查询product成功");
	       	}	
    	});
	}
	
	
	
	
	function findProductAndCategory(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findProductAndCategory',
	        data: {
	        	productId:'GZ-1001',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	
	function skuFindParentProductAndCategory(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/skuFindParentProductAndCategory',
	        data: {
	        	productId:'123-4',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	function findProductNameById(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findProductNameById',
	        data: {
	        	productId:'10524',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	function createProductAndGotoPrice(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/createProductAndGotoPrice',
	        data: {
	        <#--
	        	String productId = request.getParameter("productId");
			String productTypeId = request.getParameter("productTypeId");
			String internalName = request.getParameter("internalName");
			String productName = request.getParameter("internalName");
			String brandName = request.getParameter("brandName");
			String userLoginId = userLogin.getString("userLoginId");
			String productCategoryId = request.getParameter("productCategoryId");
			SPECIAL_COST_PRICE
	          -->
	        	productId:'10760',
	        	productTypeId:'FINISHED_GOOD',
	        	internalName:'PPPPPPP',
	        	brandName:'PPPPPPPPPP',
	        	productCategoryId:'egatee_020101',
	        	
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	function deleteSKU(){
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/deleteSKU',
	        data: {
	        	productSKUId:'00482-7',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	function findSaleChannel(){
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findSaleChannel',
	        data: {
	        	
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	function findsalechannleByStoreId(){
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/ordermgr/control/findsalechannleByStoreId',
	        data: {
	        	productStoreId : '10004', 
					},
	        success: function (data) {
	       	}	
    	});
	}
	
	
	
	function findAllProduct(){
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	         contentType: "application/json;charset=utf-8",
	        url: '/ordermgr/control/findAllProduct',
	        data: {
	        	
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
	function new_getAutoCompleteClientPartyIds(){
	
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	         contentType: "application/json;charset=utf-8",
	        url: '/ordermgr/control/new_getAutoCompleteClientPartyIds',
	        data: {
	        	
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	
	
</script>



<script>

	<#-- 
		参数：productStoreGroupId		以下五个固定的价"		AGENT_CHANNEL_PRICE 零售渠道"	 	B2R	B2R渠道"	 	DEFAULT_COST_PRICE B2R渠道"	  	RETAIL_PRICE 代理渠道"	  	WHOLESALE_CHANNELS_P 批发渠道"	  	
	  	
	  	'fromDate':'1491301327000'，返回来
	  	'currencyUomId':'USD',这是货币
	  	'productPriceTypeId':'DEFAULT_PRICE'"productPricePurposeId':'PURCHASE'	这两个参数返回来就行
	  	'termUomId':null,'customPriceCalcService':null这两个也是固定返回来就行
	  	
	    -->
	
	    


	var priceList =[{'productId':'110033-1','productPriceTypeId':'DEFAULT_PRICE',
				'productPricePurposeId':'PURCHASE','currencyUomId':'USD',
				'productStoreGroupId':'_NA_','fromDate':'1492085886000',
				'price':'44444444','termUomId':null,'customPriceCalcService':null
			},
			{'productId':'110033-1','productPriceTypeId':'DEFAULT_PRICE',
				'productPricePurposeId':'PURCHASE','currencyUomId':'USD',
				'productStoreGroupId':'RETAIL_COST_PRICE','fromDate':'1492085886000',
				'price':'55555','termUomId':null,'customPriceCalcService':null
			},
			{'productId':'110033-1','productPriceTypeId':'DEFAULT_PRICE',
				'productPricePurposeId':'PURCHASE','currencyUomId':'USD',
				'productStoreGroupId':'EGATEE_COST_PRICE','fromDate':'1492085886000',
				'price':'66666','termUomId':null,'customPriceCalcService':null
			}
			
			];
	function oneKeyUpdateAllProductPrice(){
	alert("12356");
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/oneKeyUpdateAllProductPrice',
	        data: JSON.stringify({
	        	productPriceList:priceList,
					}),
	        success: function (data) {
	        	
	       	}	
    	});
    	
    
	}
	
	
	
	
	
	
	function newCreateProductPrice(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/newCreateProductPrice',
	        data: {
	        	productId:'GZ-1001',
				productPriceTypeId:'DEFAULT_PRICE',
				productPricePurposeId:'PURCHASE',
				currencyUomId:'USD',
				productStoreGroupId:'_NA_',
				price:'12',
				termUomId:'',
				customPriceCalcService:'',
					},
	        success: function (data) {
	       	}	
    	});
	}
	
	
	
	
	
	
	
	
	
	
	
	function findAllProductFeature(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findAllProductFeature',
	        data: {
	        	productId:'123',
					},
	        success: function (data) {
	        	
	       	}	
    	});
	}
	

</script>

<div class="screenlet-body">
<form method="post" action="/catalog/control/createProductPriceTest" id="AddProductPrice" class="basic-form" onsubmit="javascript:(this)" name="AddProductPrice">
    <input type="hidden" name="productId" value="GZ-1001" id="AddProductPrice_productId">
<div class="fieldgroup" id="_G5_">
<div class="fieldgroup-title-bar">
 </div>
<div id="_G5__body" class="fieldgroup-body">
    <table cellspacing="0" class="basic-table">
    <tbody><tr>
    <td class="label">价格类型    </td>
    <td>
		<select name="productPriceTypeId" id="AddProductPrice_productPriceTypeId" size="1">
 			<option value="AVERAGE_COST">平均花费</option> <option value="BOX_PRICE">Box Price</option> <option value="COMPETITIVE_PRICE">竞争对手价格</option> <option selected="selected" value="DEFAULT_PRICE">缺省价格</option> <option value="LIST_PRICE">标价</option> <option value="MAXIMUM_PRICE">最高价</option> <option value="MIN_ADV_PRICE">Minimum Advertised Price</option> <option value="MINIMUM_ORDER_PRICE">Minimum Order Price</option> <option value="MINIMUM_PRICE">最低价</option> <option value="PROMO_PRICE">促销"/option> <option value="SPECIAL_PROMO_PRICE">特别促销"/option> <option value="WHOLESALE_PRICE">总代理价</option></select>
    </td>
    </tr>
    
    <tr>
    <td class="label">货币币种标识    </td>
    <td>
		<select name="currencyUomId" id="AddProductPrice_currencyUomId" size="1">
 			<option value="AFA">阿富汗尼 - AFA</option> <option value="ALL">阿尔巴尼亚列"- ALL</option> <option value="DZD">阿尔及利亚第纳尔 - DZD</option> <option value="ADP">安道尔比塞塔 - ADP</option> <option value="AOK">安哥拉宽"- AOK</option> <option value="ARS">阿根廷比"- ARS</option> <option value="ARA">阿根廷奥斯特罗尔 - ARA</option> <option value="AMD">亚美尼亚打兰 - AMD</option> <option value="AWG">阿鲁巴盾 - AWG</option> <option value="AUD">澳大利亚"- AUD</option> <option value="AZM">阿塞拜疆曼纳"- AZM</option> <option value="BSD">巴哈马镑 - BSD</option> <option value="BHD">巴林第纳"- BHD</option> <option value="BDT">孟加拉塔"- BDT</option> <option value="BBD">巴巴多斯"- BBD</option> <option value="BZD">洪都拉斯"- BZD</option> <option value="BYR">白俄罗斯卢布 - BYR</option> <option value="XOF">贝宁法郎 - XOF</option> <option value="BMD">百慕大元 - BMD</option> <option value="BOB">玻利维亚玻利维亚"- BOB</option> <option value="BAD">波斯尼亚和黑塞哥维那第纳"- BAD</option> <option value="BWP">博茨瓦纳普拉 - BWP</option> <option value="BRR">巴西 - BRR</option> <option value="BRL">巴西里尔 - BRL</option> <option value="GBP">英镑 - GBP</option> <option value="BND">文莱"- BND</option> <option value="BGN">保加利亚列弗 - BGN</option> <option value="BIF">布隆迪法"- BIF</option> <option value="KHR">柬埔寨里"- KHR</option> <option value="CAD">加拿大元 - CAD</option> <option value="CVE">佛得角埃斯库"- CVE</option> <option value="KYD">开曼元 - KYD</option> <option value="CLP">智利比索 - CLP</option> <option value="CNY">中国 - CNY</option> <option value="COP">哥伦比亚比索 - COP</option> <option value="KMF">科摩罗法"- KMF</option> <option value="CRC">哥斯达黎"- CRC</option> <option value="HRD">克罗地亚第纳尔元 - HRD</option> <option value="CUP">古巴比索 - CUP</option> <option value="CYP">塞普路斯"- CYP</option> <option value="CZK">捷克克郎 - CZK</option> <option value="DKK">丹麦克郎 - DKK</option> <option value="DJF">吉布提法"- DJF</option> <option value="DOP">多米加比"- DOP</option> <option value="DRP">多米尼加共和国比"- DRP</option> <option value="XCD">东加勒比"- XCD</option> <option value="ECS">厄瓜多尔苏克"- ECS</option> <option value="EGP">埃及"- EGP</option> <option value="SVC">萨尔瓦多科朗 - SVC</option> <option value="EEK">爱沙尼亚克郎 - EEK</option> <option value="ETB">埃塞俄比亚比"- ETB</option> <option value="EUR">欧元 - EUR</option> <option value="FKP">马尔维纳斯镑 - FKP</option> <option value="FJD">斐济"- FJD</option> <option value="XAF">加蓬法郎 - XAF</option> <option value="GMD">冈比亚法拉西 - GMD</option> <option value="GEK">乔治亚库"- GEK</option> <option value="GHC">加纳塞第 - GHC</option> <option value="GIP">直布罗陀"- GIP</option> <option value="GTQ">危地马拉格查"- GTQ</option> <option value="GNF">几内亚法"- GNF</option> <option value="GWP">几内亚比"- GWP</option> <option value="GYD">圭亚那元 - GYD</option> <option value="HTG">海地古德 - HTG</option> <option value="HNL">洪都拉斯伦皮"- HNL</option> <option value="HKD">港元 - HKD</option> <option value="HUF">匈牙利福"- HUF</option> <option value="ISK">冰岛克郎 - ISK</option> <option value="IDR">印尼"- IDR</option> <option value="INR">印度卢比 - INR</option> <option value="IRR">伊朗里亚"- IRR</option> <option value="IQD">伊拉克第纳尔 - IQD</option> <option value="ILS">以色列谢克尔 - ILS</option> <option value="JMD">牙买加元 - JMD</option> <option value="JPY">日元 - JPY</option> <option value="JOD">约旦第纳"- JOD</option> <option value="KZT">哈萨克斯坦坚"- KZT</option> <option value="KES">肯尼亚先"- KES</option> <option value="KIS">吉尔吉斯斯坦索姆 - KIS</option> <option value="KWD">科威特第纳尔 - KWD</option> <option value="LAK">老挝基普 - LAK</option> <option value="LVL">拉脫维亚拉特 - LVL</option> <option value="LBP">黎巴嫩镑 - LBP</option> <option value="SLL">塞拉里昂 - SLL</option> <option value="LSL">莱索托洛"- LSL</option> <option value="LRD">利比里亚"- LRD</option> <option value="LYD">利比亚第纳尔 - LYD</option> <option value="LTL">立陶宛利"- LTL</option> <option value="MOP">澳门"- MOP</option> <option value="MGF">马达加斯加法"- MGF</option> <option value="MWK">马拉维克瓦查 - MWK</option> <option value="MYR">马来西亚林吉"- MYR</option> <option value="MVR">马尔代夫卢非"- MVR</option> <option value="MTL">马耳他里拉 - MTL</option> <option value="MRO">毛里塔尼亚乌吉亚 - MRO</option> <option value="MUR">毛里求斯卢比 - MUR</option> <option value="MXN">墨西哥比"" - MXN</option> <option value="MXP">墨西哥比"" - MXP</option> <option value="MDL">摩尔多瓦列伊 - MDL</option> <option value="MNT">蒙古图格里克 - MNT</option> <option value="MAD">摩洛哥迪拉姆 - MAD</option> <option value="MZM">莫桑比克美提"- MZM</option> <option value="NPR">尼泊尔卢"- NPR</option> <option value="NIS">新以色列谢克"- NIS</option> <option value="TWD">新台"- TWD</option> <option value="NZD">新西兰元 - NZD</option> <option value="NIC">尼加拉瓜 - NIC</option> <option value="NIO">尼加拉瓜科多"- NIO</option> <option value="NGN">尼日利亚奈拉 - NGN</option> <option value="KPW">朝鲜"- KPW</option> <option value="NOK">挪威克郎 - NOK</option> <option value="OMR">阿曼里亚"- OMR</option> <option value="PKR">巴基斯坦卢比 - PKR</option> <option value="PAB">巴拿马巴波亚 - PAB</option> <option value="PGK">巴布亚新几内亚基"- PGK</option> <option value="PYG">巴拉圭瓜拉尼 - PYG</option> <option value="SOL">秘鲁 - SOL</option> <option value="PEI">秘鲁因蒂 - PEI</option> <option value="PES">秘鲁索尔 - PES</option> <option value="PEN">秘鲁新索"- PEN</option> <option value="PHP">菲律宾比"- PHP</option> <option value="PLZ">波兰 - PLZ</option> <option value="PLN">波兰兹罗"- PLN</option> <option value="QAR">卡塔尔里亚尔 - QAR</option> <option value="ROL">罗马尼亚列伊 - ROL</option> <option value="RUR">俄罗斯卢"- RUR</option> <option value="SUR">俄罗斯卢"" - SUR</option> <option value="RWF">卢旺达法"- RWF</option> <option value="WST">萨摩亚塔"- WST</option> <option value="CDP">桑托多明"- CDP</option> <option value="STD">圣多美和普林西比多布"- STD</option> <option value="SAR">沙特里亚"- SAR</option> <option value="SCR">塞舌尔卢"- SCR</option> <option value="SGD">新加坡元 - SGD</option> <option value="SBD">所罗门群岛"- SBD</option> <option value="SOS">索马里先"- SOS</option> <option value="ZAR">南非兰特 - ZAR</option> <option value="KRW">韩国"- KRW</option> <option value="LKR">斯里兰卡卢比 - LKR</option> <option value="SHP">圣赫勒拿群岛"- SHP</option> <option value="SDP">苏丹"- SDP</option> <option value="SRG">苏里南盾 - SRG</option> <option value="SZL">斯威士兰埃马兰吉"- SZL</option> <option value="SEK">瑞典克郎 - SEK</option> <option value="CHF">瑞士法郎 - CHF</option> <option value="SYP">叙利亚镑 - SYP</option> <option value="TJR">塔吉克斯坦卢"- TJR</option> <option value="TZS">坦桑尼亚先令 - TZS</option> <option value="THB">泰铢 - THB</option> <option value="TPE">帝汶埃斯库多 - TPE</option> <option value="TOP">汤加 - TOP</option> <option value="TTD">特立尼达和多巴哥"- TTD</option> <option value="TND">突尼斯第纳尔 - TND</option> <option value="TRY">土耳其里拉 - TRY</option> <option value="TMM">土库曼斯坦马纳特 - TMM</option> <option value="UGS">乌干达先"- UGS</option> <option value="UAH">乌克兰格里夫"- UAH</option> <option value="AED">阿联酋迪拉姆 - AED</option> <option selected="selected" value="USD">美元 - USD</option> <option value="UYU">乌拉"- UYU</option> <option value="UYP">乌拉圭新比索 - UYP</option> <option value="VUV">瓦努阿图瓦图 - VUV</option> <option value="VEB">委内瑞拉博利"- VEB</option> <option value="VND">越南"- VND</option> <option value="ANG">安第列斯群岛"- ANG</option> <option value="YER">也门里亚"- YER</option> <option value="ZRZ">扎伊"- ZRZ</option> <option value="ZMK">赞比亚克瓦查 - ZMK</option> <option value="ZWD">津巴布韦"- ZWD</option></select>
    </td>
    </tr>
    
    <tr>
    <td class="label">价格    </td>
    <td>
		<input type="text" name="price" size="25" id="AddProductPrice_price">
    </td>
	</tr>
    
  
    
   <tr>
   <td> 
   <input type="hidden">
    </td>
  
    <td colspan="4">
    
		<input type="button"  name="button" Onclick="newCreateProduct()" value="新建产品">
		<input type="button"  name="button" Onclick="newApplyFeatureToProduct()" value="添加产品特"这个按钮没用)">
		<input type="button"  name="button" Onclick="findAllProductFeatureType()" value="查询findAllProductFeatureType">
		<input type="button"  name="button" Onclick="newCreateProductFeatureType()" value="创建产品特性createProductFeatureType">
		<input type="button"  name="button" Onclick="newFindFroduceFeatur()" value="查找产品特性newFindFroduceFeatur">
		<input type="button"  name="button" Onclick="newAddProduceFeatur()" value="新建插入产品特">
		<input type="button"  name="button" Onclick="newAddProduceFeaturAppl()" value="添加产品特">
		<input type="button"  name="button" Onclick="newFindProduceFeaturAppl()" value="通过productId查询产品特">
		<input type="button"  name="button" Onclick="newAddProduct()" value="添加产品">
		<input type="button"  name="button" Onclick="newFindProductById()" value="查询产品newFindProductById">
		<input type="button"  name="button" Onclick="addVariantProduct()" value="添加变型产品addVariantProduct">
		
		<input type="button"  name="button" Onclick="newFindPrfindAllProductFeatureTypeAndProduceFeaturApploductById()" value="查询所有的特">
		
		<input type="button"  name="button" Onclick="findAllcheckedProductPrice()" value="查询所有产品的所有价">
		<input type="button"  name="button" Onclick="newfindAllProductPrice()" value="查询单个物品单价">
		<input type="button"  name="button" Onclick="findTableHead()" value="查询所找表">
		
		<input type="button"  name="button" Onclick="oneKeyUpdateAllProductPrice()" value="一键更新所有价">
		<input type="button"  name="button" Onclick="findAllProductFeature()" value="查询产品的所有属">
		
		
		<#-- 
			<input type="button"  name="button" Onclick="newCreateProductPrice()" value="新增产品价格">
		 
		   -->
		
		
		<input type="button"  name="button" Onclick="anotherAddVariantProduct()" value="另一个新的添加变型产">
		<input type="button"  name="button" Onclick="deleteProductFeatureType()" value="删除一个属">
		<input type="button"  name="button" Onclick="findProductAndCategory()" value="chaxun">
		 
		<input type="button"  name="button" Onclick="deleteSKU()" value="删除SKU">
		<input type="button"  name="button" Onclick="createPersonContact()" value="创建客户">
		
		<input type="button"  name="button" Onclick="findGeo()" value="查询地区">
		<input type="button"  name="button" Onclick="skuFindParentProductAndCategory()" value="查询skuFindParentProductAndCategory">
		<input type="button"  name="button" Onclick="createProductAndGotoPrice()" value="保存createProductAndGotoPrice">
		<input type="button"  name="button" Onclick="findSaleChannel()" value="查询findSaleChannel">
		
		<input type="button"  name="button" Onclick="LookupAutoCompleteByName()" value="查询LookupAutoCompleteByName">
		
		<input type="button"  name="button" Onclick="findProductNameById()" value="查询findProductNameById">
		<input type="button"  name="button" Onclick="LookupAutoCompleteByTelecom()" value="查询LookupAutoCompleteByTelecom">
		<input type="button"  name="button" Onclick="LookupAutoComplete()" value="查询LookupAutoComplete">
		<input type="button"  name="button" Onclick="findsalechannleByStoreId()" value="查询findsalechannleByStoreId">
		<input type="button"  name="button" Onclick="findPriceUOM()" value="查询findPriceUOM">
		<input type="button"  name="button" Onclick="newUpdatePassword()" value="修改密码newUpdatePassword">
		
		
    </td>
    </tr>
    </tbody></table>
</div></div>
</form>



