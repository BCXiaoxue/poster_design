package com.govy.model.dto;

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
import java.util.List;

/**
 * 
 * @TableName work
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkEnlistsDTO implements Serializable {
    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer competitionId;

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 作品集
     */
    @Schema(
            description = "作品集",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "array",
            example = "[{\"workId\":1,\"name\":\"作品A\",\"introduction\":\"简介示例\",\"picture\":\"https://example.com/img.jpg\"}]"
    )
    private List<WorksDTO> works;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}