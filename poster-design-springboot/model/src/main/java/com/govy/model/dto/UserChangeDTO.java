package com.govy.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：user
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserChangeDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    /**
     * 账户状态（枚举值）
     */
    @Schema(description = "用户状态: 0-正常 1-禁用 2-未激活",
            example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status; // 默认正常状态


}