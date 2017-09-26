define(['Zepto','Inherit','template','AbstractDialog','text!editImeiDialogHtml'],
		function($,Inherit,template,AbstractDialog, html){
	var BaseView = new Inherit.Class(AbstractDialog,{
		setting:{
			width:'600px'
		},
		__propertys__:function(options){
			this.onSelected=undefined;
			this.options=options;
			this.editImeiDialogColumn = $(html).filter('#editImeiDialogColumn');
			this.imeiCodeColumn = $(html).filter('#imeiCodeColumn');
		},
		initialize:function(){
			var content=template.render(this.editImeiDialogColumn.html(), { 
				currentModel: this.options.currentModel,
				trackingId : this.options.trackingId,
        		productId : this.options.productId
			});
			this.dialog.content(content);
			this.dialog.showModal();
			this.codesData = [];
			
			this.initData();
			this.$page = $('#editImeiDialog');
			this.timeoutid = null;
		},
		events:{
			'click #editImeiDialog .returnIcon':'hideDialog',
			'click #editImeiDialog .clearAllBtn':'clearAllItems',
			'click #editImeiDialog .saveBtn':'saveItems',
			'click #editImeiDialog .imeiClose':'imeiCloseEvt',
			'keypress #editImeiDialog .enterImeiInput':'changeImeiContent',
		},
		initData: function(){
			var t = this;
			var trackingId= t.$el.find('.imeiTitle').attr('data-trackingId');
			var productId= t.$el.find('.imeiTitle').attr('data-productId');
			
           	$.ajax({
   				url:'/warehouse/control/queryScan',
   				type: 'post',
   	            dataType: 'json',
   	         	data: JSON.stringify({
   	         		transhipmentShippingBillId: trackingId,
   	         		productId: productId
   	         	}),
   	           	success:function(results){
   					if(results.resultCode==1){
   						if(results.resultDate.length > 0){
   							var content = template.render(t.imeiCodeColumn.html(), { result : results.resultDate });
   							
   							t.codesData = results.resultDate;
							t.$el.find('.currentContent').html(content);
							t.initHeight();
							t.$el.find('.currentItems').text(results.resultDate.length);
   						}else{
							t.$el.find('.currentItems').text(0);
   						}
   					}
   	           	}
           	});
		},
		initHeight: function(){
			var t = this;
			if(t.$el.find('.currentContent').height() > 200){
				t.$el.find('.currentContent').css({
					"max-height":"200px",
					"overflow-y":"scroll"
				});
			}else{
				t.$el.find('.currentContent').css({
					"max-height":"none",
					"overflow-y":"auto"
				});
			}
		},
        filterEvt : function(str) { 
        	return str.replace(/[-|\r\n\s+,]/g, "");
        },
        changeImeiContent : function(e){
			var t = this;
			var key = e.which;
        	var evt = $(e.currentTarget);
        	var value = $.trim(t.filterEvt(evt.val()));
        	var arrList = [];
        	var str = '';
        	var batchTarget=true;
        	var singleTarget=true;
        	
        	if (key == 13 && value) {
        		var reg= /^[0-9]\d*$/;
        		
        		if(this.options.currentNum < parseInt(t.$page.find('.currentItems').text()) + Math.ceil(value.length / 15)){
        			
        			t.$page.find('.errorMessageList').html('The products input cannot be more than the required quantity.');
        			t.$page.find('.enterImeiInput').val('');
        			
        			return false;
        		}
        		
		    	if(value.length > 15 && value.length % 15 == 0){
		        	if(!reg.test(value)){
		        		t.$page.find('.errorMessageList').html('Incorrect input. Please input 15-digits IMEI code.');
		        		
			    		return false;
		        	}else{
		        		t.$page.find('.errorMessageList').html('');
		        	}
		    	    var arrList=[];
		    	    var currentList = [];
		    	    var str="";
		    		for(var i=0,len=value.length;i<len;i++){
		    			str += value[i];
		    		    if((i+1) % 15 == 0){
		    		    	arrList.push(str);
		    		    	str="";
		    		    }
		    		}	
	    			for(var i=0;i<t.codesData.length;i++){
	    				for(var v=0;v<arrList.length;v++){
							if(t.codesData[i].imei==arrList[v]){		    			
								batchTarget = false;
								currentList.push(arrList[v]);
				    		}
				    	}
	    			}
		    		
			    	if(!batchTarget){
			    		var htmlContent = '';
			    		$.each(currentList, function(index, elem){
			    			htmlContent += "<div>This IMEI("+ elem +") has already been in the list.</div>"
			    		});
			    		t.$page.find('.errorMessageList').html(htmlContent);
			    	}else{
			    		t.addImeiToCode(batchTarget, 'batch', arrList);
			    	}
		    		
		    	}else{
		    		var reg=new RegExp("^([0-9]{15})$");
		        	if(!reg.test(value)){
		        		t.$page.find('.errorMessageList').html('Incorrect input. Please input 15-digits IMEI code.');
		        		
			    		return false;
		        	}else{
		        		t.$page.find('.errorMessageList').html('');
		        	}
		        	
			    	for(var i=0;i<t.codesData.length;i++){
			    		if(t.codesData[i].imei == value){		    			
			    			singleTarget = false;	
			    			break;
			    		}
			    	}
		        	
			    	if(!singleTarget){
			    		t.$page.find('.errorMessageList').html('This IMEI('+ value +') has already been in the list.');
			    	}else{
			    		t.addImeiToCode(singleTarget, 'single', value);
			    	}
		    	}
        	}
		},
		addImeiToCode : function(flag, type, options){
			var t = this;
    		if(flag){
	    		switch(type){
		    		case"batch":
		    			t.generateDebounceFn(function(){
			    			var dataList = [];
			    			$.each(options,function(index,elem){		    		
			    				dataList.push({ imei: elem });
		    				});
			    			t.checkImeiCode(options, "batch", function(data){
			    				if(data.resultCode==1){
			    					t.codesData = t.codesData.concat(dataList);
			    					
					    			var content = template.render(t.imeiCodeColumn.html(), { result: dataList });
					    			t.$page.find('.currentContent').prepend(content);
					    			t.initHeight();
				    				t.$page.find('.currentItems').text($('.imeiGroup').length);	
				    				t.$page.find('.enterImeiInput').val('');
			    				}else{
			    					var messageList = data.resultdate;
			    					var str = '';
			    					
			    					$.each(messageList, function(index, elem){
			    						if(elem.result == "failure"){
			    							str += "<div>" + elem.reason + "</div>";
			    						}
			    					})
			    					t.$page.find('.errorMessageList').html(str);
			    				}
			    			});
		        		}, 1000);
		    			break;
		    		case"single":
		    			t.generateDebounceFn(function(){
					        t.checkImeiCode(options, "single", function(data){
			    				if(data.resultCode==1){
			    					t.codesData.push({ imei: options });
			    					
							        var content = template.render(t.imeiCodeColumn.html(), { result: [{ imei: options }] });
							        t.$page.find('.currentContent').prepend(content);
							        t.initHeight();
							        t.$page.find('.currentItems').text($('.imeiGroup').length);
							        t.$page.find('.enterImeiInput').val('');
			    				}else{
			    					var messageList = data.resultdate;
			    					var str = '';
			    					
			    					$.each(messageList, function(index, elem){
			    						if(elem.result == "failure"){
			    							str += "<div>" + elem.reason + "</div>";
			    						}
			    					})
			    					t.$page.find('.errorMessageList').html(str);
			    				}
			    			});
				        }, 1000);
		    			break;
		    	}
	    	}
    	},
    	checkImeiCode : function(options, type, callback){
    		var t = this;
    		var imeiArr = [];
    		var productId= t.$el.find('.imeiTitle').attr('data-productId');
    		
    		if(type && type == "batch"){
    			t.$page.find('.imeiLoadingContent').css('display', 'block');
    			$.each(options, function(index, elem){
    				imeiArr.push({
    					productId:productId,
    					imei:elem
    				});
    			});
    		}else if(type && type == "single"){
    			imeiArr.push({
					productId:productId,
					imei:options
				});
    		}

			$.ajax({
				url:'/warehouse/control/queryIMEIStatus',
   				type: 'post',
   	            dataType: 'json',
   	         	data: JSON.stringify(imeiArr),
   	           	success:function(results){
	   	     		if(type && type == "batch"){
	   	    			t.$page.find('.imeiLoadingContent').css('display', 'none');
	   	    		}
   	           		callback && callback(results);
   	           	},
   	           	error: function(){
	   	           	if(type && type == "batch"){
	   	    			t.$page.find('.imeiLoadingContent').css('display', 'none');
	   	    		}
   	           	}
           	});
    	},
    	generateDebounceFn : function(fn, wait){
    		var t = this;
    		
    		clearTimeout(t.timeoutid);
    		t.timeoutid = setTimeout(function(){
    			typeof fn === 'function' && fn();
    		}, wait);
	    },
		saveItems : function(e){
			var t = this;
			var imeiStr = [];
			var trackingId= t.$page.find('.imeiTitle').attr('data-trackingId');
			var productId= t.$page.find('.imeiTitle').attr('data-productId');
			
			if(t.codesData.length > 0){
				$.each(t.codesData, function(index, elem){
					imeiStr.push({
						transhipmentShippingBillId: trackingId,
						productId: productId,
						imei: elem.imei
					});
				})
			}
			
			e && jQuery.showLoading(jQuery(e.currentTarget));
			$.ajax({
   				url:'/warehouse/control/saveScanImei',
   				type: 'post',
   	            dataType: 'json',
   	         	data: JSON.stringify(imeiStr),
   	           	success:function(results){
   	           		e && jQuery.hideLoading(jQuery(e.currentTarget));
   					if(results.resultCode==1){
   						
						typeof t.options.onSelected == "function" && t.options.onSelected(t.codesData.length);
						t.codesData = [];
						t.dialog.remove();
   					}else{
   						t.showToast(results.resultMsg);
   					}
   	           	},
   	           	error : function(option, status){
    				e && jQuery.hideLoading(jQuery(e.currentTarget));
    			}
           	});
		},
		imeiCloseEvt : function(e){
			var t = this;
			
			var evt = $(e.currentTarget);
			var value= evt.closest('.imeiGroup').find('.imeiNumberInput').val();
			var trackingId= t.$page.find('.imeiTitle').attr('data-trackingId');
			var productId= t.$page.find('.imeiTitle').attr('data-productId');
			var arrList = [{
   	         		transhipmentShippingBillId: trackingId,
   	         		productId: productId,
   	         		imei: value
   	        }];
			
			t.removeItemsEvt(null, arrList, function(results){
				if(results.resultCode==1){
					
					evt.closest('.imeiGroup').remove();
		        	t.$page.find('.currentItems').text(t.codesData.length - 1);
		        	
		        	$.each(t.codesData,function(index,elem){
		        		if(elem.imei == value){
		        	    	t.codesData.splice(index,1);
		        	    	return false;
		        	    }
		        	});
				}else{
					t.showToast(results.resultMsg);
				}
			});
		},
		hideDialog : function(){
			var t = this;
			
			typeof t.options.onSelected == "function" && t.options.onSelected(t.codesData.length);
			this.dialog.remove();
		},
		removeItemsEvt : function(evt, options, callback){
			
			evt && jQuery.showLoading(jQuery(evt.currentTarget));
			$.ajax({
   				url:'/warehouse/control/deleteScanImei',
   				type: 'post',
   	            dataType: 'json',
   	         	data: JSON.stringify(options),
   	           	success:function(results){
   	           		evt && jQuery.hideLoading(jQuery(evt.currentTarget));
   	           	    callback && callback(results);
   	           	},
   	           	error : function(option, status){
   	           		evt && jQuery.hideLoading(jQuery(evt.currentTarget));
    			}
           	});
		},
		clearAllItems : function(e){
			var t = this;
			
			var valueList= t.$page.find('.imeiGroup .imeiNumberInput');
			var trackingId= t.$page.find('.imeiTitle').attr('data-trackingId');
			var productId= t.$page.find('.imeiTitle').attr('data-productId');
			var arrList = [];
			
			if(valueList.length == 0){
				t.showToast('No items to remove.');
				
				return false;
			}
			
			$.each(valueList, function(index, elem){
				arrList.push({
					transhipmentShippingBillId: trackingId,
   	         		productId: productId,
   	         		imei: $(elem).val()
				});
			});
			
			t.removeItemsEvt(e, arrList, function(results){
				if(results.resultCode==1){
					
					t.$page.find('.currentContent').html('');
					t.$page.find('.currentItems').text(0);
					t.codesData = [];
				}else{
					t.showToast(results.resultMsg);
				}
			});
		}
	});
	return BaseView;
})