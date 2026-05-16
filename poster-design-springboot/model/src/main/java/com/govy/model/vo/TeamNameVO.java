package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName team
 */

@Schema(description = "队伍表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamNameVO implements Serializable {

    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    /**
     * 队名
     */
    @Schema(description = "队名", example = "上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}