<#if (requestAttributes.person)?exists><#assign person = requestAttributes.person></#if>
<#if (requestAttributes.partyGroup)?exists><#assign partyGroup = requestAttributes.partyGroup></#if>
<#assign docLangAttr = locale.toString()?replace("_", "-")>
<#assign langDir = "ltr">
<#if "ar.iw"?contains(docLangAttr?substring(0, 2))>
    <#assign langDir = "rtl">
</#if>

    <title>${layoutSettings.companyName}: <#if (page.titleProperty)?has_content>${uiLabelMap[page.titleProperty]}<#else>${(page.title)?if_exists}</#if></title>

    <#if layoutSettings.shortcutIcon?has_content>
      <#assign shortcutIcon = layoutSettings.shortcutIcon/>
    <#elseif layoutSettings.VT_SHORTCUT_ICON?has_content>
      <#assign shortcutIcon = layoutSettings.VT_SHORTCUT_ICON.get(0)/>
    </#if>
    <#if shortcutIcon?has_content>
    <link rel="shortcut icon" href="<@ofbizContentUrl>${StringUtil.wrapString(shortcutIcon)}</@ofbizContentUrl>" />
    </#if>
    <#if layoutSettings.styleSheets?has_content>
        <#list layoutSettings.styleSheets as styleSheet>
            <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>" media="screen,projection" type="text/css" charset="UTF-8"/>
        </#list>
    </#if>
    <#if userLogin?has_content>
    <#if layoutSettings.VT_STYLESHEET?has_content>
        <#list layoutSettings.VT_STYLESHEET as styleSheet>
            <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>" media="screen,projection" type="text/css" charset="UTF-8"/>
        </#list>
    </#if>
    <#else>
        <link rel="stylesheet" href="/bizznesstime/css/login.css" type="text/css"/>
    </#if>
    <#if layoutSettings.rtlStyleSheets?has_content && langDir == "rtl">
        <#--layoutSettings.rtlStyleSheets is a list of rtl style sheets.-->
        <#list layoutSettings.rtlStyleSheets as styleSheet>
            <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>" media="screen,projection" type="text/css" charset="UTF-8"/>
        </#list>
    </#if>
    <#if layoutSettings.VT_RTL_STYLESHEET?has_content && langDir == "rtl">
        <#list layoutSettings.VT_RTL_STYLESHEET as styleSheet>
            <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>" media="screen,projection" type="text/css" charset="UTF-8"/>
        </#list>
    </#if>
    ${layoutSettings.extraHead?if_exists}
    <#if layoutSettings.VT_EXTRA_HEAD?has_content>
        <#list layoutSettings.VT_EXTRA_HEAD as extraHead>
            ${extraHead}
        </#list>
    </#if>
    
      <#if layoutSettings.javaScripts?has_content>
        <#assign javaScriptsSet = Static["org.ofbiz.base.util.UtilMisc"].toSet(layoutSettings.javaScripts)/>
        <#list layoutSettings.javaScripts as javaScript>
            <#if javaScriptsSet.contains(javaScript)>
                <#assign nothing = javaScriptsSet.remove(javaScript)/>
                <script src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>" type="text/javascript"></script>
            </#if>
        </#list>
    </#if>
    <#if layoutSettings.VT_HDR_JAVASCRIPT?has_content>
        <#list layoutSettings.VT_HDR_JAVASCRIPT as javaScript>
            <script src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>" type="text/javascript"></script>
        </#list>
    </#if>
<div id="wrap">
  <div id="header">
    <div class="navigator-first">
<#if (requestAttributes.externalLoginKey)?exists><#assign externalKeyParam = "?externalLoginKey=" + requestAttributes.externalLoginKey?if_exists></#if>
<#if (externalLoginKey)?exists><#assign externalKeyParam = "?externalLoginKey=" + requestAttributes.externalLoginKey?if_exists></#if>
<#assign ofbizServerName = application.getAttribute("_serverId")?default("default-server")>
<#assign contextPath = request.getContextPath()>
<#assign displayApps = Static["org.ofbiz.base.component.ComponentConfig"].getAppBarWebInfos(ofbizServerName, "main")>

<#if userLogin?has_content>
                <ul style="margin-bottom:5px;">
		            <#list displayApps as display>
		              <#assign thisApp = display.getContextRoot()>
		              <#assign permission = true>
		              <#assign selected = false>
		              <#assign permissions = display.getBasePermission()>
		              <#list permissions as perm>
		                <#if perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session)>
		                  <#-- User must have ALL permissions in the base-permission list -->
		                  <#assign permission = false>
		                </#if>
		              </#list>
		              <#if permission == true>
		                <#if thisApp == contextPath || contextPath + "/" == thisApp>
		                  <#assign selected = true>
		                </#if>
		                <#assign thisURL = thisApp>
		                <#if thisApp != "/">
		                  <#assign thisURL = thisURL + "/control/main">
		                </#if>
		                  <#if layoutSettings.suppressTab?exists && display.name == layoutSettings.suppressTab>
		                    <!-- do not display this component-->
		                  <#else>
		                    <li><a href="${thisURL + externalKeyParam}" <#if uiLabelMap?exists> title="${uiLabelMap[display.description]}">${uiLabelMap[display.title]}<#else> title="${display.description}">${display.title}</#if></a></li>
		                  </#if>
		              </#if>
		            </#list>
                </ul>
                
                <#--include "component://bizznesstime/includes/secondary-appbar.ftl" /-->
        
</#if>  
        <#-- <ul>
            <li>
            <a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
            <li><a href="/catalog/control/main?externalLoginKey=EL84833530519" id="ext-gen636">Catalog</a></li>
        </ul>     -->
        <div id="controls">
            <span id="prefBtn">
                <a href="#" class="contracted">${uiLabelMap.CommonPreferences}</a>
                <span id="preferences" style="display:none">
                    <a href="<@ofbizUrl>ListLocales</@ofbizUrl>" id="language">${uiLabelMap.CommonLanguageTitle} - ${locale.getDisplayName(locale)}</a>
                    <a href="<@ofbizUrl>ListTimezones</@ofbizUrl>" id="timezone">${nowTimestamp?datetime?string.short} - ${timeZone.getDisplayName(timeZone.useDaylightTime(), Static["java.util.TimeZone"].LONG, locale)}</a>
                    <a href="<@ofbizUrl>ListVisualThemes</@ofbizUrl>" id="theme">${uiLabelMap.CommonVisualThemes}</a>
                </span>
            </span>
            <span>
            <#if person?has_content>
              ${uiLabelMap.CommonWelcome},  ${person.firstName?if_exists} ${person.lastName?if_exists} ( ${userLogin.userLoginId} )
            <#elseif partyGroup?has_content>
              ${uiLabelMap.CommonWelcome},  ${partyGroup.groupName?if_exists} ( ${userLogin.userLoginId} )
            <#else>
              ${uiLabelMap.CommonWelcome}
            </#if>
            </span>
            <span><a href="<@ofbizUrl>logout</@ofbizUrl>">${uiLabelMap.CommonLogout}</a></span>
            <#if webSiteId?exists && requestAttributes._CURRENT_VIEW_?exists>
              <#include "component://common/webcommon/includes/helplink.ftl" />
              <span><a href="javascript:lookup_popup2('showHelp?helpTopic=${helpTopic}&amp;portalPageId=${parameters.portalPageId?if_exists}','help' ,500,500);">${uiLabelMap.CommonHelp}</a></span>
           </#if>
    </div>
    </div>
   <div>
       <a href="/opentaps/">  
         <img alt="ordermgr.title" src="/opentaps_images/opentaps_logo.png" border="0"/>
       </a>
   </div>
    
</div>