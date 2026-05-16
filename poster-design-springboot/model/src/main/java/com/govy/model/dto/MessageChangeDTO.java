package com.govy.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName message
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageChangeDTO implements Serializable {
    /**
     * 消息id
     */
    @Schema(description = "消息id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    /**
     * 消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）
     */
    @Schema(description = "消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）
     */
    @Schema(description = "消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer newStatus;

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;
}