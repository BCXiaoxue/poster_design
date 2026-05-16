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
 * Govy
 * @TableName judge
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionWithJudgeVO  implements Serializable{

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "我是你爹", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;


    /**
     * 评委列表
     */
    @Schema(description = "评委列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<JudgeVO> judges;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}