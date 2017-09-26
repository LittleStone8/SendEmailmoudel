
<div class="screenlet-body">
<!-- Begin  Form Widget  component://product/widget/catalog/ProductForms.xml#UpdateProductPrice --><table cellspacing="0" class="basic-table">
    <tbody><tr class="header-row">
    <td>
价格类型    </td>
    <td>
目的    </td>
    <td>
货币    </td>
    <td>
产品店铺组    </td>
    <td>
开始日期  时间    </td>
    <td>
截止日期 - 
价格 - 
条件度量单位标识 - 
定制价格计算服务 - 
更新    </td>
    <td>
上次修改者:    </td>
    <td>
&nbsp;    </td>
    </tr>
    <tr>
    <td>

标价    </td>
    <td>

购买/初始    </td>
    <td>

美元 [USD]    </td>
    <td>

不可用    </td>
    <td>

17-3-3 15:08:07    </td>
    <td>
<!-- Begin  Form Widget - Form Element  component://product/widget/catalog/ProductForms.xml#UpdateProductPrice -->
<form method="post" action="/catalog/control/updateProductPrice" id="UpdateProductPrice_o_0" class="basic-form" onsubmit="javascript:submitFormDisableSubmits(this)" name="UpdateProductPrice_o_0">
    <input type="hidden" name="productId" value="10014" id="UpdateProductPrice_productId_o_0">
    <input type="hidden" name="productPriceTypeId" value="LIST_PRICE" id="UpdateProductPrice_productPriceTypeId_o_0">
    <input type="hidden" name="productPricePurposeId" value="PURCHASE" id="UpdateProductPrice_productPricePurposeId_o_0">
    <input type="hidden" name="currencyUomId" value="USD" id="UpdateProductPrice_currencyUomId_o_0">
    <input type="hidden" name="productStoreGroupId" value="_NA_" id="UpdateProductPrice_productStoreGroupId_o_0">
    <input type="hidden" name="fromDate" value="17-3-3 15:08:07" id="UpdateProductPrice_fromDate_o_0">
  <span class="view-calendar">
      <input type="text" name="thruDate" title="Format: yy-M-d HH:mm:ss" size="25" maxlength="30" id="UpdateProductPrice_thruDate_o_0">             <a href="javascript:call_cal(document.UpdateProductPrice_o_0.thruDate,'17-3-27%2014:11:51');" title="浏览日历">          </a>    <input type="hidden" name="" value="Timestamp">
  </span>

<input type="text" name="price" value="200" size="25" id="UpdateProductPrice_price_o_0">*
<select name="termUomId" id="UpdateProductPrice_termUomId_o_0" size="1">
<option value="">&nbsp;</option>
 <option value="AREA_A">地区: 英亩 (a)</option> <option value="AREA_cm2">地区: 平方厘米 (cm2)</option> <option value="AREA_ft2">地区: 平方英尺 (ft2)</option> <option value="AREA_ha">地区: 公顷 (ha)</option> <option value="AREA_in2">地区: 平方英寸 (in2)</option> <option value="AREA_km2">地区: 平方公里 (km2)</option> <option value="AREA_m2">地区: 平方米 (m2)</option> <option value="AREA_mi2">地区: 平方英里 (mi2)</option> <option value="AREA_mm2">地区: 平方毫米 (mm2)</option> <option value="AREA_rd2">地区: 平方杆 (rd2)</option> <option value="AREA_yd2">地区: 平方码 (yd2)</option> <option value="DATA_b">数据大小: 比特 (B)</option> <option value="DATA_Gb">数据大小: Gigabyte of Data (GB)</option> <option value="DATA_Kb">数据大小: Kilobyte of Data (KB)</option> <option value="DATA_Mb">数据大小: Megabyte of Data (MB)</option> <option value="DATA_PB">数据大小: Petabyte of Data (PB)</option> <option value="DATA_Tb">数据大小: Terabyte of Data (TB)</option> <option value="DATASPD_bps">数据速度: b/s (bps)</option> <option value="DATASPD_Gbps">数据速度: Gb/s (Gbps)</option> <option value="DATASPD_Kbps">数据速度: Kb/s (Kbps)</option> <option value="DATASPD_Mbps">数据速度: Mb/s (Mbps)</option> <option value="DATASPD_Tbps">数据速度: Tb/s (Tbps)</option> <option value="VDRY_cm3">固体体积: 立方分米 (cm3)</option> <option value="VDRY_ft3">固体体积: 立方英尺 (ft3)</option> <option value="VDRY_in3">固体体积: 立方英寸 (in3)</option> <option value="VDRY_m3">固体体积: 立方米 (m3)</option> <option value="VDRY_mm3">固体体积: 立方毫米 (mm3)</option> <option value="VDRY_ST">固体体积: 立方米 (ST)</option> <option value="VDRY_yd3">固体体积: 立方码 (yd3)</option> <option value="EN_Btu">能量: 英国热量单位 (Btu)</option> <option value="EN_cal15">能量: 卡 (@15.5c) (cal15)</option> <option value="EN_HP">能量: 马力 (HP)</option> <option value="EN_J">能量: 焦耳 (J)</option> <option value="EN_Kw">能量: 千瓦 (Kw)</option> <option value="EN_w">能量: 瓦 (w)</option> <option value="LEN_A">长度: 埃 (A)</option> <option value="LEN_cb">长度: 链 (cb)</option> <option value="LEN_chG">长度: 链 (甘特/勘测) (chG)</option> <option value="LEN_chR">长度: 链 (拉姆丹/工程) (chR)</option> <option value="LEN_cm">长度: 厘米 (cm)</option> <option value="LEN_dam">长度: 十米 (dam)</option> <option value="LEN_dm">长度: 分米 (dm)</option> <option value="LEN_fm">长度: 英寻 (fm)</option> <option value="LEN_ft">长度: 英尺 (ft)</option> <option value="LEN_fur">长度: 浪 (fur)</option> <option value="LEN_hand">长度: 手 (马的高度) (hand)</option> <option value="LEN_in">长度: 英寸 (in)</option> <option value="LEN_km">长度: 千米 (km)</option> <option value="LEN_league">长度: 里格 (league)</option> <option value="LEN_lnG">长度: 令 (甘特) (lnG)</option> <option value="LEN_lnR">长度: 令 (拉姆丹) (lnR)</option> <option value="LEN_m">长度: 米 (m)</option> <option value="LEN_mi">长度: 英里 (法定/陆地) (mi)</option> <option value="LEN_mil">长度: 密耳 (千分之一英寸) (mil)</option> <option value="LEN_mm">长度: 毫米 (mm)</option> <option value="LEN_nmi">长度: 英里 (航海/海洋) (nmi)</option> <option value="LEN_pica">长度: 12点活字 (铅字大小) (pica)</option> <option value="LEN_point">长度: 点 (铅字大小) (point)</option> <option value="LEN_rd">长度: 杆 (rd)</option> <option value="LEN_u">长度: 微米 (u)</option> <option value="LEN_yd">长度: 码 (yd)</option> <option value="VLIQ_bbl">液体体积: 桶 (美) (bbl)</option> <option value="VLIQ_cup">液体体积: 杯 (cup)</option> <option value="VLIQ_dr">液体体积: 打兰 (美) (dr)</option> <option value="VLIQ_galUK">液体体积: 加仑 (英) (gal)</option> <option value="VLIQ_galUS">液体体积: 加仑 (美) (gal)</option> <option value="VLIQ_gi">液体体积: 及耳 (1/4 英制品脱) (gi)</option> <option value="VLIQ_L">液体体积: 升 (L)</option> <option value="VLIQ_ml">液体体积: 毫升 (ml)</option> <option value="VLIQ_ozUK">液体体积: 盎司，液体 (英) (fl. oz (UK))</option> <option value="VLIQ_ozUS">液体体积: 盎司，液体 (美) (fl. oz (US))</option> <option value="VLIQ_ptUK">液体体积: 品脱 (英) (pt (UK))</option> <option value="VLIQ_ptUS">液体体积: 品脱 (美) (pt (US))</option> <option value="VLIQ_qt">液体体积: 夸脱 (qt)</option> <option value="VLIQ_Tbs">液体体积: 大汤匙 (Tbs)</option> <option value="VLIQ_tsp">液体体积: 茶匙 (tsp)</option> <option value="OTH_A">其它: 安培 -电流 (A)</option> <option value="OTH_cd">其它: 烛光 -发光度 (发光强度) (cd)</option> <option value="OTH_ea">其它: 每个 (ea)</option> <option value="OTH_mol">其它: 摩尔 -物质  (分子) (mol)</option> <option value="OTH_pp">其它: 每人 (pp)</option> <option value="TEMP_C">温度: 摄氏度 (C)</option> <option value="TEMP_F">温度: 华氏度 (F)</option> <option value="TEMP_K">温度: 绝对温度 (K)</option> <option value="TF_century">时间/频率: 百年 (century)</option> <option value="TF_day">时间/频率: 日 (day)</option> <option value="TF_decade">时间/频率: 十年 (decade)</option> <option value="TF_hr">时间/频率: 小时 (hr)</option> <option value="TF_millenium">时间/频率: 千年 (millenium)</option> <option value="TF_min">时间/频率: 分钟 (min)</option> <option value="TF_mon">时间/频率: 月 (mon)</option> <option value="TF_ms">时间/频率: 毫秒 (ms)</option> <option value="TF_s">时间/频率: 秒 (s)</option> <option value="TF_score">时间/频率: 二十年 (score)</option> <option value="TF_wk">时间/频率: 周 (wk)</option> <option value="TF_yr">时间/频率: 年 (yr)</option> <option value="WT_dr_avdp">重量: 打兰 (dr avdp)</option> <option value="WT_dwt">重量: 本尼威特 (dwt)</option> <option value="WT_g">重量: 克 (g)</option> <option value="WT_gr">重量: 格林 (gr)</option> <option value="WT_kg">重量: 千克 (kg)</option> <option value="WT_lb">重量: 磅 (英国常衡制) (lb)</option> <option value="WT_lt">重量: 吨 (英制) (lt)</option> <option value="WT_mg">重量: 毫克 (mg)</option> <option value="WT_mt">重量: 吨 (公制) (mt)</option> <option value="WT_oz">重量: 盎司 (英国常衡制) (oz)</option> <option value="WT_oz_tr">重量: 盎司 (金衡制) (oz tr)</option> <option value="WT_sh_t">重量: 吨 (短吨) (sh t)</option> <option value="WT_st">重量: 英石 (st)</option></select>
<select name="customPriceCalcService" id="UpdateProductPrice_customPriceCalcService_o_0" size="1">
<option value="">&nbsp;</option>
</select>
<input type="submit" name="submitButton" value="更新">
</form>
<!-- End  Form Widget - Form Element  component://product/widget/catalog/ProductForms.xml#UpdateProductPrice -->    </td>
    <td>

[admin] 在 2017-03-03 15:08:07.000    </td>
    <td>
<form method="post" action="/catalog/control/deleteProductPrice" onsubmit="javascript:submitFormDisableSubmits(this)" name="UpdateProductPrice_o_0_0_o_deleteLink"><input name="productId" value="10014" type="hidden"><input name="productPriceTypeId" value="LIST_PRICE" type="hidden"><input name="productPricePurposeId" value="PURCHASE" type="hidden"><input name="currencyUomId" value="USD" type="hidden"><input name="productStoreGroupId" value="_NA_" type="hidden"><input name="fromDate" value="2017-03-03 15:08:07.0" type="hidden"></form><a class="buttontext" href="javascript:document.UpdateProductPrice_o_0_0_o_deleteLink.submit()">删除</a>    </td>
    </tr>
</tbody></table>
<!-- End  Form Widget - Formal List Wrapper  component://product/widget/catalog/ProductForms.xml#UpdateProductPrice --></div>