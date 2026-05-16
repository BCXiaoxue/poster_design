package com.govy.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    PARAM_ERROR(202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    DELETE_ERROR(207, "请先删除子集"),

    REGEXP_ERROR(400, "匹配错误"),
    TOKEN_NOT_EXIST(401, "token不存在"),
    NOT_FOUND(404, "未找到"),
    USER_ACCOUNT_EXIST(409, "账号已存在"),
    USER_PHONE_EXIST(410, "手机号已存在"),
    USER_EMAIL_EXIST(411, "邮箱已存在"),
    CODE_NOT_EXIST(412, "验证码不存在"),
    USER_NOT_EXIST(413, "用户不存在"),
    TOKEN_ERROR(501, "token错误"),
    TEAM_NAME_ERROR(500, "队名名重复"),
    ALREADY_EXISTS(501, "已存在"),
    USER_NOT_CAPTAIN(502, "用户不是队长"),
    COMPETITION_TEAM_OVER_LIMIT(503, "团队人员超出竞赛人员限制"),
    COMPETITION_TEAM_ALREADY_EXISTS(504, "团队已存在"),
    TEAM_LOCKED(505, "团队正在参加比赛，已锁定"),
    USER_EXIST_TEAM(506, "用户已加入团队, 不能重复加入团队"),
    TEAM_NOT_EXIST(507, "团队不存在或已解散"),
    USER_STATUS_DISABLED(508, "账号被禁用，请联系管理员"),
    COMPETITION_TEAM_JUDGE_CONFLICT(509, "团队已存在评委，该团队无法报名这个比赛"),


    TOKEN_EXPIRED(601, "token过期"),
    TOKEN_INVALID(602, "token非法"),
    CODE_ERROR(603, "验证码错误"),
    PASSWORD_ERROR(604, "密码错误"),
    PHONE_ERROR(605, "手机号错误"),
    CODE_RESULT(606, "由于国家政策，个人用户无法调用发送短信的接口，我将Redis中的验证码固定为'123456'");



    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
