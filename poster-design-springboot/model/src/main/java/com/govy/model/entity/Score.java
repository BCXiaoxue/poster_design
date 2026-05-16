package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName score
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "评分表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="score")
@Data
public class Score extends BaseEntity implements Serializable {

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 评委id
     */
    @Schema(description = "评委id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer judgeId;

    /**
     * 评分
     */
    @Schema(description = "评分", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer score;

    /**
     * 评语
     */
    @Schema(description = "评语", example = "这是一个评语", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id")
    private Integer competitionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}