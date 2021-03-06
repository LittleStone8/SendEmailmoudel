/* yzl 20170628  增加安全组用于销售经理30天内取消订单*/

INSERT INTO `SECURITY_PERMISSION` (`PERMISSION_ID`, `DESCRIPTION`, `DYNAMIC_ACCESS`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ('BIM_ORDER_CANCEL_30', 'cancel订单权限,销售经理.', '', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05');

INSERT INTO `SECURITY_GROUP` (`GROUP_ID`, `DESCRIPTION`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ('BIM_ORDER_CANCEL_30', 'cancel订单权限,销售经理.', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05');

INSERT INTO `SECURITY_GROUP_PERMISSION` (`GROUP_ID`, `PERMISSION_ID`, `LAST_UPDATED_STAMP`, `LAST_UPDATED_TX_STAMP`, `CREATED_STAMP`, `CREATED_TX_STAMP`) VALUES ('BIM_ORDER_CANCEL_30', 'BIM_ORDER_CANCEL_30', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05', '2017-06-13 15:46:05');



ALTER TABLE `ORDER_HEADER` 
ADD COLUMN `CONTACT_MECH_ID` VARCHAR(45) NULL AFTER `IS_NEED_SHIP`;
