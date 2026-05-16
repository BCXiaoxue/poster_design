package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName price
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Schema(description = "奖状表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="price")
@Data
public class Price extends BaseEntity implements Serializable {
    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 奖项
     */
    @Schema(description = "奖项", example = "金奖", requiredMode = Schema.RequiredMode.REQUIRED)
    private String award;

    /**
     * 奖状url
     */
    @Schema(description = "奖状url", example = "https://example.com/price.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String priceUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}