-- 角色表添加字段，用于自定义数据权限
ALTER TABLE sys_role ADD COLUMN `scope_resources` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据范围资源，当数据范围类型为自定义时使用' AFTER `scope_type`;

-- 用户表添加组织机构索引
ALTER TABLE `sys_user` ADD INDEX `idx_organizaiton_id`(`organization_id`);

-- 用户的组织机构不选默认为空，而不是 0
ALTER TABLE `sys_user`
    MODIFY COLUMN `organization_id` int(11) NULL DEFAULT NULL COMMENT '所属组织ID' AFTER `type`;

-- 数据范围默认值
ALTER TABLE `sys_role`
    MODIFY COLUMN `scope_type` tinyint(1) NULL DEFAULT 1 COMMENT '数据范围类型' AFTER `type`;
