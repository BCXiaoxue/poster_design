package com.govy.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName work
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="work")
@Data
public class WorksDTO implements Serializable {

    /**
     * 作品id
     */
    @Schema(description = "作品id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer workId;

    /**
     * 作品名称
     */
    @Schema(description = "作品名称", example = "这是一个作品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 简介
     */
    @Schema(description = "简介", example = "这是一个简介", requiredMode = Schema.RequiredMode.REQUIRED)
    private String introduction;

    /**
     * 图片
     */
    @Schema(description = "图片", example = "https://example.com/picture.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String picture;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}