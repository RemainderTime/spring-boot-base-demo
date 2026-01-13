
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID（sys_menu）',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称（sys_menu.name）',
                             `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端路由路径（sys_menu.path）',
                             `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID（sys_menu.parent_id，树结构）',
                             `type` tinyint(4) NOT NULL COMMENT '菜单类型（sys_menu.type），0=目录,1=菜单,2=按钮',
                             `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标（sys_menu.icon）',
                             `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_menu.create_time）',
                             `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_menu.create_by）',
                             `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_menu.update_time）',
                             `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_menu.update_by）',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表 sys_menu' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '用户菜单', '/admin/user', 0, 1, NULL, '2025-08-17 13:51:17', NULL, '2025-08-17 13:51:17', NULL);
INSERT INTO `sys_menu` VALUES (2, '订单管理', '/admin/order', 0, 1, NULL, '2025-08-18 14:45:15', NULL, '2025-08-18 14:45:15', NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID（sys_permission）',
                                   `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识（sys_permission.code），如 user:add、order:delete',
                                   `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称（sys_permission.name）',
                                   `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '所属菜单ID（sys_permission.menu_id）',
                                   `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_permission.create_time）',
                                   `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_permission.create_by）',
                                   `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_permission.update_time）',
                                   `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_permission.update_by）',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `fk_permission_menu`(`menu_id`) USING BTREE,
                                   CONSTRAINT `fk_permission_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统权限表 sys_permission' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 'user:list', '用户列表', 1, '2025-08-18 14:41:04', NULL, '2025-08-18 14:41:04', NULL);
INSERT INTO `sys_permission` VALUES (2, 'user:add', '添加用户', 1, '2025-08-18 14:43:26', NULL, '2025-08-18 14:43:26', NULL);
INSERT INTO `sys_permission` VALUES (3, 'user:delete', '删除用户', 1, '2025-08-18 14:43:50', NULL, '2025-08-18 14:43:50', NULL);
INSERT INTO `sys_permission` VALUES (4, 'order:list', '订单列表', 2, '2025-08-18 14:46:23', NULL, '2025-08-18 14:46:26', NULL);
INSERT INTO `sys_permission` VALUES (5, 'user:info', '用户信息', 1, '2025-08-21 14:13:18', NULL, '2025-08-21 14:13:23', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID（sys_role）',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称（sys_role.name）',
                             `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识（sys_role.code），如 ADMIN、OPERATOR',
                             `status` tinyint(4) NULL DEFAULT 1 COMMENT '角色状态（sys_role.status），1=启用，0=禁用',
                             `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_role.create_time）',
                             `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_role.create_by）',
                             `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_role.update_time）',
                             `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_role.update_by）',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表 sys_role' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', 1, '2025-08-18 14:41:37', NULL, '2025-08-18 14:41:37', NULL);
INSERT INTO `sys_role` VALUES (2, '助理', 'salve', 1, '2025-08-18 14:44:14', NULL, '2025-08-18 14:44:14', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `role_id` bigint(20) NOT NULL COMMENT '角色ID（sys_role_menu.role_id）',
                                  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID（sys_role_menu.menu_id）',
                                  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_role_menu.create_time）',
                                  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_role_menu.create_by）',
                                  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_role_menu.update_time）',
                                  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_role_menu.update_by）',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表 sys_role_menu' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, '2025-08-18 14:42:17', NULL, '2025-08-18 14:42:17', NULL);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, '2025-08-18 14:46:41', NULL, '2025-08-18 14:46:41', NULL);
INSERT INTO `sys_role_menu` VALUES (3, 2, 1, '2025-08-18 14:46:47', NULL, '2025-08-18 14:46:47', NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                        `role_id` bigint(20) NOT NULL COMMENT '角色ID（sys_role_permission.role_id）',
                                        `permission_id` bigint(20) NOT NULL COMMENT '权限ID（sys_role_permission.permission_id）',
                                        `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_role_permission.create_time）',
                                        `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_role_permission.create_by）',
                                        `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_role_permission.update_time）',
                                        `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_role_permission.update_by）',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表 sys_role_permission' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1, '2025-08-18 14:42:27', NULL, '2025-08-18 14:42:27', NULL);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2, '2025-08-18 14:46:57', NULL, '2025-08-18 14:46:57', NULL);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3, '2025-08-18 14:47:04', NULL, '2025-08-18 14:47:04', NULL);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4, '2025-08-18 14:47:12', NULL, '2025-08-18 14:47:12', NULL);
INSERT INTO `sys_role_permission` VALUES (5, 2, 1, '2025-08-18 14:47:19', NULL, '2025-08-18 14:47:19', NULL);
INSERT INTO `sys_role_permission` VALUES (6, 1, 5, '2025-08-24 12:36:08', NULL, '2025-08-24 12:36:08', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `user_id` bigint(20) NOT NULL COMMENT '用户ID（sys_user_role.user_id）',
                                  `role_id` bigint(20) NOT NULL COMMENT '角色ID（sys_user_role.role_id）',
                                  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（sys_user_role.create_time）',
                                  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人（sys_user_role.create_by）',
                                  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间（sys_user_role.update_time）',
                                  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人（sys_user_role.update_by）',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表 sys_user_role' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2025-08-18 14:42:44', NULL, '2025-08-18 14:42:44', NULL);
INSERT INTO `sys_user_role` VALUES (2, 2, 2, '2025-08-18 14:48:05', NULL, '2025-08-18 14:48:05', NULL);

-- ----------------------------
-- Table structure for xf_user
-- ----------------------------
DROP TABLE IF EXISTS `xf_user`;
CREATE TABLE `xf_user`  (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xf_user
-- ----------------------------
INSERT INTO `xf_user` VALUES (1, '又菜又爱玩', 'admin', '123456', '19999999999', '2024-12-11 14:29:34');
INSERT INTO `xf_user` VALUES (2, '有点东西', 'xx', '111111', '10101100', '2024-12-11 08:58:07');

SET FOREIGN_KEY_CHECKS = 1;
