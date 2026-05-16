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
public class UserRegisterDTO extends BaseEntity {
    /**
     * 用户名（唯一标识）
     */
    @Schema(description = "用户名(真实姓名)", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    // ↑ Swagger：字段描述、示例值、标记为必填
    @Column(name = "username", length = 50)
    // ↑ JPA：映射到username列，非空，长度限制50
    private String username;

    /**
     * 密码（BCrypt加密存储）
     */
    @Schema(description = "加密后的密码", example = "$2a$10$xVCHq7WZxYgJ.9n5JQzr...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "password")
    private String password;

    /**
     * 手机号（需符合正则校验）
     */
    @Schema(description = "手机号码(唯一)", example = "13800138000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码") // 验证注解：必须匹配手机号正则
    private String phone;

    /**
     * 性别（枚举值）
     */
    @Schema(description = "性别: 0-未知 1-男 2-女", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "gender")
    private Integer gender;


    /**
     * 身份证号（脱敏处理）
     */
    @Schema(description = "身份证号", example = "123456789012345678", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "id_card", length = 18)
    private String idCard;


    /**
     * 学校
     */
    @Schema(description = "学校", example = "上海交通大学", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "school", length = 50)
    private Integer schoolId;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "code", length = 6)
    private String code;
}
