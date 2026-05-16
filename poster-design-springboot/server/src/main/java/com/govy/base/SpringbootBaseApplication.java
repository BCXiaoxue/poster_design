package com.govy.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主应用程序启动类，用于引导和启动Spring Boot应用。
 * 包含了多个注解来启用特定的功能：
 * - {@link EnableScheduling}：启用Spring框架的任务调度功能，支持基于注解的方式进行任务调度。
 * - {@link EnableAsync}：启用异步方法执行的能力，通过@Async注解的方法将异步执行。
 * - {@link EnableTransactionManagement}：启用Spring的事务管理功能，通常与@Transactional注解一起使用来管理事务。
 * - {@link SpringBootApplication}：这是一个组合注解，包括@SpringBootConfiguration, @EnableAutoConfiguration, 和 @ComponentScan，
 *   使得Spring Boot应用能够自动配置自身，并扫描指定包下的组件以供注入使用。
 * - {@link ComponentScan}：指定了基础包路径，Spring将会在这个包及其子包下查找@Component, @Service, @Repository等注解标注的类，
 *   并将其注册为Spring容器中的Bean。
 */
@EnableScheduling // 启用定时任务功能
@EnableAsync // 启用异步方法执行
@EnableTransactionManagement // 启用事务管理
@MapperScan("com.govy.base.mapper")
@ComponentScan(basePackages = {"com.govy.base", "com.govy.common"}) // 设置Spring的组件扫描路径
@SpringBootApplication // 标记该类为Spring Boot应用的主入口点，并激活默认配置
public class SpringbootBaseApplication {
    /**
     * 应用程序的入口方法。
     * 使用SpringApplication.run()方法来启动Spring Boot应用程序，此方法会创建应用上下文并运行它。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringbootBaseApplication.class, args);
    }
}