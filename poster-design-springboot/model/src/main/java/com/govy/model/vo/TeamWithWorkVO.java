package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName work
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamWithWorkVO implements Serializable {

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 队伍名称
     */
    @Schema(description = "队伍名称", example = "这是一个队伍名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;

    /**
     * 是否已经评价
     */
    @Schema(description = "是否已经评价")
    private Boolean isEvaluated;


    /**
     * 作品列表
     */
    @Schema(description = "作品列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<WorkVO> works;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}