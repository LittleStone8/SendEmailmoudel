define(['Zepto','Inherit','template','AbstractDialog','text!batchAddMemberDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'580px',
			height:'725px',
			title:'Find Member'
		},
         __propertys__:function(options){
			this.onSelected=undefined;
			this.options=options;			
			this.productColumn=$(html).filter('#findProductColumn');
			this.tbodyColumn=$(html).filter('#productTbodyColumn');
			this.productPageColumn=$(html).filter('#memberPage');
			this.form={};
			this.page={};
		},
		initialize:function(){
			var content=template.render(this.productColumn.html(),{});
			this.dialog.content(content);
			this.dialog.showModal();	
			this.$form=$("#find_member");	   
			this.initEvent();
			this.lookUpProductData(1);
		},
		lookUpProductData:function(pageNum,target){
			
			var t=this;
			t.renderProductFormData(pageNum);
			target&&jQuery.showLoading(target.currentTarget);
			jQuery.ajax({
				url:'/warehouse/control/queryPerson',
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
				t.partyId=$(this).text();
				t.dialog.remove();
				typeof t.options.onSelected == "function" && t.options.onSelected(t.partyId);
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
				var reg=/^\b[1-9]\d*$/;
				if(key==13){					
					if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val()-0)&&parseInt($(e.currentTarget).val())<=parseInt($(e.currentTarget).attr("data-big-num"))) {
				    	t.lookUpProductData($(e.currentTarget).val());
				    }
				}
			    
			})
		},
		renderProductFormData:function(pageNum){			
			this.form.partyIdNum=this.$form.find("#partyIdNum").val() + "";
			this.form.partyId =this.$form.find("#partyId").val() + "";
			this.form.partyIdStatus=this.$form.find("#partyIdStatu").is(":checked")?1+"":0+"";
			
			this.form.firstNameNum=this.$form.find("#firstNameNum").val()+ "";
			this.form.firstName=this.$form.find("#firstName").val()+ "";
			this.form.firstNameStatus=this.$form.find("#firstNameStatu").is(":checked")?1+"":0+"";
			
			this.form.pageNum=pageNum.toString();			
		},
	});
	return BaseView;
})