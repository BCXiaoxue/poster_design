package com.govy.model.vo;

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
public class CompetitionMyVO extends BaseEntity implements Serializable {

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;

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
     * 竞赛状态status未开始-0, 招募中-1，进行中-2，已结束-3
     */
    @Schema(description = "竞赛状态status未开始-0, 招募中-1，进行中-2，已结束-3", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionStatus;

    /**
     * 参与角色
     */
    @Schema(description = "参与角色（管理员3、评委2、队长1、队员0）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer role;
}