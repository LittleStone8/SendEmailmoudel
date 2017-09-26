ALTER TABLE `TAKE_STOCK` ADD COLUMN `FACILITY_ID` VARCHAR(20) NULL DEFAULT NULL;


/* yzl 20170911  分库。数据迁移*/
/* yzl 20170911  复制表结构*/
CREATE TABLE INVENTORY_REPORT201709 SELECT * FROM INVENTORY_REPORT WHERE 1=2;

/* yzl 20170911  复制表9.1月以后的数据到新表*/
insert into INVENTORY_REPORT201709 select * from INVENTORY_REPORT where INVENTORY_REPORT.RECORD_DATE>='2017-09-01 00:00:00';

/* yzl 20170911  删除旧表9.1月以后的数据*/
delete from INVENTORY_REPORT where INVENTORY_REPORT.RECORD_DATE>='2017-09-01 00:00:00';





/* yzl 20170911  BIM与BAM数据对接相关SQL*/

CREATE TABLE `bim_orders` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `country` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '国家(Ghana|Uganda)',
  `date` date NOT NULL COMMENT '日期',
  `valid_order` int(11) NOT NULL DEFAULT '0' COMMENT '有效订单数',
  `goods_count` int(11) NOT NULL DEFAULT '0' COMMENT '销售数量',
  `goods_sum` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '销售总额',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `orders_date_country_unique` (`date`,`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `bim_order_shops` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `country` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '国家(Ghana|Uganda)',
  `date` date NOT NULL COMMENT '日期',
  `shop_id` int(11) NOT NULL DEFAULT '0' COMMENT '店铺ID',
  `shop_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '店铺名',
  `valid_order` int(11) NOT NULL DEFAULT '0' COMMENT '有效订单数',
  `valid_order_percent` decimal(3,2) NOT NULL DEFAULT '0.00' COMMENT '有效订单占比',
  `goods_count` int(11) NOT NULL DEFAULT '0' COMMENT '销售数量',
  `goods_count_percent` decimal(3,2) NOT NULL DEFAULT '0.00' COMMENT '销售数量占比',
  `goods_sum` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '销售总额',
  `goods_sum_percent` decimal(3,2) NOT NULL DEFAULT '0.00' COMMENT '销售总额占比',
  `order_member` int(11) NOT NULL DEFAULT '0' COMMENT '提货用户数',
  `count_per_member` int(20) NOT NULL DEFAULT '0' COMMENT '客单台，销量/提货用户数',
  `price_per_order` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '笔单价，销售总额/有效订单',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_shops_date_shop_id_country_unique` (`date`,`shop_id`,`country`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


/* qzk 20170913  添加新表SQL*/
DROP TABLE IF EXISTS `PRODUCT_FEATURE_TYPE_CATEGORY`;
CREATE TABLE `PRODUCT_FEATURE_TYPE_CATEGORY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_FEATURE_TYPE_ID` varchar(20) NOT NULL,
  `PRODUCT_CATEGORY_ID` varchar(20) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `CREATED_TIME` bigint(20) DEFAULT NULL,
  `CREATED_USER` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `UPDATED_TIME` bigint(20) DEFAULT NULL,
  `UPDATED_USER` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

/* qzk 新表数据初始化 */
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('1', 'BatteryCLR', '100020', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('2', 'BlenderCLR', '100164', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('3', 'BluetoothHeadsetCLR', '100021', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('4', 'BulblightPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('5', 'CarchargerCLR', '100027', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('6', 'CaseCLR', '100026', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('7', 'CaseCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('8', 'CaseCLR', '100030', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('9', 'CeilinglampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('10', 'CeilingmountlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('11', 'ChargerCLR', '100022', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('12', 'COLOUR', '100144', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('13', 'COLOUR', '100026', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('14', 'COLOUR', '100022', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('15', 'COLOUR', '100025', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('16', 'COLOUR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('17', 'COLOUR', '100023', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('18', 'COLOUR', '100031', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('19', 'COLOUR', '100024', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('20', 'CovelightPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('21', 'D-panellampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('22', 'DataCableCLR', '100022', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('23', 'DataCableCLR', 'c100377', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('24', 'DoorBellCLR', '100206', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('25', 'DownlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('26', 'DownlampPN', 'c100388', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('27', 'ElectricaccessoryCLR', '100209', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('28', 'FanCLR', '100151', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('29', 'FanCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('30', 'FlashDriveCLR', '100025', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('31', 'FlashDriveVOL', '100025', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('32', 'FloodlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('33', 'HighbaylampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('34', 'HUAWEICLR', '100022', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('35', 'HUAWEICLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('36', 'HUAWEICLR', '100069', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('37', 'InfinixCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('38', 'IPHONECLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('39', 'IronCLR', '100157', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('40', 'ItelCLR', '100020', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('41', 'ItelCLR', '100026', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('42', 'ItelCLR', '100022', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('43', 'ItelCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('44', 'ItelCLR', '100069', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('45', 'KettleCLR', '100177', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('46', 'KettleCLR', '100152', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('47', 'KONKACLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('48', 'KZGCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('49', 'LawnampGardenlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('50', 'LUMIACLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('51', 'MR16PowerPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('52', 'OutdoorPowerPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('53', 'PanelbottomlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('54', 'PaneledgelampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('55', 'PowerBankCLR', '100031', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('56', 'PowerBankVOL', '100031', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('57', 'ProjectlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('58', 'SAMSUNGCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('59', 'SandwichMakerCLR', '100168', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('60', 'SelfieStickCLR', '100032', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('61', 'SocketCLR', '100035', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('62', 'SocketCLR', '100208', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('63', 'SpotlightPN', 'c100535', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('64', 'SpotlightPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('65', 'STORAGE', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('66', 'StriplightPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('67', 'StriplightPN', '100035', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('68', 'SwitchCLR', '100207', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('69', 'TECNOCLR', '100035', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('70', 'TECNOCLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('71', 'TECNOCLR', '100069', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('72', 'TracklampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('73', 'Tri-prooflampPN', 'c100330', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('74', 'Tri-prooflampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('75', 'TubelightPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('76', 'UnderwaterlampPN', '100200', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('77', 'VIWACLR', '100018', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('78', 'WiredHeadsetCLR', '100023', null, '0', 'admin', null, null);
INSERT INTO `PRODUCT_FEATURE_TYPE_CATEGORY` VALUES ('79', 'X-TIGICLR', '100018', null, '0', 'admin', null, null);