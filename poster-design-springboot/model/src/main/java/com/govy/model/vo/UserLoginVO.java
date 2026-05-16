package com.govy.model.vo;

import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录返回信息
 */
//@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Data // Lombok注解：自动生成getter/setter/toString等方法
@Schema(description = "登录返回信息") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO implements Serializable {
    @Schema(description = "jwt令牌")
    private String jwt;

    @Schema(description = "用户权限 0-普通用户 1-管理员")
    private Integer authority;
}
