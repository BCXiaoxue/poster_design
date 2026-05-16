package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName competition
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="competition")
@Data
public class Competition extends BaseEntity implements Serializable {

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

    /**
     * 竞赛分类
     */
    @Schema(description = "竞赛分类", example = "机器学习", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer categoryId;

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
     * 状态
     */
    @Schema(description = "状态（0未颁奖、1已颁奖）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer awardsStatus;
}