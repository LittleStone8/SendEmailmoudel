<div id="price_maintenance">
	<input type="hidden" id="egateeCostId" />
	<input type="hidden" id="operatorCostId" />
	<input type="hidden" id="retailCostId" />
	<input type="hidden" id="countryId" />
	<div class="price_maintenance_content_wap">
		<div class="price_maintenance_content">
		    <div class="price_maintenance_base_information">
		        <div class="product_flag">
                    <span>Product ID :</span>
                    <span class="product_flag_input" name="product_flag_input"></span>
                </div>
                <div class="name_type">
                    <span>Model :</span>
                    <span class="name_type_input" name="name_type_input"></span>
                </div>
                <div class="brand_name">
                    <span>Brand Name :</span>
                    <span class="brand_name_input" name="brand_name_input"></span>
                </div>
		    </div>
			<div class="price_maintenance_choose">
				<div class="price_maintenance_choose_title">
					Select Products
				</div>
				<div class="price_maintenance_choose_content">
					<table class="product_list_table">
						<thead>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="price_maintenance_info">
				<div class="price_maintenance_info_title">
					Price Maintenance
				</div>
				<div class="price_maintenance_info_content">
					<div class="retail">
						<span class="retail_title">Retail Channel Price</span>
						<div class="retail_content">
							<select class="retail_content_select">
								
							</select>
							<input type="number" class="retail_content_input" min="0" />
						</div>
					</div>
					<div class="b2r">
						<span class="b2r_title">B2R Channel Price</span>
						<div class="b2r_content">
							<select class="b2r_content_select">
							
							</select>
							<input type="number" class="b2r_content_input" min="0"/>
						</div>
					</div>
					<div class="agent">
						<span class="agent_title">Agent Channel Price</span>
						<div class="agent_content">
							<select class="agent_content_select">
								
							</select>
							<input type="number" class="agent_content_input" min="0"/>
						</div>
					</div>
					<div class="wholesale">
						<span class="wholesale_title">Wholesale Channel Price</span>
						<div class="wholesale_content">
							<select class="wholesale_content_select">
								
							</select>
							<input type="number" class="wholesale_content_input" min="0"/>
						</div>
					</div>
                    <div class="default">
                        <span class="default_title">Default Cost</span>
                        <div class="default_content">
                            <select class="default_content_select">
                                
                            </select>
                            <input type="number" class="default_content_input" min="0"/>
                        </div>
                    </div>
                    <div class="special defaultHide">
                        <span class="special_title">Special Cost</span>
                        <div class="special_content">
                            <select class="special_content_select">
                                
                            </select>
                            <input type="number" class="special_content_input" min="0"/>
                        </div>
                    </div>
                    <div class="egatee defaultHide">
                        <span class="egatee_title">Egatee Cost</span>
                        <div class="egatee_content">
                            <select class="egatee_content_select">
                                
                            </select>
                            <input type="number" class="egatee_content_input" min="0"/>
                        </div>
                    </div>
                    <div class="retailCost defaultHide">
                        <span class="retailCost_title">Retail  Cost</span>
                        <div class="retailCost_content">
                            <select class="retailCost_content_select">
                                
                            </select>
                            <input type="number" class="retailCost_content_input" min="0"/>
                        </div>
                    </div>
				</div>
				<div class="price_maintenance_info_operator">
					<a class="batch_update" href="javascript:void(0);">Batch Edit</a>
				</div>
				<div class="price_maintenance_info_list">
				</div>
			</div>
			<div class="price_maintenance_create">
				<a href="javascript:void(0);" class="create_product">Submit</a>
			</div>
		</div>
	</div>
</div>
<div class="error_tips">
    <div class="fixed_screen"></div>
    <div class="error_tips_box">
        <div class="error_tips_title">Message</div>
        <div class="error_tips_content">
            <span></span>
        </div>
        <div class="error_tips_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<script type="text/javascript">
    var headLi = '';
    var currencyList = [];
	//获取币种
	function findPriceUOM(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/findPriceUOM',
	        data: {},
	        success: function (data) {
	        	if(data.responseMessage == 'success'){
	        		var dataResult = data.uomMap;
	        			currencyList.push(data.uomMap);
	        		
	        		var retailContentSelect = $('.retail_content_select'),
	        			b2rContentSelect = $('.b2r_content_select'),
	        			agentContentSelect = $('.agent_content_select'),
	        			wholesaleContentSelect = $('.wholesale_content_select'),
	        			defaultContentSelect = $('.default_content_select'),
	        			specialContentSelect = $('.special_content_select'),
	        			egateeContentSelect = $('.egatee_content_select'),
	        			retailCostContentSelect = $('.retailCost_content_select');
	        		
	        		var content = '<option value="'+ dataResult.uomId +'">'+ dataResult.abbreviation +'</option>' ;
	        		var egateeCostId = $('#egateeCostId'),
	        			operatorCostId = $('#operatorCostId'),
	        			retailCostId = $('#retailCostId');
	        		
	        		retailContentSelect.append(content);
	        		b2rContentSelect.append(content);
	        		agentContentSelect.append(content);
	        		wholesaleContentSelect.append(content);
	        		defaultContentSelect.append(content);
	        		specialContentSelect.append(content);
	        		egateeContentSelect.append(content);
	        		retailCostContentSelect.append(content);
	        		
	        		/* justment */
	        		egateeCostId.val(dataResult.egateeCost);
	        		operatorCostId.val(dataResult.specialCost);
	        		retailCostId.val(dataResult.retailCost);
	        		
	        		/* show cost */
	        		var retailCost = $('.retailCost'),
	        			egatee = $('.egatee'),
	        			special = $('.special');
	        		var countryId = $('#countryId');
	        		
	        		if(dataResult.countryId == 'UGA'){
	        			retailCost.removeClass('defaultHide');
	        			egatee.removeClass('defaultHide');
	        			special.removeClass('defaultHide');
	        		}
	        		countryId.val(dataResult.countryId);
	        	}
	       	}	
		});
	}
	findPriceUOM();
	
    //获取参数
    function getUrlParams(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
      return null;
    }
    //错误信息
    function showErrorMessage(message, callBack){
        var errorTips = jQuery('.error_tips');
        errorTips.find('.error_tips_content span').html('').html(message);
        errorTips.show();
        errorTips.find('.error_tips_operator').off('click','a').on('click','a',function () {
            jQuery('.error_tips').hide();
            callBack && callBack();
        })
    }
    //数字序列�    
     function parseNumberList(option){
        return parseInt(option, 10);
    }
    function findProductNameById(){
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType : 'application/json;charset=utf-8',
            url: '/catalog/control/findProductNameById',
            data:JSON.stringify({
					productId:getUrlParams('productId')
				}),
            success: function (res) {
                var dataResult = res.data;
                if(res.success){
                    var product_flag_input = $('.product_flag_input');
                    var name_type_input = $('.name_type_input');
                    var brand_name_input = $('.brand_name_input');
                    product_flag_input.text(dataResult.productId);
                    name_type_input.text(dataResult.internalName);
                    brand_name_input.text(dataResult.brandName);
                }else {
                    showErrorMessage(res.message);
                }
            }
        });
    }
    findProductNameById();
    function findAllProductFeature(){
        jQuery.ajax({
            type: 'post',
            dataType: 'json',
             contentType : 'application/json;charset=utf-8',
            url: '/catalog/control/findAllProductFeature',
            data:JSON.stringify({
					"productId":getUrlParams('productId')
				}),
            success: function (res) {
                var dataResult = res.data;
                if(res.success){
                	var objAttr = {};
                    $.each(dataResult.priceProductFeatureMap, function(key, value){
	
                    	if(value.isActive == "Y"){
                    		objAttr[key] = value;
                    	}
                    });
                    
                    var bodyContent = '<tr>',
                    headContent = '<th style="width:120px"><input type="checkbox" class="checkAll"/><span>Select All</span></th>';
                    var titles =[];
                    jQuery.each(objAttr, function(key, value){
                        var keyValue = value.productFeatureMap;
                        jQuery.each(keyValue, function(Fkey, Fvalue){
                            if(titles.indexOf(Fkey) == -1){
                                titles.push(Fkey);
                            }
                        })
                    })
                    headLi = titles;
                    jQuery.each(objAttr,function(key, value){
                        var dataKey = value;
                        var keyArr = [],valueArr=dataKey.productFeatureMap;
                        var productIdSku = value.productIdSku;
                        bodyContent+='<td><input type="checkbox" data-id="'+ productIdSku +'"  class="checkList"/><textarea class="dataSave" style="display:none">'+JSON.stringify(value)+'</textarea></td>';
                        jQuery.each(valueArr,function(Fkey, Fvalue){
                            keyArr.push(Fkey);
                        })
                        for(var i = 0 ;i< titles.length; i++){
                            if(keyArr.indexOf(titles[i]) > -1){
                                bodyContent+= '<td>'+ valueArr[titles[i]] +'</td>';
                            }else{
                                bodyContent+= '<td></td>';
                            }
                        }
                        bodyContent+= '<td>'+ dataKey.productIdSku +'</td>';
                        bodyContent+='</tr>'
                    })
                    for(var i = 0; i<titles.length; i++){
                        headContent+= '<th>'+ titles[i] +'</th>';
                    }
                    headContent+='<th>Product ID(SKU)</th>';
                    jQuery('.product_list_table thead').html('').append('<tr>'+ headContent +'</tr>');
                    jQuery('.product_list_table tbody').html('').append(bodyContent);
                }else {
                    showErrorMessage(res.message);
                }
            }
        });
    }
    findAllProductFeature();
    function getPriceList(listId ,priceMaintenanceInfoList){
        var idList  =  listId;
        jQuery.ajax({
            type: 'post',
            dataType: 'json',
            url: '/catalog/control/findAllcheckedProductPrice',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
              productList : idList
            }),
            success: function (data) {
                var dataList = data;
                if(dataList.responseMessage == 'success'){
                    var dataContent = dataList.productIdList;
                    jQuery.each(dataContent, function(key, value){
                        var productKey = key;
                        var productValue = value;
                        //生成表头
                        var theadStr = '<tr>';
                        jQuery.each(headLi, function(key,value){
                             theadStr+='<th>'+ value +'</th>';
                        });
                        theadStr+='<th>product id(SKU)</th></tr>';
                        //生成表格
                        var tbodyStr = '<tr>';
                        var keyArr = [];
                        var bodyStr = jQuery('.product_list_table').find('input[data-id = "'+ productKey +'"]').parent().find('.dataSave').html();
                        var bodyLi = JSON.parse(bodyStr);
                        jQuery.each(bodyLi.productFeatureMap, function(key,value){
                              keyArr.push(key);
                        });
                        for(var i = 0 ;i< headLi.length; i++){
                            if(keyArr.indexOf(headLi[i]) > -1){
                                tbodyStr+= '<td>'+ bodyLi['productFeatureMap'][headLi[i]] +'</td>';
                            }else{
                                tbodyStr+= '<td></td>';
                            }
                        }
                        tbodyStr+= '<td>'+ bodyLi.productIdSku +'</td>';
                        tbodyStr+='</tr>';
                        var html_first = '',html_second = '',html_third = '',html_fourth = '',html_fifth = '', html_six='',html_seven = '', html_eight='';
                        var optionList = '';
                        //币种选中
                        jQuery.each(productValue, function(sortIndex, sortElem){
                            optionList = '';
                            jQuery.each(currencyList, function(index, currentElem){
                                if(currentElem.uomId == sortElem.currencyUomId){
                                    optionList+='<option value="'+ currentElem.uomId  +'" selected>'+ currentElem.abbreviation +'</option>';
                                }else{
                                    optionList+='<option value="'+ currentElem.uomId  +'">'+ currentElem.abbreviation  +'</option>';
                                }
                            });
                            //价格取�                            
                            if(sortElem.productStoreGroupID == "RETAIL"){
                                html_first =  '<div class="retail_content">'+
                                                   '<select class="retail_content_select">'+
                                                       optionList +
                                                   '</select>'+
                                                   '<input type="number" class="retail_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="RETAIL" />'+
                                               '</div>';
                            }else if(sortElem.productStoreGroupID == "B2R"){
                                html_second = '<div class="b2r_content">'+
                                                 '<select class="b2r_content_select">'+
                                                     optionList +
                                                 '</select>'+
                                                 '<input type="number" class="b2r_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="B2R" />'+
                                             '</div>';
                            }else if(sortElem.productStoreGroupID == "DEFAULT_COST_PRICE"){
                                html_third = '<div class="default_content">'+
                                                  '<select class="default_content_select">'+
                                                      optionList +
                                                  '</select>'+
                                                  '<input type="number" class="default_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="DEFAULT_COST_PRICE" />'+
                                              '</div>';
                            }else if(sortElem.productStoreGroupID == "SPECIAL_COST_PRICE"){
                                html_six = '<div class="special_content">'+
                                                '<select class="special_content_select">'+
                                                    optionList +
                                                '</select>'+
                                                '<input type="number" class="special_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="SPECIAL_COST_PRICE" />'+
                                            '</div>';
                            }else if(sortElem.productStoreGroupID == "AGENT"){
                                html_fourth = '<div class="agent_content">'+
                                                    '<select class="agent_content_select">'+
                                                        optionList +
                                                    '</select>'+
                                                    '<input type="number" class="agent_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="AGENT" />'+
                                               '</div>';
                            }else if(sortElem.productStoreGroupID == "WHOLESALE"){
                                html_fifth = '<div class="wholesale_content">'+
                                                 '<select class="wholesale_content_select">'+
                                                      optionList +
                                                  '</select>'+
                                                  '<input type="number" class="wholesale_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="WHOLESALE" />'+
                                              '</div>';
                            }else if(sortElem.productStoreGroupID == "EGATEE_COST_PRICE"){
                            	html_seven = '<div class="egatee_content">'+
					                                '<select class="egatee_content_select">'+
					                                    optionList +
					                                '</select>'+
					                                '<input type="number" class="egatee_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="EGATEE_COST_PRICE" />'+
					                           '</div>';
					        }else if(sortElem.productStoreGroupID == "RETAIL_COST_PRICE"){
					            html_eight = '<div class="retailCost_content">'+
					                             '<select class="retailCost_content_select">'+
					                                  optionList +
					                              '</select>'+
					                              '<input type="number" class="retailCost_content_input" min="0" value="'+ parseNumberList(sortElem.price) +'" data-type="RETAIL_COST_PRICE" />'+
					                          '</div>';
					        } 
                        });
                        if(optionList == ''){
                            jQuery.each(currencyList, function(index, currentElem){
                               optionList+='<option value="'+ currentElem.uomId  +'">'+ currentElem.abbreviation  +'</option>';
                            });
                        }
                        if(html_first == ''){
                            html_first =  '<div class="retail_content">'+
                                               '<select class="retail_content_select">'+
                                                   optionList +
                                               '</select>'+
                                               '<input type="number" class="retail_content_input" min="0" value="" data-type="DEFAULT_PRICE" />'+
                                           '</div>';
                        }
                        if(html_second == ''){
                            html_second = '<div class="b2r_content">'+
                                             '<select class="b2r_content_select">'+
                                                 optionList +
                                             '</select>'+
                                             '<input type="number" class="b2r_content_input" min="0" value="" data-type="WHOLESALE_PRICE" />'+
                                         '</div>';
                        }
                        if(html_third == ''){
                            html_third = '<div class="default_content">'+
                                              '<select class="default_content_select">'+
                                                  optionList +
                                              '</select>'+
                                              '<input type="number" class="default_content_input" min="0" value="" data-type="MINIMUM_PRICE" />'+
                                          '</div>';
                        }
                        if(html_fourth == ''){
                            html_fourth = '<div class="agent_content">'+
                                                '<select class="agent_content_select">'+
                                                    optionList +
                                                '</select>'+
                                                '<input type="number" class="agent_content_input" min="0" value="" data-type="LIST_PRICE" />'+
                                           '</div>';
                        }
                        if(html_fifth == ''){
                           html_fifth = '<div class="wholesale_content">'+
                                            '<select class="wholesale_content_select">'+
                                                 optionList +
                                             '</select>'+
                                             '<input type="number" class="wholesale_content_input" min="0" value="" data-type="_NA_" />'+
                                         '</div>';
                        }
                        if(html_six == ''){
                            html_six = '<div class="special_content">'+
                                            '<select class="special_content_select">'+
                                                optionList +
                                            '</select>'+
                                            '<input type="number" class="special_content_input" min="0" value="" data-type="SPECIAL_PRICE" />'+
                                        '</div>';
                        }
                        if(html_seven == ''){
                        	html_seven = '<div class="egatee_content">'+
                                             '<select class="egatee_content_select">'+
                                                  optionList +
                                              '</select>'+
                                              '<input type="number" class="egatee_content_input" min="0" value="" data-type="EGATEE_COST_PRICE" />'+
                                          '</div>';
                         }
                         if(html_eight == ''){
                        	 html_eight = '<div class="retailCost_content">'+
                                             '<select class="retailCost_content_select">'+
                                                 optionList +
                                             '</select>'+
                                             '<input type="number" class="retailCost_content_input" min="0" value="" data-type="RETAIL_COST_PRICE" />'+
                                         '</div>';
                         }
                        var html = '<div class="content_list" data-id="'+ productKey +'" >'+
                                '<table class="content_list_table">'+
                                    '<thead>'+
                                        theadStr +
                                    '</thead>'+
                                    '<tbody>'+
                                        tbodyStr +
                                    '</tbody>'+
                                '</table>'+
                                '<div class="content_list_operator">'+
                                    '<div class="retail">'+
                                        '<span class="retail_title">Retail Channel Price</span>'+
                                        html_first +
                                    '</div>'+
                                    '<div class="b2r">'+
                                        '<span class="b2r_title">B2R Channel Price</span>'+
                                        html_second +
                                    '</div>'+
                                    '<div class="agent">'+
                                        '<span class="agent_title">Agent Channel Price</span>'+
                                        html_fourth +
                                    '</div>'+
                                    '<div class="wholesale">'+
                                        '<span class="wholesale_title">Wholesale Channel Price</span>'+
                                        html_fifth +
                                    '</div>'+
                                    '<div class="default">'+
                                        '<span class="default_title">Default Cost</span>'+
                                        html_third +
                                    '</div>';
                                    
                            var countryId = $('#countryId').val();
                            if(countryId == 'UGA'){
	                            html +='<div class="special">'+
			                                '<span class="special_title">Special Cost</span>'+
			                                    html_six +
			                            '</div>'+
			                            '<div class="egatee">'+
			                                '<span class="egatee_title">Egatee Cost</span>'+
			                                html_seven +
			                            '</div>'+
			                            '<div class="retailCost">'+
			                                '<span class="retailCost_title">Retail  Cost</span>'+
			                                    html_eight +
			                            '</div>'+
			                        '</div>'+
			                    '</div>';
                            }else{
	                            html +='<div class="special defaultHide">'+
			                                '<span class="special_title">Special Cost</span>'+
			                                    html_six +
			                            '</div>'+
			                            '<div class="egatee defaultHide">'+
			                                '<span class="egatee_title">Egatee Cost</span>'+
			                                html_seven +
			                            '</div>'+
			                            '<div class="retailCost defaultHide">'+
			                                '<span class="retailCost_title">Retail  Cost</span>'+
			                                    html_eight +
			                            '</div>'+
			                        '</div>'+
			                    '</div>';
                            }
                            priceMaintenanceInfoList.append(html);
                    });
                }else if(dataList.responseMessage == 'error'){
                    showErrorMessage(dataList.errorMessage);
                }
            }
        });
    }
    jQuery('.product_list_table').on('click', '.checkAll', function(){
        var priceMaintenanceInfoList = jQuery('.price_maintenance_info_list');
        var checkList = jQuery('.product_list_table .checkList');
        var contentList = jQuery('.product_list_table .content_list');
        var listId = [];
        jQuery.each(checkList, function(index, elem){
            var id = jQuery(elem).attr('data-id');
            listId.push(id);
        });
        if(jQuery('.checkAll').is(':checked')){
            jQuery('.product_list_table').find('.checkList').prop('checked',true);
            jQuery('.price_maintenance_info_list').html('');
            getPriceList(listId, priceMaintenanceInfoList)
        }else{
            jQuery('.product_list_table').find('.checkList').prop('checked',false);
            jQuery('.price_maintenance_info_list').html('');
        }
    });
    jQuery('.product_list_table').on('click', '.checkList', function(){
        var _this = this;
        var priceMaintenanceInfoList = jQuery('.price_maintenance_info_list');
        var id = jQuery(_this).attr('data-id');
        var len = 0;
        var arrList = jQuery('.product_list_table').find('.checkList').length;
        jQuery('.product_list_table').find('.checkList').each(function(){
            if(jQuery(this).prop('checked')){
                len++;
            };
        })
        if(len == arrList){
            jQuery('.product_list_table').find('.checkAll').prop('checked',true);
        }else{
            jQuery('.product_list_table').find('.checkAll').prop('checked',false);
        }
        if(jQuery(_this).prop('checked') == false){
            jQuery('.content_list[data-id="'+ id +'"]').remove();
        }else{
            getPriceList([id], priceMaintenanceInfoList)
        }
    })
    //批量修改
    jQuery('.batch_update').on('click', function(){
        var priceMaintenanceInfoContent = jQuery('.price_maintenance_info_content');
        var priceMaintenanceInfoList = jQuery('.price_maintenance_info_list');
        var retailContentSelect =  priceMaintenanceInfoContent.find('.retail_content_select');
        var retailContentInput =  priceMaintenanceInfoContent.find('.retail_content_input');
        var b2rContentSelect =  priceMaintenanceInfoContent.find('.b2r_content_select');
        var b2rContentInput =  priceMaintenanceInfoContent.find('.b2r_content_input');
        var defaultContentSelect =  priceMaintenanceInfoContent.find('.default_content_select');
        var defaultContentInput =  priceMaintenanceInfoContent.find('.default_content_input');
        var specialContentSelect =  priceMaintenanceInfoContent.find('.special_content_select');
        var specialContentInput =  priceMaintenanceInfoContent.find('.special_content_input');
        var agentContentSelect =  priceMaintenanceInfoContent.find('.agent_content_select');
        var agentContentInput =  priceMaintenanceInfoContent.find('.agent_content_input');
        var wholesaleContentSelect =  priceMaintenanceInfoContent.find('.wholesale_content_select');
        var wholesaleContentInput =  priceMaintenanceInfoContent.find('.wholesale_content_input');
        var egateeContentSelect =  priceMaintenanceInfoContent.find('.egatee_content_select');
        var egateeContentInput =  priceMaintenanceInfoContent.find('.egatee_content_input');
        var retailCostContentSelect =  priceMaintenanceInfoContent.find('.retailCost_content_select');
        var retailCostContentInput =  priceMaintenanceInfoContent.find('.retailCost_content_input');
        var contentList = priceMaintenanceInfoList.find('.content_list');
        jQuery.each(contentList, function(index, elem){
            var List_retailContentSelect =  jQuery(elem).find('.retail_content_select');
            var List_retailContentInput =  jQuery(elem).find('.retail_content_input');
            var List_b2rContentSelect =  jQuery(elem).find('.b2r_content_select');
            var List_b2rContentInput =  jQuery(elem).find('.b2r_content_input');
            var List_defaultContentSelect =  jQuery(elem).find('.default_content_select');
            var List_defaultContentInput =  jQuery(elem).find('.default_content_input');
            var List_specialContentSelect =  jQuery(elem).find('.special_content_select');
            var List_specialContentInput =  jQuery(elem).find('.special_content_input');
            var List_agentContentSelect =  jQuery(elem).find('.agent_content_select');
            var List_agentContentInput =  jQuery(elem).find('.agent_content_input');
            var List_wholesaleContentSelect =  jQuery(elem).find('.wholesale_content_select');
            var List_wholesaleContentInput =  jQuery(elem).find('.wholesale_content_input');
            var List_egateeContentSelect =  jQuery(elem).find('.egatee_content_select');
            var List_egateeContentInput =  jQuery(elem).find('.egatee_content_input');
            var List_retailCostContentSelect =  jQuery(elem).find('.retailCost_content_select');
            var List_retailCostContentInput =  jQuery(elem).find('.retailCost_content_input');
            List_retailContentSelect.find('option[value="'+retailContentSelect.val()+'"]').prop('selected', true);
            if(List_retailContentInput.val() == ''){
                List_retailContentInput.val(retailContentInput.val());
            }
            List_b2rContentSelect.find('option[value="'+b2rContentSelect.val()+'"]').prop('selected', true);
            if(List_b2rContentInput.val() == ''){
                List_b2rContentInput.val(b2rContentInput.val());
            }
            List_defaultContentSelect.find('option[value="'+defaultContentSelect.val()+'"]').prop('selected', true);
            if(List_defaultContentInput.val() == ''){
                List_defaultContentInput.val(defaultContentInput.val());
            }
            List_agentContentSelect.find('option[value="'+agentContentSelect.val()+'"]').prop('selected', true);
            if(List_agentContentInput.val() == ''){
                List_agentContentInput.val(agentContentInput.val());
            }
            List_wholesaleContentSelect.find('option[value="'+wholesaleContentSelect.val()+'"]').prop('selected', true);
            if(List_wholesaleContentInput.val() == ''){
                List_wholesaleContentInput.val(wholesaleContentInput.val());
            }
            List_specialContentSelect.find('option[value="'+specialContentSelect.val()+'"]').prop('selected', true);
            if(List_specialContentInput.val() == ''){
                List_specialContentInput.val(specialContentInput.val());
            }
            
            List_egateeContentSelect.find('option[value="'+egateeContentSelect.val()+'"]').prop('selected', true);
            if(List_egateeContentInput.val() == ''){
            	List_egateeContentInput.val(egateeContentInput.val());
            }
            List_retailCostContentSelect.find('option[value="'+retailCostContentSelect.val()+'"]').prop('selected', true);
            if(List_retailCostContentInput.val() == ''){
            	List_retailCostContentInput.val(retailCostContentInput.val());
            }
        })
    });
    //格式化输入的数字
    $('input[type="number"]').on('keydown',function (e) {
        var formatValue = e.keyCode;
        if(e.ctrlKey || ((formatValue >= 48 && formatValue <= 57) || (formatValue >= 96 && formatValue <= 105) || formatValue == 8)){
            return true;
        }
        return false;
    });
    //提交信息
    jQuery('.create_product').on('click', function(e){
        var messageList = [];
        var flagFill = false;
        var priceMaintenanceInfoList = jQuery('.price_maintenance_info_list');
        var contentList = priceMaintenanceInfoList.find('.content_list');
        var costList = priceMaintenanceInfoList.find('.content_list .default_content_input');
        costList.length > 0 && jQuery.each(costList, function(index,elem){
            if(jQuery(elem).val() == ''){
                flagFill = true;
            }
        });
        if(flagFill){
            showErrorMessage('Please fill in the cost price');
            return false;
        }
        jQuery.each(contentList, function(index, elem){
            var List_retailContentSelect =  jQuery(elem).find('.retail_content_select');
            var List_retailContentInput =  jQuery(elem).find('.retail_content_input');
            var List_b2rContentSelect =  jQuery(elem).find('.b2r_content_select');
            var List_b2rContentInput =  jQuery(elem).find('.b2r_content_input');
            var List_defaultContentSelect =  jQuery(elem).find('.default_content_select');
            var List_defaultContentInput =  jQuery(elem).find('.default_content_input');
            var List_specialContentSelect =  jQuery(elem).find('.special_content_select');
            var List_specialContentInput =  jQuery(elem).find('.special_content_input');
            var List_agentContentSelect =  jQuery(elem).find('.agent_content_select');
            var List_agentContentInput =  jQuery(elem).find('.agent_content_input');
            var List_wholesaleContentSelect =  jQuery(elem).find('.wholesale_content_select');
            var List_wholesaleContentInput =  jQuery(elem).find('.wholesale_content_input');
            var List_egateeContentSelect =  jQuery(elem).find('.egatee_content_select');
            var List_egateeContentInput =  jQuery(elem).find('.egatee_content_input');
            var List_retailCostContentSelect =  jQuery(elem).find('.retailCost_content_select');
            var List_retailCostContentInput =  jQuery(elem).find('.retailCost_content_input');
            
            var countryId = $('#countryId').val();
            
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_egateeContentSelect.val(),
                productStoreGroupId : "EGATEE_COST_PRICE",
                fromDate : (new Date).getTime(),
                price : countryId == 'UGA' ? List_egateeContentInput.val() : '',
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_retailCostContentSelect.val(),
                productStoreGroupId : "RETAIL_COST_PRICE",
                fromDate : (new Date).getTime(),
                price : countryId == 'UGA' ? List_retailCostContentInput.val() : '',
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_retailContentSelect.val(),
                productStoreGroupId : "RETAIL",
                fromDate : (new Date).getTime(),
                price : List_retailContentInput.val(),
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_b2rContentSelect.val(),
                productStoreGroupId : "B2R",
                fromDate : (new Date).getTime(),
                price : List_b2rContentInput.val(),
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_defaultContentSelect.val(),
                productStoreGroupId : "DEFAULT_COST_PRICE",
                fromDate : (new Date).getTime(),
                price : List_defaultContentInput.val(),
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_agentContentSelect.val(),
                productStoreGroupId : "AGENT",
                fromDate : (new Date).getTime(),
                price : List_agentContentInput.val(),
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_wholesaleContentSelect.val(),
                productStoreGroupId : "WHOLESALE",
                fromDate : (new Date).getTime(),
                price : List_wholesaleContentInput.val(),
                termUomId : null,
                customPriceCalcService : null
            });
            messageList.push({
                productId : jQuery(elem).attr('data-id'),
                productPriceTypeId : "DEFAULT_PRICE",
                productPricePurposeId : "PURCHASE",
                currencyUomId : List_specialContentSelect.val(),
                productStoreGroupId : "SPECIAL_COST_PRICE",
                fromDate : (new Date).getTime(),
                price : countryId == 'UGA' ? List_specialContentInput.val() : '',
                termUomId : null,
                customPriceCalcService : null
            });
        });
        $.showLoading(e.currentTarget);
        jQuery.ajax({
            type: 'post',
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            url: '/catalog/control/oneKeyUpdateAllProductPrice',
            data: JSON.stringify({
                productPriceList:messageList
	            }),
	            success: function (data) {
	            	$.hideLoading(e.currentTarget);
	                var responseData =  data;
	                if(responseData.responseMessage == 'success'){
                    showErrorMessage(responseData.successMessage, function () {
                        window.location.href = '/catalog/control/FindProductListPage';
                    });
	                }else if(responseData.responseMessage == 'error'){
                    showErrorMessage(responseData.errorMessage);
                }
            },
            error : function(option, status){
            	$.hideLoading(e.currentTarget);
            }
        });
    });
    /**
     * 实现default cost �specail cost 联动效果
     * 按照沟通结果（2017/4.29）：specail = dafault * (1+7%)，每次调整完default cost后自动调整后面special cost�     * 四拾伍入取整处理
     */
    jQuery('.price_maintenance_info').on('blur', '.default_content_input', function(e){
        var target = $(e.currentTarget),
            defaultPri = target.val() || 0,
            p      = target.parents('.price_maintenance_info_content, .content_list');
		var egateeCostId = ($('#egateeCostId').val()-0);
		var operatorCostId = ($('#operatorCostId').val()-0);
		var retailCostId = ($('#retailCostId').val()-0);
		
        var specialPri = Math.round(defaultPri * operatorCostId);
        var specialPriEgatee = Math.round(defaultPri * egateeCostId);
        var specialPriRetailCost = Math.round(defaultPri * retailCostId);
        
        p.find('.special_content_input').val(specialPri);
        p.find('.egatee_content_input').val(specialPriEgatee);
        p.find('.retailCost_content_input').val(specialPriRetailCost);
    });
</script>