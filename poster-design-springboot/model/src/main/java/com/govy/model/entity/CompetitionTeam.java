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
 * @TableName competition_team
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报名表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="competition_team")
@Data
public class CompetitionTeam extends BaseEntity implements Serializable {
    /**
     * 竞赛id
     */
    private Integer competitionId;

    /**
     * 队伍id
     */
    private Integer teamId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}