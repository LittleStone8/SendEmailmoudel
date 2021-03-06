/* yzl 20170823 店铺联系方式*/

alter table PRODUCT_STORE drop column PRODUCTADDRESS ; 

ALTER TABLE `PRODUCT_STORE`	ADD COLUMN `STORE_CONTACT_NUMBER` VARCHAR(100) NULL DEFAULT NULL AFTER `AREA_ID`;

ALTER TABLE `INVENTORY_ITEM_DETAIL`	ADD COLUMN `STATUS_ID` VARCHAR(20) NULL DEFAULT NULL;

/* hewei 转运单*/
CREATE TABLE `TRANSHIPMENT_SHIPPING_BILL` (
  `TRANSHIPMENT_SHIPPING_BILL_ID` varchar(20) NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `FROM_WAREHOUSE` varchar(255) DEFAULT NULL,
  `TO_WAREHOUSE` varchar(255) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER_lOGIN_ID` varchar(255) DEFAULT NULL,
  `LAST_UPDATE_USER_lOGIN_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TRANSHIPMENT_SHIPPING_BILL_ID`)
)

ALTER TABLE INVENTORY_TRANSFER ADD COLUMN TRANSHIPMENT_SHIPPING_BILL_ID VARCHAR(20);

INSERT INTO `STATUS_ITEM` 
(`STATUS_ID`, `STATUS_TYPE_ID`, `STATUS_CODE`, `SEQUENCE_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ('IXF_PICKING', 'INVENTORY_XFER_STTS', 'PICKING', NULL, 'Picking', '2017-08-16 00:00:00', '2017-08-16 00:00:00', '2017-08-16 00:00:00', '2017-08-16 00:00:00');


/*乌干达only */
ALTER TABLE `USER_LOGIN` 
CHANGE COLUMN `SCOPE` `SCOPE` VARCHAR(45) NULL DEFAULT 'UGX' ;
/*乌干达only end*/

/* 加纳 only */
ALTER TABLE `USER_LOGIN` 
CHANGE COLUMN `SCOPE` `SCOPE` VARCHAR(45) NULL DEFAULT 'GHS' ;
/* 加纳 only end */



