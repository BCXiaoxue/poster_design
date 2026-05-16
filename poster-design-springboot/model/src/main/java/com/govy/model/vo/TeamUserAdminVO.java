package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName team
 */

@Schema(description = "队伍表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamUserAdminVO implements Serializable {

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "http:13456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 用户角色
     */
    @Schema(description = "用户角色", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}