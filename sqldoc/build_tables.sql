CREATE TABLE t_user(
`id` BIGINT(20) NOT NULL COMMENT '用户ID,手机号码',
`nickname` VARCHAR(255) not NULL,
`password` VARCHAR(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
`salt` VARCHAR(10) DEFAULT NULL,
`head` VARCHAR(128) DEFAULT NULL COMMENT '头像',
`register_date` datetime DEFAULT NULL COMMENT '注册时间',
`last_login_date` datetime DEFAULT NULL COMMENT '最后一次登录事件',
`login_count` int(11) DEFAULT '0' COMMENT '登录次数',
PRIMARY KEY(`id`)
)engine = INNODB default charset = utf8mb4;

create table `t_goods`(
id bigint(20) not null auto_increment comment '商品ID',
goods_name varchar(16) default null comment '商品名称',
goods_title varchar(64) default null comment '商品标题',
goods_img varchar(64) default null comment '商品图片',
goods_detail longtext comment '商品详情',
goods_price decimal(10, 2) default '0.00' comment '商品价格',
goods_stock int(11) default '0' comment '商品库存, -1表示没有限制',
primary key (id)
)engine = INNODB auto_increment = 3 default charset = utf8mb4;

create table `t_orders`(
id bigint(20) not null auto_increment comment '订单ID',
user_id bigint(20) default null comment '用户ID',
goods_id bigint(20) default null comment '商品ID',
delivery_addr_id bigint(20) default null comment '收货地址ID',
goods_name varchar(16) default null comment '冗余过来的商品名称',
goods_count int(11) default '0' comment '商品数量',
goods_price decimal(10, 2) default '0.00' comment '商品单价',
order_channel tinyint(4) default '0' comment '下单渠道: 1pc, 2android, 3ios',
status tinyint(4) default '0' comment '订单状态: 0新建未支付, 1已支付, 2已发货, 3已收货, 4已退款, 5已完成',
create_date datetime default null comment '创建日期',
pay_date datetime default null comment '支付时间',
primary key (id)
)engine = INNODB auto_increment = 12 default charset = utf8mb4;

create table t_seckill_goods(
id bigint(20) not null auto_increment comment '秒杀商品(主键)ID',
goods_id bigint(20) default null comment '商品ID',
seckill_price decimal(10, 2) default '0.00' comment '秒杀价',
stock_count int(10) default null comment '库存数量',
start_date datetime default null comment '开始时间',
end_date datetime default null comment '结束时间',
primary key (id)
)engine = INNODB auto_increment = 3 default charset = utf8mb4;

create table `t_seckill_orders`(
id bigint(20) not null auto_increment comment '秒杀订单(主键)ID',
user_id bigint(20) default null comment '用户ID',
order_id bigint(20) default null comment '订单ID',
goods_id bigint(20) default null comment '商品ID',
primary key (id)
)engine = INNODB auto_increment = 12 default charset = utf8mb4;

insert into t_goods values
(1, 'IPHONE 15 256GB', 'IPHONE 15 256GB', '/img/iphone15.png', 'IPHONE 15 256GB', '6999.0', 100),
(2, 'MATE60PRO 256GB', 'MATE 60 PRO 256GB', '/img/MATE60PRO.png', 'MATE 60 PRO 256GB', '7999.0', 100),
(3, 'S24 Ultra 256GB', 'S24 Ultra 256GB', '/img/S24Ultra.png', 'S24 Ultra 256GB', '9999.0', 100);

insert into t_seckill_goods values
(1, 1, '629', 10, '2024-01-01 19:00:00', '2024-01-01 20:00:00'),
(2, 2, '729', 10, '2024-01-01 19:00:00', '2024-01-01 20:00:00'),
(3, 3, '829', 10, '2024-01-01 19:00:00', '2024-01-01 20:00:00');
