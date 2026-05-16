package com.govy.common.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateGeneratorProperties {

    /**
     * 大学名称
     */
    private String university;

    /**
     * 团队名
     */
    private String teamName;

    /**
     * 团队成员
     */
    private String[] members;

    /**
     * 竞赛名
     */
    private String competitionName;

    /**
     * 获奖级别
     */
    private String awardLevel;
}
