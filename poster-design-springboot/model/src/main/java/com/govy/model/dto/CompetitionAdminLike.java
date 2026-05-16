package com.govy.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName competition
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionAdminLike  implements Serializable {


    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 竞赛状态
     */
    @Schema(description = "竞赛状态status未开始-0, 招募中-1，进行中-2，已结束-3", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionStatus;

    /**
     * 竞赛分类id
     */
    @Schema(description = "竞赛分类id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer categoryId;
}