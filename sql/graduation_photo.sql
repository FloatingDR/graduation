/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : graduation_photo

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 25/05/2020 16:30:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_num` varchar(50) DEFAULT NULL COMMENT '学号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `college` varchar(50) DEFAULT NULL COMMENT '学院',
  `profession` varchar(50) DEFAULT NULL COMMENT '专业',
  `class_name` varchar(20) DEFAULT NULL COMMENT '班级',
  `photo` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `analysed_photo` varchar(255) DEFAULT NULL COMMENT '抠图后的图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (3, '20160511000', 'lisi', '计算机学院', '计算机科学与技术', '1602', '/Users/taylor/springboot/graduation/src/main/resources/photo/b08c77e73f6343d9ada3b8428ab0eadc_20160511000.jpeg', '/Users/taylor/springboot/graduation/src/main/resources/photo/b08c77e73f6343d9ada3b8428ab0eadc_20160511000_analysed.png');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
