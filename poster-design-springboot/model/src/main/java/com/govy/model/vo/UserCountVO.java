package com.govy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCountVO {

    @Schema(description = "普通用户数", example = "10")
    private Long userCount;

    @Schema(description = "管理员数", example = "10")
    private Long adminCount;
}
