package com.govy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamWorkVO {
    @Schema(description = "时间", example = "2021-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "MM-dd")
    private Date time;

    @Schema(description = "新增用户数量", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long user;

    @Schema(description = "新增团队数量", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long team;

    @Schema(description = "新增作品数量", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long work;
}
