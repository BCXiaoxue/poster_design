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
 * 
 * @TableName score
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScoreVO implements Serializable {

    @Schema(description = "评委名字", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String judgeName;

    @Schema(description = "评委头像", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String judgeAvatar;

    /**
     * 评分
     */
    @Schema(description = "评分", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer score;

    @Schema(description = "评价", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}