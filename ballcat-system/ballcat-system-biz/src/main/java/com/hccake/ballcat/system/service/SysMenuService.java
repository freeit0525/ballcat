package com.hccake.ballcat.system.service;

import com.hccake.ballcat.system.model.dto.SysMenuCreateDTO;
import com.hccake.ballcat.system.model.dto.SysMenuUpdateDTO;
import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.qo.SysMenuQO;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
public interface SysMenuService extends ExtendService<SysMenu> {

	/**
	 * 更新菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 */
	void update(SysMenuUpdateDTO sysMenuUpdateDTO);

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO);

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	List<SysMenu> listByRoleCode(String roleCode);

	/**
	 * 新建菜单权限
	 * @param sysMenuCreateDTO 菜单全新新建传输对象
	 * @return 新建成功返回 true
	 */
	boolean create(SysMenuCreateDTO sysMenuCreateDTO);

}