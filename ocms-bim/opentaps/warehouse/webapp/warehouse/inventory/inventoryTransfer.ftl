<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>inventory report</title>
	<link type="text/css" rel="stylesheet" href="../css/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="../css/bootstrap-select.css" />
	<link type="text/css" rel="stylesheet" href="../css/normalize.css" />
	<link type="text/css" rel="stylesheet" href="../css/yw_main.css" />
	<link rel="stylesheet" type="text/css" href="../css/inventory_transfer.css">
	<link rel="stylesheet" type="text/css" href="../css/select2.css">
</head>
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/underscore.js"></script>
<script type="text/javascript" src="../js/bootstrap.js"></script>
<script type="text/javascript" src="../js/bootstrap-select.js"></script>
<script type="text/javascript" src="/commondefine_js/select2/select2.js"></script>
<script type="text/javascript" src="../js/yw_comm.js"></script>
<script type="text/javascript" src="../js/yw_main.js"></script>

<body>

<div id="inventory_transfer">	
	<span  hidden id="pageNum"></span>
	<span  hidden id="currentNum"></span>
	<span  hidden id="orderBy"></span>
	<div class="inventory_transfer_handle">
		<div class="inventory_transfer_sreach">
			<div>
				<span class="inventory_transfer_name">Product Id</span>
				<select id="productIdNum">
					<option value="1">Contain</option>
					<option value="2">Beginning</option>
					<option value="3">Equal</option>
				</select><input type="text" id="productId" />
				<input type="checkbox" class="checklist" id="productIdStatu" />
				<span>Ignore Case</span>
			</div>
			<div >
				<span class="inventory_transfer_name">Brand Name</span>
				<select id="brandNameNum">
					<option value="1" >Contain</option>
					<option value="2">Beginning</option>
					<option value="3">Equal</option>
				</select><input type="text" id="brandName">
				<input type="checkbox" class="checklist" id="brandNameStatu" value="">
				<span>Ignore Case</span>
			</div>
			<div>
				<span class="inventory_transfer_name">Model</span>
				<select id="internalNameNum">
					<option value="1">Contain</option>
					<option value="2">Beginning</option>
					<option value="3">Equal</option>
				</select><input type="text" id="internalName">
				<input type="checkbox" class="checklist" id="internalNameStatu" value="">
				<span>Ignore Case</span>
				<a id="sreach" onclick="searchProduct('1','20','',this)"  href="javascript:void(0)">Query</a>
			</div>
			
		</div>
		<div id="transDiv" hidden>
		<div class="inventory_transfer_result" >
		<nav aria-label="...">
		  <ul class="pagination" id="fenye">
		  
		  </ul>
		</nav>
			<table class="inventory_transfer_table">
				<thead>
					<tr>
						<th>
							<input type="checkbox" id="selectAll" onclick="selectAll(this)">
							<span>Select All</span>
						</th>
						<th><a href="javascript:searchProduct('1','20','x.product_Id')">Product Id</a></th>
						<th><a href="javascript:searchProduct('1','20','x.BRAND_NAME')">Brand Name</th>
						<th><a href="javascript:searchProduct('1','20','x.INTERNAL_NAME')">Model</th>
						<th><a href="javascript:searchProduct('1','20','y.productAttributes')">Products Attributes</th>
						<th><a href="javascript:searchProduct('1','20','x.QUANTITY_ON_HAND_TOTAL')">QTY</th>
						<th>Inventory Transfers</th>
					</tr>
				</thead>
			
				<tbody id="tbody">
					
				</tbody>
				
			</table>
		</div>
		<div class="inventory_transfer_info">
			<div class="selected_product">
				<div id="product_id">Product Id</div>
				<ul class="selected_product_id" id="productUl">
					
				</ul>
			</div>
			<div hidden>
				<span>Transfer Status</span>
				<select class="transfer_status" id="statusId">
					<option value="IXF_REQUESTED">Requests for Proposals</option>
					<!-- <option value="IXF_SCHEDULED">Already plans</option>
					<option value="IXF_EN_ROUTE">On the way</option>
					<option value="IXF_CANCELLED">Already canceled</option> -->
				</select>
			</div>
			<div hidden>
				<span>Delivery Date</span>
				<input type="text" class="tarnsfer_date" id="tarnsfer_date">
			</div>
			<div>
				<span>Place</span>
				<select class="transfer_site" id="place"  class="selectpicker" data-live-search="true">
					
				</select>				
			</div>
			<a class="submit" onclick="transfer(this)" href="javascript:void(0)">confirm</a>
		</div>
	</div>
</div>
</div>
</body>

<script type="text/javascript" >

	confirmFlag = true;
	queryFlag = true;
		
	//全选
	function selectAll(obj){
		//判断当前是否选中
		if(obj.checked){
			
			jQuery("#tbody input[type='checkbox']").each(function (){
				this.checked=true;
				selected(jQuery(this).attr("id").substring(5),this); 
			})
		}else{
			jQuery("#tbody input[type='checkbox']").each(function (){
				this.checked=false;
				selected(jQuery(this).attr("id").substring(5),this); 
			})
		} 
		
	}


	//转运
	function transfer(target){	
		if(confirmFlag){
			confirmFlag = false;
			//包装参数
			var productId = "";
			var transNum = "";
			var maxNumss = "";
			var ifPass=true;
			
			var facilityIdTo = jQuery('#place').val();
			var statusId = jQuery("#statusId").val();
			jQuery("#tbody .transfer_count").each(function (){
				var id=jQuery(this).attr('id');
				if(jQuery("#"+id+"span").attr('isclear')=='yes'){
					jQuery("#"+id).focus();
					ifPass=false;
					return false;
				}
			})
			//获得参数
			 	jQuery("#productUl li").each(function (){  
		            var text = jQuery(this).text().split(":")[0];
		            var nums = jQuery(this).attr("name");
		            //限制只能输入数字
		            var regu='^[0-9]*$';
		            var re = new RegExp(regu);
		            if (nums.search(re) == -1) {
		            	jQuery("#"+text).focus();
		            	jQuery("#"+text+"span").text("please enter the numbers").show();
		            	jQuery(this).remove();
		            	ifPass=false;
		            	return false;  
		            }
		            
		            var maxNums = jQuery(this).attr("value");
		            
		            if(productId == ""){
		            	productId = productId + text
		            	transNum = transNum + nums
		            	maxNumss = maxNumss + maxNums
		            }else{
		            	productId = productId + "," + text
		            	transNum = transNum + "," + nums
		            	maxNumss = maxNumss + ","+ maxNums
		            }
		            
		         }); 
			if(!ifPass){
				confirmFlag = true;
				return false;
			}
			var data = {"productId":productId,
					"transNum":transNum,
					"nums":maxNumss,
					"facilityId":facility_tran_001,
					"facilityIdTo":facilityIdTo,
					"statusId":statusId
			}
			if(productId != ""){
				target&&jQuery.showLoading(target);
				//发送请求
				jQuery.ajax({
					type:"post",
					url:"/warehouse/control/masTransfer",
					contentType: "application/json;charset=utf-8",
				    dataType:"json",
					data:JSON.stringify(data),
					success:function(data){
						if(data.resultCode == 1){
							//跳转页面
							location.href="/warehouse/control/FindFacilityTransfers?facilityId="+facility_tran_001;
						}else{
							alert(data.resultMsg);
							confirmFlag = true;
						}
						target&&jQuery.hideLoading(target);
					}
					})
						
			}else{
				alert("Please choose to transport products")
				confirmFlag = true;
			}
			
		}
		
	}

     this.onload = function(){
    	 jQuery("#place").html("");
    	 jQuery.ajax({
 			type:"post",
 			url:"/warehouse/control/findAllFacility",
 			contentType: "application/json;charset=utf-8",
 		    dataType:"json",
 			success:function(data){
 				if(data.resultCode == 1){
 					var result = data.result;
	 					for(var i=0;i<result.length;i++){
	 						if(result[i] != facility_tran_001){
	 							jQuery("#place").append("<option value='"+result[i]+"'>"+result[i]+"</option>") 
	 						}
	 					}
	 					 $("#place").select2();
 					
 				}else{
 					alert(data.resultMsg);
 				}
 			}
 		})
     }
	//失去焦点，li列表增增元素
	function getProduct(productId,quantityOnHandTotal){
			var inputText = jQuery("#"+productId).val();
			if(inputText == ""|| inputText == "0" || inputText.indexOf("-") != -1 || inputText.indexOf(".") != -1){
				jQuery("#"+productId+"span").attr('isclear','yes').text('Please enter a positive integer').show();
				jQuery("#"+productId+"li").remove();
				return;
			}
			var regu='^[0-9]*$';
	        var re = new RegExp(regu);
	        if (inputText.search(re) == -1) {
	        	jQuery("#"+productId+"span").attr('isclear','yes').text('Please enter the numbers').show();
				jQuery("#"+productId+"li").remove();				
	        	return;	
			}
	        if(inputText>quantityOnHandTotal){
	        	jQuery("#"+productId+"span").attr('isclear','yes').text('the number is not greater than QTY').show();
				jQuery("#"+productId+"li").remove();
	        	return;	
	        }
	        jQuery("#"+productId+"span").attr('isclear','no');
			jQuery("#"+productId+"span").hide();
			//判断是否存在
			jQuery("#productUl").each(function (){  
		            var text = jQuery(this).text(); 
		          
		            if(text.indexOf(productId) < 0){
		       		 jQuery("#productUl").append("<li value='"+quantityOnHandTotal+"' name='"+inputText+"' id='"+productId+"li'>"+productId+":"+inputText+"</li>")

		            }
		            if(text.indexOf(productId) > 0){
		            	jQuery("#"+productId+"li").attr("name",inputText)
		            	jQuery("#"+productId+"li").text(productId+":"+inputText)
		            }
		          
		     });  
	    
	}
	
	//选中某一产品，显示input框
	function selected(productId,obj){
		if(obj.checked){
			//显示出文本框
            jQuery("#"+productId).show();
        	jQuery("#"+productId+"span").text('').attr('isclear','no'); 
        }else{
        	//隐藏文本框，清空内容
        	jQuery("#"+productId).val("");
        	jQuery("#"+productId).hide();
        	jQuery("#"+productId+"span").text('').attr('isclear','no'); 
        	//去除列表中的元素
        	jQuery("#"+productId+"li").remove();
        }
	}
	//查询产品数据
	function searchProduct(pageNum,currentNum,orders,target){
		if(queryFlag){
			queryFlag = false;
			
			var orderBy = jQuery("#orderBy").val();
			if(orders != null && orders != ""){
				if(orderBy == orders){
					orders = orders + " DESC"
					jQuery("#orderBy").val(orders);
				}else{
					jQuery("#orderBy").val(orders);
				}
			}
			//获取查询数据
			var productId = jQuery("#productId").val();
			var productIdNum = jQuery("#productIdNum").val();
			var productIdStatu = "";
			if(jQuery('#productIdStatu').is(':checked')) {
				productIdStatu = "1";
			}else{
				productIdStatu = "0";
			}
	
			var brandName = jQuery("#brandName").val();
			var brandNameNum = jQuery("#brandNameNum").val();
			var brandNameStatu = "";
			if(jQuery('#brandNameStatu').is(':checked')) {
				brandNameStatu = "1";
			}else{
				brandNameStatu = "0";
			}
	
			var internalName = jQuery("#internalName").val();
			var internalNameNum = jQuery("#internalNameNum").val();
			var internalNameStatu = "";
			if(jQuery('#internalNameStatu').is(':checked')) {
				internalNameStatu = "1";
			}else{
				internalNameStatu = "0";
			}
	
	
			var data = {"productId":productId,
						"productIdNum":productIdNum,
						"productIdStatu":productIdStatu,
						"brandName":brandName,
						"brandNameNum":brandNameNum,
						"brandNameStatu":brandNameStatu,
						"internalName":internalName,
						"internalNameNum":internalNameNum,
						"internalNameStatu":internalNameStatu,
						"pageNum":pageNum,
						"pageTotal":currentNum,
						"facilityId": facility_tran_001,
						"orderBy":jQuery("#orderBy").val()
			}
	
		
			//清空数据
			jQuery("#tbody").html("");
			target&&jQuery.showLoading(target);
			//发送请求
			jQuery.ajax({
				type:"post",
				url:"/warehouse/control/findProductByFacility",
				contentType: "application/json;charset=utf-8",
			    dataType:"json",
				data:JSON.stringify(data),
				success:function(data){
					target&&jQuery.hideLoading(target);
					if(data.resultCode == 1){
						var result = data.result;
						for(var i=0;i<result.length;i++){
							var productId = result[i].productId;
							var brandName = result[i].brandName;
							var description = result[i].description;
							if(description == null){
								description = "";
							}
							if(brandName == null){
								brandName = "";
							}
							var internalName = result[i].internalName;
							if(internalName == null){
								internalName = "";
							}
							var quantityOnHandTotal = result[i].quantityOnHandTotal;
							jQuery("#tbody").append("<tr> <td> <input type='checkbox' id='check"+productId+"' onclick='selected(\""+productId+"\","+"this"+")'> </td> <td>"+productId+"</td> <td>"+brandName+"</td> <td>"+internalName+"</td> <td>"+description+"</td> <td>"+quantityOnHandTotal+"</td>   <td ><input type='text' class='transfer_count'  onchange=\"getProduct('"+productId+"',"+quantityOnHandTotal+")\" hidden id='"+productId+"'></td><td> <span style='color:red' id='"+productId+"span' hidden isclear='no'></span></td> </tr>");
						}
						//分页
						jQuery("#fenye").html("");
						var pageNum = data.pageNum;
						var totalNum = data.totalNum;
						
						var provious = pageNum-1;
						var nextPage = pageNum+1;
						
						var startPage = data.startPage;
						var endPage = data.endPage;
						if(totalNum > 0){
							jQuery("#fenye").append("<li ><a href=\"javascript:searchProduct('1','20')\" ><span aria-hidden='true'>Home Page</span></a></li>")
							if(pageNum > 1){
								jQuery("#fenye").append("<li ><a href=\"javascript:searchProduct('"+provious+"','20')\" ><span aria-hidden='true'>Previous Page</span></a></li>")
							}
							for(var i=startPage;i<=endPage;i++){
								
								
	
								if(i == pageNum){
									jQuery("#fenye").append("<li class='active'><a href=\"javascript:searchProduct('"+i+"','20')\">"+i+" <span class='sr-only'>"+i+"</span></a></li>		    ")
								}else{
									jQuery("#fenye").append("<li ><a href=\"javascript:searchProduct('"+i+"','20')\">"+i+" <span class='sr-only'>"+i+"</span></a></li>		    ")
								}
								
								
	
							}
							if(pageNum < totalNum){
								jQuery("#fenye").append("<li ><a href=\"javascript:searchProduct('"+nextPage+"','20')\"><span aria-hidden='true'>Next Page</span></a></li>")
	
							}
							jQuery("#fenye").append("<li ><a href=\"javascript:searchProduct('"+totalNum+"','20')\"><span aria-hidden='true'>End Page</span></a></li>")
	
						}
						
					}else{
						alert(data.resultMsg);
					}
					queryFlag = true;
				}
			})
	
	
			//显示div
			jQuery("#transDiv").show();
			}
		}
	
</script>
</html>