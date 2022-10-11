package com.hccake.ballcat.system.checker;

import com.hccake.ballcat.system.model.entity.SysUser;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
public interface AdminUserChecker {

	/**
	 * 校验用户是否为超级管理员
	 * @param user 用户信息
	 * @return boolean
	 */
	boolean isAdminUser(SysUser user);

	/**
	 * 修改权限校验
	 * @param targetUser 目标用户
	 * @return 是否有权限修改目标用户
	 */
	boolean hasModifyPermission(SysUser targetUser);

}
