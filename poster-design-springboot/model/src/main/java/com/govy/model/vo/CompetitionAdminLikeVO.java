package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
import java.util.List;

/**
 * 
 * @TableName competition
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionAdminLikeVO implements Serializable {

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id") // Swagger接口文档描述
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 竞赛分类id
     */
    @Schema(description = "竞赛分类id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryId;

    /**
     * 竞赛分类id
     */
    @Schema(description = "竞赛分类名", example = "竞赛类海报", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryName;

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
     * 竞赛图片
     */
    @Schema(description = "竞赛图片", example = "https://example.com/competition.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    List<String> competitionImages;

    /**
     * 参赛人数
     */
    @Schema(description = "参赛人数")
    private Integer competitionPeople;

    /**
     * 参赛用户头像
     */
    @Schema(description = "参赛用户头像")
    List<String> userAvatars;

    /**
     * 竞赛状态status未开始-0, 招募中-1，进行中-2，已结束-3
     */
    @Schema(description = "竞赛状态status未开始-0, 招募中-1，进行中-2，已结束-3", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionStatus;


}