package cn.edu.swpu.jdata_exam.task;

import cn.edu.swpu.jdata_exam.dao.UserBasicInfoDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author panghu
 * @title: TestTask
 * @projectName jdata_exam
 * @date 19-6-23 下午7:14
 */
@Slf4j
//@Component
public class ScheduleTask {

    private final static String prefixKey = "username : ";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserBasicInfoDAO userBasicInfoDAO;

    /**
     * 默认单位是毫秒
     * 每天的开始将用户的信息添加到缓存当中
     */
    @Scheduled(cron = "0 0 0 23/1 * ? ")
    public void addUsernameToCache(){
        log.info("-------------将所有的用户添加到cache当中----------------");
        List<Long> users = userBasicInfoDAO.getUserList();
        for (Long userId:users
             ) {
            redisTemplate.opsForValue().set(prefixKey + userId,"登录信息有效");
        }
     }


    /**
     * 每天晚上将用户的登录信息清除
     */
    @Scheduled(cron = "0 59 23 23/1 * ? ")
    public void removeUsernameFromCache(){
        log.info("----------------将所有的用户从cache当中删除-----------------------");
        List<Long> users = userBasicInfoDAO.getUserList();
        for (Long userId:users
             ) {
            redisTemplate.delete(prefixKey+userId);
        }
    }
}