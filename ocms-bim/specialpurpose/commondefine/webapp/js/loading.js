(function($){

	if($.ajaxSetup){
		$.ajaxSetup({timeout:1000 * 180});
	}else{
		$.ajaxSettings = $.extend($.ajaxSettings, {
			timeout: 1000 * 180
		});
	}
	
	$.showLoading = function(target, options){
		
		var currentTarget = target;
		var innerwidth;
		if($(currentTarget).innerWidth()){
			innerwidth = $(currentTarget).innerWidth();
		}else{
			innerwidth = $(currentTarget).width();
		}
		var clientHeight = $(currentTarget).height();
		var padding = innerwidth > 90 ? (innerwidth - 90) / 2 : 10;

		
		var newHtml = '<a href="javascript:void(0);" class="loading-btn" style="line-height:'+ clientHeight +'px'+';height:'+ clientHeight +'px'+';min-width:'+ innerwidth + 'px' +';width:90px;padding:0 '+ padding + 'px !important' +';">' +
			'<span class="loading-icon">Loading</span>' +
		'</a>';
		
		$(currentTarget).hide();
		$(currentTarget).after(newHtml);
	};
	$.hideLoading = function(target){
		
		var currentTarget = target;
		
		$(currentTarget).show();
		$(currentTarget).siblings('.loading-btn').remove();
		
	};
})(jQuery)