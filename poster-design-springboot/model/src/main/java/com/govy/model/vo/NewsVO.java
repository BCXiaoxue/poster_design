package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @TableName news
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Schema(description = "新闻表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsVO extends BaseEntity implements Serializable {

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
    @Schema(description = "发布日期", example = "2023-08-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadTime;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    @Schema(description = "图片", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> images;
}