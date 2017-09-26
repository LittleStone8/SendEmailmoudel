<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>
<@frameSectionHeader title="Shipping" extra=extraOptions/>
<div class="screenlet">
    <div class="screenlet-body">
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tbody>
				<tr>
		        	<td align="right" width="20%">
					  <div class="tabletext">
					      <b>Customer</b>
					  </div>
					</td>
					<td width="5%">&nbsp;</td>
					<td align="left" width="75%">
					  <div class="tabletext">
					      <span class="shipCustomerInfo"></span>
					  </div>
					</td>
				</tr>
				<tr><td colspan="3"><hr class="sepbar" /></td></tr>
				<tr>
		        	<td align="right" width="20%">
					  <div class="tabletext">
					      <b>Shipping Address</b>
					  </div>
					</td>
					<td width="5%">&nbsp;</td>
					<td align="left" width="75%">
					  <div class="tabletext">
					      <span class="shipAddressInfo"></span>
					  </div>
					</td>
				</tr>
				<tr><td colspan="3"><hr class="sepbar" /></td></tr>
				<tr>
		        	<td align="right" width="20%">
					  <div class="tabletext">
					      <b>Tel. No.</b>
					  </div>
					</td>
					<td width="5%">&nbsp;</td>
					<td align="left" width="75%">
					  <div class="tabletext">
					      <span class="shipTelNoInfo"></span>
					  </div>
					</td>
				</tr>
				<tr><td colspan="3"><hr class="sepbar" /></td></tr>
				<tr>
		        	<td align="right" width="20%">
					  <div class="tabletext">
					      <b>Est. Ship Date</b>
					  </div>
					</td>
					<td width="5%">&nbsp;</td>
					<td align="left" width="75%">
					  <div class="tabletext">
					      <span class="shipDateInfo"></span>
					  </div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<script src="/commondefine_js/jquery.js"></script>

<#-- chenshihua		2017-4-7 -->
<script>
$(document).ready(function() {
      shippingInfo();
          
      function shippingInfo(){
   		
   		var orderId = getUrlParams('orderId');
   		$.ajax({
   	        type: 'post',
   	        dataType: 'json',
   	        url: '/crmsfa/control/shippingInfo',
   	        data: {
   	        	orderId:orderId
   			},
   	        success: function (data) {
   	        	if(data.responseMessage == 'success'){
   	        		var dataResult = data.shippingInfo;
   	        		var shipDateInfo = $('.shipDateInfo');
   	        		var shipTelNoInfo = $('.shipTelNoInfo');
   	        		var shipAddressInfo = $('.shipAddressInfo');
   	        		var shipCustomerInfo = $('.shipCustomerInfo');
   	        		var otherAdjustmentsCurrencyUom = $('.otherAdjustmentsCurrencyUom');
   	        		var otherAdjustmentsAmount = $('.otherAdjustmentsAmount');
   	        		var orderTotalOtherOrderAdjustments = $('.orderTotalOtherOrderAdjustments');
   	        		var countryGeo = dataResult.countryGeo || '';
   	        		var city = dataResult.city || '';
   	        		var countyGeo = dataResult.countyGeo || '';
   	        		var address1 = dataResult.address1 || '';
   	        		var stateProvinceGeo = dataResult.stateProvinceGeo || '';
   	        		var address = countryGeo + " " + stateProvinceGeo + " " + city + " " + countyGeo + " " + address1;
   	        		var adjustmentsList = dataResult.adjustmentsList;
   	        		var adjustmentBoxTr = $('.adjustmentBox').closest('tr');
   	        		
   	        		adjustmentsList.length > 0 && $.each(adjustmentsList, function(index, elem){
   	        			adjustmentBoxTr.eq(index).find('.adjustmentBox').append('<span>'+ elem +'</span>');
   	        		});
   	        			
   	        		shipDateInfo.text(dataResult.promisedDatetime);
   	        		shipTelNoInfo.text(dataResult.contactNumber);
   	        		shipAddressInfo.text(address);
   	        		shipCustomerInfo.text(dataResult.firstNameLocal);
   	        		otherAdjustmentsCurrencyUom.text(dataResult.currencyUom);
   	        		orderTotalOtherOrderAdjustments.text(dataResult.totalAdjustments);
   	        	}
   	       	}	
       	});
   	}
      	
    function getUrlParams(name) {
   		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
   		var r = window.location.search.substr(1).match(reg);
   		if (r != null) return unescape(r[2]);
   		return null;
   	}
});
</script>	
