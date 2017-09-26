<div id="print_box">
<div style="padding:0px  0px 0px 0px;">
	<style type="text/css" media="all">
		* {font-family:verdana!important;}
	</style>
	
	
	<div style="width:100%;text-align:center; margin-bottom:10px;">
		<span style="text-align:center;font-size:14px;word-wrap:break-word;font-weight:bold;">
			${ProductStoretemp.getString("storeName")?if_exists}
		</span>
	</div>


	<div style="font-size:10px;" class="test_tag" id="test_tag">
		<span style="float:left;">Customer: ${buyerperson.getString("firstNameLocal")?if_exists}</span><br>
	</div>
	<div style="font-size:10px;" class="test_tag" id="test_tag">
		<span style="float:left;"> Sales: ${saleperson.getString("firstName")?if_exists} ${saleperson.getString("lastName")?if_exists}</span><br>
	</div>
	<div style="font-size:10px;" class="test_tag" id="test_tag">
		<span style="float:left;">INVOICE: CI${OrderHeaderr.getString("orderId")?if_exists}</span><br>
	</div>
	<div style="font-size:10px;" class="test_tag" id="test_tag">
		<span style="float:left;"> Date: ${orderDate?if_exists}</span><br>
	</div>
	<div style="clear:both;"></div>
	
	<div style="width: 100%;border-bottom: 1px solid;"></div>
		<div style="font-size:12px;margin-top:6px;" class="test_tag" id="test_tag">
			<#list productlist as temp>
				${temp.getString("quantity").replace(".000000", "")?if_exists}*${temp.getString("itemDescription")?if_exists} 
				<br />
				<div style="float:right;">${temp.getString("currentPrice")?if_exists} | ${temp.getString("unitRecurringPrice")?if_exists} |  ${temp.getString("unitPrice")?if_exists}</div> 
				<br />
			</#list>
			
			<div style="clear:both;"></div>
			
			<div style="width: 100%;border-bottom: 1px solid;margin-top:6px;"></div>
			
		</div>
	
		<div style="font-size:15px;" class="test_tag" id="test_tag">
		<span style="float:left;">Total:</span>
		<span style="float:right;">${OrderHeaderr.getString("currencyUom")?if_exists}  ${total?if_exists} </span>
		</div>
		
		
		
		
		<div style="clear:both;"></div>
		<div style="width: 100%;border-bottom: 1px solid #424242;margin:6px 0;"></div>
	
		<div style="font-size:12px;width:100%;">
			Payment
		</div>
	
		<#if CASHpay!=0>
			<div style="font-size:10px;">
				Cash Paid:${OrderHeaderr.getString("currencyUom")?if_exists} ${CASHpay?if_exists}
			</div>
			<div style="font-size:10px;">
				Change:${OrderHeaderr.getString("currencyUom")?if_exists} ${CHANGE?if_exists}
			</div>
		</#if>
	
		<#if cardpay!=0>
			<div style="font-size:10px;">
				Purchase on account:${OrderHeaderr.getString("currencyUom")?if_exists} ${cardpay?if_exists}
			</div>
		</#if>
		<div style="font-size:10px;">
			Trading Time:${tracktime?if_exists}
		</div>
	
		
		<div style="width: 100%;border-bottom: 1px solid #424242;margin:6px 0;"></div>
		<div style="clear:both;"></div>
		
	<#if ProductStoretemp.getString("tinNo")?has_content>		
		<div style="font-size:10px;">
			TIN No:${ProductStoretemp.getString("tinNo")?if_exists} 
		</div>
	</#if>	
		
	<#if ProductStoretemp.getString("StoreContactNumber")?has_content>	
		<div style="font-size:10px;">
			TEL:${ProductStoretemp.getString("StoreContactNumber")?if_exists} 
		</div>
	</#if>	
	<#if Address?has_content>	
		<div style="font-size:10px;">
			Address:${Address?if_exists} 
		</div>
	</#if>	
		<div style="width: 100%;border-bottom: 1px solid #424242;margin:6px 0;"></div>
		<div style="clear:both;"></div>
	
	
		<div style="width:100%;font-size:10px;">
		    <span style="width:100%;">Thank you. Please visit again.</span><br>
			<span>Please keep your slip as proof of purchase.</span><br>
			<span>Goods sold are not returnable.</span><br>
			<span>13 MONTHS WARRANTY ON TECNO / Infinix / itel PHONES.</span><br>
		</div>
	</div>
	
	
	<div style="width:100%;text-align:center; margin-bottom:10px;">
		<span style="text-align:center;font-size:10px;">
			.
		</span>
	</div>
</div>

<script>

window.onload=function(){ 
        var prnhtml=window.document.getElementById('print_box').innerHTML;
        window.document.body.innerHTML=prnhtml;
        window.print();
} 




</script>