-- ----------------------------
-- Table structure for ingameshop
-- ----------------------------
DROP TABLE IF EXISTS `ingameshop`;
CREATE TABLE `ingameshop` (
  `object_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL,
  `item_count` bigint(13) NOT NULL DEFAULT '0',
  `item_price` bigint(13) NOT NULL DEFAULT '0',
  `category` tinyint(1) NOT NULL DEFAULT '0',
  `sub_category` tinyint(1) NOT NULL DEFAULT '0',
  `list` int(11) NOT NULL DEFAULT '0',
  `sales_ranking` int(11) NOT NULL DEFAULT '0',
  `item_type` tinyint(1) NOT NULL DEFAULT '0',
  `gift` tinyint(1) NOT NULL DEFAULT '0',
  `title_description` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`object_id`)
) ENGINE=MyISAM AUTO_INCREMENT=183140 DEFAULT CHARSET=utf8;
