package top.qxfly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class VirtualPathConfig implements WebMvcConfigurer {
    @Value("${VirtualPath}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
          资源映射路径
          addResourceHandler：访问映射路径
          addResourceLocations：资源绝对路径
         */
        registry.addResourceHandler("/download/**")
                .addResourceLocations(filePath);
    }
}