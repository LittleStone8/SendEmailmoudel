<link rel="stylesheet" type="text/css" href="/commondefine_css/category/promotion.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/need/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_js/layDate/skins/blue/laydate.min.css">
<link rel="stylesheet" type="text/css" href="/commondefine_css/ui-dialog.css">
<div id="batchPrice">
	<div class="batchManagePrice_box">
	   <div class="left_column">
			<div class="left_column_tit">Shortcuts</div>
			<div class="left_column_content"></div>
		</div>
    <div class="center_content">
       <div id="batchManagePrice">
           <div class="tips_box">
				<div class="tips_box_tit">Batch Manage Price</div>
				<div class="tips_box_content clearfix">
					<p>Please click the button to upload the price spreadsheet.</p>
				    <a id="operate_file_upload" href="javascript:void(0)"  class="operate_btn">
				    	Upload<input id="uploadFile" type="file" class="operate_file"/>
				    </a>
				    <div id="uploadProgress">  
                              <div id="upload_progress" class="bar" ></div>  
                    </div>
                    <ul id="upload_error">
                    </ul>
				    <div class="tips_a">
					    <p>TIPS:</p>
					    <p>1.Please upload the price spreadsheet according to the template. If you do not have the template, click the "Download" button to download it.</p>
					    <p>2.Product ID / Brand / Model / Store must be existing.</p>
				    </div>
				    <p class="tips_b">Please click the button to download the template.</p>
				    <a href="/commondefine_file/demo_price.xls"  class="operate_btn">Download</a>
				</div>
			</div>
			<div class="list_box">
			    <div class="list_box_title">Fill in the sample</div>				
				<table class="list_box_table">
					<thead>
						<tr>
							<td>Product ID</td>
							<td>Model</td>
							<td>Description</td>
							<td>Brand</td>
							<td>Store</td>
							<td>Current Price(GHS)</td>												
						</tr>
					</thead>
					<tbody id="tbody">
					<tr>
						 <td>1000</td>
						 <td>TECNO C9</td>
						 <td>Black</td>
			             <td>TECNO</td>
						 <td>Accra Retail Warehouse</td>
						 <td>599</td>
			       </tr>
			       <tr>
						 <td></td>
						 <td></td>
			             <td></td>
						 <td></td>
						 <td></td>
						 <td></td>
			       </tr>
					</tbody>
				</table>
				<div class="pageBox clearfix"></div>
				
			</div>
       </div>
       <div id="uploadBatchPrice"  style="display:none;">
	       <div class="tips_box">
					<div class="tips_box_tit">Batch Manager Price</div>			
		   </div>
		   <div class="list_box">
			    <div class="list_box_title">Please check the upload products prices:</div>				
				<table class="list_box_table">
					<thead>
						<tr>
							<td>Product ID</td>
							<td>Description</td>
							<td>Store</td>
							<td>Current Price(GHS)</td>
							<td>Last Date</td>							
						</tr>
					</thead>
					<tbody id="pre_tbody">
					
					</tbody>
				</table>
				<div class="pageBox"></div>
				<div id="priceVerify">Remeber Setting Commencement Data</div>
				<div class="price_date">
				   <span>commencement date</span>
				   <input type="text" class="price_date_input laydate-icon" id="start" data-type="start"/>
				   <span>-</span>
				   <input type="text" class="price_date_input laydate-icon" id="end" data-type="end"/>
				   <a href="javascript:void(0)" class="setting_date">Setting</a>
				</div>
			</div>
       </div>
		
    </div>
	</div>    



</div>

<script type="text/html" id="leftColumnContent">
<ul>
    {{if findProductp=='Y'}}<li><a href="/catalog/control/ProductPriceRule">Find Product Price</a></li>{{/if}}
	{{if findLog=='Y'}}<li><a href="/catalog/control/ProductPriceLog">Find Product Price Log</a></li> {{/if}}
	{{if batchManage=='Y'}}<li class="menu_active"><a href="/catalog/control/BatchManagerPrice">Batch Manage Price</a></li>{{/if}}
</ul>
</script>

<script src="/commondefine_js/ywcore.min.js"></script>


<script src="/commondefine_js/upload/jquery.ui.widget.js"></script>
<script src="/commondefine_js/upload/jquery.iframe-transport.js"></script>
<script src="/commondefine_js/upload/jquery.fileupload.js"></script>


<script src="/commondefine_js/ywcore.min.js"></script>
<script src="/commondefine_js/ywrequire.min.js"></script>
<script>
require.config({
	paths:{	
		layDate:'/commondefine_js/layDate/laydate.min'
	}
})
</script>
<script>
require(['Inherit', 'AbstractView', 'template', 'AbstractDialog'], function (Inherit, AbstractView, template) {
	
	var View = Inherit.Class(AbstractView, {
		el: 'body',
        events: {        	
        	"click #batchManagePrice .operate_btn":"showPriceFile",
        	"click #uploadBatchPrice #start":"showStartDate",
        	"click #uploadBatchPrice #end":"showEndDate",
        	"click #uploadBatchPrice .setting_date":"settingCommencementDate"
        },
        __propertys__: function () {
        	this.endMinDate="";
        	this.endStartDate="";
        	this.startMaxDate="";
        },
        initialize: function ($super) {
        	 var _self = this;
        	 $super();  
        	 this.$content=$("#batchManagePrice");
        	 this.initMenuData();    	 
        	 this.fileUploadPrice();       	 
        },
        initMenuData:function(){
        	$.ajax({
	            type: 'post',
	            dataType: 'json',
	            url: '/catalog/control/permission',	            
	            success: function (data) {
	                var dataResult = data;
	                if(dataResult.responseMessage == 'success'){
	                	if(dataResult.findLog=='Y'){
	                		$("#batchPrice").show();
	                	}
	                	$('.left_column_content').html(template("leftColumnContent",dataResult));
	                }
	            }
	        });
        	
        }, 
        fileUploadPrice:function(){
        
       	    var _self = this;
        	var url = "/catalog/control/batchManagerFileUpload";
			 $('#uploadFile').fileupload({
		        url: url,
		        dataType: 'json',		        
		        add:function(e,data){			        	
		        	jQuery.showLoading($("#operate_file_upload"));		        	
		             if(data.originalFiles[0]['type'].length && data.originalFiles[0]['type']!="application/vnd.ms-excel") {
		            	 _self.showToast("upload the file type must be closing in on xls");
		            	 $("#upload_error").html("");
		            	 jQuery.hideLoading($("#operate_file_upload"));
		             }else{
		            	 data.submit();
		             }		        	
		        },
		        done: function (e, data) {
		         	jQuery.hideLoading($("#operate_file_upload"));
		         	$("#uploadProgress").width(0);			         	
			        if(data.result.errList&&data.result.errList.length > 0){
			      		 var errListlength =  data.result.errList.length;
			      		 var errArr =[];
			      		 for(var i=0;i<errListlength;i++){
			      		 	var err = data.result.errList[i];
			      		 	errArr.push("<li>"+err+"</li>");
			      		 }
			       		 $("#upload_error").html(errArr.join(""));
			        }else{
		            	$("#batchManagePrice").hide();
			        	$("#uploadBatchPrice").show();
			        	_self.$content=$("#uploadBatchPrice");
			        	var excellist = data.result.excel;
			        	var tbodyArr =[];
			        	for(var i=0,len=excellist.length;i<len;i++){
			        		var exce = excellist[i];
				        	var productId = exce.productId;
				        	var description = exce.description;
				        	var storeName = exce.storeName;
				        	var price = exce.price;
			        		tbodyArr.push("<tr><td>"+productId+"</td><td>"+description+"</td><td>"+storeName+"</td><td>"+price+"</td><td></td></tr>");
			        	}
			        	_self.$data = excellist;
			        	$("#pre_tbody").html(tbodyArr.join(""));
			        }
		        },
		        progressall: function (e, data) {			        	
            	   var progress = parseInt(data.loaded / data.total * 100, 10);			           	     	
   				$("#uploadProgress").width(600);
   				$('#upload_progress').css('width',progress + '%');
		        }
		    });
        },               
        showStartDate:function(e){        	
        	var t=this;        	
        	require(['layDate'], function(laydate){         		
        		laydate({
   				 	  eventArg: e,
		   			  format: 'DD/MM/YYYY',
		   			  min: laydate.now(), //设定最小日期为当前日期            		laydate.now()
		   			  max:t.startMaxDate,
		   			  istime: true,
		   			  istoday: false,            			
		   			  choose: function(datas){			   				  
		   				t.endMinDate=t.formatDate(datas);//开始日选好后，重置结束日的最小日期
		   			 	t.endStartDate=t.formatDate(datas);//将结束日的初始值设定为开始日
		   			  }
		   			});
        	})
        }, 
        showEndDate:function(e){
        	var t=this;        	
        	require(['layDate'], function(laydate){
        		if(!t.endMinDate){
        			t.endMinDate=laydate.now();
        		}
        		laydate( {
	    			  eventArg: e,
	      			  format: 'DD/MM/YYYY',
	      			  min: t.endMinDate,
	      			  istime: true,
	      			  istoday: false,
	      			  choose: function(datas){	      			    
	      			    t.startMaxDate=t.formatDate(datas);//结束日选好后，重置开始日的最大日期
	      			  }
      				});
        	})
        },              
        showPriceFile:function(e){
        	$(e.currentTarget).find(".operate_file").click();        	
        },
        settingCommencementDate:function(e){
        var _self=this;
        	var excellist = _self.$data;
        	
        	console.log(excellist);
        
        	
        	var startDate=_self.$content.find("#start").val();
        	var endDate=_self.$content.find("#end").val();
        	if(!startDate){
        		_self.showToast("commencement date can not empty");
        		return false;
        	}
        	
        	
        	if(startDate){
        		var startDateArr =startDate.split("/");
        		var day = startDateArr[0];
        		var mon = startDateArr[1];
        		var year = startDateArr[2];
        		var date = new Date(year+"/"+mon+"/"+day);
        		startDate = date.getTime();
        	}
        	if(endDate){
        		var endDateArr =endDate.split("/");
        		var day = endDateArr[0];
        		var mon = endDateArr[1];
        		var year = endDateArr[2];
        		var date = new Date(year+"/"+mon+"/"+day);
        		endDate = date.getTime();
        	}
        	
        	
        	var objArr = [];
        		for(var i=0,len=excellist.length;i<len;i++){
						var obj = {};
		        		var exce = excellist[i];
			        	 obj.productId = exce.productId;
			        	 obj.internalName = exce.internalName;
			        	 obj.brandName = exce.brandName;
			        	 obj.productId = exce.productId;
			        	 obj.description = exce.description;
			        	 obj.storeName = exce.storeName;
			        	 obj.price = exce.price;
			        	 obj.fromDate = startDate;
			        	 obj.thruDate = endDate;
			        	objArr.push(obj);
	        	}
        		e&&e.currentTarget&&jQuery.showLoading(e.currentTarget);
        		jQuery.ajax({
					dataType:'json',
					type: 'post',
					url: '/catalog/control/batchUpload',
					contentType: "application/json;charset=utf-8",
					data:JSON.stringify({							
						productList:objArr
					}),
					success: function (data) {
						e&&e.currentTarget&&jQuery.hideLoading(e.currentTarget); 
						$("#upload_error").html("");
						$("#batchManagePrice").show();
			        	$("#uploadBatchPrice").hide();
						_self.$content=$("batchManagePrice");
					}      
				});	        	
        },
        formatDate:function(date){
        	var dateArr=date.split("/");
        	var str="";
        	var spe="-";
        	for(var i=dateArr.length;i>0;i--){
        		if(i==1){
        			spe="";
        		}
        		str+=dateArr[i-1]+spe;
        	}
        	return str;
        	
        }
	})
	new View();
	
	

    
})	
</script>



