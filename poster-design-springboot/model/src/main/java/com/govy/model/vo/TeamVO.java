package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class TeamVO implements Serializable {

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    /**
     * 队名
     */
    @Schema(description = "队名", example = "上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String team;

    /**
     * 指导老师
     */
    @Schema(description = "指导老师", example = "游龙威", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teacher;

    /**
     * 游龙威
     */
    @Schema(description = "成员列表", requiredMode = Schema.RequiredMode.REQUIRED)
    List<TeamUserVO> teamUserList;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}