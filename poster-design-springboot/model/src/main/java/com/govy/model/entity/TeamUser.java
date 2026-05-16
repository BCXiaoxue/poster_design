package com.govy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName team_user
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "竞赛类别表") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="team_user")
@Data
public class TeamUser extends BaseEntity implements Serializable {

    /**
     * 队伍id
     */
    private Integer teamId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户状态（0队长、1队员）
     */
    private Integer status;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}