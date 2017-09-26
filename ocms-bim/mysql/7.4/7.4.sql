/* fenghui 20170628 修改库存查找菜单 */
UPDATE OPENTAPS_SHORTCUT SET LINK_URL = 'newFindInventoryItem' WHERE SHORTCUT_ID = 'findInventoryItem';

/* yzl 20170628 */
INSERT INTO STATUS_ITEM(STATUS_ID,STATUS_TYPE_ID,STATUS_CODE,SEQUENCE_ID,DESCRIPTION,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP) 
 VALUES ("ORDER_COMPLETED_W_P","ORDER_STATUS","COMPLETED_WITHOUT_PAYMENT","00","Complete without payment","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24");

INSERT INTO INVOICE_TYPE(INVOICE_TYPE_ID,HAS_TABLE,DESCRIPTION,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP) 
 VALUES ("YIWLLINVOICE","N","YIWLLInvoice","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24");

INSERT INTO INVOICE_TYPE(INVOICE_TYPE_ID,PARENT_TYPE_ID,HAS_TABLE,DESCRIPTION,LAST_UPDATED_STAMP,LAST_UPDATED_TX_STAMP,CREATED_STAMP,CREATED_TX_STAMP) 
 VALUES ("COMMON_YIWLLINVOICE","YIWLLINVOICE","N","COMMON_YIWLLInvoice","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24","2017-05-19 14:27:24");
 
INSERT INTO `PAYMENT_METHOD_TYPE` (`PAYMENT_METHOD_TYPE_ID`, `DESCRIPTION`, `DEFAULT_GL_ACCOUNT_ID`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) 
VALUES ('CREDIT', 'Credit', NULL, '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00');

/*yzl 20170705 */
INSERT INTO `SECURITY_GROUP` (`GROUP_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) 
VALUES ('REP_INV_GH_MAN', 'Ghana manager report permissions','2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00');

INSERT INTO `SECURITY_GROUP` (`GROUP_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) 
VALUES ('REP_INV_UG_MAN', 'Uganda manager report permissions','2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00');

INSERT INTO `SECURITY_GROUP` (`GROUP_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) 
VALUES ('REP_INV_ADMIN', 'admin manager report permissions','2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00');


/*  fallboy 2017.6.30 payemt 状态*/
INSERT INTO `STATUS_ITEM` (`STATUS_ID`, `STATUS_TYPE_ID`, `STATUS_CODE`, `SEQUENCE_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ('PAYMENT_CHANGE', 'PAYMENT_PREF_STATUS', 'CHANGE', '99', 'CHANGE', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00', '2017-06-30 00:00:00');


