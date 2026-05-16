package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName competition
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionCategoryDataVO implements Serializable {

    @Schema(description = "比赛类型", example = "海报设计 - 平面设计 等等", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @Schema(description = "这个比赛类型的数量", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long count;

    @Schema(description = "颜色", example = "#909399", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;
}