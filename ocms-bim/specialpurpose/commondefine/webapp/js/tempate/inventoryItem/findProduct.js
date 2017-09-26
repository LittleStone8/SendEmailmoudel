define(['Zepto','Inherit','template','text!findProductHtml'],
		function($,Inherit,template, html){
	var BaseView = new Inherit.Class({
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
			this.options.el.html(content);
			 this.$form=$("#find_product");	         
	         this.initEvent();
	         this.lookUpProductData(1);
	         this.renderProductTypesData();
		},		
		lookUpProductData:function(pageNum,target){			
			var t=this;
			t.renderProductFormData(pageNum);
			console.dir(t.form);
			target&&jQuery.hideLoading(target.currentTarget);
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
			t.$form.on("click",".product_btn a",function(e){
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
			t.$form.on("click",".product_search_more",function(){				
				if($(this).find(".more_arrow").hasClass("more_down")){
					t.$form.find(".product_more").fadeIn();
					$(this).find(".more_arrow").removeClass("more_down").addClass("more_up");
					$(this).find(".more_text").hide();				
				}else{
					t.$form.find(".product_more").fadeOut();
					$(this).find(".more_arrow").removeClass("more_up").addClass("more_down");
					$(this).find(".more_text").show();		
				}
				
			});
			t.$form.on("click",".select_productId",function(){
				t.productId=$(this).attr("data-productId");
				t.productDesc=$(this).find(".product_desc").text();
				//根据productId查询对应的ean码
				$.ajax({
					url:'/warehouse/control/findEANByProductId',
					type: 'post',
		            dataType: 'json',
		            data:JSON.stringify({"productId":t.productId}),
					success:function(results){	
						typeof t.options.onSelected == "function" && t.options.onSelected({"target":t.options.target,"productId":t.productId,"productDesc":t.productDesc,"EAN":results.result});
						t.options.target="";
					}
				});
				
				
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
			this.$form.find("#ProductType").html(template.render(this.productTypsColumn.html(),{}))
		},
		renderProductFormData:function(pageNum){			
			this.form.productIdNum=this.$form.find("#productIdNum").val().toString();
			this.form.productId=this.$form.find("#productId").val().trim().toString();
			this.form.productIdStatu=this.$form.find("#productIdStatu").is(":checked")?1+"":0+"";
			this.form.brandNameNum=this.$form.find("#brandNameNum").val().toString();
			this.form.brandName=this.$form.find("#brandName").val().toString();			
			this.form.brandNameStatu=this.$form.find("#brandNameStatu").is(":checked")?1+"":0+"";
			this.form.internalNameNum=this.$form.find("#internalNameNum").val().toString();
			this.form.internalName=this.$form.find("#internalName").val().trim().toString();
			this.form.internalNameStatu=this.$form.find("#internalNameStatu").is(":checked")?1+"":0+"";
			this.form.pageNum=pageNum.toString();			
		},
	});
	return BaseView;
})