package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础实体类
 * 所有实体类的基类，包含公共字段和通用属性
 * 实现Serializable接口支持序列化传输
 */
@Data // Lombok注解自动生成getter/setter/toString等方法
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     * 使用MyBatis Plus的@TableId注解标识主键
     * value = "id" 对应数据库字段名
     * IdType.AUTO 表示自增主键
     */
    @Id
    @Schema(description = "主键") // Swagger接口文档描述
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     * 自动填充策略：插入时自动填充
     * @JsonFormat 日期格式化为"yyyy-MM-dd HH:mm:ss"，时区UTC+8
     * @JsonIgnore 防止序列化到JSON
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT) // 插入时自动填充
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 自动填充策略：更新时自动填充
     * @JsonFormat 日期格式化为"yyyy-MM-dd HH:mm:ss"，时区UTC+8
     * @JsonIgnore 防止序列化到JSON
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 更新时自动填充
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

//    /**
//     * 逻辑删除标识
//     * 使用@TableLogic注解实现逻辑删除功能
//     * 数据库字段名为"is_deleted"
//     * 1表示已删除，0表示未删除
//     */
//    @Schema(description = "逻辑删除")
//    @TableField("is_deleted")
//    @TableLogic
//    @JsonIgnore
//    private Byte isDeleted;
}
