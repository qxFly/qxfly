package top.qxfly.filter;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import top.qxfly.pojo.Result;
import top.qxfly.utils.JwtUtils;

import java.io.IOException;

@Slf4j
/*@WebFilter(urlPatterns = "/*")*/
public class LoginCheckFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //获取请求的url
        String url = req.getRequestURI();
        log.info("请求的url:{}", url);
        //判断是否为login,如果是放行
        if (url.contains("login")) {
            log.info("登入操作，放行...");
            chain.doFilter(request, response);
            return;
        }

        //获取令牌token
        String jwt = req.getHeader("token");

        //判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，返回为登入信息");
            Result error = Result.error("NOT_LOGIN");
            //转换为json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);
            return;
        }

        //解析token，解析失败，返回错误结果（未登录）
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
            //转换为json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);
            return;
        }

        //放行
        log.info("令牌合法，放行");
        chain.doFilter(request, response);
    }
}
