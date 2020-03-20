package com.hccake.ballcat.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 12:19
 */
@Getter
@AllArgsConstructor
public enum BaseResultMsg implements ResultMsg{
    /**
     * 数据库保存/更新异常
     */
    UPDATE_DATABASE_ERROR(90001, "Update Database Error"),

    /**
     * 通用的逻辑校验异常
     */
    LOGIC_CHECK_ERROR(90004, "Logic Check Error"),

    /**
     * 恶意请求
     */
    MALICIOUS_REQUEST(90005, "Malicious Request"),

    /**
     * 文件上传异常
     */
    FILE_UPLOAD_ERROR(90006, "File Upload Error"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(99999, "Unknown Error");

    private Integer code;
    private String message;
}
