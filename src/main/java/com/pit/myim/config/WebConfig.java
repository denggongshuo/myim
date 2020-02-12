package com.pit.myim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: deng
 * @Date: 2019/9/24 19:18
 * @Version 1.0
 */
@Configuration
@RefreshScope
public class WebConfig implements WebMvcConfigurer {

    @Value("${root_file_path}")
    String root_file_path;  //  => E:/work
    @Value("${root_http_img_path}")
    String root_http_img_path; //  => /img/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(root_http_img_path+"**")
                .addResourceLocations("file:" + root_file_path+root_http_img_path);
    }
}
