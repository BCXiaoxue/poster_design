package com.govy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Data // Lombok注解：自动生成getter/setter/toString等方法
@AllArgsConstructor
@NoArgsConstructor
public class UserVO extends BaseEntity {

    /**
     * 用户名（唯一标识）
     */
    @Schema(description = "用户名(真实姓名)", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码（BCrypt加密存储）
     */
    @Schema(description = "加密后的密码", example = "$2a$10$xVCHq7WZxYgJ.9n5JQzr...",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonIgnore
    private String password;

    /**
     * 电子邮箱（需符合格式校验）
     */
    @Schema(description = "电子邮箱(唯一)", example = "user@example.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "email", length = 100)
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "请输入有效的电子邮件地址") // 验证注解：必须符合邮箱格式
    private String email;

    /**
     * 手机号（需符合正则校验）
     */
    @Schema(description = "手机号码(唯一)", example = "13800138000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码") // 验证注解：必须匹配手机号正则
    private String phone;

    /**
     * 账户状态（枚举值）
     */
    @Schema(description = "用户状态: 0-正常 1-禁用 2-未激活",
            example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "status", nullable = false)
    private Integer status = 0; // 默认正常状态

    /**
     * 最后登录时间（自动记录）
     */
    @Schema(description = "最后登录时间", example = "2023-08-15T10:30:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "last_login")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLogin;

    /**
     * 头像网络地址
     */
    @Schema(description = "头像URL地址", example = "https://example.com/avatar.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "avatar_url", length = 255) // 长度255兼容URL长链接
    private String avatarUrl;

    /**
     * 性别（枚举值）
     */
    @Schema(description = "性别: 0-未知 1-男 2-女", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "gender")
    private Integer gender;

    /**
     * 出生日期（ISO格式）
     */
    @Schema(description = "出生日期", example = "1990-01-01",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "birthday")
    private LocalDate birthday; // 使用LocalDate仅存储日期部分


    /**
     * 身份证号（脱敏处理）
     */
    @Schema(description = "身份证号", example = "123456789012345678", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "id_card", length = 18)
    @JsonIgnore
    private String idCard;

    /**
     * 权限
     */
    @Schema(description = "权限 0-普通用户 1-管理员", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "0")
    private Integer authority = 0;


    /**
     * 学校
     */
    @Schema(description = "学校", example = "上海交通大学", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "school", length = 50)
    private Integer schoolId;

    @Schema(description = "大学", example = "上海交通大学", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name = "university", length = 50)
    private String university;
}