/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
/* This file has been modified by Open Source Strategies, Inc. */

package org.ofbiz.common.login;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.common.authentication.AuthHelper;
import org.ofbiz.common.authentication.api.AuthenticatorException;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityFunction;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.security.Security;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.webapp.control.LoginWorker;

/**
 * <b>Title:</b> Login Services
 */
public class NewLoginServices {

    public static final String module = NewLoginServices.class.getName();
    public static final String resource = "SecurityextUiLabels";

    

    private static void createUserLoginPasswordHistory(Delegator delegator,String userLoginId, String currentPassword) throws GenericEntityException{
        int passwordChangeHistoryLimit = 0;
        try {
            passwordChangeHistoryLimit = Integer.parseInt(UtilProperties.getPropertyValue("security.properties", "password.change.history.limit", "0"));
        } catch (NumberFormatException nfe) {
            //No valid value is found so don't bother to save any password history
            passwordChangeHistoryLimit = 0;
        }
        if (passwordChangeHistoryLimit == 0 || passwordChangeHistoryLimit < 0) {
            // Not saving password history, so return from here.
            return;
        }

        EntityFindOptions efo = new EntityFindOptions();
        efo.setResultSetType(EntityFindOptions.TYPE_SCROLL_INSENSITIVE);
        EntityListIterator eli = delegator.find("UserLoginPasswordHistory", EntityCondition.makeConditionMap("userLoginId", userLoginId), null, null, UtilMisc.toList("-fromDate"), efo);
        Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
        GenericValue pwdHist;
        if ((pwdHist = eli.next()) != null) {
            // updating password so set end date on previous password in history
            pwdHist.set("thruDate", nowTimestamp);
            pwdHist.store();
            // check if we have hit the limit on number of password changes to be saved. If we did then delete the oldest password from history.
            eli.last();
            int rowIndex = eli.currentIndex();
            if (rowIndex==passwordChangeHistoryLimit) {
                eli.afterLast();
                pwdHist = eli.previous();
                pwdHist.remove();
            }
        }
        eli.close();

        // save this password in history
        GenericValue userLoginPwdHistToCreate = delegator.makeValue("UserLoginPasswordHistory", UtilMisc.toMap("userLoginId", userLoginId,"fromDate", nowTimestamp));
        boolean useEncryption = "true".equals(UtilProperties.getPropertyValue("security.properties", "password.encrypt"));
        userLoginPwdHistToCreate.set("currentPassword", useEncryption ? HashCrypt.getDigestHash(currentPassword, getHashType()) : currentPassword);
        userLoginPwdHistToCreate.create();
    }


    /** Updates UserLogin Password info
     *@param ctx The DispatchContext that this service is operating in
     *@param context Map containing the input parameters
     *@return Map with the result of the service, the output parameters
     */
    public static Map<String, Object> updatePassword(DispatchContext ctx, Map<String, ?> context) {
        Delegator delegator = ctx.getDelegator();
        Security security = ctx.getSecurity();
        GenericValue loggedInUserLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess(UtilProperties.getMessage(resource, "loginevents.password_was_changed_with_success", locale));

        // load the external auth modules -- note: this will only run once and cache the objects
        if (!AuthHelper.authenticatorsLoaded()) {
            AuthHelper.loadAuthenticators(ctx.getDispatcher());
        }

        boolean useEncryption = "true".equals(UtilProperties.getPropertyValue("security.properties", "password.encrypt"));
        boolean adminUser = false;

        String userLoginId = (String) context.get("userLoginId");
        String errMsg = null;

        if (UtilValidate.isEmpty(userLoginId)) {
            userLoginId = loggedInUserLogin.getString("userLoginId");
        }

        // <b>security check</b>: userLogin userLoginId must equal userLoginId, or must have PARTYMGR_UPDATE permission
        // NOTE: must check permission first so that admin users can set own password without specifying old password
        // TODO: change this security group because we can't use permission groups defined in the applications from the framework.
        
            adminUser = true;
        

        String currentPassword = (String) context.get("currentPassword");
        String newPassword = (String) context.get("newPassword");
        String newPasswordVerify = (String) context.get("newPasswordVerify");
        String passwordHint = (String) context.get("passwordHint");

        GenericValue userLoginToUpdate = null;

        try {
            userLoginToUpdate = delegator.findOne("UserLogin", false, "userLoginId", userLoginId);
        } catch (GenericEntityException e) {
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
            errMsg = UtilProperties.getMessage(resource,"loginservices.could_not_change_password_read_failure", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }

        if (userLoginToUpdate == null) {
            // this may be a full external authenticator; first try authenticating
            boolean authenticated = false;
            try {
                authenticated = AuthHelper.authenticate(userLoginId, currentPassword, true);
            } catch (AuthenticatorException e) {
                // safe to ingore this; but we'll log it just in case
                Debug.logWarning(e, e.getMessage(), module);
            }

            // call update password if auth passed
            if (authenticated) {
                try {
                    AuthHelper.updatePassword(userLoginId, currentPassword, newPassword);
                } catch (AuthenticatorException e) {
                    Debug.logError(e, e.getMessage(), module);
                    Map<String, String> messageMap = UtilMisc.toMap("userLoginId", userLoginId);
                    errMsg = UtilProperties.getMessage(resource,"loginservices.could_not_change_password_userlogin_with_id_not_exist", messageMap, locale);
                    return ServiceUtil.returnError(errMsg);
                }
                //result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
                result.put("updatedUserLogin", userLoginToUpdate);
                return result;
            } else {
                Map<String, String> messageMap = UtilMisc.toMap("userLoginId", userLoginId);
                errMsg = UtilProperties.getMessage(resource,"loginservices.could_not_change_password_userlogin_with_id_not_exist", messageMap, locale);
                return ServiceUtil.returnError(errMsg);
            }
        }

        if ("true".equals(UtilProperties.getPropertyValue("security.properties", "password.lowercase"))) {
            currentPassword = currentPassword.toLowerCase();
            newPassword = newPassword.toLowerCase();
            newPasswordVerify = newPasswordVerify.toLowerCase();
        }

        List<String> errorMessageList = FastList.newInstance();
        if (newPassword != null) {
            checkNewPassword(userLoginToUpdate, currentPassword, newPassword, newPasswordVerify,
                passwordHint, errorMessageList, adminUser, locale);
        }

        if (errorMessageList.size() > 0) {
            return ServiceUtil.returnError(errorMessageList);
        }

        String externalAuthId = userLoginToUpdate.getString("externalAuthId");
        if (UtilValidate.isNotEmpty(externalAuthId)) {
            // external auth is set; don't update the database record
            try {
                AuthHelper.updatePassword(externalAuthId, currentPassword, newPassword);
            } catch (AuthenticatorException e) {
                Debug.logError(e, e.getMessage(), module);
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                errMsg = UtilProperties.getMessage(resource,"loginservices.could_not_change_password_write_failure", messageMap, locale);
                return ServiceUtil.returnError(errMsg);
            }
        } else {
            userLoginToUpdate.set("currentPassword", useEncryption ? HashCrypt.getDigestHash(newPassword, getHashType()) : newPassword, false);
            userLoginToUpdate.set("passwordHint", passwordHint, false);
            userLoginToUpdate.set("requirePasswordChange", "N");

            try {
                userLoginToUpdate.store();
                createUserLoginPasswordHistory(delegator,userLoginId, newPassword);
            } catch (GenericEntityException e) {
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                errMsg = UtilProperties.getMessage(resource,"loginservices.could_not_change_password_write_failure", messageMap, locale);
                return ServiceUtil.returnError(errMsg);
            }
        }

        //result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
        result.put("updatedUserLogin", userLoginToUpdate);
        return result;
    }

   
    public static void checkNewPassword(GenericValue userLogin, String currentPassword, String newPassword, String newPasswordVerify, String passwordHint, List<String> errorMessageList, boolean ignoreCurrentPassword, Locale locale) {
        boolean useEncryption = "true".equals(UtilProperties.getPropertyValue("security.properties", "password.encrypt"));

        String errMsg = null;

        if (!ignoreCurrentPassword) {

            String encodedPassword = useEncryption ? HashCrypt.getDigestHash(currentPassword, getHashType()) : currentPassword;
            String encodedPasswordOldFunnyHexEncode = useEncryption ? HashCrypt.getDigestHashOldFunnyHexEncode(currentPassword, getHashType()) : currentPassword;
            String encodedPasswordUsingDbHashType = encodedPassword;

            String oldPassword = userLogin.getString("currentPassword");
            if (useEncryption && oldPassword != null && oldPassword.startsWith("{")) {
                // get encode according to the type in the database
                String dbHashType = HashCrypt.getHashTypeFromPrefix(oldPassword);
                if (dbHashType != null) {
                    encodedPasswordUsingDbHashType = HashCrypt.getDigestHash(currentPassword, dbHashType);
                }
            }

            // if the password.accept.encrypted.and.plain property in security is set to true allow plain or encrypted passwords
            // if this is a system account don't bother checking the passwords
            boolean passwordMatches = (oldPassword != null &&
                (HashCrypt.removeHashTypePrefix(encodedPassword).equals(HashCrypt.removeHashTypePrefix(oldPassword)) ||
                        HashCrypt.removeHashTypePrefix(encodedPasswordOldFunnyHexEncode).equals(HashCrypt.removeHashTypePrefix(oldPassword)) ||
                        HashCrypt.removeHashTypePrefix(encodedPasswordUsingDbHashType).equals(HashCrypt.removeHashTypePrefix(oldPassword)) ||
                    ("true".equals(UtilProperties.getPropertyValue("security.properties", "password.accept.encrypted.and.plain")) && currentPassword.equals(oldPassword))));

            if ((currentPassword == null) || (userLogin != null && currentPassword != null && !passwordMatches)) {
                errMsg = UtilProperties.getMessage(resource,"loginservices.old_password_not_correct_reenter", locale);
                errorMessageList.add(errMsg);
            }
            if (currentPassword.equals(newPassword) || encodedPassword.equals(newPassword)) {
                errMsg = UtilProperties.getMessage(resource,"loginservices.new_password_is_equal_to_old_password", locale);
                errorMessageList.add(errMsg);
            }

        }

        if (!UtilValidate.isNotEmpty(newPassword) || !UtilValidate.isNotEmpty(newPasswordVerify)) {
            errMsg = UtilProperties.getMessage(resource,"loginservices.password_or_verify_missing", locale);
            errorMessageList.add(errMsg);
        } else if (!newPassword.equals(newPasswordVerify)) {
            errMsg = UtilProperties.getMessage(resource,"loginservices.password_did_not_match_verify_password", locale);
            errorMessageList.add(errMsg);
        }

        int passwordChangeHistoryLimit = 0;
        try {
            passwordChangeHistoryLimit = Integer.parseInt(UtilProperties.getPropertyValue("security.properties", "password.change.history.limit", "0"));
        } catch (NumberFormatException nfe) {
            //No valid value is found so don't bother to save any password history
            passwordChangeHistoryLimit = 0;
        }
        Debug.logInfo(" password.change.history.limit is set to " + passwordChangeHistoryLimit, module);
        Debug.logInfo(" userLogin is set to " + userLogin, module);
        if (passwordChangeHistoryLimit > 0 && userLogin != null) {
            Debug.logInfo(" checkNewPassword Checking if user is tyring to use old password " + passwordChangeHistoryLimit, module);
            Delegator delegator = userLogin.getDelegator();
            String newPasswordHash = newPassword;
            if (useEncryption) {
                newPasswordHash = HashCrypt.getDigestHash(newPassword, getHashType());
            }
            try {
                List<GenericValue> pwdHistList = delegator.findByAnd("UserLoginPasswordHistory", UtilMisc.toMap("userLoginId",userLogin.getString("userLoginId"),"currentPassword",newPasswordHash));
                Debug.logInfo(" checkNewPassword pwdHistListpwdHistList " + pwdHistList.size(), module);
                if (pwdHistList.size() >0) {
                    Map<String, Integer> messageMap = UtilMisc.toMap("passwordChangeHistoryLimit", passwordChangeHistoryLimit);
                    errMsg = UtilProperties.getMessage(resource,"loginservices.password_must_be_different_from_last_passwords", messageMap, locale);
                    errorMessageList.add(errMsg);
                    Debug.logInfo(" checkNewPassword errorMessageListerrorMessageList " + pwdHistList.size(), module);
                }
            } catch (GenericEntityException e) {
                Debug.logWarning(e, "", module);
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                errMsg = UtilProperties.getMessage(resource,"loginevents.error_accessing_password_change_history", messageMap, locale);
            }

        }

        int minPasswordLength = 0;

        try {
            minPasswordLength = Integer.parseInt(UtilProperties.getPropertyValue("security.properties", "password.length.min", "0"));
        } catch (NumberFormatException nfe) {
            minPasswordLength = 0;
        }

        if (newPassword != null) {
            if (!(newPassword.length() >= minPasswordLength)) {
                Map<String, String> messageMap = UtilMisc.toMap("minPasswordLength", Integer.toString(minPasswordLength));
                errMsg = UtilProperties.getMessage(resource,"loginservices.password_must_be_least_characters_long", messageMap, locale);
                errorMessageList.add(errMsg);
            }
            if (userLogin != null && newPassword.equalsIgnoreCase(userLogin.getString("userLoginId"))) {
                errMsg = UtilProperties.getMessage(resource,"loginservices.password_may_not_equal_username", locale);
                errorMessageList.add(errMsg);
            }
            if (UtilValidate.isNotEmpty(passwordHint) && (passwordHint.toUpperCase().indexOf(newPassword.toUpperCase()) >= 0)) {
                errMsg = UtilProperties.getMessage(resource,"loginservices.password_hint_may_not_contain_password", locale);
                errorMessageList.add(errMsg);
            }
        }
    }

    public static String getHashType() {
        String hashType = UtilProperties.getPropertyValue("security.properties", "password.encrypt.hash.type");

        if (UtilValidate.isEmpty(hashType)) {
            Debug.logWarning("Password encrypt hash type is not specified in security.properties, use SHA", module);
            hashType = "SHA";
        }

        return hashType;
    }
    
    
    
    public static Map<String, Object> newUpdatePassword(HttpServletRequest request, HttpServletResponse response) {
    	
    	HttpSession session = request.getSession();
    	GenericValue userLogin = (GenericValue)session.getAttribute("userLogin");
    	
    	
    	try {
    		Delegator delegator = (Delegator) request.getAttribute("delegator");
        	String userLoginId = request.getParameter("userLoginId");
        	String partyId = request.getParameter("partyId");
        	String currentPassword = request.getParameter("currentPassword");
        	String newPassword = request.getParameter("newPassword");
        	String newPasswordVerify = request.getParameter("newPasswordVerify");
        	String passwordHint = request.getParameter("passwordHint");
        	String localeLanguage = request.getParameter("localeLanguage");
        	List<String> errorMessageList = FastList.newInstance();
//        	GenericValue updatedUserLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId",userLoginId), false);
        	GenericValue userLoginToUpdate = delegator.findOne("UserLogin", false, "userLoginId", userLoginId);
        	if(!newPassword.equals(newPasswordVerify)){
        		return ServiceUtil.returnError("new passwords are inconsistent");
        	}

        	
        	
        	
        	boolean useEncryption = "true".equals(UtilProperties.getPropertyValue("security.properties", "password.encrypt"));
        	
        	if(currentPassword != null){
        		String encodedPassword = useEncryption ? HashCrypt.getDigestHash(currentPassword, getHashType()) : currentPassword;
        		if(encodedPassword.length() > 40){
        			encodedPassword = encodedPassword.substring(5, encodedPassword.length());
        		}
        		String encodedPasswordOldFunnyHexEncode = useEncryption ? HashCrypt.getDigestHashOldFunnyHexEncode(currentPassword, getHashType()) : currentPassword;
        		if(encodedPasswordOldFunnyHexEncode.length() > 40){
        			encodedPasswordOldFunnyHexEncode = encodedPasswordOldFunnyHexEncode.substring(5, encodedPasswordOldFunnyHexEncode.length());
        		}
        		String encryptionCurrentPassword = userLoginToUpdate.getString("currentPassword");
        		if(encryptionCurrentPassword.length() > 40){
        			encryptionCurrentPassword = encryptionCurrentPassword.substring(5, encryptionCurrentPassword.length());
        		}
        		if(!encryptionCurrentPassword.equals(encodedPassword) && !encryptionCurrentPassword.equals(encodedPasswordOldFunnyHexEncode)){
        			return ServiceUtil.returnError("original password error");
        		}
        	}else{
        		return ServiceUtil.returnError("Please enter the original password");
        	}
        	

        	
        	Map<String, Object> context = new HashMap<String, Object>();
        	
        	context.put("userLogin", userLogin);
        	context.put("userLoginId", userLoginId);
//        	context.put("partyId", partyId);
        	context.put("currentPassword", currentPassword);
        	context.put("newPassword", newPassword);
        	context.put("newPasswordVerify", newPasswordVerify);
        	context.put("passwordHint", passwordHint);
        	
        	
        	
        	LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
			Map<String, Object> result = dispatcher.runSync("newUpdatePassword", context);
			if(result != null){
				return ServiceUtil.returnSuccess("update success");
			}else{
				return ServiceUtil.returnError("update error");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServiceUtil.returnError("update error");
		}
    }
}
