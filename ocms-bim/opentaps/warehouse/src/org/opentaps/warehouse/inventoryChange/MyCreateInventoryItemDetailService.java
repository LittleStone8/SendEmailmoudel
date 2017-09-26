package org.opentaps.warehouse.inventoryChange;


import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.ofbiz.entity.GenericValue;
import org.opentaps.foundation.infrastructure.User;
import org.opentaps.foundation.service.ServiceWrapper;


public class MyCreateInventoryItemDetailService extends ServiceWrapper{


    public static final String NAME = "createInventoryItemDetail";
    public static final Boolean REQUIRES_AUTHENTICATION;
    public static final Boolean REQUIRES_NEW_TRANSACTION;
    public static final Boolean USES_TRANSACTION;
    private BigDecimal inAccountingQuantityDiff;
    private BigDecimal inAvailableToPromiseDiff;
    private String inDescription;
    private String inFixedAssetId;
    private String inInventoryItemId;
    private String inItemIssuanceId;
    private Locale inLocale;
    private String inLoginPassword;
    private String inLoginUsername;
    private String inMaintHistSeqId;
    private String inOrderId;
    private String inOrderItemSeqId;
    private String inPhysicalInventoryId;
    private BigDecimal inQuantityOnHandDiff;
    private String inReasonEnumId;
    private String inReceiptId;
    private String inReturnId;
    private String inReturnItemSeqId;
    private String inShipGroupSeqId;
    private String inShipmentId;
    private String inShipmentItemSeqId;
    
    private String operator;
    private String usageType;
    private String fromWarehouse;
    private String toWarehouse;
    private BigDecimal qoh;
    private BigDecimal atp;
    
    private TimeZone inTimeZone;
    private BigDecimal inUnitCost;
    private GenericValue inUserLogin;
    private String inWorkEffortId;
    private String outErrorMessage;
    private List outErrorMessageList;
    private String outInventoryItemDetailSeqId;
    private Locale outLocale;
    private String outResponseMessage;
    private String outSuccessMessage;
    private List outSuccessMessageList;
    private TimeZone outTimeZone;
    private GenericValue outUserLogin;
    private Set<String> inParameters = FastSet.newInstance();
    private Set<String> outParameters = FastSet.newInstance();

    public MyCreateInventoryItemDetailService() {
    }

    public MyCreateInventoryItemDetailService(User user) {
        super(user);
    }

    
    
    public BigDecimal getQoh() {
		return qoh;
	}

	public void setQoh(BigDecimal qoh) {
		this.inParameters.add("qoh");
		this.qoh = qoh;
	}

	public BigDecimal getAtp() {
		return atp;
	}

	public void setAtp(BigDecimal atp) {
		this.inParameters.add("atp");
		this.atp = atp;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.inParameters.add("operator");
		this.operator = operator;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.inParameters.add("usageType");
		this.usageType = usageType;
	}

	public String getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(String fromWarehouse) {
		this.inParameters.add("fromWarehouse");
		this.fromWarehouse = fromWarehouse;
	}

	public String getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(String toWarehouse) {
		this.inParameters.add("toWarehouse");
		this.toWarehouse = toWarehouse;
	}

	public BigDecimal getInAccountingQuantityDiff() {
        return this.inAccountingQuantityDiff;
    }

    public BigDecimal getInAvailableToPromiseDiff() {
        return this.inAvailableToPromiseDiff;
    }

    public String getInDescription() {
        return this.inDescription;
    }

    public String getInFixedAssetId() {
        return this.inFixedAssetId;
    }

    public String getInInventoryItemId() {
        return this.inInventoryItemId;
    }

    public String getInItemIssuanceId() {
        return this.inItemIssuanceId;
    }

    public Locale getInLocale() {
        return this.inLocale;
    }

    public String getInLoginPassword() {
        return this.inLoginPassword;
    }

    public String getInLoginUsername() {
        return this.inLoginUsername;
    }

    public String getInMaintHistSeqId() {
        return this.inMaintHistSeqId;
    }

    public String getInOrderId() {
        return this.inOrderId;
    }

    public String getInOrderItemSeqId() {
        return this.inOrderItemSeqId;
    }

    public String getInPhysicalInventoryId() {
        return this.inPhysicalInventoryId;
    }

    public BigDecimal getInQuantityOnHandDiff() {
        return this.inQuantityOnHandDiff;
    }

    public String getInReasonEnumId() {
        return this.inReasonEnumId;
    }

    public String getInReceiptId() {
        return this.inReceiptId;
    }

    public String getInReturnId() {
        return this.inReturnId;
    }

    public String getInReturnItemSeqId() {
        return this.inReturnItemSeqId;
    }

    public String getInShipGroupSeqId() {
        return this.inShipGroupSeqId;
    }

    public String getInShipmentId() {
        return this.inShipmentId;
    }

    public String getInShipmentItemSeqId() {
        return this.inShipmentItemSeqId;
    }

    public TimeZone getInTimeZone() {
        return this.inTimeZone;
    }

    public BigDecimal getInUnitCost() {
        return this.inUnitCost;
    }

    public GenericValue getInUserLogin() {
        return this.inUserLogin;
    }

    public String getInWorkEffortId() {
        return this.inWorkEffortId;
    }

    public String getOutErrorMessage() {
        return this.outErrorMessage;
    }

    public List getOutErrorMessageList() {
        return this.outErrorMessageList;
    }

    public String getOutInventoryItemDetailSeqId() {
        return this.outInventoryItemDetailSeqId;
    }

    public Locale getOutLocale() {
        return this.outLocale;
    }

    public String getOutResponseMessage() {
        return this.outResponseMessage;
    }

    public String getOutSuccessMessage() {
        return this.outSuccessMessage;
    }

    public List getOutSuccessMessageList() {
        return this.outSuccessMessageList;
    }

    public TimeZone getOutTimeZone() {
        return this.outTimeZone;
    }

    public GenericValue getOutUserLogin() {
        return this.outUserLogin;
    }

    public void setInAccountingQuantityDiff(BigDecimal inAccountingQuantityDiff) {
        this.inParameters.add("accountingQuantityDiff");
        this.inAccountingQuantityDiff = inAccountingQuantityDiff;
    }

    public void setInAvailableToPromiseDiff(BigDecimal inAvailableToPromiseDiff) {
        this.inParameters.add("availableToPromiseDiff");
        this.inAvailableToPromiseDiff = inAvailableToPromiseDiff;
    }

    public void setInDescription(String inDescription) {
        this.inParameters.add("description");
        this.inDescription = inDescription;
    }

    public void setInFixedAssetId(String inFixedAssetId) {
        this.inParameters.add("fixedAssetId");
        this.inFixedAssetId = inFixedAssetId;
    }

    public void setInInventoryItemId(String inInventoryItemId) {
        this.inParameters.add("inventoryItemId");
        this.inInventoryItemId = inInventoryItemId;
    }

    public void setInItemIssuanceId(String inItemIssuanceId) {
        this.inParameters.add("itemIssuanceId");
        this.inItemIssuanceId = inItemIssuanceId;
    }

    public void setInLocale(Locale inLocale) {
        this.inParameters.add("locale");
        this.inLocale = inLocale;
    }

    public void setInLoginPassword(String inLoginPassword) {
        this.inParameters.add("login.password");
        this.inLoginPassword = inLoginPassword;
    }

    public void setInLoginUsername(String inLoginUsername) {
        this.inParameters.add("login.username");
        this.inLoginUsername = inLoginUsername;
    }

    public void setInMaintHistSeqId(String inMaintHistSeqId) {
        this.inParameters.add("maintHistSeqId");
        this.inMaintHistSeqId = inMaintHistSeqId;
    }

    public void setInOrderId(String inOrderId) {
        this.inParameters.add("orderId");
        this.inOrderId = inOrderId;
    }

    public void setInOrderItemSeqId(String inOrderItemSeqId) {
        this.inParameters.add("orderItemSeqId");
        this.inOrderItemSeqId = inOrderItemSeqId;
    }

    public void setInPhysicalInventoryId(String inPhysicalInventoryId) {
        this.inParameters.add("physicalInventoryId");
        this.inPhysicalInventoryId = inPhysicalInventoryId;
    }

    public void setInQuantityOnHandDiff(BigDecimal inQuantityOnHandDiff) {
        this.inParameters.add("quantityOnHandDiff");
        this.inQuantityOnHandDiff = inQuantityOnHandDiff;
    }

    public void setInReasonEnumId(String inReasonEnumId) {
        this.inParameters.add("reasonEnumId");
        this.inReasonEnumId = inReasonEnumId;
    }

    public void setInReceiptId(String inReceiptId) {
        this.inParameters.add("receiptId");
        this.inReceiptId = inReceiptId;
    }

    public void setInReturnId(String inReturnId) {
        this.inParameters.add("returnId");
        this.inReturnId = inReturnId;
    }

    public void setInReturnItemSeqId(String inReturnItemSeqId) {
        this.inParameters.add("returnItemSeqId");
        this.inReturnItemSeqId = inReturnItemSeqId;
    }

    public void setInShipGroupSeqId(String inShipGroupSeqId) {
        this.inParameters.add("shipGroupSeqId");
        this.inShipGroupSeqId = inShipGroupSeqId;
    }

    public void setInShipmentId(String inShipmentId) {
        this.inParameters.add("shipmentId");
        this.inShipmentId = inShipmentId;
    }

    public void setInShipmentItemSeqId(String inShipmentItemSeqId) {
        this.inParameters.add("shipmentItemSeqId");
        this.inShipmentItemSeqId = inShipmentItemSeqId;
    }

    public void setInTimeZone(TimeZone inTimeZone) {
        this.inParameters.add("timeZone");
        this.inTimeZone = inTimeZone;
    }

    public void setInUnitCost(BigDecimal inUnitCost) {
        this.inParameters.add("unitCost");
        this.inUnitCost = inUnitCost;
    }

    public void setInUserLogin(GenericValue inUserLogin) {
        this.inParameters.add("userLogin");
        this.inUserLogin = inUserLogin;
    }

    public void setInWorkEffortId(String inWorkEffortId) {
        this.inParameters.add("workEffortId");
        this.inWorkEffortId = inWorkEffortId;
    }

    public void setOutErrorMessage(String outErrorMessage) {
        this.outParameters.add("errorMessage");
        this.outErrorMessage = outErrorMessage;
    }

    public void setOutErrorMessageList(List outErrorMessageList) {
        this.outParameters.add("errorMessageList");
        this.outErrorMessageList = outErrorMessageList;
    }

    public void setOutInventoryItemDetailSeqId(String outInventoryItemDetailSeqId) {
        this.outParameters.add("inventoryItemDetailSeqId");
        this.outInventoryItemDetailSeqId = outInventoryItemDetailSeqId;
    }

    public void setOutLocale(Locale outLocale) {
        this.outParameters.add("locale");
        this.outLocale = outLocale;
    }

    public void setOutResponseMessage(String outResponseMessage) {
        this.outParameters.add("responseMessage");
        this.outResponseMessage = outResponseMessage;
    }

    public void setOutSuccessMessage(String outSuccessMessage) {
        this.outParameters.add("successMessage");
        this.outSuccessMessage = outSuccessMessage;
    }

    public void setOutSuccessMessageList(List outSuccessMessageList) {
        this.outParameters.add("successMessageList");
        this.outSuccessMessageList = outSuccessMessageList;
    }

    public void setOutTimeZone(TimeZone outTimeZone) {
        this.outParameters.add("timeZone");
        this.outTimeZone = outTimeZone;
    }

    public void setOutUserLogin(GenericValue outUserLogin) {
        this.outParameters.add("userLogin");
        this.outUserLogin = outUserLogin;
    }

    public String name() {
        return "createInventoryItemDetail";
    }

    public Boolean requiresAuthentication() {
        return REQUIRES_AUTHENTICATION;
    }

    public Boolean requiresNewTransaction() {
        return REQUIRES_NEW_TRANSACTION;
    }

    public Boolean usesTransaction() {
        return USES_TRANSACTION;
    }

    public Map<String, Object> inputMap() {
        Map<String, Object> mapValue = new FastMap();
        if(this.inParameters.contains("accountingQuantityDiff")) {
            mapValue.put("accountingQuantityDiff", this.getInAccountingQuantityDiff());
        }

        if(this.inParameters.contains("availableToPromiseDiff")) {
            mapValue.put("availableToPromiseDiff", this.getInAvailableToPromiseDiff());
        }

        if(this.inParameters.contains("description")) {
            mapValue.put("description", this.getInDescription());
        }

        if(this.inParameters.contains("fixedAssetId")) {
            mapValue.put("fixedAssetId", this.getInFixedAssetId());
        }

        if(this.inParameters.contains("inventoryItemId")) {
            mapValue.put("inventoryItemId", this.getInInventoryItemId());
        }

        if(this.inParameters.contains("itemIssuanceId")) {
            mapValue.put("itemIssuanceId", this.getInItemIssuanceId());
        }

        if(this.inParameters.contains("locale")) {
            mapValue.put("locale", this.getInLocale());
        }

        if(this.inParameters.contains("login.password")) {
            mapValue.put("login.password", this.getInLoginPassword());
        }

        if(this.inParameters.contains("login.username")) {
            mapValue.put("login.username", this.getInLoginUsername());
        }

        if(this.inParameters.contains("maintHistSeqId")) {
            mapValue.put("maintHistSeqId", this.getInMaintHistSeqId());
        }

        if(this.inParameters.contains("orderId")) {
            mapValue.put("orderId", this.getInOrderId());
        }

        if(this.inParameters.contains("orderItemSeqId")) {
            mapValue.put("orderItemSeqId", this.getInOrderItemSeqId());
        }

        if(this.inParameters.contains("physicalInventoryId")) {
            mapValue.put("physicalInventoryId", this.getInPhysicalInventoryId());
        }

        if(this.inParameters.contains("quantityOnHandDiff")) {
            mapValue.put("quantityOnHandDiff", this.getInQuantityOnHandDiff());
        }

        if(this.inParameters.contains("reasonEnumId")) {
            mapValue.put("reasonEnumId", this.getInReasonEnumId());
        }

        if(this.inParameters.contains("receiptId")) {
            mapValue.put("receiptId", this.getInReceiptId());
        }

        if(this.inParameters.contains("returnId")) {
            mapValue.put("returnId", this.getInReturnId());
        }

        if(this.inParameters.contains("returnItemSeqId")) {
            mapValue.put("returnItemSeqId", this.getInReturnItemSeqId());
        }

        if(this.inParameters.contains("shipGroupSeqId")) {
            mapValue.put("shipGroupSeqId", this.getInShipGroupSeqId());
        }

        if(this.inParameters.contains("shipmentId")) {
            mapValue.put("shipmentId", this.getInShipmentId());
        }

        if(this.inParameters.contains("shipmentItemSeqId")) {
            mapValue.put("shipmentItemSeqId", this.getInShipmentItemSeqId());
        }

        if(this.inParameters.contains("timeZone")) {
            mapValue.put("timeZone", this.getInTimeZone());
        }

        if(this.inParameters.contains("unitCost")) {
            mapValue.put("unitCost", this.getInUnitCost());
        }

        if(this.inParameters.contains("userLogin")) {
            mapValue.put("userLogin", this.getInUserLogin());
        }

        if(this.inParameters.contains("workEffortId")) {
            mapValue.put("workEffortId", this.getInWorkEffortId());
        }

        if(this.getUser() != null) {
            mapValue.put("userLogin", this.getUser().getOfbizUserLogin());
        }
        
        if(this.inParameters.contains("operator")) {
            mapValue.put("operator", this.getOperator());
        }
        
        if(this.inParameters.contains("usageType")) {
            mapValue.put("usageType", this.getUsageType());
        }
        
        if(this.inParameters.contains("fromWarehouse")) {
            mapValue.put("fromWarehouse", this.getFromWarehouse());
        }
        
        if(this.inParameters.contains("toWarehouse")) {
            mapValue.put("toWarehouse", this.getToWarehouse());
        }
        
        if(this.inParameters.contains("qoh")) {
            mapValue.put("qoh", this.getQoh());
        }
        
        if(this.inParameters.contains("atp")) {
            mapValue.put("atp", this.getAtp());
        }

        return mapValue;
    }

    public Map<String, Object> outputMap() {
        Map<String, Object> mapValue = new FastMap();
        if(this.outParameters.contains("errorMessage")) {
            mapValue.put("errorMessage", this.getOutErrorMessage());
        }

        if(this.outParameters.contains("errorMessageList")) {
            mapValue.put("errorMessageList", this.getOutErrorMessageList());
        }

        if(this.outParameters.contains("inventoryItemDetailSeqId")) {
            mapValue.put("inventoryItemDetailSeqId", this.getOutInventoryItemDetailSeqId());
        }

        if(this.outParameters.contains("locale")) {
            mapValue.put("locale", this.getOutLocale());
        }

        if(this.outParameters.contains("responseMessage")) {
            mapValue.put("responseMessage", this.getOutResponseMessage());
        }

        if(this.outParameters.contains("successMessage")) {
            mapValue.put("successMessage", this.getOutSuccessMessage());
        }

        if(this.outParameters.contains("successMessageList")) {
            mapValue.put("successMessageList", this.getOutSuccessMessageList());
        }

        if(this.outParameters.contains("timeZone")) {
            mapValue.put("timeZone", this.getOutTimeZone());
        }

        if(this.outParameters.contains("userLogin")) {
            mapValue.put("userLogin", this.getOutUserLogin());
        }

        return mapValue;
    }

    public void putAllInput(Map<String, Object> mapValue) {
        if(mapValue.containsKey("accountingQuantityDiff")) {
            this.setInAccountingQuantityDiff((BigDecimal)mapValue.get("accountingQuantityDiff"));
        }

        if(mapValue.containsKey("availableToPromiseDiff")) {
            this.setInAvailableToPromiseDiff((BigDecimal)mapValue.get("availableToPromiseDiff"));
        }

        if(mapValue.containsKey("description")) {
            this.setInDescription((String)mapValue.get("description"));
        }

        if(mapValue.containsKey("fixedAssetId")) {
            this.setInFixedAssetId((String)mapValue.get("fixedAssetId"));
        }

        if(mapValue.containsKey("inventoryItemId")) {
            this.setInInventoryItemId((String)mapValue.get("inventoryItemId"));
        }

        if(mapValue.containsKey("itemIssuanceId")) {
            this.setInItemIssuanceId((String)mapValue.get("itemIssuanceId"));
        }

        if(mapValue.containsKey("locale")) {
            this.setInLocale((Locale)mapValue.get("locale"));
        }

        if(mapValue.containsKey("login.password")) {
            this.setInLoginPassword((String)mapValue.get("login.password"));
        }

        if(mapValue.containsKey("login.username")) {
            this.setInLoginUsername((String)mapValue.get("login.username"));
        }

        if(mapValue.containsKey("maintHistSeqId")) {
            this.setInMaintHistSeqId((String)mapValue.get("maintHistSeqId"));
        }

        if(mapValue.containsKey("orderId")) {
            this.setInOrderId((String)mapValue.get("orderId"));
        }

        if(mapValue.containsKey("orderItemSeqId")) {
            this.setInOrderItemSeqId((String)mapValue.get("orderItemSeqId"));
        }

        if(mapValue.containsKey("physicalInventoryId")) {
            this.setInPhysicalInventoryId((String)mapValue.get("physicalInventoryId"));
        }

        if(mapValue.containsKey("quantityOnHandDiff")) {
            this.setInQuantityOnHandDiff((BigDecimal)mapValue.get("quantityOnHandDiff"));
        }

        if(mapValue.containsKey("reasonEnumId")) {
            this.setInReasonEnumId((String)mapValue.get("reasonEnumId"));
        }

        if(mapValue.containsKey("receiptId")) {
            this.setInReceiptId((String)mapValue.get("receiptId"));
        }

        if(mapValue.containsKey("returnId")) {
            this.setInReturnId((String)mapValue.get("returnId"));
        }

        if(mapValue.containsKey("returnItemSeqId")) {
            this.setInReturnItemSeqId((String)mapValue.get("returnItemSeqId"));
        }

        if(mapValue.containsKey("shipGroupSeqId")) {
            this.setInShipGroupSeqId((String)mapValue.get("shipGroupSeqId"));
        }

        if(mapValue.containsKey("shipmentId")) {
            this.setInShipmentId((String)mapValue.get("shipmentId"));
        }

        if(mapValue.containsKey("shipmentItemSeqId")) {
            this.setInShipmentItemSeqId((String)mapValue.get("shipmentItemSeqId"));
        }

        if(mapValue.containsKey("timeZone")) {
            this.setInTimeZone((TimeZone)mapValue.get("timeZone"));
        }

        if(mapValue.containsKey("unitCost")) {
            this.setInUnitCost((BigDecimal)mapValue.get("unitCost"));
        }

        if(mapValue.containsKey("userLogin")) {
            this.setInUserLogin((GenericValue)mapValue.get("userLogin"));
        }

        if(mapValue.containsKey("workEffortId")) {
            this.setInWorkEffortId((String)mapValue.get("workEffortId"));
        }

        if(mapValue.containsKey("operator")) { 
            this.setOperator((String)mapValue.get("operator"));
        }
        
        if(mapValue.containsKey("usageType")) {
            this.setUsageType((String)mapValue.get("usageType"));
        }
        
        if(mapValue.containsKey("fromWarehouse")) {
            this.setFromWarehouse((String)mapValue.get("fromWarehouse"));
        }
        
        if(mapValue.containsKey("toWarehouse")) {
            this.setToWarehouse((String)mapValue.get("toWarehouse"));
        }
        
        if(mapValue.containsKey("qoh")) {
            this.setQoh((BigDecimal)mapValue.get("qoh"));
        }
        
        if(mapValue.containsKey("atp")) {
            this.setAtp((BigDecimal)mapValue.get("atp"));
        }
    }

    public void putAllOutput(Map<String, Object> mapValue) {
        if(mapValue.containsKey("errorMessage")) {
            this.setOutErrorMessage((String)mapValue.get("errorMessage"));
        }

        if(mapValue.containsKey("errorMessageList")) {
            this.setOutErrorMessageList((List)mapValue.get("errorMessageList"));
        }

        if(mapValue.containsKey("inventoryItemDetailSeqId")) {
            this.setOutInventoryItemDetailSeqId((String)mapValue.get("inventoryItemDetailSeqId"));
        }

        if(mapValue.containsKey("locale")) {
            this.setOutLocale((Locale)mapValue.get("locale"));
        }

        if(mapValue.containsKey("responseMessage")) {
            this.setOutResponseMessage((String)mapValue.get("responseMessage"));
        }

        if(mapValue.containsKey("successMessage")) {
            this.setOutSuccessMessage((String)mapValue.get("successMessage"));
        }

        if(mapValue.containsKey("successMessageList")) {
            this.setOutSuccessMessageList((List)mapValue.get("successMessageList"));
        }

        if(mapValue.containsKey("timeZone")) {
            this.setOutTimeZone((TimeZone)mapValue.get("timeZone"));
        }

        if(mapValue.containsKey("userLogin")) {
            this.setOutUserLogin((GenericValue)mapValue.get("userLogin"));
        }

    }

    public static MyCreateInventoryItemDetailService fromInput(MyCreateInventoryItemDetailService input) {
        new MyCreateInventoryItemDetailService();
        return fromInput(input.inputMap());
    }

    public static MyCreateInventoryItemDetailService fromOutput(MyCreateInventoryItemDetailService output) {
    	MyCreateInventoryItemDetailService service = new MyCreateInventoryItemDetailService();
        service.putAllOutput(output.outputMap());
        return service;
    }

    public static MyCreateInventoryItemDetailService fromInput(Map<String, Object> mapValue) {
    	MyCreateInventoryItemDetailService service = new MyCreateInventoryItemDetailService();
        service.putAllInput(mapValue);
        if(mapValue.containsKey("userLogin")) {
            GenericValue userGv = (GenericValue)mapValue.get("userLogin");
            if(userGv != null) {
                service.setUser(new User(userGv, userGv.getDelegator()));
            }
        }

        return service;
    }

    public static MyCreateInventoryItemDetailService fromOutput(Map<String, Object> mapValue) {
    	MyCreateInventoryItemDetailService service = new MyCreateInventoryItemDetailService();
        service.putAllOutput(mapValue);
        return service;
    }

    static {
        REQUIRES_AUTHENTICATION = Boolean.TRUE;
        REQUIRES_NEW_TRANSACTION = Boolean.FALSE;
        USES_TRANSACTION = Boolean.TRUE;
    }

    public static enum Out {
        errorMessage("errorMessage"),
        errorMessageList("errorMessageList"),
        inventoryItemDetailSeqId("inventoryItemDetailSeqId"),
        locale("locale"),
        responseMessage("responseMessage"),
        successMessage("successMessage"),
        successMessageList("successMessageList"),
        timeZone("timeZone"),
        userLogin("userLogin");

        private final String _fieldName;

        private Out(String name) {
            this._fieldName = name;
        }

        public String getName() {
            return this._fieldName;
        }
    }

    public static enum In {
        accountingQuantityDiff("accountingQuantityDiff"),
        availableToPromiseDiff("availableToPromiseDiff"),
        description("description"),
        fixedAssetId("fixedAssetId"),
        inventoryItemId("inventoryItemId"),
        itemIssuanceId("itemIssuanceId"),
        locale("locale"),
        loginPassword("login.password"),
        loginUsername("login.username"),
        maintHistSeqId("maintHistSeqId"),
        orderId("orderId"),
        orderItemSeqId("orderItemSeqId"),
        physicalInventoryId("physicalInventoryId"),
        quantityOnHandDiff("quantityOnHandDiff"),
        reasonEnumId("reasonEnumId"),
        receiptId("receiptId"),
        returnId("returnId"),
        returnItemSeqId("returnItemSeqId"),
        shipGroupSeqId("shipGroupSeqId"),
        shipmentId("shipmentId"),
        shipmentItemSeqId("shipmentItemSeqId"),
        timeZone("timeZone"),
        unitCost("unitCost"),
        userLogin("userLogin"),
        workEffortId("workEffortId"),  	
    	operator("operator"),
    	usageType("usageType"),
    	fromWarehouse("fromWarehouse"),
    	atp("atp"),
    	qoh("qoh"),
    	toWarehouse("toWarehouse");

        private final String _fieldName;

        private In(String name) {
            this._fieldName = name;
        }

        public String getName() {
            return this._fieldName;
        }
    }

}
