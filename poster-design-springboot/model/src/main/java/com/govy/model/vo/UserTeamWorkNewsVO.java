package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamWorkNewsVO {
    @Schema(description = "用户数", example = "10")
    private Long userCount;
    @Schema(description = "队伍数", example = "10")
    private Long teamCount;
    @Schema(description = "作品数", example = "10")
    private Long workCount;
    @Schema(description = "新闻数", example = "10")
    private Long newsCount;
    @Schema(description = "公告数", example = "10")
    private Long AnnunciationCount;
    @Schema(description = "正在进行的比赛数", example = "10")
    private Long competitionCount;
}
