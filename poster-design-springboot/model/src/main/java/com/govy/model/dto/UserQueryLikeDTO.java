package com.govy.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserQueryLikeDTO implements Serializable {
    /**
     * 用户名（唯一标识）
     */
    @Schema(description = "用户名(真实姓名)", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    /**
     * 权限
     */
    @Schema(description = "权限 0-普通用户 1-管理员", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer authority = 0;

    @Schema(description = "用户状态: 0-正常 1-禁用",
            example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;
}