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
 * @TableName annunciation
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通告表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="annunciation")
@Data
public class Annunciation extends BaseEntity implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "标题", example = "这是一个标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容", example = "这是一个内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    /**
     * 发布日期
     */
    @Schema(description = "发布日期", example = "2023-08-15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadTime;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}