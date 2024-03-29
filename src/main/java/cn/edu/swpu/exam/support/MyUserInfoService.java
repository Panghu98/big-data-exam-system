package cn.edu.swpu.exam.support;

import cn.edu.swpu.exam.dao.UserBasicInfoDAO;
import cn.edu.swpu.exam.entity.UserBasicInfo;
import cn.edu.swpu.exam.enums.ExceptionEnum;
import cn.edu.swpu.exam.exception.JdataExamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author panghu
 */
@Slf4j
@Component
public class MyUserInfoService implements UserDetailsService {

    @Autowired
    private UserBasicInfoDAO userBasicInfoDAO;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("进入MyUserDetailService的loadUserByUsername方法");

        //根据用户名查找用户信息
        UserBasicInfo userBasicInfo = userBasicInfoDAO.getUserById(userId);

        if(StringUtils.isEmpty(userBasicInfo)){
            log.info("用户名不存在，userId={}",userId);
            throw new JdataExamException(ExceptionEnum.USER_NOT_EXIT);
        }

        log.info("登录用户名：userId={}",userId);

        //根据查找到的用户信息判断用户是否被冻结
        //.....

        String password = userBasicInfo.getUserPassword();

        return new User(userId,password,AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }

}
