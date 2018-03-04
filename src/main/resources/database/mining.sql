/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.17-log : Database - mining
*********************************************************************
*/
CREATE DATABASE `mining` DEFAULT CHARACTER SET utf8;

USE `mining`;

/*Table structure for table `tab_dictionary` */

DROP TABLE IF EXISTS `tab_dictionary`;

CREATE TABLE `tab_dictionary` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `DIC_KEY` varchar(50) NOT NULL COMMENT '字典键',
  `DIC_VALUE` varchar(50) DEFAULT NULL COMMENT '字典值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tab_dictionary` */

/*Table structure for table `tab_mininginfo` */

DROP TABLE IF EXISTS `tab_mininginfo`;

CREATE TABLE `tab_mininginfo` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_ID` int(10) NOT NULL COMMENT '用户ID',
  `START_TIME` datetime(6) NOT NULL COMMENT '本次挖矿开始时间',
  `END_TIME` datetime(6) NOT NULL COMMENT '本次挖矿结束时间',
  `TYPE` int(1) NOT NULL COMMENT '挖矿类型',
  `STATE` int(1) NOT NULL COMMENT '本次挖矿状态',
  `RUNNING_TIME` decimal(7,3) DEFAULT '0.000' COMMENT '本次挖矿总共用时',
  `RUNNING_MILE` decimal(7,3) DEFAULT '0.000' COMMENT '本次湾矿总共里程',
  `MINING_AMOUNT` decimal(7,3) DEFAULT '0.000' COMMENT '本次一共挖矿金额',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tab_mininginfo` */

/*Table structure for table `tab_miningoverview` */

DROP TABLE IF EXISTS `tab_miningoverview`;

CREATE TABLE `tab_miningoverview` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_ID` int(10) NOT NULL COMMENT '用户ID',
  `TOTAL_AMOUNT` decimal(7,3) DEFAULT '0.000' COMMENT '用户总共挖矿数量',
  `TOTAL_MILE` decimal(7,3) DEFAULT '0.000' COMMENT '用户总共挖矿里程',
  `TOTAL_TIME` decimal(7,3) DEFAULT '0.000' COMMENT '用户总共挖矿时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tab_miningoverview` */

/*Table structure for table `tab_user` */

DROP TABLE IF EXISTS `tab_user`;

CREATE TABLE `tab_user` (
  `ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PHONE_ID` varchar(32) NOT NULL COMMENT '用户手机号',
  `PASSWORD` varchar(32) NOT NULL COMMENT '用户密码',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

