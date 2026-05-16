package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkAndCommentVO {
    @Schema(description = "作品列表", requiredMode = Schema.RequiredMode.REQUIRED)
    List<WorkAdminVO> workList;

    @Schema(description = "评论列表", requiredMode = Schema.RequiredMode.REQUIRED)
    List<ScoreVO> commentList;
}
