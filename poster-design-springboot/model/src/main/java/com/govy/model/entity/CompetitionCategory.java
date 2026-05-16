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
 * @TableName competition_category
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛类别表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="competition_category")
@Data
public class CompetitionCategory extends BaseEntity implements Serializable {

    /**
     * 竞赛分类名称id
     */
    @Schema(description = "竞赛分类名称id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}