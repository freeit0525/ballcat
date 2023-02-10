package com.hccake.ballcat.system.mapper;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.ballcat.system.converter.SysRoleConverter;
import com.hccake.ballcat.system.model.entity.SysRole;
import com.hccake.ballcat.system.model.qo.SysRoleQO;
import com.hccake.ballcat.system.model.vo.SysRolePageVO;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
public interface SysRoleMapper extends ExtendMapper<SysRole> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		IPage<SysRole> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<SysRole> wrapper = WrappersX.lambdaQueryX(SysRole.class)
				.likeIfPresent(SysRole::getName, qo.getName()).like(SysRole::getCode, qo.getCode())
				.between(CharSequenceUtil.isNotBlank(qo.getStartTime()) && CharSequenceUtil.isNotBlank(qo.getEndTime()),
						SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
		this.selectPage(page, wrapper);
		IPage<SysRolePageVO> voPage = page.convert(SysRoleConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 获取角色下拉框数据
	 * @return 下拉选择框数据集合
	 */
	List<SelectData<Void>> listSelectData();

}
