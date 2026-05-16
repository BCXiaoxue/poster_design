package com.govy.model.dto;

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
public class TeamQueryLikeDTO implements Serializable {

    /**
     * 队名
     */
    @Schema(description = "队名", example = "上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String team;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}