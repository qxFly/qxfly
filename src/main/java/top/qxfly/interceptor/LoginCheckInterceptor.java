package top.qxfly.interceptor;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.LogoutService;
import top.qxfly.utils.JwtUtils;

@CrossOrigin
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    LogoutService logoutService;

    //目标资源方法运行前运行，返回true:放行，返回false:不放行
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(req.getMethod())) {
            log.info("OPTIONS请求，放行");
            return true;
        }

        //获取请求的url
        String url = req.getRequestURI();
        log.info("请求的url:{}", url);
//        //判断是否为 login 或 listfile,如果是放行
//        String[] urlList = {"login", "logout", "register", "listFile", "updateLoginStatue",
//                "download", "fileList", "listWord", "listSite", "getImage", "swagger", "doc",
//                "userAvatar", "getArticles", "getArticleById","articleCover","articleImage","getUserInfo"};
//        for (String s : urlList) {
//            if (url.contains(s)) {
//                log.info("该url无需验证：{}", url);
//                return true;
//            }
//        }

        //获取证书token
        String token = req.getHeader("token");
        //判断证书是否存在，如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(token)) {
            log.info("请求头token为空，返回未登入信息");
            Result error = Result.error("NOT_LOGIN");
            //转换为json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);
            return false;
        }

        //解析token，解析失败，返回错误结果（未登录）
        try {
            JwtUtils.parseJWT(token);
            /*查询用户是否退出，退出则返回错误结果*/
            String username = logoutService.getLogoutStatus(token);
            if (username != null) return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析证书失败，未登录");
            Result error = Result.error("NOT_LOGIN");
            //转换为json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);
            return false;
        }

        //放行
        log.info("证书合法，放行");
        return true;
    }


    @Override //目标资源运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override //试图渲染完毕后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
