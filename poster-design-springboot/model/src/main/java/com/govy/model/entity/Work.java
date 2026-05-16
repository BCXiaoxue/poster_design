package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 
 * @TableName work
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Schema(description = "作品表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="work")
@Data
@Builder
public class Work extends BaseEntity implements Serializable {

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 作品名称
     */
    @Schema(description = "作品名称", example = "这是一个作品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 提交日期
     */
    @Schema(description = "提交日期", example = "2023-08-15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date submissionDate;

    /**
     * 简介
     */
    @Schema(description = "简介", example = "这是一个简介", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String introduction;

    /**
     * 分享次数
     */
    @Schema(description = "分享次数", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer share;

    /**
     * 图片
     */
    @Schema(description = "图片", example = "https://example.com/picture.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String picture;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}