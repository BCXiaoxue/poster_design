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
import java.util.List;

/**
 * 
 * @TableName competition
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionDTO extends BaseEntity implements Serializable {

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
     * 竞赛介绍
     */
    @Schema(description = "竞赛介绍", example = "这是一个机器学习竞赛", requiredMode = Schema.RequiredMode.REQUIRED)
    private String introduction;

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
     * 活动结束
     */
    @Schema(description = "活动结束", example = "2023-09-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date activityEnd;

    /**
     * 人数限定
     */
    @Schema(description = "人数限定", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer people;

    /**
     * 报名须知
     */
    @Schema(description = "报名须知", example = "请务必阅读并了解竞赛规则", requiredMode = Schema.RequiredMode.REQUIRED)
    private String notes;

    /**
     * 竞赛赛程
     */
    @Schema(description = "竞赛赛程", example = "2023-09-01 9:00-10:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String schedule;

    /**
     * 竞赛图片
     */
    @Schema(description = "竞赛图片", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> images;

}