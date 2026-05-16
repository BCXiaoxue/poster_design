package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamUserWorkScoreVO {
    /**
     * 参赛用户头像
     */
    @Schema(description = "参赛用户头像")
    List<TeamUserAdminVO> userAvatars;

    /**
     * 作品
     */
    @Schema(description = "作品")
    List<WorkAdminVO> works;

    /**
     * 评委打分
     */
    @Schema(description = "评委打分")
    List<ScoreVO> scores;

    /**
     * 指导老师名
     */
    @Schema(description = "指导老师名")
    private String teacher;
}
