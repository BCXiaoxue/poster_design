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
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛表-分页查询数据模型") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionQueryLikeDTO extends BaseEntity implements Serializable {

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 竞赛分类
     */
    @Schema(description = "竞赛分类", example = "机器学习", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryId;

    /**
     * 竞赛分类名
     */
    @Schema(description = "竞赛分类名")
    private String categoryName;

    /**
     * 报名时间
     */
    @Schema(description = "报名时间", example = "2023-08-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationBegin;

    /**
     * 截止时间
     */
    @Schema(description = "截止时间", example = "2023-08-31", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationEnd;

    /**
     * 人数限定
     */
    @Schema(description = "人数限定", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer people;

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