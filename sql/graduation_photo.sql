create table user
(
    id             bigint auto_increment comment '自增id'
        primary key,
    user_num       varchar(50)   null comment '学号',
    user_name      varchar(50)   null comment '姓名',
    college        varchar(50)   null comment '学院',
    profession     varchar(50)   null comment '专业',
    class_name     varchar(20)   null comment '班级',
    photo          varchar(255)  null comment '图片地址',
    analysed_photo varchar(255)  null comment '抠图后的图片地址',
    analysed_state int default 0 null comment '抠图结果
0 - 未上传；
1 - 上传中；
2 - 上传失败；
3 - 上传成功；'
)
    comment '用户表' charset = utf8;
BEGIN;
INSERT INTO graduation_photo.user (id, user_num, user_name, college, profession, class_name, photo, analysed_photo, analysed_state) VALUES (3, '20160511000', 'lisi', '计算机学院', '计算机科学与技术', '1602', '/Users/taylor/springboot/graduation/src/main/resources/photo/0aec8407bde64a30ab54c13de8831ca9_20160511000.jpeg', '/Users/taylor/springboot/graduation/src/main/resources/photo/0aec8407bde64a30ab54c13de8831ca9_20160511000_analysed.png', 3);
COMMIT;

