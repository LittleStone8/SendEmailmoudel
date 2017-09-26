<div class="storeGroups" id="storeGroups" style="display: none;">
    <label class="main_store_groups">Edit Store Group and Channel</label>
    <div class="main_store_groups_content">
        <span id="main_store_groups_btn">Choose store group</span>
    </div>
</div>

<div class="store_dialog" id="store_dialog">
    <div class="fixed_screen"></div>
    <div class="store_dialog_box">
        <div class="store_dialog_tit">Store Group</div>
        <div class="store_dialog_content">
            <div class="topIndex" id="topIndex">
                <div class="group">
                    <span><input type="checkbox" data-id="AGENT" />Agent</span>
                </div>
                <div class="group">
                    <span><input type="checkbox" data-id="RETAIL" />Retail</span>
                </div>
                <div class="group">
                    <span><input type="checkbox" data-id="WHOLESALE"/>WhoseSale</span>
                </div>
                <div class="group">
                    <span><input type="checkbox" data-id="B2R" />B2R</span>
                </div>
                <!----<div class="group">
                    <span><input type="checkbox" data-id="DEFAULT"  />Default</label>
                </div>---->
            </div>
            <div class="btmIndex" id="btmIndex">
                <div class="group">
                    <span><input type="radio" name="store_group" data-id="MTN_S"  />MTN</span>
                </div>
                <div class="group">
                    <span><input type="radio" name="store_group" data-id="BANANA_S"  />BANANA</span>
                </div>
                <div class="group">
                    <span><input type="radio" name="store_group" data-id="AIRTEL_S"  />AIRTEL</span>
                </div>
                <div class="group">
                    <span><input type="radio" name="store_group" data-id="UTL_S"  />UTL</span>
                </div>
                <div class="group">
                    <span><input type="radio" name="store_group" data-id="TEST"  />TEST</span>
                </div>
            </div>
        </div>
        <div class="store_dialog_operator">
            <a href="javascript:void(0);" class="confirm" id="confirm">Confirm</a>
            <a href="javascript:void(0);" class="cancel" id="cancel">Cancel</a>
        </div>
    </div>
</div>
<div class="error_tips" id="error_tips">
    <div class="fixed_screen"></div>
    <div class="error_tips_box">
        <div class="error_tips_title">Err info.</div>
        <div class="error_tips_content">
            <span></span>
        </div>
        <div class="error_tips_operator">
            <a href="javascript:void(0);" class="confirm" id="tipConfirm">Confirm</a>
        </div>
    </div>
</div>
<script type="text/javascript">
    window.onload = function(){
        var storeId = (window.location.search).split('=')[1];

        var storeGroups = document.getElementById("storeGroups");
        var main_store_groups_btn = document.getElementById("main_store_groups_btn");
        var store_dialog = document.getElementById("store_dialog");
        var errorTips = document.getElementById("error_tips");
        var confirm = document.getElementById("confirm");
        var tipConfirm = document.getElementById("tipConfirm");
        var cancel = document.getElementById("cancel");

        var inputList = store_dialog.getElementsByTagName("input");
        var topIndex = document.getElementById("topIndex");
        var btmIndex = document.getElementById("btmIndex");
        var topIndexInput = topIndex.getElementsByTagName("input");
        var btmIndexInput = btmIndex.getElementsByTagName("input");

        var checkedInput = [];

        if(storeId){
            storeGroups.style.display = "block";
        }

        main_store_groups_btn.onclick = function () {

            for(var i=0; i<inputList.length; i++){
                inputList[i].checked = false;
            }
            store_dialog.style.display = "block";

            jQuery.ajax({
              url:'/report/control/SearchStoreAndGroupsByConditions',
              type:'post',
              data : {
                StoreId:storeId
              },
              success: function(result) {
                var dataResponse = result;
                if(dataResponse.responseMessage == 'success'){
                    var StoreAndSGroup = dataResponse.StoreAndSGroup;

                    //如果没有选中过，让它默认选中上面四个
                    if(StoreAndSGroup.length == 0){
                        for(var i=0; i<topIndexInput.length; i++){
                            topIndexInput[i].checked = true;
                        }
                        return false;
                    }
                    for(var i=0; i<StoreAndSGroup.length; i++){
                        checkedInput.push(StoreAndSGroup[i].storeGroupID);
                        for(var j=0; j<inputList.length; j++){
                            if(inputList[j].getAttribute("data-id") == StoreAndSGroup[i].storeGroupID){
                                inputList[j].checked = true;
                            }
                        }
                    }
                }else if(dataResponse.responseMessage == 'error'){
                    store_dialog.style.display = "none";

                    errorTips.children('span').text('').text(dataResponse.errorMessage);
                    store_dialog.style.display = "block";
                }
              }
            });
        }

        cancel.onclick = function(){
            store_dialog.style.display = "none";
        }
        tipConfirm.onclick = function(){
            errorTips.style.display = "none";
        }

        confirm.onclick = function(){

            var flag = false;
            var list = [];

            function contains(arr, obj) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i] === obj) {
                        return true;
                    }
                }

                return false;
            }

            for(var i=0; i<inputList.length; i++){
                if(inputList[i].checked){
                    list.push({
                        sGroupId : inputList[i].getAttribute("data-id")
                    })
                }
            }

            if(list.length == checkedInput.length){
                for(var i=0; i<list.length; i++){
                    if(contains(checkedInput ,list[i])){
                        flag = true;
                    }
                }
            }

            if(flag == true){
                store_dialog.style.display = "none";

                return false;
            }

            var storeList = 0;
            var priceList = 0;
            for(var k = 0; k<topIndexInput.length; k++){
                if(topIndexInput[k].checked == true){
                    priceList++;
                }
            }
            for(var m = 0; m<btmIndexInput.length; m++){
                if(btmIndexInput[m].checked == true){
                    storeList++;
                }
            }

            if(storeList != 1){
                var spanText = errorTips.getElementsByTagName('span')[0];
                spanText.innerText  = 'Only choose a store';
                errorTips.style.display = "block";

                return false;
            }

            if(priceList < 1){

                var spanText = errorTips.getElementsByTagName('span')[0];
                spanText.innerText  = 'Please choose price';
                errorTips.style.display = "block";

                return false;
            }

            jQuery.ajax({
              url:'/report/control/SGroupsBatchUpdates',
              type:'post',
              contentType: "application/json;charset=utf-8",
              data: JSON.stringify({
                 StoreId:storeId,
                 SGroupJSONArr:list
              }),
              success: function (data) {
                if(data.responseMessage == 'success'){
                    store_dialog.style.display = "none";
                }else if(data.responseMessage == 'error'){

                    var spanText = errorTips.getElementsByTagName('span')[0];

                    spanText.innerText  = data.errorMessage;
                    errorTips.style.display = "block";
                }
              }
            });
        }
    }
</script>