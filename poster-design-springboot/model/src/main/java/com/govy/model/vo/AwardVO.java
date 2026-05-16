package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 颁奖表
 * @TableName award
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AwardVO implements Serializable {

    @Schema(description = "颁奖id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    /**
     * 队伍id（颁奖与队伍相关联）
     */
    @Schema(description = "队伍id（颁奖与队伍相关联）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 队伍名
     */
    @Schema(description = "队伍名", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;


    /**
     * 几等奖
     */
    @Schema(description = "几等奖", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer awardRank;

    /**
     * 这个团队的作品url列表
     */
    @Schema(description = "这个团队的作品url列表", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> worksUrl;
}