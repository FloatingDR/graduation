
-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `password` varchar(50) COLLATE utf8_bin DEFAULT '0000' COMMENT '密码，默认 0000',
  `role` varchar(50) COLLATE utf8_bin DEFAULT 'student' COMMENT '角色：student(普通学生),monitor（班长），teacher（辅导员），admin（系统管理员）\n默认student',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统登录表';

-- ----------------------------
-- Records of login
-- ----------------------------
BEGIN;
INSERT INTO `login` VALUES (1, 5, '0000', 'student');
INSERT INTO `login` VALUES (3, 7, '0000', 'student');
COMMIT;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `model_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模板名称',
  `model_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '模板地址',
  `model_des` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模板描述',
  `model_type` int(11) NOT NULL COMMENT '模版类型，\n0：clothes(学士服)\n1：background(背景图片)\n3：other(其他)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='照片模板信息';

-- ----------------------------
-- Records of model
-- ----------------------------
BEGIN;
INSERT INTO `model` VALUES (1, '计算机学院学士服模板 1', '/Users/taylor/springboot/graduation/src/main/resources/model/c5eb19137f644de995b9f1e16a0e0ee8_model.png', '学士服模板', 0);
COMMIT;

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
  `analysed_state` int(11) DEFAULT '0' COMMENT '抠图结果\n0 - 未上传；\n1 - 上传中；\n2 - 上传失败；\n3 - 上传成功；',
  `clothes_path` varchar(255) DEFAULT '' COMMENT '合成学士服照地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (5, '20160511000', '张三', '计算机学院', '计算机科学与技术', '1602', '/Users/taylor/springboot/graduation/src/main/resources/photo/80b7cfe35e6f4e2db8975f7d8b91eedd_20160511000.jpeg', '/Users/taylor/springboot/graduation/src/main/resources/photo/80b7cfe35e6f4e2db8975f7d8b91eedd_20160511000_analysed.png', 3, '');
INSERT INTO `user` VALUES (7, '20160511001', '李四', '计算机学院', '计算机科学与技术', '1602', NULL, NULL, 0, '');
COMMIT;

-- ----------------------------
-- Table structure for user_interface_confine
-- ----------------------------
DROP TABLE IF EXISTS `user_interface_confine`;
CREATE TABLE `user_interface_confine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_invoke_count` int(11) DEFAULT NULL COMMENT '用户已调用次数',
  `interface_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '接口描述',
  `interface_path` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '接口地址',
  `confine_count` int(11) DEFAULT NULL COMMENT '限制调用次数',
  `invoking_forbid` bit(1) DEFAULT b'1' COMMENT '超出调用次数是否限制 0：不限制 1：限制；默认限制',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户-接口限制，限制调用次数';

-- ----------------------------
-- Records of user_interface_confine
-- ----------------------------
BEGIN;
INSERT INTO `user_interface_confine` VALUES (2, 5, 12, '人像AI抠图', '/photo/upload', 5, b'0', '2020-05-29 11:16:03');
COMMIT;

-- ----------------------------
-- Triggers structure for table user_interface_confine
-- ----------------------------
DROP TRIGGER IF EXISTS `user_interface_confine_update_time`;
delimiter ;;
CREATE TRIGGER `user_interface_confine_update_time` BEFORE UPDATE ON `user_interface_confine` FOR EACH ROW begin
    set new.update_time= CURRENT_TIMESTAMP;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
