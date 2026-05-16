package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 颁奖表
 * @TableName award
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AwardWithCompetitionVO implements Serializable {

    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    @Schema(description = "获奖列表",  requiredMode = Schema.RequiredMode.REQUIRED)
    List<AwardVO> awardList;
}