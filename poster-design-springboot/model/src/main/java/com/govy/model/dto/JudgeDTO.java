package com.govy.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Govy
 * @TableName judge
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JudgeDTO implements Serializable{

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 团队id
     */
    @Schema(description = "团队id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 分数
     */
    @Schema(description = "分数", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer score;

    /**
     * 评语
     */
    @Schema(description = "评语", example = "这是一个评语", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;
}