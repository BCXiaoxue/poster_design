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
public class TeamUserVO implements Serializable {

    /**
     * 用户id
     */
    @Schema(description = "用户id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer userId;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "http:13456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    /**
     * 成员类型
     */
    @Schema(description = "成员类型 0-队长 1-队员", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 大学名
     */
    @Schema(description = "大学名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String university;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}