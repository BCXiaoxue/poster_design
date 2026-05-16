package com.govy.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data // Lombok注解：自动生成getter/setter/toString等方法
@AllArgsConstructor
@NoArgsConstructor
public class UserAllVO  {

    @Schema(description = "参与的比赛数", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer participateCompetitionNum;

    @Schema(description = "获奖次数", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer winNum;

    @Schema(description = "作品数量", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer workNum;

    @Schema(description = "参与团队数量", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer teamNum;

}