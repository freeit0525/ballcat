package com.hccake.ballcat.system.controller;

import cn.hutool.core.collection.CollUtil;
import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.security.constant.UserAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.system.converter.SysMenuConverter;
import com.hccake.ballcat.system.enums.SysMenuType;
import com.hccake.ballcat.system.model.dto.SysMenuCreateDTO;
import com.hccake.ballcat.system.model.dto.SysMenuUpdateDTO;
import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.qo.SysMenuQO;
import com.hccake.ballcat.system.model.vo.SysMenuGrantVO;
import com.hccake.ballcat.system.model.vo.SysMenuPageVO;
import com.hccake.ballcat.system.model.vo.SysMenuRouterVO;
import com.hccake.ballcat.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
@Tag(name = "菜单权限管理")
public class SysMenuController {

	private final SysMenuService sysMenuService;

	/**
	 * 返回当前用户的路由集合
	 * @return 当前用户的路由
	 */
	@GetMapping("/router")
	@Operation(summary = "动态路由", description = "动态路由")
	public R<List<SysMenuRouterVO>> getUserPermission() {
		// 获取角色Code
		User user = SecurityUtils.getUser();
		Map<String, Object> attributes = user.getAttributes();

		Object rolesObject = attributes.get(UserAttributeNameConstants.ROLE_CODES);
		if (!(rolesObject instanceof Collection)) {
			return R.ok(new ArrayList<>());
		}

		@SuppressWarnings("unchecked")
		Collection<String> roleCodes = (Collection<String>) rolesObject;
		if (CollUtil.isEmpty(roleCodes)) {
			return R.ok(new ArrayList<>());
		}

		// 获取符合条件的权限
		Set<SysMenu> all = new HashSet<>();
		roleCodes.forEach(roleCode -> all.addAll(sysMenuService.listByRoleCode(roleCode)));

		// 筛选出菜单
		List<SysMenuRouterVO> menuVOList = all.stream()
			.filter(menuVo -> SysMenuType.BUTTON.getValue() != menuVo.getType())
			.sorted(Comparator.comparingInt(SysMenu::getSort))
			.map(SysMenuConverter.INSTANCE::poToRouterVo)
			.collect(Collectors.toList());

		return R.ok(menuVOList);
	}

	/**
	 * 查询菜单列表
	 * @param sysMenuQO 菜单权限查询对象
	 * @return R 通用返回体
	 */
	@GetMapping("/list")
	@PreAuthorize("@per.hasPermission('system:menu:read')")
	@Operation(summary = "查询菜单列表", description = "查询菜单列表")
	public R<List<SysMenuPageVO>> getSysMenuPage(SysMenuQO sysMenuQO) {
		List<SysMenu> sysMenus = sysMenuService.listOrderBySort(sysMenuQO);
		if (CollUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuPageVO> voList = sysMenus.stream()
			.map(SysMenuConverter.INSTANCE::poToPageVo)
			.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 查询授权菜单列表
	 * @return R 通用返回体
	 */
	@GetMapping("/grant-list")
	@PreAuthorize("@per.hasPermission('system:menu:read')")
	@Operation(summary = "查询授权菜单列表", description = "查询授权菜单列表")
	public R<List<SysMenuGrantVO>> getSysMenuGrantList() {
		List<SysMenu> sysMenus = sysMenuService.list();
		if (CollUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuGrantVO> voList = sysMenus.stream()
			.map(SysMenuConverter.INSTANCE::poToGrantVo)
			.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 新增菜单权限
	 * @param sysMenuCreateDTO 菜单权限
	 * @return R 通用返回体
	 */
	@CreateOperationLogging(msg = "新增菜单权限")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:menu:add')")
	@Operation(summary = "新增菜单权限", description = "新增菜单权限")
	public R<Void> save(@Valid @RequestBody SysMenuCreateDTO sysMenuCreateDTO) {
		return sysMenuService.create(sysMenuCreateDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增菜单权限失败");
	}

	/**
	 * 修改菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 * @return R 通用返回体
	 */
	@UpdateOperationLogging(msg = "修改菜单权限")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:menu:edit')")
	@Operation(summary = "修改菜单权限", description = "修改菜单权限")
	public R<Void> updateById(@RequestBody SysMenuUpdateDTO sysMenuUpdateDTO) {
		sysMenuService.update(sysMenuUpdateDTO);
		return R.ok();
	}

	/**
	 * 通过id删除菜单权限
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteOperationLogging(msg = "通过id删除菜单权限")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:menu:del')")
	@Operation(summary = "通过id删除菜单权限", description = "通过id删除菜单权限")
	public R<Void> removeById(@PathVariable("id") Long id) {
		return sysMenuService.removeById(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除菜单权限失败");
	}

}