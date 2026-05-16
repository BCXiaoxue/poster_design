package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkUrlVO {
    @Schema(description = "作品地址", example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    @Schema(description = "作品id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
}
