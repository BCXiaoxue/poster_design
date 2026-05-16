package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName team
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Schema(description = "队伍表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="team")
@Data
public class Team extends BaseEntity implements Serializable {

    /**
     * 队名
     */
    @Schema(description = "队名", example = "上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String team;

    @Schema(description = "指导老师", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teacherId;

    @Schema(description = "团队状态（0可操作、1锁定）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}