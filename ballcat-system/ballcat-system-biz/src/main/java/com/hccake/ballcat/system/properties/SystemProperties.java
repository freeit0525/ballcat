package com.hccake.ballcat.system.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 权限管理系统相关的基础配置
 *
 * @author Hccake 2021/4/23
 * @version 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SystemProperties.PREFIX)
public class SystemProperties {

	public static final String PREFIX = "ballcat.system";

	/**
	 * 超级管理员的配置
	 */
	private Administrator administrator = new Administrator();

	/**
	 * 密码的规则：值为正则表达式，当为空时，不对密码规则进行校验
	 */
	private String passwordRule;

	@Getter
	@Setter
	public static class Administrator {

		/**
		 * 指定id的用户为超级管理员
		 */
		private int userId = 0;

		/**
		 * 指定 username 为超级管理员
		 */
		private String username;

	}

}
