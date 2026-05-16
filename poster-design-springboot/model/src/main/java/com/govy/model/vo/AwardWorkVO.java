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
public class AwardWorkVO implements Serializable {

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 竞赛名
     */
    @Schema(description = "竞赛名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 这个团队的作品url列表
     */
    @Schema(description = "这个团队的作品url列表", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> worksUrl;
}