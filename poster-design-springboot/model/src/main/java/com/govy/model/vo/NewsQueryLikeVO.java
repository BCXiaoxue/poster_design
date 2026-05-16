package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName news
 */
@Schema(description = "新闻表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="news")
@Data
@Builder
public class NewsQueryLikeVO implements Serializable {

    private Integer id;
    /**
     * 标题
     */
    @Schema(description = "标题", example = "这是一个标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    /**
     * 发布日期
     */
    @Schema(description = "发布日期", example = "2023-08-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}