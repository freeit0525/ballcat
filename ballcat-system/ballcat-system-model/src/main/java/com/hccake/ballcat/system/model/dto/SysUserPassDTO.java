package com.hccake.ballcat.system.model.dto;

import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户密码传输DTO，字段序列化时忽略，防止记录
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Data
@Schema(title = "系统用户密码传输实体")
public class SysUserPassDTO {

	/**
	 * 前端传入密码
	 */
	@NotBlank(message = "The password cannot be empty!")
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@Schema(title = "前端输入密码")
	private String pass;

	/**
	 * 前端确认密码
	 */
	@NotBlank(message = "The confirm password cannot be empty!")
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@Schema(title = "前端确认密码")
	private String confirmPass;

}
