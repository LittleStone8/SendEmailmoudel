define(['Zepto','Inherit','template','AbstractDialog','text!stockDetailDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	template.helper('formatDiff', function(price) {
		if(price){			
			var str=price.toString().substr(0,price.indexOf(".")+3); 
			var arrayPrice = str.split(".");
			return arrayPrice[1]=="00"?arrayPrice[0]:str;			
		}
	    return price;
	});
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'580px',
			height:'700px',
			title:'find Detail'
		},
         __propertys__:function(options){	
        	 
        	this.options = options;
			this.findDtailColumn=$(html).filter('#findDetailColumn');
			this.detailTbodyColumn=$(html).filter('#detailTbodyColumn');
			this.detailPage=$(html).filter('#detailPage');
			this.form={};
			this.page={};
		},
		initialize:function(){
			var content=template.render(this.findDtailColumn.html(),{});
			this.dialog.content(content);
			this.dialog.showModal();	
			this.$form=$("#findDetail_info");	   
	        this.initEvent();
	        this.lookUpProductCategoryData("1");
		},		
		lookUpProductCategoryData:function(pageNum,target){			
			var t=this;
			t.renderProductCategoryFormData(pageNum);
			target&&jQuery.showLoading(target.currentTarget);
			jQuery.ajax({
				url:'/warehouse/control/queryPurchaseInventory',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.form),
				success:function(results){
					target&&jQuery.hideLoading(target.currentTarget);
					t.page.pageNum=results.pageNum;
					t.page.totalPage=results.totalPage;
					t.page.totalNum=results.totalNum;
					
					t.$form.find("#findDetail_tbody").html(template.render(t.detailTbodyColumn.html(),results));
					var pages=[];
					var startPage=results.startPage;
					var endPage=results.endPage;
					
					for(var i=startPage;i<=endPage;i++){
						pages.push(i);
					}
					var pageResults={
							"startPage":results.startPage,
							"endPage":results.endPage,
							"pageNum":results.pageNum,
							"totalNum":results.totalNum,
							"totalPage":results.totalPage,
							"nums":pages
					};
					t.$form.find("#tablePager").html(template.render(t.detailPage.html(),pageResults));
				}
			})
			
		},
		initEvent:function(){
			var t=this;
			t.$form.on("click",".search_btn a",function(e){
				t.lookUpProductCategoryData(1,e);				
			});			
			t.$form.on("click",".paging a",function(){
				
				if($(this).attr("data-type")&&$(this).attr("data-type")=='home'&&t.page.pageNum=="1"){
					return false;
				}
				if($(this).attr("data-type")&&$(this).attr("data-type")=='final'&&t.page.pageNum==$(this).attr('data-num')){
					return false;
				}
				if($(this).attr('data-num')<1){
					return false;
				}
				if($(this).attr('data-num')>t.page.totalPage){
					return false;
				}
				t.lookUpProductCategoryData($(this).attr('data-num'));
			});
			t.$form.on("keypress",".go_page_input",function(e){
				var key = e.which;
				var reg=new RegExp("^[1-9]*$");
				if(key==13){					
					if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
				    	t.lookUpProductCategoryData($(e.currentTarget).val());
				    }
				}
			    
			})
		},
		renderProductCategoryFormData:function(pageNum){
			
			this.form.facilityId= $("#facilityId").val().toString();
			this.form.productId= this.options.productId;
			this.form.internalName=$("#internalName").val().toString();
			this.form.productTypeId=$("#productTypeId").val().toString();
			this.form.productCategoryId=$("#productCategoryId").val().toString();
			this.form.qohm=$("#qohm").val().toString();
			this.form.atpm=$("#atpm").val().toString();			
			this.form.pageNum=pageNum.toString();	
			this.form.flag = "0";
		},
	});
	return BaseView;
})