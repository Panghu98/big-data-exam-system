package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.dao.UserScoreDAO;
import cn.edu.swpu.jdata_exam.entity.UserScore;
import cn.edu.swpu.jdata_exam.entity.UserScore2;
import cn.edu.swpu.jdata_exam.entity.dto.ResponseUserScore;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserScoreServiceImpl implements UserScoreService {

    @Autowired
    private UserScoreDAO userScoreDAO;

    //成绩缓存时间 30分钟
    private long expirationTime = 30*60;


    /**
     * 获取成绩：
     *      如果该id缓存存在，则从缓存中获取信息
     *      如果缓存中不存在，则从数据库中获取，然后存入缓存
     */
    @Override
    public ResultVo getScoreById() {

        log.info("进入UserScoreServiceImpl的getScoreById");

        String userId = AuthenticationUtil.getAuthUserId();
        return ResultVoUtil.success(userScoreDAO.getScoreById(userId));
    }

    @Override
    public ResultVo getRankingList() {
        return ResultVoUtil.success(userScoreDAO.getRankingList(userScoreDAO.getUserList()));
    }

    //检验用户token中的userId与查询的userId是否一致。
    public boolean checkId(String userId){
        String authUserId = AuthenticationUtil.getAuthUserId();

        log.info("AuthUserId={}",authUserId);
        log.info("userId={}",userId);

        //不一致
        if (!userId.equals(authUserId)){
            return false;
        }

        return true;
    }


}
