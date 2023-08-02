package com.hccake.ballcat.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.system.model.dto.OrganizationMoveChildParam;
import com.hccake.ballcat.system.model.entity.SysOrganization;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
public interface SysOrganizationMapper extends ExtendMapper<SysOrganization> {

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	default List<SysOrganization> listSubOrganization(Long organizationId) {
		LambdaQueryWrapper<SysOrganization> wrapper = Wrappers.<SysOrganization>lambdaQuery()
			.eq(SysOrganization::getParentId, organizationId);
		return this.selectList(wrapper);
	}

	/**
	 * 跟随父节点移动子节点
	 * @param param OrganizationMoveChildParam 跟随移动子节点的参数对象
	 */
	void followMoveChildNode(@Param("param") OrganizationMoveChildParam param);

	/**
	 * 根据组织机构Id，查询该组织下的所有子部门
	 * @param organizationId 组织机构ID
	 * @return 子部门集合
	 */
	List<SysOrganization> listChildOrganization(@Param("organizationId") Long organizationId);

	/**
	 * 批量更新节点层级和深度
	 * @param depth 深度
	 * @param hierarchy 层级
	 * @param organizationIds 组织id集合
	 */
	default void updateHierarchyAndPathBatch(int depth, String hierarchy, List<Long> organizationIds) {
		LambdaUpdateWrapper<SysOrganization> wrapper = Wrappers.lambdaUpdate(SysOrganization.class)
			.set(SysOrganization::getDepth, depth)
			.set(SysOrganization::getHierarchy, hierarchy)
			.in(SysOrganization::getId, organizationIds);
		this.update(null, wrapper);

	}

	/**
	 * 检查指定机构下是否存在子机构
	 * @param organizationId 机构id
	 * @return 存在返回 true
	 */
	@Nullable
	Boolean existsChildOrganization(Long organizationId);

}