package com.eredar.stepflow.starter;

import com.eredar.stepflow.StepExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Step Spring Boot Starter 自动装配入口。
 * <p>
 * 该配置类负责：
 * 1. 在类路径存在 Step 相关类时触发自动装配；
 * 2. 扫描 Step 核心包下的组件（执行器、处理器、表达式引擎等）；
 * 3. 启用 Step 配置属性绑定，让用户通过 application.yml 进行配置；
 * 4. 避免业务方手写 @ComponentScan 或 @Import。
 * <p>
 * 这样业务项目只需引入 Starter 依赖，即可完成 Step 能力的装配。
 */
@Configuration
@ConditionalOnClass(StepExecutor.class) // 仅在 Step 相关类存在时启用自动装配
@ComponentScan(basePackages = "com.eredar.step") // 扫描 Step 核心包，自动注册组件
//@EnableConfigurationProperties(StepConfig.class) // 启用 Step 配置属性绑定
public class StepAutoConfiguration {
    // 该类仅用于触发自动装配，不需要额外代码实现。
    // 保留此空类结构是为了让 Spring Boot 自动装配机制可识别入口。
    // 详细的 Bean 构建逻辑由 Step 核心模块的 @Component 注解负责。
}
