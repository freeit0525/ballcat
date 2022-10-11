package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.AccessTokenResponseEnhancer;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 * 密码模式支持
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class PasswordAuthorizationServerConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final AuthenticationManager authenticationManager;

	private final OAuth2AuthorizationService authorizationService;

	private final AccessTokenResponseEnhancer accessTokenResponseEnhancer;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) {
		// 添加 resource owner password 模式支持
		oAuth2AuthorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
			OAuth2ResourceOwnerPasswordAuthenticationProvider authenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
					authenticationManager, authorizationService, OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity),
					accessTokenResponseEnhancer);
			tokenEndpoint.authenticationProvider(authenticationProvider);
			tokenEndpoint.accessTokenRequestConverter(new OAuth2ResourceOwnerPasswordAuthenticationConverter());
		});

	}

}
