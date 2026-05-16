package com.govy.model.dto;

import com.govy.model.vo.JudgeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
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
public class CompetitionWithJudgeDTO implements Serializable{

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 评委列表
     */
    @Schema(description = "评委列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> judges;
}