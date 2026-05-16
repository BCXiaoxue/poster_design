package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 
 * @TableName image
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Image extends BaseEntity implements Serializable {
    /**
     * 图片地址
     */
    @Schema(description = "图片地址")
    private String url;

    /**
     * 该图片作用于哪张表（0表示竞赛表、1表示通知表、2表示新闻表）
     */
    @Schema(description = "该图片作用于哪张表（0表示竞赛表、1表示通知表、2表示新闻表）")
    private Integer category;

    /**
     * 该图片具体作用于哪条记录
     */
    @Schema(description = "该图片具体作用于哪条记录")
    private Integer imageId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}