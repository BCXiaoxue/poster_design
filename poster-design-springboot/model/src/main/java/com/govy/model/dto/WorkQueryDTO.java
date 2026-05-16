package com.govy.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkQueryDTO implements Serializable {

    @Schema(description = "作品名", example = "作品A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String workName;

    @Schema(description = "作品状态 0-未评审 1-已评审", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer workStatus;

    /**
     * 页码
     */
    @Schema(description = "页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;
}
