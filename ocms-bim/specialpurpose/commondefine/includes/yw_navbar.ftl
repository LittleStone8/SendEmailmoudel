<div id="yiwill_header">
	<#if currenttime?has_content>
         <input type="hidden" id="timeZones" value="${currenttime}" />
    </#if>
    
	<#if timeNow?has_content>
         <input type="hidden" id="timeNow" value="${timeNow}" />
    </#if>
    
	<#if rawOffset?has_content>
         <input type="hidden" id="rawOffset" value="${rawOffset}" />
    </#if>
    
    <div class="yiwill_header_wap">
        <div class="yiwill_header_list">
            <div class="yiwill_header_header">
                <div class="yiwill_header_login">
                   <#if userLogin?has_content>
                      <span>Welcome,<em class="userLoginId">${userLogin.userLoginId}<i></i></em>
	                      <#if security.hasPermission("WRHS_INV_TI", session)>
	                      		<em class="receiveNum receiveContent"></em>
	                      </#if>
                      </span>
                   </#if>
                     <div class="menuList">
                     	<#if security.hasPermission("WRHS_INV_TI", session)>
                     		<a href="/warehouse/control/newTransferInventory?type=Receive" class="receiveTransfer" data-type="receive"><em class="receiveNumList receiveContent"></em>Transfer Item To Receive</a>
                     	</#if>
                   		<a href="javascript:void(0)" class="updatePassword" data-type="password">Change password</a>
                   </div>
                    <a href="javascript:void(0)" class="login_out" id="logout">Logout</a>
                </div>
                <div class="yiwill_header_logo"><a href="/opentaps"></a></div>
                <div class="yiwill_header_box">
                    <ul class="yiwill_header_ul" id="yiwill_header_ul">
                     	<#if apps?exists>
                     		<#list apps as app>
                     		 	<#if app.applicationId == 'catalog'>
                     		 	
                     		 	
	                     		 	 <#if security.hasPermission("PRODUCT_CATEGORY", session)>	
					                    <li><a href="/catalog/control/Classfication" class="product_management" data-id="productManagement">Catalog</a></li>
					                 <#elseif security.hasPermission("PPR_FIND", session)>
					                 	<li><a href="/catalog/control/ProductPriceRule" class="product_management" data-id="productManagement">Catalog</a></li>
					                 <#elseif security.hasPermission("PPR_FIND_LOG", session)>
					                 	<li><a href="/catalog/control/ProductPriceLog" class="product_management" data-id="productManagement">Catalog</a></li>
					                 </#if>
                     		 	
                     		 	
                     		 	
		                        	
		                         </#if>
		                         <#if app.applicationId == 'ordermgr'>
		                        	<li><a href="/ordermgr/control/findSalesOrders" class="order_management" data-id="orderManagement">Order Management</a></li>
		                         </#if>
		                         
		                         <#if app.applicationId == 'party'>
		                        	<li><a href="/partymgr/control/findparty" class="member_management" data-id="memberManagement">Party Admin</a></li>
		                         </#if> 
		                         
		                         <#if app.applicationId == 'report'>
		                         	<#if security.hasPermission("REPMENU_SALE", session)>	
					                    <li><a href="/report/control/BANSalesReport" class="data_report" data-id="dataReport">Report</a></li>
					                 <#elseif security.hasPermission("REPMENU_OP_INV", session)>
					                 	<li><a href="/report/control/BANInventoryReport" class="data_report" data-id="dataReport">Report</a></li>
					                 <#elseif security.hasPermission("WRHS_VIEW_REPORT", session)>
					                 	<li><a href="/report/control/StockinReport" class="data_report" data-id="dataReport">Report</a></li>					                 	
					                 <#elseif security.hasPermission("REPMENU_OP_INV", session)>
					                 	<li><a href="/report/control/OperatorInventoryReport" class="data_report" data-id="dataReport">Report</a></li>
					                 	<#elseif security.hasPermission("REPMENU_OP_SALE", session)>
					                 	<li><a href="/report/control/OperatorSalesReport" class="data_report" data-id="dataReport">Report</a></li>
					                 	<#elseif security.hasPermission("WRHS_VIEW_MTNREPORT", session)>
					                 	<li><a href="/report/control/OperatorStockinReport" class="data_report" data-id="dataReport">Report</a></li>					                 	
					                 </#if>
		                        	<!-- <li><a href="/report/control/main" class="data_report" data-id="dataReport">Report</a></li> -->
		                         </#if>
		                         
		                         <#if app.applicationId == 'Store'>
		                        	<li><a href="/Store/control/main" class="store_management" data-id="storeManagement">Store</a></li>
		                         </#if>
		                         
		                         <#if app.applicationId == 'warehouse'>
		                         	<#if security.hasPermission("WRHS_INV_RAI", session)>
		                         		<#if security.hasPermission("WRHS_ADMIN", session)>
					                      <li><a href="/warehouse/control/receiveInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="WRHS_ADMIN" data-id="warehouseManagement">Warehouse</a></li>	
					                   	  	<#elseif security.hasPermission("WRHS_BAM", session)>
					                   	  <li><a href="/warehouse/control/receiveInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="WRHS_BAM" data-id="warehouseManagement">Warehouse</a></li>
					                   	  	<#else>
					                   	  <li><a href="/warehouse/control/receiveInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="" data-id="warehouseManagement">Warehouse</a></li>	
					                   </#if> 
		                         	<#else>    
		                         		<#if security.hasPermission("WRHS_ADMIN", session)>
					                      <li><a href="/warehouse/control/newFindInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="WRHS_ADMIN" data-id="warehouseManagement">Warehouse</a></li>	
					                   	  	<#elseif security.hasPermission("WRHS_BAM", session)>
					                   	  <li><a href="/warehouse/control/newFindInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="WRHS_BAM" data-id="warehouseManagement">Warehouse</a></li>
					                   	  	<#else>
					                   	  <li><a href="/warehouse/control/newFindInventoryItem" id="warehouse_management" class="warehouse_management" data-admin="" data-id="warehouseManagement">Warehouse</a></li>	
					                   </#if> 
		                         	</#if>                    
		                       	 </#if>
		                         
		                         <#if app.applicationId == 'webpos'>
		                        	<li><a href="/wpos/control/main" class="pos_cash_register" data-id="posCashRegister">Web POS</a></li>
		                         </#if>
	                         </#list>
                        </#if>
                    </ul>
                </div>
            </div>
            <div class="yiwill_header_second_header" id="yiwill_header_second_header">
            </div>
        </div>
    </div>
</div>
<div id="edit_password_dialog">
	<div class="fixed_screen"></div>
	<div class="edit_password_box">
		<div class="edit_password_icon"></div>
		<div class="edit_password_title">Edit password</div>
		<div class="edit_password_content">
			<div class="input_group">
				<span>Username</span>
				<span>${userLogin.userLoginId}</span>
				<input type="hidden" class="user_name" value="${userLogin.userLoginId}" />
				<input type="hidden" class="user_id" value="${userLogin.partyId}" />
			</div>
			<div class="input_group">
				<span>Original password</span>
				<input type="password" class="original_password" />
			</div>
            <div class="input_group">
                <span>New password</span>
				<input type="password" class="new_password" />
            </div>
            <div class="input_group">
                <span>Repeat password</span>
                <input type="password" class="repeat_password" />
            </div>
		</div>
		<div class="edir_password_error_tips"><span></span></div>
		<div class="edit_password_operator">
			<a class="save" href="javascript:void(0);">Save</a>
		</div>
	</div>
</div>
<script type="text/javascript" >
    (function(){
    	
    	
    	//仓库权限
    	var wareHopuseDocument = document.getElementById('warehouse_management');
    	var dataMain = '';
    	
       	<#if security.hasPermission("WRHS_INV_RAI", session)>
       		dataMain = "<a href="+"/warehouse/control/receiveInventoryItem"+">Inventory</a>";
    		<#else>
       		dataMain = "<a href="+"/warehouse/control/newFindInventoryItem"+">Inventory</a>";
   		</#if>
   		
    	var wareHouseList = {"warehouseManagement":[
    		dataMain,
    		<#if isShip?exists&&isShip == 'Y'>
    			"<a href="+"/warehouse/control/shippingMain"+">Shipping</a>"
         	</#if>
            ]};
    	
    	if(wareHopuseDocument){
    		if(wareHopuseDocument.getAttribute('data-admin') == 'WRHS_ADMIN'){
    			wareHouseList["warehouseManagement"].push("<a href="+"/warehouse/control/configurationMain"+">Warehouse Configuration</a>");
    			wareHouseList["warehouseManagement"].push("<a href="+"/warehouse/control/batchAddMember"+">Batch Add Member</a>");
    		}else if(wareHopuseDocument.getAttribute('data-admin') == 'WRHS_BAM'){
    			wareHouseList["warehouseManagement"].push("<a href="+"/warehouse/control/batchAddMember"+">Batch Add Member</a>");
    		}
    	}
	
        //菜单map
        var dataReportsList = [];
        
        <#if security.hasPermission("REPMENU_INV", session)>
        	dataReportsList.push("<a href="+"/report/control/BANSalesReport"+">Sales Report</a>");
       	</#if>
       	<#if security.hasPermission("REPMENU_SALE", session)>
	    	dataReportsList.push("<a href="+"/report/control/BANInventoryReport"+">Inventory Report</a>");
	   	</#if>
       	
       	<#if security.hasPermission("WRHS_VIEW_REPORT", session)>
   			dataReportsList.push("<a href="+"/report/control/StockinReport"+">Stockin Report</a>");
   			dataReportsList.push("<a href="+"/report/control/StockoutReport"+">Stockout Report</a>");
   		</#if>
       	
        
       
        <#if security.hasPermission("REPMENU_OP_SALE", session)>
        	dataReportsList.push("<a href="+"/report/control/OperatorSalesReport"+">Operator Sales Report</a>");
        </#if>
        <#if security.hasPermission("REPMENU_OP_INV", session)>	
	    	dataReportsList.push("<a href="+"/report/control/OperatorInventoryReport"+">Operator Inventory Report</a>");
	    </#if>
        
        <#if security.hasPermission("WRHS_VIEW_MTNREPORT", session)>
    		dataReportsList.push("<a href="+"/report/control/OperatorStockinReport"+">Operator Stockin Report</a>");
    		dataReportsList.push("<a href="+"/report/control/OperatorStockoutReport"+">Operator Stockout Report</a>");
    	</#if>
        
        var map=[
                {"productManagement":[
                	<#if security.hasPermission("PRODUCT_CATEGORY", session)>	
                    "<a href="+"/catalog/control/Classfication"+">Category</a>",
                    "<a href="+"/catalog/control/CreateProduct"+">Product Maintain</a>",
                    "<a href="+"/catalog/control/FindProductListPage"+">Products List</a>",
                     </#if>
                    <#if security.hasPermission("PPR_FIND", session)>	
                    "<a href="+"/catalog/control/ProductPriceRule"+">Promotion</a>",
                    <#elseif security.hasPermission("PPR_FIND_LOG", session)>
                    "<a href="+"/catalog/control/ProductPriceLog"+">ProductPriceLog</a>",
                     </#if>
                    ]},
                {"orderManagement" : [
                    "<a href="+"/ordermgr/control/findSalesOrders"+">Search Orders</a>",
                    "<a href="+"/ordermgr/control/createOrderMainScreen"+">Create Order</a>"
                    ]},
                {"memberManagement" : [
                    "<a href="+"/partymgr/control/findparty"+">Find Parties</a>",
                    "<a href="+"/partymgr/control/showclassgroups"+">Party Classification Group List</a>",
                    "<a href="+"/partymgr/control/FindSecurityGroup"+">Security Groups List</a>",
                    "<a href="+"/partymgr/control/BatchAddSecurityGroup"+">Batch Add Security Group</a>"
                    ]},
                {"storeManagement" : [
                    "<a href="+"/Store/control/main"+">Store</a>",
                    "<a href="+"/Store/control/FindProductStoreRoles"+">Role</a>"
                    ]},

                    wareHouseList,
                {"posCashRegister":[
                    ]},
                {"dataReport":dataReportsList}
                ];

        //生成二级菜单列表
        var yiwillHeader = document.getElementById('yiwill_header');
        var yiwillHeaderUl = document.getElementById('yiwill_header_ul');
        var yiwillHeaderSecondHeader = document.getElementById('yiwill_header_second_header');
        var aList = yiwillHeaderUl.getElementsByTagName('a');
        var secondAList = yiwillHeaderSecondHeader.getElementsByTagName('a');

        yiwillHeaderSecondHeader.innerHTML = '';
        var contentList = '';

        for(var i = 0;i<aList.length; i++){
            (function(i){
                aList[i].onclick = function(e){
                    for(var m = 0;m<aList.length; m++){
                        aList[m].parentNode.removeAttribute('class');
                    }
                    e.target.parentNode.setAttribute('class','selected');

                    var html = '';
                    var menuName = e.target.getAttribute('data-id');
                    var menuNameList = [];
                    for(var k = 0;k<map.length; k++){
                        if(map[k][menuName]){
                           menuNameList = map[k][menuName];
                        }
                    }
                    if(menuNameList.length > 0){
                        for(var j = 0;j<menuNameList.length; j++){
                            html += '<li>'+ menuNameList[j] +'</li>'
                        }
                        yiwillHeaderSecondHeader.innerHTML = '';
                        yiwillHeaderSecondHeader.append(parseToDOM(html));
                    }else{
                        yiwillHeaderSecondHeader.innerHTML = '';
                    }
                }
            })(i)
        }

        function parseToDOM(str){
           var ul = document.createElement("ul");
           if(typeof str == "string"){
               ul.innerHTML = str;
           }
           ul.setAttribute('class','yiwill_header_second_ul');

           return ul;
        }


       //一级菜单增加选中状态
        var firstMenuObj = {};
        var GLOBAL_FIRST_MENU = 'opentaps';
        var firstTitleMapRegStr = [
            GLOBAL_FIRST_MENU,
            "catalog",
            ["ordermgr", "crmsfa/control/orderview", "crmsfa/control/createOrderContactMech",
            "crmsfa/control/viewContact","crmsfa/control/findContacts",
            "crmsfa/control/findSalesOrders","crmsfa/control/newShipGroup"
            ],

            "partymgr",
            "Store",
            "warehouse",
            "wpos",
            "report"
        ];

        // filter first menu list
        // regenerate menu list obj
        var x= 0,alen=aList.length;
        var aListHref ;
        for(x=0;x<alen;x++) {
            aListHref = aList[x].getAttribute('href');
            var currentMapContent =getCurrentMapContent(aListHref, firstTitleMapRegStr);
            if(currentMapContent){
                firstMenuObj[Array.isArray(currentMapContent)?currentMapContent[0]:currentMapContent]=aList[x];
            }
        }

        /**
         * 获取一级类目匹配规则
         * @param pathname 一级类目path
         * @returns {*} 一级类目标识符
         */
        function getCurrentMapContent(pathname, mapRegArr){
            pathname = pathname && pathname.length >0 ? pathname.substring(1) : '';
            var firstTitleMapReg = mapRegArr.map(function(item){
                return new RegExp('^'+(Array.isArray(item) ? item.join('|^') : item), 'i');
            });
            for(var t = 0;t<firstTitleMapReg.length; t++){
                if(firstTitleMapReg[t].test(pathname)){
                    return mapRegArr[t];
                }
            }
            return null;
        }

        //get auth first category reg str
        var authMapRegStr =firstTitleMapRegStr.filter(function(item){
            //global first category
            return (Array.isArray(item) ? firstMenuObj[item[0]] : firstMenuObj[item])
                    || item === GLOBAL_FIRST_MENU;
        });
        //select current first Menu
        var currentFirstMenu = getCurrentMapContent(window.location.pathname, authMapRegStr);
        if(currentFirstMenu !== GLOBAL_FIRST_MENU){
            if(currentFirstMenu){
                var currentMenuEle = firstMenuObj[Array.isArray(currentFirstMenu)?currentFirstMenu[0]:currentFirstMenu];
                var menuCode = currentMenuEle.getAttribute('data-id');
                var secondMenuList = [];
                for(var n = 0; n<map.length; n++){
                    if(map[n][menuCode]){
                        secondMenuList = map[n][menuCode]
                    }
                }
                for(var s = 0;s<secondMenuList.length; s++){
                    contentList += '<li>'+ secondMenuList[s] +'</li>'
                }
                currentMenuEle.parentNode.setAttribute('class','selected');
            }else{
                location.replace('/opentaps/control/main');
                return;
            }
        }

       if(contentList != ''){
           yiwillHeaderSecondHeader.appendChild(parseToDOM(contentList));
       }

       //二级菜单增加选中状态
       
       var reportList = [];
      
	   <#if security.hasPermission("REPMENU_SALE", session)>
	   		reportList.push({
	             moduleName : "report",
	             url : "BANSalesReport",
	             children : []
	   		});
	   </#if>
	   <#if security.hasPermission("REPMENU_INV", session)>
	  		reportList.push({
	           moduleName : "report",
	           url : "BANInventoryReport",
	           children : []
	  		});
	  </#if>
	   <#if security.hasPermission("WRHS_VIEW_REPORT", session)>
 		reportList.push({
           moduleName : "report",
           url : "StockinReport",
           children : []
 		});
 		reportList.push({
            moduleName : "report",
            url : "StockoutReport",
            children : []
  		});
		</#if>
		<#if security.hasPermission("REPMENU_OP_SALE", session)>
	   		reportList.push({
	             moduleName : "report",
	             url : "OperatorSalesReport",
	             children : []
	   		});
	   </#if>
		<#if security.hasPermission("REPMENU_OP_INV", session)>	
	   		reportList.push({
	             moduleName : "report",
	             url : "OperatorInventoryReport",
	             children : []
	   		});
	   </#if>
		
	      
	   <#if security.hasPermission("WRHS_VIEW_MTNREPORT", session)>
 		reportList.push({
           moduleName : "report",
           url : "OperatorStockinReport",
           children : []
 		});
		reportList.push({
	           moduleName : "report",
	           url : "OperatorStockoutReport",
	           children : []
	 		});
		</#if>

	   //warehouse list
	   var warehouseSecondList = [{
           moduleName : "warehouse",
           url : "newFindInventoryItem",
           children : ["findInventoryItem","receiveInventoryItem","physicalInventory","newTransferInventory","manageLots","productStock","checkStock"]
       }];
	   <#if isShip?exists&&isShip == 'Y'>
	   warehouseSecondList.push({
           moduleName : "warehouse",
           url : "shippingMain",
           children : ["readyToShip","ScheduleShipmentRouteSegment","Labels","OutgoingShipments"]
        })
       </#if>
	   if(wareHopuseDocument){
			if(wareHopuseDocument.getAttribute('data-admin') == "WRHS_ADMIN"){
				warehouseSecondList.push({
		            moduleName : "warehouse",
		            url : "configurationMain",
		            children : ["viewWarehouse","EditContactMech","viewTeamMembers","addFacilityTeamMember"]
		         });
				warehouseSecondList.push({
		             moduleName : "warehouse",
		             url : "batchAddMember",
		             children : []
		          });
			}else if(wareHopuseDocument.getAttribute('data-admin') == "WRHS_BAM"){
				warehouseSecondList.push({
		             moduleName : "warehouse",
		             url : "batchAddMember",
		             children : []
		          });
			} 
	   }
	   
       var secondTitleMap = [
           [{
                moduleName : "catalog",
                url : "Classfication",
                children : []
           },
           {
               moduleName : "catalog",
               url : "CreateProduct",
               children : ["BulkAddProductFeatures","FindProductPriceRulesTest"]
          },
          {
              moduleName : "catalog",
              url : "FindProductListPage",
              children : []
          },
          {
              moduleName : "catalog",
              url : "BatchManagerPrice",
              children : ["ProductPriceRule","ProductPriceLog","BatchManagerPrice"]
          }],
          [
          {
             moduleName : "ordermgr",
             url : "findSalesOrders",
             children : []
          },
          {
             moduleName : "ordermgr",
             url : "createOrderMainScreen",
             children : []
         }],
         [{
           moduleName : "partymgr",
           url : "findparty",
           children : ["createnew","editpartygroup","editperson","NewCustomer","NewProspect","NewEmployee"]
         },
         {
              moduleName : "partymgr",
              url : "showclassgroups",
              children : ["EditPartyClassificationGroup","EditPartyClassificationGroup"]
         },
         {
             moduleName : "partymgr",
             url : "FindSecurityGroup",
             children : ["EditSecurityGroup"]
         },
         {
             moduleName : "partymgr",
             url : "BatchAddSecurityGroup",
             children : []
         }],
         [{
            moduleName : "Store",
            url : "main",
            children : ["EditProductStore"]
         },
         {
           moduleName : "Store",
           url : "FindProductStoreRoles",
           children : []
         }],
         warehouseSecondList,
         [],
         reportList
       ];

       for(var i= 0; i<secondTitleMap.length; i++){
            var secondTitleMapList = secondTitleMap[i];
            if(secondTitleMapList.length > 0){
                for(var j = 0; j<secondTitleMapList.length ; j++){
                    if(window.location.pathname.slice(1,(secondTitleMapList[j].moduleName.length) + 1) === secondTitleMapList[j].moduleName){
                        if(window.location.pathname.indexOf(secondTitleMapList[j].url) > -1){
                           for(var m = 0;m<secondAList.length; m++){
                               secondAList[m].parentNode.removeAttribute('class');
                           }
                           secondAList[j] && secondAList[j].parentNode.setAttribute('class','selected');
                        }else if(secondTitleMapList[j].children && secondTitleMapList[j].children.length > 0){
                            var childrenList = secondTitleMapList[j].children;
                            for(var k = 0; k<childrenList.length ; k++){
                                if(window.location.pathname.indexOf(childrenList[k]) > -1){
                                    for(var m = 0;m<secondAList.length; m++){
                                       secondAList[m].parentNode.removeAttribute('class');
                                    }
                                    secondAList[j] && secondAList[j].parentNode.setAttribute('class','selected');
                                }
                            }
                        }

                    }

                }
            }
       }

       //登出
       var logout = document.getElementById('logout');
       logout.onclick = function(){
           jQuery.ajax({
              url:"/commondefine/control/logout",
              type: 'post',
              dataType: 'json',
              contentType:"application/x-www-form-urlencoded",
              success: function(jsonObj){
                  var dataResult = jsonObj;
                  if(dataResult.responseMessage == "success"){
                      location.reload();
                      deleteAllCookie();
                  }
              }
           });
       };

        //清除cookie
        function deleteAllCookie(){
            var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
            if (keys) {
                for (var i = 0; i < keys.length; i++){
                    document.cookie=keys[i]+'=0;expires=' + new Date(0).toGMTString();
                }
            }
        }
          //获取一级选中菜单
        function getFirstTitle(){
        	var firstTitleArr = [];
        	var SecondTitleArr = [];
        	
        	var yiwillHeaderUl = document.getElementById('yiwill_header_ul');
        	var aList = yiwillHeaderUl.getElementsByTagName('a');
        	
            for(var i = 0; i < aList.length; i++){
            	if(aList[i].parentNode.className && aList[i].parentNode.className == 'selected'){
            		firstTitleArr.push(aList[i].innerHTML)
            		break;
            	}
            }
            
            if(firstTitleArr.length > 0){
            	SecondTitleArr =  getSecondTitle();
            }else{
            	firstTitleArr.push(location.pathname.split('/')[1]);
            	SecondTitleArr.push(location.pathname.split('/')[(location.pathname.split('/').length) -1])
            }
            return SecondTitleArr.concat(firstTitleArr);
        }
        
        //获取二级选中菜单
        function getSecondTitle(){
        	var secondTitleArr = [];
        	
            var yiwillHeaderSecondHeader = document.getElementById('yiwill_header_second_header');
            var secondAList = yiwillHeaderSecondHeader.getElementsByTagName('a');
            
            for(var i = 0; i < secondAList.length; i++){
            	if(secondAList[i].parentNode.className && secondAList[i].parentNode.className == 'selected'){
            		secondTitleArr.push(secondAList[i].innerHTML);
            		break;
            	}
            }
            
        	if(secondTitleArr.length == 0){
        		secondTitleArr.push(location.pathname.split('/')[(location.pathname.split('/')).length-1])
        	}
        	
        	return secondTitleArr;
        }
	
        
        //修改title
        function setTitle(){
        	var titleStr = ['OCMS-BIM'];
        	
        	var titleList = getFirstTitle();
        	
        	document.title = titleList.concat(titleStr).join(' | ');
        }
        setTitle();
    })()
	showImei = "${imei}"
</script>

