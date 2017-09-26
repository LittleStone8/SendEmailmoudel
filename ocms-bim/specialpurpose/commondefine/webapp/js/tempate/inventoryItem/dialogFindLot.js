define(['Zepto','Inherit','template','AbstractDialog','text!dialogFindLotHtml'],
		function($,Inherit,template, AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'580px',
			height:'630px',
			title:'find Lot'
		},
		__propertys__:function(options){
			console.dir("dialog---");
			this.onSelected=undefined;
			this.options=options;
			this.lotColumn=$(html).filter('#findLotColumn');
			this.tbodyColumn=$(html).filter('#lotTbodyColumn');
			this.lotPageColumn=$(html).filter('#lotPage');

			this.form={};
			this.page={};
			//this.productId=options;
		},
		initialize:function(){
			 var content=template.render(this.lotColumn.html(),{});
			 this.dialog.content(content);
			 this.dialog.showModal();	
			 this.$form=$("#find_lot");
	         this.lookUpLotData(1);
	         this.initEvent();
		},
		
		lookUpLotData:function(pageNum,target){
			var t=this;
			t.renderLotFormData(pageNum);
			console.dir(t.form);
			target&&jQuery.showLoading(target.currentTarget);
        	jQuery.ajax({
				url:'/warehouse/control/queryLots',
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
					t.$form.find("#tablePager").html(template.render(t.lotPageColumn.html(),pageResults));
					}
			})
		},
		initEvent:function(){
			var t=this;
			t.$form.on("click",".lot_btn a",function(e){
				
				t.lookUpLotData(1,e);
			})
			t.$form.on("click",".table_btn",function(){
				
				t.lotId=$(this).text();
				t.dialog.remove();
				typeof t.options.onSelected == "function" && t.options.onSelected(t.lotId);
			})
			t.$form.on("click",".paging a",function(e){
				
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
				t.lookUpLotData($(this).attr('data-num'));
			});
			t.$form.on("keypress",".go_page_input",function(e){
				var key = e.which;
				var reg=new RegExp("^[1-9]*$");
				if(key==13){
					
					if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
				    	t.lookUpLotData($(e.currentTarget).val());
				    }
				}
			    
			})
		},
		
		renderLotFormData:function(pageNum){
			this.form={
					"lotId":this.$form.find("#lotId").val().toString(),
					"pageNum":pageNum&&pageNum.toString(),
			};
		},
	});
	return BaseView;
})