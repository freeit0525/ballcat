package com.hccake.ballcat.admin.upms;

import com.hccake.ballcat.admin.upms.log.LogConfiguration;
import com.hccake.ballcat.system.authentication.BallcatOAuth2TokenResponseEnhancer;
import com.hccake.ballcat.system.authentication.DefaultUserInfoCoordinatorImpl;
import com.hccake.ballcat.system.authentication.SysUserDetailsServiceImpl;
import com.hccake.ballcat.system.authentication.UserInfoCoordinator;
import com.hccake.ballcat.system.properties.SystemProperties;
import com.hccake.ballcat.system.service.SysUserService;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenResponseEnhancer;
import org.ballcat.springsecurity.oauth2.server.resource.introspection.SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author Hccake 2020/5/25 21:01
 */
@EnableAsync
@AutoConfiguration
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin.upms", "com.hccake.ballcat.system", "com.hccake.ballcat.log",
		"com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@EnableConfigurationProperties({ SystemProperties.class, SecurityProperties.class })
@Import(LogConfiguration.class)
public class UpmsAutoConfiguration {

	/**
	 * 用户详情处理类
	 *
	 * @author hccake
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(SysUserService.class)
	@ConditionalOnMissingBean(UserDetailsService.class)
	static class UserDetailsServiceConfiguration {

		/**
		 * 用户详情处理类
		 * @return SysUserDetailsServiceImpl
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserDetailsService userDetailsService(SysUserService sysUserService,
				UserInfoCoordinator userInfoCoordinator) {
			return new SysUserDetailsServiceImpl(sysUserService, userInfoCoordinator);
		}

		/**
		 * 用户信息协调者
		 * @return UserInfoCoordinator
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserInfoCoordinator userInfoCoordinator() {
			return new DefaultUserInfoCoordinatorImpl();
		}

	}

	/**
	 * 新版本 spring-security-oauth2-authorization-server 使用配置类
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(OAuth2Authorization.class)
	static class SpringOAuth2AuthorizationServerConfiguration {

		/**
		 * token 端点响应增强，追加一些自定义信息
		 * @return TokenEnhancer Token增强处理器
		 */
		@Bean
		@ConditionalOnMissingBean
		public OAuth2TokenResponseEnhancer oAuth2TokenResponseEnhancer() {
			return new BallcatOAuth2TokenResponseEnhancer();
		}

		/**
		 * 当资源服务器和授权服务器的 token 共享存储时，直接使用 OAuth2AuthorizationService 读取 token 信息
		 * @return SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector
		 */
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = "ballcat.security.oauth2.resourceserver", name = "shared-stored-token",
				havingValue = "true", matchIfMissing = true)
		public OpaqueTokenIntrospector sharedStoredOpaqueTokenIntrospector(
				OAuth2AuthorizationService authorizationService) {
			return new SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(authorizationService);
		}

	}

}
