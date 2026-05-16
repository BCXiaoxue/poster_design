package com.govy.model.dto;

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
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛表-分页查询数据模型") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionMyQueryLikeDTO extends BaseEntity implements Serializable {

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 竞赛状态
     */
    @Schema(description = "竞赛状态", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionStatus;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;
}