package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.dao.UserScoreDAO;
import cn.edu.swpu.jdata_exam.entity.UserScore;
import cn.edu.swpu.jdata_exam.entity.UserScore2;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserScoreServiceImpl implements UserScoreService {


    private static String PREFIX = "score-hash:";


    @Autowired
    private UserScoreDAO userScoreDAO;
    /**
     * 必须指定键值的类型,否则获取不到值
     */
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 获取成绩：
     *      如果该id缓存存在，则从缓存中获取信息
     *      如果缓存中不存在，则从数据库中获取，然后存入缓存
     *      通过Hash来存储和更新个人成绩,获取排行榜的时候就直接从redis里面直接获取
     */
    @Override
    public ResultVo getScoreById() {

        log.info("进入UserScoreServiceImpl的getScoreById");
        String userId = AuthenticationUtil.getAuthUserId();
        UserScore2 score = userScoreDAO.getScoreById(userId);
        redisTemplate.opsForHash().put(PREFIX,userId,score);
        return ResultVoUtil.success(score);
    }

    /**
     * 从redis里面获取数据
     * @return
     */
    @Override
    public ResultVo getRankingList() {
        Set<Object> userIdSet = redisTemplate.opsForHash().keys(PREFIX);
        List list = redisTemplate.opsForHash().multiGet(PREFIX,userIdSet);
        if(list.size()==0){
            List<UserScore2> scoreList = userScoreDAO.getRankingList();
            Map<String,UserScore2> map = scoreList.stream().collect(
                    Collectors.toMap(UserScore2::getUserId,(p)->p)
            );
            redisTemplate.opsForHash().putAll(PREFIX,map);
            return ResultVoUtil.success(scoreList);
        }
        return ResultVoUtil.success(list);
    }

    /**
     *     检验用户token中的userId与查询的userId是否一致。
     */
    public boolean checkId(String userId){
        String authUserId = AuthenticationUtil.getAuthUserId();

        log.info("AuthUserId={}",authUserId);
        log.info("userId={}",userId);

        //不一致
        return userId.equals(authUserId);
    }


}
