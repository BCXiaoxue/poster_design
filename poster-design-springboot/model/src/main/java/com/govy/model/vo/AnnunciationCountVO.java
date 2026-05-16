package com.govy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName annunciation
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnunciationCountVO implements Serializable {

    @Schema(description = "已发布大赛通知数", example = "10")
    private Long publishedCount;

    @Schema(description = "未发布大赛通知数", example = "10")
    private Long unpublishedCount;
}