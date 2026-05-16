package com.govy.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName competition
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompetitionByTeamIdVO implements Serializable {

    @Schema(description = "竞赛id")
    private Integer competitionId;

    /**
     * 竞赛名称
     */
    @Schema(description = "竞赛名称", example = "2023年上海交通大学 Hackathon", requiredMode = Schema.RequiredMode.REQUIRED)
    private String competitionName;
}