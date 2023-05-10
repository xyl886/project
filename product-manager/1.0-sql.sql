-- 2022-10-19 用户信息
CREATE TABLE `user_info`
(
    `id` bigint NOT NULL COMMENT '主键',
    `nickname` varchar(10) NOT NULL COMMENT '用户名称',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `password` varchar(255) NOT NULL COMMENT '登录密码 加密',
    `original_password` varchar(255) NOT NULL COMMENT '登录密码 明文密码',
    `avatar` varchar(225) DEFAULT NULL COMMENT '头像',
    `gender`  TINYINT(1) DEFAULT 0 COMMENT '性别 0保密 1男 2女',
    `status`  TINYINT(1) DEFAULT 0 COMMENT '是否禁用 0否 1是',
    `hobby` varchar(225) DEFAULT NULL COMMENT '爱好',
    `remark` varchar(225) DEFAULT NULL COMMENT '备注',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息';

-- 首页轮播图
  CREATE TABLE `banner`
(
    `id` bigint NOT NULL COMMENT '主键',
    `img_path` varchar(500) DEFAULT NULL COMMENT '图片路径',
    `sort`  INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `remark` varchar(225) DEFAULT NULL COMMENT '备注',
    `status`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 0否 1是',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='首页轮播图';


-- 用户关注
  CREATE TABLE `follow`
(
    `id` bigint NOT NULL COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户主键',
    `be_followed_user_id` bigint NOT NULL COMMENT '被关注用户主键',
    `status` tinyint(1) DEFAULT 0 COMMENT '是否已读 0否  1是',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_be_followed_user_id` (`user_id`,`be_followed_user_id`) USING BTREE,
    KEY `user_id` (`user_id`) USING BTREE,
    KEY `be_followed_user_id` (`be_followed_user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户关注';

-- 帖子
  CREATE TABLE `posts`
(
    `id` bigint NOT NULL COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '帖子所属用户主键',
    `posts_type`  TINYINT(1) NOT NULL COMMENT '帖子类型 1闲置帖 2校园帖',
    `title` varchar(225) NOT NULL COMMENT '标题',
    `content` varchar(1000) DEFAULT NULL COMMENT '内容',
    `school`  TINYINT(1) NOT NULL COMMENT '校区 1官塘校区 2社湾校区',
    `price` decimal(10, 2) NOT NULL DEFAULT 0 COMMENT '单价',
    `cover_path` varchar(500) DEFAULT NULL COMMENT '封面图片',
    `img_path` varchar(2000) DEFAULT NULL COMMENT '图片，多张英文逗号分割',
    `browse_num` int(11) NOT NULL DEFAULT 0 COMMENT '浏览数量',
    `collect_num` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数量',
    `like_num` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数量',
    `comment_num` int(11) NOT NULL DEFAULT 0 COMMENT '评论数量',
    `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    `status`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 0否 1是',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='帖子';


  -- 用户收藏
  CREATE TABLE `collect`
(
    `id` bigint NOT NULL COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户主键',
    `posts_id` bigint NOT NULL COMMENT '帖子主键',
    `posts_user_id` bigint NOT NULL COMMENT '帖子所属用户主键',
    `status` tinyint(1) DEFAULT 0 COMMENT '是否已读 0否  1是',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_posts_id` (`user_id`,`posts_id`) USING BTREE,
    KEY `user_id` (`user_id`) USING BTREE,
    KEY `posts_id` (`posts_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收藏';

  -- 帖子点赞
  CREATE TABLE `posts_like`
(
    `id` bigint NOT NULL COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户主键',
    `posts_id` bigint NOT NULL COMMENT '帖子主键',
    `posts_user_id` bigint NOT NULL COMMENT '帖子所属用户主键',
    `status` tinyint(1) DEFAULT 0 COMMENT '是否已读 0否  1是',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_posts_id` (`user_id`,`posts_id`) USING BTREE,
    KEY `user_id` (`user_id`) USING BTREE,
    KEY `posts_id` (`posts_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='帖子点赞';

   -- 帖子评论
  CREATE TABLE `posts_comment`
(
    `id` bigint NOT NULL COMMENT '主键',
    `parent_id` bigint DEFAULT NULL COMMENT '上级评论主键',
    `parent_user_id` bigint DEFAULT NULL COMMENT '上级评论用户主键',
    `user_id` bigint NOT NULL COMMENT '用户主键',
    `posts_id` bigint NOT NULL COMMENT '帖子主键',
    `posts_user_id` bigint NOT NULL COMMENT '帖子所属用户主键',
    `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
    `status` tinyint(1) DEFAULT 0 COMMENT '是否已读 0否  1是',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime(0) COMMENT '创建时间',
    `update_time` datetime(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='帖子评论';



