package cn.edu.swpu.jdata_exam.filter;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * token的校验
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    //私钥
    private String key = "spring-security-@Jwt!&Secret^#";

    //token的开头
    private String tokenHead = "Bearer ";

    //token头部
    private String tokenHeader = "Authorization";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {


        /*
         * 跨域问题的设置
         */
        String option = "OPTIONS";
        if (option.equals(request.getMethod())) {
            log.info("浏览器的预请求的处理..");
            response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE,HEAD");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Accept, token,Origin, No-Cache, X-Requested-With,verify-code, If-Modified-Since,authorization,Pragma, Last-Modified, Cache-Control, Expires, Authorization,Token");
            log.info("成功处理");
            return;
        } else {
            String requestURI = request.getRequestURI();
            log.error("requestURI:{}", requestURI);
        }

        log.info("进入JWTAuthenticationFilter的doFilterInternal方法。校验token");

        //取得token内容
        String token = request.getHeader(tokenHeader);

        log.info("从头部获取的token={}",token);

        if(StringUtils.isEmpty(token)||!token.startsWith(tokenHead)){
            chain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //使用两次会返回来两个结果
        chain.doFilter(request,response);


    }

    public UsernamePasswordAuthenticationToken getAuthentication(String responseToken){

        log.info("进入JWTAuthenticationFilter的getAuthentication校验token");

        Claims claims;
        String userId = null;

        String token = responseToken.split("\\s+")[1];

        log.info("检验中分离出来的token={}",token);

        if (!StringUtils.isEmpty(token)){

            try {
                //解析token
                claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token)
                        .getBody();

                userId =  (String )claims.get("name");
                log.info("解析的claims内容{}",userId);


            }catch (Exception e){
                userId = ExceptionEnum.REPEAT_LOGIN.getMessage();
            }

        }else{
            userId = ExceptionEnum.PLEASE_LOGIN_FIRST.getMessage();
        }

        return new UsernamePasswordAuthenticationToken(userId,null,new ArrayList<>());


    }




}
