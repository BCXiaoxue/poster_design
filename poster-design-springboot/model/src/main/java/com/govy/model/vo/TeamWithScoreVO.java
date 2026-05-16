package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamWithScoreVO {
    @Schema(description = "团队id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    @Schema(description = "评分列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> scoreList;

    @Schema(description = "总分", example = "520", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalScore;

    @Schema(description = "平均分", example = "95.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double avg;
}
