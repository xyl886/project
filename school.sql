-- school 数据库 - 完整表结构
-- 生成于 2026-06-02 11:52

DROP TABLE IF EXISTS `s_banner`;
CREATE TABLE `s_banner`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `img_path`    varchar(1000)   DEFAULT NULL COMMENT '图片路径',
    `sort`        int    NOT NULL DEFAULT '0' COMMENT '排序',
    `remark`      varchar(225)    DEFAULT NULL COMMENT '备注',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用 0否 1是',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime        DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime        DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='首页轮播图';
DROP TABLE IF EXISTS `s_category`;
CREATE TABLE `s_category`
(
    `id`            int                                                          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名字',
    `icon`          varchar(100) DEFAULT NULL COMMENT '图标',
    `parent_id`     int          DEFAULT NULL COMMENT '父分类ID',
    `sort`          int          DEFAULT '0' COMMENT '排序值',
    `deleted`       tinyint(1) DEFAULT '0' COMMENT '逻辑标记 0否 1是',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY             `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_chat_message`;
CREATE TABLE `s_chat_message`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `from_id`     bigint NOT NULL COMMENT '发送者id',
    `to_id`       bigint NOT NULL COMMENT '接收者id',
    `message`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '信息内容',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除0否 1是',
    `create_time` datetime                                                       DEFAULT NULL COMMENT '发送时间',
    `update_time` datetime                                                       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY           `idx_from_id` (`from_id`),
    KEY           `idx_to_id` (`to_id`),
    KEY           `idx_from_to` (`from_id`,`to_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_collect`;
CREATE TABLE `s_collect`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint NOT NULL COMMENT '用户主键',
    `posts_id`    bigint NOT NULL COMMENT '帖子主键',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_posts_id` (`user_id`,`posts_id`) USING BTREE,
    KEY           `user_id` (`user_id`) USING BTREE,
    KEY           `posts_id` (`posts_id`) USING BTREE,
    KEY           `idx_posts_id` (`posts_id`) COMMENT '甯栧瓙鏀惰棌鏌ヨ?绱㈠紩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收藏';
DROP TABLE IF EXISTS `s_comment_like`;
CREATE TABLE `s_comment_like`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint NOT NULL COMMENT '用户id',
    `comment_id`  bigint NOT NULL COMMENT '评论id',
    `deleted`     tinyint(1) DEFAULT '0',
    `status`      tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`user_id`),
    KEY           `idx_comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_follow`;
CREATE TABLE `s_follow`
(
    `id`                  bigint NOT NULL COMMENT '主键',
    `user_id`             bigint NOT NULL COMMENT '用户主键',
    `be_followed_user_id` bigint NOT NULL COMMENT '被关注用户主键',
    `deleted`             tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time`         datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_be_followed_user_id` (`user_id`,`be_followed_user_id`) USING BTREE,
    KEY                   `user_id` (`user_id`) USING BTREE,
    KEY                   `be_followed_user_id` (`be_followed_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注';
DROP TABLE IF EXISTS `s_friend`;
CREATE TABLE `s_friend`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint   DEFAULT NULL COMMENT '用户id',
    `friend_id`   bigint   DEFAULT NULL COMMENT '好友id',
    `status`      tinyint(1) DEFAULT '0' COMMENT '0可私信 1已单向删除',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除0否1是',
    `create_time` datetime DEFAULT NULL COMMENT '添加时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`user_id`),
    KEY           `idx_friend_id` (`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_history`;
CREATE TABLE `s_history`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint NOT NULL COMMENT '浏览用户主键',
    `posts_id`    bigint NOT NULL COMMENT '帖子主键',
    `deleted`     tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT NULL COMMENT '首次浏览时间',
    `update_time` datetime DEFAULT NULL COMMENT '最近浏览时间',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`user_id`),
    KEY           `idx_posts_id` (`posts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_location`;
CREATE TABLE `s_location`
(
    `id`        bigint NOT NULL COMMENT 'id',
    `name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
    `parent_id` bigint                                                        DEFAULT NULL COMMENT '上一级id',
    `code`      bigint                                                        DEFAULT NULL COMMENT '编码',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_message`;
CREATE TABLE `s_message`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint NOT NULL COMMENT '用户主键',
    `content`     varchar(1000) DEFAULT NULL COMMENT '留言内容',
    `deleted`     tinyint(1) DEFAULT '0',
    `create_time` datetime      DEFAULT NULL COMMENT '创建时间',
    `time`        int           DEFAULT '0' COMMENT '展示排序时间戳',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_notification`;
CREATE TABLE `s_notification`
(
    `id`           bigint NOT NULL COMMENT '涓婚敭',
    `user_id`      bigint NOT NULL COMMENT '鎺ユ敹閫氱煡鐨勭敤鎴稩D',
    `from_user_id` bigint       DEFAULT NULL COMMENT '瑙﹀彂閫氱煡鐨勭敤鎴稩D锛堣皝璧炰簡鎴?璇勮?浜嗘垜锛',
    `type`         tinyint(1) NOT NULL COMMENT '閫氱煡绫诲瀷 1鐐硅禐 2璇勮? 3鍥炲? 4鍏虫敞 5绯荤粺',
    `content`      varchar(500) DEFAULT NULL COMMENT '閫氱煡鍐呭?鎽樿?',
    `target_id`    bigint       DEFAULT NULL COMMENT '鍏宠仈鐩?爣ID锛堝笘瀛怚D/璇勮?ID绛夛級',
    `target_type`  tinyint(1) DEFAULT NULL COMMENT '鐩?爣绫诲瀷 1甯栧瓙 2璇勮?',
    `is_read`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '鏄?惁宸茶? 0鏈?? 1宸茶?',
    `deleted`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '閫昏緫鍒犻櫎鏍囪?',
    `create_time`  datetime     DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
    `update_time`  datetime     DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `idx_user_id` (`user_id`) COMMENT '鐢ㄦ埛閫氱煡鏌ヨ?绱㈠紩',
    KEY            `idx_user_id_read` (`user_id`,`is_read`) COMMENT '鐢ㄦ埛鏈??閫氱煡绱㈠紩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='閫氱煡琛';
DROP TABLE IF EXISTS `s_posts`;
CREATE TABLE `s_posts`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键',
    `user_id`     bigint                                                       NOT NULL COMMENT '帖子所属用户主键',
    `category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类ID',
    `posts_type`  tinyint                                                      NOT NULL COMMENT '甯栧瓙绫诲瀷 1闂茬疆 2鏍″洯',
    `price`       decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '单价',
    `title`       varchar(225)                                                 NOT NULL COMMENT '标题',
    `description` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '帖子描述',
    `content`     mediumtext COMMENT '内容',
    `cover_path`  varchar(500)                                                          DEFAULT NULL COMMENT '封面图片',
    `img_path`    varchar(2000)                                                         DEFAULT NULL COMMENT '图片，多张英文逗号分割',
    `browse_num`  int                                                          NOT NULL DEFAULT '0' COMMENT '浏览数量',
    `collect_num` int                                                          NOT NULL DEFAULT '0' COMMENT '收藏数量',
    `like_num`    int                                                          NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `comment_num` int                                                          NOT NULL DEFAULT '0' COMMENT '评论数量',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '鐘舵? 1瀹℃牳涓?2宸插彂甯?3涓嬫灦 4鍥炴敹绔',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_user_id` (`user_id`) COMMENT '鐢ㄦ埛甯栧瓙鏌ヨ?绱㈠紩',
    KEY           `idx_status` (`status`) COMMENT '甯栧瓙鐘舵?绛涢?绱㈠紩',
    KEY           `idx_posts_type` (`posts_type`) COMMENT '甯栧瓙绫诲瀷绛涢?绱㈠紩',
    KEY           `idx_create_time` (`create_time`) COMMENT '甯栧瓙鏃堕棿鎺掑簭绱㈠紩',
    KEY           `idx_user_id_status` (`user_id`,`status`) COMMENT '鐢ㄦ埛甯栧瓙鐘舵?鑱斿悎绱㈠紩',
    KEY           `idx_type_status_time` (`posts_type`,`status`,`create_time`) COMMENT '甯栧瓙鍒楄〃鏌ヨ?鑱斿悎绱㈠紩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子';
DROP TABLE IF EXISTS `s_posts_comment`;
CREATE TABLE `s_posts_comment`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `parent_id`   bigint          DEFAULT NULL COMMENT '上级评论主键',
    `user_id`     bigint NOT NULL COMMENT '用户主键',
    `posts_id`    bigint NOT NULL COMMENT '帖子主键',
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '评论内容',
    `like_num`    int    NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime        DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime        DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_posts_id` (`posts_id`),
    KEY           `idx_parent_id` (`parent_id`) COMMENT '璇勮?鍥炲?鍏宠仈绱㈠紩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子评论';
DROP TABLE IF EXISTS `s_posts_like`;
CREATE TABLE `s_posts_like`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint NOT NULL COMMENT '用户主键',
    `posts_id`    bigint NOT NULL COMMENT '帖子主键',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `user_id_posts_id` (`user_id`,`posts_id`) USING BTREE,
    KEY           `user_id` (`user_id`) USING BTREE,
    KEY           `posts_id` (`posts_id`) USING BTREE,
    KEY           `idx_user_id_posts_id_deleted` (`user_id`,`posts_id`,`deleted`) COMMENT '鐐硅禐鐘舵?鏌ヨ?绱㈠紩'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子点赞';
DROP TABLE IF EXISTS `s_posts_tag`;
CREATE TABLE `s_posts_tag`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `posts_id` bigint DEFAULT NULL,
    `tag_id`   bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_posts_tag` (`posts_id`,`tag_id`) COMMENT '甯栧瓙鏍囩?鍞?竴绾︽潫',
    KEY        `idx_posts_id` (`posts_id`),
    KEY        `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_report`;
CREATE TABLE `s_report`
(
    `id`             bigint NOT NULL COMMENT '主键',
    `posts_id`       bigint        DEFAULT NULL COMMENT '帖子主键',
    `report_user_id` bigint        DEFAULT NULL COMMENT '举报用户主键',
    `content`        varchar(1000) DEFAULT NULL COMMENT '内容',
    `status`         tinyint(1) NOT NULL DEFAULT '0' COMMENT '澶勭悊鐘舵? 0鏈??鐞?1宸插?鐞?2宸查┏鍥',
    `deleted`        tinyint(1) NOT NULL DEFAULT '0' COMMENT '閫昏緫鍒犻櫎鏍囪?',
    `create_time`    datetime      DEFAULT NULL COMMENT '时间',
    `update_time`    datetime      DEFAULT NULL COMMENT '澶勭悊鏃堕棿',
    PRIMARY KEY (`id`),
    KEY              `idx_post_id` (`posts_id`),
    KEY              `idx_report_user_id` (`report_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_tags`;
CREATE TABLE `s_tags`
(
    `id`          bigint       NOT NULL,
    `tag_name`    varchar(100) NOT NULL,
    `deleted`     tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_user_auth`;
CREATE TABLE `s_user_auth`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
    `deleted`     int      DEFAULT '0',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `s_user_info`;
CREATE TABLE `s_user_info`
(
    `id`          bigint                                                       NOT NULL COMMENT '主键',
    `nickname`    varchar(50)                                                  NOT NULL COMMENT '鐢ㄦ埛鍚嶇О',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
    `password`    varchar(255)                                                 NOT NULL COMMENT '登录密码 加密',
    `avatar`      varchar(225)                                                  DEFAULT NULL COMMENT '头像',
    `gender`      tinyint(1) DEFAULT '0' COMMENT '性别 0保密 1男 2女',
    `status`      tinyint(1) DEFAULT '0' COMMENT '是否禁用 0否 1是',
    `hobby`       varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '鐖卞ソ',
    `remark`      varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '澶囨敞',
    `role`        tinyint(1) DEFAULT '0' COMMENT '韬?唤 0娓稿? 1瀛︾敓 2鐗堜富 3绠＄悊鍛',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记 是否已删除: 0否  1是',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息';
