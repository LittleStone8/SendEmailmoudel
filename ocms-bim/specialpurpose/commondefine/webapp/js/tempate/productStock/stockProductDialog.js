define(['Zepto','Inherit','template','AbstractDialog','text!stockProductDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'580px',
			height:'725px',
			title:'find Product'
		},
         __propertys__:function(options){
		
			this.onSelected=undefined;
			this.options=options;			
			this.productColumn=$(html).filter('#findProductColumn');
			this.tbodyColumn=$(html).filter('#productTbodyColumn');
			this.productTypsColumn=$(html).filter('#productTypeColumn');
			this.productPageColumn=$(html).filter('#productPage');
			this.productTypeColumn=$(html).filter('#productTypeColumn');
			this.form={};
			this.page={};
		},
		initialize:function(){
			var content=template.render(this.productColumn.html(),{});
			this.dialog.content(content);
			this.dialog.showModal();	
			 this.$form=$("#find_stock_product");	   
	         this.initEvent();
	         this.initData();
	         this.lookUpProductData(1);
	         this.renderProductTypesData();
		},
		initData:function(){
			var t=this;
			jQuery.ajax({
				url:'/warehouse/control/queryProductType',
				type: 'post',
	            dataType: 'json',
				success:function(results){					
					var content=template.render(t.productTypeColumn.html(),results);
					$("#ProductType").html(content);
				}
			})
		},
		lookUpProductData:function(pageNum,target){
			
			var t=this;
			t.renderProductFormData(pageNum);
			console.dir(t.form);
			target&&jQuery.showLoading(target.currentTarget);
			jQuery.ajax({
				url:'/warehouse/control/queryProduct',
				type: 'post',
	            dataType: 'json',
	            data:JSON.stringify(t.form),
				success:function(results){
					target&&jQuery.hideLoading(target.currentTarget);
					t.page.pageNum=results.pageNum;
					t.page.totalPage=results.totalPage;
					t.page.totalNum=results.totalNum;
					
					t.$form.find("#tbody").html(template.render(t.tbodyColumn.html(),results));
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
					
					t.$form.find("#tablePager").html(template.render(t.productPageColumn.html(),pageResults));
				}
			})
			
		},
		initEvent:function(){
			var t=this;
			t.$form.on("click",".search_btn a",function(e){
				t.lookUpProductData(1,e);
			});
			t.$form.on("click",".product_checkbox",function(){
				
				if($(this).next("input").is(':checked')){
					$(this).removeClass("selected").addClass("noselected");
					$(this).next("input").removeAttr("checked");
				}else{
					$(this).removeClass("noselected").addClass("selected");
					$(this).next("input").attr("checked","checked");
				}
				
			});
			t.$form.on("click",".table_btn",function(){
				t.productId=$(this).text();
				t.dialog.remove();
				typeof t.options.onSelected == "function" && t.options.onSelected(t.productId);
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
				t.lookUpProductData($(this).attr('data-num'));
			});
			t.$form.on("keypress",".go_page_input",function(e){
				var key = e.which;
				var reg=new RegExp("^[1-9]*$");
				if(key==13){					
					if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
				    	t.lookUpProductData($(e.currentTarget).val());
				    }
				}
			    
			})
		},
		
		renderProductTypesData:function(){			
			//var result={datas:[{"productTypeId":"1","productType":"hhhhh"}]};
			this.$form.find("#ProductType").html(template.render(this.productTypsColumn.html(),{}))
			
		},
		renderProductFormData:function(pageNum){			
			this.form.productIdNum=this.$form.find("#productIdNum").val().toString();
			this.form.productId=this.$form.find("#productId").val().toString();
			this.form.productIdStatu=this.$form.find("#productIdStatu").is(":checked")?1+"":0+"";
			this.form.brandNameNum=this.$form.find("#brandNameNum").val().toString();
			this.form.brandName=this.$form.find("#brandName").val().toString();
			this.form.brandNameStatu=this.$form.find("#brandNameStatu").is(":checked")?1+"":0+"";
			this.form.internalNameNum=this.$form.find("#internalNameNum").val().toString();
			this.form.internalName=this.$form.find("#internalName").val().toString();
			this.form.internalNameStatu=this.$form.find("#internalNameStatu").is(":checked")?1+"":0+"";
			this.form.productTypeId=this.$form.find("#ProductType").val().toString()?this.$form.find("#ProductType").val().toString():"FINISHED_GOOD";
			this.form.pageNum=pageNum.toString();			
		},
	});
	return BaseView;
})