<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl"/>
    
<link rel="shortcut icon" href="<@ofbizContentUrl>/opentaps_images/favicon.ico</@ofbizContentUrl>">
<title><#if pageTitleLabel?exists>${uiLabelMap.get(pageTitleLabel)} |</#if> ${configProperties.get(opentapsApplicationName+".title")}</title>

    <#assign appName = Static["org.ofbiz.base.util.UtilHttp"].getApplicationName(request)/>

  <#if opentapsWebapp?has_content && opentapsWebapp.alternativeCssFile?has_content>
      <link rel="stylesheet" href="${StringUtil.wrapString(opentapsWebapp.alternativeCssFile)}" type="text/css"/>
  <#else>
    <#list Static["org.opentaps.common.util.UtilConfig"].getStylesheetFiles(opentapsApplicationName) as stylesheet>
      <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(stylesheet)}</@ofbizContentUrl>" type="text/css"/>
    </#list>

    <#-- here is where the dynamic CSS goes, for changing theme color, etc. To activate this, define sectionName = 'section' -->
    <#if sectionName?exists>
      <#assign bgcolor = StringUtil.wrapString(Static["org.opentaps.common.util.UtilConfig"].getSectionBgColor(opentapsApplicationName, sectionName))/>
      <#assign fgcolor = StringUtil.wrapString(Static["org.opentaps.common.util.UtilConfig"].getSectionFgColor(opentapsApplicationName, sectionName))/>
      <style type="text/css">
			h1, h2, .gwt-screenlet-header, .sectionHeader, .subSectionHeader, .subSectionTitle, .formSectionHeader, .formSectionHeaderTitle, .screenlet-header, .boxhead, .boxtop, div.boxtop, .toggleButtonDisabled, .tundra .dijitDialogTitleBar, .shortcutGroup td, .screenlet-title-bar {color: ${fgcolor}; background-color: ${bgcolor}; background-image:url(/opentaps_images/panels/top-bottom-${bgcolor?replace("#", "")}.gif); }
			.screenlet-header .boxhead, .subSectionHeader .subSectionTitle, .boxtop .boxhead { background:transparent;}
			div.sectionTabBorder, ul.sectionTabBar li.sectionTabButtonSelected a {color: ${fgcolor};}
			div.sectionTabBorder {background: ${bgcolor} !important;}
			.x-panel-tl, .x-panel-tr, .titleBar .x-panel-br, .titleBar .x-panel-bl { background-image:url(/opentaps_images/panels/corners-sprite-${bgcolor?replace("#", "")}.gif) !important; }
			div.sectionTabBorder, .x-panel-tc, .titleBar .x-panel-bc { background-image:url(/opentaps_images/panels/top-bottom-${bgcolor?replace("#", "")}.gif) !important; }
			.x-panel-tl .x-panel-header, .frameSectionHeader .pageNumber {color: ${fgcolor} !important; }
			.x-panel-noborder .x-panel-header-noborder { border:none !important; }
			ul.sectionTabBar li.sectionTabButtonUnselected .x-panel-tl,
			ul.sectionTabBar li.sectionTabButtonUnselected .x-panel-tr { background-image:url(/opentaps_images/panels/corners-sprite-bw.gif) !important; }
			ul.sectionTabBar li.sectionTabButtonUnselected .x-panel-tc { background-image:url(/opentaps_images/panels/top-bottom-bw.gif) !important; }
      </style>

      <script type="text/javascript">
        var bgColor = '${bgcolor?default("")?replace("#", "")}';
      </script>
    </#if>
  </#if>
      <script src="/${appName}/control/javascriptUiLabels.js" type="text/javascript"></script>

      <#assign javascripts = Static["org.opentaps.common.util.UtilConfig"].getJavascriptFiles(opentapsApplicationName, locale)/>

      <#if layoutSettings?exists && layoutSettings.javaScripts?has_content>
        <#assign javascripts = javascripts + layoutSettings.javaScripts/>
      </#if>

      <#list javascripts as javascript>
        <#if javascript?matches(".*dojo.*")>
          <#-- Unfortunately, due to Dojo's module-loading behaviour, it must be served locally -->
          <script src="${StringUtil.wrapString(javascript)}" type="text/javascript" djConfig="isDebug: false, parseOnLoad: true <#if Static["org.ofbiz.base.util.UtilHttp"].getLocale(request)?exists>, locale: '${Static["org.ofbiz.base.util.UtilHttp"].getLocale(request).getLanguage()}'</#if>"></script>
        <#else>
          <script src="<@ofbizContentUrl>${StringUtil.wrapString(javascript)}</@ofbizContentUrl>" type="text/javascript"></script>
        </#if>
      </#list>
    <script type="text/javascript">
        // This code set the timeout default value for opentaps.sendRequest
        var ajaxDefaultTimeOut = ${configProperties.get("opentaps.ajax.defaultTimeout")};
    </script>        
    <#if gwtScripts?exists>
      <meta name="gwt:property" content="locale=${locale}"/>
    </#if>

  <#assign callInEventIcon = Static["org.ofbiz.base.util.UtilProperties"].getPropertyValue("voip.properties", "voip.icon.callInEvent")>
  <#if gwtScripts?exists>
    <#list gwtScripts as gwtScript>
      <#if !gwtBlockScripts?has_content || !gwtBlockScripts.contains(gwtScript)>
        <@gwtModule widget=gwtScript />
      </#if>
    </#list>
    <#-- Bridge between server data and GWT widgets -->
    <script type="text/javascript" language="javascript">
      <#-- expose base permissions to GWT -->
      <#if user?has_content>
        var securityUser = new Object();
        <#list user.permissions as permission>
          securityUser["${permission}"] = true;
        </#list>
      </#if>
      <#-- set up the OpentapsConfig dictionary (see OpentapsConfig.java) -->
      var OpentapsConfig = {
      <#if configProperties.defaultCountryCode?has_content>
        defaultCountryCode: "${configProperties.defaultCountryCode}",
      </#if>
      <#if configProperties.defaultCountryGeoId?has_content>
        defaultCountryGeoId: "${configProperties.defaultCountryGeoId}",
      </#if>
      <#if configProperties.defaultCurrencyUomId?has_content>
        defaultCurrencyUomId: "${configProperties.defaultCurrencyUomId}",
      </#if>
      <#if callInEventIcon?has_content>
        callInEventIcon: "${callInEventIcon}",
      </#if>
      <#if infrastructure?has_content>
        showTopNavMenu: "${(infrastructure.getConfigurationValue("UI_TOP_NAV_MENU_SHOW"))!"Y"}",
      </#if>
        applicationName: "${opentapsApplicationName}"
      };
    </script>
  </#if>
<div style="width: 1280px;margin: 0 auto;position: relative;padding-bottom:50px;">
  <div align="left">
      
    <#-- voip notification -->
    <div class="gwtVoipNotification" id="gwtVoipNotification"></div>
      <#if applicationSetupFacility?has_content>
      <div class="insideHeaderSubtext">
        <b>${uiLabelMap.OpentapsWarehouse}</b>:&nbsp;${applicationSetupFacility.facilityName}&nbsp; (<@displayLink text="${uiLabelMap.CommonChange}" href="selectFacilityForm"/>)
      </div>
    </#if>
    <#if applicationSetupOrganization?has_content>
      <div class="insideHeaderSubtext">
        <b>${uiLabelMap.ProductOrganization}</b>:&nbsp;${applicationSetupOrganization.groupName}&nbsp; (<@displayLink text="${uiLabelMap.CommonChange}" href="selectOrganizationForm"/>)
      </div>
    </#if>
  
      <#-- live help link and search link.  -->
      <#if (infrastructure.getConfigurationValueAsBoolean("UI_HELP_LINK_SHOW"))!true>
        <#assign helpUrl = Static["org.opentaps.common.util.UtilCommon"].getUrlContextHelpResource(delegator, appName, parameters._CURRENT_VIEW_, screenState?default(""))!/>
      </#if>
  <div class="spacer"></div>
  <span hidden id="span001"><#if applicationSetupFacility??><#if applicationSetupFacility.facilityName??>${applicationSetupFacility.facilityName}</#if></#if></span>
  <span hidden id="span002"><#if applicationSetupFacility??><#if applicationSetupFacility.facilityId??>${applicationSetupFacility.facilityId}</#if></#if></span>

<script>
facility_tran_001 = document.getElementById("span001").innerText
facility_tran_002 = document.getElementById("span002").innerText
</script>