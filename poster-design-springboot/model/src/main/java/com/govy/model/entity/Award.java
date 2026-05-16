package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 颁奖表
 * @TableName award
 */
@TableName(value ="award")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "颁奖表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Award extends BaseEntity implements Serializable {

    /**
     * 队伍id（颁奖与队伍相关联）
     */
    @Schema(description = "队伍id（颁奖与队伍相关联）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 奖状路径
     */
    @Schema(description = "奖状路径", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String awardUrl;

    /**
     * 几等奖
     */
    @Schema(description = "几等奖", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer awardRank;

    /**
     * 平均分
     */
    @Schema(description = "平均分", example = "95.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double avgScore;
}