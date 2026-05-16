package com.govy.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.govy.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName message
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDTO extends BaseEntity implements Serializable {
    /**
     * 发件方角色
     */
    @Schema(description = "发送方角色（存储身份信息：管理员3、评委2、队长1、队员0等）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sendRole;

    /**
     * 接受方id
     */
    @Schema(description = "接受方id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer receiveId;

    /**
     * 接收方角色
     */
    @Schema(description = "接收方角色（存储身份信息：管理员3、评委2、队长1、队员0等）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer receiveRole;


    /**
     * 内容
     */
    @Schema(description = "内容", example = "这是一个内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    /**
     * 消息类型
     */
    @Schema(description = "邀请类消息-0, 通知类消息-1", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer category;



    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;
}