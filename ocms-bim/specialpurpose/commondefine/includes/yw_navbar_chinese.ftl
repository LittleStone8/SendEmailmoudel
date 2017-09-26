<div id="yiwill_header" style="display:none;">
    <div class="yiwill_header_wap">
        <div class="yiwill_header_list">
            <div class="yiwill_header_header">
                <div class="yiwill_header_login">
                    <span>欢迎您，<em>admin</em></span>
                    <a href="javascript:void(0)" class="login_out">退出</a>
                </div>
                 <div class="yiwill_header_logo"><a href="/opentaps"></a></div>
                <div class="yiwill_header_box">
                    <ul class="yiwill_header_ul" id="yiwill_header_ul">
                        <li><a href="/catalog/control/CreateProduct" class="product_management" data-id="productManagement">产品管理</a></li>
                        <li><a href="/ordermgr/control/myOrders" class="order_management" data-id="orderManagement">订单管理</a></li>
                        <li><a href="/partymgr/control/findparty" class="member_management" data-id="memberManagement">会员管理</a></li>
                        <li><a href="/Store/control/main" class="store_management" data-id="storeManagement">店铺管理</a></li>
                        <li><a href="/warehouse/control/inventoryMain" class="warehouse_management" data-id="warehouseManagement">仓库管理</a></li>
                        <li><a href="/wpos/control/main" class="pos_cash_register" data-id="posCashRegister">POS收银机</a></li>
                        <li><a href="/reports/control/InventoryReportFormsPage" class="data_report" data-id="dataReport">数据报表</a></li>
                    </ul>
                </div>
            </div>
            <div class="yiwill_header_second_header" id="yiwill_header_second_header">
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" >
    (function(){
        //菜单map
        var map=[
                {"productManagement":[
                    "<a href="+"/catalog/control/CreateProduct"+">选择类目</a>",
                    "<a href="+"/catalog/control/Classfication"+">创建类目</a>",
                    "<a href="+"/catalog/control/BulkAddProductFeatures"+">添加产品</a>",
                    "<a href="+"/catalog/control/FindProductListPage"+">产品列表</a>",
                    "<a href="+"/catalog/control/FindProductPriceRulesTest"+">价格维护</a>"
                    ]},
                {"orderManagement" : [
                    "<a href="+"/ordermgr/control/myOrders"+">我的订单</a>",
                    "<a href="+"/ordermgr/control/createOrderMainScreen"+">创建订单</a>",
                    "<a href="+"/ordermgr/control/findOrders"+">查找订单</a>"
                    ]},
                {"memberManagement" : [
                    "<a href="+"/partymgr/control/findparty"+">会员查找</a>",
                    "<a href="+"/partymgr/control/showclassgroups"+">会员分类列表</a>",
                    "<a href="+"/partymgr/control/FindSecurityGroup"+">安全组列表</a>"
                    ]},
                {"storeManagement" : [
                    "<a href="+"/Store/control/main"+">店铺</a>",
                    "<a href="+"/Store/control/FindProductStoreRoles"+">角色</a>"
                    ]},
                {"warehouseManagement":[
                    "<a href="+"/warehouse/control/inventoryMain"+">库存</a>",
                    "<a href="+"/warehouse/control/configurationMain"+">仓库配置</a>"
                    ]},
                {"posCashRegister":[
                    ]},
                {"dataReport":[
                    "<a href="+"/reports/control/InventoryReportFormsPage"+">库存报表</a>",
                    "<a href="+"/reports/control/main"+">销售报表</a>"
                    ]}
                ];

        //生成二级菜单列表
        var yiwillHeader = document.getElementById('yiwill_header');
        var yiwillHeaderUl = document.getElementById('yiwill_header_ul');
        var yiwillHeaderSecondHeader = document.getElementById('yiwill_header_second_header');
        var aList = yiwillHeaderUl.getElementsByTagName('a');
        var secondAList = yiwillHeaderSecondHeader.getElementsByTagName('a');

        yiwillHeader.style.display = 'block';

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
       var firstTitleMap = [
           "catalog",
           "ordermgr",
           "partymgr",
           "Store",
           "warehouse",
           "wpos",
           "reports"
       ];
       for(var i= 0; i<firstTitleMap.length; i++){
          if(firstTitleMap[i].length > 0){
              if(window.location.pathname.substr(1,firstTitleMap[i].length) === firstTitleMap[i]){
                 var thisIndex,
                     thisCurrent;
                 for(var f=0; f<aList.length; f++){
                    var aListHref = aList[f].getAttribute('href')
                    if(aListHref.indexOf(firstTitleMap[i]) > -1){
                        thisIndex = f;
                        thisCurrent = aList[f].getAttribute('data-id');
                    }
                 }
                 var mapLength = map[thisIndex][thisCurrent];
                 for(var s = 0;s<mapLength.length; s++){
                     contentList += '<li>'+ mapLength[s] +'</li>'
                 }
                 aList[i].parentNode.setAttribute('class','selected');
              }
          }
       }

       if(contentList != ''){
           yiwillHeaderSecondHeader.appendChild(parseToDOM(contentList));
       }

       //二级菜单增加选中状态
       var secondTitleMap = [
            ["CreateProduct","Classfication","BulkAddProductFeatures","FindProductListPage","FindProductPriceRulesTest"],
            ["myOrders","createOrderMainScreen","findOrders"],
            ["findparty","showclassgroups","FindSecurityGroup"],
            ["main","FindProductStoreRoles"],
            ["inventoryMain","configurationMain"],
            [],
            ["InventoryReportFormsPage","main"]
       ];

       for(var i= 0; i<secondTitleMap.length; i++){
            var secondTitleMapList = secondTitleMap[i];
            if(secondTitleMapList.length > 0){
                for(var j = 0; j<secondTitleMapList.length ; j++){
                    if(window.location.pathname.slice(-(secondTitleMapList[j].length)) === secondTitleMapList[j]){
                       for(var m = 0;m<secondAList.length; m++){
                           secondAList[m].parentNode.removeAttribute('class');
                       }
                      secondAList[j] && secondAList[j].parentNode.setAttribute('class','selected');
                    }
                }
            }
       }
    })()

</script>

