define(['Zepto','Inherit','template','AbstractDialog','text!findPartyDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'500px',
			height:'750px',
			title:'find Party'
		},
         __propertys__:function(options){
		
			this.onSelected=undefined;
			this.options=options;
			this.partyColumn=$(html).filter('#findPartyColumn');
			this.tbodyColumn=$(html).filter('#partyTbodyColumn');			
			this.partyPageColumn=$(html).filter('#partyPage');			
			this.form={};
			this.page={};
		},
		initialize:function(){
			var content=template.render(this.partyColumn.html(),{});
			this.dialog.content(content);
			this.dialog.showModal();	
			 this.$form=$("#find_party");	   
	         this.initEvent();	         
	         this.lookUpPartyData(1);      
		},		
		lookUpPartyData:function(pageNum,target){			
			var t=this;
			t.renderPartyFormData(pageNum);
			console.dir(t.form);
			target&&jQuery.showLoading(target.currentTarget);
			jQuery.ajax({
				url:'/partymgr/control/Findpartyanduserlogin',
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
					
					t.$form.find("#tablePager").html(template.render(t.partyPageColumn.html(),pageResults));
				}
			})
			
		},
		initEvent:function(){
			var t=this;
			t.$form.on("click",".search_btn a",function(e){
				t.lookUpPartyData(1,e);
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
				t.lookUpPartyData($(this).attr('data-num'));
			});
			t.$form.on("keypress",".go_page_input",function(e){
				var key = e.which;
				var reg=new RegExp("^[1-9]*$");
				if(key==13){					
					if ($(e.currentTarget).val()&&reg.test($(e.currentTarget).val())&&$(e.currentTarget).val()<=parseInt($(e.currentTarget).attr("data-big-num"))) {
				    	t.lookUpPartyData($(e.currentTarget).val());
				    }
				}
			    
			})
		},				
		renderPartyFormData:function(pageNum){			
			this.form.partyIdNameNum=this.$form.find("#partyIdNameNum").val().toString();
			this.form.partyIdName=this.$form.find("#partyIdName").val().toString();
			this.form.partyIdNameStatus=this.$form.find("#partyIdNameStatus").is(":checked")?1+"":0+"";
			this.form.firstNameNum=this.$form.find("#firstNameNum").val().toString();
			this.form.firstName=this.$form.find("#firstName").val().toString();
			this.form.firstNameStatus=this.$form.find("#firstNameStatus").is(":checked")?1+"":0+"";
			this.form.lastNameNum=this.$form.find("#lastNameNum").val().toString();
			this.form.lastName=this.$form.find("#lastName").val().toString();
			this.form.lastNameStatus=this.$form.find("#lastNameStatus").is(":checked")?1+"":0+"";
			this.form.userLoginNum=this.$form.find("#userLoginNum").val().toString();
			this.form.userLogin=this.$form.find("#userLogin").val().toString();
			this.form.userLoginStatus=this.$form.find("#userLoginStatus").is(":checked")?1+"":0+"";
			this.form.pageNum=pageNum.toString();			
		},
	});
	return BaseView;
})