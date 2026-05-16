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

/**
 * Govy
 * @TableName judge
 */

@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="judge")
@Data
public class JudgeVO implements Serializable{
    /**
     * 评委id
     */
    @Schema(description = "评委id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer judgeId;

    /**
     * 评委头像
     */
    @Schema(description = "评委头像", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String judgeAvatarUrl;

    /**
     * 评委名
     */
    @Schema(description = "评委名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String judgeName;

    /**
     * 评委大学
     */
    @Schema(description = "大学", example = "上海交通大学", requiredMode = Schema.RequiredMode.REQUIRED)
    private String universityName;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}