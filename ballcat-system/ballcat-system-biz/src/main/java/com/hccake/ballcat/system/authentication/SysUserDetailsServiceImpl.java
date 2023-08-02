package com.hccake.ballcat.system.authentication;

import com.hccake.ballcat.common.security.constant.UserAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.system.model.dto.UserInfoDTO;
import com.hccake.ballcat.system.model.entity.SysUser;
import com.hccake.ballcat.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Hccake 2019/9/25 20:44
 */
@Slf4j
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {

	private final SysUserService sysUserService;

	private final UserInfoCoordinator userInfoCoordinator;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.getByUsername(username);
		if (sysUser == null) {
			log.error("登录：用户名错误，用户名：{}", username);
			throw new UsernameNotFoundException("username error!");
		}
		UserInfoDTO userInfoDTO = sysUserService.findUserInfo(sysUser);
		return getUserDetailsByUserInfo(userInfoDTO);
	}

	/**
	 * 根据UserInfo 获取 UserDetails
	 * @param userInfoDTO 用户信息DTO
	 * @return UserDetails
	 */
	public UserDetails getUserDetailsByUserInfo(UserInfoDTO userInfoDTO) {

		SysUser sysUser = userInfoDTO.getSysUser();
		Collection<String> roleCodes = userInfoDTO.getRoleCodes();
		Collection<String> permissions = userInfoDTO.getPermissions();

		Collection<String> dbAuthsSet = new HashSet<>();
		if (!CollectionUtils.isEmpty(roleCodes)) {
			// 获取角色
			dbAuthsSet.addAll(roleCodes);
			// 获取资源
			dbAuthsSet.addAll(permissions);

		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 默认将角色和权限放入属性中
		HashMap<String, Object> attributes = new HashMap<>(8);
		attributes.put(UserAttributeNameConstants.ROLE_CODES, roleCodes);
		attributes.put(UserAttributeNameConstants.PERMISSIONS, permissions);

		// 用户额外属性
		userInfoCoordinator.coordinateAttribute(userInfoDTO, attributes);

		return User.builder()
			.userId(sysUser.getUserId())
			.username(sysUser.getUsername())
			.password(sysUser.getPassword())
			.nickname(sysUser.getNickname())
			.avatar(sysUser.getAvatar())
			.status(sysUser.getStatus())
			.organizationId(sysUser.getOrganizationId())
			.email(sysUser.getEmail())
			.phoneNumber(sysUser.getPhoneNumber())
			.gender(sysUser.getGender())
			.type(sysUser.getType())
			.authorities(authorities)
			.attributes(attributes)
			.build();
	}

}
