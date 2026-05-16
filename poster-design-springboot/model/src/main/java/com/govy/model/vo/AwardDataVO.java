package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * 颁奖表
 * @TableName award
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AwardDataVO implements Serializable {
    @Schema(description = "比赛状态", example = "未开始 - 报名中 - 评审中 - 已结束", requiredMode = Schema.RequiredMode.REQUIRED)
    private String label;

    @Schema(description = "这个比赛状态的数量", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long value;

    @Schema(description = "颜色", example = "#909399", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;
}