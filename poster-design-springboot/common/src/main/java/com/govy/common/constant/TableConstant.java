package com.govy.common.constant;

public class TableConstant {
    public static final Integer COMPETITION = 0;
    public static final Integer ANNUNCIATION = 1;
    public static final Integer NEWS = 2;
    public static final Integer CAPTAIN = 0;
    public static final Integer MEMBER = 1;
    public static final Integer COMMON_USER = 0;
    public static final Integer ADMIN = 1;

    // 参与角色（管理员3、评委2、队长1、队员0）
    public static final Integer ADMIN_ROLE = 3;
    public static final Integer JUDGE_ROLE = 2;
    public static final Integer CAPTAIN_ROLE = 1;
    public static final Integer MEMBER_ROLE = 0;

    // 消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）
    public static final Integer MESSAGE_WAIT = 0;
    public static final Integer MESSAGE_RECEIVED = 1;
    public static final Integer MESSAGE_REJECTED = 2;
    public static final Integer MESSAGE_WITHDRAWN = 3;

    // 团队状态（0未锁定， 1已锁定）
    public static final Integer TEAM_UNLOCKED = 0;
    public static final Integer TEAM_LOCKED = 1;

    // 状态（0未颁奖、1已颁奖）
    public static final Integer UNAWARDS = 0;
    public static final Integer AWARDS = 1;

    // 一等奖，二等奖，三等奖
    public static final Integer FIRST = 1;
    public static final Integer SECOND = 2;
    public static final Integer THIRD = 3;

    // 用户状态: 0-正常 1-禁用
    public static final Integer USER_STATUS_NORMAL = 0;
    public static final Integer USER_STATUS_DISABLED = 1;
}
