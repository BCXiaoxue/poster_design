package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName work
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkVO implements Serializable {

    /**
     * 作品id
     */
    @Schema(description = "作品id")
    private Integer id;

    /**
     * 作品名称
     */
    @Schema(description = "作品名称", example = "这是一个作品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;


    /**
     * 简介
     */
    @Schema(description = "简介", example = "这是一个简介", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String introduction;

    /**
     * 图片
     */
    @Schema(description = "图片", example = "https://example.com/picture.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String picture;

    /**
     * 团队名
     */
    @Schema(description = "团队名")
    private String teamName;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}