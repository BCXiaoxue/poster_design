package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
@Data
public class WorkAdminVO implements Serializable {

    /**
     * 作品id
     */
    @Schema(description = "作品id")
    private Integer workId;

    /**
     * 作品名称
     */
    @Schema(description = "作品名称", example = "这是一个作品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 图片
     */
    @Schema(description = "图片", example = "https://example.com/picture.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String workUrl;

    /**
     * 作品描述
     */
    @Schema(description = "作品描述", example = "这是一个作品描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}