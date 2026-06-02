-- =====================================================
-- 数据库优化迁移脚本
-- 基于实际数据库 school 的审查结果
-- 执行前请先备份数据库
-- =====================================================

-- =====================================================
-- 1. 修复 s_user_info 表
-- =====================================================

-- 1.1 nickname 长度 10→50（用户昵称太短）
ALTER TABLE s_user_info MODIFY COLUMN `nickname` varchar(50) NOT NULL COMMENT '用户名称';

-- 1.2 修复唯一键命名（phone→uk_email）
ALTER TABLE s_user_info DROP INDEX `phone`;
ALTER TABLE s_user_info ADD UNIQUE KEY `uk_email` (`email`) USING BTREE;

-- 1.3 修复 role 注释（与实际代码一致：0游客 1学生 2版主 3管理员）
ALTER TABLE s_user_info MODIFY COLUMN `role` tinyint(1) DEFAULT '0' COMMENT '身份 0游客 1学生 2版主 3管理员';

-- 1.4 清理 hobby/remark 默认值（不需要在数据库层设置中文默认值）
ALTER TABLE s_user_info MODIFY COLUMN `hobby` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '爱好';
ALTER TABLE s_user_info MODIFY COLUMN `remark` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注';


-- =====================================================
-- 2. 修复 s_posts 表
-- =====================================================

-- 2.1 添加缺失的查询索引（优化首页查询性能）
ALTER TABLE s_posts ADD INDEX idx_user_id (`user_id`) COMMENT '用户帖子查询索引';
ALTER TABLE s_posts ADD INDEX idx_status (`status`) COMMENT '帖子状态筛选索引';
ALTER TABLE s_posts ADD INDEX idx_posts_type (`posts_type`) COMMENT '帖子类型筛选索引';
ALTER TABLE s_posts ADD INDEX idx_create_time (`create_time`) COMMENT '帖子时间排序索引';
-- 联合索引：按用户+状态查询（个人中心我的帖子列表）
ALTER TABLE s_posts ADD INDEX idx_user_id_status (`user_id`, `status`) COMMENT '用户帖子状态联合索引';
-- 联合索引：按类型+状态+时间（首页列表查询）
ALTER TABLE s_posts ADD INDEX idx_type_status_time (`posts_type`, `status`, `create_time`) COMMENT '帖子列表查询联合索引';


-- =====================================================
-- 3. 修复 s_posts_comment 表
-- =====================================================

-- 3.1 添加 parent_id 索引（支持评论回复树查询）
ALTER TABLE s_posts_comment ADD INDEX idx_parent_id (`parent_id`) COMMENT '评论回复关联索引';


-- =====================================================
-- 4. 修复 s_posts_tag 表
-- =====================================================

-- 4.1 添加唯一约束，防止重复标签关联
ALTER TABLE s_posts_tag ADD UNIQUE KEY `uk_posts_tag` (`posts_id`, `tag_id`) COMMENT '帖子标签唯一约束';


-- =====================================================
-- 5. 修复 s_report 表
-- =====================================================

-- 5.1 添加缺失的字段
ALTER TABLE s_report ADD COLUMN `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '处理状态 0未处理 1已处理 2已驳回' AFTER `content`;
ALTER TABLE s_report ADD COLUMN `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记' AFTER `status`;
ALTER TABLE s_report ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '处理时间' AFTER `create_time`;


-- =====================================================
-- 6. 修复 s_posts_like 表
-- =====================================================

-- 6.1 添加 deleted+user_id+posts_id 联合索引
ALTER TABLE s_posts_like ADD INDEX idx_user_id_posts_id_deleted (`user_id`, `posts_id`, `deleted`) COMMENT '点赞状态查询索引';


-- =====================================================
-- 7. 修复 s_collect 表
-- =====================================================

-- 7.1 添加索引
ALTER TABLE s_collect ADD INDEX idx_posts_id (`posts_id`) COMMENT '帖子收藏查询索引';


-- =====================================================
-- 8. 新建 s_notification 通知表（扩展功能）
-- =====================================================

CREATE TABLE IF NOT EXISTS `s_notification` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '接收通知的用户ID',
  `from_user_id` bigint DEFAULT NULL COMMENT '触发通知的用户ID（谁赞了我/评论了我）',
  `type` tinyint(1) NOT NULL COMMENT '通知类型 1点赞 2评论 3回复 4关注 5系统',
  `content` varchar(500) DEFAULT NULL COMMENT '通知内容摘要',
  `target_id` bigint DEFAULT NULL COMMENT '关联目标ID（帖子ID/评论ID等）',
  `target_type` tinyint(1) DEFAULT NULL COMMENT '目标类型 1帖子 2评论',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读 0未读 1已读',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) COMMENT '用户通知查询索引',
  KEY `idx_user_id_read` (`user_id`, `is_read`) COMMENT '用户未读通知索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表';


-- =====================================================
-- 9. 修复 s_message 表注释（复合主键已正确，增加索引）
-- =====================================================

ALTER TABLE s_message MODIFY COLUMN `time` int DEFAULT '0' COMMENT '展示排序时间戳';


-- =====================================================
-- 10. 修复 s_posts 字段注释
-- =====================================================

ALTER TABLE s_posts MODIFY COLUMN `school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类ID（关联s_category）';
ALTER TABLE s_posts MODIFY COLUMN `posts_type` tinyint NOT NULL COMMENT '帖子类型 1闲置 2校园';
ALTER TABLE s_posts MODIFY COLUMN `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1审核中 2已发布 3下架 4回收站';


-- =====================================================
-- 验证脚本
-- =====================================================

SELECT '✅ 迁移脚本执行完成' AS result;
SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='school' AND TABLE_NAME='s_user_info' AND COLUMN_NAME='nickname';
