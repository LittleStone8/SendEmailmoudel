
<div id="print_box"> 
<div style="padding:0 15px 10px 15px;">
<style type="text/css" media="all">
* {font-family:Helvetica!important;}
</style>
<div style="width:100%;text-align:center;">
<span style="text-align:center;font-size:14px;word-wrap:break-word;font-weight:bold;">
${ProductStoretemp.getString("storeName")?if_exists} Consignment-sheet
</span>
</div>

<br>

<div style="width: 100%;font-size:12px;" class="test_tag" id="test_tag">
<span style="float:left;">
Customer: ${buyerperson.getString("firstNameLocal")?if_exists}   
</span>
<span style="float:right;"> 
Sales: ${saleperson.getString("firstName")?if_exists} ${saleperson.getString("lastName")?if_exists}
</span>
</div>

<br>
<div style="width: 100%;font-size:12px;" class="test_tag">

<span style="float:left;">
CAPTCHA: ${OrderHeaderr.getString("captcha")?if_exists}  
</span>
<span style="float:right;"> 
Date: ${orderDate?if_exists}
</span>

</div>



<div style="clear:both;"></div>
<div style="width: 100%;border-bottom: 1px solid ;margin-bottom:10px;margin-top:6px"></div>
<div style="font-size:12px;margin-top:6px;" class="test_tag" id="test_tag">
<#list productlist as temp>
${temp.getString("quantity").replace(".000000", "")?if_exists}* ${temp.getString("itemDescription")?if_exists} (${temp.getString("productId")?if_exists})
<br>

</#list>
</div>
<div style="width: 100%;border-bottom: 1px solid;margin-top:6px;"></div>
<div style="font-size:14px;margin-top:6px;">
Order Id: ${OrderHeaderr.getString("orderId")?if_exists}    
</div>
<div style="font-size:14px;margin-bottom:100px;">
Contact Phone:${ProductStoretemp.getString("StoreContactNumber")?if_exists}  
</div>

<div style="float:right;font-size:14px;">
<span>
Signature:____________
</span>
</div>
<br>
<div style="width:100%;border-bottom: 1px solid;margin-top:12px;margin-bottom:10px;"></div>
<div style="width:100%;font-size:14px;">
<div>Thank you. Please visit again.</div>
<div>Please keep your slip as proof of purchase.</div>

</div>
</div>
</div> 


<script>

window.onload=function(){ 
var prnhtml=window.document.getElementById('print_box').innerHTML; 
window.document.body.innerHTML=prnhtml; 
window.print(); 


} 




</script>