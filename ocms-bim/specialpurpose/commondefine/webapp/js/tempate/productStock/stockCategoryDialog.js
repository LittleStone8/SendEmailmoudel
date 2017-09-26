define(['Zepto','Inherit','template','AbstractDialog','text!stockCategoryDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'580px',
			height:'590px',
			title:'find Category'
		},
         __propertys__:function(options){
		
			this.onSelected=undefined;
			this.options=options;			
			this.categoryColumn=$(html).filter('#findCategoryColumn');
			this.tbodyColumn=$(html).filter('#categoryTbodyColumn');
			this.categoryPageColumn=$(html).filter('#categoryPage');
			this.form={};
			this.page={};
		},
		initialize:function(){
			var content=template.render(this.categoryColumn.html(),{});
			this.dialog.content(content);
			this.dialog.showModal();	
			 this.$form=$("#find_category");	   
	         this.initEvent();
	         this.lookUpProductCategoryData(1);
	         //this.renderProductTypesData();
		},		
		lookUpProductCategoryData:function(pageNum,target){			
			var t=this;
			t.renderProductCategoryFormData(pageNum);
			target&&jQuery.showLoading(target.currentTarget);
			jQuery.ajax({
				url:'/warehouse/control/queryCategory',
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
					t.$form.find("#tablePager").html(template.render(t.categoryPageColumn.html(),pageResults));
				}
			})
			
		},
		initEvent:function(){
			var t=this;
			t.$form.on("click",".search_btn a",function(e){
				t.lookUpProductCategoryData(1,e);				
			});			
			t.$form.on("click",".table_btn",function(){
				t.categoryId=$(this).text();
				t.dialog.remove();
				typeof t.options.onSelected == "function" && t.options.onSelected(t.categoryId);
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
		
		renderProductTypesData:function(){			
			//var result={datas:[{"productTypeId":"1","productType":"hhhhh"}]};
			this.$form.find("#ProductType").html(template.render(this.productTypsColumn.html(),{}))
			
		},
		renderProductCategoryFormData:function(pageNum){			
			this.form.categoryName=this.$form.find("#categoryName").val().toString();			
			this.form.pageNum=pageNum.toString();			
		},
	});
	return BaseView;
})