<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Content-Language" content="en" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="Content-Script-Type" content="text/javascript" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="imagetoolbar" content="false" />
    <meta name="robots" content="index, follow" />
    <meta name="googlebot" content="index,follow" />
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <meta name="copyright" content="" />
    <meta name="MSSmartTagsPreventParsing" content="true" />
    <meta name="author" content="" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <link rel="Shortcut Icon" type="image/x-icon" href="/commondefine_img/favicon.ico?t=20170503"/>
    <link rel="stylesheet" type="text/css" href="/commondefine_css/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/commondefine_css/yw_main.css?v=1.1">
    
    
    <script type="text/javascript" src="/commondefine_js/jquery.js"></script>
    <script type="text/javascript" src="/commondefine_js/loading.js"></script>
    <!-- <script type="text/javascript" src="/commondefine_js/piwik-local.js"></script> -->
	
	<script>document.title = 'Webpos | OCMS-BIM';</script>
</head>
<body>
<div id="edit_password_dialog">
	<div class="fixed_screen"></div>
	<div class="edit_password_box">
		<div class="edit_password_icon"></div>
		<div class="edit_password_title">Edit password</div>
		<div class="edit_password_content">
			<div class="input_group">
				<span>Username</span>
				<span>${userLogin.userLoginId}</span>
				<input type="hidden" class="user_name" value="${userLogin.userLoginId}" />
				<input type="hidden" class="user_id" value="${userLogin.partyId}" />
			</div>
			<div class="input_group">
				<span>Original password</span>
				<input type="password" class="original_password" />
			</div>
            <div class="input_group">
                <span>New password</span>
				<input type="password" class="new_password" />
            </div>
            <div class="input_group">
                <span>Repeat password</span>
                <input type="password" class="repeat_password" />
            </div>
		</div>
		<div class="edir_password_error_tips"><span></span></div>
		<div class="edit_password_operator">
			<a class="save" href="javascript:void(0);">Save</a>
		</div>
	</div>
</div>