
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统登录表';

-- ----------------------------
-- Records of login
-- ----------------------------
BEGIN;
INSERT INTO `login` VALUES (1, 5, '0000', 'student');
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (5, '20160511000', '张三', '计算机学院', '计算机科学与技术', '1602', '/Users/taylor/springboot/graduation/src/main/resources/photo/a66b554a8e8a483682e33152b003b83e_20160511000.jpeg', '/Users/taylor/springboot/graduation/src/main/resources/photo/a66b554a8e8a483682e33152b003b83e_20160511000_analysed.png', 3);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
