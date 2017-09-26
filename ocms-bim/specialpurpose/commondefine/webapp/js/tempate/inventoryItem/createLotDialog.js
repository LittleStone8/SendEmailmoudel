define(['Zepto','Inherit','template','AbstractDialog','text!createLotDialogHtml','layData'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'452px',
			title:'Create New Lot'
		},
		__propertys__:function(options){
			this.onSelected=undefined;
			this.options=options;
			this.createLotColumn=$(html).filter('#createLotFormColumn');
			this.showLotDetailColumn=$(html).filter('#showLotDetailColumn');
			this.showUomIdColumn=$(html).filter("#showUomIdColumn");
			this.form={};
			//this.productId=options;
		},
		initialize:function(){
			var content=template.render(this.createLotColumn.html(),{nowTime:this.options.nowTime});
			this.dialog.content(content);
			this.dialog.showModal();	
			 this.$form=$("#create_lot");
			 this.initDom();
	         this.initEvent();
		},
		initDom:function(){
			var t=this;
			
        	jQuery.ajax({
				url:'/warehouse/control/queryUomId',
				type: 'post',
	            dataType: 'json',
				success:function(results){					
					var content=template.render(t.showUomIdColumn.html(),results);
					$("#uomId").html(content);
				}
			})
        },
		getLotFormData:function(){
			this.form.lotId=this.$form.find("#lotId").val();
			this.form.expirationDate=this.$form.find("#lotDate").val();
			this.form.quantity=this.$form.find("#quantity").val();
			this.form.uomId=this.$form.find("#uomId").val();
			this.form.comments=this.$form.find("#comments").val();
		},
		initEvent:function(){
			var t=this;
            t.$form.on("click",".lot_create a",function(target){
            	t.getLotFormData();
            	if(t.form.lotId==""){
            		$("#lotId").next(".item_verify").text("lot ID can not be empty");
            		return false;
            	}
            	
            	var reg=new RegExp("^(0|([1-9]\\d*))(\\.\\d+)?$");
            	if(t.form.quantity&&!reg.test(t.form.quantity)){
            		$("#quantity").siblings(".item_verify").text("the quantity is illegal");
            		return false;
            	}
            	target.currentTarget&&jQuery.showLoading(target.currentTarget);
            	jQuery.ajax({
    				url:'/warehouse/control/createLots',
    				type: 'post',
    	            dataType: 'json',
    	            data:JSON.stringify(t.form),
    				success:function(results){
    					target.currentTarget&&jQuery.hideLoading(target.currentTarget);
    					
    					console.dir(results);
    					if(results.resultCode==-1){
    						t.showToast(results.resultMsg);
    						return false;
    					}
    					var showContent=template.render(t.showLotDetailColumn.html(),results);
    					t.$form.hide();
    	            	$("#showDetails").show().html(showContent);
    				}
    			})
            	
                
            })
             t.$form.on("change","#lotId",function(){            	
            	if($(this).val()!=""){
            		$(this).next(".item_verify").text("");
            	}
            })
            t.$form.on("change","#quantity",function(){
            	
            	var reg=new RegExp("^(0|([1-9]\\d*))(\\.\\d+)?$");
            	if(!reg.test($(this).val())){
            		$(this).siblings(".item_verify").text("this quantity is illegal");
            		return false;
            	}else{
            		$(this).siblings(".item_verify").text("");
            	}
            })
            $("#showDetails").on("click",".lot_done a",function(){
            	t.dialog.remove();
            	typeof t.options.onSelected == "function" && t.options.onSelected($(this).attr("data-lotid"));
             })
		},
		events:{
			'click #lotDate':'showDate'
		},
		showDate:function(e){
			require(['layDate'], function(layDate){
				layDate({
					eventArg: e,
					istime: true,
					format: 'DD/MM/YYYY hh:mm:ss'
				});
			});
		}
	});
	return BaseView;
})