package com.govy.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "点赞信息") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PraiseLikedDTO {
    @Schema(description = "当前用户是否点赞 0-未点赞 1-已点赞", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer liked;

    @Schema(description = "当前作品点赞数量", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long likeCount;

    @Schema(description = "当前作品id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer workId;
}
