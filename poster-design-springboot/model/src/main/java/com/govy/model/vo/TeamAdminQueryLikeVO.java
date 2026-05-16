package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName team
 */
@Schema(description = "队伍表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamAdminQueryLikeVO implements Serializable {

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    /**
     * 队名
     */
    @Schema(description = "队名", example = "上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-05-01 00:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 指导老师
     */
    @Schema(description = "指导老师", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teacherName;

    /**
     * 团队人数
     */
    @Schema(description = "团队人数", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamNum;

    @Schema(description = "队长名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    private String leaderName;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}