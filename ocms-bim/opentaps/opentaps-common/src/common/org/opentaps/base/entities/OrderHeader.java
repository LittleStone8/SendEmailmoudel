//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.opentaps.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.opentaps.base.entities.AmazonOrderImport;
import org.opentaps.base.entities.BillingAccount;
import org.opentaps.base.entities.CommunicationEventOrder;
import org.opentaps.base.entities.Enumeration;
import org.opentaps.base.entities.Facility;
import org.opentaps.base.entities.FixedAsset;
import org.opentaps.base.entities.FixedAssetMaint;
import org.opentaps.base.entities.FixedAssetMaintOrder;
import org.opentaps.base.entities.GiftCardFulfillment;
import org.opentaps.base.entities.ItemIssuance;
import org.opentaps.base.entities.OldOrderItemAssociation;
import org.opentaps.base.entities.OldOrderItemInventoryRes;
import org.opentaps.base.entities.OldOrderShipmentPreference;
import org.opentaps.base.entities.OldValueLinkFulfillment;
import org.opentaps.base.entities.OrderAdjustment;
import org.opentaps.base.entities.OrderAttribute;
import org.opentaps.base.entities.OrderContactMech;
import org.opentaps.base.entities.OrderContent;
import org.opentaps.base.entities.OrderDeliverySchedule;
import org.opentaps.base.entities.OrderHeaderContent;
import org.opentaps.base.entities.OrderHeaderNote;
import org.opentaps.base.entities.OrderHeaderNoteView;
import org.opentaps.base.entities.OrderHeaderWorkEffort;
import org.opentaps.base.entities.OrderItem;
import org.opentaps.base.entities.OrderItemAndShipGroupAssoc;
import org.opentaps.base.entities.OrderItemAssoc;
import org.opentaps.base.entities.OrderItemBilling;
import org.opentaps.base.entities.OrderItemChange;
import org.opentaps.base.entities.OrderItemContactMech;
import org.opentaps.base.entities.OrderItemGroup;
import org.opentaps.base.entities.OrderItemPriceInfo;
import org.opentaps.base.entities.OrderItemRole;
import org.opentaps.base.entities.OrderItemShipGroup;
import org.opentaps.base.entities.OrderItemShipGroupAssoc;
import org.opentaps.base.entities.OrderItemShipGrpInvRes;
import org.opentaps.base.entities.OrderNotification;
import org.opentaps.base.entities.OrderPaymentPreference;
import org.opentaps.base.entities.OrderProductPromoCode;
import org.opentaps.base.entities.OrderRequirementCommitment;
import org.opentaps.base.entities.OrderRole;
import org.opentaps.base.entities.OrderShipGroupPriority;
import org.opentaps.base.entities.OrderShipment;
import org.opentaps.base.entities.OrderStatus;
import org.opentaps.base.entities.OrderTerm;
import org.opentaps.base.entities.OrderType;
import org.opentaps.base.entities.OrderTypeAttr;
import org.opentaps.base.entities.Party;
import org.opentaps.base.entities.PicklistBin;
import org.opentaps.base.entities.PicklistItem;
import org.opentaps.base.entities.PosTerminalLog;
import org.opentaps.base.entities.ProductOrderItem;
import org.opentaps.base.entities.ProductPromoUse;
import org.opentaps.base.entities.ProductStore;
import org.opentaps.base.entities.ReturnItem;
import org.opentaps.base.entities.ReturnItemResponse;
import org.opentaps.base.entities.Shipment;
import org.opentaps.base.entities.ShipmentReceipt;
import org.opentaps.base.entities.ShoppingList;
import org.opentaps.base.entities.StatusItem;
import org.opentaps.base.entities.Subscription;
import org.opentaps.base.entities.SurveyResponse;
import org.opentaps.base.entities.TrackingCodeOrder;
import org.opentaps.base.entities.TrackingCodeOrderReturn;
import org.opentaps.base.entities.Uom;
import org.opentaps.base.entities.UserLogin;
import org.opentaps.base.entities.WorkOrderItemFulfillment;
import org.opentaps.foundation.entity.EntityFieldInterface;
import org.opentaps.foundation.repository.RepositoryException;
import org.opentaps.foundation.repository.RepositoryInterface;

import javolution.util.FastMap;

@Entity
@Indexed
@Table(
    name = "ORDER_HEADER"
)
public class OrderHeader extends org.opentaps.foundation.entity.Entity implements Serializable {
   
	@GenericGenerator(
        name = "OrderHeader_GEN",
        strategy = "org.opentaps.foundation.entity.hibernate.OpentapsIdentifierGenerator"
    )
    @GeneratedValue(
        generator = "OrderHeader_GEN"
    )
    @Id
    @DocumentId
    @org.hibernate.search.annotations.Fields({@Field(
    index = Index.TOKENIZED,
    store = Store.NO
), @Field(
    index = Index.UN_TOKENIZED,
    store = Store.YES
)})
    @Boost(10.0F)
    @Column(
        name = "ORDER_ID"
    )
    private String orderId;
    @org.hibernate.search.annotations.Fields({@Field(
    index = Index.TOKENIZED,
    store = Store.NO
), @Field(
    index = Index.UN_TOKENIZED,
    store = Store.YES
)})
    @Boost(1.0F)
    @Column(
        name = "ORDER_TYPE_ID"
    )
    private String orderTypeId;
    @org.hibernate.search.annotations.Fields({@Field(
    index = Index.TOKENIZED,
    store = Store.YES
)})
    @Boost(5.0F)
    @Column(
        name = "ORDER_NAME"
    )
    private String orderName;
    @Column(
        name = "EXTERNAL_ID"
    )
    private String externalId;
    @org.hibernate.search.annotations.Fields({@Field(
    index = Index.TOKENIZED,
    store = Store.NO
), @Field(
    index = Index.UN_TOKENIZED,
    store = Store.YES
)})
    @Boost(1.0F)
    @Column(
        name = "SALES_CHANNEL_ENUM_ID"
    )
    private String salesChannelEnumId;
    @Column(
        name = "ORDER_DATE"
    )
    private Timestamp orderDate;
    @Column(
        name = "PRIORITY"
    )
    private String priority;
    @Column(
        name = "ENTRY_DATE"
    )
    private Timestamp entryDate;
    @Column(
        name = "PICK_SHEET_PRINTED_DATE"
    )
    private Timestamp pickSheetPrintedDate;
    @Column(
        name = "VISIT_ID"
    )
    private String visitId;
    @org.hibernate.search.annotations.Fields({@Field(
    index = Index.TOKENIZED,
    store = Store.NO
), @Field(
    index = Index.UN_TOKENIZED,
    store = Store.YES
)})
    @Boost(1.0F)
    @Column(
        name = "STATUS_ID"
    )
    private String statusId;
    @Column(
        name = "CREATED_BY"
    )
    private String createdBy;
    @Column(
        name = "FIRST_ATTEMPT_ORDER_ID"
    )
    private String firstAttemptOrderId;
    @Column(
        name = "CURRENCY_UOM"
    )
    private String currencyUom;
    @Column(
        name = "SYNC_STATUS_ID"
    )
    private String syncStatusId;
    @Column(
        name = "BILLING_ACCOUNT_ID"
    )
    private String billingAccountId;
    @Column(
        name = "ORIGIN_FACILITY_ID"
    )
    private String originFacilityId;
    @Column(
        name = "WEB_SITE_ID"
    )
    private String webSiteId;
    @Column(
        name = "PRODUCT_STORE_ID"
    )
    private String productStoreId;
    @Column(
        name = "TERMINAL_ID"
    )
    private String terminalId;
    @Column(
        name = "TRANSACTION_ID"
    )
    private String transactionId;
    @Column(
        name = "AUTO_ORDER_SHOPPING_LIST_ID"
    )
    private String autoOrderShoppingListId;
    @Column(
        name = "NEEDS_INVENTORY_ISSUANCE"
    )
    private String needsInventoryIssuance;
    @Column(
        name = "IS_RUSH_ORDER"
    )
    private String isRushOrder;
    @Column(
        name = "INTERNAL_CODE"
    )
    private String internalCode;
    @Column(
        name = "REMAINING_SUB_TOTAL"
    )
    private BigDecimal remainingSubTotal;
    @Column(
        name = "GRAND_TOTAL"
    )
    private BigDecimal grandTotal;
    @Column(
        name = "IS_VIEWED"
    )
    private String isViewed;
    @Column(
        name = "LAST_UPDATED_STAMP"
    )
    private Timestamp lastUpdatedStamp;
    @Column(
        name = "LAST_UPDATED_TX_STAMP"
    )
    private Timestamp lastUpdatedTxStamp;
    @Column(
        name = "CREATED_STAMP"
    )
    private Timestamp createdStamp;
    @Column(
        name = "CREATED_TX_STAMP"
    )
    private Timestamp createdTxStamp;
    @Column(
        name = "BILL_FROM_PARTY_ID"
    )
    private String billFromPartyId;
    @Column(
        name = "BILL_TO_PARTY_ID"
    )
    private String billToPartyId;
    @Column(
        name = "IS_FIX_ORDER"
    )
    private String isFixOrder;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "ORDER_TYPE_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private OrderType orderType;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "SALES_CHANNEL_ENUM_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private Enumeration salesChannelEnumeration;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "ORIGIN_FACILITY_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private Facility originFacility;
    private transient List<OrderTypeAttr> orderTypeAttrs;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "BILLING_ACCOUNT_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private BillingAccount billingAccount;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "PRODUCT_STORE_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private ProductStore productStore;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "AUTO_ORDER_SHOPPING_LIST_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private ShoppingList autoOrderShoppingList;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "CREATED_BY",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private UserLogin createdByUserLogin;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "STATUS_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private StatusItem statusItem;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "SYNC_STATUS_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private StatusItem syncStatusItem;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "CURRENCY_UOM",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    private Uom uom;
    private transient List<OrderHeaderNoteView> orderHeaderNoteViews;
    private transient List<OrderItemAndShipGroupAssoc> orderItemAndShipGroupAssocs;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "BILL_FROM_PARTY_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    @IndexedEmbedded(
        depth = 2
    )
    private Party billFromVendorParty;
    @ManyToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "BILL_TO_PARTY_ID",
        insertable = false,
        updatable = false
    )
    @Generated(GenerationTime.ALWAYS)
    @IndexedEmbedded(
        depth = 2
    )
    private Party billToCustomerParty;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<AmazonOrderImport> amazonOrderImports;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<CommunicationEventOrder> communicationEventOrders;
    private transient List<FixedAsset> acquireFixedAssets;
    private transient List<FixedAssetMaint> purchaseFixedAssetMaints;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<FixedAssetMaintOrder> fixedAssetMaintOrders;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<GiftCardFulfillment> giftCardFulfillments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<ItemIssuance> itemIssuances;
    private transient List<OldOrderItemAssociation> salesOldOrderItemAssociations;
    private transient List<OldOrderItemAssociation> purchaseOldOrderItemAssociations;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OldOrderItemInventoryRes> oldOrderItemInventoryReses;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OldOrderShipmentPreference> oldOrderShipmentPreferences;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OldValueLinkFulfillment> oldValueLinkFulfillments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderAdjustment> orderAdjustments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderAttribute> orderAttributes;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderContactMech> orderContactMeches;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderContent> orderContents;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderDeliverySchedule> orderDeliverySchedules;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderHeaderContent> orderHeaderContents;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderHeaderNote> orderHeaderNotes;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderHeaderWorkEffort> orderHeaderWorkEfforts;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItem> orderItems;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "fromOrderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemAssoc> fromOrderItemAssocs;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "toOrderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "TO_ORDER_ID"
    )
    private List<OrderItemAssoc> toOrderItemAssocs;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemBilling> orderItemBillings;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemChange> orderItemChanges;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemContactMech> orderItemContactMeches;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemGroup> orderItemGroups;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemPriceInfo> orderItemPriceInfoes;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemRole> orderItemRoles;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemShipGroup> orderItemShipGroups;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemShipGroupAssoc> orderItemShipGroupAssocs;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderItemShipGrpInvRes> orderItemShipGrpInvReses;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderNotification> orderNotifications;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderPaymentPreference> orderPaymentPreferences;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderProductPromoCode> orderProductPromoCodes;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderRequirementCommitment> orderRequirementCommitments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    @ContainedIn
    private List<OrderRole> orderRoles;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderShipGroupPriority> orderShipGroupPrioritys;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderShipment> orderShipments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderStatus> orderStatuses;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<OrderTerm> orderTerms;
    private transient List<PicklistBin> primaryPicklistBins;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<PicklistItem> picklistItems;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<PosTerminalLog> posTerminalLogs;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<ProductOrderItem> productOrderItems;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "engagementOrderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ENGAGEMENT_ID"
    )
    private List<ProductOrderItem> engagementProductOrderItems;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<ProductPromoUse> productPromoUses;
    private transient List<ReturnItem> returnItems;
    private transient List<ReturnItemResponse> replacementReturnItemResponses;
    private transient List<Shipment> primaryShipments;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<ShipmentReceipt> shipmentReceipts;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<Subscription> subscriptions;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<SurveyResponse> surveyResponses;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<TrackingCodeOrder> trackingCodeOrders;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<TrackingCodeOrderReturn> trackingCodeOrderReturns;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "orderHeader",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(
        name = "ORDER_ID"
    )
    private List<WorkOrderItemFulfillment> workOrderItemFulfillments;

    public OrderHeader() {
        this.orderType = null;
        this.salesChannelEnumeration = null;
        this.originFacility = null;
        this.orderTypeAttrs = null;
        this.billingAccount = null;
        this.productStore = null;
        this.autoOrderShoppingList = null;
        this.createdByUserLogin = null;
        this.statusItem = null;
        this.syncStatusItem = null;
        this.uom = null;
        this.orderHeaderNoteViews = null;
        this.orderItemAndShipGroupAssocs = null;
        this.billFromVendorParty = null;
        this.billToCustomerParty = null;
        this.amazonOrderImports = null;
        this.communicationEventOrders = null;
        this.acquireFixedAssets = null;
        this.purchaseFixedAssetMaints = null;
        this.fixedAssetMaintOrders = null;
        this.giftCardFulfillments = null;
        this.itemIssuances = null;
        this.salesOldOrderItemAssociations = null;
        this.purchaseOldOrderItemAssociations = null;
        this.oldOrderItemInventoryReses = null;
        this.oldOrderShipmentPreferences = null;
        this.oldValueLinkFulfillments = null;
        this.orderAdjustments = null;
        this.orderAttributes = null;
        this.orderContactMeches = null;
        this.orderContents = null;
        this.orderDeliverySchedules = null;
        this.orderHeaderContents = null;
        this.orderHeaderNotes = null;
        this.orderHeaderWorkEfforts = null;
        this.orderItems = null;
        this.fromOrderItemAssocs = null;
        this.toOrderItemAssocs = null;
        this.orderItemBillings = null;
        this.orderItemChanges = null;
        this.orderItemContactMeches = null;
        this.orderItemGroups = null;
        this.orderItemPriceInfoes = null;
        this.orderItemRoles = null;
        this.orderItemShipGroups = null;
        this.orderItemShipGroupAssocs = null;
        this.orderItemShipGrpInvReses = null;
        this.orderNotifications = null;
        this.orderPaymentPreferences = null;
        this.orderProductPromoCodes = null;
        this.orderRequirementCommitments = null;
        this.orderRoles = null;
        this.orderShipGroupPrioritys = null;
        this.orderShipments = null;
        this.orderStatuses = null;
        this.orderTerms = null;
        this.primaryPicklistBins = null;
        this.picklistItems = null;
        this.posTerminalLogs = null;
        this.productOrderItems = null;
        this.engagementProductOrderItems = null;
        this.productPromoUses = null;
        this.returnItems = null;
        this.replacementReturnItemResponses = null;
        this.primaryShipments = null;
        this.shipmentReceipts = null;
        this.subscriptions = null;
        this.surveyResponses = null;
        this.trackingCodeOrders = null;
        this.trackingCodeOrderReturns = null;
        this.workOrderItemFulfillments = null;
        this.baseEntityName = "OrderHeader";
        this.isView = false;
        this.primaryKeyNames = new ArrayList();
        this.primaryKeyNames.add("orderId");
        this.allFieldsNames = new ArrayList();
        this.allFieldsNames.add("orderId");
        this.allFieldsNames.add("orderTypeId");
        this.allFieldsNames.add("orderName");
        this.allFieldsNames.add("externalId");
        this.allFieldsNames.add("salesChannelEnumId");
        this.allFieldsNames.add("orderDate");
        this.allFieldsNames.add("priority");
        this.allFieldsNames.add("entryDate");
        this.allFieldsNames.add("pickSheetPrintedDate");
        this.allFieldsNames.add("visitId");
        this.allFieldsNames.add("statusId");
        this.allFieldsNames.add("createdBy");
        this.allFieldsNames.add("firstAttemptOrderId");
        this.allFieldsNames.add("currencyUom");
        this.allFieldsNames.add("syncStatusId");
        this.allFieldsNames.add("billingAccountId");
        this.allFieldsNames.add("originFacilityId");
        this.allFieldsNames.add("webSiteId");
        this.allFieldsNames.add("productStoreId");
        this.allFieldsNames.add("terminalId");
        this.allFieldsNames.add("transactionId");
        this.allFieldsNames.add("autoOrderShoppingListId");
        this.allFieldsNames.add("needsInventoryIssuance");
        this.allFieldsNames.add("isRushOrder");
        this.allFieldsNames.add("internalCode");
        this.allFieldsNames.add("remainingSubTotal");
        this.allFieldsNames.add("grandTotal");
        this.allFieldsNames.add("isViewed");
        this.allFieldsNames.add("lastUpdatedStamp");
        this.allFieldsNames.add("lastUpdatedTxStamp");
        this.allFieldsNames.add("createdStamp");
        this.allFieldsNames.add("createdTxStamp");
        this.allFieldsNames.add("billFromPartyId");
        this.allFieldsNames.add("billToPartyId");
        this.allFieldsNames.add("isFixOrder");
        this.nonPrimaryKeyNames = new ArrayList();
        this.nonPrimaryKeyNames.addAll(this.allFieldsNames);
        this.nonPrimaryKeyNames.removeAll(this.primaryKeyNames);
    }

    public OrderHeader(RepositoryInterface repository) {
        this();
        this.initRepository(repository);
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setSalesChannelEnumId(String salesChannelEnumId) {
        this.salesChannelEnumId = salesChannelEnumId;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }

    public void setPickSheetPrintedDate(Timestamp pickSheetPrintedDate) {
        this.pickSheetPrintedDate = pickSheetPrintedDate;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setFirstAttemptOrderId(String firstAttemptOrderId) {
        this.firstAttemptOrderId = firstAttemptOrderId;
    }

    public void setCurrencyUom(String currencyUom) {
        this.currencyUom = currencyUom;
    }

    public void setSyncStatusId(String syncStatusId) {
        this.syncStatusId = syncStatusId;
    }

    public void setBillingAccountId(String billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public void setOriginFacilityId(String originFacilityId) {
        this.originFacilityId = originFacilityId;
    }

    public void setWebSiteId(String webSiteId) {
        this.webSiteId = webSiteId;
    }

    public void setProductStoreId(String productStoreId) {
        this.productStoreId = productStoreId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAutoOrderShoppingListId(String autoOrderShoppingListId) {
        this.autoOrderShoppingListId = autoOrderShoppingListId;
    }

    public void setNeedsInventoryIssuance(String needsInventoryIssuance) {
        this.needsInventoryIssuance = needsInventoryIssuance;
    }

    public void setIsRushOrder(String isRushOrder) {
        this.isRushOrder = isRushOrder;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public void setRemainingSubTotal(BigDecimal remainingSubTotal) {
        this.remainingSubTotal = remainingSubTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setIsViewed(String isViewed) {
        this.isViewed = isViewed;
    }

    public void setLastUpdatedStamp(Timestamp lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }

    public void setLastUpdatedTxStamp(Timestamp lastUpdatedTxStamp) {
        this.lastUpdatedTxStamp = lastUpdatedTxStamp;
    }

    public void setCreatedStamp(Timestamp createdStamp) {
        this.createdStamp = createdStamp;
    }

    public void setCreatedTxStamp(Timestamp createdTxStamp) {
        this.createdTxStamp = createdTxStamp;
    }

    public void setBillFromPartyId(String billFromPartyId) {
        this.billFromPartyId = billFromPartyId;
    }

    public void setBillToPartyId(String billToPartyId) {
        this.billToPartyId = billToPartyId;
    }
    public void setIsFixOrder(String isFixOrder) {
        this.isFixOrder = isFixOrder;
    }
    public String getOrderId() {
        return this.orderId;
    }

    public String getOrderTypeId() {
        return this.orderTypeId;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public String getSalesChannelEnumId() {
        return this.salesChannelEnumId;
    }

    public Timestamp getOrderDate() {
        return this.orderDate;
    }

    public String getPriority() {
        return this.priority;
    }

    public Timestamp getEntryDate() {
        return this.entryDate;
    }

    public Timestamp getPickSheetPrintedDate() {
        return this.pickSheetPrintedDate;
    }

    public String getVisitId() {
        return this.visitId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getFirstAttemptOrderId() {
        return this.firstAttemptOrderId;
    }

    public String getCurrencyUom() {
        return this.currencyUom;
    }

    public String getSyncStatusId() {
        return this.syncStatusId;
    }

    public String getBillingAccountId() {
        return this.billingAccountId;
    }

    public String getOriginFacilityId() {
        return this.originFacilityId;
    }

    public String getWebSiteId() {
        return this.webSiteId;
    }

    public String getProductStoreId() {
        return this.productStoreId;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getAutoOrderShoppingListId() {
        return this.autoOrderShoppingListId;
    }

    public String getNeedsInventoryIssuance() {
        return this.needsInventoryIssuance;
    }

    public String getIsRushOrder() {
        return this.isRushOrder;
    }

    public String getInternalCode() {
        return this.internalCode;
    }

    public BigDecimal getRemainingSubTotal() {
        return this.remainingSubTotal;
    }

    public BigDecimal getGrandTotal() {
        return this.grandTotal;
    }

    public String getIsViewed() {
        return this.isViewed;
    }

    public Timestamp getLastUpdatedStamp() {
        return this.lastUpdatedStamp;
    }

    public Timestamp getLastUpdatedTxStamp() {
        return this.lastUpdatedTxStamp;
    }

    public Timestamp getCreatedStamp() {
        return this.createdStamp;
    }

    public Timestamp getCreatedTxStamp() {
        return this.createdTxStamp;
    }

    public String getBillFromPartyId() {
        return this.billFromPartyId;
    }

    public String getBillToPartyId() {
        return this.billToPartyId;
    }
    public String getIsFixOrder() {
        return this.isFixOrder;
    }
    
    public OrderType getOrderType() throws RepositoryException {
        if(this.orderType == null) {
            this.orderType = (OrderType)this.getRelatedOne(OrderType.class, "OrderType");
        }

        return this.orderType;
    }

    public Enumeration getSalesChannelEnumeration() throws RepositoryException {
        if(this.salesChannelEnumeration == null) {
            this.salesChannelEnumeration = (Enumeration)this.getRelatedOne(Enumeration.class, "SalesChannelEnumeration");
        }

        return this.salesChannelEnumeration;
    }

    public Facility getOriginFacility() throws RepositoryException {
        if(this.originFacility == null) {
            this.originFacility = (Facility)this.getRelatedOne(Facility.class, "OriginFacility");
        }

        return this.originFacility;
    }

    public List<? extends OrderTypeAttr> getOrderTypeAttrs() throws RepositoryException {
        if(this.orderTypeAttrs == null) {
            this.orderTypeAttrs = this.getRelated(OrderTypeAttr.class, "OrderTypeAttr");
        }

        return this.orderTypeAttrs;
    }

    public BillingAccount getBillingAccount() throws RepositoryException {
        if(this.billingAccount == null) {
            this.billingAccount = (BillingAccount)this.getRelatedOne(BillingAccount.class, "BillingAccount");
        }

        return this.billingAccount;
    }

    public ProductStore getProductStore() throws RepositoryException {
        if(this.productStore == null) {
            this.productStore = (ProductStore)this.getRelatedOne(ProductStore.class, "ProductStore");
        }

        return this.productStore;
    }

    public ShoppingList getAutoOrderShoppingList() throws RepositoryException {
        if(this.autoOrderShoppingList == null) {
            this.autoOrderShoppingList = (ShoppingList)this.getRelatedOne(ShoppingList.class, "AutoOrderShoppingList");
        }

        return this.autoOrderShoppingList;
    }

    public UserLogin getCreatedByUserLogin() throws RepositoryException {
        if(this.createdByUserLogin == null) {
            this.createdByUserLogin = (UserLogin)this.getRelatedOne(UserLogin.class, "CreatedByUserLogin");
        }

        return this.createdByUserLogin;
    }

    public StatusItem getStatusItem() throws RepositoryException {
        if(this.statusItem == null) {
            this.statusItem = (StatusItem)this.getRelatedOne(StatusItem.class, "StatusItem");
        }

        return this.statusItem;
    }

    public StatusItem getSyncStatusItem() throws RepositoryException {
        if(this.syncStatusItem == null) {
            this.syncStatusItem = (StatusItem)this.getRelatedOne(StatusItem.class, "SyncStatusItem");
        }

        return this.syncStatusItem;
    }

    public Uom getUom() throws RepositoryException {
        if(this.uom == null) {
            this.uom = (Uom)this.getRelatedOne(Uom.class, "Uom");
        }

        return this.uom;
    }

    public List<? extends OrderHeaderNoteView> getOrderHeaderNoteViews() throws RepositoryException {
        if(this.orderHeaderNoteViews == null) {
            this.orderHeaderNoteViews = this.getRelated(OrderHeaderNoteView.class, "OrderHeaderNoteView");
        }

        return this.orderHeaderNoteViews;
    }

    public List<? extends OrderItemAndShipGroupAssoc> getOrderItemAndShipGroupAssocs() throws RepositoryException {
        if(this.orderItemAndShipGroupAssocs == null) {
            this.orderItemAndShipGroupAssocs = this.getRelated(OrderItemAndShipGroupAssoc.class, "OrderItemAndShipGroupAssoc");
        }

        return this.orderItemAndShipGroupAssocs;
    }

    public Party getBillFromVendorParty() throws RepositoryException {
        if(this.billFromVendorParty == null) {
            this.billFromVendorParty = (Party)this.getRelatedOne(Party.class, "BillFromVendorParty");
        }

        return this.billFromVendorParty;
    }

    public Party getBillToCustomerParty() throws RepositoryException {
        if(this.billToCustomerParty == null) {
            this.billToCustomerParty = (Party)this.getRelatedOne(Party.class, "BillToCustomerParty");
        }

        return this.billToCustomerParty;
    }

    public List<? extends AmazonOrderImport> getAmazonOrderImports() throws RepositoryException {
        if(this.amazonOrderImports == null) {
            this.amazonOrderImports = this.getRelated(AmazonOrderImport.class, "AmazonOrderImport");
        }

        return this.amazonOrderImports;
    }

    public List<? extends CommunicationEventOrder> getCommunicationEventOrders() throws RepositoryException {
        if(this.communicationEventOrders == null) {
            this.communicationEventOrders = this.getRelated(CommunicationEventOrder.class, "CommunicationEventOrder");
        }

        return this.communicationEventOrders;
    }

    public List<? extends FixedAsset> getAcquireFixedAssets() throws RepositoryException {
        if(this.acquireFixedAssets == null) {
            this.acquireFixedAssets = this.getRelated(FixedAsset.class, "AcquireFixedAsset");
        }

        return this.acquireFixedAssets;
    }

    public List<? extends FixedAssetMaint> getPurchaseFixedAssetMaints() throws RepositoryException {
        if(this.purchaseFixedAssetMaints == null) {
            this.purchaseFixedAssetMaints = this.getRelated(FixedAssetMaint.class, "PurchaseFixedAssetMaint");
        }

        return this.purchaseFixedAssetMaints;
    }

    public List<? extends FixedAssetMaintOrder> getFixedAssetMaintOrders() throws RepositoryException {
        if(this.fixedAssetMaintOrders == null) {
            this.fixedAssetMaintOrders = this.getRelated(FixedAssetMaintOrder.class, "FixedAssetMaintOrder");
        }

        return this.fixedAssetMaintOrders;
    }

    public List<? extends GiftCardFulfillment> getGiftCardFulfillments() throws RepositoryException {
        if(this.giftCardFulfillments == null) {
            this.giftCardFulfillments = this.getRelated(GiftCardFulfillment.class, "GiftCardFulfillment");
        }

        return this.giftCardFulfillments;
    }

    public List<? extends ItemIssuance> getItemIssuances() throws RepositoryException {
        if(this.itemIssuances == null) {
            this.itemIssuances = this.getRelated(ItemIssuance.class, "ItemIssuance");
        }

        return this.itemIssuances;
    }

    public List<? extends OldOrderItemAssociation> getSalesOldOrderItemAssociations() throws RepositoryException {
        if(this.salesOldOrderItemAssociations == null) {
            this.salesOldOrderItemAssociations = this.getRelated(OldOrderItemAssociation.class, "SalesOldOrderItemAssociation");
        }

        return this.salesOldOrderItemAssociations;
    }

    public List<? extends OldOrderItemAssociation> getPurchaseOldOrderItemAssociations() throws RepositoryException {
        if(this.purchaseOldOrderItemAssociations == null) {
            this.purchaseOldOrderItemAssociations = this.getRelated(OldOrderItemAssociation.class, "PurchaseOldOrderItemAssociation");
        }

        return this.purchaseOldOrderItemAssociations;
    }

    public List<? extends OldOrderItemInventoryRes> getOldOrderItemInventoryReses() throws RepositoryException {
        if(this.oldOrderItemInventoryReses == null) {
            this.oldOrderItemInventoryReses = this.getRelated(OldOrderItemInventoryRes.class, "OldOrderItemInventoryRes");
        }

        return this.oldOrderItemInventoryReses;
    }

    public List<? extends OldOrderShipmentPreference> getOldOrderShipmentPreferences() throws RepositoryException {
        if(this.oldOrderShipmentPreferences == null) {
            this.oldOrderShipmentPreferences = this.getRelated(OldOrderShipmentPreference.class, "OldOrderShipmentPreference");
        }

        return this.oldOrderShipmentPreferences;
    }

    public List<? extends OldValueLinkFulfillment> getOldValueLinkFulfillments() throws RepositoryException {
        if(this.oldValueLinkFulfillments == null) {
            this.oldValueLinkFulfillments = this.getRelated(OldValueLinkFulfillment.class, "OldValueLinkFulfillment");
        }

        return this.oldValueLinkFulfillments;
    }

    public List<? extends OrderAdjustment> getOrderAdjustments() throws RepositoryException {
        if(this.orderAdjustments == null) {
            this.orderAdjustments = this.getRelated(OrderAdjustment.class, "OrderAdjustment");
        }

        return this.orderAdjustments;
    }

    public List<? extends OrderAttribute> getOrderAttributes() throws RepositoryException {
        if(this.orderAttributes == null) {
            this.orderAttributes = this.getRelated(OrderAttribute.class, "OrderAttribute");
        }

        return this.orderAttributes;
    }

    public List<? extends OrderContactMech> getOrderContactMeches() throws RepositoryException {
        if(this.orderContactMeches == null) {
            this.orderContactMeches = this.getRelated(OrderContactMech.class, "OrderContactMech");
        }

        return this.orderContactMeches;
    }

    public List<? extends OrderContent> getOrderContents() throws RepositoryException {
        if(this.orderContents == null) {
            this.orderContents = this.getRelated(OrderContent.class, "OrderContent");
        }

        return this.orderContents;
    }

    public List<? extends OrderDeliverySchedule> getOrderDeliverySchedules() throws RepositoryException {
        if(this.orderDeliverySchedules == null) {
            this.orderDeliverySchedules = this.getRelated(OrderDeliverySchedule.class, "OrderDeliverySchedule");
        }

        return this.orderDeliverySchedules;
    }

    public List<? extends OrderHeaderContent> getOrderHeaderContents() throws RepositoryException {
        if(this.orderHeaderContents == null) {
            this.orderHeaderContents = this.getRelated(OrderHeaderContent.class, "OrderHeaderContent");
        }

        return this.orderHeaderContents;
    }

    public List<? extends OrderHeaderNote> getOrderHeaderNotes() throws RepositoryException {
        if(this.orderHeaderNotes == null) {
            this.orderHeaderNotes = this.getRelated(OrderHeaderNote.class, "OrderHeaderNote");
        }

        return this.orderHeaderNotes;
    }

    public List<? extends OrderHeaderWorkEffort> getOrderHeaderWorkEfforts() throws RepositoryException {
        if(this.orderHeaderWorkEfforts == null) {
            this.orderHeaderWorkEfforts = this.getRelated(OrderHeaderWorkEffort.class, "OrderHeaderWorkEffort");
        }

        return this.orderHeaderWorkEfforts;
    }

    public List<? extends OrderItem> getOrderItems() throws RepositoryException {
        if(this.orderItems == null) {
            this.orderItems = this.getRelated(OrderItem.class, "OrderItem");
        }

        return this.orderItems;
    }

    public List<? extends OrderItemAssoc> getFromOrderItemAssocs() throws RepositoryException {
        if(this.fromOrderItemAssocs == null) {
            this.fromOrderItemAssocs = this.getRelated(OrderItemAssoc.class, "FromOrderItemAssoc");
        }

        return this.fromOrderItemAssocs;
    }

    public List<? extends OrderItemAssoc> getToOrderItemAssocs() throws RepositoryException {
        if(this.toOrderItemAssocs == null) {
            this.toOrderItemAssocs = this.getRelated(OrderItemAssoc.class, "ToOrderItemAssoc");
        }

        return this.toOrderItemAssocs;
    }

    public List<? extends OrderItemBilling> getOrderItemBillings() throws RepositoryException {
        if(this.orderItemBillings == null) {
            this.orderItemBillings = this.getRelated(OrderItemBilling.class, "OrderItemBilling");
        }

        return this.orderItemBillings;
    }

    public List<? extends OrderItemChange> getOrderItemChanges() throws RepositoryException {
        if(this.orderItemChanges == null) {
            this.orderItemChanges = this.getRelated(OrderItemChange.class, "OrderItemChange");
        }

        return this.orderItemChanges;
    }

    public List<? extends OrderItemContactMech> getOrderItemContactMeches() throws RepositoryException {
        if(this.orderItemContactMeches == null) {
            this.orderItemContactMeches = this.getRelated(OrderItemContactMech.class, "OrderItemContactMech");
        }

        return this.orderItemContactMeches;
    }

    public List<? extends OrderItemGroup> getOrderItemGroups() throws RepositoryException {
        if(this.orderItemGroups == null) {
            this.orderItemGroups = this.getRelated(OrderItemGroup.class, "OrderItemGroup");
        }

        return this.orderItemGroups;
    }

    public List<? extends OrderItemPriceInfo> getOrderItemPriceInfoes() throws RepositoryException {
        if(this.orderItemPriceInfoes == null) {
            this.orderItemPriceInfoes = this.getRelated(OrderItemPriceInfo.class, "OrderItemPriceInfo");
        }

        return this.orderItemPriceInfoes;
    }

    public List<? extends OrderItemRole> getOrderItemRoles() throws RepositoryException {
        if(this.orderItemRoles == null) {
            this.orderItemRoles = this.getRelated(OrderItemRole.class, "OrderItemRole");
        }

        return this.orderItemRoles;
    }

    public List<? extends OrderItemShipGroup> getOrderItemShipGroups() throws RepositoryException {
        if(this.orderItemShipGroups == null) {
            this.orderItemShipGroups = this.getRelated(OrderItemShipGroup.class, "OrderItemShipGroup");
        }

        return this.orderItemShipGroups;
    }

    public List<? extends OrderItemShipGroupAssoc> getOrderItemShipGroupAssocs() throws RepositoryException {
        if(this.orderItemShipGroupAssocs == null) {
            this.orderItemShipGroupAssocs = this.getRelated(OrderItemShipGroupAssoc.class, "OrderItemShipGroupAssoc");
        }

        return this.orderItemShipGroupAssocs;
    }

    public List<? extends OrderItemShipGrpInvRes> getOrderItemShipGrpInvReses() throws RepositoryException {
        if(this.orderItemShipGrpInvReses == null) {
            this.orderItemShipGrpInvReses = this.getRelated(OrderItemShipGrpInvRes.class, "OrderItemShipGrpInvRes");
        }

        return this.orderItemShipGrpInvReses;
    }

    public List<? extends OrderNotification> getOrderNotifications() throws RepositoryException {
        if(this.orderNotifications == null) {
            this.orderNotifications = this.getRelated(OrderNotification.class, "OrderNotification");
        }

        return this.orderNotifications;
    }

    public List<? extends OrderPaymentPreference> getOrderPaymentPreferences() throws RepositoryException {
        if(this.orderPaymentPreferences == null) {
            this.orderPaymentPreferences = this.getRelated(OrderPaymentPreference.class, "OrderPaymentPreference");
        }

        return this.orderPaymentPreferences;
    }

    public List<? extends OrderProductPromoCode> getOrderProductPromoCodes() throws RepositoryException {
        if(this.orderProductPromoCodes == null) {
            this.orderProductPromoCodes = this.getRelated(OrderProductPromoCode.class, "OrderProductPromoCode");
        }

        return this.orderProductPromoCodes;
    }

    public List<? extends OrderRequirementCommitment> getOrderRequirementCommitments() throws RepositoryException {
        if(this.orderRequirementCommitments == null) {
            this.orderRequirementCommitments = this.getRelated(OrderRequirementCommitment.class, "OrderRequirementCommitment");
        }

        return this.orderRequirementCommitments;
    }

    public List<? extends OrderRole> getOrderRoles() throws RepositoryException {
        if(this.orderRoles == null) {
            this.orderRoles = this.getRelated(OrderRole.class, "OrderRole");
        }

        return this.orderRoles;
    }

    public List<? extends OrderShipGroupPriority> getOrderShipGroupPrioritys() throws RepositoryException {
        if(this.orderShipGroupPrioritys == null) {
            this.orderShipGroupPrioritys = this.getRelated(OrderShipGroupPriority.class, "OrderShipGroupPriority");
        }

        return this.orderShipGroupPrioritys;
    }

    public List<? extends OrderShipment> getOrderShipments() throws RepositoryException {
        if(this.orderShipments == null) {
            this.orderShipments = this.getRelated(OrderShipment.class, "OrderShipment");
        }

        return this.orderShipments;
    }

    public List<? extends OrderStatus> getOrderStatuses() throws RepositoryException {
        if(this.orderStatuses == null) {
            this.orderStatuses = this.getRelated(OrderStatus.class, "OrderStatus");
        }

        return this.orderStatuses;
    }

    public List<? extends OrderTerm> getOrderTerms() throws RepositoryException {
        if(this.orderTerms == null) {
            this.orderTerms = this.getRelated(OrderTerm.class, "OrderTerm");
        }

        return this.orderTerms;
    }

    public List<? extends PicklistBin> getPrimaryPicklistBins() throws RepositoryException {
        if(this.primaryPicklistBins == null) {
            this.primaryPicklistBins = this.getRelated(PicklistBin.class, "PrimaryPicklistBin");
        }

        return this.primaryPicklistBins;
    }

    public List<? extends PicklistItem> getPicklistItems() throws RepositoryException {
        if(this.picklistItems == null) {
            this.picklistItems = this.getRelated(PicklistItem.class, "PicklistItem");
        }

        return this.picklistItems;
    }

    public List<? extends PosTerminalLog> getPosTerminalLogs() throws RepositoryException {
        if(this.posTerminalLogs == null) {
            this.posTerminalLogs = this.getRelated(PosTerminalLog.class, "PosTerminalLog");
        }

        return this.posTerminalLogs;
    }

    public List<? extends ProductOrderItem> getProductOrderItems() throws RepositoryException {
        if(this.productOrderItems == null) {
            this.productOrderItems = this.getRelated(ProductOrderItem.class, "ProductOrderItem");
        }

        return this.productOrderItems;
    }

    public List<? extends ProductOrderItem> getEngagementProductOrderItems() throws RepositoryException {
        if(this.engagementProductOrderItems == null) {
            this.engagementProductOrderItems = this.getRelated(ProductOrderItem.class, "EngagementProductOrderItem");
        }

        return this.engagementProductOrderItems;
    }

    public List<? extends ProductPromoUse> getProductPromoUses() throws RepositoryException {
        if(this.productPromoUses == null) {
            this.productPromoUses = this.getRelated(ProductPromoUse.class, "ProductPromoUse");
        }

        return this.productPromoUses;
    }

    public List<? extends ReturnItem> getReturnItems() throws RepositoryException {
        if(this.returnItems == null) {
            this.returnItems = this.getRelated(ReturnItem.class, "ReturnItem");
        }

        return this.returnItems;
    }

    public List<? extends ReturnItemResponse> getReplacementReturnItemResponses() throws RepositoryException {
        if(this.replacementReturnItemResponses == null) {
            this.replacementReturnItemResponses = this.getRelated(ReturnItemResponse.class, "ReplacementReturnItemResponse");
        }

        return this.replacementReturnItemResponses;
    }

    public List<? extends Shipment> getPrimaryShipments() throws RepositoryException {
        if(this.primaryShipments == null) {
            this.primaryShipments = this.getRelated(Shipment.class, "PrimaryShipment");
        }

        return this.primaryShipments;
    }

    public List<? extends ShipmentReceipt> getShipmentReceipts() throws RepositoryException {
        if(this.shipmentReceipts == null) {
            this.shipmentReceipts = this.getRelated(ShipmentReceipt.class, "ShipmentReceipt");
        }

        return this.shipmentReceipts;
    }

    public List<? extends Subscription> getSubscriptions() throws RepositoryException {
        if(this.subscriptions == null) {
            this.subscriptions = this.getRelated(Subscription.class, "Subscription");
        }

        return this.subscriptions;
    }

    public List<? extends SurveyResponse> getSurveyResponses() throws RepositoryException {
        if(this.surveyResponses == null) {
            this.surveyResponses = this.getRelated(SurveyResponse.class, "SurveyResponse");
        }

        return this.surveyResponses;
    }

    public List<? extends TrackingCodeOrder> getTrackingCodeOrders() throws RepositoryException {
        if(this.trackingCodeOrders == null) {
            this.trackingCodeOrders = this.getRelated(TrackingCodeOrder.class, "TrackingCodeOrder");
        }

        return this.trackingCodeOrders;
    }

    public List<? extends TrackingCodeOrderReturn> getTrackingCodeOrderReturns() throws RepositoryException {
        if(this.trackingCodeOrderReturns == null) {
            this.trackingCodeOrderReturns = this.getRelated(TrackingCodeOrderReturn.class, "TrackingCodeOrderReturn");
        }

        return this.trackingCodeOrderReturns;
    }

    public List<? extends WorkOrderItemFulfillment> getWorkOrderItemFulfillments() throws RepositoryException {
        if(this.workOrderItemFulfillments == null) {
            this.workOrderItemFulfillments = this.getRelated(WorkOrderItemFulfillment.class, "WorkOrderItemFulfillment");
        }

        return this.workOrderItemFulfillments;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setSalesChannelEnumeration(Enumeration salesChannelEnumeration) {
        this.salesChannelEnumeration = salesChannelEnumeration;
    }

    public void setOriginFacility(Facility originFacility) {
        this.originFacility = originFacility;
    }

    public void setOrderTypeAttrs(List<OrderTypeAttr> orderTypeAttrs) {
        this.orderTypeAttrs = orderTypeAttrs;
    }

    public void setBillingAccount(BillingAccount billingAccount) {
        this.billingAccount = billingAccount;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public void setAutoOrderShoppingList(ShoppingList autoOrderShoppingList) {
        this.autoOrderShoppingList = autoOrderShoppingList;
    }

    public void setCreatedByUserLogin(UserLogin createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    public void setSyncStatusItem(StatusItem syncStatusItem) {
        this.syncStatusItem = syncStatusItem;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public void setOrderHeaderNoteViews(List<OrderHeaderNoteView> orderHeaderNoteViews) {
        this.orderHeaderNoteViews = orderHeaderNoteViews;
    }

    public void setOrderItemAndShipGroupAssocs(List<OrderItemAndShipGroupAssoc> orderItemAndShipGroupAssocs) {
        this.orderItemAndShipGroupAssocs = orderItemAndShipGroupAssocs;
    }

    public void setBillFromVendorParty(Party billFromVendorParty) {
        this.billFromVendorParty = billFromVendorParty;
    }

    public void setBillToCustomerParty(Party billToCustomerParty) {
        this.billToCustomerParty = billToCustomerParty;
    }

    public void setAmazonOrderImports(List<AmazonOrderImport> amazonOrderImports) {
        this.amazonOrderImports = amazonOrderImports;
    }

    public void setCommunicationEventOrders(List<CommunicationEventOrder> communicationEventOrders) {
        this.communicationEventOrders = communicationEventOrders;
    }

    public void setAcquireFixedAssets(List<FixedAsset> acquireFixedAssets) {
        this.acquireFixedAssets = acquireFixedAssets;
    }

    public void setPurchaseFixedAssetMaints(List<FixedAssetMaint> purchaseFixedAssetMaints) {
        this.purchaseFixedAssetMaints = purchaseFixedAssetMaints;
    }

    public void setFixedAssetMaintOrders(List<FixedAssetMaintOrder> fixedAssetMaintOrders) {
        this.fixedAssetMaintOrders = fixedAssetMaintOrders;
    }

    public void setGiftCardFulfillments(List<GiftCardFulfillment> giftCardFulfillments) {
        this.giftCardFulfillments = giftCardFulfillments;
    }

    public void setItemIssuances(List<ItemIssuance> itemIssuances) {
        this.itemIssuances = itemIssuances;
    }

    public void setSalesOldOrderItemAssociations(List<OldOrderItemAssociation> salesOldOrderItemAssociations) {
        this.salesOldOrderItemAssociations = salesOldOrderItemAssociations;
    }

    public void setPurchaseOldOrderItemAssociations(List<OldOrderItemAssociation> purchaseOldOrderItemAssociations) {
        this.purchaseOldOrderItemAssociations = purchaseOldOrderItemAssociations;
    }

    public void setOldOrderItemInventoryReses(List<OldOrderItemInventoryRes> oldOrderItemInventoryReses) {
        this.oldOrderItemInventoryReses = oldOrderItemInventoryReses;
    }

    public void setOldOrderShipmentPreferences(List<OldOrderShipmentPreference> oldOrderShipmentPreferences) {
        this.oldOrderShipmentPreferences = oldOrderShipmentPreferences;
    }

    public void setOldValueLinkFulfillments(List<OldValueLinkFulfillment> oldValueLinkFulfillments) {
        this.oldValueLinkFulfillments = oldValueLinkFulfillments;
    }

    public void setOrderAdjustments(List<OrderAdjustment> orderAdjustments) {
        this.orderAdjustments = orderAdjustments;
    }

    public void setOrderAttributes(List<OrderAttribute> orderAttributes) {
        this.orderAttributes = orderAttributes;
    }

    public void setOrderContactMeches(List<OrderContactMech> orderContactMeches) {
        this.orderContactMeches = orderContactMeches;
    }

    public void setOrderContents(List<OrderContent> orderContents) {
        this.orderContents = orderContents;
    }

    public void setOrderDeliverySchedules(List<OrderDeliverySchedule> orderDeliverySchedules) {
        this.orderDeliverySchedules = orderDeliverySchedules;
    }

    public void setOrderHeaderContents(List<OrderHeaderContent> orderHeaderContents) {
        this.orderHeaderContents = orderHeaderContents;
    }

    public void setOrderHeaderNotes(List<OrderHeaderNote> orderHeaderNotes) {
        this.orderHeaderNotes = orderHeaderNotes;
    }

    public void setOrderHeaderWorkEfforts(List<OrderHeaderWorkEffort> orderHeaderWorkEfforts) {
        this.orderHeaderWorkEfforts = orderHeaderWorkEfforts;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setFromOrderItemAssocs(List<OrderItemAssoc> fromOrderItemAssocs) {
        this.fromOrderItemAssocs = fromOrderItemAssocs;
    }

    public void setToOrderItemAssocs(List<OrderItemAssoc> toOrderItemAssocs) {
        this.toOrderItemAssocs = toOrderItemAssocs;
    }

    public void setOrderItemBillings(List<OrderItemBilling> orderItemBillings) {
        this.orderItemBillings = orderItemBillings;
    }

    public void setOrderItemChanges(List<OrderItemChange> orderItemChanges) {
        this.orderItemChanges = orderItemChanges;
    }

    public void setOrderItemContactMeches(List<OrderItemContactMech> orderItemContactMeches) {
        this.orderItemContactMeches = orderItemContactMeches;
    }

    public void setOrderItemGroups(List<OrderItemGroup> orderItemGroups) {
        this.orderItemGroups = orderItemGroups;
    }

    public void setOrderItemPriceInfoes(List<OrderItemPriceInfo> orderItemPriceInfoes) {
        this.orderItemPriceInfoes = orderItemPriceInfoes;
    }

    public void setOrderItemRoles(List<OrderItemRole> orderItemRoles) {
        this.orderItemRoles = orderItemRoles;
    }

    public void setOrderItemShipGroups(List<OrderItemShipGroup> orderItemShipGroups) {
        this.orderItemShipGroups = orderItemShipGroups;
    }

    public void setOrderItemShipGroupAssocs(List<OrderItemShipGroupAssoc> orderItemShipGroupAssocs) {
        this.orderItemShipGroupAssocs = orderItemShipGroupAssocs;
    }

    public void setOrderItemShipGrpInvReses(List<OrderItemShipGrpInvRes> orderItemShipGrpInvReses) {
        this.orderItemShipGrpInvReses = orderItemShipGrpInvReses;
    }

    public void setOrderNotifications(List<OrderNotification> orderNotifications) {
        this.orderNotifications = orderNotifications;
    }

    public void setOrderPaymentPreferences(List<OrderPaymentPreference> orderPaymentPreferences) {
        this.orderPaymentPreferences = orderPaymentPreferences;
    }

    public void setOrderProductPromoCodes(List<OrderProductPromoCode> orderProductPromoCodes) {
        this.orderProductPromoCodes = orderProductPromoCodes;
    }

    public void setOrderRequirementCommitments(List<OrderRequirementCommitment> orderRequirementCommitments) {
        this.orderRequirementCommitments = orderRequirementCommitments;
    }

    public void setOrderRoles(List<OrderRole> orderRoles) {
        this.orderRoles = orderRoles;
    }

    public void setOrderShipGroupPrioritys(List<OrderShipGroupPriority> orderShipGroupPrioritys) {
        this.orderShipGroupPrioritys = orderShipGroupPrioritys;
    }

    public void setOrderShipments(List<OrderShipment> orderShipments) {
        this.orderShipments = orderShipments;
    }

    public void setOrderStatuses(List<OrderStatus> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }

    public void setOrderTerms(List<OrderTerm> orderTerms) {
        this.orderTerms = orderTerms;
    }

    public void setPrimaryPicklistBins(List<PicklistBin> primaryPicklistBins) {
        this.primaryPicklistBins = primaryPicklistBins;
    }

    public void setPicklistItems(List<PicklistItem> picklistItems) {
        this.picklistItems = picklistItems;
    }

    public void setPosTerminalLogs(List<PosTerminalLog> posTerminalLogs) {
        this.posTerminalLogs = posTerminalLogs;
    }

    public void setProductOrderItems(List<ProductOrderItem> productOrderItems) {
        this.productOrderItems = productOrderItems;
    }

    public void setEngagementProductOrderItems(List<ProductOrderItem> engagementProductOrderItems) {
        this.engagementProductOrderItems = engagementProductOrderItems;
    }

    public void setProductPromoUses(List<ProductPromoUse> productPromoUses) {
        this.productPromoUses = productPromoUses;
    }

    public void setReturnItems(List<ReturnItem> returnItems) {
        this.returnItems = returnItems;
    }

    public void setReplacementReturnItemResponses(List<ReturnItemResponse> replacementReturnItemResponses) {
        this.replacementReturnItemResponses = replacementReturnItemResponses;
    }

    public void setPrimaryShipments(List<Shipment> primaryShipments) {
        this.primaryShipments = primaryShipments;
    }

    public void setShipmentReceipts(List<ShipmentReceipt> shipmentReceipts) {
        this.shipmentReceipts = shipmentReceipts;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setSurveyResponses(List<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }

    public void setTrackingCodeOrders(List<TrackingCodeOrder> trackingCodeOrders) {
        this.trackingCodeOrders = trackingCodeOrders;
    }

    public void setTrackingCodeOrderReturns(List<TrackingCodeOrderReturn> trackingCodeOrderReturns) {
        this.trackingCodeOrderReturns = trackingCodeOrderReturns;
    }

    public void setWorkOrderItemFulfillments(List<WorkOrderItemFulfillment> workOrderItemFulfillments) {
        this.workOrderItemFulfillments = workOrderItemFulfillments;
    }

    public void addCommunicationEventOrder(CommunicationEventOrder communicationEventOrder) {
        if(this.communicationEventOrders == null) {
            this.communicationEventOrders = new ArrayList();
        }

        this.communicationEventOrders.add(communicationEventOrder);
    }

    public void removeCommunicationEventOrder(CommunicationEventOrder communicationEventOrder) {
        if(this.communicationEventOrders != null) {
            this.communicationEventOrders.remove(communicationEventOrder);
        }
    }

    public void clearCommunicationEventOrder() {
        if(this.communicationEventOrders != null) {
            this.communicationEventOrders.clear();
        }
    }

    public void addFixedAssetMaintOrder(FixedAssetMaintOrder fixedAssetMaintOrder) {
        if(this.fixedAssetMaintOrders == null) {
            this.fixedAssetMaintOrders = new ArrayList();
        }

        this.fixedAssetMaintOrders.add(fixedAssetMaintOrder);
    }

    public void removeFixedAssetMaintOrder(FixedAssetMaintOrder fixedAssetMaintOrder) {
        if(this.fixedAssetMaintOrders != null) {
            this.fixedAssetMaintOrders.remove(fixedAssetMaintOrder);
        }
    }

    public void clearFixedAssetMaintOrder() {
        if(this.fixedAssetMaintOrders != null) {
            this.fixedAssetMaintOrders.clear();
        }
    }

    public void addOldOrderItemInventoryRese(OldOrderItemInventoryRes oldOrderItemInventoryRese) {
        if(this.oldOrderItemInventoryReses == null) {
            this.oldOrderItemInventoryReses = new ArrayList();
        }

        this.oldOrderItemInventoryReses.add(oldOrderItemInventoryRese);
    }

    public void removeOldOrderItemInventoryRese(OldOrderItemInventoryRes oldOrderItemInventoryRese) {
        if(this.oldOrderItemInventoryReses != null) {
            this.oldOrderItemInventoryReses.remove(oldOrderItemInventoryRese);
        }
    }

    public void clearOldOrderItemInventoryRese() {
        if(this.oldOrderItemInventoryReses != null) {
            this.oldOrderItemInventoryReses.clear();
        }
    }

    public void addOldOrderShipmentPreference(OldOrderShipmentPreference oldOrderShipmentPreference) {
        if(this.oldOrderShipmentPreferences == null) {
            this.oldOrderShipmentPreferences = new ArrayList();
        }

        this.oldOrderShipmentPreferences.add(oldOrderShipmentPreference);
    }

    public void removeOldOrderShipmentPreference(OldOrderShipmentPreference oldOrderShipmentPreference) {
        if(this.oldOrderShipmentPreferences != null) {
            this.oldOrderShipmentPreferences.remove(oldOrderShipmentPreference);
        }
    }

    public void clearOldOrderShipmentPreference() {
        if(this.oldOrderShipmentPreferences != null) {
            this.oldOrderShipmentPreferences.clear();
        }
    }

    public void addOrderAttribute(OrderAttribute orderAttribute) {
        if(this.orderAttributes == null) {
            this.orderAttributes = new ArrayList();
        }

        this.orderAttributes.add(orderAttribute);
    }

    public void removeOrderAttribute(OrderAttribute orderAttribute) {
        if(this.orderAttributes != null) {
            this.orderAttributes.remove(orderAttribute);
        }
    }

    public void clearOrderAttribute() {
        if(this.orderAttributes != null) {
            this.orderAttributes.clear();
        }
    }

    public void addOrderContactMeche(OrderContactMech orderContactMeche) {
        if(this.orderContactMeches == null) {
            this.orderContactMeches = new ArrayList();
        }

        this.orderContactMeches.add(orderContactMeche);
    }

    public void removeOrderContactMeche(OrderContactMech orderContactMeche) {
        if(this.orderContactMeches != null) {
            this.orderContactMeches.remove(orderContactMeche);
        }
    }

    public void clearOrderContactMeche() {
        if(this.orderContactMeches != null) {
            this.orderContactMeches.clear();
        }
    }

    public void addOrderContent(OrderContent orderContent) {
        if(this.orderContents == null) {
            this.orderContents = new ArrayList();
        }

        this.orderContents.add(orderContent);
    }

    public void removeOrderContent(OrderContent orderContent) {
        if(this.orderContents != null) {
            this.orderContents.remove(orderContent);
        }
    }

    public void clearOrderContent() {
        if(this.orderContents != null) {
            this.orderContents.clear();
        }
    }

    public void addOrderDeliverySchedule(OrderDeliverySchedule orderDeliverySchedule) {
        if(this.orderDeliverySchedules == null) {
            this.orderDeliverySchedules = new ArrayList();
        }

        this.orderDeliverySchedules.add(orderDeliverySchedule);
    }

    public void removeOrderDeliverySchedule(OrderDeliverySchedule orderDeliverySchedule) {
        if(this.orderDeliverySchedules != null) {
            this.orderDeliverySchedules.remove(orderDeliverySchedule);
        }
    }

    public void clearOrderDeliverySchedule() {
        if(this.orderDeliverySchedules != null) {
            this.orderDeliverySchedules.clear();
        }
    }

    public void addOrderHeaderContent(OrderHeaderContent orderHeaderContent) {
        if(this.orderHeaderContents == null) {
            this.orderHeaderContents = new ArrayList();
        }

        this.orderHeaderContents.add(orderHeaderContent);
    }

    public void removeOrderHeaderContent(OrderHeaderContent orderHeaderContent) {
        if(this.orderHeaderContents != null) {
            this.orderHeaderContents.remove(orderHeaderContent);
        }
    }

    public void clearOrderHeaderContent() {
        if(this.orderHeaderContents != null) {
            this.orderHeaderContents.clear();
        }
    }

    public void addOrderHeaderNote(OrderHeaderNote orderHeaderNote) {
        if(this.orderHeaderNotes == null) {
            this.orderHeaderNotes = new ArrayList();
        }

        this.orderHeaderNotes.add(orderHeaderNote);
    }

    public void removeOrderHeaderNote(OrderHeaderNote orderHeaderNote) {
        if(this.orderHeaderNotes != null) {
            this.orderHeaderNotes.remove(orderHeaderNote);
        }
    }

    public void clearOrderHeaderNote() {
        if(this.orderHeaderNotes != null) {
            this.orderHeaderNotes.clear();
        }
    }

    public void addOrderHeaderWorkEffort(OrderHeaderWorkEffort orderHeaderWorkEffort) {
        if(this.orderHeaderWorkEfforts == null) {
            this.orderHeaderWorkEfforts = new ArrayList();
        }

        this.orderHeaderWorkEfforts.add(orderHeaderWorkEffort);
    }

    public void removeOrderHeaderWorkEffort(OrderHeaderWorkEffort orderHeaderWorkEffort) {
        if(this.orderHeaderWorkEfforts != null) {
            this.orderHeaderWorkEfforts.remove(orderHeaderWorkEffort);
        }
    }

    public void clearOrderHeaderWorkEffort() {
        if(this.orderHeaderWorkEfforts != null) {
            this.orderHeaderWorkEfforts.clear();
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        if(this.orderItems == null) {
            this.orderItems = new ArrayList();
        }

        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        if(this.orderItems != null) {
            this.orderItems.remove(orderItem);
        }
    }

    public void clearOrderItem() {
        if(this.orderItems != null) {
            this.orderItems.clear();
        }
    }

    public void addFromOrderItemAssoc(OrderItemAssoc fromOrderItemAssoc) {
        if(this.fromOrderItemAssocs == null) {
            this.fromOrderItemAssocs = new ArrayList();
        }

        this.fromOrderItemAssocs.add(fromOrderItemAssoc);
    }

    public void removeFromOrderItemAssoc(OrderItemAssoc fromOrderItemAssoc) {
        if(this.fromOrderItemAssocs != null) {
            this.fromOrderItemAssocs.remove(fromOrderItemAssoc);
        }
    }

    public void clearFromOrderItemAssoc() {
        if(this.fromOrderItemAssocs != null) {
            this.fromOrderItemAssocs.clear();
        }
    }

    public void addToOrderItemAssoc(OrderItemAssoc toOrderItemAssoc) {
        if(this.toOrderItemAssocs == null) {
            this.toOrderItemAssocs = new ArrayList();
        }

        this.toOrderItemAssocs.add(toOrderItemAssoc);
    }

    public void removeToOrderItemAssoc(OrderItemAssoc toOrderItemAssoc) {
        if(this.toOrderItemAssocs != null) {
            this.toOrderItemAssocs.remove(toOrderItemAssoc);
        }
    }

    public void clearToOrderItemAssoc() {
        if(this.toOrderItemAssocs != null) {
            this.toOrderItemAssocs.clear();
        }
    }

    public void addOrderItemBilling(OrderItemBilling orderItemBilling) {
        if(this.orderItemBillings == null) {
            this.orderItemBillings = new ArrayList();
        }

        this.orderItemBillings.add(orderItemBilling);
    }

    public void removeOrderItemBilling(OrderItemBilling orderItemBilling) {
        if(this.orderItemBillings != null) {
            this.orderItemBillings.remove(orderItemBilling);
        }
    }

    public void clearOrderItemBilling() {
        if(this.orderItemBillings != null) {
            this.orderItemBillings.clear();
        }
    }

    public void addOrderItemContactMeche(OrderItemContactMech orderItemContactMeche) {
        if(this.orderItemContactMeches == null) {
            this.orderItemContactMeches = new ArrayList();
        }

        this.orderItemContactMeches.add(orderItemContactMeche);
    }

    public void removeOrderItemContactMeche(OrderItemContactMech orderItemContactMeche) {
        if(this.orderItemContactMeches != null) {
            this.orderItemContactMeches.remove(orderItemContactMeche);
        }
    }

    public void clearOrderItemContactMeche() {
        if(this.orderItemContactMeches != null) {
            this.orderItemContactMeches.clear();
        }
    }

    public void addOrderItemGroup(OrderItemGroup orderItemGroup) {
        if(this.orderItemGroups == null) {
            this.orderItemGroups = new ArrayList();
        }

        this.orderItemGroups.add(orderItemGroup);
    }

    public void removeOrderItemGroup(OrderItemGroup orderItemGroup) {
        if(this.orderItemGroups != null) {
            this.orderItemGroups.remove(orderItemGroup);
        }
    }

    public void clearOrderItemGroup() {
        if(this.orderItemGroups != null) {
            this.orderItemGroups.clear();
        }
    }

    public void addOrderItemRole(OrderItemRole orderItemRole) {
        if(this.orderItemRoles == null) {
            this.orderItemRoles = new ArrayList();
        }

        this.orderItemRoles.add(orderItemRole);
    }

    public void removeOrderItemRole(OrderItemRole orderItemRole) {
        if(this.orderItemRoles != null) {
            this.orderItemRoles.remove(orderItemRole);
        }
    }

    public void clearOrderItemRole() {
        if(this.orderItemRoles != null) {
            this.orderItemRoles.clear();
        }
    }

    public void addOrderItemShipGroup(OrderItemShipGroup orderItemShipGroup) {
        if(this.orderItemShipGroups == null) {
            this.orderItemShipGroups = new ArrayList();
        }

        this.orderItemShipGroups.add(orderItemShipGroup);
    }

    public void removeOrderItemShipGroup(OrderItemShipGroup orderItemShipGroup) {
        if(this.orderItemShipGroups != null) {
            this.orderItemShipGroups.remove(orderItemShipGroup);
        }
    }

    public void clearOrderItemShipGroup() {
        if(this.orderItemShipGroups != null) {
            this.orderItemShipGroups.clear();
        }
    }

    public void addOrderItemShipGroupAssoc(OrderItemShipGroupAssoc orderItemShipGroupAssoc) {
        if(this.orderItemShipGroupAssocs == null) {
            this.orderItemShipGroupAssocs = new ArrayList();
        }

        this.orderItemShipGroupAssocs.add(orderItemShipGroupAssoc);
    }

    public void removeOrderItemShipGroupAssoc(OrderItemShipGroupAssoc orderItemShipGroupAssoc) {
        if(this.orderItemShipGroupAssocs != null) {
            this.orderItemShipGroupAssocs.remove(orderItemShipGroupAssoc);
        }
    }

    public void clearOrderItemShipGroupAssoc() {
        if(this.orderItemShipGroupAssocs != null) {
            this.orderItemShipGroupAssocs.clear();
        }
    }

    public void addOrderItemShipGrpInvRese(OrderItemShipGrpInvRes orderItemShipGrpInvRese) {
        if(this.orderItemShipGrpInvReses == null) {
            this.orderItemShipGrpInvReses = new ArrayList();
        }

        this.orderItemShipGrpInvReses.add(orderItemShipGrpInvRese);
    }

    public void removeOrderItemShipGrpInvRese(OrderItemShipGrpInvRes orderItemShipGrpInvRese) {
        if(this.orderItemShipGrpInvReses != null) {
            this.orderItemShipGrpInvReses.remove(orderItemShipGrpInvRese);
        }
    }

    public void clearOrderItemShipGrpInvRese() {
        if(this.orderItemShipGrpInvReses != null) {
            this.orderItemShipGrpInvReses.clear();
        }
    }

    public void addOrderProductPromoCode(OrderProductPromoCode orderProductPromoCode) {
        if(this.orderProductPromoCodes == null) {
            this.orderProductPromoCodes = new ArrayList();
        }

        this.orderProductPromoCodes.add(orderProductPromoCode);
    }

    public void removeOrderProductPromoCode(OrderProductPromoCode orderProductPromoCode) {
        if(this.orderProductPromoCodes != null) {
            this.orderProductPromoCodes.remove(orderProductPromoCode);
        }
    }

    public void clearOrderProductPromoCode() {
        if(this.orderProductPromoCodes != null) {
            this.orderProductPromoCodes.clear();
        }
    }

    public void addOrderRequirementCommitment(OrderRequirementCommitment orderRequirementCommitment) {
        if(this.orderRequirementCommitments == null) {
            this.orderRequirementCommitments = new ArrayList();
        }

        this.orderRequirementCommitments.add(orderRequirementCommitment);
    }

    public void removeOrderRequirementCommitment(OrderRequirementCommitment orderRequirementCommitment) {
        if(this.orderRequirementCommitments != null) {
            this.orderRequirementCommitments.remove(orderRequirementCommitment);
        }
    }

    public void clearOrderRequirementCommitment() {
        if(this.orderRequirementCommitments != null) {
            this.orderRequirementCommitments.clear();
        }
    }

    public void addOrderRole(OrderRole orderRole) {
        if(this.orderRoles == null) {
            this.orderRoles = new ArrayList();
        }

        this.orderRoles.add(orderRole);
    }

    public void removeOrderRole(OrderRole orderRole) {
        if(this.orderRoles != null) {
            this.orderRoles.remove(orderRole);
        }
    }

    public void clearOrderRole() {
        if(this.orderRoles != null) {
            this.orderRoles.clear();
        }
    }

    public void addOrderShipGroupPriority(OrderShipGroupPriority orderShipGroupPriority) {
        if(this.orderShipGroupPrioritys == null) {
            this.orderShipGroupPrioritys = new ArrayList();
        }

        this.orderShipGroupPrioritys.add(orderShipGroupPriority);
    }

    public void removeOrderShipGroupPriority(OrderShipGroupPriority orderShipGroupPriority) {
        if(this.orderShipGroupPrioritys != null) {
            this.orderShipGroupPrioritys.remove(orderShipGroupPriority);
        }
    }

    public void clearOrderShipGroupPriority() {
        if(this.orderShipGroupPrioritys != null) {
            this.orderShipGroupPrioritys.clear();
        }
    }

    public void addOrderShipment(OrderShipment orderShipment) {
        if(this.orderShipments == null) {
            this.orderShipments = new ArrayList();
        }

        this.orderShipments.add(orderShipment);
    }

    public void removeOrderShipment(OrderShipment orderShipment) {
        if(this.orderShipments != null) {
            this.orderShipments.remove(orderShipment);
        }
    }

    public void clearOrderShipment() {
        if(this.orderShipments != null) {
            this.orderShipments.clear();
        }
    }

    public void addOrderTerm(OrderTerm orderTerm) {
        if(this.orderTerms == null) {
            this.orderTerms = new ArrayList();
        }

        this.orderTerms.add(orderTerm);
    }

    public void removeOrderTerm(OrderTerm orderTerm) {
        if(this.orderTerms != null) {
            this.orderTerms.remove(orderTerm);
        }
    }

    public void clearOrderTerm() {
        if(this.orderTerms != null) {
            this.orderTerms.clear();
        }
    }

    public void addPicklistItem(PicklistItem picklistItem) {
        if(this.picklistItems == null) {
            this.picklistItems = new ArrayList();
        }

        this.picklistItems.add(picklistItem);
    }

    public void removePicklistItem(PicklistItem picklistItem) {
        if(this.picklistItems != null) {
            this.picklistItems.remove(picklistItem);
        }
    }

    public void clearPicklistItem() {
        if(this.picklistItems != null) {
            this.picklistItems.clear();
        }
    }

    public void addProductOrderItem(ProductOrderItem productOrderItem) {
        if(this.productOrderItems == null) {
            this.productOrderItems = new ArrayList();
        }

        this.productOrderItems.add(productOrderItem);
    }

    public void removeProductOrderItem(ProductOrderItem productOrderItem) {
        if(this.productOrderItems != null) {
            this.productOrderItems.remove(productOrderItem);
        }
    }

    public void clearProductOrderItem() {
        if(this.productOrderItems != null) {
            this.productOrderItems.clear();
        }
    }

    public void addEngagementProductOrderItem(ProductOrderItem engagementProductOrderItem) {
        if(this.engagementProductOrderItems == null) {
            this.engagementProductOrderItems = new ArrayList();
        }

        this.engagementProductOrderItems.add(engagementProductOrderItem);
    }

    public void removeEngagementProductOrderItem(ProductOrderItem engagementProductOrderItem) {
        if(this.engagementProductOrderItems != null) {
            this.engagementProductOrderItems.remove(engagementProductOrderItem);
        }
    }

    public void clearEngagementProductOrderItem() {
        if(this.engagementProductOrderItems != null) {
            this.engagementProductOrderItems.clear();
        }
    }

    public void addProductPromoUse(ProductPromoUse productPromoUse) {
        if(this.productPromoUses == null) {
            this.productPromoUses = new ArrayList();
        }

        this.productPromoUses.add(productPromoUse);
    }

    public void removeProductPromoUse(ProductPromoUse productPromoUse) {
        if(this.productPromoUses != null) {
            this.productPromoUses.remove(productPromoUse);
        }
    }

    public void clearProductPromoUse() {
        if(this.productPromoUses != null) {
            this.productPromoUses.clear();
        }
    }

    public void addTrackingCodeOrder(TrackingCodeOrder trackingCodeOrder) {
        if(this.trackingCodeOrders == null) {
            this.trackingCodeOrders = new ArrayList();
        }

        this.trackingCodeOrders.add(trackingCodeOrder);
    }

    public void removeTrackingCodeOrder(TrackingCodeOrder trackingCodeOrder) {
        if(this.trackingCodeOrders != null) {
            this.trackingCodeOrders.remove(trackingCodeOrder);
        }
    }

    public void clearTrackingCodeOrder() {
        if(this.trackingCodeOrders != null) {
            this.trackingCodeOrders.clear();
        }
    }

    public void addTrackingCodeOrderReturn(TrackingCodeOrderReturn trackingCodeOrderReturn) {
        if(this.trackingCodeOrderReturns == null) {
            this.trackingCodeOrderReturns = new ArrayList();
        }

        this.trackingCodeOrderReturns.add(trackingCodeOrderReturn);
    }

    public void removeTrackingCodeOrderReturn(TrackingCodeOrderReturn trackingCodeOrderReturn) {
        if(this.trackingCodeOrderReturns != null) {
            this.trackingCodeOrderReturns.remove(trackingCodeOrderReturn);
        }
    }

    public void clearTrackingCodeOrderReturn() {
        if(this.trackingCodeOrderReturns != null) {
            this.trackingCodeOrderReturns.clear();
        }
    }

    public void addWorkOrderItemFulfillment(WorkOrderItemFulfillment workOrderItemFulfillment) {
        if(this.workOrderItemFulfillments == null) {
            this.workOrderItemFulfillments = new ArrayList();
        }

        this.workOrderItemFulfillments.add(workOrderItemFulfillment);
    }

    public void removeWorkOrderItemFulfillment(WorkOrderItemFulfillment workOrderItemFulfillment) {
        if(this.workOrderItemFulfillments != null) {
            this.workOrderItemFulfillments.remove(workOrderItemFulfillment);
        }
    }

    public void clearWorkOrderItemFulfillment() {
        if(this.workOrderItemFulfillments != null) {
            this.workOrderItemFulfillments.clear();
        }
    }

    public void fromMap(Map<String, Object> mapValue) {
        this.preInit();
        this.setOrderId((String)mapValue.get("orderId"));
        this.setOrderTypeId((String)mapValue.get("orderTypeId"));
        this.setOrderName((String)mapValue.get("orderName"));
        this.setExternalId((String)mapValue.get("externalId"));
        this.setSalesChannelEnumId((String)mapValue.get("salesChannelEnumId"));
        this.setOrderDate((Timestamp)mapValue.get("orderDate"));
        this.setPriority((String)mapValue.get("priority"));
        this.setEntryDate((Timestamp)mapValue.get("entryDate"));
        this.setPickSheetPrintedDate((Timestamp)mapValue.get("pickSheetPrintedDate"));
        this.setVisitId((String)mapValue.get("visitId"));
        this.setStatusId((String)mapValue.get("statusId"));
        this.setCreatedBy((String)mapValue.get("createdBy"));
        this.setFirstAttemptOrderId((String)mapValue.get("firstAttemptOrderId"));
        this.setCurrencyUom((String)mapValue.get("currencyUom"));
        this.setSyncStatusId((String)mapValue.get("syncStatusId"));
        this.setBillingAccountId((String)mapValue.get("billingAccountId"));
        this.setOriginFacilityId((String)mapValue.get("originFacilityId"));
        this.setWebSiteId((String)mapValue.get("webSiteId"));
        this.setProductStoreId((String)mapValue.get("productStoreId"));
        this.setTerminalId((String)mapValue.get("terminalId"));
        this.setTransactionId((String)mapValue.get("transactionId"));
        this.setAutoOrderShoppingListId((String)mapValue.get("autoOrderShoppingListId"));
        this.setNeedsInventoryIssuance((String)mapValue.get("needsInventoryIssuance"));
        this.setIsRushOrder((String)mapValue.get("isRushOrder"));
        this.setInternalCode((String)mapValue.get("internalCode"));
        this.setRemainingSubTotal(this.convertToBigDecimal(mapValue.get("remainingSubTotal")));
        this.setGrandTotal(this.convertToBigDecimal(mapValue.get("grandTotal")));
        this.setIsViewed((String)mapValue.get("isViewed"));
        this.setLastUpdatedStamp((Timestamp)mapValue.get("lastUpdatedStamp"));
        this.setLastUpdatedTxStamp((Timestamp)mapValue.get("lastUpdatedTxStamp"));
        this.setCreatedStamp((Timestamp)mapValue.get("createdStamp"));
        this.setCreatedTxStamp((Timestamp)mapValue.get("createdTxStamp"));
        this.setBillFromPartyId((String)mapValue.get("billFromPartyId"));
        this.setBillToPartyId((String)mapValue.get("billToPartyId"));
        this.setIsFixOrder((String)mapValue.get("isFixOrder"));
        
        this.postInit();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mapValue = new FastMap();
        mapValue.put("orderId", this.getOrderId());
        mapValue.put("orderTypeId", this.getOrderTypeId());
        mapValue.put("orderName", this.getOrderName());
        mapValue.put("externalId", this.getExternalId());
        mapValue.put("salesChannelEnumId", this.getSalesChannelEnumId());
        mapValue.put("orderDate", this.getOrderDate());
        mapValue.put("priority", this.getPriority());
        mapValue.put("entryDate", this.getEntryDate());
        mapValue.put("pickSheetPrintedDate", this.getPickSheetPrintedDate());
        mapValue.put("visitId", this.getVisitId());
        mapValue.put("statusId", this.getStatusId());
        mapValue.put("createdBy", this.getCreatedBy());
        mapValue.put("firstAttemptOrderId", this.getFirstAttemptOrderId());
        mapValue.put("currencyUom", this.getCurrencyUom());
        mapValue.put("syncStatusId", this.getSyncStatusId());
        mapValue.put("billingAccountId", this.getBillingAccountId());
        mapValue.put("originFacilityId", this.getOriginFacilityId());
        mapValue.put("webSiteId", this.getWebSiteId());
        mapValue.put("productStoreId", this.getProductStoreId());
        mapValue.put("terminalId", this.getTerminalId());
        mapValue.put("transactionId", this.getTransactionId());
        mapValue.put("autoOrderShoppingListId", this.getAutoOrderShoppingListId());
        mapValue.put("needsInventoryIssuance", this.getNeedsInventoryIssuance());
        mapValue.put("isRushOrder", this.getIsRushOrder());
        mapValue.put("internalCode", this.getInternalCode());
        mapValue.put("remainingSubTotal", this.getRemainingSubTotal());
        mapValue.put("grandTotal", this.getGrandTotal());
        mapValue.put("isViewed", this.getIsViewed());
        mapValue.put("lastUpdatedStamp", this.getLastUpdatedStamp());
        mapValue.put("lastUpdatedTxStamp", this.getLastUpdatedTxStamp());
        mapValue.put("createdStamp", this.getCreatedStamp());
        mapValue.put("createdTxStamp", this.getCreatedTxStamp());
        mapValue.put("billFromPartyId", this.getBillFromPartyId());
        mapValue.put("billToPartyId", this.getBillToPartyId());
        mapValue.put("isFixOrder", this.getIsFixOrder());
        return mapValue;
    }

    static {
        Map<String, String> fields = new HashMap();
        fields.put("orderId", "ORDER_ID");
        fields.put("orderTypeId", "ORDER_TYPE_ID");
        fields.put("orderName", "ORDER_NAME");
        fields.put("externalId", "EXTERNAL_ID");
        fields.put("salesChannelEnumId", "SALES_CHANNEL_ENUM_ID");
        fields.put("orderDate", "ORDER_DATE");
        fields.put("priority", "PRIORITY");
        fields.put("entryDate", "ENTRY_DATE");
        fields.put("pickSheetPrintedDate", "PICK_SHEET_PRINTED_DATE");
        fields.put("visitId", "VISIT_ID");
        fields.put("statusId", "STATUS_ID");
        fields.put("createdBy", "CREATED_BY");
        fields.put("firstAttemptOrderId", "FIRST_ATTEMPT_ORDER_ID");
        fields.put("currencyUom", "CURRENCY_UOM");
        fields.put("syncStatusId", "SYNC_STATUS_ID");
        fields.put("billingAccountId", "BILLING_ACCOUNT_ID");
        fields.put("originFacilityId", "ORIGIN_FACILITY_ID");
        fields.put("webSiteId", "WEB_SITE_ID");
        fields.put("productStoreId", "PRODUCT_STORE_ID");
        fields.put("terminalId", "TERMINAL_ID");
        fields.put("transactionId", "TRANSACTION_ID");
        fields.put("autoOrderShoppingListId", "AUTO_ORDER_SHOPPING_LIST_ID");
        fields.put("needsInventoryIssuance", "NEEDS_INVENTORY_ISSUANCE");
        fields.put("isRushOrder", "IS_RUSH_ORDER");
        fields.put("internalCode", "INTERNAL_CODE");
        fields.put("remainingSubTotal", "REMAINING_SUB_TOTAL");
        fields.put("grandTotal", "GRAND_TOTAL");
        fields.put("isViewed", "IS_VIEWED");
        fields.put("lastUpdatedStamp", "LAST_UPDATED_STAMP");
        fields.put("lastUpdatedTxStamp", "LAST_UPDATED_TX_STAMP");
        fields.put("createdStamp", "CREATED_STAMP");
        fields.put("createdTxStamp", "CREATED_TX_STAMP");
        fields.put("billFromPartyId", "BILL_FROM_PARTY_ID");
        fields.put("billToPartyId", "BILL_TO_PARTY_ID");
        fields.put("isFixOrder", "IS_FIX_ORDER");
        fieldMapColumns.put("OrderHeader", fields);
    }

    public static enum Fields implements EntityFieldInterface<OrderHeader> {
        orderId("orderId"),
        orderTypeId("orderTypeId"),
        orderName("orderName"),
        externalId("externalId"),
        salesChannelEnumId("salesChannelEnumId"),
        orderDate("orderDate"),
        priority("priority"),
        entryDate("entryDate"),
        pickSheetPrintedDate("pickSheetPrintedDate"),
        visitId("visitId"),
        statusId("statusId"),
        createdBy("createdBy"),
        firstAttemptOrderId("firstAttemptOrderId"),
        currencyUom("currencyUom"),
        syncStatusId("syncStatusId"),
        billingAccountId("billingAccountId"),
        originFacilityId("originFacilityId"),
        webSiteId("webSiteId"),
        productStoreId("productStoreId"),
        terminalId("terminalId"),
        transactionId("transactionId"),
        autoOrderShoppingListId("autoOrderShoppingListId"),
        needsInventoryIssuance("needsInventoryIssuance"),
        isRushOrder("isRushOrder"),
        internalCode("internalCode"),
        remainingSubTotal("remainingSubTotal"),
        grandTotal("grandTotal"),
        isViewed("isViewed"),
        lastUpdatedStamp("lastUpdatedStamp"),
        lastUpdatedTxStamp("lastUpdatedTxStamp"),
        createdStamp("createdStamp"),
        createdTxStamp("createdTxStamp"),
        billFromPartyId("billFromPartyId"),
        billToPartyId("billToPartyId"),
    	isFixOrder("isFixOrder");

        private final String fieldName;

        private Fields(String name) {
            this.fieldName = name;
        }

        public String getName() {
            return this.fieldName;
        }

        public String asc() {
            return this.fieldName + " ASC";
        }

        public String desc() {
            return this.fieldName + " DESC";
        }
    }
}
