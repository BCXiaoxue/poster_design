package com.govy.model.dto;

import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户注册参数
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Data // Lombok注解：自动生成getter/setter/toString等方法
@Schema(description = "用户注册参数") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO extends BaseEntity {

    /**
     * 密码（BCrypt加密存储）
     */
    @Schema(description = "原密码", example = "$2a$10$xVCHq7WZxYgJ.9n5JQzr...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "password", length = 255)
    private String password;

    /**
     * 新密码（BCrypt加密存储）
     */
    @Schema(description = "新密码", example = "a0$xVCHq7WZxYgJ.9n5JQzr...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "new_password", length = 255)
    private String newPassword;

    /**
     * 手机号（需符合正则校验）
     */
    @Schema(description = "手机号码(唯一)", example = "13800138000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码") // 验证注解：必须匹配手机号正则
    private String phone;

    /**
     * 手机号（需符合正则校验）
     */
    @Schema(description = "手机号码(唯一)", example = "13800138000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码") // 验证注解：必须匹配手机号正则
    private String newPhone;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "code", length = 6)
    private String code;
}
