package fun.qxfly.config;

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
        /*资源下载*/
        registry.addResourceHandler("/download/**")
                .addResourceLocations(filePath);
        /*用户头像*/
        registry.addResourceHandler("/userAvatar/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/data/qxfly-userAvatar/");
        /*文章封面*/
        registry.addResourceHandler("/articleCover/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/data/qxfly-articleCover/");
        /*文章内容图片*/
        registry.addResourceHandler("/articleImage/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/data/qxfly-articleImage/");
    }
}