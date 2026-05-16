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
public class MessageQueryLikeDTO implements Serializable {
    /**
     * 发件方id
     */
    @Schema(description = "发件方id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sendId;

    /**
     * 发件方角色
     */
    @Schema(description = "邀请类消息-0, 通知类消息-1", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer category;

    /**
     * 接受方id
     */
    @Schema(description = "接受方id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer receiveId;

    /**
     * 消息发送状态（0待处理、1已发送、2已接收、3已撤回, 4已拒绝, 5未读， 6已读）
     */
    @Schema(description = "消息发送状态（0待处理、1已发送、2已接收、3已撤回，4已拒绝，5未读， 6已读）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;
}