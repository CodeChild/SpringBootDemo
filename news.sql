-- 新闻表
create table `news`(
     id varchar (32) not null,
    `title` varchar (256) not null comment '标题',
     `content` text not null comment '内容',
     primary key (id)
) comment '新闻表';