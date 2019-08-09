package cn.edu.swpu.jdata_exam.support;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 身份验证
 */
@Slf4j
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MyUserInfoService myUserInfoService;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("进入UserAuthenticationProvider的authenticate方法");

        //获取用户传来用户名和密码
        String userId = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("用户传来的用户名和密码为，userId={}，password={}",userId,password);

        //根据用户名获取数据库中的用户名及密码
        UserDetails userDetails = myUserInfoService.loadUserByUsername(userId);

        if (StringUtils.isEmpty(userDetails)){
            throw new JdataExamException(ExceptionEnum.GET_USERDATA_ERROR);
        }

        if(!password.equals(userDetails.getPassword())){
            log.info("密码错误，数据库中密码={}，用户输入密码={}",userDetails.getPassword(),password);
            throw new JdataExamException(ExceptionEnum.PASSWORD_ERROR);
        }



        Authentication auth = new UsernamePasswordAuthenticationToken(userId,password);

        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
