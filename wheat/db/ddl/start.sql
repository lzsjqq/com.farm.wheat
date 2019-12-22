/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.7.19-log : Database - share
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `share`;

/*Table structure for table `deal_detail_info` */

DROP TABLE IF EXISTS `deal_detail_info`;

CREATE TABLE `deal_detail_info` (
  `id_deal_detail_info` int(11) NOT NULL AUTO_INCREMENT COMMENT '交易记录ID',
  `id_deal_info` int(11) NOT NULL COMMENT 'id_deal_info',
  `trading_date` date DEFAULT NULL COMMENT '交易日',
  `share_code` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `share_name` varchar(120) NOT NULL COMMENT '名字',
  `deal_price` decimal(12,3) DEFAULT NULL COMMENT '成交价',
  `change_money` decimal(12,3) DEFAULT NULL COMMENT '手续费',
  `stamp_duty` decimal(12,3) DEFAULT NULL COMMENT '印花税',
  `volume` int(12) DEFAULT NULL COMMENT '成交量手',
  `target` varchar(2) NOT NULL COMMENT '交易方向1=买入 2=卖出',
  `stop_loss_price` decimal(12,3) DEFAULT '0.000' COMMENT '止损位',
  `plan` text NOT NULL COMMENT '描述（交易理由）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_deal_detail_info`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Table structure for table `deal_info` */

DROP TABLE IF EXISTS `deal_info`;

CREATE TABLE `deal_info` (
  `id_deal_info` int(11) NOT NULL AUTO_INCREMENT COMMENT '交易记录ID',
  `trading_date` date DEFAULT NULL COMMENT '交易日',
  `share_code` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `share_name` varchar(120) NOT NULL COMMENT '名字',
  `first_cost` decimal(12,3) DEFAULT NULL COMMENT '成本价',
  `volume` int(12) DEFAULT NULL COMMENT '剩余量',
  `deal_price` decimal(12,3) DEFAULT NULL COMMENT '卖出价',
  `stamp_duty` decimal(12,3) DEFAULT NULL COMMENT '印花税',
  `change_money` decimal(12,3) DEFAULT NULL COMMENT '手续费',
  `stop_loss_price` decimal(12,3) DEFAULT NULL COMMENT '止损位',
  `low_price` decimal(12,3) DEFAULT NULL COMMENT '期间最低价',
  `high_price` decimal(12,3) DEFAULT NULL COMMENT '期间最高价',
  `profit` decimal(12,3) DEFAULT NULL COMMENT '收益',
  `five_low_price` decimal(12,3) DEFAULT NULL COMMENT '5日最低价(买入算)',
  `five_high_price` decimal(12,3) DEFAULT NULL COMMENT '5日最高价价(卖出算)',
  `five_profit` decimal(12,3) DEFAULT NULL COMMENT '5日风险收益比衡量短线选股水平，平均值大于3为优秀，大于2为良；',
  `ten_low_price` decimal(12,3) DEFAULT NULL COMMENT '10日最低价(买入算)',
  `ten_high_price` decimal(12,3) DEFAULT NULL COMMENT '10日最高价(卖出算)',
  `ten_profit` decimal(12,3) DEFAULT NULL COMMENT '10日风险收益比',
  `R_rate` decimal(12,3) DEFAULT NULL COMMENT 'R比率衡量买入点的合理程度，平均值大于3为优秀；',
  `plan` text COMMENT '描述（交易理由）',
  `analyse` text COMMENT '复盘',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1=持仓  2=完成',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_deal_info`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `deal_plan` */

DROP TABLE IF EXISTS `deal_plan`;

CREATE TABLE `deal_plan` (
  `id_deal_plan` int(11) NOT NULL AUTO_INCREMENT COMMENT '交易原因ID',
  `id_deal_info` int(11) NOT NULL COMMENT 'id_deal_detail_info',
  `trading_date` date DEFAULT NULL COMMENT '交易日',
  `share_code` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `share_name` varchar(120) NOT NULL COMMENT '名字',
  `tendency` text NOT NULL COMMENT '大盘趋势',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_deal_plan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `event` */

DROP TABLE IF EXISTS `event`;

CREATE TABLE `event` (
  `id_event` int(11) NOT NULL AUTO_INCREMENT COMMENT '事件ID',
  `event` text NOT NULL COMMENT '事件描述',
  `affect` text NOT NULL COMMENT '事件影响',
  `frequency` int(2) NOT NULL COMMENT '事件提及次数',
  `type` tinyint(2) NOT NULL COMMENT '事件类型',
  `event_date` date DEFAULT NULL COMMENT '事件发生日',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_event`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `share_concept_detail` */

DROP TABLE IF EXISTS `share_concept_detail`;

CREATE TABLE `share_concept_detail` (
  `id_concept_detail` int(11) NOT NULL AUTO_INCREMENT,
  `simple_name` varbinary(11) NOT NULL COMMENT '概念名',
  `share_code` varchar(10) DEFAULT NULL,
  `share_name` varchar(120) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_concept_detail`)
) ENGINE=InnoDB AUTO_INCREMENT=17392 DEFAULT CHARSET=utf8;

/*Table structure for table `share_concept_info` */

DROP TABLE IF EXISTS `share_concept_info`;

CREATE TABLE `share_concept_info` (
  `simple_name` varchar(100) NOT NULL COMMENT '概念简称',
  `concept_name` varchar(100) NOT NULL COMMENT '概念名称',
  `number` int(11) DEFAULT NULL COMMENT '公司数量',
  `desc` text COMMENT '概念描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`simple_name`),
  UNIQUE KEY `unique` (`concept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_concept_price` */

DROP TABLE IF EXISTS `share_concept_price`;

CREATE TABLE `share_concept_price` (
  `id_concept_price` int(11) NOT NULL AUTO_INCREMENT,
  `simple_name` varchar(100) NOT NULL,
  `avg_price` decimal(11,2) DEFAULT NULL COMMENT '平均价格',
  `change` decimal(11,2) DEFAULT NULL COMMENT '涨跌额',
  `price_change_ratio` decimal(11,2) DEFAULT NULL COMMENT '涨跌幅 %',
  `trading_money` decimal(11,2) DEFAULT NULL COMMENT '成交金额：万',
  `trading_volume` decimal(11,2) DEFAULT NULL COMMENT '成交量：手',
  `led_share_code` varchar(10) DEFAULT NULL COMMENT '领涨股',
  `trading_date` date NOT NULL COMMENT '交易日',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_concept_price`),
  KEY `trading_date` (`trading_date`)
) ENGINE=InnoDB AUTO_INCREMENT=865 DEFAULT CHARSET=utf8;

/*Table structure for table `share_concept_sts_limit` */

DROP TABLE IF EXISTS `share_concept_sts_limit`;

CREATE TABLE `share_concept_sts_limit` (
  `simple_name` varchar(100) NOT NULL COMMENT '概念简称',
  `concept_name` varchar(100) DEFAULT NULL COMMENT '概念name',
  `trading_date` date DEFAULT NULL COMMENT '交易日',
  `count` int(4) DEFAULT NULL COMMENT '数量',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型(1=一字板,2=涨停,3=一字跌停,4=跌停)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `unique` (`simple_name`,`trading_date`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_concept_sts_top` */

DROP TABLE IF EXISTS `share_concept_sts_top`;

CREATE TABLE `share_concept_sts_top` (
  `simple_name` varchar(100) NOT NULL COMMENT '概念简称',
  `concept_name` varchar(100) DEFAULT NULL COMMENT '概念name',
  `trading_date` date DEFAULT NULL COMMENT '交易日',
  `count` int(4) DEFAULT NULL COMMENT '数量',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型(十分之type几)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `unique` (`simple_name`,`trading_date`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_industry_detail` */

DROP TABLE IF EXISTS `share_industry_detail`;

CREATE TABLE `share_industry_detail` (
  `id_industry_detail` int(11) NOT NULL AUTO_INCREMENT,
  `id_share_info` int(11) NOT NULL,
  `share_code` varchar(10) DEFAULT NULL,
  `share_name` varchar(120) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_industry_detail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_industry_info` */

DROP TABLE IF EXISTS `share_industry_info`;

CREATE TABLE `share_industry_info` (
  `id_industry_info` int(11) NOT NULL AUTO_INCREMENT,
  `industry_name` varchar(100) NOT NULL COMMENT '概念名称',
  `number` int(11) DEFAULT NULL COMMENT '公司数量',
  `desc` text COMMENT '概念描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_industry_info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_industry_price` */

DROP TABLE IF EXISTS `share_industry_price`;

CREATE TABLE `share_industry_price` (
  `id_industry_price` int(11) NOT NULL AUTO_INCREMENT,
  `id_industry_info` int(11) NOT NULL,
  `avg_price` decimal(11,2) DEFAULT NULL COMMENT '平均价格',
  `change` decimal(11,2) DEFAULT NULL COMMENT '涨跌额',
  `price_change_ratio` decimal(11,2) DEFAULT NULL COMMENT '涨跌幅 %',
  `trading_money` decimal(11,2) DEFAULT NULL COMMENT '成交金额：万',
  `trading_volume` decimal(11,2) DEFAULT NULL COMMENT '成交量：手',
  `led_share_code` varchar(10) DEFAULT NULL COMMENT '领涨股',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_industry_price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `share_info` */

DROP TABLE IF EXISTS `share_info`;

CREATE TABLE `share_info` (
  `ID_SHARE_INFO` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SHARE_CODE` varchar(10) NOT NULL COMMENT '股票代码',
  `SHARE_NAME` varchar(120) NOT NULL COMMENT '名字',
  `CIRCULATION_MARKET_VALUE` decimal(13,2) DEFAULT NULL COMMENT '流通市值',
  `TOTAL_MARKET_VALUE` decimal(13,2) DEFAULT NULL COMMENT '总市值',
  `SOURCE` int(3) NOT NULL COMMENT '1=上 2=深 3=创 4=科创',
  `INDUSTRY` varchar(10) DEFAULT NULL COMMENT '所属行业',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `CREATE_BY` varchar(11) NOT NULL COMMENT '创建人',
  `UPDATE_BY` varchar(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`ID_SHARE_INFO`),
  UNIQUE KEY `SHARE_CODE_U` (`SHARE_CODE`),
  FULLTEXT KEY `SHARE_CODE_I` (`SHARE_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=43266 DEFAULT CHARSET=utf8;

/*Table structure for table `share_price` */

DROP TABLE IF EXISTS `share_price`;

CREATE TABLE `share_price` (
  `ID_SHARE_PRICE` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ID_SHARE_INFO` int(11) NOT NULL COMMENT 'SHARE_INFO主键',
  `TODAY_OPEN_PRICE` decimal(13,2) NOT NULL COMMENT '今日开盘价',
  `YESTERDAY_END_PRICE` decimal(13,2) DEFAULT NULL COMMENT '今日收盘价',
  `TODAY_END_PRICE` decimal(13,2) NOT NULL COMMENT '当前价格',
  `TODAY_MAX_PRICE` decimal(13,2) NOT NULL COMMENT '今日最高价',
  `TODAY_MIN_PRICE` decimal(13,2) NOT NULL COMMENT '今日最低价',
  `TRADING_VOLUME` int(11) NOT NULL COMMENT '成交量：手',
  `THE_OUTER` int(11) DEFAULT NULL COMMENT '外盘：手',
  `THE_INNER` int(11) DEFAULT NULL COMMENT '内盘：手',
  `TRADING_MONEY` decimal(13,2) NOT NULL COMMENT '成交金额：万',
  `QUARTER` int(1) DEFAULT NULL COMMENT '季度',
  `YEAR` int(4) DEFAULT NULL COMMENT '年份',
  `TRADING_DATE` date NOT NULL COMMENT '交易日',
  `PRICE_CHANGE_RATIO` decimal(13,2) DEFAULT NULL COMMENT '涨跌幅 %',
  `PRICE_CHANGE` decimal(13,2) DEFAULT NULL COMMENT '涨跌',
  `TURNOVER_RATE` decimal(13,2) DEFAULT NULL COMMENT '换手率',
  `AMPLITUDE` decimal(13,2) DEFAULT NULL COMMENT '振幅',
  `PB_RATIO` decimal(13,2) DEFAULT NULL COMMENT '市净率',
  `PE_RATIO` decimal(13,2) DEFAULT NULL COMMENT '市盈率',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `CREATE_BY` varchar(11) DEFAULT NULL COMMENT '创建人',
  `UPDATE_BY` varchar(11) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`ID_SHARE_PRICE`),
  UNIQUE KEY `UNIQUE_1` (`TRADING_DATE`,`ID_SHARE_INFO`)
) ENGINE=InnoDB AUTO_INCREMENT=1074563 DEFAULT CHARSET=utf8;

/*Table structure for table `share_turnover_rate` */

DROP TABLE IF EXISTS `share_turnover_rate`;

CREATE TABLE `share_turnover_rate` (
  `ID_TURNOVER_RATE` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ID_SHARE_INFO` int(11) NOT NULL,
  `NEW_RATE` decimal(13,2) DEFAULT NULL COMMENT '最新',
  `THREE_CHANGE_RATE` decimal(13,2) DEFAULT NULL COMMENT '三日相对于最新变化率',
  `FIVE_CHANGE_RATE` decimal(13,2) DEFAULT NULL COMMENT '五日相对于最新变化率',
  `TEN_CHANGE_RATE` decimal(13,2) DEFAULT NULL COMMENT '十日相对于最新变化率',
  `THREE_RATE` decimal(13,2) DEFAULT NULL COMMENT '三日平均',
  `FIVE_RATE` decimal(13,2) DEFAULT NULL COMMENT '五日平均',
  `TEN_RATE` decimal(13,2) DEFAULT NULL COMMENT '十日平均',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID_TURNOVER_RATE`),
  UNIQUE KEY `uniqu` (`ID_SHARE_INFO`)
) ENGINE=InnoDB AUTO_INCREMENT=12980 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_dic` */

DROP TABLE IF EXISTS `sys_dic`;

CREATE TABLE `sys_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dic_code` varchar(12) NOT NULL COMMENT '字典项',
  `dic_name` varchar(12) DEFAULT NULL COMMENT '字典名',
  `code_value` text COMMENT '字典项值',
  `code_name_cn` varchar(12) DEFAULT NULL COMMENT '字典项值对应描述',
  `code_name_en` varchar(12) DEFAULT NULL COMMENT '字典项值对应描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
