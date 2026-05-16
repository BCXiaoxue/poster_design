package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName message
 */
@EqualsAndHashCode(callSuper = true) // Lombok注解：生成equals和hashCode方法，包含父类字段
@Schema(description = "信息表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="message")
@Data
public class Message extends BaseEntity implements Serializable {
    /**
     * 发件方id
     */
    @Schema(description = "发件方id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sendId;

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
     * 时间
     */
    @Schema(description = "时间", example = "2023-08-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    /**
     * 竞赛id
     */
    @Schema(description = "竞赛id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer compensationId;

    /**
     * 队伍id
     */
    @Schema(description = "队伍id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer teamId;

    /**
     * 消息类型
     */
    @Schema(description = "邀请类消息-0, 通知类消息-1", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer category;

    /**
     * 消息发送状态（0未发送、1已发送、2已接收、3已撤回）
     */
    @Schema(description = "消息发送状态（0未发送、1已发送、2已接收、3已撤回, 4待处理）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}