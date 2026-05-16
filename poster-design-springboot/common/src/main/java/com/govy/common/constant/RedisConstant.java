package com.govy.common.constant;

/**
 * 定义不同配置和数据的常量类
 */
public class RedisConstant {
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;
    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 5;
    public static final String APP_ROOM_PREFIX = "app:room:";
    public static final String PHONE_CODE = "code:";
    public static final String NEWS_PREFIX = "news:";
    public static final String ANNUNCIATION_PREFIX = "annunciation:";
    public static final String COMPETITION_PREFIX = "competition:";
    public static final String WORK_LIKE_PREFIX = "work:liked:";
    public static final String AWARD_COMPETITION_PREFIX = "award:competition:";
    public static final String AWARD_WIN_WITH_COMPETITION = "award:work:top5";
    public static final String JUDGE_COMPETITION_PREFIX = "judge:competition:";
    public static final String WORK_PREFIX = "work:work:";
    public static final Long REDIS_TIME = 3600 * 24L;
    public static final Long EMPTY_TIME = 10L;
}