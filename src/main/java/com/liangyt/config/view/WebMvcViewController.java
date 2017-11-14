package com.liangyt.config.view;

import com.liangyt.common.view.ControllerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 描述：
 *
 * @author tony
 * @创建时间 2017-08-17 14:12
 */
@SuppressWarnings("all")
@Configuration
public class WebMvcViewController extends WebMvcConfigurerAdapter {
    @Autowired
    private ControllerInterceptor controllerInterceptor;

    /**
     * 定义访问路径与视图之间的关系，不经过Controller
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/login");
        registry.addViewController("/error");

        super.addViewControllers(registry);
    }

    /**
     * 设置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截
        registry.addInterceptor(controllerInterceptor).addPathPatterns("/api/**");
        // 不拦截
        registry.addInterceptor(controllerInterceptor)
                .excludePathPatterns("/login")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/test/session/*");
        super.addInterceptors(registry);
    }
}
