

	<title>product list</title>
<div id="product_list">
	<div class="product_list_content_wap">
		<div class="product_list_content">
			<div class="product_list_path">
				<a href="javascript:void(0)">鍚庡彴绠＄悊</a>
				<span>></span>
				<a href="javascript:void(0)">浜у搧绠＄悊</a>
				<span>></span>
				<a href="javascript:void(0)">鍩虹淇℃伅缁存姢</a>
			</div>
			<div class="product_list_search">
				<div class="product_list_title">
					<span>鍩虹淇℃伅鏌ヨ</span>
				</div>
				<div class="product_list_condition">
					<form class="search_form">
						<div class="base_condition">
							<div class="product_flag">
								<span>浜у搧鏍囩ず</span>
								<input class="product_flag_input" type="text" name="product_flag_input" id="productId"/>
							</div>
							<div class="product_type">
								<span>浜у搧绫诲瀷</span>
								<select class="product_type_select" type="text" name="product_type_select" id="productTypeId">
									<option value=""></option>
									<option value="FINISHED_GOOD">FINISHED_GOOD</option>
									<option value="FIXED_ASSET">FIXED_ASSET</option>
									<option value="ASSET_USAGE">ASSET_USAGE</option>
									<option value="AGGREGATED">AGGREGATED</option>
									<option value="DIGITAL_GOOD">DIGITAL_GOOD</option>
								</select>
							</div>
							<div class="name_type">
								<span>鍚嶇О鍨嬪彿</span>
								<input class="name_type_input" type="text" name="name_type_input" id="internalName"/>
							</div>
							<div class="brand_name">
							<span>鎵�灞炰粨</span>
							<select class="catalogues_first" name="catalogues_first" id="facilityname">
							<option value=""></option>
							</select>
							</div>
						</div>
						<div class="type_condition">
							<span>閲嶉�夊垎绫�</span>
							<select class="catalogues_first" name="catalogues_first" id="select_1">
								<option value="1">鍒嗙被閫夋嫨</option>
							</select>
							<select class="catalogues_second" name="catalogues_second" id="select_2" >
								
							</select>
							<select class="catalogues_third" name="catalogues_third" id="select_3">
				
							</select>
						</div>
						<div class="time_condition">
							<span>鍒涘缓璁㈠崟鏃堕棿</span>
							<div><input type="text" class="some_class" value="" id="some_class_1"/></div>
							
							<em>-</em>
							<div>	<input type="text" class="some_class" value="" id="some_class_2"/></div>
						</div>
					</form>
					<div class="search_list">
						<a href="javascript:LookupProductInfo();" class="search_btn">鎼滅储</a>
					</div>
				</div>
			</div>
			<div class="product_list_detail" style="height:700px; overflow:scroll; ">
				<div class="product_list_detail_title">
					<span>浜у搧鍒楄〃</span>
					<div class="excel_list">
						<a href="javascript:method5('product_list_table');" class="export" >EXCEL</a>
						<a id="dlink"  style="display:none;"></a>
					</div>
				</div>
				<table class="product_list_table" id="product_list_table" >
					<thead>
					<tr>
						<th>鎺掑簭</th>
						<th>浜у搧鏍囩ず</th>
						<th>鍚嶇О鍨嬪彿</th>
						<th>鍒嗙被</th>
						<th>鍝佺墝</th>
						<th>甯佸��</th>
						<th>鎴愭湰浠稲MB</th>
						<th>甯佸��</th>
						<th>娓犻亾1浠稲MB</th>
						<th>鎿嶄綔</th>
					</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>






<script>
	function LookupProductInfo(){
	var productId = $("input[id='productId']").val();
	var productTypeId=$("#productTypeId").val(); 
	var internalName = $("input[id='internalName']").val();
	var brandname = $("input[id='brandname']").val();
	var begintime = $("input[id='some_class_1']").val();
	var endtime = $("input[id='some_class_2']").val();
	
	var productCategoryId = $("#select_3").children('option:selected').val();  
	var productCategoryIdname = $("#select_3").children('option:selected').text();
	var facilityname = $("#facilityname").children('option:selected').text();
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        url: '/catalog/control/LookupProductInfoAjax',
	        data: {
	        	productId:productId,
				productTypeId:productTypeId,
				internalName:internalName,
				productCategoryId:productCategoryId,
				productCategoryIdname:productCategoryIdname,
				facilityname:facilityname,
				brandname:brandname,
				begintime:begintime,
				endtime:endtime,
					},
	        success: function (data) {
	        console.info(data);
	        
	        var jsonText = JSON.stringify(data);
	        jsonobjects = eval(data.resultPoductPriceList);
	        
	        console.info(jsonobjects.length);
	        
	        $(".product_list_table tr:not(:first)").remove();
	        
	        
			for(k=0;k<jsonobjects.length;k++)
			{
				index=k+1;
				var newRow = "<tr><td>"+index+"</td><td>"+jsonobjects[k].productId+"</td><td>"+jsonobjects[k].internalName+"</td><td>"+jsonobjects[k].productCategoryId+"</td><td>"+jsonobjects[k].brandName+"</td><td>---</td><td>---</td><td>---</td><td>---</td><td><a>淇敼</a><a >棰勮</a></td></tr>";
				
				$(".product_list_table tr:last").after(newRow);
			}
	        	
	        	
	       	}	
    	});
	}
	
		function LookupwarehouseInfo(){
		$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/LookupWarehouseInfoAjax',
	        data: JSON.stringify({
					}),
	        success: function (data) {
	        var jsonText = JSON.stringify(data);
	        	alert(jsonText);
	       	}	
    	});
	}
	$(document).ready(function(){  
     $.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url:'/catalog/control/ListGivenClassfication?categoryID=DefaultRootCategory',
	        success: function (data) {
	        jsonobjects = eval(data.categoryList);
	        
	        for(k=0;k<jsonobjects.length;k++)
			{
				categoryID=jsonobjects[k].categoryID;
				categoryName=jsonobjects[k].categoryName;
				strt="<option value='"+categoryID+"'>"+categoryName+"</option>"
				$("#select_1").append(strt); 
			}
	       	}	
    	});
    	
    	$.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url: '/catalog/control/LookupWarehouseInfoAjax',
	        data: JSON.stringify({
					}),
	        success: function (data) {
	        jsonobjects = eval(data.resultWarehouseInfoList);
	        
	        for(k=0;k<jsonobjects.length;k++)
			{
				strt="<option value='"+data.resultWarehouseInfoList[k]+"'>"+data.resultWarehouseInfoList[k]+"</option>"
				$("#facilityname").append(strt); 
			}
	        
	        
	        
	       	}	
    	});
    	
    	
    	
    	
});  
	    $("#select_1").change(function () {  
            var ss = $(this).children('option:selected').val();  
            $.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url:'/catalog/control/ListGivenClassfication?categoryID='+ss,
	        success: function (data) {
	          $("#select_2").empty(); 
	          $("#select_2").val("");
	          strt="<option value=''></option>"
			  $("#select_2").append(strt); 
	          $("#select_2 option[text='']").attr("selected", "selected"); 
	          
	          $("#select_3").find("option").remove();
	          strt="<option value=''></option>"
			  $("#select_3").append(strt); 
	          $("#select_3 option[text='']").attr("selected", "selected"); 
	        jsonobjects = eval(data.categoryList);
	        
	        for(k=0;k<jsonobjects.length;k++)
			{
				categoryID=jsonobjects[k].categoryID;
				categoryName=jsonobjects[k].categoryName;
				strt="<option value='"+categoryID+"'>"+categoryName+"</option>"
				$("#select_2").append(strt); 
			}
	       	}	
    	});
        }); 
        
        $("#select_2").change(function () {  
            var ss = $(this).children('option:selected').val();  
            $.ajax({
	        type: 'post',
	        dataType: 'json',
	        contentType: "application/json;charset=utf-8",
	        url:'/catalog/control/ListGivenClassfication?categoryID='+ss,
	        success: function (data) {
	        
	        $("#select_3").empty(); 
	        $("#select_3").val("");
	        strt="<option value=''></option>"
			$("#select_3").append(strt); 
	        $("#select_3 option[text='']").attr("selected", "selected"); 
	        
	        
	        jsonobjects = eval(data.categoryList);
	        
	        
	        for(k=0;k<jsonobjects.length;k++)
			{
				categoryID=jsonobjects[k].categoryID;
				categoryName=jsonobjects[k].categoryName;
				strt="<option value='"+categoryID+"'>"+categoryID+"</option>"
				$("#select_3").append(strt); 
			}
	       	}	
    	});
        }); 
        
        
        
        
        //琛ㄦ牸瀵煎嚭
        var idTmr;  
        function  getExplorer() {  
            var explorer = window.navigator.userAgent ;  
            //ie  
            if (explorer.indexOf("MSIE") >= 0) {  
                return 'ie';  
            }  
            //firefox  
            else if (explorer.indexOf("Firefox") >= 0) {  
                return 'Firefox';  
            }  
            //Chrome  
            else if(explorer.indexOf("Chrome") >= 0){  
                return 'Chrome';  
            }  
            //Opera  
            else if(explorer.indexOf("Opera") >= 0){  
                return 'Opera';  
            }  
            //Safari  
            else if(explorer.indexOf("Safari") >= 0){  
                return 'Safari';  
            }  
        } 
        function method5(tableid) {  
            if(getExplorer()=='ie')  
            {  
                var curTbl = document.getElementById(tableid);  
                var oXL = new ActiveXObject("Excel.Application");  
                var oWB = oXL.Workbooks.Add();  
                var xlsheet = oWB.Worksheets(1);  
                var sel = document.body.createTextRange();  
                sel.moveToElementText(curTbl);  
                sel.select();  
                sel.execCommand("Copy");  
                xlsheet.Paste();  
                oXL.Visible = true;  
  
                try {  
                    var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");  
                } catch (e) {  
                    print("Nested catch caught " + e);  
                } finally {  
                    oWB.SaveAs(fname);  
                    oWB.Close(savechanges = false);  
                    oXL.Quit();  
                    oXL = null;  
                    idTmr = window.setInterval("Cleanup();", 1);  
                }  
  
            }  
            else  
            {  
                tableToExcel(tableid)  
            }  
        }
        function Cleanup() {  
            window.clearInterval(idTmr);  
            CollectGarbage();  
        }  
        var tableToExcel = (function (table) {
        var uri = 'data:application/vnd.ms-excel;base64,'
        , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
        , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
        , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
        return function (table, name, filename) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }

            document.getElementById("dlink").href = uri + base64(format(template, ctx));
            document.getElementById("dlink").download = "浜у搧鍒楄〃"+getNowFormatDate();//杩欓噷鏄叧閿墍鍦�,褰撶偣鍑讳箣鍚�,璁剧疆a鏍囩鐨勫睘鎬�,杩欐牱灏卞彲浠ユ洿鏀规爣绛剧殑鏍囬浜�
            document.getElementById("dlink").click();

        }
    })()
        
    function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}    
</script>


<script>/*
window.onerror = function(errorMsg) {
	$('#console').html($('#console').html()+'<br>'+errorMsg)
}*/

$.datetimepicker.setLocale('en');

$('#datetimepicker_format').datetimepicker({value:'2015/04/15 05:03', format: $("#datetimepicker_format_value").val()});
console.log($('#datetimepicker_format').datetimepicker('getValue'));

$("#datetimepicker_format_change").on("click", function(e){
	$("#datetimepicker_format").data('xdsoft_datetimepicker').setOptions({format: $("#datetimepicker_format_value").val()});
});
$("#datetimepicker_format_locale").on("change", function(e){
	$.datetimepicker.setLocale($(e.currentTarget).val());
});

$('#datetimepicker').datetimepicker({
dayOfWeekStart : 1,
lang:'en',
disabledDates:['1986/01/08','1986/01/09','1986/01/10'],
startDate:	'1986/01/05'
});
$('#datetimepicker').datetimepicker({value:'2015/04/15 05:03',step:10});

$('.some_class').datetimepicker();

$('#default_datetimepicker').datetimepicker({
	formatTime:'H:i',
	formatDate:'d.m.Y',
	//defaultDate:'8.12.1986', // it's my birthday
	defaultDate:'+03.01.1970', // it's my birthday
	defaultTime:'10:00',
	timepickerScrollbar:false
});

$('#datetimepicker10').datetimepicker({
	step:5,
	inline:true
});
$('#datetimepicker_mask').datetimepicker({
	mask:'9999/19/39 29:59'
});

$('#datetimepicker1').datetimepicker({
	datepicker:false,
	format:'H:i',
	step:5
});
$('#datetimepicker2').datetimepicker({
	yearOffset:222,
	lang:'ch',
	timepicker:false,
	format:'d/m/Y',
	formatDate:'Y/m/d',
	minDate:'-1970/01/02', // yesterday is minimum date
	maxDate:'+1970/01/02' // and tommorow is maximum date calendar
});
$('#datetimepicker3').datetimepicker({
	inline:true
});
$('#datetimepicker4').datetimepicker();
$('#open').click(function(){
	$('#datetimepicker4').datetimepicker('show');
});
$('#close').click(function(){
	$('#datetimepicker4').datetimepicker('hide');
});
$('#reset').click(function(){
	$('#datetimepicker4').datetimepicker('reset');
});
$('#datetimepicker5').datetimepicker({
	datepicker:false,
	allowTimes:['12:00','13:00','15:00','17:00','17:05','17:20','19:00','20:00'],
	step:5
});
$('#datetimepicker6').datetimepicker();
$('#destroy').click(function(){
	if( $('#datetimepicker6').data('xdsoft_datetimepicker') ){
		$('#datetimepicker6').datetimepicker('destroy');
		this.value = 'create';
	}else{
		$('#datetimepicker6').datetimepicker();
		this.value = 'destroy';
	}
});
var logic = function( currentDateTime ){
	if (currentDateTime && currentDateTime.getDay() == 6){
		this.setOptions({
			minTime:'11:00'
		});
	}else
		this.setOptions({
			minTime:'8:00'
		});
};
$('#datetimepicker7').datetimepicker({
	onChangeDateTime:logic,
	onShow:logic
});
$('#datetimepicker8').datetimepicker({
	onGenerate:function( ct ){
		$(this).find('.xdsoft_date')
			.toggleClass('xdsoft_disabled');
	},
	minDate:'-1970/01/2',
	maxDate:'+1970/01/2',
	timepicker:false
});
$('#datetimepicker9').datetimepicker({
	onGenerate:function( ct ){
		$(this).find('.xdsoft_date.xdsoft_weekend')
			.addClass('xdsoft_disabled');
	},
	weekends:['01.01.2014','02.01.2014','03.01.2014','04.01.2014','05.01.2014','06.01.2014'],
	timepicker:false
});
var dateToDisable = new Date();
	dateToDisable.setDate(dateToDisable.getDate() + 2);
$('#datetimepicker11').datetimepicker({
	beforeShowDay: function(date) {
		if (date.getMonth() == dateToDisable.getMonth() && date.getDate() == dateToDisable.getDate()) {
			return [false, ""]
		}

		return [true, ""];
	}
});
$('#datetimepicker12').datetimepicker({
	beforeShowDay: function(date) {
		if (date.getMonth() == dateToDisable.getMonth() && date.getDate() == dateToDisable.getDate()) {
			return [true, "custom-date-style"];
		}

		return [true, ""];
	}
});
$('#datetimepicker_dark').datetimepicker({theme:'dark'})


</script>





























